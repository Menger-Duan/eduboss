/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported;

import com.eduboss.excel.InvalidExcelCellDataException;
import com.eduboss.excel.InvalidExcelDefineException;
import com.eduboss.excel.anno.ExcelIgnore;
import com.eduboss.excel.export.PoiExcelExporter;
import com.eduboss.excel.export.StyleGenerator;
import com.eduboss.excel.imported.anno.CellImportedTypeDefine;
import com.eduboss.excel.imported.anno.CellNotMandatory;
import com.eduboss.excel.imported.anno.ImportedSheetDefine;
import com.eduboss.excel.imported.metadata.CellImportedType;
import com.eduboss.excel.imported.metadata.ImportedEoUtil;
import com.eduboss.exception.EdubossRuntimeException;
import com.eduboss.utils.Assert;
import com.eduboss.utils.EdubossBeanUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * excel文件导入
 * 从Eo转为需要的dto
 * F: from T: to
 * 使用方法：
 * 1. 不需要反馈excel:
 * ExcelImporter importer = new PoiExcelImporter<ImportedExcelEo,SomeDto>(false);
 * importer.setMetaData(eo).loadExcel(is).parse().getEos();
 * importer.closeExcelStream()
 * 2. 需要反馈excel:
 * ExcelImporter<TestExcelImportedEo1 ,SomeDto> importer = new PoiExcelImporter<TestExcelImportedEo1,SomeDto>(true);
 * eos = importer.setMetaData(TestExcelImportedEo1.class).loadExcel(is).parse().getEos();
 * importer.addTransferFunction(transferFunction).executeInFeedback(dbExecuteFunction)
 *          .feebback2Excel().export2Stream(new FileOutputStream(file)).clearExcelWorkBook().closeExcelStream();
 * 参考：ContractServiceImpl。importContractFrom2007ExcelTest  以及相关junit test         
 * @author xiangshaoxu 2016年6月6日上午10:26:10
 * @version 1.0.0
 */
public class PoiExcelImporter<F extends GenericExcelImportedEo, T> implements ExcelImporter<F, T> {

	private static Logger logger = LoggerFactory.getLogger(PoiExcelExporter.class);
	
	/**要解析的eo class*/
	Class<F> eoClazz;
	
	/**从excel读取数据到eo list, 代码中需要保证eos 必须和dtos数量、顺序上一致*/
	List<F> eos = new ArrayList<>();
	
	/**把eo转换为需要的dto list, 代码中需要保证eos 必须和dtos数量、顺序上一致*/
	List<T> dtos = new ArrayList<>();
	
	InputStream is;
	boolean parsed = false;
	
	/**读取解析取得数据并处理后，是否需要回写excel表格，否则在parse后直接关闭流*/
	boolean needFeedbackExcel = false;

	private XSSFWorkbook xwb;
	
	/**导入数据的sheet*/
	private XSSFSheet sheet;
	
	/**解析反馈信息写入单元格num*/
	private int feedbackCellNum = 0;
	
	private CellParseCtx cellCtx = new CellParseCtx();
	
	public PoiExcelImporter(boolean needFeedbackExcel) {
		this.needFeedbackExcel = needFeedbackExcel;
	}

	
	@Override
	public PoiExcelImporter<F, T> loadExcel(InputStream is) throws IOException {
		this.is = is;
		xwb = new XSSFWorkbook(is);
		return this;
	}
	
	@Override
	public PoiExcelImporter<F, T> setMetaData(Class<F> eoClazz) {
		this.eoClazz = eoClazz;
		return this;
	}

	/**取得解析结果，取得结果后便关闭文件流, 结果处理完毕后最好手动清空*/
	@Override
	public List<F> getEos() {
		Assert.state(parsed, "you need to parse the Excel First.");
		return eos;
	}
	
	@Override
	public List<T> getDtos() {
		Assert.state(parsed, "you need to parse the Excel First.");
		return dtos;
	}

	@Override
	public PoiExcelImporter<F, T> parse() throws InvalidExcelDefineException {
		Assert.state(xwb != null, "you need to load Excel First.");
		Assert.state(eoClazz != null, "you need to set metaData First.");
		
		try {
			if (sheet == null) {
				sheet = parseSheet();
			}
			
			Field[] fields = getNotIgnoreFields(eoClazz);
			
			feedbackCellNum = fields.length;
			
			Map<String, Method> field2SetterMap  = getField2SetterMap();
			
			parseRows(sheet, fields, field2SetterMap);
		} catch (EdubossRuntimeException e) {
			throw new InvalidExcelDefineException(e, e.getMessage());
		}
		
		if (!needFeedbackExcel) {
			xwb = null;
			closeExcelStream();
		}
		parsed = true;
		return this;
	}

	/**
	 * 根据导出解析结果反写导入情况到excel
	 * */
	@Override
	public PoiExcelImporter<F, T> feebback2Excel() {
		
		Assert.state(sheet != null, "you need to call parsed first, sheet can't be null");
		Assert.state(parsed, "you need to call parsed first");
		feedbackHeaderRow();
		feedbackRows();
		return this;
	}
	
	@Override
	public PoiExcelImporter<F, T> moveToTargetRow(int row) {
		if (sheet == null) {
			sheet = parseSheet();
		}
		sheet.setRowBreak(row);
		return this;
	}
	
	
	@Override
	public PoiExcelImporter<F, T> export2Stream(OutputStream out) throws IOException {
		Assert.state(parsed, "you need to parse first.");
		this.xwb.write(out);
		xwb = null;
		return this;
	}


	private void feedbackRows() {
		StyleGenerator styleGenerator = new StyleGenerator(xwb);
		XSSFCellStyle style = styleGenerator.defaultStringCellStyle();
		for (F t : eos) {
			XSSFRow row = sheet.getRow(t.getExcelRowNum());
			XSSFCell cell = row.createCell(feedbackCellNum);
			
			String msg = "";
			if (t.getValidated()) {
				msg = "成功";
			} else {
				msg = "导入失败：" + t.getValidateMsg();
			}
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellStyle(style);
			cell.setCellValue(msg);
		}
	}


	private void feedbackHeaderRow() {
		XSSFRow headerRow = sheet.getRow(0);
		StyleGenerator sytleGenerator = new StyleGenerator(xwb);
		XSSFCellStyle headerStyle = sytleGenerator.defaultHeaderStyle();
		sheet.setColumnWidth(feedbackCellNum, 80*256);
		XSSFCell cell = headerRow.createCell(feedbackCellNum);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("导入结果");
	}

	private Map<String, Method> getField2SetterMap() {
		Map<String, Method> field2SetterMap = null;
		try {
			field2SetterMap = EdubossBeanUtils.getFieldName2SetterMap(eoClazz);
		} catch (IntrospectionException e) {
			Assert.state(false, String.format("can't introspect bean: %s", eoClazz.getSimpleName()), e);
		}
		return field2SetterMap;
	}


	private void parseRows(XSSFSheet sheet, Field[] fields, Map<String, Method> field2SetterMap) {
		int rowBreak = (sheet.getRowBreaks() != null && sheet.getRowBreaks().length > 0) ? sheet.getRowBreaks()[0] : sheet.getFirstRowNum();
		for (int rownum = rowBreak + 1; rownum <= sheet.getLastRowNum(); rownum++) {//注意删除空行
			XSSFRow row = sheet.getRow(rownum);
			if (row == null) {
				return;
			}
			
			F eo = initEo();
			eo.setExcelRowNum(rownum);
			
			parseCells(fields, field2SetterMap, rownum, row, eo);
			eos.add(eo);
		}
	}

	private void parseCells(Field[] fields, Map<String, Method> field2SetterMap, int rownum, XSSFRow row, F eo) {
		for (int cellnum = 0; cellnum < fields.length; cellnum++) {
			Field field = fields[cellnum];
			Method setter = field2SetterMap.get(field.getName());
			Assert.state(setter != null, String.format( "can't find setter of field: %s.", field.getName()));
			XSSFCell cell = row.getCell(cellnum);
			Object value = getCellValue(cell);
			
			//try to catch cell parsing exception 
			try {
				value = transferValueIfNecessary(field, value);
				setValue2EoByType(eo, field, setter, value);//并不一定所有的field都有上面的转换，最终还是根据value类型进行转换器选择
			} catch (InvalidExcelCellDataException e) {
				eo.setValidated(false);
				eo.appendValidateMsg(e.getValidateMsg());
				logger.info(e.getMessage(), e);
			}
		}
	}


	private void setValue2EoByType(F eo, Field field, Method setter, Object value) throws InvalidExcelCellDataException {
		if (value instanceof CharSequence) {
			CellImportedType.STRING.setValue2Eo(value, field, eo, setter, cellCtx);
			return;
		}
		if (value instanceof Boolean) {
			CellImportedType.BOOLEAN.setValue2Eo(value, field, eo, setter, cellCtx);
			return;
		}
		if (value instanceof Number) {
			CellImportedType.NUMBER.setValue2Eo(value, field, eo, setter, cellCtx);
			return;
		}
		if (value instanceof Date) {
			CellImportedType.DATE.setValue2Eo(value, field, eo, setter, cellCtx);
			return;
		}
		if (value instanceof Calendar) {
			CellImportedType.CALENDAR.setValue2Eo(value, field, eo, setter, cellCtx);
			return;
		}
		String fieldName = ImportedEoUtil.getEoFieldName(field);
		if (value == null) {
			CellNotMandatory ann = field.getAnnotation(CellNotMandatory.class);
			if (ann != null) {//can be null
				return;
			} else {
				String validateMsg = String.format("\"%s\"数据错误或者不能为空", fieldName);
				String errorMsg = String.format(
						"cell content can't be null, field: %s, value: %s", fieldName, value);
				throw new InvalidExcelCellDataException(errorMsg, validateMsg);
			}
		}
		String validateMsg = String.format("\"%s\"数据错误或者不能为空", fieldName);
		String errorMsg = String.format(
				"unspported cell type, field: %s, value: %s", fieldName, value);
		throw new InvalidExcelCellDataException(errorMsg, validateMsg);
	}


	private F initEo(){
		F eo = null;
		try {
			eo = eoClazz.newInstance();
		} catch ( InstantiationException | IllegalAccessException e) {
			Assert.state(false, String.format("can't instance class: %s, is there a default constractor?", eoClazz.getSimpleName()), e);
		}
		return eo;
	}

	/**首先根据 {@link CellImportedTypeDefine} 定义，把传入的cell内容进行转换，例如string转为Int*/
	private Object transferValueIfNecessary(Field field, Object cellValue) throws InvalidExcelCellDataException{
		CellImportedTypeDefine ann = field.getAnnotation(CellImportedTypeDefine.class);
		if (ann != null) {
			CellImportedType type = ann.toCellType();
			cellValue = type.transferValue(cellValue, field);
		}
		return cellValue;
	}

	/**
	 * @param field : the annotation attached , assert NOT be null 
	 * */
	private CellImportedType getExportedType(Field field , CellImportedType defaultValue) {
		CellImportedType type;
		CellImportedTypeDefine typeAnn = field.getAnnotation(CellImportedTypeDefine.class);
		if (typeAnn != null) {
			type = typeAnn.toCellType();
		} else {
			type = defaultValue;
		}
		return type;
	}

	/**
	 * 根据excel cell类型返回value
	 * */
	private Object getCellValue(XSSFCell cell) {
		Object ret = null;
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_BLANK:
			ret = null;
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			ret = cell.getBooleanCellValue();
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			try {
				ret = cell.getNumericCellValue();
			} catch (IllegalStateException e) {
				Assert.state(false, String.format("we only support numberic formula, error cell: %s content.", cell), e);
			}
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				ret = cell.getDateCellValue();
			} else {
				ret = cell.getNumericCellValue();
			}
			break;
		case XSSFCell.CELL_TYPE_STRING:
			ret = cell.getStringCellValue();
			break;
		default:
			Assert.state(false, String.format("unsupport cell type of cell: %s", cell) );
			break;
		}
		return ret;
	}


	private XSSFSheet parseSheet() {
		XSSFSheet retSheet = null;
		ImportedSheetDefine sheetAnn = eoClazz.getAnnotation(ImportedSheetDefine.class);
		if (sheetAnn != null) {
			retSheet = getSheetByAnn(sheetAnn);
		} else {
			retSheet = xwb.getSheetAt(0);
		}
		Assert.state(retSheet != null, "there is no suitable sheet in your excel file, please check sheetname.");
		return retSheet;
	}

	private Field[] getNotIgnoreFields(Class<F> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			ExcelIgnore ignoreAnn = field.getAnnotation(ExcelIgnore.class);
			if (ignoreAnn != null) {
				fields = ArrayUtils.removeElement(fields, field);
			}
		}
		return fields;
	}

	private XSSFSheet getSheetByAnn(ImportedSheetDefine sheetAnn) {
		String sheetName  = sheetAnn.sheetName();
		Assert.isNotBlank(sheetName, "sheetname can't be blank");
		int count = xwb.getNumberOfSheets();
		for(int i = 0; i < count; i++) {
			XSSFSheet sheet = xwb.getSheetAt(i);
			if (sheetName.equals(sheet.getSheetName())) {
				return sheet;
			}
		}
		Assert.state(false, String.format("there is no sheet named: %s founded", sheetName));
		return null;
	}

	@Override
	public PoiExcelImporter<F, T> closeExcelStream() {
		try {
			is.close();
		} catch (IOException e) {
			logger.error("close excel stream error", e);
		}
		return this;
	}
	
	/**当你确定不需要excel workbook时候可以清除，请小心操作*/
	@Override
	public PoiExcelImporter<F, T> clearExcelWorkBook() {
		xwb = null;
		return this;
	}
	
	@Override
	public PoiExcelImporter<F, T> addTransferFunction(Function<F, T> f) {
		this.dtos.addAll(Lists.transform(eos, f));
		return this;
	} 
	
	@Override
	public PoiExcelImporter<F, T> executeInFeedback(ExecuteFunction<T> fun) {
		for (int i = 0; i < dtos.size(); i++) {
			T t = dtos.get(i);
			F f = eos.get(i);
			if (t == null || f.getValidated() == false) {
				continue;
			}
			
			try {
				fun.execute(t);
			} catch (Exception e) {
				f.setValidated(false);
				f.appendValidateMsg(e.getMessage());
			}
		}
		return this;
	} 
}
