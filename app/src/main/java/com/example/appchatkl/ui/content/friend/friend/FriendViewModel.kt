package com.example.appchatkl.ui.content.friend.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.ChatDBViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FriendViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val TAG = "FriendViewModel"
    private val _response = MutableLiveData<List<User>>()
    val responseTvShow: LiveData<List<User>>
        get() = _response
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun getAllUser(
        postReference: DatabaseReference,
        list: ArrayList<User>,
        host: String,
        chatDB: ChatDBViewModel
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                val friend = dataSnapshot.child("fiend").child(host).child("allId").getValue()
                analyst(friend.toString()).forEach {a->
                    if (a!=(host) && dataSnapshot.child("user").child(a)
                            .child("id").value.toString()!=("null")
                    ) {
                        list.add(
                            User(
                                dataSnapshot.child("user").child(a)
                                    .child("id").value.toString(),
                                dataSnapshot.child("user").child(a)
                                    .child("fullName").value.toString(),
                                dataSnapshot.child("user").child(a)
                                    .child("linkPhoto").value.toString()
                            )
                        )
                        uiScope.launch {
                            chatDB.insertUser(
                                User(
                                    id = dataSnapshot.child("user").child(a)
                                        .child("id").value.toString(),
                                    fullName = dataSnapshot.child("user").child(a)
                                        .child("fullName").value.toString(),
                                    linkPhoto = dataSnapshot.child("user").child(a)
                                        .child("linkPhoto").value.toString()
                                )
                            )
                        }
                    }
                }
                _response.value = list
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                chatDB.loadUser().forEach {
                    list.add(
                        User(
                            it.id,
                            it.fullName,
                            it.linkPhoto
                        )
                    )
                }
                _response.value = list
            }
        }
        postReference.addValueEventListener(postListener)

    }

    fun getAllUserOff(
        list: ArrayList<User>,
        chatDB: ChatDBViewModel
    ) = viewModelScope.launch {
        chatDB.loadUser().forEach {
            list.add(
                User(
                    it.id,
                    it.fullName,
                    it.linkPhoto
                )
            )
            Log.d(TAG, "getAllUserOff: " + it.fullName)
        }
        Log.d(TAG, "getAllUserOff: " + chatDB.loadUser().size)
        _response.value = list
    }

    fun analyst(s: String): List<String> {
        return s.split(",").toList()
    }
}