package com.example.h_mal.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Repo(
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var description : String? = null,
    var language : String? = null,
    var date : String? = null,
    var html_url : String? = null)