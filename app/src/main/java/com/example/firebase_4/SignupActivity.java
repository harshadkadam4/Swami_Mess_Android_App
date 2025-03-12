package com.example.firebase_4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupPhone, signupDepartment, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupPhone = findViewById(R.id.signup_phone);
        signupDepartment = findViewById(R.id.signup_dept);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_passw);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users_registered");

                String name = signupName.getText().toString();
                String phone = signupPhone.getText().toString();
                String department = signupDepartment.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                if(validateName(name) && validatePhone(phone) && validateDept(department) && validatePassword(password))
                {
                    if(username.isEmpty())
                    {
                        signupUsername.setError("Enter Username");
                    }
                    else {
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users_registered");
                        Query checkUsername = reference1.orderByChild("username").equalTo(username);

                        checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    signupUsername.setError("Username Already Exists!");
                                }
                                else {
                                    HelperClass helperClass = new HelperClass(name, phone, department, username, password);
                                    reference.child(username).setValue(helperClass);

                                    Toast.makeText(SignupActivity.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public Boolean validateName(String name)
    {
        if(name.isEmpty())
        {
            signupName.setError("Enter Name");
            return false;
        }
        else {
            signupName.setError(null);
            return true;
        }
    }

    public Boolean validatePhone(String phone)
    {
        if(phone.isEmpty() || phone.length() < 10)
        {
            signupPhone.setError("Enter Valid Number");
            return false;
        }
        else {
            signupPhone.setError(null);
            return true;
        }
    }

    public Boolean validateDept(String dept)
    {
        if(dept.isEmpty())
        {
            signupDepartment.setError("Enter Department Name");
            return false;
        }
        else {
            signupDepartment.setError(null);
            return true;
        }
    }

    /*
    public Boolean validateUsername(String username)
    {
        if(username.isEmpty())
        {
            signupUsername.setError("Enter Username");
            return false;
        }
        else {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users_registered");
            Query checkUsername = reference1.orderByChild("username").equalTo(username);

            checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        signupUsername.setError("Username Already Exists!");
                    }
                    else {
                        return;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

     */

    public Boolean validatePassword(String pass)
    {
        if(pass.isEmpty())
        {
            signupPassword.setError("Enter Password");
            return false;
        }
        else {
            signupPassword.setError(null);
            return true;
        }
    }
}