package com.hlabexamples.tripplanner.modules.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.database.Exclude;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.commonmvp.utils.DialogUtils;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.ActivityBrowseAttractionsBinding;
import com.hlabexamples.tripplanner.modules.BaseWearableBindingActivity;

import java.util.ArrayList;
import java.util.List;

public class BrowseAttractionActivity extends BaseWearableBindingActivity<ActivityBrowseAttractionsBinding>
        implements BrowseContract.BrowseView, DataClient.OnDataChangedListener {

    private final String TAG = BrowseAttractionActivity.class.getSimpleName();

    private BrowseAttractionAdapter adapter;
//    private DataLayerManager dataLayerManager;

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
//        List<TripModel> tripModelList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            tripModelList.add(new TripModel("Trip "+i, "Start", "End"));
//        }
        adapter = new BrowseAttractionAdapter(this);
        getBinding().rvTrips.setAdapter(adapter);


    }

    @Override
    public void initPresenter() {
        addDataChangeListener();
//        dataLayerManager = new DataLayerManager(this);
    }

    @Override
    public void addDataChangeListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    Log.e(TAG, "TYPE_CHANGED");

                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ArrayList<DataMap> map = dataMap.getDataMapArrayList("key");
                    Log.e(TAG, "" + map.size());

                    List<TripModel> tripModelList = new ArrayList<>();
                    for (DataMap data : map) {
                        tripModelList.add(mapToData(data));
                    }
                    Toast.makeText(this, "Got : " + tripModelList.size(), Toast.LENGTH_SHORT).show();

//                    adapter.addItems(tripModelList);

//                    int a = dataMap.getInt(DataLayerManager.DATA_KEY);
//                    Toast.makeText(this, "Got : " + a, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, ""+a);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
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


    @Exclude
    public static DataMap toDataMap(TripModel model) {
        DataMap result = new DataMap();
        result.putString("id", model.getId());
        result.putString("title", model.getTitle());
        result.putString("startDate", model.getStartDate());
        result.putString("endDate", model.getEndDate());
        result.putInt("imageId", model.getImageId());
        result.putInt("isFavourite", model.isFavourite());
        return result;
    }

    @Exclude
    public static TripModel mapToData(DataMap data) {
        TripModel result = new TripModel();
        result.setId(data.getString("id"));
        result.setTitle(data.getString("title"));
        result.setStartDate(data.getString("startDate"));
        result.setEndDate(data.getString("endDate"));
        result.setImageId(data.getInt("imageId"));
        result.setFavourite(data.getInt("isFavourite"));
        return result;
    }

}
