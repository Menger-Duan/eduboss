/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.export.metadata;

import com.eduboss.excel.EdubossNumberConverter;
import com.eduboss.excel.export.anno.CellExportedTypeDefine;
import com.eduboss.exception.EdubossRuntimeException;
import com.eduboss.utils.Assert;
import com.google.common.base.Function;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * excel单元格导出类型，默认根据vo.field自动匹配，不需要设置， 有特殊要求时可以加
 * {@link CellExportedTypeDefine}注解设置
 * 
 * @author xiangshaoxu 2016年6月5日下午8:58:59
 * @version 1.0.0
 */
public enum CellExportedType {

	DATE {

		@Override
		public ExcelCell transferObj2Cell(Object value, Field field, int cellIdx, ExcelRow parent) {

			DateCell ret = new DateCell(cellIdx, parent);
			if (value == null) {
				ret.setContent(null);
			} else {
				Date date = (Date) value;
				ret.setContent(date);
			}
			
			if (field != null) {
				CellExportedTypeDefine ann = field.getAnnotation(CellExportedTypeDefine.class);
				if (ann != null && StringUtils.isNotBlank(ann.dateFormat())) {
					ret.setDateformat(ann.dateFormat());
				}
			}
			
			
			return ret;
		}

		@Override
		public Object transferValue(Object value, Field field) {
			if (field != null) {
				setDateConverter(field);
			}
			if (value instanceof Calendar) {
				setCalendarConverter(field);
			}
			return executeAndTransferException(value, field, new Function<Object, Object>() {
				@Override
				public Object apply(Object input) {
					return  ConvertUtils.convert(input, Date.class);
				}
			});
		}
	},
	STRING {
		@Override
		public ExcelCell transferObj2Cell(Object value, Field field, int cellIdx, ExcelRow parent) {
			if (field == null) {
				return transferObj2String(value, cellIdx, parent);
			} 
			CellExportedTypeDefine ann = field.getAnnotation(CellExportedTypeDefine.class);
			if (ann != null && ann.isUrl()) {
				return transferObj2Url(value, cellIdx, parent, ann);
			} else {
				return transferObj2String(value, cellIdx, parent);
			}
		}

		private ExcelCell transferObj2String(Object value, int cellIdx, ExcelRow parent) {
			StringCell ret = new StringCell(cellIdx, parent);
			if (value == null) {
				ret.setContent("");
				return ret;
			}

			CharSequence content = (CharSequence) value;
			ret.setContent(content.toString());
			return ret;
		}

		private ExcelCell transferObj2Url(Object value, int cellIdx, ExcelRow parent, CellExportedTypeDefine ann) {
			UrlCell ret = new UrlCell(cellIdx, parent);
			if (value == null) {
				ret.setLinkLabel("");
				ret.setLinkUrl("");
				return ret;
			}

			CharSequence content = (CharSequence) value;
			String linkLabel = ann.urlLinkLable();
			if (StringUtils.isBlank(linkLabel)) {
				linkLabel = content.toString();
			}
			ret.setLinkLabel(linkLabel);
			ret.setLinkUrl(content.toString());
			return ret;
		}

		@Override
		public Object transferValue(Object value, Field field) {
			if (value instanceof Date) {
				setDateConverter(field);
			}
			if (value instanceof Calendar) {
				setCalendarConverter(field);
			}
			return executeAndTransferException(value, field, new Function<Object, Object>() {
				@Override
				public Object apply(Object input) {
					return  ConvertUtils.convert(input, String.class);
				}
			});
		}
	},
	NUMBER {
		@Override
		public ExcelCell transferObj2Cell(Object value, Field field, int cellIdx, ExcelRow parent) {
			DoubleCell ret = new DoubleCell(cellIdx, parent);
			if (value == null) {
				ret.setContent(0d);
				return ret;
			}

			Number num = (Number) value;
			ret.setContent(num.doubleValue());
			return ret;
		}

		@Override
		public Object transferValue(Object value, Field field) {
			if (value instanceof Date) {
				setDateConverter(field);
			}
			if (value instanceof Calendar) {
				setCalendarConverter(field);
			}
			return executeAndTransferException(value, field, new Function<Object, Object>() {
				@Override
				public Object apply(Object input) {
					return  ConvertUtils.convert(input, Number.class);
				}
			});
		}
	},
	BOOLEAN {
		@Override
		public ExcelCell transferObj2Cell(Object value, Field field, int cellIdx, ExcelRow parent) {
			BooleanCell ret = new BooleanCell(cellIdx, parent);
			if (value == null) {
				ret.setContent(null);
				return ret;
			}

			ret.setContent((Boolean) value);
			return ret;
		}

		@Override
		public Object transferValue(Object value, Field field) {
			return executeAndTransferException(value, field, new Function<Object, Object>() {
				@Override
				public Object apply(Object input) {
					return  ConvertUtils.convert(input, Boolean.class);
				}
			});
		}
	},
	CALENDAR {
		@Override
		public ExcelCell transferObj2Cell(Object value, Field field, int cellIdx, ExcelRow parent) {
			CalendarCell ret = new CalendarCell(cellIdx, parent);
			if (value == null) {
				ret.setContent(null);
				return ret;
			}

			ret.setContent((Calendar) value);
			return ret;
		}

		@Override
		public Object transferValue(Object value, Field field) {
			if (field != null) {
				setCalendarConverter(field);
			}
			if (value instanceof Date) {
				setDateConverter(field);
			}
					
			return executeAndTransferException(value, field, new Function<Object, Object>() {
				@Override
				public Object apply(Object input) {
					return  ConvertUtils.convert(input, Calendar.class);
				}
			});
		}
	};

	static {
		ConvertUtils.register(new EdubossNumberConverter(), Number.class);
		DoubleConverter doubleConverter = new DoubleConverter();
		doubleConverter.setUseLocaleFormat(true);
	}

	/**
	 * 从object根据类型生成对应的excelCell, field 可能为空
	 * 
	 * @param value
	 *            : the value of this filed, CAN be null
	 * @param field
	 *            : the annotation attached , CAN be null
	 * */
	public abstract ExcelCell transferObj2Cell(Object value, Field field, int cellIdx, ExcelRow parent);

	public abstract Object transferValue(Object value, Field field);
	
	protected void setDateConverter(Field field) {
		if (field == null) {
			return;
		}
		String dateFormat = getDateFormatPattern(field);
		DateConverter dateConvert = new DateConverter();
		dateConvert.setPattern(dateFormat);
		dateConvert.setUseLocaleFormat(true);
		ConvertUtils.register(dateConvert, Date.class);
	}
	
	protected void setCalendarConverter(Field field) {
		if (field == null) {
			return;
		}
		String dateTimeFormat = getDateFormatPattern(field);//assert return not null

		CalendarConverter dateConvert = new CalendarConverter();
		dateConvert.setPattern(dateTimeFormat);
		dateConvert.setUseLocaleFormat(true);
		ConvertUtils.register(dateConvert, Calendar.class);
	}
	
	protected String getDateFormatPattern(Field field) {
		if (field == null) {
			return null;
		} else {
			CellExportedTypeDefine ann = field.getAnnotation(CellExportedTypeDefine.class);
			Assert.state(ann != null, String.format("ExcelCellExportedTypeDefine annotation of field: %s is mandatory.", field.getName()));
			String dateFormat = ann.dateFormat();
			Assert.isNotBlank(dateFormat, String.format("dateFormat attribution of field: %s is mandatory.", field.getName()));
			return dateFormat;
		}
	}
	
	protected Object executeAndTransferException(Object value, Field field, Function<Object, Object> f) {
		try {
			return f.apply(value);
		} catch (RuntimeException e) {
			throw new EdubossRuntimeException(String.format("failed to convert ,field: %s", field.getName()), e);
		}
	}

}
