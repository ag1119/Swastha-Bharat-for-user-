package com.example.abhishek.swasthabharat_p;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity {

    TextView fillForm,morningClinic,date,conf_or_not,logout,app_or_not,app_or_not2;
    LinearLayout app_conf_container,conf_time_container;
    static String myNumber;
    ProgressBar pb1,pb2;
    String appointmentKey;
    public static final String PREF="myNumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        fillForm=(TextView)findViewById(R.id.fillForm);
        morningClinic=(TextView)findViewById(R.id.morning_clinic);
        date=(TextView)findViewById(R.id.date);
        conf_or_not=(TextView)findViewById(R.id.Conf_or_not);
        logout=(TextView)findViewById(R.id.logout);
        app_or_not=(TextView)findViewById(R.id.app_or_not_default);
        app_or_not2=(TextView)findViewById(R.id.app_or_not_default2);
        app_conf_container=(LinearLayout)findViewById(R.id.app_conf_container);
        conf_time_container=(LinearLayout)findViewById(R.id.conf_time_container);
        SharedPreferences sf=getSharedPreferences(PREF,MODE_PRIVATE);
        myNumber=sf.getString("myNumber","null");
        pb1=(ProgressBar)findViewById(R.id.pb1);
        pb2=(ProgressBar)findViewById(R.id.pb2);

        fillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(MainPage.this);
                dialog.setContentView(R.layout.form_dialog);
                dialog.setTitle("Morning Clinic");
                dialog.show();
                final TextInputEditText age=(TextInputEditText)dialog.findViewById(R.id.age);
                final Button go_to_form=(Button)dialog.findViewById(R.id.go_to_form);
                go_to_form.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String patient_age=age.getText().toString();
                        if(!TextUtils.isEmpty(patient_age))
                        {
                            if(Integer.parseInt(patient_age)>=18&&Integer.parseInt(patient_age)<=65)
                            {
                                startActivity(new Intent(MainPage.this,Form.class));
                            }
                            else
                                Toast.makeText(MainPage.this,"Sorry you can not fill this form",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainPage.this,"Please enter your age",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        morningClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,BookAppointment.class));
            }
        });

        app_conf_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(MainPage.this);
                dialog.setContentView(R.layout.cancel_app_dialog);
                dialog.setTitle("Cancel Appointment?");
                dialog.show();
                final Button yes=(Button)dialog.findViewById(R.id.yes);
                final Button no=(Button)dialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("Doctors")
                                .child(getResources().getString(R.string.drNumber)).child("appointments")
                                .child(appointmentKey).removeValue();
                        FirebaseDatabase.getInstance().getReference("Appointments").child(myNumber)
                                .child(appointmentKey).removeValue();
                        dialog.cancel();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });
        FirebaseDatabase.getInstance().getReference("Appointments").child(myNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                appointmentKey=child.getKey();
                                String d = child.child("date").getValue(String.class);
                                String c = child.child("confirm").getValue(String.class);
                                if (d != null) {
                                    date.setText(d);
                                    app_or_not.setVisibility(View.INVISIBLE);
                                    app_or_not2.setVisibility(View.INVISIBLE);
                                    app_conf_container.setVisibility(View.VISIBLE);
                                    app_conf_container.setEnabled(true);
                                    conf_time_container.setVisibility(View.VISIBLE);
                                    if (c == null)
                                        conf_or_not.setText(getResources().getString(R.string.notConf));
                                    else
                                        conf_or_not.setText(getResources().getString(R.string.conf)+" "+ c);

                                } else {
                                    app_or_not.setVisibility(View.VISIBLE);
                                    app_or_not2.setVisibility(View.VISIBLE);
                                    app_conf_container.setVisibility(View.INVISIBLE);
                                    app_conf_container.setEnabled(false);
                                    conf_time_container.setVisibility(View.INVISIBLE);
                                }
                            }
                            pb1.setVisibility(View.INVISIBLE);
                            pb2.setVisibility(View.INVISIBLE);
                        }
                        else{
                            app_or_not.setVisibility(View.VISIBLE);
                            app_or_not2.setVisibility(View.VISIBLE);
                            app_conf_container.setVisibility(View.INVISIBLE);
                            conf_time_container.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sf=getSharedPreferences(PREF,MODE_PRIVATE);
                SharedPreferences.Editor editor=sf.edit();
                editor.putString("myNumber","null");
                editor.commit();
                finish();
                startActivity(new Intent(MainPage.this,LoginPage.class));
            }
        });
    }
}
