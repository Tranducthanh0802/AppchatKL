package com.example.appchatkl.ui.content.createConversation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.Chat
import com.example.appchatkl.data.CreateConversation
import com.example.appchatkl.data.Message
import com.example.appchatkl.databinding.CreateConversationFragmentBinding
import com.example.appchatkl.ui.content.createConversation.adapter.CreateConversationAdapter
import com.example.appchatkl.ui.content.createConversation.adapter.SelectFriendAdapter
import com.example.appchatkl.ui.content.createConversation.adapter.onclick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class CreateConversationFragment : Fragment(), onclick {
    var TAG = "CreateConversationFragment"

    companion object {
        fun newInstance() = CreateConversationFragment()
    }

    private lateinit var viewModel: CreateConversationViewModel
    private lateinit var createConversationAdapter: CreateConversationAdapter
    private lateinit var selectFriendAdapter: SelectFriendAdapter
    private lateinit var binding: CreateConversationFragmentBinding
    private val list = ArrayList<CreateConversation>()
    private var s: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.create_conversation_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateConversationViewModel::class.java)
        createConversationAdapter = CreateConversationAdapter(this)
        selectFriendAdapter = SelectFriendAdapter()

        var list = ArrayList<CreateConversation>()
        val id = commomFunction.getId().toString()
        viewModel.getAllUser(commomFunction.database, list, id)
        binding.recyclerview.apply {
            adapter = createConversationAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.responseTvShow.observe(viewLifecycleOwner, { listTvShows ->
            createConversationAdapter.listConversation = listTvShows
            binding.recyclerview.adapter?.notifyDataSetChanged()
        })
        binding.recyclerview2.apply {
            adapter = selectFriendAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.selectUser.observe(viewLifecycleOwner, { listTvShows ->
            selectFriendAdapter.listConversation = listTvShows as ArrayList<CreateConversation>
            binding.recyclerview2.adapter?.notifyDataSetChanged()
        })
        binding.btnOk.setOnClickListener {
            s += id + ","
            Log.d(TAG, "onActivityCreated: " + s)
            selectFriendAdapter.listConversation.forEach {
                s += it.id + ","
                Log.d(TAG, "onActivityCreated2: " + s)
            }
            viewModel.check(commomFunction.database, s, Chat())
            val controller = findNavController()
            val bundle = bundleOf("id" to s)
            controller.navigate(R.id.chatFragment, bundle)
        }
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.txtHuy.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun select(name: CreateConversation) {
        list.add(name)
        viewModel.getListSelect(list)
    }

    override fun minus(conversation: CreateConversation) {
        list.remove(conversation)
        viewModel.getListSelect(list)
    }

    override fun onStart() {
        super.onStart()
        list.clear()
        s = ""
        viewModel.getListSelect(list)
    }
}