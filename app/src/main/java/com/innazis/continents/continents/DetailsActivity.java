package com.innazis.continents.continents;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.innazis.continents.continents.data.CountriesContract;

/**
 * Created by inna on 30/09/2017.
 */

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] COUNTRY_DETAILS_PROJECTION = {
            CountriesContract.CountryEntry.COLUMN_NAME,
            CountriesContract.CountryEntry.COLUMN_REGION,
            CountriesContract.CountryEntry.COLUMN_SUBREGION,
            CountriesContract.CountryEntry.COLUMN_POPULATION,
            CountriesContract.CountryEntry.COLUMN_AREA,
            CountriesContract.CountryEntry.COLUMN_CURRENCIES,
            CountriesContract.CountryEntry.COLUMN_LANGUAGES,
            CountriesContract.CountryEntry.COLUMN_FLAG,
            CountriesContract.CountryEntry.COLUMN_ID
    };

    public static final int INDEX_COLUMN_NAME = 0;
    public static final int INDEX_COLUMN_REGION = 1;
    public static final int INDEX_COLUMN_SUBREGION = 2;
    public static final int INDEX_COLUMN_POPULATION = 3;
    public static final int INDEX_COLUMN_AREA = 4;
    public static final int INDEX_COLUMN_CURRENCIES = 5;
    public static final int INDEX_COLUMN_LANGUAGES = 6;
    public static final int INDEX_COLUMN_FLAG = 7;
    public static final int INDEX_COLUMN_ID = 8;

    private static final int ID_DETAIL_LOADER = 353;

    private String countrySummary;
    private Uri uri;

    ImageView flagImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_details);

        flagImageView = (ImageView) findViewById(R.id.extra_details_flag_wv);
        uri = getIntent().getData();
        if (uri == null) throw new NullPointerException("NULL URI for DetailsActivity");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {

            case ID_DETAIL_LOADER:

                //String selection = CountriesContract.CountryEntry.getSqlSelectForCountryById();
                return new CursorLoader(this,
                        uri,
                        COUNTRY_DETAILS_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        String flag = data.getString(INDEX_COLUMN_FLAG);
        Glide.with(this).load(flag).into(flagImageView);
        //int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
