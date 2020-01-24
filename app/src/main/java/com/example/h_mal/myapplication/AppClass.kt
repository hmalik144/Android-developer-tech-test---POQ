package com.example.h_mal.myapplication

import android.app.Application
import com.example.h_mal.myapplication.Api.GetData
import com.example.h_mal.myapplication.Api.NetworkConnectionInterceptor
import com.example.h_mal.myapplication.db.AppDatabase
import com.example.h_mal.myapplication.repository.Repository
import com.example.h_mal.myapplication.ui.DefaultViewFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppClass : Application(), KodeinAware{


    override val kodein
            = Kodein.lazy{ import(androidXModule(this@AppClass))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { GetData(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton {
            Repository(
                instance(),
                instance()
            )
        }
        bind() from provider { DefaultViewFactory(instance()) }

    }


}