package com.eduboss.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.eduboss.common.FundsChangeType;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eduboss.common.FundsChangeAuditStatus;
import com.eduboss.common.FundsChangeAuditType;
import com.eduboss.common.PayWay;
import com.eduboss.dao.IntenetPayDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.IntenetPay;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.domainVo.HandleTongLianPayVo;
import com.eduboss.domainVo.IntenetPayVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.intenetpay.IntenetPayResponseParam;
import com.eduboss.intenetpay.SybPayService;
import com.eduboss.service.ContractService;
import com.eduboss.service.IntenetPayService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.utils.PropertiesUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT ,rollbackFor=Exception.class)
public class IntenetPayServiceImpl implements IntenetPayService {
	
	
	Logger log =Logger.getLogger(IntenetPayServiceImpl.class);

	@Autowired
	private IntenetPayDao intenetPayDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private OrganizationDao organizationDao;

	@Override
	public Map<String,String> sendPayDetailInfo(IntenetPayVo intenetPayVo) {
		
			Map<String,String> returnMap = sendPayInfo(intenetPayVo);
			intenetPayVo.setRetmsg(returnMap.get("retmsg"));
			intenetPayVo.setErrorMsg(returnMap.get("errmsg"));
			intenetPayVo.setTrxid(returnMap.get("trxid"));
			intenetPayVo.setChnltrxid(returnMap.get("chnltrxid"));
			intenetPayVo.setRetcode(returnMap.get("retcode"));
			intenetPayVo.setReqsn(returnMap.get("reqsn"));
			intenetPayVo.setFinishTime(returnMap.get("fintime"));
			intenetPayVo.setPayInfo(returnMap.get("payinfo"));
			intenetPayVo.setTrxstatus(returnMap.get("trxstatus"));
			IntenetPay intenetPay=HibernateUtils.voObjectMapping(intenetPayVo, IntenetPay.class);
			intenetPay.setCreateTime(DateTools.getCurrentDateTime());
			intenetPay.setCreateUser(userService.getCurrentLoginUser());
			intenetPay.setModifyTime(DateTools.getCurrentDateTime());
			intenetPay.setModifyUser(userService.getCurrentLoginUser());
		if(StringUtils.isNotBlank(intenetPayVo.getAuthcode())
				&& returnMap.get("retcode")!=null
				&& "SUCCESS".equals(returnMap.get("retcode"))
				&& returnMap.get("trxstatus")!=null
				&& "0000".equals(returnMap.get("trxstatus"))){
			intenetPay.setFundsChange(saveFundInfo(intenetPay, true));
			if(intenetPay.getFundsChange()!=null) {
				returnMap.put("fundsId", intenetPay.getFundsChange().getId());
			}
		}
		try {
			intenetPayDao.save(intenetPay);

		}catch (DataIntegrityViolationException e){
			log.error(e.getMessage());

			throw new ApplicationException("已经手动确认或者自动提交了,请确认客户是否已经扣款");
//			returnMap.put("retcode","fail");
//			returnMap.put("errmsg","已经手动确认或者自动提交了,请确认客户是否已经扣款。");
		}
		HandleTongLianPayVo handleTongLianPayVo = new HandleTongLianPayVo("intenetPay",intenetPay.getTrxid(),intenetPay.getReqsn());
		try {
			JedisUtil.lpush(ObjectUtil.objectToBytes("payMessageKey"),ObjectUtil.objectToBytes(handleTongLianPayVo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("HandleTongLianPayVo  intenetPay:" + e.getMessage());
			e.printStackTrace();
		}
		
		return returnMap;
	}

	@Override
	public Response saveBarCodeInfo(IntenetPayVo intenetPayVo){
//		Map<String,String> map=findPayStatus(intenetPayVo);
//		if(map!=null && !"2008".equals(map.get("trxstatus"))){
//
//		}

		Response res=new Response();
		IntenetPay intenetPay=HibernateUtils.voObjectMapping(intenetPayVo, IntenetPay.class);
		intenetPay.setCreateTime(DateTools.getCurrentDateTime());
		intenetPay.setCreateUser(userService.getCurrentLoginUser());
		intenetPay.setModifyTime(DateTools.getCurrentDateTime());
		intenetPay.setModifyUser(userService.getCurrentLoginUser());
		if(StringUtils.isNotBlank(intenetPayVo.getAuthcode())){
			intenetPay.setFundsChange(saveFundInfo(intenetPay, false));
			intenetPay.setReqsn(intenetPay.getAuthcode());
		}
		intenetPay.setTrxstatus("0000");
		intenetPay.setStatus("2");

		try {
			intenetPayDao.save(intenetPay);
		}catch (DataIntegrityViolationException e){
			log.error(e.getMessage());
			throw new ApplicationException("已经手动确认或者自动提交了,请确认客户是否已经扣款");
		}

		return res;
	}

	public Map<String,String> findPayStatus(IntenetPayVo intenetPayVo){
		Organization o = findKeyByContract(intenetPayVo.getContractId());
		SybPayService service = new SybPayService();
		try {
			return service.query(intenetPayVo.getReqsn(), "",o.getSybCusId(),o.getSybAppId(),o.getSybAppKey());//查询支付宝或者微信订单的支付状态
		} catch (Exception e) {
			throw new ApplicationException("查询通联支付订单的时候报错，请联系管理员"+e.getMessage());
		}
	}
	
	
	/**
	 * 调用通联接口生成订单
	 * @return
	 */
	public Map<String,String> sendPayInfo(IntenetPayVo vo){
		ContractVo cvo = contractService.getContractById(vo.getContractId());
		Organization o = findKeyByContract(vo.getContractId());

		String authcode=StringUtils.isNotBlank(vo.getAuthcode()) ? vo.getAuthcode() :"";
		//转换类型
		String payType="";
		if(vo.getPayType().equals("ALI_PAY")){
			if(StringUtils.isNotBlank(vo.getAuthcode())){
				payType="A04";
			}else{
				payType="A01";
			}
		}else if(vo.getPayType().equals("WEB_CHART_PAY")){
			if(StringUtils.isNotBlank(vo.getAuthcode())){
				payType="W04";
			}else{
				payType="W01";
			}
		}
		//构造标题
		String title=PropertiesUtils.getStringValue("intenet.payCompany")+"-"+o.getName()+"-"+cvo.getStuName();
		
		SybPayService service = new SybPayService();
		String reqsn = String.valueOf(System.currentTimeMillis());

		if(StringUtils.isNotBlank(authcode)){
			reqsn=authcode;
		}

		Map<String, String> map;
		vo.setTitle(title);//设置标题
		try {
			//通联的金额是以分为单位，乘以100
			map = service.pay(vo.getAmount().multiply(BigDecimal.valueOf(100)).longValue(),
					reqsn,
					payType,
					title,
					vo.getRemark(),
					"",
					authcode,
					vo.getUrl()+PropertiesUtils.getStringValue("intenet.payUrl"),
					"",o.getSybCusId(),o.getSybAppId(),o.getSybAppKey());
		} catch (Exception e) {
			log.error("调用通联接口错误"+e.getMessage());
			try {
				log.error("再次查询支付状态"+e.getMessage());
				map=service.query(authcode, "",o.getSybCusId(),o.getSybAppId(),o.getSybAppKey());//查询支付宝或者微信订单的支付状态
			} catch (Exception e1) {
				//TODO 如果是扫描枪，可以考虑写个AOP
				log.error("通联支付返回错误，并且再次查询结果又错误！"+e1.getMessage());
				throw new ApplicationException("通联支付返回错误:"+e.getMessage());
			}
		}
		return map;
	}
	
	
	/**
	 * 获取订单支付信息,如果成功就要记录下来返回结果
	 * @param trxId
	 * @param reqsn
	 * @return
	 */
	@Override
	public  Map<String,String> commitPayInfo(String trxId, String reqsn){
		 Map<String,String> returnMap=new HashMap<String,String>();
		
		IntenetPay info = intenetPayDao.findIntenetPayByTrxid(trxId);
		if(info!=null && StringUtils.isNotBlank(info.getStatus())){//订单状态如果已经修改，证明通联回调过我们接口，可以判断是否成功
			if("0".equals(info.getStatus())){
				 returnMap.put("resultCode", "0");
				returnMap.put("resultMessage", "支付成功");
				returnMap.put("fundsId", info.getFundsChange().getId());
			}else{
				if("3044".equals(info.getTrxstatus())){
					returnMap.put("resultMessage", "交易超时");
				}else if("3008".equals(info.getTrxstatus())){
					returnMap.put("resultMessage", "余额不足");
				}else if("3999".equals(info.getTrxstatus())){
					returnMap.put("resultMessage", "交易失败");
				}else if("0000".equals(info.getTrxstatus())){
					returnMap.put("resultMessage", "交易成功");
				}else{
					returnMap.put("resultMessage", info.getTrxstatus()+":该交易码不存在，请联系管理员！");
				}
				returnMap.put("resultCode", "-1");
				
			}
		}else {
			returnMap.put("resultCode", "-2");
			returnMap.put("resultMessage", "支付中");
		}
		return returnMap;
	}

	
	/**
	 * 确认校区有绑定支付信息
	 * @param contractId
	 * @return
	 */
	public Organization findKeyByContract(String contractId){
		ContractVo cvo = contractService.getContractById(contractId);
		Organization o = userService.getBelongBranchByCampusId(cvo.getBlCampusId());
		
		//判断是否有绑定支付帐号
		if(o==null || StringUtils.isBlank(o.getSybAppId()) || StringUtils.isBlank(o.getSybAppKey()) || StringUtils.isBlank(o.getSybCusId())){
			throw new ApplicationException("该合同分公司暂未开通网络支付功能，请选择其他方式，或者联系管理员开通再使用！");
		}
		return o;
	}
	
	/* 
	 * 通联回调方法
	 */
	@Override
	public Response noticePayStatus(IntenetPayResponseParam param) {
		log.info("---------------------------------------------------------id:"+param.getTrxid()+" 状态："+param.getTrxstatus());
		
		Response res=new Response();
		IntenetPay info = intenetPayDao.findIntenetPayByTrxid(param.getTrxid());
		if(info!=null && info.getFundsChange()!=null){//如果已经存在了状态，就是手动确认了付款，需要对手动数据处理
			info.setModifyTime(DateTools.getCurrentDateTime());
			info.setModifyUser(info.getCreateUser());
			FundsChangeHistory fun=info.getFundsChange();
			 if(fun!=null && fun.getAuditStatus() != null && fun.getAuditStatus().equals(FundsChangeAuditStatus.VALIDATE)){
				 res.setResultMessage("订单处理完成");
				 return res;
			 }
			
			if(param.getTrxstatus().equals("0000")){
				info.setStatus("0");
				fun.setAuditStatus(FundsChangeAuditStatus.VALIDATE);
				fun.setAuditType(FundsChangeAuditType.SYSTEM);
				if(fun.getChannel().equals(PayWay.CASH)){//现金转网络支付
					fun.setChannel(PayWay.valueOf(info.getPayType()));
					fun.setPOSid(info.getTrxid());//支付单号
				}
				fun.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
				fun.setSystemAuditRemark(PayWay.valueOf(info.getPayType()).getName()+"自动审批完成");
				res.setResultMessage("支付成功");
			}else if(!param.getTrxstatus().equals("0000")){//如果是失败，要做冲销
				info.setStatus("3");
				if(fun.getChannel().equals(PayWay.CASH)){//现金转网络支付
					info.setTrxstatus(param.getTrxstatus());
					res.setResultMessage("订单处理完成");
					return res;
				}
				intenetPayDao.save(info);
				try{
					contractService.saveFundWashOfContract(info.getFundsChange().getId(),info.getFundsChange().getTransactionAmount());
				}catch(ApplicationException e){
					throw new ApplicationException("支付失败  :1.请先将该合同已分配的资金提取出来 2.然后进行“更新支付状态”操作（更新后系统会自动冲销） 3.请重新发起收款操作");
				}
				res.setResultCode(-1);
			}
			intenetPayDao.save(info);
			res.setResultMessage("订单处理完成");
			return res;
		}
		if(param!=null){
			info.setTrxstatus(param.getTrxstatus());
			info.setModifyTime(DateTools.getCurrentDateTime());
			info.setModifyUser(info.getCreateUser());
			if(info.getTrxstatus().equals("0000")){
				info.setFundsChange(saveFundInfo(info,true));
				info.setStatus("0");
				res.setResultMessage("支付成功");
			}else{
				info.setStatus("1");
				res.setResultCode(-1);
			}
		}
		
		intenetPayDao.save(info);
		
		return res;
	}
	
	@Override
	@Transactional
	public Map<String,String> confirmPaid(String trxId) {
		Map<String,String> map = new HashMap<String,String>();
		
		IntenetPay info = intenetPayDao.findIntenetPayByTrxid(trxId);
		if(info!=null && StringUtils.isNotBlank(info.getStatus())){
			map.put("resultCode", "0");
			return map;
		}else if (info!=null){
			info.setTrxstatus("0000");
			info.setModifyTime(DateTools.getCurrentDateTime());
			info.setModifyUser(userService.getCurrentLoginUser());
			info.setFundsChange(saveFundInfo(info,false));
			info.setStatus("2");
			intenetPayDao.save(info);
			map.put("resultCode", "0");
		}
		map.put("contractId", info.getContractId());
		
		if(info.getFundsChange()!=null) {
			map.put("fundId", info.getFundsChange().getId());
		}
		return map;
	}


	public FundsChangeHistory saveFundInfo(IntenetPay info,Boolean flag){
		FundsChangeHistoryVo fvo =new FundsChangeHistoryVo();
		fvo.setContractId(info.getContractId());
		fvo.setTransactionAmount(info.getAmount().doubleValue());
		fvo.setChannel(PayWay.valueOf(info.getPayType()));
		fvo.setPosReceiptDate(DateTools.getCurrentDate());
		fvo.setTransactionTime(DateTools.getCurrentDateTime());
		fvo.setRemark(info.getRemark());
		fvo.setChargeByWho(info.getCreateUser().getUserId());
		fvo.setPOSid(info.getTrxid());//支付单号？
		if(flag){
			fvo.setAuditStatusValue(FundsChangeAuditStatus.VALIDATE.getValue());
			fvo.setAuditType(FundsChangeAuditType.SYSTEM);
			fvo.setSystemAuditRemark(PayWay.valueOf(info.getPayType()).getName()+"自动审批完成");
			fvo.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
		}else{
			fvo.setAuditStatusValue(FundsChangeAuditStatus.UNVALIDATE.getValue());
			fvo.setAuditType(FundsChangeAuditType.SYSTEM);
			fvo.setSystemAuditRemark(PayWay.valueOf(info.getPayType()).getName()+"手动确认收款");
			fvo.setFundsChangeType(FundsChangeType.HUMAN);//人工录入
		}
		FundsChangeHistory fun=contractService.saveFundOfContract(fvo);//保存支付信息
		return fun;
	}
	
	
	@Override
	public IntenetPayVo findPayInfoByFundId(String fundsId) {
		IntenetPay info = intenetPayDao.findIntenetPayByFundId(fundsId);
		Hibernate.initialize(info);
		if(info!=null){
			return HibernateUtils.voObjectMapping(info, IntenetPayVo.class);
		}
		return null;
	}
	
	/*
	 *刷新状态
	 */
	@Override
	public Map<String, String> updatePayStatus(String trxId,String reqsn) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("resultCode", "0");
		map.put("resultMsg","刷新成功");
		IntenetPay info = new IntenetPay();
		if(StringUtils.isNotBlank(trxId)){
			 info = intenetPayDao.findIntenetPayByTrxid(trxId);
		}else if(StringUtils.isNotBlank(reqsn)){
			info=intenetPayDao.findIntenetPayByreqsn(reqsn);
		}
			Organization o=findKeyByContract(info.getContractId());
			if(info!=null && info.getStatus().equals("2")){
					SybPayService service = new SybPayService();
					try {
						map.putAll(service.query(reqsn, trxId,o.getSybCusId(),o.getSybAppId(),o.getSybAppKey()));//查询支付宝或者微信订单的支付状态
					} catch (Exception e) {
						throw new ApplicationException("查询通联支付订单的时候报错，请联系管理员");
					}
					if(map!=null && !"2008".equals(map.get("trxstatus"))){
						info.setTrxstatus(map.get("trxstatus"));
						info.setModifyTime(DateTools.getCurrentDateTime());
						info.setModifyUser(userService.getCurrentLoginUser());
						info.setErrorMsg(map.get("errmsg"));
						info.setFinishTime(map.get("fintime"));
						if(info.getTrxstatus().equals("0000")){
							FundsChangeHistory fun=info.getFundsChange();
							info.setStatus("0");
							fun.setAuditStatus(FundsChangeAuditStatus.VALIDATE);
							fun.setAuditType(FundsChangeAuditType.SYSTEM);
							fun.setSystemAuditRemark(PayWay.valueOf(info.getPayType()).getName()+"自动审批完成");
							fun.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
							intenetPayDao.save(info);
						}else{
							info.setStatus("3");
							intenetPayDao.save(info);
							try{
								contractService.saveFundWashOfContract(info.getFundsChange().getId(),info.getFundsChange().getTransactionAmount());
							}catch(ApplicationException e){
								throw new ApplicationException("支付失败  :1.请先将该合同已分配的资金提取出来 2.然后进行“更新支付状态”操作（更新后系统会自动冲销） 3.请重新发起收款操作");
							}
						}
						
					}else if ("2008".equals(map.get("trxstatus"))){
						map.put("resultMsg","已经是最新状态！");
					}
			}
		
		
		return map;
	}




	/**
	 * 给外面的接口
	 * payType A01 W01
	 * title 收款页面显示标题
	 * amount 金额
	 * remark 备注
	 * authCode 选填 反扫用户二维码值
	 * url  回调Url
	 * cusId  通联用户ID
	 * appId  通联应用id
	 * appKey 通联应用key
	 * @return
	 */
	@Override
	public Map<String,String> sendPayInfoForOtherPlat(IntenetPayVo vo){
		getSybAppInfo(vo);//获取支付账号信息
		SybPayService service = new SybPayService();
		String reqsn = String.valueOf(System.currentTimeMillis());
		if(StringUtils.isNotBlank(vo.getAuthcode())){
			reqsn=vo.getAuthcode();
		}
		Map<String, String> map;
		try {
			//通联的金额是以分为单位，乘以100
			map = service.pay(vo.getAmount().multiply(BigDecimal.valueOf(100)).longValue(),
					reqsn,
					vo.getPayType(),
					vo.getTitle(),
					vo.getRemark(),
					"",
					reqsn,
					vo.getUrl(),
					"",vo.getCusId(),vo.getAppId(),vo.getAppKey());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("调用通联接口错误"+e.getMessage());
		}
		return map;
	}

	public void getSybAppInfo(IntenetPayVo vo){
		if(StringUtils.isNotBlank(vo.getOrganizationId()) && (StringUtils.isBlank(vo.getAppKey()) || StringUtils.isBlank(vo.getAppId()) || StringUtils.isBlank(vo.getCusId()))){
			Organization org = organizationDao.findById(vo.getOrganizationId());
			vo.setAppId(org.getSybAppId());
			vo.setCusId(org.getSybCusId());
			vo.setAppKey(org.getSybAppKey());
		}
	}

	@Override
	public Map<String,Object> getPayStatusByTrxid(IntenetPayVo vo) {
		getSybAppInfo(vo);//获取支付账号信息
		SybPayService service = new SybPayService();
		Map<String, Object> map=new HashMap<>();
		try {
			map.putAll(service.query(vo.getReqsn(), vo.getTrxid(),vo.getCusId(),vo.getAppId(),vo.getAppKey()));//查询支付宝或者微信订单的支付状态
		} catch (Exception e) {
			throw new ApplicationException("查询通联支付订单的时候报错，请联系管理员");
		}
		return map;
	}
}
