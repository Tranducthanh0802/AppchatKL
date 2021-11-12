package com.example.appchatkl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.ListFragment
import com.example.appchatkl.R
import com.example.appchatkl.databinding.ActivityMainBinding
import com.example.appchatkl.ui.content.chat.ChatFragment
import com.example.appchatkl.ui.content.listMessage.ListMessageFragment
import com.example.appchatkl.ui.content.listMessage.adapter.SendData
import com.example.appchatkl.ui.content.user.Find
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
 class MainActivity : AppCompatActivity(), SendData {

    val TAG = "MainActivity"

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override open fun onBackPressed() {
        super.onBackPressed()
    }

    override fun send(s: String) {
        val bundle = Bundle()
        bundle.putString("id", s)
        val chatFragment = ChatFragment()
        chatFragment.arguments = bundle
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framentActivity, chatFragment)
        transaction.addToBackStack("list")
        transaction.commit()
    }


}