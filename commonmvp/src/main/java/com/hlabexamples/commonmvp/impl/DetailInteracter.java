package com.hlabexamples.commonmvp.impl;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.base.mvp.callback.IFirebaseCallbackListener;
import com.hlabexamples.commonmvp.data.PhotoModel;
import com.hlabexamples.commonmvp.utils.UploadProgressView;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.hlabexamples.commonmvp.impl.DBFields.TRIPS;
import static com.hlabexamples.commonmvp.impl.DBFields.TRIP_PHOTOS;

/**
 * Created by H.T. on 07/12/17.
 */

public class DetailInteracter {

    private static final String TAG = DetailInteracter.class.getSimpleName();
    private final DatabaseReference mDatabase;
    private final DatabaseReference query;
    private final FirebaseStorage storage;
    private final IFirebaseCallbackListener<PhotoModel> callbackListener;
    private String parentKey;
    private ChildEventListener childEventListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            Log.i(TAG, "onChildAdded : " + prevChildKey);
            PhotoModel model = dataSnapshot.getValue(PhotoModel.class);
            if (model != null) {
                model.setKey(dataSnapshot.getKey());
                callbackListener.childAdded(model);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            Log.i(TAG, "onChildChanged : " + prevChildKey);
            PhotoModel model = dataSnapshot.getValue(PhotoModel.class);
            if (model != null) {
                model.setKey(dataSnapshot.getKey());
                callbackListener.childChanged(model);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.i(TAG, "onChildRemoved");
            PhotoModel model = dataSnapshot.getValue(PhotoModel.class);
            if (model != null) {
                model.setKey(dataSnapshot.getKey());
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

    public DetailInteracter(final String parentKey, final @NonNull IFirebaseCallbackListener<PhotoModel> callbackListener) {
        this.parentKey = parentKey;
        this.callbackListener = callbackListener;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        query = mDatabase.child(TRIP_PHOTOS).child(parentKey);
    }

    public void getPhotos() {
        query.removeEventListener(childEventListener);
        query.orderByKey().addChildEventListener(childEventListener);
    }

    private void addPhoto(@NonNull final String key, @NonNull final String url) {
        PhotoModel photoModel = new PhotoModel();
        photoModel.setKey(key);
        photoModel.setParentKey(parentKey);
        photoModel.setUrl(url);
        Map<String, Object> postValues = photoModel.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + TRIP_PHOTOS + "/" + parentKey + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }

    public void uploadPhoto(@NonNull final String filePath, @NonNull final UploadProgressView view, @NonNull final ICallbackListener<String> listener) {
        try {
            String key = mDatabase.child(TRIP_PHOTOS).child(parentKey).push().getKey();
            File file = new File(filePath);

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .setCustomMetadata("Name", file.getName())
                    .build();

            StorageReference storageReference = storage.getReference().child("images/" + parentKey + "/" + key);
            UploadTask uploadTask = storageReference.putStream(new FileInputStream(file), metadata);

            view.showUploadProgressDialog();
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(
                    listener::onFailure)
                    .addOnSuccessListener(taskSnapshot -> {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        view.hideUploadProgressDialog();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (downloadUrl != null) {
                            Log.i(TAG, "Uploaded : " + downloadUrl);
                            addPhoto(key, downloadUrl.toString());
                            listener.onSuccess("Success");
                        }
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");
                        view.showUploadProgress((int) progress);
                    }).addOnPausedListener(taskSnapshot -> System.out.println("Upload is paused"));
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    public void deletePhoto(@NonNull final String id, @NonNull final ICallbackListener<String> listener) {
        StorageReference storageReference = storage.getReference().child("images/" + parentKey + "/" + id);
        storageReference.delete().addOnFailureListener(
                listener::onFailure)
                .addOnSuccessListener(taskSnapshot -> {
                    query.child(id).removeValue();
                    listener.onSuccess("Photo Deleted");
                });
    }

    private void deleteAllPhoto(@NonNull final String parentKey, @NonNull final ICallbackListener<Object> listener) {
        StorageReference storageReference = storage.getReference().child("images/" + parentKey);
        storageReference.delete().addOnFailureListener(
                listener::onFailure)
                .addOnSuccessListener(taskSnapshot -> {
                    listener.onSuccess(null);
                });
    }

    public void deleteTrip(@NonNull final String key, @NonNull final ICallbackListener<String> listener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (!TextUtils.isEmpty(uid)) {
            deleteAllPhoto(key, new ICallbackListener<Object>() {
                @Override
                public void onSuccess(Object data) {
                    listener.onSuccess("Trip Deleted");
                }

                @Override
                public void onFailure(Throwable t) {
                    listener.onFailure(new Exception("Error While Deleting "));
                }
            });
            mDatabase.child(TRIPS).child(uid).child(key).removeValue();
        } else
            listener.onFailure(new Exception("Error While Deleting "));
    }

}
