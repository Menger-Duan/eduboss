package com.eduboss.excel.export.metadata;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class ExcelRow {
    private List<ExcelCell> cells = new ArrayList<ExcelCell>();

    private int index;

    private ExcelSheet parent;

    public ExcelRow(int index, ExcelSheet parent) {
        this.index = index;
        this.parent = parent;
    }

    public Integer sizeOfCols() {
        return cells.size();
    }

    public void addCell(ExcelCell e) {
        cells.add(e);
    }

    public List<ExcelCell> getCells() {
        return cells;
    }

    public int getIndex() {
        return index;
    }

    public ExcelSheet getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
