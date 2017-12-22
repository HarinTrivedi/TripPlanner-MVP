package com.hlabexamples.tripplanner.modules.start.signup;

import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.base.mvp.validator.IFormValidator;
import com.hlabexamples.commonmvp.data.SignupModel;
import com.hlabexamples.commonmvp.impl.StartInteracter;

/**
 * Created by H.T. on 04/12/17.
 */

public class SignupPresetnerImpl implements SignupContract.SignupPresetner<SignupContract.SignupView>, ICallbackListener<String> {

    private IFormValidator formValidator;
    private SignupContract.SignupView view;
    private StartInteracter loginInteracter;

    SignupPresetnerImpl() {
        loginInteracter = new StartInteracter();
    }

    @Override
    public void addValidator(IFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    @Override
    public void attachView(SignupContract.SignupView view) {
        this.view = view;
    }

    @Override
    public void performSignupTask(String username, String password) {
        if (formValidator != null) {
            if (formValidator.validate()) {
                view.showProgress();
                loginInteracter.doSignup(new SignupModel(username, password), this);
            }
        }
    }

    @Override
    public void onSuccess(String data) {
        view.hideProgress();
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
