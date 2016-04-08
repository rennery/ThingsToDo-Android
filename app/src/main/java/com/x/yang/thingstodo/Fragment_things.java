package com.x.yang.thingstodo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yang on 2015/12/18.
 */
public class Fragment_things extends android.support.v4.app.Fragment {
    private ImageView iv_welcome;
    private ListView lv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_main_page,container,false);



        return v;
    }

    public void creatActivity(String code,String value){
        Intent in=new Intent(getActivity(),Fragment_things.class);
        in.putExtra(code,value);
        startActivity(in);
    }

    public String getStartInfo(){
        return getArguments().getString("start");
    }


}
