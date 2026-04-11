package com.example.ex11042;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data_base_expenses.db";
    private static final int DATABASE_VERSION = 1;
    String strCreate, strDelete;

    public HelperDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        strCreate = "CREATE TABLE " + Expenses.TABLE_EXPENSES;
        strCreate += " (" + Expenses.KEY_ID + " INTEGER PRIMARY KEY,";
        strCreate += " " + Expenses.DESCRIPTION + " TEXT,";
        strCreate += " " + Expenses.AMOUNT + " REAL,";
        strCreate += " " + Expenses.CATEGORY + " TEXT,";
        strCreate += " " + Expenses.DATE + " TEXT";
        strCreate += ");";
        sqLiteDatabase.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        strDelete = "DROP TABLE IF EXISTS " + Expenses.TABLE_EXPENSES;
        sqLiteDatabase.execSQL(strDelete);

        onCreate(sqLiteDatabase);
    }
}
