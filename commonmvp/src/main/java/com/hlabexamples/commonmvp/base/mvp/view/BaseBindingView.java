package com.hlabexamples.commonmvp.base.mvp.view;

import android.databinding.ViewDataBinding;

/**
 * Created by H.T. on 01/12/17.
 */

public interface BaseBindingView<T extends ViewDataBinding> extends BaseView {
    T getBinding();
}
