package com.hlabexamples.tripplanner.modules.start.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hlabexamples.commonmvp.base.BaseBindingActivity;
import com.hlabexamples.commonmvp.base.mvp.validator.FormValidator;
import com.hlabexamples.commonmvp.data.LoginModel;
import com.hlabexamples.commonmvp.utils.DialogUtils;
import com.hlabexamples.commonmvp.utils.Utils;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.ActivityLoginBinding;
import com.hlabexamples.tripplanner.modules.home.MainActivity;
import com.hlabexamples.tripplanner.modules.start.signup.SignupActivity;
import com.hlabexamples.tripplanner.utils.Constants;

public class LoginActivity extends BaseBindingActivity<ActivityLoginBinding> implements LoginContract.LoginView, View.OnClickListener {

    private final int RC_SIGN_IN = 101;
    private LoginModel loginModel;
    private LoginContract.LoginPresetner<LoginContract.LoginView> loginPresetner;
    private ProgressDialog progressDialog;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public int attachView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        initPresenter();

        getBinding().setListener(this);
        getBinding().signInButton.setOnClickListener(this);

        loginModel = new LoginModel();
        getBinding().setLoginModel(loginModel);
    }

    @Override
    public void initPresenter() {
        loginPresetner = new LoginPresetnerImpl();
        loginPresetner.attachView(this);
        loginPresetner.addValidator(new FormValidator() {

            @Override
            public boolean validate() {
                if (isEmptyField(loginModel.getUsername(), getString(R.string.val_enter_username))) {
                    getBinding().edtUsername.requestFocus();
                    return false;
                } else if (!isValidEmailId(loginModel.getUsername(), getString(R.string.val_enter_username))) {
                    getBinding().edtUsername.requestFocus();
                    return false;
                } else if (isEmptyField(loginModel.getPassword(), getString(R.string.val_enter_password))) {
                    getBinding().edtPassword.requestFocus();
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
                DialogUtils.displayToast(LoginActivity.this, errorMessage);
            }

        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void onClick(View view) {
        if (view == getBinding().btnLogin) {
            Utils.hideSoftKeyBoard(this);
            loginPresetner.performLoginTask(loginModel.getUsername(), loginModel.getPassword());
        } else if (view == getBinding().signInButton) {
            signInGoogle();
        } else if (view == getBinding().tvSignup) {
            goToSignup();
        }
    }

    @Override
    public void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginPresetner.performGoogleLoginTask(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e);
            }
        }
    }

    @Override
    public void goToSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToMainPage(String username) {
        preferences.setBoolean(Constants.PREF_IS_LOGIN, true);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.ARG_NAME, username);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String errorMessage) {
        DialogUtils.displayDialog(LoginActivity.this, getString(R.string.alert), errorMessage);
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
        loginPresetner.onDestroy();
    }
}
