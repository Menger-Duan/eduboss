package com.eduboss.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;
import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.InitDataDeleteDao;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.DeletedataLog;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.IncomeMessage;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;



@Repository("InitDataDeleteDao")
public class InitDataDeleteDaoImpl extends GenericDaoImpl<DeletedataLog,String> implements InitDataDeleteDao {
	
	
	@Autowired
	private AccountChargeRecordsDao accountChargeRecordsDao;
	
	private static final Logger log=LoggerFactory.getLogger(InitDataDeleteDaoImpl.class);

	@Autowired
	private FundsChangeHistoryDao fundsChangeHistoryDao;
	

    @Autowired
    private RedisDataSource redisDataSource;
	/**
	 * 根据合同ID删除系统相关信息
	 * @param contractId
	 */
	public void deleteByContractId(String contractId){
		
		SessionFactory sf=accountChargeRecordsDao.getHibernateTemplate().getSessionFactory();
		Session session = sf.openSession();
        checkContractIfCanDelete(contractId);
		List<String> sqls=new ArrayList<String>();
		//扣费记录
		sqls.add("DELETE FROM ACCOUNT_CHARGE_RECORDS WHERE CONTRACT_ID='"+contractId+"';");
		//提成记录
		sqls.add("DELETE FROM CONTRACT_BONUS WHERE FUNDS_CHANGE_ID IN (SELECT ID FROM (SELECT ID FROM funds_change_history WHERE CONTRACT_ID='"+contractId+"') AS tb);");//
		//收款记录
		sqls.add("DELETE FROM funds_change_history WHERE CONTRACT_ID='"+contractId+"';");
		//小班考勤记录
		sqls.add("DELETE mcsa.* FROM mini_class_student_attendent mcsa"
				+ " left join (SELECT mcc.MINI_CLASS_COURSE_ID,mcs.STUDENT_ID FROM MINI_CLASS_STUDENT mcs"
				+ " LEFT JOIN mini_class_course mcc ON mcs.MINI_CLASS_ID=mcc.MINI_CLASS_ID"
				+ "  WHERE mcs.CONTRACT_ID='"+contractId+"' GROUP BY mcc.MINI_CLASS_COURSE_ID,mcs.STUDENT_ID) mcc on mcsa.mini_class_course_id=mcc.mini_class_course_id"
				+ "  where  mcc.STUDENT_ID=mcsa.STUDENT_ID and mcsa.CHARGE_STATUS<>'CHARGED' ;");
		//小班报读
		sqls.add("DELETE FROM mini_class_student WHERE CONTRACT_ID ='"+contractId+"';");
		//双师考勤记录
		sqls.add("DELETE ttcsa.*  FROM two_teacher_class_student_attendent ttcsa, contract_product cp, two_teacher_class_student ttcs  " +
				"WHERE CONTRACT_ID='"+contractId+"' AND ttcs.CONTRACT_PRODUCT_ID=cp.ID AND ttcsa.CLASS_TWO_ID=ttcs.CLASS_TWO_ID AND ttcs.STUDENT_ID =ttcsa.STUDENT_ID AND ttcsa.CHARGE_STATUS<>'CHARGED';");
		//双师报读
		sqls.add("DELETE ttcs.* FROM two_teacher_class_student ttcs, contract_product cp WHERE CONTRACT_ID='"+contractId+"' AND ttcs.CONTRACT_PRODUCT_ID=cp.ID; ");

        //删除目标班
        sqls.add("DELETE FROM PROMISE_CLASS_STUDENT WHERE CONTRACT_PRODUCT_ID IN (SELECT ID FROM contract_product WHERE CONTRACT_ID='"+contractId+"');");
		//删除分配资金
		sqls.add("DELETE FROM MONEY_ARRANGE_LOG WHERE CONTRACT_PRODUCT_ID IN (SELECT ID FROM (SELECT ID FROM contract_product WHERE CONTRACT_ID='"+contractId+"') AS tb);");
		//合同产品科目
		sqls.add("DELETE FROM CONTRACT_PRODUCT_SUBJECT WHERE CONTRACT_PRODUCT_ID IN (SELECT id FROM contract_product WHERE CONTRACT_ID='"+contractId+"');");
		// 删除退费申请动态
		sqls.add("DELETE FROM refund_audit_dynamic WHERE REFUND_WORKFLOW_ID IN (SELECT ID FROM refund_workflow WHERE REFUND_CONTRACT_PRODUCT IN (SELECT ID FROM contract_product WHERE CONTRACT_ID = '"+contractId+"') AND (AUDIT_STATUS = 'TO_SUBMIT' OR AUDIT_STATUS = 'AUDIT_REVOKE'));");
		// 删除退费申请凭证
		sqls.add("DELETE FROM refund_evidence WHERE REFUND_WORKFLOW_ID IN (SELECT ID FROM refund_workflow WHERE REFUND_CONTRACT_PRODUCT IN (SELECT ID FROM contract_product WHERE CONTRACT_ID = '"+contractId+"') AND (AUDIT_STATUS = 'TO_SUBMIT' OR AUDIT_STATUS = 'AUDIT_REVOKE'));");
		// 删除退费申请
		sqls.add("DELETE FROM refund_workflow WHERE REFUND_CONTRACT_PRODUCT IN (SELECT ID FROM contract_product WHERE CONTRACT_ID = '"+contractId+"') AND (AUDIT_STATUS = 'TO_SUBMIT' OR AUDIT_STATUS = 'AUDIT_REVOKE');");
		//合同产品
		sqls.add("DELETE FROM contract_product WHERE CONTRACT_ID ='"+contractId+"';");
		
		//合同
		sqls.add("DELETE FROM contract WHERE ID ='"+contractId+"';");
		
		for(int i=0;i<sqls.size();i++){
			final String sql=sqls.get(i);
			accountChargeRecordsDao.getHibernateTemplate().execute(new HibernateCallback<Object>() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					session.createSQLQuery(sql).executeUpdate();
					// TODO Auto-generated method stub
					return null;
				}
			});
			
			session.flush();
		}
		
		
		session.clear();
	}
	
	


	/**
	 * 根据学生ID删除系统相关信息
	 * @param studentId
	 */
	public void deleteByStudentId(String studentId){
		SessionFactory sf=accountChargeRecordsDao.getHibernateTemplate().getSessionFactory();
		
		Session session = sf.openSession();
		checkStudentIfCanDelete(studentId);
		List<String> sqls=new ArrayList<String>();
		sqls.add("DELETE FROM STUDNET_ACC_MV WHERE STUDENT_ID = '"+studentId+"';");
		sqls.add("DELETE FROM ACCOUNT_CHARGE_RECORDS WHERE STUDENT_ID = '"+studentId+"';");
		sqls.add("DELETE cb.* FROM CONTRACT_BONUS cb left join funds_change_history fun on cb.FUNDS_CHANGE_ID=fun.id WHERE fun.STUDENT_ID = '"+studentId+"';");
		sqls.add("DELETE FROM funds_change_history WHERE  STUDENT_ID =  '"+studentId+"' ;");
		sqls.add("DELETE FROM mini_class_student WHERE STUDENT_ID = '"+studentId+"';");
		//删除目标班
		sqls.add("DELETE FROM PROMISE_CLASS_STUDENT WHERE STUDENT_ID = '"+studentId+"';");
		sqls.add("DELETE FROM MONEY_ARRANGE_LOG WHERE STUDENT_ID = '"+studentId+"';");
		//合同产品科目
		sqls.add("DELETE cps.* FROM CONTRACT_PRODUCT_SUBJECT cps left join contract_product cp on cps.CONTRACT_PRODUCT_ID=cp.id left join contract c on c.id=cp.contract_id  WHERE c.STUDENT_ID= '"+studentId+"'");
		sqls.add("DELETE cp.* FROM contract_product cp inner join contract c on c.id=cp.contract_id  WHERE c.STUDENT_ID = '"+studentId+"' ;");
		
		sqls.add("DELETE FROM contract WHERE STUDENT_ID = '"+studentId+"';");
		//删除学生家长关联
		sqls.add("DELETE FROM customer_student_relation WHERE STUDENT_ID ='"+studentId+"';");
		//删除排课需求
		sqls.add("DELETE FROM course WHERE STUDENT_ID = '"+studentId+"';");
		sqls.add("DELETE FROM course_summary WHERE STUDENT_ID = '"+studentId+"';");
		sqls.add("DELETE FROM course_requirement WHERE STUDENT_ID = '"+studentId+"';");
		//删除小班考勤记录
		sqls.add("DELETE FROM MINI_CLASS_STUDENT_ATTENDENT WHERE STUDENT_ID = '"+studentId+"';");
		//删除一对一考勤记录
		sqls.add("DELETE c.* FROM course_attendance_record c left join course cou on c.COURSE_ID=cou.COURSE_ID WHERE cou.STUDENT_ID ='"+studentId+"' ;");

		//删除学生动态信息
		sqls.add("DELETE FROM student_dynamic_status WHERE STUDENT_ID = '"+studentId+"';");
		
		//删除学生一对多信息
		sqls.add("DELETE FROM otm_class_student WHERE STUDENT_ID = '"+studentId+"';");
		//删除学生一对多信息
		sqls.add("DELETE FROM otm_class_student_attendent WHERE STUDENT_ID = '"+studentId+"';");
		//删除学生资金回滚信息
		sqls.add("DELETE FROM Money_Rollback_Records WHERE STUDENT_ID = '"+studentId+"';");
		
		//删除转账或退费审批动态
		sqls.add("DELETE FROM refund_audit_dynamic WHERE REFUND_WORKFLOW_ID IN (SELECT ID FROM refund_workflow WHERE REFUND_STUDENT = '"+studentId+"' AND (AUDIT_STATUS = 'TO_SUBMIT' OR AUDIT_STATUS = 'AUDIT_REVOKE'));");
		//删除转账或退费审批凭证
		sqls.add("DELETE FROM refund_evidence WHERE REFUND_WORKFLOW_ID IN (SELECT ID FROM refund_workflow WHERE REFUND_STUDENT = '"+studentId+"' AND (AUDIT_STATUS = 'TO_SUBMIT' OR AUDIT_STATUS = 'AUDIT_REVOKE'));");
		//转账或退费审批申请
		sqls.add("DELETE FROM refund_workflow WHERE REFUND_STUDENT = '"+studentId+"' AND (AUDIT_STATUS = 'TO_SUBMIT' OR AUDIT_STATUS = 'AUDIT_REVOKE');");
		
		sqls.add("DELETE FROM student WHERE ID = '"+studentId+"';");

		for(int i=0;i<sqls.size();i++){
			final String sql=sqls.get(i);
			accountChargeRecordsDao.getHibernateTemplate().execute(new HibernateCallback<Object>() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					session.createSQLQuery(sql).executeUpdate();
					// TODO Auto-generated method stub
					return null;
				}
			});
			session.flush();
		}
		
		
		session.clear();
	}
	
	@Override
	public void checkContractIfCanDelete(String contractId) {
		List list=fundsChangeHistoryDao.findByCriteria(Expression.eq("contract.id",contractId),Expression.eq("channel", PayWay.ELECTRONIC_ACCOUNT));
		if(list.size()!=0){
			throw new ApplicationException("本合同已经使用电子账户付款！不允许删除");
		}
        List list0 = accountChargeRecordsDao.findByCriteria(Expression.eq("contract.id", contractId),Expression.eq("productType", ProductType.ONE_ON_ONE_COURSE));
        if(list0.size()!=0){
            throw new ApplicationException("本合同的一对一产品已有扣费！不允许删除");
        }
        List list1=accountChargeRecordsDao.findByCriteria(Expression.eq("contract.id", contractId),Expression.eq("productType", ProductType.SMALL_CLASS));
        if(list1.size()!=0){
            throw new ApplicationException("本合同的小班产品已经扣费，不允许删除");
        }
        List list2=accountChargeRecordsDao.findByCriteria(Expression.eq("contract.id", contractId),Expression.eq("productType", ProductType.ECS_CLASS));
        if(list2.size()!=0){
            throw new ApplicationException("学生所在目标班已经扣费，不允许删除");
        }
        
        List list3=accountChargeRecordsDao.findByCriteria(Expression.eq("contract.id", contractId),Expression.eq("productType", ProductType.ONE_ON_MANY));
        if(list3.size()!=0){
            throw new ApplicationException("学生所在一对多已经扣费，不允许删除");
        }
        
        List list4=accountChargeRecordsDao.findByCriteria(Expression.eq("contract.id", contractId),Expression.eq("productType", ProductType.LECTURE));
        if(list4.size()!=0){
            throw new ApplicationException("学生所在讲座已经扣费，不允许删除");
        }

        //其他产品的扣费删除的话要把营收的也减去
        List<AccountChargeRecords> list5=accountChargeRecordsDao.findByCriteria(Expression.eq("contract.id", contractId),Expression.eq("productType", ProductType.OTHERS),Expression.ne("chargePayType", ChargePayType.WASH),Expression.eq("isWashed", "FALSE"));
        for (AccountChargeRecords acr : list5) {
        	pushChargeMsgToQueue(acr);
		}
	}




	@Override
	public void checkStudentIfCanDelete(String studentId) {
		List list0 = accountChargeRecordsDao.findByCriteria(Expression.eq("student.id", studentId),Expression.eq("productType", ProductType.ONE_ON_ONE_COURSE));
		if(list0.size()!=0){
			throw new ApplicationException("该学生一对一课程已经扣费，不允许删除");
		}
		List list1=accountChargeRecordsDao.findByCriteria(Expression.eq("student.id", studentId),Expression.eq("productType", ProductType.SMALL_CLASS));
		if(list1.size()!=0){
			throw new ApplicationException("学生所在小班已经扣费，不允许删除");
		}
		
		List list2=accountChargeRecordsDao.findByCriteria(Expression.eq("student.id", studentId),Expression.eq("productType", ProductType.ECS_CLASS));
		if(list2.size()!=0){
			throw new ApplicationException("学生所在目标班已经扣费，不允许删除");
		}
		
		List list3=accountChargeRecordsDao.findByCriteria(Expression.eq("student.id", studentId),Expression.eq("productType", ProductType.ONE_ON_MANY));
	        if(list3.size()!=0){
	            throw new ApplicationException("学生所在一对多已经扣费，不允许删除");
	    }
	        
        List list4=accountChargeRecordsDao.findByCriteria(Expression.eq("student.id", studentId),Expression.eq("productType", ProductType.LECTURE));
        if(list4.size()!=0){
            throw new ApplicationException("学生所在讲座已经扣费，不允许删除");
        }
        
        List<AccountChargeRecords> list5=accountChargeRecordsDao.findByCriteria(Expression.eq("student.id", studentId),Expression.eq("productType", ProductType.OTHERS),Expression.ne("chargePayType", ChargePayType.WASH),Expression.eq("isWashed", "FALSE"));
        for (AccountChargeRecords acr : list5) {
        	pushChargeMsgToQueue(acr);
		}
	}

	
	/** 
	 * 删除其他合同产品的扣费记录，当作冲销来计算
	* @param record
	* @author  author :Yao 
	* @date  2016年8月22日 上午11:36:01 
	* @version 1.0 
	*/
	public void pushChargeMsgToQueue(AccountChargeRecords record){
		IncomeMessage message=new IncomeMessage();
		message.setCampusId(record.getBlCampusId().getId());
		message.setAmount(record.getAmount());
		message.setQuantity(record.getQuality());
		message.setType(record.getProductType());
		message.setChargePayType(ChargePayType.WASH);//如果是删除，就相当于要把原来的费用给冲销掉，所以设为冲销，在上面的查询中过滤已经被冲销过的记录跟冲销记录，剩下的就是正常的记录
		message.setChargeType(record.getChargeType());
		message.setPayType(record.getPayType());
		message.setCountDate(record.getTransactionTime());
		
		try {// 加入队列
			JedisUtil.lpush(ObjectUtil.objectToBytes("incomeQueue"),ObjectUtil.objectToBytes(message));
		} catch (Exception e) {
			// 错了
		}
		
	}
}
