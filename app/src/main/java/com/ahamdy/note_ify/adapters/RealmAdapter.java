package com.ahamdy.note_ify.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


public class RealmAdapter extends RealmRecyclerViewAdapter {
    public static final String EXTRA_NOTE_UUID = "com.ahamdy.note_ify";
    private RealmResults<RealmNote> notes;
    private Context context;
    private ArrayList<RealmNote> selectedData;

    public RealmAdapter(@Nullable OrderedRealmCollection data, ArrayList<RealmNote> selectedData, boolean autoUpdate) {
        super(data, autoUpdate);
        notes = (RealmResults<RealmNote>) data;
        this.selectedData = selectedData;
    }

    public RealmAdapter(@Nullable OrderedRealmCollection data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
        notes = (RealmResults<RealmNote>) data;
    }

    @NonNull
    @Override
    public RealmAdapter.NotesViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_holder, parent, false);
        final RealmAdapter.NotesViewHolder holder = new NotesViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
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
            Log.w("inspect items 1", "onBindViewHolder: position " + position + " with title " + note.getTitle());

            if (note.isSelected()) {
                Log.w("inspect items 2", "onBindViewHolder: position " + position + " with title " + RealmDB.getAllNotes().get(position).getTitle());
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_normal_state));
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

        private NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.vh_note_title);
            noteDateCreated = itemView.findViewById(R.id.vh_note_date_created);
        }
    }

}
