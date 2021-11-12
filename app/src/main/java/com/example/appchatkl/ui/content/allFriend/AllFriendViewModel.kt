package com.example.appchatkl.ui.content.allFriend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class AllFriendViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val TAG = "AllFriendViewModel"
    private var _response = MutableLiveData<List<User>>()
    val responseTvShow: LiveData<List<User>>
        get() = _response
    private var _idSendRequest = MutableLiveData<String>()
    val idSendReQuest: LiveData<String>
        get() = _idSendRequest
    private var _idSendRequest1 = MutableLiveData<String>("")
    val idSendReQuest1: LiveData<String>
        get() = _idSendRequest1

    fun getAllUser(
        postReference: DatabaseReference,
        list: ArrayList<User>,
        host: String
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                val post = dataSnapshot!!.child("user").children
                var id = ""
                val Friend = dataSnapshot!!.child("fiend").child(host).child("allId").getValue()
                val Request =
                    dataSnapshot!!.child("request").child(host).child("sendRequest").getValue()
                _idSendRequest.value = Request.toString()
                val Receive =
                    dataSnapshot!!.child("request").child(host).child("receiveRequest").getValue()
                post.forEach {
                    var isFriend = false
                    val user = User()
                    if (!it.key.toString().equals(host) && !it.key.toString().equals("null")) {
                        id = dataSnapshot!!.child("user").child(it.key.toString())
                            .child("id").value.toString()
                        analyst(Friend.toString()).forEach {
                            Log.d(TAG, "Friend: " + it + " () " + id)
                            if (it.equals(id)) {
                                isFriend = true
                            }
                        }
                        analyst(Request.toString()).forEach {
                            Log.d(TAG, "Request: " + it + " () " + id)
                            if (it.equals(id)) {
                                isFriend = true
                            }
                        }
                        analyst(Receive.toString()).forEach {
                            Log.d(TAG, "Receive: " + it + " () " + id)
                            if (it.equals(id)) {
                                isFriend = true
                            }
                        }
                        user.id = dataSnapshot!!.child("user").child(it.key.toString())
                            .child("id").value.toString()
                        user.fullName = dataSnapshot!!.child("user").child(it.key.toString())
                            .child("fullName").value.toString()
                        user.linkPhoto = dataSnapshot!!.child("user").child(it.key.toString())
                            .child("linkPhoto").value.toString()
                        user.isFriend = isFriend
                        list.add(user)
                    }

                }
                list.sortedBy { it.toString() }
                _response.value = list
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        postReference.addValueEventListener(postListener)
    }

    fun guest(
        postReference: DatabaseReference,
        idGuest: String
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                var Request =
                    dataSnapshot!!.child("request").child(idGuest).child("sendRequest").getValue()
                if (Request!!.equals("null")) Request = ""
                _idSendRequest1.value = Request.toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        postReference.addValueEventListener(postListener)
    }

    fun analyst(s: String): List<String> {
        return s.split(",").toList()
    }
}