package com.silencedut.dogearedtextview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * @author SilenceDut
 * @date 2018/9/17
 */
public class DogEaredHelper {

    public static Bitmap drawableToBitmap(Drawable drawable,int width,int height) {
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0,width , height);
        drawable.draw(canvas);
        return bitmap;
    }


}
