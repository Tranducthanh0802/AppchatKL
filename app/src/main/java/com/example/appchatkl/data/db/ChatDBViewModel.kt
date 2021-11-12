package com.example.appchatkl.data.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.Data.Conversations
import com.example.appchatkl.data.db.Data.Save
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDBViewModel @Inject constructor(val noteRepository: ChatRepository) : ViewModel() {
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