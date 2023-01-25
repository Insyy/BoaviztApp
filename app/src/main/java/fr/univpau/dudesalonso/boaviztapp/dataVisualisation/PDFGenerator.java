package fr.univpau.dudesalonso.boaviztapp.dataVisualisation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
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
import com.tom_roush.pdfbox.pdmodel.font.PDFont;

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

    public PDFGenerator(File root,ArrayList<BarChart> barChartList, List<GrapheDataSet> listGds, ServerConfiguration config, Context c){
        _root = root;
        chartGlobalWarning = barChartList.get(0);
        chartPrimaryEnergy = barChartList.get(1);
        chartRessExhausted = barChartList.get(2);
        _config = config;
        _c = c;
        _listGds = listGds;

        generatePDF();
    }

    public File getFile(){
        return new File(_root.getAbsolutePath() + "/"+ name + ".pdf");
    }


   public void generatePDF(){
       Document document = new Document();
       try {

           Date now = new Date();
           name = UUID.randomUUID().toString();
           android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

           path = _root.getAbsolutePath() + "/"+ name + ".pdf";

           PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
           document.open();

           PdfContentByte cb = docWriter.getDirectContent();
           //initialize fonts for text printing
           initializeFonts();
           Log.d("generatePDF",_config.toString());
           createTitlePDF(document,"Multicritera server impacts");
           createTitleSection(document,"Server configuration");
           createTitleTable(document,"CPU");
           PdfPTable cpu_table = new PdfPTable(4);
           cpu_table.setSpacingAfter(10);

           cpu_table.addCell("Quantity");
           cpu_table.addCell("Core units");
           cpu_table.addCell("TDP (Watt)");
           cpu_table.addCell("Architecture");
           cpu_table.addCell(String.valueOf(_config.configuration.cpu.units));
           cpu_table.addCell(String.valueOf(_config.configuration.cpu.core_units));
           cpu_table.addCell(String.valueOf(_config.configuration.cpu.tdp));
           cpu_table.addCell(String.valueOf(_config.configuration.cpu.family));
           document.add(cpu_table);

           createTitleTable(document, "RAM");
           PdfPTable ram_table = new PdfPTable(3);
           ram_table.setSpacingAfter(10);
           ram_table.addCell("Quantity");
           ram_table.addCell("Capacity (GB)");
           ram_table.addCell("Manufacturer");
           ram_table.addCell(String.valueOf(_config.configuration.ram.get(0).units));
           ram_table.addCell(String.valueOf(_config.configuration.ram.get(0).capacity));
           ram_table.addCell(String.valueOf(_config.configuration.ram.get(0).manufacturer));
           document.add(ram_table);

           createTitleTable(document, "SSD");
           PdfPTable ssd_table = new PdfPTable(3);
           ssd_table.setSpacingAfter(10);
           ssd_table.addCell("Quantity");
           ssd_table.addCell("Capacity (GB)");
           ssd_table.addCell("Manufacturer");
           ssd_table.addCell(String.valueOf(_config.configuration.disk.get(0).units));
           ssd_table.addCell(String.valueOf(_config.configuration.disk.get(0).capacity));
           ssd_table.addCell(String.valueOf(_config.configuration.disk.get(0).manufacturer));
           document.add(ssd_table);

           createTitleTable(document, "OTHERS");
           PdfPTable others_table = new PdfPTable(3);
           others_table.setSpacingAfter(10);
           others_table.addCell("HDD quantity");
           others_table.addCell("Server type");
           others_table.addCell("PSU quantity");
           others_table.addCell(String.valueOf(_config.configuration.disk.get(1).units));
           others_table.addCell(String.valueOf(_config.configuration.disk.get(1).capacity));
           others_table.addCell(String.valueOf(_config.model.type));
           document.add(others_table);

           createTitleSection(document,"Usage");

           PdfPTable usage_table1 = new PdfPTable(2);
           usage_table1.setSpacingAfter(10);
           usage_table1.addCell("Localisation");
           usage_table1.addCell("Lifespan (year)");
           usage_table1.addCell(String.valueOf(_config.usage.usage_location));
           usage_table1.addCell(String.valueOf(_config.usage.years_use_time));
           document.add(usage_table1);
           PdfPTable usage_table2 = new PdfPTable(2);
           usage_table2.setSpacingAfter(10);
           usage_table2.addCell("Method");
           usage_table2.addCell("Average consumption (W)");
           usage_table2.addCell("/");
           usage_table2.addCell(String.valueOf(_config.usage.hours_electrical_consumption));
           document.add(usage_table2);

           document.newPage();

           createTitleSection(document, "Multicritera impacts during lifespan");
           createSubTitleSection(document, _c.getText(R.string.title_global_warming) + " " + _listGds.get(0).get_mTotal());
           createBodyText(document,"Evaluates the effect on global warming");
           chartToPDF(document, chartGlobalWarning);

           createSubTitleSection(document, _c.getText(R.string.title_primary_energy)  + " " + _listGds.get(1).get_mTotal());
           createBodyText(document,"Consumption of energy resources");
           chartToPDF(document, chartPrimaryEnergy);

           createSubTitleSection(document, _c.getText(R.string.title_ressource_exhausted)  + " " + _listGds.get(2).get_mTotal());
           createBodyText(document,"Evaluates the use of minerals and fossil ressources");
           chartToPDF(document, chartRessExhausted);
           document.close();

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
                FontFactory.getFont(FontFactory.HELVETICA, 14,Font.ITALIC)
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
        //todo Verif le theme

        convertLegendToBlack(chart);

        Bitmap scaledBitmap = chart.getChartBitmap();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Image image = Image.getInstance(byteArray);
        image.scaleToFit(600,300);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);

        convertLegendToWhite(chart);
    }

    private void convertLegendToBlack(BarChart chart){
        chart.getLegend().setTextColor(Color.BLACK);
    }

    private void convertLegendToWhite(BarChart chart){
        chart.getLegend().setTextColor(Color.WHITE);
    }

    private void initializeFonts(){


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

    private float getStringWidth(PDFont font, Integer fontSize, String text) throws IOException {
        return font.getStringWidth(text) / 1000 * fontSize;
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
