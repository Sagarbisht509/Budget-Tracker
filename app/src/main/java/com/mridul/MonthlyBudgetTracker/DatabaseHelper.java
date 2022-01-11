package com.mridul.MonthlyBudgetTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    public static final String DatabaseName = "MyDB";

    public static final String tableName = "Expense";
    public static final String c_id = "id";
    public static final String c_title = "title";
    public static final String c_amount = "amount";
    public static final String c_date = "date";
    public static final String c_income = "income";
    public static final String c_expense = "expense";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseName, null, 1);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = "CREATE TABLE " + tableName + " (" +
                c_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                c_title + " TEXT, " +
                c_amount + " TEXT, " +
                c_date + " TEXT, " +
                c_income + " INTEGER, " +
                c_expense + " INTEGER);";

        db.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String q = "DROP TABLE IF EXISTS " + tableName;

        db.execSQL(q);

        onCreate(db);
    }

    public void addTransactions(String title, String amount, String date, String from) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(c_title, title);
        contentValues.put(c_date, date);
        contentValues.put(c_amount, amount);

        if (from.equals("income")) {
            contentValues.put(c_income, Long.parseLong(amount));
            contentValues.put(c_expense, (0));
        } else {
            contentValues.put(c_income, 0);
            contentValues.put(c_expense, Long.parseLong(amount));
        }

        long result = database.insert(tableName, null, contentValues);

        getStatus(result);
    }

    public Cursor getAllTransactions() {
        String q = "SELECT * FROM Expense";

        // String s = "SELECT * FROM Expense ORDER BY column_1 ASC, column_2 DESC";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(q, null);
        }

        return cursor;
    }

    public Cursor getAllIncome() {
        String q = "SELECT * FROM " + tableName + " WHERE income >" + 0;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(q, null);
        }

        return cursor;
    }

    public Cursor getAllExpense() {
        String q = "SELECT * FROM " + tableName + " WHERE expense >" + 0;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(q, null);
        }

        return cursor;
    }

    public String getTotalIncome() {
        SQLiteDatabase database = this.getReadableDatabase();

        long sum = 0;
        Cursor cursor = database.rawQuery("SELECT * FROM Expense", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sum += (cursor.getLong(cursor.getColumnIndex(c_income)));
            cursor.moveToNext();
        }

        return String.valueOf(sum);
    }

    public String getTotalExpense() {
        SQLiteDatabase database = this.getReadableDatabase();

        long sum = 0;
        Cursor cursor = database.rawQuery("SELECT * FROM Expense", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sum += (cursor.getLong(cursor.getColumnIndex(c_expense)));
            cursor.moveToNext();
        }

        return String.valueOf(sum);
    }

    public String getTotal() {
        return String.valueOf((Math.abs(Integer.parseInt(getTotalExpense()) - Integer.parseInt(getTotalIncome()))));
    }

    public int delete(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(tableName, "id=?", new String[]{id});
    }

    public void update(String id, String title, String amount, long income, long expense) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(c_title, title);
        contentValues.put(c_amount, amount);

        if (income == 0) {
            contentValues.put(c_income, 0);
            contentValues.put(c_expense, Long.parseLong(amount));
        } else {
            contentValues.put(c_income, (Long.parseLong(amount)));
            contentValues.put(c_expense, 0);
        }

        long result = database.update(tableName, contentValues, "id=?", new String[]{id});

        getStatus(result);
    }

    private void getStatus(long result) {
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + tableName);
        database.close();

        return true;
    }

}
