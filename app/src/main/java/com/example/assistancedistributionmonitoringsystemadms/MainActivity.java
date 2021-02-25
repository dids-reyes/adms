package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.assistancedistributionmonitoringsystemadms.Model.Admin;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeUrl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcoscg.easylicensesdialog.EasyLicensesDialogCompat;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.net.Proxy.Type.HTTP;

public class MainActivity extends AppCompatActivity {
    private static final boolean agreement = false;
    private static final boolean cred = false;
    private static final boolean check_cred = false;
    Button admin, phone_login;
    EditText phone_num;
    String confirm_dialog_message = "Unauthorized or Improper use may result in administrative disciplinary action, civil charges or criminal penalties and other sanctions. Cancel if not an administrator.";
    String confirm_dialog_title = "Use ADMS Data Administration?";
    String dialog_message_phone = "Please use the correct format of your mobile number (i.e. +639XXXXXXXXX)";
    String dialog_message_title = "Mobile Number Format: ";
    String num;
    public static String role;
    TextView test;
    public static final String shared_pref_name = "role_Stored_pref";
    public static final String role_stored_key = "key69";
    private String get_key;
    ImageView reminder,logo;
    private int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        if (InternetConnection.checkConnection(getApplicationContext())) {
            //
        } else {
            CFAlertDialog.Builder rem = new CFAlertDialog.Builder(MainActivity.this);
            rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
            rem.setTitle("No Network Access").setTextGravity(Gravity.CENTER);
            rem.setHeaderView(R.layout.network_dialog);
            rem.setCancelable(false);
            rem.addButton("Exit", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
            rem.show();
            startActivity(new Intent(getApplicationContext(),error_form.class));
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences setup_cred = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(!prefs.getBoolean(String.valueOf(agreement), false)) {
            new EasyLicensesDialogCompat(this)
                    .setTitle("License Agreement")
                    .setCancelable(false)
                    .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prefs.edit().putBoolean(String.valueOf(agreement), true).apply();
                            startActivity(new Intent(getApplicationContext(),role.class));
                        }
                    })
                    .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                            finish();
                        }
                    })
                    .show();
        }
        else
        {
            if(!setup_cred.getBoolean(String.valueOf(cred), false)) {
                setup_cred.edit().putBoolean(String.valueOf(cred), true).apply();
            }
            else
            {
                if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("role"))
                {
                    Intent intent = getIntent();
                    role = intent.getExtras().getString("role");
                    save_data();
                }
            }
        }

        admin = (Button) findViewById(R.id.button);
        phone_login = (Button) findViewById(R.id.phone_number);
        phone_num = (EditText) findViewById(R.id.Phone_Num); // Get string
        test = (TextView) findViewById(R.id.textView);
        reminder = (ImageView) findViewById(R.id.reminder);
        logo = (ImageView) findViewById(R.id.logo);

        phone_login.setBackgroundResource(R.drawable.mobile_check);
        phone_login.setEnabled(false);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==100){
                    i=0;
                }
            }
        };

        logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getApplicationContext(), n_a.class));
                return true;
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==0)
                {
                    ++i;
                    handler.sendEmptyMessageDelayed(100,3000);
                }
                else if(i==10)
                {
                    startActivity(new Intent(MainActivity.this,not_found.class));
                    i=0;
                    handler.removeMessages(100);
                }
                else { ++i; }
            }
        });

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(MainActivity.this);
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION);
                rem.setMessage("If you're not yet registered as a resident. You can tap the logo simultaneously to request a registration.");
                rem.addButton("Got it", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });

        /*admin access*/
        // Do not modify
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data();
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(MainActivity.this);
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                rem.setHeaderView(R.layout.admin_dialog);
                rem.setMessage(confirm_dialog_message);
                rem.setTitle(confirm_dialog_title).setTextGravity(Gravity.CENTER);
                rem.addButton("Continue", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    if(get_key.equals("resident"))
                    {
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),unauthorized_access.class));
                    }
                    else if(get_key.equals("official"))
                    {
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),admin_access.class));
                    }
                });
                rem.addButton("Cancel", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });

        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() >= 13)
                {
                    phone_login.setEnabled(true);
                    phone_login.setBackgroundResource(R.drawable.mobile_check);
                }
                else
                {
                    phone_login.setEnabled(false);
                    phone_login.setBackgroundResource(R.drawable.mobile_check);
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                if(s.length() >= 13)
                {
                    phone_login.setEnabled(true);
                    phone_login.setBackgroundResource(R.drawable.mobile_check);
                }
                else
                {
                    phone_login.setEnabled(false);
                    phone_login.setBackgroundResource(R.drawable.mobile_check);
                }
            }
            public void afterTextChanged(Editable s) {
                if(s.length() >= 13)
                {
                    phone_login.setEnabled(true);
                    phone_login.setBackgroundResource(R.drawable.mobile_check);
                }
                else
                {
                    phone_login.setEnabled(false);
                    phone_login.setBackgroundResource(R.drawable.mobile_check);
                }
            }
        };

        phone_num.addTextChangedListener(mTextEditorWatcher);

        phone_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(MainActivity.this);
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET);
                rem.setTitle(dialog_message_title);
                rem.setMessage(dialog_message_phone);
                rem.addButton("Okay", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
                v.setOnClickListener(null);
            }
        });

        // Database initialization for resident_login , DO NOT MODIFY
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user_residents = database.getReference("User");

        /*residents form*/
        phone_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog dialog = builder.create();
                dialog.show();
                ProgressDialog message = new ProgressDialog(MainActivity.this);
                message.setMessage("Please Wait");
                message.show();

                table_user_residents.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(phone_num.getText().toString()).exists())
                        {
                            Intent intent = new Intent(MainActivity.this,RequestOTP.class); //change after testing
                            num = phone_num.getText().toString();
                            intent.putExtra("num", num);
                            startActivity(intent);
                            dialog.dismiss();
                            message.dismiss();
                        }
                        else
                        {
                            dialog.dismiss();
                            message.dismiss();
                            Toast.makeText(MainActivity.this, "Make sure that you are a resident of Brgy. 803", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed(){
        CFAlertDialog.Builder rem = new CFAlertDialog.Builder(MainActivity.this);
        rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        rem.setTitle("Are you sure you want to exit ADMS?");
        rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
            if (getIntent().getBooleanExtra("EXIT", false)) {
                finish();
                MainActivity.super.onBackPressed();
            }
            else
            {
                MainActivity.super.onBackPressed();
            }
            MainActivity.super.onBackPressed();
            dialog.dismiss();
        });
        rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
            dialog.dismiss();
        });
        rem.show();
    }
    public static class InternetConnection {
        /**CHECK INTERNET**/
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr != null) {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

                if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                }
            }
            return false;
        }
    }
    public void save_data()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(role_stored_key, role);
        editor.apply();
    }
    public void load_data()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref_name, MODE_PRIVATE);
        get_key = sharedPreferences.getString(role_stored_key, "");
    }
}