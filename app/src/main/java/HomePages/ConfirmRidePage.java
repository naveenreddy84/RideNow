package HomePages;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmRidePage extends AppCompatActivity {


 FirebaseAuth mAuth;
 FirebaseFirestore db;
    TextView titleyourridedetails,rideDate,rideFromLocation,rideToLocation,ridePrice,Bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_ride_page);

        titleyourridedetails = findViewById(R.id.titleyourridedetails);
        rideDate = findViewById(R.id.rideDate);
        rideFromLocation = findViewById(R.id.rideFromLocation);
        rideToLocation = findViewById(R.id.rideToLocation);
        ridePrice = findViewById(R.id.ridePrice);
        Bio = findViewById(R.id.Bio);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // getting the rideId which is passed from previous activity

        String rideId = getIntent().getStringExtra("rideId");

        if(rideId != null){
            db.collection("rides").document(rideId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()){

                            String fromLocation = documentSnapshot.getString("fromLocation");
                            String ToLocation = documentSnapshot.getString("ToLocation");
                            String price = documentSnapshot.getString("Price");
                            String bio = documentSnapshot.getString("Bio");
                            String formattedDate = documentSnapshot.getString("formatedDate");

                            // setting  the data to the textviews

                            rideFromLocation.setText(String.join(" ", "FromLocation:", fromLocation));
                            rideToLocation.setText(String.join(" ", "ToLocation:", ToLocation));
                            rideDate.setText(String.join(" ", "Date:", formattedDate));
                            ridePrice.setText(String.join(" ", "Price:", String.valueOf(price)));
                            Bio.setText(String.join(" ", "Bio:", bio));

                        }
                            }).addOnFailureListener(e -> {
                        Toast.makeText(ConfirmRidePage.this,"error retriving ride data",Toast.LENGTH_SHORT).show();
                    });


        }









    }
}