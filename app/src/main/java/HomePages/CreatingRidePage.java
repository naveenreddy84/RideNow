package HomePages;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreatingRidePage extends AppCompatActivity {

    LinearLayout lll;
    TextView driverfromAddresstitle,driverToAddresstitle;

    Spinner driversnipperfromlocations,driversnipperTolocations;


    DatePicker datepicker;
    FrameLayout fragment_container;
    BottomNavigationView bottom_navigation;

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
       fragment_container = findViewById(R.id.fragment_container);
       bottom_navigation = findViewById(R.id.bottom_navigation);


    }
}
