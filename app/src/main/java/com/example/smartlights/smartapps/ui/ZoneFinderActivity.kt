package com.example.smartlights.smartapps.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlights.R
import com.example.smartlights.localdatabase.DData
import com.example.smartlights.localdatabase.DatabaseClient
import com.example.smartlights.localdatabase.WardDevices
import com.example.smartlights.localdatabase.ZoneDevices
import com.example.smartlights.services.Response
import com.example.smartlights.services.ResponseListener
import com.example.smartlights.smartapps.adapter.ExpandableListAdapter
import com.example.smartlights.smartapps.https.ThingsManager
import com.example.smartlights.smartapps.models.AssetDetails
import com.example.smartlights.smartapps.models.FromAddress
import com.example.smartlights.utils.AppDialogs
import com.example.smartlights.utils.AppPreference
import java.lang.Exception

/**
 * Created by schnell on 22,April,2021
 */
class ZoneFinderActivity : AppCompatActivity(), ResponseListener {

    private var listAdapter: ExpandableListAdapter? = null
    private var expListView: ExpandableListView? = null
    private var listDataHeader: List<String>? = null
    private var listDataChild: HashMap<String, List<String>>? = null
    private var RServiceCall: String? = ""
    private var DServiceCall: String? = ""
    private var SelectedZone: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zonefinder)

        DatabaseClient.getInstance(applicationContext).appDatabase.zoneDeviceDAO.DeleteZone()
        DatabaseClient.getInstance(applicationContext).appDatabase.wardNumbersDAO.DeleteWard()

        // get the listview
        // get the listview
        expListView = findViewById<View>(R.id.expandableview) as ExpandableListView
        // preparing list data

        SelectedZone = AppPreference.get(
            applicationContext,
            "SelectedZone",
            ""
        )

        // preparing list data
        prepareListData(SelectedZone)

        // Listview Group click listener

        // Listview Group click listener
        expListView!!.setOnGroupClickListener { parent, v, groupPosition, id -> // Toast.makeText(getApplicationContext(),
            // "Group Clicked " + listDataHeader.get(groupPosition),
            // Toast.LENGTH_SHORT).show();
            false
        }

        // Listview Group expanded listener

        // Listview Group expanded listener
        expListView!!.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                listDataHeader!![groupPosition] + " Expanded",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Listview Group collasped listener

        // Listview Group collasped listener
        expListView!!.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                listDataHeader!![groupPosition] + " Collapsed",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Listview on child click listener

        // Listview on child click listener
        expListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id -> // TODO Auto-generated method stub
            Toast.makeText(
                applicationContext,
                listDataHeader!![groupPosition] + " : "
                        + listDataChild!![listDataHeader!![groupPosition]]!![childPosition],
                Toast.LENGTH_SHORT
            ).show()

            AppPreference.put(applicationContext, "Zone", listDataHeader!![groupPosition].toString())
            AppPreference.put(
                applicationContext,
                "Ward",
                listDataChild!![listDataHeader!![groupPosition]]!![childPosition].toString()
            )

            startActivity(Intent(this, DashboardActivity::class.java))
            finish()

            false
        }
    }

    /*
     * Preparing the list data
     */
    private fun prepareListData(selectedZone: String?) {
        AppDialogs.showProgressDialog(this, "Please wait")

        var detailslist =
            DatabaseClient.getInstance(applicationContext).appDatabase.deviceDAO!!.getSelectedname(
                selectedZone
            )
        if (!detailslist.isEmpty()) {
            for (i in 0 until detailslist.size) {
                RServiceCall = "ZoneCall"
                ThingsManager.getrelations(
                    this!!,
                    detailslist.get(i).deviceid,
                    "ASSET",
                    Saccount = "Smart"
                )
            }
        }
    }

    override fun onResponse(r: Response?) {
        try {
            if (r == null) {
            }

            if (r!!.message == "token expired") {
            }

            when (r.requestType) {
                ThingsManager.API.getRelatedDeviceFromAsset.hashCode() -> {
                    if (r is FromAddress) {
                        if (RServiceCall == "ZoneCall") {
                            if (!r.fromList.isNullOrEmpty()) {
                                var regions = DatabaseClient.getInstance(applicationContext)
                                    .appDatabase
                                    .deviceDAO!!.getSelectedname(SelectedZone)
                                if (!regions.isEmpty()) {
                                    for (k in 0 until r.fromList!!.size) {
//                                        var details =
//                                            regionDevicesDAO!!.getSelectedDevices(r.fromList!!.get(k).from!!.id!!)
                                        var devname = SelectedZone
                                        var dsize = r.fromList!!.size
//                                        for (i in 0 until dsize) {
                                        var zdevice =
                                            DatabaseClient.getInstance(applicationContext)
                                                .appDatabase
                                                .zoneDeviceDAO!!.getZsDevices(
                                                    r.fromList!!.get(k).to!!.id!!,
                                                    devname
                                                )
                                        if (zdevice.isEmpty()) {
                                            var zoneDevices = ZoneDevices()
                                            zoneDevices.deviceid = r.fromList!!.get(k).to!!.id!!
                                            zoneDevices.regionname = devname
                                            zoneDevices.devicename = ""
                                            zoneDevices.devicetype = ""
                                            DatabaseClient.getInstance(applicationContext).appDatabase.zoneDeviceDAO!!.insert(
                                                zoneDevices
                                            )
                                        }
//                                        }
                                    }
                                }
                            }
//                            AppDialogs.showProgressDialog(
//                                this,
//                                "Please wait connecting to server.."
//                            )
                            var zonedetailslist =
                                DatabaseClient.getInstance(applicationContext).appDatabase
                                    .zoneDeviceDAO!!.devices
                            if (!zonedetailslist.isEmpty()) {
                                for (i in 0 until zonedetailslist.size) {
                                    DServiceCall = "Zone"
                                    ThingsManager.getassetdetails(
                                        this,
                                        zonedetailslist.get(i).deviceid, Saccount = "Smart"
                                    )
                                }
                            }
                        } else if (RServiceCall == "WardCall") {

                            if (!r.fromList.isNullOrEmpty()) {
                                var dada =
                                    DatabaseClient.getInstance(applicationContext)
                                        .appDatabase
                                        .wardNumbersDAO!!.getwDevices(r.fromList!!.get(0).to!!.id!!)
                                if (dada.isEmpty()) {
                                    var details =
                                        DatabaseClient.getInstance(applicationContext)
                                            .appDatabase
                                            .deviceDAO!!.devices
                                    if (!details.isEmpty()) {
                                        for (k in 0 until details.size) {
                                            var devname = details.get(k).devicename
                                            var dsize = r.fromList!!.size
                                            for (i in 0 until dsize) {
                                                var wdevice =
                                                    DatabaseClient.getInstance(applicationContext)
                                                        .appDatabase
                                                        .wardNumbersDAO!!.getwSDevices(
                                                            r.fromList!!.get(i).to!!.id!!,
                                                            devname
                                                        )
                                                var mon =
                                                    DatabaseClient.getInstance(applicationContext)
                                                        .appDatabase
                                                        .wardNumbersDAO!!.getwDevices(
                                                            r.fromList!!.get(
                                                                i
                                                            ).to!!.id!!
                                                        )
                                                if (mon.isEmpty()) {
                                                    if (wdevice.isEmpty()) {
                                                        var wardDevices = WardDevices()
                                                        wardDevices.deviceid =
                                                            r.fromList!!.get(i).to!!.id!!
                                                        wardDevices.zonename = devname
                                                        wardDevices.devicename = ""
                                                        wardDevices.devicetype = ""
                                                        DatabaseClient.getInstance(
                                                            applicationContext
                                                        ).appDatabase
                                                            .wardNumbersDAO!!.insert(wardDevices)
                                                        Toast.makeText(
                                                            applicationContext,
                                                            "Please wait for Few minutes",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    var warddetailslist =
                                        DatabaseClient.getInstance(applicationContext)
                                            .appDatabase
                                            .wardNumbersDAO!!.getsdwardDevices("")
                                    if (!warddetailslist.isEmpty()) {
                                        for (i in 0 until warddetailslist.size) {
                                            DServiceCall = "Ward"

                                            var dlist =
                                                DatabaseClient.getInstance(applicationContext)
                                                    .appDatabase
                                                    .data!!.getallDevices(warddetailslist.get(i).deviceid);
                                            if (dlist.isEmpty()) {

                                                ThingsManager.getassetdetails(
                                                    this,
                                                    warddetailslist.get(i).deviceid,
                                                    Saccount = "Smart"
                                                )
                                                var dData = DData()
                                                dData.deviceid = warddetailslist.get(i).deviceid
                                                DatabaseClient.getInstance(applicationContext)
                                                    .appDatabase.data!!.insert(dData)
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Please wait for Few minutes",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ThingsManager.API.getAssetName.hashCode() -> {
                    if (DServiceCall == "Zone") {
                        if (r is AssetDetails) {
                            Toast.makeText(
                                applicationContext,
                                "Please wait for Few minutes",
                                Toast.LENGTH_SHORT
                            ).show()
                            DatabaseClient.getInstance(applicationContext).appDatabase
                                .zoneDeviceDAO!!.updatedevicezone(r.name, r.type, r.id!!.id!!)
                        }
                        var delist = DatabaseClient.getInstance(applicationContext)
                            .getAppDatabase().zoneDeviceDAO!!.getNUllDevices("");
                        if (delist.isEmpty()) {
                            var zonedetailslist =
                                DatabaseClient.getInstance(applicationContext)
                                    .appDatabase.zoneDeviceDAO!!.devices
                            if (zonedetailslist.isNotEmpty()) {
                                for (i in 0 until zonedetailslist.size) {
                                    RServiceCall = "WardCall"
                                    Toast.makeText(
                                        applicationContext,
                                        "Please wait for Few minutes",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    ThingsManager.getrelations(
                                        this!!,
                                        zonedetailslist.get(i).deviceid,
                                        "ASSET", Saccount = "Smart"
                                    )
                                }
                            }
                        } else {

                        }
                    } else if (DServiceCall == "Ward") {
                        if (r is AssetDetails) {
                            var derev = r.name!!.split("-")
                            Toast.makeText(
                                applicationContext,
                                "Please wait for Few minutes",
                                Toast.LENGTH_SHORT
                            ).show()
                            DatabaseClient.getInstance(applicationContext)
                                .appDatabase.wardNumbersDAO!!.updatedeviceward(
                                    r.name,
                                    r.type,
                                    derev[0] + "-" + derev[1],
                                    r.id!!.id!!
                                )
                            var totaldata = DatabaseClient.getInstance(applicationContext)
                                .appDatabase.wardNumbersDAO!!.getsdwardDevices("")
                            if (totaldata.isEmpty()) {

                                Toast.makeText(
                                    applicationContext,
                                    "Succesfully Details Fetched",
                                    Toast.LENGTH_SHORT
                                ).show()

                                listDataHeader = ArrayList()
                                listDataChild = HashMap()

                                var zoneListData =
                                    DatabaseClient.getInstance(applicationContext).appDatabase.zoneDeviceDAO.devices
                                for (i in 0 until zoneListData.size) {
                                    // Adding child data
                                    (listDataHeader as ArrayList<String>).add(zoneListData.get(i).devicename)

                                    var wardListData =
                                        DatabaseClient.getInstance(applicationContext).appDatabase.wardNumbersDAO.getZoneDevices(
                                            zoneListData.get(i).devicename
                                        )

                                    // Adding child data
                                    val top250: MutableList<String> = ArrayList()
                                    for (i in 0 until wardListData.size) {
                                        top250.add(wardListData.get(i).devicename)
                                    }

                                    listDataChild!![(listDataHeader as ArrayList<String>).get(i)] =
                                        top250 // Header, Child data
                                }

                                listAdapter =
                                    ExpandableListAdapter(this, listDataHeader!!, listDataChild!!)
                                expListView!!.setAdapter(listAdapter)
                            } else {

                            }
                        }
                        AppDialogs.hideProgressDialog()
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}