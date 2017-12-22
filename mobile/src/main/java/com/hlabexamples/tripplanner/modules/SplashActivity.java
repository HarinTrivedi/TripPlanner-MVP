package com.hlabexamples.tripplanner.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hlabexamples.commonmvp.utils.PreferenceUtils;
import com.hlabexamples.tripplanner.modules.home.MainActivity;
import com.hlabexamples.tripplanner.modules.start.login.LoginActivity;
import com.hlabexamples.tripplanner.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity

        boolean isLogin = new PreferenceUtils(this).getBoolean(Constants.PREF_IS_LOGIN);
        if (isLogin)
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        else
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        // close splash activity

        finish();
    }
}
