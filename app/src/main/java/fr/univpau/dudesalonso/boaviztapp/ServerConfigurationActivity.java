package fr.univpau.dudesalonso.boaviztapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import fr.univpau.dudesalonso.boaviztapp.serverconfig.ServerConfiguration;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.Configuration;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Cpu;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Disk;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Ram;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.model.Model;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.usage.Usage;

public class ServerConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setDarkMode();
        setContentView(R.layout.formulary);
        setAutofilledContents();
        setBottomNavigationBar();
        setNavigationIconFocus();

    }

    private void setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme); //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.AppTheme);  //default app theme
        }
    }


    public void populate(ArrayList<String> manufacturers, ArrayList<String> architectures, ArrayList<String> localisations){

    }

    private void setAutofilledContents() {
        setUsageContents();
    }

    private void setUsageContents() {
        setUsageMethodContents();
        updateMethodDetailContainer(String.valueOf(((MaterialAutoCompleteTextView) findViewById(R.id.usage_method_input)).getText()));
    }

    private void setUsageMethodContents() {
        setShowDropDownOnFocusAndClick(R.id.usage_method_input);
        MaterialAutoCompleteTextView methodInput = findViewById(R.id.usage_method_input);
        methodInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                updateMethodDetailContainer((String) adapterView.getItemAtPosition((int) l));
            }
        });
    }

    private void setShowDropDownOnFocusAndClick(int autoCompleteTextView) {
        MaterialAutoCompleteTextView textView = findViewById(autoCompleteTextView);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    textView.showDropDown();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.showDropDown();
            }
        });


    }

    private void updateMethodDetailContainer(String newMethodString){
        MaterialAutoCompleteTextView methodInput = findViewById(R.id.usage_method_input);
        TextInputLayout methodDetailsInputLayout = findViewById(R.id.usage_method_details_layout);
        TextInputEditText methodDetailsInputEditText = findViewById(R.id.usage_method_details_input);
        if (newMethodString.equals("Load")){
            methodDetailsInputEditText.setText(R.string.usage_server_load_placeholder);
            methodDetailsInputLayout.setHint(getString(R.string.usage_server_load_label));
            methodDetailsInputLayout.setSuffixText(getString(R.string.usage_server_load_helper));
            return;
        }
        methodDetailsInputEditText.setText(R.string.usage_average_consumption_placeholder);
        methodDetailsInputLayout.setHint(getString(R.string.usage_average_consumption_label));
        methodDetailsInputLayout.setSuffixText(getString(R.string.usage_average_consumption_helper));
    }

    private void setBottomNavigationBar(){
        BottomNavigationItemView assessServerImpactMenuBtn = findViewById(R.id.impact_visualisation_menu_button);

        assessServerImpactMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> launchImpactAssessmentActivity()).run();
            }
        });
    }

    private void setNavigationIconFocus() {
        BottomNavigationView assessServerImpactMenu = findViewById(R.id.bottom_navigation);
        assessServerImpactMenu.setSelectedItemId(R.id.server_configuration_menu_button);
    }

    private void launchImpactAssessmentActivity() {
        Intent formularyIntent = new Intent(getApplicationContext(), ServerImpactVisualizer.class);
        Log.d("SERVER CONFIG", collectServerConfiguration().toString());
        formularyIntent.putExtra("serverConfiguration", collectServerConfiguration());
        startActivity(formularyIntent);
    }



    ServerConfiguration collectServerConfiguration(){
        Cpu cpu = new Cpu(
                getNumberFromAutocompleteTextInput(R.id.cpu_quantity_input, R.string.cpu_quantity_placeholder),
                getNumberFromAutocompleteTextInput(R.id.cpu_tdp_input, R.string.cpu_tdp_placeholder),
                getNumberFromAutocompleteTextInput(R.id.cpu_core_units_input, R.string.cpu_core_units_placeholder),
                getTextFromAutocompleteTextInput(R.id.cpu_architecture_input, R.string.cpu_architecture_placeholder)
        );

        ArrayList<Ram> ramArray = new ArrayList<>();
        ramArray.add(new Ram(
                getNumberFromAutocompleteTextInput(R.id.ram_quantity_input, R.string.ram_quantity_placeholder),
                getNumberFromAutocompleteTextInput(R.id.ram_capacity_input, R.string.ram_capacity_placeholder),
                getTextFromAutocompleteTextInput(R.id.ram_manufacturer_input, R.string.ram_manufacturer_placeholder)
                )
        );

        ArrayList<Disk> disks = new ArrayList<>();
        disks.add(new Disk(
                        getNumberFromAutocompleteTextInput(R.id.ssd_quantity_input, R.string.ssd_quantity_placeholder),
                        "ssd",
                        getNumberFromAutocompleteTextInput(R.id.ssd_capacity_input, R.string.ssd_capacity_placeholder),
                        getTextFromAutocompleteTextInput(R.id.ssd_manufacturer_input, R.string.ssd_manufacturer_label)
                )
        );

        disks.add(new Disk(
                        getNumberFromAutocompleteTextInput(R.id.hdd_quantity_input, R.string.hdd_quantity_placeholder),
                        "hdd",
                        getNumberFromAutocompleteTextInput(R.id.hdd_capacity_input, R.string.hdd_capacity_placeholder)
                )
        );

        Usage usageConfiguration = new Usage(
                getNumberFromAutocompleteTextInput(R.id.usage_lifespan_input, R.string.usage_lifespan_placeholder),
                getTextFromAutocompleteTextInput(R.id.usage_location_input, R.string.usage_location_placeholder),
                getNumberFromAutocompleteTextInput(R.id.usage_method_details_input, R.string.usage_average_consumption_placeholder),
                getTextFromAutocompleteTextInput(R.id.usage_method_input, R.string.usage_method_placeholder)
        );

        Configuration componentConfiguration = new Configuration(cpu, ramArray, disks);

        Model model = new Model("rack");

        return new ServerConfiguration(model, componentConfiguration, usageConfiguration);

    }

    String getTextFromAutocompleteTextInput(int input_id, int placeholder_value){
        MaterialAutoCompleteTextView input = findViewById(input_id);
        if (input.getText().toString().equals(""))
            return getString(placeholder_value);
        return input.getText().toString();
    }

    int getNumberFromAutocompleteTextInput(int input_id, int placeholder_value){
        TextInputEditText input = findViewById(input_id);
        if (input.getText().toString().equals(""))
            return Integer.parseInt(getString(placeholder_value));
        return  Integer.parseInt(input.getText().toString());
    }

}