package com.example.ridenow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DriverRegisterPage extends AppCompatActivity {


    Button uploadButton,registerBtn;
    TextView updl;

    EditText registerEmail,registerPassword,confirmPassword;
    ImageView licenseImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_register_page);



        uploadButton = findViewById(R.id.uploadButton);
        registerBtn = findViewById(R.id.registerBtn);
        updl = findViewById(R.id.updl);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerBtn = findViewById(R.id.registerBtn);



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(DriverRegisterPage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}