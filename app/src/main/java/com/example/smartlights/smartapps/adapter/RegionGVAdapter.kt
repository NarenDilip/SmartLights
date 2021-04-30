package com.example.smartlights.smartapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.smartlights.R
import com.example.smartlights.smartapps.models.RegionModel
import java.util.*

/**
 * Created by schnell on 22,April,2021
 */

class RegionGVAdapter(
    context: Context,
    courseModelArrayList: ArrayList<RegionModel?>?
) :
    ArrayAdapter<RegionModel?>(context, 0, courseModelArrayList!!) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView =
                LayoutInflater.from(context).inflate(R.layout.card_entry, parent, false)
        }
        val courseModel = getItem(position)
        val courseTV = listitemView!!.findViewById<TextView>(R.id.idTVCourse)
        courseTV.text = courseModel!!.getCourse_name()
        return listitemView
    }
}