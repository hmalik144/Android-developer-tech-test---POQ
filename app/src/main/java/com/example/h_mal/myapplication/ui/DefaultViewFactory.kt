package com.example.h_mal.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.h_mal.myapplication.repository.Repository

@Suppress("UNCHECKED_CAST")
class DefaultViewFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}