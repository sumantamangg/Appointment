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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class PeresonalDetailsActivity extends AppCompatActivity {

    private EditText addressfield;
    private EditText nationalityfield;
    private EditText companyfield;
    private EditText positionfield;
    private Button sumbitbtn;

    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    //private DatePicker dobfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peresonal_details);

        addressfield = (EditText) findViewById(R.id.addressfield);
        nationalityfield = (EditText) findViewById(R.id.nationalityfield);
        companyfield = (EditText) findViewById(R.id.companyfield);
        positionfield = (EditText) findViewById(R.id.positionfield);
        sumbitbtn = (Button) findViewById(R.id.btnsubmit);
        progressDialog = new ProgressDialog(this);
        final String u_id = getIntent().getStringExtra("u_id");
        final String name = getIntent().getStringExtra("name");
        final String ph = getIntent().getStringExtra("phone");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        sumbitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressfield.getText().toString().trim();
                String nationality = nationalityfield.getText().toString().trim();
                String company = companyfield.getText().toString().trim();
                String position = positionfield.getText().toString().trim();
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
                    final UserInformation userinformation = new UserInformation(address,nationality,company,position,ph,name);
                    progressDialog.setMessage("Submitting Please Wait");
                    progressDialog.show();
                    mDatabase.child("users").child(u_id).setValue(userinformation);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Signup Completed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CalenderActivity.class));

                }
            }
        });



    }
}
