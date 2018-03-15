package com.eduboss.excel.export.anno;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/6/23.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportedSheetDefine {
    String sheetName();
    int defaultColumnWidth() default 15;
    float rowHeight() default 15;
}
