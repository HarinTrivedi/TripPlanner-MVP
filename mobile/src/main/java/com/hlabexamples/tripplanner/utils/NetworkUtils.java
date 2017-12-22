package com.hlabexamples.tripplanner.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hlabexamples.tripplanner.R;

/**
 * Purpose of this Class is to check internet connection of phone and perform actions on user's input
 */
public class NetworkUtils {

    /**
     * Checks internet network connection.
     *
     * @param context    Activity context
     * @param message    if want to show connection message to user then true, false otherwise.
     * @param goSettings if want to go action setting for connection then true, otherwise only OK button.
     * @return if network connectivity exists or is in the process of being established, false otherwise.
     */
    public static boolean isOnline(final Activity context, boolean message, boolean goSettings) {
        if (context != null && !context.isFinishing()) {
            if (isNetworkAvailable(context)) {
                return true;
            }

            if (message) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle(context.getString(R.string.app_name));
                dialog.setCancelable(false);
                dialog.setMessage(context.getString(R.string.alert_no_connection));

                if (goSettings) {
                    dialog.setPositiveButton(context.getString(R.string.settings), (dialog1, id) -> {
                        dialog1.dismiss();
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    });

                    dialog.setNegativeButton(context.getString(R.string.cancel), (dialog12, id) -> dialog12.dismiss());

                } else {
                    dialog.setNeutralButton(context.getString(R.string.ok), (dialog13, id) -> dialog13.dismiss());
                }
                dialog.show();
            }
        }
        return false;
    }

    /**
     * Checks the Network availability.
     *
     * @return true if internet available, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
