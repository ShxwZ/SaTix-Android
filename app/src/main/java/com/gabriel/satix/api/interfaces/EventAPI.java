package com.gabriel.satix.api.interfaces;

import com.gabriel.satix.models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EventAPI {
    @GET("api/event/{id}")
    Call<Event> find(@Path("id") String id);
    @GET("api/event")
    Call<List<Event>> getAll();
}
