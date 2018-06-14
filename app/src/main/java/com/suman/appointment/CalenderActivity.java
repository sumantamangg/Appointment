package com.suman.appointment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {


    //private TextView tm;
    //private TextView ym;
    private CalendarView mcalendarview;
    private FirebaseAuth auth;
    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();
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
        isUserVerified();
        auth = FirebaseAuth.getInstance();
        mcalendarview = (CalendarView) findViewById(R.id.calender_view);
        mcalendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                /** check if the user is verified or not.**/
                if(!IsUserVerified()){
                    startActivity(new Intent(CalenderActivity.this, EmailVerificationActivity.class));
                    return;
                }
                //Log.d("date= ", Integer.toString(year));
                final String day = Integer.toString(dayOfMonth);
                final String mnth = Integer.toString(month + 1);
                final String yr = Integer.toString(year);
                final String u_id = yr +"-"+ mnth +"-"+ day;

                SimpleDateFormat firebaseformat = new SimpleDateFormat("yyyy-MM-dd");
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
                    meetinginfo.clear();
                    return;
                }
                else {
                    //tm.setText("Date= "+day);
                    //ym.setText("unique id="+u_id);
                    databaseReference.child("requests").child(u_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                Intent i = new Intent(CalenderActivity.this, PopupActivity.class);
                                i.putExtra("MY_kEY", u_id);
                                i.putExtra("date", yr + "-" + mnth + "-" + day);
                                meetinginfo.clear();
                                Log.i("jkjj", "does'nt exist: ");
                                startActivity(i);
                            }
                            else {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                                    meetinginfo.add(meetingInformation);
                                }
                                Log.i("jkjj", " exists: ");

                                for (int i=0; i<meetinginfo.size();i++){
                                    if(meetinginfo.get(i).state.equals("accepted")){
                                        Toast.makeText(getApplicationContext(), "Sorry! The date is not availabe. Try Another one.", Toast.LENGTH_SHORT).show();
                                        meetinginfo.clear();
                                        break;
                                    }
                                    else {
                                        if(i+1==meetinginfo.size()){
                                            Intent ii = new Intent(CalenderActivity.this, PopupActivity.class);
                                            ii.putExtra("MY_kEY", u_id);
                                            ii.putExtra("date", yr + "-" + mnth + "-" + day);
                                            meetinginfo.clear();
                                            finish();
                                            startActivity(ii);
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
//        @Override
//        public void onBackPressed()
//        {
//            super.onBackPressed();
//            startActivity(new Intent(ThisActivity.this, NextActivity.class));
//            finish();
//
//        }

    }
    @Override
    public void  onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(CalenderActivity.this,ClientHomeScreenActivity.class));
        finish();
    }
    public boolean IsUserVerified(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!user.isEmailVerified())
        {
            Toast.makeText(CalenderActivity.this, "You haven't verified your email address.", Toast.LENGTH_SHORT).show();
            return (false);
        }
       return (true);
    }
    public void isUserVerified(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!user.isEmailVerified()) {
            SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
            //boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
            //if (isFirstRun) {
                AlertDialog.Builder dialogg = new AlertDialog.Builder(this);
                dialogg.setTitle("Verify Email");
                dialogg.setMessage("Please verify your email to proceed with the app");
                dialogg.setCancelable(false);
                dialogg.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(CalenderActivity.this, EmailVerificationActivity.class));
                    }
                });
                dialogg.show();
                //Toast.makeText(MainActivity.this, "you haven't verified your Email.", Toast.LENGTH_SHORT).show();
            //}
        }

    }
}
