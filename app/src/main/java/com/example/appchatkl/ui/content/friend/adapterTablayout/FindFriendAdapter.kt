package com.example.appchatkl.ui.content.friend.adapterTablayout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.ConversationLayoutAdapterBinding
import com.example.appchatkl.databinding.FindFriendLayoutAdapterBinding
import com.example.appchatkl.databinding.FindLayoutAdapterBinding

class FindFriendAdapter : RecyclerView.Adapter<FindFriendAdapter.MyViewHolder>(),
    Filterable {
    //  lateinit var list : ArrayList<Conversation>
    val TAG = "FindFriendAdapter"

    inner class MyViewHolder(val binding: FindFriendLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    var countryFilterList = ArrayList<User>()

    fun get() {
        countryFilterList = listConversation
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
    var listConversation = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FindFriendLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: User = listConversation[position]
        holder.binding.apply {
            user = currentTvShow
        }
    }

    override fun getItemCount() = listConversation.size
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.d(TAG, "performFiltering:343 " + countryFilterList.size)
                var resultList = ArrayList<User>()
                if (charSearch.isEmpty()) {
                    resultList = countryFilterList
                } else {
                    for (row in countryFilterList) {
                        Log.d(TAG, "performFiltering:343 " + row.fullName + constraint)
                        if (row.fullName.lowercase().contains(
                                constraint.toString().lowercase()
                            ) && !constraint.toString().equals("null")
                        ) {
                            Log.d(TAG, "performFiltering:343 ")
                            resultList.add(row)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listConversation = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

}
