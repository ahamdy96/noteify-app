package com.ahamdy.note_ify.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;
import com.ahamdy.note_ify.utils.AlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.ContentValues.TAG;
import static com.ahamdy.note_ify.NoteifyApplication.getAppContext;
import static com.ahamdy.note_ify.fragments.DatePickerFragment.EXTRA_CALENDAR;
import static com.ahamdy.note_ify.fragments.DatePickerFragment.EXTRA_DATE;

public class NoteFragment extends Fragment {
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static AlarmManager alarmManager = (AlarmManager) getAppContext().getSystemService(Context.ALARM_SERVICE);
    private final String DIALOG_TIME = "DialogAlarmTime";
    private final String DIALOG_DATE = "DialogAlarmDate";
    private Date alarmTime = null;
    private EditText noteTitle;
    private EditText noteBody;
    private String noteId = null;
    private RealmNote note;

    public static NoteFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putSerializable("ARG_NOTE_ID", id);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            noteId = (String) getArguments().getSerializable("ARG_NOTE_ID");
        }

        noteTitle = view.findViewById(R.id.note_tile);
        noteBody = view.findViewById(R.id.note_body);

        if (noteId != null) {
            note = RealmDB.getNote(noteId);
            alarmTime = note.getAlarmTime();
            noteTitle.setText(note.getTitle());
            noteBody.setText(note.getBody());
        }

        FloatingActionButton fabSaveNote = view.findViewById(R.id.fab_save_note);
        fabSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getAppContext(), AlarmReceiver.class);
                if (noteId == null) {
                    RealmNote note = new RealmNote(noteTitle.getText().toString(), noteBody.getText().toString());
                    note.setAlarmTime(alarmTime);
                    RealmDB.addNote(note);
                    Log.w("ADD", "add note requested");
                    intent.putExtra("NOTE_TITLE", noteTitle.getText().toString());
                    intent.putExtra("NOTE_BODY", noteBody.getText().toString());
                } else {
                    RealmNote note = new RealmNote(noteTitle.getText().toString(), noteBody.getText().toString());
                    note.setId(noteId);
                    note.setAlarmTime(alarmTime);
                    Log.w("UPDATE", "update note requested");
                    RealmDB.updateNote(note);
                    intent.putExtra("NOTE_TITLE", note.getTitle());
                    intent.putExtra("NOTE_BODY", note.getBody());
                }
                getActivity().finish();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getAppContext(), 0, intent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTime(), pendingIntent);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.w(TAG, "onCreateOptionsMenu: called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.note_alarm) {
            DatePickerFragment datePickerFragment;
            if (alarmTime != null)
                datePickerFragment = DatePickerFragment.newInstance(alarmTime);
            else
                datePickerFragment = DatePickerFragment.newInstance();

            datePickerFragment.setTargetFragment(this, REQUEST_DATE);
            FragmentManager fm = getFragmentManager();
            datePickerFragment.show(fm, DIALOG_DATE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE) {
            if (resultCode == Activity.RESULT_OK) {
                Date date = (Date) data.getSerializableExtra(EXTRA_DATE);
                GregorianCalendar gregorianCalendar = (GregorianCalendar) data.getSerializableExtra(EXTRA_CALENDAR);

                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(date, gregorianCalendar);
                timePickerFragment.setTargetFragment(NoteFragment.this, REQUEST_TIME);
                timePickerFragment.show(getFragmentManager(), DIALOG_TIME);
            }
        } else if (requestCode == REQUEST_TIME) {
            if (resultCode == Activity.RESULT_OK) {
                GregorianCalendar gregorianCalendar = (GregorianCalendar) data.getSerializableExtra(EXTRA_CALENDAR);
                alarmTime = gregorianCalendar.getTime();
                Log.w(TAG, "onActivityResult: alarm set");
            }
        }
    }
}