package com.x.yang.thingstodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainPageFragment extends Fragment {

    private ImageView iv_welcome;
    private ListView lv;
    private TextView city1,state1,day;
    Thread mThread;
    Alldata ad;
    BReceiver receiver;
    next_nearReceiver receiver2;
    private String id_near,id_next;
    SimpleDateFormat formatter;
    Date curDate;
    String str;



    public MainPageFragment() {

        formatter  = new  SimpleDateFormat("yy-MMM-dd");
        curDate = new Date(System.currentTimeMillis());
        str = formatter.format(curDate);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_main_page, container, false);
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        //Alldata ad = Alldata.getInstance(this.getActivity());
        city1 = (TextView)v.findViewById(R.id.city);
        ad = Alldata.getInstance(this.getActivity());
        state1 = (TextView)v.findViewById(R.id.state);
        city1.setText(MainPage.city);
        state1.setText(MainPage.state);
        day = (TextView)v.findViewById(R.id.day);
        receiver=new BReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.x.yang.thingstodo.GPSREADY");
        this.getActivity().registerReceiver(receiver, filter);
        receiver2=new next_nearReceiver();
        IntentFilter filter2=new IntentFilter();
        filter2.addAction("com.x.yang.thingstodo.NEXTNEARCHANGE");
        this.getActivity().registerReceiver(receiver2, filter2);



        day.setText(str);


        for (int i = 0; i < 4; i++) {
            if(i == 0){
            Map<String, Object> listem = new HashMap<String, Object>();
            listem.put("head", "You've "+ad.num_today+" need to do today");
            listem.put("name", R.drawable.check);
            listems.add(listem);
            }else if(i == 1){
                Map<String, Object> listem = new HashMap<String, Object>();
                listem.put("head", "Check all listed event");
                listem.put("name", R.drawable.all_list);
                listems.add(listem);
            }else if(i == 2){
                Map<String, Object> listem = new HashMap<String, Object>();
                listem.put("head", "Check the nearest event");
                listem.put("name", R.drawable.nearest);
                listems.add(listem);
            }else if(i == 3){
                Map<String, Object> listem2 = new HashMap<String, Object>();
                listem2.put("head", "The next moment event");
                listem2.put("name", R.drawable.newest);
                listems.add(listem2);
            }
        }



        SimpleAdapter sa = new SimpleAdapter(this.getActivity(),listems,R.layout.main_page_list_layout,new String[] { "head", "name"},
                new int[] {R.id.allthings,R.id.thingimage});
        lv =(ListView)v.findViewById(R.id.li_main);
        lv.setAdapter(sa);
        lv.setOnItemClickListener(new listclick_main());
        Log.i("currenttab", "I am here");
        return v;
    }
    public class BReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle=intent.getExtras();
            city1.setText(MainPage.city);
            state1.setText(MainPage.state);

        }
    }
    public class next_nearReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle=intent.getExtras();
            id_near = bundle.getString("idnear");
            id_next = bundle.getString("idnext");

        }
    }


    @Override
    public void onDestroy() {
        Log.i("kill","killed");
        this.getActivity().unregisterReceiver(receiver);
        this.getActivity().unregisterReceiver(receiver2);
        super.onDestroy();
    }
    private class listclick_main implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView)parent;
            HashMap<String,Object> item = (HashMap<String,Object>)lv.getItemAtPosition(position);
            String tv = item.get("head").toString();
            if(tv == "Check all listed event"){

                Intent i =new Intent("com.x.yang.thingstodo.CHANGE");
                i.putExtra("tab",1);
                MainPageFragment.this.getActivity().sendBroadcast(i);
            }else if(tv.contains("You've ")){
                Intent i =new Intent("com.x.yang.thingstodo.CHANGE");
                i.putExtra("tab",2);
                i.putExtra("action","today event");
                MainPageFragment.this.getActivity().sendBroadcast(i);
            }else if(tv == "Check the nearest event"){

                Intent i = new Intent("com.x.yang.DETAIL.SINGLE");
                i.putExtra("tab",2);
                i.putExtra("action","nearest");
                i.putExtra("id",id_near);
                MainPageFragment.this.getActivity().sendBroadcast(i);

                MainPageFragment.this.getActivity().sendBroadcast(i);
            }else if(tv == "The next moment event"){

                Intent i =new Intent("com.x.yang.DETAIL.SINGLE");
                i.putExtra("tab",2);
                i.putExtra("action","next");
                i.putExtra("id",id_next);
                MainPageFragment.this.getActivity().sendBroadcast(i);
            }

        }
    }



}
