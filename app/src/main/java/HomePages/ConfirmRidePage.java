package HomePages;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmRidePage extends AppCompatActivity {

    private Timestamp formatedDateTimestamp;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView titleyourridedetails, rideDate, rideFromLocation, rideToLocation, ridePrice, time;

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
        time = findViewById(R.id.time);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // getting the rideId which is passed from previous activity

        String rideId = getIntent().getStringExtra("rideId");

        if (rideId != null) {
            db.collection("rides").document(rideId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fromLocation = documentSnapshot.getString("fromLocation");
                            String toLocation = documentSnapshot.getString("toLocation");
                            String price = documentSnapshot.getString("price");
                            String Time = documentSnapshot.getString("time");

                            // Retrieve timestampDate as Timestamp
                            Timestamp timestampDate = documentSnapshot.getTimestamp("timestampDate");

                            // Format the timestamp to a readable date string
                            if (timestampDate != null) {
                                Date date = timestampDate.toDate();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String formattedDate = sdf.format(date);

                                // setting the data to the textviews
                                rideFromLocation.setText(String.join(" ", "FromLocation:", fromLocation));
                                rideToLocation.setText(String.join(" ", "ToLocation:", toLocation));
                                rideDate.setText(String.join(" ", "Date:", formattedDate)); // Set the formatted date
                                ridePrice.setText(String.join(" ", "Price:", String.valueOf(price)));
                                time.setText(String.join(" ", "Time:", Time));
                            } else {
                                rideDate.setText("Date not available");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ConfirmRidePage.this, "Error retrieving ride data", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
//time.setText(String.join(" ", "Time:", time));