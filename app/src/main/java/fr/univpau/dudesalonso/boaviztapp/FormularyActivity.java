package fr.univpau.dudesalonso.boaviztapp;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.DialogGrapheManager;
import fr.univpau.dudesalonso.boaviztapp.formulary.ComponentManager;

public class FormularyActivity extends AppCompatActivity {

    public ComponentManager componentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        componentManager = new ComponentManager(this);

        setContentView(R.layout.formulary);

        focusRootWindow();

        componentManager.prepareUI();

        componentManager.requestManager.populateIfInternetAvailable();

    }

    public void focusRootWindow() {
        ((LinearLayout) findViewById(R.id.root)).requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void restartActivity(){
            finish();
            startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

}