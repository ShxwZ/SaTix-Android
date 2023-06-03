package com.gabriel.satix.ui.tickets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gabriel.SaTix.R;
import com.gabriel.SaTix.databinding.FragmentTicketsBinding;
import com.gabriel.satix.api.utils.ApiKey;
import com.gabriel.satix.api.utils.TicketsApiManager;
import com.gabriel.satix.api.utils.TokenValidator;
import com.gabriel.satix.models.Event;
import com.gabriel.satix.models.adapters.RecordTicketAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketsFragment extends Fragment {

    private RecordTicketAdapter adapterTickets;
    private FragmentTicketsBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toast toast;
    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Inicializa los componentes del fragmento.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTicketsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();

        return root;
    }
    /**
     * Método llamado cuando se crea el fragmento.
     * Habilita las opciones de menú en la barra de acción.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.tickets);
        }

    }
    /**
     * Método llamado para crear el menú de opciones en la barra de acción.
     * Infla el menú para el botón del toolbar
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.info_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    /**
     * Método llamado cuando se selecciona un elemento del menú de opciones.
     * Muestra un mensaje de información.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info) {
            showToast(getString(R.string.info_entradas));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Método que realiza las inicializaciones necesarias.
     * Configura el adaptador y el diseño del RecyclerView.
     * Configura el SwipeRefreshLayout para actualizar los datos.
     */
    public void init() {
        binding.TicketsView.findViewById(R.id.TicketsView);
        binding.TicketsView.hasFixedSize();
        List<Event> eventsByUser = new ArrayList<>();
        binding.TicketsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterTickets = new RecordTicketAdapter(eventsByUser, getActivity());
        binding.TicketsView.setAdapter(adapterTickets);
        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setRefreshing(true);
        performDataUpdate();
        // Realiza la actualización de los datos en el RecyclerView
        swipeRefreshLayout.setOnRefreshListener(this::performDataUpdate);

    }
    /**
     * Método que establece la opacidad de la imagen de fondo en función del
     * número de elementos del adapter.
     */
    private void setOpacityImg(){
           binding.background.setVisibility(adapterTickets.getItemCount() != 0 ? View.GONE : View.VISIBLE);
    }
    /**
     * Método que realiza la actualización de los datos en el RecyclerView.
     * Obtiene la lista de eventos desde la API.
     * Actualiza el adaptador con los datos obtenidos.
     * Establece la opacidad de la imagen de fondo.
     * Notifica al SwipeRefreshLayout que la actualización ha finalizado.
     */
    private void performDataUpdate() {
        TicketsApiManager apiManager = new TicketsApiManager(getContext());
        Call<List<Event>> call = apiManager.getAll();
        if (call == null) return;
        call.enqueue(new Callback<List<Event>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    adapterTickets.setmData(response.body());
                    adapterTickets.notifyDataSetChanged();
                    setOpacityImg();
                }else {
                    new TokenValidator(getContext()).validateToken(ApiKey.get(Objects.requireNonNull(getContext())).getToken());

                }
                // Notifica al SwipeRefreshLayout que la actualización ha finalizado
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                // Notifica al SwipeRefreshLayout que la actualización ha finalizado
                showToast(getString(R.string.no_hay_conexi_n_a_internet_disponible));
                swipeRefreshLayout.setRefreshing(false);
                setOpacityImg();
            }
        });

    }
    /**
     * Método llamado cuando se destruye la vista del fragmento.
     * Limpia la referencia al enlace de datos.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
    /**
     * Método que muestra un mensaje emergente en forma de Toast.
     * Cancela el toast anterior si existe.
     * Muestra el nuevo toast con el mensaje proporcionado.
     */
    private void showToast(String msg){
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}