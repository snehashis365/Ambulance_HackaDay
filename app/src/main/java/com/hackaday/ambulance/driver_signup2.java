package com.hackaday.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class driver_signup2 extends AppCompatActivity {

    //Spinner ambu_type;
    Button driver_signup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup2);
        driver_signup2=findViewById(R.id.bt_signup2);
        driver_signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(driver_signup2.this, "Driver Signup: Coming Soon!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
