package com.eduboss.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.StudentMoneyChanges;
import com.eduboss.dto.DataPackage;

@Repository
public interface StudentMoneyChangesDao extends GenericDAO<StudentMoneyChanges, String>{
	
	/**
	 * 获取学生资金变更记录
	 * */
	public DataPackage getStudentMondyChanges(StudentMoneyChanges studentMoneyChanges,Map map, DataPackage dataPackage);

}
