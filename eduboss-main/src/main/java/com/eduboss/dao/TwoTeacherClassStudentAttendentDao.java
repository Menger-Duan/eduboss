package com.eduboss.dao;

import com.eduboss.domain.TwoTeacherClassStudentAttendent;
import com.eduboss.domainVo.TwoTeacherClassCourseVo;
import com.eduboss.dto.DataPackage;

import java.util.List;

public interface TwoTeacherClassStudentAttendentDao extends GenericDAO<TwoTeacherClassStudentAttendent, String> {

    DataPackage getTwoTeacherClassCourseList(TwoTeacherClassCourseVo vo, DataPackage dp);

    void updatePicByCourseAndClassId(int twoTeacherCourseId, int twoTeacherClassTwoId, String fileName);

    /**
     * 辅班的已上课时
     * @param classTwoId
     * @return
     */
    Double getConsumeHourByClassTwoId(int classTwoId);


    /**
     * 删除双师考勤记录
     * @param id
     * @param twoClassId
     */
    void deleteTwoTeacherClassStudentAttendentByStudentAndTwoClassId(String id, int twoClassId);


    List<TwoTeacherClassStudentAttendent> getAttendentByCourseAndStudentId(String studentId, int courseId);
}
