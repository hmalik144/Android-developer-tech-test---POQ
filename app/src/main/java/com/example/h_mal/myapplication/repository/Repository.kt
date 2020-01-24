package com.example.h_mal.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.h_mal.myapplication.Api.GetData
import com.example.h_mal.myapplication.Api.SafeApiRequest
import com.example.h_mal.myapplication.db.AppDatabase
import com.example.h_mal.myapplication.model.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Repository(
    private val api: GetData,
    private val db: AppDatabase
):SafeApiRequest(){

    private val repos = MutableLiveData<List<Repo>>()

    init {
        repos.observeForever {
            saveRepos(it)
        }
    }

    suspend fun getRepos(): LiveData<List<Repo>> {
        return withContext(Dispatchers.IO) {
            fetchRepos()
            db.getRepoDao().getRepos()
        }
    }

    private suspend fun fetchRepos() {
        try {
            val response = apiRequest { api.getData() }
            repos.postValue(response)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    private fun saveRepos(repos: List<Repo>) {
        CoroutineScope(Dispatchers.IO).launch{
            db.getRepoDao().saveAllRepos(repos)
        }
    }
}