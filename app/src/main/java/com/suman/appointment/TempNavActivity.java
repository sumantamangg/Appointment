package com.suman.appointment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TempNavActivity extends AppCompatActivity {

    private Button requestbtn;
    private Button profilebtn;
    private Button myrequestsbtn;

    private Button calenderviewbtn;
    private Button weekschedulebtn;
    private Button settingsbtn;
    private Button logoutbtn;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_nav);

        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Please Connect to internet first");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(TempNavActivity.this, TempNavActivity.class));
                }
            });
            dialog.show();
        }

        requestbtn = (Button) findViewById(R.id.requestbtn);
        profilebtn = (Button) findViewById(R.id.profilebtn);
        myrequestsbtn = (Button) findViewById(R.id.myrequestsbtn);
        calenderviewbtn = (Button) findViewById(R.id.calenderviewbtn);
        weekschedulebtn = (Button) findViewById(R.id.weekschedulebtn);
        settingsbtn = (Button) findViewById(R.id.settingsbtn);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);
        auth = FirebaseAuth.getInstance();

        requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CalenderActivity.class));
            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

            }
        });
        myrequestsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //yet to be made
                Toast.makeText(getApplicationContext(), "Work in Progress", Toast.LENGTH_SHORT).show();

            }
        });
        calenderviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RequestsHandleActivity.class));

            }
        });
        weekschedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WeekActivity.class));

            }
        });
        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You are on Beta Testing!!", Toast.LENGTH_SHORT).show();

            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
                startActivity(new Intent(TempNavActivity.this, MainActivity.class));
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
