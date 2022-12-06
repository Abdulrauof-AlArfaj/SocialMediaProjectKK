package com.example.socialmediaproject

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient  {

    var retrofit:Retrofit?=null


    fun getClinet():Retrofit?{
        //New Code
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        // New Code
        val gsonBuilder = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl("https://apidojo.pythonanywhere.com/").
        addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .client(okHttpClient)
            .build()
        return retrofit
    }

}