package com.suman.appointment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PopupshowActivity2 extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();
    final List<UserInformation> userinfo = new ArrayList<>();
    final List<String> keys = new ArrayList<String>();
    LinearLayout tableAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popupshow2);

       getSupportActionBar().setTitle(getIntent().getStringExtra("date"));
        tableAdd = findViewById(R.id.table_add);

        final String fd = getIntent().getStringExtra("fd");
        databaseReference.child("requests").child(fd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                    meetinginfo.add(meetingInformation);
                    keys.add(child.getKey());
                }
                givemeobj(meetinginfo, keys,fd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addView(final List<MeetingInformation> meetinginfo, final List<UserInformation> userinfo, final String fd) {

        for (int i = 0; i < meetinginfo.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.table_view, null);
            final TextView sn, title, party;

            sn = view.findViewById(R.id.sn);
            title = view.findViewById(R.id.title);
            party = view.findViewById(R.id.party);
            String value = String.valueOf(i + 1);
            sn.setText(value);

            title.setText((meetinginfo.get(i).heading));
            party.setText(userinfo.get(i).name);
            tableAdd.addView(view);
            final int finalI = i;
            party.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PopupshowActivity2.this, PopupshowActivity3.class);
                    intent.putExtra("name",userinfo.get(finalI).name);
                    intent.putExtra("heading",meetinginfo.get(finalI).heading);
                    intent.putExtra("agenda",meetinginfo.get(finalI).agenda);
                    intent.putExtra("phone", userinfo.get(finalI).phone);
                    intent.putExtra("fd",fd);
                    intent.putExtra("state",meetinginfo.get(finalI).state);
                    intent.putExtra("backbtn",getIntent().getStringExtra("backbtn"));
                    startActivity(intent);
                }
            });
        }
    }

    public void givemeobj(final List<MeetingInformation> meetinginfo, final List<String> key, final String fd) {
        final List<UserInformation> usss = new ArrayList<UserInformation>();

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                String jj=fd;
                for (DataSnapshot child : children) {
                    for (int i = 0; i < key.size(); i++) {
                        if (child.getKey().equals(key.get(i))) {
                            UserInformation userInformation = child.getValue(UserInformation.class);
                            usss.add(userInformation);
                        }
                    }

                }

                addView(meetinginfo, usss,jj);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Log.i("testetes", "obj: " + usss.size());


    }


}
