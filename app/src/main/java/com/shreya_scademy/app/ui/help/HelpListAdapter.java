package com.shreya_scademy.app.ui.help;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shreya_scademy.app.databinding.AdapterHelpBinding;
import com.shreya_scademy.app.ui.batch.HelpResponseModel;
import com.shreya_scademy.app.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

/*----------------------------------------------------*/
public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.HelpViewHolder> {

    private List<HelpResponseModel.HelpModel> items;
    private ItemClickListener itemClickListener;

    public HelpListAdapter() {
        this.items = new ArrayList<>();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItems(List<HelpResponseModel.HelpModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HelpViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        AdapterHelpBinding binding = AdapterHelpBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new HelpViewHolder(binding);
    }

    public HelpResponseModel.HelpModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder holder, int i) {
        HelpResponseModel.HelpModel item = getItem(holder.getAbsoluteAdapterPosition());
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onClick(item, holder.getAbsoluteAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class HelpViewHolder extends RecyclerView.ViewHolder {

        private final AdapterHelpBinding binding;

        public HelpViewHolder(@NonNull AdapterHelpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
