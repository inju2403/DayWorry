package com.inju.dayworry.worry.worryDetail

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.BuildConfig
import com.inju.dayworry.R
import com.inju.dayworry.utils.Constants
import com.inju.dayworry.worry.worryDetail.buildlogic.WorryDetailInjector
import kotlinx.android.synthetic.main.activity_add_worry.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import com.inju.dayworry.utils.Constants.TAG

class AddWorryActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var search: Button? = null
    var fileUri: Uri? = null
    var path: String = ""
    var myBitmap: Bitmap? = null

    var mCurrentPhotoPath: String? = null

    private val RESULT_OK = 101
    val SELECT_GALLERY_PHOTO = 20
    val REQUEST_TAKE_PHOTO = 1
    val REQUEST_TAKE_PHOTO_ALBUM = 2
    private var worryDetailViewModel: WorryDetailViewModel? = null

    private var hashTag: String? = "empty"
    private var userId: Long = -1
    private var defaultId: Long = -1

    var litePupleColor = "#9689FC" // 텍스트 색상
    var superLiteGreyColor = "#cbcdd5" // 텍스트 색상

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_worry)

        addWorryLoadingUi.visibility = View.GONE
        setStatusBarColor("dark")

        val pref = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE)
        userId = pref.getLong("userId", defaultId)

        job = Job()

        setSupportActionBar(addWorryActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_toolbar_cancel)
        selectImage.visibility = View.GONE
        photoClearImage.visibility = View.GONE

        setViewModel()
        setUpClickListener()
        observeViewModel()
        setTextChangeListener()
        setTagBtn()

        val worryId = intent.getLongExtra("WORRY_ID", -1)
        if(worryId != (-1).toLong()) {
            worryLoading(worryId)
        }
    }

    private fun setViewModel() {
        worryDetailViewModel = application!!.let {
            ViewModelProvider(this, WorryDetailInjector(
                this.application
            ).provideWorryDetailViewModelFactory())
                .get(WorryDetailViewModel::class.java)
        }
        worryDetailViewModel?.initWorry()
    }

    private fun observeViewModel() {
        worryDetailViewModel!!.worry.observe (this, Observer {
            titleEdit.setText(it.title)
            contentEdit.setText(it.content)
        })
    }

    //카메라 시작
    private fun setUpClickListener() {
        selectPictureImage.setOnClickListener { // 갤러리 선택
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_GALLERY_PHOTO)
        }
        cameraImage.setOnClickListener { // 카메라 촬영
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 카메라 실행 부분
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent()
                } else {
                    Log.d(TAG, "권한 설정 요청")
                    ActivityCompat.requestPermissions(this@AddWorryActivity,
                        arrayOf<String?>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1000)
                }
            }
        }

        photoClearImage.setOnClickListener {
            path = ""
            selectImage.visibility = View.GONE
            photoClearImage.visibility = View.GONE

            selectPictureImage.visibility = View.VISIBLE
            cameraImage.visibility = View.VISIBLE

        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            }
            catch (ex: IOException) { // 파일을 만드는데 오류가 발생한 경우
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "TEST_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        mCurrentPhotoPath  = image.absolutePath
        return image
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                REQUEST_TAKE_PHOTO -> { // result for take photo
                    if (resultCode == Activity.RESULT_OK) {
                        val file = File(mCurrentPhotoPath)
                        myBitmap = MediaStore.Images.Media
                            .getBitmap(contentResolver, Uri.fromFile(file))
                        if (myBitmap != null) {
                            val exif = mCurrentPhotoPath?.let { ExifInterface(it) }
                            val exifOrientation: Int = exif!!.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                            val exifDegree = exifOrientationToDegrees(exifOrientation)
                            myBitmap = rotate(myBitmap, exifDegree)
                        }
                        else {
                            Log.d("myBitmap null", "null")
                        }
                        // get Image uri and path
                        fileUri = getImageUri(this@AddWorryActivity, myBitmap)
                        path = getRealPathFromURI(this@AddWorryActivity, fileUri)!!

                        selectPictureImage.visibility = View.GONE
                        cameraImage.visibility = View.GONE

                        selectImage.visibility = View.VISIBLE
                        selectImage.setImageURI(fileUri)
                        photoClearImage.visibility = View.VISIBLE
                    }
                }
                SELECT_GALLERY_PHOTO -> { // result for select gallery photo
                    if (resultCode == Activity.RESULT_OK) {
                        fileUri = data?.data
                        path = getRealPathFromURI(this, fileUri)!!

                        selectPictureImage.visibility = View.GONE
                        cameraImage.visibility = View.GONE

                        selectImage.visibility = View.VISIBLE
                        selectImage.setImageURI(fileUri)
                        photoClearImage.visibility = View.VISIBLE
                    }
                }
            }
        } catch (error: Exception) {
            error.printStackTrace()
        }

    }

    private fun exifOrientationToDegrees(exifOrientation: Int): Int {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }

    private fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? { // 이미지 회전 및 이미지 사이즈 압축
        var bitmap = bitmap
        if (degrees != 0 && bitmap != null) {
            val m = Matrix()
            m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2,
                bitmap.height.toFloat() / 2)
            try {
                val converted = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.width, bitmap.height, m, true)
                if (bitmap != converted) {
                    bitmap.recycle()
                    bitmap = converted
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 4
                    bitmap = Bitmap.createScaledBitmap(bitmap, 1280, 1280, true) // 이미지 사이즈 줄이기
                }
            } catch (ex: OutOfMemoryError) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap
    }

    private fun getImageUri(inContext: Context?, inImage: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext?.contentResolver, inImage, "Title" + " - " + Calendar.getInstance().getTime(), null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(context: Context?, uri: Uri?): String? {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri!!)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split: Array<String?> = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                return if ("primary".equals(type, ignoreCase = true)) {
                    (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                } else {
                    val SDcardpath = getRemovableSDCardPath(context)?.split("/Android".toRegex())!!.toTypedArray()[0]
                    SDcardpath + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id))
                return getDataColumn(context!!, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split: Array<String?> = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context!!, contentUri, selection,
                    selectionArgs)
            }
        } else if (uri != null) {
            if ("content".equals(uri.scheme, ignoreCase = true)) {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context!!, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .authority
    }


    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .authority
    }


    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .authority
    }


    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun getRemovableSDCardPath(context: Context?): String? {
        val storages: Array<File?> =
            ContextCompat.getExternalFilesDirs(context!!, null)
        return if (storages.size > 1 && storages[0] != null && storages[1] != null) storages[1].toString() else ""
    }

    private fun getDataColumn(
        context: Context, uri: Uri?,
        selection: String?, selectionArgs: Array<String?>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    //카메라 끝

    private fun setTextChangeListener() {

        titleEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                worryDetailViewModel!!.setWorryTitle(s.toString())
                curLenText.text = s.toString().length.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        contentEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                worryDetailViewModel!!.setWorryContent(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_worry_activity_toolbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.add_worry_tab -> {
                worryUpdateLoading()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun worryUpdateLoading() = launch {
        when {
            titleEdit.text.isEmpty() -> {
                showToast("제목을 입력해주세요")
            }
            titleEdit.text.length > 20 -> {
                showToast("제목을 20자 이하로 입력해주세요")
            }
            hashTag == "empty" -> {
                showToast("태그를 선택해주세요")
            }
            contentEdit.text.isEmpty() -> {
                showToast("내용을 입력해주세요")
            }
            else -> {
                addWorryLoadingUi.visibility = View.VISIBLE
                worryDetailViewModel!!.addOrUpdateWorry(userId, hashTag!!, path).join()
                val intent = Intent()
                setResult(RESULT_OK, intent)

                finish()
            }
        }
    }

    private fun worryLoading(worryId: Long) = launch {
        worryDetailViewModel!!.getWorryById(worryId).join()
        hashTag = worryDetailViewModel!!.worry.value?.tagName
        setExistingTag(hashTag!!)

        if(worryDetailViewModel!!.worry.value?.postImage != "") {
            selectPictureImage.visibility = View.GONE
            cameraImage.visibility = View.GONE

            selectImage.visibility = View.VISIBLE
            selectImage.setImageURI(Uri.parse(worryDetailViewModel!!.worry.value?.postImage))
            photoClearImage.visibility = View.VISIBLE
        }
    }

    private fun setExistingTag(hashTag: String) {
        when(hashTag) {
            "일상" -> {
                dailyLiftBtn.isSelected = true
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "가족" -> {
                familyBtn.isSelected = true
                familyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                familyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "연애" -> {
                dateBtn.isSelected = true
                dateBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dateBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "친구사이" -> {
                friendBtn.isSelected = true
                friendBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                friendBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "학교생활" -> {
                schoolBtn.isSelected = true
                schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                schoolBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "직장생활" -> {
                jobBtn.isSelected = true
                jobBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                jobBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "취업" -> {
                employmentBtn.isSelected = true
                employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                employmentBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "진로" -> {
                courseBtn.isSelected = true
                courseBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                courseBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "돈" -> {
                moneyBtn.isSelected = true
                moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                moneyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "건강" -> {
                healthBtn.isSelected = true
                healthBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                healthBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "기혼자만 아는" -> {
                marriedBtn.isSelected = true
                marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                marriedBtn.setTextColor(Color.parseColor(litePupleColor))
            }
            "육아" -> {
                infantBtn.isSelected = true
                infantBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                infantBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }
    }

    private fun setTagBtn() {
        dailyLiftBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dailyLiftBtn.isSelected = false
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "일상"
                resetBtnColor()
                dailyLiftBtn.isSelected = true
                dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dailyLiftBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        familyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                familyBtn.isSelected = false
                familyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                familyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "가족"
                resetBtnColor()
                familyBtn.isSelected = true
                familyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                familyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        friendBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                friendBtn.isSelected = false
                friendBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                friendBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "친구사이"
                resetBtnColor()
                friendBtn.isSelected = true
                friendBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                friendBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        dateBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                dateBtn.isSelected = false
                dateBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                dateBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "연애"
                resetBtnColor()
                dateBtn.isSelected = true
                dateBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                dateBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        schoolBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                schoolBtn.isSelected = false
                schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                schoolBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "학교생활"
                resetBtnColor()
                schoolBtn.isSelected = true
                schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                schoolBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        jobBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                jobBtn.isSelected = false
                jobBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                jobBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "직장생활"
                resetBtnColor()
                jobBtn.isSelected = true
                jobBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                jobBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        employmentBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                employmentBtn.isSelected = false
                employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                employmentBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "취업"
                resetBtnColor()
                employmentBtn.isSelected = true
                employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                employmentBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        courseBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                courseBtn.isSelected = false
                courseBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                courseBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "진로"
                resetBtnColor()
                courseBtn.isSelected = true
                courseBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                courseBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        moneyBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                moneyBtn.isSelected = false
                moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                moneyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "돈"
                resetBtnColor()
                moneyBtn.isSelected = true
                moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                moneyBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        healthBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                healthBtn.isSelected = false
                healthBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                healthBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "건강"
                resetBtnColor()
                healthBtn.isSelected = true
                healthBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                healthBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        marriedBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                marriedBtn.isSelected = false
                marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                marriedBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "기혼자만 아는"
                resetBtnColor()
                marriedBtn.isSelected = true
                marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                marriedBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }

        infantBtn.setOnClickListener {
            if(it.isSelected) { //선택되어 있다면
                infantBtn.isSelected = false
                infantBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
                infantBtn.setTextColor(Color.parseColor(superLiteGreyColor))
            }
            else {
                hashTag = "육아"
                resetBtnColor()
                infantBtn.isSelected = true
                infantBtn.background = resources.getDrawable(R.drawable.tag_btn_select_style)
                infantBtn.setTextColor(Color.parseColor(litePupleColor))
            }
        }
    }

    private fun resetBtnColor() {
        dailyLiftBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        familyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        friendBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        dateBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        schoolBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        jobBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        employmentBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        courseBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        moneyBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        healthBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        marriedBtn.setTextColor(Color.parseColor(superLiteGreyColor))
        infantBtn.setTextColor(Color.parseColor(superLiteGreyColor))

        dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        familyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        friendBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        dateBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        schoolBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        jobBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        employmentBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        courseBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        moneyBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        healthBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        marriedBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)
        dailyLiftBtn.background = resources.getDrawable(R.drawable.tag_btn_unselect_style)

        dailyLiftBtn.isSelected = false
        familyBtn.isSelected = false
        friendBtn.isSelected = false
        dateBtn.isSelected = false
        schoolBtn.isSelected = false
        jobBtn.isSelected = false
        employmentBtn.isSelected = false
        courseBtn.isSelected = false
        moneyBtn.isSelected = false
        healthBtn.isSelected = false
        marriedBtn.isSelected = false
        infantBtn.isSelected = false
    }

    private fun showToast(str: String) {
        var toast = Toast.makeText(this, str, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM, 0,300)
        toast.show()
    }

    private fun setStatusBarColor(str: String) {
        var view = window.decorView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(view != null) {
                when(str) {
                    "main" -> window.statusBarColor = Color.parseColor(Constants.mainNaviColor)
                    "dark" -> window.statusBarColor = Color.parseColor(Constants.darkNaviColor)
                }
            }
        }
    }
}