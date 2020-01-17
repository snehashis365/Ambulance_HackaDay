package com.hackaday.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class driver_signup extends AppCompatActivity {

    Button bt_submit;
    EditText name, email, phone, Pass, rePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);

        email=findViewById(R.id.signup_email);
        phone=findViewById(R.id.signup_phone);
        Pass=findViewById(R.id.signup_Password);
        rePass=findViewById(R.id.signup_rePassword);

        bt_submit=findViewById(R.id.bt_user_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup2=new Intent(driver_signup.this,driver_signup2.class);
                startActivity(signup2);
            }
        });
    }
}
