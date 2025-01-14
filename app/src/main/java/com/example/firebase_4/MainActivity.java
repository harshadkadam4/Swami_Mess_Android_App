package com.example.firebase_4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyAdapter.myAdapterEvents {

    String name_1;

    FirebaseDatabase db;
    DatabaseReference reference;
    RecyclerView recyclerView,recyclerView1;
    MyAdapter myAdapter,myAdapter1;
    ArrayList<User> list,list1;
    EditText name;
    Button add,add1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        add = findViewById(R.id.add);
        add1 = findViewById(R.id.add1);
        recyclerView = findViewById(R.id.userlist);
        recyclerView1 = findViewById(R.id.userlist1);

        //Internet Connectivity

        if(!isInternetAvailable()) {
            showInternetPopup();
        }

        // Add Name(Coming List)
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_1 = name.getText().toString();

                if(!name_1.isEmpty())
                {
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    Users users = new Users(name_1,date,"y");
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Users");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long nextId = snapshot.getChildrenCount() + 1;
                            reference.child(String.valueOf(nextId)).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    name.setText("");
                                    Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Add Name(Not Coming List)
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_1 = name.getText().toString();

                if(!name_1.isEmpty())
                {
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    Users users = new Users(name_1,date,"n");
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Users");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long nextId = snapshot.getChildrenCount() + 1;
                            reference.child(String.valueOf(nextId)).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    name.setText("");
                                    Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
            }
        });



       /* show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_1 = name.getText().toString();

                if(!name_1.isEmpty())
                {
                    readData(name_1);
                }
                else {
                    Toast.makeText(MainActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/

        reference = FirebaseDatabase.getInstance().getReference("Users");

        // Jevayla aahe
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,this,list);
        recyclerView.setAdapter(myAdapter);

        // Jevayla nahi
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        list1 = new ArrayList<>();
        myAdapter1 = new MyAdapter(this,this,list1);
        recyclerView1.setAdapter(myAdapter1);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                list1.clear();

                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String date = String.valueOf(dataSnapshot.child("date").getValue());
                    String coming = String.valueOf(dataSnapshot.child("coming").getValue());

                    if(currentDate.equals(date) )
                    {
                        User user = dataSnapshot.getValue(User.class);
                       if(coming.equals("y"))
                       {
                           list.add(user);
                       }
                       else if(coming.equals("n")){
                           list1.add(user);
                       }
                    }
                }
                myAdapter.notifyDataSetChanged();
                myAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

/*
    private void readData(String name1) {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(name_1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        Toast.makeText(MainActivity.this, "Successfully read", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String r_name = String.valueOf(dataSnapshot.child("name").getValue());
                        String r_age = String.valueOf(dataSnapshot.child("age").getValue());

                        op_name.setText(r_name);
                        op_age.setText(r_age);

                    }
                    else{
                        Toast.makeText(MainActivity.this, "User doesn't exists", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Failed to Read", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
*/
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
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onNameClick(User user) {

        AlertDialog.Builder deleteAlert = new AlertDialog.Builder(this);
        deleteAlert.setTitle("Delete - "+user.getName());
        deleteAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reference = FirebaseDatabase.getInstance().getReference("Users");

                reference.orderByChild("name").equalTo(user.getName())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                    String date = dataSnapshot.child("date").getValue(String.class);
                                    if(currentDate.equals(date))
                                    {
                                        dataSnapshot.getRef().child("coming").setValue("d");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, error+"", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        deleteAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        deleteAlert.show();
    }
}