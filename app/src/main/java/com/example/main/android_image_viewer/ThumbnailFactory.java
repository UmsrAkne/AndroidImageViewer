package com.example.main.android_image_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

import timber.log.Timber;

import static java.lang.Boolean.valueOf;

public final class ThumbnailFactory {

    public void resizeFileInDirectory( File targetDirectory ) {
        File currentDirectory = new File( targetDirectory.getPath() );
        File[] pictureFiles = currentDirectory.listFiles( new FileTypeFilter() );

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ArrayList<BitmapAndName> bitmaps = new ArrayList<>();

        for(File f:pictureFiles){
            Bitmap bmp = BitmapFactory.decodeFile( f.getPath() , options );
            BitmapAndName bitmapAndName = new BitmapAndName();
            bitmapAndName.bitmap = bmp;
            bitmapAndName.fileName = f.getName();
            bitmaps.add( bitmapAndName );
        }

        Context context = AndroidImageViewer.getAppContext();

        try {
            for(BitmapAndName bmp : bitmaps){
                File destination = new File( context.getFilesDir().getPath() + "/" + bmp.fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(destination);
                bmp.bitmap.compress(Bitmap.CompressFormat.JPEG , 50 ,  fileOutputStream);
                fileOutputStream.close();
            }

        }catch (Exception e){
            Timber.i("Error" + e.toString());
        }

    }

    private class BitmapAndName{
        public String fileName;
        public Bitmap bitmap;
    }

    class FileTypeFilter implements FilenameFilter {
        public boolean accept(File file , String name){
            if(name.endsWith(".jpg")){ return true; }
            return false;
        }
    }
}

