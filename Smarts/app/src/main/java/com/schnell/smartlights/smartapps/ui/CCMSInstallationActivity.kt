package com.schnell.smartlights.smartapps.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.schnell.smartlights.R
import com.schnell.smartlights.localdatabase.*
import com.schnell.smartlights.services.Response
import com.schnell.smartlights.services.ResponseListener
import com.schnell.smartlights.smartapps.adapter.CustomAdapter
import com.schnell.smartlights.smartapps.dialogs.CCMSDialog
import com.schnell.smartlights.smartapps.dialogs.WardDialog
import com.schnell.smartlights.smartapps.dialogs.ZoneDialog
import com.schnell.smartlights.smartapps.https.ThingsManager
import com.schnell.smartlights.smartapps.https.request.AuditServiceRequest
import com.schnell.smartlights.smartapps.https.response.NewResponseListener
import com.schnell.smartlights.smartapps.models.*
import com.schnell.smartlights.utils.AppDialogs
import com.schnell.smartlights.utils.AppPreference
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by schnell on 22,April,2021
 */

class CCMSInstallationActivity : AppCompatActivity(), ResponseListener, ZoneDialog.CallBack,
    WardDialog.CallBack, CCMSDialog.CallBack, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
    OnMapReadyCallback, NewResponseListener {

    private var ilmlayer: ConstraintLayout? = null
    private var ccmsControll: AutoCompleteTextView? = null
    private var ccmsHead: TextInputLayout? = null
    private var regionCall: AutoCompleteTextView? = null
    private var zoneCall: AutoCompleteTextView? = null
    private var wardCall: AutoCompleteTextView? = null
    private var submitBtn: TextView? = null
    private var logoutBtn: TextView? = null
    private var nodeListView: ListView? = null
    private var latt: TextInputEditText? = null
    private var long: TextInputEditText? = null

    //    private var ArmModelList: ArrayList<Model>? = null
    private var deviceScan: Button? = null
    private var deviceAddBtn: ImageView? = null
    private var deviceView: TextInputEditText? = null
    private var credentialsState: String? = ""

    private var zoneDialog: ZoneDialog? = null
    private var wardDialog: WardDialog? = null
    private var ccmsDialog: CCMSDialog? = null

    private var tlist: ArrayList<TextInputLayout>? = null
    private var elist: ArrayList<TextInputLayout>? = null

    private var datalistEdittext: ArrayList<String>? = null
    private var dataEditJson: ArrayList<String>? = null
    private var dataSpinnerJson: ArrayList<String>? = null
    private var datalistspinner: ArrayList<String>? = null
    private var data = HashMap<String, String>()
//    private var wardDetailsId: String? = null
//    private var ScannedString: String? = null

    //    private var Regiondata: String? = null
//    private var Zonedata: String? = null
//    private var CCMSData: String? = null
//    private var Poledata: String? = null
//    private var Landmarkdata: String? = null
//    private var Armsdata: String? = null
//    private var Wattsdata: String? = null
//    private var Lifetimedata: String? = null
//    private var Scnodata: String? = null
    private var jsonObject: JSONObject? = null
//    private var CollectString: Int? = 0

    private var deviceType: String? = null
//    private var AssetGroupId = ""

    private var pref: SharedPreferences? = null
    private var seditor: SharedPreferences.Editor? = null

    private var originLatlng: LatLng? = null
    private var originLattitude: String? = null
    private var originLongitude: String? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val uPDATEINTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val fASTESTINTERVAL: Long = 2000 /* 2 sec */
    private var mapFragment: SupportMapFragment? = null

    //    private var mapView: MapView? = null;
    private var map: GoogleMap? = null

    private var itemModelList: ArrayList<Model>? = null
    private var poleDeviceData: ArrayList<NModel>? = null

    private var customAdapter: CustomAdapter? = null
    private var deviceCall: String? = ""
    private var cCMSDeviceGroupId: String? = ""
    private var createdGroupId: String? = ""
    private var createdRegionGroupId: String? = ""

    private var regiondata: Boolean? = false
    private var zonedata: Boolean? = false

    val my_PERMISSIONS_REQUEST_LOCATION = 99
    private var locationManager: LocationManager? = null

    private var cdeviceId: String? = null
    private var responseCode: String? = ""
    private var deviceId: String? = null
    private var serviceCall: String? = ""

    //    private var device: Device? = null
    private var preInstallDeviceGroupId: String? = ""

    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

//    private var globalss: Int? = 0
//    private var GlobalState: String? = ""
//    private var LoadingDataId: String? = ""

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installation)

        checkLocationPermission()

        ilmlayer = findViewById(R.id.mainlay)
        ccmsControll = findViewById(R.id.ccms_id)
        ccmsHead = findViewById(R.id.ccms_id_head)
        zoneCall = findViewById(R.id.zone)
        wardCall = findViewById(R.id.ward)
        regionCall = findViewById(R.id.region)
        logoutBtn = findViewById(R.id.logoutbtn)
        submitBtn = findViewById(R.id.submitbtn)
        nodeListView = findViewById(R.id.listview)

        deviceScan = findViewById(R.id.devscan)
        deviceAddBtn = findViewById(R.id.imgViewAdd)
        deviceView = findViewById(R.id.editView)

        latt = findViewById(R.id.latt)
        long = findViewById(R.id.longg)

        tlist = ArrayList()
        elist = ArrayList()

        datalistEdittext = ArrayList()
        dataEditJson = ArrayList()
        datalistspinner = ArrayList()
        dataSpinnerJson = ArrayList()
        data = HashMap()
        jsonObject = JSONObject()

        itemModelList = ArrayList()
        poleDeviceData = ArrayList()
        itemModelList!!.clear()

        poleDeviceData!!.clear()
        customAdapter = CustomAdapter(applicationContext, poleDeviceData!!)
        nodeListView!!.adapter = customAdapter
//        NodeListView!!.visibility = View.VISIBLE

        pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        seditor = pref!!.edit()

        deviceType = AppPreference[applicationContext, "DeviceType", ""]
        val sZone = AppPreference[applicationContext, "Zone", ""]
        val sWard = AppPreference[applicationContext, "Ward", ""]

        if (sZone.toString().isNotEmpty()) zoneCall!!.setText(sZone)
        if (sWard.toString().isNotEmpty()) wardCall!!.setText(sWard)

        if (deviceType.equals("ILM")) {
            ilmlayer!!.visibility = View.VISIBLE
            ccmsHead!!.visibility = View.VISIBLE
        } else {
            ilmlayer!!.visibility = View.GONE
            ccmsHead!!.visibility = View.GONE
        }

        logoutBtn!!.setOnClickListener {
            val l = object : AppDialogs.ConfirmListener {
                override fun yes() {
                    finish()
                    callSplash()
                }
            }
            AppDialogs.confirmAction(
                c = this,
                text = "Sure you want to Logout from Luminator ?",
                l = l
            )
        }

        zoneCall!!.setOnClickListener {
            zoneDialog = ZoneDialog(this)
            zoneDialog!!.setCallBack(this)
            zoneDialog!!.show()
            wardCall!!.setText(getString(R.string.selectward))
            ccmsControll!!.setText(getString(R.string.selectccms))
        }

        wardCall!!.setOnClickListener {
            wardDialog = WardDialog(this)
            wardDialog!!.setCallBack(this)
            wardDialog!!.show()
            ccmsControll!!.setText(getString(R.string.selectccms))
        }

        ccmsControll!!.setOnClickListener {
            ccmsDialog = CCMSDialog(this)
            ccmsDialog!!.setCallBack(this)
            ccmsDialog!!.show()
        }

        deviceScan!!.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            intent.putExtra("key", "CCMS")
            startActivity(intent)
        }

        val RegionName = AppPreference.get(
            applicationContext,
            "SelectedZone", ""
        )

        val detailslist =
            DatabaseClient.getInstance(applicationContext).appDatabase.deviceDAO!!.getSelectedname(
                RegionName
            )

        regionCall!!.setText(RegionName)

        if (detailslist.isNotEmpty()) {
            for (i in 0 until detailslist.size) {
                ThingsManager.getDeviceLatestAttributes(
                    c = this,
                    deviceId = detailslist[i].deviceid,
                    entityType = "ASSET",
                    Keys = deviceType!!,
                    Saccount = "Smart"
                )
            }
        }

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment!!.getMapAsync(this)

        mapFragment!!.getMapAsync { googleMap ->
            map = googleMap
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

            map!!.setOnMapClickListener { point ->
                val marker =
                    MarkerOptions().position(LatLng(point.latitude, point.longitude))
                        .title("New Marker")
                originLattitude = point.latitude.toString()
                originLongitude = point.longitude.toString()

                latt!!.setText(point.latitude.toString())
                long!!.setText(point.longitude.toString())

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

        submitBtn!!.setOnClickListener {
            data.clear()

            for (i in 0 until tlist!!.size) {
                if (tlist!![i].editText!!.text.toString().isEmpty()) {
                    tlist!![i].editText!!.error = "Invaild Entry"

                    jsonObject!!.put(
                        dataEditJson!![i],
                        "Error"
                    )

                } else {
                    AppPreference.put(
                        applicationContext,
                        datalistEdittext!![i],
                        tlist!![i].editText!!.text.toString()
                    )
                    data.put(
                        datalistEdittext!![i],
                        tlist!![i].editText!!.text.toString()
                    )

                    jsonObject!!.put(
                        dataEditJson!![i],
                        tlist!![i].editText!!.text.toString()
                    )
                }
            }

            for (i in 0 until elist!!.size) {
                if (elist!![i].editText!!.text.toString().isEmpty()) {
                    elist!![i].editText!!.error = "Invaild Entry"

                    jsonObject!!.put(
                        dataSpinnerJson!![i],
                        "Error"
                    )

                } else if (elist!![i].editText!!.text.toString() == datalistspinner!![i]
                ) {
                    elist!![i].editText!!.error = "Invaild Entry"

                    jsonObject!!.put(
                        dataSpinnerJson!![i],
                        "Error"
                    )
                } else {
                    AppPreference.put(
                        applicationContext,
                        datalistspinner!![i],
                        elist!![i].editText!!.text.toString()
                    )
                    data.put(
                        datalistspinner!![i],
                        elist!![i].editText!!.text.toString()
                    )

                    jsonObject!!.put(
                        dataSpinnerJson!![i],
                        elist!![i].editText!!.text.toString()
                    )
                }
            }

            zoneCall!!.text.toString()
            wardCall!!.text.toString()
            ccmsControll!!.text.toString()
            latt!!.text.toString()
            long!!.text.toString()

            data["No of Arms"].toString()

            if (deviceType.equals("ILM")) {

            } else if (deviceType.equals("CCMS")) {

                if (!jsonObject!!.toString().contains("Error")) {

                    AppDialogs.showProgressDialog(
                        this,
                        "Please wait"
                    )

                    jsonObject!!.put("zone", zoneCall!!.text.toString())
                    jsonObject!!.put("ward", wardCall!!.text.toString())

                    val boardNumber = jsonObject!!.get("boardnumber").toString()
                    AuditServiceRequest.CCMSSaveBoardNumber(
                        this,
                        boardNumber
                    )

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Check non filled coloumns",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else if (deviceType.equals("GATEWAY")) {
                if (!jsonObject!!.toString().contains("Error")) {

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Check non filled coloumns",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun callSplash() {
        DatabaseClient.getInstance(applicationContext).appDatabase.deviceDAO.DeleteRegion()
        DatabaseClient.getInstance(applicationContext).appDatabase.zoneDeviceDAO.DeleteZone()
        DatabaseClient.getInstance(applicationContext).appDatabase.wardNumbersDAO.DeleteWard()
        AppPreference.clear(applicationContext, "Zone")
        AppPreference.clear(applicationContext, "Ward")
        startActivity(Intent(this, SplashScreen::class.java))
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
                                this@CCMSInstallationActivity,
                                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                                my_PERMISSIONS_REQUEST_LOCATION
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
                    my_PERMISSIONS_REQUEST_LOCATION
                )
            }
            return false
        } else {
            return true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun callUserLogin() {

        val emailAddress = AppPreference.get(applicationContext, "EmailAddress", "")
        val password = AppPreference.get(applicationContext, "Password", "")

        if (emailAddress!!.toString().isNotEmpty()) {
            if (password!!.toString().isNotEmpty()) {
                ThingsManager.login(
                    this,
                    emailAddress.toString(),
                    password.toString()
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

    private fun callattributes() {

        val unixTime = System.currentTimeMillis() / 1000L
        val phone = AppPreference.get(this, "Ph", "")

        ThingsManager.addserverallStringAttribute(
            this,
            this,
            deviceId!!,
            "location",
            jsonObject!!.get("location").toString(),
            "wardName",
            wardCall!!.text.toString(),
            "zoneName",
            zoneCall!!.text.toString(),
            "slatitude",
            latt!!.text.toString(),
            "slongitude",
            long!!.text.toString(),
            "connectedWatts",
            "",
            "poleQty",
            "",
            "lampQty",
            "",
            "baseLine",
            "0",
            "roadType",
            jsonObject!!.get("roadtype").toString(),
            "ccmsType",
            jsonObject!!.get("ccmstype").toString(),
            "servicePoleNo",
            jsonObject!!.get("ccmspolenumber").toString(),
            "scNo",
            jsonObject!!.get("scnumber").toString(),
            "ebMeterNo",
            jsonObject!!.get("ebmeterno").toString(),
            "installationTime",
            unixTime.toInt(),
            "installedBy",
            phone!!,
            "appVersion",
            "GOA 1.0.1",
            Saccount = "Smart"
        )

        deleteaddentitycall(preInstallDeviceGroupId!!, deviceId!!)
        addentitycall(createdGroupId!!, deviceId!!)
        addentitycall(createdRegionGroupId!!, deviceId!!)

        AppDialogs.showProgressDialog(this, "Please wait connecting to server..")
        if (regiondata == false) {
            serviceCall = "CZone"
        } else {
            serviceCall = "ThirdCall"
        }
        ThingsManager.getDeviceDevice(this, this, Saccount = "Smart")
    }


    override fun onResponse(r: Response?) {
        try {

            if (r == null) {
                AppDialogs.hideProgressDialog()
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
                    }
                }

                ThingsManager.API.getDeviceTelemetry.hashCode() -> {
                    if (r is ThingsBoardResponse) {
//                        var details = r.devicesList!!.get(0).value!!.toString()
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

                                val editText = EditText(this)
                                val editTextParams: LinearLayout.LayoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                    )

                                val textInputLayout = TextInputLayout(this)
                                val textInputLayoutParams: LinearLayout.LayoutParams =
                                    LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                setTextInputLayoutHintColor(
                                    textInputLayout,
                                    applicationContext,
                                    R.color.black
                                )
                                val typeface =
                                    ResourcesCompat.getFont(applicationContext, R.font.mont_regular)
                                textInputLayout.typeface = typeface
                                editText.typeface = typeface
                                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

                                editText.isSingleLine = true

                                editText.filters =
                                    arrayOf<InputFilter>(InputFilter.LengthFilter(30))
                                editText.filters = arrayOf<InputFilter>(ignoreFirstWhiteSpace())

                                if (user_entry.toString().contains("Scan")) {

                                    editText.setCompoundDrawablesWithIntrinsicBounds(
                                        0,
                                        0,
                                        R.drawable.ic_baseline_qr_code_scanner_24_bl,
                                        0
                                    )

                                    editText.isClickable = false
                                    editText.isFocusable = false

                                    editText.setOnTouchListener(View.OnTouchListener { v, event ->
                                        val DRAWABLE_LEFT = 0
                                        val DRAWABLE_TOP = 1
                                        val DRAWABLE_RIGHT = 2
                                        val DRAWABLE_BOTTOM = 3
                                        if (event.action == MotionEvent.ACTION_UP) {
                                            if (event.rawX >= editText.right - editText.compoundDrawables
                                                    .get(DRAWABLE_RIGHT).bounds.width()
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
                                }

                                textInputLayout.layoutParams = textInputLayoutParams
                                textInputLayout.addView(editText, editTextParams)
                                textInputLayout.hint = dataset.get("description").toString()
                                lLayout.addView(textInputLayout)

//                                val params = LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT
//                                )
//
//                                val tv = EditText(this)
//
//                                val typeface =
//                                    ResourcesCompat.getFont(applicationContext, R.font.mont_regular)
//                                tv.typeface = typeface
//
//                                if (user_entry.toString().contains("Scan")) {
//                                    params.setMargins(
//                                        10,
//                                        10,
//                                        10,
//                                        0
//                                    ) // setMargins(left, top, right, bottom)
//                                    tv.layoutParams = params
//
//                                    tv.setCompoundDrawablesWithIntrinsicBounds(
//                                        0,
//                                        0,
//                                        R.drawable.ic_baseline_qr_code_scanner_24_bl,
//                                        0
//                                    )
//
//                                    tv.setOnTouchListener(OnTouchListener { v, event ->
//                                        val DRAWABLE_LEFT = 0
//                                        val DRAWABLE_TOP = 1
//                                        val DRAWABLE_RIGHT = 2
//                                        val DRAWABLE_BOTTOM = 3
//                                        if (event.action == MotionEvent.ACTION_UP) {
//                                            if (event.rawX >= tv.getRight() - tv.getCompoundDrawables()
//                                                    .get(DRAWABLE_RIGHT).getBounds().width()
//                                            ) {
//
//                                                AppPreference.put(
//                                                    applicationContext,
//                                                    "OnScan",
//                                                    dataset.get("description").toString()
//                                                )
//
//                                                val intent = Intent(this, ScanActivity::class.java)
//                                                intent.putExtra(
//                                                    "key",
//                                                    dataset.get("description").toString()
//                                                )
//                                                startActivity(intent)
//                                                return@OnTouchListener true
//                                            }
//                                        }
//                                        false
//                                    })
//
//                                } else {
//                                    params.setMargins(
//                                        10,
//                                        25,
//                                        10,
//                                        0
//                                    )
//                                    tv.layoutParams = params
//                                }
//
//                                tv.setTextColor(applicationContext.resources.getColor(R.color.black))
//                                tv.setHintTextColor(applicationContext.resources.getColor(R.color.black))
//                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
//
//                                tv.id = i
//                                AppPreference.put(
//                                    applicationContext,
//                                    dataset.get("description").toString(),
//                                    i
//                                )
//                                tv.hint = dataset.get("description").toString()
//                                lLayout.addView(tv)
//
                                tlist!!.add(textInputLayout)
//
                                datalistEdittext!!.add(dataset.get("description").toString())
                                dataEditJson!!.add(dataset.get("json").toString())

                            } else {

                                val textInputLayout = TextInputLayout(
                                    this,
                                    null,
                                    R.style.Widget_MaterialComponents_TextInputLayout_FilledBox_Dense_ExposedDropdownMenu
                                )
//                                val textInputLayoutParams: LinearLayout.LayoutParams =
//                                    LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.MATCH_PARENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT
//                                    )

                                val spinnerArray =
                                    ArrayList<String>()
                                spinnerArray.add(dataset.get("description").toString())

                                for (j in 0 until dropdownvalues.length()) {
                                    spinnerArray.add(dropdownvalues.get(j).toString())
                                }

                                val autoCompleteTextView = AutoCompleteTextView(this)
                                val adapter: ArrayAdapter<String> = ArrayAdapter(
                                    this,
                                    R.layout.dropdown_menu_popup_item,
                                    spinnerArray
                                )

                                val typeface =
                                    ResourcesCompat.getFont(applicationContext, R.font.mont_regular)
                                textInputLayout.typeface = typeface
                                autoCompleteTextView.typeface = typeface

                                autoCompleteTextView.id = i
                                autoCompleteTextView.isFocusable = false
                                autoCompleteTextView.setOnClickListener {
                                    autoCompleteTextView.showDropDown()
                                    autoCompleteTextView.error = null
                                }

                                autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

                                AppPreference.put(
                                    applicationContext,
                                    dataset.get("description").toString(),
                                    i
                                )
                                autoCompleteTextView.setAdapter(adapter)
                                adapter.notifyDataSetChanged()

                                textInputLayout.addView(autoCompleteTextView)
                                textInputLayout.typeface = typeface
                                setTextInputLayoutHintColor(
                                    textInputLayout,
                                    applicationContext,
                                    R.color.black
                                )

                                textInputLayout.hint = dataset.get("description").toString()

                                lLayout.addView(textInputLayout)

//                                val spinnerArray =
//                                    ArrayList<String>()
//                                spinnerArray.add(dataset.get("description").toString())
//                                for (i in 0 until dropdownvalues.length()) {
//                                    spinnerArray.add(dropdownvalues.get(i).toString())
//                                }
//
//                                val spinner = Spinner(this)
//                                val spinnerArrayAdapter =
//                                    ArrayAdapter(
//                                        this,
//                                        R.layout.row_text,
//                                        spinnerArray
//                                    )
//
//                                spinner.id = i
//                                AppPreference.put(
//                                    applicationContext,
//                                    dataset.get("description").toString(),
//                                    i
//                                )
//                                spinner.adapter = spinnerArrayAdapter
//                                lLayout.addView(spinner)
                                elist!!.add(textInputLayout)
//
                                datalistspinner!!.add(dataset.get("description").toString())
                                dataSpinnerJson!!.add(dataset.get("json").toString())
                            }
                        }
                    }
                }

                ThingsManager.API.getDeviceName.hashCode() -> {
                    if (deviceCall == "Device") {
                        if (r is DeviceDetails) {
//                            device = r
                            val warddetails =
                                DatabaseClient.getInstance(applicationContext).appDatabase.wardNumbersDAO!!.getsdwardDevices(
                                    wardCall!!.text.toString()
                                )
                            if (warddetails.isNotEmpty()) {
                                for (i in 0 until warddetails.size) {
                                    saveRelation(
                                        warddetails[i].deviceid,
                                        (r as ThingsBoardResponse).id!!.id!!
                                    )
                                    Toast.makeText(
                                        applicationContext,
                                        "Sucessfully CCMS Installed",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

//                                    zone.text = "Please choose Zone"
//                                    ward.text = "Please choose ward"
//                                    uid_details.setText("")
//                                    location.setText("")
//                                    sc_no.setText("")
//                                    spole_no.setText("")
//                                    eb_meter.setText("")
//                                    rtype.text = "Road Type"
//                                    ccmstype.text = "CCMS Type"
                                    AppDialogs.hideProgressDialog()
                                    this.finish()
                                    AppPreference.put(applicationContext, "DeviceType", "CCMS")
                                    startActivity(
                                        Intent(
                                            this,
                                            CCMSInstallationActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            }
                        }
                    }
                }

                ThingsManager.API.saveEntityGroup.hashCode() -> {
                    if (deviceCall == "Entity") {
                        if (r is Device) {
                            createdGroupId = r.id!!.id!!
                            callattributes()
                        }
                    }
                }

                ThingsManager.API.tenantdevices.hashCode() -> {
                    if (r is TenantDevices) {
                        deviceId = r.id!!.id!!

                        AppDialogs.showProgressDialog(
                            this,
                            "Please wait connecting to server.."
                        )
                        serviceCall = "FirstCall"
                        //Need to login with tenant customer with crompton account
                        ThingsManager.getDeviceDevice(this, this, Saccount = "Smart")
                    }
                }

                ThingsManager.API.getDeviceDevice.hashCode() -> {
                    if (serviceCall == "FirstCall") {
                        if (r is AssetGroup) {
                            if (r.deviceList!!.isNotEmpty()) {
                                for (i in 0 until r.deviceList!!.size) {
                                    if (r.deviceList!!.get(i).name.equals("preInstallCCMS")) {
//                                    if (r.deviceList!!.get(i).name.equals("CCMS")) {
                                        preInstallDeviceGroupId =
                                            r.deviceList!!.get(i).id!!.id!!

                                        AppDialogs.showProgressDialog(
                                            this,
                                            "Please wait connecting to server.."
                                        )
                                        serviceCall = "SecondCall"
                                        ThingsManager.getDeviceDevice(
                                            this,
                                            this,
                                            Saccount = "Smart"
                                        )
                                        //Need to login with tenant customer with crompton account
                                    }
                                }
                            }
                        }
                    } else if (serviceCall == "SecondCall") {
                        var zonegroupavailable = false
                        if (r is AssetGroup) {
                            if (r.deviceList!!.isNotEmpty()) {
                                for (i in 0 until r.deviceList!!.size) {
                                    if (r.deviceList!!.get(i).name.equals("CCMS" + "-" + zoneCall!!.text.toString())) {
                                        zonegroupavailable = true
                                        createdGroupId = r.deviceList!!.get(i).id!!.id!!
                                        zonedata = true
                                        regiondata = false
                                    }
                                }
                            }
                        }

                        if (!zonegroupavailable) {
                            deviceCall = "Entity"
                            serviceCall = "CZone"
                            if (zonedata == false) {
                                ThingsManager.saveEntityGroup(
                                    c = this,
                                    groupName = "CCMS" + "-" + zoneCall!!.text.toString(),
                                    description = "CCMS",
                                    displayName = "CCMS" + "-" + zoneCall!!.text.toString(),
                                    Saccount = "Smart"
                                )
                            }
                            regiondata = false
                            zonedata = true
                        } else {
                            serviceCall = "CZone"
                            ThingsManager.getDeviceDevice(this, this, Saccount = "Smart")
                        }
                    } else if (serviceCall == "CZone") {
//                        val RegionName = AppPreference.get(
//                            applicationContext,
//                            "SelectedZone", ""
//                        )
//                        val regionsName = AppPreference.get(applicationContext, "Region", "")
                        var regiongroupavailable = false
                        if (r is AssetGroup) {
                            if (r.deviceList!!.isNotEmpty()) {
                                for (i in 0 until r.deviceList!!.size) {
                                    if (r.deviceList!!.get(i).name.equals("CCMS-" + regionCall!!.text.toString())) {
                                        regiongroupavailable = true
                                        createdRegionGroupId = r.deviceList!!.get(i).id!!.id!!
                                        zonedata = true
                                        regiondata = true
                                    }
                                }
                            }
                        }

                        if (!regiongroupavailable) {
                            deviceCall = "Entity"
                            if (regiondata == false) {
                                ThingsManager.saveEntityGroup(
                                    c = this,
                                    groupName = "CCMS-" + regionCall!!.text.toString(),
                                    description = "CCMS",
                                    displayName = "CCMS-" + regionCall!!.text.toString(),
                                    Saccount = "Smart"
                                )
                            }
                            regiondata = true
                            zonedata = true
                        } else {
                            callattributes()
                        }

                    } else if (serviceCall == "ThirdCall") {
                        if (r is AssetGroup) {
                            if (r.deviceList!!.isNotEmpty()) {
                                for (i in 0 until r.deviceList!!.size) {
                                    if (r.deviceList!!.get(i).name.equals("CCMS")) {
                                        cCMSDeviceGroupId = r.deviceList!!.get(i).id!!.id!!

                                        addentitycall(cCMSDeviceGroupId!!, deviceId!!)

                                        AppDialogs.showProgressDialog(this, "Please wait..")
                                        deviceCall = "Device"

                                        ThingsManager.getdevicedetails(
                                            this,
                                            deviceId!!,
                                            Saccount = "Smart"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


//                ThingsManager.API.addentities.hashCode() -> {
//                    if (r is Response) {
//
//                    }
//                }
//
//                ThingsManager.API.deleteentities.hashCode() -> {
//                    if (r is Response) {
//
//                    }
//                }
//
//                ThingsManager.API.saveRelation.hashCode() -> {
//                    if (r is Response) {
//                    }
//                }
            }

        } catch (e: Exception) {
            AppDialogs.hideProgressDialog()
            e.printStackTrace()
        }
    }

    private fun ignoreFirstWhiteSpace(): InputFilter {
        return InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (Character.isWhitespace(source[i])) {
                    if (dstart == 0) return@InputFilter ""
                }
            }
            null
        }
    }

    private fun setTextInputLayoutHintColor(
        textInputLayout: TextInputLayout,
        context: Context,
        @ColorRes colorIdRes: Int
    ) {
        textInputLayout.defaultHintTextColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }

    private fun deleteaddentitycall(
        deletegroupid: String,
        deletedeviceid: String
    ) {

        val jArray = JSONArray()
        jArray.put(deletedeviceid)
        ThingsManager.deleteEntites(
            this, this, deletegroupid, jArray, null, "Smart"
        )
    }

    private fun addentitycall(
        addgroupid: String,
        adddeviceid: String
    ) {

        val jArray1 = JSONArray()
        jArray1.put(adddeviceid)
        ThingsManager.aaddEntites(this, this, addgroupid, jArray1, null, "Smart")
    }

    public override fun onResume() {

        val ilmDetails = AppPreference[this, "Ilm", ""]
        if (ilmDetails!!.isNotEmpty()) {
            deviceView!!.setText(ilmDetails)
        }

        super.onResume()

        for (i in 0 until tlist!!.size) {
            try {
                val poleDetails = AppPreference[this, datalistEdittext!![i].toString(), ""]
                if (poleDetails!!.isNotEmpty()) {
                    val editable: Editable = SpannableStringBuilder(poleDetails)
                    tlist!![i].editText!!.text = editable
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ZoneDetails(ManufacturerData: String?) {
        zoneCall!!.setText(ManufacturerData.toString())
        seditor!!.putString("Zone", ManufacturerData)
        seditor!!.commit()
        zoneDialog!!.dismiss()
    }

    override fun WardDetails(ManufacturerData: String?) {
        wardCall!!.setText(ManufacturerData.toString())
        wardDialog!!.dismiss()
    }

    override fun CCMSDetails(CCMSData: String?) {
        ccmsControll!!.setText(CCMSData.toString())
        ccmsDialog!!.dismiss()
        val iddetails = DatabaseClient.getInstance(applicationContext).appDatabase.devicesDAO()
            .getccmsid(CCMSData)
        if (iddetails.isNotEmpty()) {
            for (i in 0 until iddetails.size) {
                credentialsState = iddetails.get(i).ccmsid.toString()
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
            .setInterval(uPDATEINTERVAL)
            .setFastestInterval(fASTESTINTERVAL)
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
//        val msg = "Updated Location: " +
//                location.latitude.toString() + "," +
//                location.longitude.toString()
//        if (Mapped == "Yes") {
        originLattitude = location.latitude.toString()
        originLongitude = location.longitude.toString()
        latt!!.setText(location.latitude.toString())
        long!!.setText(location.longitude.toString())
        originLatlng = LatLng(location.latitude, location.longitude)

        map!!.clear()
        var markerOptions: MarkerOptions? = null
        markerOptions = MarkerOptions()
        markerOptions.position(LatLng(location.latitude, location.longitude))
        markerOptions.title("my position")

        map!!.addMarker(markerOptions)
        val i1 = 18 // 4 bytes
        val f1 = i1.toFloat()

        map!!.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude, location.longitude
                ), f1
            )
        )
//            Mapped = "No"
//        }

        map!!.setOnMapClickListener { point ->
            val marker =
                MarkerOptions().position(LatLng(point.latitude, point.longitude))
                    .title("New Marker")
            originLattitude = point.latitude.toString()
            originLongitude = point.longitude.toString()

            latt!!.setText(point.latitude.toString())
            long!!.setText(point.longitude.toString())

            map!!.clear()
            map!!.addMarker(marker)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        map!!.setOnMapClickListener { point ->
            val marker =
                MarkerOptions().position(LatLng(point.latitude, point.longitude))
                    .title("New Marker")
            originLattitude = point.latitude.toString()
            originLongitude = point.longitude.toString()

            latt!!.setText(point.latitude.toString())
            long!!.setText(point.longitude.toString())

            map!!.clear()
            map!!.addMarker(marker)
        }
    }

    private fun saveRelation(gatewayDeviceId: String, device: String) {
        ThingsManager.ccmssaveRelation(
            c = this,
            gatewayDeviceId = gatewayDeviceId,
            device = device,
            Saccount = "Smart"
        )
    }

//    private fun checkLocation(): Boolean {
//        if (isLocationEnabled)
//            showAlert()
//        return isLocationEnabled
//    }

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

    override fun onResponseReceived(responseData: String?, requestType: Int) {
        AppDialogs.hideProgressDialog()
        if (responseData != null) {
            AppDialogs.hideProgressDialog()
            if (requestType == 6) {
                val responseObj = JSONObject(responseData)
                responseCode = responseObj.getString("error_code").toString()
                if (responseCode.equals("0000")) {

                    AppDialogs.showProgressDialog(this, "Please wait..")
//                    CdeviceId = (responseData as ScannerResponse).panelid
                    cdeviceId = responseObj.getString("panel_uid").toString()
                    if (!cdeviceId.isNullOrEmpty() && !cdeviceId.equals("-")) {
                        AppDialogs.showProgressDialog(
                            this,
                            "Please wait connecting to server.."
                        )
                        ThingsManager.gettenantDevices(this, this, cdeviceId!!, Saccount = "Smart")
                    }
                }
            }
        } else if (responseCode.equals("1000")) {
            AppDialogs.hideProgressDialog()
        } else if (responseCode.equals("1001")) {
            AppDialogs.hideProgressDialog()
        } else {
            AppDialogs.hideProgressDialog()
        }
    }
}