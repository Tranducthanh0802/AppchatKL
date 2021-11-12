package com.example.appchatkl.ui.content.createConversation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.R
import com.example.appchatkl.data.CreateConversation
import com.example.appchatkl.databinding.CreateConversationLayoutAdapterBinding
import com.example.appchatkl.databinding.SelectFriendLayoutAdapterBinding

class SelectFriendAdapter : RecyclerView.Adapter<SelectFriendAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: SelectFriendLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CreateConversation>() {
        override fun areItemsTheSame(
            oldItem: CreateConversation,
            newItem: CreateConversation
        ): Boolean {
            return oldItem.name.equals(newItem.name)
        }
        override fun areContentsTheSame(
            oldItem: CreateConversation,
            newItem: CreateConversation
        ): Boolean {
            return newItem.equals(oldItem)
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var listConversation: List<CreateConversation>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SelectFriendLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: CreateConversation = differ.currentList[position]
        holder.binding.apply {
            create = currentTvShow
        }
    }

    override fun getItemCount() = differ.currentList.size

}
