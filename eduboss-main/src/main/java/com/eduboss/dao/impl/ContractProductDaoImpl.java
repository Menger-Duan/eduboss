package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.RoleQLConfigSearchVo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ProductType;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Course;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.OtmClassStudentAttendent;
import com.eduboss.domain.Product;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.utils.DateTools;
import com.google.common.collect.Maps;

@Repository
public class ContractProductDaoImpl extends GenericDaoImpl<ContractProduct, String> implements  ContractProductDao{
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Override
	public List<ContractProduct> findContractProductByProductAndStudent(String studentId, String productId) {
		StringBuffer hql =  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("from ContractProduct where 1=1");
		hql.append(" and contract.student.id = :studentId ");
		hql.append(" and product.id= :productId");
		params.put("studentId",studentId);
		params.put("productId",productId);
		return this.findAllByHQL(hql.toString(), params);
	}

	@Override
	public List<ContractProduct> getOneContractProducts(Student student) {
		StringBuffer hql =  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("from ContractProduct contractProduct where 1=1");
		hql.append(" and contractProduct.contract.student.id = :studentId ");
		hql.append(" and type = :type ");
		hql.append(" order by contractProduct.contract.create_time desc");
		params.put("studentId", student.getId());
		params.put("type", ProductType.ONE_ON_ONE_COURSE_NORMAL.getValue());
		List<ContractProduct> list= this.findAllByHQL(hql.toString(), params);
		return list;
	}

	@Override
	public int countByProductId(String productId) {
		List<Criterion> criterion=new ArrayList<Criterion>();
		criterion.add(Restrictions.eq("product.id", productId));
		return findCountByCriteria(criterion);
	}

	@Override
	public void deleteById(String id) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		excuteHql("delete from MoneyArrangeLog where contractProduct.id = :id ", params);

		excuteHql("delete from ContractProduct where id = :id ", params);
	}
	
	@Override
	public void save(ContractProduct contractProduct) {
		super.save(contractProduct);
	}
	
	@Override
	public List<ContractProduct> getEscProductByCampus(String campus,
			String miniClassId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from ContractProduct cp where type='ECS_CLASS' and status<>'CLOSE_PRODUCT' and status<>'ENDED' ");
		
		if(StringUtils.isNotEmpty(campus)){
			// 学生所属校区或跨校区等于小班校区
			hql.append(" and contract.student.id in (select student.id from StudentOrganization where organization.id = :campus ) ");
			params.put("campus", campus);
//			hql.append("and contract.blCampusId='"+campus+"'");
		}
		
		if(StringUtils.isNotEmpty(miniClassId)){
			hql.append("and contract.student.id not in (select student.id from MiniClassStudent where miniClass.miniClassId= :miniClassId )");
			params.put("miniClassId", miniClassId);
		}
		
		List<ContractProduct> list= this.findAllByHQL(hql.toString(), params);
		return list;
	}
	
	@Override
	public List<Map<Object, Object>> getCanUseEcsContractProduct(String campus,
			String miniClassId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select pcs.PROMISE_CLASS_ID escClassId,pc.PNAME escClassName,teacher.NAME teacherName");
		sql.append(" ,c.student_id studentId,c.id contractId,concat(cp.id,'') id,s.name studentName,p.name productionName");
		sql.append(" ,cp.PLAN_AMOUNT totalAmount ");
		sql.append(",(case when CP.STATUS<>'CLOSE_PRODUCT'and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED' then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END) else 0 end ) remainingAmount");
		sql.append("  from promise_class_student pcs");
		sql.append(" left join promise_class pc on pc.id=pcs.PROMISE_CLASS_ID");
		sql.append(" left join user teacher on teacher.USER_ID=pc.HEAD_TEACHER");
		sql.append(" left join contract_product cp on cp.id = pcs.CONTRACT_PRODUCT_ID");
		sql.append(" LEFT JOIN CONTRACT c on c.id=cp.CONTRACT_ID");
		sql.append(" left join student s on s.id = c.student_id");
		sql.append(" left join product p on cp.product_id= p.id");
		sql.append(" where cp.TYPE='ECS_CLASS' AND cp.status<>'CLOSE_PRODUCT' AND cp.STATUS<>'ENDED' and pcs.ABORT_CLASS is null ");
		
		if(StringUtils.isNotEmpty(campus)){
			// 学生所属校区或跨校区等于小班校区
			sql.append(" and c.STUDENT_ID in (select student_id from student_organization where ORGANIZATION_ID= :campus)");
			params.put("campus", campus);
		}
		
		if(StringUtils.isNotEmpty(miniClassId)){
			sql.append(" and c.STUDENT_ID not in(select student_id from mini_class_student where MINI_CLASS_ID= :miniClassId )");
			params.put("miniClassId", miniClassId);
		}
		return this.findMapBySql(sql.toString(), params);
	}
	
	/**
	 * 根据一对多类型和学生ID查询合同列表
	 * @param otmType
	 * @param studentId
	 * @return
	 */
	@Override
	public List<ContractProduct> getOtmContractProductByOtmTypeAndStu(Integer otmType, String studentId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from ContractProduct where type='ONE_ON_MANY' and status<>'CLOSE_PRODUCT' and status<>'ENDED' ");
		
		if(StringUtils.isNotEmpty(studentId)){
			hql.append("and contract.student.id= :studentId ");
			params.put("studentId", studentId);
		}
		
		if(otmType != null && otmType > 0){
			hql.append("and product.oneOnManyType = :otmType ");
			params.put("otmType", otmType);
		}
		
		List<ContractProduct> list= this.findAllByHQL(hql.toString(), params);
		return list;
	}

    /**
     * 根据一对多学生考勤记录查询剩余资金
     * @param otmClassStudentAttendent
     * @return
     */
	public BigDecimal countOtmStudentRemainAmount(OtmClassStudentAttendent otmClassStudentAttendent) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		Integer otmType = otmClassStudentAttendent.getOtmClassCourse().getOtmClass().getOtmType();
		hql.append("select sum(case when paidStatus = 'PAID' then (realAmount + promotionAmount - consumeAmount) else (paidAmount - consumeAmount) end) from ContractProduct ")
			.append(" where type='ONE_ON_MANY' and status<>'CLOSE_PRODUCT' and status<>'ENDED' ")
			.append(" and contract.student.id= :studentId  ")
			.append("and product.oneOnManyType = :otmType ");
		params.put("otmType", otmType);
		params.put("studentId", otmClassStudentAttendent.getStudentId());
		Double result = this.findSumHql(hql.toString(), params);
		return 	BigDecimal.valueOf(result);
	}
	
	/**
	 * 分页查询一对一合同产品
	 */
	@Override
	public DataPackage getMyOooContractProductList(DataPackage dataPackage, ContractVo contractVo, TimeVo timeVo, String isAllDistributed) {
		StringBuffer sql=new StringBuffer();
        Map<String, Object> params = Maps.newHashMap();
		sql.append(" select cp.* from contract_product cp, contract c, student s,organization o  ");
		StringBuffer sqlWhere = new StringBuffer(" where cp.TYPE='ONE_ON_ONE_COURSE' ");
		sqlWhere.append(" and cp.CONTRACT_ID = c.ID and c.STUDENT_ID = s.ID and o.id = c.BL_CAMPUS_ID ");
		if (StringUtils.isNotBlank(timeVo.getStartDate())) {
			sqlWhere.append(" and cp.CREATE_TIME >= :startDate ");
            params.put("startDate", timeVo.getStartDate());
		}
		if (StringUtils.isNotBlank(timeVo.getEndDate())) {
			sqlWhere.append(" and cp.CREATE_TIME < :endDate ");
            params.put("endDate", DateTools.addDateToString(timeVo.getEndDate(), 1));
		}
		if (StringUtils.isNotBlank(contractVo.getGradeId())) {
			sqlWhere.append(" and cp.CONTRACT_ID = c.ID ");
			sqlWhere.append(" and s.GRADE_ID = :gradeId ");
			params.put("gradeId", contractVo.getGradeId());
		}
		if (StringUtils.isNotBlank(contractVo.getStuName())) {
			sqlWhere.append(" and s.NAME like :stuName ");
			params.put("stuName", "%"+contractVo.getStuName()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getContractId())) {
			sqlWhere.append(" and cp.CONTRACT_ID like :contractId ");
			params.put("contractId", "%"+contractVo.getContractId());
		}
		if (StringUtils.isNotBlank(contractVo.getSignByWho())) {
			sql.append(" , user u ");
			sqlWhere.append(" and c.SIGN_STAFF_ID = u.USER_ID ");
			sqlWhere.append(" and u.NAME like :signByWho ");
			params.put("signByWho", "%"+contractVo.getSignByWho()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getBlCampusId())) {
			sqlWhere.append(" and c.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", contractVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(isAllDistributed)) {
			if (isAllDistributed.equals("TRUE")) {
				sqlWhere.append(" and (case when cp.`STATUS` != 'CLOSE_PRODUCT' AND cp.`STATUS` != 'REFUNDED' AND cp.`STATUS` != 'ENDED' AND cp.PAID_STATUS = 'PAID' "
							+ " then (ROUND((cp.REAL_AMOUNT + cp.PROMOTION_AMOUNT)/cp.PRICE, 2) = cp.OOO_SUBJECT_DISTRIBUTED_HOURS) "
						+ " when cp.`STATUS` != 'CLOSE_PRODUCT' AND cp.`STATUS` != 'REFUNDED' AND cp.`STATUS` != 'ENDED' AND cp.PAID_STATUS != 'PAID' "
							+ " then (ROUND(cp.PAID_AMOUNT/cp.PRICE, 2) = cp.OOO_SUBJECT_DISTRIBUTED_HOURS) "
						+ " else (ROUND(cp.CONSUME_AMOUNT/ cp.PRICE,2) = OOO_SUBJECT_DISTRIBUTED_HOURS) end)");
						
			} else {
				sqlWhere.append(" and (case when cp.`STATUS` != 'CLOSE_PRODUCT' AND cp.`STATUS` != 'REFUNDED' AND cp.`STATUS` != 'ENDED' AND cp.PAID_STATUS = 'PAID' "
						+ " then (ROUND((cp.REAL_AMOUNT + cp.PROMOTION_AMOUNT)/cp.PRICE, 2) != cp.OOO_SUBJECT_DISTRIBUTED_HOURS) "
						+ " when cp.`STATUS` != 'CLOSE_PRODUCT' AND cp.`STATUS` != 'REFUNDED' AND cp.`STATUS` != 'ENDED' AND cp.PAID_STATUS != 'PAID' "
						+ " then (ROUND(cp.PAID_AMOUNT/cp.PRICE, 2) != cp.OOO_SUBJECT_DISTRIBUTED_HOURS) "
						+ " else (ROUND(cp.CONSUME_AMOUNT/ cp.PRICE,2)  != cp.OOO_SUBJECT_DISTRIBUTED_HOURS) end ");
				sqlWhere.append(" or (cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS <> 'UNPAY' AND cp.OOO_SUBJECT_DISTRIBUTED_HOURS IS NULL)) ");
			}
		}


		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("signUser","c.SIGN_STAFF_ID");
		sqlMap.put("manegerId","s.STUDY_MANEGER_ID");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("课时管理","nsql","sql");
		sqlWhere.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		sqlWhere.append(" order by cp.CREATE_TIME desc ");
		sql.append(sqlWhere.toString());
		return super.findPageBySql(sql.toString(), dataPackage, true, params);
		
	}
	
	/**
     * 按照课程拿有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     */
	@Override
    public List<ContractProduct> getOrderValidSubjectOneOnOneContractProducts(Course course) {
    	Product courseProduct = course.getProduct();
    	StringBuffer sql = new StringBuffer();
        Map<String, Object> params = Maps.newHashMap();
    	sql.append(" select distinct cp.* from CONTRACT_PRODUCT_SUBJECT cps left join contract_product cp on cps.CONTRACT_PRODUCT_ID = cp.id "
    			+ " left join contract c on cp.CONTRACT_ID = c.ID ");
    	StringBuffer sqlWhere = new StringBuffer(" where cp.TYPE='ONE_ON_ONE_COURSE' and c.STUDENT_ID = :studentId "
    			+ " and (cp.STATUS = 'NORMAL' or cp.STATUS = 'STARTED') ");
        params.put("studentId", course.getStudent().getId());
    	DataDict productGroup = courseProduct.getProductGroup();
    	if (productGroup == null) {
			sqlWhere.append(" and cp.PRODUCT_ID = :productId ");
			params.put("productId", course.getProduct().getId());
		} else {
			sql.append(" left join product p on cp.PRODUCT_ID =  p.id " );
			sqlWhere.append(" and p.PRODUCT_GROUP_ID = :productGroupId ");
			params.put("productGroupId", course.getProduct().getProductGroup().getId());
		}
    	sqlWhere.append(" and cps.SUBJECT_ID = :subjectId ");
    	params.put("subjectId", course.getSubject().getId());
    	sqlWhere.append(" and cps.QUANTITY > cps.CONSUME_HOURS ");
    	sql.append(sqlWhere).append(" order by cp.CREATE_TIME asc ");
    	return super.findBySql(sql.toString(), params);
    }
    
    /**
     * 计算按照课程有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     */
	@Override
    public BigDecimal countOrderValidSubjectOneOnOneContractProducts(Course course) {
		Product courseProduct = course.getProduct();
    	StringBuffer sql = new StringBuffer();
        Map<String, Object> params = Maps.newHashMap();
    	sql.append(" select sum(cps.QUANTITY - cps.CONSUME_HOURS) REMAINHOURS from CONTRACT_PRODUCT_SUBJECT cps left join contract_product cp on cps.CONTRACT_PRODUCT_ID = cp.ID "
    			+ " left join contract c on cp.CONTRACT_ID = c.ID ");
    	StringBuffer sqlWhere = new StringBuffer(" where cp.TYPE='ONE_ON_ONE_COURSE' and c.STUDENT_ID = :studentId "
    			+ " and (cp.STATUS = 'NORMAL' or cp.STATUS = 'STARTED') ");
        params.put("studentId", course.getStudent().getId());
    	DataDict productGroup = courseProduct.getProductGroup();
    	if (productGroup == null) {
			sqlWhere.append(" and cp.PRODUCT_ID = :productId ");
            params.put("productId", course.getProduct().getId());
		} else {
			sql.append(" left join product p on cp.PRODUCT_ID =  p.id " );
			sqlWhere.append(" and p.PRODUCT_GROUP_ID = :productGroupId ");
			params.put("productGroupId", course.getProduct().getProductGroup().getId());
		}
    	sqlWhere.append(" and cps.SUBJECT_ID = :subjectId ");
    	params.put("subjectId", course.getSubject().getId());
    	sqlWhere.append(" and  cps.QUANTITY > cps.CONSUME_HOURS ");
    	sql.append(sqlWhere);
    	List<Map<Object, Object>> returnMap = super.findMapBySql(sql.toString(), params);
    	if (returnMap != null && returnMap.size() > 0) {
    		if (returnMap.get(0).get("REMAINHOURS") != null ) {
    			return (BigDecimal) returnMap.get(0).get("REMAINHOURS");
    		}
    	}
		return BigDecimal.ZERO;
    }
	
	/**
	 * 获取当前用户的课时管理操作权限
	 */
	@Override
	public List<Map<Object, Object>> getCourseTimeManageAuthTags(String userId) {
        Map<String, Object> params = Maps.newHashMap();
		String sql = "select RTAG from resource where RTYPE='BUTTON' and "
				+ " RTAG in ('COURSE_TIME_ASSIGN', 'COURSE_TIME_DISTRIBUTE_LIST', 'COURSE_HOURS_DISTRIBUTE', 'COURSE_HOURS_EXTRACT', 'COURSE_HOURS_TRANSFER')"
				+ " and id in (select resourceID from role_resource where roleID in "
				+ "(select roleID from user_role where userID = :userId )) ";
        params.put("userId", userId);
		return super.findMapBySql(sql, params);
	}

	@Override
	public List<Map<Object, Object>> getCourseTimeManageAuthTagsNew(String userId) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = "select RTAG from resource where RTYPE='BUTTON' and "
				+ " RTAG in ('COURSE_TIME_ASSIGN', 'COURSE_TIME_DISTRIBUTE_LIST', 'COURSE_HOURS_DISTRIBUTE', 'COURSE_HOURS_EXTRACT', 'COURSE_HOURS_TRANSFER')"
				+ " and id in (select resourceID from role_resource where roleID in "
				+ "(select role_ID from user_organization_role where user_ID = :userId )) ";
		params.put("userId", userId);
		return super.findMapBySql(sql, params);
	}
	
	/**
	 * 通过合同ID查找合同产品列表
	 */
	@Override
	public List<ContractProduct> getContractProductByContractId(String contractId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from ContractProduct where contract.id = :contractId ";
		params.put("contractId", contractId);
		return super.findAllByHQL(hql, params);
	}
	
	/**
	 * 通过合同studentId, 产品类型，最低单价查找合同产品列表
	 */
	@Override
	public List<ContractProduct> getContractProductByStudent(String studentId, ProductType prodctType, BigDecimal lowestPrice) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from ContractProduct where type = :prodctType and price >= :lowestPrice and contract.student.id = :studentId ";
		params.put("prodctType", prodctType);
		params.put("lowestPrice", lowestPrice);
		params.put("studentId", studentId);
		return super.findAllByHQL(hql, params);
	}

	/**
	 * 根据关联系统的订单编号查找合同产品列表
	 */
    @Override
    public List<ContractProduct> listContractProductByAssocRelatedNo(String relatedNo) {
        Map<String, Object> params = Maps.newHashMap();
        String sql = " select * from contract_product where 1=1 "
                + " and CONTRACT_ID in (select c.ID from contract c inner join business_assoc_mapping bam on c.ID = bam.business_id and business_type = 'CONTRACT'"
                + " WHERE bam.relate_no = :relatedNo ) ";
        params.put("relatedNo", relatedNo);
        return super.findBySql(sql, params);
    }
	
}
