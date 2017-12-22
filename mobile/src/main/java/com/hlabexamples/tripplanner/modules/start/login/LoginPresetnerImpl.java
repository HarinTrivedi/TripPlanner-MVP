package com.hlabexamples.tripplanner.modules.start.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.base.mvp.validator.IFormValidator;
import com.hlabexamples.commonmvp.data.LoginModel;
import com.hlabexamples.commonmvp.impl.StartInteracter;

/**
 * Created by H.T. on 04/12/17.
 */

public class LoginPresetnerImpl implements LoginContract.LoginPresetner<LoginContract.LoginView>, ICallbackListener<String> {

    private IFormValidator formValidator;
    private LoginContract.LoginView view;
    private StartInteracter loginInteracter;

    LoginPresetnerImpl() {
        loginInteracter = new StartInteracter();
    }

    @Override
    public void addValidator(IFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    @Override
    public void attachView(LoginContract.LoginView view) {
        this.view = view;
    }

    @Override
    public void performLoginTask(String username, String password) {
        if (formValidator != null) {
            if (formValidator.validate()) {
                view.showProgress();
                loginInteracter.doLogin(new LoginModel(username, password), this);
            }
        }
    }

    @Override
    public void performGoogleLoginTask(GoogleSignInAccount account) {
        loginInteracter.doGoogleLogin(account, this);
    }

    @Override
    public void onSuccess(String data) {
        view.hideProgress();
        formValidator.onValidationError("You're logged in");
        view.goToMainPage("");
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        formValidator.onValidationError("Login Error");
    }

    @Override
    public void onDestroy() {

    }
}
