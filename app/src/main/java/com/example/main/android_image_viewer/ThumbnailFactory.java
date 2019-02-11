package com.example.main.android_image_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import timber.log.Timber;

import static java.lang.Boolean.valueOf;

public final class ThumbnailFactory {

    public void resizeFileInDirectory( File targetDirectory ) {
        File currentDirectory = new File( targetDirectory.getPath() );
        File[] pictureFiles = currentDirectory.listFiles( new FileTypeFilter() );

        for(File f : pictureFiles){
            Timber.i(f.getPath());
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        Bitmap bitmap = BitmapFactory.decodeFile( pictureFiles[0].getPath() , options);

        Context context = AndroidImageViewer.getAppContext();
        File file = new File( context.getFilesDir().getPath() + "/" + "testImage.jpg" );
        File internalStorage = context.getFilesDir();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG , 80 ,  fileOutputStream);
            fileOutputStream.close();

        }catch (Exception e){
            Timber.i("Error" + e.toString());
        }

    }

    class FileTypeFilter implements FilenameFilter {
        public boolean accept(File file , String name){
            if(name.endsWith(".jpg")){ return true; }
            return false;
        }
    }
}

