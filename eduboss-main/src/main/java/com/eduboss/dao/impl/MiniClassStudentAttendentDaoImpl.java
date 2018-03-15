package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.dao.MiniClassStudentAttendentDao;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassStudentAttendent;
import com.eduboss.domainVo.MiniClassStudentAttendentVo;
import com.eduboss.dto.DataPackage;
import com.google.common.collect.Maps;

@Repository("MiniClassStudentAttendent")
public class MiniClassStudentAttendentDaoImpl extends GenericDaoImpl<MiniClassStudentAttendent, String> implements MiniClassStudentAttendentDao {
	
	/**
	 * 小班学生考勤列表
	 */
	@Override
	public DataPackage getMiniClassStudentAttendentList(MiniClassStudentAttendentVo miniClassStudentAttendentVo,
			DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from MiniClassStudentAttendent  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(miniClassStudentAttendentVo.getMiniClassCourseId())) {
			hql.append(" and  miniClassCourse.miniClassCourseId = :miniClassCourseId ");// 小班ID
		    params.put("miniClassCourseId", miniClassStudentAttendentVo.getMiniClassCourseId());
		}
		if (StringUtils.isNotBlank(miniClassStudentAttendentVo.getStudentId())) {
			hql.append(" and  studentId = :studentId ");// 学生ID
			params.put("studentId",miniClassStudentAttendentVo.getStudentId());
		}
		hql.append(" order by createTime desc ");

		return super.findPageByHQL(hql.toString(), dp,true,params);
	}
	
	/**
	 * 根据小班课程ID获取小班学生考勤列表
	 */
	@Override
	public int countMiniClassStudentAttendentByMiniClassCourse(String miniClassCourseId, String chargeStatus) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassCourseId", miniClassCourseId);
		params.put("chargeStatus", MiniClassStudentChargeStatus.valueOf(chargeStatus));
		hql.append(" select count(miniClassStudentAttendent) from MiniClassStudentAttendent miniClassStudentAttendent  where 1=1 ");
		hql.append(" and  miniClassCourse.miniClassCourseId = :miniClassCourseId ");// 小班ID
		hql.append(" and chargeStatus = :chargeStatus ");
		return super.findCountHql(hql.toString(),params);
	}
	
	
	@Override
	public List<MiniClassStudentAttendent> getMiniClassAttendsByCourseId(
			String courseId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from MiniClassStudentAttendent where 1=1 ");
		if(StringUtils.isNotBlank(courseId)){
			hql.append(" and  miniClassCourse.miniClassCourseId = :miniClassCourseId ");// 小班ID
			params.put("miniClassCourseId", courseId);
		}
		return super.findAllByHQL(hql.toString(),params);
	}
	
	/**
	 * 小班单个学生考勤
	 */
	@Override
	public MiniClassStudentAttendent getOneMiniClassStudentAttendent(String miniClassCourseId, String studentId) {
		MiniClassStudentAttendentVo miniClassStudentAttendentVo = new MiniClassStudentAttendentVo();
		miniClassStudentAttendentVo.setMiniClassCourseId(miniClassCourseId);
		miniClassStudentAttendentVo.setStudentId(studentId);
		DataPackage dp = getMiniClassStudentAttendentList(miniClassStudentAttendentVo, new DataPackage(0,999));
		List<MiniClassStudentAttendent> listAttendent= (List<MiniClassStudentAttendent>) dp.getDatas();
		
		if (listAttendent.size() > 0) {
			return listAttendent.get(0);
		} else {
			return null;
		}
	}

	@Override
	public int getCountOfAttendenceRecordByMiniClassAndStudent(
			String miniClassId, String studentId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer hql = new StringBuffer();
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);
		hql.append(" select count(miniClassStudentAttendent) from MiniClassStudentAttendent miniClassStudentAttendent where 1=1 ");
		hql.append(" and  miniClassCourse.miniClass.miniClassId = :miniClassId ");// 小班ID
		hql.append(" and  miniClassStudentAttendent.studentId = :studentId ");// 学生ID
		hql.append(" and  chargeStatus ='CHARGED'");// 已扣费的考勤记录
		int recordCount = this.findCountHql(hql.toString(),params);
		return recordCount;
	}
	
	/**
	 * 根据 小班ID 和 学生ID，  获取对应扣费状态考勤记录
	 * @param miniClassId
	 * @param studentId
	 * @return
	 */
	public int getCountOfUnchargedAttendenceRecordByMiniClassAndStudent(
			String miniClassId, String studentId, String chargeStatus) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer hql = new StringBuffer();
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);
		params.put("chargeStatus", MiniClassStudentChargeStatus.valueOf(chargeStatus));
		hql.append(" select count(miniClassStudentAttendent) from MiniClassStudentAttendent miniClassStudentAttendent where 1=1 ");
		hql.append(" and  miniClassCourse.miniClass.miniClassId = :miniClassId ");// 小班ID
		hql.append(" and  miniClassStudentAttendent.studentId = :studentId ");// 学生ID
		hql.append(" and  miniClassStudentAttendent.miniClassAttendanceStatus != 'NEW'");// 学生ID
		hql.append(" and  miniClassStudentAttendent.chargeStatus = :chargeStatus ");// 扣费状态
		int recordCount = this.findCountHql(hql.toString(),params);
		return recordCount;
	}

	public double getSumAttendentHours(String miniClassId, String studentId, MiniClassStudentChargeStatus chargeStatus){
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer  hql= new StringBuffer();
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);

		hql.append("select sum(quality) from AccountChargeRecords where miniClass.miniClassId=:miniClassId and student.id =:studentId");

		hql.append(" and chargePayType='CHARGE' and isWashed='FALSE' and chargeType='NORMAL'");

		return this.findSumHql(hql.toString(),params);
	}

	@Override
	public Double findConsumeCount(MiniClassCourse miniClassCourse) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassId", miniClassCourse.getMiniClass().getMiniClassId());
		String hql=" select sum(courseHours) from MiniClassCourse where miniClass.miniClassId= :miniClassId  "+
				" and courseStatus ='CHARGED' ";//直接从课程表里面统计
//				" and miniClassCourseId in(select miniClassCourse.miniClassCourseId from MiniClassStudentAttendent ) ";
		return this.findSumHql(hql,params);
	}
	
	/**
	 * 删除未扣费的考勤记录
	 * @param miniClassCourseId
	 */
	@Override
	public void deleteMiniClassStudentAttendent(String miniClassCourseId){
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassCourseId", miniClassCourseId);
		String hql=" delete MiniClassStudentAttendent where miniClassCourse.miniClassCourseId= :miniClassCourseId and chargeStatus='UNCHARGE'";
		super.excuteHql(hql,params);
	}
	
	/**
	 * 删除对应小班的学生的未扣费的考勤记录
	 * @param miniClassCourseId
	 */
	public void deleteMiniClassStudentAttendentByStudentAndMiniClass(String studentId, String miniClassId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from MiniClassStudentAttendent where miniClassCourse.miniClass.miniClassId = :miniClassId and studentId=:studentId and chargeStatus='UNCHARGE'");
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);
		List<MiniClassStudentAttendent> list=this.findAllByHQL(hql.toString(),params);
		this.deleteAll(list);
//		String hql=" delete MiniClassStudentAttendent where miniClassCourse.miniClass.miniClassId='"+miniClassId+"' and studentId='" + studentId +"' and miniClassAttendanceStatus='NEW'";
//		super.excuteHql(hql);
	}

	@Override
	public List getStudentMiniClassAttendent(String miniClassId, String studentId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select mc.name,mcc.course_num courseNum,mcc.course_date courseDate,mcc.COURSE_TIME startTime,mcc.COURSE_END_TIME endTime,u.name teacherName,mca.ATTENDENT_STATUS attendentStatus");
		sql.append("  from mini_class_student_attendent mca left join mini_class_course mcc on mcc.mini_class_course_id = mca.mini_class_course_id ");
		sql.append(" left join mini_class mc on mc.mini_class_id = mcc.mini_class_id ");
		sql.append(" left join user u on u.user_id = mcc.teacher_id ");
		sql.append(" where mcc.mini_class_id =:miniClassId and mca.student_id = :studentId ");
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);
		return this.findMapBySql(sql.toString(),params);
	}

	@Override
	public List<MiniClassStudentAttendent> getStudentMiniClassAttendentList(String miniClassId, String studentId) {
		String sql = "from MiniClassStudentAttendent where miniClassCourse.miniClass.miniClassId =:miniClassId and studentId=:studentId";
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);
		return this.findAllByHQL(sql,params);
	}

}
