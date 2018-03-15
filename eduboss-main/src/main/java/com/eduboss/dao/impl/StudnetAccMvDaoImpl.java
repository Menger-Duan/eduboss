package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudnetAccMvDao;
import com.eduboss.domain.StudnetAccMv;

@Repository("StudnetAccMv")
public class StudnetAccMvDaoImpl extends GenericDaoImpl<StudnetAccMv, String> implements StudnetAccMvDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	// 获取学生课时和金钱统计信息
	public StudnetAccMv getStudnetAccMvByStudentId(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from StudnetAccMv where 1=1 ");
		hql.append(" and studentId = :studentId ");
		params.put("studentId", studentId);
		List<StudnetAccMv> list = super.findAllByHQL(hql.toString(), params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void transferAmountToElectronicAcc(String studentId,
			BigDecimal amountForTransfer) {
		StudnetAccMv studnetAccMv = this.findById(studentId);
		if(studnetAccMv.getElectronicAccount() == null)
			studnetAccMv.setElectronicAccount(BigDecimal.ZERO);
		studnetAccMv.setElectronicAccount(studnetAccMv.getElectronicAccount().add(amountForTransfer));
	}
	
	
	
}
