package com.hlabexamples.tripplanner.modules.home.detail;

import com.hlabexamples.commonmvp.base.mvp.view.BaseBindingView;
import com.hlabexamples.tripplanner.databinding.FragmentDetailBinding;

/**
 * Created by H.T. on 06/12/17.
 */

final class DetailContract {
    interface DetailView extends BaseBindingView<FragmentDetailBinding> {

        void fetchPhotoTask();

        void deletePhoto(String id);

        void openPhoto(String url);

    }
}
