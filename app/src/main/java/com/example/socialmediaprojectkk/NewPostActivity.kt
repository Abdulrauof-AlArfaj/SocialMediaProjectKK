package com.example.socialmediaprojectkk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.socialmediaproject.APIClient
import com.example.socialmediaproject.APIinterface
import com.example.socialmediaproject.PostItem
import com.example.socialmediaprojectkk.databinding.ActivityNewPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPostActivity : AppCompatActivity() {
    lateinit var binding:ActivityNewPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNewPostBinding.inflate((layoutInflater))
        setContentView(binding.root)
        binding.addPostBtn.setOnClickListener {
            AddPost()
        }

    }
    private fun AddPost() {
        binding.apply {

            var titlePostTxt = titleEt.text
            var descriptionPostTxt = postEt.text

            if (titlePostTxt.isNotEmpty() && descriptionPostTxt.isNotEmpty()) {
                //create a CelebrityItem object
                var postItems =
                    PostItem("", 0, "", descriptionPostTxt.toString(), titlePostTxt.toString(), "")

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
