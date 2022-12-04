package com.example.socialmediaprojectkk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.socialmediaproject.APIClient
import com.example.socialmediaproject.APIinterface
import com.example.socialmediaproject.PostItem
import com.example.socialmediaprojectkk.databinding.ActivityDetailsBinding
import com.example.socialmediaprojectkk.databinding.ActivityDetailsBinding.inflate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getItem()


    }
    fun getItem(){
        var primaryKey= intent.getIntExtra("id",-1)

        if(primaryKey != -1){
            var apiInterface= APIClient().getClinet()?.create(APIinterface::class.java)
            //Get Items from API
            apiInterface!!.getPost(primaryKey).enqueue(object : Callback<PostItem> {
                override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                    var  posts= response.body()!!
                    binding.apply {
                        titleTv.setText(posts.title)
                        detailsTv.setText(posts.text)
                        //Split likes string and store it into list
                        var likeList = posts.likes.split(",")
                        //Split comments string and store it into list
                        var commentList = posts.comments.split(",")

                        commentTv.setText("${commentList.size} Comments")
                        likeTv.setText("${likeList.size} Likes")
                    }


                    Log.d("response", "onResponse: ${posts} ")
                }
                override fun onFailure(call: Call<PostItem>, t: Throwable) {
                    Log.d("response", "OnFailure: ${t.message}")
                }

            })
        }
    }
}