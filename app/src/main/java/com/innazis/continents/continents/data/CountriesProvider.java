package com.innazis.continents.continents.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.innazis.continents.continents.utils.CountriesDateUtils;

/**
 * Created by inna on 28/09/2017.
 */

public class CountriesProvider extends ContentProvider {
    public static final int CODE_COUNTRIES = 100;
    public static final int CODE_COUNTRY_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CountriesDbHelper _openHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CountriesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CountriesContract.PATH_COUNTRIES, CODE_COUNTRIES);

        matcher.addURI(authority, CountriesContract.PATH_COUNTRIES + "/#", CODE_COUNTRY_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        _openHelper = new CountriesDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = _openHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_COUNTRIES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long weatherDate =
                                value.getAsLong(CountriesContract.CountryEntry.COLUMN_DATE);
                        if (!CountriesDateUtils.isDateNormalized(weatherDate)) {
                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }

                        long _id = db.insert(CountriesContract.CountryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_COUNTRY_WITH_ID: {

                String normalizedID = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{normalizedID};

                cursor = _openHelper.getReadableDatabase().query(

                        CountriesContract.CountryEntry.TABLE_NAME,
                        projection,
                        CountriesContract.CountryEntry.COLUMN_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }


            case CODE_COUNTRIES: {
                cursor = _openHelper.getReadableDatabase().query(
                        CountriesContract.CountryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_COUNTRIES:
                numRowsDeleted = _openHelper.getWritableDatabase().delete(
                        CountriesContract.CountryEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new RuntimeException(
                "We are not implementing insert in Sunshine. Use bulkInsert instead");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in Sunshine");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        _openHelper.close();
        super.shutdown();
    }

}
