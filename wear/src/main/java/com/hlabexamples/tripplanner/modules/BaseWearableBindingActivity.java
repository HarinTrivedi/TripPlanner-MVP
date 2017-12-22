package com.hlabexamples.tripplanner.modules;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;

import com.hlabexamples.commonmvp.utils.PreferenceUtils;

/**
 * Created by H.T. on 01/12/17.
 */

public abstract class BaseWearableBindingActivity<T extends ViewDataBinding> extends WearableActivity {
    public PreferenceUtils preferences;
    T binding;

    protected abstract int attachView();

    protected abstract void initView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new PreferenceUtils(this);

        binding = DataBindingUtil.setContentView(this, attachView());
        initView();
    }

    public T getBinding() {
        return binding;
    }
}
