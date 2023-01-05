package fr.univpau.dudesalonso.boaviztapp.formulary;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.univpau.dudesalonso.boaviztapp.FormularyActivity;
import fr.univpau.dudesalonso.boaviztapp.R;
import fr.univpau.dudesalonso.boaviztapp.Graphe;

public class ComponentManager {

    public FormularyActivity formularyActivity;
    public final RequestManager requestManager;
    private final FieldDataRetriever fieldDataRetriever;

    Map<String, String> countriesMap = null;

    public ComponentManager(FormularyActivity activity) {
        formularyActivity = activity;
        requestManager = new RequestManager(formularyActivity);
        fieldDataRetriever = new FieldDataRetriever(formularyActivity);
    }

    private void setDropdownsListeners() {
        setShowDropDownOnFocusAndClick(R.id.usage_method_input);
        setShowDropDownOnFocusAndClick(R.id.cpu_architecture_input);
        setShowDropDownOnFocusAndClick(R.id.ssd_manufacturer_input);
        setShowDropDownOnFocusAndClick(R.id.ram_manufacturer_input);
        setShowDropDownOnFocusAndClick(R.id.usage_location_input);
    }

    public void prepareUI(){
        setUsageContents();
        setBottomNavigationBar();
        setNavigationIconFocus();

        setDropdownsListeners();
    }

    public void populate() {

        requestManager.sendGetArrayRequestAndPopulate(formularyActivity.getString(R.string.url_cpu_architectures), R.id.cpu_architecture_input);
        requestManager.sendGetArrayRequestAndPopulate(formularyActivity.getString(R.string.url_ssd_manufacturers), R.id.ssd_manufacturer_input);
        requestManager.sendGetArrayRequestAndPopulate(formularyActivity.getString(R.string.url_hdd_manufacturers), R.id.ram_manufacturer_input);
        requestManager.sendGetObjectRequestAndPopulate(formularyActivity.getString(R.string.url_countries), R.id.usage_location_input);

        populateAutocompleteDropdownValues(R.id.usage_method_input, Arrays.asList(formularyActivity.getResources().getStringArray(R.array.method_options)));
    }


    void populateAutocompleteDropdownValues(int id, List<String> values) {
        MaterialAutoCompleteTextView autoCompleteTextView = formularyActivity.findViewById(id);
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
        updateMethodDetailContainer(String.valueOf(((MaterialAutoCompleteTextView) formularyActivity.findViewById(R.id.usage_method_input)).getText()));
    }

    private void setUsageMethodContents() {
        MaterialAutoCompleteTextView methodInput = formularyActivity.findViewById(R.id.usage_method_input);
        methodInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                updateMethodDetailContainer((String) adapterView.getItemAtPosition((int) l));
            }
        });
    }

    private void setShowDropDownOnFocusAndClick(int autoCompleteTextView) {
        MaterialAutoCompleteTextView textView = formularyActivity.findViewById(autoCompleteTextView);
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
        TextInputLayout methodDetailsInputLayout = formularyActivity.findViewById(R.id.usage_method_details_layout);
        TextInputEditText methodDetailsInputEditText = formularyActivity.findViewById(R.id.usage_method_details_input);
        if (newMethodString.equals("Load")) {
            methodDetailsInputEditText.setText(R.string.usage_server_load_placeholder);
            methodDetailsInputLayout.setHint(formularyActivity.getString(R.string.usage_server_load_label));
            methodDetailsInputLayout.setSuffixText(formularyActivity.getString(R.string.usage_server_load_helper));
            return;
        }
        methodDetailsInputEditText.setText(R.string.usage_average_consumption_placeholder);
        methodDetailsInputLayout.setHint(formularyActivity.getString(R.string.usage_average_consumption_label));
        methodDetailsInputLayout.setSuffixText(formularyActivity.getString(R.string.usage_average_consumption_helper));
    }

    private void setBottomNavigationBar() {
        BottomNavigationItemView assessServerImpactMenuBtn = formularyActivity.findViewById(R.id.impact_visualisation_menu_button);

        assessServerImpactMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> launchImpactAssessmentActivity()).start();
            }
        });
    }

    private void setNavigationIconFocus() {
        BottomNavigationView assessServerImpactMenu = formularyActivity.findViewById(R.id.bottom_navigation);
        assessServerImpactMenu.setSelectedItemId(R.id.server_configuration_menu_button);
    }

    private void launchImpactAssessmentActivity() {
        Intent formularyIntent = new Intent(formularyActivity.getApplicationContext(), Graphe.class);
        Log.d("SERVER CONFIG", fieldDataRetriever.collectServerConfiguration().toString());
        formularyIntent.putExtra("serverConfiguration", fieldDataRetriever.collectServerConfiguration());
        formularyActivity.startActivity(formularyIntent);
    }




    public void startProgressIndicator() {
        LinearProgressIndicator progressIndicator = formularyActivity.findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.VISIBLE);

    }

    public void stopProgressIndicator() {
        LinearProgressIndicator progressIndicator = formularyActivity.findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);
    }




    void showNetworkErrorToast(int resString) {
        Snackbar.make(formularyActivity.findViewById(R.id.root), formularyActivity.getString(resString), Snackbar.LENGTH_LONG)
                .setAction(R.string.toast_action_retry, view -> requestManager.populateIfInternetAvailable())
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }
}
