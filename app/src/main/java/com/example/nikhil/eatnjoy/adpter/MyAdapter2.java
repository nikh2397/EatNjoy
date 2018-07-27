package com.example.nikhil.eatnjoy.adpter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nikhil.eatnjoy.R;
import com.example.nikhil.eatnjoy.model.Bean;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HP 840 G1 ULTRABOOK on 7/7/2018.
 */

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    private Context context;
    private List<Bean> list;

    String price;
    String name, url;
    String finalPrice;

    DatabaseReference dr, dr2;


    public MyAdapter2(Context context, List<Bean> list) {
        this.context = context;
        this.list = list;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {   //1.is method s view get kra
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layoutt, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v1, list, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {  // 3. It holds the id of components...
        Bean bean = list.get(position);

        holder.t1.setText(bean.getName());
        holder.t2.setText("₹" + bean.getPrice() + "/-");
//        holder.t4.append("10");
        holder.t3.setText("" + bean.getQuantity());
        Glide.with(context).load(bean.getUrl()).into(holder.im);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }  //2


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {  //2
        public ImageView im;
        public TextView t1, t2, t3, t4;
        public Button b1, b2;
        int quantity = 1;
        String id;

        List<Bean> list = new ArrayList<Bean>();
        Context context;

        ArrayList<String> al;
        int initialPrice;

        public MyViewHolder(View itemView, List<Bean> list, Context context) {
            super(itemView);

            dr = FirebaseDatabase.getInstance().getReference("shivam");
            dr2 = FirebaseDatabase.getInstance().getReference("details");

            this.list = list;
            this.context = context;

            im = itemView.findViewById(R.id.im1);  //food img
            t1 = itemView.findViewById(R.id.tvpicturename);  //picture name
            t2 = itemView.findViewById(R.id.tvprice);  //price
            t3 = itemView.findViewById(R.id.quantity);  //quantity
            t4 = itemView.findViewById(R.id.stockid);  //stock id
            b1 = itemView.findViewById(R.id.positivebutton);
            b2 = itemView.findViewById(R.id.negativebutton);

            b1.setOnClickListener(this);
            b2.setOnClickListener(this);

        }


        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();  //position of list(arraylist) ndadapter issaved in the bean class object

            Bean bean = list.get(position);

            al = new ArrayList<String>();

            id = bean.getId();
            name = bean.getName();
            url = bean.getUrl();
            quantity = Integer.parseInt(bean.getQuantity());

            if (v.getId() == R.id.positivebutton) {

                if (quantity < 10)
                {
                    dr2.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                String val = ds.getValue(String.class);
                                al.add(val);
                            }

                            initialPrice = Integer.parseInt(al.get(3));

                            quantity++;
                            t3.setText("" + quantity);
                            t2.setText("₹" + (initialPrice * quantity) + "/-");

                            Bean b = new Bean();

                            b.setName(name);
                            b.setId(id);
                            b.setUrl(url);
                            b.setQuantity(t3.getText().toString());

                            if (t2.getText().toString().length() == 6) {
                                finalPrice = t2.getText().toString().substring(1, 4);
                            } else if (t2.getText().toString().length() == 7) {
                                finalPrice = t2.getText().toString().substring(1, 5);
                            } else if (t2.getText().toString().length() == 8) {
                                finalPrice = t2.getText().toString().substring(1, 6);
                            }

                            b.setPrice(finalPrice);

                            dr.child(id).setValue(b);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    Toast.makeText(context, "Quantity is not exceeded more...", Toast.LENGTH_SHORT).show();
                }
            }


            if (v.getId() == R.id.negativebutton)
            {

                if (quantity > 1)
                {
                    dr2.child(id).addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                String val = ds.getValue(String.class);
                                al.add(val);
                            }

                            initialPrice = Integer.parseInt(al.get(3));

                            quantity--;
                            Log.e("quantity :",""+quantity);
                            t3.setText("" + quantity);
                            t2.setText("₹" + (initialPrice * quantity) + "/-");

                            Bean b = new Bean();

                            b.setName(name);
                            b.setId(id);
                            b.setUrl(url);
                            b.setQuantity(t3.getText().toString());

                            if (t2.getText().toString().length() == 6)
                            {
                                finalPrice = t2.getText().toString().substring(1, 4);
                            }
                            else if (t2.getText().toString().length() == 7)
                            {
                                finalPrice = t2.getText().toString().substring(1, 5);
                            }
                            else if (t2.getText().toString().length() == 8)
                            {
                                finalPrice = t2.getText().toString().substring(1, 6);
                            }

                            b.setPrice(finalPrice);

                            dr.child(id).setValue(b);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });

                }

                else
                {
                    Toast.makeText(context, "Invalid Quantity...", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

}
