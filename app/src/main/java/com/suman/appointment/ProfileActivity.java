package com.suman.appointment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseUser user;
    String u_id;
    private TextView view_name;
    private TextView view_phone;
    private TextView view_email;
    private TextView companyfield;
    private TextView positionfield;
    private TextView addressfield;
    private TextView nationalityfield;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        u_id=user.getUid();
        view_name = (TextView) findViewById(R.id.name_field);
        view_phone = (TextView) findViewById(R.id.ph_field);
        view_email = (TextView) findViewById(R.id.email_field);
        companyfield = (TextView) findViewById(R.id.companyf);
        positionfield = (TextView) findViewById(R.id.postitionf);
        addressfield = (TextView) findViewById(R.id.addressf);
        nationalityfield = (TextView) findViewById(R.id.nationalityf);
        String em=user.getEmail();
        view_email.setText("Email: "+em);
        //view_email = user.getEmail().toString();
        databaseReference.child("users").child(u_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                view_email.setText(userInformation.email);
                view_name.setText(userInformation.name);
                view_phone.setText(userInformation.phone);
                addressfield.setText(userInformation.address);
                nationalityfield.setText(userInformation.nationality);
                if(userInformation.company.isEmpty()){
                    companyfield.setText("-");
                    positionfield.setText("-");
                }
                companyfield.setText(userInformation.company);
                positionfield.setText(userInformation.position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(auth.getCurrentUser()==null){
            finish();
            Intent i = new Intent( ProfileActivity.this, MainActivity.class);
            startActivity(i);
        }





    }
}
