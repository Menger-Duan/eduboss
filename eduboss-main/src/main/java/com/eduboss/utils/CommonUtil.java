package com.eduboss.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eduboss.common.MonthType;
import com.eduboss.exception.ApplicationException;

public class CommonUtil {
	
	/** log */
	private static Log log = LogFactory.getLog(CommonUtil.class);
	
	public static final String CONFIG_PATH = "/systemconfig.properties";
	
	public static final String DEFAULT_URL_ENCODE = "gbk";
	
	public static int autoComplateLimit = 20;
	
	public static int ALLOW_MAX_CAMPUS_CUS=2;//分校  这三个变量用于只有几个校区的客户
	public static int ALLOW_MAX_BRENCH_CUS=1;//分公司
	public static int ALLOW_MAX_GROUNP_CUS=1;//集团
	
	public static int ALLOW_MAX_CAMPUS=2;
	public static int ALLOW_MAX_BRENCH=1;
	public static int ALLOW_MAX_GROUNP=1;
	private static String ALLOW_MAX_CAMPUS_NAME="allow_max_campus";
	private static String ALLOW_MAX_BRENCH_NAME="allow_max_brench";
	private static String ALLOW_MAX_GROUNP_NAME="allow_max_grounp";
	
	public static final String FINANCE_QUEUE="FINANCE_QUEUE";
	public static final String FINANCE_QUEUE_ERROR="FINANCE_QUEUE_ERROR";
	
	static{
		setOrganizationValue();
	}
	
	public static void setOrganizationValue() {
		try {
			Properties p = new Properties();
			p.load(CommonUtil.class.getResourceAsStream(CONFIG_PATH));
			String allowMaxCampus = p.getProperty(CommonUtil.ALLOW_MAX_CAMPUS_NAME);
			String allowMaxBrench = p.getProperty(CommonUtil.ALLOW_MAX_BRENCH_NAME);
			String allowMaxGrounp = p.getProperty(CommonUtil.ALLOW_MAX_GROUNP_NAME);
			if(StringUtil.isNotBlank(allowMaxCampus)){
				CommonUtil.ALLOW_MAX_CAMPUS=Integer.valueOf(allowMaxCampus);
			}
			if(StringUtil.isNotBlank(allowMaxBrench)){
				CommonUtil.ALLOW_MAX_BRENCH=Integer.valueOf(allowMaxBrench);
			}
			if(StringUtil.isNotBlank(allowMaxGrounp)){
				CommonUtil.ALLOW_MAX_GROUNP=Integer.valueOf(allowMaxGrounp);
			}
		} catch (Exception e) {
			throw new ApplicationException("读取配置文件失败");
		}
	}
	
	/**
	 * @param oString
	 * @return
	 */
	public static String urlEncode(String oString) {
		String returnString = "";
		try {
			returnString = URLEncoder.encode(oString, getPropertyValue("urlEncode", DEFAULT_URL_ENCODE));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return returnString;
	}
	
	public static String utfFormat(String strKey){
		String key = "";
		try {
			key = new String(strKey.getBytes("UTF-8"),"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return key;
	}
	
	/**
	 * 
	 * @param strKe
	 * @param oldByte
	 * @param newBytes
	 * @return
	 */
	public static String utfFormat(String strKey,String oldBytes,String newBytes){
		String key = "";
		try {
			key = new String(strKey.getBytes(oldBytes),newBytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * @param stringList
	 * @param symbol
	 * @return
	 */
	public  static String listToString(List<String> stringList, String symbol) {
		String returnString = "";
		for (String name : stringList) {
			returnString += name + symbol;
		}
		returnString = returnString.substring(0, returnString.length()-1);
		return returnString;
	}
	
	/**
	 * @param array
	 * @param connector
	 * @return
	 */
	public static String arrayToString(String[] array, String connector) {
		StringBuffer reutrnStringBuffer = new StringBuffer();
		for(String str : array) {
			reutrnStringBuffer.append(str).append(connector);
		}
		String returnString = reutrnStringBuffer.toString();
		return returnString.substring(0, returnString.length()-1);
	}
	
	/**
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String propertyName) {
		String propertyValue = "";
		try {
			Properties p = new Properties();
			p.load(CommonUtil.class.getResourceAsStream(CONFIG_PATH));
			
			propertyValue = p.getProperty(propertyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return propertyValue;
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String propertyName, String defaultValue) {
		String propertyValue = "";
		try {
			Properties p = new Properties();
			p.load(CommonUtil.class.getResourceAsStream(CONFIG_PATH));
			
			propertyValue = p.getProperty(propertyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (StringUtils.isBlank(propertyValue)) {
			propertyValue = defaultValue;
		}
		
		return propertyValue;
	}
	
	
	/**  
	 * ʹ��java������ʽȥ�������,��0  
	 * @param s  
	 * @return   
	 */  
	public static String subZeroAndDot(String s){   
	    if(s.lastIndexOf(",000") > 0){   
	        s = s.replaceAll("0+?$", "");//ȥ�������0   
	        s = s.replaceAll("[,]$", "");//�����һλ��,��ȥ��   
	        return subZeroAndDot(s);
	    }   
	    return s;   
	} 
	
	
	
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}


	/**
	 * �������ַ� 
	 * @return
	 */
	public static String getRandomString(int count) { 
		StringBuffer sb = new StringBuffer();         
		String str = "0123456789";         
		Random r = new Random();         
		for(int i=0;i<count;i++){             
			int num = r.nextInt(str.length());             
			sb.append(str.charAt(num));             
			str = str.replace((str.charAt(num)+""), "");         
		}     
		return sb.toString(); 
	}
	
	/**
	 * �Ƿ�������ʺ�
	 * @param account
	 * @return
	 */
	public static boolean isTest(String account) {
		boolean isTest = false;
		String testEnable = CommonUtil.getPropertyValue("testEnable");
		String testAccountArrayStr = CommonUtil.getPropertyValue("testAccount");
		if ("true".equalsIgnoreCase(testEnable) && StringUtils.isNotBlank(testAccountArrayStr) && testAccountArrayStr.contains(account)) {
			isTest = true;
		}
		return isTest;
	}
	
	/**
	 * �Ƿ�������ʺ�
	 * @param account
	 * @return
	 */
	public static boolean isVisitor(String account) {
		boolean isTest = false;
		String visitorEnable = CommonUtil.getPropertyValue("visitorEnable");
		String visitorAccountStr = CommonUtil.getPropertyValue("visitorAccount");
		if ("true".equalsIgnoreCase(visitorEnable) && StringUtils.isNotBlank(visitorAccountStr) && visitorAccountStr.contains(account)) {
			isTest = true;
		}
		return isTest;
	}
	
	public static String getBrand(String brandNo) {
		String brand = "δ֪Ʒ��";
		if ("3".equalsIgnoreCase(brandNo)) {
			brand = "���еش�";
		} else if ("2".equalsIgnoreCase(brandNo)) {
			brand = "������";
		} else if ("1".equalsIgnoreCase(brandNo)) {
			brand = "ȫ��ͨ";
		}
		return brand;
	}
	
	/**
	 * ��ֻת����δ֪
	 * @param str
	 * @return String
	 */
	public static String nullToUnknown(String str) {
		if(StringUtils.isEmpty(str)){
			return "δ֪";
		}
		return str;
	}
	/**
	 * ��ֻת���ɲ�֧��
	 * @param str
	 * @return String
	 */
	public static String nullToNonsupport(String str) {
		if(StringUtils.isEmpty(str)){
			return "��֧��";
		}
		return str;
	}
	
	public static String nullToBlank(String str) {
		if(StringUtils.isEmpty(str)){
			return "";
		}
		return str;
	}
	public static String nullToBlank(Object obj) {
		if(obj == null){
			return "";
		}
		return obj.toString();
	}
	public static Object nullToZero(Object obj) {
		if(obj == null){
			return "0";
		}
		return obj;
	}
	
	/**1��ʾ��,0��ʾ��
	 * @param value
	 * @return
	 */
	public static String convIsOrNo(String value) {
		if("1".equals(value)){
			return "��";
		}else if("0".equals(value)){
			return "��";
		}
		return value;
	}
	
	public static String  createGuid(){
	    String guid = "";
	    for (int i = 1; i <= 32; i++){
	    	String s = Integer.toHexString((int)Math.floor(Math.random()*16.0));
	    	
	    	guid += s;
	    }
	    return guid;
	}
	
	// 判断字符串是否是数字
	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
	// 根据月份数获取MonthType的value
	public static String getMonthTypeValue(int month) {
		String monthValue = "";
		switch (month) {
		case 1:
			monthValue = MonthType.JAN.getValue();
			break;
		case 2:
			monthValue = MonthType.FEB.getValue();
			break;
		case 3:
			monthValue = MonthType.MAR.getValue();
			break;
		case 4:
			monthValue = MonthType.APR.getValue();
			break;
		case 5:
			monthValue = MonthType.MAY.getValue();
			break;
		case 6:
			monthValue = MonthType.JUN.getValue();
			break;
		case 7:
			monthValue = MonthType.JUL.getValue();
			break;
		case 8:
			monthValue = MonthType.AUG.getValue();
			break;
		case 9:
			monthValue = MonthType.SEPT.getValue();
			break;
		case 10:
			monthValue = MonthType.OCT.getValue();
			break;
		case 11:
			monthValue = MonthType.NOV.getValue();
			break;
		case 12:
			monthValue = MonthType.DEC.getValue();
			break;
		}
		return monthValue;
	}
	
	 public static int getNumOfBits(double number) {
        String numberStr = String.valueOf(number);
        // 正则表达式判断是否为小数
        // 获取小数点的位置
        int bitPos = numberStr.indexOf(".");
        if (bitPos < 0) {
            return 0;
        }
        // 字符串总长度减去小数点位置，再减去1，就是小数位数
        return numberStr.length() - bitPos - 1;
    }
	 
	 public static boolean checkNumber(String num){
	     if(num.length() != 11){
	         return false;
	     }
	     if(!num.startsWith("1")){
	         return false;
	     }
	     return true;
     }
	 
	 public static String getPercent(int num1, int num2) {
         // 创建一个数值格式化对象  
         NumberFormat numberFormat = NumberFormat.getInstance();  
         // 设置精确到小数点后2位  
         numberFormat.setMaximumFractionDigits(2);  
         String result = numberFormat.format((float) num1 / (float) num2 * 100);
         return result;
	 }
	 
}
