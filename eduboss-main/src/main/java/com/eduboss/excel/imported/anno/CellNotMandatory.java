/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported.anno;

import java.lang.annotation.*;

/**
 * 标识导入的字段非强制要求输入的，非必填
 * 
 * @author xiangshaoxu 2016年6月8日下午12:19:51
 * @version 1.0.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellNotMandatory {

}
