package com.ahamdy.note_ify.activities;

import androidx.fragment.app.Fragment;

import com.ahamdy.note_ify.fragments.NoteFragment;

public class NoteActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new NoteFragment();
    }
}
