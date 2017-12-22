package com.hlabexamples.commonmvp.weardatalayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.hlabexamples.commonmvp.base.mvp.callback.IFirebaseCallbackListener;
import com.hlabexamples.commonmvp.data.TripModel;

import java.util.ArrayList;

/**
 * Created by H.T. on 18/12/17.
 */

public class DataLayerManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener {

    public static final String DATA_KEY = "data_key";
    public static final String DATA_ADD_KEY = "data_add_key";

    private final String TAG = DataLayerManager.class.getSimpleName();
    private static DataLayerManager dataLayerManager;

    private Context context;
    private final GoogleApiClient mGoogleApiClient;

    private IFirebaseCallbackListener<TripModel> onDataListener;

    private DataLayerManager(Context context) {
        this.context = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                // Request access only to the Wearable API
                .addApiIfAvailable(Wearable.API)
                .build();
    }

    public static DataLayerManager getInstance(Context context) {
        if (dataLayerManager == null) {
            dataLayerManager = new DataLayerManager(context);
        }
        return dataLayerManager;
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/trips") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ArrayList<TripModel> list = new ArrayList<>();
                    if (onDataListener != null)
                        onDataListener.childAdded(new TripModel(dataMap.getDataMap(DATA_KEY)));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/trips") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    ArrayList<TripModel> list = new ArrayList<>();
                    if (onDataListener != null)
                        onDataListener.childRemoved(new TripModel(dataMap.getDataMap(DATA_KEY)));
                }
            }
        }
    }


    public void sendStartActivityMessage(String node) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, node, DATA_ADD_KEY, new byte[0]).setResultCallback(
                sendMessageResult -> {
                    if (!sendMessageResult.getStatus().isSuccess()) {
                        Log.e(TAG, "Failed to send message with status code: "
                                + sendMessageResult.getStatus().getStatusCode());
                    }
                }
        );
    }

    public void sendData(TripModel data, ResultCallback<DataApi.DataItemResult> callback) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/trips");
        putDataMapReq.getDataMap().putDataMap(DATA_KEY, data.toDataMap());
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(callback);
    }

    public void onPause() {
        removeWearableConnection();
        mGoogleApiClient.disconnect();
    }

    public void onResume() {
        mGoogleApiClient.connect();
    }

    private void removeWearableConnection() {
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
    }

    private void addWearableConnection() {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: " + bundle);
        // Now you can use the Data Layer API
        addWearableConnection();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    public IFirebaseCallbackListener<TripModel> getOnDataListener() {
        return onDataListener;
    }

    public void setOnDataListener(IFirebaseCallbackListener<TripModel> onDataListener) {
        this.onDataListener = onDataListener;
    }
}
