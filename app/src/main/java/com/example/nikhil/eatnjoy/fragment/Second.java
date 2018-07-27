package com.example.nikhil.eatnjoy.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nikhil.eatnjoy.R;
import com.example.nikhil.eatnjoy.model.Bean;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Second extends Fragment
{

    ImageView im;
    TextView t1,t2;
    Button b1,b2,b3;

    FirebaseDatabase fd;
    DatabaseReference dr;

    String name,url,price,desc;
    String qty;

    String id;
    Menu menu;
    Toolbar toolbar;

    public Second()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_foodclick, container, false);

//        toolbar= v.findViewById(R.id.toolbar);
//
//        toolbar.setTitle("Description");
//
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setTitleTextColor(Color.WHITE);
//

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                getActivity().onBackPressed();
//            }
//        });


        im=v.findViewById(R.id.im1);
        t1=v.findViewById(R.id.tv1);
        t2=v.findViewById(R.id.tv2);
        b1=v.findViewById(R.id.button1);
        b2=v.findViewById(R.id.button2);
        b3=v.findViewById(R.id.button3);

        fd= FirebaseDatabase.getInstance();
        dr=fd.getReference("shivam");

        Bundle b=getArguments();
        name=b.getString("a");  //y data myadapter s utha rha h
        url=b.getString("b");
        price=b.getString("c");
        desc=b.getString("d");
        qty=b.getString("e");
        id=b.getString("f");

        Glide.with(getActivity()).load(url).into(im);  //image firebs s download ho rhi h url ke through

        t1.setText(""+name);
        t1.append("\n₹"+price);
        t2.setText("\n"+qty);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {               //b1 is the addtokart button
                Bean bean=new Bean();
                bean.setName(name);
                bean.setUrl(url);
                bean.setPrice(price);
                bean.setQuantity(qty);
                bean.setId(id);

                dr.child(id).setValue(bean);  //dr db ka reference h uska method child ume prameter id pass kri

                Snackbar.make(v,"Item added to cart !",3000).setAction("Action",null).show();

                b1.setVisibility(View.GONE);
                b2.setVisibility(View.VISIBLE);  //go to cart button

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {  //b2 ==go to cart

                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.main_layout,new Third());
                ft.addToBackStack("sxshcdcxd");
                ft.commit();
            }
        });


        b3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.main_layout,new Four());
                ft.addToBackStack("jmffkhv");
                ft.commit();
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (((AppCompatActivity)getActivity()).getSupportActionBar() !=null)
        {
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        }
    }


    @Override
    public void onStop()
    {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
