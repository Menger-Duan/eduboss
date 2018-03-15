package com.eduboss.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.IntenetPayVo;
import com.eduboss.dto.Response;
import com.eduboss.intenetpay.IntenetPayResponseParam;
import com.eduboss.service.IntenetPayService;
import com.eduboss.utils.PropertiesUtils;

import sun.rmi.runtime.Log;

/**
 * @author yao
 * 网络支付
 *
 */


@Controller
@RequestMapping(value="/IntenetPayController")
public class IntenetPayController {


	Logger logger = Logger.getLogger(IntenetPayController.class);
	@Autowired
	private IntenetPayService intenetPayService;
	
	@ResponseBody
	@RequestMapping(value="/sendPayDetailInfo")
	public Map<String,String> sendPayDetailInfo(IntenetPayVo intenetPayVo,HttpServletRequest request) {
		//StringBuffer url =request.getRequestURL();
		logger.info("######################################"+PropertiesUtils.getStringValue("requestURL_address"));
		//String projectUrl=url.substring(0,url.indexOf("eduboss")-1)+ request.getContextPath();
		String projectUrl = PropertiesUtils.getStringValue("requestURL_address") + request.getContextPath();
		logger.info("######################################"+projectUrl);
//		String strBackUrl = "https://" + request.getServerName() //服务器地址
//                + ":"
//                + request.getServerPort()           //端口号
//                + request.getContextPath() ;     //项目名称
		intenetPayVo.setUrl(projectUrl);
		return intenetPayService.sendPayDetailInfo(intenetPayVo);
		
	}

	@ResponseBody
	@RequestMapping(value="/sendPayInfoForOtherPlat",method = RequestMethod.POST)
	public Map<String,String> sendPayInfoForOtherPlat(IntenetPayVo intenetPayVo) {
		logger.info("标题"+intenetPayVo.getTitle()+"访问支付接口######################################"+intenetPayVo.getUrl()+",金额："+intenetPayVo.getAmount());
		return intenetPayService.sendPayInfoForOtherPlat(intenetPayVo);
	}

	@ResponseBody
	@RequestMapping(value="/getPayStatusByTrxid",method = RequestMethod.POST)
	public Map<String,Object> getPayStatusByTrxid(IntenetPayVo intenetPayVo) {
		logger.info("trxid:"+intenetPayVo.getTrxid()+"查询状态###########："+intenetPayVo.getReqsn()+"帐号信息："+intenetPayVo.getCusId()+"-"+intenetPayVo.getAppId()+"-"+intenetPayVo.getAppKey());
		return intenetPayService.getPayStatusByTrxid(intenetPayVo);
	}
	

	@ResponseBody
	@RequestMapping(value="/findPayInfo")
	public Map<String,String> findPayInfo(String trxId,String reqsn) {
		return intenetPayService.commitPayInfo(trxId,reqsn);
	}

	/**
	 * 通联支付成功回调方法
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/noticePayStatus", method =  RequestMethod.POST)
	@ResponseBody
	public Response noticePayStatus(@ModelAttribute IntenetPayResponseParam param ) {
		Response res=new Response();
		res= intenetPayService.noticePayStatus(param);
		return res; 
	}
	
	
	/**
	 * 用户支付完成了，但是系统没有反馈时，收款人可以主观判断是支付完成，生成收款记录。
	 * @param trxId
	 * @return
	 */
	@RequestMapping(value="/confirmPaid", method =  RequestMethod.POST)
	@ResponseBody
	public Map<String,String> confirmPaid(String trxId) {
		return intenetPayService.confirmPaid(trxId); 
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value="/findPayInfoByFundId")
	public IntenetPayVo findPayInfoByFundId(String fundsId) {
		return intenetPayService.findPayInfoByFundId(fundsId);
	}
	
	/**
	 * 刷新订单状态
	 * @param trxId
	 * @return
	 */
	@RequestMapping(value="/reflushPayStatus", method =  RequestMethod.POST)
	@ResponseBody
	public Map<String,String> reflushPayStatus(String trxId,String reqsn) {
		return intenetPayService.updatePayStatus(trxId,reqsn);
	}

	/**
	 * 反扫如果超时手动确认。
	 * @param intenetPayVo
	 * @return
	 */
	@RequestMapping(value="/saveBarCodeInfo", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveBarCodeInfo(IntenetPayVo intenetPayVo){
		return intenetPayService.saveBarCodeInfo(intenetPayVo);
	}
}
