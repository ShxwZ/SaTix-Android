package com.gabriel.satix.models.adapters;

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
import com.gabriel.satix.ui.tickets.TicketMenuActivity;

import java.util.List;


public class RecordTicketAdapter extends RecyclerView.Adapter<ViewHolderTicket> {


    private List<Event> mData;
    private final LayoutInflater mInflater;
    private final Context context;

    public RecordTicketAdapter(List<Event> mData, Context context) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderTicket onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ticket_list_item, parent, false);
        return new ViewHolderTicket(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTicket holder, int position) {
        Event ticketEvent = mData.get(position);
        holder.setId(ticketEvent.getId());
        holder.titleEvent.setText(ticketEvent.getName());
        holder.dateEvent.setText(ticketEvent.dateForEvent());
        holder.locationEvent.setText(ticketEvent.getAddress());
        holder.setContext(this.context);

    }

    public void setmData(List<Event> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class ViewHolderTicket extends RecyclerView.ViewHolder {
    Context context;
    TextView titleEvent, dateEvent, locationEvent;
    Long id;

    public ViewHolderTicket(@NonNull View itemView) {
        super(itemView);
        titleEvent = itemView.findViewById(R.id.event_title);
        dateEvent = itemView.findViewById(R.id.event_date);
        locationEvent = itemView.findViewById(R.id.event_location);
        itemView.setOnClickListener(l -> {
            Intent intent = new Intent(context, TicketMenuActivity.class);
            intent.putExtra("EVENT_BY_ID", id);
            context.startActivity(intent);

        });
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setId(Long id) {
        this.id = id;
    }
}