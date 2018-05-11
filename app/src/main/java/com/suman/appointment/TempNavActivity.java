package com.suman.appointment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TempNavActivity extends AppCompatActivity {

    private Button requestbtn;
    private Button profilebtn;
    private Button myrequestsbtn;

    private Button calenderviewbtn;
    private Button weekschedulebtn;
    private Button settingsbtn;
    private Button logoutbtn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_nav);
        auth = FirebaseAuth.getInstance();


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
        if (auth.getCurrentUser() != null) {
            Log.i("jkkjj", "this is my id: " + auth.getCurrentUser().getUid());
            databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    if (userInformation.address == null) {
                        final Intent intent = new Intent(TempNavActivity.this, PeresonalDetailsActivity.class);
                        intent.putExtra("name", userInformation.name);
                        intent.putExtra("phone", userInformation.phone);
                        intent.putExtra("email", userInformation.email);
                        intent.putExtra("u_id", auth.getCurrentUser().getUid());
                        intent.putExtra("msg", "You haven't completed your Signup. Please complete it");
                        finish();
                        startActivity(intent);
                    }
                    else if(!auth.getCurrentUser().getUid().equals("BP6sgUJ3dxP0uZT4Yl8sGd9nCOk1")){
                        startActivity(new Intent(TempNavActivity.this, ClientHomeScreenActivity.class));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        requestbtn = (Button) findViewById(R.id.requestbtn);
        profilebtn = (Button) findViewById(R.id.profilebtn);
        myrequestsbtn = (Button) findViewById(R.id.myrequestsbtn);
        calenderviewbtn = (Button) findViewById(R.id.calenderviewbtn);
        weekschedulebtn = (Button) findViewById(R.id.weekschedulebtn);
        settingsbtn = (Button) findViewById(R.id.settingsbtn);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);

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
                startActivity(new Intent(getApplicationContext(), MyRequestsActivity.class));

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
    @Override
    public void  onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
