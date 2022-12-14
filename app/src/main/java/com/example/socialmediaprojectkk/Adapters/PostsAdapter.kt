package com.example.socialmediaprojectkk.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaprojectkk.Data.PostItem
import com.example.socialmediaprojectkk.DetailsActivity
import com.example.socialmediaprojectkk.databinding.PostsRowBinding

class PostsAdapter(private var context : Context) :
    ListAdapter<PostItem, PostsAdapter.ItemViewHolder>(PostDiffUtill()) {

    class ItemViewHolder(var binding: PostsRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            PostsRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var pk = 0
        holder.binding.apply {
            nameTv.text=getItem(position).title
            //Split comments string and store it into list
            commentTv.text = "${getItem(position).comments.split(",").size} Comments  "
            //Split likes string and store it into list
            likesTv.text = "${getItem(position).likes.split(",").size} Likes"
            pk = getItem(position).id
            namePlace.setOnClickListener {
                var postDetailsIntent= Intent(context,DetailsActivity::class.java)
                postDetailsIntent.putExtra("id",pk)
                context.startActivity(postDetailsIntent)
            }
        }
    }
}