package com.example.smartlights.smartapps.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlights.utils.AppPreference
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    private var selectedState: String? = null
    private var OnScanner: String? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        OnScanner = AppPreference.get(
            applicationContext,
            "OnScan",
            ""
        )

        selectedState = intent.getStringExtra("key")
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)                // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        if (selectedState == OnScanner) {
            Toast.makeText(applicationContext, rawResult.text, Toast.LENGTH_SHORT).show()
            AppPreference.put(this, OnScanner!!, rawResult.text)
            onBackPressed()
        }else if(selectedState == "Ilm"){
            Toast.makeText(applicationContext, rawResult.text, Toast.LENGTH_SHORT).show()
            AppPreference.put(this, "Ilm", rawResult.text)
            onBackPressed()
        }
    }

    // If you would like to resume scanning, call this method below:
    //mScannerView.resumeCameraPreview(this);
}