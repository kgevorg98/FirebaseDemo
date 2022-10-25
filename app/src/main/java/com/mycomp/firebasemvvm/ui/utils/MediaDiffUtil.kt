package com.mycomp.firebasemvvm.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.mycomp.firebasemvvm.domain.model.Media

object MediaDiffUtil : DiffUtil.ItemCallback<Media>() {
    override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem.id == newItem.id && oldItem.uri == newItem.uri && oldItem.name == newItem.name
    }
}