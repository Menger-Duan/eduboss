package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ElectronicAccChangeType;
import com.eduboss.domain.ElectronicAccountChangeLog;
import com.eduboss.domainVo.ElectronicAccountChangeLogVo;


/**
 * @classname	PointialStudentDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface ElectronicAccountChangeLogDao extends GenericDAO<ElectronicAccountChangeLog, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
	/**
	 * @param studentAccMvId
	 * @param changeType A - add, D - decrease
	 * @param changeAmount
	 * @param afterAmount
	 * @param remark
	 */
	public void saveElecAccChangeLog(String studentAccMvId, ElectronicAccChangeType type, String changeType, BigDecimal changeAmount, BigDecimal afterAmount, String remark);
	
	
	/**
	 * 根据学生ID查询电子账户记录
	 * */
	public List<ElectronicAccountChangeLogVo> getElectronicAccountChangeLogByStudentId(ElectronicAccountChangeLogVo electronicAccountChangeLogVo);

	/**
	 *
	 * @param dataPackage
	 * @param vo
	 * @param timeVo
	 * @return
	 */
	DataPackage getStudentRechargeRecord(DataPackage dataPackage, ElectronicAccountChangeLogVo vo, TimeVo timeVo);
}
