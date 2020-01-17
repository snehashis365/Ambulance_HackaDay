package com.hackaday.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class user_signup extends AppCompatActivity {

    EditText name, email, phone, Pass, rePass;
    Button user_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        email=findViewById(R.id.signup_email);
        phone=findViewById(R.id.signup_phone);
        Pass=findViewById(R.id.signup_Password);
        rePass=findViewById(R.id.signup_rePassword);
        user_signup=findViewById(R.id.bt_user_submit);
        user_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(user_signup.this, "User Signup: Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
