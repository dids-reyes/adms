package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class data_append extends AppCompatActivity {
    EditText name, section, email, address;
    Button add;
    String check_req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_append);

        int color = Color.parseColor("#8BC34A");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

        name = (EditText) findViewById(R.id.stud_name);
        section = (EditText) findViewById(R.id.stud_sec);
        email = (EditText) findViewById(R.id.stud_email);
        address = (EditText) findViewById(R.id.stud_address);
        add = (Button) findViewById(R.id.add2);

        Intent intent = getIntent();
        check_req = intent.getExtras().getString("req");
        if(check_req.equals("request"))
        {
            setTitle("Registration Request");
            address.setVisibility(View.INVISIBLE);
            add.setText("Request");
        }
        if(check_req.equals("none"))
        {
            setTitle("Add Residents Data");
        }

        name.requestFocus();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_req.equals("request"))
                {
                    CFAlertDialog.Builder rem = new CFAlertDialog.Builder(data_append.this);
                    rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                    rem.setTitle("Request Data Registration?");
                    rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        if(section.length()==0)
                        {
                            dialog.dismiss();
                            Toast.makeText(data_append.this, "Mobile Number is Required", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dialog.dismiss();
                            process_request();
                        }
                    });
                    rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                    });
                    rem.show();
                }
                if(check_req.equals("none"))
                {
                    CFAlertDialog.Builder rem = new CFAlertDialog.Builder(data_append.this);
                    rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                    rem.setTitle("Add Data?");
                    rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                        process_insert();
                        storeDate_add();
                    });
                    rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                    });
                    rem.show();
                }
            }
        });
    }
    private void process_request() {
        String num = section.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name.getText().toString());
        map.put("Number", section.getText().toString());
        map.put("Email", email.getText().toString());
        map.put("Subsidy", address.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Pending").child(num)
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        name.setText("");
                        section.setText("");
                        email.setText("");
                        address.setText("");
                        Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void process_insert() {
        String num = section.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name.getText().toString());
        map.put("Number", section.getText().toString());
        map.put("Email", email.getText().toString());
        map.put("Subsidy", address.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("User").child(num)
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        name.setText("");
                        section.setText("");
                        email.setText("");
                        address.setText("");
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    public void onBackPressed(){
        finish();
    }
    public void storeDate_add() {
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
            map.put("Attempt", "Add Data");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
            databaseReference.push().setValue(map);
        }
        catch (Exception e)
        {
        }
    }
}