package com.example.smartlights.smartapps.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlights.R


/**
 * Created by schnell on 22,April,2021
 */

class MaintenanceActivity : AppCompatActivity() {

    private var WebLoader: WebView? = null
    var mProgress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        WebLoader = findViewById<WebView>(R.id.webloader)

        // the init state of progress dialog
        mProgress = findViewById<ProgressBar>(R.id.progressBar);
        mProgress!!.setVisibility(View.VISIBLE);

        WebLoader!!.clearHistory()
        WebLoader!!.clearCache(true)
        WebLoader!!.setClickable(false)
        WebLoader!!.setFocusable(false)
        WebLoader!!.setFocusableInTouchMode(false)

        val webSettings: WebSettings = WebLoader!!.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.lightTouchEnabled = true
        webSettings.builtInZoomControls = false
        webSettings.setSupportZoom(false)

        WebLoader!!.setWebChromeClient(object : WebChromeClient() {
            // this will be called on page loading progress
            override fun onProgressChanged(view: WebView, newProgress: Int) {

                //super.onProgressChanged(view, newProgress);
                mProgress!!.setProgress(newProgress)

                // hide the progress bar if the loading is complete
                if (newProgress == 100) {

                    /* call after laoding splash.html  */
                    WebLoader!!.loadUrl("javascript:_fully_loaded()")
                    WebLoader!!.setVisibility(View.VISIBLE)
                    mProgress!!.setVisibility(View.GONE)
                }
            }
        })

        WebLoader!!.setWebViewClient(WebViewClient())

        /* load splash screen */

        /* load splash screen */
        WebLoader!!.loadUrl("http://iotpro.io:8077")

//        val webSettings: WebSettings = WebLoader!!.settings
//        webSettings.javaScriptEnabled = true
//
//        val webViewClient = WebViewClientImpl(this)
//        WebLoader!!.webViewClient = webViewClient
//
//        WebLoader!!.settings.loadsImagesAutomatically = true
//        WebLoader!!.settings.allowContentAccess = true
//
//        WebLoader!!.loadUrl("http://iotpro.io:8077")
//        WebLoader!!.settings.useWideViewPort = true
//        WebLoader!!.settings.loadWithOverviewMode = true
//        WebLoader!!.settings.domStorageEnabled = true
//        WebLoader!!.isHorizontalScrollBarEnabled = false
//        WebLoader!!.settings.databaseEnabled = true
//
//        WebLoader!!.isVerticalScrollBarEnabled = false
//        WebLoader!!.settings.builtInZoomControls = true
//        WebLoader!!.settings.displayZoomControls = false
//        WebLoader!!.settings.allowFileAccess = true
//        WebLoader!!.isScrollbarFadingEnabled = false
//        WebLoader!!.settings.cacheMode = WebSettings.LOAD_NO_CACHE
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(0);
//            WebLoader!!.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebLoader!!.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            WebLoader!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//
//        WebLoader!!.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                WebLoader!!.loadUrl("javascript:document.getElementById('Username').value = 'smartCity@schnellenergy.com'")
//                WebLoader!!.loadUrl("javascript:document.getElementById('Password').value = 'smart@City135'")
//                WebLoader!!.loadUrl("javascript:document.forms['Login'].submit()")
//
//                WebLoader!!.webViewClient = MyWebViewClient();
//            }
//        }
//        WebLoader!!.setInitialScale(1)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ReflectorActivity::class.java))
        finish()
    }
}