package com.example.h_mal.myapplication.model

import com.google.gson.annotations.SerializedName

data class JsonObject(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("description")
    var description : String? = null,
    @SerializedName("language")
    var language : String? = null,
    @SerializedName("html_url")
    var repoUrlString : String? = null
)
