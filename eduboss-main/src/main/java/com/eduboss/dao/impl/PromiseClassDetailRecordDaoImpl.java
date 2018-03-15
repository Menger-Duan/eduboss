package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.PromiseClassDetailRecordDao;
import com.eduboss.domain.PromiseClassDetailRecord;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domainVo.PromiseClassDetailRecordVo;
import com.eduboss.utils.HibernateUtils;

@Repository("PromiseClassDetailRecordDao")
public class PromiseClassDetailRecordDaoImpl extends GenericDaoImpl<PromiseClassDetailRecord,String> implements PromiseClassDetailRecordDao{
	
	
	/**
	 * 查询学生月结详细信息
	 * */
	@Override
	public List<PromiseClassDetailRecordVo> findStudentMonthlyDetailInfo(PromiseClassRecord promiseClassRecord){
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append(" from PromiseClassDetailRecord where promiseClassRecord.id= :promiseClassRecordId ");
		params.put("promiseClassRecordId", promiseClassRecord.getId());
		List<PromiseClassDetailRecord> list = this.findAllByHQL(hql.toString(), params);
		List<PromiseClassDetailRecordVo> voList = new ArrayList<PromiseClassDetailRecordVo>();
		for(PromiseClassDetailRecord record : list){
			PromiseClassDetailRecordVo vo = HibernateUtils.voObjectMapping(record, PromiseClassDetailRecordVo.class);
			voList.add(vo);
		}
		return voList;
	}

}
