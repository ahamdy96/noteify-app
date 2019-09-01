package com.ahamdy.note_ify.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.activities.NotePagerActivity;
import com.ahamdy.note_ify.interfaces.ActionModeCallback;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;
import static com.ahamdy.note_ify.fragments.NoteListFragment.getActionMode;
import static com.ahamdy.note_ify.fragments.NoteListFragment.isMultiSelect;
import static com.ahamdy.note_ify.fragments.NoteListFragment.multi_select;
import static com.ahamdy.note_ify.fragments.NoteListFragment.setActionMode;
import static com.ahamdy.note_ify.fragments.NoteListFragment.setMultiSelect;


public class RealmAdapter extends RealmRecyclerViewAdapter {
    public static final String EXTRA_NOTE_POSITION = "com.ahamdy.note_ify";
    private RealmResults<RealmNote> notes;
    private Context context;
    private ArrayList<RealmNote> selectedData;
    private ActionModeCallback actionModeCallback;

    public RealmAdapter(@Nullable OrderedRealmCollection data, ArrayList<RealmNote> selectedData, boolean autoUpdate) {
        super(data, autoUpdate);
        notes = (RealmResults<RealmNote>) data;
        this.selectedData = selectedData;
    }

    public RealmAdapter(@Nullable OrderedRealmCollection data, boolean autoUpdate, boolean updateOnModification, ActionModeCallback actionModeCallback) {
        super(data, autoUpdate, updateOnModification);
        notes = (RealmResults<RealmNote>) data;
        this.actionModeCallback = actionModeCallback;
    }

    @NonNull
    @Override
    public RealmAdapter.NotesViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        context = parent.getContext();
        final View noteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_holder, parent, false);

        final RealmAdapter.NotesViewHolder holder = new NotesViewHolder(noteView);
        noteView.findViewById(R.id.expand_card_view_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMultiSelect() && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Log.w(TAG, "onClick: view id " + view.getId());
                    if (noteView.findViewById(R.id.note_body).getVisibility() == View.VISIBLE) {
                        TransitionManager.beginDelayedTransition(parent, new AutoTransition());
                        noteView.findViewById(R.id.note_body).setVisibility(View.GONE);
                        ((ImageButton) view).setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    } else {
                        TransitionManager.beginDelayedTransition(parent, new AutoTransition());
                        noteView.findViewById(R.id.note_body).setVisibility(View.VISIBLE);
                        ((ImageButton) view).setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    }
                }
            }
        });

        noteView.findViewById(R.id.note_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMultiSelect()) {
                    multi_select(holder.getAdapterPosition());
                } else {
                    Intent i = new Intent(context, NotePagerActivity.class);
                    RealmNote note = (RealmNote) getItem(holder.getAdapterPosition());
                    i.putExtra(EXTRA_NOTE_POSITION, holder.getAdapterPosition());
                    //Toast.makeText(getAppContext(), "note with title " + note.getTitle() + " is shown", Toast.LENGTH_SHORT).show();
                    context.startActivity(i);
                }
            }
        });
        noteView.findViewById(R.id.note_container).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isMultiSelect()) {
                    setMultiSelect(true);
                    if (getActionMode() == null) {
                        setActionMode(((Activity) context).startActionMode(actionModeCallback));
                    }
                }
                Log.w(TAG, "onLongClick: inside long click listeners");
                multi_select(holder.getAdapterPosition());
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RealmNote note = (RealmNote) getItem(position);
        if (note != null) {
            ((RealmAdapter.NotesViewHolder) holder).noteTitle.setText(note.getTitle());
            ((RealmAdapter.NotesViewHolder) holder).noteDateCreated.setText(note.getDateCreated());
            ((NotesViewHolder) holder).noteBody.setText(note.getBody());
            Log.w("inspect items 1", "onBindViewHolder: position " + position + " with title " + note.getTitle());

            if (note.isSelected()) {
                Log.w("inspect items 2", "onBindViewHolder: position " + position + " with title " + RealmDB.getAllNotes().get(position).getTitle());
                holder.itemView.findViewById(R.id.card_view_note).setBackground(ContextCompat.getDrawable(context, R.drawable.card_view_selected));

            } else {
                holder.itemView.findViewById(R.id.card_view_note).setBackground(ContextCompat.getDrawable(context, R.drawable.card_view_undo_selected));
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d("Recyclerview size", "getItemCount: " + notes.size());
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTitle;
        private TextView noteDateCreated;
        private TextView noteBody;
        private ImageButton expandButton;

        private NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.vh_note_title);
            noteDateCreated = itemView.findViewById(R.id.vh_note_date_created);
            noteBody = itemView.findViewById(R.id.note_body);
            expandButton = itemView.findViewById(R.id.expand_card_view_button);
        }
    }

}
