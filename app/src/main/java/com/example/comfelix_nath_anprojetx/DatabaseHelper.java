package com.example.comfelix_nath_anprojetx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservations.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_voyage INTEGER, " +
                "destination TEXT, " +
                "date_voyage TEXT, " +
                "montant REAL, " +
                "statut TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reservations");
        onCreate(db);
    }

    public boolean ajouterReservation(int idVoyage, String destination, String dateVoyage, double montant, String statut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_voyage", idVoyage);
        values.put("destination", destination);
        values.put("date_voyage", dateVoyage);
        values.put("montant", montant);
        values.put("statut", statut); // Ex: "confirmée"

        long result = db.insert("reservations", null, values);
        db.close();

        return result != -1;
    }


    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("reservations", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Reservation reservation = new Reservation(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("id_voyage")),
                        cursor.getString(cursor.getColumnIndex("destination")),
                        cursor.getString(cursor.getColumnIndex("date_voyage")),
                        cursor.getDouble(cursor.getColumnIndex("montant")),
                        cursor.getString(cursor.getColumnIndex("statut"))
                );
                reservations.add(reservation);
            }
            cursor.close();
        }
        return reservations;
    }

    public void updateReservationStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("statut", newStatus);
        db.update("reservations", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

