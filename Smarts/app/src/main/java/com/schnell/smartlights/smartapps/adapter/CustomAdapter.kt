package com.schnell.smartlights.smartapps.adapter;

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.schnell.smartlights.R
import com.schnell.smartlights.localdatabase.DatabaseClient
import com.schnell.smartlights.localdatabase.PoleDeviceListDAO
import com.schnell.smartlights.services.Response
import com.schnell.smartlights.services.ResponseListener
import com.schnell.smartlights.smartapps.models.Model
import com.schnell.smartlights.smartapps.models.NModel
import com.schnell.smartlights.utils.AppPreference

import java.util.*

class CustomAdapter(internal var context: Context, internal var itemModelList: ArrayList<NModel>) :
    BaseAdapter(),
    ResponseListener {

    private var poleDeviceListDAO: PoleDeviceListDAO? = null
    private var ArmModelList: ArrayList<Model>? = null

    override fun getCount(): Int {
        return itemModelList.size
    }

    override fun getItem(position: Int): Any {
        return itemModelList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = null
        if (convertView == null) {

            ArmModelList = ArrayList<Model>()
            ArmModelList!!.add(Model("Arm A"))
            ArmModelList!!.add(Model("Arm B"))
            ArmModelList!!.add(Model("Arm C"))
            ArmModelList!!.add(Model("Arm D"))
            ArmModelList!!.add(Model("Arm E"))
            ArmModelList!!.add(Model("Arm F"))
            ArmModelList!!.add(Model("Arm G"))
            ArmModelList!!.add(Model("Arm H"))
            ArmModelList!!.add(Model("Arm I"))
            ArmModelList!!.add(Model("Arm J"))
            ArmModelList!!.add(Model("Arm K"))
            ArmModelList!!.add(Model("Arm L"))
            ArmModelList!!.add(Model("Arm M"))
            ArmModelList!!.add(Model("Arm N"))
            ArmModelList!!.add(Model("Arm O"))
            ArmModelList!!.add(Model("Arm P"))
            ArmModelList!!.add(Model("Arm Q"))
            ArmModelList!!.add(Model("Arm R"))
            ArmModelList!!.add(Model("Arm S"))
            ArmModelList!!.add(Model("Arm T"))
            ArmModelList!!.add(Model("Arm U"))
            ArmModelList!!.add(Model("Arm V"))
            ArmModelList!!.add(Model("Arm W"))
            ArmModelList!!.add(Model("Arm X"))
            ArmModelList!!.add(Model("Arm Y"))
            ArmModelList!!.add(Model("Arm Z"))

            val mInflater = context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.item, null)
            val tvName = convertView!!.findViewById<View>(R.id.devicename) as EditText
            val devscann = convertView.findViewById<View>(R.id.devicescanner) as Button
            val imgRemove = convertView.findViewById<View>(R.id.imgremoveAdd) as ImageView
            val armdevice: EditText = convertView!!.findViewById<View>(R.id.armtype) as EditText

            val m = itemModelList[position]
            tvName.setText(m.name)
            if (m.type.equals("New")) {
                imgRemove.visibility = View.VISIBLE
            } else {
                imgRemove.visibility = View.GONE
            }

            val arm = ArmModelList!![position]
            armdevice.setText(arm.name)

            // click listiner for remove button
            imgRemove.setOnClickListener {
                itemModelList.removeAt(position)
                notifyDataSetChanged()
                var details = AppPreference.get(context, "PoleId", "")
                DatabaseClient.getInstance(context).appDatabase.poleDevice!!.DeletePoleDeviceId(
                    details,
                    tvName.text.toString()
                )
                DatabaseClient.getInstance(context).appDatabase.getilmDevices()!!
                    .DeletedeviceId(details, tvName.text.toString())
            }
        }
        return convertView
    }

    override fun onResponse(r: Response?) {
    }
}
