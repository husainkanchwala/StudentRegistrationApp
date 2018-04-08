package com.example.husain.assignment4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String urlToSendSubject = "https://bismarck.sdsu.edu/registration/classidslist";
    String urlToSendClass = "https://bismarck.sdsu.edu/registration/classdetails";

    ListView listView;
    ArrayList<CourseData> courseDataArray = new ArrayList<CourseData>();
    List<String> courseTitleArray = new ArrayList<String>();
    JSONArray jsonArray = new JSONArray();
    CustomListAdapter adapter;
    ProgressDialog progressDialog;
    Resources res;
    String[] levelArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_course_list);
        res = getResources();
        levelArray = res.getStringArray(R.array.level_array);
        final EditText startTimeText = (EditText) findViewById(R.id.editText7);
        final EditText endTimeText = (EditText) findViewById(R.id.editText8);
        final Spinner spin = (Spinner) findViewById(R.id.spinner3);
        spin.setOnItemSelectedListener(this);
        spin.setSelection(0);
        listView = (ListView)findViewById(R.id.courseList);
        Intent currentIntent= getIntent();
        final Bundle bundle = currentIntent.getExtras();
        adapter = new CustomListAdapter(this, courseDataArray);
        listView.setAdapter(adapter);
        retrieveSubject(bundle.get("id").toString());
        ArrayAdapter levelAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,levelArray);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(levelAdapter);
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    retrieveClassLevelStartTimeEndTime(bundle.get("id").toString(),spin.getSelectedItem().toString(),startTimeText.getText().toString(),endTimeText.getText().toString());
            }
        });
    }

    private void retrieveSubject(String id){
        JSONObject requestObject = new JSONObject();
        JSONArray ids = new JSONArray();
        ids.put(Integer.parseInt(id));
        try {
            requestObject.put("subjectids",ids);
        }
        catch(JSONException e){ }
        final String mRequestBody = requestObject.toString();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST,urlToSendSubject,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String  response){
                        Log.i("RESPONSE",response.toString());
                        try {
                            jsonArray = new JSONArray(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        retrieveClass(jsonArray);
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

    ///////////////// retrieve on the base on level
    private void retrieveClassLevelStartTimeEndTime(String id,String level,String startTime,String endTime){
        Log.i("INSIDELEVEL",id);
        Log.i("INSIDELEVEL",level);
        Log.i("INSIDELEVEL",startTime);
        Log.i("INSIDELEVEL",endTime);

        JSONObject requestObject = new JSONObject();
        JSONArray ids = new JSONArray();
        ids.put(Integer.parseInt(id));
        try {
            requestObject.put("subjectids",ids);
            requestObject.put("level",level);
            requestObject.put("starttime",startTime);
            requestObject.put("endtime",endTime);
        }
        catch(JSONException e){ }
        final String mRequestBody = requestObject.toString();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST,urlToSendSubject,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String  response){
                        Log.i("RESPONSE",response.toString());
                        try {
                            jsonArray = new JSONArray(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        retrieveClass(jsonArray);
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

    private void retrieveClass(JSONArray ids){
        courseDataArray.clear();
        courseTitleArray.clear();
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
                            jsonArray = new JSONArray(response.toString());
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
                                courseDataArray.add(courseData);
                                courseTitleArray.add(jsonObject.getString("title"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
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

