package com.example.appchatkl.ui.content


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.example.appchatkl.R
import com.example.appchatkl.CommomFunction
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.BottomFragmentBinding


import com.example.appchatkl.ui.content.user.BottomViewModel
import com.example.appchatkl.ui.content.user.Find


private const val FIND_MESSAGE = 1
private const val FIND_FRIEND = 2

class BottomFragment : Fragment() {

    val TAG = "BottomFragment"
    lateinit var find: Find

    //lateinit var controller:NavController
    val viewModel: BottomViewModel by activityViewModels()

    companion object {
        fun newInstance() = BottomFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: BottomFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_fragment, container, false
        )
        val view: View = binding.root
        // viewModel = ViewModelProvider(this).get(BottomViewModel::class.java)
        binding.lifecycleOwner = this
        val loginDialogContainer =
            childFragmentManager.findFragmentById(R.id.frag) as NavHostFragment
        val loginNavController: NavController = loginDialogContainer.navController
        binding.imgCreateMess.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.createConversationFragment)
        }
        binding.bottomNavigation.setupWithNavController(loginNavController)
        binding.bottomNavigation.setOnClickListener {
            binding.linear1.visibility = View.GONE
            binding.viewTop.visibility = View.GONE
        }
        loginNavController.addOnDestinationChangedListener { _, destination, _ ->
            binding.edtSearch.setQuery("", false)
            if (destination.id == R.id.userFragment) {
                binding.linear1.visibility = View.GONE
                binding.viewTop.visibility = View.GONE

            } else {
                binding.linear1.visibility = View.VISIBLE
                binding.viewTop.visibility = View.VISIBLE
                if (destination.id == R.id.listMessageFragment) viewModel.postion.value =
                    FIND_MESSAGE
                else viewModel.postion.value = FIND_FRIEND
            }
        }

        val id = CommomFunction.getId()

        //notifical
        val list = ArrayList<Conversation>()
        viewModel.getChat(CommomFunction.database, list, id)
        viewModel.count.observe(viewLifecycleOwner, {
            val count = it.toInt()
            if (count > 0) {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.listMessageFragment).apply {
                        isVisible = true
                        number = count
                    }
            } else {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.listMessageFragment).apply {
                        isVisible = false
                        number = count
                    }
            }
        })
        viewModel.getInvitationAndRequest(CommomFunction.database, id)
        viewModel.countRecive.observe(viewLifecycleOwner, {
            val count = it.toInt()
            if (count > 0) {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.tablayouFiendFragment).apply {
                        isVisible = true
                        number = count
                    }
            } else {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.tablayouFiendFragment).apply {
                        isVisible = false
                        number = count
                    }
            }
        })
        ///find

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.edt.value = newText
                }
                return true
            }
        })
        //searchEmpty
        viewModel.searchEmpty.observe(viewLifecycleOwner, {
            if (it) {
                Log.d(TAG, "onCreateView:23 ")
                binding.searhempty.visibility = View.VISIBLE
            } else {
                binding.searhempty.visibility = View.GONE
            }
        })
        // Log.d(TAG, "onCreateView: "+binding.bottomNavigation.selectedItemId)
        return view
    }


}