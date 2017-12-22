package com.hlabexamples.tripplanner.modules.home.browse;

import com.hlabexamples.commonmvp.base.mvp.view.BaseBindingView;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.tripplanner.databinding.FragmentBrowseAttractionsBinding;

/**
 * Created by H.T. on 06/12/17.
 */

public final class BrowseContract {
    interface BrowseView extends BaseBindingView<FragmentBrowseAttractionsBinding> {
        void fetchTrips(ItemType itemType);

        void refreshData();

        void showDetail(TripModel model);

        void changeFavorite(String id, int isFavourite);

        void deleteTrip(String key);
    }
}
