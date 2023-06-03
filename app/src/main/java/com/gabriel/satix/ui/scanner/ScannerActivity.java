package com.gabriel.satix.ui.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.gabriel.SaTix.BuildConfig;
import com.gabriel.SaTix.R;
import com.gabriel.SaTix.databinding.ActivityScannerBinding;
import com.gabriel.satix.api.interfaces.OperatorAPI;
import com.gabriel.satix.api.models.VerifyRequest;
import com.gabriel.satix.api.models.VerifyResponse;
import com.gabriel.satix.api.utils.ApiKey;
import com.gabriel.satix.api.utils.Config;
import com.gabriel.satix.api.utils.TokenValidator;
import com.gabriel.satix.ui.LoginMenuActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScannerActivity extends AppCompatActivity {
    private ActivityScannerBinding binding;
    private final static int CAMARA_REQUEST_CODE = 101;
    private CodeScanner codeScanner;
    private Dialog loadingDialog;
    private OperatorAPI operatorAPI;
    private Toast toast;
    private boolean isActiveValidation = false;
    /**
     * Método para iniciar la actividad.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityScannerBinding.inflate(getLayoutInflater());
        // prohibir capturas
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(binding.getRoot());
        new TokenValidator(this).validateToken(ApiKey.get(this).getToken());
        init();
    }
    /**
     * Método para inciar variables y configuraciones.
     */
    private void init(){
        configToolBar();
        setupPermissions(); // set permissions for camera
        codeScanner(); // config n init the code scanner
        initVars();
    }
    /**
     * Método para inicializar variables.
     */
    @SuppressLint("SetTextI18n")
    private void initVars(){
        String username = ApiKey.get(this).getUsername();
        binding.usernameText.setText(username);
        String versionName = BuildConfig.VERSION_NAME;
        binding.infoText.setText("V " + versionName);
        loadingDialog = createLoadingDialog(ScannerActivity.this);
        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GET_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new TokenValidator(this).getOkHttpClient())
                .build();

        // Obtener instancia de la interfaz UserApi
        operatorAPI = retrofit.create(OperatorAPI.class);
    }
    /**
     * Método para configurar la barra de toolbar.
     */
    private void configToolBar() {
        Toolbar toolbar = binding.toolbar2;
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                ApiKey.remove(this);
                Intent intent = new Intent(this, LoginMenuActivity.class);
                startActivity(intent);
                finish();
                showToast(getString(R.string.cerraste_la_sesi_n));
                return true;
            } else if (item.getItemId() == R.id.action_restart_camera) {
                codeScanner.releaseResources();
                codeScanner.startPreview();
                showToast(getString(R.string.escaner_reiniciado));
            }
            return false;
        });
    }
    /**
     * Método para crear un diálogo de carga.
     */
    private Dialog createLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    /**
     * Método para configurar el escáner.
     */
    private void codeScanner() {
        CodeScannerView codeScannerView = binding.scannerView;
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);

        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            if (isActiveValidation) return;
            isActiveValidation = true;
            String [] data = result.getText().split(";");
            if (data.length == 3){
                loadingDialog.show();
                validateApi(new VerifyRequest(
                        data[0],
                        data[1],
                        data[2]
                ));
            } else {
                mostrarDialogo(this,getString(R.string.error_dialogo),getString(R.string.el_codigo_qr_no_es_valido));
            }
            codeScanner.releaseResources();
        }));

        codeScanner.setErrorCallback(thrown ->
                Log.e("Main_ERROR", "Camera initialization error: " + thrown.getMessage()));

        codeScannerView.setOnClickListener(v -> {
            //codeScanner.startPreview();
        });
    }
    /**
     * Método para mostrar un mensaje Toast.
     */
    public void showToast(String msg){
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Método para mostrar un diálogo personalizado.
     */
    public void mostrarDialogo(Context context, String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)  // Establece el título del diálogo
                .setIcon(R.drawable.ticket1)
                .setMessage(mensaje)  // Establece el mensaje del diálogo
                .setCancelable(false)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    codeScanner.startPreview();
                    isActiveValidation = false;
                    dialog.dismiss();  // Cierra el diálogo
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
    /**
     * Método para configurar los permisos de la cámara.
     */
    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }
    /**
     * Método para solicitar permisos de cámara al usuario.
     */
    private void makeRequest() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMARA_REQUEST_CODE);
    }
    /**
     * Método para controlar la respuesta de la solicitud de permisos de cámara.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMARA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // El usuario denegó el permiso de la cámara y seleccionó "No volver a preguntar"
                    // Muestra un mensaje al usuario explicando cómo habilitar manualmente el permiso

                    showToast( "El permiso de cámara está desactivado. Habilita el permiso en la configuración de la aplicación");

                    // Guía al usuario a la configuración de la aplicación para habilitar manualmente el permiso
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    // El usuario denegó el permiso de la cámara
                    // Muestra un mensaje al usuario indicando que el permiso es necesario para el funcionamiento de la aplicación
                    showToast("Necesitas permisos de la cámara para usar la aplicación");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * Método para crear el menú de opciones.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_operario, menu);
        return true;
    }
    /**
     * Método para validar un ticket a través de la API.
     */
    public void validateApi(VerifyRequest request) {
            Call<VerifyResponse> call = operatorAPI.validateTicket(request);
            call.enqueue(new Callback<VerifyResponse>() {
                @Override
                public void onResponse(@NonNull Call<VerifyResponse> call, @NonNull Response<VerifyResponse> response) {
                    loadingDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null){
                        VerifyResponse responseBody = response.body();
                        mostrarDialogo(
                                ScannerActivity.this,
                                getString(R.string.entrada_valida_dialogo),
                                String.format(
                                        "Evento: %s \n"
                                        + "Nombre: %s \n"
                                        + "Apellidos: %s %s\n"
                                        + "Teléfono: %s\n"
                                        + "DNI/NIE: %s",
                                        responseBody.getNameEvent(),
                                        responseBody.getName(),
                                        responseBody.getLastname1(),
                                        responseBody.getLastname2(),
                                        responseBody.getPhone(),
                                        responseBody.getDni())
                                );
                    } else if (response.code() == 401) {
                        new TokenValidator(ScannerActivity.this).validateToken(ApiKey.get(ScannerActivity.this).getToken());
                    } else {
                        mostrarDialogo(
                                ScannerActivity.this,
                                getString(R.string.error_dialogo),
                                getString(R.string.entrada_caducada));
                    }

                }

                @Override
                public void onFailure(@NonNull Call<VerifyResponse> call, @NonNull Throwable t) {
                    mostrarDialogo(
                            ScannerActivity.this,
                            "Reintentar",
                            "No se pudo establecer conexión. ");
                    loadingDialog.dismiss();
                }
            });
    }
}