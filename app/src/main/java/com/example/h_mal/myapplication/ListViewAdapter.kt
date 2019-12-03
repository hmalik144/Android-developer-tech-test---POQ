package com.example.h_mal.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.h_mal.myapplication.model.JsonObject
import kotlinx.android.synthetic.main.repo_list_item.view.*

//custom list adapter extends from array adater
class ListViewAdapter(context: Context, objects: MutableList<JsonObject>) :
    ArrayAdapter<JsonObject>(context, 0, objects){

    //populate each view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //if the current view is null then inflate our list item object into list
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.repo_list_item, parent, false)!!
        }

        //get the current item from the current position
        val item = getItem(position)

        //set name and description
        view.name?.text = item?.name
        view.description?.text = item?.description

        //parse date from the date time string and set the date
        val dateString = item?.date?.split('T')?.get(0)
        view.date?.text = dateString

        //check if the language object is null
        if (item.language != null){
            //language exists in object
            //view holdin the language to be visible
            view.lang_layout.visibility = View.VISIBLE

            //language text and corresponding colour according to github is applied
            view.lang?.text = item.language
            getColor(item.language)?.let { it1 -> view.lang_col.setCardBackgroundColor(it1) }
        }else{
            //language was null therefore view to be hidden
            view.lang_layout.visibility = View.GONE
        }

        //apply on click listener to item
        view.setOnClickListener{
            //click item opens then url
            openLink(item.repoUrlString)
        }

        return view
    }

    //function for opening the link
    fun openLink(urlString : String?){
        //open link to repo if the url is not nu;;
        if (urlString != null){
            val openURL =  Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(urlString)
            context.startActivity(openURL)
        }

    }

    //get the corresponding colour based on the programming language
    fun getColor(language: String?) : Int?{
        return when(language) {
            "Ruby" -> context.getColor(R.color.Ruby)
            "Java"-> context.getColor(R.color.Java)
            "Objective-C" -> context.getColor(R.color.ObjectiveC)
            "JavaScript" -> context.getColor(R.color.JavaScript)
            "CSS" -> context.getColor(R.color.CSS)
            "Shell" -> context.getColor(R.color.Shell)
            //if the language has no match return nothing
            else -> null
        }
    }
}