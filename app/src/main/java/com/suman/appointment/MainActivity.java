package com.suman.appointment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    List<UserInformation> userinfo = new ArrayList<>();


//    private Button btn_signin,btn_signup;
//    private DatabaseReference mDatabase;
//    private EditText nameField;
//    private EditText email_Field;
//    private TextView view_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

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
                    } else {
                        Intent intent = new Intent(MainActivity.this, TempNavActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            startActivity(new Intent(MainActivity.this, TempNavActivity.class));
            finish();
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
                                                } else {
                                                    Intent intent = new Intent(MainActivity.this, TempNavActivity.class);
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        startActivity(new Intent(MainActivity.this, TempNavActivity.class));
                                        finish();
                                    }
//                                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
//                                finish();
//                                startActivity(new Intent(getApplicationContext(), TempNavActivity.class));
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

//        btn_signup =(Button) findViewById(R.id.signup);
//        btn_signin = (Button) findViewById(R.id.signin);
//        nameField = (EditText) findViewById(R.id.name_field);
//        email_Field = (EditText) findViewById(R.id.email_field);
//        view_name= (TextView) findViewById(R.id.name_view);
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("name");// to append to child branch
//
//        //mDatabase = FirebaseDatabase.getInstance().getReference();
//        //to fetch value from database addvalueeventlistner is used.
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String name= dataSnapshot.getValue().toString();
//                view_name.setText("name: " +name);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        //for button clicking
//        btn_signup.setOnClickListener(new View.OnClickListener() {
//             public void onClick(View view) {
//                 String name = nameField.getText().toString().trim();
//                 String email = email_Field.getText().toString().trim();
//                 //mDatabase.push().setValue(name); //push inserts the value to a random key
//                 HashMap<String, String> dataMap= new HashMap<String, String>();
//                 dataMap.put("name",name);
//                 dataMap.put("email",email);
//                 mDatabase.push().setValue(dataMap);
//                 //mDatabase.child("name").setValue(name);  //to store just one value
//
//             }
//        });
//        btn_signin.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                String name = nameField.getText().toString().trim();
//                mDatabase.child("name").setValue(name+" tamang");
//            }
//        });

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

