package com.eduboss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;
import com.eduboss.domain.Contract;
import com.eduboss.domain.Organization;
import com.eduboss.domain.SummaryCampus;
import com.eduboss.domainVo.SummaryCampusVo;
import com.eduboss.utils.HibernateUtils;

@Repository
public interface SummaryCampusDao extends GenericDAO<SummaryCampus, String> {

	/**
	 * 取单个校区总结表
	 * @param summaryCampusVo
	 * @return
	 */
	public SummaryCampus getOneSummaryCampus(SummaryCampusVo summaryCampusVo);
}
