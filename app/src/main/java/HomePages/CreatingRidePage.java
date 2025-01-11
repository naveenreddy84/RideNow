package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreatingRidePage extends AppCompatActivity {

    private TextView driverfromAddresstitle, driverToAddresstitle, time;
    private EditText price;
    private Button uploadBtn;
    private Spinner driversnipperfromlocations, driversnipperTolocations;
    private DatePicker datepicker;

    private FirebaseFirestore db;


    private Timestamp formatedDateTimestamp;
    private String[] locationsArray;
    private List<String> filteredLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_creating_ride_page);

        // Initialize views and Firebase instances
        initializeComponents();

        // Set up location spinners
        FromAdapter();
        ToAdapter();

        // Initialize DatePicker
        initializeDatePicker();

// setting datepicker to current date
        setDatePickerToCurrentDate();

        // Set up upload button
        setupUploadButton();
    }



    private void setDatePickerToCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        long currentDateInMillis = calendar.getTimeInMillis();
        datepicker.setMinDate(currentDateInMillis);  // Set minimum date to current date
    }

    private void initializeComponents() {
        driverfromAddresstitle = findViewById(R.id.driverfromAddresstitle);
        driverToAddresstitle = findViewById(R.id.driverToAddresstitle);
        driversnipperfromlocations = findViewById(R.id.driversnipperfromlocations);
        driversnipperTolocations = findViewById(R.id.driversnipperTolocations);
        datepicker = findViewById(R.id.datepicker);
        price = findViewById(R.id.price);
        uploadBtn = findViewById(R.id.uploadBtn);
        time = findViewById(R.id.time);

        db = FirebaseFirestore.getInstance();


        locationsArray = getResources().getStringArray(R.array.locations_array);
        filteredLocations = new ArrayList<>(Arrays.asList(locationsArray));
    }

    private void FromAdapter() {
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locationsArray
        );
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driversnipperfromlocations.setAdapter(fromAdapter);

        driversnipperfromlocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromLocation = locationsArray[position];

                if (!selectedFromLocation.equals("Select Location")) {
                    filteredLocations = new ArrayList<>(Arrays.asList(locationsArray));
                    filteredLocations.remove(selectedFromLocation);
                    ToAdapter(); // Refresh ToAdapter
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreatingRidePage.this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ToAdapter() {
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                filteredLocations
        );
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driversnipperTolocations.setAdapter(toAdapter);

        driversnipperTolocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // No specific logic needed here for ToAdapter
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreatingRidePage.this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeDatePicker() {

        datepicker.init(
                datepicker.getYear(),
                datepicker.getMonth(),
                datepicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Create a Calendar instance with the selected date
                        Calendar selectedCalendarDate = Calendar.getInstance();
                        selectedCalendarDate.set(year, monthOfYear, dayOfMonth);

                        // Convert the Calendar date to a Date object
                        Date selectedDate = selectedCalendarDate.getTime();

                        // Convert Date to Firestore Timestamp
                        Timestamp firestoreTimestamp = new Timestamp(selectedDate);
                        System.out.println("Selected Timestamp: " + firestoreTimestamp);

                        // Store the Timestamp in a global variable for later use
                        formatedDateTimestamp = firestoreTimestamp;
                    }
                });
    }


    private void setupUploadButton() {
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromLocation = driversnipperfromlocations.getSelectedItem().toString();
                String toLocation = driversnipperTolocations.getSelectedItem().toString();
                String priceInput = price.getText().toString().trim();
                String timeInput = time.getText().toString().trim();

                // Check if all required fields are filled
                if (!fromLocation.equals("Select Location") &&
                        !toLocation.equals("Select Location") &&
                        !priceInput.isEmpty() &&
                        !timeInput.isEmpty() &&
                        formatedDateTimestamp != null) {

                    //  saving the data  to Firestore
                    Map<String, Object> rideData = new HashMap<>();
                    rideData.put("fromLocation", fromLocation);
                    rideData.put("toLocation", toLocation);
                    rideData.put("Timestamp", formatedDateTimestamp);
                    rideData.put("price", priceInput);
                    rideData.put("time", timeInput);

                    // Save the data to Firestore
                    db.collection("rides")
                            .add(rideData)
                            .addOnSuccessListener(documentReference -> {
                                String rideId = documentReference.getId();
                                Intent intent = new Intent(CreatingRidePage.this, ConfirmRidePage.class);
                                intent.putExtra("rideId", rideId);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(CreatingRidePage.this, "Error adding data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                } else {
                    Toast.makeText(CreatingRidePage.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}




























