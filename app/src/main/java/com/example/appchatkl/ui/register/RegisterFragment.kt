package com.example.appchatkl.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.appchatkl.R
import com.example.appchatkl.data.Friend
import com.example.appchatkl.data.Request
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.RegisterFragmentBinding


class RegisterFragment : Fragment() {
    lateinit var binding: RegisterFragmentBinding
    val TAG = "register"

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.register_fragment, container, false
        )
        val view: View = binding.root
        binding.lifecycleOwner = this

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding.register = viewModel

        val user = User()
        val friend = Friend()
        val request = Request()
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            viewModel.register( user, friend, request)
        }

        // TODO: Use the ViewModel
    }


}