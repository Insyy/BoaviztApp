package fr.univpau.dudesalonso.boaviztapp.dataVisualisation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.color.MaterialColors;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.univpau.dudesalonso.boaviztapp.R;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.ServerConfiguration;

public class PDFGenerator {
    String path;
    String name;

    BarChart chartGlobalWarning;
    BarChart chartPrimaryEnergy;
    BarChart chartRessExhausted;
    List<GrapheDataSet> _listGds;

    public File _root;
    BaseFont bfBold;
    ServerConfiguration _config;
    Context _c;

    public PDFGenerator(File root, ArrayList<BarChart> barChartList, List<GrapheDataSet> listGds, ServerConfiguration config, Context c) {
        _root = root;
        chartGlobalWarning = barChartList.get(0);
        chartPrimaryEnergy = barChartList.get(1);
        chartRessExhausted = barChartList.get(2);
        _config = config;
        _c = c;
        _listGds = listGds;

        generatePDF();
    }

    public File getFile() {
        return new File(_root.getAbsolutePath() + "/" + name + ".pdf");
    }

    public void generatePDF() {
        Document document = new Document();
        try {

            Date now = new Date();
            name = UUID.randomUUID().toString();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            path = _root.getAbsolutePath() + "/" + name + ".pdf";

            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            PdfContentByte cb = docWriter.getDirectContent();
            //initialize fonts for text printing
            initializeFonts();

            createTitlePDF(document, _c.getString(R.string.title_pdf));
            createTitleSection(document, _c.getString(R.string.title_section1));
            createTitleTable(document, _c.getString(R.string.title_table1));
            PdfPTable cpu_table = new PdfPTable(4);
            cpu_table.setSpacingAfter(10);

            cpu_table.addCell(_c.getString(R.string.table_quantity));
            cpu_table.addCell(_c.getString(R.string.table_core_units));
            cpu_table.addCell(_c.getString(R.string.table_tdp));
            cpu_table.addCell(_c.getString(R.string.table_architecture));
            cpu_table.addCell(String.valueOf(_config.configuration.cpu.units));
            cpu_table.addCell(String.valueOf(_config.configuration.cpu.core_units));
            cpu_table.addCell(String.valueOf(_config.configuration.cpu.tdp));
            cpu_table.addCell(String.valueOf(_config.configuration.cpu.family));
            document.add(cpu_table);

            createTitleTable(document, _c.getString(R.string.title_table2));
            PdfPTable ram_table = new PdfPTable(3);
            ram_table.setSpacingAfter(10);
            ram_table.addCell(_c.getString(R.string.table_quantity));
            ram_table.addCell(_c.getString(R.string.table_capacity));
            ram_table.addCell(_c.getString(R.string.table_manufacturer));
            ram_table.addCell(String.valueOf(_config.configuration.ram.get(0).units));
            ram_table.addCell(String.valueOf(_config.configuration.ram.get(0).capacity));
            ram_table.addCell(String.valueOf(_config.configuration.ram.get(0).manufacturer));
            document.add(ram_table);

            createTitleTable(document, _c.getString(R.string.title_table3));
            PdfPTable ssd_table = new PdfPTable(3);
            ssd_table.setSpacingAfter(10);
            ssd_table.addCell(_c.getString(R.string.table_quantity));
            ssd_table.addCell(_c.getString(R.string.table_capacity));
            ssd_table.addCell(_c.getString(R.string.table_manufacturer));
            ssd_table.addCell(String.valueOf(_config.configuration.disk.get(0).units));
            ssd_table.addCell(String.valueOf(_config.configuration.disk.get(0).capacity));
            ssd_table.addCell(String.valueOf(_config.configuration.disk.get(0).manufacturer));
            document.add(ssd_table);

            createTitleTable(document, _c.getString(R.string.title_table4));
            PdfPTable others_table = new PdfPTable(3);
            others_table.setSpacingAfter(10);
            others_table.addCell(_c.getString(R.string.table_hdd_quantity));
            others_table.addCell(_c.getString(R.string.table_server_type));
            others_table.addCell(_c.getString(R.string.table_psu_quantity));
            others_table.addCell(String.valueOf(_config.configuration.disk.get(1).units));
            others_table.addCell(String.valueOf(_config.configuration.disk.get(1).capacity));
            others_table.addCell(String.valueOf(_config.model.type));
            document.add(others_table);

            createTitleSection(document, _c.getString(R.string.title_section2));

            PdfPTable usage_table1 = new PdfPTable(2);
            usage_table1.setSpacingAfter(10);
            usage_table1.addCell(_c.getString(R.string.table_localisation));
            usage_table1.addCell(_c.getString(R.string.table_lifespan));
            usage_table1.addCell(String.valueOf(_config.usage.usage_location));
            usage_table1.addCell(String.valueOf(_config.usage.years_use_time));
            document.add(usage_table1);
            PdfPTable usage_table2 = new PdfPTable(2);
            usage_table2.setSpacingAfter(10);
            usage_table2.addCell(_c.getString(R.string.table_method));
            usage_table2.addCell(_c.getString(R.string.table_average_consumption));
            usage_table2.addCell("/");
            usage_table2.addCell(String.valueOf(_config.usage.hours_electrical_consumption));
            document.add(usage_table2);

            document.newPage();

            createTitleSection(document, _c.getString(R.string.title_section3));
            createSubTitleSection(document, _c.getText(R.string.title_global_warming) + " " + _listGds.get(0).get_mTotal());
            createBodyText(document, _c.getString(R.string.description_global_warming));
            chartToPDF(document, chartGlobalWarning);

            createSubTitleSection(document, _c.getText(R.string.title_primary_energy) + " " + _listGds.get(1).get_mTotal());
            createBodyText(document, _c.getString(R.string.description_primary_energy));
            chartToPDF(document, chartPrimaryEnergy);

            createSubTitleSection(document, _c.getText(R.string.title_ressource_exhausted) + " " + _listGds.get(2).get_mTotal());
            createBodyText(document, _c.getString(R.string.description_ressource_exhausted));
            chartToPDF(document, chartRessExhausted);
            document.close();

            //TODO function
            chartGlobalWarning.getLegend().setTextColor(MaterialColors.getColor(_c, com.google.android.material.R.attr.colorOnBackground, Color.BLACK));
            chartPrimaryEnergy.getLegend().setTextColor(MaterialColors.getColor(_c, com.google.android.material.R.attr.colorOnBackground, Color.BLACK));
            chartRessExhausted.getLegend().setTextColor(MaterialColors.getColor(_c, com.google.android.material.R.attr.colorOnBackground, Color.BLACK));
        } catch (IOException e) {
            Log.d("PdfBox-Android-Sample", "Exception thrown while creating PDF", e);
            DialogGrapheManager.failureDownload(_c);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void createTitlePDF(Document document, String text) throws DocumentException {

        Paragraph title = new Paragraph(
                text,
                FontFactory.getFont(FontFactory.HELVETICA, 23, Font.BOLD)
        );

        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(30);
        document.add(title);

    }

    private void createTitleSection(Document document, String text) throws DocumentException {

        Paragraph title = new Paragraph(
                text,
                FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD)
        );

        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingAfter(30);
        document.add(title);

    }

    private void createSubTitleSection(Document document, String text) throws DocumentException {

        Paragraph title = new Paragraph(
                text,
                FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD)
        );

        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingAfter(5);
        document.add(title);

    }

    private void createBodyText(Document document, String text) throws DocumentException {

        Paragraph body = new Paragraph(
                text,
                FontFactory.getFont(FontFactory.HELVETICA, 14, Font.ITALIC)
        );

        body.setAlignment(Element.ALIGN_LEFT);
        document.add(body);

    }


    private void createTitleTable(Document document, String text) throws DocumentException {

        Paragraph title = new Paragraph(text,
                FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        title.setSpacingBefore(10);
        title.setIndentationLeft(54);
        title.setSpacingAfter(5);
        document.add(title);

    }

    private void chartToPDF(Document document, BarChart chart) throws DocumentException, IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        chart.getLegend().setTextColor(_c.getResources().getColor(android.R.color.darker_gray));

        chart.notifyDataSetChanged();
        chart.invalidate();

        Bitmap scaledBitmap = chart.getChartBitmap();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Image image = Image.getInstance(byteArray);
        image.scaleToFit(600, 300);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }


    private void initializeFonts() {
        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    /*private void createImage(Integer chartId,String fileName)
    {

        BarChart barchart = findViewById(chartId);

        barchart.getLegend().setTextColor(Color.BLACK);
        barchart.invalidate();
        Bitmap bitmap = barchart.getChartBitmap();
        String mPath = MediaStore.Images.Media.insertImage( getContentResolver(),
                bitmap,
                fileName.toString(),
                "") + "/" + fileName;
    }*/
}
