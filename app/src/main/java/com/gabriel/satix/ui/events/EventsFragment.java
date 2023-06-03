package com.gabriel.satix.ui.events;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gabriel.SaTix.R;
import com.gabriel.SaTix.databinding.FragmentEventsBinding;
import com.gabriel.satix.api.utils.ApiKey;
import com.gabriel.satix.api.utils.EventsApiManager;
import com.gabriel.satix.api.utils.TokenValidator;
import com.gabriel.satix.models.Event;
import com.gabriel.satix.models.adapters.RecordEventAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;
    private RecordEventAdapter eventsAdapter;
    private  SwipeRefreshLayout swipeRefreshLayout;
    private Toast toast;
    public EventsFragment() {

    }
    /**
     * Método para crear la vista del fragmento.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }
    /**
     * Método de inicialización de variables.
     */
    private void init() {
        List<Event> events = new ArrayList<>();
        binding.EventRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsAdapter = new RecordEventAdapter(events, getActivity());
        binding.EventRecycleView.setAdapter(eventsAdapter);
        loadDataAPI();
        swipeRefreshLayout = binding.swipeRefreshLayout;

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Realiza la actualización de los datos en el RecyclerView
            loadDataAPI();
            if (getContext() != null)
                new TokenValidator(getContext()).validateToken(ApiKey.get(getContext()).getToken());
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    /**
     * Método para configurar las opciones del menú en el fragmento.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.info_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    /**
     * Método para manejar los eventos de los elementos del menú.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info) {
            showToast("Los eventos disponibles iran apareciendo aquí.");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Método para establecer la opacidad de la imagen de fondo en función de los eventos cargados.
     */
    private void setOpacityImg(){
        binding.background.setVisibility(eventsAdapter.getItemCount() != 0 ? View.GONE : View.VISIBLE);
    }
    /**
     * Método para cargar los datos desde la API.
     */
    private void loadDataAPI() {
        EventsApiManager apiManager = new EventsApiManager(getContext());
        apiManager.loadData(new Callback<List<Event>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    List<Event> events = response.body();
                    if (events != null) {
                        eventsAdapter.setmData(events);
                        eventsAdapter.notifyDataSetChanged();
                    }
                    setOpacityImg();
                } else {
                    showToast("Error en la respuesta de la API");
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                setOpacityImg();
                showToast(getString(R.string.no_hay_conexi_n_a_internet_disponible));
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataAPI();
    }
    /**
     * Método para mostrar un mensaje Toast.
     */
    private void showToast(String msg){
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}


