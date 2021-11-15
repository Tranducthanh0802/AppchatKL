package com.example.appchatkl.ui.content.friend.friend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.CommomFunction
import com.example.appchatkl.data.User

import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.databinding.FriendFragmentBinding
import com.example.appchatkl.ui.content.friend.friend.adapter.FriendAdapter

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendFragment : Fragment() {
    lateinit var binding: FriendFragmentBinding
    lateinit var host: String
    val TAG = "FriendFragment"
    private val viewmodel2:ChatDBViewModel by activityViewModels()

    companion object {
        fun newInstance() = FriendFragment()
    }

    private lateinit var viewModel: FriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.friend_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FriendViewModel::class.java)

        val list = ArrayList<User>()
        if (CommomFunction.currentUser != null) {
            host = CommomFunction.getId()
        } else {
            viewmodel2.loadSave().forEach {
                if (it.id!=("null")) {
                    host = it.id
                }
            }
        }
        if (CommomFunction.checkConnect(requireContext())) {
            viewModel.getAllUser(CommomFunction.database, list, host, viewmodel2)

        } else {
            viewModel.getAllUserOff(list, viewmodel2)

        }
        viewModel.getAllUser(CommomFunction.database, list, host, viewmodel2)
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