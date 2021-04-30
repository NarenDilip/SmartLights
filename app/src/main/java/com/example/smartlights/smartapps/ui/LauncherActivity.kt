package com.example.smartlights.smartapps.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlights.R
import com.example.smartlights.localdatabase.DatabaseClient
import com.example.smartlights.smartapps.adapter.RegionGVAdapter
import com.example.smartlights.smartapps.models.RegionModel
import com.example.smartlights.utils.AppPreference

/**
 * Created by schnell on 22,April,2021
 */
class LauncherActivity : AppCompatActivity() {

    private var coursesGV: GridView? = null


    private var pref: SharedPreferences? = null
    private var seditor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        seditor = pref!!.edit()

        coursesGV = findViewById(R.id.idGVcourses)

        val regionModelArrayList = ArrayList<RegionModel?>()

        var regionList =
            DatabaseClient.getInstance(applicationContext).appDatabase.deviceDAO.devices
        for (i in 0 until regionList.size) {
            regionModelArrayList.add(RegionModel(regionList.get(i).devicename))
        }

        val adapter = RegionGVAdapter(this, regionModelArrayList)
        coursesGV!!.adapter = adapter

        coursesGV!!.setOnItemClickListener { parent, view, position, id ->
            AppPreference.put(
                applicationContext,
                "SelectedZone",
                regionModelArrayList[position]!!.getCourse_name()
            )

            seditor!!.putString("Region", regionModelArrayList[position]!!.getCourse_name())
            seditor!!.commit()

            DatabaseClient.getInstance(applicationContext).appDatabase.zoneDeviceDAO.DeleteZone()
            DatabaseClient.getInstance(applicationContext).appDatabase.wardNumbersDAO.DeleteWard()

            startActivity(Intent(this, ZoneFinderActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ReflectorActivity::class.java))
        finish()
    }
}