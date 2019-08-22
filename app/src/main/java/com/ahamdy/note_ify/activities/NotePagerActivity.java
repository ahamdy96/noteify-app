package com.ahamdy.note_ify.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.fragments.NoteFragment;
import com.ahamdy.note_ify.models.RealmNote;

import static com.ahamdy.note_ify.adapters.RealmAdapter.EXTRA_NOTE_POSITION;
import static com.ahamdy.note_ify.fragments.NoteListFragment.getAdapter;

public class NotePagerActivity extends AppCompatActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager);

        pager = findViewById(R.id.activity_note_pager_view_pagerr);

        FragmentManager fm = getSupportFragmentManager();

        pager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                RealmNote note = (RealmNote) getAdapter().getItem(position);
                return NoteFragment.newInstance(note.getId());
            }

            @Override
            public int getCount() {
                return getAdapter().getItemCount();
            }
        });

        pager.setCurrentItem(getIntent().getIntExtra(EXTRA_NOTE_POSITION, 0));
    }
}
