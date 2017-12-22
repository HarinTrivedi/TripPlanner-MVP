package com.hlabexamples.tripplanner.modules.home;

import com.hlabexamples.commonmvp.base.mvp.view.BaseBindingView;
import com.hlabexamples.tripplanner.databinding.ActivityBrowseAttractionsBinding;

/**
 * Created by H.T. on 06/12/17.
 */

final class BrowseContract {
    interface BrowseView extends BaseBindingView<ActivityBrowseAttractionsBinding> {
        void addDataChangeListener();
    }
}
