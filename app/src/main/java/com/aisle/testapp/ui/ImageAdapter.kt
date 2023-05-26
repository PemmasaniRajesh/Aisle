package com.aisle.testapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aisle.testapp.data.Profile
import com.aisle.testapp.databinding.AdapterImageItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

class ImageAdapter(private val profilesList: List<Profile>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: AdapterImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: Profile) = binding.apply {
            this.tvName.text = profile.first_name
            Glide.with(context).load(profile.avatar)
                .transform(BlurTransformation(25,5))
                .into(this.ivPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            AdapterImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return profilesList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(profilesList[position])
    }
}