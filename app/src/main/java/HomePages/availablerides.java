package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ridenow.R;

import java.util.ArrayList;

public class availablerides extends AppCompatActivity {



    TextView availableRidesTitle;

      private ListView RidesListView;

    LinearLayout Box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_availablerides);

     availableRidesTitle = findViewById(R.id.availableRidesTitle);
     RidesListView = findViewById(R.id.RidesListview);
      Box = findViewById(R.id.Box);

        // Retrieve the available rides passed from the previous activity

        Intent intent = getIntent();   // this method retrives the intent from previous activity
        ArrayList<String> availableRides = intent.getStringArrayListExtra("availableRides");

        if (availableRides != null && !availableRides.isEmpty()) {
            // Display the rides in the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, availableRides);
            RidesListView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No rides available.", Toast.LENGTH_SHORT).show();
        }




    }
}







