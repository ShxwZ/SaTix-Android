package com.gabriel.satix.api.interfaces;

import com.gabriel.satix.api.models.LoginRequest;
import com.gabriel.satix.api.models.LoginApiResponse;
import com.gabriel.satix.api.models.RegisterDTO;
import com.gabriel.satix.api.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAPI {
    @POST("api/auth/login")
    Call<LoginApiResponse> getLogin(@Body LoginRequest login);
    @POST("api/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterDTO register);
    @GET("/api/auth/validate")
    Call<Boolean> validateToken(@Query("token") String token);
}
