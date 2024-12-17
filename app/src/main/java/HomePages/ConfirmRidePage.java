package HomePages;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmRidePage extends AppCompatActivity {


 FirebaseAuth mAuth;
 FirebaseFirestore db;
    TextView titleyourridedetails,rideDate,rideFromLocation,rideToLocation,ridePrice;

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
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();









    }
}