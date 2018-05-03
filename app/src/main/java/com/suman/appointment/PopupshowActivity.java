package com.suman.appointment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

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
        agendafield = (TextView) findViewById(R.id.agendaText);
        partyfield = (TextView) findViewById(R.id.partyfield);


        final String fd = getIntent().getStringExtra("fd");

        final List<String> keys = new ArrayList<String>();
        databaseReference.child("requests").child(fd).addValueEventListener(new ValueEventListener() {
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

    }
}
