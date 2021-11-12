package com.example.appchatkl.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.Account
import com.example.appchatkl.data.Friend
import com.example.appchatkl.data.Request
import com.example.appchatkl.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val TAG = "RegisterViewModel"
    var _fullName = MutableLiveData<String>()
    val fullName: LiveData<String>
        get() = _fullName
    var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email
    var _passWord = MutableLiveData<String>()
    val passWord: LiveData<String>
        get() = _passWord
    var isCheck: Boolean = false
    var notification = MutableLiveData<String>()
    var isShowNotification = false

    fun saveIF( user: User, friend: Friend, request: Request) {
        commomFunction.database.child("user").child(user.id.toString()).setValue(user)
        commomFunction.database.child("fiend").child(user.id.toString()).setValue(friend)
        commomFunction.database.child("request").child(user.id.toString()).setValue(request)
    }

    fun register( user: User, friend: Friend, request: Request) {
        val account = Account(email.value.toString(), passWord.value.toString())
        isShowNotification = true
        if (account.isValidEmail() == true
            && account.isValidPassword() == true && !fullName.value.toString()
                .isNullOrEmpty() && isCheck
        ) {
            commomFunction.auth.createUserWithEmailAndPassword(account.email, account.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.id = commomFunction.auth.uid.toString()
                        user.fullName = fullName.value.toString()
                        saveIF( user, friend, request)
                        notification.value = "registration success "
                    } else {
                        // If sign in fails, display a message to the user.
                        notification.value = "registration failed "
                    }
                }
        } else {
            notification.value = "registration failed "
        }
    }


}