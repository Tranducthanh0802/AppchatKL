package com.example.appchatkl.ui.content.listMessage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.data.Message
import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.data.db.data.Conversations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ListMessageViewModel : ViewModel() {
    private val _conversation = MutableLiveData<List<Conversation>>()
    val conversation: LiveData<List<Conversation>>
        get() = _conversation
    val TAG = "ListMessageViewModel"
    // TODO: Implement the ViewModel
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun getChat(
        database: DatabaseReference,
        list: ArrayList<Conversation>,
        host: String,
        chatDB: ChatDBViewModel
    ) =
        viewModelScope.launch {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    list.clear()
                    val post = dataSnapshot.child("conversation").children
                    post.forEach { b->
                        var name = ""
                        var linkPhoto = ""
                        var message = ""
                        var id = ""
                        var idhost = ""
                        var idSee = ""
                        var count = ""
                        if (check(b.key, host)) {
                            if (people(b.key).size > 3) {
                                people(b.key).forEach { a->
                                    if (dataSnapshot.child("user").child(a)
                                            .child("fullName").getValue().toString()
                                            !=("null") && a!=(host)
                                    )
                                        name += dataSnapshot.child("user").child(a)
                                            .child("fullName").getValue().toString() + ","
                                }
                                if (dataSnapshot.child("user").child(b.toString())
                                        .child("linkPhoto").getValue().toString()!=("null")
                                )
                                    linkPhoto = dataSnapshot.child("user").child(b.toString())
                                        .child("linkPhoto").getValue().toString()
                            } else {
                                Log.d(TAG, "onDataChange: 1" + b.key)
                                people(b.key).forEach { c->
                                    if (dataSnapshot.child("user").child(c)
                                            .child("fullName").getValue().toString()
                                            !=("null") && !b.equals(host)
                                    ) {
                                        name = dataSnapshot.child("user").child(c)
                                            .child("fullName").getValue().toString()
                                        linkPhoto = dataSnapshot.child("user").child(c)
                                            .child("linkPhoto").getValue().toString()
                                    }
                                }

                            }
                            message = dataSnapshot.child("conversation").child(b.key.toString())
                                .child("message").getValue().toString()
                            id = b.key.toString()
                            idhost = dataSnapshot.child("conversation").child(b.key.toString())
                                .child("id").getValue().toString()
                            idSee = dataSnapshot.child("conversation").child(b.key.toString())
                                .child("idSee").getValue().toString()
                            count = dataSnapshot.child("conversation").child(b.key.toString())
                                .child("count").getValue().toString()
                        }
                        var notifatcation = false
                        if (host==(idhost) || host==(idSee))
                            notifatcation = true
                        if (message!=("")) {
                            list.add(
                                Conversation(
                                    Message(
                                        takeMessage(message),
                                        time = takeTime(message),
                                        avata = linkPhoto,
                                        id = id
                                    ),
                                    name,
                                    count = count,
                                    isNotificat = notifatcation,
                                    listMessage = message.split("@@@@@")
                                        .toList() as ArrayList<String>,
                                    isFind = false
                                )
                            )
                            uiScope.launch {
                                chatDB.insertConversation(
                                    Conversations(
                                        idTeam = id,
                                        time = takeTime(message),
                                        linkPhotoTeam = linkPhoto,
                                        nameNhom = name,
                                        count = count,
                                        isNotification = notifatcation,
                                        isFind = false,
                                        mesage = message
                                    )
                                )
                            }
                        }
                    }
                    list.sortedBy { it.message.time }
                    _conversation.value = list

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.d(TAG, "onCancelled: erro")
                    chatDB.loadConversation().forEach { a->
                        list.add(
                            Conversation(
                                Message(
                                    takeMessage(a.mesage),
                                    time = takeTime(a.mesage),
                                    avata = a.linkPhotoTeam,
                                    id = a.idTeam
                                ),
                                a.nameNhom,
                                count = a.count,
                                isNotificat = a.isNotification,
                                listMessage = a.mesage.split("@@@@@")
                                    .toList() as ArrayList<String>,
                                isFind = false
                            )
                        )
                        list.sortedBy { it.message.time }
                    }
                    _conversation.value = list
                    Log.d(TAG, "onCancelled: mat mang")
                }
            }
            database.addValueEventListener(postListener)
        }

    fun getChatOff(
        list: ArrayList<Conversation>,

        chatDB: ChatDBViewModel
    ) =
        viewModelScope.launch {
            // Get Post object and use the values to update the UI

            chatDB.loadConversation().forEach { b->
                list.add(
                    Conversation(
                        Message(
                            takeMessage(b.mesage),
                            time = takeTime(b.mesage),
                            avata = b.linkPhotoTeam,
                            id = b.idTeam
                        ),
                        b.nameNhom,
                        count = b.count,
                        isNotificat = b.isNotification,
                        listMessage = b.mesage.split("@@@@@")
                            .toList() as ArrayList<String>,
                        isFind = false
                    )
                )
                list.sortedBy { it.message.time }
            }
            _conversation.value = list
            Log.d(TAG, "onCancelled: mat mang")
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

    private fun takeTime(s: String): String {
        if (s == "") return ""
        val message1 = s.split("@@@@@").toTypedArray()
        val message = message1[message1.size - 2].split("@@@@").toTypedArray()
        return message[1]
    }

    private fun takeMessage(s: String): String {
        if (s == "") return ""
        val message2 = s.split("@@@@@").toTypedArray()
        val message1 = message2[message2.size - 2].split("@@@@").toTypedArray()[0]
        val message = message1.split("@@@").toTypedArray()[0]
        return message
    }
}