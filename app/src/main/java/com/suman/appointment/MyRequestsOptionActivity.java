package com.suman.appointment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class MyRequestsOptionActivity extends AppCompatActivity {

    private TextView headingfield;
    private TextView agendafield;
    private Button cancelbtn;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=database.getReference();
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests_option);
        getSupportActionBar().setTitle(getIntent().getStringExtra("date"));
        auth=FirebaseAuth.getInstance();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        headingfield = (TextView) findViewById(R.id.headingText);
        agendafield = (TextView) findViewById(R.id.agendaText);
        cancelbtn = (Button) findViewById(R.id.cancelbtn);

        headingfield.setText(getIntent().getStringExtra("heading"));
        headingfield.setTextColor(Color.BLUE);
        agendafield.setText(getIntent().getStringExtra("agenda"));
        agendafield.setTextColor(Color.BLUE);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("requests").child(getIntent().getStringExtra("root")).child(auth.getCurrentUser().getUid())
                        .removeValue();
                Toast.makeText(getApplicationContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(MyRequestsOptionActivity.this,TempNavActivity.class));

            }

        });

    }
}
