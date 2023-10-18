package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    Animation anim;
    ImageView logo1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo1 = findViewById(R.id.logo);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logo1.startAnimation(anim);
    }
}
