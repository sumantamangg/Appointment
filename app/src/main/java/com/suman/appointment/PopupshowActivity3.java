package com.suman.appointment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PopupshowActivity3 extends AppCompatActivity {

    private TextView headingfield;
    private TextView agendafield;
    private TextView partyfield;
    private TextView tv;
    private Button acceptbtn;
    private Button rejectbtn;
    FirebaseAuth auth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupshow3);

        getSupportActionBar().setTitle(getIntent().getStringExtra("state"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        auth = FirebaseAuth.getInstance();

        String party = getIntent().getStringExtra("name");
        final String heading = getIntent().getStringExtra("heading");
        final String agenda = getIntent().getStringExtra("agenda");
        String phone = getIntent().getStringExtra("phone");
        final String fd = getIntent().getStringExtra("fd");

        headingfield = (TextView) findViewById(R.id.headingText);
        agendafield = (TextView) findViewById(R.id.agendaText);
        partyfield = (TextView) findViewById(R.id.partyfield);
        acceptbtn = (Button) findViewById(R.id.cancelbtn);
        rejectbtn = (Button) findViewById(R.id.rejectbtn);

        headingfield.setText(heading);
        headingfield.setTextColor(Color.BLUE);
        agendafield.setText(agenda);
        agendafield.setTextColor(Color.BLUE);
        partyfield.setText("-" + party);
        partyfield.setTextColor(Color.RED);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(fd).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (final DataSnapshot child : children) {
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if (meetingInformation.agenda.equals(agenda) && meetingInformation.heading.equals(heading) /* && (meetingInformation.state.equals("requested") || meetingInformation.state.equals("ignored"))*/) {
                                databaseReference.child("requests").child(fd).child(child.getKey()).child("state").setValue("accepted");
                                final String uid = child.getKey();
                                databaseReference.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                        for (DataSnapshot child : children) {
                                            NotificationData notificationData = child.getValue(NotificationData.class);
                                            if (notificationData.from != null) {
                                                if (notificationData.from.equals(uid)) {
                                                    if (notificationData.uqid.equals(fd)) {
                                                        databaseReference.child("notifications").child(child.getKey()).child("type").setValue("accepted");
                                                        managedb();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_SHORT).show();
                                if (getIntent().getStringExtra("backbtn").equals("rqst")) {
                                    Intent intent = new Intent(PopupshowActivity3.this, RequestsHandleActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(PopupshowActivity3.this, WeekActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(fd).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if(meetingInformation.agenda.equals(agenda)&& meetingInformation.heading.equals(heading) /*&& meetingInformation.state.equals("requested")*/) {
                                databaseReference.child("requests").child(fd).child(child.getKey()).child("state").setValue("rejected");
                                final String uid = child.getKey();
                                databaseReference.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                        for (DataSnapshot child : children) {
                                            NotificationData notificationData = child.getValue(NotificationData.class);
                                            if (notificationData.from != null) {
                                                if (notificationData.from.equals(uid)) {
                                                    if (notificationData.uqid.equals(fd)) {
                                                        databaseReference.child("notifications").child(child.getKey()).child("type").setValue("rejected");
                                                        managedb();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Ignored", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }
                        finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        partyfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(fd).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if (meetingInformation.agenda.equals(agenda) && meetingInformation.heading.equals(heading)) {
                                String u_id = child.getKey();
                                databaseReference.child("users").child(u_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                                        Intent intent = new Intent(PopupshowActivity3.this, ClientProfileActivity.class);
                                        intent.putExtra("name", userInformation.name);
                                        intent.putExtra("phone", userInformation.phone);
                                        intent.putExtra("email", userInformation.email);
                                        if (userInformation.company.isEmpty()) {
                                            intent.putExtra("company", "-");
                                            intent.putExtra("position", "-");
                                        } else {

                                            intent.putExtra("company", userInformation.company);
                                            intent.putExtra("position", userInformation.position);
                                        }
                                        managedb();
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }
    /** manage database either delete em or move em. **/
    public void managedb(){

        databaseReference.child("dbms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null) {
                    String date = dataSnapshot.getValue().toString().trim();
                    Log.i("jkjjj", "onDataChange: " + date);
                    String year = new String();
                    String month = new String();
                    String day = new String();
                    int j = 0;
                    int m = 0;
                    int d = 0;
                    for (int i = 0; i < date.length(); i++) {
                        if (date.charAt(i) == '-') {
                            j++;
                            continue;
                        }

                        if (j == 0) {
                            String temp = String.valueOf(date.charAt(i));
                            year = year + temp;
                            continue;
                        }
                        if (j == 1) {
                            String temp = String.valueOf(date.charAt(i));
                            month = month + temp;
                            m++;
                            continue;
                        }
                        if (j == 2) {
                            String temp = String.valueOf(date.charAt(i));
                            day = day + temp;
                            d++;
                            continue;
                        }
                    }

                    int temp = Integer.valueOf(month);
                    temp++;
                    String month2 = String.valueOf(temp);
                    String dbms = year + "-" + month2 + "-" + day;
                    Log.i("sumaney", "onDataChange: " + dbms);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                    Date date2 = null;
                    try {
                        date2 = formatter.parse(dbms);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat formatterq = new SimpleDateFormat("yyyy-mm-dd");
                    Date date3 = new Date();
                    if(date3.after(date2)){
                        Log.i("lovelyman", "yes: ");
                    }
                    else{
                        Log.i("lovelyman", "no: ");
                    }
                    Log.i("sumaney", "onDataChange: " + date2);
                    Log.i("sumaney", "current date: " + date3);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
