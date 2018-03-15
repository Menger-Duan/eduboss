package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerImportTransformDao;
import com.eduboss.domain.CustomerImportTransform;
import com.eduboss.domain.User;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;

@Repository("CustomerImportTransformDao")
public class CustomerImportTransformDaoImpl extends GenericDaoImpl<CustomerImportTransform, String> implements CustomerImportTransformDao  {

	@Autowired
	private UserService userService;
	
	@Override
	public DataPackage getCustomerImportInfo(CustomerImportTransform cif,String startDate,String endDate,String gradeName,
			DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		User user=userService.getCurrentLoginUser();
		sql.append(" select * from customer_import_transform  where 1=1 ");
		if(StringUtils.isNotBlank(cif.getCusStatus())){
			sql.append(" and CUS_STATUS = :cusStatus ");
			params.put("cusStatus", cif.getCusStatus());
		}
		if(StringUtils.isNotBlank(startDate)){
			sql.append(" and  CREATE_TIME >= :startDate ");
			params.put("startDate", startDate + " 0000");
		}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and  CREATE_TIME<= :endDate ");
			params.put("endDate", endDate + " 2359");
		}
		if(cif.getCusContact()!=null && StringUtils.isNotBlank(cif.getCusContact())){
			sql.append(" and CONTACT LIKE :contact ");	
			params.put("contact", "%" + cif.getCusContact() + "%");
		}
		if(cif.getCusName()!=null && StringUtils.isNotBlank(cif.getCusName())){
			sql.append(" and CUS_NAME like :cusName ");
			params.put("cusName", "%" + cif.getCusName() + "%");
		}
		if(cif.getBlSchoolName()!=null && StringUtils.isNotBlank(cif.getBlSchoolName())){
			sql.append(" and SCHOOL_NAME LIKE :blSchoolName ");
			params.put("blSchoolName", "%" + cif.getBlSchoolName() + "%");
		}
		if(gradeName!=null && StringUtils.isNotBlank(gradeName) && !gradeName.equals("年级")){
			sql.append(" and GRADE_NAME = :gradeName ");
			params.put("gradeName", gradeName);
		}
		sql.append(" and CREATE_USER='"+user.getUserId()+"' ");
		sql.append(" order by create_time desc ");
		dp=this.findPageBySql(sql.toString(), dp, true, params);
		return dp;
	}
	
}
