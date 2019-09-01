package com.ahamdy.note_ify.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmNote extends RealmObject {
    @Ignore
    private boolean isChecked;
    //    @Required
    private boolean isSelected;
    @Required
    @PrimaryKey
    private String _id;
    @Required
    private String title;
    @Required
    private String body;
    @Required
    private String dateCreated;
    @Required
    private String dateModified;
    private Date alarmTime = null;

    public RealmNote() {
        this.title = "";
        this.body = "";
        this.dateCreated = new Date().toString();
        this.dateModified = new Date().toString();
        isSelected = false;
    }

    public RealmNote(String title, String body) {
        this.title = title;
        this.body = body;
        this.dateCreated = new Date().toString();
        this.dateModified = new Date().toString();
        isSelected = false;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }
}
