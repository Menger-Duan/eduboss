package com.eduboss.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.eduboss.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.HasClassCourseVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TwoClassCourseStudentRosterVo;
import com.eduboss.domainVo.TwoTeacherClassCourseVo;
import com.eduboss.domainVo.TwoTeacherClassStudentAttendentVo;
import com.eduboss.domainVo.TwoTeacherClassStudentVo;
import com.eduboss.domainVo.TwoTeacherClassTwoExcelVo;
import com.eduboss.domainVo.TwoTeacherClassTwoVo;
import com.eduboss.domainVo.TwoTeacherClassVo;
import com.eduboss.domainVo.TwoTeacherCourseSearchVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

@Service
public interface TwoTeacherClassService {

    DataPackage findTwoTeacherClassList(DataPackage dp, TwoTeacherClassVo vo);

    TwoTeacherClassVo findTwoTeacherById(int id);

    Response saveTwoTeacherClass(TwoTeacherClassVo vo);

    DataPackage findTwoTeacherClassCourseList(DataPackage dp, TwoTeacherClassCourseVo vo);

    DataPackage findTwoTeacherClassStudentList(DataPackage dp, TwoTeacherClassStudentVo vo);

    DataPackage findTwoTeacherClassTwoList(DataPackage dp, TwoTeacherClassTwoVo vo);

    void saveTwoTeacherClassCourse(TwoTeacherClassCourse[] courseList, String arrangedHours);

    Response saveTwoTeacherClassTwo(TwoTeacherClassTwoVo vo);

    void AddStudentToClasss(String studentId, int twoClassId, String id, String id1, String firstCourseDate);

    Response deleteTwoTeacherClass(int classId);

    Response deleteTwoTeacherClassTwo(int id);

    TwoTeacherClassTwoVo findTwoTeacherClassTwoById(int id);

    void deleteClassCourse(int[] courseId);

    TwoTeacherClassCourseVo findTwoTeacherClassCourseById(int courseId);

    Response saveTwoTeacherClassCourse(TwoTeacherClassCourseVo vo);

    DataPackage findTwoTeacherClassAttendentList(DataPackage dp, TwoTeacherClassStudentAttendentVo vo);

    List<StudentVo> getStudentWantListByClassId(String classId);

    double findCountCourse(int classId);

    ContractProductVo getTwoClassStuRemain(int classId, String studentId);

    Response checkAllowAddStudent4Class(String studentIds, int classId);

    void changeStudentTwoClass(int oldClassId, int newClassId, String studentIds, String firstClassDate);

    List<TwoTeacherClassTwoVo> findTwoClassForChangeClassSelect(int twoClassId);

    Map getClassCourseTimeInfo(String classId);

    TwoTeacherClassTwoVo findTwoTeacherTwoByContractProductId(String conProId);
    BigDecimal findClassWillBuyHour(int classId, String startDate);
    void deleteTwoClassStudentByCpId(String cpId);


    void updateCourseNumByClassId(int miniClassId);

    Response batchSaveTwoTeacherClassTwo(TwoTeacherClassTwoVo[] twoTeacherClassTwoVos);

    /**
     * 获取双师主班老师
     * @return
     */
    List<Map<String,String>> getTwoTeacherClassTeacher();


    DataPackage getTwoTeacherClassCourseList(DataPackage dp, TwoTeacherClassCourseVo vo);

    DataPackage getTwoTeacherClassStudentAttendentList(DataPackage dp, TwoTeacherClassCourseVo vo);

    Response modifyTwoTeacherClassTwoCourseStudentAttendance(int courseId, int classTwoId, String attendanceData, String oprationCode) throws Exception;

    List<HasClassCourseVo> checkHasTwoTeacherClassForPeriodDate(String teacherId, String startDate, String endDate);

    void modifyTwoTeacherClassTwoCourseStudentAttendanceSupplement(int id, String supplementDate, String absentRemark);

    void submitTwoTeacherPic(int twoTeacherCourseId, int twoTeacherClassTwoId, MultipartFile attendancePicFile, String servicePath);

    TwoTeacherClassCourseVo findTwoTeacherClassCourseByCourseIdAndClassId(int twoTeacherCourseId, int twoTeacherClassTwoId);

    /**
     * 双师下次上课时间
     * @param currentDate
     * @param twoTeacherClassId
     * @return
     */
    String getNextTwoTeacherClassCourseByDate(String currentDate, String twoTeacherClassId);

    //登录用户权限范围内主班老师列表
    List<Map<String,String>> getLoginUserMainTeacherList();
    //登录用户权限范围内辅班老师列表
    List<Map<String,String>> getLoginUserTwoTeacherList();

    /**
     * 双师退班
     * @param studentIds
     * @param twoClassId
     */
    void deleteStudentInTwoTeacherTwo(String studentIds, int twoClassId);
    
    /**
     * 获取辅班的学生名册
     * @param twoClassId
     * @return
     */
    public TwoClassCourseStudentRosterVo getTwoCourseStudentRoster(Integer twoClassId);
    
    List<TwoTeacherClassStudent> getTwoTeacherClassStudentsByTwoClassId(String twoClassId);
    
    List<Map<Object,Object>> getTwoTeacherCourseScheduleList(TwoTeacherCourseSearchVo courseSearch);
    
    List<TwoTeacherClassTwoExcelVo> getTwoClassTwoToExcel(TwoTeacherClassTwoVo vo,DataPackage dataPackage);
    
    List<Map<Object, Object>> getTwoClassTwoToExcelSize(TwoTeacherClassTwoVo vo,DataPackage dataPackage);
    
    //xiaojinwang 20171029
	TwoTeacherClassCourse getTwoTeacherCourseById(int twoTeacherCourseId);
	
	void updatePicByCourseAndClassId(int twoTeacherCourseId, int twoTeacherClassTwoId, String fileName);




    /**
     * 双师续班率
     * 根据老师 年份 季度找到对应的辅班
     * @param teacherId
     * @param productVersion
     * @param productQuarter
     * @param gradeId
     * @param subjectId
     * @return
     */
	List<TwoTeacherClassTwoVo> getTwoTeacherClassTwoByTeacherAndYearAndQuarter(String teacherId, String productVersion, String productQuarter, String gradeId, String subjectId);

    /**
     * 双师续班率
     * 按年级和科目分类双师辅班
     * @param list
     * @return
     */
    Map<String, List<TwoTeacherClassTwoVo>> classifyTwoTeacherClassTwoForGradeAndSubject(List<TwoTeacherClassTwoVo> list);

    /**
     *
     * @param teacherId
     * @param productVersion
     * @param productQuarter
     * @param map
     */
    Map<String, Integer> getStudentXuDu(String teacherId, String productVersion, String productQuarter, Map<String, List<TwoTeacherClassTwoVo>> map);



    /**
     *
     * @param list
     * @return
     */
    List<Student> getStudentFromTwoTeacherClassTwo(List<TwoTeacherClassTwoVo> list);


    /**
     *
     * @param preMap
     * @param nextMap
     */
    Map<String, Integer> xuduStudent(Map<String, List<Student>> preMap, Map<String, List<Student>> nextMap);


    /**
     *  @param teacherId
     * @param productVersion
     * @param productQuarter
     * @param twoTeacherClassTwoExcelVo
     */
    Map<String, String> getXuduStudent(String teacherId, String productVersion, String productQuarter, TwoTeacherClassTwoExcelVo twoTeacherClassTwoExcelVo);

    /**
     * 统一规定时间: 以下一季的第二讲上课时间为取数节点
     * @param list
     * @param productQuarter
     * @param productVersion
     * @return
     */
    List<Student> getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(List<TwoTeacherClassTwoVo> list, String productQuarter, String productVersion);


    /**
     *
     * @param utilTime
     * @param list
     * @return
     */
    List<Student> getAbleXuduStudentByTimeAndClassId(String utilTime, List<TwoTeacherClassTwoVo> list);




    /**
     *
     * @param dataPackage
     * @param twoTeacherClassTwoExcelVo
     * @return
     */
    DataPackage getTwoTeacherClassTwoXuDuRate(DataPackage dataPackage, TwoTeacherClassTwoExcelVo twoTeacherClassTwoExcelVo);


    /**
     * 获取所有有主班的分公司
     * @return
     */
    List<Organization> getAllClassBranch();

    /**
     * 双师已经考勤最多的人数
     * @param twoTeacherClassTwoByTeacherAndYearAndQuarter
     * @return
     */
    int getMaxStudentNumFromTwoTeacherClassTwo(List<TwoTeacherClassTwoVo> twoTeacherClassTwoByTeacherAndYearAndQuarter);


    /**
     * 更新双师主班开始上课日期
     * 双师主班和辅班的开始上课日期更新规则优化：
     * 1，当前双师主班未排课，双师主班和辅班的开始上课日期，获取双师主班建班或修改时填写的开始上课日期；
     * 2，当前双师主班已排课，双师主班和辅班的开始上课日期，自动更新为双师主班课程中第一讲课程的上课日期。
     * @param twoTeacherClass
     * @return
     */
    TwoTeacherClass updateTwoTeacherClassStartDate(TwoTeacherClass twoTeacherClass);

    DataPackage findCourseByClassTwoId(DataPackage dp, TwoTeacherClassCourseVo vo);
}
