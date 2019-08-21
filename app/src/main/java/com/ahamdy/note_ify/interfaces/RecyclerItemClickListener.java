package com.ahamdy.note_ify.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahamdy.note_ify.activities.NoteActivity;
import com.ahamdy.note_ify.models.RealmNote;

import static com.ahamdy.note_ify.NoteifyApplication.getAppContext;
import static com.ahamdy.note_ify.fragments.NoteListFragment.getActionMode;
import static com.ahamdy.note_ify.fragments.NoteListFragment.getAdapter;
import static com.ahamdy.note_ify.fragments.NoteListFragment.isMultiSelect;
import static com.ahamdy.note_ify.fragments.NoteListFragment.multi_select;
import static com.ahamdy.note_ify.fragments.NoteListFragment.setActionMode;
import static com.ahamdy.note_ify.fragments.NoteListFragment.setMultiSelect;
import static com.ahamdy.note_ify.adapters.RealmAdapter.EXTRA_NOTE_UUID;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private Context context;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, ActionModeCallback actionModeCallback) {
        this.context = context;
        this.actionModeCallback = actionModeCallback;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            onItemClick(childView, rv.getChildAdapterPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void onItemClick(View view, int position) {
        if (isMultiSelect()){
            multi_select(position);
        }
        else {
            Intent i = new Intent(context, NoteActivity.class);
            RealmNote note = ((RealmNote) getAdapter().getItem(position));
            i.putExtra(EXTRA_NOTE_UUID, note.getId());
            Toast.makeText(getAppContext(),"note with title " + note.getTitle() + " is shown", Toast.LENGTH_SHORT).show();
            context.startActivity(i);
        }
    }

    public void onItemLongClick(View view, int position) {
        if (!isMultiSelect()) {
            setMultiSelect(true);
            if (getActionMode() == null) {
                setActionMode(((Activity)context).startActionMode(actionModeCallback));
            }
        }
        multi_select(position);
    }
    /*
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }*/
}
