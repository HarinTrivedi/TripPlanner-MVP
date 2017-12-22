package com.hlabexamples.commonmvp.base.mvp.callback;

/**
 * Created by H.T. on 01/12/17.
 */

public interface IFirebaseCallbackListener<T> {

    void childAdded(T trip);

    void childChanged(T trip);

    void childRemoved(T trip);

    void onFailure(Throwable t);
}
