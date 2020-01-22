package com.hackaday.ambulance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class driver_signup extends AppCompatActivity {

    Button bt_submit;
    EditText name, email, phone, pass, rePass;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);

        name= findViewById(R.id.signup_Name);
        email=findViewById(R.id.signup_email);
        phone=findViewById(R.id.signup_phone);
        pass=findViewById(R.id.signup_Password);
        rePass=findViewById(R.id.signup_rePassword);
        progressBar=findViewById(R.id.progressBar);

        bt_submit=findViewById(R.id.bt_user_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                progressBar.setVisibility(View.VISIBLE);


                final String Name = name.getText().toString();
                final String Email = email.getText().toString();
                final String Phone = phone.getText().toString();
                final String Password = pass.getText().toString();
                final String RePassword = rePass.getText().toString();
                if (Name.length() >= 3) {

                    if (Email.length() > 0 && Phone.length() >= 10) {

                        if (Password.length() > 0 && Password.equals(RePassword)) {

                            Query emailQuery = FirebaseDatabase.getInstance().getReference().child("Members").child("Drivers").orderByChild("Email").equalTo(Email);
                            emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                    if (dataSnapshot.getChildrenCount() > 0) {
                                        Toast.makeText(driver_signup.this, "Email already exists!!", Toast.LENGTH_LONG).show();
                                    } else {

                                        Intent signup2 = new Intent(driver_signup.this, driver_signup2.class);
                                        signup2.putExtra("email", Email);
                                        signup2.putExtra("pass", Password);
                                        signup2.putExtra("name", Name);
                                        signup2.putExtra("phone", Phone);
                                        startActivity(signup2);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else if (Password.length() < 8) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(driver_signup.this, "Password Must be atleast 8 characters long", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(driver_signup.this, "Password don't match please Re-enter", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(driver_signup.this, "Please enter valid Email and Phone nmuber", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(driver_signup.this, "Enter Valid Name 1st", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
