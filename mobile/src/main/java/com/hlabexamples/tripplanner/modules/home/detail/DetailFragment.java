package com.hlabexamples.tripplanner.modules.home.detail;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.hlabexamples.commonmvp.base.BaseBindingFragment;
import com.hlabexamples.commonmvp.base.mvp.callback.ICallbackListener;
import com.hlabexamples.commonmvp.impl.DetailInteracter;
import com.hlabexamples.commonmvp.utils.DialogUtils;
import com.hlabexamples.commonmvp.utils.FileUtils;
import com.hlabexamples.commonmvp.utils.UploadProgressView;
import com.hlabexamples.tripplanner.R;
import com.hlabexamples.tripplanner.databinding.FragmentDetailBinding;
import com.hlabexamples.tripplanner.utils.Constants;
import com.hlabexamples.tripplanner.utils.SpacesItemDecoration;

import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by H.T. on 09/12/17.
 */

public class DetailFragment extends BaseBindingFragment<FragmentDetailBinding> implements DetailContract.DetailView, UploadProgressView {

    private static final int REQ_CHOOSE_GALLERY = 201;
    private static final int REQ_GALLEY_PERMISSION = 200;
    private DetailInteracter interacter;
    private String key;
    private PhotoAdapter adapter;
    private int[] images = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};

    private ProgressDialog uploadProgressDialog;

    @Override
    protected int attachView() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initView(View view) {
        checkArguments(getArguments());
        initRecyclerView();
        initPresenter();
        fetchPhotoTask();

        getBinding().fabPhoto.setOnClickListener(v -> dispatchGalleryIntent());
    }

    protected void checkArguments(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_KEY))
                key = bundle.getString(Constants.ARG_KEY);
            if (bundle.containsKey(Constants.ARG_URL)) {
                int imageId = bundle.getInt(Constants.ARG_URL);
                getBinding().imgTrip.setImageResource(images[imageId - 1]);
            }
        }
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        getBinding().rvPhotos.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.rv_spacing);
        getBinding().rvPhotos.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        //Set adapter
        adapter = new PhotoAdapter(this);
        getBinding().rvPhotos.setAdapter(adapter);

    }

    @Override
    public void initPresenter() {
        if (!TextUtils.isEmpty(key))
            interacter = new DetailInteracter(key, adapter.getiFirebaseCallbackListener());
    }

    /**
     * Open gallery intent
     */
    private void dispatchGalleryIntent() {
        int grant = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (grant == PERMISSION_GRANTED) {
            final Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            openGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            openGalleryIntent.setType("image/*");
            openGalleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(openGalleryIntent, REQ_CHOOSE_GALLERY);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_GALLEY_PERMISSION);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_CHOOSE_GALLERY) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    String path = FileUtils.getPath(getActivity(), selectedImageUri);
                    uploadPhoto(path);
                } else
                    showMessage("Image not found");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_GALLEY_PERMISSION && grantResults[0] == PERMISSION_GRANTED) {
            dispatchGalleryIntent();
        } else {
            showMessage("Gallery Permission Denied");
        }
    }

    private void uploadPhoto(String path) {
        interacter.uploadPhoto(path, this, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                //
            }

            @Override
            public void onFailure(Throwable t) {
                hideUploadProgressDialog();
                showMessage("Photo upload error");
            }
        });
    }

    @Override
    public void fetchPhotoTask() {
        interacter.getPhotos();
    }

    @Override
    public void deletePhoto(String id) {
        showMessage("Deleting...");
        interacter.deletePhoto(id, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                //
            }

            @Override
            public void onFailure(Throwable t) {
                showMessage("Error deleting photo");
            }
        });
    }

    @Override
    public void openPhoto(String url) {
    }

    @Override
    public void showMessage(String errorMessage) {
        DialogUtils.displayToast(getActivity(), errorMessage);
    }

    @Override
    public void showUploadProgressDialog() {
        uploadProgressDialog = new ProgressDialog(context);
        uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        uploadProgressDialog.setTitle("Uploading Photo");
        uploadProgressDialog.setMessage("Please wait...");
        uploadProgressDialog.setCancelable(false);
        uploadProgressDialog.show();
    }

    @Override
    public void hideUploadProgressDialog() {
        if (uploadProgressDialog != null) {
            uploadProgressDialog.dismiss();
        }
    }

    @Override
    public void showUploadProgress(int progress) {
        if (uploadProgressDialog != null) {
            uploadProgressDialog.setProgress(progress);
        }
    }

    @Override
    protected void initToolbar() {
        getBinding().toolbar.setNavigationOnClickListener(v -> getFragmentManager().popBackStack());
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
