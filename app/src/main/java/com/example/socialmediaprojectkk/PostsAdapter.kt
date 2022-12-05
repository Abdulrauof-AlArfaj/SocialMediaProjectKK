package com.example.socialmediaproject

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaprojectkk.DetailsActivity
import com.example.socialmediaprojectkk.databinding.PostsRowBinding

class PostsAdapter(private var context : Context) :
    ListAdapter<PostItem,PostsAdapter.ItemViewHolder>(PostDiffUtill()) {

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
        var likesCounter=0
        var commentsCounter=0

        holder.binding.apply {
            nameTv.text=getItem(position).title
            //Split likes string and store it into list
            var likeList = getItem(position).likes.split(",")
            //Split comments string and store it into list
            var commentList = getItem(position).comments.split(",")
            //For loop to calculate how many likes the user got
            for(i in likeList){
                if(i!=""){
                    likesCounter++
                }else{
                    Log.d("MainActivity", "likeList: $likeList")
                }
            }
            //For loop to calculate how many comments the user got
            for (j in commentList){
                if(j!=""){
                    commentsCounter++
                }else{
                    Log.d("MainActivity", "likeList: $commentList")
                }
            }
            commentTv.text = "$commentsCounter Comments  "
            likesTv.text = "$likesCounter Likes"
            pk = getItem(position).id
            namePlace.setOnClickListener {
                var postDetailsIntent= Intent(context,DetailsActivity::class.java)
                postDetailsIntent.putExtra("id",pk)
                context.startActivity(postDetailsIntent)
            }
        }
    }
}