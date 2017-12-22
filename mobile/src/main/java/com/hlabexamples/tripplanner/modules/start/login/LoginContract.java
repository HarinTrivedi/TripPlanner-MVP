package com.hlabexamples.tripplanner.modules.start.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.hlabexamples.commonmvp.base.mvp.presenter.BaseFormPresenter;
import com.hlabexamples.commonmvp.base.mvp.view.BaseBindingView;
import com.hlabexamples.commonmvp.base.mvp.view.BaseView;
import com.hlabexamples.tripplanner.databinding.ActivityLoginBinding;

/**
 * Created by H.T. on 04/12/17.
 */

final class LoginContract {
    interface LoginView extends BaseBindingView<ActivityLoginBinding> {
        void signInGoogle();

        void goToSignup();

        void goToMainPage(String username);
    }

    interface LoginPresetner<T extends BaseView> extends BaseFormPresenter<T> {
        void performLoginTask(String username, String password);

        void performGoogleLoginTask(GoogleSignInAccount account);
    }
}
