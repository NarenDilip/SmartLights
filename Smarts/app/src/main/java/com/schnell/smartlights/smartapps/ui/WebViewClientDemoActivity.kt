package com.schnell.smartlights.smartapps.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.annotation.RequiresApi
import com.schnell.smartlights.R
import com.schnell.smartlights.utils.AppPreference

/**
 * Created by schnell on 22,July,2021
 */ /*
 * Demo of creating an application to open any URL inside the application and clicking on any link from that URl
should not open Native browser but  that URL should open in the same screen.
 */
class WebViewClientDemoActivity : Activity() {
    /**
     * Called when the activity is first created.
     */
    var web: WebView? = null

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        val web = findViewById<View>(R.id.webloader) as WebView
        web.settings.javaScriptEnabled = true
        web.settings.saveFormData = false

        var tokenaddress = AppPreference.get(
            applicationContext,
            AppPreference.Key.accessToken,
            ""
        )

//        web.webViewClient = myWebClient()
//        web.settings.domStorageEnabled = true
//        web.settings.setUserAgentString("user-agent-string")
//
//        web.settings.domStorageEnabled = true
//        web.settings.databaseEnabled = true
//        web.settings.minimumFontSize = 1
//        web.settings.minimumLogicalFontSize = 1
//
//        val bearer = "Bearer $tokenaddress"
//
//        val headerMap = HashMap<String, String>()
//        headerMap["X-Authorization"] = bearer
//        web.loadUrl("http://iotpro.io:8077/login/", headerMap)

    }
}

//        CookieSyncManager.createInstance(this)
//        CookieSyncManager.getInstance().startSync()
//        val cookieManager = CookieManager.getInstance()
//        cookieManager.setAcceptCookie(true)
//        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true)
//        val extraHeaders: MutableMap<String, String> = HashMap()
//        extraHeaders["Authorization"] = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYW1rdW1hckBzY2huZWxsZW5lcmd5LmNvbSIsInNjb3BlcyI6WyJURU5BTlRfQURNSU4iXSwidXNlcklkIjoiNmM0ZTZiZDAtNGZkMS0xMWViLWE2ODAtZGIwZDQ3YjAxNzVjIiwiZW5hYmxlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJ0ZW5hbnRJZCI6IjVmNzQwNDEwLTRmZDEtMTFlYi1hNjgwLWRiMGQ0N2IwMTc1YyIsImN1c3RvbWVySWQiOiIxMzgxNDAwMC0xZGQyLTExYjItODA4MC04MDgwODA4MDgwODAiLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTYyNjk1MjY0OCwiZXhwIjoxNjI2OTYxNjQ4fQ.40_iza4R5-iE7saa0mxtp4VqCukLyWvbYG4md8HDqIjdzbmy8BwFvkXwc0rv9ICOIJDc25M0D0h7Y5CdYZCdGQ"
//        web.settings.javaScriptEnabled = true


//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("X-Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYW1rdW1hckBzY2huZWxsZW5lcmd5LmNvbSIsInNjb3BlcyI6WyJURU5BTlRfQURNSU4iXSwidXNlcklkIjoiNmM0ZTZiZDAtNGZkMS0xMWViLWE2ODAtZGIwZDQ3YjAxNzVjIiwiZW5hYmxlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJ0ZW5hbnRJZCI6IjVmNzQwNDEwLTRmZDEtMTFlYi1hNjgwLWRiMGQ0N2IwMTc1YyIsImN1c3RvbWVySWQiOiIxMzgxNDAwMC0xZGQyLTExYjItODA4MC04MDgwODA4MDgwODAiLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTYyNjk0NzA1MywiZXhwIjoxNjI2OTU2MDUzfQ.2o7hsUwW4WvkKg2UJBflWvDL3jJ4o-CZZJNBR7pOQrG6i4YlIPPFaMUUwJ1Xuc-zvkJV7CSaw2vCf4kadCGg8A");
//
//        web.getSettings().setAppCacheEnabled(true);
//        web.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        web.loadUrl("http://iotpro.io:8077/login/", extraHeaders);


        //        web = (WebView) findViewById(R.id.webloader);
//        web.setWebViewClient(new myWebClient());
//        web.getSettings().setJavaScriptEnabled(true);
//        web.getSettings().setDomStorageEnabled(true);
//        web.getSettings().setUserAgentString("user-agent-string");
//        web.getSettings().setJavaScriptEnabled(true);
//        web.getSettings().setDomStorageEnabled(true);
//        web.getSettings().setDatabaseEnabled(true);
//        web.getSettings().setMinimumFontSize(1);
//        web.getSettings().setMinimumLogicalFontSize(1);
//
//        web.loadUrl("http://iotpro.io:8077/login");
//    }
//        class myWebClient : WebViewClient() {
////            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
////                // TODO Auto-generated method stub
//////                super.onPageStarted(view, "http://iotpro.io:8077/login", favicon)
////            }
//
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                val extraHeaders: MutableMap<String, String> = HashMap()
//                extraHeaders["Authorization"] = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYW1rdW1hckBzY2huZWxsZW5lcmd5LmNvbSIsInNjb3BlcyI6WyJURU5BTlRfQURNSU4iXSwidXNlcklkIjoiNmM0ZTZiZDAtNGZkMS0xMWViLWE2ODAtZGIwZDQ3YjAxNzVjIiwiZW5hYmxlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJ0ZW5hbnRJZCI6IjVmNzQwNDEwLTRmZDEtMTFlYi1hNjgwLWRiMGQ0N2IwMTc1YyIsImN1c3RvbWVySWQiOiIxMzgxNDAwMC0xZGQyLTExYjItODA4MC04MDgwODA4MDgwODAiLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTYyNjk1MjY0OCwiZXhwIjoxNjI2OTYxNjQ4fQ.40_iza4R5-iE7saa0mxtp4VqCukLyWvbYG4md8HDqIjdzbmy8BwFvkXwc0rv9ICOIJDc25M0D0h7Y5CdYZCdGQ"
//                view.loadUrl("http://iotpro.io:8077/login", extraHeaders)
//                return super.shouldOverrideUrlLoading(view, "http://iotpro.io:8077/login")
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, "http://iotpro.io:8077/login")
//                view.loadUrl("javascript:var x = document.getElementById('Username').value = 'ramkumar@schnellenergy.com'")
//                view.loadUrl("javascript:var y = document.getElementById('Password').value= 'Schnell'")
//                view.loadUrl("javascript:document.getElementById('Login').submit()")
//            }
//        }
//    }
