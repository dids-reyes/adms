package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class public_info extends AppCompatActivity {
    TextView subsidy_stat,public_announcements,registered_name,date_txt;
    DatabaseReference mDatabase; //unused
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_info);
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

        registered_name = (TextView) findViewById(R.id.registered_name);
        subsidy_stat = (TextView) findViewById(R.id.subsidy_stat);
        public_announcements = (TextView) findViewById(R.id.set_public_a);
        date_txt = (TextView) findViewById(R.id.date_txt);

        Intent intent = getIntent();
        num = intent.getExtras().getString("num");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("User").child(num);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String subsidy = dataSnapshot.child("Subsidy").getValue().toString();
                subsidy_stat.setText(subsidy);
                registered_name.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference id = root.child("Public").child("announcements");
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Message").getValue().toString();
                String date = dataSnapshot.child("Date").getValue().toString();
                public_announcements.setText(name);
                date_txt.setText(date);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        id.addListenerForSingleValueEvent(valueEventListener1);
    }
    public void onBackPressed(){
        CFAlertDialog.Builder rem = new CFAlertDialog.Builder(public_info.this);
        rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        rem.setTitle("Exit Public Data?");
        rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
            System.exit(0);
            dialog.dismiss();
        });
        rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
            dialog.dismiss();
        });
        rem.show();
    }
}