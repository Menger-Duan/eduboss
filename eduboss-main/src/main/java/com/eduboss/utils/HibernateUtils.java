package com.eduboss.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

import com.eduboss.domain.ContractProduct;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.CourseExcelVo;
import com.eduboss.dto.DataPackage;

public class HibernateUtils {
	
	public static String CRITERIA_CASCADE_CONCAT_FLAG = "_";
	
	private static Mapper mapper = (Mapper) ApplicationContextUtil.getContext().getBean("mapper");
	
	// 如果前端有提交排序信息，则用前端的排序信息，否则使用方法调用中定义的默认排序
	public static List<Order> prepareOrder(DataPackage dp, String defaultPropertity, String defaultOrder) {
		List<Order> scoreOrderList = new ArrayList<Order>();
		Order propertityOrder = null;
		if (dp!= null 
				&&StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			// 前端提交的排序
			propertityOrder = "asc".equalsIgnoreCase(dp.getSord()) ? Order
					.asc(dp.getSidx()) : Order.desc(dp.getSidx());
		} else {
			// 默认排序
			propertityOrder = "asc".equalsIgnoreCase(defaultOrder) ? Order
					.asc(defaultPropertity) : Order.desc(defaultPropertity);
		}
		scoreOrderList.add(propertityOrder);
		return scoreOrderList;
	}

	public static List<Criterion> buildAndLikeCriterionWhenPropertiesNotEmty(Object object) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		Criterion andCriterion = null;
		
		for (Method method : object.getClass().getDeclaredMethods()) {
			try {
				if (method.getName().startsWith("get")) {
					Object properyValue = method.invoke(object);
					if (properyValue != null && !"".equals(properyValue)) {
						String propertyName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4, method.getName().length());
						Criterion criterion = null;
						Field field = object.getClass().getDeclaredField(propertyName);
						if (field.getType().getSimpleName().equalsIgnoreCase("String")) {
							criterion = Expression.like(propertyName, properyValue.toString(), MatchMode.ANYWHERE);
						} else if (field.getType().getSimpleName().toLowerCase().contains("short")) {
							criterion = Expression.eq(propertyName, Short.valueOf(properyValue.toString()));
						}  else if (field.getType().getSimpleName().toLowerCase().contains("int")) {
							criterion = Expression.eq(propertyName, Integer.valueOf(properyValue.toString()));
						} else if (field.getType().getSimpleName().toLowerCase().contains("long")) {
							criterion = Expression.eq(propertyName, Long.valueOf(properyValue.toString()));
						} else if (field.getType().getSimpleName().toLowerCase().contains("double")) {
							criterion = Expression.eq(propertyName, Double.valueOf(properyValue.toString()));
						} else if (field.getType().getSimpleName().toLowerCase().contains("bigdecimal")) {
							criterion = Expression.eq(propertyName, new BigDecimal(properyValue.toString()));
						}  else if(field.getType().isEnum()){
							criterion = Expression.eq(propertyName, properyValue);
						}
						if(criterion != null){
							if (andCriterion == null) {
								andCriterion = criterion;
							} else{
								andCriterion = Expression.and(andCriterion,
										criterion);
							}
						}
					}
				}
			} catch (Exception e) {
//					e.printStackTrace();
			}
		}
		
		if (andCriterion != null) {
			criterionList.add(andCriterion);
		}
		
		return criterionList;
	}
	

	public static void copyPropertysWithoutNull(Object des, Object src) {
		Class<?> clazz = des.getClass();
		Field[] srcfields = src.getClass().getDeclaredFields();
		for (Field field : srcfields) {
			if (field.getName().equals("serialVersionUID"))
				continue;
			Field f;
			try {
				f = clazz.getDeclaredField(field.getName());
				field.setAccessible(true);
				Object obj = field.get(src);
				if (obj != null && !"".equals(obj.toString())) {
					String fieldName = field.getName();
					String targetMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
					des.getClass().getMethod(targetMethod, new Class[]{field.getType()}).invoke(des, new Object[]{obj});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static <T> Set<T> voListMapping(Set list, Class<T> voClassName) {
		Set<T> voList = new HashSet<T>();
		for(Object obj: list) {
			voList.add(mapper.map(obj, voClassName));
		}
		return voList;
	}
	
	public static <T> List<T> voListMapping(List list, Class<T> voClassName) {
		List<T> voList = new ArrayList<T>();
		for(Object obj: list) {
			voList.add(mapper.map(obj, voClassName));
		}
		return voList;
	}
	
	public static <T> T voObjectMapping(Object obj, Class<T> voClassName) {
		return mapper.map(obj, voClassName);
	}
	
	public static <T> List<T> voListMapping(List list, Class<T> voClassName, String mapId) {
		List<T> voList = new ArrayList<T>();
		for(Object obj: list) {
			voList.add(voObjectMapping(obj, voClassName, mapId));
		}
		return voList;
	}
	
	public static <T> T voObjectMapping(Object obj, Class<T> voClassName, String mapId) {
		return mapper.map(obj, voClassName, mapId);
	}
	
	/**
	 * 用英文早号隔开的多个映射声明
	 * @param mapString
	 * @return
	 */
	public static Map<String, String> buildAliasMap(String... mapString) {
		Map<String, String> map = new HashMap<String, String>();
		if (mapString != null) {
			for (String string : mapString) {
				String[] strings = string.split(":");
				if (strings.length > 1) {
					map.put(strings[0], strings[1]);
				}
			}

		}
		return map;
	}

	public static<T> Set<T> voSetMapping( Set set, Class <T> voClassName) {
		Set<T> voSet = new HashSet<T>();
		for(Object obj: set) {
			voSet.add(mapper.map(obj, voClassName));
		}
		return voSet;
	}

	/**
	 * 姚玉祺
	 * 用于Collection时转换数据
	 * @param Collection
	 * @param voClassName
	 * @return
	 */
	public static <T> Set<T> voCollectionMapping(Collection list, Class<T> voClassName) {
		Set<T> voList = new HashSet<T>();
		for(Object obj: list) {
			voList.add(mapper.map(obj, voClassName));
		}
		return voList;
	}
	
	/**
	 * 韩彦良
	 * 重载CollectionMapping，提供mapId进行更精细的转换
	 * @param list
	 * @param voClassName
	 * @param mapId
	 * @return
	 */
	public static <T> Set<T> voCollectionMapping(Collection list, Class<T> voClassName, String mapId) {
		Set<T> voList = new HashSet<T>();
		for(Object obj: list) {
			voList.add(mapper.map(obj, voClassName, mapId));
		}
		return voList;
	}
	
	/**
	 * 获取mapper;
	 */
	public static Mapper getMapper(){
		return mapper;
	}
}
