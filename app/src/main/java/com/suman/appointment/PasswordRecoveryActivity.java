package com.suman.appointment;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private EditText email;
    private Button resetbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        email = findViewById(R.id.emailff);
        resetbtn = findViewById(R.id.resetbtn);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = email.getText().toString().trim();
                if (TextUtils.isEmpty(useremail)){
                    Toast.makeText(getApplicationContext(), "Please enter your E-mail.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Email is wrong.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Pasword reset email has been sent.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }
            }
        });




    }

}
