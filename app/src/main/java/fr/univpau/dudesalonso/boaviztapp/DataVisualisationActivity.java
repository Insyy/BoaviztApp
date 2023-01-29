package fr.univpau.dudesalonso.boaviztapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.CustomMarkerView;
import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.DialogGrapheManager;
import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.GrapheDataSet;
import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.PostServerRequest;
import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.PDFGenerator;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.ServerConfiguration;

public class DataVisualisationActivity extends AppCompatActivity {

    ArrayList<BarChart> barChartList = new ArrayList<>();
    ServerConfiguration config;
    CustomMarkerView mv;
    PostServerRequest psr;
    PDFGenerator pdf;

    BarChart chartGlobalWarning;
    BarChart chartPrimaryEnergy;
    BarChart chartRessExhausted;

    List<GrapheDataSet> _listGds;

    int[] colorClassArray1 = new int[]{
            Color.rgb(1, 139, 140),
            Color.rgb(203, 230, 255)};

    int[] colorClassArray2 = new int[]{
            Color.rgb(1, 139, 140),
            Color.rgb(24, 60, 92),
            Color.rgb(84, 183, 188),
            Color.rgb(246, 211, 72),
            Color.rgb(203, 163, 124),
            Color.rgb(157, 181, 183)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_visualisation);

        setupCharts();
        setupHyperlink();

        config = (ServerConfiguration) getIntent().getSerializableExtra("serverConfiguration");
        psr = new PostServerRequest(this, config);
        psr.sendRequestServer();

        setupPdf();

        if (!DialogGrapheManager.dialogZoom) return;

        DialogGrapheManager.dialogZoom = false;
        DialogGrapheManager.showDialogZoom(this);

    }

    private void setupHyperlink() {
        MaterialTextView tv1 = findViewById(R.id.textView9);
        tv1.setMovementMethod(LinkMovementMethod.getInstance());

        MaterialTextView tv2 = findViewById(R.id.textView11);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void setTopAppBarListeners() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(view -> finish());
        topAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.boazvitapp_logo_toolbar:
                    visitMainPage();
                    return true;
                case R.id.download_icon:
                    downloadCharts();
                    return true;
            }

            return false;
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

    private void setupCharts() {

        //setup dialog
        DialogGrapheManager.view = findViewById(R.id.progress_indicator);
        DialogGrapheManager.startProgressIndicator();
        setTopAppBarListeners();

        //Initialisation des graphiques
        chartGlobalWarning = findViewById(R.id.global_warming);
        chartPrimaryEnergy = findViewById(R.id.primary_energy);
        chartRessExhausted = findViewById(R.id.ressource_exhausted);

        //Ajout des graphiques à la liste
        barChartList.add(chartGlobalWarning);
        barChartList.add(chartPrimaryEnergy);
        barChartList.add(chartRessExhausted);

        mv = new CustomMarkerView(this, R.layout.tv_content);

        setCustomMarker(barChartList, mv);
        animateCharts(barChartList);

    }

    private void setupPdf() {
        PDFBoxResourceLoader.init(getApplicationContext());
        Log.d("setupPdf", config.toString());

        // pdf.initRoot();
    }


    public void initCharts(List<GrapheDataSet> listGds) {
        _listGds = listGds;

        initLayoutChart(createBarData(_listGds.get(0)), barChartList.get(0));
        initLayoutChart(createBarData(_listGds.get(1)), barChartList.get(1));
        initLayoutChart(createBarData(listGds.get(2)), barChartList.get(2));
        for (int i = 0; i < barChartList.size(); i++) {
            initLayoutLegend(barChartList.get(i), i);
            barChartList.get(i).notifyDataSetChanged();
        }

        MaterialTextView tv1 = findViewById(R.id.value_graph_1);
        tv1.setText(tv1.getText() + " " + _listGds.get(0).get_mTotal());

        MaterialTextView tv2 = findViewById(R.id.value_graph_2);
        tv2.setText(tv2.getText() + " " + _listGds.get(1).get_mTotal());

        MaterialTextView tv3 = findViewById(R.id.value_graph_3);
        tv3.setText(tv3.getText() + " " + _listGds.get(2).get_mTotal());
    }

    public void animateCharts(List<BarChart> barChart) {
        for (BarChart chart : barChart) {
            chart.animateY(400);
        }
    }

    public void setCustomMarker(List<BarChart> charts, CustomMarkerView mv) {
        for (BarChart chart : charts) {
            chart.setMarker(mv);
        }
    }

    private BarData createBarData(GrapheDataSet gds) {
        BarDataSet barDataUp = new BarDataSet(dataValuesUp(gds), "");
        barDataUp.setColors(colorClassArray1);
        barDataUp.setStackLabels(getResources().getStringArray(R.array.label_top_bar));
        BarDataSet barDataDown = new BarDataSet(dataValuesDown(gds), "");
        barDataDown.setColors(colorClassArray2);
        barDataDown.setStackLabels(getResources().getStringArray(R.array.label_bottom_bar));

        // barDataDown.setHighlightEnabled(true);
        barDataUp.setHighLightColor(Color.WHITE); // color for highlight indicator
        barDataUp.setDrawValues(true);
        barDataDown.setHighLightColor(Color.WHITE); // color for highlight indicator
        barDataDown.setDrawValues(true);

        BarData barData = new BarData(barDataUp, barDataDown);
        barData.setBarWidth(5.0f);
        barData.setDrawValues(false);

        return barData;
    }

    private void initLayoutChart(BarData barData, BarChart barChart) {
        barChart.setData(barData);
        barChart.setExtraOffsets(0f, 5f, 0f, 5f);

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

    private void initLayoutLegend(BarChart barChart, int index) {

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
        List<String> topDataSet = _listGds.get(index).get_topDataSet();
        for (int i = 0; i < label_top_bar.length; i++) {
            if (topDataSet.get(i).equals("0.0"))
                nonEmptyLegend.get(i).label = label_top_bar[i] + " " + "no data";
            else nonEmptyLegend.get(i).label = label_top_bar[i] + " " + topDataSet.get(i);
        }
        List<String> bottomDataSet = _listGds.get(index).get_bottomDataSet();
        for (int i = 0; i < label_bottom_bar.length - 1; i++) {
            if (bottomDataSet.get(i).equals("0.0"))
                nonEmptyLegend.get(i).label = label_bottom_bar[i + 1] + " " + "no data";
            else
                nonEmptyLegend.get(i + 2).label = label_bottom_bar[i + 1] + " " + bottomDataSet.get(i);
        }

        legend.setTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnBackground, Color.BLACK));
        legend.setCustom(nonEmptyLegend);
    }

    private int getLegendColor() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                return Color.DKGRAY;
            case Configuration.UI_MODE_NIGHT_YES:
                return Color.WHITE;
            default:
                return Color.DKGRAY;
        }
    }

    private ArrayList<BarEntry> dataValuesUp(GrapheDataSet gds) {
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(4.95F, new float[]{Float.parseFloat(gds.get_usage()), Float.parseFloat(gds.get_manufacturing())}));
        return dataVals;
    }

    private ArrayList<BarEntry> dataValuesDown(GrapheDataSet gds) {
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0f, new float[]{Float.parseFloat(gds.get_usage()), Float.parseFloat(gds.get_mRAM()), Float.parseFloat(gds.get_mCPU()), Float.parseFloat(gds.get_mSDD()), Float.parseFloat(gds.get_mHDD()), Float.parseFloat(gds.get_mOther())}));
        return dataVals;
    }

    private void visitMainPage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_boavizta))));
    }

    public boolean checkPerm() {
        String requiredPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int checkVal = this.checkCallingOrSelfPermission(requiredPermission);
        return (checkVal == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadCharts();
                } else {
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Permission refused !")
                            .setMessage("To download the summary please accept permissions")
                            .setNeutralButton(this.getString(R.string.neutral_action), (dialogInterface, i) -> {
                            })
                            .show();
                }
                return;
            }
        }
    }

    private void downloadCharts() {
        if (!checkPerm()) {
            DialogGrapheManager.askForPerms(this);
            return;
        }

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        PDFGenerator pdf = new PDFGenerator(root, barChartList, _listGds, config, this);
        try {
            File file = pdf.getFile();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            }
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            DialogGrapheManager.failureDownload(this);
        }
    }

}