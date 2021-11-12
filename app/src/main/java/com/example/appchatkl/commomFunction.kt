package com.example.appchatkl

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object commomFunction {
    var auth = Firebase.auth
    val currentUser = auth.currentUser
    val database = Firebase.database.reference
    fun getId(): String {
        return currentUser!!.uid
    }
    fun getEmail(): String {
        return currentUser!!.email.toString()
    }

    fun compare(id: String, host: String): Boolean {
        var a: List<String> = id.split(",").toList()
        var b = host.split(",").toList()
        a = a.sortedBy { it.toString().trim() }
        b = b.sortedBy { it.toString().trim() }
        if (a.toString().equals(b.toString())) return true
        return false
    }

    fun checkConnect(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netWorkInfo = connectivityManager.activeNetworkInfo
        if (netWorkInfo != null && netWorkInfo.isConnectedOrConnecting) return true
        return false
    }
}