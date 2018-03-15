package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.MatchingStatus;
import com.eduboss.dao.PosPayDataDao;
import com.eduboss.domain.PosPayData;
import com.eduboss.domainVo.PosPayDataVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("PosPayDataDao")
public class PosPayDataDaoImpl   extends GenericDaoImpl<PosPayData, Integer> implements PosPayDataDao {
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	/*
	 * 查找银联反馈支付数据列表
	 */
	@Override
	public DataPackage findPagePosPayData(DataPackage dp, PosPayDataVo posPayDataVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select ppd from PosPayData ppd " );
		StringBuffer hqlWhere = new StringBuffer(" where 1=1 ") ;
		if (StringUtils.isNotBlank(posPayDataVo.getPosType())) {
		    hql.append(" ,PosMachineManage pmm  ");
		    hqlWhere.append(" AND ppd.posNumber = pmm.posNumber AND pmm.posType.value = :posType ");
            params.put("posType", posPayDataVo.getPosType());
		}
		if (StringUtils.isNotBlank(posPayDataVo.getBlBrenchId())) {
		    hqlWhere.append(" AND ppd.blCampus.parentId = :blBrenchId ");
			params.put("blBrenchId", posPayDataVo.getBlBrenchId());
		}
		if (StringUtils.isNotBlank(posPayDataVo.getBlCampusId())) {
		    hqlWhere.append(" AND ppd.blCampus.id = :blCampusId ");
			params.put("blCampusId", posPayDataVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(posPayDataVo.getPosNumber())) {
		    hqlWhere.append(" AND ppd.posNumber LIKE :posNumber ");
			params.put("posNumber", "%" + posPayDataVo.getPosNumber() + "%");
		}
		if (StringUtils.isNotBlank(posPayDataVo.getPosId())) {
		    hqlWhere.append(" AND ppd.posId LIKE :posId ");
			params.put("posId", "%" + posPayDataVo.getPosId() + "%");
		}
		if (posPayDataVo.getAmount() != null && posPayDataVo.getAmount().compareTo(BigDecimal.ZERO) > 0) {
		    hqlWhere.append(" AND ppd.amount = :amount ");
			params.put("amount", posPayDataVo.getAmount());
		}
		if (StringUtils.isNotBlank(posPayDataVo.getMerchantName())) {
		    hqlWhere.append(" AND ppd.merchantName LIKE :merchantName ");
			params.put("merchantName", "%" + posPayDataVo.getMerchantName() + "%");
		}
		if (StringUtils.isNotBlank(posPayDataVo.getStartDate())) {
		    hqlWhere.append(" AND date_format(ppd.posTime, '%Y%m%d%H%i%s') >= :startDate ");
			params.put("startDate", posPayDataVo.getStartDate().replaceAll("-", "") + "000000");
		}
		if (StringUtils.isNotBlank(posPayDataVo.getEndDate())) {
		    hqlWhere.append(" AND date_format(ppd.posTime, '%Y%m%d%H%i%s') <= :endDate ");
			params.put("endDate",posPayDataVo.getEndDate().replaceAll("-", "") + "235959");
		}
		if (StringUtils.isNotBlank(posPayDataVo.getMatchingStatusValue())) {
		    hqlWhere.append(" AND ppd.matchingStatus = :matchingStatus ");
			params.put("matchingStatus", MatchingStatus.valueOf(posPayDataVo.getMatchingStatusValue()));
		}
		hqlWhere.append(roleQLConfigService.getAppendSqlByAllOrg("已导入银联数据","hql","ppd.blCampus.id"));
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
		    hqlWhere.append(" order by ppd." + dp.getSidx() + " " + dp.getSord());
		}
		hql.append(hqlWhere);
		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
}
