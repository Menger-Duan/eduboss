package com.eduboss.excel.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface ExcelExporter {

    /**增加要导出的数据，每次为一个sheet,可以多次增加*/
    public abstract <T extends ExcelExportEo> ExcelExporter addData(List<T> vos);

    /**增加要导出的数据，每次为一个sheet,可以多次增加*/
    public abstract <T extends ExcelExportEo> ExcelExporter addData(String sheetName, String[] headers, String[] columns, List<Map<String, ?>> mapList);

    /**
     * 创建 excel文件，你需要至少调用一次addData()增加sheet
     * */
    public abstract ExcelExporter createExcel();


    /**
     * 创建 excel文件，你需要至少调用一次addData()增加sheet
     * */
    public abstract ExcelExporter createExcel(TimeZone zone, Locale local);

    public abstract void export2Stream(OutputStream out) throws IOException;

}
