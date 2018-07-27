package com.example.nikhil.eatnjoy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class OnboardingActivity extends TutorialActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("my_preferences",
                MODE_PRIVATE);


        // Check if onboarding_complete is false
        if (preferences.getBoolean("onboarding_complete", false)) {

// Start the onboarding Activity...it works after signout
            Intent intent= new Intent(this, ProfileActivity.class);
            startActivity(intent);

            // Close the main Activity
            finish();
            return;
        }


//slide1
        addFragment(new Step.Builder().setTitle("Welcome To EatNjoy!!")
                .setSummary("It seems like you are a New User!!\nWe welcome you to the official app of EatNjoy.We are happy to have you here!!")
                .setBackgroundColor(Color.parseColor("#FFFFD900")) // int background color
                .setDrawable(R.drawable.burger) // int top drawable
                .build());

//slide2
        addFragment(new Step.Builder().setTitle("aja").setSummary("Good food ordered at Good price!!! Eat-N-Joy!!")
                .setBackgroundColor(Color.parseColor("#FFFFD900"))
                .setDrawable(R.drawable.pasta ).build());


        //slide3

        addFragment(new Step.Builder().setTitle("aja").setSummary("Welcome to Eat-N-Joy")
                .setBackgroundColor(Color.parseColor("#FFFFD900"))
                .setDrawable(R.drawable.fdel_3).build());
    }

  @Override
        public void finishTutorial () {


        //without this method onboardind do not work
            SharedPreferences preferences =
                    getSharedPreferences("my_preferences", MODE_PRIVATE);

            // Set onboarding_complete to true
            preferences.edit()
                    .putBoolean("onboarding_complete",true).apply();

            // Launch the main Activity, called MainActivity
            Intent profile = new Intent(this, ProfileActivity.class);
            startActivity(profile);

            // Close the OnboardingActivity
            finish();


        }
    }

//Content provider:isk thorugh data ko read nd write krskte h...phone s kch bhi data read krskte h
