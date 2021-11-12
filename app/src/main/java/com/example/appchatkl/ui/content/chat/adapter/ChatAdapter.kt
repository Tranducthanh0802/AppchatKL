package com.example.appchatkl.ui.content.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.appchatkl.R
import com.example.appchatkl.data.Message
import com.example.appchatkl.databinding.*
import com.example.appchatkl.ui.content.createConversation.adapter.CreateConversationAdapter
import kotlin.math.log

private const val messageLeft = 1
private const val messageRight = 2
private const val imageLeft = 3
private const val imageRight = 4
private const val TAG = "ChatAdapter"

class ChatAdapter(val id: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id.equals(newItem.id)
        }
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return newItem.equals(oldItem)
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var listConversation: List<Message>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    inner class MyViewHolder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val currentTvShow: Message = listConversation[position]
            if (binding is ItemMessageImageLeftBinding)
                binding.message = currentTvShow
            else if (binding is ItemMessageImageRightBinding)
                binding.message = currentTvShow
            else if (binding is ItemMessageLeftBinding) {
                binding.message = currentTvShow
                Log.d(TAG, "bind: " + currentTvShow.avata)
            } else if (binding is ItemMessageRightBinding)
                binding.message = currentTvShow
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (viewType == messageLeft) {
            return MyViewHolder(
                ItemMessageLeftBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else if (viewType == messageRight) {
            return MyViewHolder(
                ItemMessageRightBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else if (viewType == imageRight) {
            return MyViewHolder(
                ItemMessageImageRightBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            return MyViewHolder(
                ItemMessageImageLeftBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentTvShow: Message = listConversation[position]
        (holder as MyViewHolder).bind(position)
    }

    override fun getItemCount() = listConversation.size

    override fun getItemViewType(position: Int): Int {
        if (listConversation.get(position).id.equals(id)) {
            if (listConversation.get(position).isImage) {
                return imageRight;
            } else return messageRight;
        } else {
            if (listConversation.get(position).isImage) {
                return imageLeft;
            } else return messageLeft;
        }
    }
}
