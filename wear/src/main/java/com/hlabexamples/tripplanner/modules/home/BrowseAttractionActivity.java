package com.hlabexamples.tripplanner.modules.home;

import android.support.v7.widget.LinearLayoutManager;

import com.hlabexamples.commonmvp.utils.DialogUtils;
import com.hlabexamples.commonmvp.weardatalayer.DataLayerManager;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.ActivityBrowseAttractionsBinding;
import com.hlabexamples.tripplanner.modules.BaseWearableBindingActivity;

public class BrowseAttractionActivity extends BaseWearableBindingActivity<ActivityBrowseAttractionsBinding> implements BrowseContract.BrowseView {

    private BrowseAttractionAdapter adapter;

    @Override
    protected int attachView() {
        return R.layout.activity_browse_attractions;
    }

    @Override
    protected void initView() {
        initRecyclerView();
        initPresenter();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        getBinding().rvTrips.setLayoutManager(layoutManager);

        //Set adapter
        adapter = new BrowseAttractionAdapter(this);
        getBinding().rvTrips.setAdapter(adapter);

    }

    @Override
    public void initPresenter() {
        //Interacter logic to make changes to adapter based on the firebase events
        addDataChangeListener();
    }

    @Override
    public void addDataChangeListener() {
        DataLayerManager.getInstance(getApplicationContext()).setOnDataListener(adapter.getiFirebaseCallbackListener());
    }

    @Override
    public void showMessage(String errorMessage) {
        DialogUtils.displayToast(this, errorMessage);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void destroy() {

    }

}
