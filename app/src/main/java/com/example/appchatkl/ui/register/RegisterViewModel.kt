package com.example.appchatkl.ui.register


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appchatkl.CommomFunction
import com.example.appchatkl.data.Account
import com.example.appchatkl.data.Friend
import com.example.appchatkl.data.Request
import com.example.appchatkl.data.User


class RegisterViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val TAG = "RegisterViewModel"
    var _fullName = MutableLiveData<String>()
    val fullName: LiveData<String>
        get() = _fullName
    var _email = MutableLiveData<String>()
   private val email: LiveData<String>
        get() = _email
    var _passWord = MutableLiveData<String>()
    val passWord: LiveData<String>
        get() = _passWord
    var isCheck: Boolean = false
    var notification = MutableLiveData<String>()
    private var isShowNotification = false

    private fun saveIF( user: User, friend: Friend, request: Request) {
        CommomFunction.database.child("user").child(user.id).setValue(user)
        CommomFunction.database.child("fiend").child(user.id).setValue(friend)
        CommomFunction.database.child("request").child(user.id).setValue(request)
    }

    fun register( user: User, friend: Friend, request: Request) {
        val account = Account(email.value.toString(), passWord.value.toString())
        isShowNotification = true
        if (account.isValidEmail() 
            && account.isValidPassword()  && !fullName.value.toString()
                .isNullOrEmpty() && isCheck
        ) {
            CommomFunction.auth.createUserWithEmailAndPassword(account.email, account.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.id = CommomFunction.auth.uid.toString()
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