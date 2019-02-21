package com.kaim808.countdown.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaim808.countdown.model.Item;
import com.kaim808.countdown.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pointsTextView;
        TextView incrementTextView;
        TextView titleTextView;
        TextView timeTextView;
        TextView timePeriodTextView;
        SwitchCompat toggle;

        public ViewHolder(View itemView) {
            super(itemView);
            pointsTextView = itemView.findViewById(R.id.value_label);
            incrementTextView = itemView.findViewById(R.id.increment_label);
            titleTextView = itemView.findViewById(R.id.title_label);
            timeTextView = itemView.findViewById(R.id.formatted_time_label);
            timePeriodTextView = itemView.findViewById(R.id.time_period_label);
            toggle = itemView.findViewById(R.id.toggle);
        }
    }

    private List<Item> items;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.timePeriodTextView.setText(item.getTimePeriod().name());
        holder.timeTextView.setText(item.getFormattedTime());
        holder.titleTextView.setText(item.getTitle());
        holder.incrementTextView.setText(String.format("%s%s @ ", item.getIncrement() < 0 ? "-" : "+", String.valueOf(item.getIncrement())));
        holder.pointsTextView.setText(String.valueOf(item.getValue()));
        holder.toggle.setChecked(item.isActive());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
