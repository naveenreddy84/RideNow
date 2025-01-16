package Payments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentPage extends AppCompatActivity {

    private TextView paymentAmountText;
    private Button paymentButton;
    private PaymentSheet paymentSheet;
    private String clientSecret;

    // Assume price is passed as a String from the previous activity
    private String price;

    // Variables to hold the passed ride details
    private String fromLocation, toLocation, time, formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        // Initialize Stripe SDK with your publishable key
        PaymentConfiguration.init(this, "pk_test_51QfwnlFTdqp1BVfOlcBaG0SJftyxo4zULXt6Tb2FrKwvTvgUaTAAdxRhH9qh5mmqXySE3LeDeVg7SeKOCt6TqeqC00sH1k2SD4"); // Replace with your actual publishable key

        // Initialize PaymentSheet
        paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        // Initialize UI elements
        paymentAmountText = findViewById(R.id.paymentAmountText);
        paymentButton = findViewById(R.id.paymentButton);

        // Retrieve the ride details passed from the previous activity
        Intent intent = getIntent();
        fromLocation = intent.getStringExtra("fromLocation");
        toLocation = intent.getStringExtra("toLocation");
        price = intent.getStringExtra("price");  // Assuming price is passed as a string
        time = intent.getStringExtra("time");
        String dateStr = intent.getStringExtra("date");

// Setting  the amount  to be collected by the customer
        paymentAmountText.setText("Amount: " + price);


        formattedDate = formatDate(dateStr);

// Set up the payment button click listener
        paymentButton.setOnClickListener(v -> {
            // Call your backend to create the payment intent
            createPaymentIntent(price);
        });
    }

    // Method to format the date
    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "Date not available";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            return sdf.format(date);
        } catch (Exception e) {
            return "Date not available";
        }
    }


    // Method to call your backend and create the payment intent
    private void createPaymentIntent(String amount) {
        new Thread(() -> {
            try {
                // Make a POST request to your backend to create the Payment Intent
                OkHttpClient client = new OkHttpClient();
                String backendUrl = "https://adf4-67-70-134-21.ngrok-free.app/create-payment-intent"; // Your backend URL

                JSONObject json = new JSONObject();
                double amountInDouble = Double.parseDouble(amount);
                int amountInCents = (int) (amountInDouble * 100);
                json.put("amount", amountInCents); // Amount in cents (Stripe expects this)
                json.put("currency", "usd"); // Currency of the payment

                RequestBody body = RequestBody.create(json.toString(), okhttp3.MediaType.get("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url(backendUrl)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // Parse the response to get the client secret
                    String responseData = response.body().string();
                    JSONObject responseJson = new JSONObject(responseData);
                    clientSecret = responseJson.getString("clientSecret");

                    // Present the Payment Sheet on the UI thread
                    runOnUiThread(this::presentPaymentSheet);
                } else {
                    showToast("Failed to create Payment Intent");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error creating Payment Intent");
            }
        }).start();
    }

    // Method to present the Payment Sheet with the obtained client secret
    private void presentPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration(
                "Payment Page" // Payment Sheet title
        );

        paymentSheet.presentWithPaymentIntent(clientSecret, configuration);
    }

    // Method to handle the result of the payment process
    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            showToast("Payment Successful!");

            // Create the intent to pass to the next page
            Intent intent = new Intent(PaymentPage.this, PaymentSuccessPage.class);

            // Pass the necessary data to the next activity
            intent.putExtra("paymentAmount", price);  // You can pass more data as needed
            intent.putExtra("paymentStatus", "Success");
            intent.putExtra("fromLocation", fromLocation);
            intent.putExtra("toLocation", toLocation);
            if (price == null || price.isEmpty()) {
                showToast("Price is missing");
            }
            intent.putExtra("time", time);

            if (formattedDate == null || formattedDate.isEmpty()) {
                showToast("Date is missing");
            }
            intent.putExtra("formattedDate", formattedDate);
            intent.putExtra("clientSecret", clientSecret); // If needed, pass the client secret as well

            // Start the next activity
            startActivity(intent);
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            showToast("Payment Canceled.");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            String error = ((PaymentSheetResult.Failed) paymentSheetResult).getError().getLocalizedMessage();
            showToast("Payment Failed: " + error);
        }
    }

    // Utility method to show toast messages
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}
