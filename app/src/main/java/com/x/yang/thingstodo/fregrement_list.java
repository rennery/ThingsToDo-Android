package com.x.yang.thingstodo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**

 * to handle interaction events.
 */
public class fregrement_list extends android.support.v4.app.Fragment {


    private ListView lv;
    SimpleAdapter sa;
    List<Map<String, Object>> listems;
    Alldata ad;
    NewReceiver receiver;

    public fregrement_list() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_list, container, false);
        listems = new ArrayList<Map<String, Object>>();
        ad =Alldata.getInstance(this.getActivity());
        lv =(ListView)v.findViewById(R.id.li_list);
        if(ad.list.isEmpty()){
            Map<String, Object> listem2 = new HashMap<String, Object>();
            listem2.put("head", "click to add new event");
            listem2.put("name", R.drawable.add);
            listem2.put("id","addnew");
            listem2.put("date","");
            listems.add(listem2);
            sa = new SimpleAdapter(this.getActivity(),listems,R.layout.add_new_list,new String[] { "head", "name","id","date"},
                    new int[] {R.id.allthings,R.id.thingimage,R.id.evet_id,R.id.thingdate});
            lv.setAdapter(sa);
            lv.setOnItemClickListener(new listclick());
        }else{
            Map<String, Object> listem = new HashMap<String, Object>();
            listem.put("head", "click to add new event");
            listem.put("name", R.drawable.add);
            listem.put("id","addnew");
            listem.put("date","");
            listems.add(listem);
            Iterator<Thing_data> lt = ad.list.iterator();
            while(lt.hasNext()){
                Log.i("readlist","reading");
                Thing_data td = lt.next();
                Map<String, Object> listem2 = new HashMap<String, Object>();
                listem2.put("head",td.getTitle());
                if(td.getFre().equalsIgnoreCase("weekly")){
                    Log.i("fre","here");
                    listem2.put("name", R.drawable.week);
                }else if(td.getFre().equalsIgnoreCase("daily")){
                    listem2.put("name", R.drawable.dayly);
                }else if(td.getFre().equalsIgnoreCase("monthly")){
                    listem2.put("name", R.drawable.month);
                }else {
                    listem2.put("name", R.drawable.once);
                }

                listem2.put("id",td.getId());
                if((td.getMonth()+1) < 10 && td.getDay()<10){
                    listem2.put("date","0"+(td.getMonth()+1)+"-0"+td.getDay()+" "+td.getHour()+" : "+td.getMin());
                }
                else if((td.getMonth()+1) < 10){
                    listem2.put("date","0"+(td.getMonth()+1)+"-"+td.getDay()+" "+td.getHour()+" : "+td.getMin());
                }else if(td.getDay()<10){
                    listem2.put("date",(td.getMonth()+1)+"-0"+td.getDay()+" "+td.getHour()+" : "+td.getMin());
                }else{
                    listem2.put("date",(td.getMonth()+1)+"-"+td.getDay()+" "+td.getHour()+" : "+td.getMin());
                }

                listems.add(listem2);
            }
            sa = new SimpleAdapter(this.getActivity(),listems,R.layout.add_new_list,new String[] { "head", "name","id","date"},
                    new int[] {R.id.allthings,R.id.thingimage,R.id.evet_id,R.id.thingdate});
            lv.setAdapter(sa);
            lv.setOnItemClickListener(new listclick());
        }
        receiver=new NewReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.x.yang.thingstodo.NEWEVENT");
        this.getActivity().registerReceiver(receiver, filter);


        // Inflate the layout for this fragment

        return v;
    }
    private class myAdapter extends SimpleAdapter{

        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public myAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            TextView t = (TextView)v.findViewById(R.id.allthings);
            return v;
        }
    }

    private class listclick implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView)parent;
            HashMap<String,Object> item = (HashMap<String,Object>)lv.getItemAtPosition(position);
            String tv = item.get("head").toString();
            if(tv == "click to add new event"){

                Intent i =new Intent(getActivity(),Adding.class);
                startActivity(i);
            }else{

            }

        }
    }
    public class NewReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle=intent.getExtras();
            String v;

            Map<String, Object> listem2 = new HashMap<String, Object>();
            listem2.put("head", bundle.getString("tit"));
            Log.i("fre",bundle.getString("fre"));
            if(bundle.getString("fre").equalsIgnoreCase("weekly")){
                Log.i("fre","here");
                listem2.put("name", R.drawable.week);
            }else if(bundle.getString("fre").equalsIgnoreCase("daily")){
                listem2.put("name", R.drawable.dayly);
            }else if(bundle.getString("fre").equalsIgnoreCase("monthly")){
                listem2.put("name", R.drawable.month);
            }else {
                listem2.put("name", R.drawable.once);
            }
            listem2.put("id",bundle.getString("id"));
            listem2.put("date",bundle.getString("date"));
            listems.add(listem2);
            sa.notifyDataSetChanged();


        }
    }

    @Override
    public void onDestroy() {
        this.getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }
}





