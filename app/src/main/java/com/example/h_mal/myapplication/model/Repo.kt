package com.example.h_mal.myapplication.model

import com.google.gson.annotations.SerializedName

data class Repo(
    var name: String? = null,
    var description : String? = null,
    var language : String? = null,
    var date : String? = null,
    var html_url : String? = null)