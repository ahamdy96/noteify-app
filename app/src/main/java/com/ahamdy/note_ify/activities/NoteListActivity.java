package com.ahamdy.note_ify.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.fragments.NoteListFragment;

public class NoteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        Fragment noteListFragment = fm.findFragmentById(R.id.fragment_container);

        if (noteListFragment == null) {
            noteListFragment = new NoteListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, noteListFragment)
                    .commit();
        }
        setContentView(R.layout.activity_main);
    }

}
