package com.hlabexamples.commonmvp.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.data.TripModel;
import com.hlabexamples.commonmvp.data.UserModel;

import java.util.HashMap;
import java.util.Map;

import static com.hlabexamples.commonmvp.impl.DBFields.TRIPS;
import static com.hlabexamples.commonmvp.impl.DBFields.USERS;

/**
 * Created by H.T. on 06/12/17.
 */

public class AddTripInteracter {

    private static final String TAG = AddTripInteracter.class.getSimpleName();
    private final DatabaseReference mDatabase;
    private final String uid;

    public AddTripInteracter() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void addTrip(TripModel model, @NonNull ICallbackListener<Object> callbackListener) {
        mDatabase.child(USERS).child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserModel user = dataSnapshot.getValue(UserModel.class);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + uid + " is unexpectedly null");
                            callbackListener.onFailure(new Exception("User not found"));
                        } else {
                            String key = mDatabase.child(TRIPS).child(uid).push().getKey();
                            Map<String, Object> postValues = model.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/" + TRIPS + "/" + uid + "/" + key, postValues);
                            mDatabase.updateChildren(childUpdates);
                        }

                        callbackListener.onSuccess(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
