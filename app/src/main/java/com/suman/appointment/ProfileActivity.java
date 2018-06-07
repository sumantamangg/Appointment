package com.suman.appointment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private TextView dobfield;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();
    private Button editbtn;
    private Button isitbtn;

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
        dobfield = findViewById(R.id.dobfield);
        String em=user.getEmail();
        view_email.setText("Email: "+em);
        editbtn = findViewById(R.id.editbtn);
        isitbtn = findViewById(R.id.isitbtn);
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
                dobfield.setText(userInformation.dob);
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
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "verification email has been sent", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "varification email couldn't be sent", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
        isitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user.isEmailVerified())
                {
                    Toast.makeText(ProfileActivity.this, "verified user", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "unverified user", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}
