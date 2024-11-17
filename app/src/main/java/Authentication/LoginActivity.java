package Authentication;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import HomePages.CustomerHomeScreen;
import HomePages.DriverHomePage;
import com.example.ridenow.MainActivity;
import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;



public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button  loginBtn;
    TextView registerLink;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerLink);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, DecidingPage.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void loginUser(){
        String email = loginEmail.getText().toString().trim();
        String pswd = loginPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pswd)){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pswd).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                checkUserRole(email);

            }
            else{
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void checkUserRole(String email) {

        db.collection("customers")
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Log.d("LoginActivity", "Customer found, navigating to CustomerPage");
                        navigateToCustomerPage();
                    } else {
                        Log.d("LoginActivity", "Customer not found, checking for drivers");


                        db.collection("drivers")
                                .whereEqualTo("email",email)
                                .get()
                                .addOnCompleteListener(Drivertask -> {
                                    if (Drivertask.isSuccessful() && !Drivertask.getResult().isEmpty()) {
                                        navigateToDriverPage();

                                    } else {

                                        Toast.makeText(this, "No user found with this email", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void navigateToCustomerPage() {

        Intent intent = new Intent(LoginActivity.this, CustomerHomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void navigateToDriverPage() {

        Intent intent = new Intent(LoginActivity.this, DriverHomePage.class);
        startActivity(intent);
        finish();
    }
}
