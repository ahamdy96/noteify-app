package com.ahamdy.note_ify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.ahamdy.note_ify.adapters.RealmAdapter.EXTRA_NOTE_UUID;

public class NoteActivity extends AppCompatActivity {

    private EditText noteTitle;
    private EditText noteBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent i = getIntent();
        final String noteId = i.getStringExtra(EXTRA_NOTE_UUID);

        noteTitle = findViewById(R.id.note_tile);
        noteBody = findViewById(R.id.note_body);

        if (noteId != null) {
            RealmNote note = RealmDB.getNote(noteId);
            noteTitle.setText(note.getTitle());
            noteBody.setText(note.getBody());
        }

        FloatingActionButton fabSaveNote = findViewById(R.id.fab_save_note);
        fabSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteId == null) {
                    RealmNote note = new RealmNote(noteTitle.getText().toString(), noteBody.getText().toString());
                    RealmDB.addNote(note);
                    Toast.makeText(NoteActivity.this, "add note requested", Toast.LENGTH_LONG);
                    Log.w("ADD", "add note requested");
                    finish();
                } else {
                    RealmNote note = new RealmNote(noteTitle.getText().toString(), noteBody.getText().toString());
                    note.setId(noteId);
                    Toast.makeText(NoteActivity.this, "update note requested", Toast.LENGTH_LONG);
                    Log.w("UPDATE", "update note requested");
                    RealmDB.updateNote(note);
                    finish();
                }
            }
        });
    }
}
