package com.ahamdy.note_ify.interfaces;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.ahamdy.note_ify.activities.NoteActivity;

public class FABAddNoteClickListener implements View.OnClickListener {

    private Context context;
    public FABAddNoteClickListener(Context context){
        super();
        this.context = context;
    }
    @Override
    public void onClick(View view) {
        Intent i = new Intent(context, NoteActivity.class);
        context.startActivity(i);
    }
}
