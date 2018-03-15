package com.eduboss.controller;

import com.eduboss.bill.CerEncode;
import com.eduboss.bill.Pkipair;
import com.eduboss.domain.YeePayInfo;
import com.eduboss.domainVo.YeePayInfoVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.YeePayInfoService;
import com.eduboss.utils.StringUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.directwebremoting.guice.RequestParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value="/BillPayController")
@Controller
public class BillPayController {

	private static Logger logger= Logger.getLogger(BillPayController.class);

	@Autowired
	private YeePayInfoService yeePayInfoService;


	/**	 * 保存POS机支付信息返回信息ID供生成二维码
	 * @return
	 */
	@RequestMapping(value = "/saveMoneyReadyToPay", method =  RequestMethod.GET)
	@ResponseBody
	public String saveMoneyReadyToPay(@ModelAttribute YeePayInfoVo mrtVo) {
		String pk=yeePayInfoService.saveYeePayInfo(mrtVo);
		return pk;
	}

	@RequestMapping(value = "/findMoneyReadyToPayById", method =  RequestMethod.GET)
	@ResponseBody
	public YeePayInfoVo findMoneyReadyToPayById(@RequestParam String payNo){
		return yeePayInfoService.findById(payNo);
	}


	@RequestMapping(value = "/getPayInfo", method =  RequestMethod.POST)
	@ResponseBody
	public void findMoneyReadyToPayById(HttpServletRequest request,HttpServletResponse response){
		String respTime =  new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

		String orderId = request.getParameter("orderId"); // 订单编号
		String reqTime = request.getParameter("reqTime");  //请求时间
		String merchantId = request.getParameter("merchantId"); //商户ID
		String terminalId = request.getParameter("terminalId"); //终端号
		String ext1 = request.getParameter("ext1");
		String ext2 = request.getParameter("ext2");
		String MAC = request.getParameter("MAC");

		String mscVal = "";
		mscVal = appendParam(mscVal,"orderId",orderId);
		mscVal = appendParam(mscVal,"reqTime",reqTime);
		mscVal = appendParam(mscVal,"ext1",ext1);
		mscVal = appendParam(mscVal,"ext2",ext2);
		CerEncode cer = new CerEncode();
		boolean flag = cer.enCodeByCer(mscVal,MAC);

		String responseCode = "";
		BigDecimal amount=BigDecimal.ZERO;
		String contractId="";
		String studentName="";
		if (flag) {
			YeePayInfoVo yeePayInfo=yeePayInfoService.findById(orderId);
			amount=yeePayInfo.getMoney();
			contractId=yeePayInfo.getContractId();
			studentName=yeePayInfo.getStudentName();
			responseCode = "00";
		} else {
			responseCode = "56";
		}

		String xmlString =appendXml(reqTime,respTime,responseCode,orderId,merchantId,amount,contractId,studentName);
		response.setCharacterEncoding("utf-8");
		Pkipair pki = new Pkipair();
		String macPki = pki.signMsg(xmlString);
		//windows: macPki.replaceAll("\r\n","") ; linux:macPki.replaceAll("\n","")
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<ResponseMessage>" + "<MAC>"
				+ macPki.replaceAll("\n","")
				+ "</MAC>"
				+ xmlString
				+ "</ResponseMessage>";
		logger.info(xml);
		try {
			BufferedWriter bw = new BufferedWriter(response.getWriter());
			bw.write(xml);
			bw.flush();
			bw.close();
		} catch (IOException e){
			logger.error("回填response报错："+e.getMessage());
		}


	}

	public String appendXml(String reqTime, String respTime, String responseCode, String orderId, String merchantId, BigDecimal amount,String contractId,String studentName){
		StringBuilder returnXmlContent=new StringBuilder();
		returnXmlContent.append("<MessageContent>");
		returnXmlContent.append("<reqTime>"+reqTime+"</reqTime>");
		returnXmlContent.append("<respTime>"+respTime+"</respTime>");
		returnXmlContent.append("<responseCode>"+responseCode+"</responseCode> ");
		returnXmlContent.append("<message>");
		returnXmlContent.append("<orderId>"+orderId+"</orderId>");
		returnXmlContent.append("<merchantId>"+merchantId+"</merchantId>");
		returnXmlContent.append("<merchantName>spark</merchantName>");
		returnXmlContent.append("<amt>"+amount+"</amt>");
		returnXmlContent.append("<amt2></amt2>");
		returnXmlContent.append("<amt3></amt3>");
		returnXmlContent.append("<amt4></amt4>");
		returnXmlContent.append("<ext>");
		returnXmlContent.append("<userdata1>");
		returnXmlContent.append("<value>"+contractId+"</value>");
		returnXmlContent.append("<chnName>合同ID</chnName>");
		returnXmlContent.append("</userdata1>");
		returnXmlContent.append("<userdata2>");
		returnXmlContent.append("<value>"+studentName+"</value>");
		returnXmlContent.append("<chnName>学生名字</chnName>");
		returnXmlContent.append("</userdata2>");
		returnXmlContent.append("</ext>");
		returnXmlContent.append("<desc></desc>");
		returnXmlContent.append("</message> ");
		returnXmlContent.append("</MessageContent>");
		return returnXmlContent.toString();
	}

	public String appendParam(String returnStr, String paramId,String paramValue)
	{
		if (!returnStr.equals("")) {
			if (!paramValue.equals("")&& null != paramValue)
			{
				returnStr = returnStr + paramId + "=" + paramValue;
			}
		}
		else
		{
			if (!paramValue.equals(""))
			{
				returnStr = paramId + "=" + paramValue;
			}
		}
		return returnStr;
	}

	@RequestMapping(value = "/saveResultNotify", method =  RequestMethod.POST)
	@ResponseBody
	public void saveResultNotify(HttpServletRequest request, HttpServletResponse response) {

		String responseMessage ="";
		try {
			response.setCharacterEncoding("gbk");
			responseMessage = new String(request.getParameter("responseMessage").getBytes("utf-8"),"utf-8"); // 交易返回信息
			logger.info("1:"+responseMessage);
			responseMessage = new String(request.getParameter("responseMessage").getBytes("GBK"),"GBK"); // 交易返回信息
			logger.info("3:"+responseMessage);
		} catch (UnsupportedEncodingException e) {
		}
		Map map =new HashMap();
		String processFlag = request.getParameter("processFlag"); //处理结果 0：成功 1：失败
		String txnType = request.getParameter("txnType");         //交易类型
		String orgTxnType = request.getParameter("orgTxnType");    //原始交易类
		String amt = request.getParameter("amt");                  //交易金额
		String externalTraceNo = request.getParameter("externalTraceNo"); //外部跟踪编号
		String orgExternalTraceNo = request.getParameter("orgExternalTraceNo"); //原始外部跟踪号
		String terminalOperId = request.getParameter("terminalOperId");  //操作员编号
		String terminalId = request.getParameter("terminalId");          //终端编号
		String authCode = request.getParameter("authCode");             //授权码
		String RRN = request.getParameter("RRN");                        //系统参考号
		String txnTime = request.getParameter("txnTime");    //交易时间
		String shortPAN = request.getParameter("shortPAN");  //缩略卡号


		String responseCode  = request.getParameter("responseCode"); // 交易返回码
		String cardType = request.getParameter("cardType") ; // 卡类型
		String issuerId = request.getParameter("issuerId"); //发卡机构
		String issuerIdView = request.getParameter("issuerIdView"); // 发卡机构名称 issuerIdView
		String merchantId = request.getParameter("merchantId"); // 商户编号
		String signature =  request.getParameter("signature");
		String val = processFlag + txnType + amt + externalTraceNo
				+ terminalOperId + authCode + RRN + txnTime + shortPAN
				+ responseCode + cardType + issuerId;

		CerEncode ce = new CerEncode();
		boolean flag = ce.enCodeByCer(val,signature);
		map.put("txnTime",txnTime);
		map.put("externalTraceNo",externalTraceNo);
		map.put("processFlag",processFlag);
		map.put("amt",amt);
		map.put("RRN",RRN);
		map.put("terminalId",terminalId);
		map.put("responseMessage",responseMessage);
		map.put("merchantId",merchantId);
		map.put("shortPAN",shortPAN);
		map.put("processFlag",processFlag);
		if (flag)
		{
			logger.info("验证OK");
			Response res=yeePayInfoService.saveResultNotify(map);
			try {
				BufferedWriter bw = new BufferedWriter(response.getWriter());
				if(res.getResultCode()==0) {
					bw.write("0");
				}else{
					bw.write(res.getResultCode()+"-:"+res.getResultMessage());
				}
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("出现异常",e);
			}
		}
		else
		{
			throw new ApplicationException("验签失败了");
		}


	}

	@RequestMapping(value = "/reflushYeePayStatus", method =  RequestMethod.POST)
	@ResponseBody
	public Response reflushYeePayStatus(@RequestParameters String fundsId) {
		return yeePayInfoService.reflushYeePayStatus(fundsId);
	}

	@RequestMapping(value = "/surePosPaid", method =  RequestMethod.POST)
	@ResponseBody
	public Map<String,String> updatePosPaid(@RequestParameters String payNo) {
		return yeePayInfoService.updatePosPaid(payNo);
	}
}
