package fr.univpau.dudesalonso.boaviztapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import fr.univpau.dudesalonso.boaviztapp.ImpactVisualizer.CustomMarkerView;

public class ServerImpactVisualizer extends AppCompatActivity {

    ArrayList<BarChart> barChartList = new ArrayList<>();

    int[] colorClassArray1 = new int[]{
            Color.rgb(1,139,140),
            Color.rgb(203,230,255)};

    int[] colorClassArray2 = new int[]{
            Color.rgb(1,139,140),
            Color.rgb(24,60,92),
            Color.rgb(84,183,188),
            Color.rgb(246,211,72),
            Color.rgb(203,163,124),
            Color.rgb(157,181,183)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_visualisation);

        setDarkMode();
        setBottomNavigationBarListener();
        setNavigationIconFocus();

       // ServerConfiguration config = (ServerConfiguration) getIntent().getSerializableExtra("serverConfiguration");
      /*  Log.d("ServerImpactVisualizer: configuration", config.toString());
        Log.d("ServerImpactVisualizer", "Attempting to write value as JSON string");
        Log.d("ServerImpactVisualizer", config.getAsJson());*/

        BarChart layoutGlobalWarming = findViewById(R.id.global_warming);
        BarChart layoutPrimaryEnergy = findViewById(R.id.primary_energy);
        BarChart layoutRessExhausted = findViewById(R.id.ressource_exhausted);

        barChartList.add(layoutGlobalWarming);
        barChartList.add(layoutPrimaryEnergy);
        barChartList.add(layoutRessExhausted);

        int arraySize = barChartList.size();
        for (int i = 0; i < arraySize; i ++) {
            initLayoutChart(createBarData(i), barChartList.get(i));
            initLayoutLegend(barChartList.get(i));
        }

        CustomMarkerView mv = new CustomMarkerView(this, R.layout.tv_content);
        layoutGlobalWarming.setMarker(mv);
        layoutPrimaryEnergy.setMarker(mv);
        layoutRessExhausted.setMarker(mv);

    }

    @Override
    public void onPause() {
        super.onPause();
        //POUR DES TRANSITIONS CLEAN
        overridePendingTransition(0, 0);
    }

    private void setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme); //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.AppTheme);  //default app theme
        }
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
        formularyIntent.setClass(getApplicationContext(), FormularyActivity.class);
        startActivity(formularyIntent);
    }


    public void startProgressIndicator() {
        LinearProgressIndicator progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.VISIBLE);

    }

    public void stopProgressIndicator() {
        LinearProgressIndicator progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            showNetworkErrorToast(R.string.internet_connection_not_available);
        }
        return false;
    }

    private void showNetworkErrorToast(int resString){
        Snackbar.make(this.findViewById(R.id.root), getString(resString), Snackbar.LENGTH_SHORT)
                .setAction(R.string.toast_action_retry, view -> populate())
                .show();
    }

    private void populate(){
        if (!isInternetAvailable())
            return;

        //TODO FAIRE LES REQUETES PUIS MONTRER LES DONNEES
    }

    private BarData createBarData(int index){
        BarDataSet barDataUp = new BarDataSet(dataValuesUp(index),"");
        barDataUp.setColors(colorClassArray1);
        barDataUp.setStackLabels(getResources().getStringArray(R.array.label_top_bar));
        BarDataSet barDataDown = new BarDataSet(dataValuesDown(index),"");
        barDataDown.setColors(colorClassArray2);
        barDataDown.setStackLabels(getResources().getStringArray(R.array.label_bottom_bar));

       // barDataDown.setHighlightEnabled(true);
        barDataUp.setHighLightColor(Color.WHITE); // color for highlight indicator
        barDataUp.setDrawValues(true);
        barDataDown.setHighLightColor(Color.WHITE); // color for highlight indicator
        barDataDown.setDrawValues(true);

        BarData barData = new BarData(barDataUp,barDataDown);
        barData.setBarWidth(5f);
        barData.setDrawValues(false);

        return barData;
    }

    private void initLayoutChart(BarData barData, BarChart barChart){
        barChart.setData(barData);
        barChart.setExtraOffsets(0f,5f,0f,5f);

        barChart.getDescription().setEnabled(false);
        //disable axis things
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getXAxis().setDrawAxisLine(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);

        //disable interaction
        barChart.setTouchEnabled(true);
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);

        //refresh
        barChart.invalidate();
    }

    private void initLayoutLegend(BarChart barChart){
        Legend legend = barChart.getLegend();
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(4f);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(Color.DKGRAY);

        LegendEntry[] legends = barChart.getLegend().getEntries();

        for (LegendEntry l : legends) {

            if(l.label.equals("none")){
                Log.d("lengendEnt", l.label);
                l.label = "";
                l.formColor  = Color.TRANSPARENT;
            }
        }

        legend.setCustom(legends);

    }

    private ArrayList<BarEntry> dataValuesUp(int index){
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(4.95F , new float[]{1f,6f}));
        dataVals.add(new BarEntry(4.95F , new float[]{1f,6f}));
        return dataVals;
    }

    private ArrayList<BarEntry> dataValuesDown(int index){
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0 , new float[]{1f,0.1f,0.8f,1.1f,0.2f,3.8f}));
        return dataVals;
    }

}