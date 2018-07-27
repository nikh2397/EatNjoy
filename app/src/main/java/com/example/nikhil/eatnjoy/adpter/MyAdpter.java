package com.example.nikhil.eatnjoy.adpter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nikhil.eatnjoy.R;
import com.example.nikhil.eatnjoy.fragment.Second;
import com.example.nikhil.eatnjoy.model.Bean;

import java.util.List;

/**
 * Created by HP 840 G1 ULTRABOOK on 7/6/2018.
 */

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyViewHolder> {
    private Context con;
    private List<Bean> list;

    public MyAdpter(Context con, List<Bean> list)  //constructor bnaya myadapter ka
    {
        this.con = con;
        this.list = list;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) //1.is method s view get kra
    {

        // it is used to inflate the layout (likeas custom lv)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(view, list, con);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)  // 3. It holds the id of components...
    {
        Bean bean = list.get(position);  //list is lyk the array list
        holder.t1.setText(bean.getName()); // t1 is the name of food
        holder.t2.setText("₹" + bean.getPrice() + "/-"); // price of food
        Glide.with(con).load(bean.getUrl()).into(holder.im);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }  //2.

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  //2.
{
    public ImageView im;
    public TextView t1, t2;

    private Context con;
    private List<Bean> list;

    public MyViewHolder(View itemView, List<Bean> list, Context con) {
        super(itemView);

        this.con = con;
        this.list = list;

        itemView.setOnClickListener(this);

        im = itemView.findViewById(R.id.cardImage);
        t1 = itemView.findViewById(R.id.cardText);
        t2 = itemView.findViewById(R.id.cardText2);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        Bean bean = list.get(position);  //position of list(arraylist) ndadapter issaved in the bean class object

        Bundle b = new Bundle();
        b.putString("a", bean.getName());
        b.putString("b", bean.getUrl());
        b.putString("c", bean.getPrice());
        b.putString("d", bean.getDescription());
        b.putString("e", bean.getQuantity());
        b.putString("f", bean.getId());

        Second s = new Second();
        s.setArguments(b);

        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_layout, s);
        ft.addToBackStack("gfsgdg");
        ft.commit();

    }
}





}

