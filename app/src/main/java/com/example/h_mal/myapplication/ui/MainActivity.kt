package com.example.h_mal.myapplication.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.h_mal.myapplication.R
import com.example.h_mal.myapplication.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(), KodeinAware {
    lateinit var searchView: SearchView
    lateinit var adapterLV: ListViewAdapter

    override val kodein by kodein()

    private val factory: DefaultViewFactory by instance()

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this,factory).get(MainViewModel::class.java)

        bindUI()
        //set a listener for the swipe to refresh
        swipe_refresh.setOnRefreshListener(swipeRefreshListener)
    }

    private fun bindUI() = CoroutineScope(Dispatchers.Main).launch {
        spinner.visibility = View.VISIBLE

        viewModel.repos.await().observe(this@MainActivity, Observer {
            spinner.visibility = View.GONE

            if (it.isEmpty()){
                empty_view.visibility = View.VISIBLE
                searchView.setOnQueryTextListener(null)
            }else{
                empty_view.visibility = View.GONE

                adapterLV = ListViewAdapter(baseContext, it.toMutableList())
                //apply adapter to listview
                list_view.adapter = adapterLV

                list_view.setOnItemClickListener { parent, view, position, id ->
                    adapterLV.openLink(position)
                }

                //search view has its query change listener applied
                searchView.setOnQueryTextListener(queryListener)
            }


        })
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
        bindUI()

    }

    val queryListener = object : SearchView.OnQueryTextListener{

        override fun onQueryTextSubmit(query: String?): Boolean {

            return true
        }

        //as the test is changed the list is filtered
        override fun onQueryTextChange(newText: String?): Boolean {
            //filter list function
            adapterLV.filter.filter(newText)

            return true
        }
    }


}
