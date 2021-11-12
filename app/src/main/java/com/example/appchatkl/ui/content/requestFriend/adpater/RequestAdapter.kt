package com.example.appchatkl.ui.content.requestFriend.adpater

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.R
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.InvitationLayoutAdapterBinding
import com.example.appchatkl.databinding.RequestLayoutAdapterBinding


class RequestAdapter(val decision: Decision) : RecyclerView.Adapter<RequestAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding: RequestLayoutAdapterBinding) :
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
        get() = differ.currentList.sortedBy { it.fullName.toString() }
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RequestLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: User = listConversation[position]
        currentTvShow.section = currentTvShow.fullName.toString().substring(0, 1)
        holder.binding.apply {
            user = currentTvShow
        }
        holder.binding.huy.setOnClickListener {
            decision.onClikNo(currentTvShow.id)
        }
    }
    override fun getItemCount() = listConversation.size

}
