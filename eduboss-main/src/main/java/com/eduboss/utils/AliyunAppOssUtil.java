package com.eduboss.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

/**
 * app 阿里云上传
 * 
 * @author xiaojinwang
 *
 */
public class AliyunAppOssUtil {
	
    private static final String host = PropertiesUtils.getStringValue("oss.sts.host");
    private static final String callbackUrl = PropertiesUtils.getStringValue("oss.sts.callbackUrl");
    private static final String endpoint = PropertiesUtils.getStringValue("oss.sts.endpoint");

	// 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
	public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
	public static final String STS_API_VERSION = "2015-04-01";
	public static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
			String roleSessionName, String policy, ProtocolType protocolType, long durationSeconds) throws ClientException 
	{
		try {
			// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
			IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
			DefaultAcsClient client = new DefaultAcsClient(profile);

			// 创建一个 AssumeRoleRequest 并设置请求参数
			final AssumeRoleRequest request = new AssumeRoleRequest();
			request.setVersion(STS_API_VERSION);
			request.setMethod(MethodType.POST);
			request.setProtocol(protocolType);

			request.setRoleArn(roleArn);
			request.setRoleSessionName(roleSessionName);
			request.setPolicy(policy);
			request.setDurationSeconds(durationSeconds);

			// 发起请求，并得到response
			final AssumeRoleResponse response = client.getAcsResponse(request);

			return response;
		} catch (ClientException e) {
			throw e;
		}
	}

	public static String ReadJson(String path){
        //从给定位置获取文件
        File file = new File(path);
        BufferedReader reader = null;
        //返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            reader = new BufferedReader(new FileReader(file));
            //每次读取文件的缓存
            String temp = null;
            while((temp = reader.readLine()) != null){
                data.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭文件流
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }
	
	
	public static void getAppSTSKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 只有 RAM用户（子账号）才能调用 AssumeRole 接口
		// 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
		// 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
		
		
		String accessKeyId = PropertiesUtils.getStringValue("oss.sts.AccessKeyId");
		String accessKeySecret = PropertiesUtils.getStringValue("oss.sts.AccessKeySecret");
		
		// RoleArn 需要在 RAM 控制台上获取
		String roleArn = PropertiesUtils.getStringValue("oss.sts.RoleArn");
		Integer durationSeconds = PropertiesUtils.getIntValue("oss.sts.TokenExpireTime");
		ClassLoader classLoader = AliyunAppOssUtil.class.getClassLoader();
		URL url = classLoader.getResource(PropertiesUtils.getStringValue("oss.sts.PolicyFile"));
		String policy = ReadJson(url.getFile());
		String bucketName = PropertiesUtils.getStringValue("oss.sts.bucketName");
		policy=policy.replace("${bucketName}", bucketName);
		
		// RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
		// 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
		// 具体规则请参考API文档中的格式要求
		String roleSessionName = "alice-001";

		// 此处必须为 HTTPS
		ProtocolType protocolType = ProtocolType.HTTPS;
		
		
        // 生成回调
//        String callbackBodyType = "application/json";
//        String callback_param = "{"
//                + "                \"callbackUrl\":\""
//                + callbackUrl
//                + "\","
//                + "                    \"callbackBody\":\"{\\\"bucket\\\":${bucket},\\\"x:userid\\\":${x:userid},\\\"x:filename\\\":${x:filename},\\\"x:uploadtype\\\":${x:uploadtype},\\\"x:uploadid\\\":${x:uploadid},\\\"object\\\":${object},\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}\","
//                + "                    \"callbackBodyType\":\""
//                + callbackBodyType + "\"" + "            }";
//        byte[] binaryDatas = callback_param.getBytes("utf-8");
//
//        byte[] encodeBase64 = org.apache.commons.codec.binary.Base64
//                .encodeBase64(binaryDatas);
//        String callback = new String(encodeBase64);
        
//        String callback = callback_param;
		
		

		try {
			final AssumeRoleResponse stsResponse = assumeRole(accessKeyId, accessKeySecret, roleArn, roleSessionName,
					policy, protocolType, durationSeconds);
			
			Map<String, String> respMap = new LinkedHashMap<String, String>();
			respMap.put("status", "200");
            respMap.put("AccessKeyId", stsResponse.getCredentials().getAccessKeyId());
            respMap.put("AccessKeySecret", stsResponse.getCredentials().getAccessKeySecret());        
            respMap.put("SecurityToken", stsResponse.getCredentials().getSecurityToken());
            respMap.put("Expiration", stsResponse.getCredentials().getExpiration());
            respMap.put("bucketName", bucketName);
            respMap.put("host", host);
            respMap.put("endpoint", endpoint);
            respMap.put("callback", callbackUrl);
            
            response(request, response, JSON.toJSONString(respMap));
            
		} catch (ClientException e) {
			Map<String, String> respMap = new LinkedHashMap<String, String>();
			respMap.put("status", e.getErrCode());
            respMap.put("AccessKeyId", "");
            respMap.put("AccessKeySecret", "");
            respMap.put("SecurityToken", "");
            respMap.put("Expiration", "");    
            respMap.put("bucketName", "");
            respMap.put("host", "");
            respMap.put("endpoint", "");
            respMap.put("callback", "");
            response(request, response, JSON.toJSONString(respMap));
		}
	}
	
	public static void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
		String callbackFunName = request.getParameter("callback");
		if (callbackFunName==null || callbackFunName.equalsIgnoreCase(""))
			response.getWriter().println(results);
		else
			response.getWriter().println(callbackFunName + "( "+results+" )");
		response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
	}
	
	
	public static boolean doCheck(String content, byte[] sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes());
			boolean bverify = signature.verify(sign);
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	@SuppressWarnings("finally")
	public static String executeGet(String url) {
		BufferedReader in = null;

		String content = null;
		try {
			// 定义HttpClient
			@SuppressWarnings("resource")
			DefaultHttpClient client = new DefaultHttpClient();
			// 实例化HTTP方法
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			content = sb.toString();
		} catch (Exception e) {
		} finally {
			if (in != null) {
				try {
					in.close();// 最后要关闭BufferedReader
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return content;
		}
	}
	
	
	public static boolean VerifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody) throws NumberFormatException, IOException
	{
		boolean ret = false;	
		String autorizationInput = new String(request.getHeader("Authorization"));
		String pubKeyInput = request.getHeader("x-oss-pub-key-url");
		byte[] authorization = BinaryUtil.fromBase64String(autorizationInput);
		byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput);
		String pubKeyAddr = new String(pubKey);
		if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/") && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/"))
		{
			System.out.println("pub key addr must be oss addrss");
			return false;
		}
		String retString = executeGet(pubKeyAddr);
		if(StringUtil.isBlank(retString)){
			return false;
		}
		
		retString = retString.replace("-----BEGIN PUBLIC KEY-----", "");
		retString = retString.replace("-----END PUBLIC KEY-----", "");
		String queryString = request.getQueryString();
		String uri = request.getRequestURI();
		String decodeUri = java.net.URLDecoder.decode(uri, "UTF-8");
		String authStr = decodeUri;
		if (queryString != null && !queryString.equals("")) {
			authStr += "?" + queryString;
		}
		authStr += "\n" + ossCallbackBody;
		ret = doCheck(authStr, authorization, retString);
		return ret;
	}
	
	public static String GetPostBody(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					if (readLengthThisTime == -1) {// Should not happen.
						break;
					}
					readLen += readLengthThisTime;
				}
				return new String(message);
			} catch (IOException e) {
			}
		}
		return "";
	}
	
	public static void response(HttpServletRequest request, HttpServletResponse response, String results, int status) throws IOException {
		String callbackFunName = request.getParameter("callback");
		response.addHeader("Content-Length", String.valueOf(results.length()));
		if (callbackFunName == null || callbackFunName.equalsIgnoreCase(""))
			response.getWriter().println(results);
		else
			response.getWriter().println(callbackFunName + "( " + results + " )");
		response.setStatus(status);
		response.flushBuffer();
	}
	
	public static void main(String[] args) {
        String bucketName = PropertiesUtils.getStringValue("oss.sts.bucketName");
        System.out.println(bucketName);
		ClassLoader classLoader = AliyunAppOssUtil.class.getClassLoader();
		URL url = classLoader.getResource(PropertiesUtils.getStringValue("oss.sts.PolicyFile"));
		String policy = ReadJson(url.getFile());
		policy=policy.replace("${bucketName}", bucketName);
        System.out.println(policy);
	}
	
	
	
	

}
