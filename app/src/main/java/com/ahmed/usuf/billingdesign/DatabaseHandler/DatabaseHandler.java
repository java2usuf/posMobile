package com.ahmed.usuf.billingdesign.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmed.usuf.billingdesign.data.TrasactionDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/14/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "POSMobile";

    // Contacts table name
    private static final String TABLE_TRANSACTION = "transactionDetail";

    private static final String KEY_ID = "id";
    public static final String KEY_FINAL_TOTAL = "finalTotal";
    private static final String KEY_DATE = "date";
    private static final String KEY_DISCOUNT_TOTAL = "discountTotal";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_DETAILS_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FINAL_TOTAL + " TEXT," + KEY_DISCOUNT_TOTAL + " TEXT,"+ KEY_DATE + " TEXT" + ");";

        db.execSQL(CREATE_DETAILS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(db);
    }

    public void addTransaction(TrasactionDetails txn) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FINAL_TOTAL, txn.getFinalTotal());
        values.put(KEY_DISCOUNT_TOTAL, txn.getDiscount());
        values.put(KEY_DATE, txn.getDate());
        // Inserting Row
        db.insert(TABLE_TRANSACTION, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Totals and it's Dates
    public List<TrasactionDetails> getAllTransantions() {
        List<TrasactionDetails> txnList = new ArrayList<TrasactionDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrasactionDetails txn = new TrasactionDetails();
                txn.setFinalTotal(Integer.parseInt(cursor.getString(1)));
                txn.setDiscount(Integer.parseInt(cursor.getString(2)));
                txn.setDate(cursor.getString(3));
                // Adding contact to list
                txnList.add(txn);
            } while (cursor.moveToNext());
        }

        // return contact list
        return txnList;
    }

    public int getTxnCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
