package com.example.appchatkl.ui.content.requestFriend.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.InvitationLayoutAdapterBinding

class InvitationAdapter(val decision: Decision) :
    RecyclerView.Adapter<InvitationAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: InvitationLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


    private val diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.fullName.equals(newItem.fullName)
        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return newItem.equals(oldItem)
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var listConversation: List<User>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            InvitationLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: User = listConversation[position]
        holder.binding.apply {
            user = currentTvShow
        }
        holder.binding.dongy.setOnClickListener {
            decision.onClickYes(currentTvShow.id)
        }
    }

    override fun getItemCount() = listConversation.size

}
