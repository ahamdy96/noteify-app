package com.ahamdy.note_ify.network;

import com.ahamdy.note_ify.models.NoteId;
import com.ahamdy.note_ify.models.RealmNote;
import com.ahamdy.note_ify.models.RealmUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebAppService {
    @GET("/note/{query}")
    Call<List<RealmNote>> getNotes(
            @Header("Authorization") String token,
            @Path("query") String query);

    @POST("/note/add")
    @FormUrlEncoded
    Call<NoteId> addNote(
            @Header("Authorization") String token,
            @Field("title") String title,
            @Field("body") String body,
            @Field("dateCreated") String dateCreated,
            @Field("dateModified") String dateModified);

    @PUT("/note/update")
    @FormUrlEncoded
    Call<NoteId> updateNote(
            @Header("Authorization") String token,
            @Field("_id") String id,
            @Field("title") String title,
            @Field("body") String body,
            @Field("dateModified") String dateModified);

    @DELETE("/note/delete")
    Call<NoteId> deleteNote(
            @Header("Authorization") String token,
            @Query("_id") String id);

    @POST("/user/register")
    @FormUrlEncoded
    Call<RealmUser> registerUser(
            @Field("email") String mail,
            @Field("password") String password);

    @POST("/user/login")
    @FormUrlEncoded
    Call<RealmUser> loginUser(
            @Field("email") String mail,
            @Field("password") String password);

    @POST("/user/validate")
    Call<String> validateUser(
            @Header("Authorization") String token
    );
}
