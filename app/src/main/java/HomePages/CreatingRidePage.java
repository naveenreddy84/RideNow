package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CreatingRidePage extends AppCompatActivity {



    TextView driverfromAddresstitle,driverToAddresstitle,bio;

    EditText price;

    Button uploadBtn;

    Spinner driversnipperfromlocations,driversnipperTolocations;


    DatePicker datepicker;

    FirebaseFirestore db;
    FirebaseAuth mAuth;


  private  String formattedDate;

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
        bio = findViewById(R.id.bio);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


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
        driversnipperTolocations.setAdapter(toAdapter);

        driversnipperTolocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromLocation = driversnipperfromlocations.getSelectedItem().toString();
                String ToLocation =  driversnipperTolocations.getSelectedItem().toString();
                String  Price = price.getText().toString().trim();
                String Bio = bio.getText().toString().trim();
                String formatteddate = formattedDate;


                if (!fromLocation.equals("Select Location") && !ToLocation.equals("Select Location") && !Price.isEmpty() && !Bio.isEmpty() && formatteddate != null) {


                Map<String,Object> rideData = new HashMap<>();

                rideData.put("fromLocation",fromLocation);
                rideData.put("ToLocation",ToLocation);
                rideData.put("formatedDate",formatteddate);
                rideData.put("Price",Price);
                rideData.put("Bio",Bio);


                // saving data in the 'rides' under drivers document

                db.collection("rides")
                        .add(rideData)
                        .addOnSuccessListener(documentReference -> {

                            String rideId = documentReference.getId();

                            Intent intent = new Intent(CreatingRidePage.this, ConfirmRidePage.class);
                            intent.putExtra("rideId",rideId);
                            startActivity(intent);
                            finish();

                        })
                        .addOnFailureListener(e -> {
                            System.out.println("Error adding data: " + e.getMessage());
                        });



                } else {
                    Toast.makeText(CreatingRidePage.this, "Please select all the fields", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //   saving the date selected from the xml file


        datepicker.init(
                datepicker.getYear(),
                datepicker.getMonth(),
                datepicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedCalenderDate = Calendar.getInstance();
                        selectedCalenderDate.set(dayOfMonth, monthOfYear, year);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        formattedDate = sdf.format(selectedCalenderDate.getTime());

                    }
                });






















    }
}
