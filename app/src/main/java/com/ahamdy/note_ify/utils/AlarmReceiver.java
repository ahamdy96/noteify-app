package com.ahamdy.note_ify.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ahamdy.note_ify.activities.NoteAlarmActivity;

import static android.content.ContentValues.TAG;
import static com.ahamdy.note_ify.NoteifyApplication.getAppContext;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, "onReceive: Alarm! Alarm!");
        Toast.makeText(getAppContext(), "Alarm! Alarm!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getAppContext(), NoteAlarmActivity.class);

        i.putExtra("NOTE_TITLE", intent.getSerializableExtra("NOTE_TITLE"));
        i.putExtra("NOTE_BODY", intent.getSerializableExtra("NOTE_BODY"));

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
