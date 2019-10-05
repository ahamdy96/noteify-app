package com.ahamdy.note_ify.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ahamdy.note_ify.R;

public class NoteAlarmActivity extends AppCompatActivity {
    private TextView noteTitleTextView;
    private TextView noteBodyTextView;
    private Button okButton;
    private TextView cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_alarm);

        noteBodyTextView = findViewById(R.id.note_body);
        noteTitleTextView = findViewById(R.id.note_tile);
        okButton = findViewById(R.id.button_ok);
        cancelButton = findViewById(R.id.button_cancel);

        String noteTitle = (String) getIntent().getSerializableExtra("NOTE_TITLE");
        String noteBody = (String) getIntent().getSerializableExtra("NOTE_BODY");

        noteTitleTextView.setText(noteTitle);
        noteBodyTextView.setText(noteBody);


    }
}
