package com.ahamdy.note_ify.interfaces;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;

import java.util.ArrayList;

import static com.ahamdy.note_ify.NoteifyApplication.getAppContext;
import static com.ahamdy.note_ify.fragments.NoteListFragment.getActionMode;
import static com.ahamdy.note_ify.fragments.NoteListFragment.getAdapter;
import static com.ahamdy.note_ify.fragments.NoteListFragment.setActionMode;
import static com.ahamdy.note_ify.fragments.NoteListFragment.setMultiSelect;
import static com.ahamdy.note_ify.fragments.NoteListFragment.setSelectedNotesCounter;

public class ActionModeCallback implements ActionMode.Callback {
    private Menu contextMenue;
    private ArrayList<RealmNote> selectedData;

    public ActionModeCallback(Menu contextMenue, ArrayList<RealmNote> selectedData){
        this.contextMenue = contextMenue;
        this.selectedData = selectedData;
    }
    @Override
    public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_multi_select, menu);
        contextMenue = menu;
        return true;

    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(getAppContext(), "Delete request invoked", Toast.LENGTH_SHORT).show();
                RealmDB.deleteSelectedNotes();
//                RealmDB.clearNotesSelection();
                getAdapter().updateData(RealmDB.getAllNotes());
                getActionMode().finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode action) {
        setActionMode(null);
        setMultiSelect(false);
        RealmDB.clearNotesSelection();
        setSelectedNotesCounter(0);
    }
}
