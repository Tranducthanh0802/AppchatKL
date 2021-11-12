package com.example.appchatkl

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:imageUri")
fun loadImage(imageView: ImageView, imgaeUrl: String) {
    Glide.with(imageView.getContext()).load(imgaeUrl).placeholder(R.drawable.personal1)
        .into(imageView);
}