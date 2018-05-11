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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientHomeScreenActivity extends AppCompatActivity {

    private Button profilebtn;
    private Button requestbtn;
    private Button myrequestsbtn;
    private Button logoutbtn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_screen);

        requestbtn = (Button) findViewById(R.id.requestbtn);
        profilebtn = (Button) findViewById(R.id.profilebtn);
        myrequestsbtn = (Button) findViewById(R.id.myrequestsbtn);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    if (userInformation.address == null) {
                        final Intent intent = new Intent(ClientHomeScreenActivity.this, PeresonalDetailsActivity.class);
                        intent.putExtra("name", userInformation.name);
                        intent.putExtra("phone", userInformation.phone);
                        intent.putExtra("email", userInformation.email);
                        intent.putExtra("u_id", auth.getCurrentUser().getUid());
                        intent.putExtra("msg", "You haven't completed your Signup. Please complete it");
                        finish();
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
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
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
                startActivity(new Intent(ClientHomeScreenActivity.this, MainActivity.class));
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
