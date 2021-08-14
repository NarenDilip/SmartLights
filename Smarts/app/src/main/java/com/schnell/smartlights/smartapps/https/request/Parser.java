package com.schnell.smartlights.smartapps.https.request;

import android.util.Log;

import com.schnell.smartlights.smartapps.https.pojo.ScannerResponse;
import com.schnell.smartlights.smartapps.https.response.BaseRS;
import com.schnell.smartlights.smartapps.https.response.LoginBaseRs;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;


/**
 * Created by narendran on 7/5/16.
 */
public class Parser {
//
//    public static synchronized LoginBaseRs parseViewResponse(String response) {
//        try {
//
//            String res = response.toString();
//            Log.i("Login Sucess", "======>" + res);
//
//            Gson gson = new Gson();
//            // converting or parsing the content
//            LoginBaseRs out = gson.fromJson(res, LoginBaseRs.class);
//            Log.i("Reg", "======>" + out.toString());
//            return out;
//
//        } catch (JsonParseException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IllegalStateException e) {
//            Log.e("", "MyResponse WS Parsing failed in Parser");
//            e.printStackTrace();
//            return null;
//        } catch (Exception e) {
//            Log.e("", "MyResponse WS Parsing failed in Parser");
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static synchronized LoginBaseRs parseViewResponse(String response) {

        try {
            String res = response.toString();
            Log.i("Login Sucess", "======>" + res);

            Gson gson = new Gson();
            // converting or parsing the content
            LoginBaseRs out = gson.fromJson(res, LoginBaseRs.class);
            Log.i("Reg", "======>" + out.toString());
            return out;

        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalStateException e) {
            Log.e("", "MyResponse WS Parsing failed in Parser");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e("", "MyResponse WS Parsing failed in Parser");
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized BaseRS OtpResponse(String response) {

        try {
            String res = response.toString();
            Log.i("Login Sucess", "======>" + res);

            Gson gson = new Gson();
            // converting or parsing the content
            BaseRS out = gson.fromJson(res, BaseRS.class);
            Log.i("Reg", "======>" + out.toString());
            return out;

        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalStateException e) {
            Log.e("", "MyResponse WS Parsing failed in Parser");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e("", "MyResponse WS Parsing failed in Parser");
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized ScannerResponse ScanCodeResponse(String response) {
        try {

            String res = response.toString();
            Log.i("Login Sucess", "======>" + res);

            Gson gson = new Gson();
            // converting or parsing the content
            ScannerResponse out = gson.fromJson(res, ScannerResponse.class);
            Log.i("Reg", "======>" + out.toString());
            return out;

        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalStateException e) {
            Log.e("", "MyResponse WS Parsing failed in Parser");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e("", "MyResponse WS Parsing failed in Parser");
            e.printStackTrace();
            return null;
        }
    }

}
