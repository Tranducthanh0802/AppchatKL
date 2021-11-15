package com.example.appchatkl.ui.content.chat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appchatkl.R
import com.example.appchatkl.CommomFunction

import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.databinding.ChatFragmentBinding

import com.example.appchatkl.ui.content.chat.adapter.ChatAdapter
import com.example.appchatkl.ui.content.listMessage.ListMessageViewModel

import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChatFragment : Fragment() {
    val TAG = "ChatFragment"
    private var host = ""
    private lateinit var binding: ChatFragmentBinding


    companion object {
        fun newInstance() = ChatFragment()
    }
    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelLM: ListMessageViewModel
    private val viewModel2:ChatDBViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.chat_fragment, container, false
        )
        return binding.root
    }

    var list = ArrayList<com.example.appchatkl.data.Message>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        viewModelLM = ViewModelProvider(this).get(ListMessageViewModel::class.java)

        binding.chat = viewModel

        if (CommomFunction.currentUser != null) {
            host = CommomFunction.getId()
        } else {
            viewModel2.loadSave().forEach {
                if (it.id!=("null")) {
                    host = it.id
                }
            }

        }
        val adapterBinding = ChatAdapter(host)
        binding.recyclerviewmessage.apply {
            adapter = adapterBinding
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
        }
        val id = arguments?.getString("id").toString()
        //   databse.child("conversation").child(id).child("idSee").setValue(host)
        adapterBinding.listConversation
        if (CommomFunction.checkConnect(requireContext())) {
            viewModel.takeMessage(id, CommomFunction.database, list, host, viewModel2)
        } else {
            viewModel.takeMessageOff(id, list, host, viewModel2)
        }
        viewModel.takeMessage(id, CommomFunction.database, list, host, viewModel2)
        viewModel.list.observe(viewLifecycleOwner, {
            adapterBinding.listConversation = it
            adapterBinding.notifyDataSetChanged()
        })
        viewModel.max.observe(viewLifecycleOwner, {
            binding.recyclerviewmessage.smoothScrollToPosition(
                it
            )
        })
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        viewModel.name.observe(viewLifecycleOwner, {
            binding.namefull.text = it
        })
        viewModel.avata.observe(viewLifecycleOwner, {
            Glide.with(this).load(it).placeholder(R.drawable.personal1).into(binding.imgAvataTop)
        })
        binding.edtInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.recyclerviewmessage.layoutManager?.scrollToPosition(
                    viewModel.max.value!!
                )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s == null) {
                    binding.imgSend.visibility = View.GONE
                } else {
                    binding.imgSend.visibility = View.VISIBLE
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.imgSend.setOnClickListener {
            viewModel.send(
                binding.edtInput.text.toString(),
                CommomFunction.database,
                viewModel.idName.value.toString(),
                host,
                viewModel2
            )
        }
        viewModel.message.observe(viewLifecycleOwner, {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())
            val s = it + "@@@" + host + "@@@@" + currentDate + "@@@@@"
            CommomFunction.database.child("conversation").child(viewModel.idName.value.toString()).child("message")
                .setValue(s)
        })
        viewModel.count.observe(viewLifecycleOwner, {
            CommomFunction.database.child("conversation").child(viewModel.idName.value.toString()).child("count")
                .setValue(it.toString())
        })
    }
}