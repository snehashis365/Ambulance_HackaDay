package com.hackaday.ambulance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_login extends AppCompatActivity {

    Button bt_signup,bt_login;
    EditText userName, pass;
    TextView forgot;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private void startMap(){
        //Method to switch activity

        Intent user_map=new Intent(user_login.this, User_Map.class);
        startActivity(user_map);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        userName=findViewById(R.id.userName);
        pass=findViewById(R.id.login_Password);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Toast.makeText(user_login.this, "          Login Success with Firebase\nUser Login: Don't Judge Work in progress", Toast.LENGTH_SHORT).show();
                    startMap();
                    /*
                    Intent user_map=new Intent(user_login.this, User_Map.class);
                    startActivity(user_map);
                    */
                }

            }
        };

        bt_signup=findViewById(R.id.bt_signup);
        bt_login=findViewById(R.id.bt_login);
        forgot=findViewById(R.id.text_Forgot);
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
                /*Toast.makeText(user_login.this, "User Login: Don't Judge Work in progress", Toast.LENGTH_SHORT).show();
                Intent user_map=new Intent(user_login.this, User_Map.class);
                startActivity(user_map);*/

                final String UserName=userName.getText().toString().trim();
                final String Password=pass.getText().toString();

                if(UserName.length()>0 && Password.length()>0){
                    mAuth.signInWithEmailAndPassword(UserName, Password).addOnCompleteListener(user_login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(user_login.this, "Login Failed! Please Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(user_login.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                    startMap();
                    Toast.makeText(user_login.this, "Assuming tester Map opened for test", Toast.LENGTH_SHORT).show();
                }

            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(user_login.this, "User Reset Password: Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Starting Listener
        mAuth.addAuthStateListener(firebaseAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stopping Listener
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
