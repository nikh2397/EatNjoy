package com.example.nikhil.eatnjoy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signin extends AppCompatActivity {

    FirebaseDatabase fdb;
    DatabaseReference dbr;
    String e_mail, password,TAG;
    EditText edtmail, edtpassword,loginusername;
    Button login;
    AlertDialog.Builder ab;
    ProgressDialog progressDialog;
    CheckBox mCbShowPwd;
    FirebaseAuth firebaseAuth;
    TextView forgot;
    private TextView signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtmail = findViewById(R.id.email);
        edtpassword = findViewById(R.id.password);
        signup=findViewById(R.id.textViewSignUp);
        login = findViewById(R.id.buttonSignin);
        forgot=findViewById(R.id.btn_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        //  fdb = FirebaseDatabase.getInstance();
  //      FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }


signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Signin.this,ProfileActivity.class);
                startActivity(i);
            }
        });

forgot.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

       Intent resetactivity=new Intent(getApplicationContext(),Reset.class);
       startActivity(resetactivity);
    }
});



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e_mail = edtmail.getText().toString().trim();
                password = edtpassword.getText().toString().trim();

                if (edtmail.getText().toString().trim()
                        .length() < 3) {

                    edtmail.setError("Please enter your Full Name");
                } else {
                    edtmail.setError(null);
                }
                if (edtpassword.getText().toString().trim()
                        .length() < 5) {

                    edtpassword.setError("Password size must be greater than 5");
                } else {
                    edtpassword.setError(null);
                }
                //checking if email and passwords are empty
                if (TextUtils.isEmpty(e_mail)) {
                    // Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
                    edtmail.setError("Please enter valid E-Mail");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    //Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
                    edtpassword.setError("Please enter valid Password");
                    return;
                }


                firebaseAuth.signInWithEmailAndPassword(e_mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

//                        progressDialog.dismiss();


                        if(task.isSuccessful()) {
                            //start the profile activity

                            Log.d(TAG,"signinWithEmail:success");
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            updateUI();
                           // Toast.makeText(Signin.this, "chhmgvkhb", Toast.LENGTH_SHORT).show();

                            updateUI();

                        }
                        else {

                            Log.w(TAG,"signInWithEmail:Failed");

                            Toast.makeText(Signin.this, "Authentication Failed....", Toast.LENGTH_LONG).show();
                            updateUI();
                        }

                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                        if(user!=null)
                        {

                            String name=user.getDisplayName();
                            String email=user.getEmail();
                            Uri photoUrl=user.getPhotoUrl();

                            boolean emailVerified=user.isEmailVerified();
                            String uid=user.getUid();
                        }
                    }

                    private void updateUI() {
                        Intent i = new Intent(Signin.this, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(Signin.this, "You are Successfully Login", Toast.LENGTH_SHORT).show();
                    }
                });



            }

        });

        mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwdlogin);
        mCbShowPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // checkbox status is changed from uncheck to checked.
                        if (!isChecked) {
                            // show password
                            edtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            // hide password
                            edtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                });

            }
        });



    }


//    @Override
//    public void onBackPressed() {
//
//        ab = new AlertDialog.Builder(this);
//        ab.setMessage("Are you sure you want to go back");
//        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                progressDialog.onBackPressed();
//            }
//        });
//
//        ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        ab.show();
//
//    }
}





