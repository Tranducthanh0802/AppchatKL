package com.example.appchatkl.data.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.data.Conversations
import com.example.appchatkl.data.db.data.Save
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDBViewModel @Inject constructor(private val noteRepository: ChatRepository) : ViewModel() {
    fun insertConversation(note: Conversations) = viewModelScope.launch {
        noteRepository.insertConversation(note)
    }

    fun insertUser(note: User) = viewModelScope.launch {
        noteRepository.insertUser(note)
    }

    fun insertSave(note: Save) = viewModelScope.launch {
        noteRepository.insertSave(note)
    }
    fun loadConversation():List<Conversations> = noteRepository.loadConversation()


    fun loadUser() :List<User> {
        return noteRepository.loadUser()
    }

    fun loadSave():List<Save>  {
       return noteRepository.loadSave()
    }

    fun deleteSave() =viewModelScope.launch {
        noteRepository.deleteNote()
    }
}