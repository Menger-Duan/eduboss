package com.eduboss.dao;



import com.eduboss.domain.DeletedataLog;
import org.springframework.stereotype.Repository;


@Repository
public interface InitDataDeleteDao extends GenericDAO<DeletedataLog, String> {
	
	/**
	 * 根据合同ID删除系统相关信息
	 * @param contractId
	 */
	public void deleteByContractId(String contractId);
	
	
	/**
	 * 根据学生ID删除系统相关信息
	 * @param studentId
	 */
	public void deleteByStudentId(String studentId);


	public void checkContractIfCanDelete(String id);
	
	/**
	 * 检查学生是否能删除
	 * @param studentId
	 */
	public void checkStudentIfCanDelete(String studentId);


	
	
	
	
	
	
	
}
