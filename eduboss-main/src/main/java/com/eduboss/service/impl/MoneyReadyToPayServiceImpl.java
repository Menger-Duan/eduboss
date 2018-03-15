package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.PayWay;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.MoneyReadyToPayDao;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.MoneyReadyToPay;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.domainVo.MoneyReadyToPayVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ContractService;
import com.eduboss.service.MoneyReadyToPayService;
import com.eduboss.service.UserService;
import com.eduboss.utils.CertUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.gnete.merchant.api.Crypt;

@Service
public class MoneyReadyToPayServiceImpl implements MoneyReadyToPayService {

	@Autowired
	private MoneyReadyToPayDao moneyReadyToPayDao;
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContractService  contractService;
	
	@Autowired
	private FundsChangeHistoryDao  fundsChangeHistoryDao;
    
	private final static ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public DataPackage getMoneyReadyToPay(MoneyReadyToPay moneyReadyToPay,
			DataPackage dataPackage) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from MoneyReadyToPay where 1 = 1 ");
		dataPackage = moneyReadyToPayDao.findPageByHQL(hql.toString(), dataPackage, true, new HashMap<String, Object>());
		List<MoneyReadyToPay> list = (List<MoneyReadyToPay>) dataPackage.getDatas();
		List<MoneyReadyToPayVo> voList = new ArrayList<MoneyReadyToPayVo>();
		dataPackage.setDatas(voList);
		return dataPackage;
	}


	@Override
	public String saveMoneyReadyToPay(MoneyReadyToPayVo moneyReadyToPayVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" update MoneyReadyToPay set status=2 where contract.id = :contractId and status is null";
		params.put("contractId", moneyReadyToPayVo.getContractId());
		moneyReadyToPayDao.excuteHql(hql, params);
		MoneyReadyToPay moneyReadyToPay = HibernateUtils.voObjectMapping(moneyReadyToPayVo, MoneyReadyToPay.class);
		moneyReadyToPay.setCreateTime(DateTools.getCurrentDateTime());
		moneyReadyToPay.setCreateUser(userService.getCurrentLoginUser());
		moneyReadyToPayDao.save(moneyReadyToPay);
		return moneyReadyToPay.getId();
	}
	
	@Override
	public MoneyReadyToPayVo findById(String id) {
		MoneyReadyToPay domain = moneyReadyToPayDao.findById(id);
		MoneyReadyToPayVo moneyReadyToPay = new MoneyReadyToPayVo();
		
		if(domain!=null){
			moneyReadyToPay=HibernateUtils.voObjectMapping(domain, MoneyReadyToPayVo.class);
			moneyReadyToPay.setResultCode("0");
			moneyReadyToPay.setResultMsg("成功");
		}else{
			moneyReadyToPay.setResultCode("1");
			moneyReadyToPay.setResultMsg("没有查询到对应的订单");
		}
		
		return moneyReadyToPay;
	}
	
	@Override
	public Map findByIdAndSignMsg(String signMsg,String encryptedMsg) {
		MoneyReadyToPayVo moneyReadyToPay = new MoneyReadyToPayVo();
		Crypt Obj=new Crypt();
		Map map=new HashMap();
		Map signM =new HashMap();
		String returnMsg="";
		try{
			signM = CertUtil.decreptMsg(encryptedMsg, signMsg);
		}catch(Exception e){
			e.printStackTrace();
			map.put("resultCode", "1");
			map.put("resultMessage", "验签失败");
			returnMsg=CertUtil.mapToString(map);
			 return CertUtil.EncryptMsg(returnMsg);
		}
		//银联加密信息鉴权
		if(signM!=null && signM.get("payNo")!=null){
			MoneyReadyToPay domain = moneyReadyToPayDao.findById(signM.get("payNo").toString());
			if(domain!=null){
				moneyReadyToPay=HibernateUtils.voObjectMapping(domain, MoneyReadyToPayVo.class);
				map.put("payNo", moneyReadyToPay.getId());
				map.put("resultCode", "0");
				map.put("resultMessage", "成功");
				map.put("payAmount", moneyReadyToPay.getMoney()!=null ? moneyReadyToPay.getMoney().toString():"0");
			}else{
				map.put("payNo", "");
				map.put("resultCode", "1");
				map.put("resultMessage", "没有查询到对应的订单");
				map.put("payAmount","0");
			}
		}else{
			map.put("resultCode", "1");
			map.put("resultMessage", "验签失败");
		}
		
		returnMsg=CertUtil.mapToString(map);
		
		return CertUtil.EncryptMsg(returnMsg);
	}
	
	@Override
	public Map payResultNotify(String signMsg, String encryptedMsg) {
		Response rp = new Response();
		Map map=new HashMap();
		Crypt Obj=new Crypt();
		map.put("resultCode", "0");
		Map signM =new HashMap();
		String returnMsg="";
		try{
			signM = CertUtil.decreptMsg(encryptedMsg, signMsg);
		}catch(ApplicationException e){
			map.put("resultCode", "1");
			map.put("resultMessage", "验签异常");
			returnMsg=CertUtil.mapToString(map);
			 return CertUtil.EncryptMsg(returnMsg);
		}
		if(signM==null){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "验签失败");
			 returnMsg=CertUtil.mapToString(map);
			 return CertUtil.EncryptMsg(returnMsg);
		 }
			
			String payNo=signM.get("payNo")==null?null:signM.get("payNo").toString();
			String payResultCode=signM.get("payResultCode")==null?null:signM.get("payResultCode").toString();
			String payResultMessage=signM.get("payResultMessage")==null?null:signM.get("payResultMessage").toString();
			String posCode=signM.get("posCode")==null?null:signM.get("posCode").toString();
			String payCode=signM.get("payCode")==null?null:signM.get("payCode").toString();
			String busNo=signM.get("busCode")==null?null:signM.get("busCode").toString();
			String cardNo=signM.get("cardNo")==null?null:signM.get("cardNo").toString();
			String amount=signM.get("amount")==null?null:signM.get("amount").toString();
			String transactionTime=signM.get("transactionTime")==null?null:signM.get("transactionTime").toString();
		 
		//银联加密信息鉴权
			
		  if(StringUtil.isBlank(payNo)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "payNo不能为空");
		 }else if(StringUtil.isBlank(payResultCode)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "payResultCode不能为空");
		 }else if(StringUtil.isBlank(payResultMessage)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "payResultMessage不能为空");
		 }else if(StringUtil.isBlank(posCode)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "posCode不能为空");
		 }else if(StringUtil.isBlank(payCode)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "payCode不能为空");
		 }else if(StringUtil.isBlank(busNo)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "busNo不能为空");
		 }else if(StringUtil.isBlank(cardNo)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "cardNo不能为空");
		 }else if(StringUtil.isBlank(amount)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "amount不能为空");
		 }else if(StringUtil.isBlank(transactionTime)){
			 map.put("resultCode", "1");
			 map.put("resultMessage", "transactionTime不能为空");
		 }else{
			MoneyReadyToPay vo = moneyReadyToPayDao.findById(payNo);
			if(vo!=null){
				if(vo.getStatus()!=null && !"3".equals(vo.getStatus())){
					 map.put("resultCode", "1");
					 map.put("resultMessage", "支付信息已经处理，请不要重复支付");
					 returnMsg=CertUtil.mapToString(map);
					 return CertUtil.EncryptMsg(returnMsg);
				}else if("3".equals(vo.getStatus())){// 用于在收款记录表里面修改支付方式处理逻辑
					FundsChangeHistory fund=null;
					String changeResult="5";//默认支付失败
					if(payResultCode!=null && payResultCode.equals("0")){
						changeResult="4";//支付成功
						if(vo.getFundChargeId()!=null){//如果支付成功就修改原来付款记录表的支付方式跟pos机ID
							 fund=fundsChangeHistoryDao.findById(vo.getFundChargeId().getId());
							 fund.setPOSid(payCode);
							 fund.setPosNumber(posCode);
							 fund.setChannel(PayWay.POS);
							 fund.setIsTurnPosPay("Y");
							 fund.setTurnPosTime(DateTools.getCurrentDateTime());
							 fund.setPosReceiptDate(DateTools.getCurrentDate());
							 fundsChangeHistoryDao.save(fund);
						}
					}
					//把支付信息保存的pos机支付流水表里面
					 this.saveMoneyReadyTopay(changeResult, payResultMessage, posCode, payCode, fund, transactionTime, busNo, cardNo, fund.getTransactionAmount(), vo);
					
				}else{//正常支付流程逻辑
					FundsChangeHistory fund=null;
					if(payResultCode!=null && payResultCode.equals("0")){
						FundsChangeHistoryVo fvo =new FundsChangeHistoryVo();
						fvo.setContractId(vo.getContract().getId());
						fvo.setTransactionAmount(Double.valueOf(amount)/100);//银联金额需要处理
						fvo.setChannel(PayWay.POS);
						fvo.setPOSid(payCode);
						fvo.setPosNumber(posCode);
						fvo.setPosReceiptDate(DateTools.getCurrentDate());
						fvo.setTransactionTime(transactionTime);
						fvo.setRemark(payResultMessage);
						fvo.setChargeByWho(vo.getCreateUser().getUserId());
						fund=contractService.saveFundOfContract(fvo);
					}
					
					this.saveMoneyReadyTopay(payResultCode, payResultMessage, posCode, payCode, fund, transactionTime, busNo, cardNo, fund.getTransactionAmount(), vo);
				}
			}else{
				 map.put("resultCode", "1");
				 map.put("resultMessage", "找不到对应的支付信息");
			}
		  }
		returnMsg=CertUtil.mapToString(map);
		return CertUtil.EncryptMsg(returnMsg);
	}
	
	/**
	 * 保存支付流水信息
	 * @param payResultCode
	 * @param payResultMessage
	 * @param posCode
	 * @param payCode
	 * @param fund
	 * @param transactionTime
	 * @param busNo
	 * @param cardNo
	 * @param realMoney
	 * @param vo
	 */
	public void saveMoneyReadyTopay(String payResultCode,String payResultMessage,String posCode ,
			String payCode,FundsChangeHistory fund,String transactionTime,String busNo,String cardNo,BigDecimal realMoney,MoneyReadyToPay vo){
				vo.setStatus(payResultCode);
				vo.setFailReason(payResultMessage);
				vo.setPosCode(posCode);
				vo.setPayCode(payCode);
				vo.setFundChargeId(fund);
				vo.setTransationTime(transactionTime);
				vo.setBusCode(busNo);
				vo.setCardNo(cardNo);
				vo.setRealMoney(realMoney);
			moneyReadyToPayDao.save(vo);
	}
	
	/**
	 * @param payNo  星火支付单号
	 * @param payResultCode 支付结果
	 * @param payResultMessage 支付信息
	 * @param SignMsg 加密信息
	 * @param posCode 终端号
	 * @param payCode 银联支付流水号
	 * @param busNo 商户编号
	 * @param cardNo 支付卡号
	 * @param amount 金额
	 * @param transactionTime  付款时间
	 * @param encryptedMsg  加密信息
	 */
	@Override
	public Map payResultNotify(String payNo,
			String payResultCode, String payResultMessage, String signMsg,String posCode,
			String payCode,String busNo,String cardNo,String amount,String transactionTime,String encryptedMsg) {
		Response rp = new Response();
		Map map=new HashMap();
		Crypt Obj=new Crypt();
		map.put("resultCode", "0");
		Map signM = CertUtil.decreptMsg(encryptedMsg, signMsg);
		//银联加密信息鉴权
//		try {
		//银联加密信息鉴权
		 if(signM!=null && signM.get("payNo")!=null){
			MoneyReadyToPay vo = moneyReadyToPayDao.findById(payNo);
			if(vo!=null){
				if(vo.getStatus()!=null){
					 map.put("resultCode", "1");
					 map.put("resultMessage", "支付信息已经处理，请不要重复支付");
				}
				FundsChangeHistory fund=null;
				if(payResultCode!=null && payResultCode.equals("0")){
					FundsChangeHistoryVo fvo =new FundsChangeHistoryVo();
					fvo.setContractId(vo.getContract().getId());
					fvo.setTransactionAmount(Double.valueOf(vo.getMoney().toString()));
					fvo.setChannel(PayWay.POS);
					fvo.setPOSid(posCode);
					fvo.setTransactionTime(transactionTime);
					fvo.setRemark(payResultMessage);
					fvo.setPosNumber(payCode);
					fund=contractService.saveFundOfContract(fvo);
				}
				vo.setStatus(payResultCode);
				vo.setFailReason(payResultMessage);
				vo.setPosCode(posCode);
				vo.setPayCode(payCode);
				vo.setFundChargeId(fund);
				vo.setTransationTime(transactionTime);
				vo.setBusCode(busNo);
				vo.setCardNo(cardNo);
				
				moneyReadyToPayDao.save(vo);
			}else{
				 map.put("resultCode", "1");
				 map.put("resultMessage", "找不到对应的支付信息");
			}
		  }else{
				map.put("resultCode", "2");
				map.put("resultMessage", "验签失败");
			}
//		}catch(ApplicationException e){
//			throw e;
//			map.put("resultCode", "1");
//			map.put("resultMessage", e.getErrorMsg());
//		}  catch (Exception e) {
//			throw e;
//			map.put("resultCode", "1");
//			map.put("resultMessage", "系统异常");
//		}
		String returnMsg=CertUtil.mapToString(map);
//		map.putAll();
		return CertUtil.EncryptMsg(returnMsg);
	}
}
