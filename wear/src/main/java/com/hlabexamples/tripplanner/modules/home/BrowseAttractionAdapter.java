package com.hlabexamples.tripplanner.modules.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.RowItemAttractionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created in BindingConstraintMVP-Demo on 11/01/17.
 */

public class BrowseAttractionAdapter extends RecyclerView.Adapter<BrowseAttractionAdapter.BrowseItemHolder> {

    private List<TripModel> items = new ArrayList<>();
    private LayoutInflater inflater;

    BrowseAttractionAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public BrowseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowItemAttractionBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_item_attraction, parent, false);
        return new BrowseItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(BrowseItemHolder holder, int position) {
        TripModel model = items.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<TripModel> items) {
        if (items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    class BrowseItemHolder extends RecyclerView.ViewHolder {

        private RowItemAttractionBinding binding;

        BrowseItemHolder(RowItemAttractionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TripModel item) {
            binding.setData(item);
        }

    }

}
