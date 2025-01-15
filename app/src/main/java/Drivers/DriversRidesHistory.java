package Drivers;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ridenow.R;

public class DriversRidesHistory extends AppCompatActivity {



    TextView YourRidesHistoryTitle;

    ListView RidesHistoryListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drivers_rides_history);

        YourRidesHistoryTitle = findViewById(R.id.YourRidesHistoryTitle);
        RidesHistoryListview = findViewById(R.id.RidesHistoryListview);


    }
}