package com.innazis.continents.continents.utils;

import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.innazis.continents.continents.data.CountriesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by inna on 28/09/2017.
 */

public class OpenJsonUtils {

    private static final String JSON_NAME = "name";
    private static final String JSON_REGION = "region";
    private static final String JSON_SUBREGION = "subregion";
    private static final String JSON_POPULATION = "population";
    private static final String JSON_AREA = "area";
    private static final String JSON_CURRENCIES = "currencies";
    private static final String JSON_LANGUAGES = "languages";
    private static final String JSON_FLAG = "flag";

    public static ContentValues[] getContentValuesFromJson(Context context, String jsonStr)
            throws JSONException {

        //JSONObject forecastJson = new JSONObject(jsonStr);

        JSONArray jsonCountriesArray = new JSONArray(jsonStr);


        ContentValues[] contentValues = new ContentValues[jsonCountriesArray.length()];

        long normalizedUtcStartDay = CountriesDateUtils.getNormalizedUtcDateForToday();

        for (int i = 0; i < jsonCountriesArray.length(); i++) {

            long dateTimeMillis;
            String name;
            String region;
            String subregion;
            int population;
            int area;
            String currencies;
            String languages;
            String flag;

            dateTimeMillis = normalizedUtcStartDay + DateUtils.DAY_IN_MILLIS * i;

            JSONObject sortedJO = jsonCountriesArray.getJSONObject(i);

            name = sortedJO.optString(JSON_NAME, "not specified");
            region = sortedJO.optString(JSON_REGION, "not specified");
            subregion = sortedJO.optString(JSON_SUBREGION, "not specified");
            population = sortedJO.optInt(JSON_POPULATION, 0);
            area = sortedJO.optInt(JSON_AREA, 0);

            // should be a loop to run through the whole array of currencies
            currencies = sortedJO.getJSONArray(JSON_CURRENCIES).get(0).toString();
            // same as currencies
            languages = sortedJO.getJSONArray(JSON_LANGUAGES).get(0).toString();
            flag = sortedJO.getString(JSON_FLAG);

            //TODO: try this later
            //JSONObject currencybject = sortedJO.getJSONArray(JSON_CURRENCIES).getJSONObject(0);

            ContentValues countryValues = new ContentValues();
            countryValues.put(CountriesContract.CountryEntry.COLUMN_DATE, dateTimeMillis);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_NAME, name);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_REGION, region);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_SUBREGION, subregion);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_POPULATION, population);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_AREA, area);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_CURRENCIES, currencies);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_LANGUAGES, languages);
            countryValues.put(CountriesContract.CountryEntry.COLUMN_FLAG, flag);

            contentValues[i] = countryValues;
        }

        return contentValues;
    }
}
