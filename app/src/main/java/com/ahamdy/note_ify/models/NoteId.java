package com.ahamdy.note_ify.models;

public class NoteId {
    private String _id;

    public void setId(String id) {
        this._id = id;
    }

    @Override
    public String toString() {
        return _id;
    }
}
