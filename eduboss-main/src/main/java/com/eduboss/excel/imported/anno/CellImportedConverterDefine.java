/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported.anno;

import com.eduboss.excel.imported.metadata.ExcelConvertType;

import java.lang.annotation.*;

/**
 * 标识需要转换的字段，通常从数据库转换
 * 
 * @author xiangshaoxu 2016年6月7日下午5:30:43
 * @version 1.0.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellImportedConverterDefine {
	
	ExcelConvertType type();
	
	public String sql() default "";
	
	/**当转换为sql类型时，sql语句返回多于1条记录则报错*/
	public boolean throwIfMoreThanOneReturn() default true;
	
	/**
	 * 指定枚举类型，且默认从静态方法： fromLabel(String)初始化, 枚举必须提供此方法
	 * */
	public  Class<?> enumClazz() default Void.class;
}
