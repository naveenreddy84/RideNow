package HomePages;

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

import com.example.ridenow.AvailableRides;
import com.example.ridenow.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomerHomeScreen extends AppCompatActivity {

    TextView title, fromAddresstitle, ToAddresstitle;
    Spinner snipperfromlocations, snipperTolocations;
    private DatePicker date;
    Button searchBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    String[] locationsArray;
    List<String> filteredLocations;

    private Calendar calendar;

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
    }

    private void initializeUI() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        title = findViewById(R.id.title);
        fromAddresstitle = findViewById(R.id.fromAddresstitle);
        ToAddresstitle = findViewById(R.id.ToAddresstitle);
        snipperfromlocations = findViewById(R.id.snipperfromlocations);
        snipperTolocations = findViewById(R.id.snipperTolocations);
        date = findViewById(R.id.date);
        searchBtn = findViewById(R.id.searchBtn);

        locationsArray = getResources().getStringArray(R.array.locations_array);
        filteredLocations = new ArrayList<>(Arrays.asList(locationsArray));

    }


    private void setDatePickerToCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        long currentDateInMillis = calendar.getTimeInMillis();
        date.setMinDate(currentDateInMillis);  // Disable past dates
    }

    private void FromAdapter() {
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locationsArray
        );
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

    private void ToAdapter() {
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                filteredLocations
        );
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snipperTolocations.setAdapter(toAdapter);
    }

    private void setupDatePicker() {
        calendar = Calendar.getInstance();
        date.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) ->
                        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0));
    }

    // Method to perform the search and query Firestore


    private void performSearch() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromLocation = snipperfromlocations.getSelectedItem().toString().trim();
                String toLocation = snipperTolocations.getSelectedItem().toString().trim();
                long selectedDateMillis = calendar.getTimeInMillis();

                if (fromLocation.isEmpty() || toLocation.isEmpty()) {
                    Toast.makeText(CustomerHomeScreen.this, "Please select valid locations.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a Date object from the selected milliseconds
                Date selectedDate = new Date(selectedDateMillis);

                db.collection("rides")
                        .whereEqualTo("fromLocation", fromLocation)
                        .whereEqualTo("toLocation", toLocation)
                        .whereGreaterThanOrEqualTo("Timestamp", new Timestamp(selectedDate)) // Use Timestamp(Date)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                ArrayList<Ride> availableRides = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    long timestampMillis = document.getTimestamp("Timestamp").toDate().getTime();
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
                                Toast.makeText(CustomerHomeScreen.this, "Error fetching rides.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(CustomerHomeScreen.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
