package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.OtmClass;
import com.eduboss.domainVo.OtmClassStudentVo;
import com.eduboss.dto.DataPackage;

@Repository
public interface OtmClassDao extends GenericDAO<OtmClass, String> {
	
	/**
	 * 删除一对多课程
	 * @param otmClassId
	 */
	public void deleteOtmClassCourse(String otmClassId);
	
	/**
	 * 一对多学生考勤列表（未扣费）
	 * @param otmClassStudentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassStudentListUncharge(
			OtmClassStudentVo otmClassStudentVo, DataPackage dp);

}
