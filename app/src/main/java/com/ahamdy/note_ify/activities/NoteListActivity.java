package com.ahamdy.note_ify.activities;

import androidx.fragment.app.Fragment;

import com.ahamdy.note_ify.fragments.NoteListFragment;

public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }
}
