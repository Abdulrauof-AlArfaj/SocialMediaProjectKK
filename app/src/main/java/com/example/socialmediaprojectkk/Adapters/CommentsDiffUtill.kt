package com.example.socialmediaprojectkk.Adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.socialmediaprojectkk.Data.PostItem

class CommentsDiffUtill : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return when{
            oldItem==newItem -> true
            else -> {false}
        }

    }
}