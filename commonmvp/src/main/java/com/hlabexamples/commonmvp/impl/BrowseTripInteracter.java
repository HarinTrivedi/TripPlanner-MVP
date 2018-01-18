package com.hlabexamples.commonmvp.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.base.mvp.callback.IFirebaseCallbackListener;
import com.hlabexamples.commonmvp.data.TripModel;

import static com.hlabexamples.commonmvp.impl.DBFields.TRIPS;

/**
 * Created by H.T. on 07/12/17.
 */

public class BrowseTripInteracter {

    private static final String TAG = BrowseTripInteracter.class.getSimpleName();
    private final DatabaseReference mDatabase;
    private final String uid;
    private final Query query;
    private final IFirebaseCallbackListener<TripModel> callbackListener;
    private ChildEventListener childEventListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            Log.i(TAG, "onChildAdded : " + prevChildKey);
            TripModel model = dataSnapshot.getValue(TripModel.class);
            if (model != null) {
                model.setId(dataSnapshot.getKey());
                callbackListener.childAdded(model);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            Log.i(TAG, "onChildChanged : " + prevChildKey);
            TripModel model = dataSnapshot.getValue(TripModel.class);
            if (model != null) {
                model.setId(dataSnapshot.getKey());
                callbackListener.childChanged(model);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.i(TAG, "onChildRemoved");
            TripModel model = dataSnapshot.getValue(TripModel.class);
            if (model != null) {
                model.setId(dataSnapshot.getKey());
                callbackListener.childRemoved(model);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            //---
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            callbackListener.onFailure(new Exception(databaseError.getMessage()));
        }
    };

    public BrowseTripInteracter(@NonNull IFirebaseCallbackListener<TripModel> callbackListener) {
        this.callbackListener = callbackListener;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        query = mDatabase.child(TRIPS).child(uid);
    }

    public void getTrips(int type) {
        query.removeEventListener(childEventListener);
        if (type == 0)
            query.orderByKey().addChildEventListener(childEventListener);
        else
            query.orderByChild("isFavourite").equalTo(1).addChildEventListener(childEventListener); // Fetch favorite trips

    }

    public void changeToFavorite(String id, int isFavourite) {
        mDatabase.child(TRIPS).child(uid).child(id).child("isFavourite").setValue(isFavourite);
    }

    public void deleteTrip(String id, @NonNull ICallbackListener listener) {
        mDatabase.child(TRIPS).child(uid).child(id).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                listener.onFailure(new Exception(databaseError.getMessage()));
            } else
                listener.onSuccess(null);
        });
    }

}
