package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.common.OtmClassStudentChargeStatus;
import com.eduboss.dao.OtmClassDao;
import com.eduboss.domain.OtmClass;
import com.eduboss.domainVo.OtmClassStudentVo;
import com.eduboss.dto.DataPackage;

@Repository("OtmClassDao")
public class OtmClassDaoImpl extends GenericDaoImpl<OtmClass, String> implements OtmClassDao {
	
	@Override
	public void deleteOtmClassCourse(String otmClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" delete OtmClassCourse  where 1=1 ");
		hql.append(" and  otmClass.otmClassId = :otmClassId ");
		params.put("otmClassId", otmClassId);
		StringBuffer mcsaSql = new StringBuffer();
		mcsaSql.append(" delete from OTM_CLASS_STUDENT_ATTENDENT ");
		mcsaSql.append(" where OTM_CLASS_COURSE_ID in (select OTM_CLASS_COURSE_ID from otm_class_course where OTM_CLASS_ID = :otmClassId) ");
		mcsaSql.append(" and CHARGE_STATUS = 'UNCHARGE' ");
		excuteSql(mcsaSql.toString(), params);
		excuteHql(hql.toString(), params);
	}
	
	/**
	 * 一对多学生考勤列表（未扣费）
	 * @param otmClassStudentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassStudentListUncharge(
			OtmClassStudentVo otmClassStudentVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassStudent  where 1=1 ");
		
		if (StringUtils.isNotBlank(otmClassStudentVo.getOtmClassId())) {
			hql.append(" and id.otmClassId = :otmClassId ");
			params.put("otmClassId", otmClassStudentVo.getOtmClassId());
		}
		if (StringUtils.isNotBlank(otmClassStudentVo.getStudentId())) {
			hql.append(" and student.id = :studentId ");
			params.put("studentId", otmClassStudentVo.getStudentId());
		}
		if (StringUtils.isNotBlank(otmClassStudentVo.getOtmClassStudentChargeStatus())) {
			hql.append(" and otmClassStudentChargeStatus = '" + OtmClassStudentChargeStatus.UNCHARGE.getValue() + "'");
		}
		if(StringUtils.isNotBlank(otmClassStudentVo.getFirstSchoolTime())){
			hql.append(" and firstSchoolTime <= :firstSchoolTime ");
			params.put("firstSchoolTime", otmClassStudentVo.getFirstSchoolTime());
		}
		hql.append(" order by otmClassStudentChargeStatus desc ");

		return super.findPageByHQL(hql.toString(), dp, true, params);
	}

}
