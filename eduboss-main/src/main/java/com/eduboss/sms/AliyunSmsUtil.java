package com.eduboss.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.eduboss.exception.ApplicationException;
import com.eduboss.utils.PropertiesUtils;

public class AliyunSmsUtil {
	
    private static Logger logger = LoggerFactory.getLogger(AliyunSmsUtil.class);
    
	public static final String SINGLE_SMS_URL = PropertiesUtils.getStringValue("SINGLE_SMS_URL");
	
	public static final String ACESS_KEY_ID = PropertiesUtils.getStringValue("ACESS_KEY_ID");
	
	public static final String ACCESS_SECRET = PropertiesUtils.getStringValue("ACCESS_SECRET");
	
	public static final String ALI_SMS_URL = PropertiesUtils.getStringValue("ALI_SMS_URL");
	
	public static final String SIGN_NAME = PropertiesUtils.getStringValue("SIGN_NAME");
	
	//产品域名
	public static final String ALI_SMS_ADDRESS = PropertiesUtils.getStringValue("ALI_SMS_ADDRESS");
	
	//产品名称:云通信短信API产品,开发者无需替换
    static final String PRODUCT = "Dysmsapi";
	
//	public static String singleSendSmsByHppt(String signName, String templateCode, String recNum, String paramString) {
//		String getURL = SINGLE_SMS_URL + "?&SignName=" + signName +"&TemplateCode=" + templateCode + "&RecNum=" + recNum + "&ParamString=" + paramString;
//		getURL += "&Format=JSON&Version=2016-09-27&Signature=Pc5WB8gokVn0xfeu%2FZV%2BiNM1dgI%3D&SignatureMethod=HMAC-SHA1";
//		getURL += "&SignatureNonce=e1b44502-6d13-4433-9493-69eeb068e955&SignatureVersion=1.0&AccessKeyId=key-test&Timestamp=2015-11-23T12:00:00Z";
//		String returnStr = "";
//		try {
//			URL getUrl = new URL(getURL);
//			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
//			connection.connect();
//			returnStr = connection.getResponseMessage();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return returnStr;
//	}
	
    /**
     * 弃用，改用新的sdk
     * @param signName
     * @param templateCode
     * @param recNum
     * @param paramString
     * @return
     */
    @Deprecated
	public static boolean singleSendSmsBySdk(String signName, String templateCode, String recNum, String paramString) {
		boolean successed = true;
        try {
	        IClientProfile profile = DefaultProfile.getProfile(ALI_SMS_ADDRESS, ACESS_KEY_ID, ACCESS_SECRET);
	        DefaultProfile.addEndpoint(ALI_SMS_ADDRESS, ALI_SMS_ADDRESS, "Sms",  ALI_SMS_URL);
	        IAcsClient client = new DefaultAcsClient(profile);
	        SingleSendSmsRequest request = new SingleSendSmsRequest();
	        request.setSignName(signName);//控制台创建的签名名称
	        request.setTemplateCode(templateCode);//控制台创建的模板CODE
	        request.setParamString(paramString);//短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。"
	        request.setRecNum(recNum);//接收号码
	        SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
	        httpResponse.getRequestId();
        } catch (ServerException e) {
        	successed = false;
            e.printStackTrace();
        }
        catch (ClientException e) {
        	successed =false;
            e.printStackTrace();
        }
        return successed;
    }
	
	public static boolean sendSms(String signName, String templateCode, String recNum, String paramString) {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(ALI_SMS_ADDRESS, ACESS_KEY_ID, ACCESS_SECRET);
        try {
            DefaultProfile.addEndpoint(ALI_SMS_ADDRESS, ALI_SMS_ADDRESS, PRODUCT, ALI_SMS_URL);
            IAcsClient acsClient = new DefaultAcsClient(profile);
    
            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(recNum);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(paramString);
    
            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
    
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
    //        request.setOutId("yourOutId");
    
            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            logger.info("短信接口返回的数据----------------");
            logger.info("Code=" + sendSmsResponse.getCode());
            logger.info("Message=" + sendSmsResponse.getMessage());
            logger.info("RequestId=" + sendSmsResponse.getRequestId());
            logger.info("BizId=" + sendSmsResponse.getBizId());
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("短信发送失败");
        }
        return false;
    }
	
	public static void main(String[] args) {
//		"{\"courseTime\":\"2017-01-20_10:00\",\"grade\":\"一年级\",\"subject\":\"英语\",\"studentName\":\"李未央\"}"
//		"{\"courseTime\":\"17-01-20 10:00\",\"grade\":\"一年级\",\"subject\":\"英语\",\"studentName\":\"李未央\"}"
//		"{\"account\":\"huangyan\",\"password\":\"789123abc\"}"
//		"{\"schedulerNames\":\"学生状态，学生一对一状态，\"}"
//		"{\"studentNames\":\"李未央，李小龙\"}"
//		"{\"loginAddress\":\"http://120.24.61.47/eduboss/\",\"account\":\"huangyan\",\"password\":\"789123abc\"}"
//		"{\"miniClassName\":\"2017春季初一语文冲刺班1班\",\"courseTime\":\"17-02-20 10:00\",\"studentName\":\"李未央\"}"
		//singleSendSmsBySdk("学邦技术", "SMS_75815039", "15820296719", "{\"verification\":\"123456\"}")
		System.out.println(singleSendSmsBySdk("学邦技术", "SMS_47915144", "13450214963", "{\"teacherName\":\"李老师\",\"account\":\"huangyan\",\"password\":\"789123abc\"}"));
	}
	
}
