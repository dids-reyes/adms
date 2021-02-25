package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class public_a extends AppCompatActivity {
    Button announcement;
    EditText pa;
    TextView pa2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_a);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        int color = Color.parseColor("#FFFFFF");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        pa = (EditText) findViewById(R.id.pa_text);
        pa2 = (TextView) findViewById(R.id.pa2);
        announcement = (Button) findViewById(R.id.button3);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference id = root.child("Public").child("announcements");
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Message").getValue().toString();

                pa2.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        id.addListenerForSingleValueEvent(valueEventListener1);

        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Map<String,Object> map=new HashMap<>();
                    map.put("Message", pa.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("Public")
                            .child("announcements").updateChildren(map)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    storeDate_s();
                                    storeDate_a();
                                    Toast.makeText(getApplicationContext(), "Broadcasting", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                catch (Exception e)
                {
                    Toast.makeText(public_a.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void storeDate_s() {
        try{
            SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat t = new SimpleDateFormat("h:mm a");
            Date date1 = new Date();
            Date date = new Date();
            String date_str = dt.format(date);
            Map<String, Object> map = new HashMap<>();
            map.put("Date", date_str);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Public").child("announcements");
            databaseReference.updateChildren(map);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void storeDate_a() {
        try{
            SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat t = new SimpleDateFormat("h:mm a");
            Date date1 = new Date();
            Date date = new Date();
            String date_str = dt.format(date);
            String date1_str = t.format(date1);
            Map<String, Object> map = new HashMap<>();
            map.put("Email", "admin");
            map.put("Time", date1_str);
            map.put("Date", date_str);
            map.put("Attempt", "Announcement");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
            databaseReference.push().setValue(map);
        }
        catch (Exception e)
        {
        }
    }
}