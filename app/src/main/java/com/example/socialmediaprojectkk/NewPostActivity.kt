package com.example.socialmediaprojectkk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.socialmediaprojectkk.API.APIClient
import com.example.socialmediaprojectkk.API.APIinterface
import com.example.socialmediaprojectkk.Data.PostItem
import com.example.socialmediaprojectkk.Data.UserItem
import com.example.socialmediaprojectkk.databinding.ActivityNewPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPostActivity : AppCompatActivity() {
    var API_Key=""
    lateinit var userData: UserItem
    var userName=""
    lateinit var binding:ActivityNewPostBinding
    lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        API_Key= intent.getStringExtra("API_Key").toString()
        context=this
        //Get user data
        if (API_Key.length>12){
            //*********************************************************************************************
            var apiInterface = APIClient().getClinet()?.create(APIinterface::class.java)
            apiInterface!!.getUserData(API_Key)?.enqueue(object :Callback<UserItem> {
                override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                    if(response.body()!=null){
                        userData=response.body()!!
                        binding.apply {
                            userName=userData.username



                        }//End of binding

                    }//end if response != null
                }

                override fun onFailure(call: Call<UserItem>, t: Throwable) {
                    Toast.makeText(this@NewPostActivity, "Sorry, Something goes wrong", Toast.LENGTH_SHORT).show()
                }
            })
            //*********************************************************************************************
        }
        super.onCreate(savedInstanceState)
        binding=ActivityNewPostBinding.inflate((layoutInflater))
        setContentView(binding.root)
        binding.addPostBtn.setOnClickListener {
            AddPost()
        }

        //**************************************
        binding.apply {
            homeBtn.setOnClickListener{
                var intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }

    }
    private fun AddPost() {
        binding.apply {


            var titlePostTxt = titleEt.text
            var descriptionPostTxt = postEt.text

            if (titlePostTxt.isNotEmpty() && descriptionPostTxt.isNotEmpty()) {
                //create a CelebrityItem object
                var postItems =
                    PostItem("", 0, "", descriptionPostTxt.toString(), titlePostTxt.toString(), userName)

                var apiClient = APIClient().getClinet()
                if (apiClient != null) {
                    var apiInterface = apiClient?.create(APIinterface::class.java)
                    apiInterface!!.addPost(postItems)?.enqueue(object :
                        Callback<PostItem> {
                        override fun onResponse(
                            call: Call<PostItem>,
                            response: Response<PostItem>
                        ) {
                            Log.d("response", "onResponse: ${response} ")

                            var mainActivityIntent = Intent(this@NewPostActivity, MainActivity::class.java)
                            mainActivityIntent.putExtra("API_Key",API_Key)
                            startActivity(mainActivityIntent)
                            finish()
                        }

                        override fun onFailure(call: Call<PostItem>, t: Throwable) {
                            Toast.makeText(
                                applicationContext,
                                "Something Wrong!!",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("response", "OnFailure: ${t.message}")
                        }

                    })
                } else {
                    Log.d("TAG", "AddPost: $apiClient")
                }
            } else {
                Toast.makeText(
                    this@NewPostActivity,
                    "Please Enter the required Data!!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
