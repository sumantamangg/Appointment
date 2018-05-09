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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PopupshowActivity3 extends AppCompatActivity {

    private TextView headingfield;
    private TextView agendafield;
    private TextView partyfield;
    private TextView tv;
    private Button acceptbtn;
    private Button rejectbtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupshow3);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        String party = getIntent().getStringExtra("name");
        final String heading = getIntent().getStringExtra("heading");
        final String agenda = getIntent().getStringExtra("agenda");
        String phone = getIntent().getStringExtra("phone");
        final String fd = getIntent().getStringExtra("fd");

        headingfield = (TextView) findViewById(R.id.headingText);
        agendafield = (TextView) findViewById(R.id.agendaText);
        partyfield = (TextView) findViewById(R.id.partyfield);
        acceptbtn = (Button) findViewById(R.id.acceptbtn);
        rejectbtn = (Button) findViewById(R.id.rejectbtn);

        headingfield.setText(heading);
        headingfield.setTextColor(Color.BLUE);
        agendafield.setText(agenda);
        agendafield.setTextColor(Color.BLUE);
        partyfield.setText("-"+party);
        partyfield.setTextColor(Color.RED);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(fd).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if(meetingInformation.agenda.equals(agenda)&& meetingInformation.heading.equals(heading)) {
                                databaseReference.child("requests").child(fd).child(child.getKey()).child("state").setValue("accepted");
                                Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_SHORT).show();
                                if (getIntent().getStringExtra("backbtn").equals("rqst")) {
                                    Intent intent = new Intent(PopupshowActivity3.this, RequestsHandleActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(PopupshowActivity3.this, WeekActivity.class);
                                    finish();
                                    startActivity(intent);
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
        /*
        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(fd).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if(meetingInformation.agenda.equals(agenda)&& meetingInformation.heading.equals(heading)) {
                                databaseReference.child("requests").child(fd).child(child.getKey()).child("state").setValue("ignore");
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
        */
        partyfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(fd).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if(meetingInformation.agenda.equals(agenda)&& meetingInformation.heading.equals(heading)) {
                                String u_id = child.getKey();
                                databaseReference.child("users").child(u_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UserInformation userInformation  = dataSnapshot.getValue(UserInformation.class);
                                        Intent intent = new Intent(PopupshowActivity3.this, ClientProfileActivity.class);
                                        intent.putExtra("name",userInformation.name);
                                        intent.putExtra("phone",userInformation.phone);
                                        intent.putExtra("email",userInformation.email);
                                        if(userInformation.company.isEmpty()){
                                            intent.putExtra("company","-");
                                            intent.putExtra("position", "-");
                                    }
                                        else {

                                            intent.putExtra("company",userInformation.company);
                                            intent.putExtra("position",userInformation.position);
                                        }
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
}
