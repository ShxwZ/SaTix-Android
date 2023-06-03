package com.gabriel.satix.ui.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gabriel.SaTix.R;
import com.gabriel.SaTix.databinding.ActivityEventInfoBinding;
import com.gabriel.satix.api.models.JoinEventRequest;
import com.gabriel.satix.api.utils.ApiKey;
import com.gabriel.satix.api.utils.EventsApiManager;
import com.gabriel.satix.api.utils.TicketsApiManager;
import com.gabriel.satix.models.Event;
import com.gabriel.satix.ui.tickets.TicketMenuActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfoActivity extends AppCompatActivity {


    private Long eventId;
    private Event event;
    private Context context;
    private ActivityEventInfoBinding binding;
    private Toast toast;
    /**
     * Método llamado cuando se crea la actividad.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityEventInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle(R.string.informaci_n_del_evento);
        toolbar.setNavigationIcon(R.drawable.baseline_close_24_white);
        toolbar.setNavigationOnClickListener(l -> finish());
        init();
    }
    /**
     * Método para inicializar variables.
     */
    public void init() {

        context = this;
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("EVENT_BY_ID")) {
                eventId = extras.getLong("EVENT_BY_ID");
                loadApiData();
            }
        }
        binding.buttonJoin.setOnClickListener(this::buttonJoin);

    }
    /**
     * Método para cargar los datos desde la API.
     */
    private void loadApiData() {
        EventsApiManager apiManager = new EventsApiManager(this);
        apiManager.getDataById(String.valueOf(eventId), new Callback<Event>() {
            @Override
            public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
                if (response.isSuccessful()) {
                    Event eventResponse = response.body();
                    if (eventResponse != null) {
                        event = eventResponse;
                        loadData();
                        if (event.getMin_age() >= 18) {
                            binding.textViewMayor.setVisibility(View.VISIBLE);
                            binding.textViewMayor.setText(R.string.dni_msg);
                        }
                        availableButton();
                    } else {
                        showToast("Evento no encontrado");
                        finish();
                    }
                } else {
                    showToast("Error al intentar obtener el evento");
                    finish();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {
                showToast("Error de red. Reintente de nuevo.");
                finish();

            }
        });


    }

    /**
     * Método para habilitar o deshabilitar el botón de unirse al evento según la disponibilidad.
     */
    private void availableButton() {
        TicketsApiManager apiManager = new TicketsApiManager(this);
        String username = ApiKey.get(this).getUsername();
        apiManager.alreadyHaveTicket(new JoinEventRequest(username, event.getId()))
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            boolean alreadyJoined = !response.body();
                            binding.buttonJoin.setEnabled(alreadyJoined && availableByAssistants());
                            setButtonText(!response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                        // Fail get api response
                    }
                });
    }
    /**
     * Método para establecer el texto del botón de unirse al evento.
     */
    private void setButtonText(boolean buttonStatus) {
        binding.buttonJoin.setText(
                !availableByAssistants() ?
                        getString(R.string.evento_completo) :
                        buttonStatus ?
                        getString(R.string.apuntarse) :
                        getString(R.string.already_signed));

    }
    /**
     * Método para verificar si hay disponibilidad de cupos para asistentes en el evento.
     */
    private boolean availableByAssistants() {
        return event.getAssistants() < event.getMax_assistants();
    }

    /**
     * Método llamado cuando se presiona el botón de unirse al evento.
     */
    private void buttonJoin(View view) {
        TicketsApiManager apiManager = new TicketsApiManager(this);
        String username = ApiKey.get(this).getUsername();
        apiManager.joinEvent(new JoinEventRequest(username, event.getId()))
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body()) {
                                redirectToTicket();
                                Toast.makeText(context, getString(R.string.joined_event), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(EventInfoActivity.this, getString(R.string.cant_join), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                        // Fail get api response
                        Toast.makeText(
                                        EventInfoActivity.this, R.string.try_later, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
    /**
     * Método para redirigir a la actividad de los tickets después de unirse al evento.
     */
    private void redirectToTicket() {
        Intent intent = new Intent(this, TicketMenuActivity.class);
        intent.putExtra("EVENT_BY_ID", eventId);
        intent.putExtra("NEW_TICKET",true);
        startActivity(intent);
    }
    /**
     * Método para cargar los datos del evento.
     */
    private void loadData() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateStart = LocalDateTime.parse(event.getStart_date(), formatter);
        LocalDateTime dateFinish = LocalDateTime.parse(event.getFinish_date(), formatter);
        binding.tituloEvento.setText(event.getName());
        binding.descripcion.setText(event.getDescription());
        binding.edadMinima.setText(String.valueOf(event.getMin_age()));
        String assistantCount = event.getAssistants() + getString(R.string.slash) + event.getMax_assistants();
        binding.participantes.setText(assistantCount);
        binding.fechaFin.setText(dateFinish.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        binding.fechaInicio.setText(dateStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        binding.ubicacion.setText(event.getAddress());


    }
    /**
     * Método para mostrar un mensaje Toast.
     */
    private void showToast(String msg){
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}