package com.eduboss.excel.export.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class ExcelSheet {

    final int EXCEL_DEFAULT_ROW_HEIGHT = 15;
    final int EXCEL_DEFAULT_CELL_WIDTH = 15;

    private int index;

    private String sheetName;

    private Integer defaultColumnWidth = EXCEL_DEFAULT_CELL_WIDTH;

    public float rowHeight = EXCEL_DEFAULT_ROW_HEIGHT;

    List<ExcelRow> rows = new ArrayList<>();

    public ExcelSheet(int index) {
        this.index = index;
    }

    public ExcelSheet(String sheetName) {
        this.sheetName = sheetName;
    }

    public void addRow(ExcelRow row){
        rows.add(row);
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getDefaultColumnWidth() {
        return defaultColumnWidth;
    }

    public void setDefaultColumnWidth(Integer defaultColumnWidth) {
        this.defaultColumnWidth = defaultColumnWidth;
    }

    public List<ExcelRow> getRows() {
        return rows;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
