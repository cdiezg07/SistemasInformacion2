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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    ArrayList<String> prorrataExtra = new ArrayList<>();
    ArrayList<Integer> rowNull = new ArrayList<>();
    ArrayList<String> primeraFila = new ArrayList<>();
    
    HashMap<String, String[]> categorias = new HashMap<>();
    HashMap<String, String> trienios = new HashMap<>();
    HashMap<String, String> brutoAnual = new HashMap<>();
    HashMap<String, String> cuotas = new HashMap<>();
    
    
    public ArrayList<Trabajadorbbdd> accesoHoja3() throws FileNotFoundException, IOException, ParseException {
        String excelFilePath = "./resources/SistemasInformacionII.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

        Workbook workbook = WorkbookFactory.create(new File(excelFilePath));

        Sheet sheet = workbook.getSheetAt(2);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter fmt = new DataFormatter();

        ArrayList<Trabajadorbbdd> atb = new ArrayList<Trabajadorbbdd>();
        Trabajadorbbdd tb = null;

        for (int rn = 0; rn <= sheet.getLastRowNum(); rn++) {
            Row row = sheet.getRow(rn);

            ArrayList<String> col = new ArrayList<String>();

            Categorias c = new Categorias();
            // Row "rn" has data
            for (int cn = 0; cn < sheet.getRow(0).getLastCellNum(); cn++) {
                Cell cell = row.getCell(cn);
                String cellStr = "";

                if (cell == null) {
                    // This cell is empty/blank/un-used, handle as needed
                    col.add(cellStr);
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                String cellValue = formatter1.format(cell.getDateCellValue());
                                col.add(cellValue);
                            }
                            break;
                        default:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                    }
                }
            }
            //Id de la columna
            col.add(Integer.toString(rn));

            if (rn == 0) {
                primeraFila = col;
            } else if (col.get(0).equals("")) {
                rowNull.add(0);
            } else {
                rowNull.add(1);
                tb = new Trabajadorbbdd();
                tb.setEmpresas(new Empresas(col.get(0), col.get(1)));
                c.setNombreCategoria(col.get(2));
                tb.setCategorias(c);
                tb.setFechaAlta(formatter1.parse(col.get(3)));
                tb.setNombre(col.get(6));
                tb.setApellido1(col.get(4));
                tb.setApellido2(col.get(5));
                tb.setEmail(col.get(12));
                tb.setNifnie(col.get(7));
                prorrataExtra.add(col.get(8));
                tb.setCodigoCuenta(col.get(9));
                tb.setIban(col.get(10));
                tb.setIdTrabajador(Integer.parseInt(col.get(13)) + 1);
                atb.add(tb);
            }

        }

        workbook.close();
        inputStream.close();

        return atb;
    }

    public void accesoHoja1() throws FileNotFoundException, IOException, ParseException {
        String excelFilePath = "./resources/SistemasInformacionII.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

        Workbook workbook = WorkbookFactory.create(new File(excelFilePath));

        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter fmt = new DataFormatter();

        for (int rn = 0; rn < 15; rn++) {
            Row row = sheet.getRow(rn);

            ArrayList<String> col = new ArrayList<String>();

            // Row "rn" has data
            for (int cn = 0; cn < 3; cn++) {
                Cell cell = row.getCell(cn);
                String cellStr = "";

                if (cell == null) {
                    // This cell is empty/blank/un-used, handle as needed
                    col.add(cellStr);
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                String cellValue = formatter1.format(cell.getDateCellValue());
                                col.add(cellValue);
                            }else{
                                cellStr = fmt.formatCellValue(cell);
                                col.add(cellStr);
                            }
                            break;
                        default:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                    }
                }
            }
            //Id de la columna
            if (rn == 0) {
            } else if (col.get(0).equals("")) {

            } else {
                categorias.put(col.get(0), new String[]{col.get(1), col.get(2)});
            }

        }

        for (int rn = 0; rn < 19; rn++) {
            Row row = sheet.getRow(rn);

            ArrayList<String> col = new ArrayList<String>();

            // Row "rn" has data
            for (int cn = 5; cn < 7; cn++) {
                Cell cell = row.getCell(cn);
                String cellStr = "";

                if (cell == null) {
                    // This cell is empty/blank/un-used, handle as needed
                    col.add(cellStr);
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                String cellValue = formatter1.format(cell.getDateCellValue());
                                col.add(cellValue);
                            }else{
                                cellStr = fmt.formatCellValue(cell);
                                col.add(cellStr);
                            }
                            break;
                        default:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                    }
                }
            }
            //Id de la columna
            if (rn == 0) {
            } else if (col.get(0).equals("")) {
            } else {
                trienios.put(col.get(0), col.get(1));
            }
        }
        workbook.close();
        inputStream.close();
        
    }

    public void accesoHoja2() throws FileNotFoundException, IOException, ParseException {
        String excelFilePath = "./resources/SistemasInformacionII.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

        Workbook workbook = WorkbookFactory.create(new File(excelFilePath));

        Sheet sheet = workbook.getSheetAt(1);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter fmt = new DataFormatter();

        for (int rn = 0; rn < 50; rn++) {
            Row row = sheet.getRow(rn);

            ArrayList<String> col = new ArrayList<String>();

            // Row "rn" has data
            for (int cn = 0; cn < 2; cn++) {
                Cell cell = row.getCell(cn);
                String cellStr = "";

                if (cell == null) {
                    // This cell is empty/blank/un-used, handle as needed
                    col.add(cellStr);
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                String cellValue = formatter1.format(cell.getDateCellValue());
                                col.add(cellValue);
                            }else{
                                cellStr = fmt.formatCellValue(cell);
                                col.add(cellStr);
                            }
                            break;
                        default:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                    }
                }
            }
            //Id de la columna
            if (rn == 0) {
                
            } else if (col.get(0).equals("")) {
            } else {
                brutoAnual.put(col.get(0), col.get(1));
            }

        }

        for (int rn = 0; rn < 8; rn++) {
            Row row = sheet.getRow(rn);

            ArrayList<String> col = new ArrayList<String>();

            // Row "rn" has data
            for (int cn = 5; cn < 7; cn++) {
                Cell cell = row.getCell(cn);
                String cellStr = "";

                if (cell == null) {
                    // This cell is empty/blank/un-used, handle as needed
                    col.add(cellStr);
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                String cellValue = formatter1.format(cell.getDateCellValue());
                                col.add(cellValue);
                            }else{
                                cellStr = fmt.formatCellValue(cell);
                                col.add(cellStr);
                            }
                            break;
                        default:
                            cellStr = fmt.formatCellValue(cell);
                            col.add(cellStr);
                            break;
                    }
                }
            }
            //Id de la columna
            if (col.get(0).equals("")) {
            } else {
                cuotas.put(col.get(0), col.get(1));
            }

        }
        workbook.close();
        inputStream.close();

    }

    
    
    public void cargarNuevosDatos(ArrayList<Trabajadorbbdd> atb) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        Cell cell = null;
        short dateFormat = createHelper.createDataFormat().getFormat("dd/MM/yyyy");
        cellStyle.setDataFormat(dateFormat);

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Hoja 3");

        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        for (int i = 0; i < 13; i++) {
            row.createCell(i).setCellValue(primeraFila.get(i));
        }

        int x = 0;
        for (int i = 0; i < rowNull.size(); i++) {
            row = sheet.createRow(rowNum++);
            if (rowNull.get(i) == 1) {
                row.createCell(0).setCellValue(atb.get(i - x).getEmpresas().getNombre());
                row.createCell(1).setCellValue(atb.get(i - x).getEmpresas().getCif());
                row.createCell(2).setCellValue(atb.get(i - x).getCategorias().getNombreCategoria());
                cell = row.createCell(3);
                cell.setCellValue(atb.get(i - x).getFechaAlta());
                cell.setCellStyle(cellStyle);
                row.createCell(4).setCellValue(atb.get(i - x).getApellido1());
                row.createCell(5).setCellValue(atb.get(i - x).getApellido2());
                row.createCell(6).setCellValue(atb.get(i - x).getNombre());
                row.createCell(7).setCellValue(atb.get(i - x).getNifnie());
                row.createCell(8).setCellValue(prorrataExtra.get(i - x));
                row.createCell(9).setCellValue(atb.get(i - x).getCodigoCuenta());
                row.createCell(10).setCellValue(atb.get(i - x).getIban().substring(0, 2));
                row.createCell(11).setCellValue(atb.get(i - x).getIban());
                row.createCell(12).setCellValue(atb.get(i - x).getEmail());
            } else {
                x++;
            }

        }

        // Resize all columns to fit the content size
        for (int i = 0; i < atb.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("./resources/SistemasInformacionIITrasEjecucion.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }
    
    public HashMap getBrutoAnual(){
        return this.brutoAnual;
    }
    public HashMap getTrienios(){
            return this.trienios;
        }
    public HashMap getCuotas(){
            return this.cuotas;
        }
    public HashMap getCategorias(){
        return this.categorias;
    }
    public ArrayList<String> getProrrata(){
        return this.prorrataExtra;
    }
}
