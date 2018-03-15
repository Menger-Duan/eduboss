package com.eduboss.controller;

import com.eduboss.domainVo.YeePayInfoVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.YeePayInfoService;
import com.eduboss.utils.CertUtil;
import net.sf.json.JSONObject;
import org.directwebremoting.guice.RequestParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value="/YeePayController")
@Controller
public class YeePayController {

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
	public Map findMoneyReadyToPayById(@RequestBody Map requestMap, HttpServletRequest request){
			return yeePayInfoService.findByIdAndSignMsg(requestMap,request);
	}

	@RequestMapping(value = "/saveResultNotify", method =  RequestMethod.POST)
	@ResponseBody
	public Map<String,String> saveResultNotify(@RequestBody Map requestMap, HttpServletRequest request) {
		Map body = JSONObject.fromObject(requestMap.get("msg_body"));
		if(body!=null && body.get("tran_cd")!=null && body.get("tran_cd").equals("5201")){
			return yeePayInfoService.findByIdAndSignMsg(requestMap,request);
		}else if(body!=null && body.get("tran_cd")!=null && body.get("tran_cd").equals("6201")){
			return yeePayInfoService.saveResultNotify(requestMap,request);
		}
		return null;
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
