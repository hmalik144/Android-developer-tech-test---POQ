package com.example.h_mal.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.h_mal.myapplication.model.JsonObject
import kotlinx.android.synthetic.main.repo_list_item.view.*

class ListViewAdapter(context: Context, objects: MutableList<JsonObject>) :
    ArrayAdapter<JsonObject>(context, 0, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.repo_list_item, parent, false)
        }

        val item = getItem(position)

        view?.name?.text = item?.name
        view?.description?.text = item?.description


        return view!!
    }
}