package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

public class message extends AppCompatActivity {
    EditText phone_num,message,email_to;
    Button send;
    ImageView image_n,rem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
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

        phone_num = (EditText) findViewById(R.id.phone);
        message = (EditText) findViewById(R.id.message_cmp);
        send = (Button) findViewById(R.id.send_btn);
        email_to = (EditText) findViewById(R.id.email_to);
        image_n = (ImageView) findViewById(R.id.imageView9);
        rem = (ImageView) findViewById(R.id.reminder_message);

        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(message.this);
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION);
                rem.setMessage("Note that: SMS charges you from your current subscription. Email uses internet connection.");
                rem.addButton("Got it", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });

        phone_num.requestFocus();

        email_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(email_to.hasFocus())
                {
                    image_n.setImageResource(R.drawable.gmail);
                }
            }
        });

        phone_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(phone_num.hasFocus())
                {
                    image_n.setImageResource(R.drawable.message_icon222);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(message.this);
                builder.setMessage("Send SMS and Email to notify?")
                        .setTitle("SMS")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // CONFIRM
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                                    {
                                        send_sms();
                                        try {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        GMailSender sender = new GMailSender("assistance.dist.sys@gmail.com",
                                                                "jnkwj-4e5ul-iuei3-7tnx4");
                                                        sender.sendMail("Assistance Distribution Monitoring System", message.getText().toString(),
                                                                "assistance.dist.sys@gmail.com", email_to.getText().toString());
                                                    } catch (Exception e) {
                                                        Log.e("SendMail", e.getMessage(), e);
                                                    }
                                                }
                                            }).start();
                                        }
                                        catch (Exception exception) {
                                            Toast.makeText(message.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // CANCEL
                            }

                        })
                        .setIcon(android.R.drawable.ic_menu_send);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
    public void send_sms()
    {
        try {
            String phoneNo = phone_num.getText().toString().trim();
            String SMS = message.getText().toString().trim();

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, SMS, null,null);
            Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}