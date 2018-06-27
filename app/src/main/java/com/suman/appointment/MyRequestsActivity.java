package com.suman.appointment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRequestsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth auth;
    final List<String> keys1 = new ArrayList<String>();
    final List<MeetingInformation> meetininfo = new ArrayList<MeetingInformation>();
    LinearLayout tableAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        tableAdd = findViewById(R.id.table_add2);

        auth = FirebaseAuth.getInstance();
        databaseReference.child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child:children)
                    {
                        keys1.add(child.getKey());

                }
                givemedata(keys1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public void givemedata(final List<String> s){
        for(int i=0;i<s.size();i++){
            final int finalI = i;
            databaseReference.child("requests").child(s.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for(DataSnapshot child:children){
                        if(child.getKey().equals(auth.getCurrentUser().getUid())){
                            MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                            Log.i("jkjj", "inside: "+meetingInformation.heading);
                            addview(meetingInformation,s.get(finalI));
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
    private void addview(final MeetingInformation meetingInformation, final String s){
        View view = LayoutInflater.from(this).inflate(R.layout.table_view2, null);
        final TextView date,heading;

        date = view.findViewById(R.id.dt);
        heading = view.findViewById(R.id.headding);
        heading.setText(meetingInformation.heading);
        date.setText(meetingInformation.date);
        if(!date.equals("")){
            date.setText(meetingInformation.date);
        }
        else{
            date.setText("--");
        }
        tableAdd.addView(view);

        heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRequestsActivity.this, MyRequestsOptionActivity.class);
                intent.putExtra("heading",meetingInformation.heading);
                intent.putExtra("agenda",meetingInformation.agenda);
                intent.putExtra("date",meetingInformation.date);
                intent.putExtra("state",meetingInformation.state);
                intent.putExtra("root",s);
                startActivity(intent);
            }
        });

    }
}
