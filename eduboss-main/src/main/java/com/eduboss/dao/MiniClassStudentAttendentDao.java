package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassStudentAttendent;
import com.eduboss.domainVo.MiniClassStudentAttendentVo;
import com.eduboss.dto.DataPackage;

public interface MiniClassStudentAttendentDao extends GenericDAO<MiniClassStudentAttendent, String> {
	
	/**
	 * 小班学生考勤列表
	 * @param miniClassCourse
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassStudentAttendentList(MiniClassStudentAttendentVo miniClassStudentAttendentVo,
			DataPackage dp);
	
	/**
	 * 根据小班课程ID和扣费状态计算考勤学生数
	 * @param miniClassCourseId
	 * @param chargeStatus
	 * @return
	 */
	public int countMiniClassStudentAttendentByMiniClassCourse(String miniClassCourseId, String chargeStatus);
	
	
	/**
	 * 根据考勤状态获取考勤记录
	 * @param courseId
	 * @return
	 */
	public List<MiniClassStudentAttendent> getMiniClassAttendsByCourseId(String courseId);

	/**
	 * 小班单个学生考勤
	 * @param miniClassCourseId
	 * @param studentId
	 * @return
	 */
	public MiniClassStudentAttendent getOneMiniClassStudentAttendent(
			String miniClassCourseId, String studentId);

	/**
	 * 根据 小班ID 和 学生ID，  获取 考勤记录
	 * @param miniClassId
	 * @param studentId
	 * @return
	 */
	public int getCountOfAttendenceRecordByMiniClassAndStudent(
			String miniClassId, String studentId);
	
	/**
	 * 根据 小班ID 和 学生ID，  获取对应扣费状态考勤记录
	 * @param miniClassId
	 * @param studentId
	 * @return
	 */
	public int getCountOfUnchargedAttendenceRecordByMiniClassAndStudent(
			String miniClassId, String studentId, String chargeStatus);

	public double getSumAttendentHours(String miniClassId, String studentId, MiniClassStudentChargeStatus chargeStatus);
	
	/**
	 * 计算已上课消耗
	 * @param miniClassCourse
	 * @return
	 */
	public Double findConsumeCount(MiniClassCourse miniClassCourse);

	/**
	 * 删除未扣费的考勤记录
	 * @param miniClassCourseId
	 */
	public void deleteMiniClassStudentAttendent(String miniClassCourseId);
	
	/**
	 * 删除对应小班的学生的未扣费的考勤记录
	 * @param miniClassCourseId
	 */
	public void deleteMiniClassStudentAttendentByStudentAndMiniClass(String studentId, String miniClassId);

	List getStudentMiniClassAttendent(String miniClassId, String studentId);

	List<MiniClassStudentAttendent> getStudentMiniClassAttendentList(String miniClassId, String studentId);
}
