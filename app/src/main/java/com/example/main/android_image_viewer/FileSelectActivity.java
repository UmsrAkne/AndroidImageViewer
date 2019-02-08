package com.example.main.android_image_viewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import timber.log.Timber;


public class FileSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        permissionRequest();
    private void permissionRequest(){
        String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE };
        final int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE , REQUEST_EXTERNAL_STORAGE);
        }
    }
}
