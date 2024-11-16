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


    TextView updl,registerLink;
    Uri imageUri;


    EditText registerEmail, registerPassword, confirmPassword;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    private ImageView licenseImageView;
    private Button uploadButton, registerBtn;
    private ActivityResultLauncher<Intent> imagePickerLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_register_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        uploadButton = findViewById(R.id.uploadButton);
        registerBtn = findViewById(R.id.registerBtn);
        updl = findViewById(R.id.updl);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        licenseImageView = findViewById(R.id.licenseImageView);
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



        ActivityResultLauncher<Intent> imagePickerLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        licenseImageView.setImageURI(imageUri);
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        uploadButton.setOnClickListener(v -> selectImage());
    }


    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }


    private void registerDriver() {
        String email = registerEmail.getText().toString().trim();
        String cPassword = confirmPassword.getText().toString().trim();
        String pswd = registerPassword.getText().toString().trim();


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
        if (imageUri == null) {
            Toast.makeText(this, "Please upload a license image", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {

                Drivers Driver = new Drivers(email, cPassword, pswd, imageUri);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Drivers").add(Driver);

                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DriverRegisterPage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}