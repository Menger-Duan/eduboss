package com.eduboss.excel.export.metadata;

import com.eduboss.excel.anno.ExcelIgnore;
import com.eduboss.excel.export.ExcelExportEo;
import com.eduboss.excel.export.anno.CellExportedTypeDefine;
import com.eduboss.excel.export.anno.ExportedHeaderDefine;
import com.eduboss.excel.export.anno.ExportedSheetDefine;
import com.eduboss.utils.Assert;
import com.eduboss.utils.EdubossBeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Administrator on 2016/6/23.
 */
public class ExcelData {

    public List<ExcelSheet> sheets = new ArrayList<>();

    private int currentSheetIdx = -1;

    public ExcelData() {
    }

    public void addSheet(ExcelSheet sheet) {
        sheets.add(sheet);
    }

    public List<ExcelSheet> getSheets() {
        return sheets;
    }


    public <T extends ExcelExportEo> void addExcelMetadata(String sheetName, String[] headers, String[] columns, List<Map<String,?>> mapList) {
        Assert.isNotBlank(sheetName, "sheetName of Excel can't be blank");
        Assert.isNotEmpty(headers, "headers of Excel can't be empty");
        Assert.isNotEmpty(columns, "columns of Excel can't be empty");
        Assert.state(headers.length == columns.length, "the count of headers need to equal to columns");

        currentSheetIdx++;

        ExcelSheet sheet = new ExcelSheet(currentSheetIdx);
        sheet.setSheetName(sheetName);

        int rowIndex = 0;
        ExcelRow headerRow = new ExcelRow(rowIndex, sheet);
        for (int i = 0;i < headers.length; i++) {
            HeaderCell cell = new HeaderCell(i, headerRow);
            cell.setContent(headers[i]);
            headerRow.addCell(cell);
        }
        sheet.addRow(headerRow);

        for (int i = 0; i< mapList.size(); i++) {
            rowIndex++;
            Map<String, ?> innerMap = mapList.get(i);
            ExcelRow row = new ExcelRow(rowIndex, sheet);
            for (int j = 0; j < columns.length; j++) {
                String columnStr = columns[j];
                ExcelCell cell = transferObj2Cell(innerMap.get(columnStr), null, j, row);
                row.addCell(cell);
            }
            sheet.addRow(row);
        }

        addSheet(sheet);
    }


    /**
     * 增加一种类型的vo,可以连续加入多个类型的vo导出为多个sheet
     * */
    public <T extends ExcelExportEo> void addExcelMetadata(List<T> vos) {
        Assert.isNotEmpty(vos, "excel vos can't be empty.");

        currentSheetIdx++;
        ExcelExportEo firstVo = vos.get(0);
        ExcelSheet sheet = new ExcelSheet(currentSheetIdx);

        //createSheet
        ExportedSheetDefine sheetAnn = firstVo.getClass().getAnnotation(ExportedSheetDefine.class);
        if (sheetAnn != null) {
            sheet.setSheetName(sheetAnn.sheetName());
            sheet.setDefaultColumnWidth(sheetAnn.defaultColumnWidth());
            sheet.setRowHeight(sheetAnn.rowHeight());
        } else {
            sheet.setSheetName(firstVo.getClass().getSimpleName());
            sheet.setDefaultColumnWidth(20);
            sheet.setRowHeight(20);
        }

        // createHeaderRow
        int rowIndex = 0;
        ExcelRow headerRow = new ExcelRow(rowIndex, sheet);
        Field[] fields = getNotIgnoreFields(firstVo);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExportedHeaderDefine headerAnn = field.getAnnotation(ExportedHeaderDefine.class);
            HeaderCell header = new HeaderCell(i, headerRow);
            if (headerAnn != null) {
                header.setContent(headerAnn.headerName());
                header.setWidth(headerAnn.cellWidth());
            } else {
                header.setContent(" ");
                header.setWidth(20);
            }
            headerRow.addCell(header);

        }
        sheet.addRow(headerRow);

        // createRows
        for (ExcelExportEo vo : vos) {
            rowIndex++;
            ExcelRow row = new ExcelRow(rowIndex, sheet);

            Map<?, ?> map = null;
            try {
                map = EdubossBeanUtils.convertBean(vo);
            } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                Assert.state(false, String.format("excel vo: %s reflecting failed, Be sure that there is one Getter for each field.", vo.getClass().getSimpleName()), e);
            }

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Object obj = map.get(field.getName());
                ExcelCell cell = transferObj2Cell(obj, field, i, row);
                row.addCell(cell);
            }
            sheet.addRow(row);
        }

        addSheet(sheet);
    }

    private Field[] getNotIgnoreFields(ExcelExportEo firstVo) {
        Field[] fields = firstVo.getClass().getDeclaredFields();
        for (Field field : fields) {
            ExcelIgnore ignoreAnn = field.getAnnotation(ExcelIgnore.class);
            if (ignoreAnn != null) {
                fields = ArrayUtils.removeElement(fields, field);
            }
        }
        return fields;
    }


    /**
     * 从object根据类型生成对应的excelCell
     * @param value : the value of this filed, CAN be null
     * @param field : the annotation attached , CAN be null
     * */
    private ExcelCell transferObj2Cell(Object value , Field field, int index, ExcelRow parent) {
        if (value == null) {
            return CellExportedType.STRING.transferObj2Cell(value, field, index, parent);
        }

        value = transferValueIfNecessary(value, field);
        if (value instanceof CharSequence) {
            CellExportedType exportedType = getExportedType(field, CellExportedType.STRING);
            return exportedType.transferObj2Cell(value, field, index, parent);
        }
        if (value instanceof Boolean) {
            CellExportedType exportedType = getExportedType(field, CellExportedType.BOOLEAN);
            return exportedType.transferObj2Cell(value, field, index, parent);
        }
        if (value instanceof Number) {
            CellExportedType exportedType = getExportedType(field, CellExportedType.NUMBER);
            return exportedType.transferObj2Cell(value, field, index, parent);
        }
        if (value instanceof Date) {
            CellExportedType exportedType = getExportedType(field, CellExportedType.DATE);
            return exportedType.transferObj2Cell(value, field, index, parent);
        }
        if (value instanceof Calendar) {
            CellExportedType exportedType = getExportedType(field, CellExportedType.CALENDAR);
            return exportedType.transferObj2Cell(value, field, index, parent);
        }
        // alternatively, we can ignore this cell
        Assert.state(false, String.format("unsupported field type: %s  , can't be set to excel cell.", value.getClass().getSimpleName()));
        return null;
    }

    private CellExportedType getExportedType(Field field, CellExportedType defaultValue) {
        if (field == null) {
            return defaultValue;
        }
        CellExportedType exportedType;
        CellExportedTypeDefine toTypeAnn = field.getAnnotation(CellExportedTypeDefine.class);
        if (toTypeAnn != null) {
            exportedType = toTypeAnn.toCellType();
        } else {
            exportedType = defaultValue;
        }
        return exportedType;
    }

    private Object transferValueIfNecessary(Object value, Field field) {
        if (field != null){
            CellExportedTypeDefine typeDefineAnn = field.getAnnotation(CellExportedTypeDefine.class);
            if (typeDefineAnn != null){
                CellExportedType exportedType = typeDefineAnn.toCellType();
                value = exportedType.transferValue(value, field);
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
