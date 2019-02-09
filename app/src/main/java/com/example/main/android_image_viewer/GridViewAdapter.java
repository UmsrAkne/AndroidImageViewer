package com.example.main.android_image_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public final class GridViewAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private int layoutId;
    private List<String> imagePaths;

    GridViewAdapter(Context context , int layoutId , List<String> imagePaths){
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        this.imagePaths = imagePaths;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent){
        String imageFilePath = imagePaths.get(position);
        if(convertView == null){
            convertView = inflater.inflate(layoutId , parent , false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath , options);
        imageView.setImageBitmap(bitmap);

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

}
