package com.innazis.continents.continents;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.innazis.continents.continents.adapters.CountriesAdapter;
import com.innazis.continents.continents.data.CountriesContract;
import com.innazis.continents.continents.sync.ContriesSyncUtils;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        CountriesAdapter.CountriesAdapterOnClickHandler {

    private final String TAG = MainActivity.class.getSimpleName();

    public static final String[] MAIN_COUNTRIES_PROJECTION = {
            CountriesContract.CountryEntry.COLUMN_NAME,
            CountriesContract.CountryEntry.COLUMN_REGION,
            CountriesContract.CountryEntry.COLUMN_SUBREGION,
            CountriesContract.CountryEntry.COLUMN_AREA,
            CountriesContract.CountryEntry.COLUMN_FLAG,
            CountriesContract.CountryEntry.COLUMN_ID
    };

    //public static final int INDEX_CONTINENT_NAME = 0;
    public static final int INDEX_COLUMN_NAME = 0;
    public static final int INDEX_COLUMN_REGION = 1;
    public static final int INDEX_COLUMN_SUBREGION = 2;
    public static final int INDEX_COLUMN_AREA = 3;
    public static final int INDEX_COLUMN_FLAG = 4;
    public static final int INDEX_COLUMN_COUNTRY_ID = 5;
    // TODO: add alpha3Code to db and query

    private static final int ID_COUNTRIES_LOADER = 44;

    private CountriesAdapter countriesAdapter;
    private RecyclerView recyclerView;
    private int position = RecyclerView.NO_POSITION;

    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countries);
        getSupportActionBar().setElevation(0f);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_countries);

        loadingIndicator = (ProgressBar) findViewById(R.id.countries_loading_indicator_pb);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        countriesAdapter = new CountriesAdapter(this, this);
        recyclerView.setAdapter(countriesAdapter);

        showLoading();

        getSupportLoaderManager().initLoader(ID_COUNTRIES_LOADER, null, this);

        ContriesSyncUtils.initialize(MainActivity.this);

    }

    // PROGRESS BAR
    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showCountriesDataView() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    // LOADER
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_COUNTRIES_LOADER:
                Uri queryUri = CountriesContract.CountryEntry.CONTENT_URI;

                String sortOrder = CountriesContract.CountryEntry.COLUMN_AREA + " DESC";
                String selection = CountriesContract.CountryEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this,
                        queryUri,
                        MAIN_COUNTRIES_PROJECTION,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        countriesAdapter.swapCursor(data);
        if (position == RecyclerView.NO_POSITION) position = 0;
        recyclerView.smoothScrollToPosition(position);
        if (null != data && data.getCount() != 0) showCountriesDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        countriesAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String id) {
        Intent detailIntent = new Intent(MainActivity.this, DetailsActivity.class);
        Uri uriForDateClicked = CountriesContract.CountryEntry.buildCountryUriWithID(id);
        detailIntent.setData(uriForDateClicked);
        startActivity(detailIntent);
    }


}
