package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.common.OrganizationType;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.DataPackage;

public interface StudentDao extends GenericDAO<Student, String> {

	public void updateStudentManeger(String studentIds, String oldStudyManagerId, String newStudyManagerId);

	public Student findByAttendanceNo(String attendanceNo,Organization org);

	/**
	 * 查找学生列表（自动搜索）
	 * @param input
	 * @return
	 */
	public List<Map<Object, Object>> getStudentAutoComplate(String input,List<Organization> organization);
	
	public List<Student> getStudentAutoComplate(String input);
	
	public List<Student> getStudentAutoComplateByCampusId(String input, String campusId);

	public boolean isExistsStudent(String name,String contact, String fatherPhone, String notherPhone);

	/**
	 * 根据IC卡编号查找学生
	 * @param icCardNo
	 * @return
	 */
	Student findByIcCardNo(String icCardNo);
	
	/**
	 * 检查考勤编号是否存在
	 * @param attendanceNo
	 * @return
	 */
	public boolean checkIfAttendanceNoExist(String studentId, String attendanceNo,String blCampusId);

    /**
     * 查找所有学生with课程数量
     * @return
     */
    public List<StudentVo> findStudentWithCourseCount(CourseVo courseVo,Organization organization);

	public List<Object[]> getAllStudentFingerInfo();
	
	public List<Student> getStudentForPerform(String input);
	
	
	/**
	 * 用于查询 用户相关联的 学生信息列表
	 * @param staffId
	 * @return
	 */
	public List<Student> findStudentByStaffId(String staffId);
	
	/**
	 * @param 根据校区找学生
	 * @return
	 */
	public List<Student> findStudentByCampus(Organization organization);

	public DataPackage findPageByHQLForAcc(String hql2, DataPackage dp,
			String countHql, Map<String, ?> params);
	
	/**
	 * @param 查询所有学生校区ID（包括跨校区）
	 * @return
	 */
	public List<Object> getAllStudentOrganization();
	
	/**
	 * @param 查询该校区所有为上课中的学生ID
	 * @return
	 */
	public List<Map<Object, Object>> getStudentIdByOrganizationId(String organizationId);
	
	/**
	 * @param 查询所有为上课中的学生ID
	 * @return
	 */
	public List<Map<Object, Object>> getAllStudentId();
	
	/**
	 * 查询所有为上课中的学生指纹
	 * @return
	 */
	public List<Object[]> getAllStudentFingerInfoByStatus();
	
	/**
	 * 查询所有今天有课的学生指纹
	 * @return
	 */
	public List<Object[]> getTodayStudentFingerInfo();

	public Map<Object, Object> getCusAndStuByStudentId(String studentId);
	
	/**
	 * 批量升级学生年级
	 * 
	 */
	
	public void upgrade(String cla,String course,String orgnizationId,OrganizationType type, String gradeNames);
	
	/**
	 * 根据学管查找其管理的学生
	 * @param studyManagerId
	 * @return
	 */
	public List<Student> getStudentListByStudyManager(String studyManagerId);

	/**
	 * 获取学管校区学生List
	 * @param userId
	 * @param changeCampusId
	 * @return
	 */
	List<Student> findStuByManegerIdAndCampusId(String userId, String changeCampusId);
}
