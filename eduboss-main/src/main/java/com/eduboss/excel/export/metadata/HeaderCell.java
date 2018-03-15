package com.eduboss.excel.export.metadata;

import com.eduboss.excel.export.CellVistor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Administrator on 2016/6/23.
 */
public class HeaderCell extends ExcelCell {
    public HeaderCell(int index, ExcelRow parent) {
        super(index, parent);
    }

    private String content;

    @Override
    public void accept(CellVistor vistor) {
        vistor.visit(this);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
