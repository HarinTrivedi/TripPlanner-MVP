package com.hlabexamples.tripplanner.modules.add;

import com.hlabexamples.commonmvp.base.mvp.presenter.BaseFormPresenter;
import com.hlabexamples.commonmvp.base.mvp.view.BaseBindingView;
import com.hlabexamples.commonmvp.base.mvp.view.BaseView;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.tripplanner.databinding.ActivityAddTripBinding;

/**
 * Created by H.T. on 04/12/17.
 */

final class AddTripContract {
    interface AddTripView extends BaseBindingView<ActivityAddTripBinding> {
        void changeCategory(int type);

        void selectStartData();

        void selectEndData();

        void finishWithSuccess();
    }

    interface AddTripPresetner<T extends BaseView> extends BaseFormPresenter<T> {
        void performAddTripTask(TripModel model);
    }
}
