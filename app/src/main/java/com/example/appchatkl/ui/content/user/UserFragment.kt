package com.example.appchatkl.ui.content.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.appchatkl.R
import com.example.appchatkl.CommomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.databinding.UserFragmentBinding
import com.example.appchatkl.ui.content.user.edit.EditIfFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    var id = ""
    lateinit var binding: UserFragmentBinding
    private val viewmodel2:ChatDBViewModel by activityViewModels()
    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.user_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val user = User()
        binding.user = user
        id = CommomFunction.getId()
        binding.txtEmail.text=CommomFunction.getEmail()

        viewModel.getIF(CommomFunction.database, id)
        viewModel.user.observe(viewLifecycleOwner, {
            binding.user = it
        })
        binding.lineLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.finish()
            viewmodel2.deleteSave()
        }
        binding.edit.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.framentActivity, EditIfFragment.newInstance())
            transaction.addToBackStack("")
            transaction.commit()
        }
    }

}