package com.suman.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;

public class PeresonalDetailsActivity extends AppCompatActivity {

    private EditText addressfield;
    private EditText nationalityfield;
    private EditText companyfield;
    private EditText positionfield;
    private Button sumbitbtn;
    private Button logoutbtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    //private DatePicker dobfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peresonal_details);

        Toast.makeText(getApplicationContext(), getIntent().getStringExtra("msg"), Toast.LENGTH_SHORT).show();
        addressfield = (EditText) findViewById(R.id.addressfield);
        nationalityfield = (EditText) findViewById(R.id.nationalityfield);
        companyfield = (EditText) findViewById(R.id.companyfield);
        positionfield = (EditText) findViewById(R.id.positionfield);
        sumbitbtn = (Button) findViewById(R.id.btnsubmit);
        progressDialog = new ProgressDialog(this);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);

        final String u_id = getIntent().getStringExtra("u_id");
        final String name = getIntent().getStringExtra("name");
        final String ph = getIntent().getStringExtra("phone");
        final String email = getIntent().getStringExtra("email");
        final String pass = getIntent().getStringExtra("pass");
        auth = FirebaseAuth.getInstance();

        sumbitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String address = addressfield.getText().toString().trim();
                final String nationality = nationalityfield.getText().toString().trim();
                final String company = companyfield.getText().toString().trim();
                final String position = positionfield.getText().toString().trim();
                if(TextUtils.isEmpty(address)){
                    Toast.makeText(getApplicationContext(), "Adress is missing.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(nationality)){
                    Toast.makeText(getApplicationContext(), "Nationality is missing.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(company)){
                    if(TextUtils.isEmpty(position)){
                        Toast.makeText(getApplicationContext(), "Position is missing", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(!TextUtils.isEmpty(position)){
                    if(TextUtils.isEmpty(company)){
                        Toast.makeText(getApplicationContext(), "Company is missing", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(!TextUtils.isEmpty(company)){
                    UserInformation userInformation = new UserInformation( address, nationality, company, position, ph, name, email);
                   databaseReference.child("users").child(u_id).setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(getApplicationContext(), "signup completed", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(PeresonalDetailsActivity.this, TempNavActivity.class);
                                startActivity(intent);
                           }
                           else {
                               Toast.makeText(getApplicationContext(), "signup failed", Toast.LENGTH_SHORT).show();
                                return;
                           }
                       }
                   });

                }
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
                startActivity(new Intent(PeresonalDetailsActivity.this, MainActivity.class));

            }
        });

    }
}
