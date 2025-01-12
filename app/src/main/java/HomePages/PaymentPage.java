package HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentPage extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();  // Create a single thread executor
    private Stripe stripe;
    Button paymentButton;
    TextView paymentStatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        // Initialize views
        paymentButton = findViewById(R.id.paymentButton);
        paymentStatusMessage = findViewById(R.id.paymentStatusMessage);

        // Initialize Stripe with your publishable key
        stripe = new Stripe(this, "pk_test_51QfwnlFTdqp1BVfOlcBaG0SJftyxo4zULXt6Tb2FrKwvTvgUaTAAdxRhH9qh5mmqXySE3LeDeVg7SeKOCt6TqeqC00sH1k2SD4");

        // Set the onClickListener for the payment button
        paymentButton.setOnClickListener(v -> {
            // Fetch clientSecret from backend when payment button is clicked
            fetchClientSecretFromBackend();
        });
    }

    // Fetch client secret using ExecutorService (instead of AsyncTask)
    private void fetchClientSecretFromBackend() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Fetch the client secret from backend
                final String clientSecret = getClientSecretFromBackend();

                // After fetching client secret, run this on the main thread to update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (clientSecret != null) {
                            confirmPayment(clientSecret);
                        } else {
                            Toast.makeText(PaymentPage.this, "Failed to fetch client secret", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // method to get the client secret from backend
    public static String getClientSecretFromBackend() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:3000/create-payment-intent") // Use your backend URL (without the dot)
                    .build();

            // Execute the request and fetch the response
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                // Parse the JSON response to extract the clientSecret
                JSONObject jsonResponse = new JSONObject(responseBody);
                return jsonResponse.getString("clientSecret"); // Return the clientSecret from JSON
            } else {
                Log.e("Stripe", "Failed to fetch client secret: " + response.message());
            }
        } catch (Exception e) {
            Log.e("Stripe", "Error fetching client secret: " + e.getMessage());
        }
        return null; // Return null if fetching the clientSecret fails
    }

    // confirming the payment using fetched client secret
    private void confirmPayment(String clientSecret) {
        // Only confirm payment if clientSecret is not null
        if (clientSecret != null) {
            stripe.confirmPayment(
                    this,
                    ConfirmPaymentIntentParams.create(clientSecret) // Pass the clientSecret to confirm payment
            );
        } else {
            Log.e("Stripe", "Failed to fetch client secret.");
        }
    }

    // Handling the result of the payment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the result to Stripe to handle success or failure
        stripe.onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
            @Override
            public void onSuccess(PaymentIntentResult result) {
                // Get the PaymentIntent from the result
                PaymentIntent paymentIntent = result.getIntent();
                PaymentIntent.Status status = paymentIntent.getStatus();

                if (status == PaymentIntent.Status.Succeeded) {
                    // Payment succeeded, log success and show a message to the user
                    Log.d("Stripe", "Payment succeeded!");
                    Toast.makeText(PaymentPage.this, "Payment success", Toast.LENGTH_SHORT).show();
                    paymentStatusMessage.setText("Payment status: " + status);

                    // Retrieve the ride details that were passed to PaymentPage
                    Intent intent = getIntent();
                    String fromLocation = intent.getStringExtra("fromLocation");
                    String toLocation = intent.getStringExtra("toLocation");
                    String price = intent.getStringExtra("price");
                    String time = intent.getStringExtra("time");
                    String date = intent.getStringExtra("date");

                    // Pass these details to the PaymentSuccessPage activity
                    Intent successIntent = new Intent(PaymentPage.this, PaymentSuccessPage.class);
                    successIntent.putExtra("fromLocation", fromLocation);
                    successIntent.putExtra("toLocation", toLocation);
                    successIntent.putExtra("price", price);
                    successIntent.putExtra("time", time);
                    successIntent.putExtra("date", date);

                    // Start the PaymentSuccessPage activity
                    startActivity(successIntent);
                } else {
                    // Handle cases where payment status is not "Succeeded"
                    Log.e("Stripe", "Payment status: " + status);
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle any errors that occur during payment process
                Log.e("Stripe", "Payment error: " + e.getMessage());
                Toast.makeText(PaymentPage.this, "Payment failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
