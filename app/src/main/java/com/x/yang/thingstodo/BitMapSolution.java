package com.x.yang.thingstodo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by Yang on 2015/12/21.
 */
public class BitMapSolution {

    public static Bitmap bitmapScale(String path, int aim_wight, int aim_hight){
        Bitmap bt=null;
        float file_wight,file_hight;
        int size=1;

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);
        file_hight=options.outHeight;
        file_wight=options.outWidth;
        if(file_hight>=aim_hight&&file_wight>=aim_wight){
            if(file_hight>=file_wight){
                size=Math.round(file_hight/aim_hight);
            }else{
                size=Math.round(file_wight/aim_wight);

            }
        }
        options=new BitmapFactory.Options();
        options.inSampleSize=size;
        bt=BitmapFactory.decodeFile(path,options);


        return bt;
    }

    public static Bitmap bitmap_scaleToscreen(Bitmap original,float weight, float heigh){
        Matrix m=new Matrix();
        m.postScale(weight,heigh);
        Bitmap newbit=Bitmap.createBitmap(original,0,0,(int)weight,(int)heigh);
        return newbit;
    }
}
