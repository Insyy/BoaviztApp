package fr.univpau.dudesalonso.boaviztapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.DataRenderer;

import java.util.ArrayList;

public class ServerVisualization extends AppCompatActivity {

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

    String[] labels1 = new String[]{
            "Utilisation :",
            "Fabrication :"
    };

    String[] labels2 = new String[]{
            "",
            "Fabrication RAM :",
            "Fabrication CPU :",
            "Fabrication SSD :",
            "Fabrication HDD :",
            "Fabrication Autres :"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_visualization);

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

    }

    private BarData createBarData(int index){
        BarDataSet barDataUp = new BarDataSet(dataValuesUp(index),"");
        barDataUp.setColors(colorClassArray1);
        barDataUp.setStackLabels(labels1);
        BarDataSet barDataDown = new BarDataSet(dataValuesDown(index),"");
        barDataDown.setColors(colorClassArray2);
        barDataDown.setStackLabels(labels2);

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
        barChart.setTouchEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);

        //refresh
        barChart.invalidate();
    }

    private void initLayoutLegend(BarChart barChart){
        Legend legend = barChart.getLegend();
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(4f);
        legend.setTextColor(Color.WHITE);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(Color.DKGRAY);

    }

    private ArrayList<BarEntry> dataValuesUp(int index){
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(4.95F , new float[]{1f,6f}));
        return dataVals;
    }

    private ArrayList<BarEntry> dataValuesDown(int index){
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0 , new float[]{1f,0.1f,0.8f,1.1f,0.2f,3.8f}));
        return dataVals;
    }
}