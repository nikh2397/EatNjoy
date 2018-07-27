package com.example.nikhil.eatnjoy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText edtEmail;
    Button btnResetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);


        edtEmail = findViewById(R.id.edt_reset_email);
        btnResetPassword = findViewById(R.id.resetbtn);

        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String email = edtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext() , "Enter your email!", Toast.LENGTH_LONG).show();
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Toast.makeText(getApplicationContext(), "rshjryhwe", Toast.LENGTH_SHORT).show();
    }
}
