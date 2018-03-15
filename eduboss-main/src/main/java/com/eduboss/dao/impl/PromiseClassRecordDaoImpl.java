package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.PromiseClassRecordDao;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domainVo.PromiseClassRecordVo;
import com.eduboss.utils.HibernateUtils;

@Repository("PromiseClassRecordDao")
public class PromiseClassRecordDaoImpl extends GenericDaoImpl<PromiseClassRecord, String> implements PromiseClassRecordDao{

	@Autowired
	private ContractProductDao contractProductDao;
	
	/**
	 * 合同完结时判断是否还有未扣款的月份的学生
	 * */
	public String getStudentRecordIsInProgress(String studentId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(id) from PromiseClassRecord where studentId = :studentId and status='1' ");
		params.put("studentId", studentId);
		int result = this.findCountHql(hql.toString(), params);
		//如果为不为0则表示还未只是做了保存但未月结扣款的记录
		if(result > 0){
			return "1";
		}else{
		    return "0";
		}
	}
	
	/**
	 * 根据目标班ID和学生ID查询月结信息
	 * */
	public List<PromiseClassRecordVo> getPromiseClassRecordByStudentIdAndPrmoseClassId(String promiseClassId,String studentId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
//		hql.append(" from PromiseClassRecord where promiseClass.id = '").append(promiseClassId).append("'")
		hql.append(" from PromiseClassRecord where 1=1 ")
		.append(" and studentId = :studentId order by classYear asc, classMonth asc ");
		params.put("studentId", studentId);
		List<PromiseClassRecord> list = this.findAllByHQL(hql.toString(), params);
		List<PromiseClassRecordVo> voList = new ArrayList<PromiseClassRecordVo>();
		for(PromiseClassRecord promise : list){
			PromiseClassRecordVo vo = HibernateUtils.voObjectMapping(promise, PromiseClassRecordVo.class);
			voList.add(vo);
		}
		return voList;
	}
	
	@Override
	public List<PromiseClassRecordVo> findPromiseClassRecordByConProIdAndStuId(
			String contractProductId, String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseClassRecord where contractProductId = :contractProductId ")
		.append(" and studentId = :studentId order by classYear asc, classMonth asc ");
		params.put("contractProductId", contractProductId);
		params.put("studentId", studentId);
		List<PromiseClassRecord> list = this.findAllByHQL(hql.toString(), params);
		List<PromiseClassRecordVo> voList = HibernateUtils.voListMapping(list, PromiseClassRecordVo.class);
		return voList;
	}

	/**
	 * 根据目标班ID和学生ID查询月结信息
	 * */
	@Override
	public List<PromiseClassRecord> getPromiseClassRecordByStudentIdAndStatus(String studentId,String status){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseClassRecord where studentId = :studentId ")
		.append(" and status = :status ")
		.append(" order by classYear asc, classMonth asc ");
		params.put("studentId", studentId);
		params.put("status", status);
		return this.findAllByHQL(hql.toString(), params);
	}
	
	@Override
	public BigDecimal countStudentPromiseChargeHoursByStudentId(String studentId) {
		BigDecimal chargeHours = BigDecimal.ZERO;
		List<PromiseClassRecord> pcrs = this.getPromiseClassRecordByStudentIdAndStatus(studentId, "0");
		for (PromiseClassRecord pcr :pcrs) {
			chargeHours = chargeHours.add(pcr.getChargeHours());
		}
		return chargeHours;
	}
	
}
