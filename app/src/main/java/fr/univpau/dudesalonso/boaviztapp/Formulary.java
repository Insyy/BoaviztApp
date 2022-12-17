package fr.univpau.dudesalonso.boaviztapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Formulary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulary);
        setAutofilledContents();
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
        Log.d("TAG", "updateMethodDetailContainer: ");
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

}