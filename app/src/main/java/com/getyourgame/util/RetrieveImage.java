package com.getyourgame.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by User on 25/10/2015.
 */
public class RetrieveImage extends AsyncTask<Void, Void, Bitmap> {

        private String url;

        public RetrieveImage(String imageUrl)
        {
            this.url = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(Void... params)
        {
            Bitmap retVal = null;
            try
            {
                URL imageUrl = new URL(url);
                return BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }
            catch (Exception ex)
            {
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            try
            {
                //Do something with the result Bitmap
            }
            catch (Exception e)
            {
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
}
