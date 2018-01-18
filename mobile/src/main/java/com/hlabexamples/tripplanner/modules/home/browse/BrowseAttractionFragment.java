package com.hlabexamples.tripplanner.modules.home.browse;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.hlabexamples.commonmvp.base.BaseBindingFragment;
import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.commonmvp.impl.BrowseTripInteracter;
import com.hlabexamples.commonmvp.utils.DialogUtils;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.FragmentBrowseAttractionsBinding;
import com.hlabexamples.tripplanner.modules.add.AddTripActivity;
import com.hlabexamples.tripplanner.modules.home.detail.DetailFragment;
import com.hlabexamples.tripplanner.modules.start.login.LoginActivity;
import com.hlabexamples.tripplanner.utils.Constants;

import java.util.List;

public class BrowseAttractionFragment extends BaseBindingFragment<FragmentBrowseAttractionsBinding> implements BrowseContract.BrowseView,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private BrowseAttractionAdapter adapter;
    private ItemType itemType;

    private BrowseTripInteracter interacter;

    @Override
    protected int attachView() {
        return R.layout.fragment_browse_attractions;
    }

    @Override
    protected void initView(View view) {
        getBinding().swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        getBinding().swipeRefreshLayout.setOnRefreshListener(this);
        getBinding().btnAdd.setOnClickListener(this);

        checkArguments(getArguments());
        initRecyclerView();
        initPresenter();
        fetchTrips(itemType);
    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_watch) {
                // TODO: 18/01/18 pending
//                Fragment child = getFragmentManager().findFragmentByTag(BrowseAttractionFragment.class.getSimpleName());
//                if (child != null && child instanceof BrowseAttractionFragment) {
//                    increaseCounter(((BrowseAttractionFragment) child).getItems());
//                }
                return true;
            } else if (item.getItemId() == R.id.action_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(context, LoginActivity.class));
                context.finish();
                return true;
            }
            return false;
        });
    }

    protected void checkArguments(Bundle bundle) {
        if (bundle != null && bundle.containsKey(Constants.ARG_TYPE))
            itemType = (ItemType) bundle.getSerializable(Constants.ARG_TYPE);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        getBinding().frBrowseRecyclerView.setLayoutManager(layoutManager);

        //Set adapter
        adapter = new BrowseAttractionAdapter(this);
        getBinding().frBrowseRecyclerView.setAdapter(adapter);

    }

    @Override
    public void initPresenter() {
        //Interacter logic to make changes to adapter based on the firebase events
        interacter = new BrowseTripInteracter(adapter.getiFirebaseCallbackListener());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            Intent intent = new Intent(getActivity(), AddTripActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void fetchTrips(ItemType itemType) {
        showProgress();
        interacter.getTrips(itemType.getType());

        new Handler().postDelayed(this::hideProgress, 2000);
    }

    @Override
    public void refreshData() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        fetchTrips(itemType);
    }

    @Override
    public void showDetail(TripModel model) {
        Fragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARG_KEY, model.getId());
        bundle.putInt(Constants.ARG_URL, model.getImageId());
        fragment.setArguments(bundle);
        context.addFragment(R.id.container, this, fragment);

    }

    @Override
    public void changeFavorite(String key, int isFavourite) {
        interacter.changeToFavorite(key, isFavourite);
    }

    @Override
    public void deleteTrip(final String key) {
        DialogUtils.displayDialog(context, getString(R.string.alert), getString(R.string.str_delete_trip),
                (dialog, which) -> interacter.deleteTrip(key, new ICallbackListener() {
                    @Override
                    public void onSuccess(Object data) {
                        showMessage("Trip Deleted");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showMessage("Error Deleting Trip");
                    }
                }));
    }

    public void showProgress() {
        if (!getBinding().swipeRefreshLayout.isRefreshing()) {
            getBinding().frBrowseRecyclerView.setVisibility(View.GONE);
            getBinding().frBrowseLoadingProgress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        getBinding().frBrowseLoadingProgress.setVisibility(View.GONE);
        getBinding().frBrowseRecyclerView.setVisibility(View.VISIBLE);
        if (getBinding().swipeRefreshLayout.isRefreshing())
            getBinding().swipeRefreshLayout.setRefreshing(false);
    }

    // TODO: 18/01/18 pending
//    private void increaseCounter(List<TripModel> items) {
//        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");
//
//        Log.e("App", "Sending "+items.size());
//
//        ArrayList<DataMap> map = new ArrayList<>();
//        for (TripModel model : items) {
//            map.add(DataLayerManager.toDataMap(model));
//        }
//        putDataMapReq.getDataMap().putDataMapArrayList(DataLayerManager.DATA_KEY, map);
////        putDataMapReq.getDataMap().putInt(DataLayerManager.DATA_KEY, 123);
//        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
//        putDataReq.setUrgent();
//
//        Task<DataItem> putDataTask = Wearable.getDataClient(this).putDataItem(putDataReq);
//        putDataTask
//                .addOnSuccessListener(dataItem -> Toast.makeText(MainActivity.this, "Sent to watch", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show());
//    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showMessage(String errorMessage) {
        DialogUtils.displayToast(getActivity(), errorMessage);
    }

    @Override
    public void destroy() {

    }

    public List<TripModel> getItems() {
        return adapter.getItems();
    }
}
