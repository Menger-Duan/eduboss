/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析excel单元格的上下文
 * 
 * @author xiangshaoxu 2016年6月8日上午10:20:37
 * @version 1.0.0
 */
public class CellParseCtx {
	
	//field>>(value>>transfered)
	private Map<Field, Map<Object, Object>> cache =  new HashMap<Field, Map<Object,Object>>();
	
	/**
	 * 根据field, value取得缓存, 如果没有缓存则返回null
	 * */
	public Object getObject(Field field, Object value) {
		Map<Object, Object> fieldMap = cache.get(field);
		if (fieldMap != null) {
			return fieldMap.get(value);
		}
		return null;
	}
	
	/**
	 * 更新缓存
	 * */
	public void putObject(Field field, Object value, Object bePutted) {
		Map<Object, Object> fieldMap = cache.get(field);
		if (fieldMap == null) {
			fieldMap = new HashMap<Object, Object>();
		} 
		fieldMap.put(value, bePutted);
		cache.put(field, fieldMap);
	}
	
	
	public void clear () {
		cache.clear();
	}
}
