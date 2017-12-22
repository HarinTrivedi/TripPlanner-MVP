package com.hlabexamples.tripplanner.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.hlabexamples.tripplanner.R;

public class CommonBindingUtils {

    /**
     * Example 1/1
     * To show image from drawable
     *
     * @param imageView
     * @param drawableId
     * @param placeholder
     */
    @BindingAdapter(value = {"drawableId", "placeholder"}, requireAll = false)
    public static void setDrawable(ImageView imageView, int drawableId, Drawable placeholder) {
        if (drawableId != 0) {
            RequestBuilder<Bitmap> builder = Glide.with(imageView.getContext()).asBitmap();
            RequestOptions options = new RequestOptions().placeholder(placeholder).error(placeholder);
            Context c = imageView.getContext();
            int id = c.getResources().getIdentifier("img_" + drawableId, "drawable", c.getPackageName());
            builder.apply(options).load(id).into(imageView);
        }
    }

    @BindingAdapter(value = "isFavourite")
    public static void setFavorite(ImageView view, int isFavourite) {
        view.setImageResource(isFavourite == 1 ? R.drawable.ic_heart : R.drawable.ic_heart_white);
    }
}
