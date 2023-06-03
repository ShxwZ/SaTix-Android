package com.gabriel.satix.api.utils;

import android.content.Context;

import com.gabriel.satix.api.interfaces.EventAPI;
import com.gabriel.satix.models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsApiManager {
    private final EventAPI eventAPI;
    /**
     * Constructor de EventsApiManager
     * @param context Contexto de la aplicación
     */
    public EventsApiManager(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GET_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new TokenValidator(context).getOkHttpClient())
                .build();
        eventAPI = retrofit.create(EventAPI.class);
    }

    /**
     * Obtiene un evento por su ID
     * @param id ID del evento
     * @param callback Callback de llamada para obtener el evento
     */
    public void getDataById(String id, Callback<Event> callback) {
        Call<Event> call = eventAPI.find(id);
        call.enqueue(callback);
    }

    /**
     * Carga los datos de todos los eventos
     * @param callback callback de llamada para obtener la lista de eventos
     */
    public void loadData(Callback<List<Event>> callback) {
        Call<List<Event>> call = eventAPI.getAll();
        call.enqueue(callback);
    }



}
