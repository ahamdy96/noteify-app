package com.ahamdy.note_ify.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.models.RealmUser;
import com.ahamdy.note_ify.network.NoteifyService;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Realm realm = Realm.getDefaultInstance();

        RealmUser user = realm.where(RealmUser.class).findFirst();

        if (user != null) {
            token = user.getToken();
        } else {
            realm.beginTransaction();
            realm.where(RealmUser.class).findAll().deleteAllFromRealm();
            realm.commitTransaction();
        }

        realm.close();

        if (token != null && !token.isEmpty()) {
            new ValidateUser().execute(token);

        } else {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }

    private class ValidateUser extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return NoteifyService.getInstance().validateUser(strings[0]);
        }

        @Override
        protected void onPostExecute(final Boolean aBoolean) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent i;
                    if (aBoolean) {
                        i = new Intent(SplashActivity.this, NoteListActivity.class);
                    } else {
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(i);
                    finish();
                }
            });
        }
    }
}
