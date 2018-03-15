package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.PromiseClass;
import com.eduboss.dto.DataPackage;

/**
 * 目标班
 * @author laiyongchang
 * */

@Repository
public interface PromiseClassDao extends GenericDAO<PromiseClass,String>{
	
	/**
	 * 目标班列表
	 * */
	public DataPackage getPromiseClassList(PromiseClass pc,DataPackage dataPackage);
	
	/**
	 * 统计学生的成功率
	 * */
	public double  countStudentSuccessRate(String promiseClassId);
	
	/**
	 * 对目标班做结课处理
	 * */
	public void endPromiseClass(String promiseClassId,String successRate);

}
