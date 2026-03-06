
[Section: 2 SQLite Database in Android](https://www.notion.so/liakatbiswas/Section-2-SQLite-Database-in-Android-31bead649cbf801ea229ec8a3dd8d665)


### activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="<http://schemas.android.com/apk/res/android>"
    xmlns:tools="<http://schemas.android.com/tools>"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp"
    tools:context=".MainActivity">

    <!-- student name -->
    <EditText
        android:id="@+id/edt_name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="12dp"
        android:hint="Enter your name"
        android:inputType="textPersonName" />

    <!-- phone number -->
    <EditText
        android:id="@+id/edt_phone"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp"
        android:hint="Enter phone number"
        android:inputType="phone" />

    <!-- insert data -->
    <Button
        android:id="@+id/btn_insert"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="10dp"
        android:text="Insert Data" />

    <!-- ShowDataActivity -->
    <Button
        android:id="@+id/btn_show"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:backgroundTint="#4444CD"
        android:text="Show Data" />

</LinearLayout>
```

### MainActivity.java

```java
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
```

### DatabaseHelper.java

```jsx
package com.liakatbiswas.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * DatabaseHelper manages SQLite database creation and CRUD operations.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database configuration
    private static final String DATABASE_NAME = "school";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_NAME = "students";

    // Table columns
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone";

    /**
     * Constructor
     * Initializes the SQLiteOpenHelper with database name and version.
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * This is where tables should be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT, " +
                        COL_PHONE + " TEXT)";

        db.execSQL(CREATE_TABLE);
    }

    /**
     * Called when database version changes.
     * This example simply drops the old table and recreates it.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    /**
     * Insert a new student record into the database.
     *
     * @param name  student name
     * @param phone student phone number
     * @return true if insertion successful, otherwise false
     */
    public boolean insertData(String name, String phone) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);

        long result = db.insert(TABLE_NAME, null, values);

        return result != -1;
    }

    /**
     * Retrieve all student records from the database.
     * Cursor is an interface used to read query results
     * returned from the database.
     */
    public Cursor getAllData() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
```

### activity_show_data.xml

```jsx
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="<http://schemas.android.com/apk/res/android>"
    xmlns:tools="<http://schemas.android.com/tools>"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".ShowDataActivity">

    <TextView
        android:id="@+id/tv_display"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Result here" />
</LinearLayout>
```

### ShowDataActivity.java

```java
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
        tvDisplay.setText("Total Data: " + cursor.getCount() + "\\n\\n");

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
                            " | Phone: " + phone + "\\n"
            );
        }

        // Always close cursor to prevent memory leak
        cursor.close();
    }

}
```

