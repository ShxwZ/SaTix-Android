package com.gabriel.satix.api.interfaces;

import com.gabriel.satix.api.models.VerifyRequest;
import com.gabriel.satix.api.models.VerifyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OperatorAPI {
    @POST("api/operator/verify")
    Call<VerifyResponse> validateTicket(@Body VerifyRequest request);
}
