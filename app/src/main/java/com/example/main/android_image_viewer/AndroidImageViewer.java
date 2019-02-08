package com.example.main.android_image_viewer;
import android.app.Application;

import timber.log.Timber;

public class AndroidImageViewer extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
