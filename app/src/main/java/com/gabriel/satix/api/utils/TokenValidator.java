package com.gabriel.satix.api.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gabriel.SaTix.R;
import com.gabriel.satix.MainActivity;
import com.gabriel.satix.api.interfaces.UserAPI;
import com.gabriel.satix.ui.LoginMenuActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenValidator {

    private final UserAPI apiService;
    private final Context context;
    private final Activity activity;
    /**
     * Constructor de la clase TokenValidator
     * @param context Contexto de la aplicación
     */
    public TokenValidator(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GET_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.context = context;
        this.activity = (Activity) context;
        apiService = retrofit.create(UserAPI.class);
    }
    /**
     * Método para validar un token
     * @param token Token a validar
     */
    public void validateToken(String token) {
        Call<Boolean> call = apiService.validateToken(token);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean isValid = response.body();
                    if (Boolean.FALSE.equals(isValid)) {
                        redirectToLoginMenu();
                        ApiKey.remove(context);
                        Toast.makeText(context, "Sesión expirada.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // La petición no fue exitosa
                    ApiKey.remove(context);
                    redirectToLoginMenu();
                    Toast.makeText(context, "Error al intentar recuperar la sesión.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                // Ocurrió un error en la comunicación
                if (!isNetworkAvailable()) {
                    // No hay conexión a Internet
                    Toast.makeText(context, context.getString(R.string.no_hay_conexi_n_a_internet_disponible), Toast.LENGTH_SHORT).show();
                } else {
                    // Otro tipo de error
                    ApiKey.remove(context);
                    redirectToLoginMenu();
                }
            }
            /**
             * Método para verificar si hay conexión a Internet disponible
             * @return true si hay conexión a Internet, false de lo contrario
             */
            private boolean isNetworkAvailable() {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                // nuevas versiones no soportan el metodo isConnected y las antiguas necesitan esa.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                    return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                } else {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    return activeNetworkInfo != null && activeNetworkInfo.isConnected(); // aqui se ve que esta deprecado, es por los sdk
                }
            }

        });
    }
    /**
     * Método para redirigir a la pantalla de inicio de sesión
     */
    private void redirectToLoginMenu() {
        Intent intent = new Intent(context, LoginMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        activity.finish();
    }
    /**
     * Método para obtener un OkHttpClient configurado con el token de autenticación
     * @return OkHttpClient configurado con el token de autenticación
     */
    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + ApiKey.get(context).getToken());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        return httpClient.build();
    }
}
