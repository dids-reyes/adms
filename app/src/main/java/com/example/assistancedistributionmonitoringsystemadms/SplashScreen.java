package com.example.assistancedistributionmonitoringsystemadms;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {
    private static int TIME_OUT = 5000;
    GifImageView load_kick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        int color = Color.parseColor("#f3f3f3");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

        load_kick = (GifImageView) findViewById(R.id.load_kick_push);
        final TextView load_text = (TextView) findViewById(R.id.load_text);

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                load_text.setText("Please Wait " + String.valueOf(animation.getAnimatedValue()) + "%");
                if(animation.getAnimatedValue().hashCode() > 20 && animation.getAnimatedValue().hashCode() < 48)
                {
                    load_text.setText("Main Screen " + String.valueOf(animation.getAnimatedValue()) + "%");
                }
                else if(String.valueOf(animation.getAnimatedValue()).hashCode() > 49 && animation.getAnimatedValue().hashCode() < 68)
                {
                    load_text.setText("Utilities and Media " + String.valueOf(animation.getAnimatedValue() + "%"));
                }
                else if(String.valueOf(animation.getAnimatedValue()).hashCode() > 69 && animation.getAnimatedValue().hashCode() < 95)
                {
                    load_text.setText("Checking network access " + String.valueOf(animation.getAnimatedValue() + "%"));
                }
                else if(animation.getAnimatedValue().hashCode() < 19 && animation.getAnimatedValue().hashCode() > 95)
                {
                    load_text.setText("Please Wait " + String.valueOf(animation.getAnimatedValue()) + "%");
                }
                else if(animation.getAnimatedValue().hashCode() == 69)
                {
                    load_text.setText("What the Fuck " + String.valueOf(animation.getAnimatedValue()) + "%");
                }
            }
        });
        animator.setDuration(5000); // here you set the duration of the anim
        animator.start();

        int[] ListOfImages= {R.drawable.load_kick_push, R.drawable.skate_m};

        Random random = new Random(System.currentTimeMillis());
        int posOfImage = random.nextInt(ListOfImages.length - 0);


        load_kick.setBackgroundResource(ListOfImages[posOfImage]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },TIME_OUT);
    }
}