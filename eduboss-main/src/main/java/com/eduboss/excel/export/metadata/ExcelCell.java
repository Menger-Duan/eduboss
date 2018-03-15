package com.eduboss.excel.export.metadata;

import com.eduboss.excel.export.CellVistor;

/**
 * Created by Administrator on 2016/6/23.
 */
public abstract class ExcelCell {
    private int index;

    private ExcelRow parent;

    /**
     * 记录单元格的宽度，仅仅在header中会有意义
     * */
    private int width = 15;

    public ExcelCell(int index, ExcelRow parent) {
        this.index = index;
        this.parent = parent;
    }

    /**
     * 返回单元格的宽度，仅仅在header中会有意义
     * */
    public int getWidth() {
        return width;
    }

    /**
     * 设置单元格的宽度，仅仅在header中会有意义
     * */
    public void setWidth(int width) {
        this.width = width;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ExcelRow getParent() {
        return parent;
    }

    public void setParent(ExcelRow parent) {
        this.parent = parent;
    }

    public abstract void accept(CellVistor vistor);
}
