package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    EditText input1,input2,input3,input4,input5,input6;
    Button verify;
    String key,num;
    TextView otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
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
        input1 = (EditText) findViewById(R.id.input1);
        input2 = (EditText) findViewById(R.id.input2);
        input3 = (EditText) findViewById(R.id.input3);
        input4 = (EditText) findViewById(R.id.input4);
        input5 = (EditText) findViewById(R.id.input5);
        input6 = (EditText) findViewById(R.id.input6);
        verify = (Button) findViewById(R.id.btnVerify);
        final ProgressBar verify_bar = findViewById(R.id.verify_bar);
        otp = (TextView) findViewById(R.id.otp_resend);
        OTP_inputs();
        verify_bar.setVisibility(View.INVISIBLE);

        input1.requestFocus();
        input6.clearFocus();

        key = getIntent().getStringExtra("id");
        num = getIntent().getStringExtra("num");

        CountDownTimer countDownTimer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                otp.setText("Please Wait " + (millisUntilFinished/1000) + " seconds to request another OTP.");
                otp.setEnabled(false);
            }

            @Override
            public void onFinish() {
                otp.setText("Click here to resend OTP");
                otp.setPaintFlags(otp.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                otp.setEnabled(true);
            }
        };
        countDownTimer.start();

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyOTP.this, "Requesting OTP", Toast.LENGTH_SHORT).show();
                countDownTimer.start();
                FirebaseAuth auth = FirebaseAuth.getInstance();


                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(num)
                        .setTimeout((long) 20, TimeUnit.SECONDS)
                        .setActivity(VerifyOTP.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(String verificationId,
                                                   PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                // Save the verification id somewhere
                                // ...

                                // The corresponding whitelisted code above should be used to complete sign-in.
                            }

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                // Sign in with the credential
                                // ...

                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                // ...

                                Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify.setVisibility(View.INVISIBLE);
                verify_bar.setVisibility(View.VISIBLE);
                if(input1.getText().toString().trim().isEmpty()
                        ||input2.getText().toString().trim().isEmpty()
                        ||input3.getText().toString().trim().isEmpty()
                        ||input4.getText().toString().trim().isEmpty()
                        ||input5.getText().toString().trim().isEmpty()
                        ||input6.getText().toString().trim().isEmpty()
                )
                {
                    Toast.makeText(VerifyOTP.this, "Enter valid code", Toast.LENGTH_SHORT).show();
                }
                String code = input1.getText().toString() +
                        input2.getText().toString() +
                        input3.getText().toString() +
                        input4.getText().toString() +
                        input5.getText().toString() +
                        input6.getText().toString();
                if(key!=null)
                {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(key,code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        finish();
                                        Intent pass = new Intent(VerifyOTP.this,public_info.class);
                                        pass.putExtra("num", num);
                                        startActivity(pass);
                                    }
                                    else
                                    {
                                        verify.setVisibility(View.VISIBLE);
                                        verify_bar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }
            }
        });

    }

    private void OTP_inputs()
    {
        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void onBackPressed(){
        CFAlertDialog.Builder rem = new CFAlertDialog.Builder(VerifyOTP.this);
        rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        rem.setMessage("Are you sure you want to exit OTP Verification?");
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