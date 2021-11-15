package com.example.appchatkl.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.data.Conversations
import com.example.appchatkl.data.db.data.Save

@Database(entities = [User::class, Conversations::class, Save::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val  DB_NAME = "chat_db"
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}