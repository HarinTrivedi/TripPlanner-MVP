package com.hlabexamples.commonmvp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Purpose of this Class is to display different dialog in application.
 */
public class DialogUtils {

    /**
     * Displays alert dialog to show common messages.
     *
     * @param title   Title of the Dialog : Application's Name
     * @param message Message to be shown in the Dialog displayed
     * @param context Context of the Application, where the Dialog needs to be displayed
     */
    public static void displayDialog(final Context context, final String title, final String message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), (dialog, which) -> dialog.dismiss());

        if (!((Activity) context).isFinishing()) {
            alertDialog.show();
        }
    }

    /**
     * Displays alert dialog to show common messages.
     *
     * @param title   Title of the Dialog : Application's Name
     * @param message Message to be shown in the Dialog displayed
     * @param context Context of the Application, where the Dialog needs to be displayed
     */
    public static void displayDialog(final Context context, final String title, final String message, final DialogInterface.OnClickListener clickListener) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            clickListener.onClick(dialog, which);
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(android.R.string.ok), (dialog, which) -> dialog.dismiss());
        if (!((Activity) context).isFinishing()) {
            alertDialog.show();
        }
    }

    public static ProgressDialog showProgressDialog(Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void hideProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static void displayToast(final Context context, final String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
