package com.example.smartlights.smartapps.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.example.smartlights.R
import com.example.smartlights.localdatabase.AssetDevices
import com.example.smartlights.localdatabase.DatabaseClient
import com.example.smartlights.services.Response
import com.example.smartlights.services.ResponseListener
import com.example.smartlights.smartapps.dialogs.CCMSDialog
import com.example.smartlights.smartapps.dialogs.WardDialog
import com.example.smartlights.smartapps.dialogs.ZoneDialog
import com.example.smartlights.smartapps.https.ThingsManager
import com.example.smartlights.smartapps.models.AssetSelectedWards
import com.example.smartlights.smartapps.models.DeviceDetails
import com.example.smartlights.smartapps.models.ThingsBoardResponse
import com.example.smartlights.utils.AppDialogs
import com.example.smartlights.utils.AppPreference
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by schnell on 22,April,2021
 */

class InstallationActivity : AppCompatActivity(), ResponseListener, ZoneDialog.CallBack,
    WardDialog.CallBack, CCMSDialog.CallBack, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
    OnMapReadyCallback {

    private var ilmlayer: ConstraintLayout? = null
    private var CcmsControll: TextView? = null
    private var ZoneCall: TextView? = null
    private var WardCall: TextView? = null
    private var SubmitBtn: TextView? = null
    private var Latt: EditText? = null
    private var Long: EditText? = null

    private var DeviceScan: Button? = null
    private var DeviceAddBtn: ImageView? = null
    private var DeviceView: EditText? = null

    private var zoneDialog: ZoneDialog? = null
    private var wardDialog: WardDialog? = null
    private var ccmsDialog: CCMSDialog? = null

    private var tlist: ArrayList<EditText>? = null
    private var elist: ArrayList<Spinner>? = null

    private var DatalistEdittext: ArrayList<String>? = null
    private var Datalistspinner: ArrayList<String>? = null

    private var wardDetailsId: String? = null
    private var ScannedString: String? = null

    private var pref: SharedPreferences? = null
    private var seditor: SharedPreferences.Editor? = null

    private var originLatlng: LatLng? = null
    private var originLattitude: String? = null
    private var originLongitude: String? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private var mapFragment: SupportMapFragment? = null;
    private var mapView: MapView? = null;
    private var map: GoogleMap? = null

    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private var locationManager: LocationManager? = null

    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation)

        ilmlayer = findViewById<ConstraintLayout>(R.id.mainlay)
        CcmsControll = findViewById<TextView>(R.id.ccms_id)
        ZoneCall = findViewById<TextView>(R.id.zone)
        WardCall = findViewById<TextView>(R.id.ward)
        SubmitBtn = findViewById<TextView>(R.id.submitbtn)

        DeviceScan = findViewById<Button>(R.id.devscan)
        DeviceAddBtn = findViewById<ImageView>(R.id.imgViewAdd)
        DeviceView = findViewById<EditText>(R.id.editView)

        Latt = findViewById<EditText>(R.id.latt)
        Long = findViewById<EditText>(R.id.longg)

        tlist = ArrayList<EditText>()
        elist = ArrayList<Spinner>()

        DatalistEdittext = ArrayList<String>()
        Datalistspinner = ArrayList<String>()

        pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        seditor = pref!!.edit()

        var DeviceType = AppPreference.get(applicationContext, "DeviceType", "")

        var sZone = AppPreference.get(applicationContext, "Zone", "")
        var sWard = AppPreference.get(
            applicationContext,
            "Ward",
            ""
        )

        if (sZone.toString().isNotEmpty()) {
            ZoneCall!!.text = sZone.toString()
        }

        if (sWard.toString().isNotEmpty()) {
            WardCall!!.text = sWard.toString()
        }

        if (DeviceType.equals("ILM")) {
            ilmlayer!!.visibility = View.VISIBLE
            CcmsControll!!.visibility = View.VISIBLE
        } else {
            ilmlayer!!.visibility = View.GONE
            CcmsControll!!.visibility = View.GONE
        }

        ZoneCall!!.setOnClickListener {
            zoneDialog = ZoneDialog(this)
            zoneDialog!!.setCallBack(this)
            zoneDialog!!.show()
            WardCall!!.text = "Please Select Ward"
            CcmsControll!!.text = "Please Select CCMS"
        }

        WardCall!!.setOnClickListener {
            wardDialog = WardDialog(this)
            wardDialog!!.setCallBack(this)
            wardDialog!!.show()
            CcmsControll!!.text = "Please Select CCMS"
        }

        CcmsControll!!.setOnClickListener {
            ccmsDialog = CCMSDialog(this)
            ccmsDialog!!.setCallBack(this)
            ccmsDialog!!.show()
        }

        DeviceScan!!.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            intent.putExtra("key", "Ilm")
            startActivity(intent)
        }

        DeviceAddBtn!!.setOnClickListener {

        }

        var RegionName = AppPreference.get(
            applicationContext,
            "SelectedZone", ""
        )

        val detailslist =
            DatabaseClient.getInstance(applicationContext).appDatabase.deviceDAO!!.getSelectedname(
                "Dummy"
            )
        if (detailslist.isNotEmpty()) {
            for (i in 0 until detailslist.size) {
                ThingsManager.getDeviceLatestAttributes(
                    c = this,
                    deviceId = detailslist.get(i).deviceid,
                    entityType = "ASSET",
                    Keys = DeviceType!!,
                    Saccount = "Smart"
                )
            }
        }

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment!!.getMapAsync(this)

        mapFragment!!.getMapAsync { googleMap ->
            map = googleMap
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)

            map!!.setOnMapClickListener { point ->
                val marker =
                    MarkerOptions().position(LatLng(point.latitude, point.longitude))
                        .title("New Marker")
                originLattitude = point.latitude.toString()
                originLongitude = point.longitude.toString()

                Latt!!.setText(point.latitude.toString())
                Long!!.setText(point.longitude.toString())

                map!!.clear()
                map!!.addMarker(marker)
            }
        }

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        SubmitBtn!!.setOnClickListener {

            for (i in 0 until tlist!!.size) {
//                Toast.makeText(
//                    applicationContext,
//                    tlist!!.get(i).text.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()

                if (tlist!!.get(i).text.toString().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        DatalistEdittext!!.get(i).toString() + " " + "Invalid",
                        Toast.LENGTH_SHORT
                    ).show()
                    tlist!!.get(i).error = "Invaild Entry"
                } else {

                }
            }

            for (i in 0 until elist!!.size) {
//                Toast.makeText(
//                    applicationContext,
//                    elist!!.get(i).selectedItem.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()

                if (elist!!.get(i).selectedItem.toString() == Datalistspinner!!.get(i).toString()) {
                    val errorText = elist!!.get(i).selectedView as TextView
                    errorText.setTextColor(resources.getColor(R.color.light_red))
                } else {

                }
            }
        }

        DeviceAddBtn!!.setOnClickListener {
            if (!ZoneCall!!.text.toString().isEmpty()) {


            } else {
                Toast.makeText(
                    applicationContext,
                    "Please fill the above fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun callUserLogin() {

        var EmailAddress = AppPreference.get(applicationContext, "EmailAddress", "")
        var Password = AppPreference.get(applicationContext, "Password", "")

        if (EmailAddress!!.toString().isNotEmpty()) {
            if (Password!!.toString().isNotEmpty()) {
                ThingsManager.login(
                    this,
                    EmailAddress.toString(),
                    Password.toString()
                )
            } else {
                Toast.makeText(applicationContext, "Invalid Password", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(applicationContext, "Invalid Email", Toast.LENGTH_SHORT)
                .show()
        }
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

                ThingsManager.API.getDeviceTelemetry.hashCode() -> {
                    if (r is ThingsBoardResponse) {
                        var details = r.devicesList!!.get(0).value!!.toString()
                        val obj = JSONObject(r.devicesList!!.get(0).value!!.toString())
                        val dslist = obj.get("details") as JSONObject
                        val dslister = dslist.get("forms") as JSONArray

                        val lLayout = findViewById<View>(R.id.viewlayer) as LinearLayout
                        lLayout.removeAllViews()

                        for (i in 0 until dslister.length()) {

                            val dataset = dslist.get(dslister.get(i).toString()) as JSONObject
                            val user_entry = dataset.get("entry") as JSONArray

                            val dropdownvalues = dataset.get("enum") as JSONArray
                            if (dataset.get("enum").toString().equals("[]")) {

                                val params = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )

                                val tv = EditText(this)

                                val typeface =
                                    ResourcesCompat.getFont(applicationContext, R.font.mont_regular)
                                tv.typeface = typeface

                                if (user_entry.toString().contains("Scan")) {
                                    params.setMargins(
                                        10,
                                        10,
                                        10,
                                        0
                                    ) // setMargins(left, top, right, bottom)
                                    tv.layoutParams = params

                                    tv.setCompoundDrawablesWithIntrinsicBounds(
                                        0,
                                        0,
                                        R.drawable.ic_baseline_qr_code_scanner_24_bl,
                                        0
                                    )

                                    tv.setOnTouchListener(OnTouchListener { v, event ->
                                        val DRAWABLE_LEFT = 0
                                        val DRAWABLE_TOP = 1
                                        val DRAWABLE_RIGHT = 2
                                        val DRAWABLE_BOTTOM = 3
                                        if (event.action == MotionEvent.ACTION_UP) {
                                            if (event.rawX >= tv.getRight() - tv.getCompoundDrawables()
                                                    .get(DRAWABLE_RIGHT).getBounds().width()
                                            ) {

                                                AppPreference.put(
                                                    applicationContext,
                                                    "OnScan",
                                                    dataset.get("description").toString()
                                                )

                                                val intent = Intent(this, ScanActivity::class.java)
                                                intent.putExtra(
                                                    "key",
                                                    dataset.get("description").toString()
                                                )
                                                startActivity(intent)
                                                return@OnTouchListener true
                                            }
                                        }
                                        false
                                    })

                                } else {
                                    params.setMargins(
                                        10,
                                        25,
                                        10,
                                        0
                                    )
                                    tv.layoutParams = params
                                }

                                tv.setTextColor(applicationContext.resources.getColor(R.color.black))
                                tv.setHintTextColor(applicationContext.resources.getColor(R.color.black))
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

                                tv.id = i
                                AppPreference.put(
                                    applicationContext,
                                    dataset.get("description").toString(),
                                    i
                                )
                                tv.hint = dataset.get("description").toString()
                                lLayout.addView(tv)

                                tlist!!.add(tv)

                                DatalistEdittext!!.add(dataset.get("description").toString())

                            } else {

                                val spinnerArray =
                                    ArrayList<String>()
                                spinnerArray.add(dataset.get("description").toString())
                                for (i in 0 until dropdownvalues.length()) {
                                    spinnerArray.add(dropdownvalues.get(i).toString())
                                }

                                val spinner = Spinner(this)
                                val spinnerArrayAdapter =
                                    ArrayAdapter(
                                        this,
                                        R.layout.row_text,
                                        spinnerArray
                                    )

                                spinner.id = i
                                AppPreference.put(
                                    applicationContext,
                                    dataset.get("description").toString(),
                                    i
                                )
                                spinner.adapter = spinnerArrayAdapter
                                lLayout.addView(spinner)
                                elist!!.add(spinner)

                                Datalistspinner!!.add(dataset.get("description").toString())
                            }
                        }
                    }
                }

                ThingsManager.API.getfromdevice.hashCode() -> {
                    var Selector = false
                    if (r is AssetSelectedWards) {
                        if (r.deviceList!!.isNotEmpty()) {
                            for (i in 0 until r.deviceList!!.size) {
                                var assetDevices = AssetDevices()
                                if (r.deviceList!!.get(i).to_id!!.entityType.equals("DEVICE")) {
                                    assetDevices.wardid = wardDetailsId
                                    assetDevices.ccmsid =
                                        r.deviceList!!.get(i).to_id!!.id.toString()
                                    assetDevices.ccmsname = "NO"
                                    DatabaseClient.getInstance(applicationContext).appDatabase.devicesDAO()
                                        .insert(assetDevices)
                                    Selector = true
                                }
                            }
                        } else {
                            Toast.makeText(applicationContext, "No CCMS Found", Toast.LENGTH_SHORT)
                                .show()
                            CcmsControll!!.text = "No CCMS Found"
                            AppDialogs.hideProgressDialog()
                        }
                        if (Selector) {
                            callNameFiller()
                        } else {
                            CcmsControll!!.text = "No CCMS Found"
                            AppDialogs.hideProgressDialog()
                        }
                    }
                }

                ThingsManager.API.getDeviceName.hashCode() -> {
                    if (r is DeviceDetails) {
                        var deviceaddress =
                            DatabaseClient.getInstance(applicationContext).appDatabase.devicesDAO()
                                .getccmsDevices(r.id!!.id!!)
                        if (deviceaddress.isNotEmpty()) {
                            for (i in 0 until deviceaddress.size)
                                DatabaseClient.getInstance(applicationContext).appDatabase.devicesDAO()!!
                                    .updatedevicename(
                                        r.name,
                                        deviceaddress.get(i).wardid,
                                        deviceaddress.get(i).ccmsid
                                    )
                            callNameFiller()
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public override fun onResume() {

        val ilmDetails = AppPreference.get(this, "Ilm", "")
        if (ilmDetails!!.isNotEmpty()) {
            DeviceView!!.setText(ilmDetails)
        }

        super.onResume()

        for (i in 0 until tlist!!.size) {
            try {
                val PoleDetails = AppPreference.get(this, DatalistEdittext!!.get(i).toString(), "")
                if (PoleDetails!!.isNotEmpty()) {
                    val editable: Editable = SpannableStringBuilder(PoleDetails)
                    tlist!!.get(i).text = editable
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ZoneDetails(ManufacturerData: String?) {
        ZoneCall!!.text = ManufacturerData.toString()
        seditor!!.putString("Zone", ManufacturerData)
        seditor!!.commit()
        zoneDialog!!.dismiss()
    }

    override fun WardDetails(ManufacturerData: String?) {
        WardCall!!.text = ManufacturerData.toString()
        wardDialog!!.dismiss()
        callController()
    }

    private fun callController() {
        AppDialogs.showProgressDialog(this@InstallationActivity, "Please Wait")

        DatabaseClient.getInstance(applicationContext).appDatabase.devicesDAO()!!.DeleteDetails()

        var ward_details =
            DatabaseClient.getInstance(applicationContext).appDatabase.wardNumbersDAO!!.getsdwardDevices(
                WardCall!!.text.toString()
            )
        if (!ward_details.isNullOrEmpty()) {
            for (i in 0 until ward_details.size) {
                wardDetailsId =
                    ward_details.get(i).deviceid
            }
        }
        if (ward_details!!.isNotEmpty()) {
            ThingsManager.fromaddress(this, wardDetailsId!!, "ASSET", "Smart")
        }
    }

    private fun callNameFiller() {
        var ccmslist =
            DatabaseClient.getInstance(applicationContext).appDatabase.devicesDAO().getccmsid("NO")
        if (ccmslist.isNotEmpty()) {
            for (i in 0 until ccmslist.size) {
                if (i == 0) {
                    AppDialogs.showProgressDialog(this@InstallationActivity, "Please wait")
                    ThingsManager.getdevicedetails(
                        this,
                        ccmslist.get(i).ccmsid,
                        "Smart"
                    )
                }
            }
        }
    }

    override fun CCMSDetails(CCMSData: String?) {
        CcmsControll!!.text = CCMSData
        ccmsDialog!!.dismiss()
        var iddetails = DatabaseClient.getInstance(applicationContext).appDatabase.devicesDAO()
            .getccmsid(CCMSData)
        if (iddetails.isNotEmpty()) {
            for (i in 0 until iddetails.size) {
                Toast.makeText(
                    applicationContext,
                    iddetails.get(i).ccmsid.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        startLocationUpdates()

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionSuspended(i: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest, this
        )
        Log.d("reque", "--->>>>")
    }

    override fun onLocationChanged(location: Location) {
        val msg = "Updated Location: " +
                java.lang.Double.toString(location.latitude) + "," +
                java.lang.Double.toString(location.longitude)
//        if (Mapped == "Yes") {
        originLattitude = location.latitude.toString()
        originLongitude = location.longitude.toString()
        Latt!!.setText(location.latitude.toString())
        Long!!.setText(location.longitude.toString())
        originLatlng = LatLng(location.latitude, location.longitude)

        map!!.clear();
        var markerOptions: MarkerOptions? = null
        markerOptions = MarkerOptions()
        markerOptions!!.position(LatLng(location.getLatitude(), location.getLongitude()));
        markerOptions!!.title("my position");

        map!!.addMarker(markerOptions);

        var i1 = 18 // 4 bytes
        var f1 = i1.toFloat()

        map!!.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.getLatitude(), location.getLongitude()
                ), f1
            )
        );
//            Mapped = "No"
//        }

        map!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(point: LatLng) {
                val marker =
                    MarkerOptions().position(LatLng(point.latitude, point.longitude))
                        .title("New Marker")
                originLattitude = point.latitude.toString()
                originLongitude = point.longitude.toString()

                Latt!!.setText(point.latitude.toString())
                Long!!.setText(point.longitude.toString())

                map!!.clear()
                map!!.addMarker(marker)
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        map!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(point: LatLng) {
                val marker =
                    MarkerOptions().position(LatLng(point.latitude, point.longitude))
                        .title("New Marker")
                originLattitude = point.latitude.toString()
                originLongitude = point.longitude.toString()

                Latt!!.setText(point.latitude.toString())
                Long!!.setText(point.longitude.toString())

                map!!.clear()
                map!!.addMarker(marker)
            }
        })
    }

    private fun checkLocation(): Boolean {
        if (isLocationEnabled)
            showAlert()
        return isLocationEnabled
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }
}