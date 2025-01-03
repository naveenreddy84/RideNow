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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomerHomeScreen extends AppCompatActivity {


    TextView title, fromAddresstitle, ToAddresstitle;
    Spinner snipperfromlocations, snipperTolocations;

    DatePicker date;
    Button searchBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    String[] locationsArray;
    List<String> filteredLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_screen);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.title);
        fromAddresstitle = findViewById(R.id.fromAddresstitle);
        ToAddresstitle = findViewById(R.id.ToAddresstitle);
        snipperfromlocations = findViewById(R.id.snipperfromlocations);
        snipperTolocations = findViewById(R.id.snipperTolocations);
        date = findViewById(R.id.date);
        searchBtn = findViewById(R.id.searchBtn);


        locationsArray = getResources().getStringArray(R.array.locations_array);
        filteredLocations = new ArrayList<>(Arrays.asList(locationsArray));


        FromAdapter();
        ToAdapter();


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
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

        snipperTolocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedToLocation = filteredLocations.get(position);
                if (!selectedToLocation.equals("Select Location")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CustomerHomeScreen.this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch() {
        String fromLocation = snipperfromlocations.getSelectedItem().toString();
        String toLocation = snipperTolocations.getSelectedItem().toString();

       // retriving the date
        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year = date.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);  // month is 0-based (January = 0)
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long selectedDateInMillis = calendar.getTimeInMillis();


// Set the default date
        Calendar defaultCalendar = Calendar.getInstance();
        defaultCalendar.set(Calendar.YEAR, 2024);
        defaultCalendar.set(Calendar.MONTH, Calendar.DECEMBER);  // December is Calendar.DECEMBER (11)
        defaultCalendar.set(Calendar.DAY_OF_MONTH, 30);
        defaultCalendar.set(Calendar.HOUR_OF_DAY, 0);
        defaultCalendar.set(Calendar.MINUTE, 0);
        defaultCalendar.set(Calendar.SECOND, 0);
        defaultCalendar.set(Calendar.MILLISECOND, 0);
        long defaultDateInMillis = defaultCalendar.getTimeInMillis();


        // Create a Date object from milliseconds
        Date selectedDate = new Date(selectedDateInMillis);

        // Convert the Date to Firestore Timestamp

        Timestamp firestoreTimestamp = new Timestamp(selectedDate);

        if (!fromLocation.equals("Select Location") && !toLocation.equals("Select Location")) {
            if (selectedDateInMillis < defaultDateInMillis) {
                Toast.makeText(this, "select correct Date", Toast.LENGTH_SHORT).show();
            } else {
                db.collection("rides")
                        .whereEqualTo("fromLocation", fromLocation)
                        .whereEqualTo("toLocation", toLocation)
                        .whereEqualTo("formatedDate", firestoreTimestamp)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                ArrayList<String> availableRides = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    availableRides.add(document.getId()); // Collect ride IDs or details
                                }
                                if (availableRides.isEmpty()) {
                                    Toast.makeText(this, "No rides available.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(CustomerHomeScreen.this, availablerides.class);
                                    intent.putStringArrayListExtra("availableRides", availableRides);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(this, "Error fetching rides.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
            } else{
                Toast.makeText(this, "Please select both From and To locations", Toast.LENGTH_SHORT).show();
            }

    }

}







