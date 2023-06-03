package com.gabriel.satix.api.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gabriel.satix.api.interfaces.UserAPI;
import com.gabriel.satix.api.models.LoginApiResponse;
import com.gabriel.satix.api.models.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenLogin {
    /**
     * Interfaz para controlar la respuesta del token
     */
    public interface TokenCallback {
        void onTokenReceived(LoginApiResponse token);
        void onTokenError(String errorMessage);
    }
    /**
     * Obtiene la respuesta del usuario
     * @param username Nombre de usuario
     * @param password Contraseña del usuario
     * @param context Contexto de la aplicación
     * @param callback Callback para controlar la respuesta del token
     */
    public static void getUserResponse(String username, String password, Context context, TokenCallback callback) {
        getUserLogin(username, password, context, callback);
    }
    /**
     * Realiza la solicitud de inicio de sesión del usuario
     * @param username Nombre de usuario
     * @param password Contraseña del usuario
     * @param context Contexto de la aplicación
     * @param callback Callback para controlar la respuesta del token
     */
    private static void getUserLogin(String username, String password,Context context, TokenCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GET_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserAPI userAPI = retrofit.create(UserAPI.class);

        Call<LoginApiResponse> call = userAPI.getLogin(new LoginRequest(username, password));

        call.enqueue(new Callback<LoginApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginApiResponse> call, @NonNull Response<LoginApiResponse> response) {
                if (response.isSuccessful()) {
                    LoginApiResponse loginApiResponse = response.body();
                    if (loginApiResponse != null) {
                        callback.onTokenReceived(loginApiResponse);
                    } else {
                        callback.onTokenError("Login response body is null");
                        Toast.makeText(context, "Algo ha fallado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    callback.onTokenError("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginApiResponse> call, @NonNull Throwable t) {
                callback.onTokenError("Error al contactar con el servicio. Reintentelo.");
            }
        });
    }
}
