package com.gabriel.satix.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.gabriel.SaTix.R;
import com.gabriel.SaTix.databinding.ActivityLoginMenuBinding;
import com.gabriel.satix.MainActivity;
import com.gabriel.satix.api.models.LoginApiResponse;
import com.gabriel.satix.api.utils.ApiKey;
import com.gabriel.satix.api.utils.TokenLogin;
import com.gabriel.satix.ui.scanner.ScannerActivity;

import java.util.Objects;

public class LoginMenuActivity extends AppCompatActivity {

    private ActivityLoginMenuBinding binding;
    private EditText username, password;
    private Toast toast;
    /**
     * Método que se llama cuando se crea la actividad.
     * Configura la orientación de la pantalla como vertical.
     * Oculta la barra de acción.
     * Verifica si el usuario ya ha iniciado sesión previamente.
     * Inicializa los componentes de la actividad.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityLoginMenuBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);// forzar que no pongan modo oscuro
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        checkIfUserAlreadyLogin();
        init();
    }
    /**
     * Método que verifica si el usuario ya ha iniciado sesión previamente.
     * Obtiene el token y el rol del usuario almacenados.
     * Muestra un mensaje de bienvenida si hay un token y un rol válidos.
     * Inicia la actividad principal si el rol es "ROLE_ADMIN" o "ROLE_USER".
     * Ó
     * Inicia la actividad de operador si el rol es "ROLE_OPERATOR".
     */
    public void checkIfUserAlreadyLogin(){
        String key = ApiKey.get(this).getToken();
        String role = ApiKey.get(this).getAuthorities().get(0);

        if (key.isEmpty() || role.isEmpty()) return;
        showToast("Bienvenid@ de nuevo");
        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_USER")) {
            startMainActivity();
        } else if (role.equals("ROLE_OPERATOR")) {
            startOperatorActivity();
        }

    }
    /**
     * Método que inicia las variables necesarias;
     * Configura los listeners de los botones de inicio de sesión y registro.
     * Asigna las vistas de los campos de texto a las variables correspondientes.
     */
    public void init(){
        Button buttonLogin = binding.buttonLogin;
        Button buttonRegister = binding.buttonRegister;
        buttonLogin.setOnClickListener(this::loginButton);
        buttonRegister.setOnClickListener(this::registerButton);
        username = binding.usernameText;
        password = binding.passwordText;
    }

    /**
     * Método que se llama al hacer clic en el botón de inicio de sesión.
     */
    public void loginButton(View view){
        String _username = username.getText().toString();
        String _password = password.getText().toString();
        if (_username.isEmpty()){
            username.setError(getString(R.string.username_empty));
            return;
        } else if ( _password.isEmpty()) {
            password.setError(getString(R.string.password_empty));
            return;
        }
        binding.buttonLogin.setEnabled(false); // Deshabilita el botón de inicio de sesión.
        TokenLogin.getUserResponse(_username, _password,this, new TokenLogin.TokenCallback(){
            @Override
            public void onTokenReceived(LoginApiResponse response) {
                showToast("Bienvenid@ " + response.getUsername());
                String role = response.getAuthorities().get(0);
                ApiKey.save(
                        getApplicationContext(),
                        response.getUsername(),
                        response.getToken(),
                        response.getAuthorities().get(0));
                if (role.equals("ROLE_ADMIN") || role.equals("ROLE_USER")) {
                    startMainActivity(); // Inicia la actividad principal si el rol es "ROLE_ADMIN" o "ROLE_USER".
                }
                else if (role.equals("ROLE_OPERATOR")) {
                    startOperatorActivity(); // Inicia la actividad de operador si el rol es "ROLE_OPERATOR".
                }
                binding.buttonLogin.setEnabled(true); // Habilita nuevamente el botón de inicio de sesión.
            }

            @Override
            public void onTokenError(String errorMessage) {
                showToast(errorMessage);
                binding.buttonLogin.setEnabled(true);
            }
        });


    }

    /**
     * Método que se llama al hacer clic en el botón de registro.
     * Crea un intent para iniciar la actividad de registro.
     * Inicia la actividad de registro.
     */
    public void registerButton(View view){
        Intent intent = new Intent(LoginMenuActivity.this, RegisterMenuActivity.class);
        startActivity(intent);
    }
    /**
     * Método que muestra un mensaje emergente en forma de Toast.
     */
    public void showToast(String msg){
        if (toast != null) {
            toast.cancel(); // Cancela el toast anterior si existe.
        }
        toast = Toast.makeText(LoginMenuActivity.this, msg, Toast.LENGTH_SHORT); // Muestra el nuevo toast con el mensaje proporcionado.
        toast.show();
    }
    /**
     * Método que inicia la actividad principal para usuarios ADMIN y USER
     */
    public void startMainActivity(){
        Intent intent = new Intent(LoginMenuActivity.this,MainActivity.class); // Crea un intent para iniciar la actividad principal.
        startActivity(intent); // Inicia la actividad principal.
        finish(); // Finaliza la actividad actual.
    }
    /**
     * Método que inicia la actividad principal para usuarios OPERARIO
     */
    public void startOperatorActivity(){
        Intent intent = new Intent(LoginMenuActivity.this, ScannerActivity.class);
        startActivity(intent);
        finish();
    }
}