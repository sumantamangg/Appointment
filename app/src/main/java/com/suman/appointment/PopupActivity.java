package com.suman.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopupActivity extends AppCompatActivity {

    private EditText hf;
    private EditText af;
    private Button rqbtn;
    FirebaseUser user;
    private  String u_id;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        getSupportActionBar().setTitle(getIntent().getStringExtra("date"));

        hf= (EditText) findViewById(R.id.heading_field);
        af = (EditText) findViewById(R.id.agenda_field);
        rqbtn = (Button) findViewById(R.id.requestbtn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        u_id = user.getUid();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        Intent intent = getIntent();
        final String result = intent.getStringExtra("MY_kEY");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //heading_field and Agenda_Field
        rqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heading = hf.getText().toString().trim();
                String agenda = af.getText().toString().trim();
                if(TextUtils.isEmpty(heading)){
                    Toast.makeText(getApplicationContext(), "Please Mention Heading", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(agenda)){
                    Toast.makeText(getApplicationContext(), "Please Enter your agenda", Toast.LENGTH_SHORT).show();
                    return;
                }
                MeetingInformation meetingInformation = new MeetingInformation(heading,agenda,"requested");
                mDatabase.child("requests").child(result).child(u_id).setValue(meetingInformation);
                Toast.makeText(getApplicationContext(), "Requested", Toast.LENGTH_SHORT).show();

//                Intent intent=new Intent(PopupActivity.this,CalenderActivity.class);
//                startActivity(intent);

            }
        });


    }
}
