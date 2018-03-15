/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.export.anno;

import com.eduboss.IConstracts;

import java.lang.annotation.*;

/**
 * 对要导出为excel的字段标识导出表头字段名称
 * 
 * @author xiangshaoxu 2016年6月2日上午9:36:04
 * @version 1.0.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportedHeaderDefine {
	String headerName();
	int cellWidth() default IConstracts.EXCEL_DEFAULT_CELL_WIDTH;
}
