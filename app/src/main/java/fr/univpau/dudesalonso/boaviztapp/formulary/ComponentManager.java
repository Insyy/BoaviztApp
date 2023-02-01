package fr.univpau.dudesalonso.boaviztapp.formulary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.univpau.dudesalonso.boaviztapp.FormularyActivity;
import fr.univpau.dudesalonso.boaviztapp.DataVisualisationActivity;
import fr.univpau.dudesalonso.boaviztapp.R;

public class ComponentManager {

    public FormularyActivity formularyActivity;
    public final RequestManager requestManager;
    private final FieldDataRetriever fieldDataRetriever;

    //TODO
    private final HashMap<Integer, Boolean> validInputsMap = new HashMap<>();

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
        setShowDropDownOnFocusAndClick(R.id.server_type_input);
    }

    private void setFieldControlListeners(){
        setIntegerFieldControl(R.id.hdd_capacity_input, R.id.hdd_capacity_layout);
        setIntegerFieldControl(R.id.hdd_quantity_input, R.id.hdd_quantity_layout);

        setIntegerFieldControl(R.id.ram_capacity_input, R.id.ram_capacity_layout);
        setIntegerFieldControl(R.id.ram_quantity_input, R.id.ram_quantity_layout);

        setIntegerFieldControl(R.id.ssd_capacity_input, R.id.ssd_capacity_layout);
        setIntegerFieldControl(R.id.ssd_quantity_input, R.id.ssd_quantity_layout);

        setIntegerFieldControl(R.id.cpu_quantity_input, R.id.cpu_quantity_layout);
        setIntegerFieldControl(R.id.cpu_core_units_input, R.id.cpu_core_units_layout);
        setIntegerFieldControl(R.id.cpu_tdp_input, R.id.cpu_tdp_layout);

        setIntegerFieldControl(R.id.usage_lifespan_input, R.id.usage_lifespan_layout);
        setIntegerFieldControl(R.id.usage_method_details_input, R.id.usage_method_details_layout);
    }

    private void setIntegerFieldControl(int inputFieldId, int inputLayoutId){

        TextInputEditText inputView = formularyActivity.findViewById(inputFieldId);
        TextInputLayout layoutView = formularyActivity.findViewById(inputLayoutId);

        layoutView.setErrorIconDrawable(null);

        validInputsMap.put(inputFieldId, true);

        inputView.setOnKeyListener((v, keyCode, event) -> {
            verifyValidInput(inputFieldId, inputView, layoutView);
            return false;
        });
    }

    private void verifyValidInput(int inputFieldId, TextInputEditText inputView, TextInputLayout layoutView) {
        try {
            String input = Objects.requireNonNull(inputView.getText()).toString();
            Integer.parseUnsignedInt(input);

            layoutView.setErrorEnabled(false);
            layoutView.setError(null);

            validInputsMap.put(inputFieldId, true);

        } catch (NumberFormatException numberFormatException){

            layoutView.setErrorEnabled(true);
            layoutView.setError(" ");

            hideUnnecessaryGeneratedViews(layoutView);

            validInputsMap.put(inputFieldId, false);
        }
    }

    private void hideUnnecessaryGeneratedViews(TextInputLayout layoutView) {
        for (int i = 0; i < layoutView.getChildCount(); i++) {
            View child = layoutView.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) child;
                linearLayout.setVisibility(View.GONE);
                break;
            }
        }
    }


    private boolean inputsAreValid(){
        return validInputsMap.values().stream().allMatch(aBoolean -> aBoolean);
    }

    private void setClearOnClickListeners() {
        setClearInputOnClick(R.id.cpu_quantity_input, R.id.cpu_quantity_layout, R.string.cpu_quantity_placeholder);
        setClearInputOnClick(R.id.cpu_core_units_input, R.id.cpu_core_units_layout, R.string.cpu_core_units_placeholder);
        setClearInputOnClick(R.id.cpu_tdp_input, R.id.cpu_tdp_layout, R.string.cpu_tdp_placeholder);

        setClearInputOnClick(R.id.ssd_capacity_input, R.id.ssd_capacity_layout, R.string.ssd_capacity_placeholder);
        setClearInputOnClick(R.id.ssd_quantity_input, R.id.ssd_quantity_layout, R.string.ssd_quantity_placeholder);

        setClearInputOnClick(R.id.hdd_capacity_input, R.id.hdd_capacity_layout, R.string.hdd_capacity_placeholder);
        setClearInputOnClick(R.id.hdd_quantity_input,R.id.hdd_quantity_layout, R.string.hdd_quantity_placeholder);

        setClearInputOnClick(R.id.ram_quantity_input, R.id.ram_quantity_layout, R.string.ram_quantity_placeholder);
        setClearInputOnClick(R.id.ram_capacity_input, R.id.ram_capacity_layout, R.string.ram_capacity_placeholder);

        setClearInputOnClick(R.id.usage_lifespan_input, R.id.usage_lifespan_layout, R.string.usage_lifespan_placeholder);
    }

    private void setClearInputOnClick(int inputId, int inputLayoutId, int defaultValue) {
        TextInputEditText inputView = formularyActivity.findViewById(inputId);
        inputView.setOnFocusChangeListener((view, b) -> {
            if (b && Objects.requireNonNull(inputView.getText()).length() > 0){
                inputView.getText().clear();
                verifyValidInput(inputId, inputView, formularyActivity.findViewById(inputLayoutId));
            }

            else if (Objects.requireNonNull(inputView.getText()).length() == 0) {
                inputView.setText(defaultValue);
                verifyValidInput(inputId, inputView, formularyActivity.findViewById(inputLayoutId));
            }
        });
    }

    public void prepareUI() {
        setUsageContents();
        setBottomNavigationBar();
        setClearOnClickListeners();
        setDropdownsListeners();
        setTopAppBarListeners();
        setFieldControlListeners();
    }

    private void setTopAppBarListeners() {
        MaterialToolbar topAppBar = formularyActivity.findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view ->
                new MaterialAlertDialogBuilder(formularyActivity)
                        .setTitle(formularyActivity.getString(R.string.credits_title))
                        .setMessage(formularyActivity.getString(R.string.credits))
                        .setNeutralButton(formularyActivity.getString(R.string.ok_message), null)
                        .setPositiveButton(formularyActivity.getString(R.string.learn_more_message),
                                (dialogInterface, i) ->
                                        visitGithubPage()).show());

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.boazvitapp_logo_toolbar) {
                visitMainPage();
                return true;
            }
            return false;
        });
    }

    private void visitGithubPage() {
        formularyActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(formularyActivity.getString(R.string.url_github))));
    }

    private void visitMainPage() {
        formularyActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(formularyActivity.getString(R.string.url_boavizta))));
    }

    public void setOnlineIcon() {
        MaterialToolbar topAppBar = formularyActivity.findViewById(R.id.topAppBar);
        for (int i = 0; i < topAppBar.getMenu().size(); i++) {
            MenuItem item = topAppBar.getMenu().getItem(i);
            if (item.getItemId() == R.id.boazvitapp_logo_toolbar) continue;
            item.setVisible(item.getItemId() == R.id.online_icon);
        }
    }

    public void setOfflineIcon() {
        MaterialToolbar topAppBar = formularyActivity.findViewById(R.id.topAppBar);
        for (int i = 0; i < topAppBar.getMenu().size(); i++) {
            MenuItem item = topAppBar.getMenu().getItem(i);
            if (item.getItemId() == R.id.boazvitapp_logo_toolbar) continue;
            item.setVisible(item.getItemId() == R.id.offline_icon);
        }
    }

    public void populate() {
        requestManager.responsesReceived = 0;

        requestManager.sendGetArrayRequestAndPopulate(formularyActivity.getString(R.string.url_cpu_architectures), R.id.cpu_architecture_input);
        requestManager.sendGetArrayRequestAndPopulate(formularyActivity.getString(R.string.url_ssd_manufacturers), R.id.ssd_manufacturer_input);
        requestManager.sendGetArrayRequestAndPopulate(formularyActivity.getString(R.string.url_hdd_manufacturers), R.id.ram_manufacturer_input);
        requestManager.sendGetObjectRequestAndPopulate(formularyActivity.getString(R.string.url_countries), R.id.usage_location_input);
        requestManager.sendGetArrayRequestAndPopulate(formularyActivity.getString(R.string.url_case_type), R.id.server_type_input);

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
        methodInput.setOnItemClickListener((adapterView, view, i, l) -> updateMethodDetailContainer((String) adapterView.getItemAtPosition((int) l)));
    }

    private void setShowDropDownOnFocusAndClick(int autoCompleteTextView) {
        MaterialAutoCompleteTextView textView = formularyActivity.findViewById(autoCompleteTextView);
        textView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                //textView.showDropDown();
                if (textView.getText().length() > 0) textView.getText().clear();
            }
        });
        textView.setOnClickListener(view -> textView.showDropDown());
    }

    private void updateMethodDetailContainer(String newMethodString) {
        TextInputLayout methodDetailsInputLayout = formularyActivity.findViewById(R.id.usage_method_details_layout);
        TextInputEditText methodDetailsInputEditText = formularyActivity.findViewById(R.id.usage_method_details_input);
        if (newMethodString.equals("Load")) {
            methodDetailsInputEditText.setText(R.string.usage_server_load_placeholder);
            methodDetailsInputLayout.setHint(formularyActivity.getString(R.string.usage_server_load_label));
            methodDetailsInputLayout.setSuffixText(formularyActivity.getString(R.string.usage_server_load_helper));
            verifyValidInput(R.id.usage_method_details_input, methodDetailsInputEditText, methodDetailsInputLayout);
            return;
        }
        methodDetailsInputEditText.setText(R.string.usage_average_consumption_placeholder);
        methodDetailsInputLayout.setHint(formularyActivity.getString(R.string.usage_average_consumption_label));
        methodDetailsInputLayout.setSuffixText(formularyActivity.getString(R.string.usage_average_consumption_helper));
        verifyValidInput(R.id.usage_method_details_input, methodDetailsInputEditText, methodDetailsInputLayout);
    }

    private void setBottomNavigationBar() {

        BottomNavigationView bottom = formularyActivity.findViewById(R.id.bottom_navigation);
        bottom.setSelectedItemId(R.id.invisible_menu_button);

        BottomNavigationItemView assessServerImpactMenuBtn = formularyActivity.findViewById(R.id.impact_visualisation_menu_button);

        assessServerImpactMenuBtn.setOnClickListener(view -> new Thread(this::launchImpactAssessmentActivity).start());
    }

    public void launchImpactAssessmentActivity() {
        if (!inputsAreValid()){
            showInvalidInputErrorToast();
            return;
        }

        if (requestManager.isNotConnected()) {
            return;
        }
        if (requestManager.responsesReceived != requestManager.responsesNeeded){
            requestManager.populateIfInternetAvailable();
            return;
        }

        Intent formularyIntent = new Intent(formularyActivity.getApplicationContext(), DataVisualisationActivity.class);
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

    @SuppressLint("RestrictedApi")
    public void refreshConnectionStatusLayout(boolean networkActive) {
        BottomNavigationView bottom = formularyActivity.findViewById(R.id.bottom_navigation);

        formularyActivity.runOnUiThread(() -> {
            if (networkActive) {
                setOnlineIcon();
                bottom.setItemIconTintList(null);
                bottom.setItemIconTintList(ColorStateList.valueOf(MaterialColors.getColor(bottom, androidx.appcompat.R.attr.colorPrimary)));

                return;
            }
            setOfflineIcon();
            bottom.setItemIconTintList(null);
            bottom.setItemIconTintList(ColorStateList.valueOf(MaterialColors.getColor(bottom, androidx.appcompat.R.attr.colorError)));
            showNoInternetErrorToast();
        });

    }


    void showNetworkErrorToast() {
        Snackbar.make(
                formularyActivity.findViewById(R.id.root),
                        formularyActivity.getString(R.string.request_error_message), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.toast_action_retry, view -> requestManager.populateIfInternetAvailable())
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

    void showNoInternetErrorToast() {

        Snackbar.make(formularyActivity.findViewById(R.id.root),
                formularyActivity.getString(R.string.internet_connection_not_available), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.toast_action_retry, view -> new Thread(requestManager::isNotConnected).start())
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }

    void showInvalidInputErrorToast() {
        Snackbar.make(
                        formularyActivity.findViewById(R.id.root),
                        formularyActivity.getString(R.string.invalid_input_error), Snackbar.LENGTH_SHORT)
                .setAction(R.string.toast_action_reset_values, view -> formularyActivity.restartActivity())
                .setAnchorView(R.id.bottom_navigation)
                .show();
    }
}
