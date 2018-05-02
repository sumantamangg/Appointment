package com.suman.appointment;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderActivity extends AppCompatActivity {


    //private TextView tm;
    //private TextView ym;
    private CalendarView mcalendarview;
    private Button btnlg;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        //tm = (TextView) findViewById(R.id.temp);
        //ym = (TextView) findViewById(R.id.temp1);
//        if(auth.getCurrentUser()==null){
//            finish();
//            Intent i = new Intent( CalenderActivity.this, MainActivity.class);
//            startActivity(i);
//        }
//
        btnlg = (Button) findViewById(R.id.btn_logout);
        auth = FirebaseAuth.getInstance();
        mcalendarview = (CalendarView) findViewById(R.id.calender_view);
        mcalendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                //Log.d("date= ", Integer.toString(year));
                String day = Integer.toString(dayOfMonth);
                String mnth = Integer.toString(month + 1);
                String yr = Integer.toString(year);
                String u_id = yr + mnth + day;

                SimpleDateFormat firebaseformat = new SimpleDateFormat("yyyyMd");
                Date ddd = null;
                try {
                    ddd = firebaseformat.parse(u_id);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar ck = Calendar.getInstance();
                ck.setTime(new Date());
                //ck.add(Calendar.DATE, 6);
                if (ck.getTime().after(ddd)) {
                    Toast.makeText(getApplicationContext(), "Sorry! You need to book a week advance.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    //tm.setText("Date= "+day);
                    //ym.setText("unique id="+u_id);
                    Intent i = new Intent(CalenderActivity.this, PopupActivity.class);
                    i.putExtra("MY_kEY", u_id);
                    i.putExtra("date", yr + "-" + mnth + "-" + day);
                    startActivity(i);

                }

            }
        });
        btnlg.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                auth.signOut();
                finish();
                startActivity(new Intent(CalenderActivity.this, MainActivity.class));
            }
        });



    }
}
