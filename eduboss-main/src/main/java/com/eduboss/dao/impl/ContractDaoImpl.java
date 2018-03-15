package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eduboss.dao.ContractBusinessDao;
import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.domain.ContractBusiness;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ContractType;
import com.eduboss.dao.ContractDao;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Repository
public class ContractDaoImpl extends GenericDaoImpl<Contract, String> implements  ContractDao{


	@Autowired
	private ContractBusinessDao contactBusinessDao;

	@Override
	public void save(Contract contract) {
		if (StringUtil.isBlank(contract.getId())) {
			ContractBusiness business = new ContractBusiness();
			contactBusinessDao.save(business);
			String businessId = "";
			if (business.getId() > 0) {
				businessId += "CON" + StringUtil.numberPadding0(business.getId(), 10);
			}
			contract.setId(businessId);
		}
		super.save(contract);
	}

	/**
	 * 根据学生id查询最新的一条合同
	 * @param studentId
	 * @return
	 */
	public Contract getContractByStudentId(String studentId){
		Map<String, Object> params = Maps.newHashMap();
		String hql=" from Contract where student.id= :studentId order by createTime desc ";
		params.put("studentId", studentId);
		List<Contract> list=this.findAllByHQL(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	
	
	@Override
	public List<Contract> getPaidNormalDeposits(String stuId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Restrictions.eq("student.id",stuId));
		criterionList.add(Restrictions.isNull("referenceContract"));
//		criterionList.add(Restrictions.eq("contractType",ContractType.DEPOSIT));
		criterionList.add(Restrictions.eq("contractStatus",ContractStatus.NORMAL));
		criterionList.add(Restrictions.eq("paidStatus",ContractPaidStatus.PAID));
		List<Contract> deposits = super.findAllByCriteria(criterionList);
		return deposits;
	}

	/**
	 * 根据学生id查询 一对一 剩余课时>0的合同
	 * @param studentId
	 * @return
	 */
	@Override
	public List<Contract> getNewContractByStudentId(String studentId){
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("select contract from Contract contract inner join contract.contractProducts as contractProduct " ) 
		.append(" where contractProduct.type = 'ONE_ON_ONE_COURSE' and contract.student.id= :studentId ");

		params.put("studentId", studentId);
		List<Contract> list=this.findAllByHQL(hql.toString(), params);

		return list;
	}

	@Override
	public void updateRefundContract(String parentConId, String contractIds,String userId) {
		Map<String, Object> params = Maps.newHashMap();
		//String ids="";
		List<String> ids = new ArrayList<>();
		for(String id:StringUtil.replaceSpace(contractIds).split(",")){
			//ids+=",'"+id.trim()+"'";
			ids.add(id.trim());
		}
		String hql=" update Contract set contractStatus= :contractStatus ,referenceContract.id= :parentConId " +
				",createTime= :createTime ,createByStaff= :createByStaff where id in (:ids) ";
		params.put("contractStatus", ContractStatus.FINISHED);
		params.put("parentConId", parentConId);
		params.put("createTime", DateTools.getCurrentDateTime());
		params.put("createByStaff", userId);
		params.put("ids", ids);
		super.excuteHql(hql, params);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> getOrderContracts(String stuId, Order order) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criterias = session.createCriteria(Contract.class)
			    .add( Restrictions.eq("student.id", stuId))
			    .add(Restrictions.in("contractType", new ContractType[]{ContractType.NEW_CONTRACT, ContractType.RE_CONTRACT, ContractType.INIT_CONTRACT}))
			    .add(Restrictions.eq("contractStatus",ContractStatus.NORMAL))
			    //只用合同的状态 来判断
//				.add(Restrictions.or (Restrictions.ge("remainingAmount",BigDecimal.ZERO),Restrictions.ge("freeRemainingHour",BigDecimal.ZERO) ))
//			    .add(Restrictions.eq("paidStatus",ContractPaidStatus.PAID))
//			    .addOrder(order);
			    ;
		if(order != null) {
			criterias.addOrder(order);
		}
		List<Contract> list = criterias.list();
		return list;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> findSpecificMiniClassContractsByStudentId(
			String studentId, String productId) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<Contract> list = session.createCriteria(Contract.class)
			    .add( Restrictions.eq("student.id", studentId))
			    .add(Restrictions.or(Restrictions.eq("contractType",ContractType.NEW_CONTRACT), Restrictions.eq("contractType",ContractType.RE_CONTRACT)))
			    .add(Restrictions.eq("contractStatus",ContractStatus.NORMAL))
			    .add(Restrictions.eq("paidStatus",ContractPaidStatus.PAID))
			    .list();
		List<Contract> returnList =  new ArrayList<Contract>();
		for(Contract contract : list ) {
			for(ContractProduct conProduct: contract.getContractProducts()) {
				if(conProduct.getProduct().getId().equals(productId)) {
					returnList.add(contract);
					break;
				}
			}
		}
		return returnList;
	}



	@Override
	public Contract getLastedContracts(String stuId) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criterias = session.createCriteria(Contract.class)
			    .add(Restrictions.eq("student.id", stuId))
			    .addOrder(Order.desc("createTime"))
		 		.setFirstResult(0).setMaxResults(1);
		Contract contract = null;
		if (criterias.list().size() > 0) {
			contract = (Contract) criterias.list().get(0);
		}
		return contract;
	}



	/**
	 * 根据客户查询合同
	 * */
	@Override
	public List<Contract> findContractByCustomer(String customerId, String paidStatus, String contractStatus) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from Contract where 1=1 ");
		//客户ID
		if(StringUtil.isNotEmpty(customerId)){
			hql.append(" and customer.id = :customerId ");
			params.put("customerId", customerId);
		}
		//合同支付状态
		if(StringUtil.isNotEmpty(paidStatus)){
			hql.append(" and paidStatus = :paidStatus ");
			params.put("paidStatus", paidStatus);
		}
		if(StringUtil.isNotEmpty(contractStatus)){
			hql.append(" and contractStatus = :contractStatus ");
			params.put("contractStatus", contractStatus);
		}
		hql.append(" order by createTime asc ");
		return super.findAllByHQL(hql.toString(), params);
	}
	
	@Override
	public boolean checkHasFrozenCp(Contract contract) {
    	for (ContractProduct cp : contract.getContractProducts()) {
    		if (cp.getIsFrozen() == 0) {
    			return true;
    		}
    	}
    	return false;
    }
	
	@Override
	public int countContractByCustomer(String customerId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select count(*) as amount from contract where customer_id = :customerId ");
		params.put("customerId", customerId);
		List<Map<Object, Object>> list = this.findMapBySql(sql.toString(), params);
		return Integer.parseInt(list.get(0).get("amount").toString());
	}
	
	
}
