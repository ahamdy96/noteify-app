package com.ahamdy.note_ify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.network.NoteifyService;

import static com.ahamdy.note_ify.NoteifyApplication.getAppContext;

public class PostLoginActivity extends AppCompatActivity {

    private static boolean notesloaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        Log.w("PostLoginActivity", "onCreate: called");
        new LoadNotes().execute(RealmDB.getToken());
    }

    private class LoadNotes extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            NoteifyService.getInstance().loadNotes(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getAppContext(), NoteListActivity.class);
                    i.putExtra("isNotesLoaded", notesloaded);
                    startActivity(i);
                    finish();
                }
            });
        }


    }

    public static void setNotesloaded(boolean notesloaded) {
        PostLoginActivity.notesloaded = notesloaded;
        Log.w("NotesLoaded", "setNotesloaded: called");
    }
}
