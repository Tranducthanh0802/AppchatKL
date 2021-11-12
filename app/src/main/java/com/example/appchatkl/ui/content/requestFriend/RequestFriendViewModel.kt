package com.example.appchatkl.ui.content.requestFriend

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

class RequestFriendViewModel : ViewModel() {
    val TAG = "RequestFriendViewModel"
    private var _invitation = MutableLiveData<List<User>>()
    val invitation: LiveData<List<User>>
        get() = _invitation
    private var _request = MutableLiveData<List<User>>()
    val request: LiveData<List<User>>
        get() = _request
    private var _idSendRequest = MutableLiveData<String>()
    val idSendReQuest: LiveData<String>
        get() = _idSendRequest
    private var _idReciveRequest = MutableLiveData<String>()
    val idReceiveReQuest: LiveData<String>
        get() = _idReciveRequest
    private var _idSendRequest1 = MutableLiveData<String>()
    val idSendReQuest1: LiveData<String>
        get() = _idSendRequest1
    private var _idReciveRequest1 = MutableLiveData<String>()
    val idReceiveReQuest1: LiveData<String>
        get() = _idReciveRequest1
    private var _friend = MutableLiveData<String>()
    val friend: LiveData<String>
        get() = _friend
    private var _friend1 = MutableLiveData<String>()
    val friend1: LiveData<String>
        get() = _friend1


    fun getInvitationAndRequest(
        postReference: DatabaseReference,
        listInvitation: ArrayList<User>, listRequest: ArrayList<User>, host: String
    ) = viewModelScope.launch {
        val currentInv: List<String> = ArrayList<String>()
        val currentRequest: List<String> = ArrayList<String>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                listInvitation.clear()
                listRequest.clear()
                val post = dataSnapshot!!.child("request").children
                post.forEach {
                    if (it.key.toString().equals(host) && !it.key.toString().equals("null")) {
                        val invitation1 =
                            dataSnapshot!!.child("request").child(host).child("receiveRequest")
                                .getValue()
                        val request1 =
                            dataSnapshot!!.child("request").child(host).child("sendRequest")
                                .getValue()
                        _friend.value =
                            dataSnapshot!!.child("fiend").child(host).child("allId").getValue()
                                .toString()
                        _idReciveRequest.value = request1.toString()
                        _idSendRequest.value = invitation1.toString()
                        val currentInv = analysis(invitation1 as String)
                        val currentRequest = analysis(request1 as String)
                        currentInv.forEach {
                            if (!dataSnapshot!!.child("user").child(it.toString())
                                    .child("fullName").value.toString().equals("null")
                            )
                                listInvitation.add(
                                    User(
                                        dataSnapshot!!.child("user").child(it.toString())
                                            .child("id").value.toString(),
                                        dataSnapshot!!.child("user").child(it.toString())
                                            .child("fullName").value.toString(),
                                        dataSnapshot!!.child("user").child(it.toString())
                                            .child("linkPhoto").value.toString()
                                    )
                                )
                        }
                        currentRequest.forEach {
                            if (!dataSnapshot!!.child("user").child(it.toString())
                                    .child("id").value.toString().equals("null")
                            )
                                listRequest.add(
                                    User(
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
                }
                _invitation.value = listInvitation
                _request.value = listRequest
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
                val post = dataSnapshot!!.child("request").children
                post.forEach {
                    if (it.key.toString().equals(idGuest) && !it.key.toString().equals("null")) {
                        val invitation1 =
                            dataSnapshot!!.child("request").child(idGuest).child("receiveRequest")
                                .getValue()
                        val request1 =
                            dataSnapshot!!.child("request").child(idGuest).child("sendRequest")
                                .getValue()
                        _friend1.value =
                            dataSnapshot!!.child("fiend").child(idGuest).child("allId").getValue()
                                .toString()
                        _idReciveRequest1.value = request1.toString()
                        _idSendRequest1.value = invitation1.toString()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        postReference.addValueEventListener(postListener)
    }

    private fun analysis(post: String): List<String> {
        val current = post.split(",").toList()
        return current
    }

    fun delete(s: String, id: String): String {
        var a = ""
        s.split(",").forEach {
            if (!it.equals(id) && !it.equals("")) {
                a += it + ","
            }
        }
        return a
    }
}