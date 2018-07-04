package com.suman.appointment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button btn_signup;
    private Button btn_login;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private MyPreferences appPreference;
    private TextView forgotpass;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    List<UserInformation> userinfo = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//
//        }
//        else{
//
//        }
        if (!isOnline()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Please Connect to internet first");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            });
            dialog.show();
        }
        mEmailField = (EditText) findViewById(R.id.email_field);
        mPasswordField = (EditText) findViewById(R.id.password_field);
        btn_signup = (Button) findViewById(R.id.signup);
        btn_login = (Button) findViewById(R.id.signin);
        forgotpass = findViewById(R.id.forgotpass);
        forgotpass.setTextColor(Color.RED);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.i("jkkjj", "this is my id: " + auth.getCurrentUser().getUid());
            progressDialog.setMessage("Connecting Please Wait!");
            progressDialog.show();
            databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    if (userInformation.address == null) {
                        final Intent intent = new Intent(MainActivity.this, PeresonalDetailsActivity.class);
                        intent.putExtra("name", userInformation.name);
                        intent.putExtra("phone", userInformation.phone);
                        intent.putExtra("email", userInformation.email);
                        intent.putExtra("u_id", auth.getCurrentUser().getUid());
                        intent.putExtra("msg", "You haven't completed your Signup. Please complete it");
                        finish();
                        progressDialog.dismiss();
                        startActivity(intent);
                    } else {
                        if (!auth.getCurrentUser().getUid().equals("BP6sgUJ3dxP0uZT4Yl8sGd9nCOk1")) {
                            startActivity(new Intent(MainActivity.this, ClientHomeScreenActivity.class));
                            progressDialog.dismiss();
                            return;
                        }
                        Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                        finish();
                        progressDialog.dismiss();
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString().trim();
                String pass = mPasswordField.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Logging in please wait!");
                progressDialog.show();
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    if (auth.getCurrentUser() != null) {
                                        Log.i("jkkjj", "this is my id: " + auth.getCurrentUser().getUid());
                                        databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                final UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                                                if (userInformation.address == null) {
                                                    final Intent intent = new Intent(MainActivity.this, PeresonalDetailsActivity.class);
                                                    intent.putExtra("name", userInformation.name);
                                                    intent.putExtra("phone", userInformation.phone);
                                                    intent.putExtra("email", userInformation.email);
                                                    intent.putExtra("u_id", auth.getCurrentUser().getUid());
                                                    intent.putExtra("msg", "You haven't completed your Signup. Please complete it");
                                                    finish();
                                                    startActivity(intent);
                                                } else if (auth.getCurrentUser().getUid().equals("BP6sgUJ3dxP0uZT4Yl8sGd9nCOk1")) {
                                                    String userId = auth.getCurrentUser().getUid();
                                                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                                    databaseReference.child("users").child(userId).child("deviceToken").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });

                                                } else {
                                                    String userId = auth.getCurrentUser().getUid();
                                                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                                    databaseReference.child("users").child(userId).child("deviceToken").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            finish();
                                                            startActivity(new Intent(MainActivity.this, ClientHomeScreenActivity.class));
                                                        }
                                                    });

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Unuccessfull", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            }
                        });

            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PasswordRecoveryActivity.class));
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

