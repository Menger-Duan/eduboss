package com.eduboss.utils;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * 
 * @author niudd
 * 2014-3-26
 */
public class CookieUtil {
	public static int maxAge=3600*24;//一天（单位秒）
	public static String callsPhoneCheckFail="callsPhoneCheckFail";
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 删除cookie
	 * @param request
	 * @param response
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}

	public static void deleteCookieByKey(String key, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(key)) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
				break;
			}
		}
	}
	public static void deleteCookieByKey(String key,String domain, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(key)) {
				cookie.setMaxAge(0);
				cookie.setDomain(domain);
				cookie.setPath("/");
				response.addCookie(cookie);
				break;
			}
		}
	}
	/**
	 * 设置cookie value 进行编码
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, URLEncoder.encode(value));
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	
	public static void addCookie(HttpServletResponse response, String name, String value,String domain, int maxAge) {
		Cookie cookie = new Cookie(name, URLEncoder.encode(value));
		cookie.setPath("/");
		cookie.setDomain(domain);
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	/**
	 * 获取解码后的value
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValueByName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					String cartJson = cookie.getValue();
					return URLDecoder.decode(cartJson);
				}
			}
		}
		return null;
	}

	/**
	 * 获取cookie中的所有json信息
	 * 
	 * @param request
	 * @return
	 */
	public static List<String> getCookieJson(HttpServletRequest request) {
		List<String> list = new ArrayList<String>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			String cartJson = null;
			for (Cookie cookie : cookies) {
				cartJson = URLDecoder.decode(cookie.getValue());
				list.add(cartJson);
			}
		}
		return list;
	}

	/**
	 * 根据名字获取cookie
	 * 
	 * @param request
	 * @param name
	 *            cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * @return
	 */
	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
	
	
	public static void putCookieCallsPhoneCheckFail(HttpServletRequest request,HttpServletResponse response,String phone,String callsTime) throws Exception{
		Cookie cookie=getCookieByName(request, CookieUtil.callsPhoneCheckFail);
		Map<String, String> map=null;
		if(cookie!=null){
			String value=URLDecoder.decode(cookie.getValue());
			map=objectMapper.readValue(value, Map.class);
		}else{
			 map=new HashMap<String, String>();
		}
		map.put(phone, callsTime);
		addCookie(response,  CookieUtil.callsPhoneCheckFail, objectMapper.writeValueAsString(map), CookieUtil.maxAge);
	}
	
	/**
	 * 获取cookie 同事删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static Map getCookieCallsPhoneCheckFail(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Cookie cookie=getCookieByName(request, CookieUtil.callsPhoneCheckFail);
		Map<String, String> map=null;
		if(cookie!=null){
			String value=cookie.getValue();
			map=objectMapper.readValue(URLDecoder.decode(value), Map.class);
			deleteCookieByKey(CookieUtil.callsPhoneCheckFail, request, response);
		}
		
		return map;
	}

}
