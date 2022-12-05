package com.example.socialmediaproject

import com.example.socialmediaprojectkk.User
import com.example.socialmediaprojectkk.UserItem
import retrofit2.Call
import retrofit2.http.*

interface APIinterface {

    @GET("users/")
    fun getUsers() : Call<User>

    @POST("users/")
    fun addUser(@Body userItem: UserItem) : Call<UserItem>

    @POST("users/")
    fun updateUser(@Path("api_key") api_key: String, @Body userItem: UserItem) : Call<UserItem>

    @GET("login/")
    fun loginUser(@Path("username") userName: String, @Path("password") password: String) : Call<UserItem>

    @GET("users/")
    fun getUserData(@Path("api_key") api_key: String) :Call<UserItem>

    @GET("posts/")
     fun getPosts(): Call<Post>

     @GET("posts/{id}")
     fun getPost(@Path("id") id : Int): Call<PostItem>

    @POST("posts/")
    fun AddPosts(@Body PostInfo: PostItem): Call<PostItem>

    @POST("posts/")
    fun updatePost(@Path("id") id: Int, @Body postItem: PostItem) : Call<PostItem>


}