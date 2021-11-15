package com.example.appchatkl.data.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Conversations")
class Conversations(@PrimaryKey var idTeam:String,
                    var mesage:String,
                    var time:String,
                    var linkPhotoTeam:String,
                    var nameNhom:String,
                    var count:String,
                    var isNotification:Boolean,
                    var isFind:Boolean
                    ) {
}