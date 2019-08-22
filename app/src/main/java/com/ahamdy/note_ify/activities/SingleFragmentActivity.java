package com.ahamdy.note_ify.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ahamdy.note_ify.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment noteListFragment = fm.findFragmentById(R.id.fragment_container);

        if (noteListFragment == null) {
            noteListFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, noteListFragment)
                    .commit();
        }
    }

    protected abstract Fragment createFragment();

}
