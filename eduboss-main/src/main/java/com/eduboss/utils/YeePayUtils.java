package com.eduboss.utils;

import com.eduboss.domain.YeePayInfo;
import com.eduboss.intenetpay.HttpConnectionUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class YeePayUtils {
	private static Logger log= Logger.getLogger(YeePayInfo.class);

	public static String getYeePayStatus(String orderId) throws Exception {
		HttpConnectionUtil http = new HttpConnectionUtil(PropertiesUtils.getStringValue("YEE_PAY_URL"));
		http.init();
		Map<String,String> params = new HashMap<String,String>();
		params.put("version", "1.0.0");
		params.put("encoding", "UTF-8");
		params.put("sign_type", "01");
		Map sonMap = new HashMap();
		sonMap.put("prod_cd", "1130");
		sonMap.put("biz_cd", "0000028");
		sonMap.put("tran_cd", "6201");
		sonMap.put("reserver", "");
		Map order = new HashMap();
		order.put("order_id", orderId);
		sonMap.put("order_info", order);
		params.put("msg_body",JSONObject.fromObject(sonMap).toString());
		params.put("sign", RSASignature.sign(JSONObject.fromObject(sonMap).toString(),RSAEncrypt.loadPrivateKeyByFile(RSASignature.class.getResource("/cert/").getPath())));
		log.info(JSONObject.fromObject(params).toString());
		byte[] bys = http.postParams(JSONObject.fromObject(params).toString(), true);
		String result = new String(bys,"UTF-8");
		return result;
	}



}
