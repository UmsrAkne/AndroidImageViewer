package com.example.main.android_image_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import timber.log.Timber;

public final class GridViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutId;
    private List<String> imagePaths;
    private final String thumbnailsDirectoryPath;

    GridViewAdapter(Context context , int layoutId , List<String> imagePaths){
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.imagePaths = imagePaths;

        thumbnailsDirectoryPath = context.getFilesDir().getPath() + "/thumbnails";

        if(!new File(thumbnailsDirectoryPath).exists()){
            new File(thumbnailsDirectoryPath).mkdirs();
        }
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent){

        String imageFilePath = imagePaths.get(position);
        if(convertView == null){
            convertView = inflater.inflate(layoutId , parent , false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);

        File imageFile = new File( imageFilePath );

        if(!imageFile.isDirectory()){
            //サムネファイルは軽量に作ってはあるものの、
            //デコードの際に僅かなカクつきが出る程度にはパフォーマンスに影響が出る。
            //そのため、サムネの存在確認とデコードの両方を別スレッドにて行う。
            AsyncBitmapDecode asyncBitmapDecode = new AsyncBitmapDecode(imageView);
            asyncBitmapDecode.execute(imageFilePath);
        }else{
            //ディレクトリだった場合は固有の画像をセットする予定だが、今は用意していない。
            //画像ファイルに比べると数が少なく、動作への影響は少ないと思うが、
            //重くなったりするようなら別スレッドに投げることも考える。
            imageView.setImageBitmap( Bitmap.createBitmap(1,1,Bitmap.Config.RGB_565) );
        }

        return convertView;
    }

    @Override
    public int getCount(){
        return imagePaths.size();
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    private class AsyncBitmapDecode extends AsyncTask<String, Void, Bitmap>{

        private final ImageView view;

        public AsyncBitmapDecode( ImageView view ){
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... picturePath) {

            Timber.i(picturePath[0]);

            //サムネイルファイルの保存先オブジェクト作成
            File imageFile = new File( picturePath[0] );

            File thumbnailFile =
                new File( thumbnailsDirectoryPath + "/" + imageFile.getName() );

            if(thumbnailFile.exists()){
                Timber.i("キャッシュ使用");
                Bitmap thumbnailBmp = BitmapFactory.decodeFile(thumbnailFile.getPath() );
                return thumbnailBmp;
            }

            //オプションを使ってデコードしてサムネを出力。ここから下の処理は重い。
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 16;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath[0] , options);

            try {
                FileOutputStream fos = new FileOutputStream(thumbnailFile);
                bitmap.compress( Bitmap.CompressFormat.JPEG , 50 , fos );
                fos.close();
            }catch(Exception e){
                Timber.i(e.toString());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //処理完了時にこのメソッドがコールされる。
            view.setImageBitmap( result );
        }
    }

}
