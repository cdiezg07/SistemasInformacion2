/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author carlos
 */
public class AccesoExcel {

    public void acceso() throws IOException {
        String excelFilePath = "./resources/SistemasInformacionII.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = WorkbookFactory.create(new File(excelFilePath));
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        HashMap<String, int[]> categoria = new HashMap<String, int[]>();

        // 1. You can obtain a rowIterator and columnIterator and iterate over them
        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            ArrayList<String> col = new ArrayList<String>();

//            while (cellIterator.hasNext()) {
            int[] salarios = new int[2];
            String cate = "";
            for (int i = 0; i < 3; i++) {

                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                if (i == 0) {
                    cate = cellValue;
                } else {
                    salarios[i - 1] = Integer.parseInt(cellValue);

                }
                System.out.print(cellValue + "\t");
            }
            categoria.put(cate, salarios);

            for (int i = 0; i < 2; i++) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                //  categoria.put(cellValue, new )
                System.out.print(cellValue + "\t");
            }

//            }
            System.out.println();
        }

        workbook.close();
        inputStream.close();

        // Create a DataFormatter to format and get each cell's value as String
//        DataFormatter dataFormatter = new DataFormatter();
//        ArrayList<Trabajadorbbdd> atb = new ArrayList<Trabajadorbbdd>();
//
//        // 1. You can obtain a rowIterator and columnIterator and iterate over them
//        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
//        Iterator<Row> rowIterator = sheet.rowIterator();
//        rowIterator.next();
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//
//            // Now let's iterate over the columns of the current row
//            Iterator<Cell> cellIterator = row.cellIterator();
//            ArrayList<String> col = new ArrayList<String>();
//
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//                String cellValue = dataFormatter.formatCellValue(cell);
//                col.add(cellValue);
//                System.out.print(cellValue + "\t");
//            }
//
//            Trabajadorbbdd tb = new Trabajadorbbdd(new Categorias(col.get(2)),);
//            System.out.println();
//        }
//
//        workbook.close();
//        inputStream.close();
    }

    public ArrayList<Trabajadorbbdd> accesoHoja3() throws FileNotFoundException, IOException, ParseException {
        String excelFilePath = "./resources/SistemasInformacionII.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");  

        Workbook workbook = WorkbookFactory.create(new File(excelFilePath));
        // workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        Sheet sheet = workbook.getSheetAt(2);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter fmt = new DataFormatter();

        ArrayList<Trabajadorbbdd> atb = new ArrayList<Trabajadorbbdd>();
        Trabajadorbbdd tb = null;
        // 1. You can obtain a rowIterator and columnIterator and iterate over them
        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");

        for (int rn = 1; rn <= sheet.getLastRowNum(); rn++) {
            Row row = sheet.getRow(rn);
            if (row == null) {
                // There is no data in this row, handle as needed
            } else {
                ArrayList<String> col = new ArrayList<String>();
                // Row "rn" has data
                for (int cn = 0; cn < 13; cn++) {
                    Cell cell = row.getCell(cn);
                    String cellStr = "";
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        // This cell is empty/blank/un-used, handle as needed
                        col.add(cellStr);
                        System.out.print(cellStr + "\t");
                    } else {
                        cellStr = fmt.formatCellValue(cell);
                        col.add(cellStr);
                        System.out.print(cellStr + "\t");

                    }

                }
                
                if(!col.get(0).equals("")){
                    tb = new Trabajadorbbdd();
                tb.setEmpresas(new Empresas(col.get(0), col.get(1)));
                //tb.setCategorias((new Categorias()).setNombreCategoria(col.get(2)));
                tb.setFechaAlta(formatter1.parse(col.get(3)));
                System.out.println(new SimpleDateFormat("dd/MM/yyyy").parse(col.get(3))+"--------------------------------------------------------");
                tb.setNombre(col.get(6));
                tb.setApellido1(col.get(4));
                tb.setApellido2(col.get(5));
                tb.setEmail(col.get(12));
                tb.setNifnie(col.get(7));
                tb.setCodigoCuenta(col.get(9));
                tb.setIban(col.get(10));
                atb.add(tb);
                }
                
            }

            System.out.println();
        }

        workbook.close();
        inputStream.close();

        return atb;
    }

    public void cargarNuevosDatos(ArrayList<Trabajadorbbdd> atb) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat, 
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Hoja 3");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        int rowNum = 1;
        for (int i = 0; i < atb.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            if (!atb.get(i).getNombre().equals("")) {
                row.createCell(0).setCellValue(atb.get(i).getEmpresas().getNombre());
                row.createCell(1).setCellValue(atb.get(i).getEmpresas().getCif());
                //row.createCell(2).setCellValue(atb.get(i).getCategorias().getNombreCategoria());
               row.createCell(3).setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(atb.get(i).getFechaAlta()));
               System.out.println(atb.get(i).getFechaAlta().toString());
               row.createCell(4).setCellValue(atb.get(i).getApellido1());
               row.createCell(5).setCellValue(atb.get(i).getApellido2());
               row.createCell(6).setCellValue(atb.get(i).getNombre());
               row.createCell(7).setCellValue(atb.get(i).getNifnie());
               row.createCell(8).setCellValue(atb.get(i).getNifnie());
               row.createCell(9).setCellValue(atb.get(i).getCodigoCuenta());
               row.createCell(10).setCellValue(atb.get(i).getIban());
               row.createCell(11).setCellValue(atb.get(i).getEmail());
            }

        }

        // Resize all columns to fit the content size
        for (int i = 0; i < atb.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("./resources/Nuevo.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

}
