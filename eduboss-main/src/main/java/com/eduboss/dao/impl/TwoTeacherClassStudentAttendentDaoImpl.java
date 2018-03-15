package com.eduboss.dao.impl;

import com.eduboss.dao.TwoTeacherClassStudentAttendentDao;
import com.eduboss.domain.TwoTeacherClassCourse;
import com.eduboss.domain.TwoTeacherClassStudentAttendent;
import com.eduboss.domainVo.TwoTeacherClassCourseVo;
import com.eduboss.dto.DataPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/30.
 */
@Repository("TwoTeacherClassStudentAttendentDao")
public class TwoTeacherClassStudentAttendentDaoImpl extends GenericDaoImpl<TwoTeacherClassStudentAttendent, String> implements TwoTeacherClassStudentAttendentDao {
    @Override
    public DataPackage getTwoTeacherClassCourseList(TwoTeacherClassCourseVo vo, DataPackage dp) {
        Map<String, Object> params = new HashMap<String, Object>();
//        StringBuffer hql = new StringBuffer();
//        hql.append(" select ttcsa from TwoTeacherClassStudentAttendent ttcsa  ");
//
//        hql.append(" where 1=1  GROUP BY ttcsa.twoTeacherClassTwo, ttcsa.twoTeacherClassCourse  ");//order by ttcsa.twoTeacherClassCourse.courseDate desc


//        sql.append(" select from two_teacher_class_student_attendent GROUP BY TWO_CLASS_COURSE_ID, CLASS_TWO_ID ");

        StringBuffer sql = new StringBuffer();
//        sql.append(" select ttcsa.* from TWO_TEACHER_CLASS_STUDENT_ATTENDENT ttcsa left join  TWO_TEACHER_CLASS_COURSE ttcc on ttcsa.TWO_CLASS_COURSE_ID=ttcc.COURSE_ID  ");//, two_teacher_class_two ttct, TWO_TEACHER_CLASS ttc
//        sql.append(" left join two_teacher_class_two ttct on ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID ");
//        sql.append(" left join TWO_TEACHER_CLASS ttc on ttct.CLASS_ID= ttc.CLASS_ID");
//        sql.append(" , product p, organization campus, user mainTeacher, user secondTeacher  ");
//        sql.append(" where ttcc.CLASS_ID = ttct.CLASS_ID   "); //考勤 课程
////        sql.append(" and ttcsa.CLASS_TWO_ID=ttct.CLASS_TWO_ID "); // 考勤 辅班
//        sql.append(" and ttct.TEACHER_ID = secondTeacher.USER_ID ");
////        sql.append(" and ttct.CLASS_ID = ttcc.CLASS_ID ");// 辅班 课程
////        sql.append(" and ttc.CLASS_ID = ttcc.CLASS_ID");//主班 辅班
//        sql.append(" and ttc.TEACHER_ID = mainTeacher.USER_ID ");
//        sql.append(" and ttct.BL_CAMPUS_ID =  campus.id ");
//        sql.append(" and campus.orgType='CAMPUS' ");
//        sql.append(" and p.ID=ttc.PRODUCE_ID  ");

        sql.append(" select ttcsa.* from  ");
        sql.append(" two_teacher_class_course ttcc, ");
        sql.append(" two_teacher_class_student_attendent ttcsa, ");
        sql.append(" two_teacher_class_two ttct, ");
        sql.append(" two_teacher_class ttc, ");
        sql.append(" product p, ");
        sql.append(" organization campus, user mainTeacher, user secondTeacher ");
        sql.append(" WHERE ttcc.COURSE_ID= ttcsa.TWO_CLASS_COURSE_ID ");
        sql.append(" and ttc.CLASS_ID = ttcc.CLASS_ID ");
        sql.append(" and ttct.CLASS_ID = ttcc.CLASS_ID ");
        sql.append(" and ttc.PRODUCE_ID = p.ID ");
        sql.append(" and ttct.TEACHER_ID = secondTeacher.USER_ID ");
        sql.append(" and ttc.TEACHER_ID = mainTeacher.USER_ID ");
        sql.append(" and ttct.BL_CAMPUS_ID =  campus.id ");
        sql.append(" and ttct.CLASS_TWO_ID = ttcsa.CLASS_TWO_ID ");
        sql.append(" and campus.orgType='CAMPUS' ");

        if (StringUtils.isNotBlank(vo.getStartDate())){
            sql.append(" and ttcc.COURSE_DATE>= :startDate ");
            params.put("startDate", vo.getStartDate());
        }
        if (StringUtils.isNotBlank(vo.getEndDate())){
            sql.append(" and ttcc.COURSE_DATE <= :endDate ");
            params.put("endDate", vo.getEndDate());
        }
        if (StringUtils.isNotBlank(vo.getTwoTeacherClassTwoName())){
            sql.append(" and ttct.NAME like :twoTeacherClassTwoName ");
            params.put("twoTeacherClassTwoName", "%"+vo.getTwoTeacherClassTwoName()+"%");
        }

        if (StringUtils.isNotBlank(vo.getBlCampusName())){
            sql.append(" and campus.name like :campusName ");
            params.put("campusName", "%"+vo.getBlCampusName()+"%");
        }

        if (StringUtils.isNotBlank(vo.getGradeId())){
            sql.append(" and p.GRADE_ID = :gradeId ");
            params.put("gradeId", vo.getGradeId());
        }

        if (StringUtils.isNotBlank(vo.getSubjectId())){
            sql.append(" and ttc.SUBJECT= :subject ");
            params.put("subject", vo.getSubjectId());
        }

        if (StringUtils.isNotBlank(vo.getTeacherName())){
            sql.append(" and mainTeacher.name like :mainTeacherName ");
            params.put("mainTeacherName", "%"+vo.getTeacherName()+"%");
        }
        if (StringUtils.isNotBlank(vo.getTeacherTwoName())){
            sql.append(" and secondTeacher.name like :secondTeacherName ");
            params.put("secondTeacherName", "%"+vo.getTeacherTwoName()+"%");
        }
        if (StringUtils.isNotBlank(vo.getTeacherTwoUserId())){
            sql.append(" and ttct.TEACHER_ID = :teacherTwoUserId ");
            params.put("teacherTwoUserId", vo.getTeacherTwoUserId());
        }

        if (StringUtils.isNotBlank(vo.getCourseStatusValue())){
            sql.append(" and ttcsa.COURSE_STATUS = :courseStatus");
            params.put("courseStatus", vo.getCourseStatusValue());
        }

        if (vo.getTwoTeacherClassTwoId()>0){
            sql.append(" and ttcsa.CLASS_TWO_ID = :classTwoId ");
            params.put("classTwoId", vo.getTwoTeacherClassTwoId());
        }

        if (StringUtils.isNotBlank(vo.getSearchParam())){
            sql.append(" and (mainTeacher.name like :p1 ");
            sql.append(" or ttct.NAME like  :p2 ) ");
            params.put("p1", "%" + vo.getSearchParam() + "%");
            params.put("p2", "%" + vo.getSearchParam() + "%");
        }

        if (StringUtils.isNotBlank(vo.getTeacherTwoUserId())){
            sql.append(" and ttct.TEACHER_ID = :teacherTwoUserId");
            params.put("teacherTwoUserId", vo.getTeacherTwoUserId());
        }

        if (vo.getAuditStatus()!=null){
            sql.append(" and ttcsa.AUDIT_STATUS = :auditStatus ");
            params.put("auditStatus", vo.getAuditStatus());
        }

        sql.append(" group by ttcsa.TWO_CLASS_COURSE_ID, ttcsa.CLASS_TWO_ID  order by ttcc.COURSE_DATE desc  ");//
        dp = super.findPageBySql(sql.toString(), dp, true, params);
//        dp = super.findPageByHQL(hql.toString(), dp, true, params);
        dp.setRowCount(findCountSql("select count(*) from ("+sql.toString()+") a", params));
        return dp;


    }


    /**
     * 更新考勤图片信息
     * @param courseId
     * @param classId
     * @param picName
     */
    @Override
    public void updatePicByCourseAndClassId(int courseId, int classId, String picName) {
        String sql = " update two_teacher_class_student_attendent set ATTENDANCE_PIC_NAME=:picName where TWO_CLASS_COURSE_ID=:courseId and CLASS_TWO_ID=:classId ";
        Map<String,Object> param= new HashMap<>();
        param.put("picName",picName);
        param.put("courseId",courseId);
        param.put("classId",classId);
        this.excuteSql(sql,param);
    }

    /**
     * 辅班的已上课时
     *
     * @param classTwoId
     * @return
     */
    @Override
    public Double getConsumeHourByClassTwoId(int classTwoId) {
        String sql = " select * from two_teacher_class_student_attendent  where (COURSE_STATUS='CHARGED'or COURSE_STATUS='TEACHER_ATTENDANCE') and  class_two_id=:classTwoId group by two_class_course_id ";

        Map<String,Object> params= new HashMap<>();
        params.put("classTwoId", classTwoId);
        List<TwoTeacherClassStudentAttendent> result = super.findBySql(sql, params);
        Double consume = 0.0;
        for (TwoTeacherClassStudentAttendent v :result){
            TwoTeacherClassCourse twoTeacherClassCourse = v.getTwoTeacherClassCourse();
            if (twoTeacherClassCourse!=null){
                consume+=twoTeacherClassCourse.getCourseHours();
            }
        }
        return consume;
    }


    /**
     * 删除双师考勤记录
     *
     * @param id
     * @param twoClassId
     */
    @Override
    public void deleteTwoTeacherClassStudentAttendentByStudentAndTwoClassId(String id, int twoClassId) {
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", id);
        map.put("twoClassId", twoClassId);
        String sql =" DELETE  FROM two_teacher_class_student_attendent WHERE STUDENT_ID=:studentId AND CLASS_TWO_ID=:twoClassId  AND CHARGE_STATUS='UNCHARGE' ";
        super.excuteSql(sql, map);
    }

    @Override
    public List<TwoTeacherClassStudentAttendent> getAttendentByCourseAndStudentId(String studentId, int courseId) {
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", studentId);
        map.put("courseId", courseId);
        String sql =" FROM TwoTeacherClassStudentAttendent WHERE student.id=:studentId AND twoTeacherClassCourse.courseId=:courseId";
        return this.findAllByHQL(sql, map);
    }
}
