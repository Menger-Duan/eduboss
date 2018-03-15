package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.CourseDateTemplateDao;
import com.eduboss.domain.CourseDateTemplate;
import com.eduboss.domain.User;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.CourseDateTemplateService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service
public class CourseDateTemplateServiceImpl implements CourseDateTemplateService {

	@Autowired
	private CourseDateTemplateDao courseDateTemplateDao;
	
	@Autowired
	private UserService userService;

	@Override
	public DataPackage getCourseDateTemplateList(CourseDateTemplate courseDateTemplate, DataPackage dp) {
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(courseDateTemplate);
		dp = courseDateTemplateDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "createTime", "desc"), criterionList);
		return dp;
	}

	@Override
	public void saveOrUpdateCourseDateTemplate(CourseDateTemplate courseDateTemplate) {
		User user = userService.getCurrentLoginUser();
		courseDateTemplate.setModifyTime(DateTools.getCurrentDateTime());
		courseDateTemplate.setModifyUserId(user.getUserId());
		if (courseDateTemplate.getTemplateId() == null) {
			courseDateTemplate.setCreateTime(DateTools.getCurrentDateTime());
			courseDateTemplate.setCreateUserId(user.getUserId());
		}
		courseDateTemplateDao.save(courseDateTemplate);
	}

	@Override
	public void deleteCourseDateTemplate(CourseDateTemplate courseDateTemplate) {
		courseDateTemplateDao.deleteById(courseDateTemplate.getTemplateId());
	}
	
	@Override
	public List<CourseDateTemplate> getCourseDateTemplateByUserIdAndDate(String userId, String startDate, String endDate) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		Criterion criterion = Expression.eq("userId", userId);
		criterionList.add(criterion);
		Criterion startCrit = null;
		if (StringUtils.isNotEmpty(startDate)) {
			//criterionList.add(Expression.le("startDate", startDate));
			criterionList.add(Expression.ge("endDate", startDate));
			//startCrit = Expression.and(Expression.le("startDate", startDate), Expression.ge("endDate", startDate));
			//startCrit =  Expression.ge("endDate", startDate);
		}
		Criterion endCrit = null;
		if (StringUtils.isNotEmpty(endDate)) {
			criterionList.add(Expression.le("startDate", endDate));
			//criterionList.add(Expression.ge("endDate", endDate));
			//endCrit = Expression.and(Expression.le("startDate", endDate), Expression.ge("endDate", endDate));
			//endCrit = Expression.le("startDate", endDate);
		}
		//if (startCrit != null && endCrit != null){
		//	criterionList.add(Expression.or(startCrit, endCrit));
		//}else if(startCrit != null){
		//	criterionList.add(startCrit);
		//}else if(endCrit != null){
		//	criterionList.add(endCrit);
		//}
		return courseDateTemplateDao.findAllByCriteria(criterionList);
	}

	public static void main(String[] args) {
		System.out.println(DateTools.getCurrentDate());;
	}
}
