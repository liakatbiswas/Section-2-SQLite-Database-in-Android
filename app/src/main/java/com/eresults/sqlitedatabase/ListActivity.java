package com.eresults.sqlitedatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListActivity extends AppCompatActivity {

    TextView tvList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvList = findViewById(R.id.tv_list);
        databaseHelper = new DatabaseHelper(this);

        // Loading data
        loadData();
    }

    private void loadData() {
        Cursor cursor = databaseHelper.showAllInfo();

        if (cursor.getCount() == 0) {
            tvList.setText("No data found");
            cursor.close();
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Total Data: ").append(cursor.getCount()).append("\n\n");

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

            builder.append("ID: ").append(id).append("\n")
                    .append("Name: ").append(name).append("\n")
                    .append("Email: ").append(email).append("\n")
                    .append("Phone: ").append(phone).append("\n\n");
        }

        tvList.setText(builder.toString());

        cursor.close();
    }
}