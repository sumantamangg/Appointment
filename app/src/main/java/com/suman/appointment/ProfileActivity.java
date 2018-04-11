package com.suman.appointment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private Button btn_logout;
    private FirebaseAuth auth;
    FirebaseUser user;
    String u_id;
    private TextView view_name;
    private TextView view_phone;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btn_logout = (Button) findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        u_id=user.getUid();
        view_name = (TextView) findViewById(R.id.name_field);
        view_phone = (TextView) findViewById(R.id.ph_field);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name=dataSnapshot.child(u_id).child("name").getValue().toString();
                    String ph=dataSnapshot.child(u_id).child("phone").getValue().toString();
                    view_name.setText("name: "+name);
                    view_phone.setText("phone: "+ph);
                }
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

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                auth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });



    }
}
