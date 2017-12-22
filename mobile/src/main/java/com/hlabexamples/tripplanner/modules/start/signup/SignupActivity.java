package com.hlabexamples.tripplanner.modules.start.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;

import com.hlabexamples.commonmvp.base.BaseBindingActivity;
import com.hlabexamples.commonmvp.base.mvp.validator.FormValidator;
import com.hlabexamples.commonmvp.data.SignupModel;
import com.hlabexamples.commonmvp.utils.DialogUtils;
import com.hlabexamples.commonmvp.utils.Utils;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.ActivitySignupBinding;
import com.hlabexamples.tripplanner.modules.home.MainActivity;
import com.hlabexamples.tripplanner.utils.Constants;

public class SignupActivity extends BaseBindingActivity<ActivitySignupBinding> implements SignupContract.SignupView, View.OnClickListener {

    private SignupModel signupModel;
    private SignupContract.SignupPresetner<SignupContract.SignupView> signupPresetner;

    private ProgressDialog progressDialog;

    @Override
    public int attachView() {
        return R.layout.activity_signup;
    }

    @Override
    public void initView() {
        initPresenter();

        getBinding().setListener(this);
        signupModel = new SignupModel();
        getBinding().setSignupModel(signupModel);
    }

    @Override
    public void initPresenter() {
        signupPresetner = new SignupPresetnerImpl();
        signupPresetner.attachView(this);
        signupPresetner.addValidator(new FormValidator() {

            @Override
            public boolean validate() {
                if (isEmptyField(signupModel.getUsername(), getString(R.string.val_enter_username))) {
                    getBinding().edtUsername.requestFocus();
                    return false;
                } else if (!isValidEmailId(signupModel.getUsername(), getString(R.string.val_enter_username))) {
                    getBinding().edtUsername.requestFocus();
                    return false;
                } else if (isEmptyField(signupModel.getPassword(), getString(R.string.val_enter_password))) {
                    getBinding().edtPassword.requestFocus();
                    return false;
                } else if (!isConfirmPasswordSame(signupModel.getPassword(), signupModel.getConfirmPassword(), getString(R.string.val_confirm_password))) {
                    getBinding().edtConfirmPassword.requestFocus();
                    return false;
                }
                return true;
            }

            @Override
            public void onValidationSuccess() {
                // if want to perform something on success
            }

            @Override
            public void onValidationError(String errorMessage) {
                DialogUtils.displayToast(SignupActivity.this, errorMessage);
            }

        });
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void onClick(View view) {
        if (view == getBinding().btnLogin) {
            Utils.hideSoftKeyBoard(this);
            signupPresetner.performSignupTask(signupModel.getUsername(), signupModel.getPassword());
        }
    }

    @Override
    public void goToMainPage(String username) {
        preferences.setBoolean(Constants.PREF_IS_LOGIN, true);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String errorMessage) {
        DialogUtils.displayDialog(SignupActivity.this, getString(R.string.alert), errorMessage);
    }

    @Override
    public void showProgress() {
        progressDialog = DialogUtils.showProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        DialogUtils.hideProgressDialog(progressDialog);
    }

    @Override
    public void showNoData() {

    }

    @Override
    public void destroy() {
        signupPresetner.onDestroy();
    }
}
