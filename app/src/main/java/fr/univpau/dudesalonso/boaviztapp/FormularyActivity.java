package fr.univpau.dudesalonso.boaviztapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import fr.univpau.dudesalonso.boaviztapp.formulary.ComponentManager;

public class FormularyActivity extends AppCompatActivity {

    public ComponentManager componentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        componentManager = new ComponentManager(this);

        setContentView(R.layout.formulary);

        focusRootWindow();

        //setOnOrientationChangeListener();

        componentManager.prepareUI();
    }

    private void setOnOrientationChangeListener() {
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int i) {
                Log.d("FORMULARYACTIITY", "onOrientationChanged: ");
                if (isPortrait(i)) ;
                else componentManager.launchImpactAssessmentActivity();
            }
        };
        orientationEventListener.enable();
    }

    private boolean isPortrait(int orientation) {
        return (orientation >= (360 - 90) && orientation <= 360) || (orientation >= 0 && orientation <= 90);
    }

    public void focusRootWindow() {
        ((LinearLayout) findViewById(R.id.root)).requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        componentManager.requestManager.populateIfInternetAvailable();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}