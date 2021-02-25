package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class RequestOTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_o_t_p);
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

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final TextView input_num = findViewById(R.id.input_num);
        final Button get = findViewById(R.id.btn_getOTP);
        progressBar.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String num = intent.getExtras().getString("num");
        input_num.setText(num);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_num.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(RequestOTP.this, "Enter your Mobile Number", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                get.setVisibility(View.INVISIBLE);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(num)
                        .setTimeout((long) 20, TimeUnit.SECONDS)
                        .setActivity(RequestOTP.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(String verificationId,
                                                   PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                // Save the verification id somewhere
                                // ...

                                // The corresponding whitelisted code above should be used to complete sign-in.
                                progressBar.setVisibility(View.GONE);
                                finish();
                                Intent intent = new Intent(RequestOTP.this,VerifyOTP.class);
                                intent.putExtra("id", verificationId);
                                intent.putExtra("num", num);
                                startActivity(intent);
                            }

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                // Sign in with the credential
                                // ...
                                progressBar.setVisibility(View.GONE);
                                get.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                // ...
                                progressBar.setVisibility(View.GONE);
                                get.setVisibility(View.VISIBLE);
                                Toast.makeText(RequestOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

    }
    public void onBackPressed(){
        CFAlertDialog.Builder rem = new CFAlertDialog.Builder(RequestOTP.this);
        rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        rem.setMessage("Are you sure you want to exit OTP Request?");
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