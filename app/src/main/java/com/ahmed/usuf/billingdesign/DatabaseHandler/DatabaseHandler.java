package com.ahmed.usuf.billingdesign.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmed.usuf.billingdesign.data.TrasactionDetails;
import com.ahmed.usuf.billingdesign.utili.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/14/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    public DatabaseHandler(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DETAILS_TABLE = "CREATE TABLE " + AppConstants.TABLE_TRANSACTION + "("
                + AppConstants.KEY_ID + " INTEGER PRIMARY KEY," + AppConstants.KEY_FINAL_TOTAL + " TEXT," + AppConstants.KEY_DISCOUNT_TOTAL + " TEXT,"+ AppConstants.KEY_DATE + " TEXT" + ");";
        db.execSQL(CREATE_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_TRANSACTION);
        onCreate(db);
    }

    public void addTransaction(TrasactionDetails txn) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppConstants.KEY_FINAL_TOTAL, txn.getFinalTotal());
        values.put(AppConstants.KEY_DISCOUNT_TOTAL, txn.getDiscountedTotal());
        values.put(AppConstants.KEY_DATE, txn.getDate());
        // Inserting Row
        db.insert(AppConstants.TABLE_TRANSACTION, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Totals and it's Dates
    public List<TrasactionDetails> getAllTransantions() {

        List<TrasactionDetails> txnList = new ArrayList<TrasactionDetails>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + AppConstants.TABLE_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                TrasactionDetails txn = new TrasactionDetails();
                txn.setFinalTotal(Integer.parseInt(cursor.getString(1)));
                txn.setDiscountedTotal(Integer.parseInt(cursor.getString(2)));
                txn.setDate(cursor.getString(3));

                // Adding contact to list
                    txnList.add(txn);

            } while (cursor.moveToNext());
        }

        // return contact list
        return txnList;
    }


    public List<TrasactionDetails> getTransactionOn(String date) {
        List<TrasactionDetails> txnList = new ArrayList<TrasactionDetails>();
        String selectQuery = "SELECT  * FROM " + AppConstants.TABLE_TRANSACTION +" WHERE "+ AppConstants.KEY_DATE+"='"+date+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                TrasactionDetails txn = new TrasactionDetails();
                txn.setFinalTotal(Integer.parseInt(cursor.getString(1)));
                txn.setDiscountedTotal(Integer.parseInt(cursor.getString(2)));
                txn.setDate(cursor.getString(3));
                txnList.add(txn);
            } while (cursor.moveToNext());
        }
        return txnList;
    }

    public int getTxnCount() {
        String countQuery = "SELECT  * FROM " + AppConstants.TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
