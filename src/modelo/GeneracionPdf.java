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

/**
 *
 * @author carlos
 */
public class GeneracionPdf {

    
    public void GeneracionPdfNominas(Nomina nom) throws FileNotFoundException{
       // PdfWriter writer = new PdfWriter("./resources/nominas/"+tb.getNifnie()+tb.getNombre()+tb.getApellido1()+tb.getApellido2()+".pdf");
       // PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, PageSize.LETTER);

        Paragraph empty = new Paragraph("");
        Table tabla1 = new Table(2);
        tabla1.setWidth(500);

//        Paragraph nom = new Paragraph("NOMBRE"+tb.getNombre());
//        Paragraph cif = new Paragraph("CIF: "+tb.getEmpresas().getCif());

        Paragraph dir1 = new Paragraph("Avenida de la facultad - 6");
        Paragraph dir2 = new Paragraph("24001 León");

        Cell cell1 = new Cell();
        
        cell1.setWidth(250);
        cell1.setTextAlignment(TextAlignment.CENTER);

        cell1.add(nom);
        cell1.add(cif);
        cell1.add(dir1);
        cell1.add(dir2);
        tabla1.addCell(cell1);

        Cell cell2 = new Cell();
        cell2.setPadding(10);
        cell2.setTextAlignment(TextAlignment.RIGHT);
//        cell2.add(new Paragraph("IBAN: "+tb.getIban()));
//        cell2.add(new Paragraph("Bruto anual: "));
//        cell2.add(new Paragraph("Categoría: "+tb.getCategorias().getNombreCategoria()));
//        cell2.add(new Paragraph("Fecha de alta: "+tb.getFechaAlta()));
        tabla1.addCell(cell2);



        Table tabla2 = new Table(2); tabla2.setWidth(500);
        
        Cell cell3 = new Cell();
        
        cell3.setPaddingLeft(23);
        cell3.setPaddingTop(20);



        cell3.setWidth(250);
        tabla2.addCell(cell3);

        doc.add(tabla1);
        doc.add(tabla2);

        doc.close();
    }
   
}
