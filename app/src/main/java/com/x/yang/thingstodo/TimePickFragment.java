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
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by yang on 2016/4/8.
 */
public class TimePickFragment extends DialogFragment{
    private String intent;
    private int h,m;

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.timepick, null);
        TimePicker date = (TimePicker) v.findViewById(R.id.timepick1);
        date.setIs24HourView(true);
        h = date.getCurrentHour();
        m = date.getCurrentMinute();
        intent = getArguments().getSerializable("Time of use").toString();
        if (intent == "0") {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR);
            int min = c.get(Calendar.MINUTE);
            date.setOnTimeChangedListener(new timeChange());
        }
        Log.i("dialog", "here");
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("select time").setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimePick tp = (TimePick) getActivity();
                        tp.InputComplete_t(h, m);
                    }
                }).create();
    }
    public interface TimePick
    {
        void InputComplete_t(int hour, int min);
    }

    private class timeChange implements TimePicker.OnTimeChangedListener {
        @Override
        public void onTimeChanged(TimePicker arg0, int hour, int min) {
            h = hour;
            m = min;
        }
    }
}
