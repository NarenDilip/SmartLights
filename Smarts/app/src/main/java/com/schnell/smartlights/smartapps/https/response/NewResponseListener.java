package com.schnell.smartlights.smartapps.https.response;

public interface NewResponseListener {

    void onResponseReceived(String responseObj, int requestType);

}