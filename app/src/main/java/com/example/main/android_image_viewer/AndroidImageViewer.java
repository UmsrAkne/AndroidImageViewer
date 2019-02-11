package com.example.main.android_image_viewer;
import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class AndroidImageViewer extends Application {

    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        AndroidImageViewer.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return AndroidImageViewer.context;
    }

}
