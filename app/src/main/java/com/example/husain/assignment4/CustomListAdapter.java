package com.example.husain.assignment4;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by husain on 4/1/2018.
 */

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<CourseData> courseItems;

    public CustomListAdapter(Activity activity, List<CourseData> courseItem) {
        this.activity = activity;
        this.courseItems = courseItem;
    }

    @Override
    public int getCount() {
        return courseItems.size();
    }

    @Override
    public Object getItem(int location) {
        return courseItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.course_listview, null);
        final CourseData courseData = courseItems.get(position);
        TextView title = (TextView) convertView.findViewById(R.id.textView39);
        TextView seats = (TextView) convertView.findViewById(R.id.textView44);
        TextView waitlist = (TextView) convertView.findViewById(R.id.textView50);
        TextView level = (TextView) convertView.findViewById(R.id.textView6);
        TextView startTime = (TextView) convertView.findViewById(R.id.textView12);

        Button buttonRegister = (Button)  convertView.findViewById(R.id.button17);
        if(Integer.parseInt(courseData.getSeats())>0){
            buttonRegister.setText("REGISTER");
        }else{
            buttonRegister.setText("WAITLIST");
        }
        buttonRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Integer.parseInt(courseData.getSeats())>0){
                    RegisterUserInClass user = new RegisterUserInClass();
                    user.register(courseData.getId(),parent.getContext());
                }else{
                    RegisterUserInWaitlist user = new RegisterUserInWaitlist();
                    user.register(courseData.getId(),parent.getContext());
                }
            }

        });
        title.setText(courseData.getTitle());
        seats.setText("Seats: " + String.valueOf(courseData.getSeats()));
        waitlist.setText("Waitlist: " + String.valueOf(courseData.getWaitlist()));
        level.setText("Level: " + String.valueOf(courseData.getLevel()));
        startTime.setText("StartTime: " + String.valueOf(courseData.getStartTime()));

        return convertView;
    }

}