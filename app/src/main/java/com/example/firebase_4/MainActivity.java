package com.example.firebase_4;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String name_1,age_1;

    FirebaseDatabase db;
    DatabaseReference reference;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<User> list;
    EditText name,age;
    Button button,show;
    TextView op_name,op_age;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        button = findViewById(R.id.button);
        show = findViewById(R.id.show);
        op_age = findViewById(R.id.op_age);
        op_name = findViewById(R.id.op_name);
        recyclerView = findViewById(R.id.userlist);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_1 = name.getText().toString();
                age_1 = age.getText().toString();


                if(!name_1.isEmpty() && !age_1.isEmpty())
                {

                    Users users = new Users(name_1,age_1);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Users");
                    reference.child(name_1).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            age.setText("");
                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

               //
            }
        });


        show.setOnClickListener(new View.OnClickListener() {
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


        reference = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

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
}