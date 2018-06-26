package com.suman.appointment;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyRequestsOptionActivity extends AppCompatActivity {

    private TextView headingfield;
    private TextView agendafield;
    private Button cancelbtn;
    private  Button resendbtn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests_option);
        getSupportActionBar().setTitle(getIntent().getStringExtra("date") + " (" + getIntent().getStringExtra("state") + ")");
        auth = FirebaseAuth.getInstance();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        headingfield = (TextView) findViewById(R.id.headingText);
        agendafield = (TextView) findViewById(R.id.agendaText);
        cancelbtn = (Button) findViewById(R.id.cancelbtn);
        resendbtn = findViewById(R.id.resendbtn);

        headingfield.setText(getIntent().getStringExtra("heading"));
        headingfield.setTextColor(Color.BLUE);
        agendafield.setText(getIntent().getStringExtra("agenda"));
        agendafield.setTextColor(Color.BLUE);
        if(getIntent().getStringExtra("state").equals("cancelled")){
            cancelbtn.setVisibility(View.GONE);
            resendbtn.setVisibility(View.VISIBLE);
            resendbtn.setBackgroundColor(Color.GREEN);
        }
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(getIntent().getStringExtra("root")).child(auth.getCurrentUser().getUid())
                        .child("state").setValue("cancelled");
                databaseReference.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            NotificationData notificationData = child.getValue(NotificationData.class);
                            if (notificationData.uqid!=null && getIntent().getStringExtra("root") != null) {
                                if (notificationData.uqid.equals(getIntent().getStringExtra("root"))) {
                                    databaseReference.child("notifications").child(child.getKey()).removeValue();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(MyRequestsOptionActivity.this, ClientHomeScreenActivity.class));

            }

        });
        resendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(getIntent().getStringExtra("root")).child(auth.getCurrentUser().getUid())
                        .child("state").setValue("requested").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        NotificationData notificationData=new NotificationData(auth.getCurrentUser().getUid(),"request",getIntent().getStringExtra("root"));
                        databaseReference.child("notifications").push().setValue(notificationData);
                    }
                });

                Toast.makeText(getApplicationContext(), "Request resent", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(MyRequestsOptionActivity.this, ClientHomeScreenActivity.class));
            }
        });

    }
}
