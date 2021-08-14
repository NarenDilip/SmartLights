package com.schnell.smartlights.smartapps.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.schnell.smartlights.R
import com.schnell.smartlights.utils.AppPreference
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.client.ClientProtocolException
import java.io.IOException

/**
 * Created by schnell on 22,July,2021
 */
class TableViewTest : AppCompatActivity() {
    var scrollView: ScrollView? = null
    var webView: WebView? = null
    var pref: SharedPreferences? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        //final ProgressDialog pd = ProgressDialog.show(this, "", "Loading...", true);
        val url = "myurl"
        webView = findViewById<View>(R.id.webloader) as WebView
        webView!!.isVerticalScrollBarEnabled = true
        webView!!.isHorizontalScrollBarEnabled = true
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.domStorageEnabled = true

//        webView!!.webViewClient = wvc
        webView!!.loadUrl("http://iotpro.io:8077/dashboardGroups/25d3de10-9ea0-11eb-83d7-095ba5239364/2b744c10-9ea0-11eb-83d7-095ba5239364")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView!!.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView!!.restoreState(savedInstanceState)
    }

    private fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            when (extension) {
                "js" -> return "text/javascript"
                "woff" -> return "application/font-woff"
                "woff2" -> return "application/font-woff2"
                "ttf" -> return "application/x-font-ttf"
                "eot" -> return "application/vnd.ms-fontobject"
                "svg" -> return "image/svg+xml"
            }
            type = "application/json"
        }
        return type
    }

    var wvc: WebViewClient = object : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
            return try {

                var tokenaddress = AppPreference.get(
                    applicationContext,
                    AppPreference.Key.accessToken,
                    ""
                )

                val okHttpClient = OkHttpClient()
                val request = Request.Builder()
                    .url("http://iotpro.io:8077/login")
                    .addHeader(
                        "X-Authorization",
                        "Bearer $tokenaddress"
                    )
                    .build()
                val response = okHttpClient.newCall(request).execute()
                WebResourceResponse(
                    getMimeType("http://iotpro.io:8077/login"),  // You can set something other as default content-type
                    response.header(
                        "content-type",
                        "utf-8"
                    ),  // Again, you can set another encoding as default
                    response.body()!!.byteStream()
                )
            } catch (e: ClientProtocolException) {
                //return null to tell WebView we failed to fetch it WebView should try again.
                null
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
}