package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.domain.*;
import com.eduboss.service.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.ContractType;
import com.eduboss.common.FundsPayType;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;
import com.eduboss.dao.ContractDao;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.InitDataDeleteDao;
import com.eduboss.dao.RollbackBackupRecordsDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.StudnetAccMvDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domainVo.DeleteDataLogVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.FinanceMessage;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.utils.StringUtil;

@Service
public class InitDataDeleteServiceImpl implements InitDataDeleteService {

    @Autowired
    private InitDataDeleteDao initDataDeleteDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private StudentDao studentDao;

	@Autowired
	private UserService userService;

    @Autowired
    private UserDao userDao;
    
    @Autowired
	private FundsChangeHistoryDao fundsChangeHistoryDao;
	
	@Autowired
	private OperationCountService operationCountService;
	
	@Autowired
	private RollbackBackupRecordsDao rollbackBackupRecordsDao;
	
	@Autowired
	private StudnetAccMvDao studnetAccMvDao;

	@Autowired
	private StudentService studentService;
	
    @Autowired
    private RedisDataSource redisDataSource;

	@Autowired
	private CourseService courseService;

    @Override
    public void deleteInitData(String id, String reason) throws Exception {
        if (id != null&&id!="") {
            if (id.startsWith("C") && (id.matches("^CON[0-9]{10}$") || id.matches("^CON[0-9]{12}$"))) {
                DeletedataLog log = new DeletedataLog();
				log.setOperationUser(userService.getCurrentLoginUser());
                log.setDelete_id(id);
                log.setOperationTime(DateTools.getCurrentDateTime());
                Contract contract = contractDao.findById(id);
                if (contract != null) {
                	if (contractDao.checkHasFrozenCp(contract)) {
                		throw new ApplicationException("合同产品已冻结，不能删除，如需操作，请先撤销退费或转账申请");
                	}
                	List list=rollbackBackupRecordsDao.getReCordByContractId(id,ProductType.OTHERS);
                	if(list.size()>0){
                		throw new ApplicationException("学生合同有产生电子账户资金流转，不可删除合同。");
                	}else{
	                    log.setRecordCreateUser(contract.getSignStaff().getUserId());
	                    log.setStudent_name(contract.getStudent().getName());
	                    log.setCreateUser_name(contract.getCreateByStaff().getName());
	                    log.setReason(reason);
	                    log.setRecordCreateTime(contract.getCreateTime());
	                    // 获取需要更新h5现金流存储过程的日期
	                    List<FundsChangeHistory> funds = fundsChangeHistoryDao.getFundsChangeHistoryListByContractId(id);
	                    List<String> updateDateList = new ArrayList<String>(); 
	                    String updateFinanceDate = "";
	                    List<FinanceMessage> messageList=new ArrayList<FinanceMessage>();
	                    putMsgToList(messageList, updateDateList, funds);
	                    initDataDeleteDao.deleteByContractId(id);
	                    initDataDeleteDao.save(log);
						studentService.updateStudentProductFirstTime(contract.getStudent());
						putMsgToQueue(messageList);
						courseService.getStudentAccoutInfo(contract.getStudent().getId());//更新学生资金

                	}
                }
            } else if (id.startsWith("S") && (id.matches("^STU[0-9]{10}$") || id.matches("^STU[0-9]{12}$"))) {
                DeletedataLog log = new DeletedataLog();
				log.setOperationUser(userService.getCurrentLoginUser());
                log.setDelete_id(id);
                log.setOperationTime(DateTools.getCurrentDateTime());
                Student student = studentDao.findById(id);
                if (student != null) {
                	List<Contract> contractList = contractDao.getPaidNormalDeposits(id);
                	for (Contract contract : contractList) {
                		if (contractDao.checkHasFrozenCp(contract)) {
                    		throw new ApplicationException("该学生有合同产品冻结，不能删除，如需操作，请先撤销退费或转账申请");
                    	}
                	}
                	StudnetAccMv stua = studnetAccMvDao.getStudnetAccMvByStudentId(id);
                	if (stua.getFrozenAmount() != null && stua.getFrozenAmount().compareTo(BigDecimal.ZERO) > 0 ) {
                		throw new ApplicationException("该学生有资金冻结，不能删除，如需操作，请先撤销退费或转账申请");
                	}
                	if(stua.getElectronicAccount() != null && stua.getElectronicAccount().compareTo(BigDecimal.ZERO)>0){
                		throw new ApplicationException("学生账户有产生电子账户资金流转，不可删除学生。");
                	}else{
	                    log.setStudent_name(student.getName());
	                    User user = userDao.findById(student.getCreateUserId());
	                    log.setCreateUser_name(user.getName());
	                    log.setRecordCreateUser(student.getCreateUserId());
	                    log.setReason(reason);
	                    log.setRecordCreateTime(student.getCreateTime());
	                    // 获取需要更新h5现金流存储过程的日期
	                    List<FundsChangeHistory> funds = fundsChangeHistoryDao.getFundsChangeHistoryListByStudentId(id);
	                    List<String> updateDateList = new ArrayList<String>(); 
	                    List<FinanceMessage> messageList=new ArrayList<FinanceMessage>();
	                    putMsgToList(messageList, updateDateList, funds);
	                    initDataDeleteDao.deleteByStudentId(id);
	                    initDataDeleteDao.save(log);
	                    putMsgToQueue(messageList);
                	}
                }
            } else {
                throw new ApplicationException("ID格式错误");
            }
        } else {
            throw new ApplicationException("要删除ID为空！");
        }

    }
    public void putMsgToQueue(List<FinanceMessage> messageList){
    	for (FinanceMessage msg : messageList) {
    		try {// 加入队列
    			JedisUtil.lpush(ObjectUtil.objectToBytes(CommonUtil.FINANCE_QUEUE),ObjectUtil.objectToBytes(msg));
    		} catch (Exception e) {
    			// 错了
    		}
		}
    }
    
    public void putMsgToList(List<FinanceMessage> messageList,List<String> updateDateList,List<FundsChangeHistory> funds){
    	 String updateFinanceDate = "";
    	 for (FundsChangeHistory fund : funds) {
    		FinanceMessage msg=new FinanceMessage();
    		msg.setCampusId(fund.getFundBlCampusId());
    		msg.setUserId(fund.getContract().getSignStaff().getUserId());
    		msg.setUserName(fund.getContract().getSignStaff().getName());
    		msg.setCountDate(fund.getTransactionTime().substring(0, 10));
    		for(ContractProduct cp:fund.getContract().getContractProducts()){
    			if(cp.getType().equals(ProductType.LIVE)){
    				msg.setOnline(true);
    				break;
				}
			}
    		
    		if(fund.getChannel().equals(PayWay.REFUND_MONEY)){
    			msg.setRefundMoney(fund.getTransactionAmount());
    			msg.setFlag("2");
    		}else if (fund.getChannel().equals(PayWay.BANK_TRANSFER)
    				||fund.getChannel().equals(PayWay.CASH)
    				||fund.getChannel().equals(PayWay.POS)
    				||fund.getChannel().equals(PayWay.WEB_CHART_PAY)
    				||fund.getChannel().equals(PayWay.ALI_PAY)){
	    		if(fund.getContract().getContractType()!=null && fund.getContract().getContractType().equals(ContractType.NEW_CONTRACT)){
	    			msg.setNewMoney(fund.getTransactionAmount());
	    		}else if(fund.getContract().getContractType()!=null && fund.getContract().getContractType().equals(ContractType.RE_CONTRACT)){
	    			msg.setPreMoney(fund.getTransactionAmount());
	    		}
	    		msg.setTotalMoney(fund.getTransactionAmount());
	    		msg.setFlag("2");
    		}
    		
    		
    		if(fund.getFundsPayType()!=null && fund.getFundsPayType().equals(FundsPayType.WASH)){
    			msg.setFlag("1");
    		}
    		
    		
    		
         	String transactionDate = fund.getTransactionTime().substring(0, 10);
         	if (!updateFinanceDate.equals(transactionDate)) {
         		updateFinanceDate = transactionDate;
         		updateDateList.add(updateFinanceDate);
         	}
         	messageList.add(msg);
         }
    }
    
    @Override
    @Transactional(readOnly = true)
    public DataPackage findPageDeleteLog(DataPackage dataPackage, DeleteDataLogVo deleteDataLogVo, TimeVo timeVo) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        List<Order> orders = new ArrayList<Order>();
        if(StringUtil.isNotBlank(deleteDataLogVo.getStudent_name())){
            criterions.add(Restrictions.like("student_name",deleteDataLogVo.getStudent_name(), MatchMode.ANYWHERE));
        }
        if(StringUtil.isNotBlank(deleteDataLogVo.getCreateUser_name())){
            criterions.add(Restrictions.like("createUser_name",deleteDataLogVo.getCreateUser_name(), MatchMode.ANYWHERE));
        }
        orders.add(Order.desc("id"));
        DataPackage dp=initDataDeleteDao.findPageByCriteria(dataPackage,orders,criterions);
        dp.setDatas(HibernateUtils.voListMapping((List) dp.getDatas(),DeleteDataLogVo.class));
        return dp;
    }

    
    @Override
    public void deleteSomeConract() {
    	StringBuffer sql=new StringBuffer();
    	sql.append("select temp.* from temp_contract temp left join contract c on temp.contract_id =c.id where paid_status='UNPAY' and temp.flag=0 ");
    	List<Map<Object,Object>> list=initDataDeleteDao.findMapBySql(sql.toString(), new HashMap<String, Object>());
    	for (Map<Object, Object> map : list) {
    		try {
				deleteInitData(map.get("contract_id").toString(), "空合同处理");
				
				String updateSql="update temp_contract set flag=2 where contract_id='"+map.get("contract_id")+"'";
				initDataDeleteDao.excuteSql(updateSql, new HashMap<String, Object>());
			}catch (Exception e) {
				String updateSql="update temp_contract set flag=1 where contract_id='"+map.get("contract_id")+"'";
				initDataDeleteDao.excuteSql(updateSql, new HashMap<String, Object>());
			}
    		
    		initDataDeleteDao.commit();
		}
    	
    	
    }

}
