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
import com.example.smartlights.localdatabase.AssetDevices;
import com.example.smartlights.localdatabase.AssetDevicesDAO;
import com.example.smartlights.localdatabase.DatabaseClient;
import com.example.smartlights.smartapps.adapter.CCMSAdapter;
import com.example.smartlights.smartapps.basepojo.CCMS;

import java.util.ArrayList;
import java.util.List;

public class CCMSDialog extends Dialog {

    CallBack mCallBack;
    Context mContext;
    ListView pType;
    String ManufacturerData;
    ArrayList LiveData1;
    CCMSAdapter ccmsAdapter;
    List<AssetDevices> wards;
    List<AssetDevices> sswards;

    public CCMSDialog(Context context) {
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
        setContentView(R.layout.ccms_dialog);

        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        LiveData1 = new ArrayList();
        List<AssetDevices> dlist = DatabaseClient.getInstance(mContext).getAppDatabase().devicesDAO().getsdlistDevices("NO");
        sswards = DatabaseClient.getInstance(mContext).getAppDatabase().devicesDAO().getsdlistDevices("NO");
        wards = DatabaseClient.getInstance(mContext).getAppDatabase().devicesDAO().getsdlistDevices("NO");


        pType = (ListView) findViewById(R.id.manufaturerList);
        filldata();

        pType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ManufacturerData = LiveData1.get(position).toString();
                String Data = LiveData1.get(position).toString();
                mCallBack.CCMSDetails(Data);
            }
        });
    }

    private void filldata() {
        ArrayList<CCMS> Manufacturer = new ArrayList<CCMS>();
        LiveData1.clear();
        for (int u = 0; u < wards.size(); u++) {
            String data = wards.get(u).getCcmsname();
            CCMS assetDevices = new CCMS(data);
            LiveData1.add(data);
            Manufacturer.add(assetDevices);
        }

        ccmsAdapter = new CCMSAdapter(mContext, Manufacturer);
        pType.setAdapter(ccmsAdapter);
    }

    public interface CallBack {
        public void CCMSDetails(String CCMSData);
    }
}
