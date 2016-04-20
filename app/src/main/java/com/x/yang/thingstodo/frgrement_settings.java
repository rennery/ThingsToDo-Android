package com.x.yang.thingstodo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class frgrement_settings extends android.support.v4.app.Fragment {

    String src="";
    boolean opened = false;
    TextView t;
    Button b;

    public frgrement_settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_layout_settings, container, false);
        t = (TextView)v.findViewById(R.id.about);
        t.setText("Thanks to use my app! \n" +
                "You can add to do event and this app will alarm you according to location and time\n" +
                "If you have any suggestions or comments, please send email to my address\n" +
                "yxiao4@lakeheadu.ca");
        b = (Button)v.findViewById(R.id.about_but);
        b.setOnClickListener(new ccListener());
        return v;
    }


    private class ccListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(opened == false){
                opened = true;
                Log.i("setting","herererre");
                src = "Thanks to use my app! \n" +
                        "You can add to do event and this app will alarm you according to location and time\n" +
                        "If you have any suggestions or comments, please send email to my address\n" +
                        "yxiao4@lakeheadu.ca";
            }else{
                opened = false;
                src = "";
            }
        }
    }

}
