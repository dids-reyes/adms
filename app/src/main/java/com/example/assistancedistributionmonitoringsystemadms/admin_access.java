package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.assistancedistributionmonitoringsystemadms.Model.Admin;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcoscg.easylicensesdialog.EasyLicensesDialogCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class admin_access extends AppCompatActivity {
    EditText username,password;
    Button login;
    String key = "admin_access";
    TextView setup_admin,f_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_access);
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        int color = Color.parseColor("#FFFFFF");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Admin");

        username = (EditText) findViewById(R.id.usernametemp);
        password = (EditText) findViewById(R.id.passwordtemp);
        login = (Button) findViewById(R.id.btn_login);
        f_p = (TextView) findViewById(R.id.f_p);
        f_p.setPaintFlags(f_p.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);


        f_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login_e_admin.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog message = new ProgressDialog(admin_access.this);
                message.setMessage("Logging in");
                message.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        message.dismiss();
                        Admin admin = snapshot.child(key).getValue(Admin.class);
                        if(admin.getPassword().equals(password.getText().toString()) && admin.getName().equals(username.getText().toString()))
                        {
                            storeDate_s();
                            startActivity(new Intent(getApplicationContext(),admin_access_crud.class));
                            finish();
                        }
                        else
                        {
                            YoYo.with(Techniques.Shake)
                                    .duration(100)
                                    .repeat(2)
                                    .playOn(username);
                            YoYo.with(Techniques.Shake)
                                    .duration(100)
                                    .repeat(2)
                                    .playOn(password);
                            storeDate_f();
                            username.setEnabled(false);
                            password.setEnabled(false);
                            Snackbar snackbar = Snackbar.make(v, "Login Failed Wrong Credentials", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    username.setEnabled(true);
                                    password.setEnabled(true);
                                }
                            });
                            snackbar.show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
            String date1_str = t.format(date1);
            Map<String, Object> map = new HashMap<>();
            map.put("Email", username.getText().toString());
            map.put("Time", date1_str);
            map.put("Date", date_str);
            map.put("Attempt", "Succeed");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
            databaseReference.push().setValue(map);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void storeDate_f() {
        try{
            SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat t = new SimpleDateFormat("h:mm a");
            Date date1 = new Date();
            Date date = new Date();
            String date_str = dt.format(date);
            String date1_str = t.format(date1);
            Map<String, Object> map = new HashMap<>();
            map.put("Email", username.getText().toString());
            map.put("Time", date1_str);
            map.put("Date", date_str);
            map.put("Attempt", "Failed");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
            databaseReference.push().setValue(map);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}