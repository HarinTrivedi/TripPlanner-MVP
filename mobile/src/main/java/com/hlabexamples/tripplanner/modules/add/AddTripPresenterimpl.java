package com.hlabexamples.tripplanner.modules.add;

import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.base.mvp.validator.IFormValidator;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.commonmvp.impl.AddTripInteracter;

/**
 * Created by H.T. on 06/12/17.
 */

public class AddTripPresenterimpl implements AddTripContract.AddTripPresetner<AddTripContract.AddTripView>, ICallbackListener<Object> {

    private AddTripContract.AddTripView view;
    private AddTripInteracter controller;
    private IFormValidator formValidator;

    public AddTripPresenterimpl() {
        controller = new AddTripInteracter();
    }

    @Override
    public void attachView(AddTripContract.AddTripView view) {
        this.view = view;
    }

    @Override
    public void addValidator(IFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    @Override
    public void performAddTripTask(TripModel model) {
        if (formValidator != null) {
            if (formValidator.validate()) {
                view.showProgress();
                controller.addTrip(model, this);
            }
        }
    }

    @Override
    public void onSuccess(Object data) {
        view.hideProgress();
        view.finishWithSuccess();
    }

    @Override
    public void onFailure(Throwable t) {
        view.hideProgress();
        formValidator.onValidationError("Add Trip Error");
    }

    @Override
    public void onDestroy() {

    }
}
