package com.schnell.smartlights.smartapps.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.schnell.smartlights.R;
import com.schnell.smartlights.localdatabase.DatabaseClient;
import com.schnell.smartlights.localdatabase.RegionDevices;
import com.schnell.smartlights.smartapps.adapter.RegionAdapter;
import com.schnell.smartlights.smartapps.basepojo.Region;
import com.schnell.smartlights.utils.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class RegionDialog extends Dialog {

    CallBack mCallBack;
    Context mContext;
    ListView pType;
    String ManufacturerData;
    String mData;
    AppPreference appPreference;
    ArrayList LiveData1;
    String SelectedRegion;
    List<RegionDevices> zones;

    RegionAdapter zoneTypeSelectAdapter;

    public RegionDialog(Context context) {
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

        zones = DatabaseClient.getInstance(mContext).getAppDatabase().getDeviceDAO().getDevices();

        pType = (ListView) findViewById(R.id.manufaturerList);
        filldata();

        pType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ManufacturerData = LiveData1.get(position).toString();
                String Data = LiveData1.get(position).toString();
                mCallBack.RegionDetails(Data);
            }
        });
    }

    private void filldata() {

        ArrayList<Region> Manufacturer = new ArrayList<Region>();
        LiveData1.clear();
        for (int u = 0; u < zones.size(); u++) {
            String data = zones.get(u).getDevicename();
            Region vehcileManufacturer = new Region(data);
            Manufacturer.add(vehcileManufacturer);
            LiveData1.add(data);
        }

        zoneTypeSelectAdapter = new RegionAdapter(mContext, Manufacturer);
        pType.setAdapter(zoneTypeSelectAdapter);
    }

    public interface CallBack {
        public void RegionDetails(String ManufacturerData);

    }
}