package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;

import java.util.ArrayList;
import java.util.List;

public class availablerides extends AppCompatActivity {

    private ListView RidesListView;
    private List<Ride> availableRides;
    private RideAdapter rideAdapter;
    TextView availableRidesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablerides);

        availableRidesTitle = findViewById(R.id.availableRidesTitle);
        RidesListView = findViewById(R.id.RidesListview);

        // Initialize the rides list
        availableRides = new ArrayList<>();
        rideAdapter = new RideAdapter(this, availableRides);

        // Set the adapter for the ListView
        RidesListView.setAdapter(rideAdapter);

        // Fetch and display the rides
        fetchRides();
    }

    public void fetchRides() {
        // Retrieve the available rides from the Intent
        Intent intent = getIntent();
        ArrayList<Ride>  availableRides = (ArrayList<Ride>) intent.getSerializableExtra("availableRides");

        // Check if availableRides is null or empty
        if (availableRides == null || availableRides.isEmpty()) {
            Toast.makeText(this, "No available rides found.", Toast.LENGTH_SHORT).show();
        } else {
            // Notify the adapter that the data has changed
            Log.d("AvailableRides", "Fetched Rides: " + availableRides.size());
            rideAdapter.notifyDataSetChanged();
        }
    }
}









