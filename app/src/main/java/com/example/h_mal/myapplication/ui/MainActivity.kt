package com.example.h_mal.myapplication.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.View
import android.widget.SearchView
import com.example.h_mal.myapplication.Api.GetData
import com.example.h_mal.myapplication.R
import com.example.h_mal.myapplication.model.Repo
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.disposables.CompositeDisposable
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit

import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver


class MainActivity : AppCompatActivity() {
    lateinit var searchView: SearchView
    lateinit var myCompositeDisposable: CompositeDisposable

    val urlString = "https://api.github.com/orgs/square/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner.visibility = View.VISIBLE

        myCompositeDisposable = CompositeDisposable()

        //begin populating list
        loadData()

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

    override fun onDestroy() {
        super.onDestroy()

        myCompositeDisposable?.clear()
    }

    val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener{
        //populate list when pulling to refresh
//        callData()
        loadData()
    }

    fun loadData(){
        //clear list before populating
        list_view.adapter = null

        val requestInterface = Retrofit.Builder()
            .baseUrl(urlString)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GetData::class.java)

        myCompositeDisposable.add(requestInterface.getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : DisposableObserver<List<Repo>>() {
                override fun onNext(t: List<Repo>) {
                    handleResponse(t)
                }

                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                    handleError()
                }
            }))
    }

    private fun handleResponse(objectList: List<Repo>) {

        spinner.visibility = View.GONE

        //if swipe refresh is refreshing then stop
        swipe_refresh.isRefreshing = false

        if (objectList.isNotEmpty()){
            //list is not empty
            empty_view.visibility = View.GONE
            //custom list view adapter created
            val adapterLV = ListViewAdapter(baseContext, objectList.toMutableList())
            //apply adapter to listview
            list_view.adapter = adapterLV

            list_view.setOnItemClickListener { parent, view, position, id ->
                adapterLV.openLink(position)
            }

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

    }

    fun handleError(){
        //if swipe refresh is refreshing then stop
        swipe_refresh.isRefreshing = false
        //list is empty
        empty_view.visibility = View.VISIBLE
        //progress bar hidden
        spinner.visibility = View.GONE
    }

}
