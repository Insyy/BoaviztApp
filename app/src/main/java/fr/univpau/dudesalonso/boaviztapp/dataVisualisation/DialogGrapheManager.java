package fr.univpau.dudesalonso.boaviztapp.dataVisualisation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import fr.univpau.dudesalonso.boaviztapp.R;

public class DialogGrapheManager {

    public static boolean dialogZoom = true;
    public static boolean internetAvailable = true;
    public static View view;

    public static void showDialogZoom(Context c) {

        new MaterialAlertDialogBuilder(c).setTitle(c.getString(R.string.neutral_title)).setMessage(c.getString(R.string.zoom_message)).setNeutralButton(c.getString(R.string.neutral_action), (dialogInterface, i) -> {
        }).show();
    }

    public static void successfulDownload(Context c) {
        new MaterialAlertDialogBuilder(c).setTitle(c.getString(R.string.successfullTitleDownload)).setMessage(c.getString(R.string.successfullMainDownload)).setNeutralButton(c.getString(R.string.neutral_action), (dialogInterface, i) -> {
        }).show();
    }

    public static void failureDownload(Context c) {
        new MaterialAlertDialogBuilder(c).setTitle(c.getString(R.string.failureTitleDownload)).setMessage(c.getString(R.string.failureMainDownload)).setNeutralButton(c.getString(R.string.neutral_action), (dialogInterface, i) -> {
        }).show();
    }

    public static void startProgressIndicator() {

        LinearProgressIndicator progressIndicator = view.findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.VISIBLE);

    }

    public static void stopProgressIndicator() {
        LinearProgressIndicator progressIndicator = view.findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    public static void askForPerms(Context c) {
        ActivityCompat.requestPermissions((Activity) c, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
    }

    public static void showNetworkErrorToast(Activity activity, PostServerRequest psr) {
        Snackbar.make(activity.getWindow().findViewById(R.id.rootVisu),activity.getString(R.string.request_error_message),
                Snackbar.LENGTH_INDEFINITE).setAction(
                        R.string.toast_action_retry,
                        v -> psr.sendRequestServer()
                ).setAnchorView(
                    activity.getWindow().findViewById(R.id.bottom_navigation)
                ).show();
    }


}
