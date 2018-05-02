package com.paymentview.payplan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paymentview.payplan.Model.JobProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // har objektene til JobProfile
    HashMap<String, JobProfile> hm;
    Spinner mvelgJobb;
    //Asigner mDatabase som firebase variable
    DatabaseReference mDatabase;
    // lagrer brukere i variablen
    FirebaseAuth mAuth;
    View mRegView;


    // Date and timer picker
    EditText txtDate, startTime, sluttTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Vi faar logget inn");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Kommer vi hit? Main");
        mRegView = getLayoutInflater().inflate(R.layout.register_time,null);
        //establish connection with user
        mAuth = FirebaseAuth.getInstance();
        // kobler opp til firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getCurrentUser().getUid());

        // Datepicker
        txtDate=(EditText)mRegView.findViewById(R.id.date);
        startTime=(EditText)mRegView.findViewById(R.id.timeStart);
        sluttTime=(EditText)mRegView.findViewById(R.id.timeSlutt);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChooser(startTime);
            }
        });
        sluttTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChooser(sluttTime);
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // add job to firebase
        Button tbutton = (Button)findViewById(R.id.buttonjob);
        tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
            final AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
            final View mView = getLayoutInflater().inflate(R.layout.leggtil,null);
            final EditText mjobbType = (EditText) mView.findViewById(R.id.Jobb_Type);
            final EditText mlonn = (EditText) mView.findViewById(R.id.timelonn);
            Button mlagre = (Button) mView.findViewById(R.id.tknapp);

            mbuilder.setView(mView);
            final AlertDialog dialog = mbuilder.create();
            dialog.show();

            mlagre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mjobbType.getText().toString().isEmpty() && !mlonn.getText().toString().isEmpty()) {

                        JobProfile jp = new JobProfile(mjobbType.getText().toString(), Integer.parseInt(mlonn.getText().toString()));
                        mDatabase.child("Jobs").push().setValue(jp);
                        Toast.makeText(MainActivity.this,
                                R.string.success_lagret,
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(MainActivity.this,
                                R.string.error_lagret,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            }
        });

        // register time
        final ArrayList<String> array = new ArrayList<>();
        hm = new HashMap<>();

        array.add("Velg jobbtype");

        // firebase get jobs
        System.out.println(mDatabase);
        mDatabase.child("Jobs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                JobProfile jp = dataSnapshot.getValue(JobProfile.class);
                String type = jp.getJobType();

                array.add(type);
                hm.put(type,jp);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Spinner kan bare komunnisere gjennom ArrayAdapter, mellomledd
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);

        Button tidbutton = (Button)findViewById(R.id.buttontime);
        tidbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
                mvelgJobb = (Spinner) mRegView.findViewById(R.id.velg_jobb);
                mvelgJobb.setAdapter(adapter);
                final EditText mdate = (EditText) mRegView.findViewById(R.id.date);
                final EditText mStime = (EditText) mRegView.findViewById(R.id.timeStart);
                final EditText mEtime = (EditText) mRegView.findViewById(R.id.timeSlutt);
                Button mlagre = (Button) mRegView.findViewById(R.id.tknapp);

                mbuilder.setView(mRegView);
                final AlertDialog dialog = mbuilder.create();
                dialog.show();

                mlagre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mvelgJobb.getSelectedItem().toString().isEmpty() && !mdate.getText().toString().isEmpty() && !mStime.getText().toString().isEmpty() && !mEtime.getText().toString().isEmpty() ) {
                            Toast.makeText(MainActivity.this,
                                    R.string.success_lagret,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this,
                                    R.string.error_lagret,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        Button outButton = (Button)findViewById(R.id.buttonlogout);
       outButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(MainActivity.this, LoginActivity.class));
           }
       });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // Method used for starttime and sluttime click listener
    public void timeChooser(final EditText time) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // formatting time into String
                        time.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                        if (!(sluttTime.getText().toString().isEmpty() && startTime.getText().toString().isEmpty())) {
                            try {
                                double diff = calculateTime(startTime, sluttTime);
                                calculateEarnings(diff);
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void calculateEarnings(double diff) {
        TextView krFelt = (TextView)mRegView.findViewById(R.id.viewKr);
        TextView timeFelt = (TextView)mRegView.findViewById(R.id.viewTimer);
        TextView totalFelt = (TextView)mRegView.findViewById(R.id.viewGjenstar);

        JobProfile jp = hm.get(mvelgJobb.getSelectedItem().toString());

        timeFelt.setText(getString(R.string.hour) + diff);
        krFelt.setText(getString(R.string.kr) + jp.getPayment());
        totalFelt.setText(getString(R.string.earned) + (jp.getPayment() * diff));
    }

    private double calculateTime(EditText startTime, EditText sluttTime) throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        double diff;
        Date date1 = format.parse(startTime.getText().toString());
        Date date2 = format.parse(sluttTime.getText().toString());
        if (date2.before(date1)) {
            diff = 24 - (date1.getTime() - date2.getTime());

            System.out.println("Første if-en " + "date1 " + date1 + " Date2" + date2);
        }
        else {
            System.out.println("Andre ifen");
            diff = date2.getTime() - date1.getTime();
        }
        return (diff / (60 * 60 * 1000) % 24);
    }
}
