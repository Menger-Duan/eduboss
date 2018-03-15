/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel;

import com.eduboss.utils.Assert;
import org.apache.commons.beanutils.Converter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author xiangshaoxu 2016年6月6日上午12:35:14
 * @version 1.0.0
 */
public class EdubossNumberConverter implements Converter {

	public EdubossNumberConverter() {
	}

	@Override
	public Object convert(@SuppressWarnings("rawtypes") Class type, Object value) { 
		Assert.state(value != null, "src object can't be null in EdubossNumberConverter");
		if (value instanceof Date) {
			return ((Date) value).getTime();
		}
		if (value instanceof CharSequence) {
			return new BigDecimal(((CharSequence) value).toString());
		}
		if (value instanceof Number) {
			return value;
		}
		if (value instanceof Boolean) {
			if ((Boolean) value) {
				return 1;
			} else {
				return 0;
			}
		}
		if (value instanceof Calendar) {
			return ((Calendar) value).getTime().getTime();
		}
		return null;
	}
}