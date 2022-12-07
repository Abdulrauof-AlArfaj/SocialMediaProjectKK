package com.example.socialmediaprojectkk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.socialmediaprojectkk.API.APIClient
import com.example.socialmediaprojectkk.API.APIinterface
import com.example.socialmediaprojectkk.Adapters.CommentsAdapter
import com.example.socialmediaprojectkk.Data.PostItem
import com.example.socialmediaprojectkk.Data.UserItem
import com.example.socialmediaprojectkk.Data.UserKey
import com.example.socialmediaprojectkk.databinding.ActivityDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityDetailsBinding

    private lateinit var commentsAdapter: CommentsAdapter

    private lateinit var commentList: List<String>//split comments
    private lateinit var likeList: List<String>//split likes
    lateinit var likesList:ArrayList<String>//trim likes
    lateinit var userData: UserItem

    lateinit var posts:PostItem
    private var primaryKey=0
    //Set username
    private var username=""
    var API= UserKey.API.publickApiKey
    lateinit var context: Context

    private val apiInterface by lazy { APIClient().getClinet()?.create(APIinterface::class.java) }

    /*<!--------------------------------------------------------------------------------------------------!>*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        commentList= listOf()
        likeList= listOf()
        likesList= arrayListOf()

        commentsAdapter= CommentsAdapter()
        binding.commentsRv.adapter=commentsAdapter
        primaryKey= intent.getIntExtra("id",-1)
        context=this@DetailsActivity

        getPost(primaryKey)

        if(API.length>12){//That means the user sign in
            //*********************************************************************************************
            //Get user name
            var apiInterface = APIClient().getClinet()?.create(APIinterface::class.java)
            apiInterface!!.getUserData(API)?.enqueue(object :Callback<UserItem> {
                override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                    if(response.body()!=null){
                        userData=response.body()!!
                        binding.apply {
                            username=userData.username
                        }//End of binding
                    }//end if response != null
                }
                override fun onFailure(call: Call<UserItem>, t: Throwable) {
                    Toast.makeText(this@DetailsActivity, "Sorry, Something goes wrong", Toast.LENGTH_SHORT).show()
                }
            })
            //******************************************************
            binding.apply {
                likeBtn.visibility= View.VISIBLE
                commentEt.visibility= View.VISIBLE
                commentBtn.visibility= View.VISIBLE
            }
        }//End  if(API.length>12)
        else{//That means the user not sign in, Therefore disable comment & like
            binding.apply {
                likeBtn.visibility= View.INVISIBLE
                commentEt.visibility= View.INVISIBLE
                commentBtn.visibility= View.INVISIBLE
            }
        }//End of else

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

        }//End of  binding.commentBtn.setOnClickListener
        //----------------------------------------------------------------------

        binding.apply {

            likeBtn.setOnClickListener {
                //List to split likes name
                likeList = posts.likes.trim().split(",")

                //CLEAR ARRAY AND ADD THE NEW USERS NAMES THAT LIKED THE POST
                likesList.clear()
                likesList.addAll(likeList)

                Log.d("LikesList", "Remove: $likesList")
                if (likesList.contains(username)) {
                    //remove user from list
                    likesList.remove(username)
                    //convert Array elements to string
                    var likes = likesList.joinToString(",")
                    posts.likes = likes
                    binding.likeBtn.setImageResource(R.drawable.like1)
                    Toast.makeText(this@DetailsActivity, "Post Unliked", Toast.LENGTH_SHORT).show()
                    updatePost(posts, false)
                } else {
                    var like = username
                    if (posts.likes.isNotEmpty()) {
                        like = posts.likes + ",$username"
                    }
                    updatePost(
                        PostItem(
                            posts.comments,
                            posts.id,
                            like!!,
                            posts.text,
                            posts.title,
                            posts.user
                        ), true
                    )
                }
            }
            homeBtn.setOnClickListener {
                var MainIntent= Intent(this@DetailsActivity,MainActivity::class.java)
                startActivity(MainIntent)
                finish()
            }
        }

    }//End on create
    /*<!--------------------------------------------------------------------------------------------------!>*/



    //Get Content of the Post
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

    //Calculate the size of comment split list
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
    //Calculate the size of likes split list
    private fun LikesHandle(likes: String): Int {
        if(likes.isNotEmpty()) {
            return likes.split(",").size
        }
        return 0
    }

    //Update Post either comment or like
    private fun updatePost(post: PostItem, like: Boolean){
        apiInterface?.updatePost(post.id,post)!!.enqueue(object: Callback<PostItem> {
            override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                Log.d("MAIN", "Response: $response")
            }
            override fun onFailure(call: Call<PostItem>, t: Throwable) {
                Toast.makeText(this@DetailsActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                Log.d("Post Details", "onFailure: ${t.message} ")
            }
        })
        if(like){
            binding.likeBtn.setImageResource(R.drawable.like)
            Toast.makeText(this, "Post liked", Toast.LENGTH_SHORT).show()
        }else if(binding.commentEt.text.isNotEmpty()){
            binding.commentEt.text.clear()
            Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show()
        }
        getPost(post.id)
    }
}