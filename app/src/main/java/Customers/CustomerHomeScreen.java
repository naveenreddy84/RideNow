package Customers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Rides.Ride;

public class CustomerHomeScreen extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "CustomerHomeScreen";

    private Spinner snipperfromlocations, snipperTolocations;
    private Button searchBtn;
    private DatePicker date;
    private GoogleMap map;

    private String[] locationsArray;
    private List<String> filteredLocations;

    private Calendar calendar;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_screen);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeUI();
        setupFromLocationAdapter();

        // Initialize the calendar
        calendar = Calendar.getInstance();

        setDatePickerToCurrentDate();

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        performSearch();
    }

    // Initialize UI elements
    private void initializeUI() {
        snipperfromlocations = findViewById(R.id.snipperfromlocations);
        snipperTolocations = findViewById(R.id.snipperTolocations);
        searchBtn = findViewById(R.id.searchBtn);
        locationsArray = getResources().getStringArray(R.array.locations_array);
        filteredLocations = Arrays.asList(locationsArray);
        date = findViewById(R.id.date); // Initialize the DatePicker view
    }

    private void setDatePickerToCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        long currentDateInMillis = calendar.getTimeInMillis();
        date.setMinDate(currentDateInMillis);  // Disable past dates
    }

    private void performSearch() {
        searchBtn.setOnClickListener(v -> {
            // Get the selected location from spinners, with null checks
            String fromLocation = snipperfromlocations.getSelectedItem() != null ? snipperfromlocations.getSelectedItem().toString().trim() : "";
            String toLocation = snipperTolocations.getSelectedItem() != null ? snipperTolocations.getSelectedItem().toString().trim() : "";

            // Ensure the calendar is initialized
            if (calendar == null) {
                calendar = Calendar.getInstance(); // Initialize calendar if not done yet
            }

            // Get the selected date
            long selectedDateMillis = calendar.getTimeInMillis();
            Date selectedDate = new Date(selectedDateMillis);

            // Check if the locations are valid
            if (fromLocation.isEmpty() || toLocation.isEmpty()) {
                Toast.makeText(CustomerHomeScreen.this, "Please select valid locations.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Query Firestore for rides based on selected locations and timestamp
            db.collection("rides")
                    .whereEqualTo("fromLocation", fromLocation)
                    .whereEqualTo("toLocation", toLocation)
                    .whereGreaterThanOrEqualTo("Timestamp", new Timestamp(selectedDate))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            ArrayList<Ride> availableRides = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                long timestampMillis = 0;
                                if (document.getTimestamp("Timestamp") != null) {
                                    timestampMillis = document.getTimestamp("Timestamp").toDate().getTime();
                                }

                                Ride ride = new Ride(
                                        document.getId(),
                                        document.getString("fromLocation"),
                                        document.getString("toLocation"),
                                        document.getString("time"),
                                        document.getString("price"),
                                        timestampMillis
                                );
                                availableRides.add(ride);
                            }

                            if (availableRides.isEmpty()) {
                                Toast.makeText(CustomerHomeScreen.this, "No rides available.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(CustomerHomeScreen.this, AvailableRides.class);
                                intent.putExtra("availableRides", availableRides);
                                startActivity(intent);
                            }
                        } else {
                            // Handle Firestore query failure
                            Toast.makeText(CustomerHomeScreen.this, "Error fetching rides.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(CustomerHomeScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    // Adapter for "from" locations
    private void setupFromLocationAdapter() {
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationsArray);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snipperfromlocations.setAdapter(fromAdapter);

        snipperfromlocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupToLocationAdapter(position);
                updateMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CustomerHomeScreen.this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Adapter for "to" locations
    private void setupToLocationAdapter(int selectedFromPosition) {
        String selectedFromLocation = locationsArray[selectedFromPosition];
        filteredLocations = Arrays.asList(locationsArray);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            filteredLocations = filteredLocations.stream()
                    .filter(location -> !location.equals(selectedFromLocation))
                    .toList();
        }

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filteredLocations);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snipperTolocations.setAdapter(toAdapter);

        snipperTolocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CustomerHomeScreen.this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the map
    private void updateMap() {
        if (map == null) {
            Log.e(TAG, "Google Map is not ready yet.");
            return;
        }

        String fromLocation = snipperfromlocations.getSelectedItem() != null ? snipperfromlocations.getSelectedItem().toString() : "";
        String toLocation = snipperTolocations.getSelectedItem() != null ? snipperTolocations.getSelectedItem().toString() : "";

        if (fromLocation.isEmpty() || toLocation.isEmpty()) {
            return;
        }

        // Get coordinates for both locations
        LatLng startLocation = getCoordinates(fromLocation);
        LatLng endLocation = getCoordinates(toLocation);

        if (startLocation != null && endLocation != null) {
            // Clear previous markers and polylines
            map.clear();

            // Add markers for both locations
            map.addMarker(new MarkerOptions().position(startLocation).title("Start: " + fromLocation));
            map.addMarker(new MarkerOptions().position(endLocation).title("Destination: " + toLocation));

            // Move the camera to show both locations
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.include(startLocation);
            boundsBuilder.include(endLocation);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));

            // Draw a route between the two locations
            map.addPolyline(new PolylineOptions().add(startLocation, endLocation).width(10).color(R.color.purple));
        }
    }

    // Method to get coordinates for known locations
    private LatLng getCoordinates(String location) {
        switch (location) {
            case "Montreal":
                return new LatLng(45.5017, -73.5673);
            case "Toronto":
                return new LatLng(43.6532, -79.3832);
            case "Ottawa":
                return new LatLng(45.4215, -75.6992);
            default:
                return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
    }
}
