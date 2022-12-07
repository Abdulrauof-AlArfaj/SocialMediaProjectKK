package com.example.socialmediaprojectkk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.socialmediaprojectkk.API.APIClient
import com.example.socialmediaprojectkk.API.APIinterface
import com.example.socialmediaprojectkk.Data.Post
import com.example.socialmediaprojectkk.Adapters.PostsAdapter
import com.example.socialmediaprojectkk.Data.UserKey
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
        if(UserKey.API.publickApiKey.length<10) {
            try {


                ApiKey = intent.getStringExtra("API_Key").toString()
                UserKey.API.publickApiKey = ApiKey
            }catch (e:Exception){}
        }
        else {
            ApiKey=UserKey.API.publickApiKey
        }

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
                var addPostIntent = Intent(this@MainActivity, NewPostActivity::class.java)
                addPostIntent.putExtra("API_Key",ApiKey)
                startActivity(addPostIntent)
            }
            sinInBtn.setOnClickListener {
                if (sinInBtn.text.toString()=="Sign in/up"){
                    var intent= Intent(context,LoginActivity::class.java)
                    context.startActivity(intent)}
                else{
                    ApiKey=""
                    UserKey.API.publickApiKey=ApiKey
                    sinInBtn.setText("Sign in/up")
                    addPostBtn.visibility=View.INVISIBLE
                    goToProfile.visibility=View.INVISIBLE
                }
            }
            goToProfile.setOnClickListener {
                var intentP= Intent(context,ProfileActivity::class.java)
                intentP.putExtra("ApiKey",ApiKey)
                context.startActivity(intentP)
            }//End  goToProfile.setOnClickListener
        }//End of binding
        if (ApiKey.length>20){//so the API key is available and user log in
            binding.apply {
                UserKey.API.publickApiKey=ApiKey
                sinInBtn.setText("Sign Out")
                addPostBtn.visibility=View.VISIBLE
                goToProfile.visibility=View.VISIBLE

            }


        }
    }
}