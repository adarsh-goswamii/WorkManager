package com.example.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.workmanager.SampleWorker.TAG;
import static com.example.workmanager.SampleWorker.WORKER_KEY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWorker();
    }

    private void initWorker()
    {
        /**
         * There are cases when we want to pass data to our background task for that we use the Data
         * that comes from androidx.work
         *
         * Many times background tasks requires some prerequisites like internet connection or maybe
         * the battery should be not low and so on. We can define that from Constraints.
         */

        Data data= new Data.Builder()
                .putInt(WORKER_KEY, 10)
                .build();

        Constraints constraints= new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
        //        .setRequiresCharging(true)
        //        .setRequiresStorageNotLow(true)
                .build();

        /**
         * We can create our request in two different ways
         * (i). One time work request: which will get executed once
         * (ii). Periodically: and we can set time lets say if we want some task to get
         * done every day or maybe after several hours for that purposes we have this work request.
         *
         * there are various setters some of them are given below
         * initial delay will make our background task to run after the defined time.
         */

        OneTimeWorkRequest downloadRequest= new OneTimeWorkRequest.Builder(SampleWorker.class)
        //        .setInitialDelay(5, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("download")
                .build();

        //WorkManager.getInstance(this).enqueue(downloadRequest);

        PeriodicWorkRequest downloadRequest2= new PeriodicWorkRequest.Builder(SampleWorker.class, 1, TimeUnit.DAYS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("Daily notification")
                .build();

        WorkManager.getInstance(this).enqueue(downloadRequest2);

        WorkManager.getInstance(this).getWorkInfosByTagLiveData("Daily notification").observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                for(WorkInfo i: workInfos)
                    Log.d(TAG, "work status "+i.getState());
            }
        });

        //WorkManager.getInstance(this).cancelWorkById(downloadRequest2.getId());
    }
}