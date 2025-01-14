package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;  // Changed from AppCompatActivity to Fragment for navbar

import com.example.ridenow.AvailableRides;
import com.example.ridenow.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomerHomeScreen extends Fragment {  // Changed from AppCompatActivity to Fragment

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_customer_home_screen, container, false);

        initializeUI(view);
        FromAdapter();
        ToAdapter();
        setupDatePicker();
        setDatePickerToCurrentDate();

        performSearch();

        return view;  // Return the inflated view
    }

    private void initializeUI(View view) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        title = view.findViewById(R.id.title);
        fromAddresstitle = view.findViewById(R.id.fromAddresstitle);
        ToAddresstitle = view.findViewById(R.id.ToAddresstitle);
        snipperfromlocations = view.findViewById(R.id.snipperfromlocations);
        snipperTolocations = view.findViewById(R.id.snipperTolocations);
        date = view.findViewById(R.id.date);
        searchBtn = view.findViewById(R.id.searchBtn);

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
                getActivity(),  // Use getActivity() instead of 'this'
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
                Toast.makeText(getActivity(), "No location selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ToAdapter() {
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                getActivity(),  // Use getActivity() instead of 'this'
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

    private void performSearch() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromLocation = snipperfromlocations.getSelectedItem().toString().trim();
                String toLocation = snipperTolocations.getSelectedItem().toString().trim();
                long selectedDateMillis = calendar.getTimeInMillis();

                if (fromLocation.isEmpty() || toLocation.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select valid locations.", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                                    Toast.makeText(getActivity(), "No rides available.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(getActivity(), AvailableRides.class);
                                    intent.putExtra("availableRides", availableRides);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error fetching rides.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
