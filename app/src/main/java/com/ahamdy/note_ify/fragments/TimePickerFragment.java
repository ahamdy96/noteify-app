package com.ahamdy.note_ify.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ahamdy.note_ify.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.ahamdy.note_ify.fragments.DatePickerFragment.EXTRA_CALENDAR;

public class TimePickerFragment extends DialogFragment {


    private static final String EXTRA_DATE = "com.ahamdy.note_ify.date";

    public static TimePickerFragment newInstance(Date date, GregorianCalendar gregorianCalendar) {

        Bundle args = new Bundle();

        args.putSerializable("date", date);
        args.putSerializable("calendar", gregorianCalendar);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TimePickerFragment newInstance() {

        Bundle args = new Bundle();

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable("date");
        GregorianCalendar gregorianCalendar = (GregorianCalendar) getArguments().getSerializable("calendar");
        Calendar calendar = null;

        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alarm_time, null);
        TimePicker timePicker = view.findViewById(R.id.alarm_time_picker);

        if (calendar != null) {
            timePicker.setCurrentHour((calendar.get(Calendar.HOUR_OF_DAY)));
            timePicker.setCurrentMinute((calendar.get(Calendar.MINUTE)));
        }
        timePicker.setIs24HourView(true);
        return new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gregorianCalendar.set(GregorianCalendar.HOUR, timePicker.getCurrentHour());
                        gregorianCalendar.set(GregorianCalendar.MINUTE, timePicker.getCurrentMinute());

                        sendRsults(Activity.RESULT_OK, gregorianCalendar);
                    }
                })
                .create();
    }

    private void sendRsults(int resultCode, GregorianCalendar gregorianCalendar) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CALENDAR, gregorianCalendar);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
