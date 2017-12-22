package com.hlabexamples.commonmvp.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlabexamples.commonmvp.utils.PreferenceUtils;

/**
 * Created by H.T. on 01/12/17.
 */

public abstract class BaseFragment extends Fragment {

    public BaseActivity context;

    protected abstract int attachView();

    protected abstract void initView(View view);

    protected abstract void initToolbar();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(attachView(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            initToolbar();
    }

    protected PreferenceUtils getPreferences() {
        return context.preferences;
    }
}
