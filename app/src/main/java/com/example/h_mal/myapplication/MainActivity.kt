package com.example.h_mal.myapplication

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import com.example.h_mal.myapplication.model.JsonObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var searchView: SearchView

    val urlString = "https://api.github.com/orgs/square/repos"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //begin populating list
        executeTask()

        //set a listener for the swipe to refresh
        swipe_refresh.setOnRefreshListener(swipeRefreshListener)
    }

    //implement search interface in the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflate custom menu as our menu
        menuInflater.inflate(R.menu.menu, menu)
        //extract searchview
        val searchItem = menu?.findItem(R.id.search)
        //set searchview globally
        searchView = searchItem?.actionView as SearchView

        return true
    }

    val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        //populate list when pulling to refresh
        executeTask()
    }

    fun executeTask(){
        //clear list before populating
        list_view.adapter = null

        //create url from url string urlstring
        val url = Request.Builder().url(urlString).build()
        //create a okhttpclient for a get request from url
        val client = OkHttpClient()

        //call the url and retrieve its callback
        client.newCall(url).enqueue(object: Callback {
            //failure of retrieval callback
            override fun onFailure(call: Call, e: IOException) {
                //print error to log
                System.err.println(e)
                //if swipe refresh is refreshing then stop
                swipe_refresh.isRefreshing = false
                //list is empty
            }

            //successful retrieval callback
            override fun onResponse(call: Call, response: Response) {
                //get the JSON from the body
                val urlResponse = response.body?.string()

                //print the response to the logs
                println("response = $urlResponse")

                //create gson object
                val gson = Gson()
                //create a mutable list of objects extracted from the json text
                val objectList = gson.fromJson(urlResponse, Array<JsonObject>::class.java).asList().toMutableList()

                if (objectList.size >0){
                    //update the ui
                    runOnUiThread{
                        //custom list view adapter created
                        val adapterLV = ListViewAdapter(baseContext, objectList)
                        //apply adapter to listview
                        list_view.adapter = adapterLV
                        //if swipe refresh is refreshing then stop
                        swipe_refresh.isRefreshing = false

                        //search view has its query change listener applied
                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

                            override fun onQueryTextSubmit(query: String?): Boolean {

                                return true
                            }

                            //as the test is changed the list is filtered
                            override fun onQueryTextChange(newText: String?): Boolean {
                                //filter list function
                                adapterLV.filter.filter(newText)

                                return true
                            }
                        })
                    }
                }else{
                    //list is empty
                }

            }
        })
    }


}
