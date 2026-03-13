package com.eresults.sqlitedatabase;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPhone;
    Button btnSave, btnShow;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize UI components
        initializeViews();

        // saveInfo
        btnSave.setOnClickListener(v -> {

            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            // Name validation
            if (name.isEmpty()) {
                edtName.setError("Name is required");
                edtName.requestFocus();
                return;
            }

            // Email validation
            if (email.isEmpty()) {
                edtEmail.setError("Email is required");
                edtEmail.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Enter a valid email address");
                edtEmail.requestFocus();
                return;
            }

            // Phone validation
            if (phone.isEmpty()) {
                edtPhone.setError("Phone number is required");
                edtPhone.requestFocus();
                return;
            }

            boolean isInserted = dbHelper.saveInfo(name, email, phone);

            if (isInserted) {
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
            }

        });

        // show info
        btnShow.setOnClickListener(v -> {
            // TODO
        });
    }

    private void clearFields() {
        edtName.setText("");
        edtEmail.setText("");
        edtPhone.setText("");
    }

    private void initializeViews() {
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        btnSave = findViewById(R.id.btn_save);
        btnShow = findViewById(R.id.btn_show);
        dbHelper = new DatabaseHelper(this);
    }
}