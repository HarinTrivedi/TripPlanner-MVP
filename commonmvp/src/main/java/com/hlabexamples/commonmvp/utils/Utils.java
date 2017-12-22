package com.hlabexamples.commonmvp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Performs common utility operations.
 */
public class Utils {

    /**
     * Hide the soft keyboard from screen for edit text only
     *
     * @param activity context of current visible activity
     */
    public static void hideSoftKeyBoard(final Activity activity) {
        try {
            final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the soft keyboard from screen for edit text only
     *
     * @param context context of current visible activity
     * @param view    clicked view
     */
    public static void hideSoftKeyBoard(final Context context, final View view) {
        try {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs the share intent.
     */
    public static void shareItem(final Activity activity, final String url, final String chooserTitle) {
        if (!TextUtils.isEmpty(url) && activity != null) {
            final Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, chooserTitle));
        }
    }

    public static String getFormattedDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        return dateFormat.format(date);
    }
}
