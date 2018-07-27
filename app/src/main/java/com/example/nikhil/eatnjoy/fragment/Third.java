package com.example.nikhil.eatnjoy.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nikhil.eatnjoy.HomeActivity;
import com.example.nikhil.eatnjoy.R;
import com.example.nikhil.eatnjoy.adpter.MyAdapter2;
import com.example.nikhil.eatnjoy.model.Bean;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Third extends Fragment
{

    RecyclerView rv;

    List<Bean> list;

    RecyclerView.Adapter ad;

    FirebaseDatabase fd;
    DatabaseReference dr;
    Button b;

    int totalPrice;

    List<String> total;

    TextView t1,t2;
    Button b1;

    Toolbar toolbar;
    LinearLayout linearLayout;
    CircleProgressBar circleProgressBar;



    public Third()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v1=inflater.inflate(R.layout.fragment_mycartorder, container, false);

//        toolbar= v1.findViewById(R.id.toolbar);
//
//        toolbar.setTitle("Cart");
//
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setTitleTextColor(Color.WHITE);

        linearLayout=v1.findViewById(R.id.progresslayout);
        circleProgressBar=v1.findViewById(R.id.progress_bar);

        circleProgressBar.setColorSchemeResources(R.color.colorPrimary);
        linearLayout.setVisibility(View.VISIBLE);
        circleProgressBar.setVisibility(View.VISIBLE);



//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                getActivity().onBackPressed();
//            }
//        });

        rv=v1.findViewById(R.id.recycler2);
        b=v1.findViewById(R.id.btnContinue);
        t1=v1.findViewById(R.id.a);   //rs.
        t2=v1.findViewById(R.id.b);   //rs.
        b1=v1.findViewById(R.id.c);   //rs. wla button


        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false){

            @Override
            public boolean canScrollHorizontally()
            {
                return false;
            }

            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        });


        fd= FirebaseDatabase.getInstance();
        dr=fd.getReference("shivam");

        list=new ArrayList<Bean>();
        total=new ArrayList<String>();

        dr.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                linearLayout.setVisibility(View.GONE);
                circleProgressBar.setVisibility(View.GONE);

                list.clear();
                total.clear();
                totalPrice=0;

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Bean bean=ds.getValue(Bean.class);
                    list.add(bean);

                    total.add(bean.getPrice());
                }

                for (int i=0;i<total.size();i++)
                {
                    totalPrice=totalPrice+Integer.parseInt(total.get(i));
                }

                t1.setText("₹"+totalPrice+"/-");
                t2.setText("₹"+totalPrice+"/-");
                b1.setText("₹"+totalPrice+"/-");

                ad=new MyAdapter2(getActivity(),list);  //mycart m add items isi ki wajeh s ho rhe h
                rv.setAdapter(ad);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent i=new Intent(getActivity(), HomeActivity.class);
                startActivity(i);

//                FragmentManager fm=getFragmentManager();
//                FragmentTransaction ft=fm.beginTransaction();
//                ft.replace(R.id.main_layout,new Four());
//                ft.addToBackStack("sdxsgcgk");
//                ft.commit();

            }
        });


        return v1;
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
