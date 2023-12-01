package com.example.mcfinal;import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HealthData.db";
    private static final int DATABASE_VERSION = 1;

    // Heart rate table
    private static final String TABLE_HEART_RATE = "heart_rate";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HEART_RATE = "heart_rate";

    // Sleep cycle table
    private static final String TABLE_SLEEP_CYCLE = "sleep_cycle";
    private static final String COLUMN_CYCLE_ID = "id";
    private static final String COLUMN_SLEEP_CYCLE = "sleep_cycle";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create heart rate table
        String createHeartRateTable = "CREATE TABLE " + TABLE_HEART_RATE +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HEART_RATE + " INTEGER)";
        db.execSQL(createHeartRateTable);

        // Create sleep cycle table
        String createSleepCycleTable = "CREATE TABLE " + TABLE_SLEEP_CYCLE +
                "(" + COLUMN_CYCLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SLEEP_CYCLE + " TEXT)";
        db.execSQL(createSleepCycleTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEART_RATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP_CYCLE);

        // Create new tables
        onCreate(db);
    }

    // Insert heart rate into the database
    public void insertHeartRate(int heartRate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + TABLE_HEART_RATE + "(" + COLUMN_HEART_RATE + ") VALUES (" + heartRate + ")";
        db.execSQL(insertQuery);
        db.close();
    }

    // Insert sleep cycle into the database
    public void insertSleepCycle(String sleepCycle) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + TABLE_SLEEP_CYCLE + "(" + COLUMN_SLEEP_CYCLE + ") VALUES ('" + sleepCycle + "')";
        db.execSQL(insertQuery);
        db.close();
    }
}
