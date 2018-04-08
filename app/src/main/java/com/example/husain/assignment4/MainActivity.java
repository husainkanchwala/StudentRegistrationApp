package com.example.husain.assignment4;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pdlg;
    SharedPreferences sharedPreferences;
    public static final String PREFERENCES = "PreferencesInfo";
    UserData userData = new UserData();
    private String firstName;
    private String lastName;
    private String redId;
    private String email;
    private String password;

    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextRedid;
    EditText editTextEmail;
    EditText editTextPassword;

    String urlToSend = "https://bismarck.sdsu.edu/registration/addstudent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(PREFERENCES,0);

        editTextFirstName = (EditText) this.findViewById(R.id.editText);
        editTextLastName = (EditText) this.findViewById(R.id.editText2);
        editTextRedid = (EditText) this.findViewById(R.id.editText3);
        editTextEmail = (EditText) this.findViewById(R.id.editText4);
        editTextPassword = (EditText) this.findViewById(R.id.editText5);

        firstName = sharedPreferences.getString("FirstName",null);
        lastName = sharedPreferences.getString("LastName",null);
        redId = sharedPreferences.getString("RedId",null);
        email = sharedPreferences.getString( "Email",null);
        password = sharedPreferences.getString("Password",null);

        if(firstName!=null && lastName!=null && redId!=null && email!=null && password!=null){
            startActivity(new Intent(getApplicationContext(), OptionsActivity.class));
        }

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = editTextFirstName.getText().toString();
                lastName = editTextLastName.getText().toString();
                redId = editTextRedid.getText().toString();
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                userData.setFirstName(firstName);
                userData.setLastName(lastName);
                userData.setRedId(redId);
                userData.setEmail(email);
                userData.setPassword(password);

                submitData();
            }
        });

    }

    public void submitData(){
        JSONObject requestObject = new JSONObject();
        Log.i("DATA","Submitting user to server");
        Log.i("DATA",userData.getFirstName());
        Log.i("DATA",userData.getLastName());
        Log.i("DATA",userData.getRedId());
        Log.i("DATA",userData.getPassword());
        Log.i("DATA",userData.getEmail());

        try {
            requestObject.put("firstname", userData.getFirstName());
            requestObject.put("lastname", userData.getLastName());
            requestObject.put("redid", userData.getRedId());
            requestObject.put("password", userData.getPassword());
            requestObject.put("email", userData.getEmail());
        }
        catch(JSONException e){ }
        JsonObjectRequest postJsonRequest = new JsonObjectRequest(urlToSend, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        Log.i("RESPONSE",response.toString());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.toString());
                            if(response.toString().contains("ok")) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("FirstName", firstName);
                                editor.putString("LastName", lastName);
                                editor.putString("RedId", redId);
                                editor.putString("Email", email);
                                editor.putString("Password", password);
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), OptionsActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(), jsonObject.get("error").toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("rew", "Error: " + error.getMessage());
                try {
                    JSONObject jsonObject = new JSONObject(error.toString());
                    Toast.makeText(getApplicationContext(),jsonObject.get("error").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        VolleyQueue.instance(getApplicationContext()).add(postJsonRequest);
    }

}
