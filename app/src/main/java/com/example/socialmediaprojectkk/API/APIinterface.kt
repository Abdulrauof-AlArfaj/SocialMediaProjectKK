package com.example.socialmediaprojectkk.API

import com.example.socialmediaprojectkk.Data.Post
import com.example.socialmediaprojectkk.Data.PostItem
import com.example.socialmediaprojectkk.Data.User
import com.example.socialmediaprojectkk.Data.UserItem
import retrofit2.Call
import retrofit2.http.*

interface APIinterface {

    @GET("users/")
    fun getUsers() : Call<User>

    @POST("users/")
    fun addUser(@Body userItem: UserItem) : Call<UserItem>

    @POST("users/{api_key}")
    fun updateUser(@Path("api_key") api_key: String, @Body userItem: UserItem) : Call<UserItem>


    @GET("login/{username}/{password}")
    fun loginUser(@Path("username") userName: String, @Path("password") password: String) : Call<String>

    @GET("users/{api_key}")
    fun getUserData(@Path("api_key") api_key: String) :Call<UserItem>

    @GET("posts/")
    fun getPosts() : Call<Post>

    @POST("posts/")
    fun addPost(@Body postItem: PostItem) : Call<PostItem>

    @GET("posts/{id}")
    fun getPostDetails(@Path("id") id: Int) : Call<PostItem>

    @PUT("posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body postItem: PostItem) : Call<PostItem>


}