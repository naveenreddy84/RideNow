package HomePages;

import android.content.Intent;
import android.os.Bundle;
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


        welcomemsg = findViewById(R.id.welcomemsg);
        create_a_rideBtn = findViewById(R.id.create_a_rideBtn);

       mAuth = FirebaseAuth.getInstance();

       db = FirebaseFirestore.getInstance();

       user = mAuth.getCurrentUser();


       if(user != null){

           db.collection("Drivers").document(user.getUid()).get()
                   .addOnSuccessListener(documentSnapshot -> {
                       if(documentSnapshot.exists()){

                           String uname = documentSnapshot.getString("driverusername");
                           System.out.println("user name is " + uname);

                           welcomemsg.setText( "welcome " + uname);

                       }else{
                           welcomemsg.setText("welcome ,driver");
                       }
                   }).addOnFailureListener(e -> System.out.println("Error: " + e.getMessage()));

       }else{
           System.out.println("No user is signed in.");
       }





        create_a_rideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverHomePage.this,CreatingRidePage.class);
                startActivity(intent);
                finish();
            }
        });

        }

}