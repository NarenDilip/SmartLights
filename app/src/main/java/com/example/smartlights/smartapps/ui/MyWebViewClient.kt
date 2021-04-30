package com.example.smartlights.smartapps.ui

import android.webkit.HttpAuthHandler
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient : WebViewClient() {
    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler, host: String?, realm: String?
    ) {
        handler.proceed("smartCity@schnellenergy.com", "smart@City135")
    }
}
