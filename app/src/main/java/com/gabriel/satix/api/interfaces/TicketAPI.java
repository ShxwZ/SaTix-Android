package com.gabriel.satix.api.interfaces;

import com.gabriel.satix.api.models.JoinEventRequest;
import com.gabriel.satix.api.models.TicketInfoQR;
import com.gabriel.satix.api.models.TicketQRRequest;
import com.gabriel.satix.models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TicketAPI {
    @GET("api/ticket/all-event-user")
    Call<List<Event>> getAllForUser();
    @POST("api/ticket")
    Call<TicketInfoQR> getQRTicket(@Body TicketQRRequest qrRequest);
    @POST("api/ticket/alreadysigned")
    Call<Boolean> alreadySigned(@Body JoinEventRequest joinRequest);
    @POST("api/ticket/join")
    Call<Boolean> join(@Body JoinEventRequest joinRequest);
}
