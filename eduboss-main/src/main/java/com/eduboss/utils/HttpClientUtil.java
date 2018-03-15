package com.eduboss.utils;



import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.DigestException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.HttpHeaders;
import com.eduboss.common.GroupCode;
import com.eduboss.domainVo.AttentanceInfoVo;
import com.eduboss.domainVo.TextBookBossVo;
import com.eduboss.domainVo.TextBookVo;
//import com.eduboss.common.GroupCode;
//import com.eduboss.domainVo.AttentanceInfoVo;
import com.eduboss.domainVo.EduPlatform.BaseRelateDataVo;
import com.eduboss.domainVo.EduPlatform.UpdateAttentanceInfoVo;
import com.eduboss.exception.ApplicationException;

/**
 * 
 * @author xiaojinwang
 *
 */
public class HttpClientUtil {
	
	private final static Logger LOGGER = Logger.getLogger(HttpClientUtil.class);

	static final int timeOut = 60 * 1000;

	private static CloseableHttpClient httpClient = null;

	public static final String CHARSET = "UTF-8";

	private final static Object syncLock = new Object();

	private static void config(HttpRequestBase httpRequestBase) {
		// 设置Header等
		// httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
		// httpRequestBase
		// .setHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		// httpRequestBase.setHeader("Accept-Language",
		// "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
		// httpRequestBase.setHeader("Accept-Charset",
		// "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
		httpRequestBase.setConfig(requestConfig);
	}

	public static CloseableHttpClient getHttpClient(String url) {
		String hostname = url.split("/")[2];
		int port = 80;
		if (hostname.contains(":")) {
			String[] arr = hostname.split(":");
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}
		if (httpClient == null) {
			synchronized (syncLock) {
				if (httpClient == null) {
					httpClient = createHttpClient(200, 40, 100, hostname, port);
				}
			}
		}
		return httpClient;
	}

	public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
			int port) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);
		HttpHost httpHost = new HttpHost(hostname, port);
		// 将目标主机的最大连接数增加
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 3) {// 如果已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
					return false;
				}
				if (exception instanceof InterruptedIOException) {// 超时
					return false;
				}
				if (exception instanceof UnknownHostException) {// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {// SSL握手异常
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setRetryHandler(httpRequestRetryHandler).build();

		return httpClient;
	}

	// private static void setPostParams(HttpPost httpost,
	// Map<String, Object> params) {
	// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	// Set<String> keySet = params.keySet();
	// for (String key : keySet) {
	// nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
	// }
	// try {
	// httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static String post(String url, Map<String, Object> params) throws
	// IOException {
	// HttpPost httppost = new HttpPost(url);
	// config(httppost);
	// setPostParams(httppost, params);
	// CloseableHttpResponse response = null;
	// try {
	// response = getHttpClient(url).execute(httppost,
	// HttpClientContext.create());
	// HttpEntity entity = response.getEntity();
	// String result = EntityUtils.toString(entity, CHARSET);
	// EntityUtils.consume(entity);
	// return result;
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw e;
	// } finally {
	// try {
	// if (response != null)
	// response.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// public static String get(String url) {
	// HttpGet httpget = new HttpGet(url);
	// config(httpget);
	// CloseableHttpResponse response = null;
	// try {
	// response = getHttpClient(url).execute(httpget,
	// HttpClientContext.create());
	// HttpEntity entity = response.getEntity();
	// String result = EntityUtils.toString(entity, CHARSET);
	// EntityUtils.consume(entity);
	// return result;
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (response != null)
	// response.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	private static Header[] parseHeader(Map<String, String> headers) {
		if (null == headers || headers.isEmpty()) {
			return getDefaultHeaders();
		}
		Header[] allHeader = new BasicHeader[headers.size()];
		int i = 0;
		for (String str : headers.keySet()) {
			allHeader[i] = new BasicHeader(str, headers.get(str));
			i++;
		}
		return allHeader;
	}

	private static Header[] getDefaultHeaders() {
		Header[] allHeader = new BasicHeader[2];
		allHeader[0] = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
		allHeader[1] = new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		return allHeader;
	}

	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url
	 *            请求的url地址 ?之前的地址
	 * @param params
	 *            请求的参数
	 * @param charset
	 *            编码格式
	 * @return 页面内容
	 */
	public static String doGet(String url, Map<String, String> params, Map<String, String> headers, String charset) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
			}
			LOGGER.info("httpClient-doGet:"+url);
			HttpGet httpGet = new HttpGet(url);
			config(httpGet);
			if (headers != null) {
				httpGet.setHeaders(parseHeader(headers));
			}
			CloseableHttpResponse response = getHttpClient(url).execute(httpGet, HttpClientContext.create());
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doPost(String url, Map<String, String> params, Map<String, String> headers, String charset) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		try {
			List<NameValuePair> pairs = null;
			if (params != null && !params.isEmpty()) {
				pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			LOGGER.info("httpClient-doPost:"+url);
			config(httpPost);
			if (headers != null) {
				httpPost.setHeaders(parseHeader(headers));
			}
			if (pairs != null && pairs.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
			}
			CloseableHttpResponse response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param url
	 *            请求的url地址 ?之前的地址
	 * @param params
	 *            请求的参数
	 * @param charset
	 *            编码格式
	 * @return 页面内容
	 */
	public static String doPost(String url, Map<String, String> params, String charset) {
		return doPost(url, params, null, charset);
	}

	public static String doGet(String url, Map<String, String> params, String charset) {
		return doGet(url, params, null, charset);
	}

	public static String doGet(String url, Map<String, String> params) {
		return doGet(url, params, CHARSET);
	}

	public static String doPost(String url, Map<String, String> params) {
		return doPost(url, params, CHARSET);
	}
    /**
     * post json字符串
     * @param url
     * @param postContent json字符串
     * @return
     */
	public static String doPostJson(String url, String postContent) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		try {

			HttpPost httpPost = new HttpPost(url);
			config(httpPost);

			StringEntity content = new StringEntity(postContent, CHARSET);// 解决中文乱码问题
			content.setContentEncoding(CHARSET);
			content.setContentType("application/json");
			httpPost.setEntity(content);
			CloseableHttpResponse response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	 /**
     * post json字符串
     * @param url
     * @param postContent json字符串
     * @return
     */
	public static String doPostJson2(String url, String postContent,Map<String, String> headers) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		try {

			HttpPost httpPost = new HttpPost(url);
			config(httpPost);

			StringEntity content = new StringEntity(postContent, CHARSET);// 解决中文乱码问题
			content.setContentEncoding(CHARSET);
			content.setContentType("application/json");
			httpPost.setEntity(content);
			if (headers != null) {
				httpPost.setHeaders(parseHeader(headers));
			}
			CloseableHttpResponse response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			LOGGER.info("doPostJson2:"+result);
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Map<String, String> getLiveDefaultHeaders() {
		Random random=new Random();
		Long ran=random.nextLong();
		
		Map<String, String> headerds = new HashMap<>();
		
		Date date=new Date();
		date=DateTools.add(date,Calendar.MONTH,1);
		Map<String,Object> param=new HashMap<>();

		param.put("1",PropertiesUtils.getStringValue("LIVE_SECRET"));
		param.put("2",ran.toString());
		param.put("3",String.valueOf(date.getTime()));

		headerds.put("Xiao-User-Agent", PropertiesUtils.getStringValue("LIVE_USER"));
		headerds.put("X-App-Id", PropertiesUtils.getStringValue("LIVE_APP_ID"));
		headerds.put("X-Nonce", ran.toString());
		headerds.put("X-Expiration-At", String.valueOf(date.getTime()));
		//headerds.put(HttpHeaders.CONTENT_TYPE, "Content-Type");
		try {

			headerds.put("X-Signature", Sha1Utils.SHA1(param));

			LOGGER.info("Xiao-User-Agent"+PropertiesUtils.getStringValue("LIVE_USER"));
			LOGGER.info("X-App-Id"+PropertiesUtils.getStringValue("LIVE_APP_ID"));
			LOGGER.info("X-Nonce"+ran.toString());
			LOGGER.info("X-Expiration-At"+String.valueOf(date.getTime()));
			LOGGER.info("X-Signature"+Sha1Utils.SHA1(param));
		} catch (DigestException e) {
			e.printStackTrace();
			throw new ApplicationException("加密SHA1失败");
		}
		
		return headerds;
	}
	public static void main(String[] args) {
		// Map<String, String> params = new HashMap<>();
		// params.put("pageNum", "1");
		// params.put("pageSize", "20");
		// String getData =
		// doGet("http://test.teaching.xiaojiaoyu100.com/tea_api/openapi/pycourse/list",params);
		// System.out.println(getData);
		// System.out.println("----------------------分割线-----------------------");

		List<AttentanceInfoVo> infos = new ArrayList<>();

		AttentanceInfoVo attentanceInfoVo1 = new AttentanceInfoVo();
		attentanceInfoVo1.setCourseId("MIN0000000024");
		attentanceInfoVo1.setStudentId("STU1611140002");
		attentanceInfoVo1.setStatus("CONPELETE");// CONPELETE
		attentanceInfoVo1.setGroupCode(GroupCode.APLUS.getValue());

		AttentanceInfoVo attentanceInfoVo2 = new AttentanceInfoVo();
		attentanceInfoVo2.setCourseId("MIN0000000024");
		attentanceInfoVo2.setStudentId("STU1611140001");
		attentanceInfoVo2.setStatus("LATE");// LATE
		attentanceInfoVo2.setGroupCode(GroupCode.APLUS.getValue());
		AttentanceInfoVo attentanceInfoVo3 = new AttentanceInfoVo();
		attentanceInfoVo3.setCourseId("MIN0000000025");
		attentanceInfoVo3.setStudentId("STU1611250001");
		attentanceInfoVo3.setStatus("NEW");// NEW
		attentanceInfoVo3.setGroupCode(GroupCode.APLUS.getValue());
		infos.add(attentanceInfoVo1);
		infos.add(attentanceInfoVo2);
		infos.add(attentanceInfoVo3);
		
		UpdateAttentanceInfoVo info = new UpdateAttentanceInfoVo();
		info.setInfos(infos);
		info.setType(UpdateAttentanceInfoVo.Type.UPDATE);
		
		String postContent = JSON.toJSONString(infos);
		System.out.println(postContent);
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		//String url = "http://test.teaching.xiaojiaoyu100.com/tea_api/openapi/pycourse/rollcall";
		String url ="http://localhost:8082/eduboss/EduPlatform/student/updateAttentanceInfo.do";
		String response = doPostJson2(url, postContent,HttpClientUtil.getLiveDefaultHeaders());
		System.out.println("response:" + response);
//		Map<String, String> params = new HashMap<>();
//		final String listUrl = PropertiesUtils.getStringValue("bossTextBook"); 
//		String json = null;
//        json = HttpClientUtil.doGet(listUrl, params);
//        // 解析结果
//        JSONObject object = JSON.parseObject(json);
//
//
//        Integer code = (Integer) object.get("code");
//        JSONObject data = (JSONObject) object.get("data");
//        JSONArray jsonArray = data.getJSONArray("result");
//        Integer totalCount = (Integer) data.get("totalCount");
//        List<TextBookBossVo> result = null;
//        if(jsonArray!=null){
//            result = JSON.parseArray(jsonArray.toJSONString(), TextBookBossVo.class);
//        }
//        System.out.println(result.size());
	}

}
