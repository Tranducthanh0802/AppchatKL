package com.example.appchatkl.ui.content.user.Edit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.FragmentEditIfBinding
import com.example.appchatkl.databinding.UserFragmentBinding
import com.example.appchatkl.ui.content.user.UserFragment
import com.example.appchatkl.ui.content.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File


class EditIfFragment : Fragment() {
    var id = ""
    val TAG = "EditIfFragment"
    lateinit var binding: FragmentEditIfBinding
    private lateinit var viewModel: UserViewModel
    var user = User()
    val REQUEST_CODE = 123

    companion object {
        fun newInstance() = EditIfFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_if, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.user = user
        id = commomFunction.getId().toString()
        viewModel.getIF(commomFunction.database, id)
        viewModel.user.observe(viewLifecycleOwner, {
            binding.user = it
            user = it
            user.linkPhoto = it.linkPhoto
        })
        binding.txtSave.setOnClickListener {
            user.fullName = binding.edtFN.text.toString()
            user.phoneNumber = binding.edtPN.text.toString()
            user.date = binding.edtDate.text.toString()
            user.id = id
            commomFunction.database.child("user").child(id).setValue(user)
        }
        binding.camera.setOnClickListener {
            requestPermission(requireContext())
            openGalleryForImage()
        }
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            //imageView.setImageURI(data?.data) // handle chosen image
            val storage = Firebase.storage
            val file: Uri = data?.data!!
            var storageRef =
                FirebaseStorage.getInstance().getReference("" + System.currentTimeMillis())
            storageRef.putFile(file).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    user.linkPhoto = "" + it
                    binding.user = user
                    // database.child("user").child(id).child("linkPhoto").setValue(""+it)
                }

            }

        }
    }

    fun CheckPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(context: Context) {
        var permission = mutableListOf<String>()
        if (!CheckPermission(context)) {
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permission.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, permission.toTypedArray(), 0)
        }
    }
}