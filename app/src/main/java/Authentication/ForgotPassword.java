package Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;

import HomePages.CustomerHomeScreen;

public class ForgotPassword extends AppCompatActivity {

    EditText inputEmail;
    Button resetpswdBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgot_password);

        inputEmail = findViewById(R.id.inputEmail);
       resetpswdBtn = findViewById(R.id.resetpswdBtn);

        mAuth = FirebaseAuth.getInstance();





        resetpswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                   inputEmail.setError("Email is required");
                    inputEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Invalid Email");
                    inputEmail.requestFocus();
                    return;
                }


                sendPasswordResetEmail(email);
                navigateToLoginPage();
            }

        });
    }
    private void navigateToLoginPage() {

        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this,
                                "Password reset email sent. Check your inbox.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPassword.this,
                                "Failed to send reset email. Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

