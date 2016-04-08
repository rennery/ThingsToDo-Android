package com.x.yang.thingstodo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**

 * to handle interaction events.
 */
public class fregrement_list extends android.support.v4.app.Fragment {


    private ListView lv;
    public fregrement_list() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_list, container, false);
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        Alldata ad =Alldata.getInstance(this.getActivity());
        lv =(ListView)v.findViewById(R.id.li_list);
        if(ad.list.isEmpty()){
            Map<String, Object> listem2 = new HashMap<String, Object>();
            listem2.put("head", "There is no event,click to add");
            listem2.put("name", R.drawable.add);
            listems.add(listem2);
            SimpleAdapter sa = new SimpleAdapter(this.getActivity(),listems,R.layout.add_new_list,new String[] { "head", "name"},
                    new int[] {R.id.allthings,R.id.thingimage});
            lv.setAdapter(sa);
        }else{
            while(ad.list.iterator().hasNext()){

            }
        }
        // Inflate the layout for this fragment

        return v;
    }



}





