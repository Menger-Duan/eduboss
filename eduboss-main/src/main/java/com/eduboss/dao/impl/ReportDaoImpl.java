
package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.dto.ModelVo;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.exception.ApplicationException;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.common.ContractType;
import com.eduboss.common.DataDictCategory;
import com.eduboss.common.Dimensionality;
import com.eduboss.common.FundsChangeAuditStatus;
import com.eduboss.common.MiniClassSurplusMoneyType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.ProductType;
import com.eduboss.common.ReportStudentSurplusFundingType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ReportDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.CampusEmployeesVo;
import com.eduboss.domainVo.CourseConsumeTeacherVo;
import com.eduboss.domainVo.GradeProportiStatObj;
import com.eduboss.domainVo.OperatBonusAndClsHrInxVo;
import com.eduboss.domainVo.OperatStuClsCompInxVo;
import com.eduboss.domainVo.ReportMiniClassSurplusMoneyVo;
import com.eduboss.domainVo.ReportStudentSurplusFundingVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MiniClassProductSearchVo;
import com.eduboss.dto.OrganizationDateReqVo;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.ReportUtil;
import com.eduboss.utils.StringUtil;

@Repository
public class ReportDaoImpl extends GenericDaoImpl<Object, String> implements ReportDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
	private UserDeptJobDao userDeptJobDao;

	@Override
	public List<OperatBonusAndClsHrInxVo> qryDataOperatBonusAndClsHrForStuMan(String beginDate, String endDate) {
		// TODO Auto-generated method stub
		//String sql="select ID as contractId,STUDENT_ID as stuId from contract  where ID='CON0000000024'";
		List<OperatBonusAndClsHrInxVo> bonusAndClsHrInxVoList=new ArrayList<OperatBonusAndClsHrInxVo>();
		Session  session = hibernateTemplate.getSessionFactory().openSession();
		Connection conn=session.connection();
		ResultSet rs=null;
		CallableStatement cs =null;
		try {
			cs  =conn.prepareCall("  { call getReportData_OperatBonusAndClsHr(?,?,?)}");
			cs.setString(1, "2014-10-13"); //设置输出参数
			cs.setString(2, "2014-10-20"); //设置输出参数
			cs.setString(3, "4028818d46e1abf10146e1abf3fd0001"); //设置输出参数
			System.out.println("00000");
			rs=  cs.executeQuery();
			System.out.println("1111111");
			while(rs.next()){
				String str=rs.getString(1);
				System.out.println(str);
				String str_name=rs.getString("name");
				System.out.println(str_name);
				OperatBonusAndClsHrInxVo bonusAndClsHrInxVo=new OperatBonusAndClsHrInxVo();  //用户名
				bonusAndClsHrInxVo.setName((String)ReportUtil.setColumnValue(rs.getString("name"), "String"));  //
				bonusAndClsHrInxVo.setOooRealSignPreFundAmt((BigDecimal)ReportUtil.setColumnValue(rs.getBigDecimal("ONE_PAID_AMOUNT"), "BigDecimal"));  //一对一实签预收额
				bonusAndClsHrInxVo.setOooSignNum((Integer)ReportUtil.setColumnValue(rs.getInt("ONE_STU_NUMBER"), "Integer"));  //一对一实签人数
				bonusAndClsHrInxVo.setOooSignSubNum((Integer)ReportUtil.setColumnValue(rs.getInt("ONE_COURSE_CATE_NUMBER"), "Integer"));  // 签单科次
				bonusAndClsHrInxVo.setOooRealSignHr((Integer)ReportUtil.setColumnValue(rs.getInt("ONE_COURSE_NUMBER"), "Integer"));   //	实签课时
				bonusAndClsHrInxVo.setOooFreeSignHr((BigDecimal)ReportUtil.setColumnValue(rs.getBigDecimal("ONE_FREE_HOUR"), "BigDecimal")); //一对一的赠送课时
				bonusAndClsHrInxVo.setSmclsRealSignPreFundAmt((BigDecimal)ReportUtil.setColumnValue(rs.getBigDecimal("SMALL_PAID_AMOUNT"), "BigDecimal"));  //小班实签预收额
				bonusAndClsHrInxVo.setSmclsSignNum((Integer)ReportUtil.setColumnValue(rs.getInt("SMALL_STU_NUMBER"), "Integer"));    //签单人数
				bonusAndClsHrInxVo.setSmclsSignSubNum((Integer)ReportUtil.setColumnValue(rs.getInt("SMALL_COURSE_CATE_NUMBER"), "Integer"));    //签单科次
				bonusAndClsHrInxVo.setPermclsRealSignPreFundAmt((BigDecimal)ReportUtil.setColumnValue(rs.getBigDecimal("OTHER_PAID_AMOUNT"), "BigDecimal")); //其他的实签预收额
				bonusAndClsHrInxVo.setPermclsSignNum((Integer)ReportUtil.setColumnValue(rs.getInt("OTHER_STU_NUMBER"), "Integer"));
				bonusAndClsHrInxVo.setPreFundTotal((BigDecimal)ReportUtil.setColumnValue(rs.getBigDecimal("TOTAL_AMOUNT"), "BigDecimal"));
//				bonusAndClsHrInxVo.setPreFundTarg(new BigDecimal(10));
//				bonusAndClsHrInxVo.setOooReFund(new BigDecimal(50));
//				bonusAndClsHrInxVo.setSmReFund(new BigDecimal(150));
				bonusAndClsHrInxVoList.add(bonusAndClsHrInxVo);
			}
			System.out.println("======end=====");
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(cs!=null){
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(session!=null){
				session.close();
			}
		}
		return bonusAndClsHrInxVoList;
	}

	@Override
	public List<OperatStuClsCompInxVo> qryDataOperatStuClsCompForStuMan(String beginDate,
																		String endDate) {
		return null;
	}

	@Override
	public List getTeacherCourseGrade(String teacherId, String startDate,
			String endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select dd.name,ifnull(charges,0) charges,ifnull(uncharges,0) uncharges,ifnull((charges + uncharges),0) total from data_dict dd");
		sql.append(" left join (select GRADE,sum(case when COURSE_STATUS='CHARGED' then REAL_HOURS else 0 end) charges,sum(case when (COURSE_STATUS<>'CHARGED' and COURSE_STATUS<>'TEACHER_LEAVE' and COURSE_STATUS<>'STUDENT_LEAVE' and COURSE_STATUS<>'CANCEL') then PLAN_HOURS else 0 end) uncharges from course where 1=1 ");
		if(StringUtils.isNotEmpty(teacherId)){
			sql.append(" and teacher_id = :teacherId ");
			params.put("teacherId", teacherId);
		}
		if(StringUtils.isNotEmpty(startDate)){
			sql.append(" and course_date >= :startDate ");
			params.put("startDate", startDate);
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and course_date <= :endDate ");
			params.put("endDate", endDate);
		}
		sql.append("  group by grade )c on  c.GRADE=dd.id ");
		sql.append("  where  dd.CATEGORY='STUDENT_GRADE' ");
		sql.append(" group by dd.name order by dd.DICT_ORDER");
		return super.findMapBySql(sql.toString(), params);
	}

	@Override
	public CampusEmployeesVo qryEmployeeNumberByRole(String beginDate,String endDate,String[] roleCodes,String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqlSb = new StringBuffer(
				"SELECT count(1) cnt, organization.`name` orgName,organization.id orgId");
		sqlSb.append(" FROM user INNER JOIN role ON user.role_id = role.id INNER JOIN organization ON user.organizationID = organization.id");
		sqlSb.append(" WHERE (user.RESIGN_DATE is not null or user.RESIGN_DATE !='' )  and roleCode in(:roleCodes) ");
		sqlSb.append(" and organization.id= :campusId ");
		params.put("roleCodes", roleCodes);
		params.put("campusId", campusId);
		List list = super.findMapBySql(sqlSb.toString(), params);
		CampusEmployeesVo vo=new CampusEmployeesVo();

		if(list.size()>0){
			Map map=(Map) list.get(0);
			vo.setCampusId(map.get("orgId").toString());
			vo.setCampusName(map.get("orgName").toString());
			vo.setEmployeesNum(new Integer(map.get("cnt").toString()));
		}
		return vo;
	}

	@Override
	public CampusEmployeesVo qryPartTmAndFullTmEmpNumber(String beginDate, String endDate,String campusId) {
		//String campusId="ff808081482bb63001482c1991610002";
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer  sqlSb=new StringBuffer("SELECT count(1) cnt, organization.id orgId,organization.`name` orgName, `user`.role_id, `role`.roleCode, work_type ");
		sqlSb.append(" FROM `user` INNER JOIN role ON `user`.role_id = role.id INNER JOIN organization ON `user`.organizationID = organization.id ");
		sqlSb.append("WHERE (`user`.RESIGN_DATE is not null or `user`.RESIGN_DATE !='' ) and roleCode = 'TEATCHER'  and organization.id= :campusId");
		sqlSb.append(" GROUP BY `user`.organizationID, `user`.role_id, work_type");
		params.put("campusId", campusId);
		List list = super.findMapBySql(sqlSb.toString(), params);
		CampusEmployeesVo vo=new CampusEmployeesVo();
		vo.setFullTimeTeacherNum(0);
		vo.setPartTimeTeacherNum(0);
		for (Object rowObj: list) {
			Map map=(Map) rowObj;
			vo.setCampusId(map.get("orgId").toString());
			String workType=map.get("work_type").toString();
			if(workType.equalsIgnoreCase("FULL_TIME")){
				vo.setFullTimeTeacherNum(new Integer(map.get("cnt").toString()));
			}else if(workType.equalsIgnoreCase("PART_TIME")){
				vo.setPartTimeTeacherNum(new Integer(map.get("cnt").toString()));
			}
		}
		return vo;
	}


	@Override
	public CampusEmployeesVo qryBTypeEmpNumber(String beginDate,String endDate,String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<CampusEmployeesVo> retList=new ArrayList<CampusEmployeesVo>();
		StringBuffer  sqlSb=new StringBuffer("");
		sqlSb.append("SELECT  count(*) cnt, organization.`name`, organization.id orgId, `user`.role_id, `role`.roleCode ");
		sqlSb.append("FROM `user` INNER JOIN role ON `user`.role_id = role.id");
		sqlSb.append("	 INNER JOIN organization ON `user`.organizationID = organization.id");
		sqlSb.append(" WHERE (`user`.RESIGN_DATE is not null or `user`.RESIGN_DATE !='' ) and roleCode = 'TEATCHER' and  organization.id = :campusId ");
		sqlSb.append("and (`user`.user_id in (  SELECT course.TEACHER_ID from course where  course.COURSE_DATE > :beginDate1 ");
		sqlSb.append(" and course.COURSE_DATE < :endDate1 )");
		sqlSb.append(" or `user`.user_id in ( SELECT  mini_class_course.TEACHER_ID FROM mini_class_course where mini_class_course.COURSE_DATE > :beginDate2 ");
		sqlSb.append(" and mini_class_course.COURSE_DATE < :endDate2) ");
		sqlSb.append(") GROUP BY `user`.organizationID, `user`.role_id");
		params.put("campusId", campusId);
		params.put("beginDate1", beginDate);
		params.put("endDate1", endDate);
		params.put("beginDate2", beginDate);
		params.put("endDate2", endDate);
		List list = super.findMapBySql(sqlSb.toString(), params);
		CampusEmployeesVo vo=new CampusEmployeesVo();
		vo.setBtypeTeacherNum(0);
		if(list.size()>0){
			Map map=(Map) list.get(0);
			vo.setCampusId(map.get("orgId").toString());
			vo.setBtypeTeacherNum(new Integer(map.get("cnt").toString()));
		}
		return vo;
	}

	@Override
	public CampusEmployeesVo qryResignEmpNumber(String beginDate, String endDate,String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<CampusEmployeesVo> retList=new ArrayList<CampusEmployeesVo>();
		StringBuffer  sqlSb=new StringBuffer("SELECT count(1) as resignNumber, organizationID FROM `user`");
		sqlSb.append("WHERE `user`.RESIGN_DATE > :beginDate ");
		sqlSb.append(" and `user`.RESIGN_DATE < :endDate ");
		sqlSb.append(" and organizationID = :campusId");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("campusId", campusId);
		List list = super.findMapBySql(sqlSb.toString(), params);
		CampusEmployeesVo vo=new CampusEmployeesVo();
		vo.setBtypeTeacherNum(0);
		if(list.size()>0){
			Map map=(Map) list.get(0);
			vo.setResignNum(new Integer(map.get("resignNumber").toString()));
		}
		return vo;
	}

	@Override
	public Map<String, Integer> getMiniClassFirstSchoolTimeStatistics(String miniClassId, String startDate, String endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String,Integer> map= new LinkedHashMap<String, Integer>();
		StringBuffer  sql=new StringBuffer("");
		sql.append("SELECT FIRST_SCHOOL_TIME, count(1) as number ");
		sql.append(" FROM mini_class_student ");
		sql.append(" WHERE MINI_CLASS_ID = :miniClassId");
		sql.append(" AND :startDate <= FIRST_SCHOOL_TIME ");
		sql.append("AND FIRST_SCHOOL_TIME <= :endDate GROUP BY FIRST_SCHOOL_TIME");
		sql.append(" order by FIRST_SCHOOL_TIME");
		params.put("miniClassId", miniClassId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		List list = super.findMapBySql(sql.toString(), params);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Map result=(Map)list.get(i);
				map.put(result.get("FIRST_SCHOOL_TIME").toString(),new Integer(result.get("number").toString()));
			}
		}
		return map;
	}

	@Override
	public Map<String,Integer> getMiniClassEnrollmentAnalysis(String miniClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String,Integer> map= new HashMap<String,Integer>();
		StringBuffer  sql=new StringBuffer("");
		sql.append("SELECT  t.NAME ,count(1) as numInSchool ");
		sql.append("FROM student s LEFT JOIN STUDENT_SCHOOL t ON s.SCHOOL=t.ID  ");
		sql.append("WHERE s.ID IN ");
		sql.append("(SELECT STUDENT_ID from mini_class_student where MINI_CLASS_ID = :miniClassId)");
		sql.append("GROUP BY school");
		params.put("miniClassId", miniClassId);
		List list = super.findMapBySql(sql.toString(), params);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Map result=(Map)list.get(i);
				if(result.get("NAME")==null){
					map.put("无学校",new Integer(result.get("numInSchool").toString()));
				}else {
					map.put(result.get("NAME").toString(),new Integer(result.get("numInSchool").toString()));
				}
			}
		}
		return map;
	}


	@Override
	public CampusEmployeesVo qryResignRatio(String beginDate,
											String endDate,String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<CampusEmployeesVo> retList=new ArrayList<CampusEmployeesVo>();
		StringBuffer  sqlSb=new StringBuffer("select totalTable.totalNumber, totalTable.organizationID, IFNULL(resignTable.resignNumber,0) resignNum, ");
		sqlSb.append("	IFNULL(resignTable.resignNumber,0) / (IFNULL(resignTable.resignNumber,0)+totalTable.totalNumber) as ratio from ");
		sqlSb.append("(");
		sqlSb.append("select count(*) as totalNumber, `user`.organizationID from user ");
		sqlSb.append("	where (`user`.RESIGN_DATE is not null or `user`.RESIGN_DATE !='')");
		sqlSb.append(" and organizationID = :campusId1");
		sqlSb.append(") as totalTable left join");
		sqlSb.append("(");
		sqlSb.append(" SELECT count(1) as resignNumber, organizationID ");
		sqlSb.append("	FROM `user`");
		sqlSb.append(" WHERE `user`.RESIGN_DATE > :beginDate ");
		sqlSb.append("	and `user`.RESIGN_DATE < :endDate ");
		sqlSb.append(" and organizationID = :campusId2");
		sqlSb.append(" ) as resignTable ");
		sqlSb.append("on resignTable.organizationID = totalTable.organizationID;");
		params.put("campusId1", campusId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("campusId2", campusId);
		List list = super.findMapBySql(sqlSb.toString(), params);
		CampusEmployeesVo vo=new CampusEmployeesVo();
		if(list.size()>0){
			Map map=(Map) list.get(0);
			Object ratio=map.get("ratio");
			if(ratio!=null){
				vo.setResignRat(ReportUtil.getFormatDecimal(new BigDecimal( map.get("ratio").toString())) );
			}else{   //为null则置为0
				vo.setResignRat(ReportUtil.getFormatDecimal(new BigDecimal("0")) );
			}

		}
		return vo;
	}


	@Override
	public CampusEmployeesVo qryTeacherResignRatio(String beginDate,
												   String endDate,String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<CampusEmployeesVo> retList=new ArrayList<CampusEmployeesVo>();
		StringBuffer  sqlSb=new StringBuffer("select totalTable.totalNumber, totalTable.organizationID, IFNULL(resignTable.resignNumber,0) resignNum, ");
		sqlSb.append("	IFNULL(resignTable.resignNumber,0) / (IFNULL(resignTable.resignNumber,0)+totalTable.totalNumber) as ratio from ");
		sqlSb.append("(");
		sqlSb.append("	select count(*) as totalNumber, `user`.organizationID from user, role ");
		sqlSb.append("	where (`user`.RESIGN_DATE is not null or `user`.RESIGN_DATE !='')");
		sqlSb.append("		and user.role_id = role.id");
		sqlSb.append("		and role.roleCode = 'TEACHER'");
		sqlSb.append("  and organizationID = :campusId1");
		sqlSb.append(") as totalTable left join");
		sqlSb.append("(");
		sqlSb.append("	SELECT count(1) as resignNumber, organizationID");
		sqlSb.append("		FROM `user`, role");
		sqlSb.append("	WHERE `user`.RESIGN_DATE > :beginDate ");
		sqlSb.append("");
		sqlSb.append("		and `user`.RESIGN_DATE < :endDate ");
		sqlSb.append("");
		sqlSb.append("		and user.role_id = role.id");
		sqlSb.append("		and role.roleCode = 'TEACHER'");
		sqlSb.append("  and organizationID = :campusId2");
		sqlSb.append(") as resignTable ");
		sqlSb.append("on resignTable.organizationID = totalTable.organizationID;");
		sqlSb.append("");
		params.put("campusId1", campusId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("campusId2", campusId);
		List list = super.findMapBySql(sqlSb.toString(), params);
		CampusEmployeesVo vo=new CampusEmployeesVo();
		if(list.size()>0){
			Map map=(Map) list.get(0);
			Object ratio=map.get("ratio");
			if(ratio!=null){
				vo.setTeacherResignRat(ReportUtil.getFormatDecimal(new BigDecimal(map.get("ratio").toString())));
			}else{
				vo.setTeacherResignRat(ReportUtil.getFormatDecimal(new BigDecimal("0")));
			}
		}
		return vo;
	}


	@Override
	public CampusEmployeesVo qryOtherEmpNumByRole(String beginDate,
												  String endDate, String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String[] otherRoleCodes = {"CONSULTOR","CONSULTOR_DIRECTOR","STUDY_MANAGER","STUDY_MANAGER_HEAD"};
		StringBuffer sqlSb = new StringBuffer(
				"SELECT count(1) cnt, organization.`name` orgName,organization.id orgId");
		sqlSb.append(" FROM user INNER JOIN role ON user.role_id = role.id INNER JOIN organization ON user.organizationID = organization.id");
		sqlSb.append(" WHERE (user.RESIGN_DATE is not null or user.RESIGN_DATE !='' )  and roleCode not in(:otherRoleCodes");
		sqlSb.append(") and organization.id= :campusId");
		params.put("otherRoleCodes", otherRoleCodes);
		params.put("campusId", campusId);
		List list = super.findMapBySql(sqlSb.toString(), params);
		CampusEmployeesVo vo=new CampusEmployeesVo();
		if(list.size()>0){
			Map map=(Map) list.get(0);
			vo.setOtherNum(new Integer(map.get("cnt").toString()));
		}
		return vo;
	}


	@Override
	public List<GradeProportiStatObj> getGradeProportionRpData(
			String startDate, String endDate, String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqlSb = new StringBuffer("");
		sqlSb.append("select GRADE_ID grdId,type,count(DISTINCT student.ID) cnt,data_dict.`NAME` grdName  ");
		sqlSb.append(" from contract INNER JOIN contract_product cp  on contract.ID=cp.CONTRACT_ID");
		sqlSb.append(" INNER JOIN   student on contract.STUDENT_ID=student.ID");
		sqlSb.append(" INNER JOIN   data_dict on student.GRADE_ID=data_dict.ID");
		sqlSb.append(" where     BL_CAMPUS_ID = :campusId ");
		sqlSb.append(" and    contract.CREATE_TIME > :startDate");
		sqlSb.append(" and    contract.CREATE_TIME < :endDate ");
		sqlSb.append(" group by data_dict.ID,type");
		params.put("campusId", campusId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		List list = super.findMapBySql(sqlSb.toString(), params);
		List<GradeProportiStatObj>dataList=new ArrayList<GradeProportiStatObj>();
		for (Object rowObj:list) {
			Map map=(Map) rowObj;
			GradeProportiStatObj statObj=new GradeProportiStatObj();
			Object typeObj=map.get("type");
			if(typeObj!=null){
				statObj.setType(typeObj.toString());
				statObj.setGradeId(map.get("grdId").toString());
				statObj.setGradeName(map.get("grdName").toString());
				statObj.setStatiNum(new Integer(map.get("cnt").toString()));
				dataList.add(statObj);
			}
		}
		return dataList;
	}


	/**
	 * 一对一课消老师视图(科目)
	 */
	@Override
	public DataPackage getOneOnOneCourseConsumeTeacherView(CourseConsumeTeacherVo vo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(vo.getOrganizationType())) {
			sql.append(" 	concat(o3.id, '_', o3.name) GROUNP, concat(o2.id, '_', o2.name) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' WORK_TYPE, '' COURSE_CAMPUS_ID,'' teacherCampus,  ");
		} else if (OrganizationType.BRENCH.equals(vo.getOrganizationType())) {
			sql.append(" 	concat(o3.id, '_', o3.name) GROUNP, concat(o2.id, '_', o2.name) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, '' TEACHER_NAME, '' WORK_TYPE, '' COURSE_CAMPUS_ID,'' teacherCampus, ");
		} else if (OrganizationType.CAMPUS.equals(vo.getOrganizationType())) {
			sql.append(" 	concat(o3.id, '_', o3.name) GROUNP, concat(o2.id, '_', o2.name) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME)  TEACHER_NAME, (case when e.WORK_TYPE ='FULL_TIME' THEN '全职' else '兼职' end) WORK_TYPE , org_course.NAME COURSE_CAMPUS_ID, ");
		} 
		
		sql.append(" SUM((case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)) TOTAL_CONSUME_HOUR,");
		sql.append(" SUM(case when d.REMARK='CHINESE_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) CHINESE_SUBJECT");
		sql.append(",SUM(case when d.REMARK='MATH_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) MATH_SUBJECT");
		sql.append(",SUM(case when d.REMARK='ENGLISH_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) ENGLISH_SUBJECT");
		sql.append(",SUM(case when d.REMARK='PHYSICS_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) PHYSICS_SUBJECT");
		sql.append(",SUM(case when d.REMARK='CHEMISTRY_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) CHEMISTRY_SUBJECT");
		sql.append(",SUM(case when d.REMARK='POLITICS_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) POLITICS_SUBJECT");
		sql.append(",SUM(case when d.REMARK='HISTORY_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) HISTORY_SUBJECT");
		sql.append(",SUM(case when d.REMARK='GEOGRAPHY_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) GEOGRAPHY_SUBJECT");
		sql.append(",SUM(case when d.REMARK='BIOLOGY_SUBJECT' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) BIOLOGY_SUBJECT");
		sql.append(",SUM(case when d.REMARK IS NULL OR d.REMARK='' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) OTHER_SUBJECT");
		sql.append("     from organization o3, organization o2 , organization o  ,  account_charge_records acr, course c, organization org_course, `data_dict` d,user e "                     );
		sql.append(" WHERE 1=1 ");
		sql.append(" and o.orgType='CAMPUS' and  d.ID = c.SUBJECT and c.TEACHER_ID=e.USER_ID")
		   .append(" and acr.COURSE_ID=c.COURSE_ID ")
		   .append(" and c.COURSE_STATUS='CHARGED'")
		   .append(" and o.id=c.BL_CAMPUS_ID ")
		   .append(" and  o3.id=o2.parentid ")
		   .append(" and  o2.id=o.parentid  ")
		   .append(" and c.BL_CAMPUS_ID = org_course.id ");
		
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND c.course_date >= :startDate ");
			params.put("startDate", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND c.course_date <= :endDate ");
			params.put("endDate", vo.getEndDate());
		}
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or c.subject = :subjects" + i + " ");
						params.put("subjects" + i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}
		if (OrganizationType.BRENCH.equals(vo.getOrganizationType())) {
			sql.append(" AND o2.ID = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		} else if (OrganizationType.CAMPUS.equals(vo.getOrganizationType())) {
			sql.append(" AND o.ID = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("课时消耗统计","sql","o.id"));

		if (OrganizationType.GROUNP.equals(vo.getOrganizationType())) {
			sql.append(" GROUP BY o2.id ");
		} else if (OrganizationType.BRENCH.equals(vo.getOrganizationType())) {
			sql.append(" GROUP BY o.id ");
		} else if (OrganizationType.CAMPUS.equals(vo.getOrganizationType())) {
			sql.append(" GROUP BY o.id,c.TEACHER_ID ");
		} 
		List list = super.findMapBySql(sql.toString(), params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}

	
	/**
	 * 一对一课消老师实时
	 */
	@Override
	public DataPackage getOneOnOneCourseConsumeTeacher(CourseConsumeTeacherVo vo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(vo.getOrganizationType())) {
			sql.append(" 	concat(o3.id, '_', o3.name) GROUNP, concat(o2.id, '_', o2.name) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID,'' teacherCampus,'' teacherLevel,'' teacherType,  ");
		} else if (OrganizationType.BRENCH.equals(vo.getOrganizationType())) {
			sql.append(" 	concat(o3.id, '_', o3.name) GROUNP, concat(o2.id, '_', o2.name) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID,'' teacherCampus,'' teacherLevel,'' teacherType, ");
		} else if (OrganizationType.CAMPUS.equals(vo.getOrganizationType())) {
			sql.append(" 	concat(o3.id, '_', o3.name) GROUNP, concat(o2.id, '_', o2.name) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME)  TEACHER_NAME, e.employee_No EMPLOYEE_NO, (case when e.WORK_TYPE ='FULL_TIME' THEN '全职' else '兼职' end) WORK_TYPE, org_course.NAME COURSE_CAMPUS_ID, ");
			sql.append("  (select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
	    	sql.append("  from teacher_version where version_date<='"+vo.getEndDate()+"' and teacher_id =c.teacher_id) and TEACHER_ID=c.teacher_id) teacherLevel,");
	    	if (StringUtils.isNotBlank(vo.getTeacherType())) {
				sql.append("   tv.TEACHER_TYPE teacherType, ");
			} else {
				sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
				sql.append("  from teacher_version where version_date<='"+vo.getEndDate()+"' and teacher_id =c.teacher_id) and TEACHER_ID=c.teacher_id) teacherType,");
			}
		} 
		if(StringUtils.isNotBlank(vo.getAnshazhesuan()) && "anxiaoshi".equals(vo.getAnshazhesuan())){
			sql.append("    ROUND(SUM(case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end * ifnull(c.COURSE_MINUTES,45))/ 60,2) TOTAL_CONSUME_HOUR,");
			sql.append("    ROUND(SUM(case when d.REMARK='FIRST_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end ) / 60,2)      FIRST_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='SECOND_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end )/ 60,2)      SECOND_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='THIRD_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end )/ 60,2)       THIRD_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='FOURTH_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end )/ 60,2)       FOURTH_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='FIFTH_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end )/ 60,2)      FIFTH_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='SIXTH_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end ) / 60,2)        SIXTH_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='MIDDLE_SCHOOL_FIRST_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end )/ 60,2)  MIDDLE_SCHOOL_FIRST_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='MIDDLE_SCHOOL_SECOND_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end )/ 60,2)   MIDDLE_SCHOOL_SECOND_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='MIDDLE_SCHOOL_THIRD_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end )/ 60,2)    MIDDLE_SCHOOL_THIRD_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='HIGH_SCHOOL_FIRST_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end ) / 60,2)   HIGH_SCHOOL_FIRST_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='HIGH_SCHOOL_SECOND_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end ) / 60,2)   HIGH_SCHOOL_SECOND_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK='HIGH_SCHOOL_THIRD_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end ) / 60,2)  HIGH_SCHOOL_THIRD_GRADE,");
			sql.append(" 	ROUND(SUM(case when d.REMARK IS NULL then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)*ifnull(c.COURSE_MINUTES,45) else 0 end ) / 60,2) OTHER_GRADE");
		}else {
			sql.append(" SUM((case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end)) TOTAL_CONSUME_HOUR,");
			sql.append(" SUM(case when d.REMARK='FIRST_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) FIRST_GRADE");
			sql.append(",SUM(case when d.REMARK='SECOND_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) SECOND_GRADE");
			sql.append(",SUM(case when d.REMARK='THIRD_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) THIRD_GRADE");
			sql.append(",SUM(case when d.REMARK='FOURTH_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) FOURTH_GRADE");
			sql.append(",SUM(case when d.REMARK='FIFTH_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) FIFTH_GRADE");
			sql.append(",SUM(case when d.REMARK='SIXTH_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) SIXTH_GRADE");
			sql.append(",SUM(case when d.REMARK='MIDDLE_SCHOOL_FIRST_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) MIDDLE_SCHOOL_FIRST_GRADE");
			sql.append(",SUM(case when d.REMARK='MIDDLE_SCHOOL_SECOND_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) MIDDLE_SCHOOL_SECOND_GRADE");
			sql.append(",SUM(case when d.REMARK='MIDDLE_SCHOOL_THIRD_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) MIDDLE_SCHOOL_THIRD_GRADE");
			sql.append(",SUM(case when d.REMARK='HIGH_SCHOOL_FIRST_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) HIGH_SCHOOL_FIRST_GRADE");
			sql.append(",SUM(case when d.REMARK='HIGH_SCHOOL_SECOND_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) HIGH_SCHOOL_SECOND_GRADE");
			sql.append(",SUM(case when d.REMARK='HIGH_SCHOOL_THIRD_GRADE' then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) HIGH_SCHOOL_THIRD_GRADE");
			sql.append(",SUM(case when d.REMARK IS NULL then (case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) else 0 end ) OTHER_GRADE");
		}
		sql.append("     from organization o3, organization o2 , organization o  ,  account_charge_records acr, course c   "                     );
		if (StringUtils.isNotBlank(vo.getTeacherType())){
			sql.append(" INNER JOIN teacher_version tv ON c.TEACHER_ID=tv.TEACHER_ID ");
		}
		sql.append(" , organization org_course, `data_dict` d,user e ");

		sql.append(" WHERE 1=1 ");
		sql.append(" and o.orgType='CAMPUS' and  d.ID = c.GRADE and c.TEACHER_ID=e.USER_ID")
		   .append(" and acr.COURSE_ID=c.COURSE_ID ")
		   .append(" and c.COURSE_STATUS='CHARGED'")
		   .append(" and o.id=c.BL_CAMPUS_ID ")
		   .append(" and  o3.id=o2.parentid ")
		   .append(" and  o2.id=o.parentid  ")
		   .append(" and c.BL_CAMPUS_ID = org_course.id  ");
//		if(OrganizationType.CAMPUS.equals(vo.getOrganizationType()) && StringUtils.isNotBlank(vo.getTeacherType())){
//			sql.append(" and a.teacherType='"+vo.getTeacherType()+"'");
//		}
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND c.course_date >= :startDate ");
			params.put("startDate", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND c.course_date <= :endDate ");
			params.put("endDate", vo.getEndDate());


			if (StringUtils.isNotBlank(vo.getTeacherType())){
				sql.append(" AND tv.version_date = (select max(version_date) ");
				sql.append("  from teacher_version where version_date<='"+vo.getEndDate()+"' and teacher_id =c.teacher_id " );
				sql.append(" and TEACHER_TYPE = :teacherType ");
				params.put("teacherType", vo.getTeacherType());
				sql.append(" ) ");
			}

		}
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or c.subject = :subjects" + i + " ");
						params.put("subjects" + i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}
		if (OrganizationType.BRENCH.equals(vo.getOrganizationType())) {
			sql.append(" AND o2.ID = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		} else if (OrganizationType.CAMPUS.equals(vo.getOrganizationType())) {
			if (StringUtil.isNotBlank(vo.getBlCampusId())){
				sql.append(" AND o.ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			}
			if (StringUtil.isNotBlank(vo.getBranchId())){
				sql.append(" AND o2.ID=:branchId ");
				params.put("branchId", vo.getBranchId());
			}

		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("课时消耗统计","sql","o.id"));
		if (OrganizationType.GROUNP.equals(vo.getOrganizationType())) {
			sql.append(" GROUP BY o2.id ");
		} else if (OrganizationType.BRENCH.equals(vo.getOrganizationType())) {
			sql.append(" GROUP BY o.id ");
		} else if (OrganizationType.CAMPUS.equals(vo.getOrganizationType())) {
			sql.append(" GROUP BY o.id,c.TEACHER_ID ");
		} 
		
		List<Map<Object, Object>> list= super.findMapBySql(sql.toString(), params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}

	/**
	 * 通过小时折算
	 *
	 * @param startDate
	 * @param endDate
	 * @param organizationType
	 * @param blCampusId
	 * @param dp
	 * @param currentLoginUser
	 * @param belongCampus
	 * @return
	 */
	@Override
	public DataPackage getOneOnOneCourseConsumeTeacherViewHours(String startDate, String endDate, OrganizationType organizationType, String blCampusId, DataPackage dp, User currentLoginUser, Organization belongCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(organizationType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' WORK_TYPE, '' COURSE_CAMPUS_ID,  ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, '' TEACHER_NAME, '' WORK_TYPE, '' COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME)  TEACHER_NAME, case when e.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(f.`name`,'') COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.OTHER.equals(organizationType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME)  TEACHER_NAME, case when e.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(f.`name`,'') COURSE_CAMPUS_ID, ");
		}
		sql.append(" 	ROUND(SUM(a.`TOTAL_CONSUME_HOUR`)        /60,2)  TOTAL_CONSUME_HOUR, ");
		sql.append(" 	ROUND(SUM(a.`CHINESE_SUBJECT`)           /60,2)  CHINESE_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`MATH_SUBJECT`)              /60,2)  MATH_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`ENGLISH_SUBJECT`)           /60,2)  ENGLISH_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`PHYSICS_SUBJECT`)           /60,2)  PHYSICS_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`CHEMISTRY_SUBJECT`)         /60,2)  CHEMISTRY_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`POLITICS_SUBJECT`)          /60,2)  POLITICS_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`HISTORY_SUBJECT`)           /60,2)  HISTORY_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`GEOGRAPHY_SUBJECT`)         /60,2)  GEOGRAPHY_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`BIOLOGY_SUBJECT`)           /60,2)  BIOLOGY_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`OTHER_SUBJECT`)             /60,2)  OTHER_SUBJECT, ");
		sql.append(" 	ROUND(SUM(a.`FIRST_GRADE`)               /60,2)  FIRST_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`SECOND_GRADE`)              /60,2)  SECOND_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`THIRD_GRADE`)               /60,2)  THIRD_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`FOURTH_GRADE`)              /60,2)  FOURTH_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`FIFTH_GRADE`)               /60,2)  FIFTH_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`SIXTH_GRADE`)               /60,2)  SIXTH_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`MIDDLE_SCHOOL_FIRST_GRADE`) /60,2)  MIDDLE_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`MIDDLE_SCHOOL_SECOND_GRADE`)/60,2)  MIDDLE_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`MIDDLE_SCHOOL_THIRD_GRADE`) /60,2)  MIDDLE_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`HIGH_SCHOOL_FIRST_GRADE`)   /60,2)  HIGH_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`HIGH_SCHOOL_SECOND_GRADE`)  /60,2)  HIGH_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	ROUND(SUM(a.`HIGH_SCHOOL_THIRD_GRADE`)   /60,2)  HIGH_SCHOOL_THIRD_GRADE ");
		sql.append(" FROM `ods_day_course_xiaoshi_consume_teacher_subject` a ");
		sql.append(" LEFT JOIN `organization` b ON b.ID = a.GROUP_ID ");
		sql.append(" LEFT JOIN `organization` c ON c.ID = a.BRANCH_ID ");
		sql.append(" LEFT JOIN `organization` o ON o.ID = a.CAMPUS_ID ");
		sql.append(" LEFT JOIN `user` e ON e.USER_ID = a.USER_ID ");
		sql.append(" LEFT JOIN `organization` f ON f.ID = a.COURSE_CAMPUS_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" AND COUNT_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" AND COUNT_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if (OrganizationType.GROUNP.equals(organizationType)) {
//			sql.append(" AND ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" AND (c.ID = :blCampusId1 ");
			params.put("blCampusId1", blCampusId);
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				sql.append("    OR (a.BRANCH_ID = :blCampusId2 AND  a.COURSE_CAMPUS_ID IN (SELECT id FROM organization WHERE ");
				sql.append(" orgLevel like :orgLevel ");
				params.put("blCampusId2", blCampusId);
				params.put("orgLevel", org.getOrgLevel() + "%");
				for(int i = 1; i < userOrganizations.size(); i++){
					sql.append(" or orgLevel like :orgLevel" + i);
					params.put("orgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
				}
				sql.append(")))");
			}

		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			boolean flag=false;
			sql.append(" AND (o.ID = :blCampusId3 ");
			params.put("blCampusId3", blCampusId);
			StringBuilder inSql=new StringBuilder("");
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				inSql.append("     AND  (a.COURSE_CAMPUS_ID IN (SELECT id FROM organization WHERE ");
				inSql.append(" orgLevel like :orgLevel ");
				params.put("orgLevel", org.getOrgLevel() + "%");
				if(org.getId().equals(blCampusId)){
					flag=true;
				}
				for(int i = 1; i < userOrganizations.size(); i++){
					if(org.getId().equals(blCampusId)){
						flag=true;
					}
					inSql.append(" or orgLevel like :orgLevel" + i);
					params.put("orgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
				}
				inSql.append(") or a.USER_ID = :userId1))");
				params.put("userId1", userService.getCurrentLoginUser().getUserId());
			}
			if(flag){//如果是操作人自己的所属校区
				sql.append(" OR a.COURSE_CAMPUS_ID = :blCampusId) ");
				params.put("blCampusId4", blCampusId);
			}else{//如果是外校的老师在本校区上课
				sql.append(inSql);
			}
		} else if (OrganizationType.OTHER.equals(organizationType)) {
			sql.append(" AND a.USER_ID = :blCampusId ");
			params.put("blCampusId5", blCampusId);
		}
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (");
			sql.append("  e.USER_ID = :userId2 ");
			params.put("userId2", currentLoginUser.getUserId());
			sql.append(" or f.orgLevel like :fOrgLevel ");
			params.put("fOrgLevel", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or f.orgLevel like :fOrgLevel" + i);
				params.put("fOrgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			if (OrganizationType.GROUNP.equals(organizationType)) {//集团可以看到所属组织架构统计以及分
				sql.append("  OR a.COURSE_CAMPUS_ID IN (SELECT id FROM organization WHERE ");
				sql.append(" orgLevel like :gOrgLevel ");
				params.put("gOrgLevel", org.getOrgLevel() + "%");
				for(int i = 1; i < userOrganizations.size(); i++){
					sql.append(" or orgLevel like :gOrgLevel" + i);
					params.put("gOrgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
				}
				sql.append("))");
			}else if (OrganizationType.BRENCH.equals(organizationType)) {
//            sql.append("  OR a.COURSE_CAMPUS_ID IN (SELECT id FROM organization WHERE parentID = '"+blCampusId+"') )");
				sql.append("  OR (a.BRANCH_ID = :blCampusId6 AND a.COURSE_CAMPUS_ID IN (SELECT id FROM organization WHERE ")
						.append(" orgLevel like :bOrgLevel ");
				params.put("blCampusId6", blCampusId);
				params.put("bOrgLevel", org.getOrgLevel() + "%");
				for(int i = 1; i < userOrganizations.size(); i++){
					sql.append(" or orgLevel like :bOrgLevel" + i);
					params.put("bOrgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
				}
				sql.append(")))");
			} else if (OrganizationType.CAMPUS.equals(organizationType)) {
				sql.append(" OR a.CAMPUS_ID = :blCampusId7 )");
				params.put("blCampusId7", blCampusId);
			}else{
				sql.append(")");
			}
		}
		List<Role> roles = userService.getCurrentLoginUserRoles();
		if (roles.size()==1 && roles.get(0).getRoleCode()!=null  && roles.get(0).getRoleCode().getValue().equals("TEATCHER")) {
			sql.append(" and e.USER_ID = :userId3 ");
			params.put("userId3", currentLoginUser.getUserId());
		}
		if (OrganizationType.GROUNP.equals(organizationType)) {
			sql.append(" GROUP BY a.BRANCH_ID ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" GROUP BY a.CAMPUS_ID ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" GROUP BY a.USER_ID,a.COURSE_CAMPUS_ID ");
		} else if (OrganizationType.OTHER.equals(organizationType)) {
			sql.append(" GROUP BY a.COURSE_CAMPUS_ID ");
		}

		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 一对一课消老师视图（学生维度）  废弃方法   2016-05-31
	 */
	@Override
	public DataPackage getOneOnOneCourseConsumeStudentView(String startDate, String endDate, OrganizationType organizationType, String blCampusId, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(organizationType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' WORK_TYPE,   ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, '' TEACHER_NAME, '' WORK_TYPE, ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(o.ID,'_',o.`name`) CAMPUS, e.NAME TEACHER_NAME, case when e.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  ");
		}
		sql.append(" 	SUM(a.`TOTAL_CONSUME_HOUR`) TOTAL_CONSUME_HOUR, ");
		sql.append(" 	SUM(a.`CHINESE_SUBJECT`) CHINESE_SUBJECT, ");
		sql.append(" 	SUM(a.`MATH_SUBJECT`) MATH_SUBJECT, ");
		sql.append(" 	SUM(a.`ENGLISH_SUBJECT`) ENGLISH_SUBJECT, ");
		sql.append(" 	SUM(a.`PHYSICS_SUBJECT`) PHYSICS_SUBJECT, ");
		sql.append(" 	SUM(a.`CHEMISTRY_SUBJECT`) CHEMISTRY_SUBJECT, ");
		sql.append(" 	SUM(a.`POLITICS_SUBJECT`) POLITICS_SUBJECT, ");
		sql.append(" 	SUM(a.`HISTORY_SUBJECT`) HISTORY_SUBJECT, ");
		sql.append(" 	SUM(a.`GEOGRAPHY_SUBJECT`) GEOGRAPHY_SUBJECT, ");
		sql.append(" 	SUM(a.`BIOLOGY_SUBJECT`) BIOLOGY_SUBJECT, ");
		sql.append(" 	SUM(a.`OTHER_SUBJECT`) OTHER_SUBJECT, ");
		sql.append(" 	SUM(a.`FIRST_GRADE`) FIRST_GRADE, ");
		sql.append(" 	SUM(a.`SECOND_GRADE`) SECOND_GRADE, ");
		sql.append(" 	SUM(a.`THIRD_GRADE`) THIRD_GRADE, ");
		sql.append(" 	SUM(a.`FOURTH_GRADE`) FOURTH_GRADE, ");
		sql.append(" 	SUM(a.`FIFTH_GRADE`) FIFTH_GRADE, ");
		sql.append(" 	SUM(a.`SIXTH_GRADE`) SIXTH_GRADE, ");
		sql.append(" 	SUM(a.`MIDDLE_SCHOOL_FIRST_GRADE`) MIDDLE_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.`MIDDLE_SCHOOL_SECOND_GRADE`) MIDDLE_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.`MIDDLE_SCHOOL_THIRD_GRADE`) MIDDLE_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.`HIGH_SCHOOL_FIRST_GRADE`) HIGH_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.`HIGH_SCHOOL_SECOND_GRADE`) HIGH_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.`HIGH_SCHOOL_THIRD_GRADE`) HIGH_SCHOOL_THIRD_GRADE ");
		sql.append(" FROM `ODS_DAY_COURSE_CONSUME_STUDENT_SUBJECT` a ");
		sql.append(" LEFT JOIN `organization` b ON b.ID = a.GROUP_ID ");
		sql.append(" LEFT JOIN `organization` c ON c.ID = a.BRANCH_ID ");
		sql.append(" LEFT JOIN `organization` o ON o.ID = a.CAMPUS_ID ");
		sql.append(" LEFT JOIN `user` e ON e.USER_ID = a.USER_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" AND COUNT_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" AND COUNT_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if (OrganizationType.GROUNP.equals(organizationType)) {
//			sql.append(" AND ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" AND c.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" AND o.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("老师课消统计","sql","o.id"));
		if (OrganizationType.GROUNP.equals(organizationType)) {
			sql.append(" GROUP BY a.BRANCH_ID ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" GROUP BY a.CAMPUS_ID ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" GROUP BY a.USER_ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 小班剩余资金
	 * @param reportMiniClassSurplusMoneyVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	@Override
	public DataPackage getMiniClassSurplusMoney(ReportMiniClassSurplusMoneyVo reportMiniClassSurplusMoneyVo, DataPackage dp,
												User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		MiniClassSurplusMoneyType miniClassSurplusMoneyType = reportMiniClassSurplusMoneyVo.getMiniClassSurplusMoneyType();
		String countDate = reportMiniClassSurplusMoneyVo.getCountDate();
		String blCampusId = reportMiniClassSurplusMoneyVo.getBlCampusId();
		String userID = currentLoginUser.getUserId();
		String organizationId = currentLoginUser.getOrganizationId();//where orgLevel Like orgLevel+"%"
		String roleCode = currentLoginUser.getRoleCode();
		String orgType = currentLoginUserCampus.getOrgType().getValue();
		if (MiniClassSurplusMoneyType.GROUNP.equals(miniClassSurplusMoneyType)) {
			sql.append(" 	(SELECT CONCAT(ID,'_',`name`) FROM `organization` WHERE orgLevel = SUBSTRING(p.orgLevel, 1, 4)) GROUNP,"
					+ " (SELECT CONCAT(ID,'_',`name`) FROM `organization` WHERE orgLevel = SUBSTRING(p.orgLevel, 1, 8)) BRENCH, "
					+ " '' CAMPUS, '' TEACHER_NAME, '' STUDY_MANEGER_NAME, '' MINI_CLASS_NAME,  ");
		} else if (MiniClassSurplusMoneyType.BRENCH.equals(miniClassSurplusMoneyType)) {
			sql.append(" 	(SELECT CONCAT(ID,'_',`name`) FROM `organization` WHERE orgLevel = SUBSTRING(p.orgLevel, 1, 4)) GROUNP,"
					+ " (SELECT CONCAT(ID,'_',`name`) FROM `organization` WHERE orgLevel = SUBSTRING(p.orgLevel, 1, 8)) BRENCH, "
					+ "  CONCAT(p.ID,'_',p.`name`) CAMPUS, "
					+ " '' TEACHER_NAME, '' STUDY_MANEGER_NAME, '' MINI_CLASS_NAME, ");
		} else {
			sql.append(" 	(SELECT CONCAT(ID,'_',`name`) FROM `organization` WHERE orgLevel = SUBSTRING(p.orgLevel, 1, 4)) GROUNP,"
					+ " (SELECT CONCAT(ID,'_',`name`) FROM `organization` WHERE orgLevel = SUBSTRING(p.orgLevel, 1, 8)) BRENCH, "
					+ "  CONCAT(p.ID,'_',p.`name`) CAMPUS, "
					+ " CONCAT(n.USER_ID,'_',n.NAME) TEACHER,"
					+ " n.WORK_TYPE, "
					+ " CONCAT(o.USER_ID,'_',o.NAME) STUDY_MANEGER,"
					+ " m.name MINI_CLASS_NAME,"
					+ " m.START_DATE,"
					+ " m.STATUS, ");
		}
		sql.append("  cast(SUM(m.TOTAL_CLASS_HOURS/m.EVERY_COURSE_CLASS_NUM) as decimal(10,2)) as TOTAL_TEACH_NUM, ");
		sql.append("  cast(SUM(m.CONSUME/m.EVERY_COURSE_CLASS_NUM) as decimal(10,2)) as CONSUMED_TEACH_NUM, ");
		sql.append("  cast(SUM((m.TOTAL_CLASS_HOURS-m.CONSUME)/m.EVERY_COURSE_CLASS_NUM ) as decimal(10,2)) as SURPLUS_TEACH_NUM, ");
		sql.append("  cast(SUM(IFNULL(( ");
		sql.append("  SELECT sum(((case b.PAID_STATUS when 'PAID' then b.PAID_AMOUNT+b.PROMOTION_AMOUNT else b.PAID_AMOUNT end)  - b.CONSUME_AMOUNT) / (b.PRICE * b.DEAL_DISCOUNT)) ");
		sql.append("  FROM `mini_class_student` a ");
		sql.append("  INNER JOIN `contract_product` b ON a.CONTRACT_PRODUCT_ID = b.ID ");
		sql.append("  WHERE a.MINI_CLASS_ID = m.MINI_CLASS_ID ");
		sql.append("  ) , 0))as decimal(10,2)) SURPLUS_COURSE_HOUR, ");
		sql.append("  cast(SUM(IFNULL(( ");
		sql.append("  SELECT sum((case b.PAID_STATUS when 'PAID' then b.PAID_AMOUNT+b.PROMOTION_AMOUNT else b.PAID_AMOUNT end) - b.CONSUME_AMOUNT) ");
		sql.append("  FROM `mini_class_student` a ");
		sql.append("  INNER JOIN `contract_product` b ON a.CONTRACT_PRODUCT_ID = b.ID ");
		sql.append("  WHERE a.MINI_CLASS_ID = m.MINI_CLASS_ID ");
		sql.append("  ), 0))as decimal(15,2)) SURPLUS_MONEY ");
		sql.append("  FROM `mini_class` m ");
		sql.append("  left JOIN `user` n ON n.USER_ID = m.TEACHER_ID ");
		sql.append("  left JOIN `user` o ON o.USER_ID = m.STUDY_MANEGER_ID ");
		sql.append("  left JOIN `organization` p ON p.ID = m.BL_CAMPUS_ID ");
		sql.append(" left JOIN `organization` c ON c.ID = p.parentID ");
		sql.append(" left JOIN `organization` b ON b.ID = c.parentID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(reportMiniClassSurplusMoneyVo.getStartDate())) {
			sql.append(" AND m.START_DATE >= :startDate ");
			params.put("startDate", reportMiniClassSurplusMoneyVo.getStartDate());
		}
		if (StringUtils.isNotBlank(reportMiniClassSurplusMoneyVo.getEndDate())) {
			sql.append(" AND m.START_DATE <= :endDate ");
			params.put("endDate", reportMiniClassSurplusMoneyVo.getEndDate());
		}
		if(!reportMiniClassSurplusMoneyVo.getIsAllCourseStatus()){
			String courseStatus = reportMiniClassSurplusMoneyVo.getCourseStatus().getValue();
			sql.append(" AND m.STATUS = :courseStatus ");
			params.put("courseStatus", courseStatus);
		}
		if (MiniClassSurplusMoneyType.GROUNP.equals(miniClassSurplusMoneyType)) {
//			sql.append(" AND ");
		} else if (MiniClassSurplusMoneyType.BRENCH.equals(miniClassSurplusMoneyType)) {
			sql.append(" AND c.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (MiniClassSurplusMoneyType.CAMPUS.equals(miniClassSurplusMoneyType)) {
			sql.append(" AND p.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (MiniClassSurplusMoneyType.TEACHER.equals(miniClassSurplusMoneyType)) {
			sql.append(" AND n.USER_ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (MiniClassSurplusMoneyType.STUDY_MANEGER.equals(miniClassSurplusMoneyType)) {
			sql.append(" AND o.USER_ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("小班剩余资金","sql","m.BL_CAMPUS_ID"));
		if (MiniClassSurplusMoneyType.GROUNP.equals(miniClassSurplusMoneyType)) {
			sql.append(" GROUP BY c.id ");
		} else if (MiniClassSurplusMoneyType.BRENCH.equals(miniClassSurplusMoneyType)) {
			sql.append(" GROUP BY p.id ");
		} else if (MiniClassSurplusMoneyType.CAMPUS.equals(miniClassSurplusMoneyType)) {
			sql.append(" GROUP BY m.MINI_CLASS_ID ");
		} else if (MiniClassSurplusMoneyType.TEACHER.equals(miniClassSurplusMoneyType)) {
			sql.append(" GROUP BY m.MINI_CLASS_ID ");
		} else if (MiniClassSurplusMoneyType.STUDY_MANEGER.equals(miniClassSurplusMoneyType)) {
			sql.append(" GROUP BY m.MINI_CLASS_ID ");
		}
			sql.append(" order by c.id,p.id,m.study_maneger_id,m.TEACHER_ID ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 学生剩余资金 *** -yanliang
	 */
	@Override
	public DataPackage getReportStudentSurplusFunding(ReportStudentSurplusFundingVo reportStudentSurplusFundingVo, DataPackage dp,
												User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		ReportStudentSurplusFundingType reportStudentSurplusFundingType = reportStudentSurplusFundingVo.getReportStudentSurplusFundingType();
		String countDate = reportStudentSurplusFundingVo.getCountDate();
		String blCampusId = reportStudentSurplusFundingVo.getBlCampusId();
		if (ReportStudentSurplusFundingType.GROUP.equals(reportStudentSurplusFundingType)) {
			sql.append(" 	CONCAT(a.GROUP_ID, '_', (SELECT NAME FROM organization WHERE id = a.GROUP_ID)) GROUNP, "
					+ " CONCAT(a.BRANCH_ID, '_',  (SELECT NAME FROM organization WHERE id = a.BRANCH_ID)) BRANCH, "
					+ " '' CAMPUS, '' STUDENT_NAME, '' STUDY_MANAGER_NAME, ");
		} else if (ReportStudentSurplusFundingType.BRANCH.equals(reportStudentSurplusFundingType)) {
			sql.append(" 	CONCAT(a.GROUP_ID, '_', (SELECT NAME FROM organization WHERE id = a.GROUP_ID)) GROUNP, "
					+ " CONCAT(a.BRANCH_ID, '_',  (SELECT NAME FROM organization WHERE id = a.BRANCH_ID)) BRANCH, "
					+ " CONCAT(a.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE id = a.CAMPUS_ID)) CAMPUS, "
					+ " '' STUDENT_NAME, '' STUDY_MANAGER_NAME, ");
		} else if (ReportStudentSurplusFundingType.CAMPUS.equals(reportStudentSurplusFundingType)) {
			sql.append(" 	CONCAT(a.GROUP_ID, '_', (SELECT NAME FROM organization WHERE id = a.GROUP_ID)) GROUNP, "
					+ " CONCAT(a.BRANCH_ID, '_',  (SELECT NAME FROM organization WHERE id = a.BRANCH_ID)) BRANCH, "
					+ " CONCAT(a.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE id = a.CAMPUS_ID)) CAMPUS, "
					+ " '' STUDENT, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANAGER, ");// +
		} else if (ReportStudentSurplusFundingType.STUDENT.equals(reportStudentSurplusFundingType)) {
			sql.append(" 	CONCAT(a.GROUP_ID, '_', (SELECT NAME FROM organization WHERE id = a.GROUP_ID)) GROUNP, "
					+ " CONCAT(a.BRANCH_ID, '_',  (SELECT NAME FROM organization WHERE id = a.BRANCH_ID)) BRANCH, "
					+ " CONCAT(a.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE id = a.CAMPUS_ID)) CAMPUS, "
					+ " CONCAT(e.ID,'_',e.NAME) STUDENT, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANAGER, ");// +
		} else if (ReportStudentSurplusFundingType.STUDY_MANAGER.equals(reportStudentSurplusFundingType)) {
			sql.append(" 	CONCAT(a.GROUP_ID, '_', (SELECT NAME FROM organization WHERE id = a.GROUP_ID)) GROUNP, "
					+ " CONCAT(a.BRANCH_ID, '_',  (SELECT NAME FROM organization WHERE id = a.BRANCH_ID)) BRANCH, "
					+ " CONCAT(a.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE id = a.CAMPUS_ID)) CAMPUS, "
					+ " CONCAT(e.ID,'_',e.NAME) STUDENT, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANAGER, ");// +
		}

		sql.append("  SUM(a.`ONE_ON_ONE_REMAINING_HOUR`) ONE_ON_ONE_REMAINING_HOUR, ");
		sql.append("  SUM(a.`ONE_ON_ONE_REMAINING_FUNDING`) ONE_ON_ONE_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`ONE_ON_ONE_REMAINING_GIFTED_HOUR`) ONE_ON_ONE_REMAINING_GIFTED_HOUR, ");
		sql.append("  SUM(a.`ONE_ON_ONE_REMAINING_GIFTED_FUNDING`) ONE_ON_ONE_REMAINING_GIFTED_FUNDING, ");

		sql.append("  SUM(a.`SMALL_CLASS_REMAINING_HOUR`) SMALL_CLASS_REMAINING_HOUR, ");
		sql.append("  SUM(a.`SMALL_CLASS_REMAINING_FUNDING`) SMALL_CLASS_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`SMALL_CLASS_REMAINING_GIFTED_HOUR`) SMALL_CLASS_REMAINING_GIFTED_HOUR, ");
		sql.append("  SUM(a.`SMALL_CLASS_REMAINING_GIFTED_FUNDING`) SMALL_CLASS_REMAINING_GIFTED_FUNDING, ");

		sql.append("  SUM(a.`PROMISED_CLASS_REAL_REMAINING_FUNDING`) PROMISED_CLASS_REAL_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`PROMISED_CLASS_REMAINING_GIFTED_FUNDING`) PROMISED_CLASS_REMAINING_GIFTED_FUNDING, ");
		sql.append("  SUM(a.`PROMISED_CLASS_REMAINING_FUNDING`) PROMISED_CLASS_REMAINING_FUNDING, ");

		sql.append("  SUM(a.`LECTURE_REAL_REMAINING_FUNDING`) LECTURE_REAL_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`LECTURE_REMAINING_GIFTED_FUNDING`) LECTURE_REMAINING_GIFTED_FUNDING, ");
		sql.append("  SUM(a.`LECTURE_REMAINING_FUNDING`) LECTURE_REMAINING_FUNDING, ");

		sql.append("  SUM(a.`OTHER_REAL_REMAINING_FUNDING`) OTHER_REAL_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`OTHER_REMAINING_GIFTED_FUNDING`) OTHER_REMAINING_GIFTED_FUNDING, ");
		sql.append("  SUM(a.`OTHER_REMAINING_FUNDING`) OTHER_REMAINING_FUNDING, ");

		sql.append("  SUM(a.`ACTUAL_REMAINING_FUNDING`) ACTUAL_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`GIFTED_REMAINING_FUNDING`) GIFTED_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`TOTAL_REMAINING_FUNDING`) TOTAL_REMAINING_FUNDING, ");
		
		sql.append("  SUM(a.`ONE_ON_ONE_REAL_REMAINING_HOUR`) ONE_ON_ONE_REAL_REMAINING_HOUR, ");
		sql.append("  SUM(a.`ONE_ON_ONE_REAL_REMAINING_FUNDING`) ONE_ON_ONE_REAL_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`SMALL_CLASS_REAL_REMAINING_HOUR`) SMALL_CLASS_REAL_REMAINING_HOUR, ");
		sql.append("  SUM(a.`SMALL_CLASS_REAL_REMAINING_FUNDING`) SMALL_CLASS_REAL_REMAINING_FUNDING, ");
		
		sql.append("  SUM(a.`OTM_CLASS_REMAINING_HOUR`) OTM_CLASS_REMAINING_HOUR, ");
		sql.append("  SUM(a.`OTM_CLASS_REMAINING_FUNDING`) OTM_CLASS_REMAINING_FUNDING, ");
		sql.append("  SUM(a.`OTM_CLASS_REMAINING_GIFTED_HOUR`) OTM_CLASS_REMAINING_GIFTED_HOUR, ");
		sql.append("  SUM(a.`OTM_CLASS_REMAINING_GIFTED_FUNDING`) OTM_CLASS_REMAINING_GIFTED_FUNDING, ");
		sql.append("  SUM(a.`OTM_CLASS_REAL_REMAINING_HOUR`) OTM_CLASS_REAL_REMAINING_HOUR, ");
		sql.append("  SUM(a.`OTM_CLASS_REAL_REMAINING_FUNDING`) OTM_CLASS_REAL_REMAINING_FUNDING ,");
		
		sql.append("  SUM(a.`ELECTRONIC_ACCOUNT`) ELECTRONIC_ACCOUNT, ");
		sql.append("  SUM(a.`UN_DISTRIBUTION_AMOUNT`) UN_DISTRIBUTION_AMOUNT ");
		sql.append(" FROM `ODS_DAY_REPORT_STUDENT_SURPLUS_FUNDING` a ");
		sql.append(" LEFT JOIN `user` f ON f.USER_ID = a.STUDY_MANAGER_ID ");
		sql.append(" LEFT JOIN `student` e ON e.ID = a.STUDENT_ID ");
		sql.append(" LEFT JOIN `organization` o ON o.id = a.CAMPUS_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(countDate)) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", countDate);
		}
		if(!reportStudentSurplusFundingVo.getIsAllStudentStatus()){
			String courseStatus = reportStudentSurplusFundingVo.getStudentStatus().getValue();
			sql.append(" AND e.STATUS = :courseStatus ");
			params.put("courseStatus", courseStatus);
		}
		if(StringUtils.isNotBlank(reportStudentSurplusFundingVo.getStudentId())){
			sql.append(" AND a.STUDENT_ID = :studentId");
			params.put("studentId", reportStudentSurplusFundingVo.getStudentId());
		}
		if(StringUtils.isNotBlank(reportStudentSurplusFundingVo.getStudyManagerId())){
			sql.append(" AND a.STUDY_MANAGER_ID = :studyManagerId ");
			params.put("studyManagerId", reportStudentSurplusFundingVo.getStudyManagerId());
		}
		if (ReportStudentSurplusFundingType.GROUP.equals(reportStudentSurplusFundingType)) {
//			sql.append(" AND ");
		} else if (ReportStudentSurplusFundingType.BRANCH.equals(reportStudentSurplusFundingType)) {
			sql.append(" AND a.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (ReportStudentSurplusFundingType.CAMPUS.equals(reportStudentSurplusFundingType)) {
			sql.append(" AND a.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (ReportStudentSurplusFundingType.STUDENT.equals(reportStudentSurplusFundingType)) {
			sql.append(" AND e.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (ReportStudentSurplusFundingType.STUDY_MANAGER.equals(reportStudentSurplusFundingType)) {
			if(blCampusId.indexOf("-")==0){
				sql.append(" and a.CAMPUS_ID = :blCampusId ");
				sql.append(" and (f.USER_ID='' or f.USER_ID is null ) ");
				params.put("blCampusId", blCampusId.replace("-", ""));
			}else{
				sql.append(" and f.USER_ID = :blCampusId ");
				params.put("blCampusId", blCampusId);
			}
			
		}
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("manergerId","f.USER_ID");
		sqlMap.put("selManerger","('"+userService.getCurrentLoginUserId()+"')");

		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生剩余资金","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		if (ReportStudentSurplusFundingType.GROUP.equals(reportStudentSurplusFundingType)) {
			sql.append(" GROUP BY a.GROUP_ID,a.BRANCH_ID ");
		} else if (ReportStudentSurplusFundingType.BRANCH.equals(reportStudentSurplusFundingType)) {
			sql.append(" GROUP BY a.GROUP_ID,a.BRANCH_ID,a.CAMPUS_ID ");
		} else if (ReportStudentSurplusFundingType.CAMPUS.equals(reportStudentSurplusFundingType)) {
			sql.append(" GROUP BY a.GROUP_ID,a.BRANCH_ID,a.CAMPUS_ID, a.STUDY_MANAGER_ID ");
		} else if (ReportStudentSurplusFundingType.STUDENT.equals(reportStudentSurplusFundingType)) {
			sql.append(" GROUP BY a.GROUP_ID,a.BRANCH_ID,a.CAMPUS_ID, a.STUDY_MANAGER_ID, a.STUDENT_ID ");
		} else if (ReportStudentSurplusFundingType.STUDY_MANAGER.equals(reportStudentSurplusFundingType)) {
			sql.append(" GROUP BY a.GROUP_ID,a.BRANCH_ID,a.CAMPUS_ID, a.STUDY_MANAGER_ID, a.STUDENT_ID ");
		}
		if(!reportStudentSurplusFundingVo.isShowAll())
		sql.append(" HAVING SUM(a.`TOTAL_REMAINING_FUNDING`)<>0 ");
		sql.append(" ORDER BY a.GROUP_ID,a.BRANCH_ID,a.CAMPUS_ID,a.STUDY_MANAGER_ID desc");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params, 300));
		return dp;
	}

	/**
	 * 小班学生人数
	 */
	@Override
	public DataPackage getMiniClassStudentCount(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
		String countDate =basicOperationQueryVo.getCountDate();
		String blCampusId=basicOperationQueryVo.getBlCampusId();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' STUDY_MANEGER_NAME, '' MINI_CLASS_NAME,'' status, '' startdate,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(d.ID,'_',d.`name`) CAMPUS, '' TEACHER_NAME, '' STUDY_MANEGER_NAME, '' MINI_CLASS_NAME,'' status, '' startdate, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(d.ID,'_',d.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME) TEACHER, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, g.NAME MINI_CLASS_NAME,g.status,g.start_date startdate, ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(d.ID,'_',d.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME) TEACHER, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, g.NAME MINI_CLASS_NAME,g.status,g.start_date startdate, ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(d.ID,'_',d.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME) TEACHER, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, g.NAME MINI_CLASS_NAME,g.status,g.start_date startdate, ");
		}
		sql.append(" 	SUM(a.`ENROLL_COUNT`) ENROLL_COUNT ");
		sql.append(" FROM `ODS_DAY_MINI_CLASS_STUDENT_COUNT` a ");
		sql.append(" INNER JOIN `organization` b ON b.ID = a.GROUP_ID ");
		sql.append(" INNER JOIN `organization` c ON c.ID = a.BRANCH_ID ");
		sql.append(" INNER JOIN `organization` d ON d.ID = a.CAMPUS_ID ");
		sql.append(" INNER JOIN `user` e ON e.USER_ID = a.TEACHER_ID ");
		sql.append(" INNER JOIN `user` f ON f.USER_ID = a.STUDY_HEAD_ID ");
		sql.append(" INNER JOIN `mini_class` g ON g.MINI_CLASS_ID = a.MINI_CLASS_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(countDate)) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", countDate);
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND g.start_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND g.start_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (basicOperationQueryVo.getMiniClassStatus()!=null) {
			sql.append(" AND g.status = :status ");
			params.put("status", basicOperationQueryVo.getMiniClassStatus());
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType)) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
			sql.append(" AND c.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
			sql.append(" AND d.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryLevelType)) {
			sql.append(" AND e.USER_ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType)) {
			sql.append(" AND f.USER_ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("小班学生人数统计","sql","g.BL_CAMPUS_ID"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.MINI_CLASS_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.MINI_CLASS_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.MINI_CLASS_ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	@Override
	public DataPackage getMiniClassConsumeAnalyze(String startDate, String endDate ,
												  BasicOperationQueryLevelType basicOperationQueryLevelType,
												  String blCampusId, DataPackage dp, User currentLoginUser,
												  Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 小班课消统计
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		// 集团        分公司                   校区                  老师                  班主任                    小班名字 
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' STUDY_MANEGER_NAME, '' MINI_CLASS_NAME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(d.ID,'_',d.`name`) CAMPUS, '' TEACHER_NAME, '' STUDY_MANEGER_NAME, '' MINI_CLASS_NAME, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
			sql.append(" 	CONCAT(b.ID,'_',b.`name`) GROUNP, CONCAT(c.ID,'_',c.`name`) BRENCH, CONCAT(d.ID,'_',d.`name`) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME) TEACHER, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, g.NAME MINI_CLASS_NAME, ");
		}
		// 课次 课时 实际上课人数 扣费人数 扣费金额
		sql.append(" 	SUM(a.`COURSE_TIMES`) COURSE_TIMES , ");
		sql.append(" 	SUM(a.`COURSE_HOURS`) COURSE_HOURS ,");
		sql.append(" 	SUM(a.`ON_SCHOOL_NUMBER`) ON_SCHOOL_NUMBER, ");
		sql.append(" 	SUM(a.`ON_CHARGE_NUMBER`) ON_CHARGE_NUMBER ,");
		sql.append(" 	SUM(a.`CHARGE_AMOUNT`) CHARGE_AMOUNT ");

		sql.append(" FROM `ODS_DAY_MINI_CLASS_CONSUME` a ");
		sql.append(" INNER JOIN `organization` b ON b.ID = a.GROUP_ID ");
		sql.append(" INNER JOIN `organization` c ON c.ID = a.BRANCH_ID ");
		sql.append(" INNER JOIN `organization` d ON d.ID = a.CAMPUS_ID ");
		//之前的班主任可能会出现null 值     ， 所以要left join 
		sql.append(" LEFT JOIN `user` e ON e.USER_ID = a.TEACHER_ID ");
		sql.append(" LEFT JOIN `user` f ON f.USER_ID = a.STUDY_HEAD_ID ");
		sql.append(" INNER JOIN `mini_class` g ON g.MINI_CLASS_ID = a.MINI_CLASS_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" AND COUNT_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" AND COUNT_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType)) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
			sql.append(" AND c.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
			sql.append(" AND d.ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("小班课消统计","sql","g.BL_CAMPUS_ID"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.MINI_CLASS_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.MINI_CLASS_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType)) {
			sql.append(" GROUP BY a.MINI_CLASS_ID ");
		}
		sql.append(" HAVING COURSE_TIMES<>0 and COURSE_HOURS<>0 and ON_SCHOOL_NUMBER<>0 and ON_CHARGE_NUMBER<>0 and CHARGE_AMOUNT<>0 ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 现金流
	 */
	@Override
	public DataPackage getFinanceAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.USER_ID, '_', (select u.name from user u where u.USER_ID = f.USER_ID)) as userId, ");
		}  else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.USER_ID, '_', (select u.name from user u where u.USER_ID = f.USER_ID)) as userId, CONCAT(f.STUDENT_ID, '_', (select s.name from student s where s.ID = f.STUDENT_ID)) as stuId, ");
		}
		sql.append(" 	sum(f.COUNT_ONO_ON_ONE) as countOneOnOne,  ");
		sql.append(" 	sum(f.COUNT_MINI_CLASS) as countMiniClass,  ");
		sql.append(" 	sum(f.COUNT_PROMISE) as countPromise,  ");
		sql.append(" 	sum(f.COUNT_OTHERS) as countOthers,   ");
		sql.append(" 	sum(f.COUNT_TOTAL) as countTotal,  ");
		sql.append(" 	sum(f.COUNT_PROMOTION_AMOUNT) as countPromotionAmount,  ");
//		sql.append(" 	sum(f.COUNT_PROMOTION_AMOUNT/f.COUNT_PADDING_AMOUNT * 100)  as countPromotionRate,  ");
		sql.append(" 	sum(f.COUNT_PROMOTION_AMOUNT)/sum(f.COUNT_TOTAL)*100  as countPromotionRate,  ");
		sql.append(" 	sum(f.COUNT_PADDING_AMOUNT) as countPaddingAmount,  ");
		sql.append(" 	sum(f.COUNT_PAID_CASH_AMOUNT) as countPaidCashAmount,  ");
		sql.append(" 	sum(f.COUNT_PAID_ELECT_AMOUNT) as countPaidElectAmount,  ");
		sql.append(" 	sum(f.COUNT_PAID_TOTAL_AMOUNT)  as countPaidTotalAmount   ");
		sql.append(" FROM ODS_DAY_FINANCE f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND COUNT_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND COUNT_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if(basicOperationQueryVo.getContractType()!=null){
			sql.append(" AND CONTRACT_TYPE = :contractType ");
			params.put("contractType", basicOperationQueryVo.getContractType());
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.USER_ID= :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("现金流统计","sql","f.CAMPUS_ID"));
		/** 多组织架构查询  开始*/
		List<Organization> organizationList= organizationDao.findOrganizationByUserId(currentLoginUser.getUserId());
		for(Organization o:organizationList){
			sql.append("OR f.BRANCH_ID ='"+o.getId()+"'");
		}
		/** 多组织架构查询  结束*/
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.USER_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.USER_ID, f.STUDENT_ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 现金流
	 */
	@Override
	public DataPackage getFinanceContractAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, '' as userId,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, ");
		}  else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, CONCAT(ct.STUDENT_ID, '_', (select s.name from student s where s.ID = ct.STUDENT_ID)) as stuId, ");
		}
		sql.append(" sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' then ct.transaction_amount when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'WASH' then -ct.transaction_amount else 0 end)  countPaidCashAmount, ");
		sql.append(" sum(case when ct.CHANNEL ='ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' then ct.transaction_amount when ct.CHANNEL ='ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'WASH' then -ct.transaction_amount else 0 end)  countPaidElectAmount, ");
		sql.append(" SUM(CASE WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'NEW_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'NEW_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) AS countPaidCashAmount_new, ");
		sql.append(" SUM(CASE WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'RE_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'RE_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) AS countPaidCashAmount_re, ");
		sql.append(" sum(CASE WHEN ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) countPaidTotalAmount, ");
		sql.append(" sum(CASE WHEN ct.CONTRACT_TYPE = 'LIVE_CONTRACT' THEN ct.transaction_amount ELSE 0 END) liveIncomeAmount ");
		sql.append(" from ( ");
		
		sql.append(" select fun.CHANNEL,fun.FUNDS_PAY_TYPE,fun.transaction_amount,aa.CONTRACT_TYPE,fun.fund_campus_id campus_id");
		sql.append(" ,aa.sign_staff_id,aa.STUDENT_ID ");
		sql.append(" from funds_change_history fun  ");
		sql.append(" inner join contract aa on fun.contract_id = aa.id  ");
		
		sql.append(" where 1=1    and aa.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		sql.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER','ELECTRONIC_ACCOUNT') ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.TRANSACTION_TIME >= :startDateTime ");
			params.put("startDateTime", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.TRANSACTION_TIME <= :endDateTime ");
			params.put("endDateTime", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}

		if(basicOperationQueryVo.getContractType()!=null){
			sql.append(" AND aa.CONTRACT_TYPE = :contractType ");
			params.put("contractType", basicOperationQueryVo.getContractType());
		}

		if(basicOperationQueryVo.getAuditStatus()!=null){
			sql.append(" and fun.AUDIT_STATUS = :auditStatus ");
			params.put("auditStatus", basicOperationQueryVo.getAuditStatus());
		}
		if(basicOperationQueryVo.getContractType() != ContractType.RE_CONTRACT 
				&& basicOperationQueryVo.getAuditStatus() != FundsChangeAuditStatus.UNAUDIT
				&& basicOperationQueryVo.getAuditStatus() != FundsChangeAuditStatus.UNVALIDATE) {
			sql.append(" UNION ALL ");
			
			sql.append(" select '' CHANNEL,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,'LIVE_CONTRACT' CONTRACT_TYPE,ll.order_campusId  campus_id  ");
			sql.append(" ,(select user_id from user where employee_No = ll.user_employeeNo  limit 1) sign_staff_id,ll.student_id STUDENT_ID ");
			sql.append(" from live_payment_record ll ");
			sql.append(" WHERE 1=1 ");
			
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql.append(" AND ll.payment_date >= :startDate ");
				params.put("startDate", basicOperationQueryVo.getStartDate());
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql.append(" AND ll.payment_date <= :endDate ");
				params.put("endDate", basicOperationQueryVo.getEndDate());
			}
			sql.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		}
		
		sql.append(" ) ct ");
		sql.append(" INNER JOIN organization o  ON ct.campus_id = o.id  ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o2.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ct.sign_staff_id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("现金流统计","sql","o.id"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id, ct.STUDENT_ID ");
		}
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' then ct.transaction_amount when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'WASH' then -ct.transaction_amount else 0 end) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		/**按  按实收金额 获取 分公司的排名 开始*/
		Map<String, Object> branchParams = new HashMap<String, Object>();
		StringBuffer sql_branch = new StringBuffer();
		sql_branch.append(" SELECT concat(o2.id,'') as BRANCH_ID  ");
		
		sql_branch.append(" from ( ");
		
		sql_branch.append(" select fun.CHANNEL,fun.FUNDS_PAY_TYPE,fun.transaction_amount,aa.CONTRACT_TYPE,fun.fund_campus_id campus_id");
		sql_branch.append(" ,aa.sign_staff_id,aa.STUDENT_ID ");
		sql_branch.append(" from funds_change_history fun  ");
		sql_branch.append(" inner join contract aa on fun.contract_id = aa.id  ");
		
		sql_branch.append(" where 1=1    and aa.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		sql_branch.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql_branch.append(" AND fun.TRANSACTION_TIME >= :startDateTime ");
			branchParams.put("startDateTime", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql_branch.append(" AND fun.TRANSACTION_TIME <= :endDateTime ");
			branchParams.put("endDateTime", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}

		sql_branch.append(" UNION ALL ");
		
		sql_branch.append(" select '' CHANNEL,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,'LIVE_CONTRACT' CONTRACT_TYPE,ll.order_campusId  campus_id  ");
		sql_branch.append(" ,(select user_id from user where employee_No = ll.user_employeeNo  limit 1) sign_staff_id,ll.student_id STUDENT_ID ");
		sql_branch.append(" from live_payment_record ll ");
		sql_branch.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql_branch.append(" AND ll.payment_date >= :startDate ");
			branchParams.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql_branch.append(" AND ll.payment_date <= :endDate ");
			branchParams.put("endDate", basicOperationQueryVo.getEndDate());
		}
		sql_branch.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		
		sql_branch.append(" ) ct ");
		sql_branch.append(" INNER JOIN organization o  ON ct.campus_id = o.id  ");
		sql_branch.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql_branch.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		
		sql_branch.append(" GROUP BY  o2.id ORDER BY sum(CASE WHEN ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) DESC ");
		List<Map<Object, Object>> list_branch = super.findMapBySql(sql_branch.toString(), branchParams);
		Map<String, String> map_branch = new HashMap<String, String>();
		int branchSort=0;
		String BRANCH_ID ="";
		Object BRANCH_ID_VALUE="";
		for(Map branch:list_branch){
			branchSort++;
			Set<String> set = branch.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				BRANCH_ID = iterator.next();
				BRANCH_ID_VALUE=branch.get(BRANCH_ID);
				if(BRANCH_ID_VALUE!=null){
					map_branch.put( BRANCH_ID_VALUE.toString(), branchSort+"");
				}

			}

		}
		/**按  按实收金额 获取 分公司的排名  结束*/
		
		/**按  按实收金额 获取 校区的排名 开始*/
		Map<String, Object> campusParams = new HashMap<String, Object>();
		StringBuffer sql_campus = new StringBuffer();
		sql_campus.append(" SELECT ct.campus_id as CAMPUS_ID ");
		
		sql_campus.append(" from ( ");
		
		sql_campus.append(" select fun.CHANNEL,fun.FUNDS_PAY_TYPE,fun.transaction_amount,aa.CONTRACT_TYPE,fun.fund_campus_id campus_id");
		sql_campus.append(" ,aa.sign_staff_id,aa.STUDENT_ID ");
		sql_campus.append(" from funds_change_history fun  ");
		sql_campus.append(" inner join contract aa on fun.contract_id = aa.id  ");
		
		sql_campus.append(" where 1=1    and aa.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		sql_campus.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql_campus.append(" AND fun.TRANSACTION_TIME >= :startDateTime ");
			campusParams.put("startDateTime", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql_campus.append(" AND fun.TRANSACTION_TIME <= :endDateTime ");
			campusParams.put("endDateTime", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}

		sql_campus.append(" UNION ALL ");
		
		sql_campus.append(" select '' CHANNEL,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,'LIVE_CONTRACT' CONTRACT_TYPE,ll.order_campusId  campus_id  ");
		sql_campus.append(" ,(select user_id from user where employee_No = ll.user_employeeNo  limit 1) sign_staff_id,ll.student_id STUDENT_ID ");
		sql_campus.append(" from live_payment_record ll ");
		sql_campus.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql_campus.append(" AND ll.payment_date >= :startDate ");
			campusParams.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql_campus.append(" AND ll.payment_date <= :endDate ");
			campusParams.put("endDate", basicOperationQueryVo.getEndDate());
		}
		sql_campus.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		
		sql_campus.append(" ) ct ");
		
		sql_campus.append(" GROUP BY  ct.campus_id ORDER BY sum(CASE WHEN ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) DESC ");
		List<Map<Object, Object>> list_campus=super.findMapBySql(sql_campus.toString(), campusParams);
		Map<String, String> map_campus = new HashMap<String, String>();

		int campusSort=0;
		String CAMPUS_ID ="";
		Object CAMPUS_ID_VALUE="";
		for(Map campus:list_campus){
			campusSort++;
			Set<String> set = campus.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				CAMPUS_ID = iterator.next();
				CAMPUS_ID_VALUE=campus.get(CAMPUS_ID);
				if(CAMPUS_ID_VALUE!=null){
					map_campus.put( CAMPUS_ID_VALUE.toString(), campusSort+"");
				}

			}

		}
		/**按  按实收金额 获取 校区的排名 结束*/

		//添加分公司排名和校区排名后的list
		List<Map> list_new=new ArrayList<Map>();
		String brenchId="";//分公司
		String campusId="";//分校
		for(Map map:list){
			//获取分公司排名
			if(map.get("brenchId")!=null){
				brenchId=map.get("brenchId").toString();
				if(brenchId.indexOf("_")!=-1){
					brenchId=brenchId.substring(0, brenchId.indexOf("_"));
				}
				map.put("BRANCH_SORT", map_branch.get(brenchId));
			}else{
				map.put("BRANCH_SORT","");
			}

			//获取分校区排名
			if(map.get("campusId")!=null){
				campusId=map.get("campusId").toString();
				if(campusId.indexOf("_")!=-1){
					campusId=campusId.substring(0, campusId.indexOf("_"));
				}
				map.put("CAMPUS_SORT", map_campus.get(campusId));
			}else{
				map.put("CAMPUS_SORT","");
			}

			list_new.add(map);
		}
		dp.setDatas(list_new);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	
	@Override
	public DataPackage getCampusSignContract(BasicOperationQueryVo vo,
			DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, '' as userId,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, ");
		}  else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, CONCAT(ct.STUDENT_ID, '_', (select s.name from student s where s.ID = ct.STUDENT_ID)) as stuId, ");
		}
		sql.append(" sum(cp.countOneOnOne)  countOneOnOne,");
		sql.append(" sum(cp.countMiniClass)  countMiniClass,");
		sql.append(" sum(cp.countOthers)  countOthers,");
		sql.append(" sum(cp.countPromise)  countPromise,");
		sql.append(" sum(cp.countOtmClass) countOtmClass,");
		sql.append(" sum(cp.countLecture) countLecture,");
		sql.append(" sum(cp.countTwoTeacher) countTwoTeacher,");
		sql.append(" sum(cp.countTotal)  countTotal,");
		sql.append(" cast(sum(cp.countPromotionAmount)/sum(cp.countTotal)*100 as decimal(10,2)) countPromotionRate,");
		sql.append(" sum(cp.countPromotionAmount) countPromotionAmount, ");
		sql.append(" sum(cp.countPaddingAmount) as countPaddingAmount,");
		sql.append(" sum(ct.PAID_AMOUNT)  paidAmount  from ( select ");
		
		sql.append(" CONTRACT_ID,sum(cp.PROMOTION_AMOUNT) countPromotionAmount, sum(REAL_AMOUNT) as countPaddingAmount,");
		sql.append(" sum(case when cp.TYPE = 'ONE_ON_ONE_COURSE' then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end)  countOneOnOne,");
		sql.append(" sum(case when cp.TYPE = 'SMALL_CLASS' then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end)  countMiniClass,");
		sql.append(" sum(case when cp.TYPE = 'OTHERS' then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end)  countOthers,");
		sql.append(" sum(case when cp.TYPE = 'ECS_CLASS' then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end)  countPromise,");
		sql.append(" sum(case when cp.TYPE = 'ONE_ON_MANY' then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end) countOtmClass,");
		sql.append(" sum(case when cp.TYPE = 'LECTURE' then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end) countLecture,");
		sql.append(" sum(case when cp.TYPE = 'TWO_TEACHER' then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end) countTwoTeacher,");
		sql.append(" sum(case when cp.TYPE in ('ONE_ON_ONE_COURSE', 'SMALL_CLASS', 'ECS_CLASS','ONE_ON_MANY' ,'OTHERS', 'LECTURE') then cp.PROMOTION_AMOUNT + REAL_AMOUNT else 0 end)  countTotal");
//		sql.append(" cast(sum(cp.PROMOTION_AMOUNT)/sum(cp.PROMOTION_AMOUNT + REAL_AMOUNT)*100 as decimal(10,2)) as countPromotionRate");
		sql.append(" from contract_product cp,contract c where  cp.CONTRACT_ID=c.ID");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND c.CREATE_TIME >= :startDate1 ");
			params.put("startDate1", vo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND c.CREATE_TIME <= :endDate1 ");
			params.put("endDate1", vo.getEndDate() + " 23:59:59");
		}
		sql.append(" group by CONTRACT_ID ) cp ");
		sql.append(" inner join contract ct on cp.contract_id = ct.id");
		sql.append(" inner join organization o on CT.bl_campus_id=o.id");
		sql.append(" inner join organization o2 on o.parentID=o2.id");
		sql.append(" inner join organization o3 on o2.parentID=o3.id");
		sql.append(" where o.orgType='CAMPUS'");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND ct.CREATE_TIME >= :startDate2 ");
			params.put("startDate2", vo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND ct.CREATE_TIME <= :endDate2 ");
			params.put("endDate2", vo.getEndDate() + " 23:59:59");
		}
		if(vo.getContractType()!=null){
			sql.append(" AND ct.CONTRACT_TYPE = :contractType ");
			params.put("contractType", vo.getContractType());
		}
		if(vo.getPaidStatus()!=null){
			sql.append(" AND ct.PAID_STATUS = :paidStatus ");
			params.put("paidStatus", vo.getPaidStatus());
		}
		if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" and o2.id = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id= :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" and ct.sign_staff_id= :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("现金流统计","sql","o.id"));
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id, ct.STUDENT_ID ");
		}
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY sum(cp.countTotal) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	
	
	@Override
	public DataPackage getFinanceContractAnalyzeRate(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus,String yearAndMonth,String targetType) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, '' as campusId, '' as userId,    ");
			sql.append(" 	  (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else CONCAT(FORMAT(sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' then ct.transaction_amount else 0 end) / pm.TARGET_VALUE*100,2),'%') end from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o2.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 0)).append(")as brenchValue,   ");
			sql.append(" 	 (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else pm.TARGET_VALUE end  from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o2.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 1)).append(")as brenchTValue,   ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, '' as userId,  ");
			sql.append(" 	 (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else CONCAT(FORMAT(sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' then ct.transaction_amount else 0 end) / pm.TARGET_VALUE*100,2),'%') end    from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 2)).append(")as campusValue,   ");
			sql.append(" 	 (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else pm.TARGET_VALUE end    from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 3)).append(")as campusTValue,   ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, ");
			sql.append(" 	 (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else CONCAT(FORMAT(sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' then ct.transaction_amount else 0 end) / pm.TARGET_VALUE*100,2),'%') end    from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 4)).append(")as campusValue,   ");
			sql.append(" 	 (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else pm.TARGET_VALUE end    from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 5)).append(")as campusTValue,   ");
		}  else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, CONCAT(ct.STUDENT_ID, '_', (select s.name from student s where s.ID = ct.STUDENT_ID)) as stuId, ");
			sql.append(" 	 (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else CONCAT(FORMAT(sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' then ct.transaction_amount else 0 end) / pm.TARGET_VALUE*100,2),'%') end    from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 6)).append(")as campusValue,   ");
			sql.append(" 	 (select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else pm.TARGET_VALUE end    from PLAN_MANAGEMENT  pm where pm.GOAL_ID=o.id ")
			.append(this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, params, targetType, 7)).append(")as campusTValue,   ");
		}

		sql.append(" sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' then ct.transaction_amount when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'WASH' then -ct.transaction_amount else 0 end)  countPaidCashAmount, ");
		sql.append(" sum(case when ct.CHANNEL ='ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' then ct.transaction_amount when ct.CHANNEL ='ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'WASH' then -ct.transaction_amount else 0 end)  countPaidElectAmount, ");
		sql.append(" SUM(CASE WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'NEW_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'NEW_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) AS countPaidCashAmount_new, ");
		sql.append(" SUM(CASE WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'RE_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'RE_CONTRACT' AND ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) AS countPaidCashAmount_re, ");
		sql.append(" sum(CASE WHEN ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) countPaidTotalAmount, ");
		sql.append(" sum(CASE WHEN ct.CONTRACT_TYPE = 'LIVE_CONTRACT' THEN ct.transaction_amount ELSE 0 END) liveIncomeAmount ");
		sql.append(" from ( ");
		
		sql.append(" select fun.CHANNEL,fun.FUNDS_PAY_TYPE,fun.transaction_amount,aa.CONTRACT_TYPE,fun.fund_campus_id campus_id");
		sql.append(" ,aa.sign_staff_id,aa.STUDENT_ID ");
		sql.append(" from funds_change_history fun  ");
		sql.append(" inner join contract aa on fun.contract_id = aa.id  ");
		
		sql.append(" where 1=1    and aa.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		sql.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER','ELECTRONIC_ACCOUNT') ");
		if (StringUtils.isNotBlank(yearAndMonth)) {
			sql.append(" AND fun.TRANSACTION_TIME like :yearAndMonth ");
			params.put("yearAndMonth", yearAndMonth + "%");
		}

		if(basicOperationQueryVo.getContractType()!=null){
			sql.append(" AND aa.CONTRACT_TYPE = :contractType ");
			params.put("contractType", basicOperationQueryVo.getContractType());
		}

		if(basicOperationQueryVo.getAuditStatus()!=null){
			sql.append(" and fun.AUDIT_STATUS = :auditStatus ");
			params.put("auditStatus", basicOperationQueryVo.getAuditStatus());
		}
		if(basicOperationQueryVo.getContractType() != ContractType.RE_CONTRACT 
				&& basicOperationQueryVo.getAuditStatus() != FundsChangeAuditStatus.UNAUDIT
				&& basicOperationQueryVo.getAuditStatus() != FundsChangeAuditStatus.UNVALIDATE) {
			sql.append(" UNION ALL ");
			
			sql.append(" select '' CHANNEL,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,'LIVE_CONTRACT' CONTRACT_TYPE,ll.order_campusId  campus_id  ");
			sql.append(" ,(select user_id from user where employee_No = ll.user_employeeNo  limit 1) sign_staff_id,ll.student_id STUDENT_ID ");
			sql.append(" from live_payment_record ll ");
			sql.append(" WHERE 1=1 ");
			
			if (StringUtils.isNotBlank(yearAndMonth)) {
				sql.append(" AND ll.payment_date like :yearAndMonth ");
				params.put("yearAndMonth", yearAndMonth + "%");
			}
			sql.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		}
		
		sql.append(" ) ct ");
		sql.append(" INNER JOIN organization o  ON ct.campus_id = o.id  ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");

		 if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o2.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ct.sign_staff_id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}

		sql.append(roleQLConfigService.getAppendSqlByAllOrg("现金流统计","sql","o.id"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id, ct.STUDENT_ID ");
		}
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY sum(case when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'RECEIPT' then ct.transaction_amount when ct.CHANNEL <>'ELECTRONIC_ACCOUNT' AND ct.FUNDS_PAY_TYPE = 'WASH' then -ct.transaction_amount else 0 end) DESC ");
//		sql.append(" ORDER BY sum(case when fun.CHANNEL <>'ELECTRONIC_ACCOUNT' then fun.transaction_amount else 0 end) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		/**按  按实收金额 获取 分公司的排名  开始*/
		StringBuffer sql_branch = new StringBuffer();
		Map<String, Object> branchParams = new HashMap<String, Object>();
		Map<String, String> map_brenchrate = new HashMap<String, String>();
		Map<String, String> map_brenchrateT = new HashMap<String, String>();
		sql_branch.append(" SELECT concat(o2.id,'') as BRANCH_ID, ");
		sql_branch.append(" ifnull((select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else CONCAT(FORMAT(SUM(ct.transaction_amount) / pm.TARGET_VALUE*100, 2), '%') end from PLAN_MANAGEMENT pm where  pm.GOAL_ID=o2.id "
				+this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, branchParams, targetType, 0)+"),'-') as tagetValue, ");
		sql_branch.append(" ifnull((select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else pm.TARGET_VALUE end from PLAN_MANAGEMENT pm where  pm.GOAL_ID=o2.id "
				+this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, branchParams, targetType, 1)+"),'-')  as tagetTValue ");
		
		sql_branch.append(" from ( ");
		
		sql_branch.append(" select fun.CHANNEL,fun.FUNDS_PAY_TYPE,fun.transaction_amount,aa.CONTRACT_TYPE,fun.fund_campus_id campus_id");
		sql_branch.append(" ,aa.sign_staff_id,aa.STUDENT_ID ");
		sql_branch.append(" from funds_change_history fun  ");
		sql_branch.append(" inner join contract aa on fun.contract_id = aa.id  ");
		
		sql_branch.append(" where 1=1    and aa.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		sql_branch.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		if (StringUtils.isNotBlank(yearAndMonth)) {
			sql_branch.append(" AND fun.TRANSACTION_TIME like :yearAndMonth ");
			branchParams.put("yearAndMonth", yearAndMonth + "%");
		}

		sql_branch.append(" UNION ALL ");
		
		sql_branch.append(" select '' CHANNEL,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,'LIVE_CONTRACT' CONTRACT_TYPE,ll.order_campusId  campus_id  ");
		sql_branch.append(" ,(select user_id from user where employee_No = ll.user_employeeNo  limit 1) sign_staff_id,ll.student_id STUDENT_ID ");
		sql_branch.append(" from live_payment_record ll ");
		sql_branch.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(yearAndMonth)) {
			sql_branch.append(" AND ll.payment_date like :yearAndMonth ");
			branchParams.put("yearAndMonth", yearAndMonth + "%");
		}
		sql_branch.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		
		sql_branch.append(" ) ct ");
		sql_branch.append(" INNER JOIN organization o  ON ct.campus_id = o.id  ");
		sql_branch.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql_branch.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		
		sql_branch.append(" GROUP BY  o2.id ORDER BY sum(CASE WHEN ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) DESC ");
		
		List<Map<Object, Object>> list_branch = super.findMapBySql(sql_branch.toString(), branchParams);
		Map<String, String> map_branch = new HashMap<String, String>();
		int branchSort=0;
		String BRANCH_ID ="";
		Object BRANCH_ID_VALUE="";
		for(Map branch:list_branch){
			branchSort++;
			Set<String> set = branch.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				BRANCH_ID = iterator.next();
				BRANCH_ID_VALUE=branch.get(BRANCH_ID);
				if(BRANCH_ID_VALUE!=null){
					map_branch.put( BRANCH_ID_VALUE.toString(), branchSort+"");
					map_brenchrate.put(BRANCH_ID_VALUE.toString(), branch.get("tagetValue").toString());
					map_brenchrateT.put(BRANCH_ID_VALUE.toString(), branch.get("tagetTValue").toString());
				}

			}

		}
		/**按  按实收金额 获取 分公司的排名  结束*/



		/**按  按实收金额 获取 校区的排名 开始*/
		StringBuffer sql_campus = new StringBuffer();
		Map<String, Object> campusParams = new HashMap<String, Object>();
		sql_campus.append(" SELECT ct.campus_id as CAMPUS_ID ");
		
		sql_campus.append(" from ( ");
		
		sql_campus.append(" select fun.CHANNEL,fun.FUNDS_PAY_TYPE,fun.transaction_amount,aa.CONTRACT_TYPE,fun.fund_campus_id campus_id");
		sql_campus.append(" ,aa.sign_staff_id,aa.STUDENT_ID ");
		sql_campus.append(" from funds_change_history fun  ");
		sql_campus.append(" inner join contract aa on fun.contract_id = aa.id  ");
		
		sql_campus.append(" where 1=1    and aa.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		sql_campus.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		if (StringUtils.isNotBlank(yearAndMonth)) {
			sql_campus.append(" AND fun.TRANSACTION_TIME like :yearAndMonth ");
			campusParams.put("yearAndMonth", yearAndMonth + "%");
		}

		sql_campus.append(" UNION ALL ");
		
		sql_campus.append(" select '' CHANNEL,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,'LIVE_CONTRACT' CONTRACT_TYPE,ll.order_campusId  campus_id  ");
		sql_campus.append(" ,(select user_id from user where employee_No = ll.user_employeeNo  limit 1) sign_staff_id,ll.student_id STUDENT_ID ");
		sql_campus.append(" from live_payment_record ll ");
		sql_campus.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(yearAndMonth)) {
			sql_campus.append(" AND ll.payment_date like :yearAndMonth ");
			campusParams.put("yearAndMonth", yearAndMonth + "%");
		}
		sql_campus.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		
		sql_campus.append(" ) ct ");
		
		sql_campus.append(" GROUP BY  ct.campus_id ORDER BY sum(CASE WHEN ct.FUNDS_PAY_TYPE = 'RECEIPT' THEN ct.transaction_amount WHEN ct.FUNDS_PAY_TYPE = 'WASH' THEN -ct.transaction_amount ELSE 0 END) DESC ");
		List<Map<Object, Object>> list_campus=super.findMapBySql(sql_campus.toString(), campusParams);
		Map<String, String> map_campus = new HashMap<String, String>();

		int campusSort=0;
		String CAMPUS_ID ="";
		Object CAMPUS_ID_VALUE="";
		for(Map campus:list_campus){
			campusSort++;
			Set<String> set = campus.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				CAMPUS_ID = iterator.next();
				CAMPUS_ID_VALUE=campus.get(CAMPUS_ID);
				if(CAMPUS_ID_VALUE!=null){
					map_campus.put( CAMPUS_ID_VALUE.toString(), campusSort+"");
				}

			}

		}
		/**按  按实收金额 获取 校区的排名 结束*/

		/**获取集团的完成率*/
		Map<String, Object> groupParams = new HashMap<String, Object>();
		StringBuffer groupRate = new StringBuffer();
		groupRate.append(" SELECT concat(o3.id,'') AS groupId, ");
		groupRate.append(" ifnull((select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else CONCAT(FORMAT(SUM(ct.transaction_amount) / pm.TARGET_VALUE*100, 2), '%') end from PLAN_MANAGEMENT pm where  pm.GOAL_ID=o3.id "
		+this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, groupParams, targetType, 0)+"),'-') as tagetValue, ");
		groupRate.append(" ifnull((select case when pm.TARGET_VALUE is null or pm.TARGET_VALUE<=0  then '-' else pm.TARGET_VALUE end from PLAN_MANAGEMENT pm where  pm.GOAL_ID=o3.id "
		+this.getFinanceContractAnalyzeRateAppendSql(basicOperationQueryVo, groupParams, targetType, 1)+"),'-')  as tagetTValue ");
		
		groupRate.append(" from ( ");
		
		groupRate.append(" select fun.CHANNEL,fun.FUNDS_PAY_TYPE,fun.transaction_amount,aa.CONTRACT_TYPE,fun.fund_campus_id campus_id");
		groupRate.append(" ,aa.sign_staff_id,aa.STUDENT_ID ");
		groupRate.append(" from funds_change_history fun  ");
		groupRate.append(" inner join contract aa on fun.contract_id = aa.id  ");
		
		groupRate.append(" where 1=1    and aa.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		groupRate.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		if (StringUtils.isNotBlank(yearAndMonth)) {
			groupRate.append(" AND fun.TRANSACTION_TIME like :yearAndMonth ");
			groupParams.put("yearAndMonth", yearAndMonth + "%");
		}

		groupRate.append(" UNION ALL ");
		
		groupRate.append(" select '' CHANNEL,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,'LIVE_CONTRACT' CONTRACT_TYPE,ll.order_campusId  campus_id  ");
		groupRate.append(" ,(select user_id from user where employee_No = ll.user_employeeNo  limit 1) sign_staff_id,ll.student_id STUDENT_ID ");
		groupRate.append(" from live_payment_record ll ");
		groupRate.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(yearAndMonth)) {
			groupRate.append(" AND ll.payment_date like :yearAndMonth ");
			groupParams.put("yearAndMonth", yearAndMonth + "%");
		}
		groupRate.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		
		groupRate.append(" ) ct ");
		
		
		groupRate.append(" INNER JOIN organization o  ON ct.campus_id = o.id    ");
		groupRate.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		groupRate.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");

		groupRate.append(" GROUP BY o3.id ORDER BY sum(ct.transaction_amount) DESC ");
		List<Map<Object, Object>> list_grouprate=super.findMapBySql(groupRate.toString(), groupParams);
		Map<String, String> map_grouprate = new HashMap<String, String>();
		Map<String, String> map_grouprateT = new HashMap<String, String>();
		for(Map brench:list_grouprate){
			map_grouprate.put(brench.get("groupId").toString(), brench.get("tagetValue").toString());
			map_grouprateT.put(brench.get("groupId").toString(), brench.get("tagetTValue").toString());
		}

		//添加分公司排名和校区排名后的list
		List<Map> list_new=new ArrayList<Map>();
		String groupId="";//集团
		String brenchId="";//分公司
		String campusId="";//分校
		for(Map map:list){
			//获取分公司排名
			if(map.get("groupId")!=null){
				groupId=map.get("groupId").toString();
				if(groupId.indexOf("_")!=-1){
					groupId=groupId.substring(0, groupId.indexOf("_"));
				}
				map.put("groupRate", map_grouprate.get(groupId));
				map.put("groupTRate", map_grouprateT.get(groupId));
			}else{
				map.put("groupRate","-");
				map.put("groupTRate","-");
			}

			//获取分公司排名
			if(map.get("brenchId")!=null){
				brenchId=map.get("brenchId").toString();
				if(brenchId.indexOf("_")!=-1){
					brenchId=brenchId.substring(0, brenchId.indexOf("_"));
				}
				map.put("BRANCH_SORT", map_branch.get(brenchId));
				if (!BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
					map.put("brenchValue", map_brenchrate.get(brenchId));
					map.put("brenchTValue", map_brenchrateT.get(brenchId));
				}
			}else{
				map.put("BRANCH_SORT","");
			}

			//获取分校区排名
			if(map.get("campusId")!=null){
				campusId=map.get("campusId").toString();
				if(campusId.indexOf("_")!=-1){
					campusId=campusId.substring(0, campusId.indexOf("_"));
				}
				map.put("CAMPUS_SORT", map_campus.get(campusId));
			}else{
				map.put("CAMPUS_SORT","");
			}

			list_new.add(map);
		}

		dp.setDatas(list_new);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 一对一学生统计
	 */
	@Override
	public DataPackage getOneOnOneStudentCountAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.user_id = f.STAFF_ID)) as userId, ");
		}

		sql.append(" 	sum(f.STU_COUNT) as studentCount,   ");
		sql.append(" 	sum(f.AVB_STU_COUNT) as avalibleStudentCount,  ");
		sql.append(" 	sum(f.STP_STU_COUNT) as stopStudentCount,  ");
		sql.append(" 	sum(f.CMP_STU_COUNT) as completeStudentCount,  ");
		sql.append(" 	sum(f.NEW_STU_COUNT) as newStudentCount, ");
		sql.append(" 	sum(f.GRADUATION_STU_COUNT) as graduationStudentCount  ");

		sql.append(" FROM ODS_REAL_STUDENT_COUNT f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("一对一学生统计","sql","f.CAMPUS_ID"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	@Override
	public DataPackage getStudentOOOStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			User currentLoginUser, Organization belongCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.user_id = f.STAFF_ID)) as userId, ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.user_id = f.STAFF_ID)) as userId,f.student_id,f.student_name, ");
		}

		sql.append(" 	sum(f.NEW_COUNT) as newCount,   ");
		sql.append(" 	sum(f.CLASSING_COUNT) as classingCount,  ");
		sql.append(" 	sum(f.STOP_COUNT) as stopCount,  ");
		sql.append(" 	sum(f.SPE_STOP_COUNT) as speStopCount,  ");
		sql.append(" 	sum(f.FINISH_COUNT) as finishCount, ");
		sql.append(" 	sum(f.GRADUATION_STU_COUNT) as graduationStuCount,  ");
		sql.append(" 	sum(f.STU_COUNT) as stuCount  ");

		sql.append(" FROM ODS_REAL_STUDENT_OOO_COUNT f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			if(basicOperationQueryVo.getBlCampusId().equals("hasNone")){
				sql.append(" and f.STAFF_ID is null ");
			}else{
				String[] campus = basicOperationQueryVo.getBlCampusId().split("_");
				if(campus.length>1){
					sql.append(" and f.CAMPUS_ID = :campusId ");
					sql.append(" and f.STAFF_ID = :staffId ");
					params.put("campusId", campus[0]);
					params.put("staffId", campus[1]);
				}else{
					sql.append(" and f.STAFF_ID = :blCampusId ");
					params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
			}
		}

//		sql.append(roleQLConfigService.getValueResult("一对一学生统计","sql"));
		
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (f.CAMPUS_ID in ( select id from organization where orgLevel like :orgLevel ");
			params.put("orgLevel", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like :orgLevel" + i);
				params.put("orgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			sql.append(") or f.STAFF_ID = :userId) ");
			params.put("userId", userService.getCurrentLoginUser().getUserId());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
			sql.append(" order by f.BRANCH_ID desc ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
			sql.append(" order by f.CAMPUS_ID desc ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID ");
			sql.append(" order by f.STAFF_ID desc ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID,f.student_id,f.student_name ");
			sql.append(" order by f.STAFF_ID desc ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	
	@Override
	public DataPackage getStudentOTMStatusCount(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			User currentLoginUser, Organization belongCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.user_id = f.STAFF_ID)) as userId, ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.user_id = f.STAFF_ID)) as userId,f.student_id,f.student_name, ");
		}

		sql.append(" 	sum(f.NEW_COUNT) as newCount,   ");
		sql.append(" 	sum(f.CLASSING_COUNT) as classingCount,  ");
		sql.append(" 	sum(f.STOP_COUNT) as stopCount,  ");
		sql.append(" 	sum(f.SPE_STOP_COUNT) as speStopCount,  ");
		sql.append(" 	sum(f.FINISH_COUNT) as finishCount, ");
		sql.append(" 	sum(f.GRADUATION_STU_COUNT) as graduationStuCount,  ");
		sql.append(" 	sum(f.STU_COUNT) as stuCount  ");

		sql.append(" FROM ODS_REAL_STUDENT_OTM_COUNT f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			if(basicOperationQueryVo.getBlCampusId().equals("hasNone")){
				sql.append(" and f.STAFF_ID is null ");
			}else{
				String[] campus = basicOperationQueryVo.getBlCampusId().split("_");
				if(campus.length>1){
					sql.append(" and f.CAMPUS_ID = :campusId ");
					sql.append(" and f.STAFF_ID = :staffId ");
					params.put("campusId", campus[0]);
					params.put("staffId", campus[1]);
				}else{
					sql.append(" and f.STAFF_ID = :blCampusId ");
					params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
			}
		}

		
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (f.CAMPUS_ID in ( select id from organization where orgLevel like :orgLevel ");
			params.put("orgLevel", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like :orgLevel" + i);
				params.put("orgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			sql.append(") or f.STAFF_ID = :userId) ");
			params.put("userId", userService.getCurrentLoginUser().getUserId());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
			sql.append(" order by f.BRANCH_ID desc ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
			sql.append(" order by f.CAMPUS_ID desc ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID ");
			sql.append(" order by f.STAFF_ID desc ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID,f.student_id,f.student_name ");
			sql.append(" order by f.STAFF_ID desc ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 一对一剩余资金
	 */
	@Override
	public DataPackage getOneOnOneStudentRemainAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId,  '' as userId,   ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.user_id = f.STAFF_ID)) as userId, ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.USER_ID = f.STAFF_ID)) as userId, CONCAT(f.STU_ID, '_', (select s.name from student s where s.ID = f.STU_ID)) as stuId, ");
		}

		sql.append(" 	sum(f.REMAINING_AMOUNT) as remainAmount, ");
		sql.append(" 	sum(f.REMAINING_HOUR) as remainHours,   ");
		sql.append(" 	sum(f.REMAINING_AMOUNT_REAL) as remainAmountReal, ");
		sql.append(" 	sum(f.REMAINING_AMOUNT_PROMOTION) as remainAmountPromotion, ");
		sql.append(" 	sum(f.REMAINING_HOUR_REAL) as remainHoursReal, ");
		sql.append(" 	sum(f.REMAINING_HOUR_PROMOTION) as remainHoursPromotion ");

		sql.append(" FROM ODS_REAL_STUDENT_REMAINING f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID= :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			if(basicOperationQueryVo.getBlCampusId().indexOf("-")==0){
				sql.append(" and f.CAMPUS_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId().replace("-", ""));
				sql.append(" and (f.STAFF_ID='' or f.STAFF_ID is null ) ");
			}else{
				sql.append(" and f.STAFF_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("一对一剩余资金统计","sql","f.CAMPUS_ID"));


		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID, f.STU_ID ");
		}
		sql.append(" order by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID desc , f.STU_ID desc ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 课消
	 */
	@Override
	public DataPackage getCourseConsomeAnalyse(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	concat(o3.id,'_',o3.name) groupId,concat(o2.id,'_',o2.name) brenchId, '' as campusId, '' as userId, '' as stuId,  ");

		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	concat(o3.id,'_',o3.name) groupId,concat(o2.id,'_',o2.name) brenchId,concat(o.id,'_',o.name) campusId, '' as userId, '' as stuId, ");

		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) || BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	concat(o3.id,'_',o3.name) groupId,concat(o2.id,'_',o2.name) brenchId,concat(o.id,'_',o.name) campusId, ");
			if (RoleCode.TEATCHER.equals(basicOperationQueryVo.getRole())) {
				sql.append(" CONCAT(c.TEACHER_ID, '_', (select u.name from user u where u.USER_ID = c.TEACHER_ID)) as userId ");
			} else if (RoleCode.STUDY_MANAGER.equals(basicOperationQueryVo.getRole())) {
				sql.append(" CONCAT(c.STUDY_MANAGER_ID, '_', (select u.name from user u where u.USER_ID = c.STUDY_MANAGER_ID)) as userId ");
			} else if (RoleCode.EDUCAT_SPEC.equals(basicOperationQueryVo.getRole())) {
				sql.append(" CONCAT(acr.OPERATE_USER_ID, '_', (select u.name from user u where u.USER_ID = acr.OPERATE_USER_ID)) as userId ");
			}
			if(BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
				sql.append(" , CONCAT(c.STUDENT_ID, '_', (select s.name from student s where s.ID = c.STUDENT_ID)) as stuId, ");
			}else{
				sql.append(", '' as stuId, ");
			}

		} 

		sql.append(" 	sum(case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.QUANTITY else -1*acr.QUANTITY end) as consumeHours,  ");
		sql.append(" 	sum(case when acr.CHARGE_PAY_TYPE='CHARGE' then acr.amount else -1*acr.amount end) as consumeAmount ");

		sql.append(" from organization o3,organization o2 ,organization o  ,account_charge_records acr,course c    ");
		
		
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND  c.course_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND  c.course_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}

		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and c.bl_campus_id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			if (RoleCode.TEATCHER.equals(basicOperationQueryVo.getRole())) {
				sql.append(" and c.TEACHER_ID = :teacherId ");
				params.put("teacherId", basicOperationQueryVo.getBlCampusId());
			} else if (RoleCode.STUDY_MANAGER.equals(basicOperationQueryVo.getRole())) {
				sql.append(" and c.STUDY_MANAGER_ID = :studyManagerId ");
				params.put("studyManagerId", basicOperationQueryVo.getBlCampusId());
			} else if (RoleCode.EDUCAT_SPEC.equals(basicOperationQueryVo.getRole())) {
				sql.append(" and acr.OPERATE_USER_ID = :operateUserId ");
				params.put("operateUserId", basicOperationQueryVo.getBlCampusId());
			}
			sql.append(" and c.bl_campus_id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getStaticCampusId());
		}

		if (StringUtils.isNotBlank(basicOperationQueryVo.getTeacherId())) {
			sql.append(" and c.TEACHER_ID ='"+basicOperationQueryVo.getTeacherId()+"' ");
		}
		
		sql.append(" and acr.COURSE_ID=c.COURSE_ID and c.COURSE_STATUS='CHARGED' ");
		

		sql.append(" and acr.IS_WASHED='FALSE' and acr.CHARGE_PAY_TYPE='CHARGE' ");
		sql.append(" and o.orgType='CAMPUS' and  o3.id=o2.parentid and  o2.id=o.parentid and o.id=c.BL_CAMPUS_ID ");
		
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o2.id = :brenchId ");
			params.put("brenchId", basicOperationQueryVo.getBlCampusId());
		}


		sql.append(roleQLConfigService.getAppendSqlByAllOrg("课时消耗统计","sql","o.id"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id,o2.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id,o2.id,o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) || BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id,o2.id,o.id  ");
			if (RoleCode.TEATCHER.equals(basicOperationQueryVo.getRole())) {
				sql.append(",c.TEACHER_ID ");
			} else if (RoleCode.STUDY_MANAGER.equals(basicOperationQueryVo.getRole())) {
				sql.append(",c.STUDY_MANAGER_ID ");
			} else if (RoleCode.EDUCAT_SPEC.equals(basicOperationQueryVo.getRole())) {
				sql.append(",acr.OPERATE_USER_ID ");
			}
			if(BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()))
			sql.append(" , c.STUDENT_ID ");
		}

		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		//DAT0000000253   课消目标
		Map<String,BigDecimal> targetMap=new HashMap<String,BigDecimal>();
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			targetMap = getTargetValueByParams( basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000253", "MONTH", "BRENCH");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			targetMap = getTargetValueByParams( basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000253", "MONTH", "CAMPUS");
		}
		
		List<Map> list_new=new ArrayList<Map>();
		
		for (Map<Object, Object> dataMap : list) {
			
			dataMap.put("target_value", 0);
			if(dataMap.get("brenchId")!=null && !"".equals(dataMap.get("brenchId"))){
				String brenchId=dataMap.get("brenchId").toString();
				if(brenchId.indexOf("_")!=-1){
					brenchId=brenchId.substring(0, brenchId.indexOf("_"));
				}
				
				if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
						&& targetMap != null) {
					dataMap.put("target_value", targetMap.get(brenchId)==null?0:targetMap.get(brenchId));
				}
			}

			if(dataMap.get("campusId")!=null && !"".equals(dataMap.get("campusId"))){
				String campusId=dataMap.get("campusId").toString();
				if(campusId.indexOf("_")!=-1){
					campusId=campusId.substring(0, campusId.indexOf("_"));
				}
				if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
						&& targetMap != null) {
					dataMap.put("target_value", targetMap.get(campusId)==null?0:targetMap.get(campusId));
				}

			}
			
			list_new.add(dataMap);
		}
		
		dp.setDatas(list_new);
		dp.setRowCount(list_new.size());
		return dp;
	}
	
	
	
	/**
	 * 获取月度指标集合
	 * @param startDate
	 * @param endDate
	 * @param targetType
	 * @param timeType
	 * @param goalType
	 * @return
	 */
	public Map<String,BigDecimal> getTargetValueByParams(String startDate,String endDate ,String targetType,String timeType,String goalType){
		/**月度计划指标 开始*/
		List<String> listMonth =new ArrayList<String>();
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
		  listMonth = DateTools.getMonths(startDate, endDate);
		}
		Map<String, BigDecimal>  month_rate_map = new HashMap<String, BigDecimal>();
		
		for(String months:listMonth){
			Map<String, Object> params = new HashMap<String, Object>();
			String year = months.substring(0, 4);
			String month = months.substring(5, 7);
			String yearId = dataDictDao.getDataDictIdByName(year, DataDictCategory.YEAR);
			String monthValue = CommonUtil.getMonthTypeValue(Integer.parseInt(month));
			StringBuffer month_rate_sql = new StringBuffer();
			month_rate_sql.append(" SELECT GOAL_ID, TARGET_VALUE FROM PLAN_MANAGEMENT where 1=1 ");
			month_rate_sql.append(" AND YEAR_ID = :yearId AND MONTH_ID = :monthValue AND TIME_TYPE = :timeType and TARGET_TYPE = :targetType ");
			month_rate_sql.append(" AND GOAL_TYPE = :goalType ");
			params.put("yearId", yearId);
			params.put("monthValue", monthValue);
			params.put("timeType", timeType);
			params.put("targetType", targetType);
			params.put("goalType", goalType);
			List<Map<Object, Object>> month_rate_list=super.findMapBySql(month_rate_sql.toString(), params);
			for(Map campus:month_rate_list){
				if(campus.get("TARGET_VALUE")!=null && month_rate_map.get(campus.get("GOAL_ID"))==null){
					month_rate_map.put( campus.get("GOAL_ID").toString(), new BigDecimal(campus.get("TARGET_VALUE").toString()));
				}else if(campus.get("TARGET_VALUE")!=null && month_rate_map.get(campus.get("GOAL_ID"))!=null){
					BigDecimal oldValue=month_rate_map.get(campus.get("GOAL_ID"));
					BigDecimal newValue=new BigDecimal(campus.get("TARGET_VALUE").toString());
					month_rate_map.put( campus.get("GOAL_ID").toString(), oldValue.add(newValue));
				}
			}
			}
		
		
		return month_rate_map;
	}

	/**
	 * 学生待收款资金
	 */
	public DataPackage getStudentPendingMoney(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new  HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId,  '' as userId,   ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.SIGN_STAFF_ID, '_', (select u.name from user u where u.user_id = f.SIGN_STAFF_ID)) as userId, ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.SIGN_STAFF_ID, '_', (select u.name from user u where u.USER_ID = f.SIGN_STAFF_ID)) as userId, CONCAT(f.STUDENT_ID, '_', (select s.name from student s where s.ID = f.STUDENT_ID)) as stuId, ");
		}
		sql.append(" 	sum(f.PENDING_AMOUNT) as pendingAmount ");
		sql.append(" FROM OSD_REAL_STUDENT_PENDING_MONEY f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.SIGN_STAFF_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}

		sql.append(roleQLConfigService.getAppendSqlByAllOrg("学生待收款统计","sql","f.CAMPUS_ID"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.SIGN_STAFF_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.SIGN_STAFF_ID, f.STUDENT_ID ");
		}

		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 购买课时数
	 */
	public DataPackage getPayClassHour(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, '' CAMPUS, '' as STUDY_MANAGE, '' as STUDENT, ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' as STUDY_MANAGE, '' as STUDENT, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(u.USER_ID, '_', u.name) as STUDY_MANAGE, '' as STUDENT, ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(u.USER_ID, '_', u.name) as STUDY_MANAGE, CONCAT(mal.STUDENT_ID, '_', s.name) as STUDENT, ");
		}

		sql.append(" sum(case when cp.TYPE = 'ONE_ON_ONE_COURSE' then mal.CHANGE_AMOUNT else 0 end) as ONE_ON_ONE_AMOUNT, ");
		sql.append(" sum(case when cp.TYPE = 'ONE_ON_ONE_COURSE' then mal.CHANGE_AMOUNT/cp.PRICE else 0 end) as ONE_ON_ONE_HOURS, ");
		sql.append(" sum(case when cp.TYPE = 'SMALL_CLASS' then mal.CHANGE_AMOUNT else 0 end) as SMALL_CLASS_AMOUNT, ");
		sql.append(" sum(case when cp.TYPE = 'SMALL_CLASS' then mal.CHANGE_AMOUNT/cp.PRICE else 0 end) as SMALL_CLASS_HOURS, ");
		sql.append(" pro.ONE_ON_ONE_PROMOTION_HOURS, pro.SMALL_CLASS_PROMOTION_HOURS  ");
		sql.append(" 	from money_arrange_log mal ");
		sql.append(" 	inner join student s on mal.STUDENT_ID = s.ID ");
		sql.append(" 	left join user u on s.STUDY_MANEGER_ID = u.USER_ID ");
		sql.append(" 	inner join organization o on s.BL_CAMPUS_ID = o.ID ");
		sql.append(" 	inner join organization org_brench on o.parentID = org_brench.ID ");
		sql.append(" 	inner join organization org_group on org_brench.parentID = org_group.ID ");
		sql.append(" 	inner join contract_product cp on mal.CONTRACT_PRODUCT_ID = cp.ID ");
		sql.append(" 	inner join (select c.STUDENT_ID, sum(case when cp.TYPE = 'ONE_ON_ONE_COURSE' then cp.PROMOTION_AMOUNT/cp.PRICE else 0 end) as ONE_ON_ONE_PROMOTION_HOURS, ");
		sql.append(" 				sum(case when cp.TYPE = 'SMALL_CLASS' then cp.PROMOTION_AMOUNT/cp.PRICE else 0 end) as SMALL_CLASS_PROMOTION_HOURS ");
		sql.append(" 		from contract_product cp ");
		sql.append(" 		inner join contract c on cp.CONTRACT_ID = c.ID  ");
		sql.append(" 		where cp.PAID_TIME is not null ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" 	AND cp.PAID_TIME >= :paidStartDate ");
			params.put("paidStartDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" 	AND cp.PAID_TIME <= :paidEndDate ");
			params.put("paidEndDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		sql.append(" 		group by c.STUDENT_ID) pro on mal.STUDENT_ID = pro.STUDENT_ID ");
		
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND mal.CHANGE_TIME >= :changeStartDate ");
			params.put("changeStartDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND mal.CHANGE_TIME <= :changeEndDate ");
			params.put("changeEndDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		sql.append(" AND (cp.TYPE = 'ONE_ON_ONE_COURSE' or cp.TYPE = 'SMALL_CLASS') ");
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
				&& !Dimensionality.CAMPUS_DIM.equals(basicOperationQueryVo.getDimensionality())) {
			sql.append(" and org_brench.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			if (basicOperationQueryVo.getBlCampusId().indexOf("-")==0) {
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId().replace("-", ""));
				sql.append(" and u.USER_ID is null ");
			} else {
				sql.append(" and u.USER_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("购买课时数统计","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id, o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id, o.id, u.USER_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by org_group.id, org_brench.id, o.id, u.USER_ID, s.ID ");
		}
		List<Map<Object, Object>> list = super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 购买课时数(校区维度)
	 */
	public DataPackage getPayClassHourCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId,  '' as userId  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId,  CONCAT(f.STUDENT_ID, '_', (select s.name from student s where s.ID = f.STUDENT_ID)) as stuId ");
		}
		sql.append(" ,sum(f.ONE_ON_ONE_PAID_AMOUNT) as oneOnOnePaidAmount ");
		sql.append(" ,sum(f.ONE_ON_ONE_PAID_HOUR) as oneOnOnePaidHour ");
		sql.append(" ,sum(f.ONE_ON_ONE_PROMOTION_HOUR) as oneOnOnePromotionHour ");
		sql.append(" ,sum(f.ONE_ON_ONE_TOTAL_HOUR) as oneOnOneTotalHour ");
		sql.append(" ,sum(f.SMALL_PAID_AMOUNT) as smallPaidAmount ");
		sql.append(" ,sum(f.SMALL_PAID_HOUR) as smallPaidHour ");
		sql.append(" FROM OSD_REAL_PAY_CLASS_HOUR_CAMPUS f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = : ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getValueResult("购买课时数统计(校区)","sql"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STUDENT_ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 退费 - 退费统计
	 * redmine#941 - 改读实时表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	@Override
	public DataPackage getRefundAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId, '' as userId, '' as stuId,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId, '' as stuId, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, ");
			sql.append(getRefundAnalyzeColumn(basicOperationQueryVo.getRole()));
			sql.append(", '' as stuId, ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, ");
			sql.append(getRefundAnalyzeColumn(basicOperationQueryVo.getRole()));
			sql.append(" , CONCAT(f.STUDENT_ID, '_', (select s.name from student s where s.ID = f.STUDENT_ID)) as stuId, f.REFUND_TYPE as refundType, ");
		}

		sql.append(" 	sum(f.BASE_AMOUNT) as baseAmount,  ");
		sql.append(" 	sum(f.SPECIAL_AMOUNT) as specialAmount,  ");
		sql.append(" 	sum(f.PROMOTION_AMOUNT) as promotionAmount ");

		sql.append(" FROM REAL_STUDENT_REFUND_ANALYSIS f,student s,organization o  ");
		sql.append(" WHERE 1=1 and f.student_id = s.id and o.id =f.CAMPUS_ID ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND COUNT_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND COUNT_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND f.GROUP_ID='"+userService.getBelongGrounpByUserId(userService.getCurrentLoginUser().getUserId()).getId()+"'");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" and f.SIGN_STAFF_ID = :blCampusId	 ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("signUser","f.SIGN_STAFF_ID");
		sqlMap.put("manegerId","s.STUDY_MANEGER_ID");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("退费统计","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");

		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");

		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.SIGN_STAFF_ID  ");

		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.SIGN_STAFF_ID, f.STUDENT_ID, f.REFUND_TYPE ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("SELECT count(*) FROM ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 收入
	 *
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	@Override
	public DataPackage getIncomingAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new  HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.name) as groupId, CONCAT(org_brench.id, '_', org_brench.name) as brenchId, '' as campusId, '' as studentId, ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.name) as groupId, CONCAT(org_brench.id, '_', org_brench.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, '' as studentId, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.name) as groupId, CONCAT(org_brench.id, '_', org_brench.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(s.ID, '_', s.name) as studentId, ");
		} 

		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount   ");
		sql.append(" 		when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as oneOnoneRealAmount,  ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 		when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as oneOnonePromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append("	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as smallClassRealAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as smallClassPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as escClassRealAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as escClassPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as otmClassRealAmount,  ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as otmClassPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as othersRealAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as othersPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as lectureRealAmount, ");
		sql.append("   sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append("   when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as lecturePromotionAmount, ");

		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as twoTeacherRealAmount, ");
		sql.append("   sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append("   when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as twoTeacherPromotionAmount, ");


		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LIVE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount/2.0  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LIVE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -(amount/2.0) end) as liveRealAmount, ");
		sql.append("   sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LIVE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount/2.0 ");
		sql.append("   when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LIVE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -(amount/2.0) end) as livePromotionAmount, ");


		sql.append(" 	sum(case when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as isNormalRealAmount, ");
		
		sql.append(" 	sum(case when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as isNormalPromotionAmount ");

		sql.append(" from account_charge_records acr ");
		sql.append(" inner join organization o on acr.BL_CAMPUS_ID = o.id ");
		sql.append(" inner join organization org_brench on o.parentID = org_brench.id ");
		sql.append(" inner join organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" left join student s on acr.STUDENT_ID = s.ID ");
		sql.append(" WHERE 1=1 ");//and not exists (select 1 from contract where id = acr.contract_id and contract_type = 'LIVE_CONTRACT'  )
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND acr.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND acr.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		sql.append(" AND acr.amount > 0 ");
		sql.append(" AND (CHARGE_TYPE = 'NORMAL' or CHARGE_TYPE = 'IS_NORMAL_INCOME') ");

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND org_group.id='"+userService.getBelongGrounpByUserId(userService.getCurrentLoginUser().getUserId()).getId()+"'");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
				&& !Dimensionality.CAMPUS_DIM.equals(basicOperationQueryVo.getDimensionality())) {
			sql.append(" and org_brench.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}

		sql.append(roleQLConfigService.getAppendSqlByAllOrg("课时收入统计","sql","acr.BL_CAMPUS_ID"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id, o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id, o.id, s.ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params, 300));
		return dp;
	}
	
	/**
	 * 收入
	 * 
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	@Override
	public DataPackage getIncomingAnalyze_1849(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) { 
		Map<String, Object> params = new  HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.name) as groupId, CONCAT(org_brench.id, '_', org_brench.name) as brenchId, '' as campusId, '' as studentId, ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.name) as groupId, CONCAT(org_brench.id, '_', org_brench.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, '' as studentId, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.name) as groupId, CONCAT(org_brench.id, '_', org_brench.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(s.ID, '_', s.name) as studentId, ");
		} 

		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount   ");
		sql.append(" 		when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as oneOnoneRealAmount,  ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 		when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as oneOnonePromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append("	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as smallClassRealAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as smallClassPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as escClassRealAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as escClassPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as otmClassRealAmount,  ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as otmClassPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as othersRealAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as othersPromotionAmount, ");
		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as lectureRealAmount, ");
		sql.append("   sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append("   when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as lecturePromotionAmount, ");

		sql.append(" 	sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount  ");
		sql.append(" 	when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as twoTeacherRealAmount, ");
		sql.append("   sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append("   when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'TWO_TEACHER' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as twoTeacherPromotionAmount, ");


		sql.append(" sum( IF(contact_type = 'NEW' and PRODUCT_TYPE = 'LIVE',total_amount,0)) as liveTotalAmount_NEW,");
		sql.append(" sum( IF(contact_type = 'RENEW' and PRODUCT_TYPE = 'LIVE',total_amount,0)) as liveTotalAmount_RENEW, ");
		sql.append(" sum( IF(contact_type = 'NEW' and PRODUCT_TYPE = 'LIVE',paid_amount,0)) as livePaidAmount_NEW, ");
		sql.append(" sum( IF(contact_type = 'RENEW' and PRODUCT_TYPE = 'LIVE',paid_amount,0)) as livePaidAmount_RENEW, ");


		sql.append(" 	sum(case when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'REAL' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'REAL' then -amount end) as isNormalRealAmount, ");
		
		sql.append(" 	sum(case when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'CHARGE' and PAY_TYPE = 'PROMOTION' then amount ");
		sql.append(" 	when CHARGE_TYPE = 'IS_NORMAL_INCOME' and CHARGE_PAY_TYPE = 'WASH' and PAY_TYPE = 'PROMOTION' then -amount end) as isNormalPromotionAmount ");
		
		sql.append(" from ( ");
		
		sql.append(" select aa.TRANSACTION_TIME,aa.amount,aa.STUDENT_ID,aa.BL_CAMPUS_ID ");
		sql.append(" ,aa.CHARGE_TYPE,aa.PRODUCT_TYPE,aa.CHARGE_PAY_TYPE,aa.PAY_TYPE ");
		sql.append(" ,'' contact_type,'' total_amount,'' paid_amount ");
		sql.append(" from account_charge_records aa ");
		sql.append(" WHERE 1=1 ");//and not exists (select 1 from contract where id = acr.contract_id and contract_type = 'LIVE_CONTRACT'  )
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND aa.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND aa.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		sql.append(" AND aa.amount > 0 ");
		sql.append(" AND (aa.CHARGE_TYPE = 'NORMAL' or aa.CHARGE_TYPE = 'IS_NORMAL_INCOME') ");
		sql.append(" AND (aa.PRODUCT_TYPE is null  or  aa.PRODUCT_TYPE <> 'Live' ) ");
		
		sql.append(" UNION ALL ");
		
		sql.append(" select ll.payment_date TRANSACTION_TIME,'' amount,ll.student_id STUDENT_ID,ll.order_campusId BL_CAMPUS_ID ");
		sql.append(" ,'' CHARGE_TYPE,'LIVE' PRODUCT_TYPE,'' CHARGE_PAY_TYPE,'' PAY_TYPE ");
		sql.append(" ,ll.contact_type,ll.total_amount,ll.paid_amount ");
		sql.append(" from live_payment_record ll ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND ll.payment_date >= :startDateLive ");
			params.put("startDateLive", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND ll.payment_date <= :endDateLive ");
			params.put("endDateLive", basicOperationQueryVo.getEndDate());
		}
		sql.append(" AND ll.finance_type = 'REVENUE' " );
		sql.append(" ) acr ");
		
		sql.append(" inner join organization o on acr.BL_CAMPUS_ID = o.id and o.orgType ='CAMPUS' ");
		sql.append(" inner join organization org_brench on o.parentID = org_brench.id ");
		sql.append(" inner join organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" left join student s on acr.STUDENT_ID = s.ID ");
		sql.append(" WHERE 1=1 ");//and not exists (select 1 from contract where id = acr.contract_id and contract_type = 'LIVE_CONTRACT'  )

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND org_group.id='"+userService.getBelongGrounpByUserId(userService.getCurrentLoginUser().getUserId()).getId()+"'");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
				&& !Dimensionality.CAMPUS_DIM.equals(basicOperationQueryVo.getDimensionality())) {
			sql.append(" and org_brench.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}

		sql.append(roleQLConfigService.getAppendSqlByAllOrg("课时收入统计","sql","acr.BL_CAMPUS_ID"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id, o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id, o.id, s.ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params, 300));
		return dp;
	}



	/**
	 * 营收列表
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getIncomingAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new  HashMap<String, Object>();
		// 总的和目标sql开始
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(o3.id, '', '') as groupId, o3.NAME as groupName, CONCAT(o2.id, '', '') as brenchId, o2.NAME as brenchName, '' as campusId, '' as campusName, ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(o3.id, '', '') as groupId, o3.NAME as groupName, CONCAT(o2.id, '', '') as brenchId, o2.NAME as brenchName, CONCAT(o.id, '', '') as campusId, o.NAME as campusName, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(o3.id, '', '') as groupId, o3.NAME as groupName, CONCAT(o2.id, '', '') as brenchId, o2.NAME as brenchName, CONCAT(o.id, '', '') as campusId, o.NAME as campusName, ");
		}
		
		sql.append(" ifnull(sum(oneOnoneRealAmount),0) as oneOnoneRealAmount,");
		sql.append(" ifnull(sum(oneOnonePromotionAmount),0) as oneOnonePromotionAmount,");
		sql.append(" ifnull(sum(oneOnOneQuantity),0) as oneOnOneQuantity,");
		sql.append(" ifnull(sum(smallClassRealAmount),0) as smallClassRealAmount,");
		sql.append(" ifnull(sum(smallClassPromotionAmount),0) as smallClassPromotionAmount,");
		sql.append(" ifnull(sum(smallClassQuantity),0) as smallClassQuantity,");
		sql.append(" ifnull(sum(escClassRealAmount),0) as escClassRealAmount,");
		sql.append(" ifnull(sum(escClassPromotionAmount),0) as escClassPromotionAmount,");
		sql.append(" ifnull(sum(othersRealAmount),0) as othersRealAmount,");
		sql.append(" ifnull(sum(othersPromotionAmount),0) as othersPromotionAmount,");
		sql.append(" ifnull(sum(otmRealAmount),0) as otmRealAmount,");
		sql.append(" ifnull(sum(otmPromotionAmount),0) as otmPromotionAmount,");
		sql.append(" ifnull(sum(otmQuantity),0) as otmQuantity,");
		sql.append(" ifnull(sum(lectureRealAmount),0) as lectureRealAmount,");
		sql.append(" ifnull(sum(lecturePromotionAmount),0) as lecturePromotionAmount,");
		sql.append(" ifnull(sum(isNormalRealAmount),0) as isNormalRealAmount,");
		sql.append(" ifnull(sum(isNormalPromotionAmount),0) as isNormalPromotionAmount,");
		sql.append(" ifnull(sum(realTotalAmount),0) as realTotalAmount  ");
		
		sql.append(" FROM organization o2, organization o3, organization o ");
		sql.append(" left join (SELECT  org_campus.id as campusId,");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and PAY_TYPE = 'REAL' then amount end), 0) as oneOnoneRealAmount,  ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' and PAY_TYPE = 'PROMOTION' then amount end), 0) as oneOnonePromotionAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' then quantity end), 0) as oneOnOneQuantity,  ");
		
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and PAY_TYPE = 'REAL' then amount end), 0) as smallClassRealAmount,  ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' and PAY_TYPE = 'PROMOTION' then amount end), 0) as smallClassPromotionAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'SMALL_CLASS' then quantity end), 0) as smallClassQuantity,  ");
		
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and PAY_TYPE = 'REAL' then amount end), 0) as escClassRealAmount,  ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ECS_CLASS' and PAY_TYPE = 'PROMOTION' then amount end), 0) as escClassPromotionAmount, ");
		
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and PAY_TYPE = 'REAL' then amount end), 0) as othersRealAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'OTHERS' and PAY_TYPE = 'PROMOTION' then amount end), 0) as othersPromotionAmount, ");
		
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and PAY_TYPE = 'REAL' then amount end), 0) as otmRealAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' and PAY_TYPE = 'PROMOTION' then amount end), 0) as otmPromotionAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'ONE_ON_MANY' then quantity end), 0) as otmQuantity,  ");
		
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and PAY_TYPE = 'REAL' then amount end), 0) as lectureRealAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'NORMAL' and PRODUCT_TYPE = 'LECTURE' and PAY_TYPE = 'PROMOTION' then amount end), 0) as lecturePromotionAmount, ");
		
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'IS_NORMAL_INCOME' and PAY_TYPE = 'REAL' then amount end), 0) as isNormalRealAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE = 'IS_NORMAL_INCOME' and PAY_TYPE = 'PROMOTION' then amount end), 0) as isNormalPromotionAmount, ");
		sql.append(" 	ifnull(sum(case when CHARGE_TYPE in ('NORMAL','IS_NORMAL_INCOME') and PAY_TYPE = 'REAL' then amount end), 0) as realTotalAmount, ");
		sql.append(" 	ifnull(sum(amount), 0) as totalAmount ");

		sql.append(" from organization org_campus ");
		sql.append(" inner join organization org_brench on org_campus.parentID = org_brench.id ");
		sql.append(" inner join organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" left join account_charge_records acr  on acr.BL_CAMPUS_ID = org_campus.id");
		sql.append(" 				WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" 				AND acr.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" 				AND acr.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		sql.append(" 					AND (CHARGE_TYPE = 'IS_NORMAL_INCOME' OR CHARGE_TYPE = 'NORMAL') ");

		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and org_campus.id in ( select id from organization where orgLevel like :campusOrgLevel ");
			params.put("campusOrgLevel", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like :campusOrgLevel" + i);
				params.put("campusOrgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			sql.append(")");
		}
		sql.append(" AND org_campus.orgType='CAMPUS'");
		sql.append(" group by org_campus.id) dataInfo on o.id=dataInfo.campusId ");
		
		sql.append("  WHERE o.parentID = o2.id AND o2.parentID = o3.id AND o.orgType = 'CAMPUS' ");
		
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) && StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			sql.append(" AND o2.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like :oOrgLevel ");
			params.put("oOrgLevel", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like :oOrgLevel" + i);
				params.put("oOrgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			sql.append(")");
		}
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o2.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o.id ");
		}
		sql.append(" ORDER BY SUM(dataInfo.realTotalAmount) DESC ");
		
		/**月度计划指标 开始*/
		String startDate = basicOperationQueryVo.getStartDate();
		String endDate = basicOperationQueryVo.getEndDate();
		Map<String, String> month_rate_map = null;
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			if (DateTools.getDate(startDate).compareTo(DateTools.getFirstDayOfMonth(startDate)) == 0
					&& DateTools.getDate(endDate).compareTo(DateTools.getLastDayOfMonth(startDate)) == 0) {
				String year = startDate.substring(0, 4);
				String month = startDate.substring(5, 7);
				String yearId = dataDictDao.getDataDictIdByName(year, DataDictCategory.YEAR);
				String monthValue = CommonUtil.getMonthTypeValue(Integer.parseInt(month));
				Map<String, Object> monthParams = new HashMap<String, Object>();
				StringBuffer month_rate_sql = new StringBuffer();
				month_rate_sql.append(" SELECT GOAL_ID, TARGET_VALUE FROM PLAN_MANAGEMENT where 1=1 ");
				month_rate_sql.append(" AND YEAR_ID = :yearId AND MONTH_ID = :monthValue AND TIME_TYPE = 'MONTH' and TARGET_TYPE='DAT0000000253' ");
				monthParams.put("yearId", yearId);
				monthParams.put("monthValue", monthValue);
				if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
					month_rate_sql.append(" AND GOAL_TYPE = 'BRENCH' ");
				} else {
					month_rate_sql.append(" AND GOAL_TYPE = 'CAMPUS' ");
				}
				List<Map<Object, Object>> month_rate_list=super.findMapBySql(month_rate_sql.toString(), monthParams);
				month_rate_map = new HashMap<String, String>();

				for(Map campus:month_rate_list){
					if(campus.get("TARGET_VALUE")!=null){
						month_rate_map.put( campus.get("GOAL_ID").toString(), campus.get("TARGET_VALUE").toString());
					}
				}
			}
		}

		
		/**营收排名 开始*/
		StringBuffer sql_sort = new StringBuffer();
		Map<String, Object> sortParams = new HashMap<String, Object>();
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql_sort.append(" SELECT CONCAT(org_brench.id, '', '') as brenchId FROM organization org_brench ");
			sql_sort.append(" left JOIN organization org_campus ON org_campus.parentID = org_brench.id ");
			sql_sort.append("  LEFT JOIN  account_charge_records acr ON acr.BL_CAMPUS_ID = org_campus.id ");
			sql_sort.append(" 				WHERE 1=1 ");
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql_sort.append(" 				AND acr.TRANSACTION_TIME >= :startDate ");
				sortParams.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql_sort.append(" 				AND acr.TRANSACTION_TIME <= :endDate ");
				sortParams.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
			}
			sql_sort.append(" AND org_campus.orgType='CAMPUS' ");
			sql_sort.append(" AND (acr.CHARGE_TYPE = 'IS_NORMAL_INCOME' OR acr.CHARGE_TYPE = 'NORMAL') and PAY_TYPE = 'REAL' ");
			sql_sort.append(" GROUP BY  org_brench.id ORDER BY sum(acr.AMOUNT) DESC ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql_sort.append(" SELECT CONCAT(org_campus.id, '', '') as campusId FROM organization org_campus ");
			sql_sort.append("  LEFT JOIN  account_charge_records acr ON acr.BL_CAMPUS_ID = org_campus.id ");
			sql_sort.append(" 				WHERE 1=1 ");
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql_sort.append(" 				AND acr.TRANSACTION_TIME >= :startDate ");
				sortParams.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql_sort.append(" 				AND acr.TRANSACTION_TIME <= :endDate ");
				sortParams.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
			}
			sql_sort.append(" AND org_campus.orgType='CAMPUS' ");
			sql_sort.append(" AND (acr.CHARGE_TYPE = 'IS_NORMAL_INCOME' OR acr.CHARGE_TYPE = 'NORMAL') and PAY_TYPE = 'REAL' ");
			sql_sort.append(" GROUP BY  org_campus.id ORDER BY sum(acr.AMOUNT) DESC ");
		}
		List<Map<Object, Object>> sortDate =super.findMapBySql(sql_sort.toString(), sortParams);
		/**营收排名 */
		
		Map<String, String> sortMap = new HashMap<String, String>();
		List<Map<Object, Object>> targetDatas = super.findMapBySql(sql.toString(), params);
		List<Map> returnDatas = new ArrayList<Map>();
		Map<String, Map> targetMap = new HashMap<String, Map>();
		
		int firstResult = dp.getPageNo()*dp.getPageSize();
		int lastResult =  firstResult + dp.getPageSize();	
		int count = 0;
		int sort = 0;
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			for (Map map : sortDate) {
				sort++;
				sortMap.put((String) map.get("brenchId"), "" + sort);
			}
			for (Map map : targetDatas) {
				map.put("sort", sortMap.get((String) map.get("brenchId"))==null?"-":sortMap.get((String) map.get("brenchId")));
				if (month_rate_map != null) {
					String brenchId = (String) map.get("brenchId");
					if (null != month_rate_map) {
						map.put("target_value", month_rate_map.get(brenchId)==null?0:month_rate_map.get(brenchId));
					}
				}
			}
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			for (Map map : sortDate) {
				sort++;
				sortMap.put((String) map.get("campusId"), "" + sort);
			}
			
			for (Map map : targetDatas) {
				map.put("sort", sortMap.get((String) map.get("campusId"))==null?"-":sortMap.get((String) map.get("campusId")));
				String campusId = (String) map.get("campusId");
				if (null != month_rate_map) {
					map.put("target_value", month_rate_map.get(campusId)==null?0:month_rate_map.get(campusId));
				}
			}
		}
		dp.setDatas(targetDatas);
		dp.setRowCount(targetDatas.size());
		return dp;
	}

	@Override
	public DataPackage getIncomingBrenchForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> rankParams = new HashMap<String, Object>();
		StringBuffer sql =new StringBuffer();
		StringBuffer sqlRank =new StringBuffer();
		sqlRank.append(" select brench_id brenchId from income_count_brench bre left join organization o on o.id=bre.brench_id where 1=1");
		
		sql.append(" 	select ");
		sql.append(" 	bre.brench_id brenchId,o.name brenchName,");
		sql.append(" 	ifnull(sum(oneOnoneRealAmount),0) as oneOnoneRealAmount,");
		sql.append(" 	ifnull(sum(oneOnonePromotionAmount),0) as oneOnonePromotionAmount,");
		sql.append(" 	ifnull(sum(oneOnOneQuantity),0) as oneOnOneQuantity,");
		sql.append(" 	ifnull(sum(smallClassRealAmount),0) as smallClassRealAmount,");
		sql.append(" 	ifnull(sum(smallClassPromotionAmount),0) as smallClassPromotionAmount,");
		sql.append(" 	ifnull(sum(smallClassQuantity),0) as smallClassQuantity,");
		sql.append(" 	ifnull(sum(escClassRealAmount),0) as escClassRealAmount,");
		sql.append(" 	ifnull(sum(escClassPromotionAmount),0) as escClassPromotionAmount,");
		sql.append(" 	ifnull(sum(othersRealAmount),0) as othersRealAmount,");
		sql.append(" 	ifnull(sum(othersPromotionAmount),0) as othersPromotionAmount,");
		sql.append(" 	ifnull(sum(otmRealAmount),0) as otmRealAmount,");
		sql.append(" 	ifnull(sum(otmPromotionAmount),0) as otmPromotionAmount,");
		sql.append(" 	ifnull(sum(otmQuantity),0) as otmQuantity,");
		sql.append(" 	ifnull(sum(lectureRealAmount),0) as lectureRealAmount,");
		sql.append(" 	ifnull(sum(lecturePromotionAmount),0) as lecturePromotionAmount,");
		sql.append(" 	ifnull(sum(isNormalRealAmount),0) as isNormalRealAmount,");
		sql.append(" 	ifnull(sum(isNormalPromotionAmount),0) as isNormalPromotionAmount,");
		sql.append(" 	ifnull(sum(realTotalAmount),0) as realTotalAmount,  ");

		sql.append(" 	ifnull(sum(twoTeacherRealAmount),0) as twoTeacherRealAmount,  ");
		sql.append(" 	ifnull(sum(twoTeacherPromotionAmount),0) as twoTeacherPromotionAmount,  ");
		sql.append(" 	ifnull(sum(twoTeacherQuantity),0) as twoTeacherQuantity , ");
		sql.append(" 	ifnull(sum(liveRealAmount),0) as liveRealAmount,  ");
		sql.append(" 	ifnull(sum(livePromotionAmount),0) as livePromotionAmount,  ");
		sql.append(" 	ifnull(sum(liveQuantity),0) as liveQuantity  ");
		sql.append(" from income_count_brench bre");
		sql.append(" left join organization o on o.id=bre.brench_id");
		sql.append(" where 1=1");
		
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND bre.count_date >= :startDate ");
			sqlRank.append(" AND bre.count_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
			rankParams.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND bre.count_date <= :endDate ");
			sqlRank.append(" AND bre.count_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
			rankParams.put("endDate", basicOperationQueryVo.getEndDate());
		}
		
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like :orgLevel ");
			params.put("orgLevel", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like :orgLevel" + i);
				params.put("orgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			sql.append(")");
		}
		sql.append(" group by o.id ");
		sql.append(" ORDER BY SUM(bre.realTotalAmount) DESC ");
		sqlRank.append(" group by o.id ");
		sqlRank.append(" ORDER BY SUM(bre.realTotalAmount) DESC ");
		List<Map<Object, Object>> list = this.findMapBySql(sql.toString(), params);
		List<Map<Object, Object>> listRank = this.findMapBySql(sqlRank.toString(), rankParams);
		
		Map<String,Integer> rankMap=new HashMap<String,Integer>();
		Integer i=1;
		for (Map map : listRank) {
			String brenchId=map.get("brenchId") != null ? map.get("brenchId").toString() : "";
			rankMap.put(brenchId, i);
			i++;
		}
		/**月度计划指标 开始*/
		Map month_rate_map=getOrganizationTargetValue(basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000253", "BRENCH");
		for (Map map : list) {
			String brenchId=map.get("brenchId") != null ? map.get("brenchId").toString() : "";
			if(month_rate_map!=null)
			map.put("target_value", month_rate_map.get(brenchId)==null?0:month_rate_map.get(brenchId));
			if(rankMap!=null)
			map.put("sort", rankMap.get(brenchId));
		}
		dp.setDatas(list);
		dp.setPageSize(list.size());
		return dp;
	}
	
	public Map getOrganizationTargetValue(String startDate,String endDate,String targetType,String campusType){
		Map<String, String> month_rate_map = null;
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			if (DateTools.getDate(startDate).compareTo(DateTools.getFirstDayOfMonth(startDate)) == 0
					&& DateTools.getDate(endDate).compareTo(DateTools.getLastDayOfMonth(startDate)) == 0) {
				String year = startDate.substring(0, 4);
				String month = startDate.substring(5, 7);
				String yearId = dataDictDao.getDataDictIdByName(year, DataDictCategory.YEAR);
				String monthValue = CommonUtil.getMonthTypeValue(Integer.parseInt(month));
				Map<String, Object> params = new HashMap<String, Object>();
				StringBuffer month_rate_sql = new StringBuffer();
				month_rate_sql.append(" SELECT GOAL_ID, TARGET_VALUE FROM PLAN_MANAGEMENT where 1=1 ");
				month_rate_sql.append(" AND YEAR_ID = :yearId AND MONTH_ID = :monthValue AND TIME_TYPE = 'MONTH' and TARGET_TYPE = :targetType ");
				month_rate_sql.append(" AND GOAL_TYPE = :campusType ");
				params.put("yearId", yearId);
				params.put("monthValue", monthValue);
				params.put("targetType", targetType);
				params.put("campusType", campusType);
				List<Map<Object, Object>> month_rate_list=super.findMapBySql(month_rate_sql.toString(), params);
				month_rate_map = new HashMap<String, String>();

				for(Map campus:month_rate_list){
					if(campus.get("TARGET_VALUE")!=null){
						month_rate_map.put( campus.get("GOAL_ID").toString(), campus.get("TARGET_VALUE").toString());
					}
				}
			}
		}
		
		return month_rate_map;
	}
	
	@Override
	public DataPackage getIncomingCampusForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> rankParams = new HashMap<String, Object>();
		StringBuffer sql =new StringBuffer();
		StringBuffer sqlRank =new StringBuffer();
		sqlRank.append(" select campus_id campusId from income_count_campus bre left join organization o on o.id=bre.campus_id where 1=1");
		
		sql.append(" 	select ");
		sql.append(" 	bre.campus_id campusId,o.name campusName,");
		sql.append(" 	ifnull(sum(oneOnoneRealAmount),0) as oneOnoneRealAmount,");
		sql.append(" 	ifnull(sum(oneOnonePromotionAmount),0) as oneOnonePromotionAmount,");
		sql.append(" 	ifnull(sum(oneOnOneQuantity),0) as oneOnOneQuantity,");
		sql.append(" 	ifnull(sum(smallClassRealAmount),0) as smallClassRealAmount,");
		sql.append(" 	ifnull(sum(smallClassPromotionAmount),0) as smallClassPromotionAmount,");
		sql.append(" 	ifnull(sum(smallClassQuantity),0) as smallClassQuantity,");
		sql.append(" 	ifnull(sum(escClassRealAmount),0) as escClassRealAmount,");
		sql.append(" 	ifnull(sum(escClassPromotionAmount),0) as escClassPromotionAmount,");
		sql.append(" 	ifnull(sum(othersRealAmount),0) as othersRealAmount,");
		sql.append(" 	ifnull(sum(othersPromotionAmount),0) as othersPromotionAmount,");
		sql.append(" 	ifnull(sum(otmRealAmount),0) as otmRealAmount,");
		sql.append(" 	ifnull(sum(otmPromotionAmount),0) as otmPromotionAmount,");
		sql.append(" 	ifnull(sum(otmQuantity),0) as otmQuantity,");
		sql.append(" 	ifnull(sum(lectureRealAmount),0) as lectureRealAmount,");
		sql.append(" 	ifnull(sum(lecturePromotionAmount),0) as lecturePromotionAmount,");
		sql.append(" 	ifnull(sum(isNormalRealAmount),0) as isNormalRealAmount,");
		sql.append(" 	ifnull(sum(isNormalPromotionAmount),0) as isNormalPromotionAmount,");
		sql.append(" 	ifnull(sum(realTotalAmount),0) as realTotalAmount,  ");

		sql.append(" 	ifnull(sum(twoTeacherRealAmount),0) as twoTeacherRealAmount,  ");
		sql.append(" 	ifnull(sum(twoTeacherPromotionAmount),0) as twoTeacherPromotionAmount,  ");
		sql.append(" 	ifnull(sum(twoTeacherQuantity),0) as twoTeacherQuantity , ");
		sql.append(" 	ifnull(sum(liveRealAmount),0) as liveRealAmount,  ");
		sql.append(" 	ifnull(sum(livePromotionAmount),0) as livePromotionAmount,  ");
		sql.append(" 	ifnull(sum(liveQuantity),0) as liveQuantity  ");

		sql.append(" from income_count_campus bre");
		sql.append(" left join organization o on o.id=bre.campus_id");
		sql.append(" where 1=1");
		
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND bre.count_date >= :startDate ");
			sqlRank.append(" AND bre.count_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
			rankParams.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND bre.count_date <= :endDate ");
			sqlRank.append(" AND bre.count_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
			rankParams.put("endDate", basicOperationQueryVo.getEndDate());
		}
		
		if (StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			sql.append(" AND o.parentId = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like :orgLevel ");
			params.put("orgLevel", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like :orgLevel" + i);
				params.put("orgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			sql.append(")");
		}
		
		sql.append(" group by o.id ");
		sql.append(" ORDER BY SUM(bre.realTotalAmount) DESC ");
		sqlRank.append(" group by o.id ");
		sqlRank.append(" ORDER BY SUM(bre.realTotalAmount) DESC ");
		
		List<Map<Object, Object>> list = this.findMapBySql(sql.toString(), params);
		List<Map<Object, Object>> listRank = this.findMapBySql(sqlRank.toString(), rankParams);
		
		Map<String,Integer> rankMap=new HashMap<String,Integer>();
		Integer i=1;
		for (Map map : listRank) {
			rankMap.put(map.get("campusId").toString(), i);
			i++;
		}
		/**月度计划指标 开始*/
		Map month_rate_map=getOrganizationTargetValue(basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000253", "CAMPUS");
		for (Map map : list) {
			String brenchId=map.get("campusId").toString();
			if(month_rate_map!=null)
			map.put("target_value", month_rate_map.get(brenchId)==null?0:month_rate_map.get(brenchId));
			if(rankMap!=null)
			map.put("sort", rankMap.get(brenchId));
		}
		dp.setDatas(list);
		dp.setPageSize(list.size());
		return dp;
	}


	/**
	 * 小班产品统计
	 *
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	@Override
	public DataPackage getMiniClassPeopleAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId,'' as userId,'' as productId, ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId,'' as userId,'' as productId, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.PRODUCT_ID, '_', (select name from product where id = f.PRODUCT_ID)) as productId,'' as userId,");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId,CONCAT(f.PRODUCT_ID, '_', (select name from product where id = f.PRODUCT_ID)) as productId,CONCAT(f.STUDY_MANAGER_ID, '_', (select name from user where user_id = f.STUDY_MANAGER_ID)) as userId, ");
		}
		sql.append(" 	sum(f.count) as count ");
		sql.append(" FROM ODS_DAY_SMALL_CLASS_PEOPLE f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND f.GROUP_ID='"+userService.getBelongGrounpByUserId(userService.getCurrentLoginUser().getUserId()).getId()+"'");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" and f.PRODUCT_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
//        sql.append(roleQLConfigService.getValueResult("小班产品统计","sql"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.PRODUCT_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.PRODUCT_ID, f.STUDY_MANAGER_ID ");
		}

		if (StringUtils.isNotBlank(dp.getSord()) && StringUtils.isNotBlank(dp.getSidx())) {
			sql.append(" order by ").append(dp.getSidx()).append(" ").append(dp.getSord());
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 课时
	 * 小班考勤分析
	 */
	@Override
	public DataPackage getMiniClassKaoqinTongji(BasicOperationQueryVo vo, DataPackage dp,String miniClassTypeId,
			  User currentLoginUser, Organization currentLoginUserCampus) {
		boolean blCampusIdWhere = true;
		if (StringUtils.isNotBlank(vo.getOrganizationId()) || StringUtils.isNotBlank(vo.getTeacherId()) || StringUtils.isNotBlank(vo.getStudyManegerId()) || StringUtils.isNotBlank(vo.getMiniClassName())) {
			vo.setBasicOperationQueryLevelType(BasicOperationQueryLevelType.CAMPUS);
			blCampusIdWhere = false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		}
		sql.append(" ifnull(sum(a.COURSE_HOURS), 0) COURSE_HOURS, ifnull(sum(aq.PEOPLE_QUANTITY), 0) PEOPLE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.attendentQuatity), 0)  ATTENDENT_QUANTITY, ifnull(sum(aq.lateQuatity), 0) LATE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.absentQuatity), 0) ABSENT_QUANTITY, ifnull(sum(aq.supplementQuatity), 0) SUPPLEMENT_QUANTITY, ");
		sql.append(" ifnull(sum(CASE WHEN "
		        + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = a.TEACHER_ID  AND tv.VERSION_DATE <= a.COURSE_DATE) AND TEACHER_ID = a.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
		        + " THEN aq.chargeQuatity ELSE IF(aq.chargeQuatity -1 >= 0, aq.chargeQuatity -1, 0) END), 0) SALARY_QUANTITY, ");
		String loginUserRoleStr = currentLoginUser.getRoleCode();
		sql.append(" ifnull(sum(aq.chargeQuatity), 0) CHARGE_QUANTITY, ifnull(sum(aq.unChargeQuatity), 0) UNCHARGE_QUANTITY ");
		sql.append(" FROM mini_class_course a ");
		sql.append(" INNER JOIN mini_class mc ON a.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN organization o ON mc.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" INNER JOIN user e  ON e.USER_ID = a.TEACHER_ID ");
		sql.append(" INNER JOIN organization org on e.organizationID = org.id ");
		sql.append(" INNER JOIN user f ON f.USER_ID = a.STUDY_MANEGER_ID ");
		sql.append(" LEFT JOIN CLASSROOM_MANAGE crm on crm.ID = mc.CLASSROOM ");
		sql.append(" LEFT JOIN data_dict h ON h.ID = a.GRADE ");
		sql.append(" left join product p on p.ID=mc.PRODUCE_ID ");
		sql.append(" LEFT JOIN (SELECT a.MINI_CLASS_COURSE_ID, SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'CONPELETE' THEN 1 ELSE 0 END) as attendentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'LATE' THEN 1 ELSE 0 END) as lateQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'ABSENT' THEN 1 ELSE 0 END) as absentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.SUPPLEMENT_DATE IS NOT NULL THEN 1 ELSE 0 END) as supplementQuatity, ");
//		sql.append(" if((SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL OR mcsa.ATTENDENT_STATUS = 'LATE' ) THEN 1 ELSE 0 END) - 1)>0,  ");
//		sql.append(" (SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL OR mcsa.ATTENDENT_STATUS = 'LATE' ) THEN 1 ELSE 0 END) - 1), 0) as salaryQuantity, ");
//		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as salaryQuantity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as chargeQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'UNCHARGE' THEN 1 ELSE 0 END) as unChargeQuatity, ");
		sql.append(" COUNT(1) as PEOPLE_QUANTITY ");
		sql.append(" FROM MINI_CLASS_STUDENT_ATTENDENT mcsa ");
		sql.append(" INNER join mini_class_course a on mcsa.MINI_CLASS_COURSE_ID = a.MINI_CLASS_COURSE_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate ");
			params.put("startDate", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate ");
			params.put("endDate", vo.getEndDate());
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		if (StringUtils.isNotBlank(vo.getStudyManegerId())) {
			sql.append(" AND a.STUDY_MANEGER_ID = :studyManagerId ");
			params.put("studyManagerId", vo.getStudyManegerId());
		}
		if (StringUtils.isNotBlank(vo.getMiniClassName())) {
			sql.append(" AND a.MINI_CLASS_NAME like :miniClassName ");
			params.put("miniClassName", "%" + vo.getMiniClassName() + "%");
		}
		sql.append(" GROUP BY a.MINI_CLASS_COURSE_ID) aq on a.MINI_CLASS_COURSE_ID = aq.MINI_CLASS_COURSE_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate ");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate ");
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
		}
		if (StringUtils.isNotBlank(vo.getStudyManegerId())) {
			sql.append(" AND a.STUDY_MANEGER_ID = :studyManagerId ");
		}
		if (StringUtils.isNotBlank(vo.getMiniClassName())) {
			sql.append(" AND a.MINI_CLASS_NAME like :miniClassName ");
		}
		sql.append(" AND a.COURSE_STATUS = 'CHARGED' ");
		if (StringUtils.isNotBlank(vo.getOrganizationId())) {
			sql.append(" AND mc.BL_CAMPUS_ID = :organizationId ");
			params.put("organizationId", vo.getOrganizationId());
		}
		if(StringUtils.isNotBlank(miniClassTypeId)){			
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
				sql.append(" AND (p.CLASS_TYPE_ID = :miniClassType ");
				params.put("miniClassType", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sql.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					params.put("miniClassType" + i, miniClassTypes[i]);
				}
				sql.append(" )");
			}
		}
		if (blCampusIdWhere) {
			if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(vo.getBlCampusId())) {
					sql.append(" AND org_brench.id = :blCampusId ");
					params.put("blCampusId", vo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
				sql.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
				sql.append(" AND e.USER_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
				sql.append(" AND f.USER_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
				sql.append(" AND mc.MINI_CLASS_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			}
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("teacherId","e.USER_ID");
		sqlMap.put("manegerId","f.USER_ID");
		sqlMap.put("courseTeacherId","a.teacher_id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("小班考勤统计","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		}
		sql.append("order by org_brench.id, o.id, mc.MINI_CLASS_ID, e.USER_ID, a.COURSE_DATE, a.COURSE_TIME");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 小时
	 * 小班考勤分析
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param miniClassTypeId
	 * @param currentLoginUser
	 * @param belongCampus
	 * @return
	 */
	@Override
	public DataPackage getMiniClassKaoqinTongjiHour(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, String miniClassTypeId, User currentLoginUser, Organization belongCampus) {
		boolean blCampusIdWhere = true;
		if (StringUtils.isNotBlank(basicOperationQueryVo.getOrganizationId()) || StringUtils.isNotBlank(basicOperationQueryVo.getTeacherId()) || StringUtils.isNotBlank(basicOperationQueryVo.getStudyManegerId()) || StringUtils.isNotBlank(basicOperationQueryVo.getMiniClassName())) {
			basicOperationQueryVo.setBasicOperationQueryLevelType(BasicOperationQueryLevelType.CAMPUS);
			blCampusIdWhere = false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " mc.NAME MINI_CLASS_NAME, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		}
		sql.append(" ifnull(Round( sum(a.COURSE_HOURS*IFNULL(a.COURSE_MINUTES,0)/60),2 ),0) COURSE_HOURS, ifnull(sum(aq.PEOPLE_QUANTITY), 0) PEOPLE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.attendentQuatity), 0)  ATTENDENT_QUANTITY, ifnull(sum(aq.leaveQuatity), 0) LEAVE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.absentQuatity), 0) ABSENT_QUANTITY, ifnull(sum(aq.supplementQuatity), 0) SUPPLEMENT_QUANTITY, ");
		sql.append(" ifnull(sum(CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = a.TEACHER_ID  AND tv.VERSION_DATE <= a.COURSE_DATE) AND TEACHER_ID = a.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN aq.chargeQuatity ELSE IF(aq.chargeQuatity -1 >= 0, aq.chargeQuatity -1, 0) END), 0) SALARY_QUANTITY, ");
		String loginUserRoleStr = currentLoginUser.getRoleCode();
		sql.append(" ifnull(sum(aq.chargeQuatity), 0) CHARGE_QUANTITY, ifnull(sum(aq.unChargeQuatity), 0) UNCHARGE_QUANTITY ");
		sql.append(" FROM mini_class_course a ");
		sql.append(" INNER JOIN mini_class mc ON a.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN organization o ON mc.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" INNER JOIN user e  ON e.USER_ID = a.TEACHER_ID ");
		sql.append(" INNER JOIN organization org on e.organizationID = org.id ");
		sql.append(" INNER JOIN user f ON f.USER_ID = a.STUDY_MANEGER_ID ");
		sql.append(" LEFT JOIN CLASSROOM_MANAGE crm on crm.ID = mc.CLASSROOM ");
		sql.append(" LEFT JOIN data_dict h ON h.ID = a.GRADE ");
		sql.append(" left join product p on p.ID=mc.PRODUCE_ID ");
		sql.append(" LEFT JOIN (SELECT a.MINI_CLASS_COURSE_ID, SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'CONPELETE' THEN 1 ELSE 0 END) as attendentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'LEAVE' THEN 1 ELSE 0 END) as leaveQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'ABSENT' THEN 1 ELSE 0 END) as absentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.SUPPLEMENT_DATE IS NOT NULL THEN 1 ELSE 0 END) as supplementQuatity, ");
//		sql.append(" if((SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL) THEN 1 ELSE 0 END) - 1)>0,  ");
//		sql.append(" (SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL) THEN 1 ELSE 0 END) - 1), 0) as salaryQuantity, ");
//		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as salaryQuantity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as chargeQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'UNCHARGE' THEN 1 ELSE 0 END) as unChargeQuatity, ");
		sql.append(" COUNT(1) as PEOPLE_QUANTITY ");
		sql.append(" FROM MINI_CLASS_STUDENT_ATTENDENT mcsa ");
		sql.append(" INNER join mini_class_course a on mcsa.MINI_CLASS_COURSE_ID = a.MINI_CLASS_COURSE_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
			params.put("teacherId", basicOperationQueryVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStudyManegerId())) {
			sql.append(" AND a.STUDY_MANEGER_ID = :studyManagerId ");
			params.put("studyManagerId", basicOperationQueryVo.getStudyManegerId());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getMiniClassName())) {
			sql.append(" AND a.MINI_CLASS_NAME like :miniClassName ");
			params.put("miniClassName", "%" + basicOperationQueryVo.getMiniClassName() + "%");
		}

		sql.append(" GROUP BY a.MINI_CLASS_COURSE_ID) aq on a.MINI_CLASS_COURSE_ID = aq.MINI_CLASS_COURSE_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate2 ");
			params.put("startDate2", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate2 ");
			params.put("endDate2", basicOperationQueryVo.getEndDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId2 ");
			params.put("teacherId2", basicOperationQueryVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStudyManegerId())) {
			sql.append(" AND a.STUDY_MANEGER_ID = :studyManagerId2 ");
			params.put("studyManagerId2", basicOperationQueryVo.getStudyManegerId());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getMiniClassName())) {
			sql.append(" AND a.MINI_CLASS_NAME like :miniClassName2 ");
			params.put("miniClassName2", "%" + basicOperationQueryVo.getMiniClassName() + "%");
		}
		sql.append(" AND a.COURSE_STATUS = 'CHARGED' ");

		if (StringUtils.isNotBlank(basicOperationQueryVo.getOrganizationId())) {
			sql.append(" AND mc.BL_CAMPUS_ID = :organizationId ");
			params.put("organizationId", basicOperationQueryVo.getOrganizationId());
		}

		if(StringUtils.isNotBlank(miniClassTypeId)){
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
				sql.append(" AND (p.CLASS_TYPE_ID = :miniClassType ");
				params.put("miniClassType", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sql.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					params.put("miniClassType" + i, miniClassTypes[i]);
				}
				sql.append(" )");
			}
		}

		if (blCampusIdWhere) {
			if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
					sql.append(" AND org_brench.id = :blCampusId ");
					params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" AND e.USER_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" AND f.USER_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" AND mc.MINI_CLASS_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("teacherId","e.USER_ID");
		sqlMap.put("manegerId","f.USER_ID");
		sqlMap.put("courseTeacherId","a.teacher_id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("小班考勤统计","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));


		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		}
		sql.append("order by org_brench.id, o.id, mc.MINI_CLASS_ID, e.USER_ID, a.COURSE_DATE, a.COURSE_TIME");

		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 小班扣费分析
	 */
	@Override
	public DataPackage getMiniClassKoufeiTongji(BasicOperationQueryVo vo, DataPackage dp,String miniClassTypeId,
			  User currentLoginUser, Organization currentLoginUserCampus) {
		boolean blCampusIdWhere = true;
		if (StringUtils.isNotBlank(vo.getOrganizationId()) || StringUtils.isNotBlank(vo.getTeacherId()) || StringUtils.isNotBlank(vo.getStudyManegerId()) || StringUtils.isNotBlank(vo.getMiniClassName())) {
			vo.setBasicOperationQueryLevelType( BasicOperationQueryLevelType.CAMPUS);
			blCampusIdWhere = false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' SUBJECTS, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' SUBJECTS, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		}
		sql.append(" ifnull(sum(a.COURSE_HOURS), 0) COURSE_HOURS, ifnull(sum(aq.PEOPLE_QUANTITY), 0) PEOPLE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.attendentQuatity), 0)  ATTENDENT_QUANTITY, ifnull(sum(aq.lateQuatity), 0) LATE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.absentQuatity), 0) ABSENT_QUANTITY, ifnull(sum(aq.supplementQuatity), 0) SUPPLEMENT_QUANTITY, ");
		sql.append(" ifnull(sum(CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = a.TEACHER_ID  AND tv.VERSION_DATE <= a.COURSE_DATE) AND TEACHER_ID = a.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN aq.chargeQuatity ELSE IF(aq.chargeQuatity -1 >= 0, aq.chargeQuatity -1, 0) END), 0) SALARY_QUANTITY, ");
		String loginUserRoleStr = currentLoginUser.getRoleCode();
		sql.append(" ifnull(sum(aq.chargeQuatity), 0) CHARGE_QUANTITY, ifnull(sum(aq.unChargeQuatity), 0) UNCHARGE_QUANTITY, ");
		sql.append(" sum(acr.totalAmount) as CHARGE_AMOUNT, ");
		sql.append(" sum(acr_real.totalAmount) as REAL_CHARGE_AMOUNT ");
		sql.append(" FROM mini_class_course a ");
		sql.append(" LEFT JOIN (select mcr.MINI_CLASS_COURSE_ID,sum(amount) as totalAmount from account_charge_records mcr WHERE ");
		sql.append("  mcr.CHARGE_TYPE='NORMAL'  and mcr.PRODUCT_TYPE='SMALL_CLASS' and mcr.CHARGE_PAY_TYPE='CHARGE' and mcr.IS_WASHED='FALSE' ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME >= '" + vo.getStartDate() + " 00:00:00' ");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME <= '" + vo.getEndDate() + " 23:59:59'");
		}
		sql.append(" GROUP BY mcr.MINI_CLASS_COURSE_ID ) acr on a.MINI_CLASS_COURSE_ID = acr.MINI_CLASS_COURSE_ID");

		//实际扣费-start
		sql.append(" LEFT JOIN (select mcr.MINI_CLASS_COURSE_ID,sum(amount) as totalAmount, sum(QUANTITY) as course_hours from account_charge_records mcr WHERE ");
		sql.append("  mcr.CHARGE_TYPE='NORMAL'  and mcr.PRODUCT_TYPE='SMALL_CLASS' and mcr.CHARGE_PAY_TYPE='CHARGE' and mcr.IS_WASHED='FALSE' and mcr.PAY_TYPE='REAL' ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME >= '" + vo.getStartDate() + " 00:00:00' ");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME <= '" + vo.getEndDate() + " 23:59:59'");
		}
		sql.append(" GROUP BY mcr.MINI_CLASS_COURSE_ID ) acr_real on a.MINI_CLASS_COURSE_ID = acr_real.MINI_CLASS_COURSE_ID");
		//实际扣费-end

		sql.append(" INNER JOIN mini_class mc ON a.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN organization o ON mc.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" INNER JOIN user e  ON e.USER_ID = a.TEACHER_ID ");
        // 取人事部门的组织架构 start
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=e.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		//取人事部门的组织架构 end

		sql.append(" INNER JOIN user f ON f.USER_ID = a.STUDY_MANEGER_ID ");
		sql.append(" LEFT JOIN CLASSROOM_MANAGE crm on crm.ID = mc.CLASSROOM ");
		sql.append(" LEFT JOIN data_dict h ON h.ID = a.GRADE ");
		sql.append(" LEFT JOIN data_dict sub ON sub.id=a.`SUBJECT` ");
		sql.append(" left join product p on p.ID=mc.PRODUCE_ID ");
		sql.append(" left join data_dict classType on classType.id = p.CLASS_TYPE_ID ");
		sql.append(" LEFT JOIN (SELECT a.MINI_CLASS_COURSE_ID, SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'CONPELETE' THEN 1 ELSE 0 END) as attendentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'LATE' THEN 1 ELSE 0 END) as lateQuatity, "); //迟到
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'ABSENT' THEN 1 ELSE 0 END) as absentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.SUPPLEMENT_DATE IS NOT NULL THEN 1 ELSE 0 END) as supplementQuatity, ");
//		sql.append(" if((SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL OR mcsa.ATTENDENT_STATUS = 'LATE' ) THEN 1 ELSE 0 END) - 1)>0,  ");
//		sql.append(" (SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL  OR mcsa.ATTENDENT_STATUS = 'LATE' ) THEN 1 ELSE 0 END) - 1), 0) as salaryQuantity, ");
//		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as salaryQuantity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as chargeQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'UNCHARGE' THEN 1 ELSE 0 END) as unChargeQuatity, ");
		sql.append(" COUNT(1) as PEOPLE_QUANTITY ");
		sql.append(" FROM MINI_CLASS_STUDENT_ATTENDENT mcsa ");
		sql.append(" INNER join mini_class_course a on mcsa.MINI_CLASS_COURSE_ID = a.MINI_CLASS_COURSE_ID ");		
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate ");
			params.put("startDate", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate ");
			params.put("endDate", vo.getEndDate());
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		if (StringUtils.isNotBlank(vo.getStudyManegerId())) {
			sql.append(" AND a.STUDY_MANEGER_ID = :studyManagerId ");
			params.put("studyManagerId", vo.getStudyManegerId());
		}
		if (StringUtils.isNotBlank(vo.getMiniClassName())) {
			sql.append(" AND a.MINI_CLASS_NAME like :miniClassName ");
			params.put("miniClassName", "%" + vo.getMiniClassName() + "%");
		}
		
		sql.append(" GROUP BY a.MINI_CLASS_COURSE_ID) aq on a.MINI_CLASS_COURSE_ID = aq.MINI_CLASS_COURSE_ID ");
		StringBuffer sqlWhere=new StringBuffer();
		sqlWhere.append(" WHERE 1=1 ");
		//主职位
		sqlWhere.append(" and udj.isMajorRole = 0");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sqlWhere.append(" AND a.COURSE_DATE >= :startDate2 ");
			params.put("startDate2", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sqlWhere.append(" AND a.COURSE_DATE <= :endDate2 ");
			params.put("endDate2", vo.getEndDate());
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sqlWhere.append(" AND a.TEACHER_ID = :teacherId2 ");
			params.put("teacherId2", vo.getTeacherId());
		}
		if (StringUtils.isNotBlank(vo.getStudyManegerId())) {
			sqlWhere.append(" AND a.STUDY_MANEGER_ID = :studyManagerId2 ");
			params.put("studyManagerId2", vo.getStudyManegerId());
		}
		if (StringUtils.isNotBlank(vo.getMiniClassName())) {
			sqlWhere.append(" AND a.MINI_CLASS_NAME like :miniClassName2 ");
			params.put("miniClassName2", "%" + vo.getMiniClassName() + "%");
		}		
		sqlWhere.append(" AND a.COURSE_STATUS = 'CHARGED' ");
		
		if (StringUtils.isNotBlank(vo.getOrganizationId())) {
			sqlWhere.append(" AND mc.BL_CAMPUS_ID = :organizationId ");
			params.put("organizationId", vo.getOrganizationId());
		}
		
		if(StringUtils.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sqlWhere.append("AND ( a.subject = :subject ");
				params.put("subject", subjects[0]);
				for (int i = 1; i < subjects.length; i++) {
					sqlWhere.append(" or a.subject = :subject" + i);
					params.put("subject" + i, subjects[i]);
				}
				sqlWhere.append(" )");
			}
		}
		
		if(StringUtils.isNotBlank(miniClassTypeId)){			
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
				sqlWhere.append(" AND (p.CLASS_TYPE_ID = :miniClassType ");
				params.put("miniClassType", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sqlWhere.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					params.put("miniClassType" + i, miniClassTypes[i]);
				}
				sqlWhere.append(" )");
			}
		}
		
		if (blCampusIdWhere) {
			if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(vo.getBlCampusId())) {
					sqlWhere.append(" AND org_brench.id = :blCampusId ");
					params.put("blCampusId", vo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND e.USER_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND f.USER_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND mc.MINI_CLASS_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			}
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("teacherId","e.USER_ID");
		sqlMap.put("manegerId","f.USER_ID");
		sqlMap.put("courseTeacherId","a.teacher_id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("小班考勤统计","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		
		StringBuffer sqlGroupBy=new StringBuffer();
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		}
		sql.append(sqlWhere);
		sql.append(sqlGroupBy);
		sql.append("order by org_brench.id, o.id, mc.MINI_CLASS_ID, e.USER_ID, a.COURSE_DATE, a.COURSE_TIME");
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * (小时)
	 * 查询小班扣费统计
	 *
	 * @param vo
	 * @param dp
	 * @param miniClassTypeId
	 * @param currentLoginUser
	 * @param belongCampus
	 * @return
	 */
	@Override
	public DataPackage getMiniClassKoufeiTongjiHour(BasicOperationQueryVo vo, DataPackage dp, String miniClassTypeId, User currentLoginUser, Organization belongCampus) {
		boolean blCampusIdWhere = true;
		if (StringUtils.isNotBlank(vo.getOrganizationId()) || StringUtils.isNotBlank(vo.getTeacherId()) || StringUtils.isNotBlank(vo.getStudyManegerId()) || StringUtils.isNotBlank(vo.getMiniClassName())) {
			vo.setBasicOperationQueryLevelType( BasicOperationQueryLevelType.CAMPUS);
			blCampusIdWhere = false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");

		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' SUBJECTS, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' MINI_CLASS_NAME, '' CLASS_ROOM, '' STUDY_MANEGER, '' GRADE, '' SUBJECTS, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " CONCAT(mc.MINI_CLASS_ID,'_',mc.NAME) MINI_CLASS_NAME, classType.name CLASS_TYPE_NAME, mc.PEOPLE_QUANTITY PLAN_PEOPLE, crm.CLASS_ROOM  CLASS_ROOM, CONCAT(f.USER_ID,'_',f.NAME) STUDY_MANEGER,org1.NAME STUDY_MANEGER_DEPT_NAME, h.NAME GRADE, sub.`NAME` SUBJECTS, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		}
		sql.append(" ifnull(Round( sum(a.COURSE_HOURS*IFNULL(a.COURSE_MINUTES,0)/60),2 ),0) COURSE_HOURS, ifnull(sum(aq.PEOPLE_QUANTITY), 0) PEOPLE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.attendentQuatity), 0)  ATTENDENT_QUANTITY, ifnull(sum(aq.lateQuatity), 0) LATE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.absentQuatity), 0) ABSENT_QUANTITY, ifnull(sum(aq.supplementQuatity), 0) SUPPLEMENT_QUANTITY, ");
		sql.append(" ifnull(sum(CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = a.TEACHER_ID  AND tv.VERSION_DATE <= a.COURSE_DATE) AND TEACHER_ID = a.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN aq.chargeQuatity ELSE IF(aq.chargeQuatity -1 >= 0, aq.chargeQuatity -1, 0) END), 0) SALARY_QUANTITY, ");
		sql.append(" ifnull(sum(aq.chargeQuatity), 0) CHARGE_QUANTITY, ifnull(sum(aq.unChargeQuatity), 0) UNCHARGE_QUANTITY, ");
		sql.append(" sum(acr.totalAmount) as CHARGE_AMOUNT, ");
		sql.append(" sum(acr_real.totalAmount) as REAL_CHARGE_AMOUNT ");
		sql.append(" FROM mini_class_course a ");
		sql.append(" LEFT JOIN (select mcr.MINI_CLASS_COURSE_ID,sum(amount) as totalAmount from account_charge_records mcr WHERE ");
		sql.append("  mcr.CHARGE_TYPE='NORMAL'  and mcr.PRODUCT_TYPE='SMALL_CLASS'  and mcr.CHARGE_PAY_TYPE='CHARGE' and mcr.IS_WASHED='FALSE' ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME >= '" + vo.getStartDate() + " 00:00:00' ");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME <= '" + vo.getEndDate() + " 23:59:59'");
		}
		sql.append(" GROUP BY mcr.MINI_CLASS_COURSE_ID ) acr on a.MINI_CLASS_COURSE_ID = acr.MINI_CLASS_COURSE_ID");

		//实际扣费-start
		sql.append(" LEFT JOIN (select mcr.MINI_CLASS_COURSE_ID,sum(amount) as totalAmount, sum(QUANTITY) as course_hours from account_charge_records mcr WHERE ");
		sql.append("  mcr.CHARGE_TYPE='NORMAL'  and mcr.PRODUCT_TYPE='SMALL_CLASS' and mcr.CHARGE_PAY_TYPE='CHARGE' and mcr.IS_WASHED='FALSE' and mcr.PAY_TYPE='REAL' ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME >= '" + vo.getStartDate() + " 00:00:00' ");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND mcr.TRANSACTION_TIME <= '" + vo.getEndDate() + " 23:59:59'");
		}
		sql.append(" GROUP BY mcr.MINI_CLASS_COURSE_ID ) acr_real on a.MINI_CLASS_COURSE_ID = acr_real.MINI_CLASS_COURSE_ID");
		//实际扣费-end

		sql.append(" INNER JOIN mini_class mc ON a.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN organization o ON mc.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" INNER JOIN user e  ON e.USER_ID = a.TEACHER_ID ");
//		sql.append(" INNER JOIN organization org on e.organizationID = org.id ");

		// 取人事部门的组织架构 start
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=e.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		//取人事部门的组织架构 end

		sql.append(" INNER JOIN user f ON f.USER_ID = a.STUDY_MANEGER_ID ");
		sql.append(" LEFT JOIN CLASSROOM_MANAGE crm on crm.ID = mc.CLASSROOM ");
		sql.append(" LEFT JOIN data_dict h ON h.ID = a.GRADE ");
		sql.append(" LEFT JOIN data_dict sub ON sub.id=a.`SUBJECT` ");
		sql.append(" left join product p on p.ID=mc.PRODUCE_ID ");
		sql.append(" left join data_dict classType on classType.id = p.CLASS_TYPE_ID ");
		sql.append(" LEFT JOIN (SELECT a.MINI_CLASS_COURSE_ID, SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'CONPELETE' THEN 1 ELSE 0 END) as attendentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'LATE' THEN 1 ELSE 0 END) as lateQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.ATTENDENT_STATUS = 'ABSENT' THEN 1 ELSE 0 END) as absentQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.SUPPLEMENT_DATE IS NOT NULL THEN 1 ELSE 0 END) as supplementQuatity, ");
//		sql.append(" if((SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL OR mcsa.ATTENDENT_STATUS = 'LATE' ) THEN 1 ELSE 0 END) - 1)>0,  ");
//		sql.append(" (SUM(CASE WHEN (mcsa.ATTENDENT_STATUS = 'CONPELETE' OR mcsa.SUPPLEMENT_DATE IS NOT NULL OR mcsa.ATTENDENT_STATUS = 'LATE' ) THEN 1 ELSE 0 END) - 1), 0) as salaryQuantity, ");
//		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as salaryQuantity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as chargeQuatity, ");
		sql.append(" SUM(CASE WHEN mcsa.CHARGE_STATUS = 'UNCHARGE' THEN 1 ELSE 0 END) as unChargeQuatity, ");
		sql.append(" COUNT(1) as PEOPLE_QUANTITY ");
		sql.append(" FROM MINI_CLASS_STUDENT_ATTENDENT mcsa ");
		sql.append(" INNER join mini_class_course a on mcsa.MINI_CLASS_COURSE_ID = a.MINI_CLASS_COURSE_ID ");
		sql.append(" WHERE 1=1 ");

		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate ");
			params.put("startDate", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate ");
			params.put("endDate", vo.getEndDate());
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		if (StringUtils.isNotBlank(vo.getStudyManegerId())) {
			sql.append(" AND a.STUDY_MANEGER_ID = :studyManagerId ");
			params.put("studyManagerId", vo.getStudyManegerId());
		}
		if (StringUtils.isNotBlank(vo.getMiniClassName())) {
			sql.append(" AND a.MINI_CLASS_NAME like :miniClassName ");
			params.put("miniClassName", "%" + vo.getMiniClassName() + "%");
		}
		sql.append(" GROUP BY a.MINI_CLASS_COURSE_ID) aq on a.MINI_CLASS_COURSE_ID = aq.MINI_CLASS_COURSE_ID ");
		StringBuffer sqlWhere=new StringBuffer();
		sqlWhere.append(" WHERE 1=1 ");
		//主职位
		sqlWhere.append(" and udj.isMajorRole = 0");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sqlWhere.append(" AND a.COURSE_DATE >= :startDate2 ");
			params.put("startDate2", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sqlWhere.append(" AND a.COURSE_DATE <= :endDate2 ");
			params.put("endDate2", vo.getEndDate());
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sqlWhere.append(" AND a.TEACHER_ID = :teacherId2 ");
			params.put("teacherId2", vo.getTeacherId());
		}
		if (StringUtils.isNotBlank(vo.getStudyManegerId())) {
			sqlWhere.append(" AND a.STUDY_MANEGER_ID = :studyManagerId2 ");
			params.put("studyManagerId2", vo.getStudyManegerId());
		}
		if (StringUtils.isNotBlank(vo.getMiniClassName())) {
			sqlWhere.append(" AND a.MINI_CLASS_NAME like :miniClassName2 ");
			params.put("miniClassName2", "%" + vo.getMiniClassName() + "%");
		}
		
		sqlWhere.append(" AND a.COURSE_STATUS = 'CHARGED' ");
		

		if (StringUtils.isNotBlank(vo.getOrganizationId())) {
			sqlWhere.append(" AND mc.BL_CAMPUS_ID = :organizationId ");
			params.put("organizationId", vo.getOrganizationId());
		}
		
		if(StringUtils.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sqlWhere.append("AND ( a.subject = :subject ");
				params.put("subject", subjects[0]);
				for (int i = 1; i < subjects.length; i++) {
					sqlWhere.append(" or a.subject = :subject" + i);
					params.put("subject" + i, subjects[i]);
				}
				sqlWhere.append(" )");
			}
		}
		
		if(StringUtils.isNotBlank(miniClassTypeId)){
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
				sqlWhere.append(" AND (p.CLASS_TYPE_ID = :miniClassType ");
				params.put("miniClassType", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sqlWhere.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					params.put("miniClassType" + i, miniClassTypes[i]);
				}
				sqlWhere.append(" )");
			}
		}

		if (blCampusIdWhere) {
			if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(vo.getBlCampusId())) {
					sqlWhere.append(" AND org_brench.id = :blCampusId ");
					params.put("blCampusId", vo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND e.USER_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND f.USER_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND mc.MINI_CLASS_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			}
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("teacherId","e.USER_ID");
		sqlMap.put("manegerId","f.USER_ID");
		sqlMap.put("courseTeacherId","a.teacher_id");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("小班考勤统计","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		StringBuffer sqlGroupBy=new StringBuffer();
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY mc.MINI_CLASS_ID, a.MINI_CLASS_COURSE_ID ");
		}
		sql.append(sqlWhere);
		sql.append(sqlGroupBy);
		sql.append("order by org_brench.id, o.id, mc.MINI_CLASS_ID, e.USER_ID, a.COURSE_DATE, a.COURSE_TIME");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 每天分配课时数
	 */
	public DataPackage getOsdMoneyArrangeRecord(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId,  '' as userId  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.SIGN_STAFF_ID, '_', (select u.name from user u where u.user_id = f.SIGN_STAFF_ID)) as userId ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.SIGN_STAFF_ID, '_', (select u.name from user u where u.USER_ID = f.SIGN_STAFF_ID)) as userId, CONCAT(f.STUDENT_ID, '_', (select s.name from student s where s.ID = f.STUDENT_ID)) as stuId ");
		}
		sql.append(" ,sum(f.ONE_ON_ONE_PAID_AMOUNT) as oneOnOnePaidAmount ");
		sql.append(" ,sum(f.ONE_ON_ONE_PAID_HOUR) as oneOnOnePaidHour ");
		sql.append(" ,sum(f.ONE_ON_ONE_PROMOTION_HOUR) as oneOnOnePromotionHour ");
		sql.append(" ,sum(f.ONE_ON_ONE_TOTAL_HOUR) as oneOnOneTotalHour ");
		sql.append(" ,sum(f.SMALL_PAID_AMOUNT) as smallPaidAmount ");
		sql.append(" ,sum(f.SMALL_PAID_HOUR) as smallPaidHour ");
		sql.append(" FROM OSD_MONEY_ARRANGE_RECORD f");

		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("每天分配课时数统计","sql","f.CAMPUS_ID"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID='"+basicOperationQueryVo.getBlCampusId()+"' ");
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID='"+basicOperationQueryVo.getBlCampusId()+"' ");
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.SIGN_STAFF_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.SIGN_STAFF_ID='"+basicOperationQueryVo.getBlCampusId()+"' ");
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.SIGN_STAFF_ID, f.STUDENT_ID ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 业绩（校区）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getContractBonusOrganization(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		sql.append("	CONCAT(ref.GROUP_ID,'_',(SELECT  o.name FROM organization o WHERE  o.id = ref.GROUP_ID)) AS groupId, ");
		sql.append("    CONCAT(ref.BRANCH_ID,'_',(SELECT o.name FROM organization o WHERE o.id = ref.BRANCH_ID)) AS brenchId, ");
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
			sql.append("    CONCAT(ref.CAMPUS_ID,'_',(SELECT o.name FROM organization o WHERE o.id = ref.CAMPUS_ID)) AS campusId, ");
		}
		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("    CONCAT(ref.CAMPUS_ID,'_',(SELECT o.name FROM organization o WHERE o.id = ref.CAMPUS_ID)) AS campusId, ");
			sql.append("    CONCAT(fch.fund_campus_id,'_',(SELECT o.name FROM organization o WHERE o.id = fch.fund_campus_id)) AS bonusCampusId, ");
			sql.append("    CONCAT(fch.STUDENT_ID,'_', (SELECT s.name FROM student s WHERE s.id =fch.STUDENT_ID)) AS studentId, ");
			sql.append("    (case when c.SIGN_STAFF_ID is not null then CONCAT(c.SIGN_STAFF_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID))"
					+ " else CONCAT(fch.CHARGE_USER_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID)) end) AS signStaffId,  ");
			sql.append("	f.product_type AS productType, ");
		}
		sql.append("	SUM(CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS campusAmount_new, ");
		sql.append("    SUM(CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS campusAmount_re, ");
		sql.append("	SUM(CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER')  and c.contract_type <> 'LIVE_CONTRACT' THEN f.amount ELSE 0 END) as campusAmount, ");
		sql.append("	SUM(CASE WHEN f.product_type='LIVE' AND fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) as onlineAmount, ");
		sql.append("	SUM(CASE WHEN (f.product_type is null or f.product_type <> 'LIVE') AND fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) as lineAmount, ");
		sql.append("	SUM(CASE WHEN fch.CHANNEL ='REFUND_MONEY' and (f.product_type is null or f.product_type <> 'LIVE')  THEN f.amount ELSE 0 END) as refundAmount,  ");
		sql.append("    c.BL_CAMPUS_ID,c.STUDENT_ID,c.SIGN_STAFF_ID,c.CONTRACT_TYPE ");
		sql.append("	FROM income_distribution  f  ");
		sql.append("	INNER JOIN (select DISTINCT GROUP_ID,BRANCH_ID,CAMPUS_ID from REF_USER_ORG ) ref ON f.organizationId=ref.CAMPUS_ID ");
		sql.append("	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID  ");
		sql.append("	left JOIN contract c ON fch.CONTRACT_ID=c.ID  ");
		sql.append("    left join organization o on o.id=ref.CAMPUS_ID ");
		sql.append("    left join organization o2 on o2.id=c.bl_campus_id ");
		sql.append("	where f.base_bonus_type='CAMPUS'  ");
		sql.append("    and  fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' )  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(basicOperationQueryVo.getProductType()!=null){
			sql.append(" AND f.product_type = :productType ");
			params.put("productType", basicOperationQueryVo.getProductType());
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ref.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ref.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("业绩（校区）","sql","ref.CAMPUS_ID"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by ref.GROUP_ID, ref.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by ref.GROUP_ID, ref.BRANCH_ID, ref.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by ref.GROUP_ID, ref.BRANCH_ID, ref.CAMPUS_ID, c.BL_CAMPUS_ID,fch.STUDENT_ID,c.SIGN_STAFF_ID,f.product_type ");
		}
		sql.append(" order by c.SIGN_STAFF_ID,fch.STUDENT_ID,fch.TRANSACTION_TIME DESC");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 业绩（校区）
	 * @param basicOperationQueryVo
	 * @param dp 
	 * @return
	 */
	public DataPackage getContractBonusOrganization_1849(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) { 
		long time1 = System.currentTimeMillis();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		sql.append(" CONCAT(tt.GROUP_ID,'_',(SELECT o.name FROM  organization o  WHERE o.id = tt.GROUP_ID)) AS groupId, ");
		sql.append(" CONCAT( tt.BRANCH_ID, '_',(SELECT  o.name  FROM organization o WHERE o.id = tt.BRANCH_ID)) AS brenchId, ");
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
			sql.append(" CONCAT(tt.CAMPUS_ID,'_',(SELECT o.name FROM organization o WHERE o.id = tt.CAMPUS_ID)) AS campusId, ");
		}
		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(tt.CAMPUS_ID,'_',(SELECT o.name FROM organization o WHERE o.id = tt.CAMPUS_ID)) AS campusId, ");
			sql.append(" CONCAT(tt.fund_campus_id,'_',(SELECT o.name FROM organization o WHERE o.id = tt.fund_campus_id)) AS bonusCampusId, ");
			sql.append(" CONCAT(tt.STUDENT_ID,'_', (SELECT s.name FROM student s WHERE s.id =tt.STUDENT_ID)) AS studentId, ");
			sql.append(" tt.signStaffName,tt.product_type AS productType,  ");
		}
		sql.append(" SUM( CASE WHEN tt.CONTRACT_TYPE = 'NEW_CONTRACT'  AND tt.CHANNEL IN ( 'CASH', 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER' )  THEN tt.amount  ELSE 0  END ) AS campusAmount_new, ");
		sql.append(" SUM( CASE WHEN tt.CONTRACT_TYPE = 'RE_CONTRACT'  AND tt.CHANNEL IN ('CASH','POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN tt.amount ELSE 0  END ) AS campusAmount_re, ");
		sql.append(" SUM(CASE WHEN tt.CHANNEL = 'REFUND_MONEY'  AND (tt.product_type IS NULL  OR tt.product_type <> 'LIVE' )  THEN tt.amount  ELSE 0  END ) AS refundAmount, ");
		sql.append(" SUM( CASE WHEN tt.contact_type = 'NEW'  AND tt.finance_type = 'INCOME' THEN tt.amount ELSE 0  END ) AS liveAmount_income_new, ");
		sql.append(" SUM( CASE WHEN tt.contact_type = 'RENEW'  AND tt.finance_type = 'INCOME' THEN tt.amount ELSE 0  END ) AS liveAmount_income_renew, ");
		sql.append(" SUM( CASE WHEN tt.contact_type = 'NEW'  AND tt.finance_type = 'REFUND' THEN tt.amount ELSE 0  END ) AS liveAmount_refund_new, ");
		sql.append(" SUM( CASE WHEN tt.contact_type = 'RENEW'  AND tt.finance_type = 'REFUND' THEN tt.amount ELSE 0  END ) AS liveAmount_refund_renew ");
		
		sql.append(" from ( ");
		
		if(basicOperationQueryVo.getProductType()==null||
				 basicOperationQueryVo.getProductType() != ProductType.LIVE){

			sql.append(" select ref.GROUP_ID,ref.BRANCH_ID,ref.CAMPUS_ID,fch.fund_campus_id,fch.STUDENT_ID, ");
			sql.append(" (CASE WHEN c.SIGN_STAFF_ID IS NOT NULL THEN (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID)" + 
					"	 ELSE (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID) END) AS signStaffName,   ");
			sql.append(" c.CONTRACT_TYPE,fch.CHANNEL,f.amount,f.product_type,'' contact_type,'' finance_type,fch.TRANSACTION_TIME ");
			sql.append("	FROM income_distribution  f  ");
			sql.append("	INNER JOIN (select DISTINCT GROUP_ID,BRANCH_ID,CAMPUS_ID from REF_USER_ORG ) ref ON f.organizationId=ref.CAMPUS_ID ");
			sql.append("	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID  ");
			sql.append("	left JOIN contract c ON fch.CONTRACT_ID=c.ID  ");
			sql.append("    left join organization o on o.id=ref.CAMPUS_ID ");
			sql.append("    left join organization o2 on o2.id=c.bl_campus_id ");
			sql.append("	where f.base_bonus_type='CAMPUS'  ");
			sql.append("    and  fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' )  ");
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
				params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
				params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
			}
			if(basicOperationQueryVo.getProductType()!=null){
				sql.append(" AND f.product_type = :productType ");
				params.put("productType", basicOperationQueryVo.getProductType());
			}else {
				sql.append(" AND ( f.product_type is null  or f.product_type <> 'LIVE' ) ");
			}
			sql.append(roleQLConfigService.getAppendSqlByAllOrg("业绩（校区）","sql","ref.CAMPUS_ID"));
		}
		
		if(basicOperationQueryVo.getProductType()==null){
			sql.append(" UNION ALL");
		}
		
		if(basicOperationQueryVo.getProductType()==null || basicOperationQueryVo.getProductType() == ProductType.LIVE){
			sql.append(" SELECT ref.GROUP_ID,ref.BRANCH_ID,ref.CAMPUS_ID,pd.order_campusId fund_campus_id,pd.student_id,pd.user_name signStaffName, ");
			sql.append(" 'LIVE_CONTRACT' CONTRACT_TYPE,''  CHANNEL,pd.campus_achievement amount,'LIVE' product_type,pd.contact_type,pd.finance_type,pd.payment_date TRANSACTION_TIME ");
			sql.append(" FROM live_payment_record pd  ");
			sql.append(" INNER JOIN  (SELECT DISTINCT  GROUP_ID, BRANCH_ID, CAMPUS_ID  FROM REF_USER_ORG) ref  ON pd.order_campusId = ref.CAMPUS_ID ");
			sql.append(" LEFT JOIN organization o ON o.id = pd.order_campusId ");
			sql.append(" where 1=1 ");
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql.append(" AND pd.payment_date >= :startDateLive ");
				params.put("startDateLive", basicOperationQueryVo.getStartDate());
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql.append(" AND pd.payment_date <= :endDateLive ");
				params.put("endDateLive", basicOperationQueryVo.getEndDate());
			}
			
			sql.append(roleQLConfigService.getAppendSqlByAllOrg("直播校区归属","sql","o.id"));
		}
		
		sql.append(" ) tt ");
		sql.append(" where 1=1 ");
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and tt.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and tt.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by tt.GROUP_ID, tt.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by tt.GROUP_ID, tt.BRANCH_ID, tt.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by tt.GROUP_ID, tt.BRANCH_ID, tt.CAMPUS_ID, tt.fund_campus_id,tt.STUDENT_ID,tt.signStaffName,tt.product_type  ");
		}
		sql.append(" ORDER BY tt.signStaffName,tt.STUDENT_ID,tt.TRANSACTION_TIME DESC  ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		
		long time2 = System.currentTimeMillis();
		System.out.println("getContractBonusOrganization_1849-time:"+(time2-time1));
		return dp;
	}


	/**
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getContractBonusBranch(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		String brenchId = basicOperationQueryVo.getBlCampusId();
		sql.append("select true AS isBrench, ");
		sql.append("	CONCAT(o.id,'_',o.name) AS groupId,     ");
		sql.append("    CONCAT(o2.id,'_',o2.name ) AS brenchId,  ");
		sql.append("    CONCAT(o2.id,'_',o2.name ) AS campusId,  ");

		sql.append("    CONCAT(fch.fund_campus_id,'_', (SELECT o.name FROM organization o WHERE o.id = fch.fund_campus_id)) AS bonusCampusId, ");
		sql.append("    CONCAT(fch.STUDENT_ID,'_', (SELECT s.name FROM student s WHERE s.id =fch.STUDENT_ID)) AS studentId, ");
		sql.append("    (case when c.SIGN_STAFF_ID is not null then CONCAT(c.SIGN_STAFF_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID))"
				+ " else CONCAT(fch.CHARGE_USER_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID)) end) AS signStaffId,  ");
		if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
			if (basicOperationQueryVo.getContractType()!=null && basicOperationQueryVo.getContractType().equals(ContractType.REFUND)) {
				sql.append(" '' as contractType, '' as productType,");
			}else{
				sql.append(" c.CONTRACT_TYPE as contractType, '' as productType,");
			}
		}else{
			sql.append(" f.product_type as productType,'' as contractType,");
		}
		sql.append("	CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_new, ");
		sql.append("    CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_re, ");
		sql.append("	CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END as bonusAmount, ");
		sql.append("	CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END as refundAmount,  ");
		sql.append("    c.BL_CAMPUS_ID,c.STUDENT_ID,c.SIGN_STAFF_ID,c.CONTRACT_TYPE ");

		sql.append("    FROM income_distribution f ");
//		sql.append("	INNER JOIN REF_USER_ORG  ref ON f.bonus_staff_id=ref.USER_ID ");
		sql.append("	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID  ");
		sql.append("	left JOIN contract c ON fch.CONTRACT_ID=c.ID  ");
		sql.append("    left join organization o2 on o2.id=f.organizationId "); //
		sql.append("    left join organization o on o.id=o2.parentID ");
		sql.append("	where    f.sub_bonus_type= 'USER_BRANCH' ");
		sql.append("    AND  f.organizationId ='"+brenchId+"'");
		sql.append("    AND  fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER')  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(basicOperationQueryVo.getProductType()!=null){
			sql.append(" AND f.product_type = :productType ");
			params.put("productType", basicOperationQueryVo.getProductType());
		}

		if (basicOperationQueryVo.getContractType()!=null) {
			if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
				sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
			}else {
				sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
				params.put("contractType", basicOperationQueryVo.getContractType());
			}
		}
		sql.append(" order by fch.TRANSACTION_TIME DESC");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getContractBonusCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		String campusId = basicOperationQueryVo.getBlCampusId();
		sql.append("select true AS isCampus, ");
		sql.append("    CONCAT(o3.id,'_',o3.name ) AS groupId  , ");
		sql.append("    CONCAT(o2.id,'_',o2.name ) AS brenchId , ");
		sql.append("    CONCAT(o.id,'_',o.name )   AS campusId , ");
		sql.append("	CONCAT(o.id,'_',o.name )   AS bonusStaffId , ");
		sql.append("    CONCAT(fch.fund_campus_id,'_', (SELECT o.name FROM organization o WHERE o.id = fch.fund_campus_id)) AS bonusCampusId, ");
		sql.append("    CONCAT(fch.STUDENT_ID,'_', (SELECT s.name FROM student s WHERE s.id =fch.STUDENT_ID)) AS studentId, ");
		sql.append("    (case when c.SIGN_STAFF_ID is not null then CONCAT(c.SIGN_STAFF_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID))"
				+ " else CONCAT(fch.CHARGE_USER_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID)) end) AS signStaffId,  ");
		if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
			if (basicOperationQueryVo.getContractType()!=null && basicOperationQueryVo.getContractType().equals(ContractType.REFUND)) {
				sql.append(" '' as contractType, '' as productType,");
			}else{
				sql.append(" c.CONTRACT_TYPE as contractType, '' as productType,");
			}
		}else{
			sql.append(" f.product_type as productType,'' as contractType,");
		}
		sql.append("	CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_new, ");
		sql.append("    CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END AS bonusAmount_re, ");
		sql.append("	CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END as bonusAmount, ");
		sql.append("	CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END as refundAmount,  ");
		sql.append("    c.BL_CAMPUS_ID,c.STUDENT_ID,c.SIGN_STAFF_ID,c.CONTRACT_TYPE ");
		sql.append("    FROM income_distribution f ");
//		sql.append("	INNER JOIN REF_USER_ORG  ref ON f.bonus_staff_id=ref.USER_ID ");
		sql.append("	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID  ");
		sql.append("	left JOIN contract c ON fch.CONTRACT_ID=c.ID  ");
		sql.append("    left join  organization o on o.id=f.organizationId ");
		sql.append("    left join  organization o2 on o2.id = o.parentID  "); //
		sql.append("    left JOIN  organization o3 on o3.id = o2.parentID ");
		sql.append("	where    f.sub_bonus_type= 'USER_CAMPUS' ");
		sql.append("    AND  f.organizationId ='"+campusId+"'");
		sql.append("    AND  fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER')  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(basicOperationQueryVo.getProductType()!=null){
			sql.append(" AND f.product_type = :productType ");
			params.put("productType", basicOperationQueryVo.getProductType());
		}

		if (basicOperationQueryVo.getContractType()!=null) {
			if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
				sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
			}else {
				sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
				params.put("contractType", basicOperationQueryVo.getContractType());
			}
		}
		sql.append(" order by fch.TRANSACTION_TIME DESC");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}



	/**
	 * 业绩（签单人）
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getContractBonusStaff(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		sql.append("	CONCAT(ref.GROUP_ID,'_',(SELECT  o.name FROM organization o WHERE  o.id = ref.GROUP_ID)) AS groupId, ");
		sql.append("    CONCAT(ref.BRANCH_ID,'_',(SELECT o.name FROM organization o WHERE o.id = ref.BRANCH_ID)) AS brenchId, ");
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())){
			sql.append("    CONCAT(ref.CAMPUS_ID,'_',(SELECT o.name FROM organization o WHERE o.id = ref.CAMPUS_ID)) AS campusId, ");
		}
		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("    CONCAT(ref.CAMPUS_ID,'_',(SELECT o.name FROM organization o WHERE o.id = ref.CAMPUS_ID)) AS campusId, ");
			sql.append("    CONCAT(f.bonus_staff_id,'_',(SELECT u.name FROM `user` u WHERE u.USER_ID =f.bonus_staff_id)) AS bonusStaffId, ");
			sql.append("    CONCAT('',(SELECT ( select name from organization where id=u.ORGANIZATIONID) FROM `user` u WHERE u.USER_ID = f.BONUS_STAFF_ID)) AS signStaffOrganization,");
		}
		if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("    CONCAT(ref.CAMPUS_ID,'_',(SELECT o.name FROM organization o WHERE o.id = ref.CAMPUS_ID)) AS campusId, ");
			sql.append("    CONCAT(f.bonus_staff_id,'_',(SELECT u.name FROM `user` u WHERE u.USER_ID =f.bonus_staff_id)) AS bonusStaffId, ");
			sql.append("    CONCAT('',(SELECT ( select name from organization where id=u.ORGANIZATIONID) FROM `user` u WHERE u.USER_ID = f.bonus_staff_id)) AS signStaffOrganization,");
			sql.append("    CONCAT(fch.fund_campus_id,'_', (SELECT o.name FROM organization o WHERE o.id = fch.fund_campus_id)) AS bonusCampusId, ");
			sql.append("    CONCAT(fch.STUDENT_ID,'_', (SELECT s.name FROM student s WHERE s.id =fch.STUDENT_ID)) AS studentId, ");
			sql.append("    (case when c.SIGN_STAFF_ID is not null then CONCAT(c.SIGN_STAFF_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID))"
					+ " else CONCAT(fch.CHARGE_USER_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID)) end) AS signStaffId,  ");
			
			if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
				if (basicOperationQueryVo.getContractType()!=null && basicOperationQueryVo.getContractType().equals(ContractType.REFUND)) {
					sql.append(" '' as contractType, '' as productType,");
				}else{
					sql.append(" c.CONTRACT_TYPE as contractType, '' as productType,");
				}
			}else{
				sql.append(" f.product_type as productType,'' as contractType,");
			}
		}
		sql.append("	SUM(CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS bonusAmount_new, ");
		sql.append("    SUM(CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT' and fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) AS bonusAmount_re, ");
		sql.append("	SUM(CASE WHEN fch.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN f.amount ELSE 0 END) as bonusAmount, ");
		sql.append("	SUM(CASE WHEN fch.CHANNEL ='REFUND_MONEY' THEN f.amount ELSE 0 END) as refundAmount,  ");
		sql.append("    c.BL_CAMPUS_ID,c.STUDENT_ID,c.SIGN_STAFF_ID,c.CONTRACT_TYPE ");
//		sql.append("	FROM CONTRACT_BONUS  f  ");
		sql.append("    FROM income_distribution f ");
		sql.append("	INNER JOIN REF_USER_ORG  ref ON f.bonus_staff_id=ref.USER_ID ");
		sql.append("	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID  ");
		sql.append("	left JOIN contract c ON fch.CONTRACT_ID=c.ID  ");
		sql.append("    left join organization o on o.id=ref.CAMPUS_ID ");
		sql.append("    left join organization o2 on o2.id=c.bl_campus_id ");
		sql.append("	where f.BONUS_STAFF_ID is not null and f.BONUS_STAFF_ID<>'' ");
		sql.append("    and  fch.CHANNEL in('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER')  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fch.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fch.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(basicOperationQueryVo.getProductType()!=null){
			sql.append(" AND f.product_type = :productType ");
			params.put("productType", basicOperationQueryVo.getProductType());
		}
		if (basicOperationQueryVo.getContractType()!=null) {
			if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
				sql.append(" and fch.CHANNEL ='REFUND_MONEY'");
			}else {
				sql.append(" and fch.CHANNEL <>'REFUND_MONEY' and c.CONTRACT_TYPE = :contractType ");
				params.put("contractType", basicOperationQueryVo.getContractType());
			}
		}
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ref.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ref.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ref.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("业绩（签单人）","sql","ref.CAMPUS_ID"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by ref.GROUP_ID, ref.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by ref.GROUP_ID, ref.BRANCH_ID, ref.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by ref.GROUP_ID, ref.BRANCH_ID, ref.CAMPUS_ID,f.BONUS_STAFF_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("    group by ref.GROUP_ID , ref.BRANCH_ID , ref.CAMPUS_ID , f.bonus_staff_id , c.BL_CAMPUS_ID , fch.STUDENT_ID , c.SIGN_STAFF_ID  ");
			if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
				sql.append(", c.CONTRACT_TYPE");
			}else{
				sql.append(", f.product_type");
			}
		}
		sql.append(" order by f.bonus_staff_id,fch.TRANSACTION_TIME DESC");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	public DataPackage getContractBonusStaff2(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String flag) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select  '000001_星火集团' as groupId,");
		sql.append(" concat(brenchId,'_',(select name from organization where id = brenchId)) AS brenchId,");
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) && !("isBrench".equals(flag) || "isCampus".equals(flag))) {
			sql.append(" concat(campusId,'_',(select name from organization where id = campusId))  AS campusId,");
		}
		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) && !("isBrench".equals(flag) || "isCampus".equals(flag))) {
			sql.append(" concat(campusId,'_',(select name from organization where id = campusId))  AS campusId,");
			sql.append(" (select name from organization where id = signStaffOrganization)  AS signStaffOrganization,");
			sql.append(" (case when sub_bonus_type='USER_CAMPUS' then concat(bonusStaffId,'_',(select name from organization where id =bonusStaffId)) else  concat(bonusStaffId,'_',(select name from user where user_id =bonusStaffId)) end )  as bonusStaffId,");
		}
		if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) || ("isBrench".equals(flag) || "isCampus".equals(flag))) {
			sql.append(" concat(campusId,'_',(select name from organization where id = campusId))  AS campusId,");
			sql.append(" (select name from organization where id = signStaffOrganization)  AS signStaffOrganization,");
			sql.append(" (case when sub_bonus_type='USER_CAMPUS' then concat(bonusStaffId,'_',(select name from organization where id =bonusStaffId)) else  concat(bonusStaffId,'_',(select name from user where user_id =bonusStaffId)) end )  as bonusStaffId,");
			sql.append(" CONCAT(fund_campus_id,'_', (SELECT o.name FROM organization o WHERE o.id = fund_campus_id)) AS bonusCampusId, ");
			sql.append(" (SELECT s.name FROM student s WHERE s.id =STUDENT_ID) AS studentId, ");
			sql.append(" concat(case when SIGN_STAFF_ID is not null then CONCAT(SIGN_STAFF_ID,'_', (SELECT u.name FROM `user` u WHERE u.USER_ID = SIGN_STAFF_ID)) else '' end) AS signStaffId,");
			if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
				if (basicOperationQueryVo.getContractType()!=null && basicOperationQueryVo.getContractType().equals(ContractType.REFUND)) {
					sql.append(" '' as contractType, '' as productType,");
				}else{
					sql.append(" CONTRACT_TYPE as contractType, '' as productType,");
				}
			}else{
				sql.append(" product_type as productType,'' as contractType,");
			}
		}
		sql.append(" sum(case when  CONTRACT_TYPE = 'NEW_CONTRACT' and CHANNEL<>'REFUND_MONEY' then amount else 0 end) bonusAmount_new,");
		sql.append(" sum(case when  CONTRACT_TYPE = 'RE_CONTRACT' and CHANNEL<>'REFUND_MONEY' then amount else 0 end) bonusAmount_re,");
		sql.append(" sum(case when  CHANNEL<>'REFUND_MONEY' and CONTRACT_TYPE <> 'LIVE_CONTRACT' then amount else 0 end) bonusAmount,");
		sql.append(" sum(case when  CHANNEL='REFUND_MONEY' AND (product_type is null or product_type <> 'LIVE')  then amount else 0 end) refundAmount,");


		sql.append(" sum(case when product_type='LIVE' AND CHANNEL<>'REFUND_MONEY' then amount when product_type='LIVE' AND CHANNEL='REFUND_MONEY' then -amount else 0 end) onlineAmount,");
		sql.append(" sum(case when (product_type is null or product_type <> 'LIVE') AND CHANNEL<>'REFUND_MONEY' then amount when (product_type is null or product_type <> 'LIVE') AND CHANNEL='REFUND_MONEY' then -amount else 0 end) lineAmount,");

		sql.append(" BL_CAMPUS_ID,");
		sql.append(" STUDENT_ID,");
		sql.append(" SIGN_STAFF_ID,");
		sql.append(" sub_bonus_type");
		sql.append(" from (");
		sql.append(" SELECT ");
		sql.append("         `ref`.`GROUP_ID` AS `groupId`,");
		sql.append("         `ref`.`BRANCH_ID` AS `brenchId`,");
		sql.append("         `ref`.`CAMPUS_ID` AS `campusId`,");
		sql.append("         `f`.`bonus_staff_id` AS `bonusStaffId`,");
		sql.append("         `f`.`bonus_staff_campus` AS `signStaffOrganization`,");
		sql.append("         `fch`.`CHANNEL` AS `CHANNEL`,");
		sql.append("         `f`.`amount` AS `amount`,");
		sql.append("         `c`.`BL_CAMPUS_ID` AS `BL_CAMPUS_ID`,");
		sql.append("         `fch`.`STUDENT_ID` AS `STUDENT_ID`,");
		sql.append("         (case when `c`.`SIGN_STAFF_ID` is null then fch.CHARGE_USER_ID else c.SIGN_STAFF_ID END) AS `SIGN_STAFF_ID`,");
		sql.append("         `c`.`CONTRACT_TYPE` AS `CONTRACT_TYPE`,");
		sql.append("         `f`.`sub_bonus_type` AS `sub_bonus_type`, ");
		sql.append("         `fch`.`TRANSACTION_TIME` AS `TRANSACTION_TIME`,");
		sql.append("         `fch`.`fund_campus_id` AS `fund_campus_id`,");
		sql.append("         `f`.`product_type` AS `product_type`,");
		sql.append("         `f`.`id` AS `id`");
		sql.append(" FROM `income_distribution` `f`");
		sql.append(" JOIN `ref_user_org` `ref` ON `f`.`bonus_staff_id` = `ref`.`USER_ID`");
		sql.append(" JOIN `funds_change_history` `fch` ON `f`.`funds_change_id` = `fch`.`ID`");
		sql.append(" LEFT JOIN `contract` `c` ON `fch`.`CONTRACT_ID` = `c`.`ID`");
		sql.append(" LEFT JOIN `organization` `o` ON `o`.`id` = `ref`.`CAMPUS_ID`");
		sql.append(" LEFT JOIN `organization` `o2` ON `o2`.`id` = `c`.`BL_CAMPUS_ID`");
		sql.append(" WHERE `f`.`sub_bonus_type` = 'USER_USER'");
		sql.append(" AND `fch`.`CHANNEL` IN ( 'CASH', 'POS', 'REFUND_MONEY', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') ");
		sql.append( this.getTimeSql(basicOperationQueryVo, params, 0));
		sql.append(" ");
		sql.append(" ");
		sql.append(" ");
		sql.append(" ");
		sql.append("     UNION SELECT ");
		sql.append("         '00001' AS `groupId`,");
		sql.append("         `o2`.`id` AS `brenchId`,");
		sql.append("         `o`.`id` AS `campusId`,");
		sql.append("         `o`.`id` AS `bonusStaffId`,");
		sql.append("         `f`.`bonus_staff_campus` AS `signStaffOrganization`,");
		sql.append("         `fch`.`CHANNEL` AS `CHANNEL`,");
		sql.append("         `f`.`amount` AS `amount`,");
		sql.append("         `c`.`BL_CAMPUS_ID` AS `BL_CAMPUS_ID`,");
		sql.append("         `fch`.`STUDENT_ID` AS `STUDENT_ID`,");
		sql.append("         (case when `c`.`SIGN_STAFF_ID` is null then fch.CHARGE_USER_ID else c.SIGN_STAFF_ID END) AS `SIGN_STAFF_ID`,");
		sql.append("         `c`.`CONTRACT_TYPE` AS `CONTRACT_TYPE`,");
		sql.append("         `f`.`sub_bonus_type` AS `sub_bonus_type`,");
		sql.append("         `fch`.`TRANSACTION_TIME` AS `TRANSACTION_TIME`,");
		sql.append("         `fch`.`fund_campus_id` AS `fund_campus_id`,");
		sql.append("         `f`.`product_type` AS `product_type`,");
		sql.append("         `f`.`id` AS `id`");

		sql.append(" FROM `income_distribution` `f`");
		sql.append(" LEFT JOIN `funds_change_history` `fch` ON `f`.`funds_change_id` = `fch`.`ID`");
		sql.append(" LEFT JOIN `contract` `c` ON `fch`.`CONTRACT_ID` = `c`.`ID`");
		sql.append(" LEFT JOIN `organization` `o` ON `o`.`id` = `f`.`organizationId`");
		sql.append(" LEFT JOIN `organization` `o2` ON `o`.`parentID` = `o2`.`id`");
		sql.append(" WHERE `f`.`sub_bonus_type` = 'USER_CAMPUS'");
		sql.append(" AND `fch`.`CHANNEL` IN ( 'CASH', 'POS', 'REFUND_MONEY', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') ");
		sql.append(this.getTimeSql(basicOperationQueryVo, params, 1));

		sql.append("     UNION SELECT ");
		sql.append("         '00001' AS `groupId`,");
		sql.append("         `o`.`id` AS `brenchId`,");
		sql.append("         `o`.`id` AS `campusId`,");
		sql.append("         NULL AS `bonusStaffId`,");
		sql.append("         `f`.`bonus_staff_campus` AS `signStaffOrganization`,");
		sql.append("         `fch`.`CHANNEL` AS `CHANNEL`,");
		sql.append("         `f`.`amount` AS `amount`,");
		sql.append("         `c`.`BL_CAMPUS_ID` AS `BL_CAMPUS_ID`,");
		sql.append("         `fch`.`STUDENT_ID` AS `STUDENT_ID`,");
		sql.append("         (case when `c`.`SIGN_STAFF_ID` is null then fch.CHARGE_USER_ID else c.SIGN_STAFF_ID END) AS `SIGN_STAFF_ID`,");
		sql.append("         `c`.`CONTRACT_TYPE` AS `CONTRACT_TYPE`,");
		sql.append("         `f`.`sub_bonus_type` AS `sub_bonus_type`,");
		sql.append("         `fch`.`TRANSACTION_TIME` AS `TRANSACTION_TIME`,");
		sql.append("         `fch`.`fund_campus_id` AS `fund_campus_id`,");
		sql.append("         `f`.`product_type` AS `product_type`,");
		sql.append("         `f`.`id` AS `id`");
		sql.append(" FROM `income_distribution` `f`");
		sql.append(" LEFT JOIN `funds_change_history` `fch` ON `f`.`funds_change_id` = `fch`.`ID`");
		sql.append(" LEFT JOIN `contract` `c` ON `fch`.`CONTRACT_ID` = `c`.`ID`");
		sql.append(" LEFT JOIN `organization` `o` ON `o`.`id` = `f`.`organizationId`");
		sql.append(" WHERE `f`.`sub_bonus_type` = 'USER_BRANCH'");
		sql.append(" AND `fch`.`CHANNEL` IN ( 'CASH', 'POS', 'REFUND_MONEY', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER')");
		sql.append(this.getTimeSql(basicOperationQueryVo, params, 2));
		sql.append("             ) f");

		sql.append(" left join organization o on o.id= f.campusId");
		sql.append(" left join organization o2 on o2.id  = f.brenchId");
		sql.append("    where   1=1  ");
		if(basicOperationQueryVo.getProductType()!=null){
			sql.append(" AND product_type = :productType ");
			params.put("productType", basicOperationQueryVo.getProductType());
		}
		if (basicOperationQueryVo.getContractType()!=null) {
			if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
				sql.append(" and CHANNEL ='REFUND_MONEY'");
			}else {
				sql.append(" and CHANNEL <>'REFUND_MONEY' and CONTRACT_TYPE = :contractType ");
				params.put("contractType", basicOperationQueryVo.getContractType());
			}
		}
		if("isBrench".equals(flag) ){
			sql.append(" and sub_bonus_type='USER_BRANCH'");
		}else if ("isCampus".equals(flag)){
			sql.append(" and sub_bonus_type='USER_CAMPUS'");
		}
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and brenchId = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and campusId = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and campusId = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("业绩（签单人）","sql","campusId"));
		if("isBrench".equals(flag) || "isCampus".equals(flag)){
			sql.append(" group by f.id");
		}else {
			if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" group by brenchId ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" group by campusId ");
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" group by bonusStaffId ");
			} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append("    group by bonusStaffId,student_id ");
				if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
					sql.append(", CONTRACT_TYPE");
				}else{
					sql.append(", product_type");
				}
			}
		}
		sql.append(" order by bonusStaffId,TRANSACTION_TIME DESC");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params, 300));
		return dp;
	}

	public DataPackage getContractBonusStaff_1849(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String flag) { 
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select  '000001_星火集团' as groupId,");
		sql.append(" concat(f.brenchId,'_',(select name from organization where id = f.brenchId)) AS brenchId,");
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) && !("isBrench".equals(flag) || "isCampus".equals(flag))) {
			sql.append(" concat(f.campusId,'_',(select name from organization where id = f.campusId))  AS campusId,");
		}
		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) && !("isBrench".equals(flag) || "isCampus".equals(flag))) {
			sql.append(" concat(f.campusId,'_',(select name from organization where id = f.campusId))  AS campusId,");
			sql.append(" (select name from organization where id = f.signStaffOrganization)  AS signStaffOrganization,");
			sql.append(" (case when sub_bonus_type='USER_CAMPUS' then concat(bonusStaffId,'_',(select name from organization where id =bonusStaffId)) else  concat(bonusStaffId,'_',(select name from user where user_id =bonusStaffId)) end )  as bonusStaffId,");
		}
		if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) || ("isBrench".equals(flag) || "isCampus".equals(flag))) {
			sql.append(" concat(f.campusId,'_',(select name from organization where id = f.campusId))  AS campusId,");
			sql.append(" (select name from organization where id = f.signStaffOrganization)  AS signStaffOrganization,");
			sql.append(" (case when sub_bonus_type='USER_CAMPUS' then concat(bonusStaffId,'_',(select name from organization where id =bonusStaffId)) else  concat(bonusStaffId,'_',(select name from user where user_id =bonusStaffId)) end )  as bonusStaffId,");
			sql.append(" CONCAT(f.fund_campus_id,'_', (SELECT o.name FROM organization o WHERE o.id = f.fund_campus_id)) AS bonusCampusId, ");
			sql.append(" (SELECT s.name FROM student s WHERE s.id =f.STUDENT_ID) AS studentId, ");
			sql.append(" f.signStaffName,f.CONTRACT_TYPE as contractType,");
			if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "2".equals(basicOperationQueryVo.getConTypeOrProType())){
				sql.append(" '' as productType,");
			}else{
				sql.append(" product_type as productType,");
			}
		}
		sql.append(" SUM( CASE WHEN f.CONTRACT_TYPE = 'NEW_CONTRACT'  AND f.CHANNEL IN ( 'CASH', 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER' )  THEN f.amount  ELSE 0  END ) AS campusAmount_new,");
		sql.append(" SUM( CASE WHEN f.CONTRACT_TYPE = 'RE_CONTRACT'  AND f.CHANNEL IN ('CASH','POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN f.amount ELSE 0  END ) AS campusAmount_re,");
		sql.append(" SUM(CASE WHEN f.CHANNEL = 'REFUND_MONEY'  AND (f.product_type IS NULL  OR f.product_type <> 'LIVE' )  THEN f.amount  ELSE 0  END ) AS refundAmount,");
		sql.append(" SUM( CASE WHEN f.contact_type = 'NEW'  AND f.finance_type = 'INCOME' THEN f.amount ELSE 0  END ) AS liveAmount_income_new,");
		sql.append(" SUM( CASE WHEN f.contact_type = 'RENEW'  AND f.finance_type = 'INCOME' THEN f.amount ELSE 0  END ) AS liveAmount_income_renew,");
		sql.append(" SUM( CASE WHEN f.contact_type = 'NEW'  AND f.finance_type = 'REFUND' THEN f.amount ELSE 0  END ) AS liveAmount_refund_new,");
		sql.append(" SUM( CASE WHEN f.contact_type = 'RENEW'  AND f.finance_type = 'REFUND' THEN f.amount ELSE 0  END ) AS liveAmount_refund_renew,");

		sql.append(" sub_bonus_type");
		sql.append(" from (");
		
		if(basicOperationQueryVo.getProductType()==null||
				 basicOperationQueryVo.getProductType() != ProductType.LIVE){
			sql.append(" SELECT ");
			sql.append("         `ref`.`GROUP_ID` AS `groupId`,");
			sql.append("         `ref`.`BRANCH_ID` AS `brenchId`,");
			sql.append("         `ref`.`CAMPUS_ID` AS `campusId`,");
			sql.append("         `f`.`bonus_staff_id` AS bonusStaffId,");
			sql.append("         `f`.`bonus_staff_campus` AS `signStaffOrganization`,");
			sql.append("         `fch`.`CHANNEL` AS `CHANNEL`,");
			sql.append("         `f`.`amount` AS `amount`,");
			sql.append("         `fch`.`STUDENT_ID` AS `STUDENT_ID`,");
			sql.append("         (CASE WHEN c.SIGN_STAFF_ID IS NOT NULL THEN (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID)" + 
					"	 ELSE (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID) END) AS signStaffName,");
			sql.append("         `c`.`CONTRACT_TYPE` AS `CONTRACT_TYPE`,");
			sql.append("         `f`.`sub_bonus_type` AS `sub_bonus_type`, ");
			sql.append("         `fch`.`TRANSACTION_TIME` AS `TRANSACTION_TIME`,");
			sql.append("         `fch`.`fund_campus_id` AS `fund_campus_id`,");
			sql.append("         `f`.`product_type` AS `product_type`,");
			sql.append("         `f`.`id` AS `id`,");
			sql.append("         '' contact_type,'' finance_type");
			sql.append(" FROM `income_distribution` `f`");
			sql.append(" JOIN `ref_user_org` `ref` ON `f`.`bonus_staff_id` = `ref`.`USER_ID`");
			sql.append(" JOIN `funds_change_history` `fch` ON `f`.`funds_change_id` = `fch`.`ID`");
			sql.append(" LEFT JOIN `contract` `c` ON `fch`.`CONTRACT_ID` = `c`.`ID`");
			sql.append(" LEFT JOIN `organization` `o` ON `o`.`id` = `ref`.`CAMPUS_ID`");
			sql.append(" LEFT JOIN `organization` `o2` ON `o2`.`id` = `c`.`BL_CAMPUS_ID`");
			sql.append(" WHERE `f`.`sub_bonus_type` = 'USER_USER'");
			sql.append(" AND `fch`.`CHANNEL` IN ( 'CASH', 'POS', 'REFUND_MONEY', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') ");
			if(basicOperationQueryVo.getProductType()!=null){
				sql.append(" AND f.product_type = :productType ");
				params.put("productType", basicOperationQueryVo.getProductType());
			}else {
				sql.append(" AND ( f.product_type is null or f.product_type <> 'LIVE' ) ");
			}
			sql.append( this.getTimeSql(basicOperationQueryVo, params, 0));
			sql.append(" ");
			sql.append(" ");
			sql.append(" ");
			sql.append(" ");
			sql.append("     UNION SELECT ");
			sql.append("         '00001' AS `groupId`,");
			sql.append("         `o2`.`id` AS `brenchId`,");
			sql.append("         `o`.`id` AS `campusId`,");
			sql.append("          o.id AS bonusStaffId,");
			sql.append("         `f`.`bonus_staff_campus` AS `signStaffOrganization`,");
			sql.append("         `fch`.`CHANNEL` AS `CHANNEL`,");
			sql.append("         `f`.`amount` AS `amount`,");
			sql.append("         `fch`.`STUDENT_ID` AS `STUDENT_ID`,");
			sql.append("         (CASE WHEN c.SIGN_STAFF_ID IS NOT NULL THEN (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID)" + 
					"	 ELSE (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID) END) AS signStaffName,");
			sql.append("         `c`.`CONTRACT_TYPE` AS `CONTRACT_TYPE`,");
			sql.append("         `f`.`sub_bonus_type` AS `sub_bonus_type`,");
			sql.append("         `fch`.`TRANSACTION_TIME` AS `TRANSACTION_TIME`,");
			sql.append("         `fch`.`fund_campus_id` AS `fund_campus_id`,");
			sql.append("         `f`.`product_type` AS `product_type`,");
			sql.append("         `f`.`id` AS `id`,");
			sql.append("         '' contact_type,'' finance_type");
			
			sql.append(" FROM `income_distribution` `f`");
			sql.append(" LEFT JOIN `funds_change_history` `fch` ON `f`.`funds_change_id` = `fch`.`ID`");
			sql.append(" LEFT JOIN `contract` `c` ON `fch`.`CONTRACT_ID` = `c`.`ID`");
			sql.append(" LEFT JOIN `organization` `o` ON `o`.`id` = `f`.`organizationId`");
			sql.append(" LEFT JOIN `organization` `o2` ON `o`.`parentID` = `o2`.`id`");
			sql.append(" WHERE `f`.`sub_bonus_type` = 'USER_CAMPUS'");
			sql.append(" AND `fch`.`CHANNEL` IN ( 'CASH', 'POS', 'REFUND_MONEY', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') ");
			if(basicOperationQueryVo.getProductType()!=null){
				sql.append(" AND f.product_type = :productType ");
				params.put("productType", basicOperationQueryVo.getProductType());
			}else {
				sql.append(" AND ( f.product_type is null or f.product_type <> 'LIVE' ) ");
			}
			sql.append(this.getTimeSql(basicOperationQueryVo, params, 1));

			sql.append("     UNION SELECT ");
			sql.append("         '00001' AS `groupId`,");
			sql.append("         `o`.`id` AS `brenchId`,");
			sql.append("         `o`.`id` AS `campusId`,");
			sql.append("         NULL AS `bonusStaffId`,");
			sql.append("         `f`.`bonus_staff_campus` AS `signStaffOrganization`,");
			sql.append("         `fch`.`CHANNEL` AS `CHANNEL`,");
			sql.append("         `f`.`amount` AS `amount`,");
			sql.append("         `fch`.`STUDENT_ID` AS `STUDENT_ID`,");
			sql.append("         (CASE WHEN c.SIGN_STAFF_ID IS NOT NULL THEN (SELECT u.name FROM `user` u WHERE u.USER_ID = c.SIGN_STAFF_ID)" + 
					"	 ELSE (SELECT u.name FROM `user` u WHERE u.USER_ID = fch.CHARGE_USER_ID) END) AS signStaffName,");
			sql.append("         `c`.`CONTRACT_TYPE` AS `CONTRACT_TYPE`,");
			sql.append("         `f`.`sub_bonus_type` AS `sub_bonus_type`,");
			sql.append("         `fch`.`TRANSACTION_TIME` AS `TRANSACTION_TIME`,");
			sql.append("         `fch`.`fund_campus_id` AS `fund_campus_id`,");
			sql.append("         `f`.`product_type` AS `product_type`,");
			sql.append("         `f`.`id` AS `id`,");
			sql.append("         '' contact_type,'' finance_type");
			sql.append(" FROM `income_distribution` `f`");
			sql.append(" LEFT JOIN `funds_change_history` `fch` ON `f`.`funds_change_id` = `fch`.`ID`");
			sql.append(" LEFT JOIN `contract` `c` ON `fch`.`CONTRACT_ID` = `c`.`ID`");
			sql.append(" LEFT JOIN `organization` `o` ON `o`.`id` = `f`.`organizationId`");
			sql.append(" WHERE `f`.`sub_bonus_type` = 'USER_BRANCH'");
			sql.append(" AND `fch`.`CHANNEL` IN ( 'CASH', 'POS', 'REFUND_MONEY', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER')");
			if(basicOperationQueryVo.getProductType()!=null){
				sql.append(" AND f.product_type = :productType ");
				params.put("productType", basicOperationQueryVo.getProductType());
			}else {
				sql.append(" AND ( f.product_type is null or f.product_type <> 'LIVE' ) ");
			}
			sql.append(this.getTimeSql(basicOperationQueryVo, params, 2));
			
		}
		
		if(basicOperationQueryVo.getProductType()==null) {
			sql.append(" UNION ALL ");
		}
		if(basicOperationQueryVo.getProductType()==null || basicOperationQueryVo.getProductType() == ProductType.LIVE){
			sql.append(" select  `ref`.`GROUP_ID` AS `groupId`,");
			sql.append("         `ref`.`BRANCH_ID` AS `brenchId`,");
			sql.append("         `ref`.`CAMPUS_ID` AS `campusId`,");
			//sql.append(" null as bonusStaffId ,");
			sql.append("          (select user_id from user where employee_No = pd.user_employeeNo  limit 1) bonusStaffId,");
			sql.append("          pd.order_campusId signStaffOrganization,");
			sql.append("          ''  CHANNEL,");
			sql.append("          pd.user_achievement amount,");
			sql.append("          pd.STUDENT_ID,");
			sql.append("          pd.user_name signStaffName,");
			sql.append("          'LIVE_CONTRACT' CONTRACT_TYPE,");
			sql.append("          'USER_USER' AS `sub_bonus_type`,");
			sql.append("          pd.payment_date TRANSACTION_TIME,");
			sql.append("          pd.order_campusId fund_campus_id,");
			sql.append("          'LIVE' product_type,");
			sql.append("          '' AS `id` ,");
			sql.append("          pd.contact_type,pd.finance_type");
			sql.append(" FROM live_payment_record pd  ");
			sql.append(" INNER JOIN  (SELECT DISTINCT  GROUP_ID, BRANCH_ID, CAMPUS_ID  FROM REF_USER_ORG) ref  ON pd.order_campusId = ref.CAMPUS_ID ");
			sql.append(" LEFT JOIN organization o ON o.id = pd.order_campusId ");
			sql.append(" where 1=1 ");
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql.append(" AND pd.payment_date >= :startDateLive ");
				params.put("startDateLive", basicOperationQueryVo.getStartDate());
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql.append(" AND pd.payment_date <= :endDateLive ");
				params.put("endDateLive", basicOperationQueryVo.getEndDate());
			}
			//sql.append(roleQLConfigService.getValueResult("直播校区归属","sql"));
		}
		
		sql.append("             ) f");

		sql.append(" left join organization o on o.id= f.campusId");
		sql.append(" left join organization o2 on o2.id  = f.brenchId");
		sql.append("    where   1=1  ");
		
		if (basicOperationQueryVo.getContractType()!=null) {
			if(basicOperationQueryVo.getContractType().equals(ContractType.REFUND)){
				sql.append(" and ( f.CHANNEL ='REFUND_MONEY' )");
			}else if(basicOperationQueryVo.getContractType().equals(ContractType.LIVE_CONTRACT)) {
				sql.append(" and CONTRACT_TYPE = :contractType ");
				params.put("contractType", basicOperationQueryVo.getContractType());
			}else {
				sql.append(" and CHANNEL <>'REFUND_MONEY' and CONTRACT_TYPE = :contractType ");
				params.put("contractType", basicOperationQueryVo.getContractType());
			}
		}
		if("isBrench".equals(flag) ){
			sql.append(" and sub_bonus_type='USER_BRANCH'");
		}else if ("isCampus".equals(flag)){
			sql.append(" and sub_bonus_type='USER_CAMPUS'");
		}
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and brenchId = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and campusId = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and campusId = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("业绩（签单人）","sql","campusId"));
		if("isBrench".equals(flag) || "isCampus".equals(flag)){
			sql.append(" group by f.id");
		}else {
			if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" group by f.brenchId ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" group by f.brenchId,f.campusId ");
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" group by f.brenchId,f.campusId,f.bonusStaffId ");
			} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" group by f.brenchId,f.campusId,f.bonusStaffId,f.STUDENT_ID ");
				if(StringUtils.isNotBlank(basicOperationQueryVo.getConTypeOrProType())&& "1".equals(basicOperationQueryVo.getConTypeOrProType())){
					sql.append(", f.product_type");
				}
			}
		}
		sql.append(" order by f.bonusStaffId,f.STUDENT_ID,f.TRANSACTION_TIME DESC");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params, 300));
		return dp;
	}
	
	/* 课程系列统计
	 * @see com.eduboss.dao.ReportDao#getMiniClassCourseSeries(com.eduboss.domainVo.BasicOperationQueryVo, com.eduboss.dto.DataPackage)
	 */
	public DataPackage getMiniClassCourseSeries(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer totalSql=new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT groupp.NAME as groupName,branch.NAME as branchName,campus.NAME as campusName, ");
		sql.append(" case when p.COURSE_SERIES_ID is null then '其他' else (select name from data_dict where id= p.COURSE_SERIES_ID) end as courseSeriesId,sum(cp.PAID_AMOUNT) as total from");
		sql.append(" organization as campus,organization as branch,organization as groupp ,contract as c left join contract_product cp on c.ID=cp.CONTRACT_ID ");
		sql.append(" left join product p on cp.PRODUCT_ID=p.ID  ");
		sql.append(" where 1=1 and branch.parentID = groupp.id  and c.BL_CAMPUS_ID = campus.id and campus.parentID = branch.id  and cp.TYPE='SMALL_CLASS' ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND cp.CREATE_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND cp.CREATE_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			Organization org = organizationDao.findById(basicOperationQueryVo.getBlCampusId());
			sql.append(" AND c.BL_CAMPUS_ID in ( select id from organization where orgLevel like :orgLevel ");
			params.put("orgLevel", org.getOrgLevel() + "%");
		}

		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (");
			sql.append(" campus.orgLevel like :orgLevel0 ");
			params.put("orgLevel0", org.getOrgLevel() + "%");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or campus.orgLevel like :orgLevel" + i);
				params.put("orgLevel" + i, userOrganizations.get(i).getOrgLevel() + "%");
			}
			sql.append(")");
		}
		sql.append(")");

		sql.append(" GROUP BY groupp.id,branch.id,campus.id,p.COURSE_SERIES_ID ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		List returnList=new ArrayList();
		totalSql.append("select sum(sumAll.total) from ( " + sql.toString() + " ) sumAll ");
		
		SQLQuery q= super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(totalSql.toString());
		super.setParamsIfNotNull(QueryTypeEnum.SQL, params, q);
		Object sum=q.list().get(0);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			map.put("sumAll", sum);
			returnList.add(map);
		}
		dp.setDatas(returnList);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 记录刷新报表次数
	 */
	public void saveProcedureRefreshs(String procedureName){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="select * from PROCEDURE_REFRESHS where PROCEDURE_NAME = :procedureName ";
		params.put("procedureName", procedureName);
		List list=this.findMapBySql(sql, params);
		if(list!=null && list.size()>0){
			sql="update PROCEDURE_REFRESHS set MODIFY_TIME='"+DateTools.getCurrentDateTime()+"',REFRESHS=REFRESHS+1 where PROCEDURE_NAME= :procedureName";
		}else{
			sql="insert into PROCEDURE_REFRESHS(PROCEDURE_NAME,MODIFY_TIME,REFRESHS) values(:procedureName,'"+DateTools.getCurrentDateTime()+"',0)";
		}
		this.excuteSql(sql, params);
	}


	@Override
	public Map<String, Integer> getTodayAndMonthTotal() {
		Map<String,Integer> map= new LinkedHashMap<String, Integer>();
		StringBuffer  sql=new StringBuffer("");
		sql.append(" SELECT ");
		sql.append(" SUM(CASE WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' and (select 1 from contract c where contract_type='NEW_CONTRACT' and c.id=fun.contract_id) THEN fun.transaction_amount WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'WASH' and (select 1 from contract c where contract_type='NEW_CONTRACT' and c.id=fun.contract_id) THEN -fun.transaction_amount ELSE 0 END) AS countPaidCashAmount_new, ");
		sql.append(" SUM(CASE WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' and (select 1 from contract c where contract_type='RE_CONTRACT' and c.id=fun.contract_id) THEN fun.transaction_amount WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'WASH' and (select 1 from contract c where contract_type='RE_CONTRACT' and c.id=fun.contract_id) THEN -fun.transaction_amount ELSE 0 END) AS countPaidCashAmount_re, ");
		sql.append(" sum(CASE WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' then fun.transaction_amount WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'WASH' then -fun.transaction_amount else 0 end) countPaidCashAmount, ");
		sql.append(" 	sum(case when fun.TRANSACTION_TIME like concat(CURDATE(),'%') AND fun.FUNDS_PAY_TYPE = 'RECEIPT' then fun.transaction_amount when fun.TRANSACTION_TIME like concat(CURDATE(),'%') AND fun.FUNDS_PAY_TYPE = 'WASH' then -fun.transaction_amount else 0 end) as today  ");
		sql.append(" from funds_change_history fun  ");
		sql.append(" INNER JOIN organization o  ON fun.fund_campus_id = o.id    ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		sql.append(" left JOIN contract c  ON fun.contract_id = c.id ");
		sql.append(" where 1=1 and c.contract_type <>'LIVE_CONTRACT' ");
		sql.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		sql.append(" AND fun.transaction_time like '"+DateTools.getCurrentDate().substring(0, 7)+"%'");
		sql.append(" and (o2.id='"+userService.getBelongCampus().getId()+"' ");

		sql.append(" or o3.id='"+userService.getBelongCampus().getId()+"' ");
		sql.append(" or o.id='"+userService.getBelongCampus().getId()+"' ");
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append(" or o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
		}
		sql.append(" )");
//	        sql.append(roleQLConfigService.getValueResult("业绩（校区）","sql"));
		System.out.println(sql.toString());
		List list = super.findMapBySql(sql.toString(), new HashMap<String, Object>());
		
		StringBuffer sql2= new StringBuffer("");
		sql2.append(" SELECT ");
		sql2.append(" sum(f.QUANTITY) as consumeHours, ");
		sql2.append(" sum(f.AMOUNT) as consumeAmount, ");
		sql2.append(" sum(case when TRANSACTION_TIME >= concat(CURDATE(),' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(),current_time()) then f.QUANTITY else 0 end) as todayHours, ");
		sql2.append(" sum(case when TRANSACTION_TIME >= concat(CURDATE(),' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(),current_time()) then f.AMOUNT else 0 end) as todayAmount ");
		sql2.append(" FROM account_charge_records f   left join organization o on o.id=f.BL_CAMPUS_ID LEFT JOIN organization d ON d.id = o.parentID   ");
		sql2.append(" WHERE 1=1     ");
		sql2.append(" AND TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql2.append(" AND TRANSACTION_TIME <= '"+DateTools.getCurrentDateTime()+"' ");
		sql2.append("  AND f.CHARGE_TYPE = 'NORMAL' ");
		sql2.append("  AND f.PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' ");
		sql2.append("  AND f.IS_WASHED = 'FALSE' AND F.CHARGE_PAY_TYPE='CHARGE' ");
		
		
		sql2.append(" and ( 1=2 ");
		List<Organization> userOrganizations2= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations2 != null && userOrganizations2.size() > 0){
			for(int i = 0; i < userOrganizations2.size(); i++){
				sql2.append(" or o.orgLevel like '").append(userOrganizations2.get(i).getOrgLevel()).append("%'");
			}
		}
		sql2.append(" )");
		List list2 = super.findMapBySql(sql2.toString(), new HashMap<String, Object>());
		if(list.size()>0){
			map=(Map)list.get(0);
		}
		if(list2.size()>0){
			map.putAll((Map)list2.get(0));
		}
		return map;
	}


	@Override
	public List getTodayAndMonthTotalIncome() {
		Map<String,Integer> map= new LinkedHashMap<String, Integer>();
		StringBuffer  sql=new StringBuffer("");
		
		sql.append(" SELECT ");
		
		sql.append(" o3.name as groupId, o2.name as brenchId, o.name as campusId, ");
		sql.append(" SUM(CASE WHEN  (select 1 from contract c where contract_type='NEW_CONTRACT' and c.id=fun.contract_id) AND fun.FUNDS_PAY_TYPE = 'RECEIPT' THEN fun.transaction_amount WHEN  (select 1 from contract c where contract_type='NEW_CONTRACT' and c.id=fun.contract_id) AND fun.FUNDS_PAY_TYPE = 'WASH' THEN -fun.transaction_amount ELSE 0 END) AS month_new, ");
		sql.append(" SUM(CASE WHEN  (select 1 from contract c where contract_type='RE_CONTRACT' and c.id=fun.contract_id) AND fun.FUNDS_PAY_TYPE = 'RECEIPT' THEN fun.transaction_amount WHEN  (select 1 from contract c where contract_type='RE_CONTRACT' and c.id=fun.contract_id) AND fun.FUNDS_PAY_TYPE = 'WASH' THEN -fun.transaction_amount ELSE 0 END) AS month_re, ");
		sql.append(" sum(case when  fun.FUNDS_PAY_TYPE = 'RECEIPT' then fun.transaction_amount else -fun.transaction_amount end) month, ");
		sql.append(" sum(case when  fun.CONTRACT_TYPE = 'LIVE_CONTRACT' then fun.transaction_amount else 0 end) month_live, ");
		sql.append(" SUM(CASE WHEN fun.TRANSACTION_TIME like concat(CURDATE(),'%') AND fun.FUNDS_PAY_TYPE = 'RECEIPT' and (select 1 from contract c where contract_type='NEW_CONTRACT' and c.id=fun.contract_id) THEN fun.transaction_amount WHEN fun.TRANSACTION_TIME like concat(CURDATE(),'%') AND fun.FUNDS_PAY_TYPE = 'WASH' and (select 1 from contract c where contract_type='NEW_CONTRACT' and c.id=fun.contract_id) THEN -fun.transaction_amount ELSE 0 END) AS today_new, ");
		sql.append(" SUM(CASE WHEN fun.TRANSACTION_TIME like concat(CURDATE(),'%') AND fun.FUNDS_PAY_TYPE = 'RECEIPT' and (select 1 from contract c where contract_type='RE_CONTRACT' and c.id=fun.contract_id) THEN fun.transaction_amount WHEN fun.TRANSACTION_TIME like concat(CURDATE(),'%') AND fun.FUNDS_PAY_TYPE = 'WASH' and (select 1 from contract c where contract_type='RE_CONTRACT' and c.id=fun.contract_id) THEN -fun.transaction_amount ELSE 0 END) AS today_re, ");
		sql.append(" sum(case when fun.TRANSACTION_TIME like concat(CURDATE(),'%') AND fun.CONTRACT_TYPE = 'LIVE_CONTRACT' then fun.transaction_amount else 0 end) today_live, ");
		sql.append(" sum(case when fun.TRANSACTION_TIME like concat(CURDATE(),'%') and fun.FUNDS_PAY_TYPE = 'RECEIPT' then fun.transaction_amount when fun.TRANSACTION_TIME like concat(CURDATE(),'%') and fun.FUNDS_PAY_TYPE = 'WASH' then -fun.transaction_amount else 0 end) as today  ");
		
		sql.append(" from ( ");
		
		sql.append(" select f.contract_id,f.fund_campus_id campus_id,f.FUNDS_PAY_TYPE,f.transaction_amount,f.TRANSACTION_TIME,'' CONTRACT_TYPE ");
		sql.append(" from funds_change_history f  ");
		
		sql.append(" left JOIN contract c  ON f.contract_id = c.id ");
		sql.append(" where 1=1 and c.contract_type <>'LIVE_CONTRACT' ");
		sql.append("  and f.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		sql.append(" AND f.transaction_time like '"+DateTools.getCurrentDate().substring(0, 7)+"%'");
		
		sql.append(" UNION ALL ");
		
		sql.append(" select '' contract_id,ll.order_campusId  campus_id ,'RECEIPT' FUNDS_PAY_TYPE,ll.paid_amount transaction_amount,ll.payment_date TRANSACTION_TIME,'LIVE_CONTRACT' CONTRACT_TYPE ");
		sql.append(" from live_payment_record ll ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND ll.payment_date like '"+DateTools.getCurrentDate().substring(0, 7)+"%'");
		sql.append(" AND ll.finance_type = 'INCOME' AND ll.contact_type = 'NEW' " );
		sql.append(" ) fun ");
		
		sql.append(" INNER JOIN organization o  ON fun.campus_id = o.id    ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		
		sql.append(" and (o2.id='"+userService.getBelongCampus().getId()+"' ");
		sql.append(" or o3.id='"+userService.getBelongCampus().getId()+"' ");
		sql.append(" or o.id='"+userService.getBelongCampus().getId()+"' ");
		
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append(" or o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
		}
		sql.append(" )");
		sql.append(" group by o3.id, o2.id, o.id order by sum(fun.transaction_amount) desc,o2.id ");

		System.out.println(sql.toString());
		List list = this.findMapBySql(sql.toString(), new HashMap<String, Object>());
		return list;
	}


	@Override
	public List getTodayAndMonthTotalComsume() {
		StringBuffer sql2= new StringBuffer("");
		sql2.append(" SELECT ");
		sql2.append(" 	 d.name as brenchId, o.name as campusId, ");
		sql2.append(" sum(f.QUANTITY) as consumeHours, ");
		sql2.append(" sum(f.AMOUNT) as consumeAmount, ");
		sql2.append(" sum(case when TRANSACTION_TIME >= concat(CURDATE(),' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(),current_time()) then f.QUANTITY else 0 end) as todayHours, ");
		sql2.append(" sum(case when TRANSACTION_TIME >= concat(CURDATE(),' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(),current_time()) then f.AMOUNT else 0 end) as todayAmount ");
		sql2.append(" FROM account_charge_records f   left join organization o on o.id=f.BL_CAMPUS_ID LEFT JOIN organization d ON d.id = o.parentID   ");
		sql2.append(" WHERE 1=1     ");
		sql2.append(" AND TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql2.append(" AND TRANSACTION_TIME <= '"+DateTools.getCurrentDateTime()+"' ");
		sql2.append("  AND f.CHARGE_TYPE = 'NORMAL' ");
		sql2.append("  AND f.PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' ");
		sql2.append("  AND f.IS_WASHED = 'FALSE' AND F.CHARGE_PAY_TYPE='CHARGE' ");
		
		sql2.append(" AND ( ");
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql2.append("  o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql2.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
		}
		sql2.append(" )");
		sql2.append(" group by  d.id, o.id order by sum(f.QUANTITY) desc,d.id ");
		
		List list2 = this.findMapBySql(sql2.toString(), new HashMap<String, Object>());
		return list2;
	}


	/**
	 * 手机现金流
	 */
	@Override
	public DataPackage getFinanceAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) { 
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId,   ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId,concat(dataInfo.sign_staff_id,'_',(select name from user where user_id=dataInfo.sign_staff_id)) userId,  ");
		}
		sql.append(" ifnull(SUM(countPaidCashAmount_new),0) AS countPaidCashAmount_new,");
		sql.append(" ifnull(SUM(countPaidCashAmount_re),0) AS countPaidCashAmount_re,");
		sql.append(" ifnull(SUM(countPaidTotalAmount),0) countPaidTotalAmount");
		sql.append(" from organization o2,organization o3 ,organization o  ");
		sql.append(" left join (select  o3.id AS groupId,o2.id AS brenchId,o.id AS  campusId,ct.sign_staff_id,");
		sql.append(" SUM(CASE WHEN ct.CONTRACT_TYPE = 'NEW_CONTRACT' THEN (case when fun.funds_pay_type='RECEIPT' then fun.transaction_amount else -1*fun.transaction_amount end) ELSE 0 END) AS countPaidCashAmount_new, ");
		sql.append(" SUM(CASE WHEN ct.CONTRACT_TYPE = 'RE_CONTRACT' THEN (case when fun.funds_pay_type='RECEIPT' then fun.transaction_amount else -1*fun.transaction_amount end) ELSE 0 END) AS countPaidCashAmount_re, ");
		sql.append(" sum(case when fun.funds_pay_type='RECEIPT' then fun.transaction_amount else -1*fun.transaction_amount end) countPaidTotalAmount ");
		sql.append(" from funds_change_history fun  ");
		sql.append(" inner join contract ct on fun.contract_id = ct.id  ");
		sql.append(" INNER JOIN organization o  ON fun.fund_campus_id = o.id    ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		sql.append(" where 1=1 ");
		sql.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		sql.append("  and o.orgType='CAMPUS' ");
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		
		sql.append(" group by ct.sign_staff_id,o.id) dataInfo ON o.id=dataInfo.campusId ");

		sql.append(" WHERE o.orgType='CAMPUS' and o.parentID = o2.id and  o2.parentID = o3.id  ");
		
		//权限查询所属组织架构下的所有校区信息
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		//要修改的部分
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o2.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id , o2.id ");
		}else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) { 
			sql.append(" group by o3.id , o2.id,o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id , o2.id,o.id,dataInfo.sign_staff_id ");
		}
		
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY  sum(dataInfo.countPaidTotalAmount) DESC,o.orgLevel ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		
		/*********************************************************************************/
			/**按  按实收金额 获取 分公司的排名 开始*/
			Map<String, Object> branchParams = new HashMap<String, Object>();
			StringBuffer sql_branch = new StringBuffer();
			sql_branch.append(" SELECT concat(o2.id,'') as BRANCH_ID from funds_change_history fun ");
			sql_branch.append( " inner join contract ct on fun.contract_id = ct.id  ");
					
			sql_branch.append(" INNER JOIN organization o  ON fun.fund_campus_id = o.id    ");
			sql_branch.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
			sql_branch.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
					
			sql_branch.append(" WHERE fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
	
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql_branch.append(" AND fun.TRANSACTION_TIME >= :startDate ");
				branchParams.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql_branch.append(" AND fun.TRANSACTION_TIME <= :endDate ");
				branchParams.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
			}
			sql_branch.append(" GROUP BY  o2.id ORDER BY sum(case when fun.funds_pay_type='RECEIPT' then fun.transaction_amount else -1*fun.transaction_amount end) DESC ");
			List<Map<Object, Object>> list_branch=super.findMapBySql(sql_branch.toString(), branchParams);
			Map<String, String> map_branch = new HashMap<String, String>();
			int branchSort=0;
			String BRANCH_ID ="";
			Object BRANCH_ID_VALUE="";
			for(Map branch:list_branch){
				branchSort++;
				Set<String> set = branch.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					BRANCH_ID = iterator.next();
					BRANCH_ID_VALUE=branch.get(BRANCH_ID);
					if(BRANCH_ID_VALUE!=null){
						map_branch.put( BRANCH_ID_VALUE.toString(), branchSort+"");
					}
	
				}
	
			}
			/**按  按实收金额 获取 分公司的排名  结束*/
			
			/**按  按实收金额 获取 校区的排名 开始*/
			Map<String, Object> campusParams = new HashMap<String, Object>();
			StringBuffer sql_campus = new StringBuffer();
			sql_campus.append(" SELECT fun.fund_campus_id as CAMPUS_ID from funds_change_history fun ");
					
			sql_campus.append(" WHERE  fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
	
			if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
				sql_campus.append(" AND fun.TRANSACTION_TIME >= :startDate ");
				campusParams.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
			}
			if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
				sql_campus.append(" AND fun.TRANSACTION_TIME :endDate ");
				campusParams.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
			}
			sql_campus.append(" GROUP BY  fun.fund_campus_id ORDER BY sum(case when fun.funds_pay_type='RECEIPT' then fun.transaction_amount else -1*fun.transaction_amount end) DESC ");
			List<Map<Object, Object>> list_campus=super.findMapBySql(sql_campus.toString(), campusParams);
			Map<String, String> map_campus = new HashMap<String, String>();
	
			int campusSort=0;
			String CAMPUS_ID ="";
			Object CAMPUS_ID_VALUE="";
			for(Map campus:list_campus){
				campusSort++;
				Set<String> set = campus.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					CAMPUS_ID = iterator.next();
					CAMPUS_ID_VALUE=campus.get(CAMPUS_ID);
					if(CAMPUS_ID_VALUE!=null){
						map_campus.put( CAMPUS_ID_VALUE.toString(), campusSort+"");
					}
	
				}
	
			}
			/**按  按实收金额 获取 校区的排名 结束*/
			
			/**按  按实收金额 获取 校区用户的排名 开始*/
			int userSort=0;
			String USER_ID ="";
			Object USER_ID_VALUE="";
			Map<String, String> map_user = new HashMap<String, String>();
			if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				Map<String, Object> userParams = new HashMap<String, Object>();
				StringBuffer sql_user = new StringBuffer();
				sql_user.append(" SELECT ct.sign_staff_id as singStaffId from funds_change_history fun ");
				sql_user.append( " inner join contract ct on fun.contract_id = ct.id  ");
				sql_user.append(" WHERE  fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
				if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
					sql_user.append(" AND fun.TRANSACTION_TIME >= :startDate ");
					userParams.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
				}
				if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
					sql_user.append(" AND fun.TRANSACTION_TIME <= :endDate ");
					userParams.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
				}
				//要修改的部分
				if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
						sql_user.append(" and fun.fund_campus_id = :blCampusId ");
						userParams.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
				sql_user.append(" GROUP BY  ct.sign_staff_id ORDER BY sum(case when fun.funds_pay_type='RECEIPT' then fun.transaction_amount else -1*fun.transaction_amount end) DESC ");
				List<Map<Object, Object>> user_list=super.findMapBySql(sql_user.toString(), userParams);
				for(Map campus:user_list){
					userSort++;
					Set<String> set = campus.keySet();
					Iterator<String> iterator = set.iterator();
					while (iterator.hasNext()) {
						USER_ID = iterator.next();
						USER_ID_VALUE=campus.get(USER_ID);
						if(USER_ID_VALUE!=null){
							map_user.put( USER_ID_VALUE.toString(), userSort+"");
						}
		
					}
		
				}
			
			}
			/**按  按实收金额 获取 校区用户的排名 结束*/
			
		
		
		/**********************************************************************************/
		
		
		/**月度计划指标 开始*/
		String startDate = basicOperationQueryVo.getStartDate();
		String endDate = basicOperationQueryVo.getEndDate();
		Map<String, String> month_rate_map = null;
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			if (DateTools.getDate(startDate).compareTo(DateTools.getFirstDayOfMonth(startDate)) == 0
					&& DateTools.getDate(endDate).compareTo(DateTools.getLastDayOfMonth(startDate)) == 0) {
				String year = startDate.substring(0, 4);
				String month = startDate.substring(5, 7);
				String yearId = dataDictDao.getDataDictIdByName(year, DataDictCategory.YEAR);
				String monthValue = CommonUtil.getMonthTypeValue(Integer.parseInt(month));
				Map<String, Object> monthParams = new HashMap<String, Object>();
				StringBuffer month_rate_sql = new StringBuffer();
				month_rate_sql.append(" SELECT GOAL_ID, TARGET_VALUE FROM PLAN_MANAGEMENT where 1=1 ");
				month_rate_sql.append(" AND YEAR_ID = :yearId AND MONTH_ID = :monthValue AND TIME_TYPE = 'MONTH' and TARGET_TYPE='DAT0000000251' ");
				monthParams.put("yearId", yearId);
				monthParams.put("monthValue", monthValue);
				if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
					month_rate_sql.append(" AND GOAL_TYPE = 'BRENCH' ");
				} else {
					month_rate_sql.append(" AND GOAL_TYPE = 'CAMPUS' ");
				}
				List<Map<Object, Object>> month_rate_list=super.findMapBySql(month_rate_sql.toString(), monthParams);
				month_rate_map = new HashMap<String, String>();

				for(Map campus:month_rate_list){
					if(campus.get("TARGET_VALUE")!=null){
						month_rate_map.put( campus.get("GOAL_ID").toString(), campus.get("TARGET_VALUE").toString());
					}
				}
			}
		}
		
		/**月度计划指标 结束*/
		
		/** 退费 开始 */
		Map<String, Object> returnParams = new HashMap<String, Object>();
		StringBuffer return_fee_sql = new StringBuffer();
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			return_fee_sql.append(" select CONCAT(org_brench.id, '') as RELATE_ID, ifnull(sum(srf.RETURN_AMOUNT),0) as RETURN_AMOUNT ");
		} else {
			return_fee_sql.append(" select CONCAT(o.id, '') as RELATE_ID, ifnull(sum(srf.RETURN_AMOUNT),0) as RETURN_AMOUNT ");
		}
		
		return_fee_sql.append(" from STUDENT_RETURN_FEE srf ");
		return_fee_sql.append(" inner join organization o on srf.CAMPUS = o.id ");
		return_fee_sql.append(" inner join organization org_brench on o.parentID = org_brench.id ");
		return_fee_sql.append(" inner join organization org_group on org_brench.parentID = org_group.id ");
		return_fee_sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			return_fee_sql.append("  and srf.create_time >= :startDate ");
			returnParams.put("startDate", basicOperationQueryVo.getStartDate() + "00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			return_fee_sql.append(" and srf.create_time <= :endDate ");
			returnParams.put("endDate", basicOperationQueryVo.getEndDate() + "23:59.59");
		}
		
		//权限查询所属组织架构下的所有校区信息
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			return_fee_sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				return_fee_sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			return_fee_sql.append(")");
		}
		//要修改的部分
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				return_fee_sql.append(" and org_brench.id = :blCampusId ");
				returnParams.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				return_fee_sql.append(" and o.id = :blCampusId ");
				returnParams.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			return_fee_sql.append(" group by org_group.id, org_brench.id ");
		} else {
			return_fee_sql.append(" group by org_group.id, org_brench.id, o.id ");
		}
		List<Map<Object, Object>> return_fee_list=super.findMapBySql(return_fee_sql.toString(), returnParams);
		Map<String, String> return_fee_map = new HashMap<String, String>();
		for(Map returnFee : return_fee_list){
			if(returnFee.get("RETURN_AMOUNT")!=null){
				return_fee_map.put(returnFee.get("RELATE_ID").toString(), returnFee.get("RETURN_AMOUNT").toString());
			}
		}
		/** 退费 结束 */
		
		//添加分公司排名和校区排名后的list
		List<Map> list_new=new ArrayList<Map>();
		String brenchId="";//分公司
		String campusId="";//分校
		String userId="";//用户
		for(Map map:list){
			map.put("target_value", 0);
			map.put("return_fee", 0);
			//获取分公司排名
			if(map.get("brenchId")!=null && !"".equals(map.get("brenchId"))){
				brenchId=map.get("brenchId").toString();
				if(brenchId.indexOf("_")!=-1){
					brenchId=brenchId.substring(0, brenchId.indexOf("_"));
				}
				if(map_branch.get(brenchId)!=null){
				map.put("BRANCH_SORT", map_branch.get(brenchId));}
				else{
				map.put("BRANCH_SORT", "-");}
				
				List<Organization> campus = organizationDao.getOrganizationsByParentOrgIdAndType(brenchId,OrganizationType.CAMPUS);
				if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
						&& month_rate_map != null) {
					map.put("target_value", month_rate_map.get(brenchId)==null?0:month_rate_map.get(brenchId));
				}
				if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
						&& return_fee_map != null) {
					map.put("return_fee", return_fee_map.get(brenchId)==null?0:return_fee_map.get(brenchId));
				}
			}else{
				map.put("BRANCH_SORT","");
			}

			//获取分校区排名
			if(map.get("campusId")!=null && !"".equals(map.get("campusId"))){
				campusId=map.get("campusId").toString();
				if(campusId.indexOf("_")!=-1){
					campusId=campusId.substring(0, campusId.indexOf("_"));
				}
				if(map_campus.get(campusId)!=null){
					map.put("CAMPUS_SORT", map_campus.get(campusId));
				}else{
					map.put("CAMPUS_SORT", "-");
				}
				if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
						&& month_rate_map != null) {
					map.put("target_value", month_rate_map.get(campusId)==null?0:month_rate_map.get(campusId));
				}

				if(BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
						&& return_fee_map != null){
					map.put("return_fee", return_fee_map.get(campusId)==null?0:return_fee_map.get(campusId));
				}
			}else{
				map.put("CAMPUS_SORT","");
			}
			
			
			//获取分校区用户排名
			if(map.get("userId")!=null && !"".equals(map.get("userId"))){
				userId=map.get("userId").toString();
				if(userId.indexOf("_")!=-1){
					userId=userId.substring(0, userId.indexOf("_"));
				}
				if(map_user.get(userId)!=null){
					map.put("USER_SORT", map_user.get(userId));
				}else{
					map.put("USER_SORT", "-");
				}

			}else{
				map.put("USER_SORT","");
			}


			list_new.add(map);
		}

		dp.setDatas(list_new);
		dp.setRowCount(list_new.size());
		return dp;
	}
	
	
	@Override
	public DataPackage getFinanceBrenchAnalyzeForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select CONCAT(brench_id, '_', brench_name) as brenchId, '' as campusId, '' as userId, ") ;
		sql.append(" SUM(count_paid_cash_amount_new) AS countPaidCashAmount_new,");
		sql.append(" SUM(count_paid_cash_amount_re ) AS countPaidCashAmount_re,");
		sql.append(" SUM(count_paid_total_amount) countPaidTotalAmount,");
		sql.append(" SUM(online_amount) onlineAmount,");
		sql.append(" SUM(line_amount) lineAmount,");
		sql.append(" SUM(return_fee) return_fee ");
		sql.append(" from finance_brench fun");
		sql.append(" INNER JOIN organization o  ON fun.brench_id = o.id    ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		//权限查询所属组织架构下的所有校区信息
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		sql.append(" group by brench_id ");
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY  sum(fun.count_paid_total_amount) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		/**月度计划指标 开始*/
		String startDate = basicOperationQueryVo.getStartDate();
		String endDate = basicOperationQueryVo.getEndDate();
		Map<String, String> month_rate_map = getOrganizationTargetValue(basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000251", "BRENCH");
		Map<String,Integer> rankMap=getFinanceBrenchRank(startDate, endDate);
		
		for (Map map : list) {
			String brenchId=map.get("brenchId").toString();
			if(brenchId.indexOf("_")!=-1){
				brenchId=brenchId.substring(0, brenchId.indexOf("_"));
			}
			if(month_rate_map!=null)
			map.put("target_value", month_rate_map.get(brenchId)==null?0:month_rate_map.get(brenchId));
			if(rankMap!=null)
			map.put("BRANCH_SORT", rankMap.get(brenchId));
		}
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	public Map<String,Integer> getFinanceBrenchRank(String startDate,String endDate){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqlrank=new StringBuffer();
		sqlrank.append("select brench_Id as brenchId from finance_brench fun where 1=1");
		if (StringUtils.isNotBlank(startDate)) {
			sqlrank.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sqlrank.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", endDate);
		}
		sqlrank.append(" group by brench_id ");
		sqlrank.append(" ORDER BY  sum(fun.count_paid_total_amount) DESC ");
		List<Map<Object, Object>> list= this.findMapBySql(sqlrank.toString(), params);
		
		Map<String,Integer> rankMap=new HashMap<String,Integer>();
		Integer i=1;
		for (Map map : list) {
			rankMap.put(map.get("brenchId").toString(), i);
			i++;
		}
		return rankMap;
	}
	
	public Map<String,Integer> getFinanceCampusRank(String startDate,String endDate){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqlrank=new StringBuffer();
		sqlrank.append("select campus_Id as campusId from finance_Campus fun where 1=1");
		if (StringUtils.isNotBlank(startDate)) {
			sqlrank.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sqlrank.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", endDate);
		}
		sqlrank.append(" group by campus_Id ");
		sqlrank.append(" ORDER BY  sum(fun.count_paid_total_amount) DESC ");
		List<Map<Object, Object>> listRank= this.findMapBySql(sqlrank.toString(), params);
		
		Map<String,Integer> rankMap=new HashMap<String,Integer>();
		Integer i=1;
		for (Map map : listRank) {
			rankMap.put(map.get("campusId").toString(), i);
			i++;
		}
		return rankMap;
	}
	
	@Override
	public DataPackage getFinanceCampusAnalyzeForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select '' as brenchId, CONCAT(campus_id, '_', campus_name) as campusId, '' as userId, ") ;
		sql.append(" SUM(count_paid_cash_amount_new) AS countPaidCashAmount_new,");
		sql.append(" SUM(count_paid_cash_amount_re ) AS countPaidCashAmount_re,");
		sql.append(" SUM(count_paid_total_amount) countPaidTotalAmount,");
		sql.append(" SUM(online_amount) onlineAmount,");
		sql.append(" SUM(line_amount) lineAmount,");
		sql.append(" SUM(return_fee) return_fee ");
		sql.append(" from finance_campus fun");
		sql.append(" INNER JOIN organization o  ON fun.campus_id = o.id    ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
				sql.append(" and o.parentid = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		//权限查询所属组织架构下的所有校区信息
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		sql.append(" group by campus_id ");
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY  sum(fun.count_paid_total_amount) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		/**月度计划指标 开始*/
		String startDate = basicOperationQueryVo.getStartDate();
		String endDate = basicOperationQueryVo.getEndDate();
		Map<String, String> month_rate_map = getOrganizationTargetValue(basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000251", "CAMPUS");
		Map<String,Integer> rankMap=getFinanceCampusRank(startDate, endDate);
		for (Map map : list) {
			String brenchId=map.get("campusId").toString();
			if(brenchId.indexOf("_")!=-1){
				brenchId=brenchId.substring(0, brenchId.indexOf("_"));
			}
			if(month_rate_map!=null)
			map.put("target_value", month_rate_map.get(brenchId)==null?0:month_rate_map.get(brenchId));
			if(rankMap!=null)
			map.put("CAMPUS_SORT", rankMap.get(brenchId));
		}
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	@Override
	public DataPackage getFinanceUserAnalyzeForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> rankParams = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		StringBuffer sqlrank=new StringBuffer();
		sql.append("select '' as brenchId, CONCAT(campus_id, '_', campus_name) as campusId, CONCAT(user_id, '_', user_name) as userId, ") ;
		sqlrank.append("select user_Id as userId from finance_user fun where 1=1");
		sql.append(" SUM(count_paid_cash_amount_new) AS countPaidCashAmount_new,");
		sql.append(" SUM(count_paid_cash_amount_re ) AS countPaidCashAmount_re,");
		sql.append(" SUM(count_paid_total_amount) countPaidTotalAmount,");
		sql.append(" SUM(online_amount) onlineAmount,");
		sql.append(" SUM(line_amount) lineAmount,");
		sql.append(" SUM(return_fee) return_fee ");
		sql.append(" from finance_user fun");
		sql.append(" INNER JOIN organization o  ON fun.campus_id = o.id    ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.count_date >= :startDate ");
			sqlrank.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
			rankParams.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.count_date <= :endDate ");
			sqlrank.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
			rankParams.put("endDate", basicOperationQueryVo.getEndDate());
		}
		
		//要修改的部分
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		//权限查询所属组织架构下的所有校区信息
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		sql.append(" group by user_id ");
		sqlrank.append(" group by user_id ");
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY  sum(fun.count_paid_total_amount) DESC ");
		sqlrank.append(" ORDER BY  sum(fun.count_paid_total_amount) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		List<Map<Object, Object>> listRank= super.findMapOfPageBySql(sqlrank.toString(), dp, rankParams);
		Map<String,Integer> rankMap=new HashMap<String,Integer>();
		Integer i=1;
		for (Map map : listRank) {
			rankMap.put(map.get("userId").toString(), i);
			i++;
		}
		for (Map map : list) {
			String brenchId=map.get("userId").toString();
			if(brenchId.indexOf("_")!=-1){
				brenchId=brenchId.substring(0, brenchId.indexOf("_"));
			}
			if(rankMap!=null)
			map.put("USER_SORT", rankMap.get(brenchId));
		}
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	/**
	 * 手机现金流
	 */
	@Override
	public List<Map<Object, Object>> getFinanceAnalyzeForMobileLine(BasicOperationQueryVo basicOperationQueryVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append(" substr(fun.TRANSACTION_TIME,1,10) transactionTime, SUM(case when fun.funds_pay_type='RECEIPT' then fun.transaction_amount else -1*fun.transaction_amount end) countPaidTotalAmount ");
		sql.append(" from funds_change_history fun  ");
		sql.append(" inner join contract ct on fun.contract_id = ct.id  ");
		sql.append(" INNER JOIN organization o  ON fun.fund_campus_id = o.id    ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		sql.append(" where 1=1 ");
		sql.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		
		//要修改的部分
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o2.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		
		sql.append("  and o.orgType='CAMPUS' ");

		
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}

		sql.append(" GROUP BY substr(fun.TRANSACTION_TIME,1,10) ");
		
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY  substr(fun.TRANSACTION_TIME,1,10) ");
		
		return super.findMapBySql(sql.toString(), params);
	}
	
	
	@Override
	public List<Map<Object, Object>> getFinanceBrenchForMobileLine(
			BasicOperationQueryVo basicOperationQueryVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count_date transactionTime,sum(fun.count_paid_total_amount) countPaidTotalAmount  ");
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("from finance_brench fun left join organization o on fun.brench_id=o.id  ");
		}else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("from finance_campus fun left join organization o on fun.campus_id=o.id  ");
		}else{
			sql.append("from finance_user fun left join organization o on fun.campus_id=o.id  ");
		}
		sql.append(" where 1=1  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		
		//要修改的部分
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
			if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}else{
				sql.append(" and o.parentId = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
			
		}
		
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		
		
		sql.append(" group by fun.count_date  order by fun.count_date   ");
		
		
		return this.findMapBySql(sql.toString(), params);
	}
	
	@Override
	public Map getFinanceAnalyzeForMobileTotal(
			BasicOperationQueryVo basicOperationQueryVo, String preStartDate,
			String preEndDate, String nexStartDate, String nextEndDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append("  ifnull(SUM(case when fun.TRANSACTION_TIME>='"+preStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+preEndDate+" 23:59:59' and fun.funds_pay_type = 'RECEIPT' THEN fun.transaction_amount when fun.TRANSACTION_TIME>='"+preStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+preEndDate+" 23:59:59' and fun.funds_pay_type <> 'RECEIPT'  then - 1 * fun.transaction_amount else 0 end),0) preAmount, ");
		sql.append("  ifnull(SUM(case when fun.TRANSACTION_TIME>='"+nexStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+nextEndDate+" 23:59:59'  and fun.funds_pay_type = 'RECEIPT' THEN fun.transaction_amount when fun.TRANSACTION_TIME>='"+nexStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+nextEndDate+" 23:59:59' and fun.funds_pay_type <> 'RECEIPT'  then - 1 * fun.transaction_amount  else 0 end),0) nexAmount, ");
		sql.append("  ifnull(ROUND(SUM(case when fun.TRANSACTION_TIME>='"+nexStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+nextEndDate+" 23:59:59'  and fun.funds_pay_type = 'RECEIPT' THEN fun.transaction_amount when fun.TRANSACTION_TIME>='"+nexStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+nextEndDate+" 23:59:59' and fun.funds_pay_type <> 'RECEIPT'  then - 1 * fun.transaction_amount  else 0 end) - ");
		sql.append("  SUM(case when fun.TRANSACTION_TIME>='"+preStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+preEndDate+" 23:59:59'  and fun.funds_pay_type = 'RECEIPT' THEN fun.transaction_amount when fun.TRANSACTION_TIME>='"+preStartDate+" 00:00:00' and fun.TRANSACTION_TIME<='"+preEndDate+" 23:59:59' and fun.funds_pay_type <> 'RECEIPT'  then - 1 * fun.transaction_amount  else 0 end),2),0) compareNum ");
		sql.append(" from funds_change_history fun  ");
		sql.append(" inner join contract ct on fun.contract_id = ct.id  ");
		sql.append(" INNER JOIN organization o  ON fun.fund_campus_id = o.id    ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");
		sql.append(" where 1=1 ");
		sql.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') ");
		if (StringUtils.isNotBlank(preStartDate)) {
			sql.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", preStartDate + " 00:00:00");
		}
		if (StringUtils.isNotBlank(nextEndDate)) {
			sql.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", nextEndDate + " 23:59:59");
		}
		//要修改的部分
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o2.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		sql.append("  and o.orgType='CAMPUS' ");
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		List<Map<Object, Object>> list= super.findMapBySql(sql.toString(), params);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	@Override
	public Map getFinanceBrenchForMobileTotal(
			BasicOperationQueryVo basicOperationQueryVo, String preStartDate,
			String preEndDate, String nexStartDate, String nextEndDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append("  ifnull(SUM(case when fun.count_date>='"+preStartDate+"' and fun.count_date<='"+preEndDate+"' then fun.count_paid_total_amount else 0 end),0)  preAmount, ");
		sql.append("  ifnull(SUM(case when fun.count_date>='"+nexStartDate+"' and fun.count_date<='"+nextEndDate+"' then fun.count_paid_total_amount else 0 end),0) nexAmount, ");
		sql.append("  ifnull(ROUND(SUM(case when fun.count_date>='"+nexStartDate+"' and fun.count_date<='"+nextEndDate+"' then fun.count_paid_total_amount else 0 end) - ");
		sql.append("  SUM(case when fun.count_date>='"+preStartDate+"' and fun.count_date<='"+preEndDate+"' then fun.count_paid_total_amount else 0 end),2),0) compareNum ");
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("from finance_brench fun left join organization o on fun.brench_id=o.id  ");
		}else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append("from finance_campus fun left join organization o on fun.campus_id=o.id  ");
		}else{
			sql.append("from finance_user fun left join organization o on fun.campus_id=o.id  ");
		}
		sql.append(" where 1=1  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.count_date >= :startDate ");
			params.put("startDate", preStartDate + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.count_date <= :endDate ");
			params.put("endDate", nextEndDate + " 23:59:59");
		}
		//要修改的部分
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
			if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}else{
				sql.append(" and o.parentId = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		List<Map<Object, Object>> list = this.findMapBySql(sql.toString(), params);
		if(list.size()>0){
			return list.get(0);
		}
		return new HashMap(); 
	}

	@Override
	public DataPackage getMiniClassCourseSeries(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,
			String catchStudentStatu, String profitStatu,
			String lowNormalStudent) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select groupp.NAME as groupName,branch.NAME as branchName,campus.NAME as campusName,mc.NAME as className,mc.PEOPLE_QUANTITY AS peopleQuantity ,cm.CLASS_MEMBER as classMember, ");
		sql.append(" ifnull(totalStudent.actualMember,0) as actualMember,MIN_CLASS_MEMBER as minClassMember,PROFIT_MEMBER as profitMember  ");
		sql.append(" from organization as campus,organization as branch,organization as groupp ,mini_class mc  ");
		sql.append(" left join CLASSROOM_MANAGE cm on mc.CLASSROOM=cm.ID  ");
		sql.append(" left join (select MINI_CLASS_ID ,count(MINI_CLASS_ID) as actualMember from mini_class_student group by MINI_CLASS_ID) as totalStudent  on totalStudent.MINI_CLASS_ID=mc.MINI_CLASS_ID  ");
		sql.append(" where  branch.parentID = groupp.id  and mc.BL_CAMPUS_ID = campus.id and campus.parentID = branch.id   ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND mc.START_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND mc.START_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (StringUtils.isNotBlank(catchStudentStatu)) {
			if("0".equals(catchStudentStatu))
				sql.append(" AND (totalStudent.actualMember<mc.PEOPLE_QUANTITY or totalStudent.actualMember is null) ");
			else
				sql.append(" AND totalStudent.actualMember>=mc.PEOPLE_QUANTITY ");
		}
		if (StringUtils.isNotBlank(profitStatu)) {
			if("0".equals(profitStatu))
				sql.append(" AND (totalStudent.actualMember<MIN_CLASS_MEMBER or totalStudent.actualMember is null) ");
			else
				sql.append(" AND totalStudent.actualMember>=MIN_CLASS_MEMBER ");
		}
		if (StringUtils.isNotBlank(lowNormalStudent)) {
			if("0".equals(lowNormalStudent))
				sql.append(" AND totalStudent.actualMember>=PROFIT_MEMBER");
			else
				sql.append(" AND (totalStudent.actualMember<PROFIT_MEMBER  or totalStudent.actualMember is null)");
		}
//        sql.append(roleQLConfigService.getValueResult("业绩（校区）","sql"));
		if (StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			Organization org = organizationDao.findById(basicOperationQueryVo.getBlCampusId());
			sql.append(" AND mc.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%')");
		}
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (");
			sql.append(" campus.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or campus.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		List<Role> roles = userService.getCurrentLoginUserRoles();//学管师只能看自己的
		if(roles.size()==1 && roles.get(0).getRoleCode()!=null  && roles.get(0).getRoleCode().getValue().equals("STUDY_MANAGER")){
			sql.append(" AND mc.STUDY_MANEGER_ID>='"+ userService.getCurrentLoginUser().getUserId()+ "' ");
		}
		sql.append("  order by groupp.name,branch.name,campus.name ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	@Override
	public DataPackage getMiniClassMemberTotalSum(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select groupp.NAME as groupName,branch.NAME as branchName,campus.NAME as campusName, ");
		sql.append(" sum(1) as classes,sum(ifnull(mc.PEOPLE_QUANTITY,0)) AS peopleQuantity ,  ");
		sql.append(" sum(ifnull(totalStudent.actualMember,0)) as actualMember,  ");
		sql.append(" sum(case when ifnull(totalStudent.actualMember,0)>=mc.PEOPLE_QUANTITY then 1 else 0 end) as actualNum,  ");
		sql.append(" sum(ifnull(MIN_CLASS_MEMBER,0)) as minClassMember, ");
		sql.append(" sum(case when ifnull(totalStudent.actualMember,0)<mc.MIN_CLASS_MEMBER then 1 else 0 end) as minNum, ");
		sql.append(" sum(ifnull(PROFIT_MEMBER,0)) as profitMember, ");
		sql.append(" sum(case when ifnull(totalStudent.actualMember,0)>=mc.PROFIT_MEMBER then 1 else 0 end) as profitNum ");
		sql.append(" from organization as campus,organization as branch,organization as groupp ,mini_class mc ");
		sql.append(" left join CLASSROOM_MANAGE cm on mc.CLASSROOM=cm.ID ");
		sql.append(" left join (select MINI_CLASS_ID ,count(MINI_CLASS_ID) as actualMember from mini_class_student group by MINI_CLASS_ID) as totalStudent  on totalStudent.MINI_CLASS_ID=mc.MINI_CLASS_ID ");
		sql.append(" where  branch.parentID = groupp.id  and mc.BL_CAMPUS_ID = campus.id and campus.parentID = branch.id  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND mc.START_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND mc.START_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}

		if (StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			Organization org = organizationDao.findById(basicOperationQueryVo.getBlCampusId());
			sql.append(" AND mc.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%')");
		}

		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (");
			sql.append(" campus.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or campus.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		List<Role> roles = userService.getCurrentLoginUserRoles();//学管师只能看自己的
		if(roles.size()==1 && roles.get(0).getRoleCode()!=null  && roles.get(0).getRoleCode().getValue().equals("STUDY_MANAGER")){
			sql.append(" AND mc.STUDY_MANEGER_ID>='"+ userService.getCurrentLoginUser().getUserId()+ "' ");
		}
		sql.append(" group by groupp.name,branch.name,campus.name  order by groupp.name,branch.name,campus.name");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	
	@Override
	public DataPackage getPlanManagementTotal(Integer preYear,String thisYear, String goalType,
			String targetType, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if(goalType.equals("GROUNP")){
			sql.append(" o.name as groupName,  ");
		}else if(goalType.equals("BRENCH")){
			sql.append(" (SELECT name from organization where id=o.parentId) as groupName,  ");
			sql.append(" o.name as brenchName,  ");
		}else{
			sql.append(" (SELECT name from organization where id=(SELECT parentID from organization where id=o.parentId)) as groupName, ");
			sql.append(" (SELECT name from organization where id=o.parentId) as brenchName,  ");
			sql.append(" o.name as campusName,  ");
		}
		sql.append(" ifNull(sum(lv.oct),0) as oct,ifNull(sum(lv.nov),0) as nov,ifNull(sum(lv.dece),0) as dece,ifNull(sum(lv.jan),0) as jan,ifNull(sum(lv.feb),0) as feb,ifNull(sum(lv.mar),0) as mar,  ");
		sql.append(" ifNull(sum(lv.apr),0) as apr,ifNull(sum(lv.may),0) as may,ifNull(sum(lv.jun),0) as jun,ifNull(sum(lv.jul),0) as jul,ifNull(sum(lv.aug),0) as aug,ifNull(sum(lv.sept),0) as sept ");
		sql.append(" from  organization o left join (select GOAL_ID,case when dd.`NAME` = :preYear0 and pm.MONTH_ID='OCT' then TARGET_VALUE else 0 end as oct  ");
		params.put("preYear0", preYear);
		sql.append(" ,case when dd.`NAME` = :preYear1 and pm.MONTH_ID='NOV' then TARGET_VALUE else 0 end as nov  ");
		params.put("preYear1", preYear);
		sql.append(" ,case when dd.`NAME` = :preYear2 and pm.MONTH_ID='DEC' then TARGET_VALUE else 0 end as dece  ");
		params.put("preYear2", preYear);
		sql.append(" ,case when dd.`NAME` = :thisYear0 and pm.MONTH_ID='JAN' then TARGET_VALUE else 0 end as jan  ");
		params.put("thisYear0", thisYear);
		sql.append(" ,case when dd.`NAME` = :thisYear1 and pm.MONTH_ID='FEB' then TARGET_VALUE else 0 end as feb  ");
		params.put("thisYear1", thisYear);

		sql.append(" ,case when dd.`NAME` = :thisYear2 and pm.MONTH_ID='MAR' then TARGET_VALUE else 0 end as mar  ");
		params.put("thisYear2", thisYear);
		sql.append(" ,case when dd.`NAME` = :thisYear3 and pm.MONTH_ID='APR' then TARGET_VALUE else 0 end as apr  ");
		params.put("thisYear3", thisYear);
		sql.append(" ,case when dd.`NAME` = :thisYear4 and pm.MONTH_ID='MAY' then TARGET_VALUE else 0 end as may  ");
		params.put("thisYear4", thisYear);
		sql.append(" ,case when dd.`NAME` = :thisYear5 and pm.MONTH_ID='JUN' then TARGET_VALUE else 0 end as jun  ");
		params.put("thisYear5", thisYear);
		sql.append(" ,case when dd.`NAME` = :thisYear6 and pm.MONTH_ID='JUL' then TARGET_VALUE else 0 end as jul  ");
		params.put("thisYear6", thisYear);
		sql.append(" ,case when dd.`NAME` = :thisYear7 and pm.MONTH_ID='AUG' then TARGET_VALUE else 0 end as aug  ");
		params.put("thisYear7", thisYear);
		sql.append(" ,case when dd.`NAME` = :thisYear8 and pm.MONTH_ID='SEPT' then TARGET_VALUE else 0 end as sept  ");
		params.put("thisYear8", thisYear);
		sql.append(" from PLAN_MANAGEMENT pm,data_dict dd ");
		sql.append(" where pm.YEAR_ID=dd.ID  and pm.TIME_TYPE='MONTH' AND (dd.name = :preYear3 or dd.`NAME` = :thisYear9) ");
		params.put("preYear3", preYear);
		params.put("thisYear9", thisYear);
		if(StringUtil.isNotEmpty(targetType)){
			sql.append(" and pm.TARGET_TYPE = :targetType");
			params.put("targetType", targetType);
		}
		sql.append(" ) as lv  on lv.goal_id=o.id ");
		sql.append(" where 1=1 and o.orgType = :goalType " );
		params.put("goalType", goalType);
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		sql.append(" group by o.id  order by o.parentId ,o.id ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	@Override
	public DataPackage getMiniClassStudentRealCount(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp, User currentLoginUser, Organization belongCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT GROUP_ID,GROUP_NAME,BRANCH_ID,BRANCH_NAME,CAMPUS_ID,CAMPUS_NAME, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER=0 THEN 1 ELSE 0 END) AS ZERO, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 1 AND 3 THEN 1 ELSE 0 END) AS NUM1, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 4 AND 6 THEN 1 ELSE 0 END) AS NUM2, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 7 AND 8 THEN 1 ELSE 0 END) AS NUM3, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 9 AND 10 THEN 1 ELSE 0 END) AS NUM4, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 11 AND 12 THEN 1 ELSE 0 END) AS NUM5, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 13 AND 14 THEN 1 ELSE 0 END) AS NUM6, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 15 AND 20 THEN 1 ELSE 0 END) AS NUM7, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER BETWEEN 21 AND 30 THEN 1 ELSE 0 END) AS NUM8, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER > 30 THEN 1 ELSE 0 END) AS NUM9, ");
		sql.append(" SUM(CASE WHEN ACTUAL_MEMBER>0 THEN 1 ELSE 0 END) AS NUMALL, ");
		sql.append(" SUM(ACTUAL_MEMBER) AS MEMBERS, ");
		sql.append(" SUM(1) TOTAL,SUM(ACTUAL_MEMBER)/SUM(CASE WHEN ACTUAL_MEMBER>0 THEN 1 ELSE 0 END) AS RATE ");
		sql.append(" FROM ODS_DAY_MINI_CLASS_STUDENT_REAL_COUNT ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}

		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND START_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND START_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (StringUtils.isNotBlank(miniClassStatu)) {
			sql.append(" AND CLASS_STATUS = :miniClassStatu ");
			params.put("miniClassStatu", miniClassStatu);
		}

		if (StringUtils.isNotBlank(basicOperationQueryVo.getOrganizationId())) {
			sql.append(" AND (CAMPUS_ID = :campuId or GROUP_ID = :groupId or BRANCH_ID = :branchId) ");
			params.put("campuId", basicOperationQueryVo.getOrganizationId());
			params.put("groupId", basicOperationQueryVo.getOrganizationId());
			params.put("branchId", basicOperationQueryVo.getOrganizationId());
		}
//		sql.append(roleQLConfigService.getValueResult("小班学生人数统计","sql"));
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and CAMPUS_ID in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		sql.append(" GROUP BY GROUP_ID,GROUP_NAME,BRANCH_ID,BRANCH_NAME,CAMPUS_ID,CAMPUS_NAME ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	@Override
	public DataPackage getMiniClassStudentRealCountDetail(
			BasicOperationQueryVo basicOperationQueryVo, String miniClassStatu,
			DataPackage dp, User currentLoginUser, Organization belongCampus,
			String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM ODS_DAY_MINI_CLASS_STUDENT_REAL_COUNT ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			sql.append(" AND CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND START_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND START_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (StringUtils.isNotBlank(miniClassStatu)) {
			sql.append(" AND CLASS_STATUS ='"+miniClassStatu+"' ");
		}
		
		switch (type) {
		case "1":
			sql.append(" AND ACTUAL_MEMBER = 0 ");
			break;
		case "2":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 1 AND 3 ");
			break;
		case "3":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 4 AND 6 ");
			break;
		case "4":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 7 AND 8 ");
			break;
		case "5":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 9 AND 10 ");
			break;
		case "6":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 11 AND 12 ");
			break;
		case "7":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 13 AND 14 ");
			break;
		case "8":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 15 AND 20 ");
			break;
		case "9":
			sql.append(" AND ACTUAL_MEMBER BETWEEN 21 AND 30 ");
			break;
		case "10":
			sql.append(" AND ACTUAL_MEMBER BETWEEN > 30 ");
			break;
		default:
			break;
		}

		//权限查询所属组织架构下的所有校区信息
				List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
				if(userOrganizations != null && userOrganizations.size() > 0){
					Organization org = userOrganizations.get(0);
					sql.append("  and CAMPUS_ID in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
					for(int i = 1; i < userOrganizations.size(); i++){
						sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
					}
					sql.append(")");
				}
//		sql.append(roleQLConfigService.getValueResult("小班学生人数统计","sql"));
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 老师小班课时年级分布视图(按课时)
	 */
	@Override
	public DataPackage getMiniCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID,  ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(org.name,'') COURSE_CAMPUS_ID, ");
			sql.append(" a.teacherLevel,a.teacherType,");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(org.name,'') COURSE_CAMPUS_ID, ");
		}
		sql.append(" 	SUM(a.courseHours) TOTAL_CONSUME_HOUR, ");
		sql.append(" 	SUM(a.gradeOneHours) FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeTwoHours) SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeThreeHours) THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeFourHours) FOURTH_GRADE, ");
		sql.append(" 	SUM(a.gradeFiveHours) FIFTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSixHours) SIXTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSevenHours) MIDDLE_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeEightHours) MIDDLE_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeNineHours) MIDDLE_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeTenHours) HIGH_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeElevenHours) HIGH_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeTwelveHours) HIGH_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.otherHours) OTHER_GRADE ");
		sql.append(" FROM ");
		sql.append(" 	(SELECT DISTINCT mcc.MINI_CLASS_COURSE_ID, mcc.MINI_CLASS_ID, u.USER_ID as TEACHER_ID, u.NAME as teacherName, u.employee_No, u.WORK_TYPE, mcc.COURSE_DATE, MAX(IF(dd.name = '一年级', mcc.COURSE_HOURS, 0)) AS gradeOneHours,  ");
		sql.append(" 	MAX(IF(dd.name = '二年级', mcc.COURSE_HOURS, 0)) as gradeTwoHours, MAX(IF(dd.name = '三年级', mcc.COURSE_HOURS, 0)) as gradeThreeHours, ");
		sql.append(" 	MAX(IF(dd.name = '四年级', mcc.COURSE_HOURS, 0)) as gradeFourHours, MAX(IF(dd.name = '五年级', mcc.COURSE_HOURS, 0)) as gradeFiveHours, ");
		sql.append(" 	MAX(IF(dd.name = '六年级', mcc.COURSE_HOURS, 0)) as gradeSixHours, MAX(IF(dd.name = '初一', mcc.COURSE_HOURS, 0)) as gradeSevenHours, ");
		sql.append(" 	MAX(IF(dd.name = '初二', mcc.COURSE_HOURS, 0)) as gradeEightHours, MAX(IF(dd.name = '初三', mcc.COURSE_HOURS, 0)) as gradeNineHours, ");
		sql.append(" 	MAX(IF(dd.name = '高一', mcc.COURSE_HOURS, 0)) as gradeTenHours, MAX(IF(dd.name = '高二', mcc.COURSE_HOURS, 0)) as gradeElevenHours, ");
//		sql.append(" 	MAX(IF(dd.name = '高三', mcc.COURSE_HOURS, 0)) as gradeTwelveHours, mcc.COURSE_HOURS as courseHours, org.name as courseCampusName, u.organizationID as BL_CAMPUS_ID, ");
		sql.append(" 	MAX(IF(dd.name = '高三', mcc.COURSE_HOURS, 0)) as gradeTwelveHours, mcc.COURSE_HOURS as courseHours, ");
		sql.append(" 	MAX(IF(dd.name is null, mcc.COURSE_HOURS, 0)) as otherHours ");
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<='"+courseConsumeTeacherVo.getEndDate()+"' and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<='"+courseConsumeTeacherVo.getEndDate()+"' and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherType");
		sql.append(" FROM mini_class_course mcc ");
		sql.append(" LEFT JOIN data_dict dd on mcc.GRADE = dd.ID ");
		sql.append(" INNER JOIN user u on mcc.TEACHER_ID = u.USER_ID ");
		sql.append(" INNER JOIN mini_class mc ON mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" WHERE 1=1 ");
		sql.append("  AND mcc.COURSE_STATUS='CHARGED' ");
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getStartDate())) {
			sql.append(" AND mcc.COURSE_DATE >= :startDate ");
			params.put("startDate", courseConsumeTeacherVo.getStartDate());
		}
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getEndDate())) {
			sql.append(" AND mcc.COURSE_DATE <= :endDate ");
			params.put("endDate", courseConsumeTeacherVo.getEndDate());
		}

		sql.append(roleQLConfigService.getAppendSqlByAllOrg("老师小班课时分布统计子查询","sql","mc.BL_CAMPUS_ID"));
		
		if (StringUtil.isNotBlank(courseConsumeTeacherVo.getSubject())){
			String[] subjects=courseConsumeTeacherVo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or mcc.SUBJECT = :subject" + i);
						params.put("subject" + i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}
		
		sql.append(" GROUP BY mcc.MINI_CLASS_COURSE_ID) a ");
		sql.append(" INNER JOIN mini_class mc on a.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN product p on p.id = mc.PRODUCE_ID ");
		sql.append(" INNER JOIN  organization org on mc.BL_CAMPUS_ID  = org.id ");
		sql.append(" INNER JOIN ref_user_org ruo on a.TEACHER_ID = ruo.USER_ID ");
		sql.append(" INNER JOIN organization o on ruo.CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on ruo.BRANCH_ID = org_brench.id ");
		sql.append(" LEFT JOIN organization org_group on ruo.GROUP_ID = org_group.id ");
		sql.append(" WHERE 1=1 ");

		if(courseConsumeTeacherVo.getProductQuarterSearch()!=null && StringUtils.isNotBlank(courseConsumeTeacherVo.getProductQuarterSearch())){
			//产品季度
			sql.append(" and mc.MINI_CLASS_ID in (select MINI_CLASS_ID from mini_class mc INNER JOIN product p on mc.produce_id=p.id where p.product_quarter_id = :productQuarter) ");
			params.put("productQuarter", courseConsumeTeacherVo.getProductQuarterSearch());
		}

		if (StringUtil.isNotBlank(courseConsumeTeacherVo.getMiniClassTypeId())){
			String[] miniClassTypes=courseConsumeTeacherVo.getMiniClassTypeId().split(",");
			if(miniClassTypes.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < miniClassTypes.length; i++) {
					if (StringUtil.isNotBlank(miniClassTypes[i])){
						sql.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
						params.put("miniClassType" + i, miniClassTypes[i]);
					}
				}
				sql.append(" )");
			}
		}
		
		

		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
//			sql.append(" AND ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND org_brench.id = :blCampusId ");
			params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBlCampusId())){
				sql.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
			}
			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBranchId())){
				sql.append(" AND  org_brench.id =:branchId ");
				params.put("branchId", courseConsumeTeacherVo.getBranchId());
			}

			if(StringUtils.isNotBlank(courseConsumeTeacherVo.getTeacherType())){
				sql.append(" and a.teacherType = :teacherType ");
				params.put("teacherType", courseConsumeTeacherVo.getTeacherType());
			}
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND a.TEACHER_ID = :blCampusId ");
			params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
		}

		sql.append(roleQLConfigService.getAppendSqlByAllOrg("老师小班课时分布统计","sql","o.id"));
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY o.id ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY a.TEACHER_ID,org.name ");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY org.name ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 老师小班课时年级分布视图(按小时)
	 * @param courseConsumeTeacherVo
	 * @param dp
     * @return
     */
	@Override
	public DataPackage getMiniCourseConsumeTeacherViewHours(CourseConsumeTeacherVo courseConsumeTeacherVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID,  ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(org.name,'') COURSE_CAMPUS_ID, ");
			sql.append(" a.teacherLevel,a.teacherType,");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' , org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(org.name,'') COURSE_CAMPUS_ID, ");
		}
		sql.append(" 	SUM(a.courseHours) TOTAL_CONSUME_HOUR, ");
		sql.append(" 	SUM(a.gradeOneHours) FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeTwoHours) SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeThreeHours) THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeFourHours) FOURTH_GRADE, ");
		sql.append(" 	SUM(a.gradeFiveHours) FIFTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSixHours) SIXTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSevenHours) MIDDLE_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeEightHours) MIDDLE_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeNineHours) MIDDLE_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeTenHours) HIGH_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeElevenHours) HIGH_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeTwelveHours) HIGH_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.otherHours) OTHER_GRADE ");
		sql.append(" FROM ");
		sql.append(" 	(SELECT DISTINCT mcc.MINI_CLASS_COURSE_ID, mcc.MINI_CLASS_ID, u.USER_ID as TEACHER_ID, u.NAME as teacherName, u.employee_No, u.WORK_TYPE, mcc.COURSE_DATE, MAX(IF(dd.name = '一年级',ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0)/ 60,2), 0)) AS gradeOneHours,  ");
		sql.append(" 	MAX(IF(dd.name = '二年级', ROUND(mcc.COURSE_HOURS *IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeTwoHours, MAX(IF(dd.name = '三年级', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeThreeHours, ");
		sql.append(" 	MAX(IF(dd.name = '四年级', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeFourHours, MAX(IF(dd.name = '五年级',ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeFiveHours, ");
		sql.append(" 	MAX(IF(dd.name = '六年级', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeSixHours, MAX(IF(dd.name = '初一', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeSevenHours, ");
		sql.append(" 	MAX(IF(dd.name = '初二', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeEightHours, MAX(IF(dd.name = '初三', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeNineHours, ");
		sql.append(" 	MAX(IF(dd.name = '高一', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeTenHours, MAX(IF(dd.name = '高二', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeElevenHours, ");
//		sql.append(" 	MAX(IF(dd.name = '高三', mcc.COURSE_HOURS, 0)) as gradeTwelveHours, mcc.COURSE_HOURS as courseHours, org.name as courseCampusName, u.organizationID as BL_CAMPUS_ID, ");
		sql.append(" 	MAX(IF(dd.name = '高三', ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as gradeTwelveHours, ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2) as courseHours , ");
		sql.append(" 	MAX(IF(dd.name is null, ROUND(mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60,2), 0)) as otherHours ");
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<='"+courseConsumeTeacherVo.getEndDate()+"' and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<='"+courseConsumeTeacherVo.getEndDate()+"' and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherType");
		sql.append(" FROM mini_class_course mcc ");
//		sql.append(" INNER JOIN account_charge_records acc ON acc.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID ");
		sql.append(" LEFT JOIN data_dict dd on mcc.GRADE = dd.ID ");
		sql.append(" INNER JOIN user u on mcc.TEACHER_ID = u.USER_ID ");
		sql.append(" INNER JOIN mini_class mc ON mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND mcc.COURSE_STATUS = 'CHARGED' ");
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getStartDate())) {
			sql.append(" AND mcc.COURSE_DATE >= :startDate ");
			params.put("startDate", courseConsumeTeacherVo.getStartDate());
		}
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getEndDate())) {
			sql.append(" AND mcc.COURSE_DATE <= :endDate ");
			params.put("endDate", courseConsumeTeacherVo.getEndDate());
		}

		sql.append(roleQLConfigService.getAppendSqlByAllOrg("老师小班课时分布统计子查询","sql","mc.BL_CAMPUS_ID"));

		if (StringUtil.isNotBlank(courseConsumeTeacherVo.getSubject())){
			String[] subjects=courseConsumeTeacherVo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or mcc.SUBJECT = :subject" + i);
						params.put("subject" + i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}
		sql.append(" GROUP BY mcc.MINI_CLASS_COURSE_ID) a ");
		sql.append(" INNER JOIN mini_class mc on a.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN product p on p.id = mc.PRODUCE_ID ");
		sql.append(" INNER JOIN organization org on mc.BL_CAMPUS_ID  = org.id ");
		sql.append(" INNER JOIN ref_user_org ruo on a.TEACHER_ID = ruo.USER_ID ");
		sql.append(" INNER JOIN organization o on ruo.CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on ruo.BRANCH_ID = org_brench.id ");
		sql.append(" LEFT JOIN organization org_group on ruo.GROUP_ID = org_group.id ");
		sql.append(" WHERE 1=1 ");
		if(courseConsumeTeacherVo.getProductQuarterSearch()!=null && StringUtils.isNotBlank(courseConsumeTeacherVo.getProductQuarterSearch())){
			//产品季度
			sql.append(" and mc.MINI_CLASS_ID in (select MINI_CLASS_ID from mini_class mc INNER JOIN product p on mc.produce_id=p.id where p.product_quarter_id='"+courseConsumeTeacherVo.getProductQuarterSearch()+"') ");
		}
		if (StringUtil.isNotBlank(courseConsumeTeacherVo.getMiniClassTypeId())){
			String[] miniClassTypes=courseConsumeTeacherVo.getMiniClassTypeId().split(",");
			if(miniClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID = :miniClassType0 ");
				params.put("miniClassType0", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sql.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					params.put("miniClassType" + i, miniClassTypes[i]);
				}
				sql.append(" )");
			}
		}
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
//			sql.append(" AND ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND org_brench.id = '"+courseConsumeTeacherVo.getBlCampusId()+"' ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBlCampusId())){
				sql.append(" AND o.id = '"+courseConsumeTeacherVo.getBlCampusId()+"' ");
			}

			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBranchId())){
				sql.append(" AND org_brench.id ='"+courseConsumeTeacherVo.getBranchId()+"' ");
			}

			if(StringUtils.isNotBlank(courseConsumeTeacherVo.getTeacherType())){
				sql.append(" and a.teacherType='"+courseConsumeTeacherVo.getTeacherType()+"'");
			}
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND a.TEACHER_ID = '"+courseConsumeTeacherVo.getBlCampusId()+"' ");
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("老师小班课时分布统计","sql","o.id"));
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY o.id ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY a.TEACHER_ID,org.name ");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY org.name ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 老师小班人数年级分布（工资）
	 */
	@Override
	public DataPackage getMiniChargedPeopleTeacherView(String startDate, String endDate, OrganizationType organizationType, String blCampusId,String productQuarterSearch, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(organizationType)) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' WORK_TYPE, '' COURSE_CAMPUS_ID,  ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER_NAME, '' WORK_TYPE, '' COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(a.courseCampusName,'') COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.OTHER.equals(organizationType)) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(a.courseCampusName,'') COURSE_CAMPUS_ID, ");
		}
		sql.append(" 	SUM(a.PEOPLE_AMOUNT) as TOTAL_PEOPLE_AMOUNT, ");
		sql.append(" 	SUM(a.gradeOneAmount) FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeTwoAmount) SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeThreeAmount) THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeFourAmount) FOURTH_GRADE, ");
		sql.append(" 	SUM(a.gradeFiveAmount) FIFTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSixAmount) SIXTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSevenAmount) MIDDLE_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeEightAmount) MIDDLE_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeNineAmount) MIDDLE_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeTenAmount) HIGH_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeElevenAmount) HIGH_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeTwelveAmount) HIGH_SCHOOL_THIRD_GRADE ");
		sql.append(" FROM ");
		sql.append(" 	(SELECT DISTINCT mcc.MINI_CLASS_COURSE_ID, u.USER_ID as TEACHER_ID, u.NAME as teacherName, u.WORK_TYPE, mcc.COURSE_DATE, MAX(IF(dd.name = '一年级', mcsa.PEOPLE_AMOUNT, 0)) AS gradeOneAmount,  ");
		sql.append(" 	MAX(IF(dd.name = '二年级', mcsa.PEOPLE_AMOUNT, 0)) as gradeTwoAmount, MAX(IF(dd.name = '三年级', mcsa.PEOPLE_AMOUNT, 0)) as gradeThreeAmount, ");
		sql.append(" 	MAX(IF(dd.name = '四年级', mcsa.PEOPLE_AMOUNT, 0)) as gradeFourAmount, MAX(IF(dd.name = '五年级', mcsa.PEOPLE_AMOUNT, 0)) as gradeFiveAmount, ");
		sql.append(" 	MAX(IF(dd.name = '六年级', mcsa.PEOPLE_AMOUNT, 0)) as gradeSixAmount, MAX(IF(dd.name = '初一', mcsa.PEOPLE_AMOUNT, 0)) as gradeSevenAmount, ");
		sql.append(" 	MAX(IF(dd.name = '初二', mcsa.PEOPLE_AMOUNT, 0)) as gradeEightAmount, MAX(IF(dd.name = '初三', mcsa.PEOPLE_AMOUNT, 0)) as gradeNineAmount, ");
		sql.append(" 	MAX(IF(dd.name = '高一', mcsa.PEOPLE_AMOUNT, 0)) as gradeTenAmount, MAX(IF(dd.name = '高二', mcsa.PEOPLE_AMOUNT, 0)) as gradeElevenAmount, ");
		sql.append(" 	MAX(IF(dd.name = '高三', mcsa.PEOPLE_AMOUNT, 0)) as gradeTwelveAmount, mcsa.PEOPLE_AMOUNT AS PEOPLE_AMOUNT, org.name as courseCampusName, ");
		sql.append(" 	mc.STUDY_MANEGER_ID ");
		sql.append(" FROM mini_class_course mcc ");
		sql.append(" INNER JOIN ( SELECT count(mcsa.id) as PEOPLE_AMOUNT, mcc.MINI_CLASS_COURSE_ID  ");
		sql.append(" from MINI_CLASS_STUDENT_ATTENDENT mcsa INNER JOIN mini_class_course mcc on mcsa.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID ");
		sql.append(" WHERE 1 = 1 AND (mcsa.ATTENDENT_STATUS = 'CONPELETE' or  mcsa.SUPPLEMENT_DATE IS NOT NULL) ");
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" AND mcc.COURSE_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" AND mcc.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}		
		sql.append(" GROUP BY mcc.MINI_CLASS_COURSE_ID) mcsa ON mcc.MINI_CLASS_COURSE_ID = mcsa.MINI_CLASS_COURSE_ID ");
		sql.append(" LEFT JOIN data_dict dd on mcc.GRADE = dd.ID ");
		sql.append(" INNER JOIN mini_class mc on mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN user u on mcc.TEACHER_ID = u.USER_ID ");
		sql.append(" INNER JOIN  organization org on mc.BL_CAMPUS_ID  = org.id ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" AND mcc.COURSE_DATE >= :startDate2 ");
			params.put("startDate2", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" AND mcc.COURSE_DATE <= :endDate2 ");
			params.put("endDate2", endDate);
		}
		if(productQuarterSearch!=null && StringUtils.isNotBlank(productQuarterSearch)){
			//产品季度
			sql.append(" and mc.MINI_CLASS_ID in (select MINI_CLASS_ID from mini_class mc INNER JOIN product p on mc.produce_id=p.id where p.product_quarter_id= :productQuarter) ");
			params.put("productQuarter", productQuarterSearch);
		}
		sql.append(" GROUP BY mcc.MINI_CLASS_COURSE_ID) a ");
		sql.append(" INNER JOIN ref_user_org ruo on a.TEACHER_ID = ruo.USER_ID ");
		sql.append(" INNER JOIN organization o on ruo.CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on ruo.BRANCH_ID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group on ruo.GROUP_ID = org_group.id ");
		sql.append(" WHERE 1=1 ");

		if (OrganizationType.GROUNP.equals(organizationType)) {
//			sql.append(" AND ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" AND org_brench.id = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" AND o.id = :blCampusId ");
			params.put("blCampusId", blCampusId);
		} else if (OrganizationType.OTHER.equals(organizationType)) {
			sql.append(" AND a.TEACHER_ID = :blCampusId ");
			params.put("blCampusId", blCampusId);
		}
        sql.append(roleQLConfigService.getValueResult("老师小班课时分布统计(工资)","sql"));
		if (OrganizationType.GROUNP.equals(organizationType)) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (OrganizationType.BRENCH.equals(organizationType)) {
			sql.append(" GROUP BY o.id ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" GROUP BY a.TEACHER_ID,a.courseCampusName ");
		} else if (OrganizationType.OTHER.equals(organizationType)) {
			sql.append(" GROUP BY a.courseCampusName ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/* 
	 *市场统计
	 * @see com.eduboss.dao.ReportDao#getCustomerTotalByCusType(com.eduboss.domainVo.BasicOperationQueryVo, com.eduboss.dto.DataPackage)
	 */
	@Override
	public DataPackage getCustomerTotalByCusType(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer customerSql=new StringBuffer();
		customerSql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			customerSql.append("  CONCAT(groupp.id, '_' ,groupp.`name`) GROUNP,CONCAT(branch.id, '_' ,branch.`name`) BRENCH,'' CAMPUS,");
		}else{
			customerSql.append("  CONCAT(groupp.id, '_' ,groupp.`name`) GROUNP,CONCAT(branch.id, '_' ,branch.`name`) BRENCH, CONCAT(campus.id,'_',campus.`name`) CAMPUS,");
		}
		customerSql.append(" ifnull(sum(NEW_STUDENT),0) as students,ifnull(sum(CUS_COUNT),0)  customers, ifnull(sum(GROUP_CALL),0) groundCalling,ifnull(sum(INTERNET),0) internet, ");
		customerSql.append(" ifnull(sum(INTERNET_CALL),0) internetCalling,ifnull(sum(INTRODUCE),0) introduce,ifnull(sum(OUTCALLING),0) outCalling,ifnull(sum(STRANGE_CALL),0) strangeCall,ifnull(sum(TEL_CONSULTATION),0) telConsultation,ifnull(sum(VISIT),0) visit,ifnull(sum(OTHER),0) others ");
		customerSql.append(" from  organization as groupp,organization as branch, organization as campus  ");
		customerSql.append(" left join  ODS_DAY_CUSTYPE_TOTAL detail  on  detail.CAMPUS = campus.id ");
		customerSql.append(" where 1=1 and campus.parentID = branch.id and branch.parentID = groupp.id");
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getStartDate())){
			customerSql.append(" AND detail.COUNT_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getEndDate())){
			customerSql.append(" and detail.COUNT_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(BasicOperationQueryLevelType.GROUNP) && StringUtils.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {
			customerSql.append(" and branch.id = :blCampusId");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} 
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			customerSql.append(" and branch.orgType='BRENCH' ");
			
			//权限查询所属组织架构下的所有校区信息
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				customerSql.append("  and campus.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
				for(int i = 1; i < userOrganizations.size(); i++){
					customerSql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
				}
				customerSql.append(")");
			}
					
			customerSql.append("GROUP BY  groupp.id,branch.id order by sum(NEW_STUDENT) desc,branch.id desc");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			customerSql.append(" and campus.orgType='CAMPUS' ");
			//权限查询所属组织架构下的所有校区信息
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				customerSql.append("  and campus.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
				for(int i = 1; i < userOrganizations.size(); i++){
					customerSql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
				}
				customerSql.append(")");
			}
			
			customerSql.append("GROUP BY  groupp.id,branch.id, campus.id order by sum(NEW_STUDENT) desc,campus.id desc");
		} 
		List<Map<Object, Object>> list= super.findMapOfPageBySql(customerSql.toString(), dp, params);
		
		/**按  新生 获取 分公司的排名 开始*/
		StringBuffer sql_branch = new StringBuffer();
		Map<String, Object> branchParams = new HashMap<String, Object>();
		sql_branch.append(" select brench as BRANCH_ID from ODS_DAY_CUSTYPE_TOTAL where 1=1");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql_branch.append(" AND COUNT_DATE >= :startDate ");
			branchParams.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql_branch.append(" AND COUNT_DATE <= :endDate ");
			branchParams.put("endDate", basicOperationQueryVo.getEndDate());
		}
		sql_branch.append("  group by brench order by sum(new_student) desc,brench desc ");

		List<Map<Object, Object>> list_branch=super.findMapBySql(sql_branch.toString(), branchParams);
		Map<String, String> map_branch = new HashMap<String, String>();
		int branchSort=0;
		String BRANCH_ID ="";
		Object BRANCH_ID_VALUE="";
		for(Map branch:list_branch){
			branchSort++;
			Set<String> set = branch.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				BRANCH_ID = iterator.next();
				BRANCH_ID_VALUE=branch.get(BRANCH_ID);
				if(BRANCH_ID_VALUE!=null){
					map_branch.put( BRANCH_ID_VALUE.toString(), branchSort+"");
				}

			}

		}

		Map<String, Object> campusParams = new HashMap<String, Object>();
		StringBuffer sql_campus = new StringBuffer();
		sql_campus.append(" select campus as CAMPUS_ID from ODS_DAY_CUSTYPE_TOTAL WHERE 1=1 ");

		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql_campus.append(" AND COUNT_DATE >= :startDate ");
			campusParams.put("startDate", basicOperationQueryVo.getStartDate());
		} 
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql_campus.append(" AND COUNT_DATE <= :endDate ");
			campusParams.put("endDate", basicOperationQueryVo.getEndDate());
		}
		sql_campus.append(" group by campus order by sum(new_student) desc,campus desc ");
		
		List<Map<Object, Object>> list_campus=super.findMapBySql(sql_campus.toString(), campusParams);
		Map<String, String> map_campus = new HashMap<String, String>();

		int campusSort=0;
		String CAMPUS_ID ="";
		Object CAMPUS_ID_VALUE="";
		for(Map campus:list_campus){
			campusSort++;
			Set<String> set = campus.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				CAMPUS_ID = iterator.next();
				CAMPUS_ID_VALUE=campus.get(CAMPUS_ID);
				if(CAMPUS_ID_VALUE!=null){
					map_campus.put( CAMPUS_ID_VALUE.toString(), campusSort+"");
				}

			}

		}

		//添加分公司排名和校区排名后的list
		List<Map> list_new=new ArrayList<Map>();
		String brenchId="";//分公司
		String campusId="";//分校
		for(Map map:list){
					//获取分校区排名
					if(map.get("CAMPUS")!=null && !"".equals(map.get("CAMPUS"))){
						campusId=map.get("CAMPUS").toString();
						if(campusId.indexOf("_")!=-1){
							campusId=campusId.substring(0, campusId.indexOf("_"));
						}
						map.put("CAMPUS_SORT", map_campus.get(campusId)==null?"-":map_campus.get(campusId));
					}else{
						map.put("CAMPUS_SORT","-");
					}

					//获取分公司排名
					if(map.get("BRENCH")!=null && !"".equals(map.get("BRENCH"))){
						brenchId=map.get("BRENCH").toString();
						if(brenchId.indexOf("_")!=-1){
							brenchId=brenchId.substring(0, brenchId.indexOf("_"));
						}
						map.put("BRANCH_SORT", map_branch.get(brenchId)==null?"-":map_branch.get(brenchId));
					}else{
						map.put("BRANCH_SORT","-");
					}

					list_new.add(map);
		}
		
		Map<String,Map> targetMap=new HashMap();
		List<Map> returnDatas=new ArrayList<Map>();
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			for (Map map :list_new) {
				targetMap.put((String) map.get("BRANCH_SORT"), map);
			}
			
			for (int i=1; i<=list_branch.size(); i++ ) {
				if(targetMap.get("" + i)!=null)
				returnDatas.add(targetMap.get("" + i));
			}
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			for (Map map :list_new) {
				targetMap.put((String) map.get("CAMPUS_SORT"), map);
			}
			
			for (int i=1; i<=list_campus.size(); i++ ) {
				if(targetMap.get("" + i)!=null)
				returnDatas.add(targetMap.get("" + i));
			}
		} 
		dp.setDatas(returnDatas);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + customerSql.toString() + " ) countall ", params));
		return dp;
	}
	
	
	@Override
	public DataPackage getBoothNumUseRate(String startDate, String endDate,
			OrganizationType organizationType, String blCampusId,
			DataPackage dp, String searchType,String weekDay) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqlBuffer =new StringBuffer();
		sqlBuffer.append(" select ");
		if (OrganizationType.BRENCH.equals(organizationType)) {
			 sqlBuffer.append(" CONCAT(org_group.id,'_' ,org_group.`name`) GROUNP,");
				sqlBuffer.append(" CONCAT(org_brench.id,'_' ,org_brench.`name`) BRENCH,");
				sqlBuffer.append(" '' CAMPUS,");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sqlBuffer.append(" CONCAT(org_group.id,'_' ,org_group.`name`) GROUNP,");
			sqlBuffer.append(" CONCAT(org_brench.id,'_' ,org_brench.`name`) BRENCH,");
			sqlBuffer.append(" CONCAT(o.id,'_',o.`name`) CAMPUS,");
		}
		if (OrganizationType.BRENCH.equals(organizationType)) {
			sqlBuffer.append(" ifnull(( select sum(BOOTH_NUMBER) "
			 		+ "from office_space_manage osm left join organization o on o.id=osm.ORGANIZATION_ID  "
			 		+ "where o.parentId =org_brench.id  ");
	 		//权限查询所属组织架构下的所有校区信息
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				sqlBuffer.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
				for(int i = 1; i < userOrganizations.size(); i++){
					sqlBuffer.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
				}
				sqlBuffer.append(")");
			}
		  sqlBuffer.append(" group by o.parentId),0) as classroomNumber,");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sqlBuffer.append(" ifnull((select sum(BOOTH_NUMBER) from office_space_manage where ORGANIZATION_ID=c.BL_CAMPUS_ID group by ORGANIZATION_ID),0) as classroomNumber,");
		}
		 
		sqlBuffer.append(" SUM(ifnull(c.coursecount,0)) coursecount, ");
		sqlBuffer.append(" SUM(ifnull(c.first,0)) first, ");
		sqlBuffer.append(" SUM(ifnull(c.eight,0)) eight, ");
		sqlBuffer.append(" SUM(ifnull(c.ten,0)) ten, ");
		sqlBuffer.append(" SUM(ifnull(c.twenteen,0)) twenteen, ");
		sqlBuffer.append(" SUM(ifnull(c.thirteen,0)) thirteen, ");
		sqlBuffer.append(" SUM(ifnull(c.fifteen,0)) fifteen, ");
		sqlBuffer.append(" SUM(ifnull(c.seventeen,0)) seventeen, ");
		sqlBuffer.append(" SUM(ifnull(c.nineteen,0)) nineteen, ");
		sqlBuffer.append(" SUM(ifnull(c.tweentyone,0)) tweentyone   ");
        sqlBuffer.append(" from 	 organization org_group  ");    
        sqlBuffer.append(" left JOIN organization org_brench  on  org_brench.parentID = org_group.id   ");   
        sqlBuffer.append(" left JOIN organization o  on   o.parentID = org_brench.id   ");   
        sqlBuffer.append(" left JOIN  ");   
        sqlBuffer.append(" ( select c.BL_CAMPUS_ID, ");   
		
		sqlBuffer.append(" count(1) as coursecount,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=0 and substr(c.course_time,1,2)<8 then 1 else 0 end)) as first,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=8 and substr(c.course_time,1,2)<10 then 1 else 0 end)) as eight,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=10 and substr(c.course_time,1,2)<12 then 1 else 0 end)) as ten,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=12 and substr(c.course_time,1,2)<13 then 1 else 0 end)) as twenteen,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=13 and substr(c.course_time,1,2)<15 then 1 else 0 end)) as thirteen,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=15 and substr(c.course_time,1,2)<17 then 1 else 0 end)) as fifteen,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=17 and substr(c.course_time,1,2)<19 then 1 else 0 end)) as seventeen,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=19 and substr(c.course_time,1,2)<21 then 1 else 0 end)) as nineteen,");
		sqlBuffer.append(" sum((case when substr(c.course_time,1,2)>=21 and substr(c.course_time,1,2)<24 then 1 else 0 end)) as tweentyone");
		sqlBuffer.append(" from course  c");
		sqlBuffer.append(" WHERE 1=1");
		if (StringUtils.isNotBlank(startDate)) {
			sqlBuffer.append(" AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sqlBuffer.append(" AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotBlank(weekDay)) {
			String[] weekDays=weekDay.split(",");
			if(weekDays.length>0){
				sqlBuffer.append(" AND (weekday(c.COURSE_DATE) = :weekDay0 ");
				params.put("weekDay0", weekDays[0]);
				for (int i = 1; i < weekDays.length; i++) {
					sqlBuffer.append(" or weekday(c.COURSE_DATE) = :weekDay" + i);
					params.put("weekDay" + i, weekDays[i]);
				}
				sqlBuffer.append(" )");
			}
		}
		sqlBuffer.append("  group by c.BL_CAMPUS_ID) c on   c.BL_CAMPUS_ID = o.id   ");
		sqlBuffer.append("  where 1=1   and o.orgType='CAMPUS' ");
		 if (OrganizationType.CAMPUS.equals(organizationType) && (StringUtils.isBlank(searchType) || (StringUtils.isNotBlank(searchType) && "1".equals(searchType)))) {
				sqlBuffer.append(" AND org_brench.id = :blCampusId ");
				params.put("blCampusId", blCampusId);
			}
			//权限查询所属组织架构下的所有校区信息
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				sqlBuffer.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
				for(int i = 1; i < userOrganizations.size(); i++){
					sqlBuffer.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
				}
				sqlBuffer.append(")");
			}
		if (OrganizationType.BRENCH.equals(organizationType)) {
			sqlBuffer.append(" GROUP BY org_brench.id ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sqlBuffer.append(" GROUP BY o.id ");
		} 
		sqlBuffer.append(" order by org_brench.id ");
		List<Map<Object ,Object>> list= super.findMapOfPageBySql(sqlBuffer.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sqlBuffer.toString() + " ) countall ", params));
		return dp;
	}
	
	@Override
	public DataPackage getClassRoomUseRate(String startDate, String endDate,
			OrganizationType organizationType, String blCampusId,
			DataPackage dp, String searchType,String weekDay) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqlBuffer =new StringBuffer();
		sqlBuffer.append(" select ");
		 if (OrganizationType.BRENCH.equals(organizationType)) {
			 sqlBuffer.append(" CONCAT(org_group.id,'_' ,org_group.`name`) GROUNP,");
				sqlBuffer.append(" CONCAT(org_brench.id,'_' ,org_brench.`name`) BRENCH,");
				sqlBuffer.append(" '' CAMPUS,");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sqlBuffer.append(" CONCAT(org_group.id,'_' ,org_group.`name`) GROUNP,");
			sqlBuffer.append(" CONCAT(org_brench.id,'_' ,org_brench.`name`) BRENCH,");
			sqlBuffer.append(" CONCAT(o.id,'_',o.`name`) CAMPUS,");
		}
		sqlBuffer.append("sum(ifnull(coursecount,0)) as coursecount,");
		 if (OrganizationType.BRENCH.equals(organizationType)) {
			 sqlBuffer.append(" ifnull(( select sum(classroom_Number) "
			 		+ "from office_space_manage osm left join organization o on o.id=osm.ORGANIZATION_ID  "
			 		+ "where o.parentId =org_brench.id ");
			 		//权限查询所属组织架构下的所有校区信息
					List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
					if(userOrganizations != null && userOrganizations.size() > 0){
						Organization org = userOrganizations.get(0);
						sqlBuffer.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
						for(int i = 1; i < userOrganizations.size(); i++){
							sqlBuffer.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
						}
						sqlBuffer.append(")");
					}
				  sqlBuffer.append(" group by o.parentId ),0) as classroomNumber,");
	    } else if (OrganizationType.CAMPUS.equals(organizationType)) {
	    	sqlBuffer.append(" ifnull((select sum(classroom_Number) from office_space_manage where ORGANIZATION_ID=o.id group by ORGANIZATION_ID),0) as classroomNumber, ");
	    }
       sqlBuffer.append(" sum(ifnull(first,0)) as first,");
       sqlBuffer.append(" sum(ifnull(eight,0)) as eight,");
       sqlBuffer.append(" sum(ifnull(ten,0)) as ten,");
       sqlBuffer.append(" sum(ifnull(twenteen,0)) as twenteen,");
       sqlBuffer.append(" sum(ifnull(thirteen,0)) as thirteen,");
       sqlBuffer.append(" sum(ifnull(fifteen,0)) as fifteen,");
       sqlBuffer.append(" sum(ifnull(seventeen,0)) as seventeen,");
       sqlBuffer.append(" sum(ifnull(nineteen,0)) as nineteen,");
       sqlBuffer.append(" sum(ifnull(tweentyone,0)) as tweentyone ");
       
       sqlBuffer.append(" from organization org_group ");
       sqlBuffer.append(" left JOIN organization org_brench on org_brench.parentID = org_group.id   ");
       sqlBuffer.append(" left join organization o  on o.parentID = org_brench.id  ");
       sqlBuffer.append(" left join (select o.id as BL_CAMPUS_ID, ");
       
       sqlBuffer.append(" count(1) as coursecount,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=0 and substr(mcc.course_time,1,2)<8 then 1 else 0 end)) as first,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=8 and substr(mcc.course_time,1,2)<10 then 1 else 0 end)) as eight,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=10 and substr(mcc.course_time,1,2)<12 then 1 else 0 end)) as ten,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=12 and substr(mcc.course_time,1,2)<13 then 1 else 0 end)) as twenteen,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=13 and substr(mcc.course_time,1,2)<15 then 1 else 0 end)) as thirteen,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=15 and substr(mcc.course_time,1,2)<17 then 1 else 0 end)) as fifteen,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=17 and substr(mcc.course_time,1,2)<19 then 1 else 0 end)) as seventeen,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=19 and substr(mcc.course_time,1,2)<21 then 1 else 0 end)) as nineteen,");
		sqlBuffer.append(" sum((case when substr(mcc.course_time,1,2)>=21 and substr(mcc.course_time,1,2)<24 then 1 else 0 end)) as tweentyone");
		
		sqlBuffer.append(" from mini_class_course  mcc");
		sqlBuffer.append(" left join mini_class mc on mc.MINI_CLASS_ID=mcc.MINI_CLASS_ID");
		sqlBuffer.append(" left JOIN organization o  on mc.BL_CAMPUS_ID = o.id  ");
		sqlBuffer.append(" left JOIN organization org_brench on o.parentID = org_brench.id");
		sqlBuffer.append(" left JOIN organization org_group on org_brench.parentID = org_group.id ");
    	sqlBuffer.append(" WHERE 1=1 ");
    	if (StringUtils.isNotBlank(startDate)) {
			sqlBuffer.append(" AND mcc.COURSE_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sqlBuffer.append(" AND mcc.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotBlank(weekDay)) {
			String[] weekDays=weekDay.split(",");
			if(weekDays.length>0){
				sqlBuffer.append(" AND (weekday(mcc.COURSE_DATE) = :weekDay0 ");
				params.put("weekDay0", weekDays[0]);
				for (int i = 1; i < weekDays.length; i++) {
					sqlBuffer.append(" or weekday(mcc.COURSE_DATE) = :weekDay" + i);
					params.put("weekDay" + i, weekDays[i]);
				}
				sqlBuffer.append(" )");
			}
		}
		sqlBuffer.append("  GROUP BY o.id ) AS detail on detail.BL_CAMPUS_ID = o.id where 1=1 ");
//    	sqlBuffer.append(" GROUP BY mc.BL_CAMPUS_ID ) mcc on mcc.BL_CAMPUS_ID = o.id");
    	
//		sqlBuffer.append(" WHERE 1=1");
		
		
		sqlBuffer.append(" and o.orgType='CAMPUS' ");
		if (OrganizationType.CAMPUS.equals(organizationType) && (StringUtils.isBlank(searchType) || (StringUtils.isNotBlank(searchType) && "1".equals(searchType)))) {
			sqlBuffer.append(" AND org_brench.id = :blCampusId ");
			params.put("blCampusId", blCampusId);
		}

		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sqlBuffer.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sqlBuffer.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sqlBuffer.append(")");
		}
		if (OrganizationType.BRENCH.equals(organizationType)) {
			sqlBuffer.append(" GROUP BY org_brench.id ");
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sqlBuffer.append(" GROUP BY o.id  ");
		} 
		sqlBuffer.append(" order by org_brench.id ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sqlBuffer.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sqlBuffer.toString() + " ) countall ", params));
		return dp;
	}
	
	@Override
	/**
	 * 小班未报读信息 
	 * @param basicOperationQueryVo
	 * @param dp
	 * @return
	 */
	public DataPackage getNotSignMiniProductInfo(BasicOperationQueryVo basicOperationQueryVo,
			MiniClassProductSearchVo miniClassProductSearchVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH,  '' CAMPUS,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH,  CONCAT(o.id, '_', o.`name`) CAMPUS,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH,  CONCAT(o.id, '_', o.`name`) CAMPUS,  ");
		}  else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH,  CONCAT(o.id, '_', o.`name`) CAMPUS,  ");
		}
		String currentDate = DateTools.getCurrentDate();
		sql.append(" 	sum(case when p.PRO_STATUS <> 1 and p.VALID_END_DATE >= '" + currentDate + "' then 1 else 0 end) as NOT_PAST_PEOPLE_AMOUNT, count(1) TOTAL__PEOPLE_AMOUNT,  ");
		sql.append(" 	count(DISTINCT case when p.PRO_STATUS <> 1 and p.VALID_END_DATE >= '" + currentDate + "' then p.id else NULL end) as NOT_PAST_PRODUCT_AMOUNT, COUNT(DISTINCT p.id) as TOTAL__PRODUCT_AMOUNT  ");
		sql.append(" 	from contract_product cp ");
		sql.append(" 	inner join product p on cp.PRODUCT_ID = p.ID ");
		sql.append(" 	inner join contract c on cp.CONTRACT_ID = c.ID ");
		sql.append(" 	inner join student s on c.STUDENT_ID = s.ID ");
		sql.append(" 	inner join organization o on s.BL_CAMPUS_ID = o.id  ");
		sql.append(" 	inner join organization org_brench on o.parentID = org_brench.id ");
		sql.append(" 	inner join organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" 	where cp.ID not in(select contract_product_id from mini_class_student WHERE CREATE_TIME >= '"+basicOperationQueryVo.getStartDate()+" 00:00:000' )  ");
		sql.append(" 	and cp.type='SMALL_CLASS' and cp.STATUS <> 'CLOSE_PRODUCT' and cp.STATUS <> 'ENDED' ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND cp.CREATE_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND cp.CREATE_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if (StringUtil.isNotBlank(miniClassProductSearchVo.getProductVersionId())) {
			sql.append("  and p.PRODUCT_VERSION_ID = :productVersionId ");
			params.put("productVersionId", miniClassProductSearchVo.getProductVersionId());
		}
		if (StringUtil.isNotBlank(miniClassProductSearchVo.getProductQuarterId())) {
			sql.append("  and p.PRODUCT_QUARTER_ID = :productQuarterId ");
			params.put("productQuarterId", miniClassProductSearchVo.getProductQuarterId());
		}

		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
				&& !Dimensionality.CAMPUS_DIM.equals(basicOperationQueryVo.getDimensionality())) {
			sql.append(" and org_brench.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} 
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("小班未报读信息","sql","s.BL_CAMPUS_ID"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id, org_brench.id, o.id ");
		} 
		sql.append(" order by org_group.id, org_brench.id, o.id ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 小班指标添统计
	 */
	@Override
	public DataPackage getMiniClassIndexAnylize(BasicOperationQueryVo basicOperationQueryVo, MiniClassProductSearchVo miniClassProductSearchVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		boolean blCampusIdWhere = true;
		if (StringUtils.isNotBlank(basicOperationQueryVo.getTeacherId()) || StringUtils.isNotBlank(basicOperationQueryVo.getStudyManegerId()) 
				|| StringUtils.isNotBlank(basicOperationQueryVo.getMiniClassName())) {
			basicOperationQueryVo.setBasicOperationQueryLevelType(BasicOperationQueryLevelType.CAMPUS);
			blCampusIdWhere = false;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.ID, '_', org_group.`NAME`) groupId, CONCAT(org_brench.ID, '_', org_brench.`NAME`) brenchId, '' campusId, "
					+ " '' as teacherId, '' as teacherCampus, '' as workType, '' as miniClassName, "
					+ " '' as productVersionName, '' as productQuarterNmae,'' as studyManageId,'' as gradeName, "
					+ " '' as classTime,  '' as status, '' as startDate,0 classMember,0 realClassMember,0 member, ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.ID, '_', org_group.`NAME`) groupId, CONCAT(org_brench.ID, '_', org_brench.`NAME`) brenchId, CONCAT(o.ID, '_', o.`NAME`) campusId, "
					+ " '' as teacherId, '' as teacherCampus, '' as workType, '' as miniClassName, "
					+ " '' as productVersionName, '' as productQuarterNmae,'' as studyManageId,'' as gradeName, "
					+ " '' as classTime,  '' as status, '' as startDate,0 classMember,0 realClassMember,0 member, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.ID, '_', org_group.`NAME`) groupId, CONCAT(org_brench.ID, '_', org_brench.`NAME`) brenchId, CONCAT(o.ID, '_', o.`NAME`) campusId, "
					+ " CONCAT(u_teacher.USER_ID, '_', u_teacher.`NAME`) as teacherId, org_teacher.`NAME` as teacherCampus, u_teacher.WORK_TYPE as workType, mc.`NAME` as miniClassName, "
					+ " dd_version.`NAME` as productVersionName, dd_quarter.`NAME` as productQuarterNmae, CONCAT(u_study.USER_ID, '_', u_study.`NAME`) as studyManageId, dd_grade.`NAME` as gradeName, "
					+ " mc.CLASS_TIME as classTime,  mc.`STATUS` as status, mc.START_DATE as startDate,");

					sql.append(" ifnull(sum(cm.CLASS_MEMBER),0) classMember,mc.MINI_CLASS_ID, ifnull(sum(mc.PEOPLE_QUANTITY), 0) as member,");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.ID, '_', org_group.`NAME`) groupId, CONCAT(org_brench.ID, '_', org_brench.`NAME`) brenchId, CONCAT(o.ID, '_', o.`NAME`) campusId, "
					+ " CONCAT(u_teacher.USER_ID, '_', u_teacher.`NAME`) as teacherId, org_teacher.`NAME` as teacherCampus, u_teacher.WORK_TYPE as workType, mc.`NAME` as miniClassName, "
					+ " dd_version.`NAME` as productVersionName, dd_quarter.`NAME` as productQuarterNmae, CONCAT(u_study.USER_ID, '_', u_study.`NAME`) as studyManageId, dd_grade.`NAME` as gradeName, "
					+ " mc.CLASS_TIME as classTime,  mc.`STATUS` as status, mc.START_DATE as startDate, ");
			sql.append(" ifnull(sum(cm.CLASS_MEMBER),0) classMember,mc.MINI_CLASS_ID, ifnull(sum(mc.PEOPLE_QUANTITY), 0) as member,");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.ID, '_', org_group.`NAME`) groupId, CONCAT(org_brench.ID, '_', org_brench.`NAME`) brenchId, CONCAT(o.ID, '_', o.`NAME`) campusId, "
					+ " CONCAT(u_teacher.USER_ID, '_', u_teacher.`NAME`) as teacherId, org_teacher.`NAME` as teacherCampus, u_teacher.WORK_TYPE as workType, mc.`NAME` as miniClassName, "
					+ " dd_version.`NAME` as productVersionName, dd_quarter.`NAME` as productQuarterNmae, CONCAT(u_study.USER_ID, '_', u_study.`NAME`) as studyManageId, dd_grade.`NAME` as gradeName, "
					+ " mc.CLASS_TIME as classTime,  mc.`STATUS` as status, mc.START_DATE as startDate, ");
			sql.append(" ifnull(sum(cm.CLASS_MEMBER),0) classMember,mc.MINI_CLASS_ID, ifnull(sum(mc.PEOPLE_QUANTITY), 0) as member,");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.ID, '_', org_group.`NAME`) groupId, CONCAT(org_brench.ID, '_', org_brench.`NAME`) brenchId, CONCAT(o.ID, '_', o.`NAME`) campusId, "
					+ " CONCAT(u_teacher.USER_ID, '_', u_teacher.`NAME`) as teacherId, org_teacher.`NAME` as teacherCampus, u_teacher.WORK_TYPE as workType, mc.`NAME` as miniClassName, "
					+ " dd_version.`NAME` as productVersionName, dd_quarter.`NAME` as productQuarterNmae, CONCAT(u_study.USER_ID, '_', u_study.`NAME`) as studyManageId, dd_grade.`NAME` as gradeName, "
					+ " mc.CLASS_TIME as classTime,  mc.`STATUS` as status, mc.START_DATE as startDate, ");
			sql.append(" ifnull(sum(cm.CLASS_MEMBER),0) classMember,mc.MINI_CLASS_ID, ifnull(sum(mc.PEOPLE_QUANTITY), 0) as member,");
		}
		sql.append(" ifnull(sum(mc.TOTAL_CLASS_HOURS / mc.EVERY_COURSE_CLASS_NUM), 0) totalClassTimes, ifnull(sum(mc.EVERY_COURSE_CLASS_NUM), 0) as everyCourseClassNum, ifnull(sum(mc.PEOPLE_QUANTITY), 0) as peopleQuantity,  ");
		sql.append(" ifnull(sum(mcs.recruitAmount), 0) recruitAmount, ifnull(sum(mcsa.theClassNumber), 0) as theClassNumber, ifnull(sum(mcsa.attendentAmount), 0) as attendentAmount, ");
		sql.append(" ifnull(sum(mcr.continueAmount), 0) as continueAmount, ifnull(sum(mcr.extendAmount), 0) as extendAmount ");
		sql.append(" from mini_class mc ");
		sql.append(" inner join organization o on mc.BL_CAMPUS_ID = o.ID ");
		sql.append(" inner join organization org_brench on o.parentID = org_brench.ID ");
		sql.append(" inner join organization org_group on org_brench.parentID = org_group.ID ");
		sql.append(" inner join user u_teacher on mc.teacher_id = u_teacher.USER_ID ");
		sql.append(" inner join user u_study on mc.STUDY_MANEGER_ID = u_study.USER_ID ");
		sql.append(" inner join organization org_teacher on u_teacher.organizationID = org_teacher.id ");
		sql.append(" inner join product p on mc.PRODUCE_ID = p.id ");
		sql.append(" left join CLASSROOM_MANAGE cm on cm.id = mc.CLASSROOM");
		sql.append(" left join data_dict dd_version on p.PRODUCT_VERSION_ID = dd_version.ID ");
		sql.append(" left join data_dict dd_quarter on p.PRODUCT_QUARTER_ID = dd_quarter.ID ");
		sql.append(" left join data_dict dd_grade on mc.GRADE = dd_grade.ID ");
		sql.append(" left join (select MINI_CLASS_ID, count(1) as recruitAmount from mini_class_student group by MINI_CLASS_ID) mcs on mc.MINI_CLASS_ID = mcs.MINI_CLASS_ID ");
		sql.append(" left join (select mcc.MINI_CLASS_ID, count(1) as theClassNumber, sum(case when mcsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end) as attendentAmount ");
		sql.append(" 		from mini_class_student_attendent mcsa left join mini_class_course mcc on mcsa.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID  ");
		sql.append(" 		inner join mini_class mc on mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID  ");
		sql.append(" 		where mcc.COURSE_STATUS = 'CHARGED'  ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" 		AND mc.START_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" 		AND mc.START_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		sql.append(" 		group by mcc.MINI_CLASS_ID) mcsa on mc.MINI_CLASS_ID = mcsa.MINI_CLASS_ID ");
		sql.append(" left join (select NEW_MINI_CLASS_ID, sum(case when RELATION_TYPE = 'EXTEND' then 1 else 0 end) as extendAmount, ");
		sql.append(" 		sum(case when RELATION_TYPE = 'CONTINUE' then 1 else 0 end) as continueAmount from mini_class_relation group by NEW_MINI_CLASS_ID) mcr on mc.MINI_CLASS_ID = mcr.NEW_MINI_CLASS_ID  ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND mc.START_DATE >= :startDate2 ");
			params.put("startDate2", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND mc.START_DATE <= :endDate2 ");
			params.put("endDate2", basicOperationQueryVo.getEndDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getTeacherId())) {
			sql.append(" AND mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", basicOperationQueryVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStudyManegerId())) {
			sql.append(" AND mc.STUDY_MANEGER_ID = :studyManagerId ");
			params.put("studyManagerId", basicOperationQueryVo.getStudyManegerId());
		}
		if (null != basicOperationQueryVo.getMiniClassStatus() && StringUtils.isNotBlank(basicOperationQueryVo.getMiniClassStatus().getValue())) {
			sql.append(" AND mc.`STATUS` = :miniClassStatus ");
			params.put("miniClassStatus", basicOperationQueryVo.getMiniClassStatus());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getMiniClassName())) {
			sql.append(" AND mc.`NAME` like :miniClassName ");
			params.put("miniClassName", "%" + basicOperationQueryVo.getMiniClassName() + "%");
		}
		if (StringUtil.isNotBlank(miniClassProductSearchVo.getProductVersionId())) {
			sql.append("  and p.PRODUCT_VERSION_ID = :productVersionId ");
			params.put("productVersionId", miniClassProductSearchVo.getProductVersionId());
		}
		if (StringUtil.isNotBlank(miniClassProductSearchVo.getProductQuarterId())) {
			sql.append("  and p.PRODUCT_QUARTER_ID = :productQuaterId ");
			params.put("productQuaterId", miniClassProductSearchVo.getProductQuarterId());
		}

		if (StringUtil.isNotBlank(basicOperationQueryVo.getMiniClassTypeId())){
			String miniClassTypeId = basicOperationQueryVo.getMiniClassTypeId();
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID = :miniClassType0 ");
				params.put("miniClassType0", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sql.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					params.put("miniClassType" + i, miniClassTypes[i]);
				}
				sql.append(" )");
			}
		}
		
		if (StringUtils.isNotBlank(basicOperationQueryVo.getOrganizationId())) {
			sql.append(" AND mc.BL_CAMPUS_ID = :organizationId ");
			params.put("organizationId", basicOperationQueryVo.getOrganizationId());
		}
		
		if (blCampusIdWhere) {
			if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//				sql.append(" AND ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
					sql.append(" AND org_brench.id = :blCampusId ");
					params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
					sql.append(" AND o.id = :blCampusId ");
					params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
					sql.append(" AND u_teacher.USER_ID = :blCampusId ");
					params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
					sql.append(" AND u_study.USER_ID = :blCampusId ");
					params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				}
			} 
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("小班指标添统计","sql","mc.BL_CAMPUS_ID"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" GROUP BY mc.MINI_CLASS_ID ");
		}
		sql.append("order by org_brench.id, o.id, mc.MINI_CLASS_ID, u_teacher.USER_ID");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	
	}
	
	
	@Override
	public DataPackage getAllTotalForMobile(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer customerSql=new StringBuffer();
		customerSql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			customerSql.append("  CONCAT(o.id, '_' ,o.`name`) GROUNP,CONCAT(o2.id, '_' ,o2.`name`) BRENCH,'' CAMPUS,");
		}else{
			customerSql.append("  CONCAT(o.id, '_' ,o.`name`) GROUNP,CONCAT(o2.id, '_' ,o2.`name`) BRENCH, CONCAT(o3.id,'_',o3.`name`) CAMPUS,");
		}
		customerSql.append(" ifnull(sum(stu.students),0) newStudent,ifnull(sum(cus.customer),0) cusCount,ifnull(sum(income.incomeMoney),0) realIncome,ifnull(sum(money.money),0) paidTotalAmount,ifnull(sum(contract.contracts),0) contracts" +
				",ifnull(sum(money.onlineAmount),0) onlineAmount" +
				",ifnull(sum(money.lineAmount),0) lineAmount" +
				" from organization o");
		
		customerSql.append(" ,organization o2,organization o3    ");
		customerSql.append(" left join (SELECT ");
		customerSql.append("     bl_campus_id, COUNT(1) students");
		customerSql.append(" FROM");
		customerSql.append("     student");
		customerSql.append(" WHERE 1=1 ");
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getStartDate())){
			customerSql.append(" AND create_time >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getEndDate())){
			customerSql.append(" and create_time <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59.59");
		}
		customerSql.append(" GROUP BY bl_campus_id) stu on  o3.id = stu.bl_campus_id");
		customerSql.append(" left join (SELECT ");
		customerSql.append("     bl_school, COUNT(1) customer");
		customerSql.append(" FROM");
		customerSql.append("     customer");
		customerSql.append(" WHERE 1=1 ");
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getStartDate())){
			customerSql.append(" AND create_time >= :startDate2 ");
			params.put("startDate2", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getEndDate())){
			customerSql.append(" and create_time <= :endDate2 ");
			params.put("endDate2", basicOperationQueryVo.getEndDate() + " 23:59.59");
		}
		customerSql.append(" GROUP BY bl_school) cus on o3.id = cus.bl_school");
		
		customerSql.append(" left join (select campus_id campusId,sum(inc.realTotalAmount) incomeMoney from income_count_campus inc ");
		customerSql.append(" WHERE   1 = 1");
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getStartDate())){
			customerSql.append(" AND inc.count_date >= :startDate3 ");
			params.put("startDate3", basicOperationQueryVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getEndDate())){
			customerSql.append(" and inc.count_date <= :endDate3 ");
			params.put("endDate3", basicOperationQueryVo.getEndDate());
		}
		customerSql.append(" GROUP BY campus_id) income  on  o3.id = income.campusId");
		customerSql.append(" left join (");
		customerSql.append(" 	SELECT fun.campus_id BL_CAMPUS_ID, SUM(fun.count_paid_total_amount) money, SUM(fun.online_amount) onlineAmount, SUM(fun.line_Amount) lineAmount FROM finance_campus fun WHERE 1 = 1 ");
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getStartDate())){
			customerSql.append(" AND fun.count_date >= :startDate4 ");
			params.put("startDate4", basicOperationQueryVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getEndDate())){
			customerSql.append(" and fun.count_date <= :endDate4 ");
			params.put("endDate4", basicOperationQueryVo.getEndDate());
		}
		customerSql.append(" GROUP BY fun.campus_id) money on o3.id = money.BL_CAMPUS_ID");
		
		customerSql.append(" left join (        ");
		customerSql.append(" select BL_CAMPUS_ID,count(1) contracts");
		customerSql.append(" 	from contract");
		customerSql.append(" where 1=1 ");
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getStartDate())){
			customerSql.append(" AND CREATE_TIME >= :startDate5 ");
			params.put("startDate5", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotEmpty(basicOperationQueryVo.getEndDate())){
			customerSql.append(" and CREATE_TIME <= :endDate5 ");
			params.put("endDate5", basicOperationQueryVo.getEndDate() + " 23:59.59");
		}
		customerSql.append(" group by BL_CAMPUS_ID) contract on o3.id=contract.BL_CAMPUS_ID");
		customerSql.append(" ");
		customerSql.append(" where o.id=o2.parentID and o2.id=o3.parentID");
		customerSql.append(" and o3.orgType='CAMPUS'");
		
		
		if (BasicOperationQueryLevelType.GROUNP.equals(BasicOperationQueryLevelType.GROUNP) && StringUtils.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {
			customerSql.append(" and o2.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} 
		
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			customerSql.append("  and o3.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				customerSql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			customerSql.append(")");
		}
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			customerSql.append("GROUP BY  o2.id order by sum(money.money) desc,o2.id desc");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			customerSql.append("GROUP BY  o3.id order by sum(money.money) desc,o3.id desc");
		} 
		List<Map<Object, Object>> list= super.findMapOfPageBySql(customerSql.toString(), dp, params);
		String startDate=basicOperationQueryVo.getStartDate();
		String endDate=basicOperationQueryVo.getEndDate();
		Map<String, Integer> map_branch =getFinanceBrenchRank(startDate, endDate);
		Map<String,Integer> map_campus=getFinanceCampusRank(startDate, endDate);
		Map<String, String> month_rate_map=new HashMap<String,String>();
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			month_rate_map = getOrganizationTargetValue(basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000251", "BRENCH");
		} else {
			month_rate_map = getOrganizationTargetValue(basicOperationQueryVo.getStartDate(), basicOperationQueryVo.getEndDate(), "DAT0000000251", "CAMPUS");
		}
		/**月度计划指标 结束*/

		//添加分公司排名和校区排名后的list
		List<Map> list_new=new ArrayList<Map>();
		String brenchId="";//分公司
		String campusId="";//分校
		for(Map map:list){
					//获取分校区排名
					if(map.get("CAMPUS")!=null && !"".equals(map.get("CAMPUS"))){
						campusId=map.get("CAMPUS").toString();
						if(campusId.indexOf("_")!=-1){
							campusId=campusId.substring(0, campusId.indexOf("_"));
						}
						map.put("CAMPUS_SORT", map_campus.get(campusId)==null?"-":map_campus.get(campusId));
						
						
						if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
								&& month_rate_map != null) {
							map.put("target_value", month_rate_map.get(campusId)==null?0:month_rate_map.get(campusId));
						}
					}else{
						map.put("CAMPUS_SORT","-");
					}

					//获取分公司排名
					if(map.get("BRENCH")!=null && !"".equals(map.get("BRENCH"))){
						brenchId=map.get("BRENCH").toString();
						if(brenchId.indexOf("_")!=-1){
							brenchId=brenchId.substring(0, brenchId.indexOf("_"));
						}
						map.put("BRANCH_SORT", map_branch.get(brenchId)==null?"-":map_branch.get(brenchId));
						if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())&& month_rate_map != null) {
							map.put("target_value", month_rate_map.get(brenchId)==null?0:month_rate_map.get(brenchId));
						}
					}else{
						map.put("BRANCH_SORT","-");
					}
					list_new.add(map);
		}
		dp.setDatas(list_new);
		dp.setRowCount(list_new.size());
		return dp;
	}
	
	/**
	 * 手机端课程统计接口
	 */
	@Override
	public DataPackage getCoursesAnalyzeForMobile(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(org_group.id, '_',org_group.name) AS groupId,CONCAT(org_brench.id, '_',org_brench.name) AS brenchId, '' as campusId,    ");
		} else {
			sql.append(" 	CONCAT(org_group.id, '_',org_group.name) AS groupId,CONCAT(org_brench.id, '_',org_brench.name) AS brenchId,CONCAT(o.id, '_',o.name) AS campusId,  ");
		}
		sql.append(" 	truncate(sum(case when COURSE_STATUS = 'NEW' then c.plan_hours  else 0 end),2) as newCourseCount,  ");//未考勤
		sql.append(" 	truncate(sum(case when COURSE_STATUS = 'TEACHER_ATTENDANCE' then c.real_hours else 0 end),2) as attendanceCourseCount,  ");//已考勤
		sql.append(" 	truncate(sum(case when COURSE_STATUS = 'STUDY_MANAGER_AUDITED' then c.real_hours else 0 end),2) as auditedCourseCount, ");//已确认
		sql.append(" 	truncate(sum(case when COURSE_STATUS = 'CHARGED' then c.real_hours else 0 end),2) as chargeCourseCount ");//已扣费
		sql.append("  from organization o  ");
		sql.append("  inner join organization org_brench on o.parentID = org_brench.id ");
		sql.append("  inner join organization org_group on org_brench.parentID = org_group.id ");
		sql.append("  left join course   c on o.id = c.BL_CAMPUS_ID ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append("  AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append("  AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		if (StringUtil.isNotEmpty(basicOperationQueryVo.getBlCampusId())) {//查询Id不等于空的时候
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and org_brench.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
				sql.append(" and o.id = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		sql.append("  and o.orgType='CAMPUS' ");
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by org_group.id , org_brench.id ");
		} else {
			sql.append(" group by org_group.id , org_brench.id, o.id ");
		}
		sql.append(" ORDER BY count(1) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	@Override
	public DataPackage getStudentOOOStatusDetail(
			BasicOperationQueryVo bo,
			String oooStatus, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT o.name brenchName,o1.name campusName,u.name studyManegerName,ooo_status oooStatus,REMAINING_HOUR oooRemainingHour,student_name studentName ,COUNT_DATE countDate FROM ods_day_ooo_student s ");
		sql.append(" left join organization o on s.brench_id=o.id ");
		sql.append(" left join  organization o1 on s.campus_id=o1.id ");
		sql.append(" left join user u on u.user_id =s.STUDY_MANaGER_ID where 1=1 ");
		if(StringUtil.isNotBlank(oooStatus)){
			if(oooStatus.equals("ALL")){
				sql.append(" and s.OOO_STATUS in('NEW','CLASSING','STOP_CLASS','SPECIAL_STOP')");
			}else{
				sql.append(" and s.OOO_STATUS = :oooStatus");
				params.put("oooStatus", oooStatus);
			}
		}
		if(StringUtil.isNotBlank(bo.getCountDate())){
			sql.append(" and s.count_date = :countDate ");
			params.put("countDate", bo.getCountDate());
		}
		if(StringUtil.isNotBlank(bo.getOrganizationId())){
			sql.append(" and s.BRENCH_ID = :brenchId ");
			params.put("brenchId", bo.getOrganizationId());
		}
		if(StringUtil.isNotBlank(bo.getBlCampusId())){
			sql.append(" and s.CAMPUS_ID = :campusId ");
			params.put("campusId", bo.getBlCampusId());
		}
		if(StringUtil.isNotBlank(bo.getStudyManegerId())){
			if(bo.getStudyManegerId().equals("hasNone")){
				sql.append(" and s.STUDY_MANAGER_ID is null ");
			}else{
				sql.append(" and s.STUDY_MANAGER_ID = :studyManagerId ");
				params.put("studyManagerId", bo.getStudyManegerId());
			}
		}
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (CAMPUS_ID in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(") or STUDY_MANAGER_ID='"+userService.getCurrentLoginUser().getUserId()+"') ");
		}
		sql.append(" order by study_manager_id desc ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	
	@Override
	public DataPackage getStudentOTMStatusDetail(
			BasicOperationQueryVo bo,
			String oooStatus, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT o.name brenchName,o1.name campusName,u.name studyManegerName,otm_status oooStatus,REMAINING_HOUR oooRemainingHour,student_name studentName ,COUNT_DATE countDate FROM ods_day_otm_student s ");
		sql.append(" left join organization o on s.brench_id=o.id ");
		sql.append(" left join  organization o1 on s.campus_id=o1.id ");
		sql.append(" left join user u on u.user_id =s.STUDY_MANaGER_ID where 1=1 ");
		if(StringUtil.isNotBlank(oooStatus)){
			if(oooStatus.equals("ALL")){
				sql.append(" and s.otm_status in('NEW','CLASSING','STOP_CLASS','SPECIAL_STOP')");
			}else{
				sql.append(" and s.otm_status = :oooStatus ");
				params.put("oooStatus", oooStatus);
			}
		}
		if(StringUtil.isNotBlank(bo.getCountDate())){
			sql.append(" and s.count_date = :countDate ");
			params.put("countDate", bo.getCountDate());
		}
		if(StringUtil.isNotBlank(bo.getOrganizationId())){
			sql.append(" and s.BRENCH_ID = :brenchId ");
			params.put("brenchId", bo.getOrganizationId());
		}
		if(StringUtil.isNotBlank(bo.getBlCampusId())){
			sql.append(" and s.CAMPUS_ID = :campusId ");
			params.put("campusId", bo.getBlCampusId());
		}
		if(StringUtil.isNotBlank(bo.getStudyManegerId())){
			if(bo.getStudyManegerId().equals("hasNone")){
				sql.append(" and s.STUDY_MANAGER_ID is null ");
			}else{
				sql.append(" and s.STUDY_MANAGER_ID = :studyManagerId ");
				params.put("studyManagerId", bo.getStudyManegerId());
			}
		}
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and (CAMPUS_ID in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(") or STUDY_MANAGER_ID='"+userService.getCurrentLoginUser().getUserId()+"') ");
		}
		sql.append(" order by study_manager_id desc ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	@Override
	public DataPackage getConsultResourceUse(
			BasicOperationQueryVo basicOperationQueryVo, DataPackage dp,String entranceIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql =new StringBuffer();
		sql.append(" select ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" concat(grounp.id,'_',grounp.name) grounpName,concat(brench.id,'_',brench.name) brenchName,"
					+ " '' campusName,'' userName,'' ddName,");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" concat(grounp.id,'_',grounp.name) grounpName,concat(brench.id,'_',brench.name) brenchName,"
					+ "concat(u.dept_id,'_',u.campusName) campusName,'' userName,'' ddName,");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" concat(grounp.id,'_',grounp.name) grounpName,concat(brench.id,'_',brench.name) brenchName,"
					+ "concat(u.dept_id,'_',u.campusName) campusName,u.userName,u.ddName,");
		}
		sql.append(" sum(ifnull(a.sourceNum,0)) sourceNum,sum(ifnull(b.comeNum,0)) comeNum,sum(ifnull(c.signNum,0)) signNum");
		sql.append("  from organization grounp,organization brench,");
		
		//组织架构过滤以及用户和资源入口类型
		sql.append(" (select (case when o.orgType='CAMPUS' THEN o.id WHEN o.orgType='DEPARTMENT' then o.belong else o.parentID end) dept_id,u.USER_ID,u.NAME userName,dd.NAME ddName,dd.VALUE,dd.CATEGORY");
		sql.append(" ,(case when o.orgType='CAMPUS' THEN o.name WHEN o.orgType='DEPARTMENT' THEN (select name from organization where id=o.belong) else '' end) campusName");
		sql.append(" ,(case when o.orgType='CAMPUS' THEN o.parentID WHEN o.orgType='DEPARTMENT' THEN (select parentId from organization where id=o.belong) else '' end) parentID");
		sql.append(" from data_dict dd,user u,user_dept_job udj ,organization o");
		//USE0000000003   生产咨询师职位ID
		sql.append(" where u.USER_ID=udj.user_id and  category='RES_ENTRANCE' and udj.job_id='USE0000000003' and o.id=udj.DEPT_ID ");
		if(StringUtils.isNotBlank(entranceIds)){		
			String[] entranceId=entranceIds.split(",");
			if(entranceId.length>0){
				sql.append(" AND (dd.value = :entranceId0 ");
				params.put("entranceId0", entranceId[0]);
				for (int i = 1; i < entranceId.length; i++) {
					sql.append(" or dd.value = :entranceId" + i);
					params.put("entranceId" + i, entranceId[i]);
				}
				sql.append(" )");
			}
		
		}
		 if(StringUtils.isNotBlank(basicOperationQueryVo.getOrganizationId())){
			 if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())
						&& Dimensionality.CAMPUS_DIM.equals(basicOperationQueryVo.getDimensionality())) {
				
			 } else {
					 Organization o = organizationDao.findById(basicOperationQueryVo.getOrganizationId());
			    	 if(o!=null)
			    	 sql.append(" and udj.dept_id in (select id from organization where orgLevel like '"+o.getOrgLevel()+"%')");
			}
	     }
		//下面要处理查询参数
	     List<Organization> orgs = userService.getCurrentLoginUser().getOrganization();
	     if(orgs.size()>0){//当前用户所有组织架构权限的数据
	    	 sql.append(" and udj.dept_id in (select id from organization where ");
	    	 int i=0;
	    	 for (Organization organization : orgs) {
	    		 if(i==0)
	    			 sql.append(" orgLevel like '"+organization.getOrgLevel()+"%'");
	    		 else
	    			 sql.append(" or orgLevel like '"+organization.getOrgLevel()+"%'");
	    		 i++;
			}
	    	  sql.append(" )) u");
	     }
		//组织架构过滤以及用户和资源入口类型 end 
		
		//资源数量 （单独查询条件）
		sql.append(" left join (select cds.REFERUSER_ID,c.RES_ENTRANCE,count(distinct CUSTOMER_ID) sourceNum from customer c ");
		sql.append(" left join customer_dynamic_status cds on cds.customer_id=c.id");
		sql.append(" where 1=1 and cds.DYNAMIC_STATUS_TYPE='FOLLOWING'");
		if(StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())){
			sql.append(" and OCCOUR_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())){
			sql.append(" and OCCOUR_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(StringUtils.isNotBlank(entranceIds)){
			String[] entranceId=entranceIds.split(",");
			if(entranceId.length>0){
				sql.append(" AND (c.RES_ENTRANCE = :cEntranceId0 ");
				params.put("cEntranceId0", entranceId[0]);
				for (int i = 1; i < entranceId.length; i++) {
					sql.append(" or c.RES_ENTRANCE = :cEntranceId" + i);
					params.put("cEntranceId" + i, entranceId[i]);
				}
				sql.append(" )");
			}
		}
		sql.append(" group by cds.REFERUSER_ID,c.RES_ENTRANCE) a on  u.USER_ID=a.REFERUSER_ID and u.value=a.RES_ENTRANCE");
		
		//上门量（单独查询条件）
		sql.append(" left join (select cfc.CREATE_USER_ID,c.RES_ENTRANCE,count(distinct CUSTOMER_ID) comeNum from customer c ");
		sql.append(" left join customer_folowup cfc on cfc.customer_id=c.id");
		sql.append(" where 1=1 and cfc.VISIT_TYPE='PARENT_COME'");
		if(StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())){
			sql.append(" and cfc.MEETING_CONFIRM_TIME >= :startDate2");
			params.put("startDate2", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())){
			sql.append(" and cfc.MEETING_CONFIRM_TIME <= :endDate2");
			params.put("endDate2", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(StringUtils.isNotBlank(entranceIds)){		
			String[] entranceId=entranceIds.split(",");
			if(entranceId.length>0){
				sql.append(" AND (c.RES_ENTRANCE = :c2EntranceId0 ");
				params.put("c2EntranceId0", entranceId[0]);
				for (int i = 1; i < entranceId.length; i++) {
					sql.append(" or c.RES_ENTRANCE = :c2EntranceId" + i);
					params.put("c2EntranceId" + i, entranceId[i]);
				}
				sql.append(" )");
			}
		}
		sql.append(" group by cfc.CREATE_USER_ID,c.RES_ENTRANCE) b  on u.USER_ID=b.CREATE_USER_ID and u.value=b.RES_ENTRANCE");
		//签约量（单独查询条件）
		sql.append(" left join (select  con.CREATE_USER_ID,c.RES_ENTRANCE,count(distinct c.id) signNum from contract con");
		sql.append(" left join customer c on c.id=con.CUSTOMER_ID where 1=1");
		if(StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())){
			sql.append(" and  con.CREATE_TIME >= :startDate3 ");
			params.put("startDate3", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())){
			sql.append(" and  con.CREATE_TIME <= :endDate3 ");
			params.put("endDate3", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(StringUtils.isNotBlank(entranceIds)){		
			String[] entranceId=entranceIds.split(",");
			if(entranceId.length>0){
				sql.append(" AND (c.RES_ENTRANCE = :c3EntranceId0 ");
				params.put("c3EntranceId0", entranceId[0]);
				for (int i = 1; i < entranceId.length; i++) {
					sql.append(" or c.RES_ENTRANCE = :c3EntranceId" + i);
					params.put("c3EntranceId" + i, entranceId[i]);
				}
				sql.append(" )");
			}
		}
		sql.append(" group by con.CREATE_USER_ID,c.RES_ENTRANCE) c on u.USER_ID=c.CREATE_USER_ID and u.value=c.RES_ENTRANCE");
		sql.append(" where category='RES_ENTRANCE' and grounp.orgType='GROUNP' and brench.orgType='BRENCH' ");
		sql.append(" and brench.parentID=grounp.id and u.parentID=brench.id");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by  grounp.name, brench.name  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by  grounp.name, brench.name ,u.campusName ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by  grounp.name, brench.name ,u.campusName,u.userName,u.ddName ");
		} 
		sql.append("  order by brench.name,u.campusName,u.userName,u.ddName");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	@Override
	public Boolean getStudentDetailStatu(String countDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="select * from ods_day_ooo_student ";
		if(StringUtil.isNotBlank(countDate)){
			sql+="where count_date = :countDate ";
			params.put("countDate", countDate);
		}
		List list = super.findMapBySql(sql, params);
		 if(list.size()>0){
			 return true;
		 }else{
			 return false;
		 }
	}
	
	@Override
	public Boolean getOtmStudentDetailStatu(String countDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="select * from ods_day_otm_student ";
		if(StringUtil.isNotBlank(countDate)){
			sql+="where count_date = :countDate ";
			params.put("countDate", countDate);
		}
		List list = super.findMapBySql(sql, params);
		 if(list.size()>0){
			 return true;
		 }else{
			 return false;
		 }
	}

	/**
	 * 获取一对多课消统计
	 * @param vo
	 * @param otmTypeStr
	 * @param dp
     * @return
     */
	public DataPackage getOtmClassAttendsAnalyze(BasicOperationQueryVo vo, String otmTypeStr, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		boolean blCampusIdWhere = true;
		if (StringUtils.isNotBlank(vo.getOrganizationId()) || StringUtils.isNotBlank(vo.getTeacherId()) || StringUtils.isNotBlank(vo.getStudyManegerId()) || StringUtils.isNotBlank(vo.getOtmClassName())) {
			vo.setBasicOperationQueryLevelType( BasicOperationQueryLevelType.CAMPUS);
			blCampusIdWhere = false;
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' OTM_CLASS_COURSE_ID, '' OTM_CLASS_NAME, '' GRADE, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append("  CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER, '' TEACHER_BLCAMPUS_NAME, '' WORK_TYPE,"
					+ " '' OTM_CLASS_COURSE_ID, '' OTM_CLASS_NAME, '' GRADE, '' COURSE_DATE, '' COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " a.OTM_CLASS_COURSE_ID OTM_CLASS_COURSE_ID, oc.NAME OTM_CLASS_NAME, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " a.OTM_CLASS_COURSE_ID OTM_CLASS_COURSE_ID, oc.NAME OTM_CLASS_NAME, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " a.OTM_CLASS_COURSE_ID OTM_CLASS_COURSE_ID, oc.NAME OTM_CLASS_NAME, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sql.append(" CONCAT(org_group.id, '_', org_group.`name`) GROUNP, CONCAT(org_brench.id, '_', org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(e.USER_ID, '_', e.NAME) TEACHER, org.NAME TEACHER_BLCAMPUS_NAME, e.WORK_TYPE WORK_TYPE,"
					+ " a.OTM_CLASS_COURSE_ID OTM_CLASS_COURSE_ID, oc.NAME OTM_CLASS_NAME, h.NAME GRADE, a.COURSE_DATE COURSE_DATE, a.COURSE_TIME  COURSE_TIME,  ");
		}
		sql.append(" ifnull(sum(a.COURSE_HOURS), 0) COURSE_HOURS, ifnull(sum(aq.PEOPLE_QUANTITY), 0) PEOPLE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.attendentQuatity), 0)  ATTENDENT_QUANTITY, ifnull(sum(aq.lateQuatity), 0) LATE_QUANTITY, ");
		sql.append(" ifnull(sum(aq.absentQuatity), 0) ABSENT_QUANTITY, ");
		sql.append(" ifnull(sum(CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = a.TEACHER_ID  AND tv.VERSION_DATE <= a.COURSE_DATE) AND TEACHER_ID = a.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN aq.chargeQuatity ELSE IF(aq.chargeQuatity -1 >= 0, aq.chargeQuatity -1, 0) END), 0) SALARY_QUANTITY, ");
		User currentLoginUser = userService.getCurrentLoginUser();
		String loginUserRoleStr = currentLoginUser.getRoleCode();
		sql.append(" ifnull(sum(aq.chargeQuatity), 0) CHARGE_QUANTITY, ifnull(sum(aq.unChargeQuatity), 0) UNCHARGE_QUANTITY, ");
		sql.append(" sum(acr.totalAmount) as CHARGE_AMOUNT ");

		sql.append(" FROM otm_class_course a ");
		sql.append("  LEFT JOIN (  select OTM_CLASS_COURSE_ID, sum(amount) as totalAmount  from  account_charge_records ");
		sql.append("  WHERE 1=1 AND OTM_CLASS_COURSE_ID IS NOT NULL  and CHARGE_PAY_TYPE='CHARGE' and IS_WASHED='FALSE' ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND TRANSACTION_TIME >= :startDate ");
			params.put("startDate", vo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND TRANSACTION_TIME <= :endDate ");
			params.put("endDate", vo.getEndDate() + " 23:59:99");
		}
		sql.append(" GROUP BY OTM_CLASS_COURSE_ID ) acr  on a.OTM_CLASS_COURSE_ID = acr.OTM_CLASS_COURSE_ID  ");
		sql.append(" INNER JOIN otm_class oc ON a.OTM_CLASS_ID = oc.OTM_CLASS_ID ");
		sql.append(" INNER JOIN organization o ON oc.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" INNER JOIN user e  ON e.USER_ID = a.TEACHER_ID ");
		sql.append(" INNER JOIN organization org on e.organizationID = org.id ");
		sql.append(" LEFT JOIN data_dict h ON h.ID = a.GRADE ");
		sql.append(" LEFT JOIN (SELECT a.OTM_CLASS_COURSE_ID, SUM(CASE WHEN ocsa.ATTENDENT_STATUS = 'CONPELETE' THEN 1 ELSE 0 END) as attendentQuatity, ");
		sql.append(" SUM(CASE WHEN ocsa.ATTENDENT_STATUS = 'LATE' THEN 1 ELSE 0 END) as lateQuatity, ");//迟到
		sql.append(" SUM(CASE WHEN ocsa.ATTENDENT_STATUS = 'ABSENT' THEN 1 ELSE 0 END) as absentQuatity, ");
		sql.append(" SUM(CASE WHEN ocsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END) as chargeQuatity, ");
		sql.append(" SUM(CASE WHEN ocsa.CHARGE_STATUS = 'UNCHARGE' THEN 1 ELSE 0 END) as unChargeQuatity, ");
//		sql.append(" ifnull((SUM(CASE WHEN ocsa.ATTENDENT_STATUS = 'CONPELETE' OR ocsa.ATTENDENT_STATUS='LATE' THEN 1 ELSE 0 END) - 1), 0) as salaryQuantity, "); //计算工资人数
		sql.append(" COUNT(1) as PEOPLE_QUANTITY ");
		sql.append(" FROM otm_class_student_attendent ocsa ");
		sql.append(" INNER join otm_class_course a on ocsa.OTM_CLASS_COURSE_ID = a.OTM_CLASS_COURSE_ID ");		
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate2 ");
			params.put("startDate2", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate2 ");
			params.put("endDate2", vo.getEndDate());
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		if (StringUtils.isNotBlank(vo.getOtmClassName())) {
			sql.append(" AND a.OTM_CLASS_NAME like :otmClassName ");
			params.put("otmClassName", "%" + vo.getOtmClassName() + "%");
		}
		sql.append(" GROUP BY a.OTM_CLASS_COURSE_ID) aq on a.OTM_CLASS_COURSE_ID = aq.OTM_CLASS_COURSE_ID ");
		StringBuffer sqlWhere=new StringBuffer();
		sqlWhere.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(vo.getStartDate())) {
			sqlWhere.append(" AND a.COURSE_DATE >= :startDate3 ");
			params.put("startDate3", vo.getStartDate());
		}
		if (StringUtils.isNotBlank(vo.getEndDate())) {
			sqlWhere.append(" AND a.COURSE_DATE <= :endDate3 ");
			params.put("endDate3", vo.getEndDate());
		}
		if (StringUtils.isNotBlank(vo.getTeacherId())) {
			sqlWhere.append(" AND a.TEACHER_ID = :teacherId2 ");
			params.put("teacherId2", vo.getTeacherId());
		}
		if (StringUtils.isNotBlank(vo.getOtmClassName())) {
			sqlWhere.append(" AND a.OTM_CLASS_NAME like :otmClassName2 ");
			params.put("otmClassName2", "%" + vo.getOtmClassName() + "%");
		}
		sqlWhere.append(" AND a.COURSE_STATUS = 'CHARGED' ");
		if (StringUtils.isNotBlank(vo.getOrganizationId())) {
			sqlWhere.append(" AND oc.BL_CAMPUS_ID like :organizationId ");
			params.put("organizationId", vo.getOrganizationId());
		}
		if(StringUtils.isNotBlank(otmTypeStr)){			
			String[] otmTypes = otmTypeStr.split(",");
			if(otmTypes.length>0){
				sql.append(" AND (oc.otmType = :otmType0 ");
				params.put("otmType0", otmTypes[0]);
				for (int i = 1; i < otmTypes.length; i++) {
					sql.append(" or oc.otmType = :otmType" + i);
					params.put("otmType" + i, otmTypes[i]);
				}
				sql.append(" )");
			}
		}
		if (blCampusIdWhere) {
			if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
			} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
				if (StringUtil.isNotBlank(vo.getBlCampusId())) {
					sqlWhere.append(" AND org_brench.id = :blCampusId ");
					params.put("blCampusId", vo.getBlCampusId());
				}
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND e.USER_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
				sqlWhere.append(" AND oc.OTM_CLASS_ID = :blCampusId ");
				params.put("blCampusId", vo.getBlCampusId());
			}
		}

		sqlWhere.append(roleQLConfigService.getAppendSqlByAllOrg("一对多课消","sql","o.id"));
		
		StringBuffer sqlGroupBy=new StringBuffer();
		if (BasicOperationQueryLevelType.GROUNP.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY org_brench.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY oc.OTM_CLASS_ID, a.OTM_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.TEACHER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY oc.OTM_CLASS_ID, a.OTM_CLASS_COURSE_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(vo.getBasicOperationQueryLevelType())) {
			sqlGroupBy.append(" GROUP BY oc.OTM_CLASS_ID, a.OTM_CLASS_COURSE_ID ");
		}
		sql.append(sqlWhere);
		sql.append(sqlGroupBy);
		sql.append("order by org_brench.id, o.id, oc.OTM_CLASS_ID, e.USER_ID, a.COURSE_DATE, a.COURSE_TIME");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 老师一对多课时年级分布视图
	 * @param courseConsumeTeacherVo
	 * @param otmTypeStr
	 * @param dp
     * @return
     */
	@Override
	public DataPackage getOtmCourseConsumeTeacherView(CourseConsumeTeacherVo courseConsumeTeacherVo, String otmTypeStr, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID,  ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(a.courseCampusName,'') COURSE_CAMPUS_ID, ");
			sql.append(" a.teacherLevel,a.teacherType,");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(a.courseCampusName,'') COURSE_CAMPUS_ID, ");
		}
		sql.append(" 	SUM(a.courseHours) TOTAL_CONSUME_HOUR, ");
		sql.append(" 	SUM(a.gradeOneHours) FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeTwoHours) SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeThreeHours) THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeFourHours) FOURTH_GRADE, ");
		sql.append(" 	SUM(a.gradeFiveHours) FIFTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSixHours) SIXTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSevenHours) MIDDLE_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeEightHours) MIDDLE_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeNineHours) MIDDLE_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeTenHours) HIGH_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeElevenHours) HIGH_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeTwelveHours) HIGH_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.otherHours) OTHER_GRADE ");
		sql.append(" FROM ");
		sql.append(" 	(SELECT DISTINCT occ.OTM_CLASS_COURSE_ID, u.USER_ID as TEACHER_ID, u.NAME as teacherName, u.employee_No, u.WORK_TYPE, occ.COURSE_DATE, MAX(IF(dd.name = '一年级', occ.COURSE_HOURS, 0)) AS gradeOneHours,  ");
		sql.append(" 	MAX(IF(dd.name = '二年级', occ.COURSE_HOURS, 0)) as gradeTwoHours, MAX(IF(dd.name = '三年级', occ.COURSE_HOURS, 0)) as gradeThreeHours, ");
		sql.append(" 	MAX(IF(dd.name = '四年级', occ.COURSE_HOURS, 0)) as gradeFourHours, MAX(IF(dd.name = '五年级', occ.COURSE_HOURS, 0)) as gradeFiveHours, ");
		sql.append(" 	MAX(IF(dd.name = '六年级', occ.COURSE_HOURS, 0)) as gradeSixHours, MAX(IF(dd.name = '初一', occ.COURSE_HOURS, 0)) as gradeSevenHours, ");
		sql.append(" 	MAX(IF(dd.name = '初二', occ.COURSE_HOURS, 0)) as gradeEightHours, MAX(IF(dd.name = '初三', occ.COURSE_HOURS, 0)) as gradeNineHours, ");
		sql.append(" 	MAX(IF(dd.name = '高一', occ.COURSE_HOURS, 0)) as gradeTenHours, MAX(IF(dd.name = '高二', occ.COURSE_HOURS, 0)) as gradeElevenHours, ");
		sql.append(" 	MAX(IF(dd.name = '高三', occ.COURSE_HOURS, 0)) as gradeTwelveHours, occ.COURSE_HOURS as courseHours, org.name as courseCampusName, ");
		sql.append("    oc.BL_CAMPUS_ID as BL_CAMPUS_ID , ");
		sql.append(" 	MAX(IF(dd.name is null, occ.COURSE_HOURS, 0)) as otherHours");
		
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date <= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherLevel,");
    	params.put("endDate", courseConsumeTeacherVo.getEndDate());
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date <= :endDate2 and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherType");
    	params.put("endDate2", courseConsumeTeacherVo.getEndDate());
		
		sql.append(" FROM otm_class_course occ ");
		sql.append(" INNER JOIN otm_class_student_attendent ocsa ON occ.OTM_CLASS_COURSE_ID = ocsa.OTM_CLASS_COURSE_ID ");
		sql.append(" LEFT JOIN data_dict dd on occ.GRADE = dd.ID ");
		sql.append(" INNER JOIN otm_class oc on occ.OTM_CLASS_ID = oc.OTM_CLASS_ID  ");
		sql.append(" INNER JOIN user u on occ.TEACHER_ID = u.USER_ID ");
		sql.append(" INNER JOIN  organization org on oc.BL_CAMPUS_ID  = org.id ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND occ.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" AND ocsa.CHARGE_STATUS = 'CHARGED' ");
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getStartDate())) {
			sql.append(" AND occ.COURSE_DATE >= :startDate ");
			params.put("startDate", courseConsumeTeacherVo.getStartDate());
		}
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getEndDate())) {
			sql.append(" AND occ.COURSE_DATE <= :endDate3 ");
			params.put("endDate3", courseConsumeTeacherVo.getEndDate());
		}
		if (StringUtil.isNotBlank(courseConsumeTeacherVo.getSubject())){
			String[] subjects=courseConsumeTeacherVo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject" + i);
						params.put("subject" + i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}
		sql.append(" GROUP BY occ.OTM_CLASS_COURSE_ID) a ");
		sql.append(" INNER JOIN ref_user_org ruo on a.TEACHER_ID = ruo.USER_ID ");
		sql.append(" INNER JOIN organization o on ruo.CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on ruo.BRANCH_ID = org_brench.id ");
		sql.append(" LEFT JOIN organization org_group on ruo.GROUP_ID = org_group.id ");
		sql.append(" WHERE 1=1 ");
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
//			sql.append(" AND ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND org_brench.id = :blCampusId ");
			params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBlCampusId())){
				sql.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
			}

			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBranchId())){
				sql.append(" AND org_brench.id = :branchId");
				params.put("branchId", courseConsumeTeacherVo.getBranchId());
			}

			if(StringUtils.isNotBlank(courseConsumeTeacherVo.getTeacherType())){
				sql.append(" and a.teacherType = :teacherType ");
				params.put("teacherType", courseConsumeTeacherVo.getTeacherType());
			}
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
			params.put("teacherId", courseConsumeTeacherVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("老师一对多课时分布统计","sql","o.id"));
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY o.id ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY a.TEACHER_ID,a.courseCampusName ");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY a.courseCampusName ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 老师一对多课时年级分布视图(小时)
	 * @param courseConsumeTeacherVo
	 * @param o
	 * @param dp
     * @return
     */
	@Override
	public DataPackage getOtmCourseConsumeTeacherViewHours(CourseConsumeTeacherVo courseConsumeTeacherVo, Object o, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, '' CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID,  ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, '' TEACHER_NAME, '' EMPLOYEE_NO, '' WORK_TYPE, '' COURSE_CAMPUS_ID, ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(a.courseCampusName,'') COURSE_CAMPUS_ID, ");
			sql.append(" a.teacherLevel,a.teacherType,");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" 	CONCAT(org_group.id, '_' , org_group.`name`) GROUNP, CONCAT(org_brench.id, '_' ,org_brench.`name`) BRENCH, CONCAT(o.id, '_', o.`name`) CAMPUS, CONCAT(a.TEACHER_ID, '_', a.teacherName)  TEACHER_NAME, a.employee_No EMPLOYEE_NO, case when a.WORK_TYPE = 'FULL_TIME'  then '全职' else '兼职' end WORK_TYPE,  IFNULL(a.courseCampusName,'') COURSE_CAMPUS_ID, ");
		}
		sql.append(" 	SUM(a.courseHours) TOTAL_CONSUME_HOUR, ");
		sql.append(" 	SUM(a.gradeOneHours) FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeTwoHours) SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeThreeHours) THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeFourHours) FOURTH_GRADE, ");
		sql.append(" 	SUM(a.gradeFiveHours) FIFTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSixHours) SIXTH_GRADE, ");
		sql.append(" 	SUM(a.gradeSevenHours) MIDDLE_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeEightHours) MIDDLE_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeNineHours) MIDDLE_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.gradeTenHours) HIGH_SCHOOL_FIRST_GRADE, ");
		sql.append(" 	SUM(a.gradeElevenHours) HIGH_SCHOOL_SECOND_GRADE, ");
		sql.append(" 	SUM(a.gradeTwelveHours) HIGH_SCHOOL_THIRD_GRADE, ");
		sql.append(" 	SUM(a.otherHours) OTHER_GRADE ");
		sql.append(" FROM ");
		sql.append(" 	(SELECT DISTINCT occ.OTM_CLASS_COURSE_ID, u.USER_ID as TEACHER_ID, u.NAME as teacherName, u.employee_No, u.WORK_TYPE, occ.COURSE_DATE, MAX(IF(dd.name = '一年级',ROUND(occ.COURSE_HOURS * IFNULL(occ.COURSE_MINUTES,0)/60,2) , 0)) AS gradeOneHours,  ");
		sql.append(" 	MAX(IF(dd.name = '二年级', ROUND(occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60,2), 0)) as gradeTwoHours, MAX(IF(dd.name = '三年级', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeThreeHours, ");
		sql.append(" 	MAX(IF(dd.name = '四年级', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeFourHours, MAX(IF(dd.name = '五年级', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeFiveHours, ");
		sql.append(" 	MAX(IF(dd.name = '六年级', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeSixHours, MAX(IF(dd.name = '初一', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeSevenHours, ");
		sql.append(" 	MAX(IF(dd.name = '初二', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeEightHours, MAX(IF(dd.name = '初三', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeNineHours, ");
		sql.append(" 	MAX(IF(dd.name = '高一', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeTenHours, MAX(IF(dd.name = '高二', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeElevenHours, ");
		sql.append(" 	MAX(IF(dd.name = '高三', occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as gradeTwelveHours, occ.COURSE_HOURS* IFNULL(occ.COURSE_MINUTES,0)/60 as courseHours, org.name as courseCampusName, ");
		sql.append("    oc.BL_CAMPUS_ID as BL_CAMPUS_ID , ");
		sql.append(" 	MAX(IF(dd.name is null, occ.COURSE_HOURS*IFNULL(occ.COURSE_MINUTES,0)/60, 0)) as otherHours");
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date <= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherLevel,");
    	params.put("endDate", courseConsumeTeacherVo.getEndDate());
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date <= :endDate2 and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherType");
    	params.put("endDate2", courseConsumeTeacherVo.getEndDate());
    	sql.append(" FROM otm_class_course occ ");
		sql.append(" INNER JOIN account_charge_records acc ON acc.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
		sql.append(" INNER JOIN otm_class_student_attendent ocsa ON occ.OTM_CLASS_COURSE_ID = ocsa.OTM_CLASS_COURSE_ID ");
		sql.append(" LEFT JOIN data_dict dd on occ.GRADE = dd.ID ");
		sql.append(" INNER JOIN otm_class oc on occ.OTM_CLASS_ID = oc.OTM_CLASS_ID  ");
		sql.append(" INNER JOIN user u on occ.TEACHER_ID = u.USER_ID ");
		sql.append(" INNER JOIN  organization org on oc.BL_CAMPUS_ID  = org.id ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND occ.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" AND ocsa.CHARGE_STATUS = 'CHARGED' ");
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getStartDate())) {
			sql.append(" AND occ.COURSE_DATE >= :startDate ");
			params.put("startDate", courseConsumeTeacherVo.getStartDate());
		}
		if (StringUtils.isNotBlank(courseConsumeTeacherVo.getEndDate())) {
			sql.append(" AND occ.COURSE_DATE <= :endDate3 ");
			params.put("endDate3", courseConsumeTeacherVo.getEndDate());
		}
		if (StringUtil.isNotBlank(courseConsumeTeacherVo.getSubject())){
			String[] subjects=courseConsumeTeacherVo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject" + i);
						params.put("subject" + i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}
		sql.append(" GROUP BY occ.OTM_CLASS_COURSE_ID) a ");
		sql.append(" INNER JOIN ref_user_org ruo on a.TEACHER_ID = ruo.USER_ID ");
		sql.append(" INNER JOIN organization o on ruo.CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench on ruo.BRANCH_ID = org_brench.id ");
		sql.append(" LEFT JOIN organization org_group on ruo.GROUP_ID = org_group.id ");
		sql.append(" WHERE 1=1 ");
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
//			sql.append(" AND ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND org_brench.id = :blCampusId ");
			params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBlCampusId())){
				sql.append(" AND o.id = :blCampusId ");
				params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
			}

			if (StringUtils.isNotBlank(courseConsumeTeacherVo.getBranchId())){
				sql.append(" AND org_brench.id = :branchId ");
				params.put("branchId", courseConsumeTeacherVo.getBranchId());
			}
			if(StringUtils.isNotBlank(courseConsumeTeacherVo.getTeacherType())){
				sql.append(" and a.teacherType = :teacherType ");
				params.put("teacherType", courseConsumeTeacherVo.getTeacherType());
			}
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" AND a.TEACHER_ID = :blCampusId ");
			params.put("blCampusId", courseConsumeTeacherVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("老师一对多课时分布统计","sql","o.id"));
		if (OrganizationType.GROUNP.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY org_brench.id ");
		} else if (OrganizationType.BRENCH.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY o.id ");
		} else if (OrganizationType.CAMPUS.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY a.TEACHER_ID,a.courseCampusName ");
		} else if (OrganizationType.OTHER.equals(courseConsumeTeacherVo.getOrganizationType())) {
			sql.append(" GROUP BY a.courseCampusName ");
		}
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	/**
	 * 一对多剩余资金
	 * @param basicOperationQueryVo
	 * @param dp
	 * @param currentLoginUser
	 * @param currentLoginUserCampus
	 * @return
	 */
	public DataPackage getOtmStudentRemainAnalyze(BasicOperationQueryVo basicOperationQueryVo,
													   DataPackage dp, User currentLoginUser,
													   Organization currentLoginUserCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, '' as campusId,  '' as userId,   ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, '' as userId, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.user_id = f.STAFF_ID)) as userId, ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(f.GROUP_ID, '_', (select o.name from organization o where o.id = f.GROUP_ID)) as groupId, CONCAT(f.BRANCH_ID, '_', (select o.name from organization o where o.id = f.BRANCH_ID)) as brenchId, CONCAT(f.CAMPUS_ID, '_', (select o.name from organization o where o.id = f.CAMPUS_ID)) as campusId, CONCAT(f.STAFF_ID, '_', (select u.name from user u where u.USER_ID = f.STAFF_ID)) as userId, CONCAT(f.STU_ID, '_', (select s.name from student s where s.ID = f.STU_ID)) as stuId, ");
		}
		sql.append(" 	sum(f.ONE_TO_TWO_REMAINING_AMOUNT) as oneToTwoRemainAmount, ");
		sql.append(" 	sum(f.ONE_TO_TWO_REMAINING_HOUR) as oneToTwoRemainHours,   ");
		sql.append(" 	sum(f.ONE_TO_THREE_REMAINING_AMOUNT) as oneToThreeRemainAmount, ");
		sql.append(" 	sum(f.ONE_TO_THREE_REMAINING_HOUR) as oneToThreeRemainHours, ");
		sql.append(" 	sum(f.ONE_TO_FOUR_REMAINING_AMOUNT) as oneToFourRemainAmount, ");
		sql.append(" 	sum(f.ONE_TO_FOUR_REMAINING_HOUR) as oneToFourRemainHours, ");
		sql.append(" 	sum(f.ONE_TO_FIVE_REMAINING_AMOUNT) as oneToFiveRemainAmount, ");
		sql.append(" 	sum(f.ONE_TO_FIVE_REMAINING_HOUR) as oneToFiveRemainHours ");
		sql.append(" FROM ods_real_otm_student_remaining f ");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
//			sql.append(" AND ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.BRANCH_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and f.CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			if(basicOperationQueryVo.getBlCampusId().indexOf("-")==0){
				sql.append(" and f.CAMPUS_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId().replace("-", ""));
				sql.append(" and (f.STAFF_ID='' or f.STAFF_ID is null ) ");
			}else{
				sql.append(" and f.STAFF_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("一对多剩余资金统计","sql","f.CAMPUS_ID"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID, f.STU_ID ");
		}
		sql.append(" order by f.GROUP_ID, f.BRANCH_ID, f.CAMPUS_ID, f.STAFF_ID desc , f.STU_ID desc ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	/**
	 * 财务审批进度
	 */
	@Override
	public DataPackage fundsChangeHistoryAuditList(String startDate,
			String endDate, String channel,DataPackage dp,String orgType,String groupById,String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select ");
		if (BasicOperationQueryLevelType.GROUNP.toString().equals(orgType) ){
			sql.append(" 	CONCAT(c.grounpId,'_',c.grounpName) as GROUNP,CONCAT(c.brenchId,'_',c.brenchName) as BRENCH, '' as CAMPUS,  '' as userName, '' as channel,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.toString().equals(orgType) ){
			sql.append(" 	CONCAT(c.grounpId,'_',c.grounpName) as GROUNP,CONCAT(c.brenchId,'_',c.brenchName) as BRENCH, CONCAT(c.campusId,'_',c.campusName) as CAMPUS, '' as userName, '' as channel, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.toString().equals(orgType) ){
			sql.append(" 	CONCAT(c.grounpId,'_',c.grounpName) as GROUNP,CONCAT(c.brenchId,'_',c.brenchName) as BRENCH, CONCAT(c.campusId,'_',c.campusName) as CAMPUS,CONCAT(u.user_id,'_',u.name) as userName, '' as channel, ");
		} else if (BasicOperationQueryLevelType.USER.toString().equals(orgType) ) {
			sql.append(" 	CONCAT(c.grounpId,'_',c.grounpName) as GROUNP,CONCAT(c.brenchId,'_',c.brenchName) as BRENCH, CONCAT(c.campusId,'_',c.campusName) as CAMPUS,CONCAT(u.user_id,'_',u.name) as userName, CONCAT(f.CHANNEL) as channel, ");
		}
		sql.append(" count(1) as allNums, "); //总笔数
		sql.append(" SUM(case when funds_pay_type ='WASH' THEN -TRANSACTION_AMOUNT ELSE TRANSACTION_AMOUNT END) as allAmount, "); //总金额
		sql.append(" SUM(CASE WHEN AUDIT_STATUS = 'VALIDATE' THEN 1 ELSE 0 END) as validateNums, ");// 审核通过笔数
		sql.append(" SUM(CASE WHEN AUDIT_STATUS = 'VALIDATE'  and FUNDS_PAY_TYPE='WASH' THEN -transaction_amount WHEN AUDIT_STATUS = 'VALIDATE' and FUNDS_PAY_TYPE<>'WASH' THEN transaction_amount   ELSE 0 END) as validateAmount ,");
		sql.append(" SUM(CASE WHEN AUDIT_STATUS = 'UNVALIDATE' THEN 1 else 0 END ) as unAuditNums, ");// 审核未通过笔数
		sql.append(" SUM(CASE WHEN AUDIT_STATUS = 'UNVALIDATE' and FUNDS_PAY_TYPE='WASH' THEN -transaction_amount WHEN AUDIT_STATUS = 'UNVALIDATE' and FUNDS_PAY_TYPE<>'WASH' THEN transaction_amount else 0 END ) as unAuditAmount, ");
		sql.append(" SUM(CASE WHEN AUDIT_STATUS = 'UNAUDIT' THEN 1 else 0 END ) as notAuditNums, ");// 未审核笔数
		sql.append(" SUM(CASE WHEN AUDIT_STATUS = 'UNAUDIT' and FUNDS_PAY_TYPE='WASH' THEN -transaction_amount WHEN AUDIT_STATUS = 'UNAUDIT' and FUNDS_PAY_TYPE<>'WASH' THEN transaction_amount ELSE 0 END) as notAuditAmount ");
		sql.append(" from funds_change_history f ");
		sql.append(" INNER JOIN ( ");
		sql.append(" select o.id as campusId,o.name as campusName,b.id as brenchId,b.name as brenchName,g.id as grounpId,g.name as grounpName,c.* ");
			sql.append(" from contract c ");
			sql.append(" INNER JOIN organization o on c.BL_CAMPUS_ID=o.id  ");
			sql.append(" INNER JOIN organization b on o.parentID = b.id ");
			sql.append(" INNER JOIN organization g on b.parentID = g.id ");
			sql.append(" where 1=1 and o.orgType='campus' ");
			if (BasicOperationQueryLevelType.BRENCH.toString().equals(orgType) ){
	            sql.append(" and b.id = :groupById ");
	            params.put("groupById", groupById);
	        } else if (BasicOperationQueryLevelType.CAMPUS.toString().equals(orgType) ){
	            sql.append(" and o.id = :groupById ");
	            params.put("groupById", groupById);
	        } else if (BasicOperationQueryLevelType.USER.toString().equals(orgType) ) {
                sql.append(" and o.id = :campusId ");
                params.put("campusId", campusId);
	        }
			Map sqlMap = new HashMap();
			sqlMap.put("hqlOrg","o.orgLevel");
			RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("收款记录审批","nsql","sql");
			sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
			sql.append("  ) c ");
		sql.append(" on f.CONTRACT_ID=c.id ");
		sql.append(" LEFT JOIN `user` u on f.AUDIT_USER=u.user_id ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotBlank(startDate)){
			sql.append(" and f.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", startDate + " 00:00:00");
		}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and f.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", endDate + " 23:59:59");
		}		
		if(StringUtils.isNotBlank(channel)){
			if(StringUtils.isNotBlank(channel)){			
				String[] otmClassTypeStrs=channel.split(",");
				if(otmClassTypeStrs.length>0){
					sql.append(" and (f.channel = :otmClassType0 ");
					params.put("otmClassType0", otmClassTypeStrs[0]);
					for (int i = 1; i < otmClassTypeStrs.length; i++) {
						sql.append(" or f.channel = :otmClassType" + i);
						params.put("otmClassType" + i, otmClassTypeStrs[i]);
					}
					sql.append(" )");
				}
			}
		}
		if (BasicOperationQueryLevelType.GROUNP.toString().equals(orgType) ){
			sql.append(" GROUP BY c.grounpId,c.brenchId ");
		} else if (BasicOperationQueryLevelType.BRENCH.toString().equals(orgType) ){
			sql.append(" GROUP BY c.grounpId,c.brenchId,c.campusId ");
		} else if (BasicOperationQueryLevelType.CAMPUS.toString().equals(orgType) ){
			sql.append(" GROUP BY c.grounpId,c.brenchId,c.campusId,f.AUDIT_USER ");
		} else if (BasicOperationQueryLevelType.USER.toString().equals(orgType) ) {
		    if(StringUtils.isNotBlank(groupById)){
                sql.append(" and f.AUDIT_USER = :groupById ");
                params.put("groupById", groupById);
            } else {
                sql.append(" and f.AUDIT_USER IS NULL ");
            }
			sql.append(" GROUP BY c.grounpId,c.brenchId,c.campusId,f.AUDIT_USER,f.CHANNEL ");
		}
		sql.append(" ORDER BY f.TRANSACTION_TIME DESC ");
		List<Map<Object,Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}


	@Override
	public DataPackage courseSubjectList(String startDate, String endDate,
			String orgType, String groupById,String workType,String personnelType,String brenchId,String blSchool, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		if(BasicOperationQueryLevelType.CAMPUS.toString().equals(orgType) && ( groupById==null || StringUtils.isBlank(groupById))){
			groupById=blSchool;
		}else if(BasicOperationQueryLevelType.BRENCH.toString().equals(orgType) && ( groupById==null || StringUtils.isBlank(groupById))){
			groupById=brenchId;
		}
		sql.append(" select ");
		if (BasicOperationQueryLevelType.GROUNP.toString().equals(orgType) ){
			sql.append("  CONCAT(g.id,'_',g.name) as GROUNP,CONCAT(b.id,'_',b.name) as BRENCH, '' as CAMPUS,'' as userName,'' as workType,'' as userOrg,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.toString().equals(orgType) ){
			sql.append("  CONCAT(g.id,'_',g.name) as GROUNP,CONCAT(b.id,'_',b.name) as BRENCH, CONCAT(o.id,'_',o.name) as CAMPUS,'' as userName,'' as workType,'' as userOrg, ");
		} else if (BasicOperationQueryLevelType.CAMPUS.toString().equals(orgType) ){
			sql.append(" CONCAT(g.id,'_',g.name) as GROUNP,CONCAT(b.id,'_',b.name) as BRENCH, CONCAT(o.id,'_',o.name) as CAMPUS,CONCAT(tea.user_id,'_',tea.name) as userName,tea.WORK_TYPE as workType,bl.`name` as userOrg, ");
		} 		
		sql.append("  SUM(sub.subjectNum) as subjectNum,SUM(stu.stuNum) as stuNum  ");
		sql.append("  from ( ");
		sql.append(" select COUNT(1) as subjectNum,cus.* from ( ");
		sql.append(" SELECT c.STUDENT_ID,c.`SUBJECT`,c.TEACHER_ID,c.STUDY_MANAGER_ID,c.CREATE_TIME,c.BL_CAMPUS_ID  ");
		sql.append(" from course c");
		sql.append(" where c.COURSE_STATUS='CHARGED' ");
		if(blSchool!=null && StringUtils.isNotBlank(blSchool)){
			sql.append(" and c.BL_CAMPUS_ID = :blSchool ");
			params.put("blSchool", blSchool);
		}
		if(StringUtils.isEmpty(blSchool) && StringUtils.isNotBlank(brenchId)){
			//查询分公司下所有客户
			sql.append(" and c.BL_CAMPUS_ID in ( select id from Organization where parentId = :brenchId )");
			params.put("brenchId", brenchId);
		}
		if(StringUtils.isNotBlank(startDate)){
			sql.append(" and c.COURSE_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and c.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("一对一科次统计","sql","c.BL_CAMPUS_ID"));
		sql.append(" GROUP BY c.STUDENT_ID,c.`SUBJECT`");	
		
		if(orgType.equals("CAMPUS")){
			if(personnelType.equals("studyManage")){
				sql.append(",c.STUDY_MANAGER_ID ");
			}else{
				sql.append(",c.TEACHER_ID ");
			}
			
		}
		
		sql.append(" ORDER BY c.CREATE_TIME DESC ) cus where 1=1 ");
		sql.append(" GROUP BY cus.TEACHER_ID ) sub ");
		
		sql.append(" LEFT JOIN  ");
		sql.append(" ( SELECT COUNT(1) as stuNum,aa.*  from ( ");
		sql.append(" SELECT  cus.STUDENT_ID,cus.`SUBJECT`,cus.TEACHER_ID,cus.STUDY_MANAGER_ID,cus.CREATE_TIME,cus.BL_CAMPUS_ID  from ( ");
		sql.append(" SELECT c.STUDENT_ID,c.`SUBJECT`,c.TEACHER_ID,c.STUDY_MANAGER_ID,c.CREATE_TIME,c.BL_CAMPUS_ID   ");
		sql.append(" from  course c ");
		sql.append(" where c.COURSE_STATUS='CHARGED' ");
		if(blSchool!=null && StringUtils.isNotBlank(blSchool)){
			sql.append(" and c.BL_CAMPUS_ID = :blSchool2 ");
			params.put("blSchool2", blSchool);
		}
		if(StringUtils.isEmpty(blSchool) && StringUtils.isNotBlank(brenchId)){
			//查询分公司下所有客户
			sql.append(" and c.BL_CAMPUS_ID in ( select id from Organization where parentId = :brenchId2 )");
			params.put("brenchId2", brenchId);
		}
		if(StringUtils.isNotBlank(startDate)){
			sql.append(" and c.COURSE_DATE >= :startDate2 ");
			params.put("startDate2", startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and c.COURSE_DATE <= :endDate2 ");
			params.put("endDate2", endDate);
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("一对一科次统计","sql","c.BL_CAMPUS_ID"));
		sql.append(" GROUP BY c.STUDENT_ID,c.`SUBJECT` ");
		//分公司和校区下钻查询时，去掉重复的学生
		if(!orgType.equals("CAMPUS")){
			sql.append("  ORDER BY c.CREATE_TIME DESC ) cus ");
			sql.append(" GROUP BY cus.STUDENT_ID   ) aa  ");	
		}else {
			if(personnelType.equals("studyManage")){
				sql.append(",c.STUDY_MANAGER_ID ");
			}else{
				sql.append(",c.TEACHER_ID ");
			}
			sql.append(" ORDER BY c.CREATE_TIME DESC ) cus ");
			sql.append(" GROUP BY cus.STUDENT_ID,cus.TEACHER_ID   ) aa  ");
		}
		
		sql.append(" GROUP BY aa.TEACHER_ID ) stu  ");
		sql.append(" on sub.TEACHER_ID=stu.TEACHER_ID ");		
		sql.append(" INNER JOIN organization o on sub.BL_CAMPUS_ID=o.id ");
		sql.append(" INNER JOIN organization b on o.parentID=b.id ");
		sql.append(" INNER JOIN organization g on b.parentID=g.id ");
		if(StringUtils.isNotBlank(personnelType)){
			if(personnelType.equals("studyManage")){
				sql.append(" INNER JOIN `user` tea on sub.STUDY_MANAGER_ID=tea.USER_ID ");
			}else{
				sql.append(" INNER JOIN `user` tea on sub.TEACHER_ID=tea.USER_ID ");
			}
		}else{
			sql.append(" INNER JOIN `user` tea on sub.TEACHER_ID=tea.USER_ID ");
		}	
		sql.append(" INNER JOIN user_dept_job udj on tea.USER_ID=udj.USER_ID ");
		sql.append(" INNER JOIN organization cam on udj.DEPT_ID=cam.id ");
		sql.append(" INNER JOIN organization bl on cam.belong=bl.id ");
		sql.append(" where udj.isMajorRole='0' ");
						
		if(workType!=null && StringUtils.isNotBlank(workType)){
			sql.append(" and tea.WORK_TYPE = :workType ");
			params.put("workType", workType);
		}		
		
		if (BasicOperationQueryLevelType.GROUNP.toString().equals(orgType) ){
			sql.append(" GROUP BY g.id,b.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.toString().equals(orgType) ){
			sql.append(" and b.id= :groupById ");
			params.put("groupById", groupById);
			sql.append(" GROUP BY g.id,b.id,o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.toString().equals(orgType) ){
			sql.append(" and o.id= :groupById ");
			params.put("groupById", groupById);
			sql.append(" GROUP BY g.id,b.id,o.id "); //老师，学管师
			if(personnelType.equals("studyManage")){
				sql.append(",sub.STUDY_MANAGER_ID");
			}else{
				sql.append(",sub.TEACHER_ID");
			}
		} 
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	
	/**
	 * 一对一科次科目分布报表
	 */
	@Override
	public DataPackage getcourseSubjectTimesList(BasicOperationQueryVo basicOperationQueryVo, String workType, String roleType, String subject, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> params2 = new HashMap<String, Object>();
		BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
		// 科目分布
		StringBuffer sql = new StringBuffer();
		StringBuffer selectSql = new StringBuffer();
		selectSql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			selectSql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, '' CAMPUS_ID, '' USER_ID, '' WORK_TYPE, '' USER_CAMPUS,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			selectSql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, '' USER_ID, '' WORK_TYPE, '' USER_CAMPUS,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			selectSql.append(" CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, CONCAT(u.USER_ID,'_',u.NAME) USER_ID, (CASE WHEN u.WORK_TYPE = 'FULL_TIME' then '全职' ELSE '兼职' END) WORK_TYPE, org_user.NAME USER_CAMPUS, ");
		} 
		sql.append(selectSql.toString());
		
		sql.append(" sum(CASE WHEN dd.`NAME` = '语文' THEN 1 ELSE 0 END) CHINESE_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '数学' THEN 1 ELSE 0 END) MATCH_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '英语' THEN 1 ELSE 0 END) ENGLISH_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '物理' THEN 1 ELSE 0 END) PHYSICS_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '化学' THEN 1 ELSE 0 END) CHEMISTRY_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '政治' THEN 1 ELSE 0 END) POLITICS_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '历史' THEN 1 ELSE 0 END) HISTORY_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '地理' THEN 1 ELSE 0 END) GEOGRAPHY_COUNT, ");
		sql.append(" sum(CASE WHEN dd.`NAME` = '生物' THEN 1 ELSE 0 END) BIOLOGY_COUNT, ");
		sql.append(" sum(1) TOTAL_COUNT ");
		sql.append(" FROM ");
		
		StringBuffer fromSql1=new StringBuffer();
		if (StringUtil.isNotBlank(roleType) && "STUDY_MANAGER".equals(roleType)) {
			fromSql1.append(" (select DISTINCT STUDY_MANAGER_ID, `SUBJECT`, STUDENT_ID, BL_CAMPUS_ID from course ");
		} else {
			fromSql1.append(" (select DISTINCT TEACHER_ID, `SUBJECT`, STUDENT_ID, BL_CAMPUS_ID from course ");
		}
		fromSql1.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			fromSql1.append(" AND COURSE_DATE >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			fromSql1.append(" AND COURSE_DATE <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate());
		}
		fromSql1.append(" AND COURSE_STATUS = 'CHARGED' ");
		fromSql1.append(roleQLConfigService.getAppendSqlByAllOrg("一对一科次科目分布","sql","BL_CAMPUS_ID"));
		if (StringUtil.isNotBlank(subject)){
			String[] subjects=subject.split(",");
			if(subjects.length>0){
				fromSql1.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						fromSql1.append(" or SUBJECT = :subject" + i);
						params.put("subject" + i, subjects[i]);
					}
				}
				fromSql1.append(" )");
			}
		}
		if(!BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)){
			fromSql1.append(" GROUP BY STUDENT_ID,`SUBJECT` ");
		}		
		fromSql1.append(" ) c ");
		sql.append(fromSql1.toString());
		sql.append(" INNER JOIN data_dict dd on c.SUBJECT = dd.id ");
		
		StringBuffer joinSql = new StringBuffer();
		if (StringUtil.isNotBlank(roleType) && "STUDY_MANAGER".equals(roleType)) {
			joinSql.append(" INNER JOIN user u ON c.STUDY_MANAGER_ID = u.USER_ID ");
			joinSql.append(" INNER JOIN ref_user_org ruo on c.STUDY_MANAGER_ID = ruo.USER_ID ");
		} else {
			joinSql.append(" INNER JOIN user u ON c.TEACHER_ID = u.USER_ID ");
			joinSql.append(" INNER JOIN ref_user_org ruo on c.TEACHER_ID = ruo.USER_ID ");
		}
		joinSql.append(" INNER JOIN organization org_user on ruo.CAMPUS_ID = org_user.ID ");
		joinSql.append(" INNER JOIN organization o on c.BL_CAMPUS_ID = o.ID ");
		joinSql.append(" INNER JOIN organization org_brench on o.parentID = org_brench.ID ");
		joinSql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.ID ");
		joinSql.append(" WHERE 1 = 1 ");
		if (StringUtil.isNotBlank(workType)) {
			joinSql.append(" AND u.WORK_TYPE = :workType ");
			params.put("workType", workType);
		}
		if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
				joinSql.append(" AND org_brench.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				params2.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
				joinSql.append(" AND o.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
				params2.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			joinSql.append(" GROUP BY org_group.ID, org_brench.ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			joinSql.append(" GROUP BY org_group.ID, org_brench.ID, c.BL_CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			if (StringUtil.isNotBlank(roleType) && "STUDY_MANAGER".equals(roleType)) {
				joinSql.append(" GROUP BY org_group.ID, org_brench.ID, c.BL_CAMPUS_ID, c.STUDY_MANAGER_ID ");
			} else {
				joinSql.append(" GROUP BY org_group.ID, org_brench.ID, c.BL_CAMPUS_ID, c.TEACHER_ID ");
			}
		} 
		sql.append(joinSql.toString());
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		// 科数学生分布
		StringBuffer sql2 = new StringBuffer();
		sql2.append(selectSql.toString());
		sql2.append(" sum(CASE WHEN c.SUBJECT_COUNT = 1 THEN 1 ELSE 0 END) ONE_COUNT, ");
		sql2.append(" sum(CASE WHEN c.SUBJECT_COUNT = 2 THEN 1 ELSE 0 END) TWO_COUNT, ");
		sql2.append(" sum(CASE WHEN c.SUBJECT_COUNT = 3 THEN 1 ELSE 0 END) THREE_COUNT, ");
		sql2.append(" sum(CASE WHEN c.SUBJECT_COUNT >= 4 THEN 1 ELSE 0 END) FOUR_COUNT ");
		sql2.append(" FROM ");
		StringBuffer fromSql2=new StringBuffer();
		if (StringUtil.isNotBlank(roleType) && "STUDY_MANAGER".equals(roleType)) {
			fromSql2.append(" (SELECT a.BL_CAMPUS_ID, a.STUDY_MANAGER_ID, a.STUDENT_ID, sum(1) SUBJECT_COUNT FROM ");
		} else {
			fromSql2.append(" (SELECT a.BL_CAMPUS_ID, a.TEACHER_ID, a.STUDENT_ID, sum(1) SUBJECT_COUNT FROM ");
		}
		
		if (StringUtil.isNotBlank(roleType) && "STUDY_MANAGER".equals(roleType)) {
			fromSql2.append(" (select DISTINCT c.STUDY_MANAGER_ID, c.`SUBJECT`, c.STUDENT_ID, c.BL_CAMPUS_ID, org_brench.id BRENCH_ID, org_group.id GROUP_ID from course c ");
		} else {
			fromSql2.append(" (select DISTINCT c.TEACHER_ID, `SUBJECT`, c.STUDENT_ID, c.BL_CAMPUS_ID, org_brench.id BRENCH_ID, org_group.id GROUP_ID from course c ");
		}
		fromSql2.append(" INNER JOIN organization o on c.BL_CAMPUS_ID = o.ID ");
		fromSql2.append(" INNER JOIN organization org_brench on o.parentID = org_brench.ID ");
		fromSql2.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.ID ");
		fromSql2.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			fromSql2.append(" AND COURSE_DATE >= :startDate ");
			params2.put("startDate", basicOperationQueryVo.getStartDate());
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			fromSql2.append(" AND COURSE_DATE <= :endDate ");
			params2.put("endDate", basicOperationQueryVo.getEndDate());
		}
		fromSql2.append(" AND COURSE_STATUS = 'CHARGED' ");
		fromSql2.append(roleQLConfigService.getAppendSqlByAllOrg("一对一科次科目分布","sql","o.id"));
		if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
				fromSql2.append(" AND org_brench.ID = :blCampusId2 ");
				params2.put("blCampusId2", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
				fromSql2.append(" AND o.ID = :blCampusId2 ");
				params2.put("blCampusId2", basicOperationQueryVo.getBlCampusId());
			}
		}
		
		if (StringUtil.isNotBlank(subject)){
			String[] subjects=subject.split(",");
			if(subjects.length>0){
				fromSql2.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						fromSql2.append(" or SUBJECT = :subject" + i);
						params2.put("subject" + i, subjects[i]);
					}
				}
				fromSql2.append(" )");
			}
		}
		if(!BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)){
			fromSql2.append(" GROUP BY STUDENT_ID,`SUBJECT` ");
		}else{
			fromSql2.append(" GROUP BY STUDENT_ID,`SUBJECT`,TEACHER_ID ");
		}
		
		fromSql2.append(" ) a ");		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			fromSql2.append(" GROUP BY a.STUDENT_ID) c ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			fromSql2.append(" GROUP BY a.STUDENT_ID) c ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			if (StringUtil.isNotBlank(roleType) && "STUDY_MANAGER".equals(roleType)) {
				fromSql2.append(" GROUP BY a.GROUP_ID, a.BRENCH_ID, a.BL_CAMPUS_ID, a.STUDY_MANAGER_ID,a.STUDENT_ID) c ");
			} else {
				fromSql2.append(" GROUP BY a.GROUP_ID, a.BRENCH_ID, a.BL_CAMPUS_ID, a.TEACHER_ID,a.STUDENT_ID) c ");
			}
		} 
		sql2.append(fromSql2.toString());
		sql2.append(joinSql.toString());
		List<Map<Object, Object>> list2= super.findMapOfPageBySql(sql2.toString(), dp, params2);
		Map<String, Map> list1Map = new HashMap<String, Map>();
		String tempId = "";
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			for (Map map : list) {
				list1Map.put((String) map.get("BRENCH_ID"), map);
			}
			tempId = "BRENCH_ID";
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			for (Map map : list) {
				list1Map.put((String) map.get("CAMPUS_ID"), map);
			}
			tempId = "CAMPUS_ID";
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			for (Map map : list) {
				list1Map.put((String) map.get("USER_ID"), map);
			}
			tempId = "USER_ID";
		} 
		for (Map map : list2) {
			Map appendMap = list1Map.get((String) map.get(tempId));
			if (appendMap != null) {
				appendMap.put("ONE_COUNT", map.get("ONE_COUNT"));
				appendMap.put("TWO_COUNT", map.get("TWO_COUNT"));
				appendMap.put("THREE_COUNT", map.get("THREE_COUNT"));
				appendMap.put("FOUR_COUNT", map.get("FOUR_COUNT"));
			}
		}
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}

	/**
	 * 一对一学生人均课消报表
	 *
	 * @param basicOperationQueryVo
	 * @param brenchId
	 * @param blCampus
	 * @param workType  全/兼职
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage courseConsumeRenJunList(BasicOperationQueryVo basicOperationQueryVo,String groupById, String brenchId, String blCampus, String workType, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
		if(BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) && ( groupById==null || StringUtils.isBlank(groupById))){
			groupById=blCampus;
		}else if(BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) && ( groupById==null || StringUtils.isBlank(groupById))){
			groupById=brenchId;
		}
		StringBuffer sql = new StringBuffer();
		StringBuffer selectSql = new StringBuffer();
		selectSql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			selectSql.append("  CONCAT(o1.ID,'_',o1.NAME) GROUNP, CONCAT(o2.ID,'_',o2.NAME) BRENCH, '' CAMPUS, '' USER, '' WORK_TYPE, '' USER_CAMPUS,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			selectSql.append("  CONCAT(o1.ID,'_',o1.NAME) GROUNP, CONCAT(o2.ID,'_',o2.NAME) BRENCH, CONCAT(o3.ID,'_',o3.NAME) CAMPUS, '' USER, '' WORK_TYPE, '' USER_CAMPUS,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			selectSql.append(" CONCAT(o1.ID,'_',o1.NAME) GROUNP, CONCAT(o2.ID,'_',o2.NAME) BRENCH, CONCAT(o3.ID,'_',o3.NAME) CAMPUS, CONCAT(e.USER_ID,'_',e.NAME) USER, (CASE WHEN e.WORK_TYPE = 'FULL_TIME' then '全职' ELSE '兼职' END) WORK_TYPE, o4.NAME USER_CAMPUS, ");
		}
		sql.append(selectSql.toString());

		sql.append(" SUM(QUANTITY) TOTAL_CONSUME_HOUR, ");
		sql.append(" SUM(AMOUNT) TOTAL_CONSUME_AMOUNT ");

		StringBuffer fromSql1=new StringBuffer();
		fromSql1.append(" from account_charge_records ,organization o1, organization o2, organization o3,USER e,user_dept_job udj,organization o4 ");
		fromSql1.append(" where 1=1 ");
		fromSql1.append(" AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' ");//一对一扣费
		fromSql1.append(" AND o3.id = BL_CAMPUS_ID ");
		if (blCampus!=null&&StringUtils.isNotBlank(blCampus)){
			fromSql1.append(" AND BL_CAMPUS_ID = :blCampus ");
			params.put("blCampus", blCampus);
		}
		if (StringUtils.isEmpty(blCampus)&&StringUtils.isNotBlank(brenchId)){
			fromSql1.append(" AND BL_CAMPUS_ID in ( select id from Organization where parentId = :brenchId )");
			params.put("brenchId", brenchId);
		}
		fromSql1.append(" AND o2.id = o3.parentid ");
		fromSql1.append(" AND o1.id = o2.parentid ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			fromSql1.append(" AND TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 0000");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			fromSql1.append(" AND TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 2359");
		}

		if (StringUtils.isNotBlank(workType)){
			fromSql1.append(" AND e.WORK_TYPE = :workType ");
			params.put("workType", workType);
		}
		fromSql1.append(" AND udj.USER_ID = e.USER_ID ");
		fromSql1.append(" AND TEACHER_ID = e.USER_ID ");
		fromSql1.append(" AND udj.isMajorRole = 0 ");
		fromSql1.append(" AND udj.DEPT_ID = o4.id ");
		fromSql1.append(roleQLConfigService.getAppendSqlByAllOrg("一对一学生人均课消报表","sql","BL_CAMPUS_ID"));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			fromSql1.append(" GROUP BY o2.id ");
		}else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			if (StringUtils.isNotBlank(groupById)){
				fromSql1.append(" AND o2.id = :groupById ");
				params.put("groupById", groupById);
			}
			fromSql1.append(" GROUP BY o3.id ");
		}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			if (StringUtils.isNotBlank(groupById)){
				fromSql1.append(" AND o3.id = :groupById ");
				params.put("groupById", groupById);
			}
			fromSql1.append(" GROUP BY o3.id,TEACHER_ID ");
		}
        sql.append(fromSql1.toString());
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		//老师人数
		Map<String, Object> params2 = new HashMap<String, Object>();
		StringBuffer sql2 = new StringBuffer();
		sql2.append(selectSql.toString());
		sql2.append(" count(*) ");
		StringBuffer fromSql2=new StringBuffer();
		fromSql2.append(" from ( SELECT DISTINCT TEACHER_ID ,BL_CAMPUS_ID FROM account_charge_records WHERE 1=1 ");
		fromSql2.append(" AND PRODUCT_TYPE ='ONE_ON_ONE_COURSE' AND TEACHER_ID IS NOT NULL");
		if (blCampus!=null&&StringUtils.isNotBlank(blCampus)){
			fromSql2.append(" AND BL_CAMPUS_ID = :blCampus ");
			params2.put("blCampus", blCampus);
		}
		if (StringUtils.isEmpty(blCampus) &&StringUtils.isNotBlank(brenchId)){
			fromSql2.append(" AND BL_CAMPUS_ID in ( select id from Organization where parentId = :brenchId )");
			params2.put("brenchId", brenchId);
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			fromSql2.append(" AND TRANSACTION_TIME >= :startDate ");
			params2.put("startDate", basicOperationQueryVo.getStartDate() + " 0000");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			fromSql2.append(" AND TRANSACTION_TIME <= :endDate ");
			params2.put("endDate", basicOperationQueryVo.getEndDate() + " 2359");
		}

		fromSql2.append(roleQLConfigService.getAppendSqlByAllOrg("一对一学生人均课消报表","sql","BL_CAMPUS_ID"));

		fromSql2.append(") acr ,`user` e, user_dept_job udj, organization o4 ,organization o1 ,organization o2, organization o3 ");
		fromSql2.append(" WHERE acr.BL_CAMPUS_ID=o3.id AND e.USER_ID=acr.TEACHER_ID AND  o3.parentID=o2.id AND o2.parentID = o1.id AND udj.USER_ID = e.USER_ID AND udj.isMajorRole = 0 AND udj.DEPT_ID = o4.id ");

		if (StringUtils.isNotBlank(workType)){
			fromSql2.append(" AND e.WORK_TYPE = :workType ");
			params2.put("workType", workType);
		}

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			fromSql2.append(" GROUP BY o2.id ");
		}else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			if (StringUtils.isNotBlank(groupById)){
				fromSql2.append(" AND o2.id= :groupById ");
				params2.put("groupById", groupById);
			}
			fromSql2.append(" GROUP BY o3.id ");
		}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			if (StringUtils.isNotBlank(groupById)){
				fromSql2.append(" AND o3.id = :groupById ");
				params2.put("groupById", groupById);
			}
			fromSql2.append(" GROUP BY o3.id, acr.TEACHER_ID ");
		}
		sql2.append(fromSql2.toString());
		List<Map<Object, Object>> list2= super.findMapOfPageBySql(sql2.toString(), dp, params2);

		//学生人数
		StringBuffer sql3 = new StringBuffer();
		Map<String, Object> params3 = new HashMap<String, Object>();
		sql3.append(selectSql.toString());
		sql3.append(" count(*) as nums ");
		StringBuffer fromSql3 = new StringBuffer();
		fromSql3.append(" from ( SELECT  STUDENT_ID, BL_CAMPUS_ID,TEACHER_ID FROM account_charge_records WHERE 1=1 ");
		fromSql3.append(" AND PRODUCT_TYPE ='ONE_ON_ONE_COURSE' AND STUDENT_ID IS NOT NULL AND TEACHER_ID IS NOT NULL ");
		if (blCampus!=null&&StringUtils.isNotBlank(blCampus)){
			fromSql3.append(" AND BL_CAMPUS_ID = :blCampus ");
			params3.put("blCampus", blCampus);
		}
		if (StringUtils.isEmpty(blCampus)&&StringUtils.isNotBlank(brenchId)){
			fromSql3.append(" AND BL_CAMPUS_ID in ( select id from Organization where parentId= :brenchId )");
			params3.put("brenchId", brenchId);
		}

		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			fromSql3.append(" AND TRANSACTION_TIME >= :startDate ");
			params3.put("startDate", basicOperationQueryVo.getStartDate() + " 0000");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())){
			fromSql3.append(" AND TRANSACTION_TIME <= :endDate ");
			params3.put("endDate", basicOperationQueryVo.getEndDate() + " 2359");
		}

		fromSql3.append(roleQLConfigService.getAppendSqlByAllOrg("一对一学生人均课消报表","sql","BL_CAMPUS_ID"));

		fromSql3.append(" GROUP BY BL_CAMPUS_ID,STUDENT_ID ");
		if(BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)){
			fromSql3.append(",TEACHER_ID");
		}

		fromSql3.append(") acr ,`user` e, user_dept_job udj, organization o4 ,organization o1 ,organization o2, organization o3 ");
		fromSql3.append(" WHERE acr.BL_CAMPUS_ID=o3.id AND e.USER_ID=acr.TEACHER_ID AND  o3.parentID=o2.id AND o2.parentID = o1.id AND udj.USER_ID = e.USER_ID AND udj.isMajorRole = 0 AND udj.DEPT_ID = o4.id ");

		if (StringUtils.isNotBlank(workType)){
			fromSql3.append(" AND e.WORK_TYPE = :workType");
			params3.put("workType", workType);
		}



		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			fromSql3.append(" GROUP BY o2.id ");
		}else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			if (StringUtils.isNotBlank(groupById)){
				fromSql3.append(" AND o2.id = :groupById ");
				params3.put("groupById", groupById);
			}
			fromSql3.append(" GROUP BY o3.id ");
		}else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			if (StringUtils.isNotBlank(groupById)){
				fromSql3.append(" AND o3.id= :groupById ");
				params3.put("groupById", groupById);
			}
			fromSql3.append(" GROUP BY o3.id, acr.TEACHER_ID ");
		}
		sql3.append(fromSql3.toString());
		List<Map<Object, Object>> list3 = super.findMapOfPageBySql(sql3.toString(), dp, params3);

		Map<String, Map> list1Map = new HashMap<String, Map>();
		String tempId = "";
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			for (Map map : list) {
				list1Map.put((String) map.get("BRENCH"), map);
			}
			tempId = "BRENCH";
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			for (Map map : list) {
				list1Map.put((String) map.get("CAMPUS"), map);
			}
			tempId = "CAMPUS";
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			for (Map map : list) {
				list1Map.put((String) map.get("USER"), map);
			}
			tempId = "USER";
		}
		for (Map map : list2) {
			Map appendMap = list1Map.get((String) map.get(tempId));
			if (appendMap != null){
				BigDecimal teachers = new BigDecimal((BigInteger) map.get("count(*)"));
				appendMap.put("teacherNum", teachers);
				BigDecimal total_consume_hour =  (BigDecimal)appendMap.get("TOTAL_CONSUME_HOUR");
				BigDecimal total_consume_amount = (BigDecimal) appendMap.get("TOTAL_CONSUME_AMOUNT");
				if (teachers.compareTo(BigDecimal.ZERO)>0){
					appendMap.put("teacherRenjunKeshi",total_consume_hour.divide(teachers,2, BigDecimal.ROUND_HALF_UP));
					appendMap.put("teacherRenjunChanZhi",total_consume_amount.divide(teachers,2, BigDecimal.ROUND_HALF_UP));
				}

			}
		}

		for (Map map : list3){
			Map appendMap = list1Map.get((String)map.get(tempId));
			if (appendMap != null){
				BigDecimal students =  map.get("nums")==null ? BigDecimal.ZERO :new BigDecimal( map.get("nums").toString());
				appendMap.put("studentNum",students);
				BigDecimal total_consume_hour =  (BigDecimal) appendMap.get("TOTAL_CONSUME_HOUR");
				BigDecimal total_consume_amount =  (BigDecimal) appendMap.get("TOTAL_CONSUME_AMOUNT");
				if (students.compareTo(BigDecimal.ZERO)>0){
					appendMap.put("studentRenjunKeshi",total_consume_hour.divide(students,2, BigDecimal.ROUND_HALF_UP));
					appendMap.put("studentRenjunJinE",total_consume_amount.divide(students,2, BigDecimal.ROUND_HALF_UP));
				}
			}
		}


		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;

	}
	
	/**
	 * 一对一月结报表
	 */
	public DataPackage getOooMonthlyBalance(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, '' STUDY_MANAGER, '' BELONG_DEPARTMENT, '' STUDENT_NAME, '' OOO_STATUS,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, '' STUDY_MANAGER, '' BELONG_DEPARTMENT, '' STUDENT_NAME, '' OOO_STATUS,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, CONCAT(u.USER_ID, '_', u.NAME) STUDY_MANAGER, org_belong.NAME BELONG_DEPARTMENT, '' STUDENT_NAME, '' OOO_STATUS, ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType)) {
			sql.append(" CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, CONCAT(u.USER_ID, '_', u.NAME) STUDY_MANAGER, org_belong.NAME BELONG_DEPARTMENT, s.NAME STUDENT_NAME, odmb.OOO_STATUS OOO_STATUS, ");
		}
		sql.append(" MAPPING_DATE, ");
		sql.append(" sum(LAST_SURPLUS_REAL_HOURS) LAST_SURPLUS_REAL_HOURS, ");
		sql.append(" sum(LAST_SURPLUS_PRO_HOURS) LAST_SURPLUS_PRO_HOURS, ");
		sql.append(" sum(LAST_SURPLUS_REAL_AMOUNT) LAST_SURPLUS_REAL_AMOUNT, ");
		sql.append(" sum(LAST_SURPLUS_PRO_AMOUNT) LAST_SURPLUS_PRO_AMOUNT, ");
		sql.append(" sum(CURRENT_SUPPLE_REAL_HOURS) CURRENT_SUPPLE_REAL_HOURS, ");
		sql.append(" sum(CURRENT_SUPPLE_PRO_HOURS) CURRENT_SUPPLE_PRO_HOURS, ");
		sql.append(" sum(ROLLBACK_REAL_HOURS) ROLLBACK_REAL_HOURS, ");
		sql.append(" sum(ROLLBACK_PRO_HOURS) ROLLBACK_PRO_HOURS, ");
		sql.append(" sum(CURRENT_NEW_REAL_HOURS) CURRENT_NEW_REAL_HOURS, ");
		sql.append(" sum(CURRENT_NEW_PRO_HOURS) CURRENT_NEW_PRO_HOURS, ");
		sql.append(" sum(CURRENT_REFUND_REAL_HOURS) CURRENT_REFUND_REAL_HOURS, ");
		sql.append(" sum(CURRENT_REFUND_PRO_HOURS) CURRENT_REFUND_PRO_HOURS, ");
		sql.append(" sum(CURRENT_CONSUME_REAL_HOURS) CURRENT_CONSUME_REAL_HOURS, ");
		sql.append(" sum(CURRENT_CONSUME_PRO_HOURS) CURRENT_CONSUME_PRO_HOURS, ");
		sql.append(" sum(CURRENT_INCOME_REAL_HOURS) CURRENT_INCOME_REAL_HOURS, ");
		sql.append(" sum(CURRENT_INCOME_PRO_HOURS) CURRENT_INCOME_PRO_HOURS, ");
		sql.append(" sum(TOTAL_REMAIN_REAL_HOURS) TOTAL_REMAIN_REAL_HOURS, ");
		sql.append(" sum(TOTAL_REMAIN_PRO_HOURS) TOTAL_REMAIN_PRO_HOURS, ");
		sql.append(" sum(TOTAL_REMAIN_REAL_AMOUNT) TOTAL_REMAIN_REAL_AMOUNT, ");
		sql.append(" sum(TOTAL_REMAIN_PRO_AMOUNT) TOTAL_REMAIN_PRO_AMOUNT ");
		sql.append(" FROM ods_day_monthly_balance odmb ");
		sql.append(" INNER JOIN organization o ON odmb.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench ON o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group ON org_brench.parentID = org_group.id ");
		sql.append(" LEFT JOIN user u on odmb.STUDY_MANAGER_ID = u.USER_ID ");
		sql.append(" LEFT JOIN organization org_belong ON odmb.BELONG_DEPARTMENT_ID = org_belong.id ");
		sql.append(" INNER JOIN student s ON odmb.STUDENT_ID = s.ID ");
		
		sql.append(" WHERE 1 = 1 ");
		if (StringUtil.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		
		if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
				sql.append(" AND org_brench.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
				sql.append(" AND o.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType)) {
				sql.append(" AND u.USER_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		} else {
			if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType)) {
				sql.append(" AND (u.USER_ID IS NULL OR u.USER_ID = '') ");
			}
		}
		
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("一对一月结报表","sql","o.ID"));
		
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID, u.USER_ID ");
		} else if (BasicOperationQueryLevelType.STUDY_MANEGER.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID, u.USER_ID, s.ID ");
		} 
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	/**
	 * 财务现金流凭证报表
	 */
	@Override
	public DataPackage getFinanceMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, '' CAMPUS_ID, '' USER_ID, '' STUDENT_NAME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, '' USER_ID, '' STUDENT_NAME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, CONCAT(u.USER_ID, '_', u.NAME) USER_ID, '' STUDENT_NAME, ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryLevelType)) {
			sql.append(" CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, CONCAT(u.USER_ID, '_', u.NAME) USER_ID, s.NAME STUDENT_NAME, ");
		}
		
		sql.append(" MAPPING_DATE, ");
		sql.append(" sum(FUNDS_AMOUNT_NEW) FUNDS_AMOUNT_NEW, ");
		sql.append(" sum(FUNDS_AMOUNT_RE) FUNDS_AMOUNT_RE, ");
		sql.append(" sum(FUNDS_AMOUNT_REAL) FUNDS_AMOUNT_REAL, ");
		sql.append(" sum(ELECTRONIC_AMOUNT) ELECTRONIC_AMOUNT, ");
		sql.append(" sum(FUNDS_HISTORY_WASH_AMOUNG) FUNDS_HISTORY_WASH_AMOUNG, ");
		sql.append(" sum(TOTAL_FUNDS_AMOUNT) TOTAL_FUNDS_AMOUNT ");
		sql.append(" FROM ods_month_finance_evidence omfe ");
		sql.append(" INNER JOIN organization o ON omfe.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench ON o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group ON org_brench.parentID = org_group.id ");
		sql.append(" LEFT JOIN user u on omfe.USER_ID = u.USER_ID ");
		sql.append(" INNER JOIN student s ON omfe.STUDENT_ID = s.ID ");
		
		sql.append(" WHERE 1 = 1 ");
		if (StringUtil.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		
		if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
				sql.append(" AND org_brench.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
				sql.append(" AND o.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryLevelType)) {
				sql.append(" AND u.USER_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		} else {
			if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryLevelType)) {
				sql.append(" AND (u.USER_ID IS NULL OR u.USER_ID = '') ");
			}
		}
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("财务现金流凭证","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID, u.USER_ID ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID, u.USER_ID, s.ID ");
		} 
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	/**
	 * 财务扣费凭证报表
	 */
	public DataPackage getIncomeMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, '' CAMPUS_ID, '' STUDENT_NAME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append("  CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, '' STUDENT_NAME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" CONCAT(org_group.ID,'_',org_group.NAME) GROUP_ID, CONCAT(org_brench.ID,'_',org_brench.NAME) BRENCH_ID, CONCAT(o.ID,'_',o.NAME) CAMPUS_ID, s.NAME STUDENT_NAME, ");
		}
		sql.append(" MAPPING_DATE, ");
		sql.append(" sum(ONE_ON_ONE_REAL_AMOUNT) ONE_ON_ONE_REAL_AMOUNT, ");
		sql.append(" sum(ONE_ON_ONE_PROMOTION_AMOUNT) ONE_ON_ONE_PROMOTION_AMOUNT, ");
		sql.append(" sum(ONE_ON_ONE_HISTORY_WASH_AMOUNT) ONE_ON_ONE_HISTORY_WASH_AMOUNT, ");
		
		sql.append(" sum(SAMLL_CLASS_REAL_AMOUNT) SAMLL_CLASS_REAL_AMOUNT, ");
		sql.append(" sum(SAMLL_CLASS_PROMOTION_AMOUNT) SAMLL_CLASS_PROMOTION_AMOUNT, ");
		sql.append(" sum(SMALL_CLASS_HISTORY_WASH_AMOUNT) SMALL_CLASS_HISTORY_WASH_AMOUNT, ");
		
		sql.append(" sum(ESC_CLASS_REAL_AMOUNT) ESC_CLASS_REAL_AMOUNT, ");
		sql.append(" sum(ESC_CLASS_PROMOTION_AMOUNT) ESC_CLASS_PROMOTION_AMOUNT, ");
		sql.append(" sum(ECS_CLASS_HISTORY_WASH_AMOUNT) ECS_CLASS_HISTORY_WASH_AMOUNT, ");
		
		sql.append(" sum(OTM_CLASS_REAL_AMOUNT) OTM_CLASS_REAL_AMOUNT, ");
		sql.append(" sum(OTM_CLASS_PROMOTION_AMOUNT) OTM_CLASS_PROMOTION_AMOUNT, ");
		sql.append(" sum(OTM_CLASS_HISTORY_WASH_AMOUNT) OTM_CLASS_HISTORY_WASH_AMOUNT, ");
		
		sql.append(" sum(OTHERS_REAL_AMOUNT) OTHERS_REAL_AMOUNT, ");
		sql.append(" sum(OTHERS_PROMOTION_AMOUNT) OTHERS_PROMOTION_AMOUNT, ");
		sql.append(" sum(OTHERS_HISTORY_WASH_AMOUNT) OTHERS_HISTORY_WASH_AMOUNT, ");
		
		sql.append(" sum(LECTURE_REAL_AMOUNT) LECTURE_REAL_AMOUNT, ");
		sql.append(" sum(LECTURE_PROMOTION_AMOUNT) LECTURE_PROMOTION_AMOUNT, ");
		sql.append(" sum(LECTURE_HISTORY_WASH_AMOUNT) LECTURE_HISTORY_WASH_AMOUNT, ");
		
		sql.append(" sum(IS_NORMAL_REAL_AMOUNT) IS_NORMAL_REAL_AMOUNT, ");
		sql.append(" sum(IS_NORMAL_PROMOTION_AMOUNT) IS_NORMAL_PROMOTION_AMOUNT, ");
		sql.append(" sum(IS_NORMAL_HISTORY_WASH_AMOUNT) IS_NORMAL_HISTORY_WASH_AMOUNT ");
		
		sql.append(" FROM ods_month_income_evidence omie ");
		sql.append(" INNER JOIN organization o ON omie.BL_CAMPUS_ID = o.id ");
		sql.append(" INNER JOIN organization org_brench ON o.parentID = org_brench.id ");
		sql.append(" INNER JOIN organization org_group ON org_brench.parentID = org_group.id ");
		sql.append(" INNER JOIN student s ON omie.STUDENT_ID = s.ID ");
		sql.append(" WHERE 1 = 1 ");
		if (StringUtil.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
				sql.append(" AND org_brench.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
				sql.append(" AND o.ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("财务扣费凭证","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY org_group.ID, org_brench.ID, o.ID, s.ID ");
		} 
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	/**
	 * 营收凭证报表
	 */
	public DataPackage getIncomeMonthlyCampus(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append(" '' ID, CONCAT(omi.GROUP_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.GROUP_ID)) GROUP_ID, "
					+ " CONCAT(omi.BRENCH_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.BRENCH_ID)) BRENCH_ID, "
					+ " '' CAMPUS_ID, '' STUDENT_NAME,  ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append(" omi.ID ID, CONCAT(omi.GROUP_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.GROUP_ID)) GROUP_ID, "
					+ " CONCAT(omi.BRENCH_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.BRENCH_ID)) BRENCH_ID, "
					+ " CONCAT(omi.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.CAMPUS_ID)) CAMPUS_ID, '' STUDENT_NAME,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" '' ID, CONCAT(omi.GROUP_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.GROUP_ID)) GROUP_ID, "
					+ " CONCAT(omi.BRENCH_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.BRENCH_ID)) BRENCH_ID, "
					+ " CONCAT(omi.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE ID = omi.CAMPUS_ID)) CAMPUS_ID, "
					+ " (SELECT NAME FROM student WHERE ID = omi.STUDENT_ID) STUDENT_NAME, ");
		}
		
		sql.append(" MAPPING_DATE, ");
		sql.append(" sum(ONE_ON_ONE_REAL_AMOUNT) ONE_ON_ONE_REAL_AMOUNT, ");
		sql.append(" sum(ONE_ON_ONE_PROMOTION_AMOUNT) ONE_ON_ONE_PROMOTION_AMOUNT, ");
		sql.append(" sum(ONE_ON_ONE_REAL_WASH_AMOUNT) ONE_ON_ONE_REAL_WASH_AMOUNT, ");
		sql.append(" sum(ONE_ON_ONE_PROMOTION_WASH_AMOUNT) ONE_ON_ONE_PROMOTION_WASH_AMOUNT, ");
		
		sql.append(" sum(SMALL_CLASS_REAL_AMOUNT) SMALL_CLASS_REAL_AMOUNT, ");
		sql.append(" sum(SMALL_CLASS_PROMOTION_AMOUNT) SMALL_CLASS_PROMOTION_AMOUNT, ");
		sql.append(" sum(SMALL_CLASS_REAL_WASH_AMOUNT) SMALL_CLASS_REAL_WASH_AMOUNT, ");
		sql.append(" sum(SMALL_CLASS_PROMOTION_WASH_AMOUNT) SMALL_CLASS_PROMOTION_WASH_AMOUNT, ");
		
		/*sql.append(" sum(LIVE_REAL_AMOUNT) LIVE_REAL_AMOUNT, ");
        sql.append(" sum(LIVE_PROMOTION_AMOUNT) LIVE_PROMOTION_AMOUNT, ");
        sql.append(" sum(LIVE_REAL_WASH_AMOUNT) LIVE_REAL_WASH_AMOUNT, ");
        sql.append(" sum(LIVE_PROMOTION_WASH_AMOUNT) LIVE_PROMOTION_WASH_AMOUNT, ");*/
		sql.append(" sum(LIVE_NEW_REAL_AMOUNT) LIVE_NEW_REAL_AMOUNT, ");
        sql.append(" sum(LIVE_NEW_REAL_DIVIDE) LIVE_NEW_REAL_DIVIDE, ");
        sql.append(" sum(LIVE_RENEW_REAL_AMOUNT) LIVE_RENEW_REAL_AMOUNT, ");
        sql.append(" sum(LIVE_RENEW_REAL_DIVIDE) LIVE_RENEW_REAL_DIVIDE, ");
		
		sql.append(" sum(TWO_TEACHER_REAL_AMOUNT) TWO_TEACHER_REAL_AMOUNT, ");
        sql.append(" sum(TWO_TEACHER_PROMOTION_AMOUNT) TWO_TEACHER_PROMOTION_AMOUNT, ");
        sql.append(" sum(TWO_TEACHER_REAL_WASH_AMOUNT) TWO_TEACHER_REAL_WASH_AMOUNT, ");
        sql.append(" sum(TWO_TEACHER_PROMOTION_WASH_AMOUNT) TWO_TEACHER_PROMOTION_WASH_AMOUNT, ");
		
		sql.append(" sum(ECS_CLASS_REAL_AMOUNT) ECS_CLASS_REAL_AMOUNT, ");
		sql.append(" sum(ECS_CLASS_PROMOTION_AMOUNT) ECS_CLASS_PROMOTION_AMOUNT, ");
		sql.append(" sum(ECS_CLASS_REAL_WASH_AMOUNT) ECS_CLASS_REAL_WASH_AMOUNT, ");
		sql.append(" sum(ECS_CLASS_PROMOTION_WASH_AMOUNT) ECS_CLASS_PROMOTION_WASH_AMOUNT, ");
		
		sql.append(" sum(OTM_CLASS_REAL_AMOUNT) OTM_CLASS_REAL_AMOUNT, ");
		sql.append(" sum(OTM_CLASS_PROMOTION_AMOUNT) OTM_CLASS_PROMOTION_AMOUNT, ");
		sql.append(" sum(OTM_CLASS_REAL_WASH_AMOUNT) OTM_CLASS_REAL_WASH_AMOUNT, ");
		sql.append(" sum(OTM_CLASS_PROMOTION_WASH_AMOUNT) OTM_CLASS_PROMOTION_WASH_AMOUNT, ");
		
		sql.append(" sum(OTHERS_REAL_AMOUNT) OTHERS_REAL_AMOUNT, ");
		sql.append(" sum(OTHERS_PROMOTION_AMOUNT) OTHERS_PROMOTION_AMOUNT, ");
		sql.append(" sum(OTHERS_REAL_WASH_AMOUNT) OTHERS_REAL_WASH_AMOUNT, ");
		sql.append(" sum(OTHERS_PROMOTION_WASH_AMOUNT) OTHERS_PROMOTION_WASH_AMOUNT, ");
		
		sql.append(" sum(LECTURE_REAL_AMOUNT) LECTURE_REAL_AMOUNT, ");
		sql.append(" sum(LECTURE_PROMOTION_AMOUNT) LECTURE_PROMOTION_AMOUNT, ");
		sql.append(" sum(LECTURE_REAL_WASH_AMOUNT) LECTURE_REAL_WASH_AMOUNT, ");
		sql.append(" sum(LECTURE_PROMOTION_WASH_AMOUNT) LECTURE_PROMOTION_WASH_AMOUNT, ");
		
		sql.append(" sum(IS_NORMAL_REAL_AMOUNT) IS_NORMAL_REAL_AMOUNT, ");
		sql.append(" sum(IS_NORMAL_PROMOTION_AMOUNT) IS_NORMAL_PROMOTION_AMOUNT, ");
		sql.append(" sum(IS_NORMAL_HISTORY_WASH_AMOUNT) IS_NORMAL_HISTORY_WASH_AMOUNT, ");
		sql.append(" COUNT_DATE, ");
		sql.append(" EVIDENCE_AUDIT_STATUS ");
		
		if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)){
			sql.append(" FROM ods_month_income_student omi ");
		} else {
			sql.append(" , sum(ieas.ADJUST_TOTAL_AMOUNT) ADJUST_TOTAL_AMOUNT, ");
			sql.append(" ieas.EDIVENCE_REMARK EDIVENCE_REMARK, ");
			sql.append(" ieas.ID SUMMARY_ID ");
			sql.append(" FROM ods_month_income_campus omi ");
			sql.append(" LEFT JOIN income_evidence_adjust_summary ieas ON omi.ID = ieas.EVIDENCE_ID ");
		}
		
		sql.append(" WHERE 1 = 1 ");
		if (StringUtil.isNotBlank(basicOperationQueryVo.getCountDate())) {
			sql.append(" AND COUNT_DATE = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}
		if (basicOperationQueryVo.getEvidenceAuditStatus() != null) {
			sql.append(" AND EVIDENCE_AUDIT_STATUS = :evidenceAuditStatus ");
			params.put("evidenceAuditStatus", basicOperationQueryVo.getEvidenceAuditStatus());
		}
		
		if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
			if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
				sql.append(" AND omi.BRENCH_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
				sql.append(" AND omi.CAMPUS_ID = :blCampusId ");
				params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
			}
		}
		
		if (StringUtil.isNotBlank(basicOperationQueryVo.getOrganizationId())) {
			sql.append(" AND omi.CAMPUS_ID = :organizationId ");
			params.put("organizationId", basicOperationQueryVo.getOrganizationId());
		}
		
		if (StringUtil.isNotBlank(basicOperationQueryVo.getBrenchId())) {
			sql.append(" AND omi.BRENCH_ID = :brenchId ");
			params.put("brenchId", basicOperationQueryVo.getBrenchId());
		}
		
//		sql.append(roleQLConfigService.getValueResult("营收凭证","sql"));
		sql.append(this.getEvidenceQlConfig());
		
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY omi.GROUP_ID, omi.BRENCH_ID ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY omi.GROUP_ID, omi.BRENCH_ID, omi.CAMPUS_ID ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
			sql.append(" GROUP BY omi.GROUP_ID, omi.BRENCH_ID, omi.CAMPUS_ID, omi.STUDENT_ID ");
		} 
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	/**
     * 剩余资金凭证报表
     */
    public DataPackage getRemainMonthlyEvidence(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp) {
        Map<String, Object> params = new HashMap<String, Object>();
        BasicOperationQueryLevelType basicOperationQueryLevelType = basicOperationQueryVo.getBasicOperationQueryLevelType();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
            sql.append(" '' ID, CONCAT(omra.GROUP_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.GROUP_ID)) GROUP_ID, "
                    + " CONCAT(omra.BRENCH_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.BRENCH_ID)) BRENCH_ID, "
                    + " '' CAMPUS_ID, '' STUDENT_NAME,  ");
        } else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
            sql.append(" omra.ID ID, CONCAT(omra.GROUP_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.GROUP_ID)) GROUP_ID, "
                    + " CONCAT(omra.BRENCH_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.BRENCH_ID)) BRENCH_ID, "
                    + " CONCAT(omra.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.CAMPUS_ID)) CAMPUS_ID, '' STUDENT_NAME,  ");
        } else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
            sql.append(" '' ID, CONCAT(omra.GROUP_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.GROUP_ID)) GROUP_ID, "
                    + " CONCAT(omra.BRENCH_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.BRENCH_ID)) BRENCH_ID, "
                    + " CONCAT(omra.CAMPUS_ID,'_',(SELECT NAME FROM organization WHERE ID = omra.CAMPUS_ID)) CAMPUS_ID, "
                    + " (SELECT NAME FROM student WHERE ID = omra.STUDENT_ID) STUDENT_NAME, ");
        }
        
        sql.append(" MAPPING_DATE, ");
        sql.append(" sum(REAL_REMAIN_AMOUNT_INIT) REAL_REMAIN_AMOUNT_INIT, ");
        sql.append(" sum(REAL_PAID_AMOUNT_MID) REAL_PAID_AMOUNT_MID, ");
        sql.append(" sum(WASH_AMOUNT_MID) WASH_AMOUNT_MID, ");
        
        sql.append(" sum(HISTORY_WASH_AMOUNT_MID) HISTORY_WASH_AMOUNT_MID, ");
        sql.append(" sum(ELECTRONIC_TRANSFER_IN_MID) ELECTRONIC_TRANSFER_IN_MID, ");
        sql.append(" sum(ELECTRONIC_RECHARGE_MID) ELECTRONIC_RECHARGE_MID, ");
        
        sql.append(" sum(REAL_CONSUME_AMOUNT_MID) REAL_CONSUME_AMOUNT_MID, ");
        sql.append(" sum(IS_NORMAL_REAL_AMOUNT_MID) IS_NORMAL_REAL_AMOUNT_MID, ");
        sql.append(" sum(REAL_RETURN_FEE_MID) REAL_RETURN_FEE_MID, ");
        
        sql.append(" sum(ELECTRONIC_TRANSFER_OUT_MID) ELECTRONIC_TRANSFER_OUT_MID, ");
        sql.append(" sum(REAL_WASH_AMOUNT_MID) REAL_WASH_AMOUNT_MID, ");
        sql.append(" sum(IS_NORMAL_WASH_AMOUNT_MID) IS_NORMAL_WASH_AMOUNT_MID, ");
        
        sql.append(" sum(REAL_HISTORY_CONSUME_AMOUNT_MID) REAL_HISTORY_CONSUME_AMOUNT_MID, ");
        sql.append(" sum(REAL_HISTORY_WASH_AMOUNT_MID) REAL_HISTORY_WASH_AMOUNT_MID, ");
        sql.append(" sum(IS_NORMAL_HISTORY_WASH_AMO_MID) IS_NORMAL_HISTORY_WASH_AMO_MID, ");
        
        sql.append(" sum(REAL_REMAIN_AMOUNT_FINAL) REAL_REMAIN_AMOUNT_FINAL, ");
        sql.append(" COUNT_DATE, ");
        sql.append(" EVIDENCE_AUDIT_STATUS ");
        
        if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)){
            sql.append(" FROM ods_month_remain_amount_student omra ");
        } else {
            sql.append(" FROM ods_month_remain_amount_campus omra ");
        }
        
        sql.append(" WHERE 1 = 1 ");
        if (StringUtil.isNotBlank(basicOperationQueryVo.getCountDate())) {
            sql.append(" AND COUNT_DATE = :countDate ");
            params.put("countDate", basicOperationQueryVo.getCountDate());
        }
        if (basicOperationQueryVo.getEvidenceAuditStatus() != null) {
            sql.append(" AND EVIDENCE_AUDIT_STATUS = :evidenceAuditStatus ");
            params.put("evidenceAuditStatus", basicOperationQueryVo.getEvidenceAuditStatus());
        }
        
        if (StringUtil.isNotBlank(basicOperationQueryVo.getBlCampusId())) {
            if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType)) {
//              sql.append(" AND omra.BRENCH_ID = '"+basicOperationQueryVo.getBlCampusId()+"' ");
            } else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType)) {
                sql.append(" AND omra.CAMPUS_ID = :blCampusId ");
                params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
            }
        }
        
        if (StringUtil.isNotBlank(basicOperationQueryVo.getOrganizationId())) {
            sql.append(" AND omra.CAMPUS_ID = :organizationId ");
            params.put("organizationId", basicOperationQueryVo.getOrganizationId());
        }
        
        if (StringUtil.isNotBlank(basicOperationQueryVo.getBrenchId())) {
            sql.append(" AND omra.BRENCH_ID = :brenchId ");
            params.put("brenchId", basicOperationQueryVo.getBrenchId());
        }
        
//      sql.append(roleQLConfigService.getValueResult("营收凭证","sql"));
        sql.append(this.getEvidenceQlConfig());
        
        if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryLevelType) ){
            sql.append(" GROUP BY omra.GROUP_ID, omra.BRENCH_ID ");
        } else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryLevelType) ){
            sql.append(" GROUP BY omra.GROUP_ID, omra.BRENCH_ID, omra.CAMPUS_ID ");
        } else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryLevelType) ){
            sql.append(" GROUP BY omra.GROUP_ID, omra.BRENCH_ID, omra.CAMPUS_ID, omra.STUDENT_ID ");
        } 
        
        if (StringUtils.isNotBlank(basicOperationQueryVo.getIsFilterZero()) && basicOperationQueryVo.getIsFilterZero().equals("TRUE")) {
            sql.append(" HAVING (sum(REAL_REMAIN_AMOUNT_INIT) != 0 OR sum(REAL_PAID_AMOUNT_MID) != 0 OR sum(WASH_AMOUNT_MID) != 0 "
                    + "OR sum(HISTORY_WASH_AMOUNT_MID) != 0 OR sum(ELECTRONIC_TRANSFER_IN_MID) != 0 ");
            sql.append(" OR sum(ELECTRONIC_RECHARGE_MID) != 0 OR sum(REAL_CONSUME_AMOUNT_MID) != 0 OR sum(IS_NORMAL_REAL_AMOUNT_MID) != 0 "
                    + "OR sum(REAL_RETURN_FEE_MID) != 0 OR sum(ELECTRONIC_TRANSFER_OUT_MID) != 0 ");
            sql.append(" OR sum(REAL_WASH_AMOUNT_MID) != 0 OR sum(IS_NORMAL_WASH_AMOUNT_MID) != 0 OR sum(REAL_HISTORY_CONSUME_AMOUNT_MID) != 0 "
                    + "OR sum(REAL_HISTORY_WASH_AMOUNT_MID) != 0 OR sum(IS_NORMAL_HISTORY_WASH_AMO_MID) != 0 ");
            sql.append(" OR sum(REAL_REMAIN_AMOUNT_FINAL) != 0) ");
        }
        List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
        dp.setDatas(list);
        dp.setRowCount(list.size());
        return dp;
    }
	
	@Override
	public List findInfoByMonth(BasicOperationQueryVo searchVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT ");
		if(BasicOperationQueryLevelType.GROUNP.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append("     `group_id` groupId,");
			sql.append("     `group_name` groupName,");
			sql.append("     `branch_id` branchId,");
			sql.append("     `branch_name` branchName,");
			sql.append("     '' campusId,");
			sql.append("     '' campusName,");
			sql.append("     '' studentName,");
		}else if(BasicOperationQueryLevelType.BRENCH.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append(" `id`,");
			sql.append("     `group_id` groupId,");
			sql.append("     `group_name` groupName,");
			sql.append("     `branch_id` branchId,");
			sql.append("     `branch_name` branchName,");
			sql.append("     `campus_id` campusId,");
			sql.append("     `campus_name` campusName,");
			sql.append("     '' studentName,");
		}else if(BasicOperationQueryLevelType.CAMPUS.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append("     `group_id` groupId,");
			sql.append("     `group_name` groupName,");
			sql.append("     `branch_id` branchId,");
			sql.append("     `branch_name` branchName,");
			sql.append("     `campus_id` campusId,");
			sql.append("     `campus_name` campusName,");
			sql.append("     (SELECT NAME FROM student WHERE ID = student_id) studentName,");
		}
		sql.append("     sum(`new_money`) newMoney,");
		sql.append("     sum(`re_money`) reMoney,");
		sql.append("     sum(`all_money`) allMoney,");
		sql.append("     sum(`wash_money`) washMoney,");
		sql.append("     sum(`total_money`) totalMoney,");
		sql.append("     sum(`bonus_money`) bonusMoney,");
		sql.append("     sum(`refund_money`) refundMoney,");
		sql.append("     sum(`special_refund_money`) specialRefundMoney,");
		sql.append("     sum(`total_refund_money`) totalRefundMoney,");
		sql.append("     sum(`refund_bonus_money`) refundBonusMoney,");
		sql.append("     `receipt_month` receiptMonth,");
		sql.append("     `receipt_date` receiptDate,");
		sql.append("     `receipt_status` receiptStatus,");
		
		sql.append("     sum(`total_finace`) totalFinace,");
		sql.append("     sum(`total_bonus`) totalBonus,");
		sql.append("     sum(`live_new_money`) liveNewMoney");
		
		if(BasicOperationQueryLevelType.CAMPUS.getValue().equals(searchVo.getConTypeOrProType())) {
			sql.append(" FROM `ods_month_payment_receipt_main_student` where 1=1 ");
		}else {
			sql.append("     ,`modify_time` modifyTime,");
			sql.append("     `modify_user` modifyUser,");
			sql.append("     sum(`modify_money`) modifyMoney,");
			sql.append("     sum(`after_modify_money`) afterModifyMoney,");
			sql.append("     sum(`live_new_money`) liveNewMoney,");
			sql.append("     `remark`");
			sql.append(" FROM `ods_month_payment_receipt_main` where 1=1 ");
		}
		
		if(StringUtils.isNotBlank(searchVo.getCountDate())){
			sql.append(" and receipt_Month= :countDate ");
			params.put("countDate", searchVo.getCountDate());
		}
		if(searchVo.getEvidenceAuditStatus()!=null){
			sql.append(" and receipt_status = :evidenceAuditStatus ");
			params.put("evidenceAuditStatus", searchVo.getEvidenceAuditStatus());
		}
		if (StringUtil.isNotBlank(searchVo.getOrganizationId())) {
			sql.append(" AND campus_id = :organizationId ");
			params.put("organizationId", searchVo.getOrganizationId());
		}
		if (StringUtil.isNotBlank(searchVo.getBrenchId())) {
			sql.append(" AND branch_id = :brenchId ");
			params.put("brenchId", searchVo.getBrenchId());
		}
		if (StringUtil.isNotBlank(searchVo.getBlCampusId())) {
			if(BasicOperationQueryLevelType.BRENCH.getValue().equals(searchVo.getConTypeOrProType())){
				sql.append(" AND branch_id = :blCampusId ");
				params.put("blCampusId", searchVo.getBlCampusId());
			}else if(BasicOperationQueryLevelType.CAMPUS.getValue().equals(searchVo.getConTypeOrProType())) {
				sql.append(" AND campus_id = :blCampusId ");
				params.put("blCampusId", searchVo.getBlCampusId());
			}
		}
		
		sql.append(this.getEvidenceQlConfig());
		if(BasicOperationQueryLevelType.GROUNP.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append(" group by branch_id");
		}else if(BasicOperationQueryLevelType.BRENCH.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append(" group by campus_id");
		}else if(BasicOperationQueryLevelType.CAMPUS.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append(" group by student_id ");
		}
		return 	this.findMapBySql(sql.toString(), params);
	}

    @Override
    public DataPackage getPlanManagementTotalForQUARTER(Integer preYear, String thisYear, String goalType, String targetType, DataPackage dp) {
    	Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select  ");
		if ("GROUP".equals(goalType)){
			sql.append(" o.name as groupName, ");
		}else if ("BRENCH".equals(goalType)){
			sql.append(" (select name from organization where id = o.parentId) as groupName, ");
			sql.append(" o.name as brenchName, ");
		}else {
			sql.append(" (SELECT name from organization where id=(SELECT parentID from organization where id=o.parentId)) as groupName, ");
			sql.append(" (SELECT name from organization where id=o.parentId) as brenchName, ");
			sql.append(" o.name as campusName, ");
		}

		sql.append(" ifNull(sum(lv.autumn),0) as autumn,ifNull(sum(lv.winter),0) as winter,ifNull(sum(lv.springtime),0) as springtime,ifNull(sum(lv.summer),0) as summer ");

		sql.append(" from organization o left join (SELECT GOAL_ID, CASE WHEN dd.`NAME` ='"+thisYear+"' AND pm.QUARTER_ID ='AUTUMN' THEN TARGET_VALUE ELSE 0 end AS autumn  ");
		sql.append(" , CASE WHEN dd.`NAME` = '"+thisYear+"' AND pm.QUARTER_ID = 'WINTER' THEN TARGET_VALUE ELSE 0 END AS winter ");
		sql.append(" , CASE WHEN dd.`NAME` = '"+thisYear+"' AND pm.QUARTER_ID = 'SPRINGTIME' THEN TARGET_VALUE ELSE 0 END  AS springtime ");
		sql.append(" , CASE WHEN dd.`NAME` = '"+thisYear+"' AND pm.QUARTER_ID = 'SUMMER' THEN TARGET_VALUE ELSE 0 END AS summer ");
		sql.append(" from PLAN_MANAGEMENT pm,data_dict dd ");
		sql.append(" where pm.YEAR_ID=dd.ID  and pm.TIME_TYPE='QUARTER' AND ( dd.`NAME`='"+thisYear+"') ");

		if(StringUtil.isNotEmpty(targetType)){
			sql.append(" and pm.TARGET_TYPE = :targetType ");
			params.put("targetType", targetType);
		}

		sql.append(" ) as lv  on lv.goal_id=o.id ");

		sql.append(" where 1=1 and o.orgType = :goalType " );
		params.put("goalType", goalType);
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}

		sql.append(" group by o.id  order by o.parentId ,o.id ");


		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
    }

	@Override
	public DataPackage getStudentSubjectStatusReport(
			BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ods.*, dd.name gradeName from ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS ods, organization org, student s left join data_dict dd on s.grade_id = dd.id where 1=1");
		sql.append(" and ods.campus_id = org.id and ods.STUDENT_ID = s.ID");
		if(StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())){
			sql.append(" and ods.count_date = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}else{
			throw new ApplicationException("请选日期再查询！");
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())){
			sql.append(" and ods.campus_id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getOrganizationId())){
			sql.append(" and ods.brench_id = :organizationId ");
			params.put("organizationId", basicOperationQueryVo.getOrganizationId());
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getStudyManegerId())){
			sql.append(" and ods.study_manager_id = :studyManegerId ");
			params.put("studyManegerId", basicOperationQueryVo.getStudyManegerId());
		}
		
		if(StringUtils.isNotBlank(basicOperationQueryVo.getStudentId())){
			sql.append(" and ods.student_id = :studentId ");
			params.put("studentId", basicOperationQueryVo.getStudentId());
		}
		
		if (studentOooStatus != null) {
			sql.append(" and ods.STUDENT_ONEONONE_STATUS = :studentOneOnOneStatus ");
			params.put("studentOneOnOneStatus", studentOooStatus);
		}
		
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		List<Role> roleList = userService.getCurrentLoginUserRoles();
		// 判断是否有学管角色
		boolean hasStudyManage = false;
		for (Role role : roleList) {
			if (role.getRoleCode() == RoleCode.STUDY_MANAGER) {
				hasStudyManage = true;
				break;
			}
		}
		// 判断是否有非部门组织架构
		boolean hasNotDepartment = false;
		if (hasStudyManage) {
			for (Organization org : userOrganizations) {
				if (org.getOrgType() != OrganizationType.DEPARTMENT) {
					hasNotDepartment = true;
					break;
				}
			}
		}
		// 存在有学管角色并且没有非部门组织架构
		if (hasStudyManage && !hasNotDepartment) {
			sql.append(" and ods.study_manager_id = '" + userService.getCurrentLoginUser().getUserId() + "' ");
		} else {
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				if (StringUtil.isNotBlank(org.getBelong()) && !org.getBelong().equals(org.getId())) {
					org = organizationDao.findById(org.getBelong());
				}
				sql.append("  and (org.orgLevel like '").append(org.getOrgLevel()).append("%' ");
				for(int i = 1; i < userOrganizations.size(); i++){
					org = userOrganizations.get(i);
					if (StringUtil.isNotBlank(org.getBelong()) && !org.getBelong().equals(org.getId())) {
						org = organizationDao.findById(org.getBelong());
					}
					sql.append(" or org.orgLevel like '").append(org.getOrgLevel()).append("%'");
				}
				sql.append(") ");
			}
		}
		
		sql.append(" order by ods.campus_id, ods.study_manager_id, s.create_time desc ");
		
		dp.setRowCount(super.findCountSql("select count(*) " + sql.substring(sql.indexOf("from") >= 0 ? sql.indexOf("from") : sql.indexOf("FROM")), params, 300));
		return this.findMapPageBySQL(sql.toString(), dp, false, params);
	}
	
	@Override
	public List<Map<Object,Object>> exportStudentSubjectStatusReport(
			BasicOperationQueryVo basicOperationQueryVo, StudentOneOnOneStatus studentOooStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ods.*, dd.name gradeName from ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS ods, student s left join data_dict dd on s.grade_id = dd.id where 1=1 and ods.STUDENT_ID = s.ID");
		if(StringUtils.isNotBlank(basicOperationQueryVo.getCountDate())){
			sql.append(" and ods.count_date = :countDate ");
			params.put("countDate", basicOperationQueryVo.getCountDate());
		}else{
			throw new ApplicationException("请选日期再查询！");
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getBlCampusId())){
			sql.append(" and ods.campus_id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getOrganizationId())){
			sql.append(" and ods.brench_id = :organizationId ");
			params.put("organizationId", basicOperationQueryVo.getOrganizationId());
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getStudyManegerId())){
			sql.append(" and ods.study_manager_id = :studyManegerId ");
			params.put("studyManegerId", basicOperationQueryVo.getStudyManegerId());
		}
		
		if(StringUtils.isNotBlank(basicOperationQueryVo.getStudentId())){
			sql.append(" and ods.student_id = :studentId ");
			params.put("studentId", basicOperationQueryVo.getStudentId());
		}
		
		if (studentOooStatus != null) {
			sql.append(" and ods.STUDENT_ONEONONE_STATUS = :studentOneOnOneStatus ");
			params.put("studentOneOnOneStatus", studentOooStatus);
		}
		
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		List<Role> roleList = userService.getCurrentLoginUserRoles();
		// 判断是否有学管角色
		boolean hasStudyManage = false;
		for (Role role : roleList) {
			if (role.getRoleCode() == RoleCode.STUDY_MANAGER) {
				hasStudyManage = true;
				break;
			}
		}
		// 判断是否有非部门组织架构
		boolean hasNotDepartment = false;
		if (hasStudyManage) {
			for (Organization org : userOrganizations) {
				if (org.getOrgType() != OrganizationType.DEPARTMENT) {
					hasNotDepartment = true;
					break;
				}
			}
		}
		// 存在有学管角色并且没有非部门组织架构
		if (hasStudyManage && !hasNotDepartment) {
			sql.append(" and ods.study_manager_id = '" + userService.getCurrentLoginUser().getUserId() + "' ");
		} else {
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				sql.append("  and ods.campus_id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
				for(int i = 1; i < userOrganizations.size(); i++){
					sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
				}
				sql.append(") ");
			}
		}
		
		sql.append(" order by ods.campus_id, ods.study_manager_id, s.create_time desc ");
		
		return this.findMapBySql(sql.toString(), params);
	}
	
	private String getFinanceContractAnalyzeRateAppendSql(BasicOperationQueryVo basicOperationQueryVo, Map<String, Object> params, String targetType, int times) {
		StringBuffer appendSql = new StringBuffer();
		if(StringUtils.isNotBlank(basicOperationQueryVo.getYearId())){
			appendSql.append(" and pm.YEAR_ID = :yearId" + times);
			params.put("yearId" + times, basicOperationQueryVo.getYearId());
		}
		if(StringUtils.isNotBlank(basicOperationQueryVo.getMonthId())){
			appendSql.append(" and pm.MONTH_ID= :monthId" + times);
			params.put("monthId" + times, basicOperationQueryVo.getMonthId());
		}
		if(StringUtils.isNotBlank(targetType)){
			appendSql.append(" and pm.TARGET_TYPE = :targetType" + times);
			params.put("targetType" + times, targetType);
		}
		return appendSql.toString();
		
	}
	
	private String getCourseConsomeAnalyseRoleColumn(RoleCode roleCode) {
		StringBuffer sql = new StringBuffer("");
		if (RoleCode.TEATCHER.equals(roleCode)) {
			sql.append(" f.TEACHER_ID ");
		} else if (RoleCode.STUDY_MANAGER.equals(roleCode)) {
			sql.append(" f.STY_MNG_ID ");
		} else if (RoleCode.EDUCAT_SPEC.equals(roleCode)) {
			sql.append(" f.EDU_ID ");
		}
		return sql.toString();
	}

	private String getCourseConsomeAnalyseConcatColumn(RoleCode roleCode) {
		String sql ="";
		if (RoleCode.TEATCHER.equals(roleCode)) {
			sql = " CONCAT(f.TEACHER_ID, '_', (select u.name from user u where u.USER_ID = f.TEACHER_ID)) as userId ";
		} else if (RoleCode.STUDY_MANAGER.equals(roleCode)) {
			sql = " CONCAT(f.STY_MNG_ID, '_', (select u.name from user u where u.USER_ID = f.STY_MNG_ID)) as userId ";
		} else if (RoleCode.EDUCAT_SPEC.equals(roleCode)) {
			sql = " CONCAT(f.EDU_ID, '_', (select u.name from user u where u.USER_ID = f.EDU_ID)) as userId ";
		}
		return sql;
	}
	
	private String getRefundAnalyzeColumn(RoleCode roleCode) {
		String sql = " CONCAT(f.SIGN_STAFF_ID, '_', (select u.name from user u where u.USER_ID = f.SIGN_STAFF_ID)) as userId ";
		return sql;
	}
	
	private String getTimeSql(BasicOperationQueryVo basicOperationQueryVo, Map<String, Object> params, int times) {
		StringBuffer timeSql =new StringBuffer();
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			timeSql.append(" AND TRANSACTION_TIME >= :startDate" + times);
			params.put("startDate" + times, basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			timeSql.append(" AND TRANSACTION_TIME <= :endDate" + times);
			params.put("endDate" + times, basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		return timeSql.toString();
	}
	
	private String getEvidenceQlConfig() {
		String evidenceJobIdStr = "xqpzshz,pzcwcsz,fgspzshz,pzcwzsz,pzckz";
		String returnSql = "";
		User currentUser = userService.getCurrentLoginUser();
		List<UserDeptJob> userDeptJobList = userDeptJobDao.findDeptJobByUserId(currentUser.getUserId());
		String orgStr = "";
		List<Organization> orgList = new ArrayList<Organization>();
		for (UserDeptJob userDeptJob : userDeptJobList) {
			if (orgStr.indexOf(userDeptJob.getDeptId()) < 0 && evidenceJobIdStr.indexOf(userDeptJob.getJobId()) >= 0) {
				Organization org = organizationDao.findById(userDeptJob.getDeptId());
				String belongId = org.getBelong() != null ? org.getBelong() : org.getId();
				orgStr += belongId;
				if (org.getBelong() != null) {
					orgList.add(organizationDao.findById(belongId));
				} else {
					orgList.add(org);
				}
			}
		}
		if (orgList.size() > 0) {
			returnSql += " AND ( ";
			for (Organization org : orgList) {
				if (org.getOrgType() == OrganizationType.BRENCH || org.getOrgType() == OrganizationType.GROUNP) {
					returnSql += " CAMPUS_ID IN (SELECT ID FROM organization WHERE orgLevel LIKE '" + org.getOrgLevel() + "%') OR ";
				} else {
					returnSql += " CAMPUS_ID = '" + org.getId() + "' OR ";
				}
			}
			returnSql = returnSql.substring(0, returnSql.length() - 4);
			returnSql += " ) ";
		} else {
			returnSql += " AND 1=2 ";
		}
		return returnSql;
	}

	@Override
	public List getTodayMonthSmallConsume() {
		//产品类型  今日课消（课时） 今日扣款 本月课消  本月扣款（元 ）
		//获取分公司 校区 
		StringBuffer sqlWhere = new StringBuffer();
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sqlWhere.append("  o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sqlWhere.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
		}
		
		
		StringBuffer sql= new StringBuffer("");
		sql.append(" select a.brenchId,a.campusId,sum(a.consumeHours) as consumeHours,sum(a.consumeAmount) as consumeAmount, ");
		sql.append(" sum(a.todayHours) as todayHours,sum(a.todayAmount) as todayAmount ");
		sql.append(" from ( ");
		sql.append(" select oo.name as brenchId,o.name as campusId,sum(mcc.COURSE_HOURS) as consumeHours, ");
		sql.append(" sum(0) as consumeAmount, ");
		sql.append(" sum(case when mcc.COURSE_DATE >= concat(CURDATE(),' 00:00:00') AND mcc.COURSE_DATE <= concat(CURDATE(),' 23:59:59') then mcc.COURSE_HOURS else 0 end) as todayHours,");
		sql.append(" sum(0) as todayAmount ");
		sql.append(" from mini_class_course mcc,mini_class mc,organization o,organization oo ");
		sql.append(" where mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID  ");	
		sql.append(" and mc.BL_CAMPUS_ID = o.id ");
		sql.append(" and o.parentID = oo.id ");
		sql.append(" AND mcc.COURSE_DATE >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND mcc.COURSE_DATE <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}	
		sql.append(" and mcc.COURSE_STATUS ='CHARGED' ");
		sql.append(" group by  oo.id,o.id ");
		
		sql.append(" UNION ALL ");
		sql.append(" select d. NAME AS brenchId,o. NAME AS campusId, ");
		sql.append(" sum(0) AS consumeHours, ");
		sql.append(" sum(f.AMOUNT) AS consumeAmount, ");
		sql.append(" sum(0) AS todayHours, ");
		sql.append(" sum( CASE WHEN TRANSACTION_TIME >= concat(CURDATE(), ' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(), CURRENT_TIME()) THEN f.AMOUNT ELSE 0 END) AS todayAmount ");
		sql.append(" FROM account_charge_records f,organization o ,organization d ");
		sql.append(" where o.id = f.BL_CAMPUS_ID ");
		sql.append(" and d.id = o.parentID ");
		sql.append(" AND f.PRODUCT_TYPE = 'SMALL_CLASS' ");
		sql.append(" AND f.TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND f.TRANSACTION_TIME <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		sql.append(" AND f.CHARGE_TYPE = 'NORMAL' ");
		sql.append(" AND f.IS_WASHED = 'FALSE' ");
		sql.append(" AND F.CHARGE_PAY_TYPE = 'CHARGE' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}

		sql.append(" GROUP BY d.id,o.id) as a ");
		sql.append(" group by a.brenchId,a.campusId order by consumeHours desc  ");
		
		List list = this.findMapBySql(sql.toString(), new HashMap<String, Object>());
		return list;
	}

	@Override
	public List getTodayMonthOtherConsume() {
		//包括 一对多 双师  由于双师主班属于分公司 不在校区 没算入统计之中
		
		StringBuffer sqlWhere = new StringBuffer();
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sqlWhere.append("  o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sqlWhere.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
		}
		StringBuffer sql= new StringBuffer("");
		sql.append(" select a.brenchId,a.campusId,sum(a.consumeHours) as consumeHours,sum(a.consumeAmount) as consumeAmount, ");
		sql.append(" sum(a.todayHours) as todayHours,sum(a.todayAmount) as todayAmount ");
		sql.append(" from ( ");
		sql.append(" select oo.name as brenchId,o.name as campusId, ");
		sql.append(" sum(occ.COURSE_HOURS) as consumeHours, ");
		sql.append(" sum(0) as consumeAmount,");
		sql.append(" sum(case when occ.COURSE_DATE >= concat(CURDATE(),' 00:00:00') AND occ.COURSE_DATE <= concat(CURDATE(),' 23:59:59') then occ.COURSE_HOURS else 0 end) as todayHours, ");
		sql.append(" sum(0) as todayAmount ");
		sql.append(" from otm_class_course occ,otm_class oc,organization o,organization oo ");
		sql.append(" where occ.OTM_CLASS_ID = oc.OTM_CLASS_ID ");
		sql.append(" and oc.BL_CAMPUS_ID = o.id ");
		sql.append(" and o.parentID = oo.id ");
		sql.append(" and occ.COURSE_STATUS ='CHARGED' ");
		sql.append(" AND occ.COURSE_DATE >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND occ.COURSE_DATE <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" group by  oo.id,o.id ");
		sql.append(" UNION ALL ");
		sql.append(" select d. NAME AS brenchId,o. NAME AS campusId, ");
		sql.append(" sum(0) AS consumeHours, ");
		sql.append(" sum(f.AMOUNT) AS consumeAmount, ");
		sql.append(" sum(0) AS todayHours, ");
		sql.append(" sum( CASE WHEN TRANSACTION_TIME >= concat(CURDATE(), ' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(), CURRENT_TIME()) THEN f.AMOUNT ELSE 0 END) AS todayAmount ");
		sql.append(" FROM account_charge_records f,organization o ,organization d ");
		sql.append(" where o.id = f.BL_CAMPUS_ID ");
		sql.append(" and d.id = o.parentID ");
		sql.append(" AND f.PRODUCT_TYPE = 'ONE_ON_MANY' ");
		sql.append(" AND f.TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND f.TRANSACTION_TIME <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		sql.append(" AND f.CHARGE_TYPE = 'NORMAL' ");
		sql.append(" AND f.IS_WASHED = 'FALSE' ");
		sql.append(" AND F.CHARGE_PAY_TYPE = 'CHARGE' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" GROUP BY d.id,o.id ");
		sql.append(" UNION ALL ");
		
		sql.append(" select oo.name as brenchId,o.name as campusId, ");
		sql.append(" sum(ttcc.COURSE_HOURS) as consumeHours, ");
		sql.append(" sum(0) as consumeAmount, ");
		sql.append(" sum(case when ttcc.COURSE_DATE >= concat(CURDATE(),' 00:00:00') AND ttcc.COURSE_DATE <= concat(CURDATE(),' 23:59:59') then ttcc.COURSE_HOURS else 0 end) as todayHours, ");
		sql.append(" sum(0) as todayAmount ");
		sql.append(" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,organization o,organization oo  ");
		sql.append(" where ttcc.CLASS_ID = ttc.CLASS_ID ");
		sql.append(" and ttct.CLASS_ID = ttc.CLASS_ID ");
		sql.append(" and ttct.BL_CAMPUS_ID = o.id ");
		sql.append(" and o.parentID = oo.id ");
		sql.append(" and ttcc.COURSE_STATUS ='CHARGED' ");
		sql.append(" AND ttcc.COURSE_DATE >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND ttcc.COURSE_DATE <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" group by  oo.id,o.id ");
		sql.append(" UNION ALL ");
		sql.append(" select d. NAME AS brenchId,o. NAME AS campusId, ");
		sql.append(" sum(0) AS consumeHours,");
		sql.append(" sum(f.AMOUNT) AS consumeAmount, ");
		sql.append(" sum(0) AS todayHours, ");
		sql.append(" sum( CASE WHEN TRANSACTION_TIME >= concat(CURDATE(), ' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(), CURRENT_TIME()) THEN f.AMOUNT ELSE 0 END) AS todayAmount ");
		sql.append(" FROM account_charge_records f,organization o ,organization d ");
		sql.append(" where o.id = f.BL_CAMPUS_ID ");
		sql.append(" and d.id = o.parentID ");
		sql.append(" AND f.PRODUCT_TYPE = 'TWO_TEACHER' ");
		sql.append(" AND f.TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND f.TRANSACTION_TIME <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		sql.append(" AND f.CHARGE_TYPE = 'NORMAL' ");
		sql.append(" AND f.IS_WASHED = 'FALSE' ");
		sql.append(" AND F.CHARGE_PAY_TYPE = 'CHARGE' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" GROUP BY d.id,o.id ");
		sql.append(" ) as a ");
		sql.append(" group by a.brenchId,a.campusId  ");
		sql.append(" order by consumeHours desc ");
		
		List list = this.findMapBySql(sql.toString(), new HashMap<String, Object>());
		return list;
	}

	@Override
	public List getTodayMonthTotalConsume() {
		//一对一 小班  其他 一对多双师  
		StringBuffer sqlWhere = new StringBuffer();
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sqlWhere.append("  o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sqlWhere.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
		}

		StringBuffer sql= new StringBuffer("");

		sql.append(" select a.brenchId,a.campusId,sum(a.consumeHours) as consumeHours,sum(a.consumeAmount) as consumeAmount, ");
		sql.append(" sum(a.todayHours) as todayHours,sum(a.todayAmount) as todayAmount ");
		sql.append(" from ( ");

		sql.append(" select d. NAME AS brenchId,o. NAME AS campusId,");
		sql.append(" sum(f.QUANTITY) as consumeHours,");
		sql.append(" sum(f.AMOUNT) as consumeAmount,");
		sql.append(" sum(case when TRANSACTION_TIME >= concat(CURDATE(),' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(),current_time()) then f.QUANTITY else 0 end) as todayHours, ");
		sql.append(" sum(case when TRANSACTION_TIME >= concat(CURDATE(),' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(),current_time()) then f.AMOUNT else 0 end) as todayAmount ");
		sql.append(" FROM account_charge_records f,organization o ,organization d ");
		sql.append(" where o.id = f.BL_CAMPUS_ID ");
		sql.append(" and d.id = o.parentID ");
		sql.append(" AND f.PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' ");
		sql.append(" AND f.TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND f.TRANSACTION_TIME <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		sql.append(" AND f.CHARGE_TYPE = 'NORMAL' ");
		sql.append(" AND f.IS_WASHED = 'FALSE' ");
		sql.append(" AND F.CHARGE_PAY_TYPE = 'CHARGE' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" GROUP BY d.id,o.id ");

		sql.append(" UNION ALL ");
		sql.append(" select oo.name as brenchId,o.name as campusId,sum(mcc.COURSE_HOURS) as consumeHours, ");
		sql.append(" sum(0) as consumeAmount, ");
		sql.append(" sum(case when mcc.COURSE_DATE >= concat(CURDATE(),' 00:00:00') AND mcc.COURSE_DATE <= concat(CURDATE(),' 23:59:59') then mcc.COURSE_HOURS else 0 end) as todayHours,");
		sql.append(" sum(0) as todayAmount ");
		sql.append(" from mini_class_course mcc,mini_class mc,organization o,organization oo ");
		sql.append(" where mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID  ");
		sql.append(" and mc.BL_CAMPUS_ID = o.id ");
		sql.append(" and o.parentID = oo.id ");
		sql.append(" AND mcc.COURSE_DATE >= '"+DateTools.getFistDayofMonth()+"' ");
		sql.append(" AND mcc.COURSE_DATE <= '"+DateTools.getCurrentDate()+"' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" and mcc.COURSE_STATUS ='CHARGED' ");
		sql.append(" group by  oo.id,o.id ");

		sql.append(" UNION ALL ");
		sql.append(" select d. NAME AS brenchId,o. NAME AS campusId, ");
		sql.append(" sum(0) AS consumeHours, ");
		sql.append(" sum(f.AMOUNT) AS consumeAmount, ");
		sql.append(" sum(0) AS todayHours, ");
		sql.append(" sum( CASE WHEN TRANSACTION_TIME >= concat(CURDATE(), ' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(), CURRENT_TIME()) THEN f.AMOUNT ELSE 0 END) AS todayAmount ");
		sql.append(" FROM account_charge_records f,organization o ,organization d ");
		sql.append(" where o.id = f.BL_CAMPUS_ID ");
		sql.append(" and d.id = o.parentID ");
		sql.append(" AND f.PRODUCT_TYPE = 'SMALL_CLASS' ");
		sql.append(" AND f.TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND f.TRANSACTION_TIME <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		sql.append(" AND f.CHARGE_TYPE = 'NORMAL' ");
		sql.append(" AND f.IS_WASHED = 'FALSE' ");
		sql.append(" AND F.CHARGE_PAY_TYPE = 'CHARGE' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" GROUP BY d.id,o.id ");

		sql.append(" UNION ALL ");
		sql.append(" select oo.name as brenchId,o.name as campusId, ");
		sql.append(" sum(occ.COURSE_HOURS) as consumeHours, ");
		sql.append(" sum(0) as consumeAmount,");
		sql.append(" sum(case when occ.COURSE_DATE >= concat(CURDATE(),' 00:00:00') AND occ.COURSE_DATE <= concat(CURDATE(),' 23:59:59') then occ.COURSE_HOURS else 0 end) as todayHours, ");
		sql.append(" sum(0) as todayAmount ");
		sql.append(" from otm_class_course occ,otm_class oc,organization o,organization oo ");
		sql.append(" where occ.OTM_CLASS_ID = oc.OTM_CLASS_ID ");
		sql.append(" and oc.BL_CAMPUS_ID = o.id ");
		sql.append(" and o.parentID = oo.id ");
		sql.append(" and occ.COURSE_STATUS ='CHARGED' ");
		sql.append(" AND occ.COURSE_DATE >= '"+DateTools.getFistDayofMonth()+"' ");
		sql.append(" AND occ.COURSE_DATE <= '"+DateTools.getCurrentDate()+"' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" group by  oo.id,o.id ");

		sql.append(" UNION ALL ");
		sql.append(" select d. NAME AS brenchId,o. NAME AS campusId, ");
		sql.append(" sum(0) AS consumeHours, ");
		sql.append(" sum(f.AMOUNT) AS consumeAmount, ");
		sql.append(" sum(0) AS todayHours, ");
		sql.append(" sum( CASE WHEN TRANSACTION_TIME >= concat(CURDATE(), ' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(), CURRENT_TIME()) THEN f.AMOUNT ELSE 0 END) AS todayAmount ");
		sql.append(" FROM account_charge_records f,organization o ,organization d ");
		sql.append(" where o.id = f.BL_CAMPUS_ID ");
		sql.append(" and d.id = o.parentID ");
		sql.append(" AND f.PRODUCT_TYPE = 'ONE_ON_MANY' ");
		sql.append(" AND f.TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND f.TRANSACTION_TIME <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		sql.append(" AND f.CHARGE_TYPE = 'NORMAL' ");
		sql.append(" AND f.IS_WASHED = 'FALSE' ");
		sql.append(" AND F.CHARGE_PAY_TYPE = 'CHARGE' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" GROUP BY d.id,o.id ");
		sql.append(" UNION ALL ");

		sql.append(" select oo.name as brenchId,o.name as campusId, ");
		sql.append(" sum(ttcc.COURSE_HOURS) as consumeHours, ");
		sql.append(" sum(0) as consumeAmount, ");
		sql.append(" sum(case when ttcc.COURSE_DATE >= concat(CURDATE(),' 00:00:00') AND ttcc.COURSE_DATE <= concat(CURDATE(),' 23:59:59') then ttcc.COURSE_HOURS else 0 end) as todayHours, ");
		sql.append(" sum(0) as todayAmount ");
		sql.append(" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,organization o,organization oo  ");
		sql.append(" where ttcc.CLASS_ID = ttc.CLASS_ID ");
		sql.append(" and ttct.CLASS_ID = ttc.CLASS_ID ");
		sql.append(" and ttct.BL_CAMPUS_ID = o.id ");
		sql.append(" and o.parentID = oo.id ");
		sql.append(" and ttcc.COURSE_STATUS ='CHARGED' ");
		sql.append(" AND ttcc.COURSE_DATE >= '"+DateTools.getFistDayofMonth()+"' ");
		sql.append(" AND ttcc.COURSE_DATE <= '"+DateTools.getCurrentDate()+"' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" group by  oo.id,o.id ");

		sql.append(" UNION ALL ");
		sql.append(" select d. NAME AS brenchId,o. NAME AS campusId, ");
		sql.append(" sum(0) AS consumeHours,");
		sql.append(" sum(f.AMOUNT) AS consumeAmount, ");
		sql.append(" sum(0) AS todayHours, ");
		sql.append(" sum( CASE WHEN TRANSACTION_TIME >= concat(CURDATE(), ' 00:00:00') AND TRANSACTION_TIME <= concat(CURDATE(), CURRENT_TIME()) THEN f.AMOUNT ELSE 0 END) AS todayAmount ");
		sql.append(" FROM account_charge_records f,organization o ,organization d ");
		sql.append(" where o.id = f.BL_CAMPUS_ID ");
		sql.append(" and d.id = o.parentID ");
		sql.append(" AND f.PRODUCT_TYPE = 'TWO_TEACHER' ");
		sql.append(" AND f.TRANSACTION_TIME >= '"+DateTools.getFistDayofMonth()+" 00:00:00' ");
		sql.append(" AND f.TRANSACTION_TIME <= '"+DateTools.getCurrentDate()+" 23:59:59' ");
		sql.append(" AND f.CHARGE_TYPE = 'NORMAL' ");
		sql.append(" AND f.IS_WASHED = 'FALSE' ");
		sql.append(" AND F.CHARGE_PAY_TYPE = 'CHARGE' ");
		if(userOrganizations != null && userOrganizations.size() > 0){
			sql.append(" AND ( ");
			sql.append(sqlWhere);
			sql.append(" ) ");
		}
		sql.append(" GROUP BY d.id,o.id ");

		sql.append(" ) as a ");
		sql.append(" group by a.brenchId,a.campusId order by consumeHours desc  ");

		List list = this.findMapBySql(sql.toString(), new HashMap<String, Object>());
		return list;
	}

    @Override
    public DataPackage getTwoTeacherCourseTotal(ModelVo modelVo,DataPackage dataPackage) {
		Map<String, Object> params = Maps.newHashMap();
    	StringBuilder sql = new StringBuilder();
    	 sql.append(" select '星火集团' GROUNP,concat(o.id,\"_\",o.name) CAMPUS,o2.name BRENCH,count(distinct two.CLASS_TWO_ID,course.course_id) allcourse")
			.append(" ,count(distinct (case when ttcsa.CHARGE_STATUS='CHARGED' THEN concat(two.CLASS_TWO_ID,course.course_id) END)) charged")
			.append(" ,sum(1) studentNums")
			.append(" ,sum(case when ttcsa.ATTENDENT_STATUS='NEW' THEN  1 else 0 END) newstudent")
			.append(" ,sum(case when ttcsa.ATTENDENT_STATUS='CONPELETE' THEN  1 else 0 END) conpelete")
			.append(" ,sum(case when ttcsa.ATTENDENT_STATUS='LATE' THEN  1 else 0 END) late")
			.append(" ,sum(case when ttcsa.ATTENDENT_STATUS='ABSENT' THEN  1 else 0 END) absent")
			.append(" ,sum(case when ttcsa.ATTENDENT_STATUS='LEAVE' THEN  1 else 0 END) leaveNum")
			.append(" ,sum(case when ttcsa.CHARGE_STATUS='CHARGED' THEN  1 else 0 END) chargedstu")
			.append(" ,sum(case when ttcsa.CHARGE_STATUS='UNCHARGE' THEN 1 else 0 END) unchargestu")
			.append(" from two_teacher_class_student_attendent ttcsa")
			.append(" left join two_teacher_class_two two on two.CLASS_TWO_ID= ttcsa.CLASS_TWO_ID")
			.append(" left join organization o on o.id = two.BL_CAMPUS_ID")
			.append(" left join organization o2 on o2.id = o.parentID")
				 .append(" left join two_teacher_class_course course on course.COURSE_ID= ttcsa.TWO_CLASS_COURSE_ID")
				 .append(" left join two_teacher_class cla on cla.CLASS_ID= course.CLASS_ID")
				 .append(" left join product p on p.ID= cla.PRODUCE_ID")
			.append(" where ttcsa.COURSE_STATUS<>'CANCEL'");


		String orgLevel=null;
		if( StringUtils.isNotBlank(modelVo.getModelName1())){
			orgLevel = organizationDao.findById(modelVo.getModelName1()).getOrgLevel();
		}

		if(StringUtils.isNotBlank(modelVo.getStartDate())){
			sql.append(" and course.COURSE_DATE >=  :startDate ");
			params.put("startDate", modelVo.getStartDate());
		}
		if(StringUtils.isNotBlank(modelVo.getEndDate())){
			sql.append(" and course.COURSE_DATE <= :endDate ");
			params.put("endDate", modelVo.getEndDate());
		}
		if(StringUtils.isNotBlank(orgLevel)){
			sql.append(" and o.orgLevel LIKE :orgLevel ");
			params.put("orgLevel", orgLevel+"%");
		}

		if(StringUtils.isNotBlank(modelVo.getModelName2())){
			String[] otmClassTypes=StringUtil.replaceSpace(modelVo.getModelName2()).split(",");
			if(otmClassTypes.length>0){
				sql.append(" and p.CLASS_TYPE_ID in(:otmClassTypes) ");
				params.put("otmClassTypes", otmClassTypes);
			}
		}

		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append(" and ( o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}


		sql.append(" group by two.BL_CAMPUS_ID");

		sql.append(" order by count(distinct (case when ttcsa.CHARGE_STATUS='CHARGED' THEN two.CLASS_TWO_ID END))/count(distinct two.CLASS_TWO_ID) desc");

        return this.findMapPageBySQL(sql.toString(),dataPackage,true,params);
    }

    @Override
    public List<Map<Object, Object>> exportTwoTeacherMainClassesReport(
            ModelVo modelVo) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select og.`name` GROUP_NAME, ob.`name` BRANCH_NAME, a.TEACHER_NAME, a.CAMPUS_NAME, ")
            .append(" sum(case when a.CLASS_COUNT <=6 then COURSE_COUNT else 0 end) ONE_TO_SIX_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 7 then COURSE_COUNT else 0 end) SEVEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 8 then COURSE_COUNT else 0 end) EIGHT_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 9 then COURSE_COUNT else 0 end) NINE_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 10 then COURSE_COUNT else 0 end) TEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 11 then COURSE_COUNT else 0 end) ELEVEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 12 then COURSE_COUNT else 0 end) TWELVE_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 13 then COURSE_COUNT else 0 end) THIRTEEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 14 then COURSE_COUNT else 0 end) FOURTEEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 15 then COURSE_COUNT else 0 end) FIFTEEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 16 then COURSE_COUNT else 0 end) SIXTEEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 17 then COURSE_COUNT else 0 end) SEVENTEEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 18 then COURSE_COUNT else 0 end) EIGHTTEEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 19 then COURSE_COUNT else 0 end) NINETEEN_CLASSES, ")
            .append(" sum(case when a.CLASS_COUNT = 20 then COURSE_COUNT else 0 end) TWENTY_CLASSES ")
            .append(" from")
            .append(" (select belong_org.id ORG_ID, belong_org.parentID, belong_org.`name` CAMPUS_NAME, ttc.CLASS_ID, ")
            .append(" ttc.TEACHER_ID, u.name TEACHER_NAME, count(DISTINCT ttct.CLASS_TWO_ID) CLASS_COUNT, count(DISTINCT ttcsa.TWO_CLASS_COURSE_ID) COURSE_COUNT ")
            .append(" from two_teacher_class_two ttct ")
            .append(" left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID ")
            .append(" left join two_teacher_class_course ttcc on ttcc.CLASS_ID = ttc.CLASS_ID ")
            .append(" left join two_teacher_class_student_attendent ttcsa on ttcsa.TWO_CLASS_COURSE_ID = ttcc.COURSE_ID ")
            .append(" inner join user u on ttc.TEACHER_ID = u.USER_ID ")
            .append(" inner join organization o on u.organizationID = o.id ")
            .append(" inner join organization belong_org on o.BELONG = belong_org.id ")
            .append(" where ttcsa.COURSE_STATUS = 'CHARGED' ");
        if(StringUtils.isNotBlank(modelVo.getStartDate())){
            sql.append(" and ttcc.COURSE_DATE >=  :startDate ");
            params.put("startDate", modelVo.getStartDate());
        }
        if(StringUtils.isNotBlank(modelVo.getEndDate())){
            sql.append(" and ttcc.COURSE_DATE <= :endDate ");
            params.put("endDate", modelVo.getEndDate());
        }
        List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append(" and ( o.orgLevel like '").append(org.getOrgLevel()).append("%'");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(")");
        }
        sql.append(" group by ttc.CLASS_ID) a ")
            .append(" inner join organization ob on ifnull(a.parentID, a.ORG_ID) = ob.id ")
            .append(" inner join organization og on ifnull(ob.parentID, ob.id) = og.id ")
            .append(" group by a.TEACHER_ID; ");
        return this.findMapBySql(sql.toString(), params);
    }

    @Override
    public List<Map<Object, Object>> exportTwoTeacherAuxiliaryClassesReport(
            ModelVo modelVo) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select og.`name` GROUP_NAME, ob.`name` BRANCH_NAME, a.TEACHER_NAME, a.CAMPUS_NAME, ");
        sql.append(" sum(case when a.STU_COUNT = 1 then 1 else 0 end) ONE_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 2 then 1 else 0 end) TWO_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 3 then 1 else 0 end) THREE_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 4 then 1 else 0 end) FOUR_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 5 then 1 else 0 end) FIVE_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 6 then 1 else 0 end) SIX_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 7 then 1 else 0 end) SEVEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 8 then 1 else 0 end) EIGHT_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 9 then 1 else 0 end) NINE_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 10 then 1 else 0 end) TEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 11 then 1 else 0 end) ELEVEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 12 then 1 else 0 end) TWELVE_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 13 then 1 else 0 end) THIRTEEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 14 then 1 else 0 end) FOURTEEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 15 then 1 else 0 end) FIFTEEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 16 then 1 else 0 end) SIXTEEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 17 then 1 else 0 end) SEVENTEEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 18 then 1 else 0 end) EIGHTTEEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 19 then 1 else 0 end) NINETEEN_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 20 then 1 else 0 end) TWENTY_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 21 then 1 else 0 end) TWENTY_ONE_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 22 then 1 else 0 end) TWENTY_TWO_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 23 then 1 else 0 end) TWENTY_THREE_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 24 then 1 else 0 end) TWENTY_FOUR_STUDENTS, ");
        sql.append(" sum(case when a.STU_COUNT = 25 then 1 else 0 end) TWENTY_FIVE_STUDENTS ");
        sql.append(" from ");
        sql.append(" ( ");
        sql.append(" select belong_org.id ORG_ID, belong_org.parentID, belong_org.`name` CAMPUS_NAME, ttct.CLASS_TWO_ID,  ");
        sql.append("	ttct.TEACHER_ID, u.name TEACHER_NAME, count(DISTINCT ttcsa.STUDENT_ID) STU_COUNT  ");
        sql.append("	from two_teacher_class_student_attendent ttcsa ");
        sql.append("	inner join two_teacher_class_course ttcc on ttcsa.TWO_CLASS_COURSE_ID = ttcc.COURSE_ID ");
        sql.append("	inner join two_teacher_class_two ttct on ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID ");
        sql.append("	inner join user u on ttct.TEACHER_ID = u.USER_ID ");
        sql.append("	inner join organization o on u.organizationID = o.id ");
        sql.append("    inner join organization belong_org on o.BELONG = belong_org.id ");
        sql.append("	where ttcsa.CHARGE_STATUS = 'CHARGED' ");
        if(StringUtils.isNotBlank(modelVo.getStartDate())){
            sql.append(" and ttcc.COURSE_DATE >=  :startDate ");
            params.put("startDate", modelVo.getStartDate());
        }
        if(StringUtils.isNotBlank(modelVo.getEndDate())){
            sql.append(" and ttcc.COURSE_DATE <= :endDate ");
            params.put("endDate", modelVo.getEndDate());
        }
        List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append(" and ( o.orgLevel like '").append(org.getOrgLevel()).append("%'");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(")");
        }
        sql.append(" group by ttct.CLASS_TWO_ID, ttcc.COURSE_ID) a ")
            .append(" inner join organization ob on ifnull(a.parentID, a.ORG_ID) = ob.id ")
            .append(" inner join organization og on ifnull(ob.parentID, ob.id) = og.id ")
            .append(" group by a.TEACHER_ID; ");
        return this.findMapBySql(sql.toString(), params);
    }

	@Override
	public DataPackage getPublicFinanceContractAnalyze(BasicOperationQueryVo basicOperationQueryVo, DataPackage dp, User currentLoginUser, Organization belongCampus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, '' as campusId, '' as userId,    ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, '' as userId,  ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, ");
		}  else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" 	CONCAT(o3.id, '_', o3.name) as groupId, CONCAT(o2.id, '_', o2.name) as brenchId, CONCAT(o.id, '_', o.name) as campusId, CONCAT(ct.sign_staff_id, '_', (select u.name from user u where u.USER_ID = ct.sign_staff_id)) as userId, CONCAT(ct.STUDENT_ID, '_', (select s.name from student s where s.ID = ct.STUDENT_ID)) as stuId, ");
		}
		sql.append(" sum(case when fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' then fun.transaction_amount when fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'WASH' then -fun.transaction_amount else 0 end)  countPaidCashAmount, ");
		sql.append(" sum(case when fun.CHANNEL ='ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' then fun.transaction_amount when fun.CHANNEL ='ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'WASH' then -fun.transaction_amount else 0 end)  countPaidElectAmount, ");
		sql.append(" SUM(CASE WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'NEW_CONTRACT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' THEN fun.transaction_amount WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'NEW_CONTRACT' AND fun.FUNDS_PAY_TYPE = 'WASH' THEN -fun.transaction_amount ELSE 0 END) AS countPaidCashAmount_new, ");
		sql.append(" SUM(CASE WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'RE_CONTRACT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' THEN fun.transaction_amount WHEN fun.CHANNEL <>'ELECTRONIC_ACCOUNT' and ct.CONTRACT_TYPE = 'RE_CONTRACT' AND fun.FUNDS_PAY_TYPE = 'WASH' THEN -fun.transaction_amount ELSE 0 END) AS countPaidCashAmount_re, ");
		sql.append(" sum(CASE WHEN fun.FUNDS_PAY_TYPE = 'RECEIPT' THEN fun.transaction_amount WHEN fun.FUNDS_PAY_TYPE = 'WASH' THEN -fun.transaction_amount ELSE 0 END) countPaidTotalAmount ");
		sql.append(",ifnull(a.platAmount,0) platAmount");
		sql.append(" from funds_change_history fun  ");
		sql.append(" inner join contract ct on fun.contract_id = ct.id  ");
		sql.append(" INNER JOIN organization o  ON fun.fund_campus_id = o.id    ");
		sql.append(" INNER JOIN organization o2  ON o.parentID = o2.id  ");
		sql.append(" INNER JOIN organization o3  ON o2.parentID = o3.id ");

		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	left join (SELECT org.parentID id,sum(log.change_amount) platAmount \n" +
					"FROM student_acc_info_log log\n" +
					"left join student s on s.id = log.student_id \n" +
					"left join organization org on  s.BL_CAMPUS_ID = org.id\n" +
					"WHERE log.type='RECHARGE' AND log.create_time>='"+basicOperationQueryVo.getStartDate()+" 00:00:00' AND log.create_time <='"+basicOperationQueryVo.getEndDate()+" 23:59:59' \n" +
					"GROUP BY org.parentID) a  on a.id = o2.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	left join (SELECT org.id id,sum(log.change_amount) platAmount \n" +
					"FROM student_acc_info_log log\n" +
					"left join student s on s.id = log.student_id \n" +
					"left join organization org on  s.BL_CAMPUS_ID = org.id\n" +
					"WHERE log.type='RECHARGE' AND log.create_time>='"+basicOperationQueryVo.getStartDate()+" 00:00:00' AND log.create_time <='"+basicOperationQueryVo.getEndDate()+" 23:59:59' \n" +
					"GROUP BY org.id) a  on a.id = o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	left join (SELECT log.sign_staff_id id,sum(log.change_amount) platAmount \n" +
					"FROM student_acc_info_log log\n" +
					"left join student s on s.id = log.student_id \n" +
					"left join organization org on  s.BL_CAMPUS_ID = org.id\n" +
					"WHERE log.type='RECHARGE' AND log.create_time>='"+basicOperationQueryVo.getStartDate()+" 00:00:00' AND log.create_time <='"+basicOperationQueryVo.getEndDate()+" 23:59:59' \n" +
					"GROUP BY log.sign_staff_id) a  on a.id = ct.sign_staff_id ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" 	left join (SELECT s.id id,sum(log.change_amount) platAmount \n" +
					"FROM student_acc_info_log log\n" +
					"left join student s on s.id = log.student_id \n" +
					"left join organization org on  s.BL_CAMPUS_ID = org.id\n" +
					"WHERE log.type='RECHARGE' AND log.create_time>='"+basicOperationQueryVo.getStartDate()+" 00:00:00' AND log.create_time <='"+basicOperationQueryVo.getEndDate()+" 23:59:59' \n" +
					"GROUP BY s.id) a  on a.id = ct.student_id ");
		}


		sql.append(" where 1=1 and  ct.pub_pay_contract=1 and  fun.pub_pay_contract=1  and ct.CONTRACT_TYPE <> 'LIVE_CONTRACT' ");
		sql.append("  and fun.CHANNEL in ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER','ELECTRONIC_ACCOUNT') ");
		if (StringUtils.isNotBlank(basicOperationQueryVo.getStartDate())) {
			sql.append(" AND fun.TRANSACTION_TIME >= :startDate ");
			params.put("startDate", basicOperationQueryVo.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(basicOperationQueryVo.getEndDate())) {
			sql.append(" AND fun.TRANSACTION_TIME <= :endDate ");
			params.put("endDate", basicOperationQueryVo.getEndDate() + " 23:59:59");
		}
		if(basicOperationQueryVo.getContractType()!=null){
			sql.append(" AND ct.CONTRACT_TYPE = :contractType ");
			params.put("contractType", basicOperationQueryVo.getContractType());
		}
		if(basicOperationQueryVo.getAuditStatus()!=null){
			sql.append(" and fun.AUDIT_STATUS = :auditStatus ");
			params.put("auditStatus", basicOperationQueryVo.getAuditStatus());
		}
		if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o2.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and o.id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" and ct.sign_staff_id = :blCampusId ");
			params.put("blCampusId", basicOperationQueryVo.getBlCampusId());
		}
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("现金流统计","sql","o.id"));
		if (BasicOperationQueryLevelType.GROUNP.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id ");
		} else if (BasicOperationQueryLevelType.BRENCH.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id ");
		} else if (BasicOperationQueryLevelType.CAMPUS.equals(basicOperationQueryVo.getBasicOperationQueryLevelType())) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id ");
		} else if (BasicOperationQueryLevelType.USER.equals(basicOperationQueryVo.getBasicOperationQueryLevelType()) ) {
			sql.append(" group by o3.id, o2.id, o.id, ct.sign_staff_id, ct.STUDENT_ID ");
		}
		//添加排序 按实收金额进行排序
		sql.append(" ORDER BY sum(case when fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'RECEIPT' then fun.transaction_amount when fun.CHANNEL <>'ELECTRONIC_ACCOUNT' AND fun.FUNDS_PAY_TYPE = 'WASH' then -fun.transaction_amount else 0 end) DESC ");
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);

		dp.setDatas(list);
		dp.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	@Override
	public List findCampusAchievementMainByMonth(BasicOperationQueryVo searchVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT ");
		if(BasicOperationQueryLevelType.GROUNP.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append("     `group_id` groupId,");
			sql.append("     `group_name` groupName,");
			sql.append("     `branch_id` branchId,");
			sql.append("     `branch_name` branchName,");
			sql.append("     '' campusId,");
			sql.append("     '' campusName,");
		}else if(BasicOperationQueryLevelType.BRENCH.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append(" `id`,");
			sql.append("     `group_id` groupId,");
			sql.append("     `group_name` groupName,");
			sql.append("     `branch_id` branchId,");
			sql.append("     `branch_name` branchName,");
			sql.append("     `campus_id` campusId,");
			sql.append("     `campus_name` campusName,");
		}
		sql.append("     sum(`campus_amount_new`) campusAmountNew,");
		sql.append("     sum(`campus_amount_re`) campusAmountRe,");
		sql.append("     sum(`all_money`) allMoney,");
		sql.append("     sum(`refund_amount`) refundAmount,");
		sql.append("     sum(`total_money`) totalMoney,");
		sql.append("     sum(`live_income_new`) liveIncomeNew,");
		sql.append("     sum(`live_income_renew`) liveIncomeRenew,");
		sql.append("     sum(`live_refund_new`) liveRefundNew,");
		sql.append("     sum(`live_refund_renew`) liveRefundRenew,");
		sql.append("     sum(`live_total_money`) liveTotalMoney,");
		sql.append("     sum(`total_income_money`) totalIncomeMoney,");
		sql.append("     sum(`total_refund_money`) totalRefundMoney,");
		sql.append("     sum(`total_bonus`) totalBonus,");
		sql.append("     `remark`,");
		sql.append("     `receipt_month` receiptMonth,");
		sql.append("     `receipt_date` receiptDate,");
		sql.append("     `receipt_status` receiptStatus,");
		
		sql.append("     sum(`modify_money`) modifyMoney,");
		sql.append("     sum(`after_modify_money`) afterModifyMoney,");
		
		sql.append("     `modify_time` modifyTime,");
		sql.append("     `modify_user` modifyUser");
		sql.append(" FROM `ods_month_campus_achievement_main` where 1=1 ");
		
		
		if(StringUtils.isNotBlank(searchVo.getCountDate())){
			sql.append(" and receipt_Month= :countDate ");
			params.put("countDate", searchVo.getCountDate());
		}
		if (StringUtil.isNotBlank(searchVo.getOrganizationId())) {
			sql.append(" AND campus_id = :organizationId ");
			params.put("organizationId", searchVo.getOrganizationId());
		}
		if (StringUtil.isNotBlank(searchVo.getBlCampusId())) {
			if(BasicOperationQueryLevelType.BRENCH.getValue().equals(searchVo.getConTypeOrProType())){
				sql.append(" AND branch_id = :blCampusId ");
				params.put("blCampusId", searchVo.getBlCampusId());
			}else if(BasicOperationQueryLevelType.CAMPUS.getValue().equals(searchVo.getConTypeOrProType())) {
				sql.append(" AND campus_id = :blCampusId ");
				params.put("blCampusId", searchVo.getBlCampusId());
			}
		}
		
		if(searchVo.getEvidenceAuditStatus()!=null){
			sql.append(" and receipt_status = :evidenceAuditStatus ");
			params.put("evidenceAuditStatus", searchVo.getEvidenceAuditStatus());
		}
		
		if (StringUtil.isNotBlank(searchVo.getBrenchId())) {
			sql.append(" AND branch_id = :brenchId ");
			params.put("brenchId", searchVo.getBrenchId());
		}
		
		sql.append(this.getEvidenceQlConfig());
		if(BasicOperationQueryLevelType.GROUNP.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append(" group by branch_id");
		}else if(BasicOperationQueryLevelType.BRENCH.getValue().equals(searchVo.getConTypeOrProType())){
			sql.append(" group by campus_id");
		}
		return 	this.findMapBySql(sql.toString(), params);
	}

	/**
	 * 按pos机类型获取收款笔数
	 */
    @Override
    public List<Map<Object, Object>> getFundsCountGroupByPosType(
            OrganizationDateReqVo reqVo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(" select fch.pos_machine_type typeId, dd.`NAME` typeName, count(1) num ");
        sql.append(" from  funds_change_history fch, data_dict dd, organization o ");
        sql.append(" where fch.pos_machine_type = dd.id ");
        sql.append(" and o.id = fch.fund_campus_id ");
        if (StringUtil.isNotBlank(reqVo.getStartDate())) {
            sql.append(" and fch.TRANSACTION_TIME >= :startDate ");
            params.put("startDate", reqVo.getStartDate() + " 00:00:00");
        }
        if (StringUtil.isNotBlank(reqVo.getEndDate())) {
            sql.append(" and fch.TRANSACTION_TIME <= :endDate ");
            params.put("startDate", reqVo.getEndDate() + " 23:59:59");
        }
        if (StringUtil.isNotBlank(reqVo.getCampusId())) {
            sql.append(" and o.id = :campusId ");
        }
        if (StringUtil.isNotBlank(reqVo.getBranchId())) {
            sql.append(" and o.parentID = :branchId ");
        }
        sql.append(" and fch.fund_campus_id in " +roleQLConfigService.getAllOrgAppendSql());
        sql.append(" and fch.pos_machine_type is not null ");
        sql.append(" group by fch.pos_machine_type ");
        return this.findMapBySql(sql.toString(), params);
    }

    /**
     * 按pos机类型获取收款金额
     */
    @Override
    public List<Map<Object, Object>> getFundsAmountGroupByPosType(
            OrganizationDateReqVo reqVo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(" select fch.pos_machine_type typeId, dd.`NAME` typeName, sum(TRANSACTION_AMOUNT) amount ");
        sql.append(" from  funds_change_history fch, data_dict dd, organization o ");
        sql.append(" where fch.pos_machine_type = dd.id ");
        sql.append(" and o.id = fch.fund_campus_id ");
        if (StringUtil.isNotBlank(reqVo.getStartDate())) {
            sql.append(" and fch.TRANSACTION_TIME >= :startDate ");
            params.put("startDate", reqVo.getStartDate() + " 00:00:00");
        }
        if (StringUtil.isNotBlank(reqVo.getEndDate())) {
            sql.append(" and fch.TRANSACTION_TIME <= :endDate ");
            params.put("startDate", reqVo.getEndDate() + " 23:59:59");
        }
        if (StringUtil.isNotBlank(reqVo.getCampusId())) {
            sql.append(" and o.id = :campusId ");
        }
        if (StringUtil.isNotBlank(reqVo.getBranchId())) {
            sql.append(" and o.parentID = :branchId ");
        }
        sql.append(" and fch.fund_campus_id in " +roleQLConfigService.getAllOrgAppendSql());
        sql.append(" and fch.pos_machine_type is not null ");
        sql.append(" group by fch.pos_machine_type ");
        return this.findMapBySql(sql.toString(), params);
    }
    
}
