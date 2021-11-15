package com.example.appchatkl.ui.content.allFriend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.CommomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.AllFriendFragmentBinding
import com.example.appchatkl.ui.content.allFriend.adapter.AddFriend
import com.example.appchatkl.ui.content.allFriend.adapter.AllFriendAdapter

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
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.all_friend_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllFriendViewModel::class.java)

        val list = ArrayList<User>()

        host = CommomFunction.getId()
        viewModel.getAllUser(CommomFunction.database, list, host)
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
        viewModel.guest(CommomFunction.database, s)
        val a = viewModel.idSendReQuest.value + s + ","
        CommomFunction.database.child("request").child(host).child("sendRequest").setValue(a)
        if (!viewModel.idSendReQuest1.value.equals("null"))
            d = viewModel.idSendReQuest1.value.toString()
        val b = d + host + ","
        CommomFunction.database.child("request").child(s).child("receiveRequest").setValue(b)
    }
}