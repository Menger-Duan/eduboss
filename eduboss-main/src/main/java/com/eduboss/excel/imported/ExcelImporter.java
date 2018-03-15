/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported;

import com.eduboss.excel.InvalidExcelDefineException;
import com.google.common.base.Function;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * excel导入
 * @author xiangshaoxu 2016年6月6日下午5:38:33
 * @version 1.0.0
 */
public interface ExcelImporter<F extends GenericExcelImportedEo, T> {

	PoiExcelImporter<F, T> loadExcel(InputStream is) throws IOException;

	PoiExcelImporter<F, T> setMetaData(Class<F> voClazz);

	List<F> getEos();

	PoiExcelImporter<F, T> parse() throws InvalidExcelDefineException;

	PoiExcelImporter<F, T> feebback2Excel();
	
	PoiExcelImporter<F, T> moveToTargetRow(int row);

	PoiExcelImporter<F, T> export2Stream(OutputStream out) throws IOException ;

	PoiExcelImporter<F, T> closeExcelStream();

	PoiExcelImporter<F, T> clearExcelWorkBook();

	PoiExcelImporter<F, T> addTransferFunction(Function<F, T> f);

	List<T> getDtos();

	PoiExcelImporter<F, T> executeInFeedback(ExecuteFunction<T> fun);


}
