package com.suman.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class WeekActivity extends AppCompatActivity {

    private TextView datefield1;      //Decrlaring Date Field column
    private TextView datefield2;
    private TextView datefield3;
    private TextView datefield4;
    private TextView datefield5;
    private TextView datefield6;
    private TextView datefield7;

    private TextView headingfield1;   //Declaring Heading Field column
    private TextView headingfield2;
    private TextView headingfield3;
    private TextView headingfield4;
    private TextView headingfield5;
    private TextView headingfield6;
    private TextView headingfield7;


    // private ArrayList<String> meetinginfo;


    Date date;

    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        datefield1 = (TextView) findViewById(R.id.date1);
        datefield2 = (TextView) findViewById(R.id.date2);
        datefield3 = (TextView) findViewById(R.id.date3);
        datefield4 = (TextView) findViewById(R.id.date4);
        datefield5 = (TextView) findViewById(R.id.date5);
        datefield6 = (TextView) findViewById(R.id.date6);
        datefield7 = (TextView) findViewById(R.id.date7);

        headingfield1 =(TextView) findViewById(R.id.heading1);
        headingfield2 =(TextView) findViewById(R.id.heading2);
        headingfield3 =(TextView) findViewById(R.id.heading3);
        headingfield4 =(TextView) findViewById(R.id.heading4);
        headingfield5 =(TextView) findViewById(R.id.heading5);
        headingfield6 =(TextView) findViewById(R.id.heading6);
        headingfield7 =(TextView) findViewById(R.id.heading7);


        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat firebaseformat = new SimpleDateFormat("yyyy-M-d");

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        String date1=dateformat.format(c.getTime());  //get time format in string
        final String fd1=firebaseformat.format(c.getTime());  //get time in firebase format (yyyy-MM-dd)
        datefield1.setText(date1);
        c.add(Calendar.DATE, 1);                 //increase date by one day
        String date2=dateformat.format(c.getTime());
        final String fd2=firebaseformat.format(c.getTime());
        datefield2.setText(date2);
        c.add(Calendar.DATE, 1);
        String date3=dateformat.format(c.getTime());
        final String fd3=firebaseformat.format(c.getTime());
        datefield3.setText(date3);
        c.add(Calendar.DATE, 1);
        String date4=dateformat.format(c.getTime());
        final String fd4=firebaseformat.format(c.getTime());
        datefield4.setText(date4);
        c.add(Calendar.DATE, 1);
        String date5=dateformat.format(c.getTime());
        final String fd5=firebaseformat.format(c.getTime());
        datefield5.setText(date5);
        c.add(Calendar.DATE, 1);
        String date6=dateformat.format(c.getTime());
        final String fd6=firebaseformat.format(c.getTime());
        datefield6.setText(date6);
        c.add(Calendar.DATE, 1);
        String date7=dateformat.format(c.getTime());
        final String fd7=firebaseformat.format(c.getTime());
        datefield7.setText(date7);
        Log.i("testingg", "today=: "+fd1);

        printweekday(fd1,headingfield1,date1);  //call printing function
        printweekday(fd2,headingfield2,date2);  //call printing function
        printweekday(fd3,headingfield3,date3);  //call printing function
        printweekday(fd4,headingfield4,date4);  //call printing function
        printweekday(fd5,headingfield5,date5);  //call printing function
        printweekday(fd6,headingfield6,date6);  //call printing function
        printweekday(fd7,headingfield7,date7);  //call printing function

        //ArrayAdapter<MeetingInformation> arrayAdapter = new ArrayAdapter<MeetingInformation>(this, android.R.layout.simple_list_item_1, meetinginfo );


    }
    public  void printweekday(final String fd, final TextView hf, final String date){

        databaseReference.child("requests").child(fd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    hf.setText("   -");
                    hf.setTextColor(Color.RED);
                } else {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                        meetinginfo.add(meetingInformation);
                        //headingfield1.setText(meetingInformation.agenda);
                        //headingfield1.setText(meetingInformation.agenda);
                    }
                    int indicate = 0;
                    for (int i = 0; i < meetinginfo.size(); i++) {
                        if (meetinginfo.get(i).state.equals("accepted")) {
                            hf.setText(meetinginfo.get(i).heading);
                            //hf.setTextColor(Color.GREEN);

                            indicate=1;
                            meetinginfo.clear();
                            break;
                        }
                        if(meetinginfo.get(i).state.equals("cancelled")){
                            indicate =100;
                        }
                    }
                    if (indicate == 0) {
                        hf.setText("PENDING");
                        hf.setTextColor(Color.RED);
                    }

                    //Log.i("testetes", "onDataChange: "+meetinginfo.get(0).agenda);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        hf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hf.getText().equals("   -")) {
                    Toast.makeText(getApplicationContext(), "No Meeting", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(hf.getText().equals("PENDING")){
                    Intent i = new Intent(WeekActivity.this, PopupshowActivity2.class);
                    i.putExtra("fd",fd);
                    i.putExtra("hf", hf.getText());
                    i.putExtra("date", date);
                    i.putExtra("backbtn","weekactivity");
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(WeekActivity.this, PopupshowActivity.class);
                    i.putExtra("fd",fd);
                    i.putExtra("hf", hf.getText());
                    i.putExtra("date", date);

                    startActivity(i);
                }
            }
        });
    }
    @Override
    public void  onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(WeekActivity.this,AdminHomeActivity.class));
        finish();
    }

}
