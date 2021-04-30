package com.example.smartlights.smartapps.ui

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlights.R
import com.example.smartlights.localdatabase.DatabaseClient
import com.example.smartlights.localdatabase.RegionDevices
import com.example.smartlights.services.Response
import com.example.smartlights.services.ResponseListener
import com.example.smartlights.smartapps.https.ThingsManager
import com.example.smartlights.smartapps.models.AssetGroup
import com.example.smartlights.smartapps.models.LoginResponse
import com.example.smartlights.smartapps.models.ThingsBoardResponse
import com.example.smartlights.utils.AppPreference
import java.lang.Exception

/**
 * Created by schnell on 22,April,2021
 */
class LoginActivity : AppCompatActivity(), ResponseListener {

    private var LoginBtn: TextView? = null
    private var EmailAddress: EditText? = null
    private var Password: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_login)

        LoginBtn = findViewById<TextView>(R.id.loginbtn)
        EmailAddress = findViewById<EditText>(R.id.email)
        Password = findViewById<EditText>(R.id.password)

        LoginBtn!!.setOnClickListener {
            callUserLogin()
        }
    }

    private fun callActivity_proceeder() {
        startActivity(Intent(this, ReflectorActivity::class.java))
        finish()
    }

    override fun onResponse(r: Response?) {
        try {

            if (r == null) {
                return
            }

            if (r.message == "Token has expired" || r.errorCode == 11 && r.status == 401) {
                callUserLogin()
            }

            when (r.requestType) {

                ThingsManager.API.login.hashCode() -> {
                    if (r is LoginResponse) {
                        AppPreference.put(
                            applicationContext!!,
                            AppPreference.Key.accessToken,
                            r.token.toString()
                        )
                        AppPreference.put(
                            applicationContext!!,
                            AppPreference.Key.refreshToken,
                            r.refreshToken.toString()
                        )

                        ThingsManager.getDeviceAssets(c = this, l = this, Saccount = "Smart")

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Invalid User",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                ThingsManager.API.getDeviceAssets.hashCode() -> {
                    if (r is AssetGroup) {
                        if (r.deviceList!!.isNotEmpty()) {
                            for (i in 0 until r.deviceList!!.size) {
                                var deid = r.deviceList!!.get(i).id!!.id!!
                                if (r.deviceList!!.get(i).name.equals("Regions")) {
                                    ThingsManager.getdevicelist(
                                        c = this!!,
                                        l = this,
                                        entityGroupId = deid, Saccount = "Smart"
                                    )
                                }
                            }
                        }
                    }
                }

                ThingsManager.API.getDeviceFromenityGroup.hashCode() -> {
                    if (r is ThingsBoardResponse)
                        if (r.devicesList!!.size != null) {
                            for (i in 0 until r.devicesList!!.size) {
                                var regiondevice = RegionDevices()
                                regiondevice.deviceid = r.devicesList!!.get(i).id!!.id!!
                                regiondevice.devicename = r.devicesList!!.get(i).name
                                regiondevice.devicetype = r.devicesList!!.get(i).type
                                DatabaseClient.getInstance(applicationContext).appDatabase
                                    .deviceDAO.insert(regiondevice)
                            }
                            callActivity_proceeder()
                        }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callUserLogin() {
        if (EmailAddress!!.text.toString().isNotEmpty()) {
            if (Password!!.text.toString().isNotEmpty()) {

                AppPreference.put(
                    applicationContext,
                    "EmailAddress",
                    EmailAddress!!.text.toString()
                )
                AppPreference.put(applicationContext, "Password", Password!!.text.toString())

                ThingsManager.login(
                    this,
                    EmailAddress!!.text.toString(),
                    Password!!.text.toString()
                )
            } else {
                Toast.makeText(applicationContext, "Invalid Password Entry", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(applicationContext, "Invalid Email Entry", Toast.LENGTH_SHORT)
                .show()
        }
    }
}