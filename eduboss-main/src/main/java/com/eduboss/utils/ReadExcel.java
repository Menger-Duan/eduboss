package com.eduboss.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	/**
	 * 对外提供读取excel 的方法
	 * */
	public static List<List<Object>> readExcel(File file,int sheetAt) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(file,sheetAt);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file,sheetAt);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}
	
	/**
	 * 对外提供读取excel 的方法
	 * */
	public static List<List<Object>> readExcel(File file) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(file,0);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file,0);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<List<Object>> read2003Excel(File file,int sheetAt) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = hwb.getSheetAt(sheetAt);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					linked.add("");
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
															// 字符
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
				DecimalFormat bf = new DecimalFormat("0.0");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println(i + "行" + j + " 列 is String type");
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out
							.println(i + "行" + j + " 列 is Number type ; DateFormt:" + cell.getCellStyle().getDataFormatString());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else if(cell.getCellStyle().getDataFormatString().indexOf("0.0_")!=-1){
						value = bf.format(cell.getNumericCellValue());
					} else if(cell.getCellStyle().getDataFormatString().indexOf("0.00_")!=-1){
						value = bf.format(cell.getNumericCellValue());
					}  else if((cell.getNumericCellValue()+"").indexOf("-")!=-1){
						value = bf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println(i + "行" + j + " 列 is Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println(i + "行" + j + " 列 is Blank type");
					value = "";
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					System.out.println(i + "行" + j + " 列 is CELL_TYPE_FORMULA 公式");
					value = bf.format(cell.getNumericCellValue());  
					break; 	
				default:
					System.out.println(i + "行" + j + " 列 is default type");
					value = cell.toString();
				}
//				if (value == null || "".equals(value)) {
//					continue;
//				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}

	/**
	 * 读取Office 2007 excel
	 * */
	private static List<List<Object>> read2007Excel(File file,int sheetAt) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(sheetAt);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					linked.add("");
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
															// 字符
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
				DecimalFormat bf = new DecimalFormat("0.0");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println(i + "行" + j + " 列 is String type");
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out
							.println(i + "行" + j + " 列 is Number type ; DateFormt:" + cell.getCellStyle().getDataFormatString());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else if(cell.getCellStyle().getDataFormatString().indexOf("0.0_")!=-1){
						value = bf.format(cell.getNumericCellValue());
					} else if(cell.getCellStyle().getDataFormatString().indexOf("0.00_")!=-1){
						value = bf.format(cell.getNumericCellValue());
					} else if((cell.getNumericCellValue()+"").indexOf("-")!=-1){
						value = bf.format(cell.getNumericCellValue());
					}else{
						value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println(i + "行" + j + " 列 is Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println(i + "行" + j + " 列 is Blank type");
					value = "";
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					System.out.println(i + "行" + j + " 列 is CELL_TYPE_FORMULA 公式");
					try {  
						value = String.valueOf(cell.getStringCellValue());  
					} catch (IllegalStateException e) {  
						value = bf.format(cell.getNumericCellValue());  
					}  
					break;  
				default:
					System.out.println(i + "行" + j + " 列 is default type");
					value = cell.toString();
				}
//				if (value == null || "".equals(value)) {
//					continue;
//				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}
	
	
	/**
	 * 读取Office 2007 excel
	 * */
	public static void write2007Excel(File file,List<Map<String, String>> mesList,String outPath,int sheetAt,int writeRow) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(sheetAt);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		int lastRow=0;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null || i<writeRow) {
				continue;
			}
			if(lastRow==0){
				lastRow=row.getLastCellNum()-1;
			}
		    if(i==writeRow){
		    	cell=row.createCell(lastRow+1);
		    	cell.setCellValue("上传结果");
		    	cell=row.createCell(lastRow+2);
		    	cell.setCellValue("失败原因");
		    	continue;
		    }
		   if((i-(writeRow+1))>=mesList.size()){
			   break;
		   }
		    Map<String,String> map=mesList.get(i-(writeRow+1));
		    cell=row.getCell(lastRow+1);
		    if(cell==null || StringUtil.isEmpty(cell.getStringCellValue()) || !"yes".equals(cell.getStringCellValue())){
		    	cell=row.createCell(lastRow+1);
		    	String isSave=map.get("isSave");
		    	cell.setCellValue(isSave.equals("old成功")?"成功":isSave);
		    	cell=row.createCell(lastRow+2);
		    	if("失败".equals(isSave) && map.get("errorMes")!=null){
			    	cell.setCellValue(map.get("errorMes"));
		    	}else{
		    		cell.setCellValue("");
		    	}
		    }
		}
		OutputStream out = new FileOutputStream(outPath);
		xwb.write(out);
	    out.close();
	}
	
	/**
	 * 读取Office 2007 excel
	 * */
	public static void write2003Excel(File file,List<Map<String, String>> mesList,String outPath,int sheetAt,int writeRow) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		HSSFWorkbook xwb = new HSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		HSSFSheet sheet = xwb.getSheetAt(sheetAt);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		int lastRow=0;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null || i<writeRow) {
				continue;
			}
			if(lastRow==0){
				lastRow=row.getLastCellNum()-1;
			}
		    if(i==writeRow){
		    	cell=row.createCell(Short.valueOf(""+(lastRow+1)));
		    	cell.setCellValue("上传结果");
		    	cell=row.createCell(Short.valueOf(""+(lastRow+2)));
		    	cell.setCellValue("失败原因");
		    	continue;
		    }
		    if((i-(writeRow+1))>=mesList.size()){
				   break;
			}
		    Map<String,String> map=mesList.get(i-(writeRow+1));
		    cell=row.getCell(lastRow+1);
		    if(cell==null || StringUtil.isEmpty(cell.getStringCellValue()) || !"yes".equals(cell.getStringCellValue())){
		    	cell=row.createCell(Short.valueOf(""+(lastRow+1)));
		    	String isSave=map.get("isSave");
		    	cell.setCellValue(isSave.equals("old成功")?"成功":isSave);
		    	cell=row.createCell(Short.valueOf(""+(lastRow+2)));
		    	if("失败".equals(isSave) && map.get("errorMes")!=null){
			    	cell.setCellValue(map.get("errorMes"));
		    	}else{
		    		cell.setCellValue("");
		    	}
		    }
		}
		OutputStream out = new FileOutputStream(outPath);
		xwb.write(out);
	    out.close();
	}

	public static void writeExcel(List<Map<String, String>> resList, String uploadFile,int sheetAt,int writeRow) throws IOException {
		if(".xls".equals(uploadFile.substring(uploadFile.lastIndexOf(".")).toLowerCase()))
			ReadExcel.write2003Excel(new File(uploadFile), resList, uploadFile, sheetAt, writeRow);
		else
			ReadExcel.write2007Excel(new File(uploadFile), resList, uploadFile, sheetAt, writeRow);
	}
}