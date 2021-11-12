package com.example.appchatkl.data

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData


class Account(var email: String = "", var password: String = "") {

    fun isValidEmail(): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= 6
    }
}