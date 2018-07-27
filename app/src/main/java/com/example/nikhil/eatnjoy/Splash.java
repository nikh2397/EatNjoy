package com.example.nikhil.eatnjoy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    ImageView imageView;
    Animation animation;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView=(ImageView)findViewById(R.id.imageView);

        //animation=AnimationUtils.loadAnimation(this,R.anim.first);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myanimation);


        imageView.setAnimation(animation);

        //getSupportActionBar().hide();

//         sharedpreferences=getSharedPreferences("eatnjoy",MODE_PRIVATE);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);

                        Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
                        startActivity(intent);
                        finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }};
        myThread.start();
    }
}
