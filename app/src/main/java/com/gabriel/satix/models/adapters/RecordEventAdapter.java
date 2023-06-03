package com.gabriel.satix.models.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.SaTix.R;
import com.gabriel.satix.models.Event;
import com.gabriel.satix.ui.events.EventInfoActivity;

import java.util.List;


public class RecordEventAdapter extends RecyclerView.Adapter<ViewHolderEvent> {

    private List<Event> mData;
    private final LayoutInflater mInflater;
    private final Context context;
    // Establece los datos del adapter
    public void setmData(List<Event> mData) {
        this.mData = mData;
    }
    // Constructor del adapter
    public RecordEventAdapter(List<Event> mData, Context context) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    // Crea una nueva instancia de ViewHolderEvent
    @NonNull
    @Override
    public ViewHolderEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_list_item,parent,false);
        return new ViewHolderEvent(view);
    }
    // Vincula los datos de un evento a las vistas del ViewHolderEvent
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvent holder, int position) {
        Event event = mData.get(position);
        holder.setId(event.getId());
        holder.titleEvent.setText(event.getName());
        holder.counter.setText(event.getAssistants()+"/"+event.getMax_assistants());
        holder.dateEvent.setText(event.dateForEvent());
        holder.locationEvent.setText(event.getAddress());
        holder.setContext(this.context);

    }

    // Devuelve la cantidad de elementos en mData
    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class ViewHolderEvent extends RecyclerView.ViewHolder{
    Context context;
    Long id;
    TextView titleEvent,counter,dateEvent,locationEvent;


    public ViewHolderEvent(@NonNull View itemView) {
        super(itemView);
        titleEvent = itemView.findViewById(R.id.event_title);
        counter = itemView.findViewById(R.id.event_participantes);
        dateEvent = itemView.findViewById(R.id.event_date);
        locationEvent = itemView.findViewById(R.id.event_location);
        itemView.setOnClickListener(l ->{
            Intent intent = new Intent(context, EventInfoActivity.class);
            context.startActivity(intent);
        });
        itemView.setOnClickListener(l -> {
            Intent intent = new Intent(context, EventInfoActivity.class);
            intent.putExtra("EVENT_BY_ID", id);
            context.startActivity(intent);
        });
    }
    // Establece el ID del evento
    public void setId(Long id) {
        this.id = id;
    }
    // Establece el contexto
    public void setContext(Context context) {
        this.context = context;
    }
}