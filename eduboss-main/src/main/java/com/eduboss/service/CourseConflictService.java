package com.eduboss.service;

import com.eduboss.domainVo.CourseConflictVo;
import com.eduboss.dto.DataPackage;

import java.util.List;

public interface CourseConflictService {

	/**
	 * 根据条件查询所有课程冲突表数据
	 * @param courseConflictVo
	 * @return
	 */
	public DataPackage findCourseConflictInfos(
			CourseConflictVo courseConflictVo,DataPackage dp) ;
	
	/**
	 * 根据条件查询冲突课程数据数量
	 * @param vo
	 * @return
	 */
	public int countCourseConflictInfos(CourseConflictVo courseConflictVo);
	
	/**
	 * 根据条件查询冲突数量
	 * @param courseConflictVo
	 * @return
	 */
	public int countDistinctConflicts(String courseId,String studentId,String teacherId,String courseDate,String courseTime);
	
	/**
	 * 根据条件查询冲突日期
	 * @param startDate
	 * @param endDate
	 * @param courseTime
	 * @param teacherId
	 * @param studentId
	 * @return
	 */
	public List<String> findConflictDateByCause(String startDate,String endDate,String courseTime,String teacherId,String studentId,String courseSummaryId);

    /**
     * 删除昨天以前的数据
     */
    public void deleteDatasBeforeYesterday();
}
