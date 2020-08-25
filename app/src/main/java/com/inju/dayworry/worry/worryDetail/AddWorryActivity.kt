package com.inju.dayworry.worry.worryDetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.inju.dayworry.R
import com.inju.dayworry.worry.worryDetail.buildlogic.WorryDetailInjector
import kotlinx.android.synthetic.main.activity_add_worry.*

class AddWorryActivity : AppCompatActivity() {

    private val RESULT_OK = 101
    private var worryDetailViewModel: WorryDetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_worry)

        setSupportActionBar(addWorryActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        worryDetailViewModel = application!!.let {
            ViewModelProvider(this, WorryDetailInjector(
                this.application
            ).provideWorryDetailViewModelFactory())
                .get(WorryDetailViewModel::class.java)
        }

        worryDetailViewModel!!.worryLiveData.observe (this, Observer {
            titleEdit.setText(it.title)
            contentEdit.setText(it.content)
        })

        titleEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                worryDetailViewModel!!.worry.title = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        contentEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                worryDetailViewModel!!.worry.content = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
//
//        val worryId = intent.getStringExtra("WORRY_ID")
//        if(worryId != null) {
//            worryLoading(worryId)
//        }
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

    private fun worryUpdateLoading() {
        worryDetailViewModel!!.addOrUpdateWorry(this)
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun worryLoading(worryId: String) {
        worryDetailViewModel!!.getWorryByIdWorry(worryId)
    }
}