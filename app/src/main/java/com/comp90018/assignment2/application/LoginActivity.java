package com.comp90018.assignment2.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.utils.DaoUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        DaoUser daoUser = new DaoUser();

        TextInputEditText tv_user = findViewById(R.id.login_username);
        TextInputEditText tv_password = findViewById(R.id.login_password);



        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.loginBtn);
        MaterialButton registerBtn = (MaterialButton) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        // admin and admin
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = tv_user.getText().toString();
                String password = tv_password.getText().toString();

                if (user.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // if e-mail exists in the database
                            if (snapshot.hasChild(user)){
                                String getPass = snapshot.child(user).child("password").getValue(String.class);

                                if (getPass.equals(password)){
                                    Toast.makeText(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = getSharedPreferences("assignment2",MODE_PRIVATE).edit();
                                    editor.putString("currentUser",user);
                                    editor.apply();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }
                                else {
                                    Toast.makeText(LoginActivity.this,"Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"User not exist",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }
}
