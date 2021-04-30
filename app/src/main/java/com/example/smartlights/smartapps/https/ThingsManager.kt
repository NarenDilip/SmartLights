package com.example.smartlights.smartapps.https;

import android.content.Context
import com.android.volley.Request
import com.example.smartlights.constants.ApplicationConstants
import com.example.smartlights.services.*
import com.example.smartlights.smartapps.models.*
import com.example.smartlights.utils.AppPreference
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

/**
 * @since 24/2/17.<BR></BR>
 * Common request elements can be updated in this class.<BR></BR>c
 * Extend this class for all web service classes to avoid redundancy
 */

open class ThingsManager {
    interface API {
        companion object {

            /* User and Login APIs */
            const val login = "/api/auth/login"
            const val saveRelation = "/api/relation"
            const val saveDeviceCredential = "/api/device/credentials"
            const val getDeviceName = "/api/device"
            const val getAssetName = "/api/asset"
            const val getfromdevice = "/api/relations/info?"
            const val device = "/api/device"
            const val asset = "/api/asset"
            const val getPoleDetails = "/api/tenant/assets?"
            const val getDeviceInfo = "/api/user/users?"

            const val getDeviceCredentialsByDeviceId = "/api/device/{assetId}/credentials"
            const val addAttribute =
                "/api/plugins/telemetry/{entityValType}/{entityId}/attributes/{scope}"

            const val getDeviceTelemetry = "/api/plugins/telemetry"
            const val getDeviceDevice = "/api/entityGroups/DEVICE"
            const val getDeviceAssets = "/api/entityGroups/ASSET"

            const val getRelatedDeviceFromAsset = "/api/relations"
            const val getDeviceFromenityGroup = "/api/entityGroup"

            const val deleteentities = "/api/entityGroup/{entityGroupId}/deleteEntities"
            const val addentities = "/api/entityGroup/{entityGroupId}/addEntities"

            const val tenantdevices = "/api/tenant/devices"
            const val saveEntityGroup = "/api/entityGroup"
            const val getusertoken = "/api/user/"
            const val saveUserEmail = "/api/user?sendActivationMail=false&entityGroupId="
            const val logout = "/api/auth/logout"

            const val getDeviceindexval =
                "/api/plugins/telemetry/ASSET"

            const val getDevicedetails =
                "/api/plugins/telemetry/DEVICE"
        }
    }

    companion object {

        private fun constructUrl(urlKey: String): String {
            return String.format("%s%s", ApplicationConstants.THINGS_BOARD_URL, urlKey)
        }

        private fun fillCommons(c: Context, r: VolleyClient, Saccount: String) {
            if (Saccount == "Smart") {
                r.addHeader(
                    "X-Authorization",
                    "Bearer ${AppPreference[c, AppPreference.Key.accessToken, ""]}"
                )
            } else {
                r.addHeader(
                    "X-Authorization",
                    "Bearer ${AppPreference[c, AppPreference.Key.accessToken, ""]}"
                )
            }
        }

        /**
         * @param c    Context of App
         * @param username - user given username
         * @param password - user given password
         * @return false if exception happened before http call
         */
        fun login(c: Context, username: String, password: String) {
            try {
                // Generating Req
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(API.login), API.login.hashCode()
                )
                val jsonObject = JSONObject()
                jsonObject.put("username", username)
                jsonObject.put("password", password)
                client.execute(c as ResponseListener, jsonObject, LoginResponse::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getdevicelist(
            c: Context,
            l: ResponseListener,
            entityGroupId: String,
            Saccount: String
        ) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDeviceFromenityGroup}/$entityGroupId" + "/entities?pageSize=100&page=0&ascOrder=false"),
                    API.getDeviceFromenityGroup.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(l, responseType = ThingsBoardResponse::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getrelations(c: Context, fromid: String, fromtype: String, Saccount: String) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getRelatedDeviceFromAsset}" + "?fromId=" + fromid + "&fromType=" + fromtype),
                    API.getRelatedDeviceFromAsset.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(c as ResponseListener, responseType = FromAddress::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getdeviceindexval(
            c: Context,
            l: ResponseListener,
            deviceId: String,
            devicename: String
        ) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDeviceindexval}/$deviceId" + "/values/timeseries?&keys=onCCMS&keys=offCCMS&keys=offlineCCMS&keys=totalCCMS&useStrictDataTypes=false"),
                    API.getDeviceindexval.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = devicename)
                client.execute(l, responseType = IndVal::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getDeviceSDetails(
            c: Context,
            l: ResponseListener,
            deviceId: String,
            devicename: String
        ) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDevicedetails}/$deviceId" + "/values/attributes?&keys=location&keys=simNo&keys=imeiNumber&keys=scNo&keys=phase&keys=slatitude&keys=slongitude"),
                    API.getDevicedetails.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = devicename)
                client.execute(l, responseType = ThingsBoardResponse::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getPole(c: Context, PoleNo: String, Saccount: String) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getPoleDetails}" + "assetName=" + PoleNo),
                    API.getPoleDetails.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(c as ResponseListener, responseType = Asset::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getassetdetails(c: Context, AssetId: String, Saccount: String) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getAssetName}/$AssetId"),
                    API.getAssetName.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(c as ResponseListener, responseType = AssetDetails::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getDeviceCredentialsByDeviceId(c: Context, deviceId: String, Saccount: String) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl(API.getDeviceCredentialsByDeviceId.replace("{assetId}", deviceId)),
                    API.getDeviceCredentialsByDeviceId.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(
                    c as ResponseListener,
                    responseType = DeviceCredential::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        fun fromaddress(c: Context, toId: String, totype: String, Saccount: String) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getfromdevice}" + "relationTypeGroup=Consist&" + "fromId=" + toId + "&fromType=" + totype),
                    API.getfromdevice.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(c as ResponseListener, responseType = AssetSelectedWards::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getdevicedetails(c: Context, deviceId: String, Saccount: String) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDeviceName}/$deviceId"),
                    API.getDeviceName.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(c as ResponseListener, responseType = DeviceDetails::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun saveDevice(
            c: Context,
            l: ResponseListener,
            entityGroupId: String,
            device: Device,
            extraOutput: String? = null, Saccount: String
        ) {
            try {
                // Generating Req
                val client = JsonClient(
                    c,
                    Request.Method.POST,
                    constructUrl("${API.device}/?entityGroupId=$entityGroupId"),
                    API.device.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.extraOutput = extraOutput
                val jsonObject = JSONTokener(Gson().toJson(device)).nextValue() as JSONObject
                client.execute(
                    l = l,
                    jsonObject = jsonObject,
                    responseType = Device::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        fun deleteDevice(c: Context, l: ResponseListener, deviceId: String, Saccount: String) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.DELETE,
                    constructUrl("${API.device}/$deviceId"),
                    API.device.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(l, responseType = Device::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun saveAsset(
            c: Context,
            l: ResponseListener,
            entityGroupId: String,
            asset: Asset,
            extraOutput: String? = null, Saccount: String
        ) {
            try {
                // Generating Req
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl("${API.asset}/?entityGroupId=$entityGroupId"), API.asset.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.extraOutput = extraOutput
                val jsonObject = JSONTokener(Gson().toJson(asset)).nextValue() as JSONObject
                client.execute(
                    l = l,
                    jsonObject = jsonObject,
                    responseType = Asset::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        fun saveRelation(c: Context, gatewayDeviceId: String, device: Device, Saccount: String) {
            try {
                // Generating Req
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(API.saveRelation), API.saveRelation.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)

                val fromObject = Entity()
                fromObject.entityType = "DEVICE"
                fromObject.id = device!!.id!!.id!!

                val toObject = Entity()
                toObject.entityType = "ASSET"
                toObject.id = gatewayDeviceId

                val jsonObject = JSONObject()
                jsonObject.put("type", "Contains")
                jsonObject.put("typeGroup", "COMMON")
                jsonObject.put(
                    "from",
                    JSONTokener(Gson().toJson(toObject)).nextValue() as JSONObject
                )
                jsonObject.put(
                    "to",
                    JSONTokener(Gson().toJson(fromObject)).nextValue() as JSONObject
                )
                client.execute(
                    c as ResponseListener,
                    jsonObject = jsonObject,
                    responseType = Response::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        fun savePoleRelation(
            c: Context,
            wardDeviceId: String,
            poleDeviceId: String,
            Saccount: String
        ) {
            try {
                // Generating Req
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(API.saveRelation), API.saveRelation.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)

                val fromObject = Entity()
                fromObject.entityType = "ASSET"
                fromObject.id = wardDeviceId

                val toObject = Entity()
                toObject.entityType = "ASSET"
                toObject.id = poleDeviceId

                val jsonObject = JSONObject()
                jsonObject.put("type", "Consist")
                jsonObject.put("typeGroup", "COMMON")
                jsonObject.put(
                    "to",
                    JSONTokener(Gson().toJson(toObject)).nextValue() as JSONObject
                )
                jsonObject.put(
                    "from",
                    JSONTokener(Gson().toJson(fromObject)).nextValue() as JSONObject
                )
                client.execute(
                    c as ResponseListener,
                    jsonObject = jsonObject,
                    responseType = Response::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        fun saveDeviceCredential(c: Context, deviceCredential: DeviceCredential, Saccount: String) {
            try {
                // Generating Req
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(API.saveDeviceCredential), API.saveDeviceCredential.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                val jsonObject =
                    JSONTokener(Gson().toJson(deviceCredential)).nextValue() as JSONObject
                client.execute(
                    c as ResponseListener,
                    jsonObject = jsonObject,
                    responseType = DeviceCredential::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        /**
         * Add number id attributes
         */
        fun addserverAttribute(
            c: Context,
            l: ResponseListener,
            deviceId: String,
            number: String,
            data: String,
            Saccount: String
        ) {
            try {
                // Generating Req`
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(
                        API.addAttribute
                            .replace("{entityValType}", "DEVICE")
                            .replace("{entityId}", deviceId)
                            .replace("{scope}", "SERVER_SCOPE")
                    )
                    , API.addAttribute.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                val jsonObject = JSONObject()
                jsonObject.put(number, data)
                client.execute(l = l, jsonObject = jsonObject, responseType = Response::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun addserverStringAttribute(
            c: Context,
            l: ResponseListener,
            deviceId: String,
            number: String,
            data: String,
            Saccount: String
        ) {
            try {
                // Generating Req`
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(
                        API.addAttribute
                            .replace("{entityValType}", "DEVICE")
                            .replace("{entityId}", deviceId)
                            .replace("{scope}", "SERVER_SCOPE")
                    )
                    , API.addAttribute.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                val jsonObject = JSONObject()
                jsonObject.put(number, data)
                client.execute(l = l, jsonObject = jsonObject, responseType = Response::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun addAssetAttribute(
            c: Context,
            l: ResponseListener,
            deviceId: String,
            number: String,
            data: String,
            Saccount: String
        ) {
            try {
                // Generating Req`
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(
                        API.addAttribute
                            .replace("{entityValType}", "ASSET")
                            .replace("{entityId}", deviceId)
                            .replace("{scope}", "SERVER_SCOPE")
                    )
                    , API.addAttribute.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                val jsonObject = JSONObject()
                jsonObject.put(number, data)
                client.execute(l = l, jsonObject = jsonObject, responseType = Response::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getDeviceLatestAttributes(
            c: Context,
            deviceId: String,
            entityType: String,
            Keys: String,
            Saccount: String
        ) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDeviceTelemetry}/$entityType/$deviceId" + "/values/attributes?keys=" + Keys),
                    API.getDeviceTelemetry.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(
                    c as ResponseListener,
                    responseType = ThingsBoardResponse::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        fun getDeviceDevice(
            c: Context,
            l: ResponseListener,
            Saccount: String
        ) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDeviceDevice}"),
                    API.getDeviceDevice.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(l, responseType = AssetGroup::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun getDeviceAssets(
            c: Context,
            l: ResponseListener,
            Saccount: String
        ) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDeviceAssets}"),
                    API.getDeviceAssets.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(l, responseType = AssetGroup::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun aaddEntites(
            c: Context, l: ResponseListener, entityGroupId: String, device: JSONArray,
            extraOutput: String? = null, Saccount: String
        ) {
            try {
                // Generating Req`
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(
                        API.addentities
                            .replace("{entityGroupId}", entityGroupId)
                    )
                    , API.addentities.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.extraOutput = extraOutput
                client.executesS(l = l, jsonObject = device, responseType = Response::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun deleteEntites(
            c: Context,
            l: ResponseListener,
            entityGroupId: String,
            device: JSONArray,
            extraOutput: String? = null,
            Saccount: String
        ) {
            try {
                // Generating Req`
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(
                        API.deleteentities
                            .replace("{entityGroupId}", entityGroupId)
                    )
                    , API.deleteentities.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.extraOutput = extraOutput
                client.executesS(l = l, jsonObject = device, responseType = Response::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun addserverallStringAttribute(
            c: Context,
            l: ResponseListener,
            deviceId: String,
            location_name: String,
            location_data: String,
            ward_name: String,
            ward_data: String,
            zone_name: String,
            zone_data: String,
            lat_name: String,
            lat_data: String,
            lon_name: String,
            lon_data: String,
            connectedwatts_name: String,
            connectedwatts_data: String,
            pole_name: String,
            pole_data: String,
            lamp_name: String,
            lamp_data: String,
            base_name: String,
            base_data: String,
            road_name: String,
            road_data: String,
            ccms_name: String,
            ccms_data: String,
            service_name: String,
            service_data: String,
            sc_name: String,
            sc_data: String,
            eb_name: String,
            eb_data: String,
            instime_name: String,
            instime_data: Any,
            insby_name: String,
            insby_data: String,
            s: String,
            s1: String,
            Saccount: String
        ) {
            try {
                // Generating Req`
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(
                        API.addAttribute
                            .replace("{entityValType}", "DEVICE")
                            .replace("{entityId}", deviceId)
                            .replace("{scope}", "SERVER_SCOPE")
                    )
                    , API.addAttribute.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                val jsonObject = JSONObject()
                jsonObject.put(location_name, location_data)
                jsonObject.put(ward_name, ward_data)
                jsonObject.put(zone_name, zone_data)
                jsonObject.put(lat_name, lat_data)
                jsonObject.put(lon_name, lon_data)
                jsonObject.put(connectedwatts_name, connectedwatts_data)
                jsonObject.put(pole_name, pole_data)
                jsonObject.put(lamp_name, lamp_data)
                jsonObject.put(base_name, base_data)
                jsonObject.put(road_name, road_data)
                jsonObject.put(ccms_name, ccms_data)
                jsonObject.put(service_name, service_data)
                jsonObject.put(sc_name, sc_data)
                jsonObject.put(eb_name, eb_data)
                jsonObject.put(instime_name, instime_data)
                jsonObject.put(insby_name, insby_data)
                jsonObject.put(s, s1)
                client.execute(l = l, jsonObject = jsonObject, responseType = Response::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun gettenantDevices(
            c: Context,
            l: ResponseListener,
            deviceName: String,
            Saccount: String
        ) {
            try {
                // Generating Req
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.tenantdevices}?" + "deviceName=" + deviceName),
                    API.tenantdevices.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = Saccount)
                client.execute(l, responseType = TenantDevices::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        fun saveEntityGroup(
            c: Context,
            groupName: String,
            description: String = "",
            displayName: String = "",
            Saccount: String
        ) {
            try {
                // Generating Req
                val client =
                    JsonClient(
                        c,
                        Request.Method.POST,
                        constructUrl(API.saveEntityGroup),
                        API.saveEntityGroup.hashCode()
                    )
                fillCommons(c = c, r = client, Saccount = Saccount)
                val jsonObject = JSONObject()
                jsonObject.put("name", groupName)
                jsonObject.put("type", "DEVICE")
                val descriptionJson = JSONObject()
                descriptionJson.put("description", description)
                descriptionJson.put("displayName", displayName)
                jsonObject.put("additionalInfo", descriptionJson)
                client.execute(
                    c as ResponseListener,
                    jsonObject = jsonObject,
                    responseType = Device::class.java
                )
            } catch (e: Exception) {
                throw e
            }
        }

        //get logs of all device and sensors
        fun getuserinformation(
            c: Context,
            l: ResponseListener,
            textSearch: String,
            limit: String,
            devicename: String
        ) {
            try {
                //generate Request
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getDeviceInfo}textSearch=" + textSearch + "&limit=" + "&pageSize=10&page=0"),
                    API.getDeviceInfo.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = devicename)
                client.execute(l, responseType = ThingsBoardResponse::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        // Save the new user registrations and signup details
        fun saveNewUserEmail(
            c: Context,
            userEmail: String,
            userId: String,
            entityGroupId: String,
            devicename: String
        ) {
            try {
                // Generating Req
                val client = JsonClient(
                    c, Request.Method.POST,
                    constructUrl(API.saveUserEmail + entityGroupId), API.logout.hashCode()
                )
                val jsonObject = JSONObject()
                val subjsonObject = JSONObject()

                jsonObject.put("email", userEmail)
                jsonObject.put("authority", "CUSTOMER_USER")
                subjsonObject.put("entityType", "CUSTOMER")
                subjsonObject.put("id", userId)
                jsonObject.put("customerId", subjsonObject)

                fillCommons(c = c, r = client, Saccount = devicename)
                client.execute(c as ResponseListener, jsonObject, ThingsBoardResponse::class.java)
            } catch (e: Exception) {
                throw e
            }
        }

        //get logs of all device and sensors
        fun getusertoken(
            c: Context,
            l: ResponseListener,
            token: String,
            devicename: String
        ) {
            try {
                //generate Request
                val client = RestClient(
                    c,
                    Request.Method.GET,
                    constructUrl("${API.getusertoken}" + token + "/token"),
                    API.getusertoken.hashCode()
                )
                fillCommons(c = c, r = client, Saccount = devicename)
                client.execute(l, responseType = LoginResponse::class.java)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}

