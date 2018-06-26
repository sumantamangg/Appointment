package com.suman.appointment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_handle);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();

        getSupportActionBar().setTitle(getIntent().getStringExtra("REQUESTS"));
        mcalendarview = (CalendarView) findViewById(R.id.calender_view2);
        date = mcalendarview.getDate();
        progressDialog = new ProgressDialog(this);
        mcalendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                progressDialog.setMessage("Wait A Second!");
                progressDialog.show();
                String day=Integer.toString(dayOfMonth);
                String mnth=Integer.toString(month+1);
                String yr=Integer.toString(year);
                final String date = yr+"-"+mnth+"-"+day;
                final String d1=yr+mnth+day;
                SimpleDateFormat firebaseformat = new SimpleDateFormat("yyyy-M-d");
                Date ddd = null;
                try {
                    ddd = firebaseformat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar ck = Calendar.getInstance();
                ck.setTime(new Date());
                ck.add(Calendar.DATE, -1);

                //Log.i("akj", "calendere date: "+d1);
                if (ck.getTime().after(ddd)) {
                    databaseReference.child("appointments").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                                    final String agenda = meetingInformation.getAgenda();
                                    final String heading = meetingInformation.getHeading();
                                    final String uid = child.getKey().toString();
                                    databaseReference.child("users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(RequestsHandleActivity.this, OldDataActivity.class);
                                            if(dataSnapshot.exists()) {
                                                String party = dataSnapshot.getValue().toString().trim();
                                                intent.putExtra("name",party);
                                            }
                                                intent.putExtra("agenda", agenda);
                                                intent.putExtra("heading", heading);
                                                intent.putExtra("uid", uid);
                                                intent.putExtra("date", date);
                                                startActivity(intent);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                }
                            else /** check in requests node*/
                                {
                                //Toast.makeText(getApplicationContext(), "Sorry!! data not available", Toast.LENGTH_SHORT).show();
                                databaseReference.child("requests").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                            for (DataSnapshot child : children) {
                                                MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                                                if(meetingInformation.getState().equals("accepted")){
                                                    final String uid = child.getKey().toString();
                                                    final String agenda = meetingInformation.getAgenda();
                                                    final String heading = meetingInformation.getHeading();
                                                    databaseReference.child("users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            progressDialog.dismiss();
                                                            Intent intent = new Intent(RequestsHandleActivity.this, OldDataActivity.class);
                                                            if(dataSnapshot.exists()) {
                                                                String party = dataSnapshot.getValue().toString().trim();
                                                                intent.putExtra("name",party);
                                                            }
                                                            intent.putExtra("agenda", agenda);
                                                            intent.putExtra("heading", heading);
                                                            intent.putExtra("uid", uid);
                                                            intent.putExtra("date", date);
                                                            startActivity(intent);

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            }
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Sorry!! data not available", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Intent intent = new Intent(RequestsHandleActivity.this, PopupshowActivity3.class);
                    return;
                }
                else {
                    databaseReference.child("requests").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "No meeting!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                                    meetinginfo.add(meetingInformation);
                                }
                                for (int i=0; i<meetinginfo.size();i++){
                                    progressDialog.dismiss();
                                    if(meetinginfo.get(i).state.equals("accepted")){
                                        Intent intent = new Intent(RequestsHandleActivity.this, PopupshowActivity.class);
                                        intent.putExtra("fd",date);
                                        intent.putExtra("hd", meetinginfo.get(i).heading);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        meetinginfo.clear();
                                    }
                                    else {
                                        Intent intent = new Intent(RequestsHandleActivity.this, PopupshowActivity2.class);
                                        intent.putExtra("fd",date);
                                        intent.putExtra("date", date+" (Pending Requests)");
                                        intent.putExtra("backbtn","rqst");
                                        progressDialog.dismiss();
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
