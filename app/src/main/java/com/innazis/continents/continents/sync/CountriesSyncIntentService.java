package com.innazis.continents.continents.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by inna on 28/09/2017.
 */

public class CountriesSyncIntentService extends IntentService {

    public CountriesSyncIntentService() {
        super("CountriesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CountriesSyncTask.syncCountries(this);
    }
}
