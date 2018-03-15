/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported.anno;

import com.eduboss.excel.imported.metadata.CellValidationType;

import java.lang.annotation.*;

/**
 * 验证导入的excel cell content
 * 
 * @author xiangshaoxu 2016年6月13日上午10:26:25
 * @version 1.0.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellValidationDefine {

	CellValidationType type() default CellValidationType.REGULAR;
	String regEx() default "";
	String message() default "validating failed.";
	
}
