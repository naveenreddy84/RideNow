package Drivers;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ridenow.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DriversRidesHistory extends AppCompatActivity {



    TextView YourRidesHistoryTitle;

    ListView RidesHistoryListview;


    FirebaseFirestore mAuth;

    FirebaseFirestore db;

    FirebaseUser CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drivers_rides_history);

        YourRidesHistoryTitle = findViewById(R.id.YourRidesHistoryTitle);
        RidesHistoryListview = findViewById(R.id.RidesHistoryListview);

        mAuth = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();


        fetchDriverRidesHistory();

    }


        private void fetchDriverRidesHistory() {


            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                String userId = currentUser.getUid();


                db.collection("rides")
                        .whereEqualTo("driverId", userId)  // Filter rides by the driver's ID
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<String> ridesList = new ArrayList<>();


                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String fromLocation = document.getString("fromLocation");
                                    String toLocation = document.getString("toLocation");
                                    String price = document.getString("price");
                                    String time = document.getString("time");

                                    // Retrieve timestampDate as Timestamp
                                    Timestamp timestampDate = document.getTimestamp("Timestamp");

                                    // Formating  the timestamp to a  string
                                    String formattedDate = "Date not available";

                                    if (timestampDate != null) {
                                        Date date = timestampDate.toDate();
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                                        formattedDate = sdf.format(date);
                                    }


                                    String rideSummary = "From: " + fromLocation + "\nTo: " + toLocation + "\nPrice: " + price + "\nTime: " + time + "\nDate: " + formattedDate;
                                    ridesList.add(rideSummary);
                                }

                                // Use an ArrayAdapter to populate the ListView with the ride data
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ridesList);
                                RidesHistoryListview.setAdapter(adapter);

                            } else {
                                Toast.makeText(DriversRidesHistory.this, "Error getting rides history: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(DriversRidesHistory.this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            }
        }
    }