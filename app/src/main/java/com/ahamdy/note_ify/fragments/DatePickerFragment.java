package com.ahamdy.note_ify.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ahamdy.note_ify.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.ahamdy.note_ify.date";
    public static final String EXTRA_CALENDAR = "com.ahamdy.note_ify.calendar";

    public static DatePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable("date", date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DatePickerFragment newInstance() {

        Bundle args = new Bundle();

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable("date");
        Calendar calendar = null;

        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alarm_date, null);
        DatePicker datePicker = view.findViewById(R.id.alarm_date_picker);

        if (calendar != null) {
            datePicker.init(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    null);
        }

        return new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth());
                        sendRsults(Activity.RESULT_OK, date, gregorianCalendar);
                    }
                })
                .create();
    }

    private void sendRsults(int resultCode, Date date, GregorianCalendar gregorianCalendar) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_CALENDAR, gregorianCalendar);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
