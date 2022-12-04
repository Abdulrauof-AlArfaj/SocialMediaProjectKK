package com.example.socialmediaproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient  {

    var retrofit:Retrofit?=null

    fun getClinet():Retrofit?{

        retrofit=Retrofit.Builder().baseUrl("https://apidojo.pythonanywhere.com/").
        addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit
    }

}