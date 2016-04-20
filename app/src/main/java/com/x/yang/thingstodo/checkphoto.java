package com.x.yang.thingstodo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class checkphoto extends AppCompatActivity {
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkphoto);
        Bundle b = getIntent().getExtras();
        String src = b.getString("photo");
        ImageView im = (ImageView)findViewById(R.id.check_photo);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(src );
              bitmap= BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {

        }
        im.setImageBitmap(bitmap);
    }
}
