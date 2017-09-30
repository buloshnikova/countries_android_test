package com.innazis.continents.continents.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.innazis.continents.continents.data.CountriesContract.CountryEntry;

/**
 * Created by inna on 29/09/2017.
 */

public class CountriesDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "weather.db";

    private static final int DATABASE_VERSION = 3;

    public CountriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + CountryEntry.TABLE_NAME + " (" +


                        CountryEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        CountryEntry.COLUMN_DATE       + " INTEGER NOT NULL, "                  +

                        CountryEntry.COLUMN_NAME       + " TEXT NOT NULL,"                      +

                        CountryEntry.COLUMN_REGION     + " TEXT NOT NULL, "                     +
                        CountryEntry.COLUMN_SUBREGION  + " TEXT, "                              +

                        CountryEntry.COLUMN_POPULATION + " INTEGER NOT NULL, "                  +
                        CountryEntry.COLUMN_AREA       + " INTEGER NOT NULL, "                  +

                        CountryEntry.COLUMN_CURRENCIES + " TEXT, "                              +
                        CountryEntry.COLUMN_LANGUAGES  + " TEXT, "                              +
                        CountryEntry.COLUMN_FLAG       + " TEXT, "                              +


                        " UNIQUE (" + CountryEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CountryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
