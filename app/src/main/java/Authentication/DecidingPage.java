package Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ridenow.R;

public class DecidingPage extends AppCompatActivity {


    Button DriverBtn,CustomerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deciding_page);

        DriverBtn = findViewById(R.id.DriverBtn);
        CustomerBtn = findViewById(R.id.CustomerBtn);



        CustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(DecidingPage.this, Register.class);
               startActivity(intent);
                finish();

            }
        });

        DriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecidingPage.this, DriverRegisterPage.class);
                startActivity(intent);
                finish();

            }
        });




    }
}