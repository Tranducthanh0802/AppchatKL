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
    private var _idSendRequest1 = MutableLiveData<String>()
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
                val post = dataSnapshot.child("user").children
                var id:String
                val friend = dataSnapshot.child("fiend").child(host).child("allId").getValue()
                val request =
                    dataSnapshot.child("request").child(host).child("sendRequest").getValue()
                _idSendRequest.value = request.toString()
                val receive =
                    dataSnapshot.child("request").child(host).child("receiveRequest").getValue()
                post.forEach {
                    var isFriend = false
                    val user = User()
                    if (it.key.toString()!=(host) && it.key.toString()!=("null")) {
                        id = dataSnapshot.child("user").child(it.key.toString())
                            .child("id").value.toString()
                        analyst(friend.toString()).forEach {    a->
                            Log.d(TAG, "Friend: " + it + " () " + id)
                            if (a==id) {
                                isFriend = true
                            }
                        }
                        analyst(request.toString()).forEach { a ->
                            Log.d(TAG, "Request: " + it + " () " + id)
                            if (a==(id)) {
                                isFriend = true
                            }
                        }
                        analyst(receive.toString()).forEach { a->
                            Log.d(TAG, "Receive: " + it + " () " + id)
                            if (a==(id)) {
                                isFriend = true
                            }
                        }
                        user.id = dataSnapshot.child("user").child(it.key.toString())
                            .child("id").value.toString()
                        user.fullName = dataSnapshot.child("user").child(it.key.toString())
                            .child("fullName").value.toString()
                        user.linkPhoto = dataSnapshot.child("user").child(it.key.toString())
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
                var request =
                    dataSnapshot.child("request").child(idGuest).child("sendRequest").getValue()
                if (request ==("null")) request = ""
                _idSendRequest1.value = request.toString()
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