package com.example.smartlights.smartapps.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smartlights.R
import com.example.smartlights.localdatabase.DatabaseClient
import com.example.smartlights.services.Response
import com.example.smartlights.services.ResponseListener
import com.example.smartlights.smartapps.https.ThingsManager
import com.example.smartlights.smartapps.models.ThingsBoardResponse
import com.example.smartlights.utils.AppDialogs
import com.example.smartlights.utils.AppPreference
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

/**
 * Created by schnell on 22,April,2021
 */
class DashboardActivity : AppCompatActivity() {

    private var IlmInstallation: TextView? = null
    private var CCMSInstallation: TextView? = null
    private var GwInstallation: TextView? = null
    private var IlmMaintenance: TextView? = null
    private var CCMSMaintenance: TextView? = null
    private var GwMaintenance: TextView? = null

    val MY_PERMISSIONS_REQUEST_CAMERA = 98
    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_dashboard)

        checkCameraPermission()

//        FindDistance(11.004230,77.024320,11.145600,76.948930)

        IlmInstallation = findViewById<TextView>(R.id.ilm_add)
        CCMSInstallation = findViewById<TextView>(R.id.ccms_add)
        GwInstallation = findViewById<TextView>(R.id.gw_add)

        IlmMaintenance = findViewById<TextView>(R.id.ilm_main)
        CCMSMaintenance = findViewById<TextView>(R.id.ccms_main)
        GwMaintenance = findViewById<TextView>(R.id.gw_main)

        /** Fetching forms based on Region as ILM , CCMS OR GATEWAY**/


        IlmInstallation!!.setOnClickListener {
            AppPreference.put(applicationContext, "DeviceType", "ILM")
            startActivity(Intent(this, InstallationActivity::class.java))
        }

        CCMSInstallation!!.setOnClickListener {
            AppPreference.put(applicationContext, "DeviceType", "CCMS")
            startActivity(Intent(this, InstallationActivity::class.java))
        }

        GwInstallation!!.setOnClickListener {
            AppPreference.put(applicationContext, "DeviceType", "GATEWAY")
            startActivity(Intent(this, InstallationActivity::class.java))
        }

        IlmMaintenance!!.setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
        }

        CCMSMaintenance!!.setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
        }

        GwMaintenance!!.setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
        }
    }

    private fun checkCameraPermission(): Boolean {
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED)
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("SMART LIGHTS")
                    .setMessage("ENABLE Camera PERMISSION")
                    .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this@DashboardActivity,
                                arrayOf<String>(Manifest.permission.CAMERA),
                                MY_PERMISSIONS_REQUEST_CAMERA
                            )
                        }
                    })
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
            }
            return false
        } else {
            return true
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED)
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("SMART LIGHTS")
                    .setMessage("ENABLE LOCATION PERMISSION")
                    .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this@DashboardActivity,
                                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        }
                    })
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            return false
        } else {
            return true
        }
    }

    override fun onBackPressed() {
        val l = object : AppDialogs.ConfirmListener {
            override fun yes() {
                finish()
            }
        }
        AppDialogs.confirmAction(
            c = this!!,
            text = "Sure you want to Exit the Installation Application ?",
            l = l
        )
    }

}