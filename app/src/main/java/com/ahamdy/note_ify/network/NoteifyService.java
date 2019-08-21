package com.ahamdy.note_ify.network;

import android.util.Log;

import com.ahamdy.note_ify.activities.PostLoginActivity;
import com.ahamdy.note_ify.models.NoteId;
import com.ahamdy.note_ify.models.RealmDB;
import com.ahamdy.note_ify.models.RealmNote;
import com.ahamdy.note_ify.models.RealmUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class NoteifyService {
    private static NoteifyService ourInstance;
    private Retrofit retrofit;
    private WebAppService service;
    private OkHttpClient.Builder okHttpClient;
    private HttpLoggingInterceptor loggingInterceptor;

    private NoteifyService() {
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(loggingInterceptor);
        //        okHttpClient = new OkHttpClient.Builder();
        //        okHttpClient.addInterceptor(new Interceptor() {
        //            @NotNull
        //            @Override
        //            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
        //                Request original = chain.request();
        //
        //                Request request = original.newBuilder()
        //                        .header("Authorization", "Your-App-Name")
        //                        .method(original.method(), original.body())
        //                        .build();
        //
        //                return chain.proceed(request);
        //            }
        //        });
        retrofit = new Retrofit.Builder()
                .baseUrl("https://noteify-service.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        service = retrofit.create(WebAppService.class);
    }

    public static NoteifyService getInstance() {
        if (ourInstance == null) {
            ourInstance = new NoteifyService();
        }
        return ourInstance;
    }

    public void loadNotes(String token) {
        final List<RealmNote> notes = new ArrayList<RealmNote>();
        Call<List<RealmNote>> getNotesCall = service.getNotes("Bearer " + token, "getAll");
        try {
            List<RealmNote> list = getNotesCall.execute().body();

            if (list != null) {
                Log.w(TAG, "loadNotes: notes " + list.toString());
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(list);
                    realm.commitTransaction();

                } finally {
                    if (realm != null) {
                        realm.close();
                        //                        PostLoginActivity.setNotesloaded(true);
                    }
                }

            } else {
                Log.w(TAG, "loadNotes: notes are null");
            }

            Log.w("Notes loaded", "onResponse: success");

        } catch (IOException e) {
//            e.printStackTrace();
            Log.w("RESPONSE_FAILED_IO", "onFailure: " + e.getMessage());
        }
        //        getNotesCall.enqueue(new Callback<List<RealmNote>>(){
        //            @Override
        //            public void onResponse(Call<List<RealmNote>> call, Response<List<RealmNote>> response) {
        //                if(!response.isSuccessful()){
        //                    Log.w("RESPONSE_UNSUCCESSFUL", "onResponse: unsuccessful");
        //                    PostLoginActivity.startMainActivity();
        //                    return;
        //                }
        //                Realm realm = Realm.getDefaultInstance();
        //
        //                realm.beginTransaction();
        //                realm.copyToRealmOrUpdate(response.body());
        //                realm.commitTransaction();
        //                realm.close();
        //                Log.w("Notes loaded", "onResponse: success");
        //                PostLoginActivity.setNotesloaded(true);
        //            }
        //
        //            @Override
        //            public void onFailure(Call<List<RealmNote>> call, Throwable t) {
        //                if(t instanceof IOException){
        //                    Log.w("RESPONSE_FAILED_IO", "onFailure: response failed " + t.getMessage());
        //                }
        //                else{
        //                    Log.w("RESPONSE_FAILED_NO", "onFailure: response failed " + t.getMessage());
        //                }
        //                PostLoginActivity.startMainActivity();
        //            }
        //        });
    }

    public NoteId addNote(RealmNote note) {
        NoteId id = null;
        Call<NoteId> addNoteCall = service.addNote(
                "Bearer " + RealmDB.getToken(),
                note.getTitle(),
                note.getBody(),
                note.getDateCreated(),
                note.getDateModified());
        try {
            id = addNoteCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
        //        addNoteCall.enqueue(new Callback<String>() {
        //            @Override
        //            public void onResponse(Call<String> call, Response<String> response) {
        //                if (!response.isSuccessful()) {
        //                    Log.d("ON_RESPONSE", "Response unsuccessful");
        //                }
        //            }
        //
        //            @Override
        //            public void onFailure(Call<String> call, Throwable t) {
        //                if (t instanceof IOException) {
        //                    Log.d("ON_FAILURE", "Response Failed, IOException " + t.getMessage());
        //                } else {
        //                    Log.d("ON_FAILURE", "Response Failed " + t.getMessage());
        //                }
        //            }
        //        });
    }

    public void deleteNote(String id) {
        Call<NoteId> deleteNote = service.deleteNote("Bearer " + RealmDB.getToken(), id);
        deleteNote.enqueue(new Callback<NoteId>() {
            @Override
            public void onResponse(Call<NoteId> call, Response<NoteId> response) {
                if (!response.isSuccessful()) {
                    Log.d("ON_RESPONSE", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<NoteId> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d("ON_FAILURE", "Response Failed, IOException " + t.getMessage());
                } else {
                    Log.d("ON_FAILURE", "Response Failed " + t.getMessage());
                }
            }
        });
    }

    public void updateNote(String id, RealmNote note) {
        Call<NoteId> updateNote = service.updateNote("Bearer " + RealmDB.getToken(), id, note.getTitle(), note.getBody(), note.getDateModified());
        updateNote.enqueue(new Callback<NoteId>() {
            @Override
            public void onResponse(Call<NoteId> call, Response<NoteId> response) {
                if (!response.isSuccessful()) {
                    Log.d("ON_RESPONSE", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<NoteId> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d("ON_FAILURE", "Response Failed, IOException " + t.getMessage());
                } else {
                    Log.d("ON_FAILURE", "Response Failed " + t.getMessage());
                }
            }
        });
    }

    public boolean registerUser(String mail, String pasword) {
        Log.w("register call", "registerUser: called");
        Call<RealmUser> registerUser = service.registerUser(mail, pasword);
        try {
            RealmUser user = registerUser.execute().body();
            if (user != null) {
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(user);
                    realm.commitTransaction();

                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
                return true;
            } else {
                Log.w("REGISTER", "registerUser: error user is null");
                return false;
            }
        } catch (IOException e) {
            Log.w("register response", "registerUserFailed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        //        registerUser.enqueue(new Callback<RealmUser>() {
        //            @Override
        //            public void onResponse(Call<RealmUser> call, Response<RealmUser> response) {
        //                if (!response.isSuccessful()) {
        //                    Log.w("ON_RESPONSE", "onResponse: failed");
        //                    return;
        //                }
        //                Log.w("ON_RESPONSE", "onResponse: success " + response.body());
        //                RealmUser user = response.body();
        //                RealmDB.addUser(user);
        //                startMainActivity();
        //            }
        //
        //            @Override
        //            public void onFailure(Call<RealmUser> call, Throwable t) {
        //                if (t instanceof IOException) {
        //                    Log.d("ON_FAILURE", "Response Failed, IOException " + t.getMessage());
        //                } else {
        //                    Log.d("ON_FAILURE", "Response Failed " + t.getMessage());
        //                }
        //            }
        //        });
    }

    public boolean loginUser(final String mail, final String password) {
        Call<RealmUser> loginUser = service.loginUser(mail, password);
        try {
            RealmUser user = loginUser.execute().body();
            if (user != null) {
                Realm realm = null;
                try {
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(user);
                    realm.commitTransaction();

                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
                return true;
            } else {
                Log.w("LOGIN", "loginUser: error user is null");
                return false;
            }
        } catch (IOException e) {
            Log.w("login response", "loginFailed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        //        loginUser.enqueue(new Callback<RealmUser>() {
        //            @Override
        //            public void onResponse(Call<RealmUser> call, Response<RealmUser> response) {
        //                if (!response.isSuccessful()) {
        //                    Log.w("ON_RESPONSE", "onResponse: failed, trying register user");
        //                    return;
        //                }
        //                Log.w("ON_LOGIN_RESPONSE", "onResponse: success " + response.body());
        //                RealmUser user = response.body();
        //                RealmDB.addUser(user);
        //                startMainActivity();
        //            }
        //
        //            @Override
        //            public void onFailure(Call<RealmUser> call, Throwable t) {
        //                if (t instanceof IOException) {
        //                    Log.d("ON_FAILURE", "Response Failed, IOException " + t.getMessage());
        //                } else {
        //                    Log.d("ON_FAILURE", "Response Failed " + t.getMessage());
        //                }
        //            }
        //        });
    }

    public boolean validateUser(String token) {
        Call<String> validateUser = service.validateUser("Bearer " + token);
        try {
            if (validateUser.execute().code() != 200) return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
