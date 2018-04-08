package com.example.husain.assignment4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String PREFERENCES = "PreferencesInfo";
    private String name;
    private String redId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_list);
        sharedPreferences = getSharedPreferences(PREFERENCES,0);
        name = sharedPreferences.getString("FirstName",null);
        redId = sharedPreferences.getString("RedId",null);
        final TextView nameView = (TextView) findViewById(R.id.textView5);
        final TextView redIdView = (TextView) findViewById(R.id.textView13);
        nameView.setText("name : " + name);
        redIdView.setText("redid : " + redId);
    }

    public void goToSubject(View v){
        startActivity(new Intent(getApplicationContext(), SubjectList.class));
    }

    public void goToSchedule(View v){
        startActivity(new Intent(getApplicationContext(), ScheduleList.class));
    }
}
