package com.example.nikhil.eatnjoy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikhil.eatnjoy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Four extends Fragment {


    public Four() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=inflater.inflate(R.layout.fragment_customerorderadress, container, false);

        return v;
    }

}
