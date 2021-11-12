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
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.data.db.AppDatabase
import com.example.appchatkl.data.db.ChatDBViewModel
import com.example.appchatkl.databinding.UserFragmentBinding
import com.example.appchatkl.ui.content.user.Edit.EditIfFragment
import com.example.appchatkl.ui.login.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    var id = ""
    lateinit var binding: UserFragmentBinding
    val viewmodel2:ChatDBViewModel by activityViewModels()
    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        id = commomFunction.getId().toString()
        binding.txtEmail.text=commomFunction.getEmail()

        viewModel.getIF(commomFunction.database, id)
        viewModel.user.observe(viewLifecycleOwner, {
            binding.user = it
        })
        binding.lineLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
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