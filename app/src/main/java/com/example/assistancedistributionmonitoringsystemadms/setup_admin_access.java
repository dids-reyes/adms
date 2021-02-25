package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class setup_admin_access extends AppCompatActivity {
    EditText email_get,pass_get;
    Button reg;
    String email_str, pass_str;
    ImageView reminder,yahoo,gmail;
    public static final String shared_pref_name = "registered_Stored_pref";
    public static final String reg_admin = "key45";
    private boolean ceti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_admin_access);
        setTitle("Setting Up Data Administrator");
        int color = Color.parseColor("#8BC34A");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

        email_get = (EditText) findViewById(R.id.email_get);
        pass_get = (EditText) findViewById(R.id.pass_get);
        reg = (Button) findViewById(R.id.reg);
        reminder = (ImageView) findViewById(R.id.reminder_d);
        yahoo = (ImageView) findViewById(R.id.imageView13);
        gmail = (ImageView) findViewById(R.id.imageView12);
        final ProgressBar login_bar = findViewById(R.id.login_bar);
        login_bar.setVisibility(View.INVISIBLE);

        load_data();
        if(ceti == false)
        {
        }
        else if(ceti == true)
        {
            pass_get.setEnabled(false);
            email_get.setEnabled(false);
            reg.setEnabled(false);
            CFAlertDialog.Builder rem = new CFAlertDialog.Builder(setup_admin_access.this);
            rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
            rem.setTitle("You've Reached your Registration Limit").setTextGravity(Gravity.CENTER_HORIZONTAL);
            rem.setHeaderView(R.layout.limit);
            rem.setCancelable(false);
            rem.addButton("Exit", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            rem.show();
        }

        yahoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(setup_admin_access.this, "Yahoo", Toast.LENGTH_SHORT).show();
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(setup_admin_access.this, "Gmail", Toast.LENGTH_SHORT).show();
            }
        });

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(setup_admin_access.this);
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION);
                rem.setMessage("Note that admin registration is only one email per-device.");
                rem.addButton("Ok", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });



        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CFAlertDialog.Builder rem = new CFAlertDialog.Builder(setup_admin_access.this);
                    rem.setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET);
                    rem.setTitle("Email Verification");
                    rem.setMessage("We will send an email verification to verify your email address.");
                    rem.addButton("Continue", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                        reg.setVisibility(View.INVISIBLE);
                        login_bar.setVisibility(View.VISIBLE);
                        email_str = email_get.getText().toString();
                        pass_str = pass_get.getText().toString();
                        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.createUserWithEmailAndPassword(email_str,pass_str).addOnCompleteListener(task -> {
                            if(task.isSuccessful())
                            {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(setup_admin_access.this, "Registered Successfully. Don't forget to Verify your Email Address.", Toast.LENGTH_SHORT).show();
                                            pass_get.setText("");
                                            email_get.setText("");
                                            pass_get.setEnabled(false);
                                            email_get.setEnabled(false);
                                            login_bar.setVisibility(View.INVISIBLE);
                                            save_data();
                                        }
                                        else
                                        {
                                            Toast.makeText(setup_admin_access.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            pass_get.setText("");
                                            email_get.setText("");
                                            login_bar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(setup_admin_access.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pass_get.setText("");
                                email_get.setText("");
                                login_bar.setVisibility(View.INVISIBLE);
                            }
                        });
                    });
                    rem.addButton("Cancel", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                    });
                    rem.show();
                }
                catch (Exception e)
                {
                    Toast.makeText(setup_admin_access.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    login_bar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void save_data()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(reg_admin, true);
        editor.apply();
    }
    public void load_data()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref_name, MODE_PRIVATE);
        ceti = sharedPreferences.getBoolean(reg_admin, false);
    }
}