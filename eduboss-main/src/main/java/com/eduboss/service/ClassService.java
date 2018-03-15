package com.eduboss.service;

import java.util.Map;

import com.eduboss.domainVo.EduPlatform.TextBookParamVo;
import com.eduboss.dto.DataPackage;

/**
 * 负责 一对多班 小班 目标班的各种业务查询
 * @author xiaojinwang
 *
 */

public interface ClassService {
	/**
	 * 开放接口给教育平台 查询小班 班级信息  
	 * @param dataPackage
	 * @param miniClassId
	 * @return
	 */
    Map<String, Object> getMiniClassInfo(DataPackage dataPackage,String miniClassId);
    
    /**
     * 双师 班级详情
     */
    Map<String, Object> getTwoTeacherClassInfo(String classId,String classIdTwo);

	/**
	 * 学生查询自己的班级列表(按照结课时间排序，最晚结课的在最上)
	 * @param studentId
	 * @param type
	 * @param map
	 * @return
	 */
    Map<String,Object> queryStudentClassInfo(String studentId, String type, Map<String, Object> map);
    
    Map<String,Object> getUnChargedClassInfo(String teacherId,String type);
    
    //主班的id查所有的副班
    Map<String, Object> getAllClassTwoByClassId(String[] classId);
    
   //老师查询自己未结课的班级内学生列表
    Map<String, Object> getUnChargedClassStudents(String teacherId,String type,String student);
    
   //根据教材id查询已绑定的班级数量(不分班级type，无论小班一对一等都需要查，支持批量)  但是目前只能支持小班教材的查询
    Map<String, Object> getClassCountByTextBookId(TextBookParamVo textBookParamVo);
    
    //根据教材id查询已绑定的未结课班级列表(按分公司id分组,不分班级type,分页)(目前只支持星火小班)
    Map<String, Object> getUnCompleteClassById(DataPackage dataPackage,String wareId);
    
	//根据教材id和公司查询已绑定的未结课班级列表(不分班级type,分页)(目前只支持星火小班)
    Map<String, Object> getBranchUnCompleteClassById(DataPackage dataPackage,String wareId,String branchId);
    
    //老师查询自己班级内学生列表  结课或者未结课的  支持班级类型 支持老师id
    Map<String, Object> getClassStudents(String type,String teacherId,Boolean finish);
    /**
	 * 
	 * @author: duanmenrun
	 * @Title: getClassStudentsListByStatus 
	 * @Description: TODO 老师查询班级内学生列表
	 * @throws 
	 * @param type 课程类型，
	 * @param teacherId 教师编号
	 * @param student 学生名字(模糊查询)
	 * @param status 是否完成 finished  unfinished
	 * @return
	 */
	Map<String, Object> getClassStudentsListByStatus(String teacherId, String type, String student, String status);
	/**
	 * 根据副班教师获取课程信息
	 * @author: duanmenrun
	 * @Title: getCourseDetailByTeacherTwo 
	 * @Description: TODO 
	 * @param type 课程类型，
	 * @param teacherTwoId 辅班教师编号
	 * @param status 1-未开课，2-开课中，3-已结课
	 * @return
	 */
	Map<String, Object> getCourseDetailByTeacherTwo(String teacherTwoId, String type, String status);

	/**
	 * 学生一对一课程 按照年级和科目聚合成一门课程
	 * @param studentId
	 * @param map
	 * @return
	 */
    Map<String,Object> getOneOnOneCourseByStudentId(String studentId, Map<String, Object> map);

	/**
	 *
	 * @param studentId
	 * @param gradeId
	 * @param subjectId
	 * @param map
	 * @return
	 */
	Map<String,Object> getOneOnOneCourse(String studentId, String gradeId, String subjectId, Map<String, Object> map);

	/**
	 * 根据主班id和客户电话获取学生信息
	 * @param classId
	 * @param phone
	 * @return
	 */
    Map<String,Object> getStudentInfoByClassIdAndPhone(String classId, String phone);

	/**
	 * 老师查一对一的学生列表
	 * @param teacherId
	 * @param student
	 * @param status
	 * @return
	 */
    Map<String,Object> getOneOnOneStudentsListByTeacherId(String teacherId, String student, String status);
}
