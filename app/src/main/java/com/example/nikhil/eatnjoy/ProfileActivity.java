package com.example.nikhil.eatnjoy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtemail, edtpassword, username;
    private TextView forlogin;
    Button signup, loginButton;
    SignInButton googlebutton;
    String TAG, profilename, email, password;
    ImageView imageView, im1;

    FirebaseAuth firebaseAuth;
    GoogleSignInClient mgoogleapiclient;
    CheckBox checkBox;
    ProgressDialog progressDialog;
    CallbackManager callbackManager;
    CheckBox mCbShowPwd1;
    GoogleSignInOptions gso;

    int RC_SIGN_IN = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }

    private void signin() {
        Intent signInIntent = mgoogleapiclient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void fblogin() {
        LoginManager.getInstance().logInWithReadPermissions(ProfileActivity.this, Arrays.asList("email", "public_profile"));  //login manager yha fb ki library h
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());    //this method use to add firebase concept..

            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "facebook:onError");
                // ...
            }

        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        //set id to the views

        edtemail = (EditText) findViewById(R.id.editTextEmail);
        edtpassword = (EditText) findViewById(R.id.editTextPassword);
        forlogin = findViewById(R.id.textViewSignin);
        signup = (Button) findViewById(R.id.buttonSignup);
        loginButton = (Button) findViewById(R.id.login_button);
        mCbShowPwd1 = (CheckBox) findViewById(R.id.cbShowPwd);
        googlebutton = (SignInButton) findViewById(R.id.google);

        googlebutton.setOnClickListener(this);
        signup.setOnClickListener(this);
        forlogin.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
        //google authentication code

        //    Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgoogleapiclient = GoogleSignIn.getClient(this, gso);

        mCbShowPwd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCbShowPwd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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


//
//
//    private void displayMessage(Profile profile) {
//
//        if (profile != null) {
//            profilename = profile.getName();
//
//            String url = profile.getProfilePictureUri(150, 150).toString();
//
//            Glide.with(this).load(url).asBitmap()
//                    .into(new BitmapImageViewTarget(imageView) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//
//                            RoundedBitmapDrawable cd = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
//                            cd.setCircular(true);
//                            imageView.setImageDrawable(cd);
//
//                        }
//                    });
//        }
//
//
//    }



    private void registerUser() {

        email = edtemail.getText().toString().trim();
        password = edtpassword.getText().toString().trim();


        if (edtemail.getText().toString().trim()
                .length() < 3) {

            edtemail.setError("Please enter your Full Name");
        } else {
            edtemail.setError(null);
        }
        if (edtpassword.getText().toString().trim()
                .length() < 5) {

            edtpassword.setError("Password size must be greater than 5");
        } else {
            edtpassword.setError(null);
        }
        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            // Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
            edtemail.setError("Please enter valid E-Mail");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
            edtpassword.setError("Please enter valid Password");
            return;
        }

        //if the email and password are not empty
        progressDialog.setMessage("User register please wait.......");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                (this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            updateUI();


                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ProfileActivity.this, "Authentication user", Toast.LENGTH_SHORT).show();


                        }

                    }
                });
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();


                if (task.isSuccessful()) {
                    //start the profile activity

                    Log.d(TAG, "signinWithEmail:success");
                    updateUI();
                } else {

                    Log.w(TAG, "signInWithEmail:Failed");

                    Toast.makeText(ProfileActivity.this, "Authentication Failed....", Toast.LENGTH_LONG).show();

                }

            }


        });
    }







    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSignup:
                registerUser();
                break;
            case R.id.textViewSignin:
                Intent i=new Intent(this,Signin.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(i);

                break;
//            case R.id.cbShowPwd:
//               // showpassword();
//                break;
            case R.id.google:
                signin();
                break;
            case R.id.login_button:

                    fblogin();
                    break;

        }

    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }  */

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(ProfileActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void updateUI() {

        Toast.makeText(this, "Congrats You are Successfully logged in....", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("a", profilename);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}



















