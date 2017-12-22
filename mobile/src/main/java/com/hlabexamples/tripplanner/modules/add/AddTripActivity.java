package com.hlabexamples.tripplanner.modules.add;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.hlabexamples.commonmvp.base.BaseBindingActivity;
import com.hlabexamples.commonmvp.base.mvp.validator.FormValidator;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.commonmvp.utils.DialogUtils;
import com.hlabexamples.commonmvp.utils.Utils;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.ActivityAddTripBinding;

import java.util.Calendar;

/**
 * Created by H.T. on 05/12/17.
 */

public class AddTripActivity extends BaseBindingActivity<ActivityAddTripBinding> implements AddTripContract.AddTripView, View.OnClickListener {

    private AddTripContract.AddTripPresetner<AddTripContract.AddTripView> addTripPresenter;
    private TripModel model = new TripModel();

    private ProgressDialog progressDialog;

    private int[] images = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};

    @Override
    protected int attachView() {
        return R.layout.activity_add_trip;
    }

    @Override
    protected void initView() {
        initPresenter();

        getBinding().setListener(this);

        getBinding().spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeCategory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initToolbar() {
        getBinding().toolbar.setTitle("Add Trip");
        getBinding().toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        getBinding().toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void initPresenter() {
        addTripPresenter = new AddTripPresenterimpl();
        addTripPresenter.attachView(this);
        addTripPresenter.addValidator(new FormValidator() {
            @Override
            public boolean validate() {
                if (isEmptyField(getBinding().edTtitle.getText().toString(), "Add Title"))
                    return false;
                else if (TextUtils.isEmpty(model.getStartDate())) {
                    onValidationError("Select start date");
                    return false;
                } else if (TextUtils.isEmpty(model.getEndDate())) {
                    onValidationError("Select end date");
                    return false;
                }
                return true;
            }

            @Override
            public void onValidationSuccess() {

            }

            @Override
            public void onValidationError(String errorMessage) {
                DialogUtils.displayToast(AddTripActivity.this, errorMessage);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            Utils.hideSoftKeyBoard(this);
            selectStartData();
        } else if (v.getId() == R.id.btnEnd) {
            Utils.hideSoftKeyBoard(this);
            selectEndData();
        } else if (v.getId() == R.id.btnSave) {
            Utils.hideSoftKeyBoard(this);

            model.setTitle(getBinding().edTtitle.getText().toString());
            model.setStartDate(getBinding().btnStart.getText().toString());
            model.setEndDate(getBinding().btnEnd.getText().toString());
            addTripPresenter.performAddTripTask(model);
        }
    }

    @Override
    public void changeCategory(int type) {
        model.setImageId(type + 1);
        getBinding().img.setImageResource(images[type]);
    }

    @Override
    public void selectStartData() {
        showDataPicker((view, year, month, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            getBinding().btnStart.setText(Utils.getFormattedDate(c.getTime()));
        });
    }

    @Override
    public void selectEndData() {
        showDataPicker((view, year, month, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            getBinding().btnEnd.setText(Utils.getFormattedDate(c.getTime()));
        });
    }

    private void showDataPicker(DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void finishWithSuccess() {
        DialogUtils.displayToast(AddTripActivity.this, "Trip Added Successfully");
        finish();
    }

    @Override
    public void showMessage(String errorMessage) {
        DialogUtils.displayDialog(AddTripActivity.this, getString(R.string.alert), errorMessage);
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
        addTripPresenter.onDestroy();
    }

}
