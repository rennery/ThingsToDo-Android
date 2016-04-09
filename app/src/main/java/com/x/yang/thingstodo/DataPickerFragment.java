package com.x.yang.thingstodo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yang on 2015/12/20.
 */

public class DataPickerFragment extends DialogFragment {
    private String intent;
    private int d,y,m;

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.datepick,null);
        DatePicker date = (DatePicker)v.findViewById(R.id.datepick);
        intent = getArguments().getSerializable("Time of use").toString();
        if(intent == "0"){
            Calendar c = Calendar.getInstance();
            int year = y = c.get(Calendar.YEAR);
            int month = m = c.get(Calendar.MONTH);
            int day = d = c.get(Calendar.DAY_OF_MONTH);

            date.init(year, month, day, new dateChange());
        }
        Log.i("dialog","here");
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("select date").setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePick dp =(DatePick)getActivity();
                        dp.InputComplete(d,m,y);
                    }
                }).create();
    }
    public interface DatePick
    {
        void InputComplete(int day, int month, int year);
    }

    private class dateChange implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker arg0, int year, int month, int day) {
            y = year;
            m = month;
            d = day;
        }
    }
}
