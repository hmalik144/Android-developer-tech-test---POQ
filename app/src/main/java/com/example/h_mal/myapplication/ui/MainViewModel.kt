package com.example.h_mal.myapplication.ui

import androidx.lifecycle.ViewModel
import com.example.h_mal.myapplication.repository.Repository
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainViewModel(val repository : Repository) : ViewModel(){

    val repos by lazy{
        GlobalScope.async(start = CoroutineStart.LAZY) {
            repository.getRepos()
        }
    }
}