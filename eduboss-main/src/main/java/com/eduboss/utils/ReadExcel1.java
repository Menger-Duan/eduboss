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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eduboss.domainVo.DataImportResult;

public class ReadExcel1 {
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
		int beginIndex=sheet.getRow(0).getFirstCellNum();
		int endIndex=sheet.getRow(0).getLastCellNum();
		for (int i = sheet.getFirstRowNum()+2; i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j =beginIndex; j <endIndex; j++) {
				cell = row.getCell(j);
				if (cell == null) {
					value="";
				}else{
					DecimalFormat df = new DecimalFormat("0");// 格式化 number String// 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
					DecimalFormat bf = new DecimalFormat("0.0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
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
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						value = bf.format(cell.getNumericCellValue());  
						break; 	
					default:
						value = cell.toString();
					}
				}
				String tempValue=value.toString();
				if(tempValue.contains(".")){
					value=tempValue.substring(0, tempValue.indexOf("."));
				}
				linked.add(value.toString());
			}
			list.add(linked);
		}
//		List<List<Object>> list1 = new LinkedList<List<Object>>();
//		for(int i=0;i<list.size();i++){
//			List<Object> lo=list.get(i);
//			if(lo.get(1)!="" && lo.get(2)!=""){
//				list1.add(lo);	
//			}
//		}
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
		int beginIndex=sheet.getRow(0).getFirstCellNum();
		int endIndex=sheet.getRow(0).getLastCellNum();		
		for (int i = sheet.getFirstRowNum()+2; i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			int count=0;
			for (int j =beginIndex; j <endIndex; j++) {
				cell = row.getCell(j);
				if (cell == null) {
					value="";
					count++;
				}else{
					DecimalFormat df = new DecimalFormat("0");// 格式化 number String
																// 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
					DecimalFormat bf = new DecimalFormat("0.0");// 格式化数字
					DecimalFormat cf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = cf.format(cell.getNumericCellValue());
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
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						count++;
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						try {  
							value = String.valueOf(cell.getStringCellValue());  
						} catch (IllegalStateException e) {  
							value = bf.format(cell.getNumericCellValue());  
						}  
						break;  
					default:
						value = cell.toString();
					}
				}
				linked.add(value.toString());
			}
			if(count==endIndex){
				
			}else{
				list.add(linked);
			}
			
		}
//		List<List<Object>> list1 = new LinkedList<List<Object>>();
//		for(int i=0;i<list.size();i++){
//			List<Object> lo=list.get(i);
//			if(lo.get(1)!="" && lo.get(2)!=""){
//				list1.add(lo);	
//			}
//		}
		return list;
	}
	
	
	/**
	 * 写Office 2007 excel
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
	 * 写Office 2007 excel
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

	/**
	 * 写Office 2007 excel
	 * */
	public static void write2007Excel1(File file,DataImportResult dir,String outPath,int sheetAt) throws IOException {
		String result=dir.getResult();
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(sheetAt);
		XSSFRow row = null;
		XSSFCell cell = null;
		int rowNum=sheet.getPhysicalNumberOfRows();
		if(dir.getSurplus()!=0){
			//标注超出资源池容量的客户资源
			int max=dir.getSurplus();
			for(int i=1;i < rowNum; i++){
				row=sheet.getRow(i);
				cell=row.createCell(17);
				if(max>i){
					if(dir.getLi().contains(i)) 
						cell.setCellValue("参数有误导入失败");
					else 
						cell.setCellValue("导入成功");
				}
				else{
					cell.setCellValue("资源池容量不足导入失败");
				}
			}
		}
		row=sheet.createRow(sheet.getPhysicalNumberOfRows()+1);
		cell=row.createCell(0);
		int rows=result.length()/200;
		sheet.addMergedRegion(new CellRangeAddress(sheet.getPhysicalNumberOfRows(), sheet.getPhysicalNumberOfRows()+rows, 0,15));
		//row.setRowNum(1);
		cell.setCellValue(result);
		OutputStream out = new FileOutputStream(outPath);
		xwb.write(out);
	    out.close();
	}
	
	/**
	 * 写Office 2003 excel
	 * */
	/**
	public static void write2003Excel1(File file,DataImportResult dir,String outPath,int sheetAt) throws IOException {
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		String result=dir.getResult();
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(sheetAt);
		XSSFRow row = null;
		XSSFCell cell = null;
		int rowNum=sheet.getPhysicalNumberOfRows();
		if(dir.getSurplus()!=0){
			//标注超出资源池容量的客户资源
			int max=dir.getSurplus();
			for(int i=1;i < rowNum; i++){
				row=sheet.getRow(i);
				cell=row.createCell(17);
				if(max>i){
					if(dir.getLi().contains(i)) 
						cell.setCellValue("参数有误导入失败");
					else 
						cell.setCellValue("导入成功");
				}
				else{
					cell.setCellValue("资源池容量不足导入失败");
				}
			}
		}
		row=sheet.getRow(sheet.getPhysicalNumberOfRows()+1);
		cell=row.createCell(0);
		int rows=result.length()/200;
		sheet.addMergedRegion(new CellRangeAddress(sheet.getPhysicalNumberOfRows(), sheet.getPhysicalNumberOfRows()+rows, 0,15));
		cell.setCellValue(result);
		OutputStream out = new FileOutputStream(outPath);
		xwb.write(out);
	    out.close();
	}
	*/
	
	public static void write2003Excel1(File file,DataImportResult dir,String outPath,int sheetAt) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		HSSFWorkbook xwb = new HSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		HSSFSheet sheet = xwb.getSheetAt(sheetAt);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		int rowNum=sheet.getPhysicalNumberOfRows();
		String result=dir.getResult();
		// 读取第一章表格内容
		if(dir.getSurplus()!=0){
			//标注超出资源池容量的客户资源
			int max=dir.getSurplus();
			for(int i=1;i < rowNum; i++){
				row=sheet.getRow(i);
				cell=row.createCell(17);
				if(max>i){
					if(dir.getLi().contains(i)) 
						cell.setCellValue("参数有误导入失败");
					else 
						cell.setCellValue("导入成功");
				}
				else{
					cell.setCellValue("资源池容量不足导入失败");
				}
			}
		}
		row=sheet.getRow(sheet.getPhysicalNumberOfRows()+1);
		cell=row.createCell(0);
		int rows=result.length()/200;
		sheet.addMergedRegion(new CellRangeAddress(sheet.getPhysicalNumberOfRows(), sheet.getPhysicalNumberOfRows()+rows, 0,15));
		cell.setCellValue(result);
		OutputStream out = new FileOutputStream(outPath);
		xwb.write(out);
	    out.close();
	}
	
	public static void writeExcel1(File file,DataImportResult dir,String outPath,int sheetAt) throws IOException {
		if(".xls".equals(outPath.substring(outPath.lastIndexOf(".")).toLowerCase()))
			ReadExcel1.write2003Excel1(new File(outPath), dir, outPath, sheetAt);
		else
			ReadExcel1.write2007Excel1(new File(outPath), dir, outPath, sheetAt);
	}
	
	public static void writeExcel(List<Map<String, String>> resList, String uploadFile,int sheetAt,int writeRow) throws IOException {
		if(".xls".equals(uploadFile.substring(uploadFile.lastIndexOf(".")).toLowerCase()))
			ReadExcel1.write2003Excel(new File(uploadFile), resList, uploadFile, sheetAt, writeRow);
		else
			ReadExcel1.write2007Excel(new File(uploadFile), resList, uploadFile, sheetAt, writeRow);
	}
	
	public static void main(String[] args){
		String str="123";
		if(str.contains(".")){
			String str1=str.substring(0,str.indexOf("."));
			System.out.println("str1= "+str1);
		}else{
			System.out.println("ccccccccc");
		}
	}

	public static List<List<Object>> readExcelByStartEnd(File file, int start, int end)  throws IOException{
		// TODO Auto-generated method stub
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(file,0,start,end);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file,0,start,end);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}
	private static List<List<Object>> read2003Excel(File file,int sheetAt,int start,int end) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = hwb.getSheetAt(sheetAt);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		int beginIndex=sheet.getRow(0).getFirstCellNum();
		int endIndex=sheet.getRow(0).getLastCellNum();
		for (int i = start; i <= end; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j =beginIndex; j <endIndex; j++) {
				cell = row.getCell(j);
				if (cell == null) {
					value="";
				}else{
					DecimalFormat df = new DecimalFormat("0");// 格式化 number String// 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
					DecimalFormat bf = new DecimalFormat("0.0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
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
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						value = bf.format(cell.getNumericCellValue());  
						break; 	
					default:
						value = cell.toString();
					}
				}
				String tempValue=value.toString();
				if(tempValue.contains(".")){
					value=tempValue.substring(0, tempValue.indexOf("."));
				}
				linked.add(value.toString());
			}
			list.add(linked);
		}
//		List<List<Object>> list1 = new LinkedList<List<Object>>();
//		for(int i=0;i<list.size();i++){
//			List<Object> lo=list.get(i);
//			if(lo.get(1)!="" && lo.get(2)!=""){
//				list1.add(lo);	
//			}
//		}
		return list;
	}
	private static List<List<Object>> read2007Excel(File file,int sheetAt,int start,int end) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(sheetAt);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		int beginIndex=sheet.getRow(0).getFirstCellNum();
		int endIndex=sheet.getRow(0).getLastCellNum();		
		for (int i = start; i <= end; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			int count=0;
			for (int j =beginIndex; j <endIndex; j++) {
				cell = row.getCell(j);
				if (cell == null) {
					value="";
					count++;
				}else{
					DecimalFormat df = new DecimalFormat("0");// 格式化 number String
																// 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
					DecimalFormat bf = new DecimalFormat("0.0");// 格式化数字
					DecimalFormat cf = new DecimalFormat("0");// 格式化数字
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = cf.format(cell.getNumericCellValue());
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
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						count++;
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						try {  
							value = String.valueOf(cell.getStringCellValue());  
						} catch (IllegalStateException e) {  
							value = bf.format(cell.getNumericCellValue());  
						}  
						break;  
					default:
						value = cell.toString();
					}
				}
				linked.add(value.toString());
			}
			if(count==endIndex){
				
			}else{
				list.add(linked);
			}
			
		}
//		List<List<Object>> list1 = new LinkedList<List<Object>>();
//		for(int i=0;i<list.size();i++){
//			List<Object> lo=list.get(i);
//			if(lo.get(1)!="" && lo.get(2)!=""){
//				list1.add(lo);	
//			}
//		}
		return list;
	}
	/**
	 * 删除数据
	 * @Title: clearData 
	 * @Description: TODO 
	 * @param file
	 * @throws IOException 
	 */
	public static void clearData(File file,String outPath,int start ,int end) throws IOException {
		// TODO Auto-generated method stub
		if (".xls".equals(outPath.substring(outPath.lastIndexOf(".")).toLowerCase())) {
			clearData2003Excel(new File(outPath),outPath,0,start,end);
		} else if (".xlsx".equals(outPath.substring(outPath.lastIndexOf(".")).toLowerCase())) {
			clearData2007Excel(new File(outPath),outPath,0,start,end);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	private static void clearData2007Excel(File file, String outpath, int sheetAt,int start ,int end) throws IOException{
		// TODO Auto-generated method stub
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file.getPath()));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(sheetAt);
		XSSFRow row = null;
		if(end == 0) {
			end = sheet.getLastRowNum() +1;
		}
		for (int i = start; i <= end; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			sheet.removeRow(row);			
		}
		OutputStream out = new FileOutputStream(outpath);
		xwb.write(out);
	    out.close();
	}

	private static void clearData2003Excel(File file, String outpath, int sheetAt,int start ,int end) throws IOException{
		HSSFWorkbook xwb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = xwb.getSheetAt(sheetAt);
		HSSFRow row = null;
		if(end == 0) {
			end = sheet.getLastRowNum() +1;
		}
		for (int i = start; i <= end; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			sheet.removeRow(row);			
		}
		OutputStream out = new FileOutputStream(outpath);
		xwb.write(out);
	    out.close();
	}

	public static void writeFalseExcel(File file, String outPath, List<List<Object>> list) throws IOException {
		// TODO Auto-generated method stub
		if (".xls".equals(outPath.substring(outPath.lastIndexOf(".")).toLowerCase())) {
			writeFalse2003Excel(new File(outPath),outPath,0,list);
		} else if (".xlsx".equals(outPath.substring(outPath.lastIndexOf(".")).toLowerCase())) {
			writeFalse2007Excel(new File(outPath),outPath,0,list);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	private static void writeFalse2007Excel(File file, String outPath, int sheetAt, List<List<Object>> list)throws IOException {
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(sheetAt);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		int number = sheet.getLastRowNum();
		if(list!=null&&list.size()>0) {
			int size = list.size();
			List<Object> obj = null;
			for(int i = 0; i < size; i++) {
				obj = list.get(i);
				if(obj!=null&&obj.get(obj.size()-1)!=null
						&& !"SUCCESS".equals(obj.get(obj.size()-1).toString())) {
					++number;
					row = sheet.createRow(number);
					for(int index = 0 ; index < obj.size() ; index++ ) {
						cell = row.createCell(index);
						if(index == 0) {
							cell.setCellValue(number-1);
						}else {
							cell.setCellValue(obj.get(index)!=null?obj.get(index).toString():"");
						}
					}
					
				}
			}
		}
		
		OutputStream out = new FileOutputStream(outPath);
		xwb.write(out);
	    out.close();

	}

	private static void writeFalse2003Excel(File file, String outPath, int sheetAt, List<List<Object>> list)throws IOException {
		// TODO Auto-generated method stub
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		HSSFWorkbook xwb = new HSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		HSSFSheet sheet = xwb.getSheetAt(sheetAt);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		int number = sheet.getLastRowNum();
		int aa = sheet.getPhysicalNumberOfRows();
		if(list!=null&&list.size()>0) {
			int size = list.size();
			List<Object> obj = null;
			for(int i = 0; i < size; i++) {
				obj = list.get(i);
				if(obj!=null&&obj.get(obj.size()-1)!=null
						&& !"SUCCESS".equals(obj.get(obj.size()-1).toString())) {
					++number;
					row = sheet.createRow(number);
					for(int index = 0 ; index < obj.size() ; index++ ) {
						cell = row.createCell(index);
						if(index == 0) {
							cell.setCellValue(number-1);
						}else {
							cell.setCellValue(obj.get(index)!=null?obj.get(index).toString():"");
						}
					}
					
				}
			}
		}
		OutputStream out = new FileOutputStream(outPath);
		xwb.write(out);
	    out.close();
	}
	
}