package com.eduboss.intenetpay;

import java.util.Map;
import java.util.TreeMap;

import com.eduboss.exception.ApplicationException;
import com.eduboss.utils.PropertiesUtils;

public class SybPayService {
	public Map<String,String> pay(long trxamt,String reqsn,String paytype,String body,String remark,String acct,String authcode,String notify_url,String limit_pay) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/pay");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("validtime", SybConstants.VILIDTIME);
		params.put("version", "11");
		params.put("trxamt", String.valueOf(trxamt));
		params.put("reqsn", reqsn);
		params.put("paytype", paytype);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("body", body);
		params.put("remark", remark);
		params.put("acct", acct);
		params.put("authcode", authcode);
		params.put("notify_url", notify_url);
		params.put("limit_pay", limit_pay);
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		return map;
		
	}
	
	
	/**
	 * 自定义支付
	 * @param trxamt
	 * @param reqsn
	 * @param paytype
	 * @param body
	 * @param remark
	 * @param acct
	 * @param authcode
	 * @param notify_url
	 * @param limit_pay
	 * @param sybCusId
	 * @param sybAppId
	 * @param sybAppKey
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> pay(long trxamt,String reqsn,String paytype,String body,String remark,String acct,String authcode,String notify_url,String limit_pay
			,String sybCusId,String sybAppId,String sybAppKey) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(PropertiesUtils.getStringValue("intenet.syb.url")+"/pay");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", sybCusId);
		params.put("appid", sybAppId);
		params.put("validtime", SybConstants.VILIDTIME);
		params.put("version", "11");
		params.put("trxamt", String.valueOf(trxamt));
		params.put("reqsn", reqsn);
		params.put("paytype", paytype);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("body", body);
		params.put("remark", remark);
		params.put("acct", acct);
		params.put("authcode", authcode);
		params.put("notify_url", notify_url);
		params.put("limit_pay", limit_pay);
		params.put("sign", SybUtil.sign(params,sybAppKey));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result,sybAppKey);
		return map;
		
	}
	
	public Map<String,String> cancel(long trxamt,String reqsn,String oldtrxid,String oldreqsn) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/cancel");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("trxamt", String.valueOf(trxamt));
		params.put("reqsn", reqsn);
		params.put("oldtrxid", oldtrxid);
		params.put("oldreqsn", oldreqsn);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		return map;
	}
	
	public Map<String,String> refund(long trxamt,String reqsn,String oldtrxid,String oldreqsn) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/refund");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("trxamt", String.valueOf(trxamt));
		params.put("reqsn", reqsn);
		params.put("oldreqsn", oldreqsn);
		params.put("oldtrxid", oldtrxid);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		return map;
	}
	
	public Map<String,String> query(String reqsn,String trxid) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(SybConstants.SYB_APIURL+"/query");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", SybConstants.SYB_CUSID);
		params.put("appid", SybConstants.SYB_APPID);
		params.put("version", "11");
		params.put("reqsn", reqsn);
		params.put("trxid", trxid);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("sign", SybUtil.sign(params,SybConstants.SYB_APPKEY));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result);
		return map;
	}
	
	
	/**
	 * 自定义查询
	 * @param reqsn
	 * @param trxid
	 * @param sybCusId
	 * @param sybAppId
	 * @param sybAppKey
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> query(String reqsn,String trxid,String sybCusId,String sybAppId,String sybAppKey) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(PropertiesUtils.getStringValue("intenet.syb.url")+"/query");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", sybCusId);
		params.put("appid", sybAppId);
		params.put("version", "11");
		params.put("reqsn", reqsn);
		params.put("trxid", trxid);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("sign", SybUtil.sign(params,sybAppKey));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result,sybAppKey);
		return map;
	}
	
	public static Map<String,String> handleResult(String result) throws Exception{
		Map map = SybUtil.json2Obj(result, Map.class);
		if(map == null){
			throw new ApplicationException("返回数据错误");
		}
		if("SUCCESS".equals(map.get("retcode"))){
			TreeMap tmap = new TreeMap();
			tmap.putAll(map);
			String sign = tmap.remove("sign").toString();
			String sign1 = SybUtil.sign(tmap,SybConstants.SYB_APPKEY);
			if(sign1.toLowerCase().equals(sign.toLowerCase())){
				return map;
			}else{
				throw new ApplicationException("验证签名失败");
			}
			
		}else{
			throw new ApplicationException(map.get("retmsg").toString());
		}
	}
	
	/**
	 * 自定义结果遍历
	 * @param result
	 * @param appKey
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> handleResult(String result,String appKey) throws Exception{
		Map map = SybUtil.json2Obj(result, Map.class);
		if(map == null){
			throw new ApplicationException("返回数据错误");
		}
		if("SUCCESS".equals(map.get("retcode"))){
			TreeMap tmap = new TreeMap();
			tmap.putAll(map);
			String sign = tmap.remove("sign").toString();
			String sign1 = SybUtil.sign(tmap,appKey);
			if(sign1.toLowerCase().equals(sign.toLowerCase())){
				return map;
			}else{
				throw new ApplicationException("验证签名失败");
			}
			
		}else{
			throw new ApplicationException(map.get("retmsg").toString());
		}
	}
	
	/**
	 * 直播自定义支付
	 * @param trxamt
	 * @param reqsn
	 * @param paytype
	 * @param body
	 * @param remark
	 * @param acct
	 * @param authcode
	 * @param notify_url
	 * @param limit_pay
	 * @param sybCusId
	 * @param sybAppId
	 * @param sybAppKey
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> livePay(long trxamt,String reqsn,String paytype,String body,String remark,String validtime,String notify_url,String limit_pay
			,String sybCusId,String sybAppId,String sybAppKey) throws Exception{
		HttpConnectionUtil http = new HttpConnectionUtil(PropertiesUtils.getStringValue("intenet.syb.url")+"/pay");
		http.init();
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("cusid", sybCusId);
		params.put("appid", sybAppId);
		params.put("version", "11");
		params.put("trxamt", String.valueOf(trxamt));
		params.put("reqsn", reqsn);
		params.put("paytype", paytype);
		params.put("randomstr", SybUtil.getValidatecode(8));
		params.put("body", body);
		params.put("remark", remark);
		params.put("validtime", validtime);
		params.put("notify_url", notify_url);
		params.put("limit_pay", limit_pay);
		params.put("sign", SybUtil.sign(params,sybAppKey));
		byte[] bys = http.postParams(params, true);
		String result = new String(bys,"UTF-8");
		Map<String,String> map = handleResult(result,sybAppKey);
		return map;
		
	}
	
}
