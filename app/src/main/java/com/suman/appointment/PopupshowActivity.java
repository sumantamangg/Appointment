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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PopupshowActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();

    private TextView headingfield;
    private TextView agendafield;
    private TextView partyfield;
    private Button rejectbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupshow);

        getSupportActionBar().setTitle(getIntent().getStringExtra("date"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        headingfield = (TextView) findViewById(R.id.headingText);
        headingfield.setTextColor(Color.BLUE);
        agendafield = (TextView) findViewById(R.id.agendaText);
        agendafield.setTextColor(Color.BLUE);
        partyfield = (TextView) findViewById(R.id.partyfield);
        partyfield.setTextColor(Color.RED);
        rejectbtn = (Button) findViewById(R.id.popuprj);


        final String fd = getIntent().getStringExtra("fd");
        final String hd = getIntent().getStringExtra("hd");

        final List<String> keys = new ArrayList<String>();
        databaseReference.child("requests").child(fd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                        MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                        meetinginfo.add(meetingInformation);
                        keys.add(child.getKey());
                }
                for (int i = 0; i < meetinginfo.size(); i++) {
                       if (meetinginfo.get(i).state.equals("accepted")) {
                            //headingfield.setText(meetinginfo.get(i).getClass().toString());
                           headingfield.setText(meetinginfo.get(i).heading);
                            agendafield.setText(meetinginfo.get(i).agenda);
                            databaseReference.child("users").child(keys.get(i)).child("name").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name=dataSnapshot.getValue().toString();
                                    partyfield.setText("-"+name);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            meetinginfo.clear();
                            break;
                       }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        partyfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(fd).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children){
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if (meetingInformation.state.equals("accepted")){
                                String u_id = child.getKey();
                                databaseReference.child("users").child(u_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UserInformation userInformation  = dataSnapshot.getValue(UserInformation.class);
                                        Intent intent = new Intent(PopupshowActivity.this, ClientProfileActivity.class);
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
        /* yet to be made */
        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jkjjj", "inside reject: ");
                databaseReference.child("requests").child(fd).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            if(meetingInformation.heading.equals(hd) && meetingInformation.state.equals("accepted")) {
                                databaseReference.child("requests").child(fd).child(child.getKey()).child("state").setValue("rejected");
                                final String uid = child.getKey();
                                //databaseReference.child("notifications").child(child.getKey()).child("uqid").setValue("rejected");
                                databaseReference.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                        for (DataSnapshot child : children) {
                                            NotificationData notificationData = child.getValue(NotificationData.class);
                                            if (notificationData.from != null) {
                                                if (notificationData.from.equals(uid)) {
                                                    if (notificationData.uqid.equals(fd)) {
                                                        databaseReference.child("notifications").child(child.getKey()).removeValue();
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
                                Toast.makeText(getApplicationContext(), "Rejected", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PopupshowActivity.this, WeekActivity.class));
                                finish();
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
