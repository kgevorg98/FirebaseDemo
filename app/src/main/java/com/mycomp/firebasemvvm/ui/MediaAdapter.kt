package com.mycomp.firebasemvvm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mycomp.firebasemvvm.databinding.ItemMediaBinding
import com.mycomp.firebasemvvm.domain.model.Media
import com.mycomp.firebasemvvm.ui.utils.MediaDiffUtil

class MediaAdapter : ListAdapter<Media, MediaAdapter.MediaViewHolder>(MediaDiffUtil) {


    inner class MediaViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Media) {
            with(binding) {
                title.text = item.name
                size.text = item.size
                Glide.with(itemView)
                    .load(item.uri)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
       val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = getItem(position)
        holder.bind(media)
    }
}