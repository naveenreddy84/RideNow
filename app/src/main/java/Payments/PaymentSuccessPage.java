package Payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ridenow.R;

import Customers.CustomerHomeScreen;

public class PaymentSuccessPage extends AppCompatActivity {

    TextView ridedetailstitle, fromLocationView, toLocationView, priceView, timeView, formatedDateView, paymentStatusMessage;
    Button Homebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_success_page);

        // Initialize views
        ridedetailstitle = findViewById(R.id.ridedetailstitle);
        Homebtn = findViewById(R.id.Homebtn);


        Homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentSuccessPage.this, CustomerHomeScreen.class);
                startActivity(intent);
            }
        });


        paymentStatusMessage.setText("Payment Status: Successful");

    }
}
