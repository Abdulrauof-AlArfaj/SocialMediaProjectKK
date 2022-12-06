package com.example.socialmediaprojectkk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.socialmediaprojectkk.API.APIClient
import com.example.socialmediaprojectkk.API.APIinterface
import com.example.socialmediaprojectkk.Data.Post
import com.example.socialmediaprojectkk.Adapters.PostsAdapter
import com.example.socialmediaprojectkk.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//----------------------------------------------------------------------
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var context:Context
    var ApiKey=""
    var postAdapter = PostsAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postsRv.adapter = postAdapter
        context=this@MainActivity
        //Get API_Key:
        ApiKey= intent.getStringExtra("API_Key").toString()
        //==========================================================

        var apiInterface = APIClient().getClinet()?.create(APIinterface::class.java)

        //Get Items from API
        apiInterface!!.getPosts()?.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                var posts = response.body()!!
                postAdapter.submitList(posts)
                Log.d("response", "onResponse: ${posts} ")
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("response", "OnFailure: ${t.message}")
            }

        })

        binding.apply {
            addPostBtn.setOnClickListener {
                var addpostIntent = Intent(this@MainActivity, NewPostActivity::class.java)
                startActivity(addpostIntent)
            }
            sinInBtn.setOnClickListener {
                var intent= Intent(context,LoginActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}