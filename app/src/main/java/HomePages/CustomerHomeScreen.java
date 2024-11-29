package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerHomeScreen extends AppCompatActivity {

    TextView title, fromAddresstitle, ToAddresstitle;
    Spinner snipperfromlocations, snipperTolocations;

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
        searchBtn = findViewById(R.id.searchBtn);


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

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromLocation = snipperfromlocations.getSelectedItem().toString();
                String toLocation = snipperTolocations.getSelectedItem().toString();

                if (!fromLocation.equals("Select Location") && !toLocation.equals("Select Location")) {
                    Intent intent = new Intent(CustomerHomeScreen.this, availablerides.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CustomerHomeScreen.this, "Please select both From and To locations", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }
}






