package com.gabriel.satix.ui.tickets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gabriel.SaTix.R;
import com.gabriel.SaTix.databinding.ActivityTicketMenuBinding;
import com.gabriel.satix.api.models.TicketInfoQR;
import com.gabriel.satix.api.models.TicketQRRequest;
import com.gabriel.satix.api.utils.ApiKey;
import com.gabriel.satix.api.utils.EventsApiManager;
import com.gabriel.satix.api.utils.TicketsApiManager;
import com.gabriel.satix.api.utils.TokenValidator;
import com.gabriel.satix.models.Event;
import com.gabriel.satix.utils.FullScreenDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketMenuActivity extends AppCompatActivity {

    private ActivityTicketMenuBinding binding;
    private Long eventId;
    private Event event;
    private Context context;

    private KonfettiView konfettiView = null;
    /**
     * Método llamado cuando se crea la actividad de menú de tickets.
     * Configura la orientación de la pantalla.
     * Inicializa los componentes de la actividad.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityTicketMenuBinding.inflate(getLayoutInflater());
        // prohibir capturas
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbarEntradas;
        toolbar.setTitle(R.string.entrada);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_close_24_white);
        toolbar.setNavigationOnClickListener(l -> finish());


        init();

    }
    /**
     * Método para iniciar las variables necesarias.
     * Obtiene la información del intend.
     */
    public void init(){
        context = this;
        konfettiView = findViewById(R.id.konfettiView);
        getIntentInfo();
    }

    /**
     * Metodo para mostrar el confeti en la pantalla
     */
    private void showKonfetti() {
        EmitterConfig emitterConfig = new Emitter(2, TimeUnit.SECONDS).perSecond(30);
        int color1 = Color.parseColor("#fce18a");
        int color2 = Color.parseColor("#ff726d");
        int color3 = Color.parseColor("#f4306d");
        int color4 = Color.parseColor("#b48def");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.tickets);
        assert drawable != null;
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true);
        konfettiView.start(new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT - 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(color1, color2, color3, color4))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(0.0, 0.5))
                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.LEFT + 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(color1, color2, color3, color4))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(1.0, 0.5))
                        .build());
    }

    /**
     * Método que obtiene la información del intend y carga los datos de la API.
     */
    private void getIntentInfo() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("EVENT_BY_ID")) {
                eventId = extras.getLong("EVENT_BY_ID");
                    loadApiData();
                binding.cardView.setOnClickListener(l -> showQR());
            }

        }
    }
    /**
     * Método que carga los datos de la API y muestra el código QR.
     */
    private void loadApiData(){
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
                        if (getIntent().getExtras().getBoolean("NEW_TICKET",false)){
                            showKonfetti();
                        }
                    } else {
                        Toast.makeText(context,getString( R.string.event_not_found), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.error_red, Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    /**
     * Método que carga los datos de la API y muestra el código QR en pantalla completa.
     */
    private void showQR() {
        TicketsApiManager ticketsApiManager = new TicketsApiManager(this);
        String username = ApiKey.get(this).getUsername();
        Call<TicketInfoQR> call = ticketsApiManager.getQR(new TicketQRRequest(username,String.valueOf(eventId)));
            call.enqueue(new Callback<TicketInfoQR>() {
                @Override
                public void onResponse(@NonNull Call<TicketInfoQR> call, @NonNull Response<TicketInfoQR> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String string_url = response.body().getUrl();  // URL de la imagen

                        FullScreenDialog fullScreenDialog = new FullScreenDialog(string_url);
                        fullScreenDialog.show(getSupportFragmentManager(), "full_screen_dialog");
                    } else if (response.code() == 401) {
                        new TokenValidator(context).validateToken(ApiKey.get(context).getToken());
                    } else {
                        Toast.makeText(context, getString(R.string.qr_not_available), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<TicketInfoQR> call, @NonNull Throwable t) {
                    Toast.makeText(TicketMenuActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
                }
            });
    }
    /**
     * Método que carga los datos del evento en la vista.
     */
    @SuppressLint("SetTextI18n")
    public void loadData(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateStart = LocalDateTime.parse(event.getStart_date(),formatter);
        LocalDateTime dateFinish = LocalDateTime.parse(event.getFinish_date(),formatter);
        binding.tituloEvento.setText(event.getName());
        binding.descripcion.setText(event.getDescription());
        binding.participantes.setText(event.getAssistants()+"/"+event.getMax_assistants());
        binding.edadMinima.setText(String.valueOf(event.getMin_age()));
        binding.fechaFin.setText(dateFinish.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        binding.fechaInicio.setText(dateStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        binding.ubicacion.setText(event.getAddress());


    }

}
