/**
 * 
 */
package com.eduboss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eduboss.common.CustomerEventType;
import com.eduboss.common.StudentEventType;
import com.eduboss.domain.Contract;
import com.eduboss.domain.Course;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.StudentComment;
import com.eduboss.domain.StudentDynamicStatus;
import com.eduboss.domain.StudentScore;
import com.eduboss.domainVo.StudentDynamicStatusVo;
import com.eduboss.dto.DataPackage;


/**
 * @classname	CommonService.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-11-13 12:04:19
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
@Service
public interface StudentDynamicStatusService {

	DataPackage findStudentDynamicStatus(StudentDynamicStatus cds,
			DataPackage dp);

	/**
	 * 学生 合同记录  的 动态信息
	 * @param contract
	 * @param string
	 */
	void createContractDynamicStatus(Contract contract, String contractStatus);

	/**
	 * 学生 Score 的 动态信息
	 * @param studentscoremanage
	 * @param scoreStatus
	 */
	void createScoreDynamicStatus(StudentScore studentscoremanage, String scoreStatus);

	/**
	 * 学生 课程 的 动态信息
	 * @param course
	 * @param courseStatus
	 */
	void createCourseDynamicStatus(Course course, String courseStatus);

	/**
	 * 获取 学生动态信息
	 * @param studentId
	 * @param pageNo
	 * @param pageSize
	 * @param studentEventType
	 * @return
	 */
	List<StudentDynamicStatusVo> getStudentDynamicStatusByPage(
			String studentId, int pageNo, int pageSize,
			StudentEventType studentEventType);

	/**
	 * 学生评论 增加动态
	 * @param comment
	 */
	void createCommentDynamicStatus(StudentComment comment);

	
	
}
