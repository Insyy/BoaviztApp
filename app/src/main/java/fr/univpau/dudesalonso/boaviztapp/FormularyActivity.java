package fr.univpau.dudesalonso.boaviztapp;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
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

import com.google.android.material.appbar.MaterialToolbar;

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
}