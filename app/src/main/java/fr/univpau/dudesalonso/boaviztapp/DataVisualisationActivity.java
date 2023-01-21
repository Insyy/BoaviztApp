package fr.univpau.dudesalonso.boaviztapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.CustomMarkerView;
import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.DialogGrapheManager;
import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.GrapheDataSet;
import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.PostServerRequest;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.ServerConfiguration;

public class DataVisualisationActivity extends AppCompatActivity {

    ArrayList<BarChart> barChartList = new ArrayList<>();
    ServerConfiguration config;
    CustomMarkerView mv;
    PostServerRequest psr;

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

     boolean dialogZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_visualisation);

        //Barre de chargement
        startProgressIndicator();

        setTopAppBarListeners();

        //Initialisation des graphiques
        BarChart layoutGlobalWarming = findViewById(R.id.global_warming);
        BarChart layoutPrimaryEnergy = findViewById(R.id.primary_energy);
        BarChart layoutRessExhausted = findViewById(R.id.ressource_exhausted);

        //Ajout des graphiques à la liste
        barChartList.add(layoutGlobalWarming);
        barChartList.add(layoutPrimaryEnergy);
        barChartList.add(layoutRessExhausted);

        mv = new CustomMarkerView(this, R.layout.tv_content);

        config = (ServerConfiguration) getIntent().getSerializableExtra("serverConfiguration");
        Log.d("configDebug", config.toString());
        psr = new PostServerRequest(this);
        psr.sendRequestServer(config);

        setCustomMarker(barChartList, mv);
        animateCharts(barChartList);

        if(!DialogGrapheManager.dialogZoom) return;

        DialogGrapheManager.dialogZoom = false;
        DialogGrapheManager.showDialogZoom(this);

    }

    private void setTopAppBarListeners() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.boazvitapp_logo_toolbar:
                        visitMainPage();
                        return true;
                    case R.id.download_icon:
                        downloadCharts();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        //POUR DES TRANSITIONS CLEAN
        overridePendingTransition(0, 0);
    }


    public void initCharts(List<GrapheDataSet> listGds) {
        initLayoutChart(createBarData(listGds.get(0)), barChartList.get(0));
        initLayoutChart(createBarData(listGds.get(1)), barChartList.get(1));
        initLayoutChart(createBarData(listGds.get(2)), barChartList.get(2));
        for (int i = 0; i < barChartList.size(); i ++) {
            initLayoutLegend(listGds, barChartList.get(i),i);
            barChartList.get(i).notifyDataSetChanged();
        }

    }

    public void animateCharts(List<BarChart> barChart){
        for(BarChart chart : barChart){
            chart.animateY(400);
        }
    }

    public void setCustomMarker(List<BarChart> charts,CustomMarkerView mv){
        for(BarChart chart : charts){
            chart.setMarker(mv);
        }
    }

    private BarData createBarData(GrapheDataSet gds){
        BarDataSet barDataUp = new BarDataSet(dataValuesUp(gds),"");
        barDataUp.setColors(colorClassArray1);
        barDataUp.setStackLabels(getResources().getStringArray(R.array.label_top_bar));
        BarDataSet barDataDown = new BarDataSet(dataValuesDown(gds),"");
        barDataDown.setColors(colorClassArray2);
        barDataDown.setStackLabels(getResources().getStringArray(R.array.label_bottom_bar));

        // barDataDown.setHighlightEnabled(true);
        barDataUp.setHighLightColor(Color.WHITE); // color for highlight indicator
        barDataUp.setDrawValues(true);
        barDataDown.setHighLightColor(Color.WHITE); // color for highlight indicator
        barDataDown.setDrawValues(true);

        BarData barData = new BarData(barDataUp,barDataDown);
        barData.setBarWidth(5.0f);
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
        barChart.setScaleEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);

        barChart.getLegend().setTextColor(MaterialColors.getColor(barChart, com.google.android.material.R.attr.colorOnBackground));

        //refresh
        barChart.invalidate();

    }

    private void initLayoutLegend(List<GrapheDataSet> listGds, BarChart barChart, int index){

        Legend legend = barChart.getLegend();
        LegendEntry[] legends = barChart.getLegend().getEntries();
        List<LegendEntry> nonEmptyLegend = new ArrayList<>();

        String[] label_bottom_bar = getResources().getStringArray(R.array.label_bottom_bar);
        String[] label_top_bar = getResources().getStringArray(R.array.label_top_bar);

        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(4f);
        legend.setWordWrapEnabled(true);



        for (LegendEntry legendEntry : legends) {
            if (legendEntry.label != null && !legendEntry.label.isEmpty() && !legendEntry.label.equals("none")) {
                nonEmptyLegend.add(legendEntry);
            }
        }
        List<String> topDataSet = listGds.get(index).get_topDataSet();
        for (int i = 0; i < label_top_bar.length; i++) {
            if(topDataSet.get(i).equals("0.0")) nonEmptyLegend.get(i).label = label_top_bar[i] + " " + "no data";
            else nonEmptyLegend.get(i).label =  label_top_bar[i] + " " + topDataSet.get(i);
        }
        List<String> bottomDataSet = listGds.get(index).get_bottomDataSet();
        for (int i = 0; i < label_bottom_bar.length - 1; i++) {
            if(bottomDataSet.get(i).equals("0.0")) nonEmptyLegend.get(i).label = label_bottom_bar[i + 1] + " " + "no data";
            else nonEmptyLegend.get(i + 2).label =  label_bottom_bar[i + 1] + " " + bottomDataSet.get(i);
        }

        legend.setCustom(nonEmptyLegend);

    }

    private int getLegendColor(){
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                return Color.DKGRAY;
            case Configuration.UI_MODE_NIGHT_YES:
                return Color.WHITE;
            default: return Color.DKGRAY;
        }
    }

    private ArrayList<BarEntry> dataValuesUp(GrapheDataSet gds){
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(4.95F , new float[]{Float.parseFloat(gds.get_usage()), Float.parseFloat(gds.get_manufacturing())}));
        return dataVals;
    }

    private ArrayList<BarEntry> dataValuesDown(GrapheDataSet gds){
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0f , new float[]{Float.parseFloat(gds.get_usage()), Float.parseFloat(gds.get_mRAM()), Float.parseFloat(gds.get_mCPU()), Float.parseFloat(gds.get_mSDD()), Float.parseFloat(gds.get_mHDD()), Float.parseFloat(gds.get_mOther())}));
        return dataVals;
    }

    public void startProgressIndicator() {
        LinearProgressIndicator progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.VISIBLE);

    }

    public void stopProgressIndicator() {
        LinearProgressIndicator progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.INVISIBLE);
    }


    private void showNetworkErrorToast(int resString){
        Snackbar.make(this.findViewById(R.id.root), getString(resString), Snackbar.LENGTH_SHORT)
                .setAction(R.string.toast_action_retry, view -> psr.sendRequestServer(config))
                .show();
    }

    public void handleErrorRequest(){
        Snackbar.make(findViewById(R.id.rootVisu), R.string.internet_connection_not_available,Snackbar.LENGTH_SHORT)
                .setAction(R.string.toast_action_retry, view -> psr.sendRequestServer(config))
                .setAnchorView(R.id.rootVisu)
                .show();

    }

    private void visitMainPage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_boavizta))));
    }


    private void downloadCharts(){
        //TODO
    }

}