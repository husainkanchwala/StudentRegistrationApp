package com.example.husain.assignment4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by husain on 3/31/2018.
 */

public class SubjectList extends AppCompatActivity {

    String urlToSend = "https://bismarck.sdsu.edu/registration/subjectlist";
    ListView listView;
    ArrayList<SubjectData> subjectDataArray = new ArrayList<SubjectData>();
    ArrayList<String> subjectTitleArray = new ArrayList<String>();
    JSONArray jsonArray = new JSONArray();
    ProgressDialog progressDialog;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);
        listView = (ListView)findViewById(R.id.subjectlist);
        retrieveSubject();
        adapter = new ArrayAdapter<String>(this,R.layout.activity_listview, subjectTitleArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String subjectTitle = parent.getItemAtPosition(position).toString();
                String subjectId = "";
                for (int i = 0; i < subjectDataArray.size(); ++i) {
                    if(subjectDataArray.get(i).getTitle().equalsIgnoreCase(subjectTitle)){
                        subjectId = subjectDataArray.get(i).getId();
                    }
                }
                Log.i("subjectid",subjectId);
                startActivity(new Intent(getApplicationContext(), CourseList.class).putExtra("id",subjectId));
            }
        });
    }

    private void retrieveSubject(){
        JsonArrayRequest postJsonRequest = new JsonArrayRequest(Request.Method.GET,urlToSend, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response){
                        Log.i("RESPONSE",response.toString());
                        Log.d("response", "CATEGORY RESPONSE: " + response.toString());
                        if (response != null) {
                            int dataLength = response.length();
                            for (int i = 0; i < dataLength; i++) {
                                JSONObject jObject = response.optJSONObject(i);
                                if (jObject != null) {
                                    Log.i("Data",jObject.optString("title"));
                                    SubjectData subjectData = new SubjectData();
                                    subjectData.setTitle(jObject.optString("title"));
                                    subjectData.setId(jObject.optString("id"));
                                    subjectData.setCollege(jObject.optString("college"));
                                    subjectData.setClasses(jObject.optString("classes"));
                                    subjectDataArray.add(subjectData);
                                }
                            }
                        }
                        for (int i = 0; i < subjectDataArray.size(); ++i) {
                            subjectTitleArray.add(subjectDataArray.get(i).getTitle());
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
                Log.i("rew", "Error: " + error.getMessage());
            }
        });
        VolleyQueue.instance(getApplicationContext()).add(postJsonRequest);
    }

}
