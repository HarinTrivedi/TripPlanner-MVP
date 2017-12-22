package com.hlabexamples.tripplanner.modules.start.signup;

import com.hlabexamples.commonmvp.base.mvp.presenter.BaseFormPresenter;
import com.hlabexamples.commonmvp.base.mvp.view.BaseBindingView;
import com.hlabexamples.commonmvp.base.mvp.view.BaseView;
import com.hlabexamples.tripplanner.databinding.ActivitySignupBinding;

/**
 * Created by H.T. on 04/12/17.
 */

final class SignupContract {
    interface SignupView extends BaseBindingView<ActivitySignupBinding> {
        void goToMainPage(String username);
    }

    interface SignupPresetner<T extends BaseView> extends BaseFormPresenter<T> {
        void performSignupTask(String username, String password);
    }

}
