package com.example.appchatkl.ui.content.friend.friend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.AppDatabase
import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.databinding.FriendFragmentBinding
import com.example.appchatkl.ui.content.friend.friend.adapter.FriendAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendFragment : Fragment() {
    lateinit var binding: FriendFragmentBinding
    lateinit var host: String
    val TAG = "FriendFragment"
    val viewmodel2:ChatDBViewModel by activityViewModels()

    companion object {
        fun newInstance() = FriendFragment()
    }

    private lateinit var viewModel: FriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.friend_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FriendViewModel::class.java)

        var list = ArrayList<User>()
        if (commomFunction.currentUser != null) {
            host = commomFunction.getId().toString()
        } else {
            viewmodel2.loadSave().forEach {
                if (!it.id.equals("null")) {
                    host = it.id
                }
            }
        }
        if (commomFunction.checkConnect(requireContext())) {
            viewModel.getAllUser(commomFunction.database, list, host, viewmodel2)

        } else {
            viewModel.getAllUserOff(list, host, viewmodel2)

        }
        viewModel.getAllUser(commomFunction.database, list, host, viewmodel2)
        val friendAdapter = FriendAdapter()
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
}