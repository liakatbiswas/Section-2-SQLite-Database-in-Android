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