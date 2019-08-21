package com.ahamdy.note_ify.models;

import android.os.AsyncTask;
import android.util.Log;

import com.ahamdy.note_ify.network.NoteifyService;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;

public class RealmDB {

    private static final Realm realm = Realm.getDefaultInstance();
    private static String token = realm.where(RealmUser.class).findFirst().getToken();

    private RealmDB() {
        Log.w(TAG, "RealmDB: constructor called");
    }

    public static void addNote(RealmNote note) {
        new addNoteODB().execute(note);
        //        NoteifyService.getInstance().addNote(note);
        //        realm.beginTransaction();
        //        realm.copyToRealm(note);
        //        realm.commitTransaction();
    }

    public static void updateNote(RealmNote note) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
        NoteifyService.getInstance().updateNote(note.getId(), note);
    }

    public static RealmNote getNote(String id) {
        RealmNote note;
        note = realm.where(RealmNote.class).equalTo("_id", id).findFirst();
        return note;
    }

    public static RealmResults<RealmNote> getAllNotes() {
        Log.w(TAG, "getAllNotes: notes size " + realm.where(RealmNote.class).sort("dateCreated").findAll().size());
        return realm.where(RealmNote.class).sort("dateCreated").findAll();
    }

    public static void clearNotesSelection() {
        realm.beginTransaction();
        realm.where(RealmNote.class).equalTo("isSelected", true).findAll().setBoolean("isSelected", false);
        realm.commitTransaction();
    }

    public static void deleteNote(String id) {
        realm.beginTransaction();
        realm.where(RealmNote.class).equalTo("_id", id).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        NoteifyService.getInstance().deleteNote(id);
    }

    public static void selectNote(String id, boolean isSelected) {
        realm.beginTransaction();
        realm.where(RealmNote.class).equalTo("_id", id).findAll().setBoolean("isSelected", isSelected);
        realm.commitTransaction();
    }

    public static void deleteSelectedNotes() {
        RealmResults<RealmNote> deletedNotes = realm.where(RealmNote.class).equalTo("isSelected", true).findAll();
        for (RealmNote note : deletedNotes) {
            NoteifyService.getInstance().deleteNote(note.getId());
        }
        realm.beginTransaction();
        deletedNotes.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public static String getToken() {
        return token;
    }

    private static class addNoteODB extends AsyncTask<RealmNote, Void, RealmNote> {

        @Override
        protected RealmNote doInBackground(RealmNote... realmNotes) {
            NoteId id = NoteifyService.getInstance().addNote(realmNotes[0]);
            realmNotes[0].setId(id.toString());
            return realmNotes[0];
        }

        @Override
        protected void onPostExecute(final RealmNote realmNoteNote) {
            realm.beginTransaction();
            realm.copyToRealm(realmNoteNote);
            realm.commitTransaction();
        }
    }
}
