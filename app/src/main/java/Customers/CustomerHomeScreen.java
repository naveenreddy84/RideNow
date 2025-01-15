package Customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Rides.Ride;

public class CustomerHomeScreen extends AppCompatActivity implements OnMapReadyCallback {

    private TextView title;
    private Spinner snipperfromlocations, snipperTolocations;
    private DatePicker date;
    private Button searchBtn;
    private FirebaseFirestore db;

    private String[] locationsArray;
    private List<String> filteredLocations;

    private Calendar calendar;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_screen);

        initializeUI();
        FromAdapter();
        ToAdapter();
        setupDatePicker();
        setDatePickerToCurrentDate();

        performSearch();

        // Initialize UI components and map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // Initialize UI elements
    private void initializeUI() {
        db = FirebaseFirestore.getInstance();
        title = findViewById(R.id.title);
        snipperfromlocations = findViewById(R.id.snipperfromlocations);
        snipperTolocations = findViewById(R.id.snipperTolocations);
        date = findViewById(R.id.date);
        searchBtn = findViewById(R.id.searchBtn);
        locationsArray = getResources().getStringArray(R.array.locations_array);
        filteredLocations = new ArrayList<>(Arrays.asList(locationsArray));
    }

    // Set the DatePicker to current date
    private void setDatePickerToCurrentDate() {
       Calendar  calendar = Calendar.getInstance();
        date.setMinDate(calendar.getTimeInMillis());
    }

    // Adapter for "from" locations
    private void FromAdapter() {
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationsArray);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snipperfromlocations.setAdapter(fromAdapter);

        snipperfromlocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromLocation = locationsArray[position];
                if (!selectedFromLocation.equals("Select Location")) {
                    filteredLocations = new ArrayList<>(Arrays.asList(locationsArray));
                    filteredLocations.remove(selectedFromLocation);
                    ToAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CustomerHomeScreen.this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Adapter for "to" locations
    private void ToAdapter() {
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filteredLocations);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snipperTolocations.setAdapter(toAdapter);
    }

    // Setup DatePicker listener
    private void setupDatePicker() {

        Calendar calendar = Calendar.getInstance();

        date.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) -> calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0));
    }

    // Perform search based on user input
    private void performSearch() {
        searchBtn.setOnClickListener(v -> {
            String fromLocation = snipperfromlocations.getSelectedItem().toString().trim();
            String toLocation = snipperTolocations.getSelectedItem().toString().trim();
            long selectedDateMillis = calendar.getTimeInMillis();

            if (fromLocation.isEmpty() || toLocation.isEmpty()) {
                Toast.makeText(CustomerHomeScreen.this, "Please select valid locations.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Query Firebase to find available rides
            Date selectedDate = new Date(selectedDateMillis);
            db.collection("rides")
                    .whereEqualTo("fromLocation", fromLocation)
                    .whereEqualTo("toLocation", toLocation)
                    .whereGreaterThanOrEqualTo("Timestamp", new Timestamp(selectedDate))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            ArrayList<Ride> availableRides = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Ride ride = new Ride(
                                        document.getId(),
                                        document.getString("fromLocation"),
                                        document.getString("toLocation"),
                                        document.getString("time"),
                                        document.getString("price"),
                                        (document.getTimestamp("Timestamp") != null)
                                                ? document.getTimestamp("Timestamp").toDate().getTime()
                                                : 0);
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
                            Toast.makeText(CustomerHomeScreen.this, "Error fetching rides.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(CustomerHomeScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        // Get the selected locations
        String fromLocation = snipperfromlocations.getSelectedItem().toString().trim();
        String toLocation = snipperTolocations.getSelectedItem().toString().trim();

        // Use MapHelper to set up the map
        MapHelper.setupMap(map, fromLocation, toLocation, this);
    }
}
