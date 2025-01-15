package Drivers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DriverHomePage extends AppCompatActivity {

    private TextView welcomemsg;
    private Button create_a_rideBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_page);

        // Initialize Firebase and UI elements
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        welcomemsg = findViewById(R.id.welcomemsg);
        create_a_rideBtn = findViewById(R.id.create_a_rideBtn);

        // Set default welcome message
        welcomemsg.setText("Welcome!");

        // Fetch user data from Firestore
        if (user != null) {
            String driverId = user.getUid();
            db.collection("Drivers").document(driverId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("driverusername");
                            if (username != null && !username.isEmpty()) {
                                welcomemsg.setText("Welcome, " + username + "!");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error fetching driver data", e);
                    });
        }

        // Set up button click listener
        create_a_rideBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DriverHomePage.this, CreatingRidePage.class);
            startActivity(intent);
        });
    }
}
