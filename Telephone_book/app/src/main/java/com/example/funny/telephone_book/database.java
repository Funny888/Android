package com.example.funny.telephone_book;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class database extends SQLiteOpenHelper {

    public static final String NAME_DATABASE = "Telephone";
    public static final String NAME_DATATABLE = "telephone";
    public static final int NAME_DATAVERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String NAME = "name";
    public static final String LASTNAME = "lastname";
    public static final String TELEPHONE = "telephone";
    public static final String LINK_PHOTO = "link_photo" ;

    public database (Context context)
    {
        super(context,NAME_DATABASE,null,NAME_DATAVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NAME_DATATABLE + " ( " + KEY_ID + " integer primary key autoincrement, " + NAME
        + " text, " + LASTNAME + " text, " + TELEPHONE + " text, " + LINK_PHOTO + " text )" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table IF EXISTS " + NAME_DATATABLE);
        onCreate(db);
    }
}
