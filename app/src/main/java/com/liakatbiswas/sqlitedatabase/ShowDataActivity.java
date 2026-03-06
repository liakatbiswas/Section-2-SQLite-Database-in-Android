package com.liakatbiswas.sqlitedatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ShowDataActivity extends AppCompatActivity {

    TextView tvDisplay;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_data);

        // Initialize UI component
        tvDisplay = findViewById(R.id.tv_display);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Load and display data
        loadData();
    }

    /**
     * Fetch all records from database and display them in TextView
     */
    private void loadData() {

        Cursor cursor = databaseHelper.getAllData();

        // If no data exists
        if (cursor.getCount() == 0) {
            tvDisplay.setText("No data found");
            cursor.close();
            return;
        }

        // Display total number of rows
        tvDisplay.setText("Total Data: " + cursor.getCount() + "\n\n");

        // Loop through all rows
        while (cursor.moveToNext()) {

            // Get column values
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

            // Append each record to TextView
            tvDisplay.append(
                    "ID: " + id +
                            " | Name: " + name +
                            " | Phone: " + phone + "\n"
            );
        }

        // Always close cursor to prevent memory leak
        cursor.close();
    }

}
