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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class driver_login extends AppCompatActivity {

    Button bt_signup,bt_login;
    TextView forgot;
    EditText userName, pass;
    ProgressBar progressBar;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    private void startMap(){

        Intent driver_map=new Intent(driver_login.this,Driver_Map.class);
        startActivity(driver_map);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        userName=findViewById(R.id.userName);
        pass=findViewById(R.id.login_Password);
        progressBar=findViewById(R.id.progressBar);


        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(driver_login.this, "          Login Success with Firebase\nUser Login: Don't Judge Work in progress", Toast.LENGTH_SHORT).show();
                    startMap();
                }

            }
        };

        bt_signup=findViewById(R.id.bt_signup);
        forgot=findViewById(R.id.text_Forgot);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent driver_signup=new Intent(driver_login.this, com.hackaday.ambulance.driver_signup.class);
                startActivity(driver_signup);
            }
        });
        bt_login=findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Starting Map Activity
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                progressBar.setVisibility(View.VISIBLE);

                final String UserName=userName.getText().toString().trim();
                final String Password=pass.getText().toString();

                if(UserName.length()>0 && Password.length()>0){
                    mAuth.signInWithEmailAndPassword(UserName, Password).addOnCompleteListener(driver_login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful()){
                                Toast.makeText(driver_login.this, "Login Failed! Please Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(driver_login.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                    startMap();
                    Toast.makeText(driver_login.this, "Assuming you are Tester\n" +
                        "Driver Map opened for testing purpose", Toast.LENGTH_LONG).show();
                }

            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                Toast.makeText(driver_login.this, "Driver Password Reset: Coming Soon!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
