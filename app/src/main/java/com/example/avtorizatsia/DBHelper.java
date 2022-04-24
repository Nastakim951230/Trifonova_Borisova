package com.example.avtorizatsia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "contactDb";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_SOTRYDNIK="sotrydnik";

    public static final String KEY_IDN = "_id";
    public static final String KEY_USER = "user";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_ID = "_id";
    public static final String KEY_TOVAR = "tovar";
    public static final String KEY_KOLVO = "kolvo";
    public static final String KEY_PRICE = "price";

    public static final String KEY_IDs = "_id";
    public static final String KEY_NAME="name";
    public static final String KEY_FAMILIA="familia";
    public static final String KEY_OTHESTVO="othestvo";
    public static final String KEY_USERs = "user";
    public static final String KEY_PASSWORDs = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_USERS + "(" + KEY_IDN + " integer primary key," + KEY_USER + " text," + KEY_PASSWORD + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key,"  + KEY_TOVAR + " text," + KEY_KOLVO + " text," +  KEY_PRICE + " text" + ")");

        db.execSQL("create table " + TABLE_SOTRYDNIK+"("+KEY_IDs+"integer primary key,"+KEY_NAME+"text,"+KEY_FAMILIA+"text,"+KEY_OTHESTVO+"text,"+KEY_USERs+"text,"+KEY_PASSWORDs+"text"+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion > newVersion){
            db.execSQL("drop table if exists " + TABLE_CONTACTS);
            onCreate(db);

            db.execSQL("drop table if exists " + TABLE_USERS);
            onCreate(db);

            db.execSQL("drop table if exists "+ TABLE_SOTRYDNIK);
            onCreate(db);
        }

    }
}
