package com.example.firebase_4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername,loginPassword;
    Button loginButton;
    TextView signupRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Internet Availability
        if(!isInternetAvailable()) {
            showInternetPopup();
        }


        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_passw);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateUsername() && validatePassword())
                {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public Boolean validateUsername()
    {
        String val = loginUsername.getText().toString();

        if(val.isEmpty())
        {
            loginUsername.setError("Enter Username");
            return false;
        }
        else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword()
    {
        String val = loginPassword.getText().toString();

        if(val.isEmpty())
        {
            loginPassword.setError("Enter Password");
            return false;
        }
        else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser()
    {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users_registered");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if(passwordFromDB.equals(userPassword))
                    {
                        loginUsername.setError(null);

                        // Save login state in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("swamiMessAppPrefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", userUsername);
                        editor.apply();

                        // Redirect to MainActivity
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        loginPassword.setError("Invalid Password");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("Username does not exist");
                    loginUsername.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isInternetAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void showInternetPopup()
    {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("OK", (dialog, which) -> {
                    if(!isInternetAvailable())
                        showInternetPopup();
                })
                .setCancelable(false)
                .show();
    }
}














