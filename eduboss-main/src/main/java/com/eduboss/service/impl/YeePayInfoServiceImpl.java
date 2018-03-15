package com.eduboss.service.impl;

import com.eduboss.common.FundsChangeAuditStatus;
import com.eduboss.common.FundsChangeAuditType;
import com.eduboss.common.FundsChangeType;
import com.eduboss.common.PayWay;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.YeePayInfoDao;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.YeePayInfo;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.domainVo.YeePayInfoVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ContractService;
import com.eduboss.service.UserService;
import com.eduboss.service.YeePayInfoService;
import com.eduboss.utils.*;
import com.gnete.merchant.api.Crypt;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YeePayInfoServiceImpl implements YeePayInfoService {

	Logger log=Logger.getLogger(YeePayInfoServiceImpl.class);

	@Autowired
	private YeePayInfoDao yeePayInfoDao;
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContractService  contractService;
	
	@Autowired
	private FundsChangeHistoryDao  fundsChangeHistoryDao;
    

	@Override
	public DataPackage getYeePayInfo(YeePayInfo moneyReadyToPay,
			DataPackage dataPackage) {
		Map mapP=new HashMap();
		StringBuilder hql = new StringBuilder();
		hql.append(" from YeePayInfo where 1 = 1 ");
		dataPackage = yeePayInfoDao.findPageByHQL(hql.toString(), dataPackage,true,mapP);
		List<YeePayInfo> list = (List<YeePayInfo>) dataPackage.getDatas();
		List<YeePayInfoVo> voList = new ArrayList<YeePayInfoVo>();
		dataPackage.setDatas(voList);
		return dataPackage;
	}


	@Override
	public String saveYeePayInfo(YeePayInfoVo moneyReadyToPayVo) {
		Map mapP=new HashMap();
		String hql=" update YeePayInfo set status=2 where contract.id='"+moneyReadyToPayVo.getContractId()+"' and status is null";
		yeePayInfoDao.excuteHql(hql,mapP);
		YeePayInfo moneyReadyToPay = HibernateUtils.voObjectMapping(moneyReadyToPayVo, YeePayInfo.class);
		moneyReadyToPay.setCreateTime(DateTools.getCurrentDateTime());
		moneyReadyToPay.setCreateUser(userService.getCurrentLoginUser().getUserId());
		yeePayInfoDao.save(moneyReadyToPay);
		return moneyReadyToPay.getId();
	}
	
	@Override
	public YeePayInfoVo findById(String id) {
		YeePayInfo domain = yeePayInfoDao.findById(id);
		YeePayInfoVo moneyReadyToPay = new YeePayInfoVo();
		
		if(domain!=null){
			moneyReadyToPay=HibernateUtils.voObjectMapping(domain, YeePayInfoVo.class);
			moneyReadyToPay.setResultCode("0");
			moneyReadyToPay.setResultMsg("成功");
		}else{
			moneyReadyToPay.setResultCode("1");
			moneyReadyToPay.setResultMsg("没有查询到对应的订单");
		}
		
		return moneyReadyToPay;
	}

	@Override
	public Map findByIdAndSignMsg(Map<String,String> map, HttpServletRequest request) {
		Map orderInfo = new HashMap();
		Map<String,String> msgBody= new HashMap();
		try {
			if(map==null && map.get("msg_body")==null){
				throw new ApplicationException("参数为空！");
			}
			checkSign(map.get("msg_body"),map.get("sign"));//校验签名

			msgBody= JSONObject.fromObject(map.get("msg_body"));
			if(msgBody.get("order_info")==null) throw new ApplicationException("参数为空！");
			orderInfo = JSONObject.fromObject(msgBody.get("order_info"));
			YeePayInfo domain = yeePayInfoDao.findById(orderInfo.get("order_id").toString());
			if (domain != null) {
				if(domain.getStatus()!=null && (!"3".equals(domain.getStatus()) && !"0".equals(domain.getStatus()))){
					msgBody.put("resp_cd", "11");
					msgBody.put("resp_nm", "已经处理过的订单");
				}else {
					msgBody.put("resp_cd", "00");
					msgBody.put("resp_nm", "成功");
				}

				orderInfo.put("order_amt", domain.getMoney() != null ? domain.getMoney().toString() : "0");
				orderInfo.put("industry_name", domain.getContract().getStudent().getName());
			} else {
				msgBody.put("resp_cd", "11");
				msgBody.put("resp_nm", "没有查询到对应的订单");
			}
		} catch (ApplicationException e) {
			msgBody.put("resp_cd", "11");
			msgBody.put("resp_nm", e.getErrorMsg());
		} catch (Exception e) {
			msgBody.put("resp_cd", "11");
			msgBody.put("resp_nm", "系统异常");
		}

		msgBody.put("order_info",orderInfo.toString());
		map.put("msg_body", msgBody.toString());
		try{
			map.put("sign", RSASignature.sign(msgBody.toString(),RSAEncrypt.loadPrivateKeyByFile(RSASignature.class.getResource("/cert/").getPath())));
		} catch (Exception e) {
			msgBody.put("resp_cd", "11");
			msgBody.put("resp_nm", e.getMessage());
		}
		return map;
	}

	public YeePayInfo findYeePayInfoByFundsId(String fundsId){
		Map mapP=new HashMap();
		String hql ="from YeePayInfo where fundChargeId.id='"+fundsId+"'";
		return yeePayInfoDao.findOneByHQL(hql,mapP);
	}

	@Override
	public Response reflushYeePayStatus(String fundsId) {
		Response res=new Response();
		YeePayInfo yeePay=findYeePayInfoByFundsId(fundsId);
		if(yeePay==null){
			res.setResultMessage("该记录不是手工确认的Pos支付，不能刷新状态");
			res.setResultCode(-1);
			return res;
		}

		if(yeePay.getStatus()!=null && "00".equals(yeePay.getStatus())){
			return res;
		}

		String yeePayInfo="";
		try {
			yeePayInfo=YeePayUtils.getYeePayStatus(yeePay.getId());
		}catch (Exception e){
			res.setResultCode(-1);
			res.setResultMessage("访问易宝服务器报错！");
			return res;
		}

		if(yeePayInfo==null){
			throw new ApplicationException("参数为空！");
		}

		Map map= JSONObject.fromObject(yeePayInfo);
		if(map==null && map.get("msg_body")==null){
			throw new ApplicationException("参数为空！");
		}
		Map<String,String> msgBody= new HashMap();
		checkSign(map.get("msg_body"),map.get("sign"));//校验签名
		msgBody= JSONObject.fromObject(map.get("msg_body"));
		Map<String,String> orderInfo=JSONObject.fromObject(msgBody.get("order_info"));
		YeePayInfo domain = yeePayInfoDao.findById(orderInfo.get("order_id").toString());
		if(domain==null){
			res.setResultCode(-1);
			res.setResultMessage("找不到对应的支付信息！");
			return res;
		}
		if(msgBody.get("orig_resp_cd")!=null && msgBody.get("orig_resp_cd").equals("00")){
			FundsChangeHistory funds=domain.getFundChargeId();
			domain.setStatus(msgBody.get("orig_resp_cd"));
			domain.setResult(msgBody.get("orig_Resp_nm"));
			funds.setPOSid(msgBody.get("be_order_id").toString());
			funds.setPosNumber(msgBody.get("term_cd").toString());
			funds.setAuditStatus(FundsChangeAuditStatus.VALIDATE);
			funds.setAuditType(FundsChangeAuditType.SYSTEM);
			funds.setSystemAuditRemark("易宝Pos支付自动审批完成");
			funds.setAuditTime(DateTools.getCurrentDateTime());
			saveYeePayInfo(domain,msgBody,funds,orderInfo);
			fundsChangeHistoryDao.save(funds);
		}else if(msgBody.get("orig_resp_cd")!=null && msgBody.get("orig_resp_cd").equals("01")){
			res.setResultCode(-1);
			res.setResultMessage("未找到该订单信息！");
			return res;
		}else{
			domain.setStatus("3");
			yeePayInfoDao.save(domain);
			try{
				contractService.saveFundWashOfContract(domain.getFundChargeId().getId(),domain.getFundChargeId().getTransactionAmount());
			}catch(ApplicationException e){
				throw new ApplicationException("支付失败  :1.请先将该合同已分配的资金提取出来 2.然后进行“更新支付状态”操作（更新后系统会自动冲销） 3.请重新发起收款操作");
			}
		}
		yeePayInfoDao.commit();
		return res;
	}

	@Override
	public Map<String,String> updatePosPaid(String payNo) {
		Map<String,String>  returnMap = new HashMap<String,String>();

		returnMap.put("resultCode","0");
		YeePayInfo domain = yeePayInfoDao.findById(payNo);
		if(domain==null){
			returnMap.put("resultCode","-2");
			return returnMap;
		}
		if(domain.getFundChargeId()==null){
			domain.setStatus("0");
			domain.setModifyUser(userService.getCurrentLoginUser().getUserId());
			domain.setModifyTime(DateTools.getCurrentDateTime());

			FundsChangeHistoryVo fvo =new FundsChangeHistoryVo();
			fvo.setContractId(domain.getContract().getId());
			fvo.setTransactionAmount(domain.getMoney().doubleValue());//银联金额需要处理
			fvo.setChannel(PayWay.POS);
			fvo.setPosReceiptDate(DateTools.getCurrentDate());
			fvo.setTransactionTime(DateTools.getCurrentDate());
			fvo.setSystemAuditRemark("手动确认POS机支付");
			fvo.setAuditStatusValue(FundsChangeAuditStatus.UNVALIDATE.getValue());
			fvo.setAuditType(FundsChangeAuditType.SYSTEM);
			fvo.setChargeByWho(domain.getCreateUser());
			fvo.setFundsChangeType(FundsChangeType.HUMAN);//系统录入
			FundsChangeHistory fund=contractService.saveFundOfContract(fvo);
			domain.setFundChargeId(fund);
			yeePayInfoDao.save(domain);
			returnMap.put("fundId",fund.getId());
		}else if(domain.getStatus()!=null && !domain.getStatus().equals("00")){
			returnMap.put("resultCode","-1");
		}

		return returnMap;
	}

	@Override
	public Response saveResultNotify(Map map) {
			Response response=new Response();
			if(map.get("externalTraceNo")==null){
				response.setResultCode(-2);
				response.setResultMessage("externalTraceNo为空");
				return response;
			}
			if(map.get("processFlag")==null){
				response.setResultCode(-2);
				response.setResultMessage("processFlag为空");
				return response;
			}
			if(map.get("amt")==null){
				response.setResultCode(-2);
				response.setResultMessage("amt为空");
				return response;
			}
			if(map.get("RRN")==null){
				response.setResultCode(-2);
				response.setResultMessage("RRN为空");
				return response;
			}
			if(map.get("terminalId")==null){
				response.setResultCode(-2);
				response.setResultMessage("terminalId为空");
				return response;
			}
			if(map.get("merchantId")==null){
				response.setResultCode(-2);
				response.setResultMessage("merchantId为空");
				return response;
			}
			if(map.get("shortPAN")==null){
				response.setResultCode(-2);
				response.setResultMessage("shortPAN为空");
				return response;
			}
			if(map.get("externalTraceNo")==null){
				response.setResultCode(-2);
				response.setResultMessage("订单号为空");
			}else{
				YeePayInfo domain = yeePayInfoDao.findById(map.get("externalTraceNo").toString());
				if(domain!=null) {
					try {
						FundsChangeHistory fund = null;
						if (map.get("processFlag") != null && map.get("processFlag").toString().equals("0")) {
						    String transactionTime=DateTools.getCurrentDateTime();
						    if(map.get("txnTime")!=null){
                                transactionTime=DateTools.dateConversString(DateTools.stringConversDate(map.get("txnTime").toString(),"yyyyMMdd HHmmss"),"yyyy-MM-dd HH-mm-ss");
                            }
							FundsChangeHistoryVo fvo = new FundsChangeHistoryVo();
							fvo.setContractId(domain.getContract().getId());
							fvo.setTransactionAmount(Double.valueOf(map.get("amt").toString()));//银联金额需要处理
							fvo.setChannel(PayWay.POS);
							fvo.setPOSid(map.get("RRN").toString());
							fvo.setPosNumber(map.get("terminalId")==null?null:map.get("terminalId").toString());
							fvo.setTransactionTime(transactionTime);
							fvo.setRemark(map.get("responseMessage")==null?null:map.get("responseMessage").toString());
							fvo.setChargeByWho(domain.getCreateUser());
							fvo.setAuditStatusValue(FundsChangeAuditStatus.UNAUDIT.getValue());
							fvo.setCategory("BILL_PAY");
							fvo.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
							fvo.setPosReceiptDate(DateTools.getCurrentDate());
							fund = contractService.saveFundOfContract(fvo);
							domain.setBusCode(map.get("merchantId")==null?null:map.get("merchantId").toString());
							domain.setCardNo(map.get("shortPAN").toString());
							domain.setCounterFee(BigDecimal.ZERO);
							domain.setFundChargeId(fund);
							domain.setPayCode(map.get("RRN").toString());
							domain.setPosCode(map.get("terminalId").toString());
							domain.setRealMoney(new BigDecimal(map.get("amt").toString()));
							domain.setStatus(map.get("processFlag").toString());
							domain.setTransationTime(transactionTime);
							domain.setResult(map.get("responseMessage")==null?null:map.get("responseMessage").toString());
							domain.setModifyTime(DateTools.getCurrentDateTime());
							yeePayInfoDao.save(domain);
						}else if(map.get("processFlag") == null){
							response.setResultCode(-2);
							response.setResultMessage("processFlag异常");
						}else{
                            domain.setStatus(map.get("processFlag").toString());
                            domain.setFailReason(map.get("responseMessage")==null?null:map.get("responseMessage").toString());
                        }
					}catch (Exception e){
						e.printStackTrace();
						response.setResultCode(-3);
						response.setResultMessage("参数处理异常");
					}
				} else {
					response.setResultCode(-4);
					response.setResultMessage("找不到订单信息");
				}
			}
		return response;
	}

	@Override
	public YeePayInfoVo updateAndFindById(String orderId, String terminalId, String merchantId) {
		YeePayInfo domain = yeePayInfoDao.findById(orderId);

		if(StringUtils.isNotBlank(terminalId)){
			domain.setPosCode(terminalId);
		}

		if(StringUtils.isNotBlank(merchantId)){
			domain.setBusCode(merchantId);
		}

		yeePayInfoDao.save(domain);

		YeePayInfoVo moneyReadyToPay = new YeePayInfoVo();

		if(domain!=null){
			moneyReadyToPay=HibernateUtils.voObjectMapping(domain, YeePayInfoVo.class);
			moneyReadyToPay.setResultCode("0");
			moneyReadyToPay.setResultMsg("成功");
		}else{
			moneyReadyToPay.setResultCode("1");
			moneyReadyToPay.setResultMsg("没有查询到对应的订单");
		}

		return moneyReadyToPay;
	}

	public void checkSign(Object content,Object sign){
		String signstr= null;

		if (content != null && sign!=null) {
			try {
				signstr = RSASignature.sign(content.toString(), RSAEncrypt.loadPrivateKeyByFile(RSASignature.class.getResource("/cert/").getPath()));
				log.info(signstr);
				log.info(content.toString());
				if (!RSASignature.doCheck(content.toString(), sign.toString(), RSAEncrypt.loadPublicKeyByFile(RSASignature.class.getResource("/cert/").getPath()))) {
					throw new ApplicationException("验签失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException("验签异常！");
			}
		}else{
			throw new ApplicationException("参数异常！");
		}
	}
	
	@Override
	public Map<String,String> saveResultNotify(Map<String,String> map, HttpServletRequest request) {
		Map<String,String> msgBody= new HashMap();
		try {
			if(map==null && map.get("msg_body")==null){
				throw new ApplicationException("参数为空！");
			}
			checkSign(map.get("msg_body"),map.get("sign"));//校验签名
			msgBody= JSONObject.fromObject(map.get("msg_body"));
			if(msgBody.get("order_info")==null) throw new ApplicationException("参数为空！");
			Map<String,String> orderInfo=JSONObject.fromObject(msgBody.get("order_info"));
			YeePayInfo domain = yeePayInfoDao.findById(orderInfo.get("order_id").toString());
			if (domain != null) {
				if(domain.getStatus()!=null && "00".equals(domain.getStatus()) && domain.getFundChargeId()!=null){
					msgBody.put("resp_cd", "00");
					msgBody.put("resp_nm", "通知处理成功");
				}else if(domain.getStatus()!=null && (!"3".equals(domain.getStatus()) && !"0".equals(domain.getStatus()))){
					msgBody.put("resp_cd", "11");
					msgBody.put("resp_nm", "支付信息已经处理，请不要重复支付");
				}else if("3".equals(domain.getStatus())){// 用于在收款记录表里面修改支付方式处理逻辑
					FundsChangeHistory fund=null;
					if(msgBody.get("orig_resp_cd")!=null && msgBody.get("orig_resp_cd").equals("00")){
						if(domain.getFundChargeId()!=null){//如果支付成功就修改原来付款记录表的支付方式跟pos机ID
							fund=fundsChangeHistoryDao.findById(domain.getFundChargeId().getId());
							fund.setPOSid(msgBody.get("be_order_id").toString());
							fund.setPosNumber(msgBody.get("term_cd").toString());
							fund.setChannel(PayWay.POS);
							fund.setIsTurnPosPay("Y");
							fund.setTurnPosTime(DateTools.getCurrentDateTime());
							fund.setPosReceiptDate(DateTools.getCurrentDate());
							fund.setAuditStatus(FundsChangeAuditStatus.VALIDATE);
							fund.setAuditType(FundsChangeAuditType.SYSTEM);
							fund.setSystemAuditRemark("易宝POS支付自动审批完成");
							fund.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
							fundsChangeHistoryDao.save(fund);
							msgBody.put("resp_cd", "00");
							msgBody.put("resp_nm", "通知处理成功");
						}
					}
					//把支付信息保存的pos机支付流水表里面
					this.saveYeePayInfo(domain,msgBody, fund,orderInfo);

				}else if(domain.getFundChargeId()!=null){
					FundsChangeHistory funds=domain.getFundChargeId();
					domain.setStatus(msgBody.get("orig_resp_cd"));
					domain.setResult(msgBody.get("orig_Resp_nm"));
					funds.setPOSid(msgBody.get("be_order_id").toString());
					funds.setPosNumber(msgBody.get("term_cd").toString());
					funds.setAuditStatus(FundsChangeAuditStatus.VALIDATE);
					funds.setAuditType(FundsChangeAuditType.SYSTEM);
					funds.setSystemAuditRemark("易宝Pos支付自动审批完成");
					funds.setAuditTime(DateTools.getCurrentDateTime());
					funds.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
					saveYeePayInfo(domain,msgBody,funds,orderInfo);
					fundsChangeHistoryDao.save(funds);
					msgBody.put("resp_cd", "00");
					msgBody.put("resp_nm", "通知处理成功");
				}else{//正常支付流程逻辑
					FundsChangeHistory fund=null;
					if(msgBody.get("orig_resp_cd")!=null && msgBody.get("orig_resp_cd").equals("00")){
						FundsChangeHistoryVo fvo =new FundsChangeHistoryVo();
						fvo.setContractId(domain.getContract().getId());
						fvo.setTransactionAmount(Double.valueOf( orderInfo.get("pay_amt").toString()));//银联金额需要处理
						fvo.setChannel(PayWay.POS);
						fvo.setPOSid(msgBody.get("be_order_id").toString());
						fvo.setPosNumber(msgBody.get("term_cd").toString());
						fvo.setPosReceiptDate(DateTools.getCurrentDate());
						fvo.setTransactionTime(DateTools.getCurrentDate());
						fvo.setRemark(msgBody.get("orig_resp_nm").toString());
						fvo.setChargeByWho(domain.getCreateUser());
						fvo.setAuditStatusValue(FundsChangeAuditStatus.VALIDATE.getValue());
						fvo.setAuditType(FundsChangeAuditType.SYSTEM);
						fvo.setSystemAuditRemark("易宝POS支付自动审批完成");
						fvo.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
						fund=contractService.saveFundOfContract(fvo);
						msgBody.put("resp_cd", "00");
						msgBody.put("resp_nm", "通知处理成功");
					}
					this.saveYeePayInfo(domain,msgBody, fund,orderInfo);

				}
			} else {
				msgBody.put("resp_cd", "11");
				msgBody.put("resp_nm", "没有查询到对应的订单");
			}
		} catch (ApplicationException e) {
			msgBody.put("resp_cd", "11");
			msgBody.put("resp_nm", e.getErrorMsg());
		}
		map.put("msg_body", msgBody.toString());
		try{
			map.put("sign", RSASignature.sign(msgBody.toString(),RSAEncrypt.loadPrivateKeyByFile(RSASignature.class.getResource("/cert/").getPath())));
		} catch (Exception e) {
			msgBody.put("resp_cd", "11");
			msgBody.put("resp_nm", "签名异常");
		}
		return map;
	}


	private void saveYeePayInfo(YeePayInfo info ,Map map, FundsChangeHistory fund,Map order) {
			info.setBusCode(map.get("mchnt_cd").toString());
			if(order.get("pay_type")!=null && order.get("pay_type").toString().equals("2") && order.get("pay_card_no")!=null){
				info.setCardNo(order.get("pay_card_no").toString());
			}
			info.setCounterFee(BigDecimal.ZERO);
			info.setFundChargeId(fund);
			info.setPayCode(map.get("be_order_id").toString());
			info.setPosCode(map.get("term_cd").toString());
			info.setRealMoney(new BigDecimal(order.get("pay_amt").toString()));
			info.setStatus(map.get("orig_resp_cd").toString());
		    info.setTransationTime(DateTools.getCurrentDateTime());
			info.setFailReason(map.get("orig_resp_nm").toString());
			info.setResult(map.get("orig_resp_nm").toString());
			info.setModifyTime(DateTools.getCurrentDateTime());
			yeePayInfoDao.save(info);
	}


}
