package com.example.husain.assignment4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String urlToFetchEnrolled = "https://bismarck.sdsu.edu/registration/studentclasses";
    String urlToSendClass = "https://bismarck.sdsu.edu/registration/classdetails";
    SharedPreferences sharedPreferences;
    public static final String PREFERENCES = "PreferencesInfo";

    ListView listView;
    ArrayList<CourseData> registeredCourseDataArray = new ArrayList<CourseData>();
    CustomScheduleAdapter adapterEnrolled;

    ListView listViewWaitlisted;
    ArrayList<CourseData> waitlistedCourseDataArray = new ArrayList<CourseData>();
    CustomWaitlistAdapter adapterwaitlisted;

    ProgressDialog progressDialog;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_list);

        listView = (ListView)findViewById(R.id.scheduleRList);
        adapterEnrolled = new CustomScheduleAdapter(this, registeredCourseDataArray);
        listView.setAdapter(adapterEnrolled);

        listViewWaitlisted = (ListView)findViewById(R.id.scheduleWList);
        adapterwaitlisted = new CustomWaitlistAdapter(this, waitlistedCourseDataArray);
        listViewWaitlisted.setAdapter(adapterwaitlisted);

        retrieveEnrollment(getApplicationContext());
        retrieveWaitlisted(getApplicationContext());
    }

    private void retrieveEnrollment(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCES,0);
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("redid",sharedPreferences.getString("RedId",null));
            requestObject.put("password",sharedPreferences.getString("Password",null));
        }
        catch(JSONException e){ }
        final String mRequestBody = requestObject.toString();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST,urlToFetchEnrolled,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String  response){
                        Log.i("RESPONSE",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            retrieveClassEnrolled(new JSONArray(jsonObject.get("classes").toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {

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
        VolleyQueue.instance(getApplicationContext()).add(postJsonRequest);
    }

    private void retrieveWaitlisted(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCES,0);
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("redid",sharedPreferences.getString("RedId",null));
            requestObject.put("password",sharedPreferences.getString("Password",null));
        }
        catch(JSONException e){ }
        final String mRequestBody = requestObject.toString();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST,urlToFetchEnrolled,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String  response){
                        Log.i("RESPONSE",response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            retrieveClassWaitlisted(new JSONArray(jsonObject.get("waitlist").toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {

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
        VolleyQueue.instance(getApplicationContext()).add(postJsonRequest);
    }


    private void retrieveClassEnrolled(JSONArray ids){
        registeredCourseDataArray.clear();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("classids", ids);
        } catch (JSONException e) {
        }
        final String mRequestBody = requestObject.toString();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, urlToSendClass,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response.toString());
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CourseData courseData = new CourseData();
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                courseData.setId(jsonObject.getString("id"));
                                courseData.setTitle(jsonObject.getString("title"));
                                courseData.setDays(jsonObject.getString("days"));
                                courseData.setSeats(jsonObject.getString("seats"));
                                courseData.setWaitlist(jsonObject.getString("waitlist"));
                                if(jsonObject.getString("course#").startsWith("1") || jsonObject.getString("course#").startsWith("2")){
                                    courseData.setLevel("Lower");
                                }else if(jsonObject.getString("course#").startsWith("3") || jsonObject.getString("course#").startsWith("4") || jsonObject.getString("course#").startsWith("5") ){
                                    courseData.setLevel("Upper");
                                }else{
                                    courseData.setLevel("Graduate");
                                }
                                courseData.setStartTime(jsonObject.getString("startTime"));
                                courseData.setEndTime(jsonObject.getString("endTime"));
                                registeredCourseDataArray.add(courseData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterEnrolled.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {

            }
        }) { // Notice no semi-colon here
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
        VolleyQueue.instance(getApplicationContext()).add(postJsonRequest);
    }

    private void retrieveClassWaitlisted(JSONArray ids){
        waitlistedCourseDataArray.clear();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("classids", ids);
        } catch (JSONException e) {
        }
        final String mRequestBody = requestObject.toString();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, urlToSendClass,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response.toString());
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CourseData courseData = new CourseData();
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                courseData.setId(jsonObject.getString("id"));
                                courseData.setTitle(jsonObject.getString("title"));
                                courseData.setDays(jsonObject.getString("days"));
                                courseData.setSeats(jsonObject.getString("seats"));
                                courseData.setWaitlist(jsonObject.getString("waitlist"));
                                if(jsonObject.getString("course#").startsWith("1") || jsonObject.getString("course#").startsWith("2")){
                                    courseData.setLevel("Lower");
                                }else if(jsonObject.getString("course#").startsWith("3") || jsonObject.getString("course#").startsWith("4") || jsonObject.getString("course#").startsWith("5") ){
                                    courseData.setLevel("Upper");
                                }else{
                                    courseData.setLevel("Graduate");
                                }
                                courseData.setStartTime(jsonObject.getString("startTime"));
                                courseData.setEndTime(jsonObject.getString("endTime"));
                                waitlistedCourseDataArray.add(courseData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapterwaitlisted.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {

            }
        }) { // Notice no semi-colon here
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
        VolleyQueue.instance(getApplicationContext()).add(postJsonRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

