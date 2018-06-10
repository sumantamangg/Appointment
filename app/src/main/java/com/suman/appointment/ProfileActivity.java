package com.suman.appointment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseUser user;
    String u_id;
    private EditText view_name;
    private EditText view_phone;
    private EditText view_email;
    private EditText companyfield;
    private EditText positionfield;
    private EditText addressfield;
    private EditText nationalityfield;
    private EditText dobfield;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();
    private Button updatebtn;
    private EditText passwordfield;
    private Button applyupdatebtn;
    private TextView txt1;
    private TextView txt0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        u_id=user.getUid();
        view_name =  findViewById(R.id.name_field);
        view_phone =  findViewById(R.id.ph_field);
        view_email =  findViewById(R.id.email_field);
        companyfield =  findViewById(R.id.companyf);
        positionfield =  findViewById(R.id.postitionf);
        addressfield =  findViewById(R.id.addressf);
        nationalityfield =  findViewById(R.id.nationalityf);
        dobfield = findViewById(R.id.dobfield);
        String em=user.getEmail();
        view_email.setText("Email: "+em);
        updatebtn = findViewById(R.id.updatebtn);
        passwordfield = findViewById(R.id.passwordfield);
        applyupdatebtn = findViewById(R.id.applyupdatebtn);
        txt0 = findViewById(R.id.txt0);
        txt1 = findViewById(R.id.txt1);
        //view_email = user.getEmail().toString();
        databaseReference.child("users").child(u_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                view_email.setText(userInformation.email);
                view_name.setText(userInformation.name);
                view_phone.setText(userInformation.phone);
                addressfield.setText(userInformation.address);
                nationalityfield.setText(userInformation.nationality);
                dobfield.setText(userInformation.dob);
                if(userInformation.company.isEmpty()){
                    companyfield.setText("-");
                    positionfield.setText("-");
                }
                companyfield.setText(userInformation.company);
                positionfield.setText(userInformation.position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(auth.getCurrentUser()==null){
            finish();
            Intent i = new Intent( ProfileActivity.this, MainActivity.class);
            startActivity(i);
        }
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordfield.setVisibility(view.VISIBLE);
                updatebtn.setVisibility(view.GONE);
                applyupdatebtn.setVisibility(view.VISIBLE);
                view_name.setFocusableInTouchMode(true);
                view_email.setFocusableInTouchMode(true);
                view_phone.setFocusableInTouchMode(true);
                companyfield.setFocusableInTouchMode(true);
                positionfield.setFocusableInTouchMode(true);
                addressfield.setFocusableInTouchMode(true);
                dobfield.setVisibility(view.GONE);
                nationalityfield.setVisibility(view.GONE);
                txt0.setVisibility(view.GONE);
                txt1.setVisibility(view.GONE);

            }
        });
        applyupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= view_name.getText().toString().trim();
                String email = view_email.getText().toString().trim();
                String phone = view_phone.getText().toString().trim();
                String company = companyfield.getText().toString().trim();
                String position = positionfield.getText().toString().trim();
                String address = addressfield.getText().toString().trim();
                String password = passwordfield.getText().toString().trim();
                if(name.isEmpty() || (email.isEmpty()) || (phone.isEmpty()) || (company.isEmpty())|| (position.isEmpty()) || (address.isEmpty())){
                    Toast.makeText(getApplicationContext(),"please fill out all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "please enter correct password", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Log.i("jkjjkk"   , "onClick: "+password);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String[] arr = {name,email,phone,company,position,address,password};
                    reauthenticateuser(email,password,arr);
                }

            }
        });
    }
    public boolean reauthenticateuser (final String newemail, String password, final String arr[]){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(),password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updateEmail(newemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            databaseReference.child("users").child(user.getUid()).child("name").setValue(arr[0]);
                            databaseReference.child("users").child(user.getUid()).child("email").setValue(arr[1]);
                            databaseReference.child("users").child(user.getUid()).child("phone").setValue(arr[2]);
                            databaseReference.child("users").child(user.getUid()).child("company").setValue(arr[3]);
                            databaseReference.child("users").child(user.getUid()).child("position").setValue(arr[4]);
                            databaseReference.child("users").child(user.getUid()).child("address").setValue(arr[5]);
                        }
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(),"Password is not matching ",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return false;
    }
}
