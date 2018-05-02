package com.suman.appointment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

public class PopupshowActivity3 extends AppCompatActivity {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupshow3);

        //getSupportActionBar().setTitle(getIntent().getStringExtra("date"));
        getSupportActionBar().setTitle("Request");
        getIntent().getSerializableExtra("args");

        tv = (TextView) findViewById(R.id.temp);
        //tv.setText(getIntent().getSerializableExtra("args").);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Serializable serialdata = bundle.getSerializable("Bundle");

    }
}
