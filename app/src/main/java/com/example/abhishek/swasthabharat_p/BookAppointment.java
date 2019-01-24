package com.example.abhishek.swasthabharat_p;

import android.app.Dialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAppointment extends AppCompatActivity {

    CircleImageView profile_image;
    TextView drName,qualification,specialization,myAddress,open,closed;
    CalendarView calendarView;
    Button bookAppBtn;
    String date,time;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        profile_image=(CircleImageView)findViewById(R.id.profile_image);
        drName=(TextView)findViewById(R.id.drName);
        qualification=(TextView)findViewById(R.id.qualification);
        specialization=(TextView)findViewById(R.id.specialization);
        myAddress=(TextView)findViewById(R.id.myAdd);
        open=(TextView)findViewById(R.id.open);
        closed=(TextView)findViewById(R.id.closed);
        calendarView=(CalendarView)findViewById(R.id.calender);
        bookAppBtn=(Button)findViewById(R.id.bookAppBtn);
        java.util.Calendar c= java.util.Calendar.getInstance();
        date=c.get(java.util.Calendar.DAY_OF_MONTH)+"-"+(c.get(java.util.Calendar.MONTH)+1)
                +"-"+c.get(java.util.Calendar.YEAR);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date=""+dayOfMonth+"-"+(month+1)+"-"+year;
            }
        });
        FirebaseDatabase.getInstance().getReference("Doctors").child(getResources().getString(R.string.drNumber))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name=dataSnapshot.child("name").getValue(String.class);
                        String qual=dataSnapshot.child("qualification").getValue(String.class);
                        String spec=dataSnapshot.child("specialization").getValue(String.class);
                        String open_status=dataSnapshot.child("open").getValue(String.class);
                        String image_uri=dataSnapshot.child("profilePic").getValue(String.class);
                        if(name!=null)
                            drName.setText("Dr. "+name);
                        if(qual!=null)
                            qualification.setText(qual);
                        if(spec!=null)
                            specialization.setText(spec);
                        if(open_status!=null)
                        {
                            if(open_status.equals("1"))
                            {
                                open.setVisibility(View.VISIBLE);
                                closed.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                closed.setVisibility(View.VISIBLE);
                                open.setVisibility(View.INVISIBLE);
                            }
                        }
                        if(image_uri!=null&&flag)
                            Glide.with(getApplicationContext()).load(Uri.parse(image_uri))
                                    .override(150,150).centerCrop().into(profile_image);
                        flag=false;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        myAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(BookAppointment.this);
                dialog.setContentView(R.layout.address_dialog);
                dialog.setTitle("Address");
                dialog.show();
                final TextView city=(TextView)dialog.findViewById(R.id.city);
                final TextView location=(TextView)dialog.findViewById(R.id.location);
                final TextView fullAdd=(TextView)dialog.findViewById(R.id.fullAdd);
                final ProgressBar progressBar=(ProgressBar)dialog.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("Doctors").child(getResources().getString(R.string.drNumber))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String c=dataSnapshot.child("city").getValue(String.class);
                                String l=dataSnapshot.child("location").getValue(String.class);
                                String f=dataSnapshot.child("fullAddress").getValue(String.class);
                                if(c!=null)
                                    city.setText(getResources().getString(R.string.city)+c);
                                if(l!=null)
                                    location.setText(getResources().getString(R.string.location)+l);
                                if(f!=null)
                                    fullAdd.setText(getResources().getString(R.string.fullAdd)+f);
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
        bookAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Appointments").child(MainPage.myNumber)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final long apps=dataSnapshot.getChildrenCount();
                                if(apps==0)
                                {
                                    final Dialog dialog=new Dialog(BookAppointment.this);
                                    dialog.setContentView(R.layout.book_app_dialog);
                                    dialog.setTitle("Book Appointment");
                                    dialog.show();
                                    final EditText name=(EditText)dialog.findViewById(R.id.name);
                                    final Button button=(Button)dialog.findViewById(R.id.conf_btn);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String n=name.getText().toString();
                                            if(!TextUtils.isEmpty(n))
                                            {
                                                Class_App app=new Class_App();
                                                app.setName(n);
                                                app.setDate(date);
                                                app.setContact(MainPage.myNumber);
                                                time=""+Calendar.getInstance().getTime();
                                                FirebaseDatabase.getInstance().getReference("Doctors").child(getResources().getString(R.string.drNumber))
                                                        .child("appointments").child(date+"_"+time+"_"+MainPage.myNumber).setValue(app);
                                                FirebaseDatabase.getInstance().getReference("Appointments").child(MainPage.myNumber)
                                                        .child(date+"_"+time+"_"+MainPage.myNumber)
                                                        .setValue(app);
                                                dialog.cancel();
                                                finish();
                                            }
                                            else
                                                Toast.makeText(BookAppointment.this,"Please Enter your name...",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                                else
                                    Toast.makeText(BookAppointment.this,"You already have an appointment.",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }
}
