package com.example.appchatkl.ui.content.createConversation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.CreateConversation
import com.example.appchatkl.databinding.CreateConversationLayoutAdapterBinding


class CreateConversationAdapter(val onclick: Onclick) :
    RecyclerView.Adapter<CreateConversationAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: CreateConversationLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    private val diffCallback = object : DiffUtil.ItemCallback<CreateConversation>() {
        override fun areItemsTheSame(
            oldItem: CreateConversation,
            newItem: CreateConversation
        ): Boolean {
            return oldItem.name==(newItem.name)
        }
        override fun areContentsTheSame(
            oldItem: CreateConversation,
            newItem: CreateConversation
        ): Boolean {
            return newItem == (oldItem)
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
            CreateConversationLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: CreateConversation = listConversation[position]
        holder.binding.apply {
            create = currentTvShow
        }
        holder.binding.checkbox.setOnClickListener {
            if (currentTvShow.isCheck) {
                onclick.select(currentTvShow)
            } else onclick.minus(currentTvShow)
        }
    }

    override fun getItemCount() = listConversation.size

}
