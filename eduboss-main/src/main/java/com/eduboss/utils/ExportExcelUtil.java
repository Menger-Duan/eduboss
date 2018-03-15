package com.eduboss.utils;

import com.eduboss.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 利用开源组件POI动态导出EXCEL文档
 */

public class ExportExcelUtil{

    private static Logger log = Logger.getLogger(ExportExcelUtil.class);
    /**
     * 创建book,设置标签页 
     * @param title
     * @return
     */
    public static HSSFWorkbook getNormalHssfWorkbook(String title) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
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
        return workbook;
    }

    /**
     * 获取样式
     * @param workbook
     * @param isTitle  抬头的字体跟颜色有区分
     * @return
     */
    public static HSSFCellStyle getNormalHssfSheet(HSSFWorkbook workbook, boolean isTitle) {
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
        HSSFFont font = workbook.createFont();
        if (isTitle) {
            font.setColor(HSSFColor.VIOLET.index);
            font.setFontHeightInPoints((short) 12);
        }
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style2.setFont(font);
        return style2;
    }

    /**
     * 根据listMap导出excel
     * @param title
     * @param headers
     * @param list
     * @param ossKey
     */
    public static void exportExcelByListMap(String title, Map<String, String> headers, List<Map> list, String ossKey) {
        HSSFWorkbook workbook = getNormalHssfWorkbook(title);
        HSSFCellStyle style2 = getNormalHssfSheet(workbook, false);
        HSSFSheet sheet = workbook.getSheet(title);
        setTitle(sheet,workbook,headers);

        int index = 1;
        for (Map map : list) {
            HSSFRow row = sheet.createRow(index);
            int dataCellNum = 0;
            for (String key : headers.keySet()) {
                HSSFCell cell = row.createCell(dataCellNum);
                cell.setCellStyle(style2);
                if (map.get(key) != null) {
                    cell.setCellValue(map.get(key).toString());
                }
                dataCellNum++;
            }

            index++;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);

        AliyunOSSUtils.put(ossKey, is, os.size());
    }

    /**
     * 根据key值获取抬头生成抬头第一行
     * @param sheet
     * @param workbook
     * @param headers
     */
    public static void setTitle(HSSFSheet sheet,HSSFWorkbook workbook,Map<String, String> headers){
        HSSFCellStyle style = getNormalHssfSheet(workbook, true);
        HSSFRow row = sheet.createRow(0);
        int cellNum = 0;
        for (String key : headers.keySet()) {
            HSSFCell cell = row.createCell(cellNum);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(key));
            cell.setCellValue(text);
            cellNum++;
        }
    }

    /**
     * 根据listVo导出excel
     * @param title
     * @param headers
     * @param list
     * @param ossKey
     * @param classs
     */
    public static void exportExcelByListVo(String title, Map<String, String> headers, Collection list, String ossKey,Class classs) {
        HSSFWorkbook workbook = getNormalHssfWorkbook(title);
        HSSFCellStyle style2 = getNormalHssfSheet(workbook, false);
        HSSFSheet sheet = workbook.getSheet(title);
        setTitle(sheet,workbook,headers);
        int index = 1;
        for (Object o : list) {
            o.getClass();
            HSSFRow row = sheet.createRow(index);

            int dataCellNum = 0;
            for (String key : headers.keySet()) {
                //获取对应的方法名
                String getMethodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
                try {
                    Method getMethod = classs.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(classs, new Object[]{});
                    HSSFCell cell = row.createCell(dataCellNum);
                    cell.setCellStyle(style2);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                    dataCellNum++;
                }catch (Exception e) {
                    log.error("导出Excel对象的属性对不上！");
                    e.printStackTrace();
                }
            }
            index++;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);

        AliyunOSSUtils.put(ossKey, is, os.size());
    }

    public static void exportExcelIncludeMerged(String sheetName, String[] headers, String[] fields, Collection list, String title,Class exportClass,Class[] repeatClass,OutputStream out) { 
    	 HSSFWorkbook workbook = getNormalHssfWorkbook(sheetName);
         HSSFCellStyle style2 = getNormalHssfSheet(workbook, false);
         HSSFSheet sheet = workbook.getSheet(sheetName);
         
         List<Integer> repeatIndex = new ArrayList<Integer>();
         String[] repeatField = new String[headers.length];
         String[] showField = new String[headers.length];
         
         setTitle(sheet,workbook,headers,fields,repeatIndex,repeatField,showField);
         int rowIndex=0;
         int count=0;
         
         HSSFRow row = null;
         StringBuffer getMethodName = new StringBuffer(64);
         
         Class showClass = null;
         HSSFRow showrow = null;
         
         for (Object obj : list) {
        	 rowIndex++;
        	 count++;//序号
        	 
             row = sheet.createRow(rowIndex);
             int startIndex = rowIndex;//合并起始行
             int endIndex = rowIndex;//合并结束行
             for (int i=0;i< headers.length;i++) {
            	 if(i==0) {
            		 HSSFCell cell = row.createCell(i);
            		 cell.setCellStyle(style2);
            		 cell.setCellValue(count);
            		 continue;
            	 }
                 //获取对应的方法名
            	 getMethodName.delete(0, getMethodName.length());
            	 try {
	            	 if(repeatIndex.contains(i)) {
	            		 getMethodName.append("get").append(repeatField[i].substring(0, 1).toUpperCase()).append(repeatField[i].substring(1));
	            		 Method getMethod = exportClass.getMethod(getMethodName.toString());
	            	     Object value = getMethod.invoke(obj);
	            	     if(value!=null) {
            	    		 List<Object> lists = (List<Object>) value;
            	    		 if(lists!=null && lists.size()>0) {
            	    			 	showClass = null;
            	    			 	showClass = getRepeatClass(lists.get(0),repeatClass);
            	    	    		for(Object showObject : lists) {
            	    	    			if(startIndex == rowIndex) {
            	    	    				showrow = row;
            	    	    			}else {
            	    	    				showrow = sheet.getRow(rowIndex)!=null?sheet.getRow(rowIndex):sheet.createRow(rowIndex);
            	    	    			}
            	    	    			getMethodName.delete(0, getMethodName.length());
            	    	    			getMethodName.append("get").append(showField[i].substring(0, 1).toUpperCase()).append(showField[i].substring(1));
            	    	    			pullCellValue(showClass,getMethodName,showrow,style2,i,showObject);
            	    	    			rowIndex++;
            	    	    		}
            	    	    		if(rowIndex > endIndex) {
            	    	    			endIndex = rowIndex-1;
            	    	    		}
            	    	    		rowIndex = startIndex;
            	    	    	}
	            	     }else {
	            	    	 HSSFCell cell = row.createCell(i);
	            	         cell.setCellStyle(style2);
	            	     }
	            	 }else {
	            		 getMethodName.append("get").append(fields[i].substring(0, 1).toUpperCase()).append(fields[i].substring(1));
	            		 pullCellValue(exportClass,getMethodName,row,style2,i,obj);
	            	 }
                 }catch (Exception e) {
                     log.error("导出Excel对象的属性对不上！");
                     e.printStackTrace();
                 }
             }
             for(int i=0;i< headers.length;i++) {
            	 if(!repeatIndex.contains(i)) {
            		 CellRangeAddress range = new CellRangeAddress(startIndex, endIndex, i, i);
            		 sheet.addMergedRegion(range);
            		 setRegionStyle(sheet,range,style2);
            	 }
             }
             
             rowIndex = endIndex;
         }
         try {
				workbook.write(out);
			} catch (IOException e) {
				log.error("导出Excel-OutputStream异常！");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) { 
    	  for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
    		  HSSFRow row = HSSFCellUtil.getRow(i, sheet);
    		  for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
    			  HSSFCell cell = HSSFCellUtil.getCell(row, (short) j);
    			  cell.setCellStyle(cs);
    		  }
    	  }
    }
    
    public static void setTitle(HSSFSheet sheet,HSSFWorkbook workbook, String[] headers, String[] fields,List<Integer> repeatIndex,String[] repeatField,String[] showField){ 
        HSSFCellStyle style = getNormalHssfSheet(workbook, true);
        HSSFRow row = sheet.createRow(0);
        for (int i=0;i< headers.length;i++) {
        	String key = fields[i];
        	if(key.contains(".")) {
        		repeatIndex.add(i);
        		repeatField[i] = key.substring(0, key.indexOf("."));
        		showField[i] = key.substring(key.indexOf(".")+1);
        	}
        	
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
    }
    
    public static void pullCellValue(Class exportClass,StringBuffer getMethodName,HSSFRow row, HSSFCellStyle style2,int rowIndex,Object obj) throws Exception{ 
    	Method getMethod = exportClass.getMethod(getMethodName.toString());
        Object value = getMethod.invoke(obj);
        HSSFCell cell = row.createCell(rowIndex);
        cell.setCellStyle(style2);
        if (value != null) {
            cell.setCellValue(value.toString());
        }
    }
    public static Class getRepeatClass(Object showObject,Class[] repeatClass){ 
    	for(Class c : repeatClass) {
    		if(showObject.getClass() == c) {
    			return c;
    		}
    	}
    	return null;
    }
    
    public static void main(String[] args) throws Exception {
    }
    
}
