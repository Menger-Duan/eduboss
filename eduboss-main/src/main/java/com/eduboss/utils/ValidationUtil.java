package com.eduboss.utils;

import java.math.BigDecimal;

import org.jsoup.helper.StringUtil;

import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;

public class ValidationUtil {

	/**
	 * 检查字符串是否为空，如果为空，throw出exception
	 * @param param
	 * @param exception
	 */
	public static void checkNull(String param) {
		if (StringUtil.isBlank(param)) {
			throw new ApplicationException(ErrorCode.PARAMETER_EMPTY);
		}
	}
	
	/**
	 * 检查对象是否为空，如果为空，throw出exception
	 * @param param
	 * @param exception
	 */
	public static void checkObjectNullWithException(Object param, ApplicationException exception) {
		if (param == null) {
			throw exception;
		}
	}
	
	public static void checkNumeric(String param) {
		checkNull(param);
		try {
			new BigDecimal(param);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
	}
	
}
