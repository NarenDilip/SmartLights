package com.example.smartlights.smartapps.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewClientImpl(activity: Activity?) : WebViewClient() {
    private var activity: Activity? = null
    override fun shouldOverrideUrlLoading(

        webView: WebView,
        url: String
    ): Boolean {

        if (url.indexOf("schnelliot.in") > -1) return false
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity!!.startActivity(intent)
        return true
    }

    init {
        this.activity = activity
    }
}
