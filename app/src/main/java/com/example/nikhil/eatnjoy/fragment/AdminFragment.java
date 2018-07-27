package com.example.nikhil.eatnjoy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nikhil.eatnjoy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment {

Button login;
EditText user,pass;
    public AdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_admin, container, false);
         user=v.findViewById(R.id.editText);
        pass=v.findViewById(R.id.editText2);
        login=v.findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname=  user.getText().toString();
                String upass=pass.getText().toString();
                if(uname.equals("") || upass.equals("")) {
                    if (uname.equals("")) {
                        user.setError("enter user name");
                    }
                    else {
                        pass.setError("enter password");
                    }
                }

                if(uname.equals("admin") && upass.equals("admin123")){
                   FragmentManager fm= getFragmentManager();
                  FragmentTransaction ft= fm.beginTransaction();
                  ft.replace(R.id.main_layout,new AddImagesFragment());
                  ft.addToBackStack("12121");
                  ft.commit();
                }else {
                    Toast.makeText(getActivity(), "wrong user name or password", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }

}
