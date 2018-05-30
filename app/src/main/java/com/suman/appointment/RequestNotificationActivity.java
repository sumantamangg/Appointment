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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestNotificationActivity extends AppCompatActivity {

    private TextView headingfield;
    private TextView agendafield;
    private TextView partyfield;
    private TextView reqdate;
    private Button acceptbtn;
    private Button rejectbtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_notification);

        headingfield = (TextView) findViewById(R.id.headingText);
        agendafield = (TextView) findViewById(R.id.agendaText);
        partyfield = (TextView) findViewById(R.id.partyfield);
        acceptbtn = (Button) findViewById(R.id.cancelbtn);
        rejectbtn = (Button) findViewById(R.id.rejectbtn);
        reqdate = (TextView) findViewById(R.id.reqdate);


        headingfield.setText(getIntent().getStringExtra("heading"));
        headingfield.setTextColor(Color.BLUE);
        partyfield.setText(getIntent().getStringExtra("partyname"));
        partyfield.setTextColor(Color.RED);
        agendafield.setText(getIntent().getStringExtra("agenda"));
        agendafield.setTextColor(Color.BLUE);
        reqdate.setText(getIntent().getStringExtra("reqdate"));
        reqdate.setTextColor(Color.BLUE);

        /** If the notification is cancelled type then no button should be visible  **/
        if (getIntent().getStringExtra("noti_type").equals("cancelledType")) {
            acceptbtn.setVisibility(View.INVISIBLE);
            rejectbtn.setVisibility(View.INVISIBLE);
        }
        /** Again if the notification is of accepted type then no button should be visible**/
        else if (getIntent().getStringExtra("noti_type").equals("acceptedType")) {
            acceptbtn.setVisibility(View.INVISIBLE);
            rejectbtn.setVisibility(View.INVISIBLE);
            partyfield.setVisibility(View.GONE);
        }

        /** If the request has already been accepted then it should not show accept button **/
        if (getIntent().getStringExtra("noti_type").equals("requestType")) {
            databaseReference.child("notifications").child(getIntent().getStringExtra("noti_id")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String notiType = dataSnapshot.child("type").getValue().toString().trim();
                    if (notiType.equals("accepted")){
                        acceptbtn.setVisibility(View.GONE);
                        rejectbtn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(getIntent().getStringExtra("fd")).child(getIntent()
                        .getStringExtra("from_user_id")).child("state").setValue("accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference.child("notifications").child(getIntent().getStringExtra("noti_id"))
                                .child("type").setValue("accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(RequestNotificationActivity.this, AdminHomeActivity.class));
                            }
                        });
                            }
                        });
                    }
                });


        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(getIntent().getStringExtra("fd")).child(getIntent()
                        .getStringExtra("from_user_id")).child("state").setValue("ignored").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference.child("notifications").child(getIntent().getStringExtra("noti_id"))
                                .child("type").setValue("rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(RequestNotificationActivity.this, AdminHomeActivity.class));
                            }
                        });
                    }
                });

            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(RequestNotificationActivity.this,MainActivity.class));

    }
}
