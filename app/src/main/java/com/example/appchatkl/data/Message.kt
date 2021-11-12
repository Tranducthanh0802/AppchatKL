package com.example.appchatkl.data

import androidx.room.Entity

@Entity(tableName = "Mesage")
class Message(
    var message: String = "",
    var id: String = "",
    var time: String = "",
    var avata: String = "",
    var image: String = "",
    var isImage: Boolean = false,
    var isShowTime: Boolean = false,
    var isShowAvata: Boolean = false,
    var isRight: Boolean = false
) {
}