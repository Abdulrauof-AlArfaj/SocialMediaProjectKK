package com.example.socialmediaprojectkk.Adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.socialmediaprojectkk.Data.PostItem

class PostDiffUtill: DiffUtil.ItemCallback<PostItem>() {
    override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
        return when{
            oldItem.id==newItem.id -> true
            oldItem.title==newItem.title-> true
            oldItem.likes==newItem.likes-> true
            oldItem.user==newItem.user-> true
            oldItem.text==newItem.text-> true
            oldItem.comments==newItem.comments-> true
            else -> {false}
        }

    }

}