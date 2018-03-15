package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.*;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.utils.PropertiesUtils;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domainVo.AccountChargeRecordsVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

@Repository
public class AccountChargeRecordsDaoImpl extends GenericDaoImpl<AccountChargeRecords, String> implements
	AccountChargeRecordsDao {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleQLConfigService roleQLConfigService;
    
    @Autowired
    private JdbcTemplateDao jdbcTemplateDao;

	@Override
	public DataPackage listAccountChargeRecord(DataPackage dp,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo) {

		Map<String, Object> params = Maps.newHashMap();

		String hql="from AccountChargeRecords  c";
		hql+=" left join c.teacher tch ";
//		hql+=" left join c.course  cou ";
//		hql+=" left join c.miniClassCourse mcc ";
//		hql+=" left join c.otmClassCourse occ ";
		hql+= " left join c.contract cont ";
//		hql += " inner join c.blCampusId organization";
		String hqlWhere=" and c.chargeType not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN', 'FEEDBACK_REFUND')";
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getContractId())){
			hqlWhere+=" and c.contract.id = :contractId ";
			params.put("contractId", accountChargeRecordsVo.getContractId());
		}
		
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getContractIdAccount())){//学生电子账户查询专用
			hqlWhere+=" and c.contract.id =  :contractIdAccount ";
			params.put("contractIdAccount", accountChargeRecordsVo.getContractIdAccount());
		}

		if (StringUtils.isNotEmpty(accountChargeRecordsVo.getCampusId())){
			hqlWhere+=" and c.blCampusId.id = :campusId ";
			params.put("campusId", accountChargeRecordsVo.getCampusId());
		}

//		else{
			if(StringUtils.isNotEmpty(timeVo.getStartDate())){
				hqlWhere+=" and c.payTime >= :startDate ";
				params.put("startDate", timeVo.getStartDate()+" 00:00:00");
			}
			
			if(StringUtils.isNotEmpty(timeVo.getEndDate())){
				hqlWhere+=" and c.payTime <= :endDate ";
				params.put("endDate", timeVo.getEndDate()+" 23:59:59");
			}
			
			
			
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getProductTypeName())){
				hqlWhere+=" and c.productType =:productType  ";
				params.put("productType", ProductType.valueOf(accountChargeRecordsVo.getProductTypeName()));
			}
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getStudentName())){
				hqlWhere+=" and c.student.name like :studentName ";
				params.put("studentName", accountChargeRecordsVo.getStudentName()+"%");
			}
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getStudentId())){
				hqlWhere+=" and c.student.id like :studentId ";
				params.put("studentId", accountChargeRecordsVo.getStudentId()+"% ");
			}
			
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getTeacherName())){
				hqlWhere+=" and tch.name like :teacherName ";
				params.put("teacherName", accountChargeRecordsVo.getTeacherName()+"%");
//				hqlWhere+=" and (teacher.name like '"+accountChargeRecordsVo.getTeacherName()+"%'";
//				hqlWhere+=" or occTeach.name like '" + accountChargeRecordsVo.getTeacherName() + "%'";
//				hqlWhere+=" or mccTeach.name like '"+accountChargeRecordsVo.getTeacherName()+"%' )";
			}
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getTeacherId())){
				hqlWhere+=" and tch.userId = :teacherId ";
				params.put("teacherId", accountChargeRecordsVo.getTeacherId());
//				hqlWhere+=" and (couTeach.userId = '"+accountChargeRecordsVo.getTeacherId()+"'";
//				hqlWhere+=" or occTeach.userId = '"+accountChargeRecordsVo.getTeacherId()+"'";
//				hqlWhere+=" or mccTeach.userId = '"+accountChargeRecordsVo.getTeacherId()+"' )";
			}
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getChargeTypeName())){
				if (!"WASH".equals(accountChargeRecordsVo.getChargeTypeName())) {
					hqlWhere+=" and c.chargeType = :chargeType ";
					params.put("chargeType", ChargeType.valueOf(accountChargeRecordsVo.getChargeTypeName()));
					hqlWhere+=" and c.chargePayType = 'CHARGE' ";
				} else {
					hqlWhere+=" and c.chargePayType = 'WASH' ";
				}
			}
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getPayTypeName())){
				hqlWhere+=" and c.payType = :payType ";
				params.put("payType", PayType.valueOf(accountChargeRecordsVo.getPayTypeName()));
			}
	
//	        if(userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER)){
//	            hqlWhere+= " and c.student.studyManegerId = '" + userService.getCurrentLoginUser().getUserId() + "'";
//	        }
//		}
		

//		hqlWhere+=RoleCodeAuthoritySearchUtil.findPageChargeRecordRoleCodeAuthority(userService.getCurrentLoginUser(), userService.getBelongCampus());
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","c.blCampusId.orgLevel");
		sqlMap.put("chargeUser","c.operateUser.userId");
		sqlMap.put("stuId","c.student.id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("扣费记录","nsql","hql");
		hqlWhere+=roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap);

		
        if(!hqlWhere.equals("")){
            hqlWhere=hqlWhere.replaceFirst("and", "where"); //将第一个and替换为where
        }
        
        hql=hql+hqlWhere;


		
		
		if (StringUtils.isNotBlank(dp.getSord())&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by c."+dp.getSidx()+" "+dp.getSord();
		}


		dp=this.findPageByHQL(hql, dp, true, params);
		return dp;
		
	}

	@Override
	public List getExcelChargeList(DataPackage dp,
											   AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo) {

		Map<String, Object> params = Maps.newHashMap();
		StringBuilder sql = new StringBuilder();

		sql.append(" select ");
		sql.append(" acr.id transactionId,acr.CONTRACT_ID contractId,o.`name` blCampusName,acr.CHARGE_pay_TYPE chargeTypeName,acr.PAY_TYPE payTypeName");
		sql.append(" ,acr.QUANTITY payHour,acr.AMOUNT amount,acr.PAY_TIME payTime,acr.TRANSACTION_TIME transactionTime,");
		sql.append(" u.name teacherName,s.name studentName,oper.name operateUserName,acr.PRODUCT_TYPE productTypeName,p.`NAME` productName,acr.COURSE_MINUTES courseMinutes,");
		sql.append(" case when acr.MINI_CLASS_COURSE_ID is not null then (select mini_class_name from mini_class_course where MINI_CLASS_COURSE_ID=acr.MINI_CLASS_COURSE_ID) ");
		sql.append(" when acr.TWO_TEACHER_STUDENT_ATTENDENT is not null then (select two.name from two_teacher_class_student_attendent ttcsa");
		sql.append(" left join two_teacher_class_two two on two.CLASS_TWO_ID = ttcsa.CLASS_TWO_ID");
		sql.append(" where ttcsa.ID=acr.TWO_TEACHER_STUDENT_ATTENDENT)");
		sql.append(" END miniClassName,");
		sql.append(" case when acr.COURSE_ID is not null then (select concat(c.course_date,' ',c.COURSE_TIME) from course c where c.course_id=acr.COURSE_ID)");
		sql.append(" when acr.MINI_CLASS_COURSE_ID is not null then (select CONCAT(course_date,' ',COURSE_TIME,'-',COURSE_END_TIME) from mini_class_course where MINI_CLASS_COURSE_ID=acr.MINI_CLASS_COURSE_ID)");
		sql.append(" when acr.OTM_CLASS_COURSE_ID is not null then (select CONCAT(course_date,' ',COURSE_TIME,'-',COURSE_END_TIME) from otm_class_course where OTM_CLASS_COURSE_ID=acr.OTM_CLASS_COURSE_ID)");
		sql.append(" when acr.LECTURE_CLASS_STUDENT_ID is not null then acr.TRANSACTION_TIME ");
		sql.append(" when acr.TWO_TEACHER_STUDENT_ATTENDENT is not null then (select ttcsa.COURSE_DATE_TIME from two_teacher_class_student_attendent ttcsa");
		sql.append(" where ttcsa.ID=acr.TWO_TEACHER_STUDENT_ATTENDENT)");
		sql.append(" END courseTime");
		sql.append("  from account_charge_records acr ");
		sql.append(" left join organization o on acr.BL_CAMPUS_ID= o.id");
		sql.append(" left join user u on acr.teacher_id = u.user_id");
		sql.append(" left join student s on s.id = acr.student_id");
		sql.append(" left join user oper on oper.USER_ID = acr.OPERATE_USER_ID");
		sql.append(" left join product p on acr.product_id = p.id");
		sql.append(" left join contract con on con.id  = acr.CONTRACT_ID");

		sql.append(" where acr.CHARGE_TYPE not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN', 'FEEDBACK_REFUND')");

		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getContractId())){
			sql.append(" and acr.contract_id = :contractId ");
			params.put("contractId", accountChargeRecordsVo.getContractId());
		}

		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getContractIdAccount())){//学生电子账户查询专用
			sql.append(" and acr.contract_id =  :contractIdAccount ");
			params.put("contractIdAccount", accountChargeRecordsVo.getContractIdAccount());
		}

		if (StringUtils.isNotEmpty(accountChargeRecordsVo.getCampusId())){
			sql.append(" and acr.bl_campus_id = :campusId ");
			params.put("campusId", accountChargeRecordsVo.getCampusId());
		}

//		else{
		if(StringUtils.isNotEmpty(timeVo.getStartDate())){
			sql.append(" and acr.pay_time >= :startDate ");
			params.put("startDate", timeVo.getStartDate()+" 00:00:00");
		}

		if(StringUtils.isNotEmpty(timeVo.getEndDate())){
			sql.append(" and acr.pay_Time <= :endDate ");
			params.put("endDate", timeVo.getEndDate()+" 23:59:59");
		}



		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getProductTypeName())){
			sql.append(" and acr.product_type =:productType  ");
			params.put("productType", ProductType.valueOf(accountChargeRecordsVo.getProductTypeName()));
		}
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getStudentName())){
			sql.append(" and s.name like :studentName ");
			params.put("studentName", accountChargeRecordsVo.getStudentName()+"%");
		}
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getStudentId())){
			sql.append(" and s.id like :studentId ");
			params.put("studentId", accountChargeRecordsVo.getStudentId()+"% ");
		}

		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getTeacherName())){
			sql.append(" and u.name like :teacherName ");
			params.put("teacherName", accountChargeRecordsVo.getTeacherName()+"%");
		}
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getTeacherId())){
			sql.append(" and u.user_id = :teacherId ");
			params.put("teacherId", accountChargeRecordsVo.getTeacherId());
		}
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getChargeTypeName())){
			if (!"WASH".equals(accountChargeRecordsVo.getChargeTypeName())) {
				sql.append(" and acr.charge_type = :chargeType ");
				params.put("chargeType", ChargeType.valueOf(accountChargeRecordsVo.getChargeTypeName()));
				sql.append(" and acr.charge_pay_type = 'CHARGE' ");
			} else {
				sql.append(" and acr.charge_pay_type = 'WASH' ");
			}
		}
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getPayTypeName())){
			sql.append(" and acr.pay_type = :payType ");
			params.put("payType", PayType.valueOf(accountChargeRecordsVo.getPayTypeName()));
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("chargeUser","acr.user_Id");
		sqlMap.put("stuId","acr.student_id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("扣费记录","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		return this.findMapBySql(sql.toString(),params);
	}

	@Override
	public void save(AccountChargeRecords accountChargeRecords) {
		if (com.eduboss.utils.StringUtil.isBlank(accountChargeRecords.getIsWashed())) {
			accountChargeRecords.setIsWashed("FALSE");
		}
		accountChargeRecords.setCourseMinutes(BigDecimal.ZERO);
		if ((accountChargeRecords.getCourse() != null && StringUtils.isNotBlank(accountChargeRecords.getCourse().getCourseId()))
				|| (accountChargeRecords.getMiniClassCourse() != null && StringUtils.isNotBlank(accountChargeRecords.getMiniClassCourse().getMiniClassCourseId()))
				|| (accountChargeRecords.getOtmClassCourse() != null && StringUtils.isNotBlank(accountChargeRecords.getOtmClassCourse().getOtmClassCourseId()))
				|| (accountChargeRecords.getPromiseClassRecord() != null && accountChargeRecords.getPromiseClassRecord().getPromiseClass()!=null)
				|| (accountChargeRecords.getLectureClassStudent() != null && accountChargeRecords.getLectureClassStudent().getLectureClass()!=null)
				|| (accountChargeRecords.getTwoTeacherClassStudentAttendent() != null)
				) {
//			if (accountChargeRecords.getProduct().getClassTimeLength() != null) {
//				accountChargeRecords.setCourseMinutes(BigDecimal.valueOf(accountChargeRecords.getProduct().getClassTimeLength()));
//			}

			//读课程的课时时长
			ProductType productType = accountChargeRecords.getProductType();
			BigDecimal courseMinutes = BigDecimal.ZERO;

			if (ProductType.ONE_ON_ONE_COURSE.equals(productType)){
				courseMinutes = accountChargeRecords.getCourse().getCourseMinutes() != null ? accountChargeRecords.getCourse().getCourseMinutes() : courseMinutes;
			}else if (ProductType.SMALL_CLASS.equals(productType)){
				courseMinutes = accountChargeRecords.getMiniClassCourse().getCourseMinutes() != null ? accountChargeRecords.getMiniClassCourse().getCourseMinutes() : courseMinutes;
			}else if (ProductType.ONE_ON_MANY.equals(productType)){
				courseMinutes = accountChargeRecords.getOtmClassCourse().getCourseMinutes() != null ? accountChargeRecords.getOtmClassCourse().getCourseMinutes() : courseMinutes;
			}else if (ProductType.LECTURE.equals(productType)){
				int i=accountChargeRecords.getLectureClassStudent().getLectureClass().getLectureTimeLong();
				if(i>0){
					courseMinutes = new BigDecimal(i);
				}
				
			}else if (ProductType.TWO_TEACHER.equals(productType)){
				BigDecimal a = accountChargeRecords.getTwoTeacherClassStudentAttendent().getTwoTeacherClassCourse().getCourseMinutes();
				if (a.compareTo(BigDecimal.ZERO)>0){
					courseMinutes = a;
				}
			}else {
				courseMinutes = accountChargeRecords.getProduct().getClassTimeLength() != null ? BigDecimal.valueOf(accountChargeRecords.getProduct().getClassTimeLength()) : courseMinutes;
			}
			accountChargeRecords.setCourseMinutes(courseMinutes);

		} 
		if (com.eduboss.utils.StringUtil.isBlank(accountChargeRecords.getTransactionTime())) {
			accountChargeRecords.setTransactionTime(DateTools.getCurrentDateTime());
		}
		super.save(accountChargeRecords);
	}
	
	/**
	 *  #355小班学生详情课程扣费金额错误 处理：可能有多条扣费记录的 优惠加实收
	 * 取得某个学生小班课程的扣费记录
	 * @param miniClassCourseId
	 * @param studentId
	 * @return
	 */
	public List<AccountChargeRecords> getMiniClassCourseRecordByStudentId(String miniClassCourseId, String studentId) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criterias = session.createCriteria(AccountChargeRecords.class)
			    .add( Restrictions.eq("miniClassCourse.miniClassCourseId", miniClassCourseId))
			    .add( Restrictions.eq("student.id", studentId))
			    .add(Restrictions.eq("chargePayType", ChargePayType.CHARGE))
			    .add(Restrictions.eq("isWashed", "FALSE"))
		 		.setFirstResult(0);
		List list = criterias.list();
		if (list.size() > 0) {
			return  list;
		} else {
			return null;
		}
	}
	
	/**
	 * 取得某个学生一对多课程的扣费记录
	 * @param otmClassCourseId
	 * @param studentId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getOtmClassCourseRecordByStudentId(
			String otmClassCourseId, String studentId) {
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criterias = session.createCriteria(AccountChargeRecords.class)
			    .add( Restrictions.eq("otmClassCourse.otmClassCourseId", otmClassCourseId))
			    .add( Restrictions.eq("student.id", studentId))
		 		.setFirstResult(0);
		List list = criterias.list();
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	@Override
	public boolean hasChargeRecord(ContractProduct contractProduct) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("select count(*) from AccountChargeRecords c")
			.append(" where  contractProduct.id = :contractProductId ");
		params.put("contractProductId", contractProduct.getId());
		int count = this.findCountHql(hql.toString(), params);
		return count>0?true: false;
		
	}
	
	/**
	 * 判断某合同是不是已经扣费啦
	 */
	@Override
	public boolean hasChargeRecord(Contract contract) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("select count(*) from AccountChargeRecords c")
				.append(" where  contract.id = :contractId");
		params.put("contractId", contract.getId());
		int count = this.findCountHql(hql.toString(), params);
		return count>0?true: false;
	}

	@Override
	public BigDecimal getChargeAmount(ContractProduct conPrd) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("select sum(amount) from AccountChargeRecords c")
			.append(" where chargePayType = 'CHARGE' and isWashed = 'false' and contractProduct.id =:contractProductId  ")
			.append(" and chargeType <> 'PROMOTION_RETURN' ");
		params.put("contractProductId", conPrd.getId());
		int chargeAmount = this.findCountHql(hql.toString(), params);
		return chargeAmount==0?BigDecimal.ZERO: new BigDecimal(chargeAmount); 
	}
	
	@Override
	public BigDecimal getRealChargeAmount(String id,PayType payType) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("select sum(amount) from AccountChargeRecords c")
			.append(" where chargePayType = 'CHARGE' and isWashed = 'false' and contractProduct.id = :contractProductId ")
			.append(" and chargeType <> 'PROMOTION_RETURN' and payType= :payType  ");
		params.put("contractProductId", id);
		params.put("payType", payType);
		int chargeAmount = this.findCountHql(hql.toString(), params);
		return chargeAmount==0?BigDecimal.ZERO: new BigDecimal(chargeAmount); 
	}
	
	/**
	 * 某个小班课程的总扣费金额
	 */
	@Override
	public BigDecimal getMiniClassConsumeFinance(String miniClassCourseId) {
		if (StringUtil.isBlank(miniClassCourseId)) {
			throw new ApplicationException("查询小班课程总扣费函数的入参\"课程ID\"不能为空！");
		}
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql=new StringBuffer(); 
		sql.append(" SELECT ");
		sql.append(" 	SUM(AMOUNT) TOTAL_CONSUME_AMOUNT  ");
		sql.append(" FROM ACCOUNT_CHARGE_RECORDS a ");
//		sql.append(" INNER JOIN `contract_product` b ON a.CONTRACT_PRODUCT_ID =  b.ID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" 	AND a.CHARGE_PAY_TYPE = 'CHARGE' AND a.IS_WASHED = 'FALSE' ");
		sql.append(" 	AND MINI_CLASS_COURSE_ID = :miniClassCourseId ");
		sql.append(" GROUP BY MINI_CLASS_COURSE_ID ");
		params.put("miniClassCourseId", miniClassCourseId);
		List<Map<Object, Object>> list= super.findMapBySql(sql.toString(), params);
		if (list !=null && list.size() > 0) {
			Map map = list.get(0);
			BigDecimal totalConsumeAmount = (BigDecimal) map.get("TOTAL_CONSUME_AMOUNT");
			if (totalConsumeAmount != null) {
				return totalConsumeAmount;
			} else {
				return BigDecimal.ZERO;
			}
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * 某个一对多课程的总扣费金额
	 * @param otmClassCourseId
	 * @return
	 */
	@Override
	public BigDecimal getOtmClassConsumeFinance(String otmClassCourseId) {
		if (StringUtil.isBlank(otmClassCourseId)) {
			throw new ApplicationException("查询小班课程总扣费函数的入参\"课程ID\"不能为空！");
		}
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql=new StringBuffer(); 
		sql.append(" SELECT ");
		sql.append(" 	SUM(AMOUNT) TOTAL_CONSUME_AMOUNT  ");
		sql.append(" FROM ACCOUNT_CHARGE_RECORDS a ");
//		sql.append(" INNER JOIN `contract_product` b ON a.CONTRACT_PRODUCT_ID =  b.ID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND a.CHARGE_PAY_TYPE = 'CHARGE' AND a.IS_WASHED = 'FALSE' ");
		sql.append(" 	AND OTM_CLASS_COURSE_ID = :otmClassCourseId ");
		sql.append(" GROUP BY OTM_CLASS_COURSE_ID ");
		params.put("otmClassCourseId", otmClassCourseId);
		List<Map<Object, Object>> list= super.findMapBySql(sql.toString(), params);
		if (list !=null && list.size() > 0) {
			Map map = list.get(0);
			BigDecimal totalConsumeAmount = (BigDecimal) map.get("TOTAL_CONSUME_AMOUNT");
			if (totalConsumeAmount != null) {
				return totalConsumeAmount;
			} else {
				return BigDecimal.ZERO;
			}
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * 获取小班课程扣费人数
	 * @param miniClassCourseId
	 * @return
	 */
	@Override
	public int getMiniClassCourseChargeStudentNum(String miniClassCourseId) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select IFNULL(case when TRANSACTION_ID is null then count(1) else count(DISTINCT TRANSACTION_ID) end, 0) FROM ACCOUNT_CHARGE_RECORDS a")
			.append(" where a.CHARGE_PAY_TYPE = 'CHARGE' AND a.IS_WASHED = 'FALSE' AND  a.MINI_CLASS_COURSE_ID = :miniClassCourseId ");
//			.append(" group by student.id ");
		params.put("miniClassCourseId", miniClassCourseId);
		int count = this.findCountSql(sql.toString(), params);
		return count;
	}
	
	/**
	 * 获取一对多课程扣费人数
	 * @param otmClassCourseId
	 * @return
	 */
	public int getOtmClassCourseChargeStudentNum(String otmClassCourseId) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select IFNULL(case when TRANSACTION_ID is null then count(1) else count(DISTINCT TRANSACTION_ID) end, 0) FROM ACCOUNT_CHARGE_RECORDS a")
			.append(" where a.CHARGE_PAY_TYPE = 'CHARGE' AND a.IS_WASHED = 'FALSE' AND a.OTM_CLASS_COURSE_ID = :otmClassCourseId ");
		params.put("otmClassCourseId", otmClassCourseId);
		int count = this.findCountSql(sql.toString(), params);
		return count;
	}

	@Override
	public List<AccountChargeRecords> findChargeRecordsForTeacher(
			String productType, String teacherId) {
		
		StringBuffer hql =  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		if(productType.equals(ProductType.ONE_ON_ONE_COURSE.toString())) {
			hql.append("select charge from AccountChargeRecords charge inner join charge.course as course")
			.append(" where charge.chargeType not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN')");
			if(StringUtils.isNotEmpty(productType)) {
				hql.append(" and charge.productType = :productType ");
				params.put("productType", ProductType.valueOf(productType));
			}
			if(StringUtils.isNotEmpty(teacherId)) {
				hql.append(" and course.teacher.userId = :teacherId ");
				params.put("teacherId", teacherId);
			}
			hql.append(" order by course.courseDate desc");
		} else if(productType.equals(ProductType.SMALL_CLASS.toString())) {
			hql.append("select charge from AccountChargeRecords charge inner join charge.miniClassCourse as course")
			.append(" where charge.chargeType not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN')");
			if(StringUtils.isNotEmpty(productType)) {
				hql.append(" and charge.productType = :productType  ");
				params.put("productType", ProductType.valueOf(productType));
			}
			if(StringUtils.isNotEmpty(teacherId)) {
				hql.append(" and course.teacher.userId = :teacherId ");
				params.put("teacherId", teacherId);
			}
			hql.append(" order by course.courseDate desc");
		}
		List<AccountChargeRecords> records =this.findAllByHQL(hql.toString(), params);
		return records;
	}

	@Override
	public List<AccountChargeRecords> findChargeRecordsForTeacher(
			String productType, String teacherId,String searchDate) {
		
		StringBuffer hql =  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		if(productType.equals(ProductType.ONE_ON_ONE_COURSE.toString())) {
			hql.append("select charge from AccountChargeRecords charge inner join charge.course as course")
			.append(" where charge.chargeType not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN')");
			if(StringUtils.isNotEmpty(productType)) {
				hql.append(" and charge.productType = :productType ");
				params.put("productType", ProductType.valueOf(productType));
			}
			if(StringUtils.isNotEmpty(teacherId)) {
				hql.append(" and course.teacher.userId = :teacherId ");
				params.put("teacherId", teacherId);
			}
			
			if(StringUtils.isNotEmpty(searchDate)) {
				hql.append(" and course.courseDate  like :searchDate ");
				params.put("searchDate", searchDate+"% ");
			}
			
			hql.append(" order by course.courseDate desc");
		} else if(productType.equals(ProductType.SMALL_CLASS.toString())) {
			hql.append("select charge from AccountChargeRecords charge inner join charge.miniClassCourse as course")
			.append(" where charge.chargeType not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN')");
			if(StringUtils.isNotEmpty(productType)) {
				hql.append(" and charge.productType = :productType  ");
				params.put("productType", ProductType.valueOf(productType));
			}
			if(StringUtils.isNotEmpty(teacherId)) {
				hql.append(" and course.teacher.userId = :teacherId ");
				params.put("teacherId", teacherId);
			}
			hql.append(" order by course.courseDate desc");
		}
		List<AccountChargeRecords> records =this.findAllByHQL(hql.toString(), params);
		return records;
	}
	
	/**
	 * 获取小班课程已扣费的人材
	 * @param miniClassCourseId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getMiniClassCourseChargeStudentList(
			String miniClassCourseId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' AND miniClassCourse.miniClassCourseId = :miniClassCourseId ");
		params.put("miniClassCourseId", miniClassCourseId);
		return this.findAllByHQL(hql.toString(), params);
	}

	/**
	 * 仅获取一对多课程已扣费的扣费记录
	 * @param otmClassCourseId
	 * @return
	 */
	public List<AccountChargeRecords> getOtmClassCourseChargeStudentList(String otmClassCourseId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer hql=new StringBuffer();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' AND otmClassCourse.otmClassCourseId = :otmClassCourseId ");
		params.put("otmClassCourseId", otmClassCourseId);
		return this.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 根据transactionId获取扣费记录
	 * @param transactionId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getAccountChargeRecordsByTransactionId(String transactionId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where 1=1 and chargePayType = 'CHARGE' and  transactionId = :transactionId ");
		params.put("transactionId", transactionId);
		return this.findAllByHQL(hql.toString(), params);
	}

	/**
	 * 根据直播课程id和合同id找到扣费记录
	 *
	 *
	 * @param courseId
	 * @param liveId
	 * @param contractId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getAccountChargeRecordsByCurriculumId(String courseId, String liveId, String contractId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where 1=1 and chargePayType = 'CHARGE' and isWashed='FALSE' and  curriculum.courseId =:courseId and  curriculum.liveId =:liveId and contract.id=:contractId ");
		params.put("courseId", courseId);
		params.put("liveId", liveId);
		params.put("contractId", contractId);
		return this.findAllByHQL(hql.toString(), params);

	}

	/**
	 * 根据courseId获取扣费记录（排除有冲销记录的）
	 * @param courseId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getAccountChargeRecordsByCourseId(String courseId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' AND course.courseId = :courseId ");
		params.put("courseId", courseId);
		return this.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 根据courseId获取计算扣费记录条数
	 */
	public int countAccountChargeRecordsByCourseId(String courseId, ProductType type) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select count(*) from AccountChargeRecords where 1=1 ");
		if (type == ProductType.ONE_ON_ONE_COURSE) {
		    hql.append( "and course.courseId = :courseId  ");
		} else if (type == ProductType.SMALL_CLASS) {
		    hql.append(" and miniClassCourse.miniClassCourseId = :courseId  ");
		} else if (type == ProductType.ONE_ON_MANY) {
		    hql.append(" and otmClassCourse.otmClassCourseId = :courseId ");
		} else if (type == ProductType.TWO_TEACHER) {
		    hql.append(" and twoTeacherClassStudentAttendent.twoTeacherClassCourse.courseId = :courseId ");
		}
		if (type == ProductType.TWO_TEACHER) {
            params.put("courseId", Integer.parseInt(courseId));
        } else {
            params.put("courseId", courseId);
        }
		
		return this.findCountHql(hql.toString(), params);
	}
	
	/**
	 * 根据讲座学生ID计算扣费记录条数
	 */
	public int countAccountChargeRecordsByLecStuId(String lectureClassStudentId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select count(*) from AccountChargeRecords where 1=1 AND lectureClassStudent.id = :lectureClassStudentId ");
		params.put("lectureClassStudentId", lectureClassStudentId);
		return this.findCountHql(hql.toString(), params);
	}
	
	/**
	 * 根据miniClassCourseId获取扣费记录（排除有冲销记录的）
	 * @param miniClassCourseId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getAccountChargeRecordsByMiniClassCourseId(String miniClassCourseId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer hql=new StringBuffer();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' AND miniClassCourse.miniClassCourseId = :miniClassCourseId ");
		params.put("miniClassCourseId", miniClassCourseId);
		return this.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 根据miniClassCourseId计算扣费记录条数
	 */
	public int countAccountChargeRecordsByMiniClassCourseId(String miniClassCourseId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select count(*) from AccountChargeRecords where 1=1 AND miniClassCourse.miniClassCourseId = :miniClassCourseId ");
		params.put("miniClassCourseId", miniClassCourseId);
		return this.findCountHql(hql.toString(),params);
	}
	
	/**
	 * 根据otmClassCourseId获取扣费记录（排除有冲销记录的）
	 * @param otmClassCourseId
	 * @return
	 */
	public List<AccountChargeRecords> getAccountChargeRecordsByOtmClassCourseId(String otmClassCourseId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' AND otmClassCourse.otmClassCourseId = :otmClassCourseId ");
		params.put("otmClassCourseId", otmClassCourseId);
		return this.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 根据otmClassCourseId计算扣费记录条数
	 */
	public int countAccountChargeRecordsByOtmClassCourseId(String otmClassCourseId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select count(*) from AccountChargeRecords where 1=1 AND otmClassCourse.otmClassCourseId = :otmClassCourseId ");
		params.put("otmClassCourseId", otmClassCourseId);
		return this.findCountHql(hql.toString(), params);
	}
	
	@Override
	public DataPackage findAccountChargeRecordToExcel(DataPackage dp,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo) {
		Map<String, Object> params = Maps.newHashMap();
		String hql="from AccountChargeRecords  c";
		hql+=" left join c.course  cou ";
		hql+=" left join cou.teacher couTeach ";
		hql+=" left join c.miniClassCourse mcc ";
		hql+=" left join mcc.teacher mccTeach ";
		String hqlWhere=" and c.chargeType not in('TRANSFER_NORMAL_TO_ELECT_ACC','TRANSFER_PROMOTION_TO_ELECT_ACC','PROMOTION_RETURN')";
		if(StringUtils.isNotEmpty(accountChargeRecordsVo.getContractId())){
			hqlWhere+=" and c.contract.id =  :contractId";
			params.put("contractId", accountChargeRecordsVo.getContractId());
		}else{
			if(StringUtils.isNotEmpty(timeVo.getStartDate())){
				hqlWhere+=" and c.payTime  >= :payStartTime";
				params.put("payStartTime", timeVo.getStartDate()+" 00:00:00");
			}
			
			if(StringUtils.isNotEmpty(timeVo.getEndDate())){
				hqlWhere+=" and c.payTime <=  :payEndTime";
				params.put("payEndTime", timeVo.getEndDate()+" 23:59:59");
			}
			
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getStudentName())){
				hqlWhere+=" and c.student.name like :studentName ";
				params.put("studentName", accountChargeRecordsVo.getStudentName()+"%");
			}
			
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getTeacherName())){
				hqlWhere+=" and (couTeach.name like :teacherName ";
				hqlWhere+=" or mccTeach.name like :teacherName )";
				params.put("teacherName", accountChargeRecordsVo.getTeacherName()+"%");
			}
			
			if(StringUtils.isNotEmpty(accountChargeRecordsVo.getCampusId())){
				hqlWhere+=" and c.blCampusId = :blCampusId ";
				params.put("blCampusId", accountChargeRecordsVo.getCampusId());
			}
	
		}
		
		/**
		 * 扣费记录的权限控制
		 */
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","c.blCampusId.orgLevel");
		sqlMap.put("chargeUser","c.operateUser.userId");
		sqlMap.put("stuId","c.student.id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("扣费记录","nsql","hql");
		hqlWhere+=roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap);

		
        if(!hqlWhere.equals("")){
            hqlWhere=hqlWhere.replaceFirst("and", "where"); //将第一个and替换为where
        }
        
        hql=hql+hqlWhere;

		
		if (StringUtils.isNotBlank(dp.getSord())&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by c."+dp.getSidx()+" "+dp.getSord();
		}

		dp = this.findPageByHQL(hql,dp, false, params);
		return dp;
		
	}

	/**
	 * 根据miniClassId和studentId获取扣费记录
	 * @param miniClassId
	 * @param studentId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getAccountChargeRecordsByMiniClassAndStudent(String miniClassId, String studentId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' and miniClass.miniClassId = :miniClassId ");
		hql.append(" and student.id = :studentId ");
		params.put("miniClassId", miniClassId);
		params.put("studentId", studentId);
		return this.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 根据一对多ID和学生ID获取扣费记录
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	@Override
	public List<AccountChargeRecords> getAccountChargeRecordsByOtmClassAndStudent(String otmClassId, String studentId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' and otmClass.otmClassId = :otmClassId ");
		hql.append(" and student.id = :studentId ");
		params.put("otmClassId", otmClassId);
		params.put("studentId", studentId);
		return this.findAllByHQL(hql.toString(), params);
	}
	
	@Override
	public List<AccountChargeRecords> getAccountChargeRecordsByOtmClass(
			String miniClassId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("select * from account_charge_records where mini_class_id = :miniClassId ");
		params.put("miniClassId", miniClassId);
		return this.findBySql(hql.toString(), params);
	}
	
	@Override
	public List<AccountChargeRecords> findAccountRecordsByContractProduct(
			String contractProductId, String isWashed) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = :isWashed and contractProduct.id = :contractProductId ");

		params.put("isWashed", isWashed);
		params.put("contractProductId", contractProductId);
		hql.append(" group by transactionId ");
		return this.findAllByHQL(hql.toString(), params);
	}

	@Override
	public List<AccountChargeRecords> getAccountChargeRecords(ContractProduct targetConPrd) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append("SELECT * FROM account_charge_records WHERE CONTRACT_PRODUCT_ID=:contractProductId and AMOUNT=0 AND CHARGE_PAY_TYPE='CHARGE' AND IS_WASHED='FALSE'  ");
		params.put("contractProductId", targetConPrd.getId());
		return this.findBySql(sql.toString(), params);
	}
}
