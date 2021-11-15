package com.example.appchatkl.ui.content.user

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

class UserViewModel : ViewModel() {
    private var _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun getIF(
        postReference: DatabaseReference,
        host: String
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                _user.value = User(
                    dataSnapshot.child("user").child(host)
                        .child("id").value.toString(),
                    dataSnapshot.child("user").child(host)
                        .child("fullName").value.toString(),
                    dataSnapshot.child("user").child(host)
                        .child("linkPhoto").value.toString(),
                    date = dataSnapshot.child("user").child(host)
                        .child("date").value.toString(),
                    phoneNumber = dataSnapshot.child("user").child(host)
                        .child("phoneNumber").value.toString()
                )
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        postReference.addValueEventListener(postListener)
    }


}