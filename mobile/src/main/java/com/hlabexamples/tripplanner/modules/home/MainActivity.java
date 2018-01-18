package com.hlabexamples.tripplanner.modules.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;

import com.hlabexamples.commonmvp.base.BaseBindingActivity;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.ActivityMainBinding;
import com.hlabexamples.tripplanner.modules.home.browse.BrowseAttractionFragment;
import com.hlabexamples.tripplanner.modules.home.browse.ItemType;
import com.hlabexamples.tripplanner.utils.Constants;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                showFragmentWithType(ItemType.HOME);
                return true;
            case R.id.navigation_fav:
                showFragmentWithType(ItemType.FAV);
                return true;
        }
        return false;
    };

    @Override
    protected int attachView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.inflateMenu(R.menu.menu_home);
//        toolbar.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.action_watch) {
//                Fragment child = getFragmentManager().findFragmentByTag(BrowseAttractionFragment.class.getSimpleName());
//                if (child != null && child instanceof BrowseAttractionFragment) {
////                    increaseCounter(((BrowseAttractionFragment) child).getItems());
//                }
//                return true;
//            }
//            return false;
//        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void initToolbar() {

    }

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

    private void showFragmentWithType(ItemType type) {
        Fragment fragment = new BrowseAttractionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ARG_TYPE, type);
        fragment.setArguments(bundle);
        replaceFragment(R.id.container, fragment);
    }


    private void replaceFragment(int container, Fragment targetFragment) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.enter,
                        R.animator.exit,
                        0, 0)
                .replace(container, targetFragment, targetFragment.getClass().getSimpleName())
                .commit();
    }


}
