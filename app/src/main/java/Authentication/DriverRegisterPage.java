package Authentication;

import static java.sql.DriverManager.registerDriver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.MainActivity;
import com.example.ridenow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DriverRegisterPage extends AppCompatActivity {


    TextView registerLink;

    Button registerBtn;
    EditText registerEmail, registerPassword, confirmPassword,driverusername;

    FirebaseAuth mAuth;
    FirebaseFirestore db;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_register_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        driverusername = findViewById(R.id.driverusername);
        registerBtn = findViewById(R.id.registerBtn);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerLink = findViewById(R.id.registerLink);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDriver();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRegisterPage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


        private void registerDriver () {
            String email = registerEmail.getText().toString().trim();
            String cPassword = confirmPassword.getText().toString().trim();
            String pswd = registerPassword.getText().toString().trim();
            String  uname  =  driverusername.getText().toString().trim();


            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pswd) || TextUtils.isEmpty(cPassword)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                registerEmail.setError("Invalid Email ");
                registerEmail.requestFocus();
                return;
            }

            if (pswd.length() < 8) {
                registerPassword.setError("Password must be atleast 8 characters");
                registerPassword.requestFocus();
                return;
            }

            if (!pswd.equals(cPassword)) {
                confirmPassword.setError("Passwords do not match");
                confirmPassword.requestFocus();
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(this, task -> {

                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();

                    if(user != null){
                        user.sendEmailVerification().addOnCompleteListener( emailTask -> {
                            if(emailTask.isSuccessful()){
                                Toast.makeText(this, "Registration Successful.please verify your email", Toast.LENGTH_SHORT).show();
                                Drivers Driver = new Drivers(email, cPassword, pswd,uname);
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Drivers").add(Driver);

                                Intent intent = new Intent(DriverRegisterPage.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(this,"failed to verify Email",Toast.LENGTH_SHORT).show();
                            }


                        });
                    }


                } else {
                    Toast.makeText(this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            });
        }


            @Override
            protected void onStart () {
                super.onStart();
            }
        }



