package com.example.appchatkl.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appchatkl.data.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginViewModel : ViewModel() {
    var _email = MutableLiveData<String>()
    var _passWord = MutableLiveData<String>()
    private var _isCheck = MutableLiveData<Boolean>()
    val isCheck: LiveData<Boolean> get() = _isCheck
    val isNotification = MutableLiveData<String>()


    // var accountLiveData =MutableLiveData<Account>()

    fun onLogin() {
        Log.d("abc", "signInWithEmail:success" + _email.value.toString())
        val account = Account(_email.value.toString(), _passWord.value.toString())
        // accountLiveData.value= Account(email.value.toString(),password.value.toString())
        if (account.isValidEmail()
            && account.isValidPassword()
        ) {
            val auth: FirebaseAuth = Firebase.auth
            auth.signInWithEmailAndPassword(account.email, account.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("abc", "signInWithEmail:success")

                        _isCheck.value = true
                    } else {
                        // If sign in fails, display a message to the user.
                        isNotification.value = "Login failure"
                        Log.w("abc", "signInWithEmail:failure", task.exception)
                    }
                }
        }


    }

}