package com.schnell.smartlights.smartapps.https.response;


import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.schnell.smartlights.smartapps.https.StringFormatter;
import com.schnell.smartlights.smartapps.https.request.Parser;
import com.schnell.smartlights.utils.BaseActivity;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewRestClient {

    private String url;
    private JSONObject data;
    public static final String URL_PWD = "medic:medic";
    private int requestType;
    private ArrayList<NameValuePair> headers;
    private ArrayList<NameValuePair> params;
    BaseActivity utility;
    final String basicAuth = "Basic "
            + Base64.encodeToString(URL_PWD.getBytes(), Base64.NO_WRAP);
    private static RequestQueue queue;

    public NewRestClient(String url, int requestType) {
        this.url = url;
        this.data = data;
        this.requestType = requestType;
        headers = new ArrayList<NameValuePair>();
        params = new ArrayList<NameValuePair>();
    }

    public void addParam(String name, String value, String S) {
        if (S == "Y") {
            try {
                data = new JSONObject(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.add(new BasicNameValuePair(name, value));
        } else {
            params.add(new BasicNameValuePair(name, value));
        }
    }

    public void addHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void execute(Context activity) throws Exception {
        Log.d("", "Request params " + url);

        for (NameValuePair p : params) {
            Log.i("", "Setting param :" + p.getName() + " = " + p.getValue());

        }
        if (utility.internetIsAvailable(activity)) {
            postData(url, data, activity, (NewResponseListener) activity);
        } else {
            utility.hideProgressDialog(activity);
            utility.showConnectionNotAvailable(activity);
        }
    }

    public void executes(Context activity) throws Exception {
        Log.d("", "Request params " + url);

        for (NameValuePair p : params) {
            Log.i("", "Setting param :" + p.getName() + " = " + p.getValue());

        }
        if (utility.internetIsAvailable(activity)) {
            posterData(url, activity, (NewResponseListener) activity);
        } else {
            utility.hideProgressDialog(activity);
            utility.showConnectionNotAvailable(activity);
        }
    }

    private void posterData(String url, final Context activity, final NewResponseListener resplist) {

        try {
            queue = Volley.newRequestQueue(activity);
            int timeout = 30000; // 30 seconds - time out

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response.toString());
//                            Object object = invokeParser(response, requestType);
                            resplist.onResponseReceived(response, requestType);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", "" + error.getMessage());
                    utility.hideProgressDialog(activity);
                  /*  if (error.getMessage() == null) {
                        utility.showAlertDialog(activity, "Connection Problem. Please try after some time");
                    }*/
                    resplist.onResponseReceived(null, requestType);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type",
                            "application/x-www-form-urlencoded");
                    //headers.put("Authorization", basicAuth);
                    headers.put("Accept-Encoding", "");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> pvalues = new HashMap<String, String>();
                    for (NameValuePair p : params) {
                        pvalues.put("" + p.getName(), "" + p.getValue());
                    }
                    return pvalues;
                }
            };

            postRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            System.setProperty("http.keepAlive", "false");

            queue.add(postRequest);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void postData(String url, final JSONObject data, final Context activity, final NewResponseListener resplist) {
        int timeout = 80000;
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//            String URL = "http://...";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("firstkey", "firstvalue");
//            jsonBody.put("secondkey", "secondobject");
        final String mRequestBody = data.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                resplist.onResponseReceived(response, requestType);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";

                if (response != null) {
                    try {
                        String str = new String(response.data, "UTF-8");
                        String detals = str.replace("\\", "");

                        String normal = StringFormatter.convertUTF8ToString(detals);
                        responseString = String.valueOf(normal);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        System.setProperty("http.keepAlive", "false");

        requestQueue.add(stringRequest);


//        int timeout = 80000;
//        RequestQueue requstQueue = Volley.newRequestQueue(activity);
//
//        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url, data,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        resplist.onResponseReceived(response, requestType);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (resplist != null) {
//                            String s = error.getMessage();
//                            Object obj = s;
//                            resplist.onResponseReceived(null, requestType);
//                        }
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                if (data != null) {
//                    Iterator<String> keysItr = data.keys();
//                    while (keysItr.hasNext()) {
//                        String key = keysItr.next();
//                        Object value = null;
//                        try {
//                            value = data.get(key);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (value instanceof JSONArray) {
//                            value = ((JSONArray) value);
//                        } else if (value instanceof JSONObject) {
//                            value = ((JSONObject) value);
//                        }
//                        params.put(key, (String) value);
//                    }
//                }
//                return params;
//            }
//        };
//
//        jsonobj.setRetryPolicy(new DefaultRetryPolicy(timeout,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        System.setProperty("http.keepAlive", "false");
//
//        requstQueue.add(jsonobj);

    }

    private Object invokeParser(String response, int requestType) {
        switch (requestType) {
            case 0:
                return Parser.ScanCodeResponse(response);
            case 1:
                return Parser.parseViewResponse(response);
            case 2:
                return Parser.OtpResponse(response);
            case 3:
                return Parser.ScanCodeResponse(response);
            case 5:
                return Parser.ScanCodeResponse(response);
            case 6:
                return Parser.ScanCodeResponse(response);
        }
        return null;
    }

    public enum RequestMethod {
        POST, GET
    }
}

