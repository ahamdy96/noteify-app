package com.ahamdy.note_ify.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ahamdy.note_ify.R;
import com.ahamdy.note_ify.network.NoteifyService;

import static com.ahamdy.note_ify.NoteifyApplication.getAppContext;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);

        loadingProgressBar = findViewById(R.id.loading);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                new Login().execute(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                new Register().execute(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private class Login extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return NoteifyService.getInstance().loginUser(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(final Boolean isLoggedIn) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isLoggedIn) {
                        Intent i = new Intent(getAppContext(), NoteListActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private class Register extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return NoteifyService.getInstance().registerUser(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(final Boolean isRegistered) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isRegistered) {
                        Intent i = new Intent(getAppContext(), NoteListActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
