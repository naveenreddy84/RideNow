package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;



public class DriverHomePage extends AppCompatActivity {

    TextView welcomemsg;


    Button create_a_rideBtn;


    FirebaseAuth mAuth;

    FirebaseFirestore db;

    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_home_page);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        user = mAuth.getCurrentUser();

        welcomemsg = findViewById(R.id.welcomemsg);
        create_a_rideBtn = findViewById(R.id.create_a_rideBtn);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        welcomemsg = findViewById(R.id.welcomemsg);
        create_a_rideBtn = findViewById(R.id.create_a_rideBtn);

        if (user != null) {

            String driverId = user.getUid();
            db.collection("Drivers").document(driverId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            String username = documentSnapshot.getString("driverusername");
                            if (username != null && !username.isEmpty()) {

                                welcomemsg.setText("Welcome, " + username + "!");
                            } else {
                                welcomemsg.setText("Welcome!");
                            }
                        } else {
                            welcomemsg.setText("Welcome!");
                        }
                    })
                    .addOnFailureListener(e -> {

                        welcomemsg.setText("Welcome!");
                        Log.e("FirestoreError", "Error fetching driver data", e);
                    });
        } else {
            welcomemsg.setText("Welcome!");
        }





        create_a_rideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverHomePage.this,CreatingRidePage.class);
                startActivity(intent);

            }
        });













        }

}