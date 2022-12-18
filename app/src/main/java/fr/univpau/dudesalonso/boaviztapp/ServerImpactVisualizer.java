package fr.univpau.dudesalonso.boaviztapp;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.univpau.dudesalonso.boaviztapp.serverconfig.ServerConfiguration;

public class ServerImpactVisualizer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_visualisation);

        setBottomNavigationBarListener();
        setNavigationIconFocus();

        Log.d("ServerImpactVisualizer", "Attempting to deserialize configuration");
        ServerConfiguration config = (ServerConfiguration) getIntent().getSerializableExtra("serverConfiguration");
        Log.d("ServerImpactVisualizer: configuration", config.toString());

            Log.d("ServerImpactVisualizer", "Attempting to write value as string jackson");
            Log.d("ServerImpactVisualizer", config.getAsJson());

    }

    private void setBottomNavigationBarListener(){
        BottomNavigationItemView serverConfigurationMenuBtn = findViewById(R.id.server_configuration_menu_button);

        serverConfigurationMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> launchServerConfigurationActivity()).run();
            }
        });
    }

    private void setNavigationIconFocus() {
        BottomNavigationView menu = findViewById(R.id.bottom_navigation);
        menu.setSelectedItemId(R.id.impact_visualisation_menu_button);
    }

    private void launchServerConfigurationActivity() {
        Intent formularyIntent = new Intent();
        formularyIntent.setClass(getApplicationContext(), ServerConfigurationActivity.class);
        startActivity(formularyIntent);
    }
}