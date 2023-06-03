package com.gabriel.satix.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gabriel.SaTix.R;
import com.squareup.picasso.Picasso;

public class FullScreenDialog extends AppCompatDialogFragment {
    private final String string_url;

    public FullScreenDialog(String string_url) {
        this.string_url = string_url;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.FullScreenDialog);

        View view = getLayoutInflater().inflate(R.layout.show_qr_ticket, null);
        ImageView imageView = view.findViewById(R.id.imageView_QR);  // Referencia al ImageView
        Picasso.get()
                .load(string_url)
                .into(imageView);
        builder.setView(view);
        view.findViewById(R.id.button).setOnClickListener(v -> dismiss());
        return builder.create();
    }
}
