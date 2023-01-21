package fr.univpau.dudesalonso.boaviztapp.dataVisualisation;

import android.content.Context;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import fr.univpau.dudesalonso.boaviztapp.DataVisualisationActivity;
import fr.univpau.dudesalonso.boaviztapp.R;

public class DialogGrapheManager {

    public static boolean dialogZoom = true;

    public static void showDialogZoom(Context c){

        new MaterialAlertDialogBuilder(c)
                .setTitle(c.getString(R.string.neutral_title))
                .setMessage(c.getString(R.string.zoom_message))
                .setNeutralButton(c.getString(R.string.neutral_action), (dialogInterface, i) -> {})
                .show();
    }

    public static /*<DataVisualisationActivity extends View>*/ void successfulDownload(/*DataVisualisationActivity viewById*/ Context c){
       /* Snackbar.make(viewById, R.string.successfullTitleDownload,Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.rootVisu)
                .show();*/
        new MaterialAlertDialogBuilder(c)
                .setTitle(c.getString(R.string.successfullTitleDownload))
                .setMessage(c.getString(R.string.successfullMainDownload))
                .setNeutralButton(c.getString(R.string.neutral_action), (dialogInterface, i) -> {})
                .show();
    }

    public static void failureDownload(Context c){
        new MaterialAlertDialogBuilder(c)
                .setTitle(c.getString(R.string.failureTitleDownload))
                .setMessage(c.getString(R.string.failureMainDownload))
                .setNeutralButton(c.getString(R.string.neutral_action), (dialogInterface, i) -> {})
                .show();
        }

}
