package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.PromiseStudent;
import com.eduboss.domainVo.PromiseClassApplyVo;
import com.eduboss.domainVo.PromiseStudentVo;

/**
 * 目标班学生管理（班主任）
 * */

public interface PromiseStudentDao extends GenericDAO<PromiseStudent,String>{
	
	/**
	 * 班主任学生管理界面点详情按钮，查出该学生的合同信息
	 * */
	public List<PromiseClassApplyVo> findStudentContractInfo(String studentId,String contractProductId);
	
	
	/**
	 * 根据班级ID和学生ID查询合同产品ID
	 * */
	public PromiseStudent getPromiseClassContractProId(String promiseClassId,String studentId);
	
	/**
	 * 目标班报名，如果之前报过此班则update，否则SAVA
	 * */
	public void updateOrSavePromiseStudent(PromiseStudent student);
	
	/**
	 * 查询目标班学生基本信息
	 * */
	public List<PromiseClassApplyVo> getPromiseStudentInfo(PromiseStudent promiseStudent);
	
	
	/**
	 * 查询目标班学生基本信息
	 * */
	public List<PromiseStudent> getPromiseStudentByContractPro(String contractProductId);

}
