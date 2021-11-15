package com.example.appchatkl.data.db


import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.data.Conversations
import com.example.appchatkl.data.db.data.Save
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(private val noteDao: ChatDao) {
     fun insertConversation(note: Conversations) = noteDao.insertConversation(note)
     fun insertUser(note: User) = noteDao.insertUser(note)
     fun insertSave(note: Save) = noteDao.insertSave(note)
     fun loadConversation():List<Conversations> = noteDao.loadConversation()
     fun loadSave():List<Save> = noteDao.loadSave()
     fun loadUser():List<User> = noteDao.loadUser()
     fun deleteNote() = noteDao.deleteSave()
}