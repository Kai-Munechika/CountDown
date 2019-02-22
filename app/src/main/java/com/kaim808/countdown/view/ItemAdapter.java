package com.kaim808.countdown.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kaim808.countdown.MainActivity;
import com.kaim808.countdown.R;
import com.kaim808.countdown.model.Item;

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
    private MainActivity activity;

    public ItemAdapter(MainActivity activity, List<Item> items) {
        this.activity = activity;
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
        final Item item = items.get(position);

        holder.timePeriodTextView.setText(item.getTimePeriod().name());
        holder.timeTextView.setText(item.getFormattedTime());
        holder.titleTextView.setText(item.getTitle());
        holder.incrementTextView.setText(String.format("%s%s @ ", item.getIncrement() < 0 ? "-" : "+", String.valueOf(item.getIncrement())));
        holder.pointsTextView.setText(String.valueOf(item.getValue()));
        holder.toggle.setChecked(item.isActive());
        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setActive(isChecked);
                item.save();
                String message = isChecked ? "Increment scheduled daily for " + item.getFormattedTime() + (item.getTimePeriod() == Item.TimePeriod.AM ? "AM" : "PM") : "Counter deactivated";
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private Item recentlyDeletedItem;
    private int recentlyDeletedItemPosition;

    void deleteItem(int position) {
        recentlyDeletedItem = items.get(position);
        recentlyDeletedItemPosition = position;
        items.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.container);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    recentlyDeletedItem.delete();
                }
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        items.add(recentlyDeletedItemPosition, recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }

    Context getContext() {
        return activity;
    }
}
