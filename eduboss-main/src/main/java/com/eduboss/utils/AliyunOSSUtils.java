package com.eduboss.utils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.eduboss.common.MiniClassAttendanceStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Aliyun oss 工具类
 * see : http://docs.aliyun.com/?spm=5176.383663.9.5.4Hdg6q#/oss/sdk/java-sdk&preface
 * oss-cn-shenzhen
 * 深圳节点外网地址： oss-cn-shenzhen.aliyuncs.com
 * 深圳节点内网地址： oss-cn-shenzhen-internal.aliyuncs.com
 * bucket地址：{bucketName}.oss-cn-shenzhen.aliyuncs.com/
 * Created by xuwen on 2015/3/19.
 */
public class AliyunOSSUtils {

    private static Logger logger = Logger.getLogger(AliyunOSSUtils.class);

    private static OSSClient client = null;

    private static final String key = PropertiesUtils.getStringValue("oss.access.key.id");
    private static final String secret = PropertiesUtils.getStringValue("oss.access.key.secret");
    private static final String endpoint = PropertiesUtils.getStringValue("oss.access.endpoint");
    private static final String bucketName = PropertiesUtils.getStringValue("oss.access.bucketName");

    private static final String host = PropertiesUtils
            .getStringValue("oss.access.host");
    private static final String sseHost = PropertiesUtils
            .getStringValue("oss.access.url.prefix");
    private static final String callbackUrl = PropertiesUtils
            .getStringValue("oss.access.callbackUrl");

    /**
     * 获取client
     * @return
     */
    public static OSSClient getClient(){
        if(client == null){
            // client 配置，参见 http://docs.aliyun.com/?spm=5176.383663.9.5.4Hdg6q#/oss/sdk/java-sdk&ossclient
            ClientConfiguration conf = new ClientConfiguration();
            conf.setMaxConnections(10);
            conf.setConnectionTimeout(5000);
            conf.setMaxErrorRetry(3);
            conf.setSocketTimeout(2000);
            client = new OSSClient(endpoint, key, secret);
            logger.debug("Aliyun OSS 客户端已创建");
        }
        return client;
    }

    /**
     * 上传文件至阿里云
     * @param key 文件标识
     * @param is 文件流
     * @param length 文件长度
     */
    public static void put(String key, InputStream is,long length){
        OSSClient client = getClient();
        // 创建上传对象的元数据，必须设置ContentLength
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(length);
        // 上传
        PutObjectResult result = client.putObject(bucketName, key, is, meta);
        logger.info("OSS上传完成，" + "key is " + key + "，ContentLength is " + length + "，eTag is " + result.getETag());
    }




    /**
     * 获取OSS直传key
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public static void getKey(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        getKey(request, response, host);
    }
    
    /**
     * 获取OSS直传key
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public static void getSseKey(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        getKey(request, response, sseHost);
    }
    
    /**
     * 获取OSS直传key
     *
     * @param request
     * @param response
     * @throws Exception
     */
    private static void getKey(HttpServletRequest request,
                              HttpServletResponse response, String hostParam) throws Exception {
        try {
            client = getClient();
            long expireTime = 3000;// 过期时间30秒
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            java.sql.Date expiration = new java.sql.Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(
                    PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);// 最大文件1G
            policyConds.addConditionItem(MatchMode.StartWith,
                    PolicyConditions.COND_KEY, "");

            String postPolicy = client.generatePostPolicy(expiration,
                    policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            // 生成回调
            String callbackBodyType = "application/json";
            String callback_param = "{"
                    + "                \"callbackUrl\":\""
                    + callbackUrl
                    + "\","
                    + "                    \"callbackBody\":\"{\\\"bucket\\\":${bucket},\\\"x:realname\\\":${x:realname},\\\"x:userid\\\":${x:userid},\\\"x:uploadtype\\\":${x:uploadtype},\\\"x:uploadid\\\":${x:uploadid},\\\"object\\\":${object},\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}\","
                    + "                    \"callbackBodyType\":\""
                    + callbackBodyType + "\"" + "            }";
            byte[] binaryDatas = callback_param.getBytes("utf-8");

            byte[] encodeBase64 = org.apache.commons.codec.binary.Base64
                    .encodeBase64(binaryDatas);
            String callback = new String(encodeBase64);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("OSSAccessKeyId", key);
            respMap.put("policy", encodedPolicy);
            respMap.put("Signature", postSignature);
            respMap.put("host", hostParam);
            respMap.put("Expires", String.valueOf(expireEndTime));
            respMap.put("callback", callback);
            net.sf.json.JSONObject ja1 = net.sf.json.JSONObject
                    .fromObject(respMap);
            System.out.println(ja1.toString());
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response, ja1.toString());

        } catch (Exception e) {
            e.getMessage();
            throw new RuntimeException("秘钥生成失败!");
        }
    } 

    /**
     * 私有工具方法
     *
     * @param request
     * @param response
     * @param results
     * @throws IOException
     */
    private static void response(HttpServletRequest request,
                                 HttpServletResponse response, String results) throws IOException {
        String callbackFunName = request.getParameter("callback");
        response.addHeader("Content-Length", String.valueOf(results.length()));
        if (callbackFunName == null || callbackFunName.equalsIgnoreCase(""))
            response.getWriter().println(results);
        else {
            response.getWriter().println(results);
            response.getWriter().println(
                    callbackFunName + "( " + results + " )");
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    /**
     * 解析阿里回调服务器的接口数据
     * @param is
     * @param contentLen
     * @return
     */
    public static String getPostBody(InputStream is, int contentLen) {
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
                e.printStackTrace();
                throw new RuntimeException("解析阿里数据出错!");
            }
        }
        return "";
    }

    /**
     * 验证回调是否是阿里发送的
     * @param request
     * @param ossCallbackBody
     * @return
     * @throws NumberFormatException
     * @throws IOException
     */
    public static boolean verifyOSSCallBackRequest(HttpServletRequest request, String ossCallbackBody) throws NumberFormatException, IOException
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
        logger.error("OSS verifyOSSCallBackRequest time："+DateTools.getCurrentDateTime()+": "+authStr);
        ret = doCheck(authStr, authorization, retString);
        return ret;
    }

    /**
     * 发送get请求
     * @param url
     * @return
     */
    @SuppressWarnings({ "finally" })
    public static String executeGet(String url) {
        BufferedReader in = null;

        String content = null;
        try {
            // 定义HttpClient
            @SuppressWarnings("resource")
            DefaultHttpClient httpClient = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = httpClient.execute(request);

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

    /**
     * 上传文件至阿里云
     * @param key 文件标识
     * @param file 文件句柄引用
     * @throws FileNotFoundException
     */
    public static void put(String key, File file) throws FileNotFoundException{
       put(key,new FileInputStream((file)),file.length());
    }

    /**
     * 上传文件至阿里云
     * @param key 文件标识
     * @param filePath 文件本地路径
     * @throws FileNotFoundException
     */
    public static void put(String key, String filePath) throws FileNotFoundException{
        put(key,new File(filePath));
    }

    /**
     * 从阿里云获取文件流
     * @param key
     * @return
     */
    public static InputStream get(String key){
        return getClient().getObject(bucketName,key).getObjectContent();
    }

    /**
     * 从阿里云删除文件
     * @param key 文件标识
     */
    public static void remove(String key){
        getClient().deleteObject(bucketName,key);
        logger.info("OSS删除完成，key is " + key);
    }

    public static void main(String[] args) throws Exception{
    	put("MINI_CLASS_ATTEND_PIC_MIN0000000008.jpg", new File("C:\\Users\\Administrator\\Desktop\\wawa.jpg"));
    	System.out.println(MiniClassStudentChargeStatus.valueOf("UNCHARGE"));
    	
    }
    
    
    
    
    

}
