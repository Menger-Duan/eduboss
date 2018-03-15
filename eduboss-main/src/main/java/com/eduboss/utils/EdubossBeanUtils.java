/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现bean map 互转, 如果字段为空返回null
 * 
 * @author xiangshaoxu 2016年6月2日下午7:57:14
 * @version 1.0.0
 */
public class EdubossBeanUtils {

	public static Map<String, Object> convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class<?> type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Assert.isNotNull(readMethod, String.format("can't get default getter of bean: %s", bean.getClass().getSimpleName()));
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, null);
				}
			}
		}
		return returnMap;
	}
	
	/**
	 * 返回属性名与setter map
	 * */
	public static Map<String, Method> getFieldName2SetterMap(Class<?> clazz) throws IntrospectionException {
		Map<String, Method> returnMap = new HashMap<>();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method writeMethod = descriptor.getWriteMethod();
				returnMap.put(propertyName, writeMethod);
			}
		}
		return returnMap;
	}

	/**
	 * @param object   对象
	 * @param unField  不需要copy的属性
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object copy(Object object,Map<String,String> unField) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object objectCopy = null;
		if(null == object) {return null;}
		Class<?> classType = object.getClass();
		//取原对象的属性
		Field fields[] = classType.getDeclaredFields();
		//创建赋值对象
		objectCopy = classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
		Field field = null;
		String suffixMethodName;
		Method getMethod, setMethod;
		Object value = null;
		for(int i = 0; i < fields.length; i++) {
			field = fields[i];
			if(unField.get(field.getName())!=null){
				continue;
			}
			suffixMethodName = field.getName().substring(0, 1).toUpperCase() + (field.getName().length()>1?field.getName().substring(1):"");
			getMethod = classType.getMethod("get" + suffixMethodName, new Class[] {});
			setMethod = classType.getMethod("set" + suffixMethodName, new Class[] { field.getType() });
			value = getMethod.invoke(object, new Object[] {});
			if(null == value){
				if(field.getType().getName().equalsIgnoreCase("java.lang.String")){
					setMethod.invoke(objectCopy, new Object[] { "" });
				}
			}else{
				setMethod.invoke(objectCopy, new Object[] { value });
			}
		}
		return objectCopy;
	}
}
