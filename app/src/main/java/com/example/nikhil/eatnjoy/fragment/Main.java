package com.example.nikhil.eatnjoy.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.eatnjoy.R;
import com.example.nikhil.eatnjoy.adpter.MyAdpter;
import com.example.nikhil.eatnjoy.helper.Constant;
import com.example.nikhil.eatnjoy.model.Bean;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Main extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DatabaseReference mDatabase;
    private List<Bean> list;
    LinearLayout linearLayout;
    TextView tvlocation;
    CircleProgressBar circleProgressBar;
    LocationManager lm;
    View v;

    public Main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView= v.findViewById(R.id.recycler);
            //getting the id of layouts
        linearLayout=v.findViewById(R.id.progresslayout);
        circleProgressBar=v.findViewById(R.id.progress_bar);

        circleProgressBar.setColorSchemeResources(R.color.colorPrimary); //color set kra
        linearLayout.setVisibility(View.VISIBLE);//jb app chalega tb y show hoga
        circleProgressBar.setVisibility(View.VISIBLE); //it is also show when app is launch

        recyclerView.setHasFixedSize(true);
//        recyclerView.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));  //recylervie k ander gridview lgaya jiske through imgs grid view m show ho rhi h
           // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        list= new ArrayList<Bean>();  //arraylist ki parent class list h

        mDatabase= FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        mDatabase.addValueEventListener(new ValueEventListener()  //This listener is used to retrieve the data
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                linearLayout.setVisibility(View.GONE);   //view.gone mtlb gyb hojayega
                circleProgressBar.setVisibility(View.GONE);

                for (DataSnapshot postSnapshot :dataSnapshot.getChildren())
                {
                    Bean bean=postSnapshot.getValue(Bean.class);  //Bean class is a pojo clss which is used for adding the discriptions
                    //it is also used to getter nd setter method...iski help s download ho rhi h images firebs db s
                    list.add(bean);  //till now arraylist is ready nd it has image tht r come from firebase
                }

                adapter=new MyAdpter(getActivity(),list); //list ko add kra myadapter
                recyclerView.setAdapter(adapter);           //recyclerview m add kra adapter ko

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }

        });


        //location code..id for textview
        tvlocation=v.findViewById(R.id.location);

     //   tvlocation.setText("nikhil");

//        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)!=getActivity().getPackageManager().PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION )!=getActivity().getPackageManager().PERMISSION_GRANTED)
//
//        {
//            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
//            return v;
//
//
//        }


        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!=getActivity().getPackageManager().PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION )!=getContext().getPackageManager().PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);



        }


//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                lm=(LocationManager)getActivity().getSystemService(LOCATION_SERVICE); // typecasting of location manager

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double lati=location.getLatitude();
                double longi=location.getLongitude();


                Geocoder geocoder=new Geocoder(getActivity());
                try {
                    List<Address> adr = geocoder.getFromLocation(lati,longi,2);



                    String country=adr.get(1).getCountryName();
                    String locality=adr.get(0).getLocality();
                    String postal_code=adr.get(0).getPostalCode();
                    String ad=adr.get(0).getAddressLine(0);
                    String sub_loc=adr.get(0).getSubLocality();
                    tvlocation.setText(""+sub_loc+","+locality+"");
              //      textView.append("\n\n"+country+","+locality+","+postal_code+","+ad+",\n\n"+sub_loc);

                }
                catch (Exception e)
                {
                        tvlocation.setText("Network Error,It will remove in some time");
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {


            }

            @Override
            public void onProviderEnabled(String provider) {
               Toast.makeText(getContext(), "Your GPS is ON", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getContext(), "Please Open the GPS", Toast.LENGTH_SHORT).show();

            }
        });

        return v;

    }




    @Override
    public void onResume()  //yha y backpressed p work kr rha h
    {
        super.onResume();

        if (getView()==null)
        {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction()==KeyEvent.ACTION_UP && keyCode==KeyEvent.KEYCODE_BACK)
                {
                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                    ab.setTitle("Exit");
                    ab.setMessage("Are you sure you want to exit?");
                    ab.setCancelable(false);

                    ab.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });

                    ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    ab.show();

                    return true;
                }

                return false;
            }
        });
    }


}
