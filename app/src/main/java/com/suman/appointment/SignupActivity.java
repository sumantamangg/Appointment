package com.suman.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText name_field;
    private EditText email_field;
    private EditText ph_field;
    private EditText pass_field;
    private Button btn_signup;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_signup);

        Log.i("jkjj", "on signupacitivty: ");
        name_field = (EditText) findViewById(R.id._name);
        email_field = (EditText) findViewById(R.id.email_field);
        ph_field = (EditText) findViewById(R.id.ph_num);
        pass_field = (EditText) findViewById(R.id.password_field);
        btn_signup = (Button) findViewById(R.id.signup);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        if (auth.getCurrentUser() != null) {
            Intent i = new Intent(SignupActivity.this, ProfileActivity.class);
            startActivity(i);
        }

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("jkjj", "after signupactivity: ");
                progressDialog.setMessage("SigningUp Please Wait!");
                progressDialog.show();
                final String email = email_field.getText().toString().trim();
                final String name = name_field.getText().toString().trim();
                final String ph = ph_field.getText().toString().trim();
                String pass = pass_field.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter your Full name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ph)) {
                    Toast.makeText(getApplicationContext(), "Enter phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass_field.getText().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should be of 6 length", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email_field.getText().toString()).matches()){
                    Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                    final UserInformation userinformation = new UserInformation(name, ph, email);

//                Intent intent = new Intent(SignupActivity.this, PeresonalDetailsActivity.class);
//                                    intent.putExtra("email",email);
//                                    intent.putExtra("name",name);
//                                    intent.putExtra("phone",ph);
//                                    intent.putExtra("pass",pass);
//                                    intent.putExtra("msg","Fill up the following Information to complete your sidnup.");
//                                    finish();
//                                    startActivity(intent);
                auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    mDatabase.child("users").child(user.getUid()).setValue(userinformation);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                    finish();
                                    //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    Intent intent = new Intent(SignupActivity.this, PeresonalDetailsActivity.class);
                                    intent.putExtra("u_id", user.getUid());
                                    intent.putExtra("email", email);
                                    intent.putExtra("name", name);
                                    intent.putExtra("phone", ph);
                                    intent.putExtra("msg", "Fill up the following Information to complete your sidnup.");
                                    startActivity(intent);

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Signup Unsuccessfull please check your email address", Toast.LENGTH_SHORT).show();
                                    return;

                                }
                            }
                        });
////                HashMap<String, String> dataMap= new HashMap<String, String>();
////                dataMap.put("name",name);
////                dataMap.put("phone",ph);
////                dataMap.put("email",email);
////                dataMap.put("password",pass);
////                mDatabase.push().setValue(dataMap);
////                Toast.makeText(getApplicationContext(), "Signup successfull", Toast.LENGTH_SHORT).show();
////                Intent i = new Intent( SignupActivity.this, MainActivity.class);
////                startActivity(i);

            }
        });


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
