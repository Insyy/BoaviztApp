package fr.univpau.dudesalonso.boaviztapp.formulary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
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

import fr.univpau.dudesalonso.boaviztapp.dataVisualisation.DialogGrapheManager;

public class PDFGenerator {

    BarChart chartGlobalWarning;
    BarChart chartPrimaryEnergy;
    BarChart chartRessExhausted;

    File _root;

    BaseFont bfBold;

    Context _c;

    public PDFGenerator(File root, ArrayList<BarChart> barChartList, Context c){
        _root = root;
        chartGlobalWarning = barChartList.get(0);
        chartPrimaryEnergy = barChartList.get(1);
        chartRessExhausted = barChartList.get(2);
        _c = c;
    }

   public void generatePDF(){
       Document document = new Document();
       try {

           String path = _root.getAbsolutePath() + "/Created2.pdf";
           PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
           document.open();

           PdfContentByte cb = docWriter.getDirectContent();
           //initialize fonts for text printing
           initializeFonts();

           createTitlePDF(document,"Multicritera server impacts");
           createTitleSection(document,"Server configuration");
           createTitleTable(document,"CPU");
           PdfPTable cpu_table = new PdfPTable(4);
           cpu_table.setSpacingAfter(10);

           cpu_table.addCell("Quantity");
           cpu_table.addCell("Core units");
           cpu_table.addCell("TDP (Watt)");
           cpu_table.addCell("Architecture");
           cpu_table.addCell("2");
           cpu_table.addCell("16");
           cpu_table.addCell("150");
           cpu_table.addCell("skylake");
           document.add(cpu_table);

           createTitleTable(document, "RAM");
           PdfPTable ram_table = new PdfPTable(3);
           ram_table.setSpacingAfter(10);
           ram_table.addCell("Quantity");
           ram_table.addCell("Capacity (GB)");
           ram_table.addCell("Manufacturer");
           ram_table.addCell("4");
           ram_table.addCell("32");
           ram_table.addCell("Samsung");
           document.add(ram_table);

           createTitleTable(document, "SSD");
           PdfPTable ssd_table = new PdfPTable(3);
           ssd_table.setSpacingAfter(10);
           ssd_table.addCell("Quantity");
           ssd_table.addCell("Capacity (GB)");
           ssd_table.addCell("Manufacturer");
           ssd_table.addCell("4");
           ssd_table.addCell("32");
           ssd_table.addCell("Micron");
           document.add(ssd_table);

           createTitleTable(document, "OTHERS");
           PdfPTable others_table = new PdfPTable(3);
           others_table.setSpacingAfter(10);
           others_table.addCell("HDD quantity");
           others_table.addCell("Server type");
           others_table.addCell("PSU quantity");
           others_table.addCell("4");
           others_table.addCell("Rack");
           others_table.addCell("Micron");
           document.add(others_table);

           createTitleSection(document,"Usage");

           PdfPTable usage_table1 = new PdfPTable(2);
           usage_table1.setSpacingAfter(10);
           usage_table1.addCell("Localisation");
           usage_table1.addCell("Lifespan (year)");
           usage_table1.addCell("0_Global");
           usage_table1.addCell("4");
           document.add(usage_table1);
           PdfPTable usage_table2 = new PdfPTable(2);
           usage_table2.setSpacingAfter(10);
           usage_table2.addCell("Method");
           usage_table2.addCell("Average consumption (W)");
           usage_table2.addCell("Electricity");
           usage_table2.addCell("150");
           document.add(usage_table2);

           document.newPage();

           createTitleSection(document,"Multicritera impacts during lifespan");

           createSubTitleSection(document, "Global Warming Potential (kgCO2eq)  - Total : 3153.8");
           createBodyText(document,"Evaluates the effect on global warming");
           chartToPDF(document, chartGlobalWarning);

           createSubTitleSection(document, "Primary energy (MJ) - Total : 82561");
           createBodyText(document,"Consumption of energy resources");
           chartToPDF(document, chartPrimaryEnergy);

           createSubTitleSection(document, "Abiotic Depletion Potential (kgSbeq) - Total : 0.141528");
           createBodyText(document,"Evaluates the use of minerals and fossil ressources");
           chartToPDF(document, chartRessExhausted);
           document.close();

           DialogGrapheManager.successfulDownload(_c);
       } catch (IOException e) {
           Log.d("PdfBox-Android-Sample", "Exception thrown while creating PDF", e);
           DialogGrapheManager.failureDownload(_c);
       } catch (DocumentException e) {
           e.printStackTrace();
       }

       //Ancienne methode
       /*   Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
            try {
                createImage(R.id.global_warming, now.toString());
                createImage(R.id.primary_energy, now.toString());
                createImage(R.id.ressource_exhausted, now.toString());
                DialogGrapheManager.successfulDownload(/*findViewById(R.id.rootVisu) this);
          /*  } catch (Throwable e) {
                DialogGrapheManager.failureDownload(this);
                e.printStackTrace();
            }*/
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
        Bitmap scaledBitmap = chart.getChartBitmap();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Image image = Image.getInstance(byteArray);
        image.scaleToFit(600,300);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
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
