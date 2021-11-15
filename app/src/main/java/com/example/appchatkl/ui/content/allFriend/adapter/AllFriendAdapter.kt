package com.example.appchatkl.ui.content.allFriend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.AllFriendLayoutAdapterBinding


class AllFriendAdapter(val addFriend: AddFriend) :
    RecyclerView.Adapter<AllFriendAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: AllFriendLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

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
        get() = differ.currentList.sortedBy { it.fullName }
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AllFriendLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: User = listConversation[position]
        currentTvShow.section = currentTvShow.fullName.substring(0, 1)
        holder.binding.apply {
            user = currentTvShow
        }
        if (position > 0) {
            val i = position - 1
            if (i < listConversation.size && currentTvShow.fullName.substring(0, 1)
                    .equals(listConversation.get(i).fullName.substring(0, 1))
            ) {
                holder.binding.mSection.visibility = View.GONE
            }
        }
        holder.binding.ketban.setOnClickListener {
            addFriend.onclick(currentTvShow.id)
            // currentTvShow.isFriend=true
        }
    }

    override fun getItemCount() = listConversation.size

}
