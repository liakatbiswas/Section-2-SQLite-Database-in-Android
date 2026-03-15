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

    TextView tvList;
    DatabaseHelper databaseHelper;
    EditText edtSearchId;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        tvList = findViewById(R.id.tv_list);
        edtSearchId = findViewById(R.id.edtSearchId);
        btnSearch = findViewById(R.id.btnSearch);

        databaseHelper = new DatabaseHelper(this);

        // Show all data
        loadAllData();

        // Auto search
        searchData();
    }


    // সব ডাটা দেখানোর method
    private void loadAllData() {

        Cursor cursor = databaseHelper.showAllInfo();

        if (cursor.getCount() == 0) {
            tvList.setText(R.string.no_data_found);
            cursor.close();
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Total Data: ").append(cursor.getCount()).append("\n\n");

        while (cursor.moveToNext()) {

            builder.append("ID: ").append(cursor.getInt(0)).append("\n")
                    .append("Name: ").append(cursor.getString(1)).append("\n")
                    .append("Email: ").append(cursor.getString(2)).append("\n")
                    .append("Phone: ").append(cursor.getString(3)).append("\n\n");
        }

        tvList.setText(builder.toString());

        cursor.close();
    }


    private void searchData() {
        edtSearchId.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String idText = s.toString().trim();

                if (idText.isEmpty()) {
                    loadAllData(); // Empty হলে সব data
                } else {

                    int id = Integer.parseInt(idText);

                    Cursor cursor = databaseHelper.searchDataById(id);

                    if (cursor.getCount() == 0) {
                        tvList.setText("No data found");
                        cursor.close();
                        return;
                    }

                    StringBuilder builder = new StringBuilder();

                    while (cursor.moveToNext()) {

                        builder.append("ID: ").append(cursor.getInt(0)).append("\n")
                                .append("Name: ").append(cursor.getString(1)).append("\n")
                                .append("Email: ").append(cursor.getString(2)).append("\n")
                                .append("Phone: ").append(cursor.getString(3)).append("\n\n");
                    }

                    tvList.setText(builder.toString());

                    cursor.close();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}