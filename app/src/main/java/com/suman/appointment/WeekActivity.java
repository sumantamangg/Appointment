package com.suman.appointment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeekActivity extends AppCompatActivity {

    private TextView datefield;
    private TextView headingfield;
    private TextView agendafield;
    Date date;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        datefield = (TextView) findViewById(R.id.date1_field);
        headingfield =(TextView) findViewById(R.id.heading1_field);
        agendafield = (TextView) findViewById(R.id.agenda1_field);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 5);

        datefield.setText("date= "+cal);


//
    }
}
