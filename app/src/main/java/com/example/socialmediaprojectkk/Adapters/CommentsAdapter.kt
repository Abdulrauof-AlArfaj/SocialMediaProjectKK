package com.example.socialmediaprojectkk.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaprojectkk.Data.PostItem
import com.example.socialmediaprojectkk.databinding.CommentsRowBinding

class CommentsAdapter:
    ListAdapter<String, CommentsAdapter.ItemViewHolder>(CommentsDiffUtill()) {

    class ItemViewHolder(var binding: CommentsRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CommentsRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var comment=getItem(position)
        holder.binding.apply {
            if(comment!=""){
                commentsTv.text=comment
            }
        }
    }




}