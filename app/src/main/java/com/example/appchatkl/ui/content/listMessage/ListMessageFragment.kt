package com.example.appchatkl.ui.content.listMessage

import android.content.Context
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
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.data.db.AppDatabase
import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.databinding.ListMessageFragmentBinding
import com.example.appchatkl.ui.content.listMessage.adapter.ConversationAdapter
import com.example.appchatkl.ui.content.listMessage.adapter.SendData
import com.example.appchatkl.ui.content.listMessage.adapter.onClick
import com.example.appchatkl.ui.content.user.BottomViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.log
import kotlin.math.sign
@AndroidEntryPoint
class ListMessageFragment() : Fragment(), onClick {
    var id = ""
    var TAG = "ListMessageFragment"
    val viewModel2:ChatDBViewModel by activityViewModels()
    var conversationAdapter: ConversationAdapter = ConversationAdapter(this)
    private lateinit var binding: ListMessageFragmentBinding
    private lateinit var sendData: SendData
    private lateinit var viewModel: ListMessageViewModel
    val viewModel1: BottomViewModel by activityViewModels()

    companion object {
        fun newInstance() = ListMessageFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.list_message_fragment, container, false
        )
        sendData = activity as SendData
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListMessageViewModel::class.java)
        //  val viewModel1=ViewModelProvider(this).get(BottomViewModel::class.java)


        if (commomFunction.currentUser != null) {
            id = commomFunction.getId().toString()
            Log.d(TAG, "onActivityCreated:ádfa " + id)
        } else {
            viewModel2.loadSave().forEach {
                if (!it.id.equals("null")) {
                    id = it.id
                }
            }
        }
        val list = ArrayList<Conversation>()
        binding.recyclListChat.apply {
            adapter = conversationAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        if (commomFunction.checkConnect(requireContext())) {
            viewModel.getChat(commomFunction.database, list, id, viewModel2)
        } else {
            viewModel.getChatOff(list, id, viewModel2)
        }
        viewModel.conversation.observe(viewLifecycleOwner, {
            conversationAdapter.listConversation = it as ArrayList<Conversation>
            conversationAdapter.get()
            conversationAdapter.notifyDataSetChanged()
        })
        viewModel1.postion.value = 1

        viewModel1.edt.observe(viewLifecycleOwner, {
            if (viewModel1.postion.value == 1 && !it.equals("")) {
                Log.d(TAG, "onActivityCreated:3434 " + it)
                val a = conversationAdapter.filter.filter(it)
                conversationAdapter.notifyDataSetChanged()
                if (conversationAdapter.listConversation.size == 0 && it.length > 0) {
                    viewModel1.searchEmpty.value = true
                } else {
                    viewModel1.searchEmpty.value = false
                }
            }
        })
        // TODO: Use the ViewModel
    }

    override fun openChat(s: String) {
        sendData.send(s)

    }


}