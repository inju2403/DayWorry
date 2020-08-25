package com.inju.dayworry.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Worry (
    @PrimaryKey
    var id : String = UUID.randomUUID().toString(),
    var createdAt : Date = Date(),
    var title : String = "",
    var content : String = ""
): RealmObject()