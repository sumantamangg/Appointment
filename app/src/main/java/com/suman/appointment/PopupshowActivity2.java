package com.suman.appointment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PopupshowActivity2 extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    final List<MeetingInformation> meetinginfo = new ArrayList<MeetingInformation>();
    List<UserInformation> userinfo = new ArrayList<>();
    List<String> keys = new ArrayList<String>();
    LinearLayout tableAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popupshow2);

        getSupportActionBar().setTitle(getIntent().getStringExtra("date"));
        tableAdd = findViewById(R.id.table_add);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        final String fd = getIntent().getStringExtra("fd");
        ValueEventListener valueEventListener = databaseReference.child(fd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    MeetingInformation meetingInformation = child.getValue(MeetingInformation.class);
                    meetinginfo.add(meetingInformation);
                    keys.add(child.getKey());
                    //keys.add(child.getKey());
                }
                Log.i("thanoss", "checking "+keys.get(0));
                List<UserInformation> temp = new ArrayList<UserInformation>();
                for(int j=0; j<keys.size();j++){
                    UserInformation ui=new UserInformation();
                    ui= (UserInformation) givemeobj(keys.get(j));
                    temp.add(ui);
                }
                addView(meetinginfo);
                //Log.i("testetes", "onDataChange: "+meetinginfo.get(0).state);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addView(List<MeetingInformation> meetinginfo) {

        for (int i = 0; i < meetinginfo.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.table_view, null);
            TextView sn, title, party;
            Log.i("lovelysex", "keys:kfsdf");
            sn = view.findViewById(R.id.sn);
            title = view.findViewById(R.id.title);
            party = view.findViewById(R.id.party);
            String value = String.valueOf(i+1);
            sn.setText(value);
            title.setText((meetinginfo.get(i).heading));
            //party.setText(meetinginfo.get(i).agenda);
            tableAdd.addView(view);

        }

    }
    public UserInformation givemeobj(String key){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation= dataSnapshot.getValue(UserInformation.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return null;
    }


}

    /*
        public void init(List<MeetingInformation> meetinginfo) {
            TableLayout stk = (TableLayout) findViewById(R.id.table_main);
            TableRow tbrow0 = new TableRow(this);
            TableLayout.LayoutParams tableRowParams=
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);


            tbrow0.setPadding(20,20,20,20);
            tbrow0.setLayoutParams(tableRowParams);

            TextView tv0 = new TextView(this);
            tv0.setText(" Sl.No");
            tv0.setTextColor(Color.WHITE);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setText(" TITLE ");
            tv1.setTextColor(Color.WHITE);
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText(" PARTY ");
            tv2.setTextColor(Color.WHITE);
            tbrow0.addView(tv2);
            stk.addView(tbrow0);
            for (int i = 0; i <meetinginfo.size(); i++) {
                TableRow tbrow = new TableRow(this);
                tbrow.setLayoutParams(tableRowParams);
                TextView t1v = new TextView(this);
                t1v.setText("" + i);
                t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(this);
                t2v.setText(meetinginfo.get(i).heading);
                t2v.setTextColor(Color.WHITE);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(this);
                t3v.setText(meetinginfo.get(i).state);
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);
            }

        }
    */