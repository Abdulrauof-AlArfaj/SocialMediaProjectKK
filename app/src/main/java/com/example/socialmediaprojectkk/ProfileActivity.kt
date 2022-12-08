package com.example.socialmediaprojectkk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.socialmediaprojectkk.API.APIClient
import com.example.socialmediaprojectkk.API.APIinterface
import com.example.socialmediaprojectkk.databinding.*
import com.example.socialmediaprojectkk.Data.UserItem
import com.example.socialmediaprojectkk.Data.UserKey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var context: Context
    var ApiKey= UserKey.API.publickApiKey
    lateinit var userData: UserItem
    lateinit var imgUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        //ApiKey= intent.getStringExtra("ApiKey").toString()

        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Get user data:
        //*********************************************************************************************
        var apiInterface = APIClient().getClinet()?.create(APIinterface::class.java)
        apiInterface!!.getUserData(ApiKey)?.enqueue(object :Callback<UserItem> {
            override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                if(response.body()!=null){
                    userData=response.body()!!
                    binding.apply {

                        nameEt.setText(userData.username)
                        emailEt.setText(userData.email)
                        webEt.setText(userData.website)
                        aboutEt.setText(userData.about)
                        imgUrl = userData.image

                        if(imgUrl.isNotEmpty()) {
                            Glide.with(this@ProfileActivity).load(imgUrl).into(binding.profileImage)
                        }


                    }//End of binding

                }//end if response != null
            }

            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Sorry, Something goes wrong", Toast.LENGTH_SHORT).show()
            }
        })
        //*********************************************************************************************
        binding.apply {
            homeBtn.setOnClickListener{
                var intent = Intent(context, MainActivity::class.java)
                UserKey.API.publickApiKey=ApiKey
               // intent.putExtra("API_Key",ApiKey)
                context.startActivity(intent)
            }
        }




    }//End of main
}

private fun <T> Call<T>.enqueue(call: Call<T>) {

}
