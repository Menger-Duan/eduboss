/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported.anno;

import java.lang.annotation.*;

/**
 * 导入excel时候用于标识此字段，仅用于错误提示时使用
 * 
 * @author xiangshaoxu 2016年6月13日下午11:20:19
 * @version 1.0.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellImportedHeaderDefine {
	String value();
}
