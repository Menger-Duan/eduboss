/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported.metadata;

import com.eduboss.excel.imported.anno.CellImportedHeaderDefine;

import java.lang.reflect.Field;

/**
 * 
 * @author xiangshaoxu 2016年6月13日下午11:33:28
 * @version 1.0.0
 */
public abstract class ImportedEoUtil {
	
	/**
	 * 取得ImportedEo.field名字，优先取得{@link CellImportedHeaderDefine} ， 然后去field.getName()
	 * */
	public static String getEoFieldName(Field field) {
		CellImportedHeaderDefine ann = field.getAnnotation(CellImportedHeaderDefine.class);
		if (ann != null) {
			return ann.value();
		} else {
			return field.getName();
		}
	}
}
