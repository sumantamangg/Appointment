package com.suman.appointment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ClientProfileActivity extends AppCompatActivity {

    private TextView namefield;
    private TextView emailfield;
    private TextView contactfield;
    private TextView companyfield;
    private TextView positionfield;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        namefield = (TextView) findViewById(R.id.name_field);
        emailfield = (TextView) findViewById(R.id.email_field);
        contactfield = (TextView) findViewById(R.id.ph_field);
        companyfield = (TextView) findViewById(R.id.companyf);
        positionfield = (TextView) findViewById(R.id.postitionf);

        namefield.setText(getIntent().getStringExtra("name"));
        emailfield.setText( getIntent().getStringExtra("email"));
        contactfield.setText(getIntent().getStringExtra("phone"));
        companyfield.setText(getIntent().getStringExtra("company"));
        positionfield.setText(getIntent().getStringExtra("position"));
    }
}
