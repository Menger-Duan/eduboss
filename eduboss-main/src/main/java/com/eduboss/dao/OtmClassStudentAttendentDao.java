package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.OtmClassStudentAttendent;
import com.eduboss.domainVo.OtmClassStudentAttendentVo;
import com.eduboss.dto.DataPackage;

@Repository
public interface OtmClassStudentAttendentDao extends GenericDAO<OtmClassStudentAttendent, String> {
	
	/**
	 * 一对多学生考勤列表
	 * @param otmClassStudentAttendentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassStudentAttendentList(OtmClassStudentAttendentVo otmClassStudentAttendentVo,
			DataPackage dp);
	
	/**
	 * 一对多单个学生考勤记录
	 * @param otmClassCourseId
	 * @param studentId
	 * @return
	 */
	public OtmClassStudentAttendent getOneOtmClassStudentAttendent(
			String otmClassCourseId, String studentId);
	
	/**
	 * 删除对应一对多的学生的未扣费的考勤记录
	 * @param studentId
	 * @param miniClassCourseId
	 */
	public void deleteOtmClassStudentAttendentByStudentAndOtmClass(String studentId, String miniClassId);
	
	/**
	 * 根据 一对多ID 和 学生ID，  获取对应扣费状态考勤记录
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	public int getCountOfUnchargedAttendenceRecordByOtmClassAndStudent(
			String otmClassId, String studentId, String chargeStatus);
	
	/**
	 * 计算已上课消耗
	 * @param otmClassCourse
	 * @return
	 */
	public Double findConsumeCount(OtmClassCourse otmClassCourse);
	
	/**
	 * 删除未扣费的考勤记录
	 * @param otmClassCourseId
	 */
	public void deleteOtmClassStudentAttendent(String otmClassCourseId);
	
	/**
	 * 根据一对多课程ID和扣费状态计算考勤学生数
	 * @param otmClassCourseId
	 * @param chargeStatus
	 * @return
	 */
	public int countOtmClassStudentAttendentByOtmClassCourse(String otmClassCourseId, String chargeStatus);
	
	/**
	 * 根据一对多课程ID查询一对多课程考勤学生列表 JDBC 
	 * @param otmClassCourseId
	 * @return
	 */
	public List<OtmClassStudentAttendent> getOtmClassStudentAttendentListJdbc(String otmClassCourseId);
	
}
