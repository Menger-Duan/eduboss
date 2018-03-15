package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.PromiseClassDetailRecord;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domainVo.PromiseClassDetailRecordVo;

@Repository
public interface PromiseClassDetailRecordDao extends GenericDAO<PromiseClassDetailRecord,String> {
	
	/**
	 * 查询学生月结详细信息
	 * */
	public List<PromiseClassDetailRecordVo> findStudentMonthlyDetailInfo(PromiseClassRecord promiseClassRecord);
	

}
