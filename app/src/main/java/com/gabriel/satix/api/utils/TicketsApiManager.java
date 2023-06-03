package com.gabriel.satix.api.utils;

import android.content.Context;

import com.gabriel.satix.api.interfaces.TicketAPI;
import com.gabriel.satix.api.models.JoinEventRequest;
import com.gabriel.satix.api.models.TicketInfoQR;
import com.gabriel.satix.api.models.TicketQRRequest;
import com.gabriel.satix.models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicketsApiManager {
    private final TicketAPI ticketAPI;
    /**
     * Constructor de TicketsApiManager
     * @param context Contexto de la aplicación
     */
    public TicketsApiManager(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GET_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new TokenValidator(context).getOkHttpClient())
                .build();
        ticketAPI = retrofit.create(TicketAPI.class);
    }
    /**
     * Obtiene todos los eventos del usuario
     * @return Llamada a la lista de eventos
     */
    public Call<List<Event>> getAll() {
        return ticketAPI.getAllForUser();
    }
    /**
     * Obtiene el código QR de la entrada
     * @param request Solicitud del código QR de la entrada
     * @return Llamada al código QR del ticket
     */
    public Call<TicketInfoQR> getQR(TicketQRRequest request) {
        return ticketAPI.getQRTicket(request);
    }
    /**
     * Verifica si el usuario ya tiene una entrada para un evento específico
     * @param eventRequest Solicitud de unirse a un evento
     * @return Call para verificar si el usuario ya tiene una entrada
     */
    public Call<Boolean> alreadyHaveTicket(JoinEventRequest eventRequest) {
        return ticketAPI.alreadySigned(eventRequest);
    }
    /**
     * Metodo para que un usuario se una a un evento
     * @param eventRequest Solicitud de unirse a un evento
     * @return Llamada para unirse al evento
     */
    public Call<Boolean> joinEvent(JoinEventRequest eventRequest) {
        return ticketAPI.join(eventRequest);
    }




}

