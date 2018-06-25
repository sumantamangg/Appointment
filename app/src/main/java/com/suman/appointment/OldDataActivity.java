package com.suman.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OldDataActivity extends AppCompatActivity {

    private TextView headingfield;
    private TextView agendafield;
    private TextView partyfield;
    private TextView tv;
    private ProgressDialog progressDialog;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_data);

        getSupportActionBar().setTitle(getIntent().getStringExtra("date"));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        headingfield = (TextView) findViewById(R.id.headingText);
        agendafield = (TextView) findViewById(R.id.agendaText);
        partyfield = (TextView) findViewById(R.id.partyfield);

        String party = getIntent().getStringExtra("name");
        final String heading = getIntent().getStringExtra("heading");
        final String agenda = getIntent().getStringExtra("agenda");
        final String uid = getIntent().getStringExtra("uid");

        headingfield.setText(heading);
        headingfield.setTextColor(Color.BLUE);
        agendafield.setText(agenda);
        agendafield.setTextColor(Color.BLUE);
        partyfield.setText("-" + party);
        partyfield.setTextColor(Color.RED);
        progressDialog = new ProgressDialog(this);

        partyfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading!!");
                progressDialog.show();
                databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                        Intent intent = new Intent(OldDataActivity.this, ClientProfileActivity.class);
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
                        progressDialog.dismiss();
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
