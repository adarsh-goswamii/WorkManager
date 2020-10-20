package com.example.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SampleWorker extends Worker
{
    public static final String WORKER_KEY= "Number";
    public static final String TAG="Sample worker";
    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork started");
        Data inputData= getInputData();
        int number = inputData.getInt(WORKER_KEY, -1);
        if(number!= -1)
        {
            for(int i=0;i<number;i++)
            {
                Log.d(TAG, "do word i is: "+i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Result.failure();
                }
            }
        }
        return Result.success();
    }
}
/**
 * Job scheduler works fine for background tasks but it requires a API version 21 or above
 * Work Manager on the other hand is compatible with API 14 and above.
 *
 * We dont need AsyncTask as work manager does not blocks the user from interacting with user interface.
 * Result data type is returned by the function doWork.
 * (i). Result.failure()
 * (ii). Result.Success()
 * (iii). Result.retry()
 *
 * All of them are quite simple to understand by their name so i wont bother to tell about them.
 */
