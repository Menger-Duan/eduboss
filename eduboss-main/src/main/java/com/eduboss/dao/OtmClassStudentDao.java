package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.OtmClassStudent;
import com.eduboss.domainVo.OtmClassStudentAttendentVo;
import com.eduboss.domainVo.OtmClassStudentVo;
import com.eduboss.dto.DataPackage;

@Repository
public interface OtmClassStudentDao extends GenericDAO<OtmClassStudent, Integer> {
	
	/**
	 * 获取一条一对多学生记录
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	public OtmClassStudent getOneOtmClassStudent(String otmClassId, String studentId);
	
	/**
	 * 一对多详情-在读学生信息列表
	 * @param otmClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassDetailStudentList(
			String otmClassId, DataPackage dp);
	
	/**
	 * 一对多学生考勤列表
	 * @param otmClassStudentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassStudentList(OtmClassStudentVo otmClassStudentVo, DataPackage dp);
	
	/**
	 * 一对多学生考勤列表(考虑到扣费后再退班的学生)
	 * @param otmClassStudentAttendentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassStudentAttendentList(OtmClassStudentAttendentVo otmClassStudentAttendentVo, DataPackage dp);
	
	/**
	 * 一对多详情-在读学生信息列表
	 * @param otmClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassStudentListUncharge(
			OtmClassStudentVo otmClassStudentVo, DataPackage dp);

}
