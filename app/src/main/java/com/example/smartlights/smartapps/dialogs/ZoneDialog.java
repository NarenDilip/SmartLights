package com.example.smartlights.smartapps.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.room.Room;


import com.example.smartlights.R;
import com.example.smartlights.localdatabase.AppDatabase;
import com.example.smartlights.localdatabase.DatabaseClient;
import com.example.smartlights.localdatabase.ZoneDevices;
import com.example.smartlights.localdatabase.ZoneDevicesDAO;
import com.example.smartlights.smartapps.adapter.ZoneAdapter;
import com.example.smartlights.smartapps.basepojo.Zone;
import com.example.smartlights.utils.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class ZoneDialog extends Dialog {

    CallBack mCallBack;
    Context mContext;
    ListView pType;
    String ManufacturerData;
    String mData;
    AppPreference appPreference;
    ArrayList LiveData1;
    String SelectedRegion;
    List<ZoneDevices> zones;

    ZoneAdapter zoneTypeSelectAdapter;

    public ZoneDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setCallBack(CallBack mCallBackDialog) {
        this.mCallBack = mCallBackDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zone_dialog);
        LiveData1 = new ArrayList();

        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        SelectedRegion = pref.getString("Region", null);

        zones = DatabaseClient.getInstance(mContext).getAppDatabase().getZoneDeviceDAO().getRegionDevices(SelectedRegion);

        pType = (ListView) findViewById(R.id.manufaturerList);
        filldata();

        pType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ManufacturerData = LiveData1.get(position).toString();
                String Data = LiveData1.get(position).toString();
                mCallBack.ZoneDetails(Data);
            }
        });
    }

    private void filldata() {

        ArrayList<Zone> Manufacturer = new ArrayList<Zone>();
        LiveData1.clear();
        for (int u = 0; u < zones.size(); u++) {
            String data = zones.get(u).getDevicename();
            Zone vehcileManufacturer = new Zone(data);
            Manufacturer.add(vehcileManufacturer);
            LiveData1.add(data);
        }

        zoneTypeSelectAdapter = new ZoneAdapter(mContext, Manufacturer);
        pType.setAdapter(zoneTypeSelectAdapter);
    }

    public interface CallBack {
        public void ZoneDetails(String ManufacturerData);

    }
}