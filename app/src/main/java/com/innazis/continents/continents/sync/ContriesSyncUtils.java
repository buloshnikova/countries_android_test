package com.innazis.continents.continents.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.innazis.continents.continents.data.CountriesContract;

import java.util.concurrent.TimeUnit;


/**
 * Created by inna on 28/09/2017.
 */

public class ContriesSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String COUNTRIES_SYNC_TAG = "countries-sync";

    // schedule a repeating sync with firebaseJobDispatcher
    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Job to periodically sync
        Job syncCountriesJob = dispatcher.newJobBuilder()
                .setService(CountriesFirebaseJobService.class)
                // unique tag identifier
                .setTag(COUNTRIES_SYNC_TAG)
                // network constraints to choose on which network to run the job
                .setConstraints(Constraint.ON_ANY_NETWORK)
                // can be changed to forever
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                // time interval to sync is set to 24 hours
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                // replace the old job, if exists, with this new
                .setReplaceCurrent(true)
                // call the builder to return the job
                .build();

        // schedule the job
        dispatcher.schedule(syncCountriesJob);
    }

    // create sync tasks and checks if an immediate sync is required
    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;

        sInitialized = true;

        // a trigger to create sync task
        scheduleFirebaseJobDispatcherSync(context);

        // to run not at UI thread
        Thread checkForEmpty = new Thread(new Runnable() {

            @Override
            public void run() {
                Uri countriesQueryUri = CountriesContract.CountryEntry.CONTENT_URI;
                String[] projectionColumns = {CountriesContract.CountryEntry._ID};
                String selectionStatement = CountriesContract.CountryEntry
                        .getSqlSelectForTodayOnwards();

                Cursor cursor = context.getContentResolver().query(
                        countriesQueryUri,
                        projectionColumns,
                        selectionStatement,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                if (null != cursor) {
                    cursor.close();
                }

            }
        });
        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, CountriesSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
