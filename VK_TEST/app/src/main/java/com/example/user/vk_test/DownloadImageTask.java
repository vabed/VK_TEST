package com.example.user.vk_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;


public class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    ImageView bmImage;

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap Icon = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            Icon = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }

        return Icon;
    }

    protected void onPostExecute(Bitmap result){
        bmImage.setImageBitmap(result);
    }
}
