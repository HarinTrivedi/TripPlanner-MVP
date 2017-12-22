package com.hlabexamples.commonmvp.base.mvp.view;

/**
 * Created by H.T. on 01/12/17.
 */

public interface ILoadingView {
    void showProgress();

    void hideProgress();

    void showNoData();

    void showMessage(String errorMessage);
}
