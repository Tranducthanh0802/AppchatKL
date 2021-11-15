package com.example.appchatkl.ui.content.listMessage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable

import androidx.recyclerview.widget.RecyclerView
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.ConversationLayoutAdapterBinding



class ConversationAdapter(private val open: OnClick) :
    RecyclerView.Adapter<ConversationAdapter.MyViewHolder>(),
    Filterable {
    //  lateinit var list : ArrayList<Conversation>
    val TAG = "ConversationAdapter"

    inner class MyViewHolder(val binding: ConversationLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    var countryFilterList = ArrayList<Conversation>()

    fun get() {
        countryFilterList = listConversation
    }



    var listConversation = ArrayList<Conversation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ConversationLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTvShow: Conversation = listConversation[position]
        holder.binding.apply {
            conversation = currentTvShow
        }
        holder.binding.Linear.setOnClickListener {
            open.openChat(currentTvShow.message.id)
        }
    }

    override fun getItemCount() = listConversation.size
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                var resultList = ArrayList<Conversation>()
                Log.d(TAG, "performFiltering: " + charSearch + countryFilterList.size)
                if (charSearch.isEmpty()) {
                    countryFilterList.forEach {
                        it.isFind = false
                    }
                    Log.d(TAG, "performFiltering: 12")
                    resultList = countryFilterList
                } else {
                    for (row in countryFilterList) {
                        var count = 0
                        row.listMessage.forEach {
                            if (it.lowercase().contains(
                                    constraint.toString().lowercase()
                                ) && constraint.toString()!=("null")
                            ) {
                                count += 1
                            }
                        }
                        if (count > 0 && constraint?.length!! > 0) {
                            row.notification = count.toString() + " tin nhắn phù hợp"
                            row.isFind = true
                            resultList.add(row)
                        } else {
                            row.isFind = false
                        }
                    }

                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listConversation = results?.values as ArrayList<Conversation>
                notifyDataSetChanged()
            }
        }
    }

}
