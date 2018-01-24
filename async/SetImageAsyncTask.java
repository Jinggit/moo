package com.moocall.moocall.async;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;
import com.moocall.moocall.util.Utils;
import java.util.HashMap;

public class SetImageAsyncTask extends AsyncTask<String, String, Bitmap> {
    private Activity activity;
    private ImageView imageView;
    private HashMap<String, Bitmap> images;
    private String position;

    public SetImageAsyncTask(Activity activity, ImageView imageView, HashMap<String, Bitmap> images, String position) {
        this.activity = activity;
        this.imageView = imageView;
        this.images = images;
        this.position = position;
    }

    protected Bitmap doInBackground(String... params) {
        byte[] decodedString = Base64.decode(params[0], 0);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (bitmapImage != null) {
            return Utils.getResizedBitmap(bitmapImage, this.activity, Integer.parseInt(params[1]));
        }
        return bitmapImage;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            this.imageView.setImageBitmap(result);
            if (this.images != null && this.position != null) {
                this.images.put(this.position, result);
            }
        }
    }
}
