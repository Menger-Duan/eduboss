/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported.anno;

import java.lang.annotation.*;

/**
 * 标识要导入的excel sheet名
 * 
 * @author xiangshaoxu 2016年6月2日上午9:36:04
 * @version 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportedSheetDefine {
	String sheetName() default "Sheet1";
}
