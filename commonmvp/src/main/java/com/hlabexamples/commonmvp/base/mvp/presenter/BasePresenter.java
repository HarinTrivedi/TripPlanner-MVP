package com.hlabexamples.commonmvp.base.mvp.presenter;

import com.hlabexamples.commonmvp.base.mvp.view.BaseView;

/**
 * Created by H.T. on 01/12/17.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);

    void onDestroy();
}