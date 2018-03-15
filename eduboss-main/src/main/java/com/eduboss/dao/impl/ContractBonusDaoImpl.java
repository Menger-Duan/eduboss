package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eduboss.common.BonusType;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ProductType;
import com.eduboss.dao.ContractBonusDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.UserOrganizationDao;
import com.eduboss.domain.ContractBonus;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.ContractBonusVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

@Transactional
@Repository
public class ContractBonusDaoImpl extends GenericDaoImpl<ContractBonus, String> implements
	ContractBonusDao {
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	@Autowired
	private UserOrganizationDao userOrganizationDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<ContractBonus> findByFundsChangeHistoryId(String fundsChangeHistoryId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Restrictions.eq("fundsChangeHistory.id",fundsChangeHistoryId));
		List<ContractBonus> bonusList = super.findAllByCriteria(criterionList);
		return bonusList;
	}

	@Override
	public void deleteByFundsChangeHistoryId(String fundsChangeHistoryId) {
		List<ContractBonus> bonuses = findByFundsChangeHistoryId(fundsChangeHistoryId);
		for(ContractBonus bonus : bonuses) {
			super.delete(bonus);
		}
	}
	
	/**
	 * 根据收款ID和业绩分配类型删除业绩分配
	 */
	@Override
	public void deleteByFundsChangeHistoryId(String fundsChangeHistoryId, String type) {
		Map<String, Object> params = Maps.newHashMap();
		String hql = "delete from ContractBonus where fundsChangeHistory.id = :fundsChangeHistoryId ";
		params.put("fundsChangeHistoryId", fundsChangeHistoryId);
		if (type.equals("bonus")) {
			hql += " and bonusAmount > 0 ";
		} else {
			hql += " and campusAmount > 0 ";
		}
		super.excuteHql(hql, params);
	}

	/**
	 * 合同业绩列表
	*/
	@Transactional
	@Override
	public DataPackage getContractBonusList(DataPackage dp,
			ContractBonusVo contractBonus,Map params) {

		Map<String, Object> map = Maps.newHashMap();

		StringBuilder hql = new StringBuilder();
		hql.append(" from IncomeDistribution where 1=1");
		if(StringUtil.isNotBlank(contractBonus.getBonusStaffName())){
			hql.append(" and bonusStaff.name like :bonusStaffName ");
			map.put("bonusStaffName", "%"+contractBonus.getBonusStaffName()+"%");
		}
		if(StringUtil.isNotBlank(contractBonus.getOrganizationId())){
			hql.append(" and bonusOrg.id = :organizationId ");
			map.put("organizationId", contractBonus.getOrganizationId());
		}
		if(StringUtil.isNotBlank(params.get("startDate").toString())){
			hql.append(" and createTime >=  :startDate ");
			map.put("startDate", DateTools.getDateTime(params.get("startDate").toString()+" 00:00 "));
		}
		if(StringUtil.isNotBlank(params.get("endDate").toString())){
			hql.append(" and createTime <= :endDate ");
			map.put("endDate", DateTools.getDateTime(params.get("endDate").toString()+" 23:59 "));
		}
		
		
		if(StringUtils.isNotBlank(contractBonus.getTypeId())){
			hql.append(" and productType = :typeId ");
			map.put("typeId", ProductType.valueOf(contractBonus.getTypeId()));
		}
		if(StringUtils.isNotBlank(contractBonus.getBonusTypeId())){
			hql.append(" and bonusType = :bonusTypeId ");
			map.put("bonusTypeId", BonusType.valueOf(contractBonus.getBonusTypeId()));
		}
		if(StringUtils.isNotBlank(contractBonus.getBonusStaffCampusId())){
			hql.append(" and bonusStaffCampus.id = :bonusStaffCampusId ");
			map.put("bonusStaffCampusId", contractBonus.getBonusStaffCampusId());
		}
		if(StringUtils.isNotBlank(contractBonus.getContractCampusId())){
			hql.append(" and contractCampus.id = :contractCampusId ");
			map.put("contractCampusId", contractBonus.getContractCampusId());
		}
		
//		hql.append("and ( contractCampus.id in (select id from Organization organization where "+hqlOrg+" ) or contractCampus.id is null )");
//		hql.append("and ( organization.id in (select id from Organization organization where "+hqlOrg+" ) or organization.id is null )");
		
		hql.append(" order by createTime desc, id desc ");
		return super.findPageByHQL(hql.toString(), dp, true, map);
	}

	@Override
	public List<ContractBonus> findByStudentReturnId(String studentReturnId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Restrictions.eq("studentReturnFee.id",studentReturnId));
		List<ContractBonus> bonusList = super.findAllByCriteria(criterionList);
		return bonusList;
	}

	@Override
	public void deleteByStudentReturnId(String StudentReturnId) {
		List<ContractBonus> bonuses = findByStudentReturnId(StudentReturnId);
		for(ContractBonus bonus : bonuses) {
			super.delete(bonus);
		}
	}
	
	
	@Override
	public List<ContractBonus> findByContractIdExeptFundId(String contractId,String fundId,String productType) {
		StringBuilder str=new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		str.append(" from ContractBonus where 1=1 and bonusType='NORMAL'");
		if(StringUtils.isNotBlank(fundId)){
			str.append(" and fundsChangeHistory.id<> :fundId ");
			params.put("fundId", fundId);
		}

		if(StringUtils.isNotBlank(contractId)){
			str.append(" and fundsChangeHistory.contract.id= :contractId ");
			params.put("contractId", contractId);
		}

		if (StringUtils.isNotBlank(productType)){
			str.append(" and type= :productType ");
			params.put("productType", productType);
		}
		
		str.append(" order by createTime desc ");
		
		return this.findAllByHQL(str.toString(), params);
	}

}
