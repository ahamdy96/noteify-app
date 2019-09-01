package com.ahamdy.note_ify.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.adapters.RealmAdapter;
import com.ahamdy.note_ify.interfaces.ActionModeCallback;
import com.ahamdy.note_ify.interfaces.FABAddNoteClickListener;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;
import com.ahamdy.note_ify.network.NoteifyService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.realm.RealmRecyclerViewAdapter;

public class NoteListFragment extends Fragment {
    private static RealmRecyclerViewAdapter adapter;
    private static boolean isMultiSelect = false;
    private static Menu contextMenue;
    private static android.view.ActionMode actionMode;
    private static int selectedNotesCounter = 0;

    public static RealmRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public static void multi_select(int position) {
        if (actionMode != null) {
            RealmNote note = (RealmNote) getAdapter().getItem(position);
            if (note.isSelected()) {
                selectedNotesCounter--;
            } else {
                selectedNotesCounter++;
            }

            if (selectedNotesCounter < 1)
                actionMode.finish();
            else {
                RealmDB.selectNote(note.getId(), !note.isSelected());
                actionMode.setTitle("" + selectedNotesCounter);
            }
        }
    }

    public static boolean isMultiSelect() {
        return isMultiSelect;
    }

    public static void setMultiSelect(boolean isTrue) {
        isMultiSelect = isTrue;
    }

    public static ActionMode getActionMode() {
        return actionMode;
    }

    public static void setActionMode(ActionMode actionMode) {
        NoteListFragment.actionMode = actionMode;
    }

    public static int getSelectedNotesCounter() {
        return selectedNotesCounter;
    }

    public static void setSelectedNotesCounter(int selectedNotesCounter) {
        NoteListFragment.selectedNotesCounter = selectedNotesCounter;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        setHasOptionsMenu(true);

        new LoadNotes().execute(RealmDB.getToken());

        FloatingActionButton fabAddNote = view.findViewById(R.id.fab_add_note);
        fabAddNote.setOnClickListener(new FABAddNoteClickListener(getContext()));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_notes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList selectedData = new ArrayList<RealmNote>();
        ActionModeCallback actionModeCallback = new ActionModeCallback(contextMenue, selectedData);
        adapter = new RealmAdapter(RealmDB.getAllNotes(), true, true, actionModeCallback);

//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, actionModeCallback));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(getContext(), "action item clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadNotes extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            NoteifyService.getInstance().loadNotes(strings[0]);
            return null;
        }
    }
}
