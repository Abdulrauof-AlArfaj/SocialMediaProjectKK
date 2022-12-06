package com.example.socialmediaprojectkk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.socialmediaprojectkk.API.APIClient
import com.example.socialmediaprojectkk.API.APIinterface
import com.example.socialmediaprojectkk.Adapters.CommentsAdapter
import com.example.socialmediaprojectkk.Data.PostItem
import com.example.socialmediaprojectkk.databinding.ActivityDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityDetailsBinding

    private lateinit var commentList: List<String>

    private lateinit var commentsAdapter: CommentsAdapter

    lateinit var posts:PostItem
    private var primaryKey=0
    //Set username
    private var username:String?=null

    private val apiInterface by lazy { APIClient().getClinet()?.create(APIinterface::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        commentList= listOf()

        commentsAdapter= CommentsAdapter()
        binding.commentsRv.adapter=commentsAdapter
        primaryKey= intent.getIntExtra("id",-1)

        getPost(primaryKey)

        binding.commentBtn.setOnClickListener {
            var comment = binding.commentEt.text.toString()
            if(posts.comments.isNotEmpty()){
                comment = posts.comments + ", ${binding.commentEt.text}"
            }
            if(binding.commentEt.text.isNotEmpty()){
                updatePost(PostItem(comment,posts.id,posts.likes,posts.text,posts.title,posts.user), false)
            }else{
                Toast.makeText(this, "The comment field must not be empty", Toast.LENGTH_LONG).show()
            }

        }

        binding.apply {

            likeBtn.setOnClickListener {
                   var like = username
                   if(posts.likes.isNotEmpty()){
                      like = posts.likes+", $username"
                   }else{

                   }
                  updatePost(PostItem(posts.comments,posts.id,like!!,posts.text,posts.title,posts.user), true)

                }

        }

    }
    private fun getPost(pk:Int){
        if(pk != -1){
            //Get Items from API
            apiInterface!!.getPostDetails(pk).enqueue(object : Callback<PostItem> {
                override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                    posts= response.body()!!
                    binding.apply {
                        titleTv.setText(posts.title)
                        detailsTv.setText(posts.text)
                        commentTv.setText("${CommentHandle(posts.comments)} Comments")
                        likeTv.setText("${LikesHandle(posts.likes)} Likes")
                    }
                    Log.d("response", "onResponse: ${posts} ")
                }
                override fun onFailure(call: Call<PostItem>, t: Throwable) {
                    Log.d("response", "OnFailure: ${t.message}")
                }

            })
        }

    }

    private fun CommentHandle(comments: String): Int {
        if(comments.isNotEmpty()){
            commentList = comments.split(",")
            val commentsList = ArrayList<String>()
            for(comment:String in commentList){
                commentsList.add(comment)
            }
            commentList = commentsList
            commentsAdapter.submitList(commentList)
            binding.commentsRv.smoothScrollToPosition(commentsList.size-1)
            return comments.split(",").size
        }
        return 0
    }

    private fun LikesHandle(likes: String): Int {
        if(likes.isNotEmpty()) {
            return likes.split(",").size
        }
        return 0
    }





    private fun updatePost(post: PostItem, like: Boolean){
        apiInterface?.updatePost(post.id,post)!!.enqueue(object: Callback<PostItem> {
            override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                Log.d("MAIN", "Response: $response")
            }
            override fun onFailure(call: Call<PostItem>, t: Throwable) {
                Toast.makeText(this@DetailsActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                Log.d("Post Details", "onFailure: ${t.message} ")
            }
        })
        if(like){
            binding.likeBtn.setImageResource(R.drawable.like)
            Toast.makeText(this, "Post liked", Toast.LENGTH_LONG).show()
        }else{
            binding.commentEt.text.clear()
            Toast.makeText(this, "Comment added", Toast.LENGTH_LONG).show()
        }
        getPost(post.id)
    }
}