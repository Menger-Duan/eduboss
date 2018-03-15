/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.anno;

import java.lang.annotation.*;

/**
 * 标记不需要导出&导入的字段
 * 
 * @author xiangshaoxu 2016年6月3日下午4:34:30
 * @version 1.0.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelIgnore {

}
