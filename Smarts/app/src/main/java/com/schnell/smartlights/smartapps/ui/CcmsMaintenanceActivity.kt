package com.schnell.smartlights.smartapps.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.CookieSyncManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.schnell.smartlights.R
import com.schnell.smartlights.utils.AppPreference

/**
 * Created by schnell on 22,April,2021
 */

class CcmsMaintenanceActivity : Activity() {

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        webView = findViewById(R.id.webloader)
        progressBar = findViewById(R.id.progressBar)

        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.domStorageEnabled = true

        webView!!.getSettings().setJavaScriptEnabled(true)
        webView!!.setWebChromeClient(WebChromeClient())

        progressBar!!.visibility = View.VISIBLE

        val settings = webView!!.settings
        settings.domStorageEnabled = true

//        webView!!.settings.setSaveFormData(true);
//        webView!!.settings.setSavePassword(true);
//        webView!!.settings.setDomStorageEnabled(true);
//        webView!!.settings.setJavaScriptEnabled(true);

        CookieSyncManager.createInstance(webView!!.context);
        CookieSyncManager.getInstance().sync();

        val tokenaddress = AppPreference.get(
            applicationContext,
            AppPreference.Key.accessToken,
            ""
        )

        val map = HashMap<String, String>()
        map["X-Authorization"] = tokenaddress.toString()

//        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = MyBrowser(progressBar!!)
        webView!!.settings.setSupportMultipleWindows(true)
        //local server
//        webView!!.loadUrl("http://iotpro.io:8077/dashboardGroups/25d3de10-9ea0-11eb-83d7-095ba5239364/5d17c790-fa96-11eb-bde0-ed9c29ba0916", map)
        //live server
        webView!!.loadUrl("http://schnelliot.in/dashboardGroups/ab56d7b0-ef66-11eb-8a35-d3944034ebfd/32404280-fbfa-11eb-85f1-8dafaa1d1e28", map)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    class MyBrowser(progressBar: ProgressBar) : WebViewClient() {
        var progressBar = progressBar
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
//            view!!.loadUrl("javascript:document.getElementsByName('username').value = 'username'");
//            view!!.loadUrl("javascript:document.getElementsByName('password').value = 'password'");
//            view!!.loadUrl("javascript:document.forms['login'].submit()");

//            view!!.loadUrl(
//                "https://schnelliot.in"
//            )
            progressBar.visibility = View.GONE
        }
    }
}


