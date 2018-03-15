package com.eduboss.bill;

import com.eduboss.utils.PropertiesUtils;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;


public class Pkipair {

	private static Logger logger = Logger.getLogger(Pkipair.class);
	
	public String signMsg( String signMsg) {
		logger.info("加密明码"+signMsg);
		String base64 = "";
		try {
			// 密钥仓库
			KeyStore ks = KeyStore.getInstance("PKCS12");
			String fStr = Pkipair.class.getResource("/cert/"+ PropertiesUtils.getStringValue("BILL_PAY_PK")+".pfx").toURI().getPath();
			// 读取密钥仓库
			FileInputStream ksfis = new FileInputStream(fStr);
			BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
			char[] keyPwd = "vpos123".toCharArray();
			ks.load(ksbufin, keyPwd);
			// 从密钥仓库得到私钥
			PrivateKey priK = (PrivateKey) ks.getKey(PropertiesUtils.getStringValue("BILL_PAY_PK"), keyPwd);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(priK);
			signature.update(signMsg.getBytes("UTF-8"));
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			base64 = encoder.encode(signature.sign());
			
		} catch(FileNotFoundException e){
			logger.error("文件找不到："+e);
		}catch (Exception ex) {
			logger.error("加密异常："+ex);
		}
		logger.info("加密后码"+base64);
		return base64;
	}
	public static void main(String[] args) {
		String stringXml = "<MessageContent><reqTime>20120926091951</reqTime><respTime>20120926091951</respTime><responseCode>00</responseCode><message><orderId>100200300</orderId><merchantId>812331445110001</merchantId><merchantName>徐海全</merchantName><amt>100.00</amt><amt2></amt2><amt3></amt3><amt4></amt4><ext><userdata1><value>6783708368651209240</value><chnName>订单号</chnName></userdata1><userdata2><value>李顺</value><chnName>收件人</chnName></userdata2><userdata3><value>武汉软件园</value><chnName>收件地址</chnName></userdata3><userdata4><value>2012-09-24 15:44:00</value><chnName>起飞时间</chnName></userdata4><userdata5><value>MU123</value><chnName>航班号</chnName></userdata5><userdata6><value>深圳-上海浦东</value><chnName>航程</chnName></userdata6></ext><desc></desc></message></MessageContent>";
		Pkipair pki = new Pkipair();
		String macPki = pki.signMsg(stringXml);
		System.out.println("macPKi==   "+macPki);
		System.out.println(macPki.replaceAll("\r\n",""));
	}
}
