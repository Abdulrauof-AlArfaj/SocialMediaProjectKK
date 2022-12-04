package com.example.socialmediaproject

import retrofit2.Call
import retrofit2.http.*

interface APIinterface {

    @GET("posts/")
     fun getPosts():Call<Post>

     @GET("posts/{id}")
     fun getPost(@Path("id") id : Int): Call<PostItem>

    @POST("posts/")
    fun AddPosts(@Body PostInfo: PostItem): Call<PostItem>


}