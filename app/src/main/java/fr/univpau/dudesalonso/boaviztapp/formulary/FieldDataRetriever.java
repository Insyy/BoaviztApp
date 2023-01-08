package fr.univpau.dudesalonso.boaviztapp.formulary;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import fr.univpau.dudesalonso.boaviztapp.FormularyActivity;
import fr.univpau.dudesalonso.boaviztapp.R;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.ServerConfiguration;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.configuration.Configuration;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.configuration.components.Cpu;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.configuration.components.Disk;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.configuration.components.Ram;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.model.Model;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.usage.Usage;

public class FieldDataRetriever {
    FormularyActivity formularyActivity;

    public FieldDataRetriever(FormularyActivity formularyActivity) {
        this.formularyActivity = formularyActivity;
    }

    ServerConfiguration collectServerConfiguration() {
        Cpu cpu = new Cpu(
                getNumberFromAutocompleteTextInput(R.id.cpu_quantity_input, R.string.cpu_quantity_placeholder),
                getNumberFromAutocompleteTextInput(R.id.cpu_tdp_input, R.string.cpu_tdp_placeholder),
                getNumberFromAutocompleteTextInput(R.id.cpu_core_units_input, R.string.cpu_core_units_placeholder),
                getTextFromAutocompleteTextInput(R.id.cpu_architecture_input)
        );

        ArrayList<Ram> ramArray = new ArrayList<>();
        ramArray.add(new Ram(
                        getNumberFromAutocompleteTextInput(R.id.ram_quantity_input, R.string.ram_quantity_placeholder),
                        getNumberFromAutocompleteTextInput(R.id.ram_capacity_input, R.string.ram_capacity_placeholder),
                        getTextFromAutocompleteTextInput(R.id.ram_manufacturer_input)
                )
        );

        ArrayList<Disk> disks = new ArrayList<>();
        disks.add(new Disk(
                        getNumberFromAutocompleteTextInput(R.id.ssd_quantity_input, R.string.ssd_quantity_placeholder),
                        "ssd",
                        getNumberFromAutocompleteTextInput(R.id.ssd_capacity_input, R.string.ssd_capacity_placeholder),
                        getTextFromAutocompleteTextInput(R.id.ssd_manufacturer_input)
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
                getTextFromAutocompleteTextInput(R.id.usage_location_input),
                getNumberFromAutocompleteTextInput(R.id.usage_method_details_input, R.string.usage_average_consumption_placeholder),
                getTextFromAutocompleteTextInput(R.id.usage_method_input)
        );

        Configuration componentConfiguration = new Configuration(cpu, ramArray, disks);

        Model model = new Model(getTextFromAutocompleteTextInput(R.id.server_type_input));

        return new ServerConfiguration(model, componentConfiguration, usageConfiguration);

    }

    String getTextFromAutocompleteTextInput(int input_id) {
        MaterialAutoCompleteTextView input = formularyActivity.findViewById(input_id);
        if (input.getText().toString().equals("") && input.getAdapter().getCount() > 0){
            return (String) input.getAdapter().getItem(0);
        }
        return input.getText().toString();
    }

    int getNumberFromAutocompleteTextInput(int input_id, int placeholder_value) {
        TextInputEditText input = formularyActivity.findViewById(input_id);
        if (Objects.requireNonNull(input.getText()).toString().equals(""))
            return Integer.parseInt(formularyActivity.getString(placeholder_value));
        return Integer.parseInt(input.getText().toString());
    }
}
