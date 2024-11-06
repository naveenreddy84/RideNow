package com.example.ridenow;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText registerEmail, registerPassword, confirmPassword;
    Button registerBtn;

    TextView registerLink;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        registerLink = findViewById(R.id.registerLink);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
    private void registerUser(){
        String email = registerEmail.getText().toString().trim();
        String cPassword = confirmPassword.getText().toString().trim();
        String pswd = registerPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pswd) || TextUtils.isEmpty(cPassword)){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmail.setError("Invalid Email ");
            registerEmail.requestFocus();
            return;
        }

        if(pswd.length() < 8){
            registerPassword.setError("Password must be atleast 8 characters");
            registerPassword.requestFocus();
            return;
        }

        if(!pswd.equals(cPassword)){
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(this, task ->{

            if(task.isSuccessful()){
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();

            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
