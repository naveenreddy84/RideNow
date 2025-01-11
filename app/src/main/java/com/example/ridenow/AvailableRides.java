package com.example.ridenow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import HomePages.Ride;
import HomePages.RideAdapter;

public class AvailableRides extends AppCompatActivity {

    private ListView RidesListView;
    private ArrayList<Ride> availableRides;
    private RideAdapter rideAdapter;
    TextView availableRidesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_rides);

        availableRidesTitle = findViewById(R.id.availableRidesTitle);
        RidesListView = findViewById(R.id.RidesListview);

        // Initialize the rides list and adapter
        availableRides = new ArrayList<>();
        rideAdapter = new RideAdapter(this, availableRides);
        RidesListView.setAdapter(rideAdapter);

        // Fetch and display the rides
        fetchRides();
    }

    private void fetchRides() {
        Intent intent = getIntent();
        ArrayList<Ride> receivedRides = (ArrayList<Ride>) intent.getSerializableExtra("availableRides");

        if (receivedRides == null || receivedRides.isEmpty()) {
            Toast.makeText(this, "No available rides found.", Toast.LENGTH_SHORT).show();
        } else {
            // Clear existing list and add new rides
            availableRides.clear();
            availableRides.addAll(receivedRides);

            // Notify adapter that data has changed
            rideAdapter.notifyDataSetChanged();
        }
    }
}
