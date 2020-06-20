package com.angelicao.repository.data

import androidx.recyclerview.widget.DiffUtil

data class Gif(val id: String, val url: String, val title: String, var favorite: Boolean = false)

val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Gif>() {
    override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean =
        oldItem == newItem
}