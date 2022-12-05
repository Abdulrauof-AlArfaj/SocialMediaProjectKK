package com.example.socialmediaprojectkk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.socialmediaproject.APIClient
import com.example.socialmediaproject.APIinterface
import com.example.socialmediaproject.Post
import com.example.socialmediaproject.PostsAdapter
import com.example.socialmediaprojectkk.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var postAdapter = PostsAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postsRv.adapter = postAdapter

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
        }
    }
}