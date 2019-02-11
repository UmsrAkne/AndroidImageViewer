package com.example.main.android_image_viewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class FileSelectActivity extends AppCompatActivity {

    private List<String> imageFilePaths = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        permissionRequest();

        ThumbnailFactory tf = new ThumbnailFactory();
        tf.resizeFileInDirectory( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        //ストレージ内のピクチャーフォルダのパスを取得
        File picDirectory =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File[] files = picDirectory.listFiles();

        for(File f:files){
            imageFilePaths.add(f.getAbsolutePath());
        }

        createGridView();
    }

    private void createGridView(){
        GridView gridView = findViewById(R.id.fileSelectGrid);
        GridViewAdapter adapter = new GridViewAdapter(this.getApplicationContext() , R.layout.grid_items  , imageFilePaths);
        gridView.setAdapter(adapter);
    }
    

    private void permissionRequest(){
        String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE };
        final int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE , REQUEST_EXTERNAL_STORAGE);
        }
    }
}
