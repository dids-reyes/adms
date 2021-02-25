package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class login_e_admin extends AppCompatActivity {
    EditText email_check,pass_check;
    Button login_check;
    String email_v,pass_v;
    TextView setf,f_p2,text_l_r;
    ImageView head_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_e_admin);
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

        email_check = (EditText) findViewById(R.id.email_check);
        pass_check = (EditText) findViewById(R.id.pass_check);
        login_check = (Button) findViewById(R.id.login_check);
        setf = (TextView) findViewById(R.id.text_l_r);
        f_p2 = (TextView) findViewById(R.id.f_p2);
        final ProgressBar login_e = findViewById(R.id.login_e);
        text_l_r = (TextView) findViewById(R.id.text_l_r);
        head_img = (ImageView) findViewById(R.id.imageView14);

        login_e.setVisibility(View.INVISIBLE);

        email_check.clearFocus();

        f_p2.setPaintFlags(f_p2.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);

        f_p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                head_img.setImageResource(R.drawable.log_e_tips);
                pass_check.setEnabled(false);
                f_p2.setVisibility(View.INVISIBLE);
                text_l_r.setText("Enter your Verified Email");
                login_check.setText("RESET");
            }
        });

        login_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login_check.setVisibility(View.INVISIBLE);
                    login_e.setVisibility(View.VISIBLE);
                    email_v = email_check.getText().toString();
                    pass_v = pass_check.getText().toString();
                    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    if(text_l_r.getText().toString().equals("Enter your Verified Email"))
                    {
                        CFAlertDialog.Builder rem = new CFAlertDialog.Builder(login_e_admin.this);
                        rem.setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET);
                        rem.setMessage("Reset password for this email?");
                        rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email_v).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(login_e_admin.this, "Email to reset your password has been sent.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(login_e_admin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        });
                        rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            dialog.dismiss();
                        });
                        rem.show();
                    }
                    if(email_v.length()==0 && pass_v.length()==0)
                    {
                        YoYo.with(Techniques.Shake)
                                .duration(100)
                                .repeat(2)
                                .playOn(email_check);
                        YoYo.with(Techniques.Shake)
                                .duration(100)
                                .repeat(2)
                                .playOn(pass_check);
                        Toast.makeText(login_e_admin.this, "Enter Email & Password", Toast.LENGTH_SHORT).show();
                    }
                    firebaseAuth.signInWithEmailAndPassword(email_v,pass_v).addOnCompleteListener(task ->
                    {
                       if(task.isSuccessful())
                       {
                           if(firebaseAuth.getCurrentUser().isEmailVerified())
                           {
                               finish();
                               storeDate_s();
                               startActivity(new Intent(getApplicationContext(),admin_access_crud.class));
                           }
                           else
                           {
                               storeDate_f();
                               login_check.setVisibility(View.VISIBLE);
                               login_e.setVisibility(View.INVISIBLE);
                           }
                       }
                       else
                       {
                           storeDate_f();
                           YoYo.with(Techniques.Shake)
                                   .duration(100)
                                   .repeat(2)
                                   .playOn(email_check);
                           YoYo.with(Techniques.Shake)
                                   .duration(100)
                                   .repeat(2)
                                   .playOn(pass_check);
                           Toast.makeText(login_e_admin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           login_check.setVisibility(View.VISIBLE);
                           login_e.setVisibility(View.INVISIBLE);
                       }
                    });
                }
                catch(Exception e)
                {
                    YoYo.with(Techniques.Shake)
                            .duration(100)
                            .repeat(2)
                            .playOn(email_check);
                    YoYo.with(Techniques.Shake)
                            .duration(100)
                            .repeat(2)
                            .playOn(pass_check);
                    storeDate_f();
                    login_check.setVisibility(View.VISIBLE);
                    login_e.setVisibility(View.INVISIBLE);
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
                    String date1_str = t.format(date1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("Email", email_v);
                    map.put("Time", date1_str);
                    map.put("Date", date_str);
                    map.put("Attempt", "Succeed");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
                    databaseReference.push().setValue(map);
                }
                catch (Exception e)
                {
                    Toast.makeText(login_e_admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            map.put("Email", email_v);
            map.put("Time", date1_str);
            map.put("Date", date_str);
            map.put("Attempt", "Failed");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
            databaseReference.push().setValue(map);
        }
        catch (Exception e)
        {
            Toast.makeText(login_e_admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}