package com.gabriel.satix.api.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gabriel.satix.api.models.LoginApiResponse;

import java.util.Collections;

public class ApiKey {

    private static final String PREFERENCE_API = "API_KEY";
    public static final String USERNAME = "USERNAME";
    public static final String TOKEN = "TOKEN";
    public static final String ROLE = "ROLE";
    /**
     * Elimina la clave de la API guardada en las sharedpreferences
     * @param context Contexto
     */
    public static void remove(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    /**
     * Guarda la clave de la API en las sharedpreferences
     * @param context Contexto
     * @param username Nombre de usuario
     * @param token Token de la API
     * @param role Rol del usuario
     */
    public static void save(Context context,String username,String token, String role){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN,token);
        editor.putString(USERNAME,username.trim());
        editor.putString(ROLE,role);
        editor.apply();
    }
    /**
     * Obtiene la clave de la API guardada en las sharedpreferences
     * @param context Contexto
     * @return Objeto LoginApiResponse que contiene la información de la clave de la API
     */
    public static LoginApiResponse get(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_API, Context.MODE_PRIVATE);
        return new LoginApiResponse(
                preferences.getString(USERNAME,""),
                Collections.singletonList(preferences.getString(ROLE, "")),
                preferences.getString(TOKEN,"")

        );
    }


}
