package com.schnell.smartlights.smartapps.https.request;

import android.content.Context;

import com.schnell.smartlights.constants.ApplicationConstants;
import com.schnell.smartlights.smartapps.https.response.NewRestClient;
import com.schnell.smartlights.utils.AppState;
import com.schnell.smartlights.utils.BaseActivity;

import org.json.JSONObject;

public class AuditServiceRequest {

    //    public static boolean registerUser(Context activity, LoginUserDetails loginuserdetails) {
//
//        try {
//            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "usr/create/", 1);
//            fillCommonParams(client, activity);
//            client.addParam("phone_no", String.valueOf(loginuserdetails.getPhonenumber()));
//            client.addParam("gateway_id", String.valueOf(loginuserdetails.getGatewayid()));
//            client.addParam("fcm_id", String.valueOf(loginuserdetails.getFcmid()));
//            client.addParam("referer_no", String.valueOf(loginuserdetails.getReferer()));
//            client.execute(activity);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
    public static boolean SaveBoardNumber(Context activity, String BoardNumber) {
        try {
            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "ilm/get/project_id/", 0);
            fillCommonParams(client, activity);
            client.addParam("board_no", BoardNumber, "Y");
            client.executes(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean CCMSSaveBoardNumber(Context activity, String BoardNumber) {
        try {
            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "gateway/details/", 6);
            fillCommonParams(client, activity);
            client.addParam("board_no", BoardNumber,"Y");
            client.executes(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean addUser(Context activity, String name, String phone_no, String project) {
        try {
            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "node/add/user/", 1);
            fillCommonParams(client, activity);
            client.addParam("name", name, "N");
            client.addParam("phone_no", phone_no, "N");
            client.addParam("project", project, "N");
            client.executes(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean SaveInstalledDevices(Context activity, String BoardNumber, String Phnumber) {
        try {
            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "node/update/installer/", 5);
            fillCommonParams(client, activity);
            client.addParam("board_no", BoardNumber, "Y");
            client.addParam("phone_no", Phnumber, "Y");
            client.executes(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean UserValidation(Context activity, String name, String phone_no, String project) {
        try {
            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "node/user/validate/", 2);
            fillCommonParams(client, activity);
            client.addParam("name", name, "N");
            client.addParam("phone_no", phone_no, "N");
            client.addParam("project", project, "N");
            client.executes(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

//    public static boolean getotpuser(Context activity, String phonenumber) {
//
//        try {
//            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "otp/", 0);
//            fillCommonParams(client, activity);
//            client.addParam("phone_no", phonenumber);
//            client.execute(activity);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    public static boolean SendDetails(Context activity, JSONObject BoardNumber) {
        try {
            NewRestClient client = new NewRestClient(ApplicationConstants.AUDIT_MAIL_SERVER + "node/install/", 3);
            fillCommonParams(client, activity);
            client.addParam("ilm_data", BoardNumber.toString(), "Y");
            client.execute(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean fillCommonParams(NewRestClient client, Context activity) {
        if (AppState.SYSTEM_INFO != null) {
            client.addParam("deviceinfo", AppState.SYSTEM_INFO, "Y");
        } else {
            BaseActivity.updateSystemInfo(activity);
            client.addParam("deviceinfo", AppState.SYSTEM_INFO, "Y");
        }
        return true;
    }
}
