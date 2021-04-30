package com.example.smartlights.smartapps.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlights.R
import com.example.smartlights.localdatabase.DatabaseClient

/**
 * Created by schnell on 27,April,2021
 */

class ReflectorActivity : AppCompatActivity() {

    private var deviceScanner: TextView? = null
    private var deviceInstallation: TextView? = null
    private var deviceController: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reflection)

        deviceScanner = (findViewById<TextView>(R.id.devicescan))
        deviceInstallation = (findViewById<TextView>(R.id.installation))
        deviceController = (findViewById<TextView>(R.id.controller))

        deviceScanner!!.setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
            finish()
        }

        deviceInstallation!!.setOnClickListener {

            var regionList =
                DatabaseClient.getInstance(applicationContext).appDatabase.wardNumbersDAO.getwardDevices()

            Handler().postDelayed({
                if (regionList.isEmpty()) {
                    startActivity(Intent(this, LauncherActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
            }, 100)
        }

        deviceController!!.setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
            finish()
        }
    }
}