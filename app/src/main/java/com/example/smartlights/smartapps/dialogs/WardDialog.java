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
import com.example.smartlights.localdatabase.WardDevices;
import com.example.smartlights.localdatabase.WardDevicesDAO;
import com.example.smartlights.smartapps.adapter.WardAdapter;
import com.example.smartlights.smartapps.basepojo.Ward;

import java.util.ArrayList;
import java.util.List;

public class WardDialog extends Dialog {

    CallBack mCallBack;
    Context mContext;
    ListView pType;
    String ManufacturerData;
    String mData;
    String SelectedWard;
    ArrayList LiveData1;
    WardAdapter wardTypeSelectAdapter;
    List<WardDevices> wards;
    List<WardDevices> sswards;

    public WardDialog(Context context) {
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
        setContentView(R.layout.ward_dialog);

        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        SelectedWard = pref.getString("Zone", null);

        LiveData1 = new ArrayList();
        List<WardDevices> dlist = DatabaseClient.getInstance(mContext).getAppDatabase().getWardNumbersDAO().getwardDevices();
        sswards = DatabaseClient.getInstance(mContext).getAppDatabase().getWardNumbersDAO().getwardDevices();
        wards = DatabaseClient.getInstance(mContext).getAppDatabase().getWardNumbersDAO().getZoneDevices(SelectedWard);


        pType = (ListView) findViewById(R.id.manufaturerList);
        filldata();

        pType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ManufacturerData = LiveData1.get(position).toString();
                String Data = LiveData1.get(position).toString();
                mCallBack.WardDetails(Data);
            }
        });
    }

    private void filldata() {
        ArrayList<Ward> Manufacturer = new ArrayList<Ward>();
        LiveData1.clear();
        for (int u = 0; u < wards.size(); u++) {
            String data = wards.get(u).getDevicename();
            Ward vehcileManufacturer = new Ward(data);
            LiveData1.add(data);
            Manufacturer.add(vehcileManufacturer);
        }

        wardTypeSelectAdapter = new WardAdapter(mContext, Manufacturer);
        pType.setAdapter(wardTypeSelectAdapter);
    }

    public interface CallBack {
        public void WardDetails(String ManufacturerData);
    }
}
