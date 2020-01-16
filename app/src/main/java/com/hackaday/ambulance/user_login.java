package com.hackaday.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class user_login extends AppCompatActivity {

    Button bt_signup,bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        bt_signup=findViewById(R.id.bt_signup);
        bt_login=findViewById(R.id.bt_login);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent user_signup=new Intent(user_login.this, com.hackaday.ambulance.user_signup.class);
                startActivity(user_signup);
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(user_login.this, "User Login: Coming Soon!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
