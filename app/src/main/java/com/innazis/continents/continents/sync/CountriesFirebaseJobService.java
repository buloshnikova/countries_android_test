package com.innazis.continents.continents.sync;

/**
 * Created by inna on 28/09/2017.
 */
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class CountriesFirebaseJobService extends JobService{

    private AsyncTask<Void, Void, Void> fetchCountriesTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters){

        fetchCountriesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Context context = getApplicationContext();
                CountriesSyncTask.syncCountries(context);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };

        fetchCountriesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
