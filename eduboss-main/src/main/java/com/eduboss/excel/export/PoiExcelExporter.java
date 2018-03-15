package com.eduboss.excel.export;

import com.eduboss.excel.export.metadata.*;
import com.eduboss.memcached.NestedIOException;
import com.eduboss.utils.Assert;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.ejb.config.JeeNamespaceHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Administrator on 2016/6/23.
 */
public class PoiExcelExporter implements ExcelExporter{

    private boolean excelCreated = false;

    private ExcelData excelData;

    private PoiCellVistor cellVistor;

    private XSSFWorkbook workbook = new XSSFWorkbook();

    private static Map<Class<?>, XSSFCellStyle> styleMap = new HashMap<>();
    StyleGenerator styleGenerator = new StyleGenerator(workbook);


    private PoiExcelExporter() {
        initCellStyleMap();
        excelData = new ExcelData();
        cellVistor = new PoiCellVistor(this);
    }

    private void initCellStyleMap() {
        styleMap.put(StringCell.class, styleGenerator.defaultStringCellStyle());
        styleMap.put(HeaderCell.class, styleGenerator.defaultHeaderStyle());
        styleMap.put(DoubleCell.class, styleGenerator.defaultStringCellStyle());
        styleMap.put(BooleanCell.class, styleGenerator.defaultStringCellStyle());
        styleMap.put(UrlCell.class, styleGenerator.defaultUrlCellStyle());
        styleMap.put(DateCell.class, styleGenerator.defaultDateCellStyle());
        styleMap.put(CalendarCell.class, styleGenerator.defaultDateCellStyle());
    }

    public static PoiExcelExporter newInstance() {
        return new PoiExcelExporter();
    }

    @Override
    public <T extends ExcelExportEo> ExcelExporter  addData(List<T> eos) {
        excelData.addExcelMetadata(eos);
        return  this;
    }

    @Override
    public <T extends ExcelExportEo> ExcelExporter addData(String sheetName, String[] headers, String[] columns, List<Map<String, ?>> mapList) {
        excelData.addExcelMetadata(sheetName, headers, columns, mapList);
        return this;
    }

    @Override
    public ExcelExporter createExcel() {
        return createExcel(TimeZone.getDefault(), Locale.getDefault());
    }

    @Override
    public ExcelExporter createExcel(TimeZone zone, Locale local) {
        Assert.state(excelData.getSheets().size() > 0, "at leaset one sheet in Excel.");

        for (ExcelSheet excelSheet : excelData.getSheets()) {

            XSSFSheet sheet = createSheet(excelSheet);

            createRows(sheet, excelSheet, zone, local);
        }

        excelCreated = true;

        excelData = null;
        return this;
    }

    @Override
    public void export2Stream(OutputStream out) throws IOException {
        Assert.state(excelCreated, "you need to add data and createExcel first.");
        this.workbook.write(out);
        workbook = null;
    }

    XSSFCellStyle getDefaultCellStyle(ExcelCell excelCell) {
        XSSFCellStyle style = styleMap.get(excelCell.getClass());
        if (style == null) {
            style =  styleMap.get(StringCell.class);
        }
        return style;
    }

    XSSFCellStyle getDateCellStyle(String dateformat) {
        return styleGenerator.dateCellStyle(dateformat);
    }

    private XSSFSheet createSheet(ExcelSheet excelSheet) {
        XSSFSheet sheet = workbook.createSheet(excelSheet.getSheetName());
        List<ExcelCell> headers = excelSheet.getRows().get(0).getCells();
        for (int i = 0; i < headers.size(); i++) {
            ExcelCell header = headers.get(i);
            sheet.setColumnWidth(i, header.getWidth()*256);
        }

        sheet.setDefaultColumnWidth(excelSheet.getDefaultColumnWidth());
        return sheet;
    }

    private void createRows(XSSFSheet sheet, ExcelSheet excelSheet, TimeZone zone, Locale local) {
        int rowIndex = -1; //row num counter begin at 1
        for(ExcelRow excelRow : excelSheet.getRows()) {
            rowIndex++;
            XSSFRow row = sheet.createRow(rowIndex);
            row.setHeightInPoints(excelSheet.getRowHeight());
            createCell(row, excelRow, zone, local);
        }
    }

    private void createCell(XSSFRow row, ExcelRow excelRow, TimeZone zone, Locale local) {
        int columnIndex = -1;
        for(ExcelCell excelCell : excelRow.getCells()) {
            columnIndex++;
            XSSFCell cell = row.createCell(columnIndex);
            cellVistor.setCell(cell);
            excelCell.accept(cellVistor);
        }
    }
}
