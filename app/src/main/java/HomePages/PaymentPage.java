package  HomePages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentPage extends AppCompatActivity {

    private Stripe stripe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        // Initialize Stripe with your publishable key
        stripe = new Stripe(this, "pk_test_51QfwnlFTdqp1BVfOlcBaG0SJftyxo4zULXt6Tb2FrKwvTvgUaTAAdxRhH9qh5mmqXySE3LeDeVg7SeKOCt6TqeqC00sH1k2SD4"); // Replace with your publishable key

        // Fetch the clientSecret from your backend
        String clientSecret = getClientSecretFromBackend(); // Replace with your method to fetch the clientSecret

        // Confirm the payment using the clientSecret
        confirmPayment(clientSecret);
    }

    private void confirmPayment(String clientSecret) {
        // Use the clientSecret to confirm the payment
        stripe.confirmPayment(
                this,
                ConfirmPaymentIntentParams.create(clientSecret) // Updated method to confirm payment
        );
    }

    // Handle the result of the payment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the result to Stripe
        stripe.onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
            @Override
            public void onSuccess(@NonNull PaymentIntentResult result) {
                PaymentIntent paymentIntent = result.getIntent();
                PaymentIntent.Status status = paymentIntent.getStatus();
                if (status == PaymentIntent.Status.Succeeded) {
                    Log.d("Stripe", "Payment succeeded!");
                    // Handle successful payment (e.g., show success screen)
                } else {
                    Log.e("Stripe", "Payment status: " + status);
                    // Handle other statuses
                }
            }

            @Override
            public void onError(@NonNull Exception e) {
                Log.e("Stripe", "Payment error: " + e.getMessage());
                // Handle error
            }
        });
    }
}
