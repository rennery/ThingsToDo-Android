package com.x.yang.thingstodo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**

 */
public class fragment_message extends android.support.v4.app.Fragment {

    private LinearLayout ll;
    DetailReceiver dr;
    changeevent dr2;
    private TextView tit, add ,mess;
    private TextView date,fre;
    private ImageButton map,back,change,delete;
    private ImageView photo;
    public int ivs = View.INVISIBLE;
    Bitmap bitmap;
    public static  String tit_t,add_t,mess_t,fre_t,date_t;
    Alldata alldata;
    public static  Thing_data display;




    public fragment_message() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(display != null){
            tit_t = display.getTitle();
            add_t = display.getLoc();
            mess_t = display.getMess();
            fre_t = "frequent: "+display.getFre();
            date_t = display.getYear()+"-"+(display.getMonth()+1)+"-"+display.getDay()+" "+display.getHour()+":"+display.getMin();
            ivs = View.VISIBLE;
        }
        alldata = Alldata.getInstance(getActivity());
        dr =new DetailReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.x.yang.thingstodo.DETAIL.SHOW");
        this.getActivity().registerReceiver(dr, filter);
        dr2 =new changeevent();
        IntentFilter filter2=new IntentFilter();
        filter2.addAction("com.x.yang.thingstodo.DETAIL.UPDATE");
        this.getActivity().registerReceiver(dr2, filter2);
        Log.i("detail","receiver");
        super.onCreate(savedInstanceState);
        Log.i("detail","creat");


    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(dr);
        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onStart() {
        Log.i("detail","resume");
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_layout_message, container, false);
        ll = (LinearLayout)v.findViewById(R.id.whole);
        photo =(ImageView)v.findViewById(R.id.pic_det);
        photo.setImageBitmap(bitmap);
        tit = (TextView)v.findViewById(R.id.title_det);
        tit.setText(tit_t);
        add = (TextView)v.findViewById(R.id.add_det);
        add.setText(add_t);
        mess = (TextView)v.findViewById(R.id.mess_det);
        date = (TextView) v.findViewById(R.id.dateofevent_det);
        mess.setText(mess_t);
        date.setText(date_t);
        map = (ImageButton) v.findViewById(R.id.checkmap);
        back = (ImageButton) v.findViewById(R.id.back_det);
        change = (ImageButton) v.findViewById(R.id.change);
        delete = (ImageButton) v.findViewById(R.id.delete);
        map.setOnClickListener(new butClick());
        back.setOnClickListener(new butClick());
        photo.setOnClickListener(new photoClick());
        change.setOnClickListener(new butClick());
        delete.setOnClickListener(new butClick());
        fre = (TextView) v.findViewById(R.id.fre_det);
        fre.setText(fre_t);
        ll.setVisibility(ivs);
        return v;
    }

    private class photoClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),checkphoto.class);
            intent.putExtra("photo",Environment.getExternalStorageDirectory()+ "/finger/"+display.getId()+".png");
            getActivity().startActivity(intent);
        }
    }

    private class DetailReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("detail","boardcastreceived");
            String action,id;
            Bundle bundle = intent.getExtras();
            action = bundle.getString("action");
            id = bundle.getString("id");
            display = alldata.getData(id);
            if(display !=null){
                Log.i("detail","setdetail");
                updata(display);
            }

        }
    }
    private void updata(Thing_data display){
        //tit = (TextView)fragment_message.this.getView().findViewById(R.id.title_det);
        tit_t = display.getTitle();
        add_t = display.getLoc();
        mess_t = display.getMess();
        fre_t = "frequent: "+display.getFre();
        date_t = display.getYear()+"-"+(display.getMonth()+1)+"-"+display.getDay()+" "+display.getHour()+":"+display.getMin();
        ivs = View.VISIBLE;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(Environment.getExternalStorageDirectory()+ "/finger/"+display.getId()+".png");
            bitmap  = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            bitmap = null;
        }

    }

    private class butClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.checkmap:
                    Uri mUri = Uri.parse("geo:"+display.getLatitude()+","+display.getLongtitude()+"?q="+display.getLoc());
                    Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
                    startActivity(mIntent);
                    break;
                case R.id.back_det:
                    ivs =View.INVISIBLE;
                    Intent intent = new Intent("com.x.yang.thingstodo.CHANGE");
                    intent.putExtra("tab",0);
                    getActivity().sendBroadcast(intent);
                    break;
                case R.id.delete:
                    new AlertDialog.Builder(fragment_message.this.getActivity()).setTitle("delete conform")
                            .setMessage("delete this event?")
                            .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alldata.deleteData(display.getId());
                                    Intent intent = new Intent("com.x.yang.thingstodo.LIST.DELTED");
                                    fragment_message.this.getActivity().sendBroadcast(intent);
                                    Intent intent2 = new Intent("com.x.yang.thingstodo.CHANGE");
                                    intent2.putExtra("tab",1);
                                    fragment_message.this.getActivity().sendBroadcast(intent2);
                                    ivs = View.INVISIBLE;
                                }
                            }).setNegativeButton("No",null).show();
                    break;
                case R.id.change:

                    Intent i =new Intent(getActivity(),Adding.class);
                    i.putExtra("id",display.getId());
                    startActivityForResult(i,0);
            }

        }
    }

    private class changeevent extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getExtras().getString("id");
            String src ="";
            display = Alldata.getInstance(fragment_message.this.getActivity()).getDatafromDB(id);
            src =display.getYear()+"-"+(display.getMonth()+1)+"-"+display.getDay()+" "+display.getHour()+":"+display.getMin();
            date_t = src;
            tit_t = display.getTitle();
            add_t = display.getLoc();
            mess_t = display.getMess();
            fre_t = "frequent: "+display.getFre();
            date_t = src;



            ivs = View.VISIBLE;
            date_t = src;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(Environment.getExternalStorageDirectory()+ "/finger/"+display.getId()+".png");
                bitmap  = BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                bitmap = null;
            }
            date_t = src;
            new AlertDialog.Builder(fragment_message.this.getActivity()).setTitle("delete conform")
                    .setMessage("event is update")
                    .setPositiveButton("OK",null).show();

        }
    }


}
