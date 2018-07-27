package com.example.nikhil.eatnjoy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikhil.eatnjoy.fragment.AdminFragment;
import com.example.nikhil.eatnjoy.fragment.Main;
import com.example.nikhil.eatnjoy.fragment.ProfileFragment;
import com.example.nikhil.eatnjoy.fragment.Third;
import com.example.nikhil.eatnjoy.fragment.developer;
import com.example.nikhil.eatnjoy.helper.Constant;
import com.example.nikhil.eatnjoy.helper.Converter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
   Menu menu;
    DatabaseReference dr;
    int cart_count=0;
    private FirebaseAuth firebaseAuth;
    TextView tv;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    ImageView im,profileim ;
    FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_nav_open);
//        FirebaseUser user = firebaseAuth.getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this,Signin.class));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EatNjoy");
        setSupportActionBar(toolbar);


        //fragmntmanager get kra to adding the fragmnt
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        ft.replace(R.id.main_layout,new Main());  //main fragmnt add kra jiski wajeh se firebase s img uploadded show ho rhi h
        ft.commit();

      //   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isFabOpen) {

                        fab.startAnimation(rotate_forward);
                        fab1.startAnimation(fab_close);
                        fab2.startAnimation(fab_close);
                        fab1.setClickable(false);
                        fab2.setClickable(false);
                        isFabOpen = false;
                        Log.d("Raj", "close");

                    } else {
                        fab.startAnimation(rotate_backward);
                        fab1.startAnimation(fab_open);
                        fab2.startAnimation(fab_open);
                        fab1.setClickable(true);
                        fab2.setClickable(true);
                        isFabOpen = true;
                        Log.d("Raj", "open");
                    }
                }
            });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

         View header=navigationView.getHeaderView(0);

        tv=header.findViewById(R.id.textView);


    //     String user=b.getString("a");
//         tv.setText(""+getIntent().getStringExtra("a"));

//        SharedPreferences sharedPreferences=getSharedPreferences("EatNjoy",MODE_PRIVATE);
//        SharedPreferences.Editor e=sharedPreferences.edit();
//
//        String user=sharedPreferences.getString("username","");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = preferences.getString("username", "");
        tv.setText(""+user);
        //im=header.findViewById(R.id.imageView);

        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu=menu;
        getMenuInflater().inflate(R.menu.icontool,menu);
        countTotalChildFirebase();

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.cart_action)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_layout,new Third());
            ft.addToBackStack("shssk");
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_layout,new Main());
            ft.commit();
            // Handle the camera action
        } else if (id == R.id.nav_admin) {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_layout,new AdminFragment());
            ft.addToBackStack("1231");
            ft.commit();

        } else if (id == R.id.food_orders) {


        } else if (id == R.id.nav_account) {

        }

        else if(id==R.id.signout){


            firebaseAuth.signOut();
            //closing activity
            finish();
         //   starting login activity
            startActivity(new Intent(this, Signin.class));

        }
        else if (id == R.id.developer) {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_layout,new developer());
            ft.addToBackStack("developer");
            ft.commit();
        }
        else if(id==R.id.profile)
        {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.main_layout,new ProfileFragment());
                ft.addToBackStack("home");
                ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void countTotalChildFirebase() {
       dr= FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                cart_count= (int) dataSnapshot.getChildrenCount();

                MenuItem mi=menu.findItem(R.id.cart_action);
                mi.setIcon(Converter.convertLayoutImage(HomeActivity.this,cart_count,R.drawable.toolcart));
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }

        });
    }


}
//volley is a product of google,it is the wy to trnsfer data,here we manage request response...json is a format to trnsfer or retrieve data from server
//xml is heavy nd there is no concept of making array....For using volley we add a library'com.android.volley:volley.....'

// request queeue ki help s server p data ja rha h,server p hit kreega y

//callback manager request bhjta h fb ko access k liye...bridge bnata h fb or app k beech me...bina iske...it allows the info to pass
//