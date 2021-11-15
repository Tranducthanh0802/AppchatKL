package com.example.appchatkl.ui.content.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.data.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class BottomViewModel : ViewModel() {
    val TAG = "BottomViewModel"
    private var _count = MutableLiveData("0")
    val count: LiveData<String> get() = _count
    private var _countRecive = MutableLiveData("0")
    val countRecive: LiveData<String> get() = _countRecive
    private var _findM = MutableLiveData<List<Conversation>>()
    var edt = MutableLiveData<String>()
    var searchEmpty = MutableLiveData(false)
    var postion = MutableLiveData<Int>()


    fun getChat(database: DatabaseReference, list: ArrayList<Conversation>, host: String) =
        viewModelScope.launch {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.child("conversation").children
                    var count1 = 0
                    post.forEach {
                        var name  = ""
                        var linkPhoto= ""
                        var message = ""
                        val id = ""
                        var idhost = ""
                        var idSee = ""
                        val count = ""
                        if (check(it.key, host)) {
                            if (people(it.key).size > 3) {
                                people(it.key).forEach { a->
                                    if (dataSnapshot.child("user").child(a)
                                            .child("fullName").getValue().toString()
                                            !=("null") && !it.equals(host)
                                    )
                                        name += dataSnapshot.child("user").child(a)
                                            .child("fullName").getValue().toString() + ","
                                }
                                if (dataSnapshot.child("user").child(it.toString())
                                        .child("linkPhoto").getValue().toString()!=("null")
                                )
                                    linkPhoto = dataSnapshot.child("user").child(it.toString())
                                        .child("linkPhoto").getValue().toString()
                                Log.d(TAG, "onDataChange: 2 " + it.key + "  " + people(it.key).size)
                            } else {
                                Log.d(TAG, "onDataChange: 1" + it.key)
                                people(it.key).forEach {a->
                                    if (dataSnapshot.child("user").child(a)
                                            .child("fullName").getValue().toString()
                                            !=("null") && a!=(host)
                                    ) {
                                        name = dataSnapshot.child("user").child(a)
                                            .child("fullName").getValue().toString()
                                        linkPhoto = dataSnapshot.child("user").child(a)
                                            .child("linkPhoto").getValue().toString()
                                    }
                                }
                            }
                            message = dataSnapshot.child("conversation").child(it.key.toString())
                                .child("message").getValue().toString()
                            idhost = dataSnapshot.child("conversation").child(it.key.toString())
                                .child("id").getValue().toString()
                            idSee = dataSnapshot.child("conversation").child(it.key.toString())
                                .child("idSee").getValue().toString()
                        }
                        if (host!=(idhost) && host!=(idSee) && it.key.toString()
                                !=("null")
                        )
                            count1 += 1
                        if (message!=("")) {
                            list.add(
                                Conversation(
                                    Message(
                                        avata = linkPhoto,
                                        id = id
                                    ),
                                    name,
                                    count = count,
                                    listMessage = message.split("@@@@@")
                                        .toList() as ArrayList<String>
                                )
                            )
                        }
                    }
                    _findM.value = list
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                }
            }
            database.addValueEventListener(postListener)
        }

    fun getInvitationAndRequest(
        postReference: DatabaseReference,
        host: String
    ) = viewModelScope.launch {


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post =
                    dataSnapshot.child("request").child(host).child("receiveRequest").getValue()
                _countRecive.value = (people(post.toString()).size - 1).toString()

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        postReference.addValueEventListener(postListener)
    }

    private fun check(key: String?, host: String): Boolean {
        val list = people(key)
        list.forEach {
            if (it==(host)) return true
        }
        return false
    }

    private fun people(key: String?): List<String> {
        return key.toString().split(",").toList()
    }
}