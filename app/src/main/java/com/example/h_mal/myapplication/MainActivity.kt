package com.example.h_mal.myapplication

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import com.example.h_mal.myapplication.model.JsonObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var thecontext: Context

    companion object{
        val urlString = "https://api.github.com/orgs/square/repos"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thecontext = this

        executeTask()

        swipe_refresh.setOnRefreshListener(swipeRefreshListener)
    }

    val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        executeTask()
    }

    fun executeTask(){
        val url = Request.Builder().url(urlString).build()
        val client = OkHttpClient()

        client.newCall(url).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                System.err.println(e)

                swipe_refresh.isRefreshing = false
            }

            override fun onResponse(call: Call, response: Response) {
                val urlResponse = response.body?.string()

                println("response = $urlResponse")

                val gson = Gson()
                val objectList = gson.fromJson(urlResponse, Array<JsonObject>::class.java).asList().toMutableList()

                runOnUiThread{
                    list_view.adapter = ListViewAdapter(thecontext, objectList)
                    swipe_refresh.isRefreshing = false
                }

            }
        })
    }


}
