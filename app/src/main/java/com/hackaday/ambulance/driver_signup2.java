package com.hackaday.ambulance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class driver_signup2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner ambu_type;
    Button driver_signup2;
    EditText license_num,vehicle_num,vehicle_owner;
    ProgressBar progressBar;


    String spinner_textAmbuType;
    int spinner_index=0;

    //Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private void startMap(){
        //Method to switch activity

        Intent driver_map=new Intent(driver_signup2.this, Driver_Map.class);
        startActivity(driver_map);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup2);


        final String Name=getIntent().getStringExtra("name");
        final String Email=getIntent().getStringExtra("email");
        final String Phone=getIntent().getStringExtra("phone");
        final String Password=getIntent().getStringExtra("pass");

        license_num=findViewById(R.id.license_num);
        vehicle_num=findViewById(R.id.vehicle_num);
        vehicle_owner=findViewById(R.id.vehicle_owner);
        progressBar=findViewById(R.id.progressBar);


        //Spinner setup
        ambu_type= findViewById(R.id.ambu_Type);
        ArrayAdapter<CharSequence> ambu_adapter=ArrayAdapter.createFromResource(this, R.array.ambu_Type_spinner,android.R.layout.simple_spinner_item);
        ambu_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ambu_type.setAdapter(ambu_adapter);
        ambu_type.setOnItemSelectedListener(this);

        //FireBase Authenticator state listener setup and activity swtitch
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Toast.makeText(driver_signup2.this, "Login after signup Success with Firebase\nDriver Signup: Dont't Judge Work in progress", Toast.LENGTH_SHORT).show();
                    startMap();
                }

            }
        };




        driver_signup2=findViewById(R.id.bt_signup2);
        driver_signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                progressBar.setVisibility(View.VISIBLE);
                if (spinner_index == 0) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(driver_signup2.this, "Select Ambulance category first", Toast.LENGTH_SHORT).show();
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

                } else {

                    final String License=license_num.getText().toString();
                    final String Vehicle=vehicle_num.getText().toString();
                    final String Vehicle_Owner=vehicle_owner.getText().toString();

                    //Ambulance type string already updated from onSelected method
                    mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(driver_signup2.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                String user_id=mAuth.getCurrentUser().getUid();
                                DatabaseReference current_driver_ref = FirebaseDatabase.getInstance().getReference().child("Members").child("Drivers").child(user_id);

                                //Mapping new details
                                Map newDriver = new HashMap();
                                newDriver.put("Name",Name);
                                newDriver.put("Phone",Phone);
                                newDriver.put("Email",Email);
                                newDriver.put("License_Number",License);
                                newDriver.put("Vehicle_Number",Vehicle);
                                newDriver.put("Vehicle_Owner",Vehicle_Owner);
                                newDriver.put("Type",spinner_textAmbuType);

                                //Uploading Details
                                current_driver_ref.setValue(newDriver);

                            }
                            else {
                                Toast.makeText(driver_signup2.this, "Driver SignUp: Failed!! Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        spinner_index=i;
        spinner_textAmbuType = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, spinner_textAmbuType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
