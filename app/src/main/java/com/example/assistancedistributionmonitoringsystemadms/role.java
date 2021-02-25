package com.example.assistancedistributionmonitoringsystemadms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

public class role extends AppCompatActivity {
    ImageView res,officials;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
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
        res = (ImageView) findViewById(R.id.residents_img);
        officials = (ImageView) findViewById(R.id.officials_img);

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(role.this);
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION);
                rem.setMessage("Confirm role as a Resident?");
                rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                    Intent intent = new Intent(role.this,MainActivity.class); //change after testing
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String sel_role = "resident";
                    intent.putExtra("role", sel_role);
                    startActivity(intent);
                });
                rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });

        officials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(role.this);
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION);
                rem.setMessage("Confirm role as Brgy.Official?");
                rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                    Intent intent = new Intent(role.this,MainActivity.class); //change after testing
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String sel_role = "official";
                    intent.putExtra("role", sel_role);
                    startActivity(intent);
                });
                rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}