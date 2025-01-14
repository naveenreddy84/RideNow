package Payments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;

public class PaymentSuccessPage extends AppCompatActivity {


    TextView ridedetailstitle, fromLocationView, toLocationView, priceView, time, formatedDateView, paymentStatusMessage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_success_page);

       // initiliaze views
        ridedetailstitle = findViewById(R.id.ridedetailstitle);
        paymentStatusMessage = findViewById(R.id.paymentStatusMessage);
        TextView fromLocationView = findViewById(R.id.fromLocationView);
        TextView toLocationView = findViewById(R.id.toLocationView);
        TextView priceView = findViewById(R.id.priceView);
        TextView timeView = findViewById(R.id.timeView);
        TextView dateView = findViewById(R.id.formatedDateView);


         // get data from the intent


        Intent intent = getIntent();
        String fromLocation = intent.getStringExtra("fromLocation");
        String toLocation = intent.getStringExtra("toLocation");
        String price = intent.getStringExtra("price");
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");





        fromLocationView.setText("From: " + fromLocation);
        toLocationView.setText("To: " + toLocation);
        priceView.setText("Price: " + price);
        timeView.setText("Time: " + time);
        dateView.setText("Date: " + date);
        paymentStatusMessage.setText("Payment Status: Successful");













    }
}