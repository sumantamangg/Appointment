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

public class AdminHomeActivity extends AppCompatActivity {

    private Button calenderviewbtn;
    private Button weekschedulebtn;
    private Button settingsbtn;
    private Button logoutbtn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        auth = FirebaseAuth.getInstance();

        calenderviewbtn = (Button) findViewById(R.id.calenderviewbtn);
        weekschedulebtn = (Button) findViewById(R.id.weekschedulebtn);
        settingsbtn = (Button) findViewById(R.id.settingsbtn);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);


        if (auth.getCurrentUser() != null) {
            Log.i("jkkjj", "this is my id: " + auth.getCurrentUser().getUid());
            databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    if (userInformation.address == null) {
                        final Intent intent = new Intent(AdminHomeActivity.this, PeresonalDetailsActivity.class);
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
                //Toast.makeText(getApplicationContext(), "You are on Beta Testing!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminHomeActivity.this, SettingsActivity.class));
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
                finish();
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
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
