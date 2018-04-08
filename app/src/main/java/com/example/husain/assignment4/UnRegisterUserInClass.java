package com.example.husain.assignment4;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by husain on 4/1/2018.
 */

public class UnRegisterUserInClass {

    public String urlToSendSubject = "https://bismarck.sdsu.edu/registration/unregisterclass";
    SharedPreferences sharedPreferences;
    public static final String PREFERENCES = "PreferencesInfo";

    public void unRegister(String id, final Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCES,0);

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("courseid",Integer.parseInt(id));
            requestObject.put("redid",sharedPreferences.getString("RedId",null));
            requestObject.put("password",sharedPreferences.getString("Password",null));
        }
        catch(JSONException e){ }
        final String mRequestBody = requestObject.toString();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST,urlToSendSubject,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String  response){
                        Log.i("RESPONSE",response.toString());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.toString());
                            if(response.toString().contains("ok")) {
                                Toast.makeText(context, jsonObject.get("ok").toString(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, jsonObject.get("error").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Toast.makeText(context,response.toString(), Toast.LENGTH_LONG).show();
                Log.i("RESPONSE",response.toString());
            }
        }){ // Notice no semi-colon here
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
        };
        VolleyQueue.instance(context).add(postJsonRequest);

    }

}
