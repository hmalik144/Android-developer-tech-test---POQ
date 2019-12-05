package com.example.h_mal.myapplication.Api

import com.example.h_mal.myapplication.model.Repo
import io.reactivex.Observable
import retrofit2.http.GET

interface GetData{

    @GET("repos")
    fun getData() : Observable<List<Repo>>
}