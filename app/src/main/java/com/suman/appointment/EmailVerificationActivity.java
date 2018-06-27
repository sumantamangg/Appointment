package com.suman.appointment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailVerificationActivity extends AppCompatActivity {

    private EditText emailfield;
    private Button sendverificationbtn;
    private  Button editemailbtn;
    private EditText passwordfield;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        emailfield = findViewById(R.id.emailfield);
        sendverificationbtn = findViewById(R.id.sendverif);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String prevEmail = user.getEmail();
        emailfield.setText(prevEmail);
        editemailbtn = findViewById(R.id.editemail);
        passwordfield = findViewById(R.id.passwordfield);
        progressDialog = new ProgressDialog(this);

        sendverificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Sending Verification link Please Wait!!");
                progressDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String password = passwordfield.getText().toString().trim();
                String newemail = emailfield.getText().toString().trim();
                if (password.isEmpty()) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "verification email has been sent", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "verification email couldn't be sent Try again!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }
                else {
                    updateemail(prevEmail,password,newemail);
                }

            }
        });

        editemailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailfield.setFocusableInTouchMode(true);
                passwordfield.setVisibility(view.VISIBLE);

            }
        });

    }
    public void updateemail(String prevEmail, String password, final String newemail){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(prevEmail,password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i("meroname", "user re-authenticated ");
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updateEmail(newemail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            databaseReference.child("users").child(user.getUid()).child("email").setValue(newemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(getApplicationContext(),"Your email has been updated and verification email has been sent.", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                    startActivity(new Intent(EmailVerificationActivity.this, EmailVerificationActivity.class));
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                        else {
                            progressDialog.dismiss();
                            passwrodWrongAlert();


                        }

                    }
                });

    }
    public void passwrodWrongAlert(){
        final AlertDialog.Builder dialogg = new AlertDialog.Builder(this);
        dialogg.setTitle("Wrong Passwrod");
        dialogg.setMessage("You want to try again?");
        dialogg.setCancelable(true);
        dialogg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** dismiss the dialog. **/
            }
        });
        dialogg.show();
    }
    @Override
    public void  onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(EmailVerificationActivity.this,ClientHomeScreenActivity.class));
        finish();
    }
}
