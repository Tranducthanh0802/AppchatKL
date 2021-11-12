package com.example.appchatkl.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class Conversation(
    var message: Message,
    @PrimaryKey
    var name: String = "",
    var notification: String = "",
    var isNotificat: Boolean = false,
    var count: String = "",
    var id: String = "",
    var idSee: String = "",
    var listMessage: ArrayList<String> = ArrayList<String>(),
    var isFind: Boolean = false
) {
    init {
        var s = ""
        if (message.message.length > 10) {
            for (c in 0..message.message.length) {
                if (c == message.message.length - 5) {
                    s += "..."
                    break
                }
                s += message.message[c]
            }
            message.message = s
        }
    }
}