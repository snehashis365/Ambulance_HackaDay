package com.hackaday.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class driver_login extends AppCompatActivity {

    Button bt_signup,bt_login;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        bt_signup=findViewById(R.id.bt_signup);
        bt_login=findViewById(R.id.bt_login);
        forgot=findViewById(R.id.text_Forgot);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent driver_signup=new Intent(driver_login.this, com.hackaday.ambulance.driver_signup.class);
                startActivity(driver_signup);
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(driver_login.this, "Driver Login: Don't Judge Work in progress", Toast.LENGTH_SHORT).show();
                Intent driver_map=new Intent(driver_login.this,Driver_Map.class);
                startActivity(driver_map);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(driver_login.this, "Driver Password Reset: Coming Soon!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
