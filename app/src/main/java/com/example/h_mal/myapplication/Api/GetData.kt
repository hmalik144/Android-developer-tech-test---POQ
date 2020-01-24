package com.example.h_mal.myapplication.Api

import com.example.h_mal.myapplication.model.Repo
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GetData{

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : GetData{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://api.github.com/orgs/square/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GetData::class.java)
        }
    }

    @GET("repos")
    suspend fun getData() : Response<List<Repo>>

}