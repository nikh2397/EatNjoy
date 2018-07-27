package com.example.nikhil.eatnjoy.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nikhil.eatnjoy.HomeActivity;
import com.example.nikhil.eatnjoy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

FirebaseDatabase fdb;
DatabaseReference dbref;
EditText username,phone;
Button submit;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View v= inflater.inflate(R.layout.fragment_profile, container, false);




        username=v.findViewById(R.id.username);
        phone=v.findViewById(R.id.phno);
        submit=v.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String profileusername=username.getText().toString();
                String profilephonenumber=phone.getText().toString();


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor e = preferences.edit();
                e.putString("username", profileusername);
                e.putString("phone",profilephonenumber);
                e.commit();
                e.apply();





                Intent home=new Intent(getActivity(), HomeActivity.class);

                startActivity(home);

                Snackbar snackbar=Snackbar.make(v,"Successfully Submit your Information",Snackbar.LENGTH_LONG);
                snackbar.show();


            }
        });


        return v;
    }

}
