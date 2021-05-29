/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import static com.itextpdf.kernel.pdf.PdfName.Document;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import com.itextpdf.layout.borders.SolidBorder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

/**
 *
 * @author carlos
 */
public class GeneracionPdf {

    public void GeneracionPdfNominas(Nomina nomina, boolean extra) throws FileNotFoundException {
        Trabajadorbbdd tb = nomina.getTrabajadorbbdd();
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#.##");
        
        PdfWriter writer = null;
        if(extra==true){
            writer = new PdfWriter("./resources/nominas/" + tb.getNifnie() + tb.getNombre() + tb.getApellido1() + tb.getApellido2() + getMesString(nomina.getMes())+ nomina.getAnio()+ "EXTRA.pdf");
        }else{
            writer = new PdfWriter("./resources/nominas/" + tb.getNifnie() + tb.getNombre() + tb.getApellido1() + tb.getApellido2() + getMesString(nomina.getMes())+ nomina.getAnio()+".pdf");
        }
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, PageSize.LETTER);

        Paragraph empty = new Paragraph("");
        Table tabla1 = new Table(2);
        tabla1.setWidth(500);

        Paragraph nom = new Paragraph("NOMBRE: " + tb.getNombre());
        Paragraph cif = new Paragraph("CIF: " + tb.getEmpresas().getCif());

        Paragraph dir1 = new Paragraph("Avenida de la facultad - 6");
        Paragraph dir2 = new Paragraph("24001 León");

        Cell cell1 = new Cell();
        cell1.setWidth(180);
        cell1.setTextAlignment(TextAlignment.CENTER);
        cell1.add(nom);
        cell1.add(cif);
        cell1.add(dir1);
        cell1.add(dir2);
        tabla1.addCell(cell1);

        Cell cell2 = new Cell();
        cell2.setBorder(SolidBorder.NO_BORDER);
        cell2.setPadding(10);
        cell2.setTextAlignment(TextAlignment.RIGHT);
        cell2.add(new Paragraph("IBAN: " + tb.getIban()));
        cell2.add(new Paragraph("Bruto anual: "+nomina.getBrutoAnual()));
        cell2.add(new Paragraph("Categoría: " + tb.getCategorias().getNombreCategoria()));
        //format de date to string, parse string to date
        cell2.add(new Paragraph("Fecha de alta: " + formatter1.format(tb.getFechaAlta())));
        tabla1.addCell(cell2);

        Table tabla2 = new Table(2);
        tabla2.setWidth(500);
        Cell cell3 = new Cell();
        cell3.setBorder(SolidBorder.NO_BORDER);
        cell3.setPaddingLeft(23);
        cell3.setPaddingTop(20);
        cell3.setWidth(250);
        tabla2.addCell(cell3);

        Cell cell4 = new Cell();
        cell4.setWidth(170);
        cell4.setPaddingLeft(20);
        cell4.setPaddingRight(23);
        cell4.setPaddingTop(8);
        cell4.setTextAlignment(TextAlignment.RIGHT);

        Paragraph dest = new Paragraph("Destinatario:").setTextAlignment(TextAlignment.LEFT).setBold();
        Paragraph nomCompleto = new Paragraph(tb.getNombre() + " " + tb.getApellido1() + " " + tb.getApellido2());
        Paragraph dni = new Paragraph("DNI: " + tb.getNifnie());

        cell4.add(dest);
        cell4.add(nomCompleto);
        cell4.add(dni);
        cell4.add(dir1);
        cell4.add(dir2);
        tabla2.addCell(cell4);

        Table tabla3 = new Table(1);
        tabla3.setWidth(500);
        Cell cell5 = new Cell();
        cell5.setPaddingTop(15);
        Paragraph fechaNomina = new Paragraph("Nómina: " + getMesString(nomina.getMes()) + " de " + nomina.getAnio()).setTextAlignment(TextAlignment.CENTER).setBold();
        cell5.setBorder(SolidBorder.NO_BORDER);
        cell5.setPaddingLeft(23);
        cell5.add(fechaNomina);
        tabla3.addCell(cell5);

        //TABLA 4 --------------------------------------
        Table tabla4 = new Table(5);
        tabla4.setWidth(500);
        Cell cell6 = new Cell();
        cell6.setWidth(100);
        //cell6.setPaddingTop(15);
        cell6.add(new Paragraph("Conceptos").setTextAlignment(TextAlignment.LEFT));
        
        Cell cell7 = new Cell();
//        cell7.setWidth(100);
        //cell7.setPaddingTop(15);
        cell7.add(new Paragraph("Cantidad").setTextAlignment(TextAlignment.CENTER));

        Cell cell8 = new Cell();
//        cell8.setWidth(100);
        cell8.add(new Paragraph("Imp. Unitario").setTextAlignment(TextAlignment.CENTER));

        Cell cell9 = new Cell();
//        cell9.setWidth(100);
        cell9.add(new Paragraph("Devengo").setTextAlignment(TextAlignment.CENTER));
        
        Cell cell10 = new Cell();
//        cell10.setWidth(100);
        cell10.add(new Paragraph("Deducción").setTextAlignment(TextAlignment.RIGHT));
        
        Cell cell11 = new Cell();
//        cell11.setWidth(100);
        cell11.add(new Paragraph("Deducción"));

        tabla4.addCell(cell6.setBorder(SolidBorder.NO_BORDER)
            .setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)));
        tabla4.addCell(cell7.setBorder(SolidBorder.NO_BORDER)
            .setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)));
        tabla4.addCell(cell8.setBorder(SolidBorder.NO_BORDER)
            .setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)));
        tabla4.addCell(cell9.setBorder(SolidBorder.NO_BORDER)
            .setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)));
        tabla4.addCell(cell10.setBorder(SolidBorder.NO_BORDER)
            .setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)));
        
        
        Cell cell12 = new Cell();
        cell12.add(new Paragraph("Salario Base").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell12.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell13 = new Cell();
        cell13.add(new Paragraph("30 días").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell13.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell14 = new Cell();
        cell14.add(new Paragraph(String.valueOf(df.format(nomina.getImporteSalarioMes()/30))).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell14.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell15 = new Cell();
        cell15.add(new Paragraph(String.valueOf(df.format(nomina.getImporteSalarioMes()))).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell15.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell16 = new Cell();
        cell16.add(new Paragraph("").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell16.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell17 = new Cell();
        cell17.add(new Paragraph("Prorrateo").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell17.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell18 = new Cell();
        cell18.add(new Paragraph("30 días").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell18.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell19 = new Cell();
        cell19.add(new Paragraph(String.valueOf(df.format(nomina.getValorProrrateo()/30))).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell19.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell20 = new Cell();
        cell20.add(new Paragraph(String.valueOf(nomina.getValorProrrateo())).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell20.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell21 = new Cell();
        cell21.add(new Paragraph("").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell21.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell22 = new Cell();
        cell22.add(new Paragraph("Complemento").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell22.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell23 = new Cell();
        cell23.add(new Paragraph("30 días").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell23.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell24 = new Cell();
        cell24.add(new Paragraph(df.format(nomina.getImporteComplementoMes()/30)+"").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell24.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell25 = new Cell();
        cell25.add(new Paragraph(String.valueOf(nomina.getImporteComplementoMes())).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell25.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell26 = new Cell();
        cell26.add(new Paragraph("").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell26.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell27 = new Cell();
        cell27.add(new Paragraph("Antigüedad").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell27.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell28 = new Cell();
        cell28.add(new Paragraph(nomina.getNumeroTrienios()+" Trienios").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell28.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell29 = new Cell();
        cell29.add(new Paragraph(df.format(nomina.getImporteTrienios()/3)+"").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell29.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell230 = new Cell();
        cell230.add(new Paragraph(nomina.getImporteTrienios()+"").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell230.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell31 = new Cell();
        cell31.add(new Paragraph("").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell31.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell32 = new Cell();
        cell32.add(new Paragraph("Contingencias generales").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell32.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell33 = new Cell();
        cell33.add(new Paragraph("04.70% de "+nomina.getBrutoNomina()).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell33.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell34 = new Cell();
        cell34.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell34.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell35 = new Cell();
        cell35.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell35.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell36 = new Cell();
        cell36.add(new Paragraph(nomina.getSeguridadSocialTrabajador()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell36.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell37 = new Cell();
        cell37.add(new Paragraph("Desempleo").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell37.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell38 = new Cell();
        cell38.add(new Paragraph("01.60% de "+nomina.getBrutoNomina()).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell38.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell39 = new Cell();
        cell39.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell39.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell40 = new Cell();
        cell40.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell40.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell41 = new Cell();
        cell41.add(new Paragraph(nomina.getDesempleoTrabajador()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell41.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell42 = new Cell();
        cell42.add(new Paragraph("Cuota de formación").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell42.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell43 = new Cell();
        cell43.add(new Paragraph("00.10% de "+nomina.getBrutoNomina()).setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell43.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell44 = new Cell();
        cell44.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell44.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell45 = new Cell();
        cell45.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell45.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell46 = new Cell();
        cell46.add(new Paragraph(nomina.getFormacionTrabajador()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell46.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell47 = new Cell();
        cell47.add(new Paragraph("IRPF").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell47.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell48 = new Cell();
        cell48.add(new Paragraph(nomina.getIrpf()+"% de ").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell48.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell49 = new Cell();
        cell49.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell49.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell50 = new Cell();
        cell50.add(new Paragraph("").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell50.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell51 = new Cell();
        cell51.add(new Paragraph(nomina.getImporteIrpf()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell51.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell52 = new Cell();
        cell52.add(new Paragraph("Total deducciones").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell52.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell53 = new Cell();
        cell53.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell53.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell54 = new Cell();
        cell54.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell54.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell55 = new Cell();
        cell55.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell55.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell56 = new Cell();
        cell56.add(new Paragraph(nomina.getImporteIrpf()+nomina.getImporteSeguridadSocialTrabajador()+nomina.getFormacionTrabajador()+nomina.getImporteDesempleoTrabajador()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell56.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell57 = new Cell();
        cell57.add(new Paragraph("Total devengos").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla4.addCell(cell57.setBorder(SolidBorder.NO_BORDER).setBorderBottom(new SolidBorder(1)));
        
        Cell cell58 = new Cell();
        cell58.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell58.setBorder(SolidBorder.NO_BORDER).setBorderBottom(new SolidBorder(1)));
        
        Cell cell59 = new Cell();
        cell59.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell59.setBorder(SolidBorder.NO_BORDER).setBorderBottom(new SolidBorder(1)));
        
        Cell cell60 = new Cell();
        cell60.add(new Paragraph(nomina.getBrutoNomina()+"").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
        tabla4.addCell(cell60.setBorder(SolidBorder.NO_BORDER).setBorderBottom(new SolidBorder(1)));
        
        Cell cell61 = new Cell();
        cell61.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell61.setBorder(SolidBorder.NO_BORDER).setBorderBottom(new SolidBorder(1)));
        
        Cell cell62 = new Cell();
        cell62.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell62.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell63 = new Cell();
        cell63.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell63.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell64 = new Cell();
        cell64.add(new Paragraph("Liquido a percibir").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell64.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell65 = new Cell();
        cell65.add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell65.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell66 = new Cell();
        cell66.add(new Paragraph(nomina.getLiquidoNomina()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla4.addCell(cell66.setBorder(SolidBorder.NO_BORDER));
        
        
        Table tabla5 = new Table(2);
        tabla5.setWidth(500);
        tabla5.setMarginTop(25);
        
        Cell cell67 = new Cell();
        cell67.add(new Paragraph("Calculo empresario: BASE").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell67.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell68 = new Cell();
        cell68.add(new Paragraph(nomina.getBaseEmpresario()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell68.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell69 = new Cell();
        cell69.setPaddingTop(25);
        cell69.add(new Paragraph("Contingencias comunes empresario 23,60%").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell69.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell70 = new Cell();
        cell70.setPaddingTop(25);
        cell70.add(new Paragraph(nomina.getSeguridadSocialEmpresario()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell70.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell71 = new Cell();
        cell71.add(new Paragraph("Desempleo 06,70%").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell71.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell72 = new Cell();
        cell72.add(new Paragraph(nomina.getDesempleoTrabajador()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell72.setBorder(SolidBorder.NO_BORDER));
       
        
        Cell cell75 = new Cell();
        cell75.add(new Paragraph("Desempleo 06,70%").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell75.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell76 = new Cell();
        cell76.add(new Paragraph(nomina.getDesempleoTrabajador()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell76.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell77 = new Cell();
        cell77.add(new Paragraph("Formación 00,60%").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell77.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell78 = new Cell();
        cell78.add(new Paragraph(nomina.getFormacionEmpresario()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell78.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell79 = new Cell();
        cell79.add(new Paragraph("Accidentes de trabajo 01,00%").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell79.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell80 = new Cell();
        cell80.add(new Paragraph(nomina.getAccidentesTrabajoEmpresario()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell80.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell81=  new Cell();
        cell81.add(new Paragraph("FOGASA 00,20%").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell81.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell82 = new Cell();
        cell82.add(new Paragraph(nomina.getFogasaempresario()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell82.setBorder(SolidBorder.NO_BORDER));
        
        Cell cell83 = new Cell();
        cell83.setPaddingBottom(20);
        cell83.add(new Paragraph("Total empresario").setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        tabla5.addCell(cell83.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell84 = new Cell();
        cell84.setPaddingBottom(20);
        cell84.add(new Paragraph(nomina.getCosteTotalEmpresario()+"").setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        tabla5.addCell(cell84.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)));
        
        Cell cell85 = new Cell();
        cell85.add(new Paragraph("COSTE TOTAL TRABAJADOR:").setTextAlignment(TextAlignment.LEFT).setFontSize(10).setFontColor(ColorConstants.RED));
        tabla5.addCell(cell85.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)).setBorderLeft(new SolidBorder(1)));
        
        Cell cell86 = new Cell();
        cell86.add(new Paragraph("Alggoo").setTextAlignment(TextAlignment.RIGHT).setFontSize(10).setFontColor(ColorConstants.RED));
        tabla5.addCell(cell86.setBorder(SolidBorder.NO_BORDER).setBorderTop(new SolidBorder(1)).setBorderBottom(new SolidBorder(1)).setBorderRight(new SolidBorder(1)));
        

        
        doc.add(tabla1);
        doc.add(tabla2);
        doc.add(tabla3);
        doc.add(tabla4);
        doc.add(tabla5);

        doc.close();
    }

    private String getMesString(int mes) {
        String mesLetra = "";
        switch (mes) {
            case 1:
                mesLetra = "Enero";
                break;
            case 2:
                mesLetra = "Febrero";
                break;
            case 3:
                mesLetra = "Marzo";
                break;
            case 4:
                mesLetra = "Abril";
                break;
            case 5:
                mesLetra = "Mayo";
                break;
            case 6:
                mesLetra = "Junio";
                break;
            case 7:
                mesLetra = "Julio";
                break;
            case 8:
                mesLetra = "Agosto";
                break;
            case 9:
                mesLetra = "Septiembre";
                break;
            case 10:
                mesLetra = "Octubre";
                break;
            case 11:
                mesLetra = "Noviembre";
                break;
            case 12:
                mesLetra = "Diciembre";
                break;
        }
        return mesLetra;
    }

}
