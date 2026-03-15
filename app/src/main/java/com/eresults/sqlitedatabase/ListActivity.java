package com.eresults.sqlitedatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvList = findViewById(R.id.tv_list);
        databaseHelper = new DatabaseHelper(this);

        // Loading data
        loadData();

        edtSearchId = findViewById(R.id.edtSearchId);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> {

            String idText = edtSearchId.getText().toString();

            if(idText.isEmpty()){
                Toast.makeText(this,"Enter ID",Toast.LENGTH_SHORT).show();
                return;
            }

            int id = Integer.parseInt(idText);

            Cursor cursor = databaseHelper.searchDataById(id);

            if(cursor.getCount() == 0){
                tvList.setText("No data found");
                cursor.close();
                return;
            }

            StringBuilder builder = new StringBuilder();

            while(cursor.moveToNext()){
                builder.append("ID: ").append(cursor.getInt(0)).append("\n")
                        .append("Name: ").append(cursor.getString(1)).append("\n")
                        .append("Email: ").append(cursor.getString(2)).append("\n")
                        .append("Phone: ").append(cursor.getString(3)).append("\n\n");
            }

            tvList.setText(builder.toString());

            cursor.close();
        });
    }

    private void loadData() {
        Cursor cursor = databaseHelper.showAllInfo();

        if (cursor.getCount() == 0) {
            tvList.setText(R.string.no_data_found);
            cursor.close();
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Total Data: ").append(cursor.getCount()).append("\n\n");

        int idIndex = cursor.getColumnIndexOrThrow("id");
        int nameIndex = cursor.getColumnIndexOrThrow("name");
        int emailIndex = cursor.getColumnIndexOrThrow("email");
        int phoneIndex = cursor.getColumnIndexOrThrow("phone");

        while (cursor.moveToNext()) {

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String email = cursor.getString(emailIndex);
            String phone = cursor.getString(phoneIndex);

            builder.append("ID: ").append(id).append("\n")
                    .append("Name: ").append(name).append("\n")
                    .append("Email: ").append(email).append("\n")
                    .append("Phone: ").append(phone).append("\n\n");
        }

        tvList.setText(builder.toString());

        cursor.close();
    }
}