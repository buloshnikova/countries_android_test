package com.innazis.continents.continents.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.innazis.continents.continents.utils.CountriesDateUtils;

/**
 * Created by inna on 27/09/2017.
 */

public class CountriesContract {

    public static final String CONTENT_AUTHORITY = "com.innazis.continents.continents";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CONTINENTS = "continents";

    public static final String PATH_COUNTRIES = "countries";

    // inner class with the table contents of countries table
    public static final class CountryEntry implements BaseColumns {

        // CONTENT_URI queries the countries table from the content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_COUNTRIES)
                .build();

        // name of the table
        public static final String TABLE_NAME = "countries";

        // columns
        // to write a last update date
        public static final String COLUMN_DATE = "date";
        // data columns
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_REGION = "region";
        public static final String COLUMN_SUBREGION = "subregion";
        public static final String COLUMN_POPULATION = "population";
        public static final String COLUMN_AREA = "area";

        public static final String COLUMN_CURRENCIES = "currencies";
        public static final String COLUMN_LANGUAGES = "languages";

        public static final String COLUMN_FLAG = "flag";

        public static final String COLUMN_ID = "_ID";

        public static Uri buildCountryUriWithID(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }

        // returns the selection part of the query
        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = CountriesDateUtils.normalizeDate(System.currentTimeMillis());
            return CountriesContract.CountryEntry.COLUMN_DATE + " >= " + normalizedUtcNow;
        }

        public static String getSqlSelectForCountryById(String countryId){
            return CountryEntry.COLUMN_ID + " == " + countryId;
        }
    }
}
