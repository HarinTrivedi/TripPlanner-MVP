package com.hlabexamples.commonmvp.base.mvp.presenter;

import com.hlabexamples.commonmvp.base.mvp.validator.IFormValidator;
import com.hlabexamples.commonmvp.base.mvp.view.BaseView;

/**
 * Created by H.T. on 01/12/17.
 */

public interface BaseFormPresenter<T extends BaseView> extends BasePresenter<T> {
    void addValidator(IFormValidator formValidator);
}