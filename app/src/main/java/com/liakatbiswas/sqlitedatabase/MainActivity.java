package com.liakatbiswas.sqlitedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI components
    private EditText edtName, edtPhone;
    private Button btnInsert, btnShow;

    // Database helper instance for performing database operations
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables drawing behind system bars (status bar, navigation bar)
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // Initialize UI components
        initViews();

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Attach click listeners to buttons
        // Insert button click event
        btnInsert.setOnClickListener(v -> handleInsertButton());

        // Show button opens another activity to display stored data
        btnShow.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShowDataActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Find and assign all view elements from the layout.
     */
    private void initViews() {
        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        btnInsert = findViewById(R.id.btn_insert);
        btnShow = findViewById(R.id.btn_show);
    }


    /**
     * Reads user input and inserts data into the database.
     */
    private void handleInsertButton() {

        // Get user input and remove extra spaces
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        // Basic validation to prevent empty data insertion
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please enter name and phone", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert data into database
        boolean result = databaseHelper.insertData(name, phone);

        // Check if insertion was successful
        if (result) {
            Toast.makeText(this, "Data Successfully Added", Toast.LENGTH_SHORT).show();

            // Clear input fields after successful insert
            edtName.setText("");
            edtPhone.setText("");

        } else {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }
    }
}