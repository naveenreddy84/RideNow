package HomePages;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreatingRidePage extends AppCompatActivity {

    LinearLayout lll;
    TextView driverfromAddresstitle,driverToAddresstitle;

    EditText price;

    Button uploadBtn;

    Spinner driversnipperfromlocations,driversnipperTolocations;


    DatePicker datepicker;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    String[] locationsArray;
    List<String> filteredLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.driver_creating_ride_page);


        driverfromAddresstitle = findViewById(R.id.driverfromAddresstitle);
       driverToAddresstitle = findViewById(R.id.driverToAddresstitle);
       driversnipperfromlocations = findViewById(R.id.driversnipperfromlocations);
       driversnipperTolocations = findViewById(R.id.driversnipperTolocations);
       datepicker = findViewById(R.id.datepicker);
       price = findViewById(R.id.price);
        uploadBtn = findViewById(R.id.uploadBtn);

        locationsArray = getResources().getStringArray(R.array.locations_array);
        filteredLocations = new ArrayList<>(Arrays.asList(locationsArray));


        FromAdapter();
        ToAdapter();
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


                    ToAdapter();
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
        driversnipperTolocations .setAdapter(toAdapter);

        driversnipperTolocations .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedToLocation = filteredLocations.get(position);

                if (!selectedToLocation.equals("Select Location")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreatingRidePage.this, "No location selected", Toast.LENGTH_SHORT).show();
            }
        });







    }
}
