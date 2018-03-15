package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentAttendanceRecordDao;
import com.eduboss.domain.StudentAttendanceRecord;


@Repository("StudentAttendanceRecordDao")
public class StudentAttendanceRecordDaoImpl extends GenericDaoImpl<StudentAttendanceRecord, String>  implements StudentAttendanceRecordDao {


}
