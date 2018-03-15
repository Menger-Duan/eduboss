package com.eduboss.dao.impl;

import com.eduboss.common.PayWay;
import com.eduboss.dao.FundsChangeHistoryBusinessDao;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.domain.Contract;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.FundsChangeHistoryBusiness;
import com.eduboss.domain.Student;
import com.eduboss.domainJdbc.FundsChangeHistoryJdbc;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.FundsChangeSearchVo;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("com.eduboss.dao.FundsChangeHistoryDao")
public class FundsChangeHistoryDaoImpl extends GenericDaoImpl<FundsChangeHistory, String> implements FundsChangeHistoryDao {
	
	@Autowired
	private UserService userService;

    @Autowired
    private RoleQLConfigService roleQLConfigService;
    
    @Autowired
    private FundsChangeHistoryBusinessDao fundsChangeHistoryBusinessDao;
	
    @Override
	public DataPackage listFundsChangeHistory(DataPackage dp,
											  FundsChangeSearchVo vo) {
		Map<String, Object> params = new HashMap<String, Object>();
    	// 组装 hql 语句
		StringBuffer sql = new StringBuffer(" SELECT f.* from funds_change_history f LEFT JOIN user u_audit ON f.AUDIT_USER = u_audit.USER_ID, ");
		StringBuffer sqlFrom = new StringBuffer(" contract c, student s LEFT JOIN data_dict dd_g ON dd_g.ID = s.Grade_id, customer cus, user u_sign, user u_charge,organization o  ");
		StringBuffer sqlJoin = new StringBuffer(" WHERE 1=1 AND f.CONTRACT_ID = c.ID AND c.STUDENT_ID = s.ID AND cus.ID = c.CUSTOMER_ID AND c.SIGN_STAFF_ID = u_sign.USER_ID AND f.CHARGE_USER_ID = u_charge.USER_ID and o.id = f.fund_campus_id ");
		StringBuffer sqlWhere = new StringBuffer();
		if(StringUtils.isNotEmpty(vo.getStartDate())) {
			sqlWhere.append(" and f.RECEIPT_TIME >= :startDate ");
			params.put("startDate", vo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotEmpty(vo.getEndDate())) {
			sqlWhere.append(" and f.RECEIPT_TIME <= :endDate ");
			params.put("endDate", vo.getEndDate() + " 23:59:59");
		}
		if(StringUtils.isNotEmpty(vo.getCusName())) {
			sqlWhere.append(" and cus.NAME like :cusName ");
			params.put("cusName", "%" + vo.getCusName() + "%");
		}
		if(StringUtils.isNotEmpty(vo.getStuName())) {
			sqlWhere.append(" and s.NAME like :stuName ");
			params.put("stuName", "%" + vo.getStuName() + "%");
		}
		if(StringUtils.isNotEmpty(vo.getSignByWho())) {
			sqlWhere.append(" and u_sign.NAME like :signByWho ");
			params.put("signByWho", "%" + vo.getSignByWho() + "%");
		}
		if (StringUtils.isNotBlank(vo.getAuditUserName())) {
			sqlWhere.append(" and u_audit.NAME like :auditUserName ");
			params.put("auditUserName", "%" + vo.getAuditUserName() + "%");
		}
		if(StringUtils.isNotEmpty(vo.getGradeId())) {
			sqlWhere.append(" and s.Grade_id = :gradeId ");
			params.put("gradeId", vo.getGradeId());
		}
		if(StringUtils.isNotBlank(vo.getContractType())) {
			sqlWhere.append(" and c.CONTRACT_TYPE = :contractType ");
			params.put("contractType", vo.getContractType());
		}
		if(StringUtils.isNotEmpty(vo.getContractId())) {
			sqlWhere.append(" and c.id like :contractId ");
			params.put("contractId", "%" + vo.getContractId() + "%");
		}
		if(StringUtils.isNotEmpty(vo.getChannel())) {
			if (vo.getChannel().equals("WASH")) {
				sqlWhere.append(" and f.FUNDS_PAY_TYPE = :channel ");
				params.put("channel", vo.getChannel());
			} else {
				sqlWhere.append(" and f.CHANNEL = :channel ");
				params.put("channel", vo.getChannel());
			}
		}
			
		if(StringUtils.isNotEmpty(vo.getCheckbyWho())) {
			sqlWhere.append(" and u_charge.name like :checkbyWho ");
			params.put("checkbyWho", "%" + vo.getCheckbyWho() + "%");
		}
		if(StringUtils.isNotEmpty(vo.getBlCampusId())) {
			sqlWhere.append(" and f.fund_campus_id = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		}
		if(StringUtils.isNotEmpty(vo.getAuditStatusValue())){
			sqlWhere.append(" and f.AUDIT_STATUS = :auditStatusValue ");
			params.put("auditStatusValue", vo.getAuditStatusValue());
		}
		if (StringUtils.isNotBlank(vo.getAuditType())) {
			sqlWhere.append(" and f.AUDIT_TYPE = :auditType ");
			params.put("auditType", vo.getAuditType());
		}

		if(StringUtils.isNotBlank(vo.getFundsChangeType())){
			sqlWhere.append(" and f.FUNDS_CHANGE_TYPE = :fundsChangeType ");
			params.put("fundsChangeType", vo.getFundsChangeType());
		}

		if (StringUtils.isNotEmpty(vo.getAssignedAmountNotZERO())){
			if ("campusNotZero".equals(vo.getAssignedAmountNotZERO())){//待分配校区业绩不为0
				sqlWhere.append(" and f.cam_not_assigned_amount <> 0");
			}
			if ("userNotZero".equals(vo.getAssignedAmountNotZERO())){//待分配个人业绩不为0
				sqlWhere.append(" and f.user_not_assigned_amount <> 0");
			}
//			if ("camAndUserNotZero".equals(assignedAmountNotZERO)){
//				sqlWhere.append(" and f.cam_not_assigned_amount <> 0");
//				sqlWhere.append(" and f.user_not_assigned_amount <> 0");
//			}
			if ("camOrUserNotZero".equals(vo.getAssignedAmountNotZERO())){//待分配校区个人业绩存在不为0
				sqlWhere.append(" and (f.cam_not_assigned_amount <> 0 or f.user_not_assigned_amount <> 0 ) ");
			}

		}


		sqlWhere.append(" and f.CHANNEL <> 'PROMOTION_MONEY'");
		sqlWhere.append(" and f.CHANNEL <> 'REFUND_MONEY'");
		sqlWhere.append(" and f.CHANNEL <> 'FEEDBACK_REFUND'");
		
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("funUserId","c.SIGN_STAFF_ID");
		sqlMap.put("manegerId","s.STUDY_MANEGER_ID");
		sqlMap.put("stuId","s.id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("收款记录","nsql","sql");
		sqlWhere.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		sql.append(sqlFrom).append(sqlJoin).append(sqlWhere);
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sql.append(" ORDER BY f.RECEIPT_TIME DESC, f.TRANSACTION_TIME ASC");
			sql.append(", "+dp.getSidx()+" "+dp.getSord());
		} else {
			sql.append(" ORDER BY f.RECEIPT_TIME DESC, f.TRANSACTION_TIME ASC ");
		}
		dp=super.findPageBySql(sql.toString(), dp, true, params);
		return dp;
	}
	
	@Override
	public void save(FundsChangeHistory fundsChangeHistory) {
		if (StringUtil.isBlank(fundsChangeHistory.getId())) {
			FundsChangeHistoryBusiness business = new FundsChangeHistoryBusiness();
			fundsChangeHistoryBusinessDao.save(business);
			String businessId = "";
			if (business.getId() > 0) {
				businessId += "FUN" + StringUtil.numberPadding0(business.getId(), 10);
			}
			fundsChangeHistory.setId(businessId);
		}
		super.save(fundsChangeHistory);
	}

	@Override
	public Double historySumFundsChange(String contractId) {
		String hql=" select sum(case when fundsPayType = 'RECEIPT' then transactionAmount else (-transactionAmount) end)  from FundsChangeHistory where channel <> 'PROMOTION_MONEY' and  contract.id='"+contractId+"'";

		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql)
		.setFirstResult(0)
		.setMaxResults(1);
		List<Object> list =q.list();
		if(list == null || list.size()==0 || list.get(0) == null)
			return 0d;
		return Double.valueOf( list.get(0).toString());
	}
	
	/**
	 * 根据事务id计算冲销总计
	 */
	@Override
	public BigDecimal getWashSumFundsByTransactionId(String transactionId) {
		String hql=" select sum(transactionAmount)  from FundsChangeHistory where channel <> 'PROMOTION_MONEY' and fundsPayType = 'WASH' and  transactionId='"+transactionId+"'";

		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql)
		.setFirstResult(0)
		.setMaxResults(1);
		List<Object> list =q.list();
		if(list == null || list.size()==0 || list.get(0) == null)
			return BigDecimal.ZERO;
		return new BigDecimal(list.get(0).toString());
	}

	@Override
	public void saveOneFundRecord(BigDecimal refundMoney, PayWay payWay, Student student) {
		FundsChangeHistory fundsChangeHistory = new FundsChangeHistory();
		fundsChangeHistory.setRemark("");
		fundsChangeHistory.setTransactionAmount(refundMoney);
		fundsChangeHistory.setTransactionTime(DateTools.getCurrentDateTime());
		fundsChangeHistory.setReceiptTime(DateTools.getCurrentDateTime());
		fundsChangeHistory.setContract(null);
		fundsChangeHistory.setChannel(payWay);
		fundsChangeHistory.setStudent(student);
		fundsChangeHistory.setChargeBy(userService.getCurrentLoginUser());
		// 需要历史记录？
		// Double historySum = this.historySumFundsChange(contract.getId());
		// fundsChangeHistory.setPaidAmount(BigDecimal.valueOf(historySum).add(amount));
		this.save(fundsChangeHistory);
		this.flush();
	}

	@Override
	public void deletePromotionRecord(Contract contract) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("contractId", contract.getId());
		StringBuffer hql = new StringBuffer(" from FundsChangeHistory f where 1=1 ");
		hql.append(" and f.contract.id = :contractId ");
		hql.append(" and f.channel = 'PROMOTION_MONEY'");
		List<FundsChangeHistory> funds = this.findAllByHQL(hql.toString(),params);
		for(FundsChangeHistory fundsHist : funds) {
			this.delete(fundsHist);
		}
		
	}

	/**
	 * 根据合同ID查找收入记录
	 */
	@Override
	public List<FundsChangeHistory> getFundsChangeHistoryListByContractId(String contractId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("contractId", contractId);
		StringBuffer hql = new StringBuffer(" from FundsChangeHistory f where 1=1 ");
		hql.append(" and f.contract.id = :contractId and f.channel != 'PROMOTION_MONEY'");
		List<FundsChangeHistory> funds = this.findAllByHQL(hql.toString(),params);
		return funds;
	}
	
	/**
	 * 根据学生ID查找收入记录
	 */
	@Override
	public List<FundsChangeHistory> getFundsChangeHistoryListByStudentId(String studentId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("studentId", studentId);
		StringBuffer hql = new StringBuffer(" from FundsChangeHistory f where 1=1 ");
		hql.append(" and f.contract.id in (select id from Contract where student.id = :studentId ) ");
		List<FundsChangeHistory> funds = this.findAllByHQL(hql.toString(),params);
		return funds;
	}
	
	/**
	 * 检查收款记录posId和posNumber是否唯一
	 */
	@Override
	public boolean isPosIAndPosNumberdUnique(String id, String posId, String posNumber) {
		Map<String, Object> params = Maps.newHashMap();
		
		String sql = "select count(1) from funds_change_history where 1=1 ";
		if (StringUtils.isNotBlank(id)) {
			sql += "and id <> :id ";
			params.put("id", id);
		}
		sql += " and POS_ID = :posId and POS_NUMBER = :posNumber ";
		params.put("posId", posId);
		params.put("posNumber", posNumber);
		int result = super.findCountSql(sql,params);
		if (result > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 根据收款记录计算有多少不能分配到合同的金额
	 *
	 * @param contractId
	 * @return
	 */
	@Override
	public BigDecimal sumCanNotAssignAmount(String contractId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select IFNULL(sum(result.TRANSACTION_AMOUNT),0) as amount  FROM (SELECT TRANSACTION_AMOUNT, CHANNEL, FUNDS_PAY_TYPE, AUDIT_STATUS FROM funds_change_history WHERE CONTRACT_ID='");
		sql.append(contractId);
		sql.append("'  GROUP BY TRANSACTION_ID HAVING count(*)=1) AS result WHERE result.CHANNEL in ('WEB_CHART_PAY', 'ALI_PAY') AND result.FUNDS_PAY_TYPE='RECEIPT' AND result.AUDIT_STATUS='UNVALIDATE'");
		List list = this.findMapBySql(sql.toString(),params);
		if (list.size()>0){
			Map map = (Map)list.get(0);
			BigDecimal amount = (BigDecimal)map.get("amount") ;
			return amount;
		}
		return BigDecimal.ZERO;
	}
}
