package com.eresults.sqlitedatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {

    // UI Components
    TextView tvList;
    EditText edtSearchId;

    // Database helper class to access SQLite database
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        // Initialize UI Components
        tvList = findViewById(R.id.tv_list);
        edtSearchId = findViewById(R.id.edtSearchId);

        // Initialize Database
        databaseHelper = new DatabaseHelper(this);


        // Load data when app start
        loadAllData();

        // Search functionality setup
        setupSearch();
    }


    // =====================================================
    // Method: loadAllData()
    // Purpose: Database থেকে সব data নিয়ে UI তে show করা
    // =====================================================
    private void loadAllData() {

        // Database থেকে সব data fetch
        Cursor cursor = databaseHelper.showAllInfo();

        // Data display method call
        displayData(cursor);
    }


    // =====================================================
    // Method: setupSearch()
    // Purpose: EditText এ ID লিখলে real-time search করা
    // =====================================================
    private void setupSearch() {

        edtSearchId.addTextChangedListener(new TextWatcher() {

            // Text change হওয়ার আগে trigger হয়
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            // Text change হওয়ার সময় trigger হয়
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // EditText থেকে ID text নেওয়া
                String idText = s.toString().trim();

                // যদি EditText খালি হয় → সব data show করবে
                if (idText.isEmpty()) {
                    loadAllData();
                    return;
                }

                try {

                    // String → Integer convert
                    int id = Integer.parseInt(idText);

                    // Database থেকে নির্দিষ্ট ID এর data search
                    Cursor cursor = databaseHelper.searchDataById(id);

                    // UI তে data show
                    displayData(cursor);

                } catch (NumberFormatException e) {

                    // যদি ID number না হয়
                    tvList.setText("Invalid ID");

                }
            }

            // Text change হওয়ার পরে trigger হয়
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    // =====================================================
    // Method: displayData()
    // Purpose: Cursor থেকে data নিয়ে TextView তে show করা
    // =====================================================
    private void displayData(Cursor cursor) {

        // যদি কোন data না থাকে
        if (cursor == null || cursor.getCount() == 0) {

            tvList.setText(R.string.no_data_found);

            // Cursor close করা
            if (cursor != null) cursor.close();

            return;
        }

        // String build করার জন্য StringBuilder ব্যবহার
        StringBuilder builder = new StringBuilder();

        // Total data count show
        builder.append("Total Data: ").append(cursor.getCount()).append("\n\n");


        // =============================
        // Column index বের করা
        // =============================
        int idIndex = cursor.getColumnIndexOrThrow("id");
        int nameIndex = cursor.getColumnIndexOrThrow("name");
        int emailIndex = cursor.getColumnIndexOrThrow("email");
        int phoneIndex = cursor.getColumnIndexOrThrow("phone");


        // =============================
        // Cursor loop করে সব row পড়া
        // =============================
        while (cursor.moveToNext()) {

            // Column index ব্যবহার করে value নেওয়া
            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String email = cursor.getString(emailIndex);
            String phone = cursor.getString(phoneIndex);

            // Data builder এ append করা
            builder.append("ID: ").append(id).append("\n").append("Name: ").append(name).append("\n").append("Email: ").append(email).append("\n").append("Phone: ").append(phone).append("\n\n");
        }

        // Final result TextView তে show
        tvList.setText(builder.toString());

        // Cursor close to avoid memory leak
        cursor.close();
    }
}