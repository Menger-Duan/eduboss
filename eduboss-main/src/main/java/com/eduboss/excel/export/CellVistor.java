package com.eduboss.excel.export;

import com.eduboss.excel.export.metadata.*;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface CellVistor {
    void visit(HeaderCell excelCell);

    void visit(StringCell excelCell);

    void visit(DoubleCell excelCell);

    void visit(BooleanCell excelCell);

    void visit(CalendarCell excelCell);

    void visit(DateCell excelCell);

    void visit(UrlCell excelCell);
}
