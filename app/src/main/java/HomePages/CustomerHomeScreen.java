package HomePages;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.MainActivity;
import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CustomerHomeScreen extends AppCompatActivity {



    TextView title,fromAddresstitle,ToAddresstitle;


Spinner snipperfromlocations,snipperTolocations;
    FirebaseAuth mAuth;

    FirebaseFirestore db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_screen);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.title);
        fromAddresstitle = findViewById(R.id.fromAddresstitle);
         ToAddresstitle =findViewById(R.id.ToAddresstitle);
         snipperfromlocations = findViewById(R.id.snipperfromlocations);
        snipperTolocations = findViewById(R.id.snipperTolocations);

        String[] locations_array = getResources().getStringArray(R.array.locations_array);

        ArrayAdapter<String> Fromadapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locations_array
        );

        Fromadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        snipperfromlocations.setAdapter(Fromadapter);








        snipperfromlocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String SelectedFromLocation = locations_array[position];

                if (!SelectedFromLocation.equals("Selected Location")) {
                    Toast.makeText(CustomerHomeScreen.this, "Selected: " + SelectedFromLocation, Toast.LENGTH_SHORT).show();
                    List<String> filteredLocations = new ArrayList<>(Arrays.asList(locations_array));
                    filteredLocations.remove(SelectedFromLocation);
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
        Toast.makeText(CustomerHomeScreen.this,"no location is Selected",Toast.LENGTH_SHORT).show();

            }

        });

        ArrayAdapter<String>Toadapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
              locations_array
        );

        Toadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        snipperTolocations.setAdapter(Toadapter);

        snipperTolocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String  SelectedFromLocation = locations_array[position];

                if(!SelectedFromLocation.equals("Selected Location")){
                    Toast.makeText(CustomerHomeScreen.this,"Location selected "+ SelectedFromLocation ,Toast.LENGTH_SHORT).show();

                    List<String> filteredLocations = new ArrayList<>(Arrays.asList(locations_array));
                    filteredLocations.remove(SelectedFromLocation);

                    Toadapter.clear();
                    Toadapter.addAll(filteredLocations);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CustomerHomeScreen.this,"no location is Selected",Toast.LENGTH_SHORT).show();
            }
        });

    }
        }





