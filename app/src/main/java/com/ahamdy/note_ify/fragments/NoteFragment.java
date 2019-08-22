package com.ahamdy.note_ify.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteFragment extends Fragment {
    private EditText noteTitle;
    private EditText noteBody;
    private String noteId = null;

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

        //        Intent i = getActivity().getIntent();
        //        noteId = i.getStringExtra(EXTRA_NOTE_UUID);

        if (getArguments() != null) {
            noteId = (String) getArguments().getSerializable("ARG_NOTE_ID");
        }

        noteTitle = view.findViewById(R.id.note_tile);
        noteBody = view.findViewById(R.id.note_body);

        if (noteId != null) {
            RealmNote note = RealmDB.getNote(noteId);
            noteTitle.setText(note.getTitle());
            noteBody.setText(note.getBody());
        }

        FloatingActionButton fabSaveNote = view.findViewById(R.id.fab_save_note);
        fabSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteId == null) {
                    RealmNote note = new RealmNote(noteTitle.getText().toString(), noteBody.getText().toString());
                    RealmDB.addNote(note);
                    Toast.makeText(getContext(), "add note requested", Toast.LENGTH_LONG);
                    Log.w("ADD", "add note requested");
                    getActivity().finish();
                } else {
                    RealmNote note = new RealmNote(noteTitle.getText().toString(), noteBody.getText().toString());
                    note.setId(noteId);
                    Toast.makeText(getContext(), "update note requested", Toast.LENGTH_LONG);
                    Log.w("UPDATE", "update note requested");
                    RealmDB.updateNote(note);
                    getActivity().finish();
                }
            }
        });
        return view;
    }
}