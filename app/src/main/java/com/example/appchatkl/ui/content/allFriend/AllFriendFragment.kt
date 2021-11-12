package com.example.appchatkl.ui.content.allFriend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.AllFriendFragmentBinding
import com.example.appchatkl.ui.content.allFriend.adapter.AddFriend
import com.example.appchatkl.ui.content.allFriend.adapter.AllFriendAdapter
import com.example.appchatkl.ui.content.friend.friend.adapter.FriendAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllFriendFragment : Fragment(), AddFriend {
    val TAG = "AllFriendFragment"
    lateinit var binding: AllFriendFragmentBinding
    lateinit var host: String

    companion object {
        fun newInstance() = AllFriendFragment()
    }

    private lateinit var viewModel: AllFriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.all_friend_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllFriendViewModel::class.java)

        var list = ArrayList<User>()

        host = commomFunction.getId()
        viewModel.getAllUser(commomFunction.database, list, host)
        val friendAdapter = AllFriendAdapter(this)
        binding.stickyListFriend.apply {
            adapter = friendAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.responseTvShow.observe(viewLifecycleOwner, {
            friendAdapter.listConversation = it
            binding.stickyListFriend.adapter?.notifyDataSetChanged()
        })
    }
    override fun onclick(s: String) {
        var d = ""
        viewModel.guest(commomFunction.database, s)
        val a = viewModel.idSendReQuest.value + s + ","
        commomFunction.database.child("request").child(host).child("sendRequest").setValue(a)
        if (!viewModel.idSendReQuest1.value.equals("null"))
            d = viewModel.idSendReQuest1.value.toString()
        val b = d + host + ","
        commomFunction.database.child("request").child(s).child("receiveRequest").setValue(b)
    }
}