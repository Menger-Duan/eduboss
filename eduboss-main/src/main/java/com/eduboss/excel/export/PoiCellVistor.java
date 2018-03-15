package com.eduboss.excel.export;

import com.eduboss.excel.export.metadata.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.util.Assert;

import javax.swing.*;

/**
 * Created by Administrator on 2016/6/23.
 */
public class PoiCellVistor implements CellVistor {

    XSSFCell cell;
    PoiExcelExporter accessor;

    public PoiCellVistor(PoiExcelExporter accessor) {
        this.accessor = accessor;
    }

    public void setCell(XSSFCell cell) {
        this.cell = cell;
    }

    @Override
    public void visit(final StringCell excelCell) {
        checkAndRemoveCell(new AbstractFunction() {

            @Override
            public void apply() {
                cell.setCellValue(excelCell.getContent());
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                XSSFCellStyle style = getStyle(excelCell);
                cell.setCellStyle(style);
            }

        });
    }

    @Override
    public void visit(final DoubleCell excelCell) {
        checkAndRemoveCell(new AbstractFunction() {

            @Override
            public void apply() {
                cell.setCellValue(excelCell.getContent());
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                XSSFCellStyle style = getStyle(excelCell);
                cell.setCellStyle(style);
            }

        });
    }

    @Override
    public void visit(final BooleanCell excelCell) {
        checkAndRemoveCell(new AbstractFunction() {

            @Override
            public void apply() {
                cell.setCellValue(excelCell.getContent());
                cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
                XSSFCellStyle style = getStyle(excelCell);
                cell.setCellStyle(style);
            }

        });
    }

    @Override
    public void visit(final CalendarCell excelCell) {
        checkAndRemoveCell(new AbstractFunction() {

            @Override
            public void apply() {
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(excelCell.getContent());
                XSSFCellStyle style = getStyle(excelCell);
                cell.setCellStyle(style);
            }

        });
    }

    @Override
    public void visit(final DateCell excelCell) {
        checkAndRemoveCell(new AbstractFunction() {

            @Override
            public void apply() {
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(excelCell.getContent());
                XSSFCellStyle style = null;
                String dateformat = excelCell.getDateformat();
                if (StringUtils.isNotBlank(dateformat)) {
                    style = accessor.getDateCellStyle(dateformat);
                }  else {
                    style = getStyle(excelCell);
                }
                cell.setCellStyle(style);
            }

        });
    }

    @Override
    public void visit(final UrlCell excelCell) {

        checkAndRemoveCell(new AbstractFunction() {

            @Override
            public void apply() {
                String linkLabel = excelCell.getLinkLabel();
                String linkUrl = excelCell.getLinkUrl();
                if (StringUtils.isBlank(linkLabel)) {
                    linkLabel = linkUrl;
                }
                if (StringUtils.isNotBlank(linkUrl)) {
                    cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula("HYPERLINK(\"" + linkUrl + "\",\"" + linkLabel + "\")");
                }
                XSSFCellStyle style = getStyle(excelCell);
                cell.setCellStyle(style);
            }

        });
    }

    @Override
    public void visit(final HeaderCell excelCell) {

        checkAndRemoveCell(new AbstractFunction() {

            @Override
            public void apply() {
                XSSFRichTextString text = new XSSFRichTextString(excelCell.getContent());
                cell.setCellValue(text);
                XSSFCellStyle style = getStyle(excelCell);
                cell.setCellStyle(style);
            }

        });
    }

    public  void checkAndRemoveCell(AbstractFunction fun) {
//        Asserts.notNull(cell, "XSSFCell in PoiExcelVistor can't be null");
        fun.apply();
        cell = null;
    }

    abstract class  AbstractFunction {
        abstract void apply();

        public XSSFCellStyle getStyle(ExcelCell excelCell) {
            XSSFCellStyle style = accessor.getDefaultCellStyle(excelCell);
            Assert.state(style != null);
            return style;
        }
    }


}
