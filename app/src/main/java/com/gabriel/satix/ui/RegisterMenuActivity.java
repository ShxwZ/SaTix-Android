package com.gabriel.satix.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gabriel.SaTix.R;
import com.gabriel.SaTix.databinding.ActivityRegisterMenuBinding;
import com.gabriel.satix.api.interfaces.UserAPI;
import com.gabriel.satix.api.models.RegisterDTO;
import com.gabriel.satix.api.models.RegisterResponse;
import com.gabriel.satix.api.utils.Config;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterMenuActivity extends AppCompatActivity {
    TextInputEditText username, password, passwordConfirmation, email, dni, name, lastname1, lastname2, phone, birthday;
    private ActivityRegisterMenuBinding binding;
    private UserAPI userApi;
    private String birthdayConverted;

    /**
     * Método que se llama cuando se crea la actividad.
     * Inicializa Retrofit y la interfaz UserApi.
     * Llama al método init() para realizar las inicializaciones necesarias.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GET_URL_API) // Reemplaza con la URL base de tu API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Obtener instancia de la interfaz UserApi
        userApi = retrofit.create(UserAPI.class);
        init();
    }
    /**
     * Método que realiza las inicializaciones necesarias.
     * Inicializa las variables y establece los listeners de los botones.
     * Limpia los campos de texto.
     */
    private void init() {
        initVars();
        binding.birthdayEditText.setOnClickListener(this::calendarPickerSetUp);
        binding.button2.setOnClickListener(this::registerButton);
        binding.button3.setOnClickListener(l -> finish());
        // Limpiar los campos de texto
        username.setText("");
        password.setText("");
        passwordConfirmation.setText("");
        email.setText("");
        dni.setText("");
        name.setText("");
        lastname1.setText("");
        lastname2.setText("");
        phone.setText("");
        birthday.setText("");

    }
    /**
     * Método que se llama al hacer clic en el botón de registro.
     * Obtiene los valores de los campos de texto.
     * Crea un objeto RegisterDTO con los valores obtenidos.
     * Llama al método registerUser() para registrar al usuario.
     */
    private void registerButton(View view) {
        String usernameTxt = Objects.requireNonNull(username.getText()).toString();
        String passwordTxt = Objects.requireNonNull(password.getText()).toString();
        String passwordConfirmationTxt = Objects.requireNonNull(passwordConfirmation.getText()).toString();
        String emailTxt = Objects.requireNonNull(email.getText()).toString();
        String dniTxt = Objects.requireNonNull(dni.getText()).toString();
        String nameTxt = Objects.requireNonNull(name.getText()).toString();
        String lastname1Txt = Objects.requireNonNull(lastname1.getText()).toString();
        String lastname2Txt = Objects.requireNonNull(lastname2.getText()).toString();
        String phoneTxt = Objects.requireNonNull(phone.getText()).toString();
        RegisterDTO registerDTO = new RegisterDTO(
                usernameTxt,
                passwordTxt,
                passwordConfirmationTxt,
                emailTxt,
                dniTxt,
                nameTxt,
                lastname1Txt,
                lastname2Txt,
                phoneTxt,
                birthdayConverted
        );
        registerUser(registerDTO);
    }
    /**
     * Método que configura el selector de fecha del cumpleaños.
     * Muestra el selector de fecha al hacer clic en el campo de texto del cumpleaños.
     * Guarda la fecha seleccionada en la variable birthdayConverted.
     */
    private void calendarPickerSetUp(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view1, year1, month1, dayOfMonth1) -> {
                    // Aquí puedes realizar acciones con la fecha seleccionada
                    @SuppressLint("DefaultLocale") String selectedDate = String.format("%02d/%02d/%d", dayOfMonth1, month1 + 1, year1);
                    birthday.setText(selectedDate);
                    birthdayConverted = selectedDate;
                },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }
    /**
     * Método que inicializa las variables de la interfaz de usuario.
     * Asigna las vistas de los campos de texto a las variables correspondientes.
     */
    public void initVars() {
        username = binding.usernameEditText;
        password = binding.passwordEditText;
        passwordConfirmation = binding.confirmPasswordEditText;
        email = binding.emailEditText;
        dni = binding.dniEditText;
        name = binding.nameEditText;
        lastname1 = binding.lastName1EditText;
        lastname2 = binding.lastName2EditText;
        phone = binding.phoneEditText;
        birthday = binding.birthdayEditText;

    }
    /**
     * Método que registra al usuario mediante una llamada a la API.
     * Realiza la llamada a la API utilizando el objeto RegisterDTO.
     * Maneja la respuesta de la API, mostrando mensajes de éxito o error.
     * Aplica mensajes de error a los campos correspondientes en caso de error de validación.
     */
    public void registerUser(RegisterDTO registerDTO) {
        Call<RegisterResponse> call = userApi.registerUser(registerDTO);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    // Registro exitoso, finalizar la actividad
                    Toast.makeText(RegisterMenuActivity.this, R.string.register_successful, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 400) {
                    Gson gson = new Gson();
                    RegisterResponse registerResponse;
                    assert response.errorBody() != null;
                    try {
                        registerResponse = gson.fromJson(response.errorBody().string(), RegisterResponse.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (registerResponse != null) {
                        Toast.makeText(RegisterMenuActivity.this, getString(R.string.error_revisa_los_campos), Toast.LENGTH_SHORT).show();
                        // Hubo errores en los campos, obtener los mensajes y aplicar a los EditText correspondientes
                        for (String fieldName : registerResponse.getErrors().keySet()) {
                            String errorMessage = registerResponse.getErrors().get(fieldName);
                            applyErrorToField(fieldName, errorMessage);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                // Manejar error de conexión u otro tipo de error
                Toast.makeText(RegisterMenuActivity.this, getString(R.string.register_no_available), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Método que aplica el mensaje de error al campo de texto correspondiente.
     * Recibe el nombre del campo y el mensaje de error.
     * Aplica el mensaje de error al campo de texto específico.
     */
    private void applyErrorToField(String fieldName, String errorMessage) {
        // Aplicar el mensaje de error al campo correspondiente
        switch (fieldName) {
            case "username":
                username.setError(errorMessage);
                break;
            case "password":
                password.setError(errorMessage);
                break;
            case "confirmPassword":
                passwordConfirmation.setError(errorMessage);
                break;
            case "email":
                email.setError(errorMessage);
                break;
            case "dni":
                dni.setError(errorMessage);
                break;
            case "name":
                name.setError(errorMessage);
                break;
            case "lastName1":
                lastname1.setError(errorMessage);
                break;
            case "lastName2":
                lastname2.setError(errorMessage);
                break;
            case "phone":
                phone.setError(errorMessage);
                break;
            case "birthday":
                birthday.setError(errorMessage);
                break;
            default:
                // Campo no reconocido
                break;
        }
    }


}