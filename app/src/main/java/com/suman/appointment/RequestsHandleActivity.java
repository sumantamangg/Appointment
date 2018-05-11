package com.suman.appointment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RequestsHandleActivity extends AppCompatActivity {

    private CalendarView mcalendarview;
    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();

    Long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_handle);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();

        getSupportActionBar().setTitle(getIntent().getStringExtra("REQUESTS"));
        mcalendarview = (CalendarView) findViewById(R.id.calender_view2);
        date = mcalendarview.getDate();
        mcalendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                String day=Integer.toString(dayOfMonth);
                String mnth=Integer.toString(month+1);
                String yr=Integer.toString(year);
                final String date = yr+"-"+mnth+"-"+day;
                final String d1=yr+mnth+day;
                SimpleDateFormat firebaseformat = new SimpleDateFormat("yyyyMd");
                Date ddd = null;
                try {
                    ddd = firebaseformat.parse(d1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar ck = Calendar.getInstance();
                ck.setTime(new Date());
                ck.add(Calendar.DATE, -1);

                //Log.i("akj", "calendere date: "+d1);
                if (ck.getTime().after(ddd)) {
                    Toast.makeText(getApplicationContext(), "Sorry!! data not available", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    databaseReference.child("requests").child(d1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(), "No meeting!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                                    meetinginfo.add(meetingInformation);
                                }
                                for (int i=0; i<meetinginfo.size();i++){
                                    if(meetinginfo.get(i).state.equals("accepted")){
                                        Intent intent = new Intent(RequestsHandleActivity.this, PopupshowActivity.class);
                                        intent.putExtra("fd",d1);
                                        intent.putExtra("date", date);
                                        startActivity(intent);
                                        meetinginfo.clear();
                                    }
                                    else {
                                        Intent intent = new Intent(RequestsHandleActivity.this, PopupshowActivity2.class);
                                        intent.putExtra("fd",d1);
                                        intent.putExtra("date", date+" (Pending Requests)");
                                        intent.putExtra("backbtn","rqst");
                                        startActivity(intent);
                                        meetinginfo.clear();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


//                    Intent i = new Intent(RequestsHandleActivity.this, ReqyestHandlePopup.class);
//                    i.putExtra("MY_kEY", d1);
//                    i.putExtra("date", yr + "-" + mnth + "-" + day);
//                    startActivity(i);
                }

//                Intent i = new Intent(RequestsHandleActivity.this, PopupActivity.class);
//                i.putExtra("MY_kEY",u_id);
//                i.putExtra("date",yr+"-"+mnth+"-"+day);
//                startActivity(i);


            }


        });

    }
    @Override
    public void  onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(RequestsHandleActivity.this,AdminHomeActivity.class));
        finish();
    }
}
