package com.x.yang.thingstodo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**

 */
public class fragment_message extends android.support.v4.app.Fragment {

    private LinearLayout ll;
    DetailReceiver dr;
    InnerReceiver ir;



    public fragment_message() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dr =new DetailReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.x.yang.thingstodo.DETAILS");
        this.getActivity().registerReceiver(dr, filter);
        ir =new InnerReceiver();
        IntentFilter filter2=new IntentFilter();
        filter2.addAction("com.x.yang.thingstodo.ACTION");
        this.getActivity().registerReceiver(ir, filter2);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_layout_message, container, false);
        ll = (LinearLayout)v.findViewById(R.id.whole);
        v.setVisibility(View.INVISIBLE);
        return v;
    }


    private class DetailReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
    private class InnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
