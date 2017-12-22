package com.hlabexamples.tripplanner.modules.home.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hlabexamples.commonmvp.base.mvp.callback.IFirebaseCallbackListener;
import com.hlabexamples.commonmvp.data.PhotoModel;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.RowPhotoViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H.T. on 09/12/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<String> ids = new ArrayList<>();

    private List<PhotoModel> items = new ArrayList<>();
    private LayoutInflater inflater;
    private IFirebaseCallbackListener<PhotoModel> iFirebaseCallbackListener;

    private DetailFragment fragment;

    PhotoAdapter(DetailFragment fragment) {
        this.fragment = fragment;
        inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.iFirebaseCallbackListener = new IFirebaseCallbackListener<PhotoModel>() {
            @Override
            public void childAdded(PhotoModel trip) {
                ids.add(trip.getKey());
                items.add(trip);
                notifyItemInserted(items.size() - 1);
            }

            @Override
            public void childChanged(PhotoModel trip) {
                String id = trip.getKey();

                int index = ids.indexOf(id);
                if (index > -1) {
                    items.set(index, trip);
                    notifyItemChanged(index);
                }
            }

            @Override
            public void childRemoved(PhotoModel trip) {
                String id = trip.getKey();

                int index = ids.indexOf(id);
                if (index > -1) {
                    ids.remove(index);
                    items.remove(index);
                    notifyItemRemoved(index);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowPhotoViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_photo_view, parent, false);
        return new PhotoHolder(binding);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        PhotoModel model = items.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    IFirebaseCallbackListener<PhotoModel> getiFirebaseCallbackListener() {
        return iFirebaseCallbackListener;
    }

    public interface PhotoListener {
        void onClick();

        void onDelete();
        //Other listener methods
    }

    class PhotoHolder extends RecyclerView.ViewHolder implements PhotoListener {

        private RowPhotoViewBinding binding;

        PhotoHolder(RowPhotoViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(PhotoModel item) {
            binding.setPresenter(this);
            binding.setData(item);
//            ViewCompat.setTransitionName(binding.imgMain, "image_" + getAdapterPosition());
        }

        @Override
        public void onClick() {
            fragment.openPhoto(items.get(getAdapterPosition()).getUrl());
        }

        @Override
        public void onDelete() {
            fragment.deletePhoto(items.get(getAdapterPosition()).getKey());
        }

    }
}
