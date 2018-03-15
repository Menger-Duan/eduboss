package com.eduboss.dao;

import com.eduboss.domain.StudentAccInfo;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentAccInfoDao extends GenericDAO<StudentAccInfo, String> {

    StudentAccInfo findInfoByStudentId(String id);
}
