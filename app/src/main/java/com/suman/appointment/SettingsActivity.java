package com.suman.appointment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private EditText oldpass;
    private EditText newpass;
    private Button resetconfirmbtn;
    private Button resetpassbtn;
    private Button delaccbtn;
    private EditText passconfirm;
    private Button passconfirmbtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        oldpass = findViewById(R.id.oldpass);
        newpass = findViewById(R.id.newpass);
        resetconfirmbtn = findViewById(R.id.resetconfirmbtn);
        resetpassbtn = findViewById(R.id.resetpassbtn);
        delaccbtn = findViewById(R.id.delaccbtn);
        passconfirm = findViewById(R.id.passconfirm);
        passconfirmbtn = findViewById(R.id.passconfirmbtn);
        progressDialog = new ProgressDialog(this);

        resetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delaccbtn.setVisibility(View.GONE);
                resetpassbtn.setVisibility(View.GONE);
                oldpass.setVisibility(View.VISIBLE);
                newpass.setVisibility(View.VISIBLE);
                resetconfirmbtn.setVisibility(View.VISIBLE);

            }
        });
        delaccbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpassbtn.setVisibility(View.GONE);
                delaccbtn.setVisibility(View.GONE);
                passconfirm.setVisibility(View.VISIBLE);
                passconfirmbtn.setVisibility(View.VISIBLE);
            }
        });
        resetconfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldpassword = oldpass.getText().toString().trim();
                final String newpassword = newpass.getText().toString().trim();
                if (TextUtils.isEmpty(oldpassword)){
                    Toast.makeText(getApplicationContext(), "Old password is missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newpassword)){
                    Toast.makeText(getApplicationContext(), "New password is missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),oldpassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Your old password is not matching.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Your password has been changed successfully.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
                                }
                            });
                        }
                    }
                });
            }
        });
        passconfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passconfirm.getText().toString().trim();
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Password is missing.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),password);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Password is Wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "your account has been deleted.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }
}
