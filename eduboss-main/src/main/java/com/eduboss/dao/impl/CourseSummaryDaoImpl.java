package com.eduboss.dao.impl;

import com.eduboss.common.OrganizationType;
import com.eduboss.dao.CourseSummaryDao;
import com.eduboss.domain.CourseSummary;
import com.eduboss.domain.Organization;
import com.eduboss.dto.CourseSummarySearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("CourseSummary")
public class CourseSummaryDaoImpl extends GenericDaoImpl<CourseSummary, String>
		implements CourseSummaryDao {

	@Autowired
	private UserService userService;

	// 排课需求
	@Override
	public DataPackage getCourseSummaryList(
			CourseSummarySearchInputVo courseSummarySearchInputVo,
			DataPackage dp) {

		List<Criterion> criterionList = new ArrayList<Criterion>();
        criterionList.add(Restrictions.eq("delFlag",0)); // 只查询未逻辑删除的
        if(courseSummarySearchInputVo.getEnd()!=null&&courseSummarySearchInputVo.getEnd()){ //查询未结束的课程
            criterionList.add(Restrictions.ge("endDate", DateTools.getCurrentDate()));
        }

		if (StringUtils.isNotBlank(courseSummarySearchInputVo.getStartDate())) {
			criterionList.add(Restrictions.ge("startDate",
					courseSummarySearchInputVo.getStartDate()));
		}
		if (StringUtils.isNotBlank(courseSummarySearchInputVo.getEndDate())) {
			criterionList.add(Restrictions.le("endDate",
					courseSummarySearchInputVo.getEndDate()));
		}
		if(StringUtils.isNotBlank(courseSummarySearchInputVo.getCourseTime())){
			criterionList.add(Restrictions.eq("courseTime",
					courseSummarySearchInputVo.getCourseTime()));
		}
		if (StringUtils.isNotBlank(courseSummarySearchInputVo.getStudentId())) {
			criterionList.add(Restrictions.eq("student.id",
					courseSummarySearchInputVo.getStudentId()));
		}
		if (StringUtils.isNotBlank(courseSummarySearchInputVo.getTeacherName())) {
			criterionList.add(Restrictions.like("teacher.name",
					courseSummarySearchInputVo.getTeacherName(),
					MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(courseSummarySearchInputVo.getGrade())) {
			criterionList.add(Restrictions.eq("grade.value",
					courseSummarySearchInputVo.getGrade()));
		}
		if (StringUtils.isNotBlank(courseSummarySearchInputVo.getSubject())) {
			criterionList.add(Restrictions.eq("subject.value",
					courseSummarySearchInputVo.getSubject()));
		}
        if (StringUtils.isNotBlank(courseSummarySearchInputVo.getWeekDay())) {
            criterionList.add(Restrictions.eq("weekDay",
                    courseSummarySearchInputVo.getWeekDay()));
        }
		
		if (StringUtils.isNotBlank(courseSummarySearchInputVo.getStudyManagerName())) {
			// 多级criteria查询时，对象之间使用 HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG 常量分开（GenericDaoImpl中实现，非hibernate原生）
			StringBuffer prop = new StringBuffer();
			prop.append("student")
			.append(HibernateUtils.CRITERIA_CASCADE_CONCAT_FLAG)
			.append("studyManeger")
			.append(".name");
			criterionList.add(Restrictions.like(prop.toString(),courseSummarySearchInputVo.getStudyManagerName(),
					MatchMode.ANYWHERE));
		}
		
		//权限控制，只能看到本校区的
		Organization campus = userService.getBelongCampus();
		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
			criterionList.add(Restrictions.eq("student.blCampusId", campus.getId()));
		}

		return super.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "createTime", "desc"),
				criterionList);
	}

	@Override
	public int countCourseOfDummyTeacher(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return findCountHql("select count(*) from CourseSummary where teacher.id = :userId", params);
	}

}
