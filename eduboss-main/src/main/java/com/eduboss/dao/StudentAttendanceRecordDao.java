package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.StudentAttendanceRecord;

@Repository
public interface StudentAttendanceRecordDao extends GenericDAO<StudentAttendanceRecord, String> {

}