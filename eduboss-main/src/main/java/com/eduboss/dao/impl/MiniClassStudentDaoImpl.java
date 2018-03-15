package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.dao.MiniClassStudentDao;
import com.eduboss.domain.Contract;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassStudent;
import com.eduboss.domainVo.MiniClassStudentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.google.common.collect.Maps;

@Repository
public class MiniClassStudentDaoImpl extends GenericDaoImpl<MiniClassStudent, Integer> implements MiniClassStudentDao {
	
	/**
	 * 小班学生考勤列表
	 */
	@Override
	public DataPackage getMiniClassStudentList(MiniClassStudentVo miniClassStudentVo,
			DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer hql = new StringBuffer();
		hql.append(" from MiniClassStudent  where 1=1 ");
		
		if (StringUtils.isNotBlank(miniClassStudentVo.getMiniClassId())) {
			hql.append(" and miniClass.miniClassId = :miniClassId ");
			params.put("miniClassId", miniClassStudentVo.getMiniClassId());
		}
		if (StringUtils.isNotBlank(miniClassStudentVo.getStudentId())) {
			hql.append(" and student.id = :studentId ");
			params.put("studentId", miniClassStudentVo.getStudentId());
		}
		if (StringUtils.isNotBlank(miniClassStudentVo.getMiniClassStudentChargeStatus())) {
			hql.append(" and miniClassStudentChargeStatus = :chargeStatus ");
			params.put("chargeStatus", miniClassStudentVo.getMiniClassStudentChargeStatus());
		}
		if (StringUtils.isNotBlank(miniClassStudentVo.getEndDate())) {// 这里的结束日期是小班插班的情况， 指小班报名必须要该节课程前，才允许上课考勤。
			hql.append(" and createTime < :createTime ");
			params.put("createTime", miniClassStudentVo.getEndDate());
		}
		
		if(StringUtils.isNotBlank(miniClassStudentVo.getFirstSchoolTime())){
			hql.append(" and firstSchoolTime <= :firstSchoolTime ");
			params.put("firstSchoolTime", miniClassStudentVo.getFirstSchoolTime());
		}
		hql.append(" order by createTime desc ");

		return super.findPageByHQL(hql.toString(), dp,true,params);
	}
	
	/**
	 * 小班学生考勤列表（未扣费）
	 * @param miniClassStudentVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassStudentListUncharge(MiniClassStudentVo miniClassStudentVo,
			DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from MiniClassStudent  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(miniClassStudentVo.getMiniClassId())) {
			hql.append(" and miniClass.miniClassId = :miniClassId ");
			params.put("miniClassId", miniClassStudentVo.getMiniClassId());
		}
		if (StringUtils.isNotBlank(miniClassStudentVo.getStudentId())) {
			hql.append(" and student.id = :studentId ");
			params.put("studentId", miniClassStudentVo.getStudentId());
		}
		if (StringUtils.isNotBlank(miniClassStudentVo.getMiniClassStudentChargeStatus())) {
			hql.append(" and miniClassStudentChargeStatus = :chargeStatus ");
			params.put("chargeStatus", MiniClassStudentChargeStatus.UNCHARGE);
		}
		if (StringUtils.isNotBlank(miniClassStudentVo.getEndDate())) {// 这里的结束日期是小班插班的情况， 指小班报名必须要该节课程前，才允许上课考勤。
			hql.append(" and createTime < :createTime ");
			params.put("createTime", miniClassStudentVo.getEndDate());
		}
		
		if(StringUtils.isNotBlank(miniClassStudentVo.getFirstSchoolTime())){
			hql.append(" and firstSchoolTime <= :firstSchoolTime ");
			params.put("firstSchoolTime", miniClassStudentVo.getFirstSchoolTime());
		}
		hql.append(" order by contractProduct.status desc ");

		return super.findPageByHQL(hql.toString(), dp,true,params);
	}

	/**
	 * 获取一条小班学生记录
	 */
	@Override
	public MiniClassStudent getOneMiniClassStudent(String miniClassId, String studentId) {
		
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criterias = session.createCriteria(MiniClassStudent.class)
			    .add( Restrictions.eq("miniClass.miniClassId", miniClassId))
			    .add( Restrictions.eq("student.id", studentId))
			    .addOrder(Order.desc("createTime"))
		 		.setFirstResult(0).setMaxResults(1);
		MiniClassStudent miniClassStudent = null;
		if (criterias.list().size() > 0) {
			miniClassStudent = (MiniClassStudent) criterias.list().get(0);
		}
		return miniClassStudent;
	
	}
	
	@Override
	public MiniClassStudent getMiniClassStudentByStuIdAndCpId(String studentId,String contractProductId) {
		//不包含精英课堂的产品
		Map<String, Object> params = Maps.newHashMap();
		params.put("studentId", studentId);
		params.put("contractProductId", contractProductId);
		String hql=" from MiniClassStudent where student.id = :studentId and contractProduct.id= :contractProductId and contractProduct.type<>'ECS_CLASS'";
		
		return this.findOneByHQL(hql,params);
	}
	
	
	@Override
	public List<MiniClassStudent> getMiniClassStudent(String miniClassId) {
		String hql=" from MiniClassStudent where 1=1";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(miniClassId)){
			hql+=" and miniClass.miniClassId= :miniClassId ";
			params.put("miniClassId", miniClassId);
		}
		return this.findAllByHQL(hql,params);
	}

	@Override
	public List<MiniClassStudent> listMiniClassStudentByContract(Contract contract) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("studentId", contract.getStudent().getId());
		params.put("contractId", contract.getId());
		StringBuffer sql = new StringBuffer(" select * from mini_class_student where 1 = 1 ");
		sql.append("  and STUDENT_ID = :studentId ");
		sql.append(" and CONTRACT_ID = :contractId ");
		
		return super.findBySql(sql.toString(),params);
	}
	
	/**
	 * 小班扣费总人数
	 */
	public int getChargedPeopleQuantity(String miniClassId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" select IFNULL(count(*), 0) from mini_class_student a ");
		sql.append(" inner join contract_product b on a.CONTRACT_PRODUCT_ID = b.ID ");
		if(StringUtils.isNotBlank(miniClassId)) {
			sql.append(" where MINI_CLASS_ID = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		int chargedPeopleQuantity = findCountSql(sql.toString(),params);
		return chargedPeopleQuantity;
	}
	
	/**
	 * 小班扣费总金额
	 */
	public BigDecimal getChargedMoneyQuantity(String miniClassId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(b.CONSUME_AMOUNT) TOTAL_CONSUME_AMOUNT from mini_class_student a ");
		sql.append(" inner join contract_product b on a.CONTRACT_PRODUCT_ID = b.ID ");
		if(StringUtils.isNotBlank(miniClassId)) {
			sql.append(" where a.MINI_CLASS_ID = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		List<Map<Object,Object>> list = findMapBySql(sql.toString(),params);
		if (list.size() > 0 && list.get(0) !=null ) {
			Map map = list.get(0);
			if (map.get("TOTAL_CONSUME_AMOUNT") != null) {
				BigDecimal bigDecimal = (BigDecimal) list.get(0).get("TOTAL_CONSUME_AMOUNT");
				return bigDecimal;
			} else {
				return BigDecimal.ZERO;
			}
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * 小班详情-在读学生信息列表
	 */
	@Override
	public DataPackage getMiniClassDetailStudentList(String miniClassId,
			DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer hql = new StringBuffer();
		hql.append(" from MiniClassStudent  where 1=1 ");
		
		if (StringUtils.isNotBlank(miniClassId)) {
			hql.append(" and miniClass.miniClassId = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		
		hql.append(" order by firstSchoolTime desc ");

		return super.findPageByHQL(hql.toString(), dp,true,params);
	}
	
	/**
	 * 小班剩余课时
	 */
	@Override
	public BigDecimal getMiniClassTotalRemainCourseHour(String miniClassId) {
		if (StringUtils.isBlank(miniClassId)) {
			throw new ApplicationException("查询方法：（小班剩余总课时），小班ID入参为空，请检查！");
		}
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" select SUM(((b.PAID_AMOUNT - b.CONSUME_AMOUNT) / (b.PRICE * b.DEAL_DISCOUNT))) TOTAL_REMAIN_COURSE_HOUR ");
		sql.append(" from mini_class_student a  ");
		sql.append(" INNER JOIN contract_product b on a.CONTRACT_PRODUCT_ID = b.ID  ");
		if(StringUtils.isNotBlank(miniClassId)) {
			sql.append(" where MINI_CLASS_ID = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		sql.append(" group by MINI_CLASS_ID ");
		List<Map<Object,Object>> list = findMapBySql(sql.toString(),params);
		if (list.size() > 0 && list.get(0) !=null ) {
			Map map = list.get(0);
			if (map.get("TOTAL_REMAIN_COURSE_HOUR") != null) {
				BigDecimal bigDecimal = (BigDecimal) list.get(0).get("TOTAL_REMAIN_COURSE_HOUR");
				return bigDecimal;
			} else {
				return BigDecimal.ZERO;
			}
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Override
	public MiniClass findMiniClassByContractProductId(String contractProductId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("contractProductId", contractProductId);
		hql.append(" from MiniClassStudent  miniClassStudent where 1=1 ");
		hql.append(" and contractProduct.id = :contractProductId ");
		List<MiniClassStudent> miniClassStudents = this.findAllByHQL(hql.toString(),params);
		if(miniClassStudents.size() == 1) {
			return miniClassStudents.get(0).getMiniClass();
		} else {
			return null;
		}
	}
	
	@Override
	public DataPackage findPageByHQLForAcc(String hql,DataPackage dp,String countHql) {
		Map<String, Object> params = Maps.newHashMap();
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setDatas(q.list());
		dp.setRowCount(findCountHql(countHql,params));
		return dp;
	}
	
	/**
	 * 计算小班人数
	 */
	@Override
	public int countMiniClassPeopleNum(String miniClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select COUNT(*) from mini_class_student WHERE 1=1 AND MINI_CLASS_ID = :miniClassId ";
		params.put("miniClassId", miniClassId);
		return super.findCountSql(sql, params);
	}

	@Override
	public String[] getCustomerContactByStuIds(String[] studentIds,String miniClassId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("studentIds", studentIds);
		params.put("miniClassId", miniClassId);
	//	sql.append(" select c.CONTACT as contact from customer c ,customer_student_relation csr where c.ID = csr.CUSTOMER_ID ");
	//	sql.append(" and csr.STUDENT_ID in ( :studentIds )");
		sql.append(" select cus.CONTACT as contact from mini_class_student mcs ,contract c,customer cus where mcs.CONTRACT_ID = c.ID and c.CUSTOMER_ID = cus.ID");
		sql.append(" and mcs.MINI_CLASS_ID = :miniClassId and mcs.STUDENT_ID in ( :studentIds ) ");
		List<Map<Object, Object>> list = this.findMapBySql(sql.toString(), params);
		if(list!=null && list.size()>0){
			String studentContacts[] = new String[list.size()];
			for(int i=0;i<list.size();i++){
				studentContacts[i] = list.get(i).get("contact")!=null?list.get(i).get("contact").toString():"";
			}
			return studentContacts;
		}
		
		return null;
	}
	
}
