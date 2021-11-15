package com.example.appchatkl.ui.content.friend.adapterTablayout

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appchatkl.ui.content.allFriend.AllFriendFragment
import com.example.appchatkl.ui.content.friend.friend.FriendFragment
import com.example.appchatkl.ui.content.requestFriend.RequestFriendFragment

class TablayoutAdapter( fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FriendFragment()
            1 -> AllFriendFragment()
            else -> RequestFriendFragment()
        }
    }

}