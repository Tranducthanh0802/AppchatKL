package com.example.appchatkl.ui.content.createConversation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class CreateConversationViewModel : ViewModel() {
    var TAG = "CreateConversationViewModel"

    // TODO: Implement the ViewModel
    private val _response = MutableLiveData<List<CreateConversation>>()
    val responseTvShow: LiveData<List<CreateConversation>>
        get() = _response
    private val _select = MutableLiveData<List<CreateConversation>>()
    val selectUser: LiveData<List<CreateConversation>>
        get() = _select


    fun getAllUser(
        postReference: DatabaseReference,
        list: ArrayList<CreateConversation>, host: String
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                val Friend =
                    dataSnapshot!!.child("fiend").child(host).child("allId").getValue().toString()
                Friend.split(",").forEach {
                    if (!dataSnapshot!!.child("user").child(it.toString())
                            .child("id").value.toString().equals("null")
                    ) {
                        list.add(
                            CreateConversation(
                                dataSnapshot!!.child("user").child(it.toString())
                                    .child("id").value.toString(),
                                dataSnapshot!!.child("user").child(it.toString())
                                    .child("fullName").value.toString(),
                                dataSnapshot!!.child("user").child(it.toString())
                                    .child("linkPhoto").value.toString()
                            )
                        )
                    }
                }
                _response.value = list
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        postReference.addValueEventListener(postListener)
    }

    fun getListSelect(list: ArrayList<CreateConversation>) {
        _select.postValue(list)
    }

    fun saveIF(database: DatabaseReference, id: String, message: Chat) {
        database.child("conversation").child(id).setValue(message)
    }

    fun check(database: DatabaseReference, id: String, message: Chat) = viewModelScope.launch {
        var k = 1
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot!!.child("conversation").children
                post.forEach {
                    if (commomFunction.compare(it.key.toString(), id)) {
                        k = 0
                        Log.d(TAG, "onDataChange123: " + it.key.toString() + " () " + id)
                    }
                }
                if (k == 1) {
                    saveIF(database, id, message)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        database.addValueEventListener(postListener)
    }


}