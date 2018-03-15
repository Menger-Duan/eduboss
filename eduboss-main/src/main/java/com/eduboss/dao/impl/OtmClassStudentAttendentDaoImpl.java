package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.common.OtmClassStudentChargeStatus;
import com.eduboss.dao.OtmClassStudentAttendentDao;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.OtmClassStudentAttendent;
import com.eduboss.domainVo.OtmClassStudentAttendentVo;
import com.eduboss.dto.DataPackage;

@Repository("OtmClassStudentAttendentDao")
public class OtmClassStudentAttendentDaoImpl extends GenericDaoImpl<OtmClassStudentAttendent, String> implements OtmClassStudentAttendentDao {
	
	/**
	 * 一对多学生考勤列表
	 * @param otmClassStudentAttendentVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOtmClassStudentAttendentList(OtmClassStudentAttendentVo otmClassStudentAttendentVo,
			DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassStudentAttendent where 1=1 ");
		if (StringUtils.isNotBlank(otmClassStudentAttendentVo.getOtmClassCourseId())) {
			hql.append(" and  otmClassCourse.otmClassCourseId = :otmClassCourseId ");// 一对多ID
			params.put("otmClassCourseId", otmClassStudentAttendentVo.getOtmClassCourseId());
		}
		if (StringUtils.isNotBlank(otmClassStudentAttendentVo.getStudentId())) {
			hql.append(" and  studentId = :studentId ");// 学生ID
			params.put("studentId", otmClassStudentAttendentVo.getStudentId());
		}
		hql.append(" order by createTime desc ");
		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
	/**
	 * 一对多单个学生考勤记录
	 * @param otmClassCourseId
	 * @param studentId
	 * @return
	 */
	public OtmClassStudentAttendent getOneOtmClassStudentAttendent(
			String otmClassCourseId, String studentId) {
		OtmClassStudentAttendentVo otmClassStudentAttendentVo = new OtmClassStudentAttendentVo();
		otmClassStudentAttendentVo.setOtmClassCourseId(otmClassCourseId);
		otmClassStudentAttendentVo.setStudentId(studentId);
		DataPackage dp = getOtmClassStudentAttendentList(otmClassStudentAttendentVo, new DataPackage(0,999));
		List<OtmClassStudentAttendent> listAttendent= (List<OtmClassStudentAttendent>) dp.getDatas();
		if (listAttendent.size() > 0) {
			return listAttendent.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 删除对应一对多的学生的未扣费的考勤记录
	 * @param studentId
	 * @param miniClassCourseId
	 */
	public void deleteOtmClassStudentAttendentByStudentAndOtmClass(String studentId, String miniClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "delete from OTM_CLASS_STUDENT_ATTENDENT where OTM_CLASS_COURSE_ID IN (select OTM_CLASS_COURSE_ID from otm_class_course where OTM_CLASS_ID = :miniClassId ) " 
				+ " AND STUDENT_ID = :studentId AND CHARGE_STATUS='UNCHARGE' ";
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);
		super.excuteSql(sql, params);
	}
	
	/**
	 * 根据 一对多ID 和 学生ID，  获取对应扣费状态考勤记录
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	public int getCountOfUnchargedAttendenceRecordByOtmClassAndStudent(
			String otmClassId, String studentId, String chargeStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(otmClassStudentAttendent) from OtmClassStudentAttendent otmClassStudentAttendent where 1=1 ");
		hql.append(" and  otmClassCourse.otmClass.otmClassId = :otmClassId ");// 一对多ID
		hql.append(" and  otmClassStudentAttendent.studentId = :studentId ");// 学生ID
		hql.append(" and  otmClassStudentAttendent.otmClassAttendanceStatus != 'NEW'");
		hql.append(" and  otmClassStudentAttendent.chargeStatus = :chargeStatus ");// 扣费状态
		params.put("otmClassId", otmClassId);
		params.put("studentId", studentId);
		params.put("chargeStatus", OtmClassStudentChargeStatus.valueOf(chargeStatus));
		int recordCount = this.findCountHql(hql.toString(), params);
		return recordCount;
	}
	
	/**
	 * 计算已上课消耗
	 * @param otmClassCourse
	 * @return
	 */
	@Override
	public Double findConsumeCount(OtmClassCourse otmClassCourse) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" select sum(courseHours) from OtmClassCourse where otmClass.otmClassId = :otmClassId " +
				" and courseStatus ='CHARGED' ";//直接从课程表里面统计
		params.put("otmClassId", otmClassCourse.getOtmClass().getOtmClassId());
		return this.findSumHql(hql, params);
	}
	
	/**
	 * 删除未扣费的考勤记录
	 * @param otmClassCourseId
	 */
	public void deleteOtmClassStudentAttendent(String otmClassCourseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" delete OtmClassStudentAttendent where otmClassCourse.otmClassCourseId = :otmClassCourseId and chargeStatus='UNCHARGE'";
		params.put("otmClassCourseId", otmClassCourseId);
		super.excuteHql(hql, params);
	}
	
	/**
	 * 根据一对多课程ID和扣费状态计算考勤学生数
	 * @param otmClassCourseId
	 * @param chargeStatus
	 * @return	
	 */
	@Override
	public int countOtmClassStudentAttendentByOtmClassCourse(String otmClassCourseId, String chargeStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(otmClassStudentAttendent) from OtmClassStudentAttendent otmClassStudentAttendent  where 1=1 ");
		hql.append(" and  otmClassCourse.otmClassCourseId = :otmClassCourseId ");// 一对多ID
		hql.append(" and chargeStatus = :chargeStatus ");
		params.put("otmClassCourseId", otmClassCourseId);
		params.put("chargeStatus", OtmClassStudentChargeStatus.valueOf(chargeStatus));
		return super.findCountHql(hql.toString(), params);
	}
	
	/**
	 * 根据一对多课程ID查询一对多课程考勤学生列表 JDBC 
	 */
	public List<OtmClassStudentAttendent> getOtmClassStudentAttendentListJdbc(String otmClassCourseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select ocsa.* from otm_class_student_attendent ocsa left join user study on ocsa.STUDY_MANAGER_ID = study.USER_ID "
				+ " where OTM_CLASS_COURSE_ID = :otmClassCourseId ";
		params.put("otmClassCourseId", otmClassCourseId);
		return super.findBySql(sql, params);
	}
	
}
