package com.example.appchatkl.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class User(
    @PrimaryKey
    var id: String = "",
    var fullName: String = "",
    var linkPhoto: String = "",
    var data: String = "",
    var phoneNumber: String = "",
    var section: String = "",
    var isFriend: Boolean = false,
    var date: String = ""
) {
}