package com.example.h_mal.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.h_mal.myapplication.model.Repo

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllRepos(repos : List<Repo>) : List<Long>

    @Query("SELECT * FROM Repo")
    fun getRepos() : LiveData<List<Repo>>

}