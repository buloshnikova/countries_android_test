package com.innazis.continents.continents.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.innazis.continents.continents.data.CountriesContract;
import com.innazis.continents.continents.utils.NetworkUtils;
import com.innazis.continents.continents.utils.OpenJsonUtils;

import java.net.URL;

/**
 * Created by inna on 28/09/2017.
 */

public class CountriesSyncTask {

    synchronized public static void syncCountries(Context context) {

        try {

            URL requestUrl = NetworkUtils.buildUrlForAll();

            String jsonCountriesResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

            ContentValues[] countriesValues = OpenJsonUtils
                    .getContentValuesFromJson(context, jsonCountriesResponse);

            if (countriesValues != null && countriesValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(
                        CountriesContract.CountryEntry.CONTENT_URI,
                        null,
                        null);
                try {
                    contentResolver.bulkInsert(
                            CountriesContract.CountryEntry.CONTENT_URI,
                            countriesValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
