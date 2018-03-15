package com.eduboss.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class EmojiConvertUtil {
	
	public final static  String ConvertPatternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";  
	
	public final static  String RecoveryPatternString = "\\[\\[(.*?)\\]\\]";  
	
	private final static Logger LOG = Logger.getLogger(EmojiConvertUtil.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String abc="123\ud83d\ude0213";
		abc = "\ud83d\ude02";
		abc = EmojiConvertUtil.emojiConvert(abc);
		System.err.println("emojiConvert:"+abc);
		abc = EmojiConvertUtil.emojiRecovery(abc);
		System.err.println("emojiRecovery:"+abc);
		abc = EmojiConvertUtil.removeEmoji(abc);
		System.err.println("emojiRecovery:"+abc);
	}
	
	/** 
	 * @Description 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式
	 * @param str 
	 *            待转换字符串 
	 * @return 转换后字符串 
	 * @throws UnsupportedEncodingException 
	 *             exception 
	 */  
	public static String emojiConvert(String str)  
	         {  
	    
	  
	    Pattern pattern = Pattern.compile(EmojiConvertUtil.ConvertPatternString);  
	    Matcher matcher = pattern.matcher(str);  
	    StringBuffer sb = new StringBuffer();  
	    while(matcher.find()) {  
	        try {  
	            matcher.appendReplacement(  
	                    sb,  
	                    "[["  
	                            + URLEncoder.encode(matcher.group(1),  
	                                    "UTF-8") + "]]");  
	        } catch(UnsupportedEncodingException e) {  
	            LOG.error("emojiConvert error", e);  
	        }  
	    }  
	    matcher.appendTail(sb);  
	    LOG.debug("emojiConvert " + str + " to " + sb.toString()  
	            + ", len：" + sb.length());  
	    return sb.toString();  
	}  
	  
	/** 
	 * @Description 还原utf8数据库中保存的含转换后emoji表情的字符串 
	 * @param str 
	 *            转换后的字符串 
	 * @return 转换前的字符串 
	 * @throws UnsupportedEncodingException 
	 *             exception 
	 */  
	public static String emojiRecovery(String str)  
	         {  
	    Pattern pattern = Pattern.compile(EmojiConvertUtil.RecoveryPatternString);  
	    Matcher matcher = pattern.matcher(str);  
	  
	    StringBuffer sb = new StringBuffer();  
	    while(matcher.find()) {  
	        try {  
	            matcher.appendReplacement(sb,  
	                    URLDecoder.decode(matcher.group(1), "UTF-8"));  
	        } catch(UnsupportedEncodingException e) {  
	            LOG.error("emojiRecovery error", e);  
	        }  
	    }  
	    matcher.appendTail(sb);  
	    LOG.debug("emojiRecovery " + str + " to " + sb.toString());  
	    return sb.toString();  
	}  
	
	/** 
	 * @Description 移除表情
	 */  
	public static String removeEmoji(String str)  
	         {  
	    Pattern pattern = Pattern.compile(EmojiConvertUtil.ConvertPatternString);  
	    Matcher matcher = pattern.matcher(str);  
	    StringBuffer sb = new StringBuffer();  
	    while(matcher.find()) {  
	            matcher.appendReplacement(  
	                    sb,  
	                    "");  
	    }  
	    matcher.appendTail(sb);  
	    LOG.debug("removeEmoji " + str + " to " + sb.toString()  
	            + ", len：" + sb.length());  
	    return sb.toString();  
	}  

}
