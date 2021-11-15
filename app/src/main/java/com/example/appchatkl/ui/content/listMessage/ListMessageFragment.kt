package com.example.appchatkl.ui.content.listMessage


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
import com.example.appchatkl.CommomFunction
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.databinding.ListMessageFragmentBinding
import com.example.appchatkl.ui.content.listMessage.adapter.ConversationAdapter
import com.example.appchatkl.ui.content.listMessage.adapter.SendData
import com.example.appchatkl.ui.content.listMessage.adapter.OnClick
import com.example.appchatkl.ui.content.user.BottomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListMessageFragment : Fragment(), OnClick {
    var id = ""
    var TAG = "ListMessageFragment"
    private val viewModel2:ChatDBViewModel by activityViewModels()
    private var conversationAdapter: ConversationAdapter = ConversationAdapter(this)
    private lateinit var binding: ListMessageFragmentBinding
    private lateinit var sendData: SendData
    private lateinit var viewModel: ListMessageViewModel
    private val viewModel1: BottomViewModel by activityViewModels()

    companion object {
        fun newInstance() = ListMessageFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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


        if (CommomFunction.currentUser != null) {
            id = CommomFunction.getId()
        } else {
            viewModel2.loadSave().forEach {
                if (it.id!=("null")) {
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
        if (CommomFunction.checkConnect(requireContext())) {
            viewModel.getChat(CommomFunction.database, list, id, viewModel2)
        } else {
            viewModel.getChatOff(list, viewModel2)
        }
        viewModel.conversation.observe(viewLifecycleOwner, {
            conversationAdapter.listConversation = it as ArrayList<Conversation>
            conversationAdapter.get()
            conversationAdapter.notifyDataSetChanged()
        })
        viewModel1.postion.value = 1

        viewModel1.edt.observe(viewLifecycleOwner, {
            if (viewModel1.postion.value == 1 && !it.equals("")) {
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