package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.RefundContractDao;
import com.eduboss.domain.RefundContract;
import com.eduboss.domainVo.RefundContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("RefundContractDao")
public class RefundContractDaoImpl extends GenericDaoImpl<RefundContract, String> implements RefundContractDao {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleQLConfigService roleQLConfigService;

	@Override
	public DataPackage getPageRefundContracts(DataPackage dp,
			RefundContractVo refundContractVo, TimeVo timeVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append( "from RefundContract as refundContract ");
		hql.append(" where 1 = 1 ");
        // 学生
        if(StringUtils.isNotBlank(refundContractVo.getStudentName())){
            hql.append(" and contract.student.name like :studentName ");
            params.put("studentName", "%" + refundContractVo.getStudentName() + "%");
        }
        // 申请人
        if(StringUtils.isNotBlank(refundContractVo.getCreateUserName())){
            hql.append(" and userByCreateUserId.name like :createUserName ");
            params.put("createUserName", "%" + refundContractVo.getCreateUserName() + "%");
        }
        if (StringUtils.isNotBlank(refundContractVo.getStuBlCampusId())) {
			hql.append(" and contract.student.blCampusId = '").append(refundContractVo.getStuBlCampusId()).append("'");
		}
        hql.append(roleQLConfigService.getAppendSqlByAllOrg("退费合同列表","hql","contract.student.blCampusId"));
		dp =  this.findPageByHQL(hql.toString(), dp, true, params);
		return dp;
	}
	
}
