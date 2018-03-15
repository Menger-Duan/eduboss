package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eduboss.common.OtmClassStudentChargeStatus;
import com.eduboss.dao.OtmClassStudentDao;
import com.eduboss.domain.OtmClassStudent;
import com.eduboss.domainVo.OtmClassStudentAttendentVo;
import com.eduboss.domainVo.OtmClassStudentVo;
import com.eduboss.dto.DataPackage;

@Repository("OtmClassStudentDao")
public class OtmClassStudentDaoImpl extends GenericDaoImpl<OtmClassStudent, Integer> implements OtmClassStudentDao {
	
	/**
	 * 获取一条一对多学生记录
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	@Override
	public OtmClassStudent getOneOtmClassStudent(String otmClassId, String studentId) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criterias = session.createCriteria(OtmClassStudent.class)
			    .add( Restrictions.eq("otmClass.otmClassId", otmClassId))
			    .add( Restrictions.eq("student.id", studentId))
			    .addOrder(Order.desc("createTime"))
		 		.setFirstResult(0).setMaxResults(1);
		OtmClassStudent otmClassStudent = null;
		if (criterias.list().size() > 0) {
			otmClassStudent = (OtmClassStudent) criterias.list().get(0);
		}
		return otmClassStudent;
	}
	
	/**
	 * 一对多详情-在读学生信息列表
	 * @param otmClassId
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOtmClassDetailStudentList(
			String otmClassId, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassStudent  where 1=1 ");
		if (StringUtils.isNotBlank(otmClassId)) {
			hql.append(" and otmClass.otmClassId = :otmClassId ");
			params.put("otmClassId", otmClassId);
		}
		hql.append(" order by FIRST_SCHOOL_TIME desc ");
		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
	/**
	 * 一对多学生考勤列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassStudentList(OtmClassStudentVo otmClassStudentVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassStudent  where 1=1 ");
		if (StringUtils.isNotBlank(otmClassStudentVo.getOtmClassId())) {
			hql.append(" and otmClass.otmClassId = :otmClassId ");
			params.put("otmClassId", otmClassStudentVo.getOtmClassId());
		}
		if (StringUtils.isNotBlank(otmClassStudentVo.getStudentId())) {
			hql.append(" and student.id = :studentId ");
			params.put("studentId", otmClassStudentVo.getStudentId());
		}
		if (null != otmClassStudentVo.getOtmClassStudentChargeStatus()) {
			hql.append(" and otmClassStudentChargeStatus = :otmClassStudentChargeStatus ");
			params.put("otmClassStudentChargeStatus", OtmClassStudentChargeStatus.valueOf(otmClassStudentVo.getOtmClassStudentChargeStatus()));
		}
		if(StringUtils.isNotBlank(otmClassStudentVo.getFirstSchoolTime())){
			hql.append(" and firstSchoolTime <= :firstSchoolTime ");
			params.put("firstSchoolTime", otmClassStudentVo.getFirstSchoolTime());
		}
		hql.append(" order by createTime desc ");

		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
	/**
	 * 一对多学生考勤列表(考虑到扣费后再退班的学生)
	 */
	public DataPackage getOtmClassStudentAttendentList(OtmClassStudentAttendentVo otmClassStudentAttendentVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassStudentAttendent  where 1=1 ");
		if (StringUtils.isNotBlank(otmClassStudentAttendentVo.getOtmClassCourseId())) {
			hql.append(" and otmClassCourse.otmClassCourseId = :otmClassCourseId ");
			params.put("otmClassCourseId", otmClassStudentAttendentVo.getOtmClassCourseId());
		}
		if (StringUtils.isNotBlank(otmClassStudentAttendentVo.getStudentId())) {
			hql.append(" and studentId = :studentId ");
			params.put("studentId", otmClassStudentAttendentVo.getStudentId());
		}
		if (null != otmClassStudentAttendentVo.getChargeStatus()) {
			hql.append(" and chargeStatus = :chargeStatus ");
			params.put("chargeStatus", otmClassStudentAttendentVo.getChargeStatus());
		}
		hql.append(" order by createTime desc ");

		return super.findPageByHQL(hql.toString(), dp, true ,params);
	}
	
	/**
	 * 一对多学生考勤列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOtmClassStudentListUncharge(OtmClassStudentVo otmClassStudentVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassStudent  where 1=1 ");
		if (StringUtils.isNotBlank(otmClassStudentVo.getOtmClassId())) {
			hql.append(" and otmClass.otmClassId = :otmClassId ");
			params.put("otmClassId", otmClassStudentVo.getOtmClassId());
		}
		if (StringUtils.isNotBlank(otmClassStudentVo.getStudentId())) {
			hql.append(" and student.id = :studentId ");
			params.put("studentId", otmClassStudentVo.getStudentId());
		}
		if (null != otmClassStudentVo.getOtmClassStudentChargeStatus()) {
			hql.append(" and otmClassStudentChargeStatus = :otmClassStudentChargeStatus ");
			params.put("otmClassStudentChargeStatus", OtmClassStudentChargeStatus.valueOf(otmClassStudentVo.getOtmClassStudentChargeStatus()));
		}
		if(StringUtils.isNotBlank(otmClassStudentVo.getFirstSchoolTime())){
			hql.append(" and firstSchoolTime <= :firstSchoolTime ");
			params.put("firstSchoolTime", otmClassStudentVo.getFirstSchoolTime());
		}
		hql.append(" order by createTime desc ");

		return super.findPageByHQL(hql.toString(), dp, true, params);
	}

}
