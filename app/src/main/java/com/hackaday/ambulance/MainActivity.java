package com.hackaday.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton bt_user;
    ImageButton bt_driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_user=(ImageButton)findViewById(R.id.bt_user);
        bt_driver=(ImageButton)findViewById(R.id.bt_driver) ;
        bt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent user=new Intent(MainActivity.this, user_login.class);
                startActivity(user);
            }
        });
        bt_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent driver=new Intent(MainActivity.this, driver_login.class);
                startActivity(driver);
            }
        });

    }
}
