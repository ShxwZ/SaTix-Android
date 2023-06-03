package com.gabriel.satix.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gabriel.SaTix.BuildConfig;
import com.gabriel.SaTix.databinding.FragmentProfileBinding;
import com.gabriel.satix.api.utils.ApiKey;
import com.gabriel.satix.api.utils.TokenValidator;
import com.gabriel.satix.ui.LoginMenuActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    /**
     * Método llamado cuando se crea la vista del fragmento.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();

        return root;
    }
    /**
     * Método de inicialización del fragmento.
     */
    @SuppressLint("SetTextI18n")
    private void init(){
        Button buttonCloseSession = binding.button;
        buttonCloseSession.setOnClickListener(this::closeSession);
        if (getContext() != null)
            new TokenValidator(getContext()).validateToken(ApiKey.get(getContext()).getToken());
        TextView versionTextView = binding.versionTextView;
        String versionName = BuildConfig.VERSION_NAME;
        versionTextView.setText("V " + versionName);
        binding.username.setText("Usuario: " + ApiKey.get(getContext()).getUsername());
    }

    /**
     * Método para cerrar la sesión del usuario.
     */
    private void closeSession(View view){
        ApiKey.remove(Objects.requireNonNull(getActivity()));
        Toast.makeText(getActivity(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginMenuActivity.class);
        startActivity(intent);
        getActivity().finish();

    }
    /**
     * Método llamado cuando se destruye la vista del fragmento.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}