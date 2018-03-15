package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.CourseConflict;
import com.eduboss.domainVo.CourseConflictVo;
import com.eduboss.dto.DataPackage;

public interface CourseConflictDao extends GenericDAO<CourseConflict, Long> {

	/**
	 * 根据条件查询所有课程冲突表数据
	 * @param courseConflictVo
	 * @return
	 */
	public DataPackage findCourseConflictInfos(CourseConflictVo courseConflictVo,DataPackage dp);
	
	/**
	 * 根据条件查询冲突课程数据数量
	 * @param vo
	 * @return
	 */
	public int countCourseConflictInfos(CourseConflictVo courseConflictVo);
	
	/**
	 * 根据条件查询冲突数量
	 * @return
	 */
	public int countDistinctConflicts(String courseId,String studentId,String teacherId,String courseDate,String courseTime);

	/**
	 * 根据条件查询冲突数量
	 * @return
	 */
	public int countMiniClassDistinctConflicts(String courseId,String studentId,String teacherId,String courseDate,String courseTime);
	
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
	 * 删除冲突
	 */
	public int deleteCourseConflictByCourseIdAndStudentId(String courseId,String studentId);
	
	/**
	 * 删除小班，一对多冲突
	 */
	public int deleteCourseConflictByCourseId(String courseId);

    /**
     * 删除昨天以前的数据
     */
    public void deleteDatasBeforeYesterday();
    
    
	/**
	 * @param courseId
	 * @param studentId
	 * @param teacherId
	 * @param courseDate
	 * @param courseTime
	 * @description 冲突课程
	 */
	public List<CourseConflict> getCourseConflictList(String courseId,String studentId,String teacherId,String courseDate,String courseTime);

	/**
	 * 小班冲突课程
	 */
	public List<CourseConflict> getMiniClassCourseConfictList(String courseId,String studentId,String teacherId,String courseDate,String courseTime);
	
	
	/** 
	 * 根据一对一课程ID查找冲突的课程数量
	* @param courseId
	* @return
	* @author  author :Yao 
	* @date  2016年9月2日 上午10:46:42 
	* @version 1.0 
	*/
	public int findCourseConflictCount(String courseId,String studentId,String teacherId);

	/** 
	* @param courseId
	* @param startTime   yyyyMMddHHmmss
	* @param endTime     yyyyMMddHHmmss
	* @param teacherId
	* @param studentId
	* @return
	* @author  author :Yao 
	* @date  2016年9月2日 下午2:09:04 
	* @version 1.0 
	*/
	List<CourseConflict> findCourseConflictList(String courseId,String studentId,String teacherId);
		
}
