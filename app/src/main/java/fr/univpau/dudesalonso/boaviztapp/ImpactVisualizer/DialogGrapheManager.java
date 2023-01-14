package fr.univpau.dudesalonso.boaviztapp.ImpactVisualizer;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

}
