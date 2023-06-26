package com.labhall.stylefinder001;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        icon=findViewById(R.id.icon);
        if (getSupportActionBar() != null) {  //hide action bar
            getSupportActionBar().hide();}
        rotateImg();
        new CountDownTimer(1000, 1000   ) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Intent intent =new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }.start();

    }
    public void rotateImg(){
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        findViewById(R.id.icon).startAnimation(rotateAnimation);
    }
}