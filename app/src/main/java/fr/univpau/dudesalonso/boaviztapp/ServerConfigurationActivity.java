package fr.univpau.dudesalonso.boaviztapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univpau.dudesalonso.boaviztapp.serverconfig.ServerConfiguration;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.Configuration;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Cpu;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Disk;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.configuration.components.Ram;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.model.Model;
import fr.univpau.dudesalonso.boaviztapp.serverconfig.usage.Usage;

public class ServerConfigurationActivity extends AppCompatActivity {

    String urlArchitectures = "https://uppa.api.boavizta.org/v1/utils/cpu_family";
    String urlSsdManufacturers = "https://uppa.api.boavizta.org/v1/utils/ssd_manufacturer";
    String urlRamManufacturers = "https://uppa.api.boavizta.org/v1/utils/ram_manufacturer";
    String urlCountries = "https://uppa.api.boavizta.org/v1/utils/country_code";

    Map<String, String> countriesMap = null;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);

        setDarkMode();

        setContentView(R.layout.formulary);

        ((LinearLayout) findViewById(R.id.root)).requestFocus();

        setUsageContents();
        setBottomNavigationBar();
        setNavigationIconFocus();

        setDropdownsListeners();
    }

    private void setDropdownsListeners() {
        setShowDropDownOnFocusAndClick(R.id.usage_method_input);
        setShowDropDownOnFocusAndClick(R.id.cpu_architecture_input);
        setShowDropDownOnFocusAndClick(R.id.ssd_manufacturer_input);
        setShowDropDownOnFocusAndClick(R.id.ram_manufacturer_input);
        setShowDropDownOnFocusAndClick(R.id.usage_location_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateIfInternetAvailable();
    }

    private void setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme); //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.AppTheme);  //default app theme
        }
    }

    public void populate() {

        Log.d("POPULATING", "POPULATING");

        sendArrayGetRequestsAndPopulate(urlArchitectures, R.id.cpu_architecture_input);
        sendArrayGetRequestsAndPopulate(urlSsdManufacturers, R.id.ssd_manufacturer_input);
        sendArrayGetRequestsAndPopulate(urlRamManufacturers, R.id.ram_manufacturer_input);
        sendMapGetRequestsAndPopulate(urlCountries, R.id.usage_location_input);

        populateAutocompleteDropdownValues(R.id.usage_method_input, Arrays.asList(getResources().getStringArray(R.array.method_options)));
    }

    boolean isConnected(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                ((NetworkInfo) activeNetwork).isConnectedOrConnecting();
    }

    public void populateIfInternetAvailable() {
            if (!isConnected()){
                showNetworkErrorToast(R.string.internet_connection_not_available);
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        InetAddress address = InetAddress.getByName("www.google.com");
                        if( !address.equals(""))
                            populate();
                    } catch (UnknownHostException e) {
                        showNetworkErrorToast(R.string.internet_connection_not_available);
                    }
                }
            }).start();
    }

    private void populateAutocompleteDropdownValues(int id, List<String> values) {
        MaterialAutoCompleteTextView autoCompleteTextView = findViewById(id);
        String[] array = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            array[i] = values.get(i);
        }
        Arrays.sort(array);
        autoCompleteTextView.setSimpleItems(array);
        autoCompleteTextView.setText(array[0]);
    }

    private void setUsageContents() {
        setUsageMethodContents();
        updateMethodDetailContainer(String.valueOf(((MaterialAutoCompleteTextView) findViewById(R.id.usage_method_input)).getText()));
    }

    private void setUsageMethodContents() {
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

    private void updateMethodDetailContainer(String newMethodString) {
        MaterialAutoCompleteTextView methodInput = findViewById(R.id.usage_method_input);
        TextInputLayout methodDetailsInputLayout = findViewById(R.id.usage_method_details_layout);
        TextInputEditText methodDetailsInputEditText = findViewById(R.id.usage_method_details_input);
        if (newMethodString.equals("Load")) {
            methodDetailsInputEditText.setText(R.string.usage_server_load_placeholder);
            methodDetailsInputLayout.setHint(getString(R.string.usage_server_load_label));
            methodDetailsInputLayout.setSuffixText(getString(R.string.usage_server_load_helper));
            return;
        }
        methodDetailsInputEditText.setText(R.string.usage_average_consumption_placeholder);
        methodDetailsInputLayout.setHint(getString(R.string.usage_average_consumption_label));
        methodDetailsInputLayout.setSuffixText(getString(R.string.usage_average_consumption_helper));
    }

    private void setBottomNavigationBar() {
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


    ServerConfiguration collectServerConfiguration() {
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

    String getTextFromAutocompleteTextInput(int input_id, int placeholder_value) {
        MaterialAutoCompleteTextView input = findViewById(input_id);
        if (input.getText().toString().equals(""))
            return getString(placeholder_value);
        return input.getText().toString();
    }

    int getNumberFromAutocompleteTextInput(int input_id, int placeholder_value) {
        TextInputEditText input = findViewById(input_id);
        if (input.getText().toString().equals(""))
            return Integer.parseInt(getString(placeholder_value));
        return Integer.parseInt(input.getText().toString());
    }

    public void startProgressIndicator() {
        LinearProgressIndicator progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.VISIBLE);

    }

    public void stopProgressIndicator() {
        LinearProgressIndicator progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);
    }


    private void sendArrayGetRequestsAndPopulate(String url, int materialAutoCompleteId){
        startProgressIndicator();

        queue.add(
                new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<String> values = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        values.add(response.getString(i));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                populateAutocompleteDropdownValues(materialAutoCompleteId, values);
                                stopProgressIndicator();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                showNetworkErrorToast(R.string.network_error_message);
                                stopProgressIndicator();
                            }
                        })
        );
    }

    private void sendMapGetRequestsAndPopulate(String url, int materialAutocompleteId) {
        startProgressIndicator();
        queue.add(
                new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Map<String, String> values = new HashMap<>();
                                response.keys().forEachRemaining
                                        (s ->
                                                {
                                                    try {
                                                        values.put(s, response.getString(s));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                        );
                                if (countriesMap == null)
                                    countriesMap = values;
                                populateAutocompleteDropdownValues(materialAutocompleteId, new ArrayList<>(values.keySet()));
                                stopProgressIndicator();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                showNetworkErrorToast(R.string.network_error_message);
                                stopProgressIndicator();
                            }
                        })
        );
    }

    private void showNetworkErrorToast(int resString){
        Snackbar.make(findViewById(R.id.root), getString(resString), Snackbar.LENGTH_LONG)
                .setAction(R.string.toast_action_retry, view -> populateIfInternetAvailable())
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }


    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}