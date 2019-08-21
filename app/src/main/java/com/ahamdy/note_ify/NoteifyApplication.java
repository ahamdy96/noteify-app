package com.ahamdy.note_ify;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NoteifyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("noteifyDB.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        context = getApplicationContext();

        Log.w("NoteifyApplication", "onCreate: called");
    }

    public static Context getAppContext(){
        return context;
    }
}
