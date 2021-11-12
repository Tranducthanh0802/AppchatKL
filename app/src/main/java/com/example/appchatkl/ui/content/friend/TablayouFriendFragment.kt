package com.example.appchatkl.ui.content.friend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.TablayoutFriendFragmentBinding
import com.example.appchatkl.ui.content.friend.adapterTablayout.FindFriendAdapter
import com.example.appchatkl.ui.content.friend.adapterTablayout.TablayoutAdapter

import com.example.appchatkl.ui.content.user.BottomViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class TablayouFiendFragment : Fragment() {
    lateinit var viewModel: TablaoutViewModel
    val TAG = "TablayouFiendFragment"
    val viewModel1: BottomViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: TablayoutFriendFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.tablayout_friend_fragment, container, false
        )
        viewModel = ViewModelProvider(this).get(TablaoutViewModel::class.java)
        binding.viewpagerTab.adapter = TablayoutAdapter(this)
        TabLayoutMediator(binding.tab, binding.viewpagerTab) { tab, position ->
            when (position) {
                0 -> tab.text = "BẠN BÈ"
                1 -> tab.text = "TẤT CẢ"
                2 -> tab.text = "YÊU CẦU"
            }
        }.attach()
        val id = commomFunction.getId().toString()

        viewModel.getInvitationAndRequest(commomFunction.database, id)
        viewModel.countRecive.observe(viewLifecycleOwner, {
            val count = it.toInt()
            Log.d(TAG, "onCreateView: " + count)
            if (count > 0) {
                binding.tab.getTabAt(2)?.getOrCreateBadge()?.setNumber(count);
            } else {
                binding.tab.getTabAt(2)?.removeBadge();
            }
        })
        val findFriendAdapter: FindFriendAdapter = FindFriendAdapter()
        binding.recBb.apply {
            adapter = findFriendAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        val list = ArrayList<User>()
        viewModel.getFriend(commomFunction.database, list, id)
        viewModel.responseTvShow.observe(viewLifecycleOwner, {
            findFriendAdapter.listConversation = it as ArrayList<User>
            findFriendAdapter.get()
            findFriendAdapter.notifyDataSetChanged()
        })

        viewModel1.edt.observe(viewLifecycleOwner, {
            Log.d(TAG, "onCreateView:tab " + findFriendAdapter.countryFilterList.size)
            if (viewModel1.postion.value == 2 && !it.equals("")) {
                findFriendAdapter.filter.filter(it)
                findFriendAdapter.notifyDataSetChanged()
                binding.lnBb.visibility = View.GONE
                binding.txtbb.visibility = View.VISIBLE
                binding.recBb.visibility = View.VISIBLE
            } else {
                binding.lnBb.visibility = View.VISIBLE
                binding.txtbb.visibility = View.GONE
                binding.recBb.visibility = View.GONE
            }
            if (findFriendAdapter.listConversation.size == 0 && it.length > 0) {
                viewModel1.searchEmpty.value = true
            } else {
                viewModel1.searchEmpty.value = false
            }
        })
        return binding.root
    }
}

