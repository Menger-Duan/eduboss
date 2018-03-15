package com.eduboss.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 * 
 * 日志记录帮助类
 *
 */
public class LoggerHelper { 
	
	private static final SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	private static Map<String, String> browserMapping=new HashMap<String, String>();
	
	private  static Pattern pattern_browserType_google= Pattern.compile("Chrome.*? ");
	
	
	private static String template_browserInfo=Constant.CONSTANT_BROWSERTYPE+"("+Constant.CONSTANT_BROWSERVERSION+")";
	
	static{
		browserMapping.put(BrowserTypeEngine.ENGINE_IE, BrowserType.IE);
		browserMapping.put(BrowserTypeEngine.ENGINE_FIREFOX, BrowserType.FIREFOX);
		browserMapping.put(BrowserTypeEngine.ENGINE_GOOGLE, BrowserType.GOOGLE);
	}
	
	public static class Constant{
		public static String CONSTANT_BROWSERTYPE="BrowserType";
		public static String CONSTANT_BROWSERVERSION="BrowserVersion";
	}
	
	
	public static class BrowserType{
		
		public  static String IE="IE";
		
		public  static String FIREFOX="Firefox";
		
		public  static String GOOGLE="Google";
		
		public  static String UNKNOW="unknow Browser";
		
	}
	
	
	public static class BrowserTypeEngine {

		public static String ENGINE_IE = "MSIE";

		public static String ENGINE_FIREFOX = "Gecko";

		public static String ENGINE_GOOGLE = "Chrome";
		
		

	}
	
	
	/**
	 * 获取浏览器版本信息
	 * @param inputStr
	 * @return
	 */
	public static String getBrowserInfo(String inputStr){
		String retValue=BrowserType.UNKNOW;
		
		if(inputStr.indexOf(BrowserTypeEngine.ENGINE_GOOGLE)>-1){
			/**格式形如 Mozilla/5.0 (Platform; Encryption; OS-or-CPU; Language) AppleWebKit/AppleWebKitVersion (KHTML, like Gecko) Chrome/ChromeVersion Safari/SafariVersion
			  eg:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36
			*/
			Matcher matcher=pattern_browserType_google.matcher(inputStr);
			if(matcher.find()){
				String browserVersion=matcher.group();
				browserVersion=browserVersion.substring(0,browserVersion.length()-1);
				//按照模板 写入信息
				String broinfo=new String(template_browserInfo);
				String key=BrowserTypeEngine.ENGINE_GOOGLE;
				String value=browserMapping.get(BrowserTypeEngine.ENGINE_GOOGLE);
				broinfo=broinfo.replaceFirst(Constant.CONSTANT_BROWSERTYPE, browserMapping.get(BrowserTypeEngine.ENGINE_GOOGLE));
				broinfo=broinfo.replaceFirst(Constant.CONSTANT_BROWSERVERSION, browserVersion);
				retValue=broinfo;
			}
		}
		
		return retValue;
		
	}
	
	
	/**
	 * 获取操作系统信息
	 * @param inputStr
	 * @return
	 */
	public static String getOperatSystemInfo(String inputStr){
		String retValue="other";
		
		if(inputStr.indexOf(BrowserTypeEngine.ENGINE_GOOGLE)>-1){
			/**格式形如 Mozilla/5.0 (Platform; Encryption; OS-or-CPU; Language) AppleWebKit/AppleWebKitVersion (KHTML, like Gecko) Chrome/ChromeVersion Safari/SafariVersion
			  eg:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36
			*/
			int beginIndex=inputStr.indexOf("(");
			int endIndex=inputStr.indexOf(")");
			retValue=inputStr.substring(beginIndex+1, endIndex);
		}
		
		return retValue;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getCurrentTime(){
		return dateFormat.format(new Date());
		
	}

}
