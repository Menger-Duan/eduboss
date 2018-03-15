package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.common.LectureClassAttendanceStatus;
import com.eduboss.domain.LectureClassStudent;
import com.eduboss.domainVo.LectureClassStudentVo;
import com.eduboss.domainVo.LectureClassVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;


public interface LectureClassService {

	DataPackage getLectureClassList(DataPackage dataPackage, LectureClassVo vo);

	Response saveOrUpdateLectureClass(String oper, LectureClassVo lectureClassVo);

	LectureClassVo findLectureClassById(String lectureClassId);

	Response delteLectureClass(String lectureClassId);
	
	List<LectureClassStudent> getLectureClassStudentByLectureClassId(String lectureClassId);

	Response addStudentToLectureClass(LectureClassStudentVo lectureClassStudentVo);

	DataPackage getLectureClassStudentList(DataPackage dataPackage,
										   LectureClassStudentVo vo);

	Response deleteStudentFromLectureClass(String lectureClassStudentId);

	List<StudentVo> getStudentWantListByLectureClassId(String lectureClassId);

	List<StudentVo> getLectureClasssStudentByClassId(String lectureClassId);

	Response removeStudentFromLecture(String lectureId, String studentId);

	List<Map<Object, Object>> getStudentWantListByBranchAndType(String lectureClassId,
												String productType);

	Response auditLectureClassStudent(String ids,
									  LectureClassAttendanceStatus auditStatus);

	Response chargeLectureClassStudent(String ids);

	void deleteStudentByContractProduct(String contractProductId);
	
	int getCountByStudentAndLecture(String studentId,String lectureId);

	int getCountByLecture(String lectureId);
}
