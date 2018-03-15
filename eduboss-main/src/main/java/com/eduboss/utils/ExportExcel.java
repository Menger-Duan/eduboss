package com.eduboss.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import com.eduboss.common.CourseStatus;
import com.eduboss.common.EvidenceAuditStatus;

/**
 * 利用开源组件POI动态导出EXCEL文档
 * <p/>
 * 转载时请保留以下信息，注明出处！
 *
 * @param <T> 应用泛型，代表任意一个符合javabean风格的类
 *            <p/>
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            <p/>
 *            byte[]表jpg格式的图片数据 
 * @author leno
 * @version v1.0
 */

public class ExportExcel<T> {
	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("Sheet1", null, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset,
							OutputStream out) {
		exportExcel("Sheet1", headers, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset,
							OutputStream out, String pattern) {
		exportExcel("Sheet1", headers, dataset, out, pattern);
	}
	
	public void exportExcel(String[] headers, Collection<T> dataset,
			OutputStream out, int status) {
		exportExcel("Sheet1", headers, dataset, out, "yyyy-MM-dd", status);
		}



	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 *
	 * @param title   表格标题名
	 * @param headers 表格属性列名数组
	 * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *                <p/>
	 *                javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */

	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers,
							Collection<T> dataset, OutputStream out, String pattern) {
		
		childExportExcel(title, headers, dataset, out, pattern, 0);
		
	}
	
	@SuppressWarnings("unchecked")
	public void exportExcelFromMap(String[] headers,List<Map> dataset, OutputStream out,String[] heardersId) {
		childExportExcelFromMap("Sheet1", headers, dataset, out, "yyyy-MM-dd", 0,heardersId);
		
	}
	
	@SuppressWarnings("unchecked")
	public void exportExcelFromMapMerge(String[] headers,List<Map> dataset, OutputStream out,String[] heardersId,String onlySign,List<String> repeatFields) {
		childExportExcelFromMapMerge("Sheet1", headers, dataset, out, heardersId, onlySign, repeatFields);
		
	}

	public void childExportExcelFromMapMerge(String sheetName, String[] headers,
			List<Map> dataset, OutputStream out, String[] heardersId, String onlySign,List<String> repeatFields){ 
				// 声明一个工作薄
				HSSFWorkbook workbook = new HSSFWorkbook();
				// 生成一个表格
				HSSFSheet sheet = workbook.createSheet(sheetName);
				// 设置表格默认列宽度为15个字节
				sheet.setDefaultColumnWidth(15);
				// 生成一个样式
				HSSFCellStyle style = workbook.createCellStyle();
				// 设置这些样式
				style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
				style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// 生成一个字体
				HSSFFont font = workbook.createFont();
				font.setColor(HSSFColor.VIOLET.index);
				font.setFontHeightInPoints((short) 12);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				// 把字体应用到当前的样式
				style.setFont(font);
				// 生成并设置另一个样式
				HSSFCellStyle style2 = workbook.createCellStyle();
				style2.setFillForegroundColor(HSSFColor.WHITE.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				// 生成另一个字体
				HSSFFont font2 = workbook.createFont();
				//font2.setColor(HSSFColor.BLACK.index);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				// 把字体应用到当前的样式
				style2.setFont(font2);
				// 声明一个画图的顶级管理器
				HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				// 定义注释的大小和位置,详见文档
				HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
						0, 0, 0, (short) 4, 2, (short) 6, 5));
				// 设置注释内容
				comment.setString(new HSSFRichTextString("BOSS校长系统"));
				// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
				comment.setAuthor("leno");
				// 产生表格标题行
				int rowIndex = 0;
				HSSFRow row = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers[i]);
					cell.setCellValue(text);
				}
				// 遍历集合数据，产生数据行
				
				Object signValue = "";
				int count=0;
				int startIndex = 1;//合并起始行
	            int endIndex = 1;//合并结束行
				for (Iterator iterator = dataset.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();
					if(!signValue.equals(map.get(onlySign))) {
						count++;
					}
					rowIndex++;
					row = sheet.createRow(rowIndex);
					for(int i = 0; i < headers.length; i++){
						//序号
						if(i==0) {
		            		 HSSFCell cell = row.createCell(i);
		            		 cell.setCellStyle(style2);
		            		 cell.setCellValue(count);
		            		 continue;
		            	 }
						HSSFCell cell = row.createCell(i);
						cell.setCellStyle(style2);
						Object value = map.get(heardersId[i]);
						if(value == null){
	 						cell.setCellValue("");
						} else if (value instanceof Number) {
							// 是数字当作double处理
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							if (value instanceof Double) {
								cell.setCellValue((Double)value);
							} else {
								cell.setCellValue(Double.parseDouble(value.toString()));
							}
						} else {
							cell.setCellValue(value.toString());
						}
					}
					
					if(!signValue.equals(map.get(onlySign))) {
						signValue = map.get(onlySign);
						endIndex = rowIndex - 1;
						if(endIndex > startIndex) {
							for(int i=0;i< heardersId.length;i++) {
				            	 if(!repeatFields.contains(heardersId[i])) {
				            		 CellRangeAddress range = new CellRangeAddress(startIndex, endIndex, i, i);
				            		 sheet.addMergedRegion(range);
				            		 setRegionStyle(sheet,range,style2);
				            	 }
				             }
						}
						startIndex = rowIndex;
					}
					
				}
				endIndex = rowIndex;
				if(endIndex > startIndex) {
					for(int i=0;i< heardersId.length;i++) {
		            	 if(!repeatFields.contains(heardersId[i])) {
		            		 CellRangeAddress range = new CellRangeAddress(startIndex, endIndex, i, i);
		            		 sheet.addMergedRegion(range);
		            		 setRegionStyle(sheet,range,style2);
		            	 }
		             }
				}
				try {
					workbook.write(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	public void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) { 
  	  for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
  		  HSSFRow row = HSSFCellUtil.getRow(i, sheet);
  		  for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
  			  HSSFCell cell = HSSFCellUtil.getCell(row, (short) j);
  			  cell.setCellStyle(cs);
  		  }
  	  }
  }
	public void childExportExcel(String title, String[] headers,
			Collection<T> dataset, OutputStream out, String pattern, int status){
				// 声明一个工作薄
				HSSFWorkbook workbook = new HSSFWorkbook();
				// 生成一个表格
				HSSFSheet sheet = workbook.createSheet(title);
				// 设置表格默认列宽度为15个字节
				sheet.setDefaultColumnWidth(15);
				// 生成一个样式
				HSSFCellStyle style = workbook.createCellStyle();
				// 设置这些样式
				style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
				style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// 生成一个字体
				HSSFFont font = workbook.createFont();
				font.setColor(HSSFColor.VIOLET.index);
				font.setFontHeightInPoints((short) 12);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				// 把字体应用到当前的样式
				style.setFont(font);
				// 生成并设置另一个样式
				HSSFCellStyle style2 = workbook.createCellStyle();
				style2.setFillForegroundColor(HSSFColor.WHITE.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				// 生成另一个字体
				HSSFFont font2 = workbook.createFont();
				//font2.setColor(HSSFColor.BLACK.index);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				// 把字体应用到当前的样式
				style2.setFont(font2);
				// 声明一个画图的顶级管理器
				HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				// 定义注释的大小和位置,详见文档
				HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
						0, 0, 0, (short) 4, 2, (short) 6, 5));
				// 设置注释内容
				comment.setString(new HSSFRichTextString("BOSS校长系统"));
				// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
				comment.setAuthor("leno");
				// 产生表格标题行
				HSSFRow row = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers[i]);
					cell.setCellValue(text);
				}
				// 遍历集合数据，产生数据行
				Iterator<T> it = dataset.iterator();
				int index = 0;
				while (it.hasNext()) {
					index++;
					row = sheet.createRow(index);
					T t = it.next();
					// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
					Field[] fields = t.getClass().getDeclaredFields();
					for (int i = 0; i < fields.length; i++) {
						HSSFCell cell = row.createCell(i);
						cell.setCellStyle(style2);
						Field field = fields[i];
						String fieldName = field.getName();
						String getMethodName = "get"
								+ fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1);
						try {
							Class tCls = t.getClass();
							Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
							Object value = getMethod.invoke(t, new Object[]{});
							// 判断值的类型后进行强制类型转换
//							String textValue = null;
							// if (value instanceof Integer) {
							// int intValue = (Integer) value;
							// cell.setCellValue(intValue);
							// } else if (value instanceof Float) {
							// float fValue = (Float) value;
							// textValue = new HSSFRichTextString(
							// String.valueOf(fValue));
							// cell.setCellValue(textValue);
							// } else if (value instanceof Double) {
							// double dValue = (Double) value;
							// textValue = new HSSFRichTextString(
							// String.valueOf(dValue));
							// cell.setCellValue(textValue);
							// } else if (value instanceof Long) {
							// long longValue = (Long) value;
							// cell.setCellValue(longValue);
							// }
//							if (value instanceof Boolean) {
//								boolean bValue = (Boolean) value;
//								textValue = "男";
//								if (!bValue) {
//									textValue = "女";
//								}
//							} else 
//							if (value instanceof Date) {
//								//日期判断失败
//								Date date = (Date) value;
//								SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//								String textValue = sdf.format(date);
//								cell.setCellValue(textValue);
//							} else if (value instanceof byte[]) {
//								// 有图片时，设置行高为60px;
//								row.setHeightInPoints(60);
//								// 设置图片所在列宽度为80px,注意这里单位的一个换算
//								sheet.setColumnWidth(i, (short) (35.7 * 80));
//								// sheet.autoSizeColumn(i);
//								byte[] bsValue = (byte[]) value;
//								HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
//								1023, 255, (short) 6, index, (short) 6, index);
//								anchor.setAnchorType(2);
//								patriarch.createPicture(anchor, workbook.addPicture(
//								bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
//							} else
							if(value == null){
		 						cell.setCellValue("");
							} else if (value instanceof Number) {
								// 是数字当作double处理
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
								if (value instanceof Double) {
									cell.setCellValue((Double)value);
								} else {
									cell.setCellValue(Double.parseDouble(value.toString()));
								}
							} else {
								cell.setCellValue(value.toString());
//								HSSFRichTextString richString = new HSSFRichTextString(
//										textValue);
//								HSSFFont font3 = workbook.createFont();
//								font3.setColor(HSSFColor.BLACK.index);
//								richString.applyFont(font3);
//								cell.setCellValue(richString);
							}
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							// 清理资源
						}
					}
								 
				}
				if(status==1){
					//合并行单元格,起始行，结束行，起始列，结束列,从0开始，0表示列头行
					CellRangeAddress range = new CellRangeAddress(1, dataset.size(), headers.length-1, headers.length-1 );   
				    sheet.addMergedRegion(range);
				
				}
				try {
					workbook.write(out);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public void childExportExcelFromMap(String title, String[] headers,
			List<Map> dataset, OutputStream out, String pattern, int status,String[] heardersId){
				// 声明一个工作薄
				HSSFWorkbook workbook = new HSSFWorkbook();
				// 生成一个表格
				HSSFSheet sheet = workbook.createSheet(title);
				// 设置表格默认列宽度为15个字节
				sheet.setDefaultColumnWidth(15);
				// 生成一个样式
				HSSFCellStyle style = workbook.createCellStyle();
				// 设置这些样式
				style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
				style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// 生成一个字体
				HSSFFont font = workbook.createFont();
				font.setColor(HSSFColor.VIOLET.index);
				font.setFontHeightInPoints((short) 12);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				// 把字体应用到当前的样式
				style.setFont(font);
				// 生成并设置另一个样式
				HSSFCellStyle style2 = workbook.createCellStyle();
				style2.setFillForegroundColor(HSSFColor.WHITE.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				// 生成另一个字体
				HSSFFont font2 = workbook.createFont();
				//font2.setColor(HSSFColor.BLACK.index);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				// 把字体应用到当前的样式
				style2.setFont(font2);
				// 声明一个画图的顶级管理器
				HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				// 定义注释的大小和位置,详见文档
				HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
						0, 0, 0, (short) 4, 2, (short) 6, 5));
				// 设置注释内容
				comment.setString(new HSSFRichTextString("BOSS校长系统"));
				// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
				comment.setAuthor("leno");
				// 产生表格标题行
				int index = 0;
				HSSFRow row = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(headers[i]);
					cell.setCellValue(text);
				}
				// 遍历集合数据，产生数据行
				
				for (Iterator iterator = dataset.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();
					index++;
					row = sheet.createRow(index);
					int i = 0;
					for(String key:heardersId){
						HSSFCell cell = row.createCell(i);
						cell.setCellStyle(style2);
						Object value = map.get(key);
						if(value == null){
	 						cell.setCellValue("");
						} else if (value instanceof Number) {
							// 是数字当作double处理
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							if (value instanceof Double) {
								cell.setCellValue((Double)value);
							} else {
								cell.setCellValue(Double.parseDouble(value.toString()));
							}
						}else if("receiptStatus".equals(key) && EvidenceAuditStatus.valueOf(value.toString())!=null){
							cell.setCellValue(EvidenceAuditStatus.valueOf(value.toString()).getName());
						} else {
							cell.setCellValue(value.toString());
						}
						i++;
					}
				}
				if(status==1){
					//合并行单元格,起始行，结束行，起始列，结束列,从0开始，0表示列头行
					CellRangeAddress range = new CellRangeAddress(1, dataset.size(), headers.length-1, headers.length-1 );   
				    sheet.addMergedRegion(range);
				
				}
				try {
					workbook.write(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	
	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers,
							Collection<T> dataset, OutputStream out, String pattern, int status) {
		
		childExportExcel(title, headers, dataset, out, pattern, status);
		 
	}
	
	@SuppressWarnings("unchecked")
	public void exportExcelByItems(String title, String[] headers,
								   Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		//font2.setColor(HSSFColor.BLACK.index);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("CICCPS会员管理系统"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("leno");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		int index = 0;
		for (Iterator iterator = dataset.iterator(); iterator.hasNext(); ) {
			int i = 0;
			row = sheet.createRow(index + 1);
			Object[] t = (Object[]) iterator.next();
			for (int j = 0; j < headers.length; j++) {
				if (j == headers.length - 1 && t[j] != null) {
					System.out.print(CourseStatus.valueOf(t[j].toString()).getName() + " --" + t[j]);
					row.createCell(i).setCellValue(CourseStatus.valueOf(t[j].toString()).getName());
				} else {
					row.createCell(i).setCellValue(t[j].toString());
				}
				i++;
			}
			index++;
		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws Exception {
		// 第一步，创建一个workbook，对应一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet hssfSheet = workbook.createSheet("sheet1");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow hssfRow = hssfSheet.createRow(0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
		//居中样式
		hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 16);
		hssfCellStyle.setFont(font);

		HSSFCell hssfCell = hssfRow.createCell(0);//列索引从0开始
		hssfCell.setCellValue("学号");//列名1
		hssfCell.setCellStyle(hssfCellStyle);//列居中显示
		hssfCell = hssfRow.createCell(1);
		hssfCell.setCellValue("年龄");//列名2
		hssfCell.setCellStyle(hssfCellStyle);
		hssfCell = hssfRow.createCell(2);
		hssfCell.setCellValue("姓名");//列名3
		hssfCell.setCellStyle(hssfCellStyle);
		hssfCell = hssfRow.createCell(3);
		hssfCell.setCellValue("生日");//列名4
		hssfCell.setCellStyle(hssfCellStyle);

//	        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
//	        List<Student> list = CreateSimpleExcelToDisk.getStudents();
//	        for (int i = 0; i < list.size(); i++) {
//	            hssfRow = hssfSheet.createRow(i+1);
//	            Student student = list.get(i);
//	            // 第六步，创建单元格，并设置值
//	            hssfRow.createCell(0).setCellValue(student.getId());
//	            hssfRow.createCell(1).setCellValue(student.getAge());
//	            hssfRow.createCell(2).setCellValue(student.getName());
//	            hssfRow.createCell(3).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(student.getDays()));
//	        }
		// 第七步，将文件存到指定位置
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("E:/student.xls");//指定路径与名字和格式
			workbook.write(fileOutputStream);//讲数据写出去
			fileOutputStream.close();//关闭输出流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
