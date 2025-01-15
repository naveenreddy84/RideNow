package Drivers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class DriverProfilePage extends AppCompatActivity {



TextView drivername;
    Button driverRides;

    EditText titledriverprofile;

   FirebaseAuth mAuth;

   FirebaseFirestore db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_profile_page);


        //initiliaze ui
        drivername =findViewById(R.id.drivername);
        driverRides = findViewById(R.id.driverRides);
        titledriverprofile = findViewById(R.id.titledriverprofile);

        //initilize firebase

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        FirebaseUser  currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null){

            String userId = currentUser.getUid();


            db.collection("Drivers").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()){


                    String Drivername = task.getResult().getString("driverusername");

                    drivername.setText(Drivername);
                    Log.d("UserInfo", "Driver Name: " + Drivername);
                }
            });
        }


       driverRides.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(DriverProfilePage.this,DriversRidesHistory.class);
               startActivity(intent);
           }
       });




    }
}

