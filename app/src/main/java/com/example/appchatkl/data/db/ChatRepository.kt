package com.example.appchatkl.data.db


import androidx.lifecycle.LiveData
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.Data.Conversations
import com.example.appchatkl.data.db.Data.Save
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(private val noteDao: ChatDao) {
    suspend fun insertConversation(note: Conversations) = noteDao.insertConversation(note)
    suspend fun insertUser(note: User) = noteDao.insertUser(note)
    suspend fun insertSave(note: Save) = noteDao.insertSave(note)
     fun loadConversation():List<Conversations> = noteDao.loadConversation()
     fun loadSave():List<Save> = noteDao.loadSave()
     fun loadUser():List<User> = noteDao.loadUser()
    suspend fun deleteNote() = noteDao.deleteSave()
}