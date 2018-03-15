package com.eduboss.utils;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 文档转换为在线预览工具类
 * @author yaoyuqi
 */
public class DocumentViewUtil {
	
	static final String TYPE0 = "0";
	static final String PDF_EXCEL = "14";
	static final String viewHost= PropertiesUtils.getStringValue("DOCUMENT_VIEW_URL");
	static final Logger log = Logger.getLogger(DocumentViewUtil.class);
	/**
	 * 把文档转换为在线预览链接
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getDocumentHtmlView(String url){
		JSONObject object = new JSONObject();
		object.put("downloadUrl",url);
		if(url.substring(url.length()-4).equals(".pdf")){
			object.put("convertType",PDF_EXCEL);
		}else{
			object.put("convertType",TYPE0);
		}
		JSONObject returnObject = HttpClientUtils.httpPost(viewHost,object);
		log.info(returnObject.toString());
		if(returnObject.getInt("result")==0){
			log.info("转换成功！路径是："+returnObject.getJSONArray("data").get(0).toString());
			return returnObject.getJSONArray("data").get(0).toString();
		}
		return null;
	}

	public static void main(String [] args) {
		String url ="https://xinghuo-eduboss-uat.oss-cn-shenzhen.aliyuncs.com/52ed35cb-829f-0346-33a3-74245013022a.pdf";
		log.info(url.substring(url.length()-4));
//		log.info(getDocumentHtmlView("https://xinghuo-eduboss-uat.oss-cn-shenzhen.aliyuncs.com/52ed35cb-829f-0346-33a3-74245013022a"));
	}
}
