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
        File[] pictureFiles = getPictureFiles( targetDirectory );
        AsyncBitmapDecoder task = new AsyncBitmapDecoder(false);
        task.execute( pictureFiles );
    }

    private File[] getPictureFiles( File targetDirectory ){
        File currentDirectory = new File( targetDirectory.getPath() );
        return currentDirectory.listFiles( new PictureFileExtraction() );
    }

    private class NamedBitmap{
        public String fileName;
        public Bitmap bitmap;
    }

    public File getThumbnailFile( File sourcePictureFile ){
        Context context = AndroidImageViewer.getAppContext();
        String thumbnailDirectoryPath = context.getFilesDir().getPath() + "/Thumbnails";
        return new File( thumbnailDirectoryPath + sourcePictureFile.getPath());
    }

    private class PictureFileExtraction implements FilenameFilter {
        public boolean accept(File file , String name){
            if(name.endsWith(".jpg")){ return true; }
            return false;
        }
    }

    private class AsyncBitmapDecoder extends android.os.AsyncTask< File , Void ,  Boolean>{

        private Boolean isDoOverwrite = false;

        public AsyncBitmapDecoder( boolean setIsDoOverwrite ) {
            isDoOverwrite = setIsDoOverwrite;
        }

        @Override
        protected Boolean doInBackground(File... pictureFiles){

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            ArrayList<NamedBitmap> namedBitmaps = new ArrayList<>();

            Context context = AndroidImageViewer.getAppContext();

            for(File f:pictureFiles){
                if(!isDoOverwrite) {
                    //後にJpegを出力する場所と同じ場所を調べ、もしファイルが存在すればコンティニューする。
                    //コンティニューした場合 bitmapsに要素は入らず、次のループで bitmap.compress もされない。
                    File saveDestination = new File(context.getFilesDir().getPath() + "/" + f.getName());
                    if (saveDestination.exists()) continue;
                }

                Bitmap bmp = BitmapFactory.decodeFile( f.getPath() , options );
                NamedBitmap bitmapAndName = new NamedBitmap();
                bitmapAndName.bitmap = bmp;
                bitmapAndName.fileName = f.getName();
                namedBitmaps.add( bitmapAndName );
            }

            try {
                for(NamedBitmap bmp : namedBitmaps){
                    File destination = new File( context.getFilesDir().getPath() + "/" + bmp.fileName);
                    FileOutputStream fileOutputStream = new FileOutputStream(destination);
                    bmp.bitmap.compress(Bitmap.CompressFormat.JPEG , 30 ,  fileOutputStream);
                    fileOutputStream.close();
                }

            }catch (Exception e){
                Timber.i("Error" + e.toString());
            }

            return true;
        }
    }
}

