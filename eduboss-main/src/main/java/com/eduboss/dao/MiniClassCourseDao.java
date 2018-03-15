package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.common.CourseStatus;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;

public interface MiniClassCourseDao extends GenericDAO<MiniClassCourse, String> {

	/**
	 * @param miniClassCourse
	 */
	@Override
	void save(MiniClassCourse miniClassCourse);

	/**
	 * 小班课程列表
	 * @param miniClassCourse
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassCourseList(MiniClassCourseVo miniClassCourse,
			DataPackage dp);
	
	/**
	 * 小班课程列表手机接口
	 * @param miniClassCourse
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassCourseListForMobile(MiniClassCourseVo miniClassCourse,
			DataPackage dp);
	
	/**
	 * 获取某课程时间段的老师课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	public DataPackage getTeacherMiniClassCourseScheduleListByCourseTime(String teacherId, String start, String end, 
			String courseStartTime, String courseEndTime, DataPackage dp);

	/**
	 * 删除小班课程
	 * @param miniClassId
	 */
	void deleteMiniClassCourse(String miniClassId);

	/**
	 * 通过小班ID查出所有小班课程，按课程时间倒序
	 * @param miniClassId
	 * @return
	 */
	public List<MiniClassCourse> getMiniClassCourseListByMiniClassId(String miniClassId);
	
	/**
	 * 通过小班ID查出所有小班课程，按课程时间倒序，返回类型为DataPackage
	 * @param miniClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassCourseListByMiniClassId(String miniClassId, String firstSchoolDate, DataPackage dp);
	
	/**
	 * 查询小班下次上课时间
	 * @param courseDate
	 * @param miniClassId
	 * @return
	 */
	public String getTop1MiniClassCourseByDate(String courseDate,String miniClassId);
	
	/**
	 * 获取小班课程时间段内占用教室列表
	 * @param startDate
	 * @param endDate
	 * @param classroomName
	 * @param miniClassName
	 * @return
	 */
	public List getMiniClassCourseUseClassroomList(String blCampusId,String startDate, String endDate, String classroomName, String miniClassName);

	/**
	 * 获取小班教室的小班课程
	 * @param startDate
	 * @param endDate
	 * @param classroomName
	 * @param miniClassName
	 * @return
	 */
	public List<MiniClassCourse> getClassroomMiniClassCoursePlus(String startDate, String endDate, String classroomName, String miniClassName);
	public List getClassroomMiniClassCourse(String startDate, String endDate, String classroomName, String miniClassName,String campusId,String miniClassTypeId);

	/**
	 * 查询哪个小班课程未安排教室
	 * @param startDate
	 * @param endDate
	 * @param classroomName
	 * @param miniClassName
	 * @return
	 */
	public List getNotArrangementClassroomMiniClassCourse(String startDate,
			String endDate, String classroomName, String miniClassName);

	/**
	 * 小班课程详情
	 * @param miniClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassCourseDetail(String miniClassId, DataPackage dp,ModelVo modelVo);

	
	public void updateMiniClassCourse(MiniClass miniClass) throws Exception;

	public Double findCountClassHours(String miniClassId);
	
	public List<MiniClassCourse> getMiniClassCourse4Class(String sbHql);

	/**
	 * 保存小班信息时，自动修改所有跟此班关联的课室为此次选定的课室
	 * @param miniClass
	 */
	public void saveClassroom4itsClass(MiniClass miniClass);
	
	/**
	 * 根据小班id获取排课的最大最小值
	 * @param miniClassId
	 * @return
	 */
	Map<Object, Object> getMaxMinCourseDateByMiniClass(String miniClassId);

    List<MiniClassCourse> findAllUnChargeMiniClassCourse(String miniClassId, CourseStatus status);

    List getMiniClassCourseDateInfo(String miniClassId);

    List<MiniClassCourse> findAllEnableMiniClassCourse(String miniClassId);
}
