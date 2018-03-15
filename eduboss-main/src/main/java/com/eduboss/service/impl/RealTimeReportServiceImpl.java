package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.common.*;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.CourseDao;
import com.eduboss.dao.CustomerCallsLogDao;
import com.eduboss.dao.CustomerDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.TwoTeacherClassStudentAttendentDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.Course;
import com.eduboss.domain.Customer;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domainVo.CampusCallDayVo;
import com.eduboss.domainVo.CampusIncomeVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.InternetUseConditionVo;
import com.eduboss.domainVo.MarketAnalysisVo;
import com.eduboss.domainVo.MarketTypeAnalysisVo;
import com.eduboss.domainVo.OneCampusIncomeVo;
import com.eduboss.domainVo.StudentAddEveryDayVo;
import com.eduboss.domainVo.StudentDistributionVo;
import com.eduboss.domainVo.StudentSchoolDistributionVo;
import com.eduboss.dto.CourseTimeMonitorSearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.RealTimeReportService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;


@Service("RealTimeReportService")
public class RealTimeReportServiceImpl implements RealTimeReportService{

	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private StudentDao studentDao;

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleQLConfigService roleQLConfigService;
       
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private CourseDao courseDao;
    
    @Autowired
    private TwoTeacherClassStudentAttendentDao twoTeacherAttendentDao;
    
	/**
	 * 市场来源分析
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	@Override
	public DataPackage getMarkeyAnalysisList(DataPackage dataPackage,
			Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		Organization org = organizationDao.findById(StringUtil.toString(params.get("organizationId")));
		String orgLevel = org.getOrgLevel();
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("SELECT groupp.NAME as groupName,branch.NAME as branchName,campus.NAME as campusName, ");
		sbSql.append("IFNULL(datadict.NAME, '其他') as sourceName, ");
		sbSql.append("count(1) as numberOfContract, ");
		sbSql.append("round(count(1)/h.num*100,1) as contractPortionInPercent,  ");
		sbSql.append("sum(con.PAID_AMOUNT) as paidAmount,  ");
		sbSql.append("IFNULL(round(sum(con.PAID_AMOUNT)/h.amount*100,1) ,0) as paidAmountPortionInPercent ");
		sbSql.append("FROM  ");
		sbSql.append("contract as con, ");
		sbSql.append("organization as campus,  ");
		sbSql.append("organization as branch,  ");
		sbSql.append("organization as groupp,  ");
		//inner loop 用来取总计，为了在外循环中计算百分比。
		sbSql.append("customer as cus LEFT JOIN ");
		sbSql.append("(SELECT data_dict.id, data_dict.NAME FROM data_dict WHERE data_dict.CATEGORY = 'RES_TYPE') datadict  ");
		sbSql.append("on cus.CUS_TYPE = datadict.ID,  ");
		sbSql.append("(SELECT ");
		if (org.getOrgType().equals(OrganizationType.CAMPUS)){
			sbSql.append("conG.BL_CAMPUS_ID as innerID, ");
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append("branchInner.id as innerID, ");
		}else if(org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append("grouppInner.id as innerID, ");
		}
		sbSql.append("COUNT(1) num, ");
		sbSql.append("SUM(conG.PAID_AMOUNT) amount from ");
		if (org.getOrgType().equals(OrganizationType.CAMPUS)){
			sbSql.append("contract conG ");
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append("contract conG, organization as campusInner, organization as branchInner ");
		}else if(org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append("contract conG, organization as campusInner, organization as branchInner, organization as grouppInner ");
		}
		sbSql.append("WHERE conG.CONTRACT_TYPE IN ('NEW_CONTRACT') ");
		sbSql.append("and conG.CREATE_TIME >= :startDate and conG.CREATE_TIME <= :endDate ");
		sbParams.put("startDate", params.get("startDate"));
		sbParams.put("endDate", params.get("endDate"));
		if (org.getOrgType().equals(OrganizationType.CAMPUS)){
			sbSql.append("GROUP BY conG.BL_CAMPUS_ID) h  ");
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append("and conG.BL_CAMPUS_ID = campusInner.ID and branchInner.id = campusInner.parentID GROUP BY branchInner.id) h  ");
		}else if(org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append("and conG.BL_CAMPUS_ID = campusInner.ID and branchInner.id = campusInner.parentID and grouppInner.id = branchInner.parentID GROUP BY grouppInner.id) h  ");
		}
		sbSql.append("WHERE con.CONTRACT_TYPE IN ('NEW_CONTRACT') ");
		sbSql.append("and con.BL_CAMPUS_ID = campus.id ");
		sbSql.append("and campus.parentID = branch.id  ");
		sbSql.append("and branch.parentID = groupp.id  ");
		sbSql.append("and con.CUSTOMER_ID = cus.ID  ");
		sbSql.append("and con.CREATE_TIME >= :startDate2 and con.CREATE_TIME <= :endDate2 ");
		sbParams.put("startDate2", params.get("startDate"));
		sbParams.put("endDate2", params.get("endDate"));
		
		if (org.getOrgType().equals(OrganizationType.CAMPUS)){
			sbSql.append("and campus.id = h.innerID ");
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append("and branch.id = h.innerID ");
		}else if(org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append("and groupp.id = h.innerID ");
		}
		sbSql.append("and con.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(orgLevel).append("%') ");
		
		if (org.getOrgType().equals(OrganizationType.CAMPUS)){
			sbSql.append("GROUP BY campus.id, datadict.id ");
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append("GROUP BY branch.id, datadict.id ");
		}else if(org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append("GROUP BY groupp.id, datadict.id ");
		}
		
		List<Map<Object, Object>> datasList = organizationDao.findMapOfPageBySql(sbSql.toString(), dataPackage, sbParams);
		List<MarketAnalysisVo> marketAnalysisVoList = new ArrayList<MarketAnalysisVo>();
			
		for(Map data : datasList){
			MarketAnalysisVo marketAnalysisVo = new MarketAnalysisVo();
			if (org.getOrgType().equals(OrganizationType.CAMPUS))
				marketAnalysisVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName") + " -- " + data.get("campusName"));
			if (org.getOrgType().equals(OrganizationType.BRENCH))
				marketAnalysisVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName"));
			if (org.getOrgType().equals(OrganizationType.GROUNP))
				marketAnalysisVo.setOrganizationName(StringUtil.toString(data.get("groupName")));
			marketAnalysisVo.setResourceTypeName(StringUtil.toString(data.get("sourceName")));
			marketAnalysisVo.setNumberOfContract(StringUtil.toString(data.get("numberOfContract")));
			marketAnalysisVo.setContractPercent(StringUtil.toString(data.get("contractPortionInPercent")) + "%");
			marketAnalysisVo.setContractAmount(StringUtil.toString(data.get("paidAmount")));
			marketAnalysisVo.setContractAmountPercent(StringUtil.toString(data.get("paidAmountPortionInPercent")) + "%");
			marketAnalysisVoList.add(marketAnalysisVo);
		}
		dataPackage.setDatas(marketAnalysisVoList);
		return setDataPackageCount(sbSql, dataPackage, sbParams);
	}

	/**
	 * 市场类型分析
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	@Override
	public DataPackage getMarkeyTypeAnalysisList(DataPackage dataPackage,
			Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		Organization org = null;
		String orgLevel = null;
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			org = organizationDao.findById(params.get("organizationId").toString());
			orgLevel = org.getOrgLevel();
		}
		sbSql.append("SELECT groupp.NAME as groupName, branch.NAME as branchName,campus.NAME as campusName,IFNULL(datadict.NAME, '其他') as sourceName, ");
		sbSql.append("count(1) as contractNum,  ");
		sbSql.append("round(count(1)/h.num*100,1) as contractPer, ");
		sbSql.append("sum(con.PAID_AMOUNT) as paidAmount, ");
		sbSql.append("IFNULL(round(sum(con.PAID_AMOUNT)/h.amount*100,1) ,0) as paidAmountPer  ");
		sbSql.append("FROM ");
		sbSql.append("contract as con,  ");
		sbSql.append("organization as campus, ");
		sbSql.append("organization as branch, ");
		sbSql.append("organization as groupp, ");
		sbSql.append("customer e left join data_dict datadict on e.CUS_ORG = datadict.ID,  ");
		sbSql.append("(select conG.BL_CAMPUS_ID,  ");
		sbSql.append("count(*) num,  ");
		sbSql.append("sum(conG.PAID_AMOUNT) amount from contract conG  ");
		sbSql.append("where conG.CONTRACT_TYPE in ('NEW_CONTRACT')  ");
		sbSql.append("and conG.CREATE_TIME >= :startDate and conG.CREATE_TIME <= :endDate GROUP BY conG.BL_CAMPUS_ID) h ");
		sbSql.append("WHERE con.CONTRACT_TYPE in ('NEW_CONTRACT')  ");
		sbSql.append("and con.BL_CAMPUS_ID = campus.id ");
		sbSql.append("and campus.parentID = branch.id  ");
		sbSql.append("and branch.parentID = groupp.id  ");
		sbSql.append("and con.CUSTOMER_ID = e.ID ");
		sbSql.append("and con.CREATE_TIME >= :startDate2 and con.CREATE_TIME <= :endDate2 and con.BL_CAMPUS_ID = h.BL_CAMPUS_ID  ");
		sbParams.put("startDate", params.get("startDate"));
		sbParams.put("endDate", params.get("endDate"));
		sbParams.put("startDate2", params.get("startDate"));
		sbParams.put("endDate2", params.get("endDate"));
		if(org != null)
			sbSql.append("and con.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(orgLevel).append("%') ");
		sbSql.append("GROUP BY groupp.NAME,branch.NAME,campus.NAME,datadict.NAME ");

		List<Map<Object, Object>> datasList = organizationDao.findMapOfPageBySql(sbSql.toString(), dataPackage, sbParams);
		List<MarketTypeAnalysisVo> marketTypeAnalysisVoList = new ArrayList<MarketTypeAnalysisVo>();
		
		for(Map<Object, Object> data : datasList){
			MarketTypeAnalysisVo marketTypeAnalysisVo = new MarketTypeAnalysisVo();
			marketTypeAnalysisVo.setGroupName(StringUtil.toString(data.get("groupName")));
			marketTypeAnalysisVo.setBranchName(StringUtil.toString(data.get("branchName")));
			marketTypeAnalysisVo.setCampusName(StringUtil.toString(data.get("campusName")));
			marketTypeAnalysisVo.setResourceTypeName(StringUtil.toString(data.get("sourceName")));
			marketTypeAnalysisVo.setContractNumber(StringUtil.toString(data.get("contractNum")));
			marketTypeAnalysisVo.setContractPercent(StringUtil.toString(data.get("contractPer")) + "%");
			marketTypeAnalysisVo.setContractAmount(StringUtil.toString(data.get("paidAmount")));
			marketTypeAnalysisVo.setContractAPercent(StringUtil.toString(data.get("paidAmountPer")) + "%");
			marketTypeAnalysisVoList.add(marketTypeAnalysisVo);
		}
		dataPackage.setDatas(marketTypeAnalysisVoList);
		return setDataPackageCount(sbSql, dataPackage, sbParams);
	}

	/**
	 * 来电量总数
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	@Override
	public DataPackage getCallList(DataPackage dataPackage,
			Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		Organization org = null;
		String orgLevel = null;
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			org = organizationDao.findById(params.get("organizationId").toString());
			orgLevel = org.getOrgLevel();
		}
		sbSql.append("select count(*) calling,a.cn campusName,a.bn branchName,a.gn groupName");
		if (org == null){
			sbSql.append(",a.gi as orgId ");
		}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
			sbSql.append(",a.ci as orgId ");
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append(",a.ci as orgId ");
		}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append(",a.gi as orgId ");
		}
		sbSql.append("from ");
		sbSql.append("(  ");
		sbSql.append("select calllog.user_id,campus.name cn,branch.name bn,groupp.name gn,campus.id ci,branch.id bi,groupp.id gi ");
		sbSql.append("from CUSTOMER_CALLS_LOG calllog,user user, ");
		sbSql.append("organization campus,organization branch,organization groupp  ");
		sbSql.append("where user.user_id = calllog.user_id and user.organizationId = campus.id ");
		sbSql.append("and campus.parentID = branch.id  ");
		sbSql.append("and branch.parentID = groupp.id  ");
		sbSql.append("and calllog.PHONE_EVENT = 'ON_HOOK' and calllog.PHONE_TYPE = 'INCOMING_TEL'  ");
		if (org != null)
			sbSql.append("and user.organizationId in ( select id from organization where orgLevel like '").append(orgLevel).append("%') ");
		sbSql.append("and calllog.CREATE_TIME >= :startDate and calllog.CREATE_TIME <= :endDate ");
		sbSql.append(") a  ");
		if (org == null){
			sbSql.append("GROUP BY a.gn,a.gi,a.bn,a.bi ");
		}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
			sbSql.append("GROUP BY a.gn,a.gi,a.bn,a.bi,a.ci,a.cn ");
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append("GROUP BY a.gn,a.gi,a.bn,a.bi,a.ci,a.cn ");
		}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append("GROUP BY a.gn,a.gi,a.bn,a.bi ");
		}
		sbSql.append("order by calling desc  ");
		sbParams.put("startDate", params.get("startDate"));
		sbParams.put("endDate", params.get("endDate"));
		
		List<Map<Object, Object>> datasList = organizationDao.findMapOfPageBySql(sbSql.toString(), dataPackage, sbParams);
		List<CampusCallDayVo> campusCallDayVoList = new ArrayList<CampusCallDayVo>();
		
		for(Map<Object, Object> data : datasList){
			CampusCallDayVo cmampusCallDayVo = new CampusCallDayVo();
			if (org == null){
				cmampusCallDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName"));
				cmampusCallDayVo.setCampusName("");
			}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
				cmampusCallDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName") + " -- " + data.get("campusName"));
				cmampusCallDayVo.setCampusName(data.get("campusName") != null ? data.get("campusName").toString() : "");
			}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
				cmampusCallDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName") + " -- " + data.get("campusName"));
				cmampusCallDayVo.setCampusName(data.get("campusName") != null ? data.get("campusName").toString() : "");
			}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
				cmampusCallDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName"));
				cmampusCallDayVo.setCampusName("");
			}
			cmampusCallDayVo.setOrganizationId(data.get("orgId").toString());
			cmampusCallDayVo.setGroupName(data.get("groupName") != null ? data.get("groupName").toString() : "");
			cmampusCallDayVo.setBranchName(data.get("branchName") != null ? data.get("branchName").toString() : "");
			
			cmampusCallDayVo.setCallNum(data.get("calling").toString());
			campusCallDayVoList.add(cmampusCallDayVo);
		}
		dataPackage.setDatas(campusCallDayVoList);
		return setDataPackageCount(sbSql, dataPackage, sbParams);
	}
	
	
	/**
	 * 学生年级分布
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	@Override
	public DataPackage getStudentDistributionList(DataPackage dataPackage,
			Map<String, Object> params) {
		StringBuilder sbSql = new StringBuilder();
		Organization org = null;
		String orgLevel = null;
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			org = organizationDao.findById(params.get("organizationId").toString());
			orgLevel = org.getOrgLevel();
		}
		sbSql.append(" SELECT IFNULL(groupp.NAME,'') groupName,IFNULL(branch.NAME,'') branchName,IFNULL(campus.NAME,'') campusName, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900002' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary1,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900003' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary2,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900004' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary3,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900005' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary4,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900006' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary5,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900007' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary6,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900008' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) junior1, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900009' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) junior2, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900010' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) junior3, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900011' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) senior1, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900012' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) senior2, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900013' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) senior3");
		sbSql.append(" FROM");
		sbSql.append(" student a,");
		sbSql.append(" organization campus,");
		sbSql.append(" organization branch,");
		sbSql.append(" organization groupp ");
		sbSql.append(" WHERE a.STATUS in ('NEW','CLASSING') and (a.STU_STATUS is null or a.STU_STATUS = 1)");
		sbSql.append(" and a.BL_CAMPUS_ID = campus.id");
		sbSql.append(" and campus.parentID = branch.id ");
		sbSql.append(" and branch.parentID = groupp.id ");
//		sbSql.append(" and a.CREATE_TIME >= '").append(params.get("startDate").toString())
//		.append("' and a.CREATE_TIME <= '").append(params.get("endDate").toString())
//		.append("' ");
		if (org != null)
			sbSql.append(" and a.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(orgLevel).append("%')");
		if (org == null){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME ");
		}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME,campus.NAME");
		}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME");
		}
		
		List<Map<Object, Object>> datasList = organizationDao.findMapOfPageBySql(sbSql.toString(), dataPackage, new HashMap<String, Object>());
		List<StudentDistributionVo> studentDistributionVoList = new ArrayList<StudentDistributionVo>();
		String groupName=null;
		for(Map<Object, Object> data : datasList){
		 groupName=(String) data.get("groupName");
		 if(StringUtil.isNotBlank(groupName)){		//如果集团名称为空，就不显示
			StudentDistributionVo studentDistributionVo = new StudentDistributionVo();
			if (org == null){
				studentDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentDistributionVo.setCampusName("");
			}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
				studentDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentDistributionVo.setCampusName(StringUtil.toString(data.get("campusName")));
			}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
				studentDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentDistributionVo.setCampusName(StringUtil.toString(data.get("campusName")));
			}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
				studentDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentDistributionVo.setCampusName("");
			}			
			
			studentDistributionVo.setPrimary1(StringUtil.toString(data.get("primary1")));
			studentDistributionVo.setPrimary2(StringUtil.toString(data.get("primary2")));
			studentDistributionVo.setPrimary3(StringUtil.toString(data.get("primary3")));
			studentDistributionVo.setPrimary4(StringUtil.toString(data.get("primary4")));
			studentDistributionVo.setPrimary5(StringUtil.toString(data.get("primary5")));
			studentDistributionVo.setPrimary6(StringUtil.toString(data.get("primary6")));
			studentDistributionVo.setJunior1(StringUtil.toString(data.get("junior1")));
			studentDistributionVo.setJunior2(StringUtil.toString(data.get("junior2")));
			studentDistributionVo.setJunior3(StringUtil.toString(data.get("junior3")));
			studentDistributionVo.setSenior1(StringUtil.toString(data.get("senior1")));
			studentDistributionVo.setSenior2(StringUtil.toString(data.get("senior2")));
			studentDistributionVo.setSenior3(StringUtil.toString(data.get("senior3")));
			
			//计算总数，内存计算比查数据库快
			int total=0;
			if(StringUtil.isNotBlank(studentDistributionVo.getPrimary1())){
				total=total+Integer.valueOf(studentDistributionVo.getPrimary1());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getPrimary2())){
				total=total+Integer.valueOf(studentDistributionVo.getPrimary2());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getPrimary3())){
				total=total+Integer.valueOf(studentDistributionVo.getPrimary3());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getPrimary4())){
				total=total+Integer.valueOf(studentDistributionVo.getPrimary4());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getPrimary5())){
				total=total+Integer.valueOf(studentDistributionVo.getPrimary5());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getPrimary6())){
				total=total+Integer.valueOf(studentDistributionVo.getPrimary6());
			}			
			
			if(StringUtil.isNotBlank(studentDistributionVo.getJunior1())){
				total=total+Integer.valueOf(studentDistributionVo.getJunior1());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getJunior2())){
				total=total+Integer.valueOf(studentDistributionVo.getJunior2());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getJunior3())){
				total=total+Integer.valueOf(studentDistributionVo.getJunior3());
			}
			
			if(StringUtil.isNotBlank(studentDistributionVo.getSenior1())){
				total=total+Integer.valueOf(studentDistributionVo.getSenior1());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getSenior2())){
				total=total+Integer.valueOf(studentDistributionVo.getSenior2());
			}
			if(StringUtil.isNotBlank(studentDistributionVo.getSenior3())){
				total=total+Integer.valueOf(studentDistributionVo.getSenior3());
			}
			studentDistributionVo.setTotal(total+"");
			
			
			studentDistributionVoList.add(studentDistributionVo);
			 
		  }
		}
		dataPackage.setDatas(studentDistributionVoList);
		return setDataPackageCount(sbSql, dataPackage, new HashMap<String, Object>());
	}

	/**
	 * 获取每天的来电量
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<Object, Object>> getCallEveryDayList(
			Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		Organization org = null;
		String orgLevel = null;
		
//		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
//			org = organizationDao.findById(params.get("organizationId").toString());
//			orgLevel = org.getOrgLevel();
//		}
		if(StringUtil.isNotBlank(params.get("organizationName").toString())){
			Map<String, Object> orgParams = new HashMap<String, Object>();
			String orgIdSql = " select * from organization where name= :orgParams "; 
			orgParams.put("organizationName", params.get("organizationName"));
			List<Map<Object, Object>> resultList = organizationDao.findMapBySql(orgIdSql, orgParams);
			for(Map<Object, Object> result : resultList){
				orgLevel = result.get("orgLevel") != null ? result.get("orgLevel").toString() : null;
			}
		}
		
		sbSql.append("select count(1) calling,substring(a.dTime,1,10) dTime from  ");
		sbSql.append("(  ");
		sbSql.append("select calllog.user_id,campus.name cn,branch.name bn,groupp.name gn,calllog.CREATE_TIME dTime from CUSTOMER_CALLS_LOG calllog,user user, ");
		sbSql.append("organization campus,organization branch,organization groupp  ");
		sbSql.append("where user.user_id = calllog.user_id and user.organizationId = campus.id ");
		sbSql.append("and campus.parentID = branch.id  ");
		sbSql.append("and branch.parentID = groupp.id  ");
		sbSql.append("and calllog.PHONE_EVENT = 'ON_HOOK' and calllog.PHONE_TYPE = 'INCOMING_TEL'  ");
		sbSql.append("and calllog.CREATE_TIME >= :startDate and calllog.CREATE_TIME <= :endDate ");
		if (orgLevel != null)
			sbSql.append("and user.organizationId in ( select id from organization where orgLevel like '").append(orgLevel).append("%') ");
		sbSql.append(") a  ");
		if (orgLevel == null){
			sbSql.append("GROUP BY a.gn,a.bn,substring(a.dTime,1,10) ");
		}else if (orgLevel.equals(OrganizationType.CAMPUS)){
			sbSql.append("GROUP BY a.gn,a.bn,a.cn,substring(a.dTime,1,10) ");
		}else if (orgLevel.equals(OrganizationType.BRENCH)){
			sbSql.append("GROUP BY a.gn,a.bn,a.cn,substring(a.dTime,1,10) ");
		}else if (orgLevel.equals(OrganizationType.GROUNP)){
			sbSql.append("GROUP BY a.gn,a.bn,substring(a.dTime,1,10) ");
		}
		sbSql.append("order by a.dTime ");
		sbParams.put("startDate", params.get("startDate"));
		sbParams.put("endDate", params.get("endDate"));
		List<Map<Object, Object>> datasList = organizationDao.findMapBySql(sbSql.toString(), sbParams);
		return datasList;
	}

	/**
	 * 每天新增学生
	 * @param dataPackage
	 * @param params
	 * @return
	 */
	@Override
	public DataPackage getStudentAddEveryDayList(DataPackage dataPackage,
			Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		Organization org = null;
		String orgLevel = null;
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			org = organizationDao.findById(params.get("organizationId").toString());
			orgLevel = org.getOrgLevel();
		}
		sbSql.append(" SELECT IFNULL(groupp.NAME,'') groupName,IFNULL(branch.NAME,'') branchName,IFNULL(campus.NAME,'') campusName, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900002' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary1,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900003' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary2,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900004' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary3,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900005' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary4,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900006' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary5,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900007' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) primary6,");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900008' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) junior1, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900009' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) junior2, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900010' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) junior3, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900011' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) senior1, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900012' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) senior2, ");
		sbSql.append(" count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900013' THEN 1");
		sbSql.append(" ELSE null ");
		sbSql.append(" END) senior3");
		sbSql.append(" FROM");
		sbSql.append(" student a,");
		sbSql.append(" organization campus,");
		sbSql.append(" organization branch,");
		sbSql.append(" organization groupp ");
		sbSql.append(" WHERE (a.STU_STATUS is null or a.STU_STATUS = 1)");
		sbSql.append(" and a.BL_CAMPUS_ID = campus.id");
		sbSql.append(" and campus.parentID = branch.id ");
		sbSql.append(" and branch.parentID = groupp.id ");
		sbSql.append(" and a.CREATE_TIME >= :startDate and a.CREATE_TIME <= :endDate ");
		sbParams.put("startDate", params.get("startDate") + " 00:00:00");
		sbParams.put("endDate", params.get("endDate") + " 23:59:59");
		if (org != null)
			sbSql.append(" and a.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(orgLevel).append("%')");
		if (org == null){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME ");
		}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME,campus.NAME");
		}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME");
		}
		
		List<Map<Object, Object>> datasList = organizationDao.findMapOfPageBySql(sbSql.toString(), dataPackage, sbParams);
		List<StudentAddEveryDayVo> studentAddEveryDayVoList = new ArrayList<StudentAddEveryDayVo>();
		
		for(Map<Object, Object> data : datasList){
			StudentAddEveryDayVo studentAddEveryDayVo = new StudentAddEveryDayVo();
			if (org == null){
				studentAddEveryDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName"));
				studentAddEveryDayVo.setCampusName("");
			}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
				studentAddEveryDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName") + " -- " + data.get("campusName"));
				studentAddEveryDayVo.setCampusName(data.get("campusName") != null ? data.get("campusName").toString() : "");
			}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
				studentAddEveryDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName") + " -- " + data.get("campusName"));
				studentAddEveryDayVo.setCampusName(data.get("campusName") != null ? data.get("campusName").toString() : "");
			}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
				studentAddEveryDayVo.setOrganizationName(data.get("groupName") + " -- " + data.get("branchName"));
				studentAddEveryDayVo.setCampusName("");
			}		
			studentAddEveryDayVo.setGroupName(data.get("groupName") != null ? data.get("groupName").toString() : "");
			studentAddEveryDayVo.setBranchName(data.get("branchName") != null ? data.get("branchName").toString() : "");
			int studentNum =Integer.parseInt(data.get("primary1").toString()) +
					Integer.parseInt(StringUtil.toString(data.get("primary2"))) +
					Integer.parseInt(StringUtil.toString(data.get("primary3"))) +
					Integer.parseInt(StringUtil.toString(data.get("primary4"))) +
					Integer.parseInt(StringUtil.toString(data.get("primary5"))) +
					Integer.parseInt(StringUtil.toString(data.get("primary6"))) +
					Integer.parseInt(StringUtil.toString(data.get("junior1"))) +
					Integer.parseInt(StringUtil.toString(data.get("junior2"))) +
					Integer.parseInt(StringUtil.toString(data.get("junior3"))) +
					Integer.parseInt(StringUtil.toString(data.get("senior1"))) +
					Integer.parseInt(StringUtil.toString(data.get("senior2"))) +
					Integer.parseInt(StringUtil.toString(data.get("senior3")));
			studentAddEveryDayVo.setStudentNum(Integer.toString(studentNum));
			studentAddEveryDayVoList.add(studentAddEveryDayVo);
		}
		dataPackage.setDatas(studentAddEveryDayVoList);
		return setDataPackageCount(sbSql, dataPackage, sbParams);
	}

	/**
	 * 单独机构，每天学生新增
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<Object, Object>> getStudentAddEveryDayDatas(
			Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		String orgLevel = null;
		String orgType = null;
		
		if(StringUtil.isNotBlank(params.get("organizationName").toString())){
			Map<String, Object> orgParams = new HashMap<String, Object>();
			String orgIdSql = " select * from organization where name = :organizationName "; 
			orgParams.put("organizationName", params.get("organizationName"));
			List<Map<Object, Object>> resultList = organizationDao.findMapBySql(orgIdSql, orgParams);
			for(Map<Object, Object> result : resultList){
				orgLevel = result.get("orgLevel") != null ? result.get("orgLevel").toString() : null;
				orgType = result.get("orgType") != null ? result.get("orgType").toString() : null;
			}
		}else{
			return new ArrayList<Map<Object,Object>>();
		}
		
		sbSql.append("SELECT  ");
		sbSql.append("groupp.NAME groupName,  ");
		sbSql.append("branch.NAME branchName, ");
		sbSql.append("campus.NAME campusName, ");
		sbSql.append("substring(a.CREATE_TIME,1,10) createTime, ");
		sbSql.append("(count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900002' THEN 1 ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900003' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900004' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900005' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900006' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900007' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900008' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900009' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900010' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900011' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900012' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END)+ ");
		sbSql.append("count(CASE WHEN a.GRADE_ID = '402881e647ce90330147ce94ef900013' THEN 1  ");
		sbSql.append("ELSE null ");
		sbSql.append("END) ) sumy ");
		sbSql.append("FROM  ");
		sbSql.append("student a,  ");
		sbSql.append("organization campus,  ");
		sbSql.append("organization branch,  ");
		sbSql.append("organization groupp ");
		sbSql.append("WHERE a.STATUS in ('NEW','CLASSING') and (a.STU_STATUS is null or a.STU_STATUS = 1)  ");
		sbSql.append("and a.BL_CAMPUS_ID = campus.id  ");
		sbSql.append("and campus.parentID = branch.id ");
		sbSql.append("and branch.parentID = groupp.id ");
		sbSql.append("and a.CREATE_TIME >= :startDate and a.CREATE_TIME <= :endDate ");
		sbParams.put("startDate", params.get("startDate"));
		sbParams.put("endDate", params.get("endDate"));
		if (orgLevel != null)
			sbSql.append("and a.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(orgLevel).append("%')  ");
		if (orgType == null){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME,substring(a.CREATE_TIME,1,10) ");
		}else if (orgType.equals(OrganizationType.CAMPUS.getValue())){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME,campus.NAME,substring(a.CREATE_TIME,1,10)");
		}else if (orgType.equals(OrganizationType.BRENCH.getValue())){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME,campus.NAME,substring(a.CREATE_TIME,1,10)");
		}else if (orgType.equals(OrganizationType.GROUNP.getValue())){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME,substring(a.CREATE_TIME,1,10)");
		}
		sbSql.append("order by a.CREATE_TIME");
		
		List<Map<Object, Object>> datasList = organizationDao.findMapBySql(sbSql.toString(), sbParams);
		return datasList;
	}
	/**
	 * 学生学校分布统计  2015-04-13
	 * @param params
	 * @return
	 */
	@Override
	public DataPackage getStudentSchoolDistributionList(
			DataPackage dataPackage, Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		Organization org = null;
		String orgLevel = null;
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			org = organizationDao.findById(params.get("organizationId").toString());
			orgLevel = org.getOrgLevel();
		}
		sbSql.append(" SELECT IFNULL(groupp.NAME,'') groupName,IFNULL(branch.NAME,'') branchName,IFNULL(campus.NAME,'') campusName, ");
		sbSql.append(" count(*) as total");
		sbSql.append(" FROM");
		sbSql.append(" student a left join STUDENT_SCHOOL e on e.ID = a.SCHOOL,");
		sbSql.append(" organization campus,");
		sbSql.append(" organization branch,");
		sbSql.append(" organization groupp ");
		sbSql.append(" WHERE a.STATUS in ('NEW','CLASSING') and a.STU_STATUS is null");
		sbSql.append(" and a.BL_CAMPUS_ID = campus.id");
		sbSql.append(" and campus.parentID = branch.id ");
		sbSql.append(" and branch.parentID = groupp.id ");
		sbSql.append(" and a.CREATE_TIME >= :startDate and a.CREATE_TIME <= :endDate ");
		sbParams.put("startDate", params.get("startDate"));
		sbParams.put("endDate", params.get("endDate"));
		if (org != null)
			sbSql.append(" and a.BL_CAMPUS_ID in ( select id from organization where orgLevel like '").append(orgLevel).append("%')");
		if (org == null){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME ");
		}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
		}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME,campus.NAME");
		}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
			sbSql.append(" GROUP BY groupp.NAME,branch.NAME");
		}
		
		List<Map<Object, Object>> datasList = organizationDao.findMapOfPageBySql(sbSql.toString(), dataPackage, sbParams);
		List<StudentSchoolDistributionVo> studentSchoolDistributionVoList = new ArrayList<StudentSchoolDistributionVo>();
		
		for(Map<Object, Object> data : datasList){
			StudentSchoolDistributionVo studentSchoolDistributionVo = new StudentSchoolDistributionVo();
			if (org == null){
				studentSchoolDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentSchoolDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentSchoolDistributionVo.setCampusName("");
			}else if (org.getOrgType().equals(OrganizationType.CAMPUS)){
				studentSchoolDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentSchoolDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentSchoolDistributionVo.setCampusName(StringUtil.toString(data.get("campusName")));
			}else if (org.getOrgType().equals(OrganizationType.BRENCH)){
				studentSchoolDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentSchoolDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentSchoolDistributionVo.setCampusName(StringUtil.toString(data.get("campusName")));
			}else if (org.getOrgType().equals(OrganizationType.GROUNP)){
				studentSchoolDistributionVo.setGroupName(StringUtil.toString(data.get("groupName")));
				studentSchoolDistributionVo.setBranchName(StringUtil.toString(data.get("branchName")));
				studentSchoolDistributionVo.setCampusName("");
			}			
			studentSchoolDistributionVo.setTotal(StringUtil.toString(data.get("total")));
			studentSchoolDistributionVoList.add(studentSchoolDistributionVo);
		}
		dataPackage.setDatas(studentSchoolDistributionVoList);
		return setDataPackageCount(sbSql, dataPackage, sbParams);
	}
	
	@Override
	public CampusIncomeVo getCampusIncome(Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT campus.id,IFNULL(campus.NAME,'') campusName,IFNULL(sum(e.transaction_amount),0) as total,  ");
		sbSql.append("(select IFNULL(sum(quantity),0) from ACCOUNT_CHARGE_RECORDS where BL_CAMPUS_ID=campus.id and PRODUCT_TYPE='ONE_ON_ONE_COURSE'");
		if(StringUtil.isNotBlank(params.get("date").toString())) {
			sbSql.append("  and PAY_TIME like :date ");
			sbParams.put("date", params.get("date") + "%");
		}
		sbSql.append(") as quantity");
		sbSql.append(" FROM");
		sbSql.append(" contract a left join funds_change_history e on e.contract_id = a.id,");
		sbSql.append(" organization campus");
		sbSql.append(" WHERE 1=1");
		sbSql.append(" and a.BL_CAMPUS_ID = campus.id");
		if(StringUtil.isNotBlank(params.get("date").toString())) {
			sbSql.append(" and e.TRANSACTION_TIME like :date2 ");
			sbParams.put("date2", params.get("date") + "%");
		}
			
		if(StringUtil.isNotBlank(params.get("orgLel").toString())) {
			sbSql.append(" and a.BL_CAMPUS_ID in ( select id from organization where orgLevel like :orgLel ");
			sbParams.put("orgLel", params.get("orgLel") + "%");
		}
			
		sbSql.append(" GROUP BY campus.id,campus.NAME");
		
		List<Map<Object, Object>> datasList = organizationDao.findMapBySql(sbSql.toString(), sbParams);
		Set<CampusIncomeVo> ciList = new HashSet<CampusIncomeVo>();
		
		for(Map<Object, Object> data : datasList){
			CampusIncomeVo vo = new CampusIncomeVo();
				vo.setCampusId(StringUtil.toString(data.get("id")));
				vo.setCampusName(StringUtil.toString(data.get("campusName")));
				vo.setTotal((BigDecimal) data.get("total"));
				vo.setQuantity((BigDecimal) data.get("quantity"));
				ciList.add(vo);
		}
		CampusIncomeVo vo2=new CampusIncomeVo();
		vo2.setCiList(ciList);
		return vo2;
	}
	
	
	@Override
	public OneCampusIncomeVo getOneCampusTotalIncome(Map<String, Object> params) {
		Map<String, Object> sbParams = new HashMap<String, Object>();
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT campus.id,campus.NAME,IFNULL(sum(e.transaction_amount),0) as total,  ");
		sbSql.append("(select IFNULL(sum(quantity),0) from ACCOUNT_CHARGE_RECORDS where BL_CAMPUS_ID=campus.id and PRODUCT_TYPE='ONE_ON_ONE_COURSE'");
		if(StringUtil.isNotBlank(params.get("startDate").toString())) {
			sbSql.append("  and PAY_TIME >= :startDate ");
			sbParams.put("startDate", params.get("startDate"));
		}
		if(StringUtil.isNotBlank(params.get("endDate").toString())) {
			sbSql.append("  and PAY_TIME <= :endDate ");
			sbParams.put("endDate", params.get("endDate"));
		}
		sbSql.append(") as quantity");
		sbSql.append(" from organization campus ,contract a,funds_change_history e ");
		sbSql.append(" WHERE  a.BL_CAMPUS_ID = campus.id and e.contract_id = a.id");
		sbSql.append(" and a.BL_CAMPUS_ID = campus.id");
		
		if(StringUtil.isNotBlank(params.get("startDate").toString())) {
			sbSql.append(" and e.TRANSACTION_TIME >= :startDate2 ");
			sbParams.put("startDate2", params.get("startDate"));
		}
		if(StringUtil.isNotBlank(params.get("endDate").toString())) {
			sbSql.append(" and e.TRANSACTION_TIME <= :endDate2 ");
			sbParams.put("endDate2", params.get("endDate"));
		}
				
		if(StringUtil.isNotBlank(params.get("campusId").toString()))
			sbSql.append(" and a.BL_CAMPUS_ID = '").append(params.get("campusId")).append("')");
			
		sbSql.append(" GROUP BY campus.id,campus.NAME");
		List<Map<Object, Object>> datasList = organizationDao.findMapBySql(sbSql.toString(), sbParams);//得到当前校区的数据
		OneCampusIncomeVo vo = new OneCampusIncomeVo();
		for(Map<Object, Object> data : datasList){
				vo.setCampusId(data.get("id").toString());
				vo.setCampusName(data.get("campusName").toString());
				vo.setTotal( data.get("total").toString());
				vo.setQuantity( data.get("quantity").toString());
		}
		/******************新增学生************************************************/
		Map<String, Object> sbParams2 = new HashMap<String, Object>();
		StringBuilder sbSql2 = new StringBuilder();//新增学生
		sbSql2.append("select substring(create_time, 1,10) as tdate,sum(1) as total from student ");
		if(StringUtil.isNotBlank(params.get("campusId").toString())) {
			sbSql2.append(" where BL_CAMPUS_ID = :campusId )");
			sbParams2.put("campusId", params.get("campusId"));
		}
		if(StringUtil.isNotBlank(params.get("startDate").toString())) {
			sbSql2.append(" and create_time >= :startDate ");
			sbParams2.put("startDate", params.get("startDate"));
		}
		if(StringUtil.isNotBlank(params.get("endDate").toString())) {
			sbSql2.append(" and create_time <= :endDate ");
			sbParams2.put("endDate", params.get("endDate"));
		}
		sbSql2.append(" GROUP BY substring(create_time, 1,10)");
		List<Map<Object, Object>> datasList2 = organizationDao.findMapBySql(sbSql2.toString(), sbParams2);//得到新增学生记录
		vo.setNewStudent(calList(datasList2,params.get("startDate").toString(),params.get("endDate").toString()));
		/******************每日收款************************************************/
		StringBuilder sbSql3 = new StringBuilder();//每日收款
		sbSql3.append("select substring(e.TRANSACTION_TIME, 1,10) as tdate,sum(e.TRANSACTION_AMOUNT) as total  from funds_change_history  e,contract a");
		sbSql3.append(" where  e.contract_id = a.id");
		if(StringUtil.isNotBlank(params.get("campusId").toString()))
			sbSql3.append(" and BL_CAMPUS_ID = :campusId)");
		if(StringUtil.isNotBlank(params.get("startDate").toString()))
			sbSql3.append(" and e.TRANSACTION_TIME >= :startDate");
		if(StringUtil.isNotBlank(params.get("endDate").toString()))
			sbSql3.append(" and e.TRANSACTION_TIME <= :endDate");
		sbSql3.append(" GROUP BY substring(e.TRANSACTION_TIME, 1,10)");
		List<Map<Object, Object>> datasList3 = organizationDao.findMapBySql(sbSql3.toString(), sbParams2);//每日收款
		vo.setIncome(calList(datasList3,params.get("startDate").toString(),params.get("endDate").toString()));
		/******************每日课消************************************************/
		StringBuilder sbSql4 = new StringBuilder();//每日课消
		sbSql4.append("select substring(pay_time, 1,10) as tdate,sum(QUANTITY) as total from ACCOUNT_CHARGE_RECORDS");
		if(StringUtil.isNotBlank(params.get("campusId").toString()))
			sbSql4.append(" where BL_CAMPUS_ID = :campusId )");
		if(StringUtil.isNotBlank(params.get("startDate").toString()))
			sbSql4.append(" and pay_time >= :startDate ");
		if(StringUtil.isNotBlank(params.get("endDate").toString()))
			sbSql4.append(" and pay_time <= :endDate ");
		sbSql4.append(" GROUP BY substring(pay_time, 1,10)");
		List<Map<Object, Object>> datasList4 = organizationDao.findMapBySql(sbSql4.toString(), sbParams2);//每日课消
		vo.setQuantitys(calList(datasList4,params.get("startDate").toString(),params.get("endDate").toString()));
		
		return vo;
	}
	
	/**
	 * 如果某一天没有数据在这里添加进去
	 * @param oldList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Map<Object, Object>> calList(List<Map<Object, Object>> oldList,String startDate,String endDate){
	 List<Map<Object, Object>> newList = new ArrayList<Map<Object, Object>>();
	 List<String> dates = DateTools.getDates(startDate,endDate);//得到两个日期之间的所有日期
		for (String date : dates) {
			boolean flag=false;
			for (Iterator iterator = oldList.iterator(); iterator.hasNext();) {
				Map<Object, Object> map = (Map<Object, Object>) iterator.next();
				if(date.equals(map.get("tdate"))){//如果查出来数据里面有，则放到新的list,并且把旧的去掉
					newList.add(map);
					iterator.remove();
					flag=true;//标记为已经找到
					break;
				}
			}
			if(!flag){//如果没有，设置一个空进去用于页面显示
				Map<Object, Object> map=new HashMap<Object, Object>();
				map.put("tdate", date);
				map.put("total", 0);
				newList.add(map);
			}
		}
		return newList;
	}

    /**
     * 一对一课时汇总
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<Object, Object>> totalOneOnOneCourse(Map<String, Object> params) {
    	Map<String, Object> sbParams = new HashMap<String, Object>();
        String orgLevel = null;
        if(params.get("organizationId") != null){
            orgLevel = organizationDao.findById(params.get("organizationId").toString()).getOrgLevel();
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" select                                                                                                                                                                                                                                                                     ");
        sql.append(" concat(groups.name) GROUNP,                                                                                                                                                                                                                                                            ");
        sql.append(" concat(breanch.name) BRENCH,                                                                                                                                                                                                                                                          ");
        sql.append(" concat(campus.id,\"_\",campus.name) CAMPUS,                                                                                                                                                                                                                                                            ");
        sql.append(" concat(campus.orgLevel) CAMPUS_LEVEL,                                                                                                                                                                                                                                                    ");
        sql.append(" sum(1) COURSES_COUNT,                                                                                                                                                                                                                                                    ");
        sql.append(" sum(case when COURSE_STATUS in ('NEW','STUDENT_ATTENDANCE') then 1 else 0 end) NEW_COURSE_COUNT,            "); //未上课的课程数
        sql.append(" concat(GROUP_CONCAT(case when COURSE_STATUS in ('NEW','STUDENT_ATTENDANCE') then COURSE_ID  end SEPARATOR ''), '') NEW_COURSE_COUNT_IDS,            "); //未上课的课程的id
        sql.append(" sum(case when COURSE_STATUS = 'STUDENT_ATTENDANCE' then 1 else 0 end) STUDENT_ATTENDANCE_COUNT,                                                                                                                                                                                    ");
        sql.append(" sum(case when COURSE_STATUS = 'TEACHER_ATTENDANCE' then 1 else 0 end) TEACHER_ATTENDANCE_COUNT,                      ");  //已考勤的课程数
        sql.append(" concat(GROUP_CONCAT(case when COURSE_STATUS = 'TEACHER_ATTENDANCE' then COURSE_ID end), '') TEACHER_ATTENDANCE_COUNT_IDS,                      ");  //已考勤的课程的id
        sql.append(" sum(case when COURSE_STATUS in ('TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') then 1 else 0 end) REAL_TEACHER_ATTENDANCE_COUNT,                                                                                                                                                                                    ");
        sql.append(" sum(case when COURSE_STATUS in ('TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and substr(c.TEACHING_ATTEND_TIME,1,10) = c.course_date then 1 else 0 end) CURRENT_ATTENDANCE_COURSE_HOURS,                                                                                      ");
        sql.append(" sum(case when COURSE_STATUS = 'STUDY_MANAGER_AUDITED' then 1 else 0 end) STUDYMANAGER_CONFIRM,                                      "); //学管已确认的课程数
        sql.append(" concat(GROUP_CONCAT(case when COURSE_STATUS = 'STUDY_MANAGER_AUDITED' then COURSE_ID end), '') STUDYMANAGER_CONFIRM_IDS,                                      "); //学管已确认的课程的id
        sql.append(" sum(case when COURSE_STATUS in ('STUDY_MANAGER_AUDITED','CHARGED') then 1 else 0 end) STUDYMANAGER_REAL_CONFIRM,                                                                                                                                                                                ");
        sql.append(" sum(case when COURSE_STATUS in ('STUDY_MANAGER_AUDITED','CHARGED') and substr(c.STADUY_MANAGER_AUDIT_TIME,1,10) = c.course_date then 1 else 0 end) CURRENT_CONFIRM_COURSE_HOURS,                                                                                                     ");
        sql.append(" sum(case when COURSE_STATUS = 'CHARGED' then 1 else 0 end) CHARGED_COURSE_HOURS,                                                                                                                                                                                                 ");
        sql.append(" sum(case when COURSE_STATUS = 'CHARGED' and substr(c.TEACHING_ATTEND_TIME,1,10) = c.course_date then 1 else 0 end) CURRENT_CHARGED_COURSE_HOURS,                                                                                                                                     ");
        sql.append(" sum(case when COURSE_STATUS = 'STUDENT_LEAVE' then 1 else 0 end) STUDENT_LEAVE_HOURS,                                                                                                                                                                                          ");
        sql.append(" sum(case when COURSE_STATUS = 'TEACHER_LEAVE' then 1 else 0 end) TEACHER_LEAVE_HOURS,                                                                                                                                                                                          ");
        sql.append(" sum(case when COURSE_STATUS = 'CANCEL' then 1 else 0 end) CANCEL_HOURS ,                                                                                                                                                                                                ");
        sql.append(" sum(case when COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and (AUDIT_STATUS is null or AUDIT_STATUS  <> 'VALIDATE') then 1 else 0 end) UNAUDIT_COURSES_COUNT ,");//未审课数 ：课程状态已结算，审批状态为未审批的课程数
        sql.append(" sum(case when COURSE_STATUS = 'CHARGED' and (AUDIT_STATUS is null or AUDIT_STATUS  <> 'VALIDATE') then 1 else 0 end) FINANCE_UNAUDIT_COURSES_COUNT ,");//财务未审课数 ：课程状态已结算，审批状态为未审批的课程数
        sql.append(" concat(GROUP_CONCAT(case when COURSE_STATUS = 'CHARGED' and AUDIT_STATUS is null  then COURSE_ID  end), '') FINANCE_UNAUDIT_COURSES_IDS ,");//未审课的id ：课程状态已结算，审批状态为未审批的课程
        sql.append(" sum(case when COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and AUDIT_STATUS ='VALIDATE'  then 1 else 0 end) AUDIT_COURSES_COUNT,  ");//已审课数 ：课程状态已结算，审批状态为有效的课程数
        sql.append(" sum(case when COURSE_STATUS = 'CHARGED' and AUDIT_STATUS ='VALIDATE'  then 1 else 0 end) FINANCE_AUDIT_COURSES_COUNT  ");//财务已审课数 ：课程状态已结算，审批状态为有效的课程数
        if(params.get("queryConflict") != null){
            /*查冲突 start*/
            sql.append(" ,sum(c.conflict) CONLICT_HOURS                                                                                                                                                                                                                                                         ");
            sql.append(" from                                                                                                                                                                                                                                                                       ");
            sql.append(" (                                                                                                                                                                                                                                                                          ");
            sql.append(" select c.*,                                                                                                                                                                                                                                                                ");
            sql.append(" (                                                                                                                                                                                                                                                                          ");
            sql.append(" 		select case when count(distinct course_id) > 1 then c.plan_hours else 0 end from course_conflict where 1=1                                                                                                                                                              ");
            sql.append(" 		and bl_campus_id=c.BL_CAMPUS_ID                                                                                                                                                                                                                                         ");
            sql.append(" 		and (                                                                                                                                                                                                                                                                   ");
            sql.append(" 					student_id = c.student_id                                                                                                                                                                                                                                         ");
            sql.append(" 					and (                                                                                                                                                                                                                                                             ");
            sql.append(" 									concat(replace(c.course_date,'-',''),substr(c.course_time,1,2),substr(c.course_time,4,2)) between start_time and end_time  and concat(replace(c.course_date,'-',''),substr(c.course_time,1,2),substr(c.course_time,4,2)) <> end_time                      ");
            sql.append(" 							 or concat(replace(c.course_date,'-',''),substr(c.course_time,9,2),substr(c.course_time,12,2)) between start_time and end_time  and concat(replace(c.course_date,'-',''),substr(c.course_time,9,2),substr(c.course_time,12,2)) <> end_time                    ");
            sql.append(" 							)                                                                                                                                                                                                                                                             ");
            sql.append(" 				)                                                                                                                                                                                                                                                                   ");
            sql.append(" 		or  (                                                                                                                                                                                                                                                                   ");
            sql.append(" 					teacher_id = c.teacher_id                                                                                                                                                                                                                                         ");
            sql.append(" 					and (                                                                                                                                                                                                                                                             ");
            sql.append(" 									concat(replace(c.course_date,'-',''),substr(c.course_time,1,2),substr(c.course_time,4,2)) between start_time and end_time  and concat(replace(c.course_date,'-',''),substr(c.course_time,1,2),substr(c.course_time,4,2)) <> end_time                      ");
            sql.append(" 							 or concat(replace(c.course_date,'-',''),substr(c.course_time,9,2),substr(c.course_time,12,2)) between start_time and end_time  and concat(replace(c.course_date,'-',''),substr(c.course_time,9,2),substr(c.course_time,12,2)) <> end_time                    ");
            sql.append(" 							)                                                                                                                                                                                                                                                             ");
            sql.append(" 				)                                                                                                                                                                                                                                                                   ");
            sql.append(" 		) conflict                                                                                                                                                                                                                                                              ");
            sql.append(" 		from course c                                                                                                                                                                                                                                                           ");
            sql.append(" ) c                                                                                                                                                                                                                                                                        ");
            /*查冲突 end*/
        }else{
            /*不查冲突 start*/
            sql.append(" from course c                                                                                                                                                                                                                                                           ");
            /*不查冲突 end*/
        }
        sql.append(" LEFT JOIN organization as campus on campus.id = c.BL_CAMPUS_ID                                                                                                                                                                                                                ");
        sql.append(" LEFT JOIN organization as breanch on breanch.id = campus.parentID                                                                                                                                                                                                             ");
        sql.append(" LEFT JOIN organization as groups on groups.id = breanch.parentID                                                                                                                                                                                                              ");
        sql.append(" where 1=1                                                                                                                                                                                                                                                                  ");
        if(params.get("startDate") != null){
            sql.append(" and course_date >= :startDate ");
            sbParams.put("startDate", params.get("startDate"));
        }
        if(params.get("endDate") != null){
            sql.append(" and course_date <= :endDate ");
            sbParams.put("endDate", params.get("endDate"));
        }
        if(orgLevel != null){
            sql.append(" and campus.orgLevel like '").append(orgLevel).append("%'                                                                                                                                                                                                                                           ");
        }
        List<Organization> userOrganizations = organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append(" and (");
            sql.append(" campus.orgLevel like '").append(org.getOrgLevel()).append("%'");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or campus.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(" )");
        }
        sql.append(" and c.COURSE_STATUS <> 'CANCEL' ");

        sql.append(" group by c.BL_CAMPUS_ID                                                                                                                                                                                                                                                    ");
        System.out.println(sql.toString());
        List<Map<Object, Object>> list = organizationDao.findMapBySql(sql.toString(), sbParams);
        Collections.sort(list, new Comparator<Map<Object, Object>>() {
            @Override
            public int compare(Map<Object, Object> o1, Map<Object, Object> o2) {
                Double charged1 = Double.valueOf(StringUtil.toString(o1.get("CURRENT_CHARGED_COURSE_HOURS")));
                Double total1 = Double.valueOf(StringUtil.toString(o1.get("COURSES_COUNT")));
                Double charged2 = Double.valueOf(StringUtil.toString(o2.get("CURRENT_CHARGED_COURSE_HOURS")));
                Double total2 = Double.valueOf(StringUtil.toString(o2.get("COURSES_COUNT")));
                Double chargedPercent1 = charged1 / total1;
                Double chargedPercent2 = charged2 / total2;
              
//                return chargedPercent1 > chargedPercent2 ? -1 : 1;
                if(chargedPercent1.compareTo(chargedPercent2)>0){
					return -1;
				}else if(chargedPercent1.compareTo(chargedPercent2)==0){
					return 0;
				}else{
					return 1;
				}
            }
        });
        return list;
    }
    
    /**
     * 一对一课时汇总
     * @param request
     * @return
     */
    public DataPackage getOneOneOneCourseInfo(DataPackage dp, String startDate, String endDate, CourseVo courseVo){
    	Map<String, Object> params = new HashMap<String, Object>();
    	StringBuffer sql=new StringBuffer();
    	sql.append("select * from course where 1=1 ");
    	sql.append(" and course_status <> 'CANCEL' ");  	
    	if(StringUtils.isNotBlank(courseVo.getCampusId())){
    		sql.append(" and BL_CAMPUS_ID = :campusId ");
    		params.put("campusId", courseVo.getCampusId());
    	}
    	if(StringUtils.isNotBlank(startDate)){
    		sql.append(" and course_date >= :startDate ");
    		params.put("startDate", startDate);
    	}   	
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and course_date <= :endDate ");
			params.put("endDate", endDate);
		}		
		if(StringUtils.isNotBlank(courseVo.getCourseDate())){
			sql.append(" and course_date = :courseDate ");
			params.put("courseDate", courseVo.getCourseDate());
		}
		if(StringUtils.isNotBlank(courseVo.getCourseStartTime())){
			sql.append(" and SUBSTR(COURSE_TIME FROM 1 FOR 5) >= :courseStartTime ");
			params.put("courseStartTime", courseVo.getCourseStartTime());
		}
		if(StringUtils.isNotBlank(courseVo.getCourseEndTime())){
			sql.append(" and SUBSTR(COURSE_TIME FROM 9 FOR 5) <= :courseEndTime ");
			params.put("courseEndTime", courseVo.getCourseEndTime());
		}
		sql.append(" and COURSE_STATUS <> 'CANCEL' ");
    	
		sql.append(" order by COURSE_DATE desc ");
		
    	dp=courseDao.findPageBySql(sql.toString(), dp, true, params);
    	List<Course> list=(List<Course>)dp.getDatas();
    	List<CourseVo> voList=HibernateUtils.voListMapping(list, CourseVo.class);
    	if(voList != null && voList.size()>0){
    		for(CourseVo vo:voList){
    			Date courseData=DateTools.stringConversDate(vo.getCourseDate(), "yyyy-MM-dd");
    			vo.setWeekDay(DateTools.getWeekOfDate(courseData));
    		}
    	}
    	dp.setDatas(voList);
    	return dp;
    }
    
    /**
	 * 学管课时监控
	 * @param gridRequest
	 * @param courseTimeMonitorSearchInputVo
	 * @return
	 */
    @Transactional(readOnly=true)
    public DataPackage getMonthlyCourseTimeMonitor(DataPackage dataPackage, 
    		CourseTimeMonitorSearchInputVo courseTimeMonitorSearchInputVo) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (StringUtil.isBlank(courseTimeMonitorSearchInputVo.getBlCampusId())) {
    		dataPackage.setDatas(null);
    		dataPackage.setRowCount(0);
    		return dataPackage;
//    		throw new ApplicationException("校区是必选条件，请选择校区。");
    	}
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select s.ID as studentId, s.NAME as studentName, u.NAME AS studyManagerName, org.name as campusName, s.STUDY_MANEGER_ID as studyManagerId, ");
    	sql.append(" ss.NAME AS school, dd.name as grade, s.STATUS as status, orsr.lastMonthRemainHours, c.currentMonthPaidHours, d.monthConsumeHours,  ");
    	sql.append(" d.avgMonthConsumeHours, sam.ONE_ON_ONE_REMAINING_HOUR as currentRemainHuors, f.monthRepairHours ");
    	sql.append(" 	from student s ");
    	sql.append(" 	left join user u on s.STUDY_MANEGER_ID = u.USER_ID ");
    	sql.append(" 	left join STUDENT_SCHOOL ss on s.SCHOOL = ss.ID ");
    	sql.append(" 	left join data_dict dd on s.GRADE_ID = dd.ID ");
    	sql.append(" 	inner join organization org on s.BL_CAMPUS_ID = org.id ");
    	sql.append(" 	left join STUDNET_ACC_MV sam on sam.STUDENT_ID = s.Id  ");
    	sql.append(" 	left join (select orsr.STU_ID, orsr.REMAINING_HOUR as lastMonthRemainHours ");
    	sql.append(" 	from ODS_REAL_STUDENT_REMAINING orsr inner join student s on orsr.STU_ID = s.ID ");
    	int betweenDate = 31;
    	Calendar c = Calendar.getInstance();
    	c.setTime(DateTools.getDate(courseTimeMonitorSearchInputVo.getStartDate()));
		int stratDay = c.get(Calendar.DAY_OF_MONTH);
    	if (DateTools.getDateToString(DateTools.getFirstDayOfMonth(courseTimeMonitorSearchInputVo.getStartDate())).equals(DateTools.getFistDayofMonth())) {
    		c.setTime(DateTools.getDate(DateTools.getCurrentDate()));
    		int currentDay = c.get(Calendar.DAY_OF_MONTH);
    		betweenDate = currentDay - stratDay + 1;
    	} else {
    		c.setTime(DateTools.getDate(courseTimeMonitorSearchInputVo.getEndDate()));
    		int endDay = c.get(Calendar.DAY_OF_MONTH);
    		betweenDate = endDay - stratDay + 1;
    	}
    	String lastDayOfTheLastMonth = "";
    	String firstDayOfTheLastMoth = "";
    	String firstDayOfTheTwoMonthAgo = "";
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getStartDate())) {
    		lastDayOfTheLastMonth = DateTools.addDateToString(courseTimeMonitorSearchInputVo.getStartDate(), -1);
    		firstDayOfTheLastMoth = DateTools.getDateToString(DateTools.getFirstDayOfMonth(lastDayOfTheLastMonth));
    		firstDayOfTheTwoMonthAgo = DateTools.getDateToString(DateTools.getFirstDayOfMonth(DateTools.addDateToString(firstDayOfTheLastMoth, -1)));
    	} else {
    		throw new ApplicationException("年月是必选的。");
    	}
    	sql.append(" 	where 1=1 ");
    	sql.append(" 	and  orsr.COUNT_DATE = '" + lastDayOfTheLastMonth + "' and s.BL_CAMPUS_ID = '" + courseTimeMonitorSearchInputVo.getBlCampusId() + "') orsr on orsr.STU_ID = s.ID  ");
    	sql.append(" 	left join (select mal.STUDENT_ID,  ");
    	sql.append(" 	ROUND(sum((CASE WHEN cp.PAID_STATUS = 'PAID' THEN (cp.PROMOTION_AMOUNT + mal.malAmount) ELSE mal.malAmount END) / cp.PRICE),2) AS currentMonthPaidHours  ");
    	sql.append(" 	from contract_product cp  ");
    	sql.append(" 	inner join (SELECT m.STUDENT_ID, m.CONTRACT_PRODUCT_ID, ");
    	sql.append(" 	sum(CASE WHEN m.REMARK = '分配' THEN m.CHANGE_AMOUNT ELSE (-m.CHANGE_AMOUNT ) END) malAmount  ");
    	sql.append(" 	FROM MONEY_ARRANGE_LOG m  ");
    	sql.append(" 	INNER JOIN student s ON m.STUDENT_ID = s.ID  ");
    	sql.append(" 	WHERE 1 = 1  ");
    	sql.append(" 	and m.CHANGE_TIME >= :startDate and m.CHANGE_TIME <= :endDate ");
    	params.put("startDate", courseTimeMonitorSearchInputVo.getStartDate() + " 00:00:00");
    	params.put("endDate", courseTimeMonitorSearchInputVo.getEndDate() + " 23:59:59");
    	sql.append("	and m.PAY_TYPE <> 'PROMOTION'	");
    	sql.append(" 	and s.BL_CAMPUS_ID = :blCampusId ");
    	params.put("blCampusId", courseTimeMonitorSearchInputVo.getBlCampusId());
    	sql.append(" 	GROUP BY m.STUDENT_ID, m.CONTRACT_PRODUCT_ID) mal ON cp.ID = mal.CONTRACT_PRODUCT_ID  ");
    	sql.append(" 	where 1 = 1 ");
    	sql.append(" 	and cp.TYPE='ONE_ON_ONE_COURSE' ");
    	sql.append(" 	group by mal.STUDENT_ID) c on s.ID = c.STUDENT_ID ");
    	sql.append(" 	left join (select acr.STUDENT_ID, sum(acr.QUANTITY) as monthConsumeHours, ROUND(sum(acr.QUANTITY)/(" + betweenDate + "/7), 2) as avgMonthConsumeHours ");
    	sql.append(" 	from ACCOUNT_CHARGE_RECORDS acr inner JOIN course c on c.COURSE_ID = acr.COURSE_ID ");
    	sql.append(" 	where 1 = 1 ");
    	sql.append(" 	and c.COURSE_DATE >= :startDate2 and c.COURSE_DATE <= :endDate2 ");
    	params.put("startDate2", courseTimeMonitorSearchInputVo.getStartDate());
    	params.put("endDate2", courseTimeMonitorSearchInputVo.getEndDate());
    	sql.append(" 	and acr.BL_CAMPUS_ID = :blCampusId2 ");
    	params.put("blCampusId2", courseTimeMonitorSearchInputVo.getBlCampusId());
    	sql.append("    AND acr.CHARGE_TYPE = 'NORMAL' AND acr.CHARGE_PAY_TYPE = 'CHARGE' AND acr.IS_WASHED = 'FALSE' ");
    	sql.append(" 	group by acr.STUDENT_ID ) d  on s.ID = d.STUDENT_ID ");
    	sql.append(" 	left join ( select acr.STUDENT_ID, sum(acr.QUANTITY) as monthRepairHours ");
    	sql.append(" 	from ACCOUNT_CHARGE_RECORDS acr inner join student s on acr.STUDENT_ID = s.ID ");
    	sql.append(" 	inner JOIN course c on c.COURSE_ID = acr.COURSE_ID ");
    	sql.append(" 	where 1 = 1 ");
    	sql.append(" 	and acr.PAY_TIME >= :startDate3 and acr.PAY_TIME <= :endDate3 ");
    	params.put("startDate3", courseTimeMonitorSearchInputVo.getStartDate() + " 00:00:00");
    	params.put("endDate3", courseTimeMonitorSearchInputVo.getEndDate() + " 23:59:59");
    	sql.append("    AND acr.CHARGE_TYPE = 'NORMAL' AND acr.CHARGE_PAY_TYPE = 'CHARGE' AND acr.IS_WASHED = 'FALSE' ");
    	sql.append(" 	and c.COURSE_DATE >= :firstDayOfTheTwoMonthAgo ");
    	params.put("firstDayOfTheTwoMonthAgo", firstDayOfTheTwoMonthAgo);
    	sql.append(" 	and c.COURSE_DATE < :startDate4 and acr.BL_CAMPUS_ID = :blCampusId3 ");
    	params.put("startDate4", courseTimeMonitorSearchInputVo.getStartDate());
    	params.put("blCampusId3", courseTimeMonitorSearchInputVo.getBlCampusId());
    	sql.append(" 	group by acr.STUDENT_ID ) f  on s.ID = f.STUDENT_ID ");
    	sql.append(" 	where 1 = 1 ");
		sql.append(" 	and s.BL_CAMPUS_ID = :blCampusId4 ");
		params.put("blCampusId4", courseTimeMonitorSearchInputVo.getBlCampusId());
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getStudyManagerId())) {
    		sql.append(" 	and s.STUDY_MANEGER_ID = :studyManagerId ");
    		params.put("studyManagerId", courseTimeMonitorSearchInputVo.getStudyManagerId());
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getSchoolId())) {
    		sql.append(" 	and s.SCHOOL = :schoolId ");
    		params.put("schoolId", courseTimeMonitorSearchInputVo.getSchoolId());
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getGradeId())) {
    		sql.append(" 	and s.GRADE = :gradeId ");
    		params.put("gradeId", courseTimeMonitorSearchInputVo.getGradeId());
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getStatus())) {
    		sql.append(" 	and s.STATUS = :status ");
    		params.put("status", courseTimeMonitorSearchInputVo.getStatus());
    	} else {
    		sql.append(" 	and s.STATUS != '" + StudentStatus.FINISH_CLASS + "'  ");
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getStudentId())) {
    		sql.append(" 	and s.ID = :studentId ");
    		params.put("studentId", courseTimeMonitorSearchInputVo.getStudentId());
    	}
    	sql.append("  and s.ONEONONE_STATUS is not null ");
    	//sql.append(roleQLConfigService.getValueResult("学生列表","sql"));
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","org.orgLevel");
		sqlMap.put("stuId","s.id");
		sqlMap.put("manergerId","s.id");
		sqlMap.put("selStuId","(select STUDENT_ID from course where TEACHER_ID = '${userId}' )");
		sqlMap.put("selManerger","(select STUDENT_ID from course where STUDY_MANAGER_ID = '${userId}' )");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

    	sql.append(" order by s.STUDY_MANEGER_ID desc, d.monthConsumeHours desc ");
    	
    	List<Map<Object, Object>> list = studentDao.findMapOfPageBySql(sql.toString(), dataPackage, params);
    	Map<String, Map<Object, Object>> tempMap = new HashMap<String, Map<Object, Object>>();
    	for (Map<Object, Object> map : list) {
    		tempMap.put((String) map.get("studentId"), map);
    	}
    	
    	Map<String, Object> monthParams = new HashMap<String, Object>();
    	StringBuffer monthSql = new StringBuffer();
    	monthSql.append(" select acr.STUDENT_ID as studentId, DATE_FORMAT(c.COURSE_DATE,'%Y-%m-%d') as courseDate,  sum(QUANTITY) as dayConsumeHours ");
    	monthSql.append(" from ACCOUNT_CHARGE_RECORDS  acr ");
    	monthSql.append(" inner join student s on acr.STUDENT_ID = s.ID ");
    	monthSql.append(" inner JOIN course c on c.COURSE_ID = acr.COURSE_ID ");
		monthSql.append(" inner JOIN organization org on s.BL_CAMPUS_ID = org.id ");
    	monthSql.append(" where c.COURSE_DATE >= :startDate and c.COURSE_DATE <= :endDate and PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' ");
    	monthParams.put("startDate", courseTimeMonitorSearchInputVo.getStartDate());
    	monthParams.put("endDate", courseTimeMonitorSearchInputVo.getEndDate());
    	monthSql.append(" and s.BL_CAMPUS_ID = :blCampusId ");
    	monthParams.put("blCampusId", courseTimeMonitorSearchInputVo.getBlCampusId());
    	monthSql.append("    AND acr.CHARGE_TYPE = 'NORMAL' AND acr.CHARGE_PAY_TYPE = 'CHARGE' AND acr.IS_WASHED = 'FALSE' ");
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getStudyManagerId())) {
    		monthSql.append(" 	and s.STUDY_MANEGER_ID = :studyManagerId  ");
    		monthParams.put("studyManagerId", courseTimeMonitorSearchInputVo.getStudyManagerId());
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getSchoolId())) {
    		monthSql.append(" 	and s.SCHOOL = :schoolId  ");
    		monthParams.put("schoolId", courseTimeMonitorSearchInputVo.getSchoolId());
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getGradeId())) {
    		monthSql.append(" 	and s.GRADE = :gradeId ");
    		monthParams.put("gradeId", courseTimeMonitorSearchInputVo.getGradeId());
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getStatus())) {
    		monthSql.append(" 	and s.STATUS = :status ");
    		monthParams.put("status", courseTimeMonitorSearchInputVo.getStatus());
    	} else {
    		monthSql.append(" 	and s.STATUS != '" + StudentStatus.FINISH_CLASS + "'  ");
    	}
    	if (StringUtil.isNotBlank(courseTimeMonitorSearchInputVo.getStudentId())) {
    		monthSql.append(" 	and s.ID = :studentId ");
    		monthParams.put("studentId", courseTimeMonitorSearchInputVo.getStudentId());
    	}
    	monthSql.append("  and s.ONEONONE_STATUS is not null ");
    	//monthSql.append(roleQLConfigService.getValueResult("学生列表","sql"));

		monthSql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));


		monthSql.append(" group by studentId, courseDate ");
    	List<Map<Object, Object>> monthList = studentDao.findMapBySql(monthSql.toString(), monthParams);
    	String studentId = "";
    	Map<Object, Object> studentMap = null;
    	String courseDate = "";
    	
    	for (Map<Object, Object> map : monthList) {
    		studentId = (String) map.get("studentId");
    		studentMap = tempMap.get(studentId);
    		if (null != studentMap) {
    			courseDate = (String) map.get("courseDate");
    			courseDate = "day" + courseDate.substring(8, 10);
    			studentMap.put(courseDate, map.get("dayConsumeHours"));
    		}
    	}
    	
    	dataPackage.setDatas(list);
    	dataPackage.setRowCount(studentDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
    	return dataPackage;
    }
    
    
    /**
   	 * 产品消耗分析
   	 * @param dp
   	 * @param startDate
   	 * @param endDate
   	 * @param organizationId
   	 * @return
   	 */
    public DataPackage getProductConsumeAnalyze(DataPackage dp, String startDate,String endDate,String organizationId) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	Organization org = organizationDao.findById(organizationId);
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT org_group.id as groupId, org_group.name as groupName,  org_branch.id as brenchId, ");
    	sql.append(" org_branch.name as brenchName, org_campus.id as campusId, org_campus.name as campusName, ");
    	sql.append(" ifnull(odpc_sum.oneOnOneIncoming, 0) AS oneOnOneIncoming, ifnull(odpc_sum.miniClassIncoming, 0) AS miniClassIncoming, ");
    	sql.append(" ifnull(odpc_sum.ecsClassIncoming, 0) AS ecsClassIncoming, ifnull(odpc_sum.othersIncoming, 0) AS othersIncoming, ");
    	sql.append(" ifnull(odpc_sum.normalIncoming, 0) AS normalIncoming, ifnull(odpc_sum.totalIncoming, 0) AS totalIncoming ");   	
    	sql.append(" FROM organization org_campus ");
    	sql.append(" LEFT JOIN organization org_branch ON org_campus.parentID = org_branch.id ");
    	sql.append(" LEFT JOIN organization org_group ON org_branch.parentID = org_group.id ");
    	sql.append(" LEFT JOIN ( select odpc.CAMPUS_ID AS campusId, sum(odpc.ONE_ON_ONE_INCOMING) AS oneOnOneIncoming, sum(odpc.MINI_CLASS_INCOMING) AS miniClassIncoming, ");
    	sql.append(" sum(odpc.ECS_CLASS_INCOMING) AS ecsClassIncoming, sum(odpc.OTHERS_INCOMING) AS othersIncoming, sum(odpc.NORMAL_INCOMING) AS normalIncoming, sum(odpc.TOTAL_INCOMING) AS totalIncoming ");
    	sql.append(" from  ods_day_product_consume odpc ");
    	sql.append(" where odpc.COUNT_DATE >= :startDate and odpc.COUNT_DATE <= :endDate ");
    	params.put("startDate", startDate);
    	params.put("endDate", endDate);
    	sql.append(" group by odpc.CAMPUS_ID ) odpc_sum ON odpc_sum.campusId = org_campus.id");
       	sql.append(" where org_campus.orgLevel like '" + org.getOrgLevel() + "%' and org_campus.orgType = 'CAMPUS' order by org_branch.id, org_campus.id");//level只要后面加%like ,前面也加就会查别的分公司的
    	List<Map<Object, Object>> list = organizationDao.findMapBySql(sql.toString(), params);
    	dp.setDatas(list);
    	dp.setRowCount(list.size());
    	return dp;
    }
    
    /**
   	 * 产品销售分析
   	 * @param gridRequest
   	 * @param startDate
   	 * @param endDate
   	 * @param organizationId
   	 * @return
   	 */
    public DataPackage getProductMarketAnalyze(DataPackage dp, String startDate,String endDate,String organizationId) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	Organization org = organizationDao.findById(organizationId);
    	StringBuffer sql = new StringBuffer(); 	
    	sql.append(" SELECT org_group.id as groupId, org_group.name as groupName,  org_branch.id as brenchId, ");
    	sql.append(" org_branch.name as brenchName, org_campus.id as campusId, org_campus.name as campusName, ");
    	sql.append(" ifnull(odpm_sum.oneOnOneAmount, 0) as oneOnOneAmount, ifnull(odpm_sum.smallAmount, 0) as smallAmount, ");
    	sql.append(" ifnull(odpm_sum.ecsAmount, 0) as ecsAmount, ifnull(odpm_sum.otherAmount, 0) as othersAmount, ");
    	sql.append(" ifnull(odpm_sum.undistributedAmount, 0) as undistributedAmount, ifnull(odpm_sum.totalAmount, 0) as totalAmount ");   	
    	sql.append(" FROM organization org_campus ");
    	sql.append(" LEFT JOIN organization org_branch ON org_campus.parentID = org_branch.id ");
    	sql.append(" LEFT JOIN organization org_group ON org_branch.parentID = org_group.id ");
    	sql.append(" LEFT JOIN ( select odpm.CAMPUS_ID AS campusId, sum(odpm.ONE_ON_ONE_AMOUNT) AS oneOnOneAmount, sum(odpm.SMALL_AMOUNT) AS smallAmount,");
    	sql.append(" sum(odpm.ECS_AMOUNT) AS ecsAmount, sum(odpm.OTHER_AMOUNT) AS otherAmount, sum(odpm.UNDISTRIBUTED_AMOUNT) AS undistributedAmount, sum(odpm.TOTAL_AMOUNT) AS totalAmount ");
    	sql.append(" from  ods_day_product_market odpm ");
    	sql.append(" where odpm.COUNT_DATE >= :startDate and odpm.COUNT_DATE <= :endDate ");
    	params.put("startDate", startDate);
    	params.put("endDate", endDate);
    	sql.append(" group by odpm.CAMPUS_ID ) odpm_sum ON odpm_sum.campusId = org_campus.id");
       	sql.append(" where org_campus.orgLevel like '" + org.getOrgLevel() + "%' and org_campus.orgType = 'CAMPUS' order by org_branch.id, org_campus.id");//level只要后面加%like ,前面也加就会查别的分公司的
    	List<Map<Object, Object>> list = organizationDao.findMapBySql(sql.toString(), params);
    	dp.setDatas(list);
    	dp.setRowCount(list.size());
    	return dp;
    }
    
    /**
	 * 产品销售分析(科目)小班
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @param productType
	 * @return
	 */
    public DataPackage getProductMarketSubjectAnalyze(DataPackage dp, String startDate,String endDate,String organizationId, String productType) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	Organization org = organizationDao.findById(organizationId);
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select org_group.id as groupId, org_group.name as groupName, org_brench.id as brenchId, org_brench.name as brenchName,   ");
    	sql.append(" org_campus.id as campusId, org_campus.name as campusName, ifnull(f.chineseAmount, 0) as chineseAmount, ifnull(f.mathAmount, 0) as mathAmount, ");
    	sql.append(" ifnull(f.englishAmount, 0) as englishAmount, ifnull(f.physicsAmount, 0) as physicsAmount, ifnull(f.chemistryAmount, 0) as chemistryAmount, ");
    	sql.append(" ifnull(f.politicsAmount, 0) as politicsAmount, ifnull(f.historyAmount, 0) as historyAmount, ifnull(f.geographyAmount, 0) as geographyAmount, ");
    	sql.append(" ifnull(f.biologyAmount, 0) as biologyAmount,  ifnull(f.subjectTotalAmount, 0) as totalAmount, ");
    	sql.append(" ifnull((f.subjectTotalAmount - f.chineseAmount - f.mathAmount - f.englishAmount - f.physicsAmount - f.chemistryAmount - f.politicsAmount - f.historyAmount - f.geographyAmount - f.biologyAmount), 0) as othersAmount ");
    	sql.append(" FROM organization org_campus ");
    	sql.append(" INNER JOIN organization org_brench on org_campus.parentID = org_brench.id ");
    	sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
    	sql.append(" INNER JOIN (select BL_CAMPUS_ID, max(IF(subjectName = '语文', amount, 0)) as 'chineseAmount', max(IF(subjectName = '数学', amount, 0)) as 'mathAmount', ");
    	sql.append(" max(IF(subjectName = '英语', amount, 0)) as 'englishAmount', max(IF(subjectName = '物理', amount, 0)) as 'physicsAmount', ");
    	sql.append(" max(IF(subjectName = '化学', amount, 0)) as 'chemistryAmount',  max(IF(subjectName = '政治', amount, 0)) as 'politicsAmount', ");
    	sql.append(" max(IF(subjectName = '历史', amount, 0)) as 'historyAmount', max(IF(subjectName = '地理', amount, 0)) as 'geographyAmount',  ");
    	sql.append(" max(IF(subjectName = '生物', amount, 0)) as 'biologyAmount', sum(amount) as subjectTotalAmount ");
    	sql.append(" FROM (SELECT c.BL_CAMPUS_ID, s.ID as subjectId, s.NAME as subjectName, sum(mal.CHANGE_AMOUNT) as amount  ");
    	sql.append(" FROM MONEY_ARRANGE_LOG mal ");
    	sql.append(" INNER JOIN contract_product cp on mal.CONTRACT_PRODUCT_ID = cp.ID ");
    	sql.append(" INNER JOIN contract c on cp.CONTRACT_ID = c.ID ");
    	sql.append(" INNER JOIN product p on cp.PRODUCT_ID = p.ID ");
    	sql.append(" LEFT JOIN (select ID, NAME from data_dict where CATEGORY = 'SUBJECT') s ON s.ID = p.SUBJECT_ID ");
    	sql.append(" WHERE  mal.CHANGE_TIME >= :startDate and mal.CHANGE_TIME <= :endDate ");
    	params.put("startDate", startDate + " 00:00:00");
    	params.put("endDate", endDate + " 23:59:59");
    	sql.append(" AND cp.TYPE = 'SMALL_CLASS' ");
    	sql.append(" GROUP BY c.BL_CAMPUS_ID, s.ID) d ");
    	sql.append(" GROUP BY BL_CAMPUS_ID) f on org_campus.id = f.BL_CAMPUS_ID ");
    	sql.append(" where org_campus.orgLevel like '" + org.getOrgLevel() + "%' and org_campus.orgType = 'CAMPUS' order by org_brench.id ");//level只要后面加%like ,前面也加就会查别的分公司的
    	List<Map<Object, Object>> list = organizationDao.findMapBySql(sql.toString(), params);
    	dp.setDatas(list);
    	dp.setRowCount(list.size());
    	return dp;
    }
    
    /**
   	 * 产品销售分析(科目) 一对一
   	 * @param gridRequest
   	 * @param startDate
   	 * @param endDate
   	 * @param organizationId
   	 * @param productType
   	 * @return
   	 */
    public DataPackage getProductMarketSubjectOneOnOneAnalyze(DataPackage dp, String startDate,String endDate,String organizationId, String productType) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	Organization org = organizationDao.findById(organizationId);
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select org_group.id as groupId, org_group.name as groupName, org_brench.id as brenchId, org_brench.name as brenchName,   ");
    	sql.append(" org_campus.id as campusId, org_campus.name as campusName, ifnull(f.chineseAmount, 0) as chineseAmount, ifnull(f.mathAmount, 0) as mathAmount, ");
    	sql.append(" ifnull(f.englishAmount, 0) as englishAmount, ifnull(f.physicsAmount, 0) as physicsAmount, ifnull(f.chemistryAmount, 0) as chemistryAmount, ");
    	sql.append(" ifnull(f.politicsAmount, 0) as politicsAmount, ifnull(f.historyAmount, 0) as historyAmount, ifnull(f.geographyAmount, 0) as geographyAmount, ");
    	sql.append(" ifnull(f.biologyAmount, 0) as biologyAmount,  ifnull(f.subjectTotalAmount, 0) as totalAmount, ");
    	sql.append(" ifnull((f.subjectTotalAmount - f.chineseAmount - f.mathAmount - f.englishAmount - f.physicsAmount - f.chemistryAmount - f.politicsAmount - f.historyAmount - f.geographyAmount - f.biologyAmount), 0) as othersAmount ");
    	sql.append(" FROM organization org_campus ");
    	sql.append(" INNER JOIN organization org_brench on org_campus.parentID = org_brench.id ");
    	sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
    	sql.append(" INNER JOIN (select BL_CAMPUS_ID, max(IF(subjectName = '语文', amount, 0)) as 'chineseAmount', max(IF(subjectName = '数学', amount, 0)) as 'mathAmount', ");
    	sql.append(" max(IF(subjectName = '英语', amount, 0)) as 'englishAmount', max(IF(subjectName = '物理', amount, 0)) as 'physicsAmount', ");
    	sql.append(" max(IF(subjectName = '化学', amount, 0)) as 'chemistryAmount',  max(IF(subjectName = '政治', amount, 0)) as 'politicsAmount', ");
    	sql.append(" max(IF(subjectName = '历史', amount, 0)) as 'historyAmount', max(IF(subjectName = '地理', amount, 0)) as 'geographyAmount',  ");
    	sql.append(" max(IF(subjectName = '生物', amount, 0)) as 'biologyAmount', sum(amount) as subjectTotalAmount ");
    	sql.append(" FROM (select dd.BL_CAMPUS_ID, dd.subjectId, dd.subjectName, sum(dd.PRICE*dd.QUANTITY) as amount ");
    	sql.append(" from (select distinct c.BL_CAMPUS_ID, s.ID as subjectId, s.NAME as subjectName, cp.PRICE, cps.QUANTITY, cp.TYPE, cp.PAID_AMOUNT ");
    	sql.append(" from contract_product cp ");
    	sql.append(" LEFT JOIN CONTRACT_PRODUCT_SUBJECT cps on cp.ID = cps.CONTRACT_PRODUCT_ID  ");
    	sql.append(" INNER JOIN contract c on cp.CONTRACT_ID = c.ID  ");
    	sql.append(" INNER JOIN MONEY_ARRANGE_LOG mal on mal.CONTRACT_PRODUCT_ID = cp.ID  ");
    	sql.append(" left join product p on cp.PRODUCT_ID = p.ID ");
    	sql.append(" LEFT JOIN (select ID, NAME from data_dict where CATEGORY = 'SUBJECT') s on s.ID = cps.SUBJECT_ID  ");
    	sql.append(" WHERE  mal.CHANGE_TIME >= :startDate and mal.CHANGE_TIME <= :endDate ");
    	params.put("startDate", startDate + " 00:00:00");
    	params.put("endDate", endDate + " 23:59:59");
    	sql.append(" AND cp.TYPE = 'ONE_ON_ONE_COURSE' ");
    	sql.append(" ) dd group by dd.BL_CAMPUS_ID, dd.subjectId) d ");
    	sql.append(" GROUP BY BL_CAMPUS_ID) f on org_campus.id = f.BL_CAMPUS_ID ");
    	sql.append(" where org_campus.orgLevel like '" + org.getOrgLevel() + "%' and org_campus.orgType = 'CAMPUS' order by org_brench.id ");//level只要后面加%like ,前面也加就会查别的分公司的
    	List<Map<Object, Object>> list = organizationDao.findMapBySql(sql.toString(), params);
    	dp.setDatas(list);
    	dp.setRowCount(list.size());
    	return dp;
    }
    
    /**
	 * 产品销售分析(年级)
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param organizationId
	 * @param productType
	 * @return
	 */
    public DataPackage getProductMarketGradeAnalyze(DataPackage dp, String startDate,String endDate,String organizationId, String productType) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	Organization org = organizationDao.findById(organizationId);
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select org_group.id as groupId, org_group.name as groupName, org_brench.id as brenchId, org_brench.name as brenchName, ");
    	sql.append(" org_campus.id as campusId, org_campus.name as campusName, ifnull(ga.gradeOneAmount, 0) as gradeOneAmount, ");
    	sql.append(" ifnull(ga.gradeTwoAmount, 0) as gradeTwoAmount, ifnull(ga.gradeThreeAmount, 0) as gradeThreeAmount, ");
    	sql.append(" ifnull(ga.gradeFourAmount, 0) as gradeFourAmount, ifnull(ga.gradeFiveAmount, 0) as gradeFiveAmount, ");
    	sql.append(" ifnull(ga.gradeSixAmount, 0) as gradeSixAmount, ifnull(ga.gradeSevenAmount, 0) as gradeSevenAmount, ");
    	sql.append(" ifnull(ga.gradeEightAmount, 0) as gradeEightAmount, ifnull(ga.gradeNineAmount, 0) as gradeNineAmount, ");
    	sql.append(" ifnull(ga.gradeTenAmount, 0) as gradeTenAmount, ifnull(ga.gradeElevenAmount, 0) as gradeElevenAmount, ");
    	sql.append(" ifnull(ga.gradeTwelveAmount, 0) as gradeTwelveAmount, ifnull(ga.othersAmount, 0) as othersAmount, ");
    	sql.append(" ifnull(ga.othersAmount, 0) as othersAmount, ifnull(ga.gradeTotalAmount, 0) as gradeTotalAmount, ifnull(ta.totalAmount, 0) as totalAmount ");
    	sql.append(" FROM organization org_campus ");
    	sql.append(" INNER JOIN organization org_brench on org_campus.parentID = org_brench.id ");
    	sql.append(" INNER JOIN organization org_group on org_brench.parentID = org_group.id ");
    	sql.append(" INNER JOIN (SELECT c.BL_CAMPUS_ID, sum(fch.TRANSACTION_AMOUNT) as totalAmount ");
    	sql.append(" FROM funds_change_history fch ");
    	sql.append(" INNER JOIN contract c ON fch.CONTRACT_ID = c.ID ");
    	sql.append(" where fch.TRANSACTION_TIME >= :startDate and fch.TRANSACTION_TIME <= :endDate ");
    	params.put("startDate", startDate + " 00:00:00");
    	params.put("endDate", endDate + " 23:59:59");
    	sql.append(" group by c.BL_CAMPUS_ID) ta ON org_campus.id = ta.BL_CAMPUS_ID  ");
    	sql.append(" INNER JOIN (SELECT BL_CAMPUS_ID, max(IF(gradeName = '一年级', amount, 0)) as 'gradeOneAmount', max(IF(gradeName = '二年级', amount, 0)) as 'gradeTwoAmount', ");
    	sql.append(" max(IF(gradeName = '三年级', amount, 0)) as 'gradeThreeAmount', max(IF(gradeName = '四年级', amount, 0)) as 'gradeFourAmount', ");
    	sql.append(" max(IF(gradeName = '五年级', amount, 0)) as 'gradeFiveAmount', max(IF(gradeName = '六年级', amount, 0)) as 'gradeSixAmount', ");
    	sql.append(" max(IF(gradeName = '初一', amount, 0)) as 'gradeSevenAmount', max(IF(gradeName = '初二', amount, 0)) as 'gradeEightAmount', ");
    	sql.append(" max(IF(gradeName = '初三', amount, 0)) as 'gradeNineAmount', max(IF(gradeName = '高一', amount, 0)) as 'gradeTenAmount', ");
    	sql.append(" max(IF(gradeName = '高二', amount, 0)) as 'gradeElevenAmount', max(IF(gradeName = '高三', amount, 0)) as 'gradeTwelveAmount', ");
    	sql.append(" max(IF(gradeName is null, amount, 0)) as 'othersAmount', sum(amount) as gradeTotalAmount ");
    	sql.append(" from  (SELECT c.BL_CAMPUS_ID, c.GRADE_ID as gradeId,g.NAME as gradeName, sum(mal.CHANGE_AMOUNT) as amount  ");
    	sql.append(" FROM MONEY_ARRANGE_LOG mal ");
    	sql.append(" INNER JOIN contract_product cp ON cp.ID = mal.CONTRACT_PRODUCT_ID ");
    	sql.append(" INNER JOIN contract c ON cp.CONTRACT_ID = c.ID ");
    	sql.append(" INNER JOIN product p ON p.ID = cp.PRODUCT_ID ");
    	sql.append(" LEFT JOIN (select ID, NAME from data_dict where CATEGORY = 'STUDENT_GRADE' ) g ON p.GRADE_ID = g.ID ");
    	sql.append(" where  mal.CHANGE_TIME >= :startDate2 and mal.CHANGE_TIME <= :endDate2 ");
    	params.put("startDate2", startDate + " 00:00:00");
    	params.put("endDate2", endDate + " 23:59:59");
    	if (StringUtils.isNotBlank(productType)) {
    		sql.append(" and cp.TYPE = :productType ");
    		params.put("productType", productType);
    	}
    	sql.append(" GROUP BY c.BL_CAMPUS_ID, p.GRADE_ID) d  group by BL_CAMPUS_ID) ga ON org_campus.id = ga.BL_CAMPUS_ID ");
    	sql.append(" where org_campus.orgLevel like '" + org.getOrgLevel() + "%' and org_campus.orgType = 'CAMPUS' order by org_brench.id ");//level只要后面加%like ,前面也加就会查别的分公司的
    	List<Map<Object, Object>> list = organizationDao.findMapBySql(sql.toString(), params);
    	dp.setDatas(list);
    	dp.setRowCount(list.size());
    	return dp;
    }
    
    /**
	 * 设置数据包记录数的公共方法
	 * @param sbSql
	 * @param dataPackage
	 * add by llh
	 */
	private DataPackage setDataPackageCount(StringBuilder sbSql, DataPackage dataPackage, Map<String, Object> params) {
		StringBuilder sbsql2 = new StringBuilder();
		sbsql2.append("select count(1) from (");
		sbsql2.append(sbSql);
		sbsql2.append(") tt");
		dataPackage.setRowCount(organizationDao.findCountSql(sbsql2.toString(), params));
		return dataPackage;
	}
	
	/**
	 * 网络资源利用情况
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public DataPackage internetUseConditionList2(DataPackage dp,String year,String month,String brenchId,String levelType,String followUserId){
		Map<String, Object> params = new HashMap<String, Object>();
		Organization org=userService.getCurrentLoginUserOrganization();
//		if (StringUtil.isNotBlank(org.getBelong())) {
//			org = organizationDao.findById(org.getBelong());
//		}
		String startDate=year+"-"+month+"-01";
		String endDate=DateTools.getLastDayofMonth(Integer.valueOf(year),Integer.valueOf(month));
		//用户职位在，客服主管，客服专员，兼职客服的用户sat('USE0000000041','USE0000000042','USE0000000043')
		//测试和正式(USE0000000114,USE0000000115,USE0000000116)
		
		StringBuffer orgSql=new StringBuffer();
		orgSql.append(" CASE WHEN g.id is NULL THEN (CASE WHEN b.id is NULL THEN CONCAT(o.id,'_',o.`name`) ELSE CONCAT(b.id,'_',b.`name`) END) ELSE CONCAT(g.id,'_',g.`name`) END GROUNP, ");
		orgSql.append(" CASE  WHEN g.id is NULL  THEN CONCAT(o.id, '_', o.`name`)  ELSE CONCAT(b.id, '_', b.`name`) END BRENCH, ");
		orgSql.append(" CASE  WHEN  CONCAT('', g.id) IS NULL THEN CONCAT('',o.id)  ELSE CONCAT('', b.id)  END as bid, ");
		orgSql.append("  CONCAT('',o.id) as oid, ");	
		
				
		//留电量
		StringBuffer sql=new StringBuffer();
		StringBuffer sqlo=new StringBuffer();
		sql.append(" select	 ");
		sql.append(orgSql);
		sql.append(" cbd.follow_user_id, u.`NAME`, count(c.id) callNumber ");
		sql.append(" from customer c ");
		sql.append("  INNER JOIN customer_before_deliver cbd on cbd.customer_id=c.id ");
		sql.append("  INNER JOIN `user` u on u.user_id=cbd.follow_user_id  ");
		sql.append(" INNER JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" INNER JOIN organization o on o.id=c.bl_school ");
		sql.append(" LEFT JOIN organization b on o.parentID=b.id ");
		sql.append(" LEFT JOIN organization g on b.parentID=g.id ");
		sql.append(" where udj.JOB_ID in ('USE0000000114','USE0000000115','USE0000000116')  ");
		sql.append("  and c.RES_ENTRANCE ='ON_LINE' ");
		sql.append(" and c.DEAL_STATUS <> 'INVALID' ");
		if(org.getOrgType()==OrganizationType.DEPARTMENT){
			Organization parentOrg=organizationDao.findById(org.getParentId());
			sql.append(" and o.orgLevel like '"+parentOrg.getOrgLevel()+"%' ");
		}else{
			sql.append(" and o.orgLevel like '"+org.getOrgLevel()+"%' ");
		}	

		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sql.append(" and c.create_time >= :startDate ");
			params.put("startDate", startDate + " 0000");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sql.append(" and c.create_time <= :endDate ");
			params.put("endDate", endDate + " 2359");
		}
		if(StringUtils.isNotBlank(followUserId)){
			sql.append(" and cbd.follow_user_id = :followUserId ");
			params.put("followUserId", followUserId);
		}					
		
		List<Map<Object, Object>> callNumberList=new ArrayList<Map<Object,Object>>();		
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sql.append(" group by b.id, cbd.follow_user_id ");
			
			sqlo.append(" select * from ( ");
			sqlo.append( sql );
			sqlo.append(" ) a where bid = :brenchId ");
			params.put("brenchId", brenchId);
			callNumberList=organizationDao.findMapBySql(sqlo.toString(), params);	
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			sql.append(" GROUP BY  bid ");
			callNumberList=organizationDao.findMapBySql(sql.toString(), params);	
		}	
	
		Map<String, Object> params2 = new HashMap<String, Object>();
		//合同总金额
		StringBuffer sqlc=new StringBuffer();
		StringBuffer sqlco=new StringBuffer();
		sqlc.append(" select c.follow_user_id,CONCAT('', c.bid) as bid, IFNULL(SUM(c.PAID_AMOUNT), 0) as paidAmount,IFNULL(SUM(c.planAmount),0) as totalAmount,c.CUSTOMER_ID,NAME,GROUNP,BRENCH ");
		sqlc.append(" from ( ");
		sqlc.append(" select  ");
		sqlc.append(orgSql);
		sqlc.append(" u.NAME,sum(c.PAID_AMOUNT) PAID_AMOUNT, cus.id as customer_id,sum(cp.PLAN_AMOUNT) planAmount,cbd.FOLLOW_USER_ID ");
		sqlc.append("  from contract c ");
		sqlc.append(" INNER JOIN customer cus on c.CUSTOMER_ID=cus.id ");		
		sqlc.append(" INNER JOIN customer_before_deliver cbd on cus.id=cbd.CUSTOMER_ID ");		
		sqlc.append(" INNER JOIN  `user` u  on u.user_id=cbd.follow_user_id ");
		sqlc.append(" INNER JOIN user_dept_job udj  on udj.user_id=u.user_id  ");
		sqlc.append(" INNER JOIN organization o on o.id=cus.BL_SCHOOL ");
		sqlc.append(" LEFT JOIN organization b on o.parentID = b.id ");
		sqlc.append(" LEFT JOIN organization g on b.parentID=g.id ");	
		sqlc.append(" INNER JOIN ( select sum(cp.PLAN_AMOUNT) PLAN_AMOUNT, cp.CONTRACT_ID from contract_product cp  ");
		sqlc.append(" where cp.CONTRACT_ID in (  ");
		sqlc.append(" select DISTINCT(c.id) from contract c  ");
		sqlc.append(" LEFT JOIN funds_change_history f on f.contract_id=c.id ");
		sqlc.append(" INNER JOIN customer_before_deliver cbd on cbd.CUSTOMER_ID=c.CUSTOMER_ID ");
		sqlc.append(" where 1=1 ");
		// 查询收款日期在跟进日期后的30天内的合同金额  收款日期<=跟进日期+30天
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and f.TRANSACTION_TIME <=date_sub(cbd.FOLLOW_TIME, interval -30 day) ");
		}
		sqlc.append(" ) GROUP BY cp.CONTRACT_ID) cp on cp.CONTRACT_ID=c.id  "); 
		sqlc.append(" where  udj.JOB_ID in ( 'USE0000000114','USE0000000115','USE0000000116') ");
		sqlc.append(" and cus.RES_ENTRANCE='ON_LINE'  ");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlc.append(" and cus.create_time >= :startDate ");
			params2.put("startDate", startDate + " 0000");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and cus.create_time <= :endDate ");
			params2.put("endDate", endDate + " 2359");
		}
		if(org.getOrgType()==OrganizationType.DEPARTMENT){
			Organization parentOrg=organizationDao.findById(org.getParentId());
			sqlc.append(" and o.orgLevel like '"+parentOrg.getOrgLevel()+"%' ");
		}else{
			sqlc.append(" and o.orgLevel like '"+org.getOrgLevel()+"%' ");
		}
		if(StringUtils.isNotBlank(followUserId)){
			sqlc.append(" and cbd.follow_user_id = :followUserId ");
			params2.put("followUserId", followUserId);
		}
		sqlc.append(" GROUP BY c.CUSTOMER_ID ) c  ");
		List<Map<Object, Object>> paidAmountList=new ArrayList<Map<Object,Object>>();
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sqlc.append(" GROUP BY  c.FOLLOW_USER_ID,c.bid ");
			
			sqlco.append(" select * from ( ");
			sqlco.append( sqlc );
			sqlco.append(" ) a where bid = :brenchId ");
			params2.put("brenchId", brenchId);
			
			paidAmountList=organizationDao.findMapBySql(sqlco.toString(), params2);
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			sqlc.append(" GROUP BY bid ");
			 paidAmountList=organizationDao.findMapBySql(sqlc.toString(), params2);
		}
		
		//签单量
		Map<String, Object> params3 = new HashMap<String, Object>();
		StringBuffer sqls=new StringBuffer();
		StringBuffer sqlso=new StringBuffer();
		sqls.append(" select con.follow_user_id,con.create_time,CONCAT('', con.bid) as bid, COUNT(1) as successNumber,con.CUSTOMER_ID,NAME,GROUNP,BRENCH ");
		sqls.append(" from ( ");
		sqls.append(" select  ");
		sqls.append(orgSql);
		sqls.append(" u.NAME,cus.id as customer_id,cbd.follow_user_id ,c.create_time,cbd.follow_time ");
		sqls.append(" from contract c ");
		sqls.append(" INNER JOIN customer cus on c.CUSTOMER_ID=cus.ID ");
		sqls.append(" INNER JOIN customer_before_deliver cbd on cbd.CUSTOMER_ID=cus.id ");
		sqls.append(" INNER JOIN  `user` u  on u.user_id=cbd.follow_user_id ");
		sqls.append(" INNER JOIN organization o on o.id=cus.BL_SCHOOL ");
		sqls.append(" LEFT JOIN organization b on o.parentID = b.id ");
		sqls.append(" LEFT JOIN organization g on b.parentID=g.id ");	
		sqls.append("  where cus.RES_ENTRANCE ='ON_LINE' ");
		sqls.append(" and cus.DEAL_STATUS <> 'INVALID' ");
		if(org.getOrgType()==OrganizationType.DEPARTMENT){
			Organization parentOrg=organizationDao.findById(org.getParentId());
			sqls.append(" and o.orgLevel like '"+parentOrg.getOrgLevel()+"%' ");
		}else{
			sqls.append(" and o.orgLevel like '"+org.getOrgLevel()+"%' ");
		}
		if(StringUtils.isNotBlank(followUserId)){
			sqls.append(" and cbd.follow_user_id = :followUserId ");
			params3.put("followUserId", followUserId);
		}
		
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqls.append(" and cus.create_time >= :startDate ");
			params3.put("startDate", startDate + " 0000");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqls.append(" and cus.create_time <= :endDate ");
			params3.put("endDate", endDate + " 2359");
		}
		//40天内签单量
		sqls.append(" and c.create_time <=date_sub(cbd.FOLLOW_TIME, interval -30 day) ");
		sqls.append(" and cbd.FOLLOW_USER_ID in ");
//		 GROUP BY c.CUSTOMER_ID
		sqls.append(" ( SELECT USER_ID from user_dept_job  where JOB_ID in ( 'USE0000000114','USE0000000115','USE0000000116')  )  ");
		sqls.append("  ) con ");		
		
		List<Map<Object, Object>> successNumberList=new ArrayList<Map<Object,Object>>();
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sqls.append(" GROUP BY  con.FOLLOW_USER_ID,con.bid ");
			
			sqlso.append(" select * from ( ");
			sqlso.append( sqls );
			sqlso.append(" ) a where bid = :brenchId ");
			params3.put("brenchId", brenchId);
			
			successNumberList=organizationDao.findMapBySql(sqlso.toString(), params3);
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			 sqls.append(" GROUP BY bid ");
			 successNumberList=organizationDao.findMapBySql(sqls.toString(), params3);
		}

		//未跟进
		Map<String, Object> params4 = new HashMap<String, Object>();
		StringBuffer sqlf=new StringBuffer();
		StringBuffer sqlfo=new StringBuffer();
		sqlf.append(" select ");
		sqlf.append(orgSql);
		sqlf.append(" cbd.follow_user_id, u.`NAME`, count(c.id) notFollowNumber ");
		sqlf.append(" from customer c ");
		sqlf.append("  INNER JOIN customer_before_deliver cbd on cbd.customer_id=c.id ");
		sqlf.append("  INNER JOIN `user` u on u.user_id=cbd.follow_user_id  ");
		sqlf.append(" INNER JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sqlf.append(" INNER JOIN organization o on o.id=c.bl_school ");
		sqlf.append(" LEFT JOIN organization b on o.parentID=b.id ");
		sqlf.append(" LEFT JOIN organization g on b.parentID=g.id ");
		sqlf.append(" where udj.JOB_ID in ('USE0000000114','USE0000000115','USE0000000116')  ");
		sqlf.append("  and c.RES_ENTRANCE ='ON_LINE' ");
		sqlf.append(" and c.DEAL_STATUS <> 'INVALID' ");
		if(org.getOrgType()==OrganizationType.DEPARTMENT){
			Organization parentOrg=organizationDao.findById(org.getParentId());
			sqlf.append(" and o.orgLevel like '"+parentOrg.getOrgLevel()+"%' ");
		}else{
			sqlf.append(" and o.orgLevel like '"+org.getOrgLevel()+"%' ");
		}
		if(StringUtils.isNotBlank(followUserId)){
			sqlf.append(" and cbd.follow_user_id = :followUserId ");
			params4.put("followUserId", followUserId);
		}
		sqlf.append(" and c.id in (select customer_id from customer_before_deliver )");
		sqlf.append(" and c.id not in (select customer_id from customer_dynamic_status where ( dynamic_status_type = 'FOLLOWING' or dynamic_status_type = 'CONTRACT_SIGN' ) and REFERUSER_ID !=cbd.FOLLOW_USER_ID and c.RES_ENTRANCE='ON_LINE' ) ");
		sqlf.append(" and c.DEAL_STATUS <> 'INVALID' ");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlf.append(" and c.create_time >= :startDate ");
			params4.put("startDate", startDate + " 0000");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlf.append(" and c.create_time <= :endDate ");
			params4.put("endDate", endDate + " 2359");
		}
		List<Map<Object, Object>> notFollowList=new ArrayList<Map<Object,Object>>();
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sqlf.append(" group by b.id, cbd.follow_user_id ");
			
			sqlfo.append(" select * from ( ");
			sqlfo.append( sqlf );
			sqlfo.append(" ) a where bid = :brenchId ");
			params4.put("brenchId", brenchId);
			
			notFollowList=organizationDao.findMapBySql(sqlfo.toString(), params4);
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			sqlf.append(" GROUP BY bid ");
			notFollowList=organizationDao.findMapBySql(sqlf.toString(), params4);
		}
		
 			
		
		List<InternetUseConditionVo> interList=new ArrayList<InternetUseConditionVo>();

		List<Map<Object, Object>> list=callNumberList.size()>notFollowList.size()?(callNumberList.size()>paidAmountList.size()?callNumberList:paidAmountList):(notFollowList.size()>paidAmountList.size()?notFollowList:paidAmountList);
		
		if(list!=null && list.size()>0){
			for(Map maps:list){
				int a=0;
				InternetUseConditionVo vo=new InternetUseConditionVo();
				String bid=StringUtil.toString(maps.get("BRENCH")).split("_")[0];
				vo.setBrench(StringUtil.toString(maps.get("BRENCH")));
				vo.setUserId(StringUtil.toString(maps.get("follow_user_id")));
				vo.setUserName(StringUtil.toString(maps.get("NAME").toString()));
				vo.setGrounp(StringUtil.toString(maps.get("GROUNP").toString()));
				vo.setBrenchId(bid);		
				
				
				for(Map map:callNumberList){
					//留电量
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					String fbl=map.get("BRENCH").toString().split("_")[0];
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){						
						if(fbl.equals(vo.getBrenchId())){
							vo.setCallNumber(Integer.valueOf(map.get("callNumber").toString()));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid)){
							vo.setCallNumber(Integer.valueOf(map.get("callNumber").toString()));
							break;
						}
					}
					
				}
				
				for(Map map:paidAmountList){
					//合同总金额
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
						if(map.get("BRENCH").toString().split("_")[0].equals(vo.getBrenchId()) ){
							vo.setPaidAmount(Float.valueOf(map.get("paidAmount").toString()));
							vo.setTotalAmount(Float.valueOf(map.get("totalAmount").toString()));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid)){
							vo.setPaidAmount(Float.valueOf(map.get("paidAmount").toString()));
							vo.setTotalAmount(Float.valueOf(map.get("totalAmount").toString()));
							break;
						}
					}
				}
				for(Map map:notFollowList){
					//未跟进
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
						if(map.get("BRENCH").toString().split("_")[0].equals(vo.getBrenchId()) ){
							vo.setNotFollow(Integer.valueOf(map.get("notFollowNumber").toString()));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid) ){
							vo.setNotFollow(Integer.valueOf(map.get("notFollowNumber").toString()));
							break;
						}
					}
				}
				for(Map map:successNumberList){
					//签单量
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
						if(map.get("BRENCH").toString().split("_")[0].equals(vo.getBrenchId()) ){
							vo.setSuccessNumber(Integer.valueOf(map.get("successNumber").toString()));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid)){
							vo.setSuccessNumber(Integer.valueOf(map.get("successNumber").toString()));
							break;
						}
					}
				}
				
				if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
					vo.setUserName("");					
					if(interList!=null && interList.size()>0){
						for(InternetUseConditionVo intenertVo:interList){
							if(intenertVo.getBrenchId().equals(bid)){
								// 统计分析属于同一个分公司
								if(intenertVo.getCallNumber()==null && vo.getCallNumber()==null){
									intenertVo.setCallNumber(0);
								}
								else if(intenertVo.getCallNumber()==null){
									intenertVo.setCallNumber(0+vo.getCallNumber());
								}else if(vo.getCallNumber()==null){
									intenertVo.setCallNumber(intenertVo.getCallNumber()+0);
								}else {
									intenertVo.setCallNumber(intenertVo.getCallNumber()+vo.getCallNumber());
								}
								
								if(intenertVo.getNotFollow()==null && vo.getNotFollow()==null){
									intenertVo.setNotFollow(0);
								}
								else if(intenertVo.getNotFollow()==null){
									intenertVo.setNotFollow(0+vo.getNotFollow());
								}
								else if(vo.getNotFollow()==null) {
									intenertVo.setNotFollow(intenertVo.getNotFollow()+0);
								}else{
									intenertVo.setNotFollow(intenertVo.getNotFollow()+vo.getNotFollow());
								}

								if(intenertVo.getPaidAmount()==null && vo.getPaidAmount()==null ){
									intenertVo.setPaidAmount(Float.valueOf(0));
								}
								else if(intenertVo.getPaidAmount()==null){
									intenertVo.setPaidAmount(0+vo.getPaidAmount());
								}
								else if(vo.getPaidAmount()==null) {
									intenertVo.setPaidAmount(intenertVo.getPaidAmount()+0);
								}
								else {
									intenertVo.setPaidAmount(intenertVo.getPaidAmount()+vo.getPaidAmount());
								}
								
								if(intenertVo.getTotalAmount()==null && vo.getTotalAmount()==null ){
									intenertVo.setTotalAmount(Float.valueOf(0));
								}
								else if(intenertVo.getTotalAmount()==null){
									intenertVo.setTotalAmount(0+vo.getTotalAmount());
								}
								else if(vo.getTotalAmount()==null) {
									intenertVo.setTotalAmount(intenertVo.getTotalAmount()+0);
								}
								else {
									intenertVo.setTotalAmount(intenertVo.getTotalAmount()+vo.getTotalAmount());
								}
								
								if(intenertVo.getSuccessNumber()==null && vo.getSuccessNumber()==null ){
									intenertVo.setSuccessNumber(0);
								}
								else if(intenertVo.getSuccessNumber()==null){
									intenertVo.setSuccessNumber(0+vo.getSuccessNumber());
								}
								else if(vo.getSuccessNumber()==null) {
									intenertVo.setSuccessNumber(intenertVo.getSuccessNumber()+0);
								}else{
									intenertVo.setSuccessNumber(intenertVo.getSuccessNumber()+vo.getSuccessNumber());
								}								
								a=a+1;
								break;
							}
						}
					}		
				}
				
				if(a==0){
					interList.add(vo);
				}			
				}
				
		}
		dp.setDatas(interList);
    	dp.setRowCount(interList.size());
		return dp;
	}
	
	
	public static void main(String[] args) {
		LinkedList<Integer> aIntegers =new LinkedList<>();
		aIntegers.add(0);
		aIntegers.add(0);
		aIntegers.add(0);
		aIntegers.add(0);
		System.out.println(aIntegers.indexOf(Collections.max(aIntegers)));
	}
	/**
	 * 网络资源利用情况  重写新的逻辑  xiaojinwang  20170327
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public DataPackage internetUseConditionList(DataPackage dp, String year, String month, String brenchId, String levelType, String followUserId){
		//当前登录者所在的组织架构
		
		//按照新的逻辑来统计 从userEventRecord实体来统计
		//传入的参数如果是集团，则按集团来统计
		Map<String, Object> params = Maps.newHashMap();
		Map<String, Object> cparams = Maps.newHashMap();
		
		String startDate=year+"-"+month+"-01";
		String endDate=DateTools.getLastDayofMonth(Integer.valueOf(year),Integer.valueOf(month));
		
		StringBuffer orgSql=new StringBuffer();
		orgSql.append(" CASE WHEN g.id is NULL THEN (CASE WHEN b.id is NULL THEN CONCAT(o.id,'_',o.`name`) ELSE CONCAT(b.id,'_',b.`name`) END) ELSE CONCAT(g.id,'_',g.`name`) END as GROUNP, ");
		orgSql.append(" CASE  WHEN g.id is NULL  THEN CONCAT(o.id, '_', o.`name`)  ELSE CONCAT(b.id, '_', b.`name`) END as BRENCH, ");
		orgSql.append(" CASE  WHEN  CONCAT('', g.id) IS NULL THEN CONCAT('',o.id)  ELSE CONCAT('', b.id)  END as bid, ");
		orgSql.append(" CONCAT('',o.id) as oid, ");	
		
				
		//留电量
		StringBuffer sql=new StringBuffer();
		StringBuffer sqlo=new StringBuffer();
		sql.append(" select	 ");
		sql.append(orgSql);
		sql.append(" cds.REFERUSER_ID as follow_user_id ,u.`NAME`, count(c.id) callNumber ");
		sql.append(" from customer c ");
		sql.append(" INNER JOIN customer_dynamic_status cds ON cds.CUSTOMER_ID = c.id ");
		sql.append(getAppendRoleSql("cds"));
		sql.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
		sql.append(" INNER JOIN organization o ON o.id = u.organizationID ");
		sql.append(" LEFT JOIN organization b on o.parentID=b.id ");
		sql.append(" LEFT JOIN organization g on b.parentID=g.id ");
		sql.append(" where r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sql.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.PHONECALL+"'");
		sql.append(" and c.DEAL_STATUS <> 'INVALID' ");
		
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sql.append(" and cds.OCCOUR_TIME >= '"+startDate+" 00:00:00' ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sql.append(" and cds.OCCOUR_TIME <= '"+endDate+" 23:59:59' ");
		}
		if(StringUtils.isNotBlank(followUserId)){
			sql.append(" and cds.REFERUSER_ID= :followUserId ");
            params.put("followUserId", followUserId);		
        }					
		
		List<Map<Object, Object>> callNumberList=new ArrayList<Map<Object,Object>>();		
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sql.append(" group by b.id, cds.REFERUSER_ID ");
			
			sqlo.append(" select * from ( ");
			sqlo.append( sql );
			sqlo.append(" ) a where bid= :brenchId ");
			params.put("brenchId", brenchId);
			callNumberList=organizationDao.findMapBySql(sqlo.toString(),params);	
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			sql.append(" GROUP BY  bid ");
			callNumberList=organizationDao.findMapBySql(sql.toString(),params);	
		}	
	
		
		//合同总金额
		StringBuffer sqlc=new StringBuffer();
		StringBuffer sqlco=new StringBuffer();
		sqlc.append(" select cc.follow_user_id,CONCAT('', cc.bid) AS bid,IFNULL(SUM(cc.PAID_AMOUNT), 0) AS paidAmount,IFNULL(SUM(cc.PLAN_AMOUNT), 0) AS totalAmount,");
		sqlc.append(" cc.CUSTOMER_ID,cc.`NAME`,cc.GROUNP,cc.BRENCH");
		sqlc.append(" from ( ");
		
		sqlc.append(" select sum(aaa.PLAN_AMOUNT) as PLAN_AMOUNT,aaa.ID,aaa.CUSTOMER_ID,aaa.oid,aaa.`NAME`,sum(aaa.PAID_AMOUNT) AS PAID_AMOUNT, aaa.BRENCH,aaa.GROUNP,aaa.bid,aaa.follow_user_id from ( ");
		
		
		sqlc.append(" select  ");
		sqlc.append(orgSql);
		sqlc.append(" c.ID,c.CUSTOMER_ID,u.`NAME`,c.PAID_AMOUNT,cpp.PLAN_AMOUNT,cds.REFERUSER_ID AS follow_user_id ");
		sqlc.append("  from contract c ");
		sqlc.append(" INNER JOIN customer cus on c.CUSTOMER_ID=cus.id ");		
		sqlc.append(" INNER JOIN customer_dynamic_status cds ON cds.CUSTOMER_ID = cus.id ");
		sqlc.append(getAppendRoleSql("cds"));
		sqlc.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
		sqlc.append(" INNER JOIN organization o ON o.id = u.organizationID ");
		sqlc.append(" LEFT JOIN organization b on o.parentID=b.id ");
		sqlc.append(" LEFT JOIN organization g on b.parentID=g.id ");
		sqlc.append(" INNER JOIN ( select sum(cp.PLAN_AMOUNT) PLAN_AMOUNT, cp.CONTRACT_ID from contract_product cp  ");
		sqlc.append(" where cp.CONTRACT_ID in (  ");
		sqlc.append(" select DISTINCT(c.id) from contract c  ");
		sqlc.append(" LEFT JOIN funds_change_history f on f.contract_id=c.id ");
		sqlc.append(" INNER JOIN customer_dynamic_status cds on cds.CUSTOMER_ID=c.CUSTOMER_ID ");
		sqlc.append(getAppendRoleSql("cds"));
		sqlc.append(" where 1=1 ");
		sqlc.append(" and r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sqlc.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.CONTRACT_SIGN+"'");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlc.append(" and cds.OCCOUR_TIME >= :startDate ");
			cparams.put("startDate", startDate+" 00:00:00 ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and cds.OCCOUR_TIME <= :endDate ");
			cparams.put("endDate", endDate+" 23:59:59 ");
		}
		// 查询收款日期在客户创建后的30天内的合同金额  收款日期<=跟进日期+30天
//		if(endDate != null &&StringUtils.isNotBlank(endDate)){
//			sqlc.append(" and f.TRANSACTION_TIME <=date_sub(cus.create_time, interval -30 day) ");
//		}
		sqlc.append(" ) GROUP BY cp.CONTRACT_ID) cpp on cpp.CONTRACT_ID=c.ID  ");
		sqlc.append(" where r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sqlc.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.CONTRACT_SIGN+"'");
		
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlc.append(" and cds.OCCOUR_TIME >= :startDate ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and cds.OCCOUR_TIME <= :endDate ");
		}
		
		if(StringUtils.isNotBlank(followUserId)){
			sqlc.append(" and cds.REFERUSER_ID= :followUserId ");
			cparams.put("followUserId", followUserId);
		}
		sqlc.append(" GROUP BY c.ID ) aaa GROUP BY aaa.CUSTOMER_ID )  ");
		List<Map<Object, Object>> paidAmountList=new ArrayList<Map<Object,Object>>();
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sqlc.append(" cc GROUP BY cc.FOLLOW_USER_ID,cc.bid ");
			
			sqlco.append(" select * from ( ");
			sqlco.append( sqlc );
			sqlco.append(" ) xxx where xxx.bid= :brenchId ");
			cparams.put("brenchId", brenchId);
			paidAmountList=organizationDao.findMapBySql(sqlco.toString(),cparams);
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			sqlc.append(" cc GROUP BY cc.bid ");
			paidAmountList=organizationDao.findMapBySql(sqlc.toString(),cparams);
		}
		
		Map<String, Object> sparams = Maps.newHashMap();
		//签单量			  
		StringBuffer sqls=new StringBuffer();
		StringBuffer sqlso=new StringBuffer();
		sqls.append(" select con.follow_user_id,con.create_time,CONCAT('', con.bid) as bid, COUNT(DISTINCT(con.customer_id)) as successNumber,con.CUSTOMER_ID,NAME,GROUNP,BRENCH ");
		sqls.append(" from ( ");
		sqls.append(" select  ");
		sqls.append(orgSql);
		sqls.append(" u.`NAME`,cds.CUSTOMER_ID,cds.REFERUSER_ID as follow_user_id ,cds.OCCOUR_TIME as create_time,cds.OCCOUR_TIME as follow_time ");
		sqls.append(" from customer_dynamic_status cds  ");
		sqls.append(getAppendRoleSql("cds"));
		sqls.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
		sqls.append(" INNER JOIN organization o ON o.id = u.organizationID ");
		sqls.append(" LEFT JOIN organization b on o.parentID = b.id ");
		sqls.append(" LEFT JOIN organization g on b.parentID=g.id ");
		sqls.append(" where r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sqls.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.CONTRACT_SIGN+"'");
				
		if(StringUtils.isNotBlank(followUserId)){
			sqls.append(" and cds.REFERUSER_ID= :followUserId  ");
			sparams.put("followUserId", followUserId);
		}
		
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqls.append(" and cds.OCCOUR_TIME >= :startDate ");
			sparams.put("startDate", startDate+" 00:00:00 ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqls.append(" and cds.OCCOUR_TIME <= :endDate  ");
			sparams.put("endDate", endDate+" 23:59:59 ");
		}
		//40天内签单量
//		sqls.append(" and c.create_time <=date_sub(uer.create_time, interval -30 day) ");
//		sqls.append(" and cbd.FOLLOW_USER_ID in ");
//		 GROUP BY c.CUSTOMER_ID
//		sqls.append(" ( SELECT USER_ID from user_dept_job  where JOB_ID in ( 'USE0000000114','USE0000000115','USE0000000116')  )  ");
		sqls.append("  ) con ");		
		
		List<Map<Object, Object>> successNumberList=new ArrayList<Map<Object,Object>>();
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sqls.append(" GROUP BY  con.FOLLOW_USER_ID,con.bid ");
			
			sqlso.append(" select * from ( ");
			sqlso.append( sqls );
			sqlso.append(" ) a where bid= :brenchId ");
			sparams.put("brenchId", brenchId);
			successNumberList=organizationDao.findMapBySql(sqlso.toString(),sparams);
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			 sqls.append(" GROUP BY bid ");
			 successNumberList=organizationDao.findMapBySql(sqls.toString(),sparams);
		}

		Map<String, Object> fparams = Maps.newHashMap();
		//未跟进 =分配校区主任-咨询师获取的
		StringBuffer sqlf=new StringBuffer();
		StringBuffer sqlfo=new StringBuffer();
		sqlf.append(" select ");
		sqlf.append(orgSql);
		sqlf.append(" cds.REFERUSER_ID as follow_user_id,u.`NAME`, count(c.id) notFollowNumber ");
		sqlf.append(" from customer c ");
		sqlf.append(" INNER JOIN customer_dynamic_status cds ON cds.CUSTOMER_ID = c.id ");
		sqlf.append(getAppendRoleSql("cds"));
		sqlf.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
		sqlf.append(" INNER JOIN organization o ON o.id = u.organizationID ");
		sqlf.append(" LEFT JOIN organization b on o.parentID=b.id ");
		sqlf.append(" LEFT JOIN organization g on b.parentID=g.id ");
		sqlf.append(" where r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sqlf.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.APPOINTMENT_ONLINE+"'");
		
		sqlf.append(" and c.id not in ( SELECT cdss.CUSTOMER_ID FROM customer_dynamic_status cdss  ");
		sqlf.append(getAppendRoleSql("cdss"));
		sqlf.append(" and rr.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sqlf.append(" and cdss.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.COUNSELOR_NETWORK+"'");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlf.append(" and cdss.OCCOUR_TIME >='"+startDate+" 00:00:00' ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlf.append(" and cdss.OCCOUR_TIME <='"+endDate+" 23:59:59' ");
		}
		sqlf.append(" ) ");
				
		if(StringUtils.isNotBlank(followUserId)){
			sqlf.append(" and cds.REFERUSER_ID=:followUserId ");
			fparams.put("followUserId", followUserId);
		}

		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlf.append(" and cds.OCCOUR_TIME >= :startDate ");
			fparams.put("startDate", startDate+" 00:00:00 ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlf.append(" and cds.OCCOUR_TIME <= :endDate ");
			fparams.put("endDate", endDate+" 23:59:59 ");
		}
		List<Map<Object, Object>> notFollowList=new ArrayList<Map<Object,Object>>();
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType) && StringUtils.isNotBlank(brenchId)) {
			sqlf.append(" group by b.id, cds.REFERUSER_ID ");
			
			sqlfo.append(" select * from ( ");
			sqlfo.append( sqlf );
			sqlfo.append(" ) a where bid= :brenchId ");
			fparams.put("brenchId", brenchId);
			notFollowList=organizationDao.findMapBySql(sqlfo.toString(),fparams);
		}else if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
			sqlf.append(" GROUP BY bid ");
			notFollowList=organizationDao.findMapBySql(sqlf.toString(),fparams);
		}
				
		
		List<InternetUseConditionVo> interList=new ArrayList<InternetUseConditionVo>();

		LinkedList<Integer> linkedList = new LinkedList<>();
		linkedList.add(callNumberList.size());
		linkedList.add(notFollowList.size());
		linkedList.add(paidAmountList.size());
		linkedList.add(successNumberList.size());

		Integer index =linkedList.indexOf(Collections.max(linkedList));
		List<Map<Object, Object>> list = null;
        if(index==0){
        	list = callNumberList;
        }else if(index==1){
        	list = notFollowList;
        }else if(index==2){        	
    	    list = paidAmountList;
        }else{ 
        	list = successNumberList;
        }

		
		if(list!=null && list.size()>0){
			for(Map maps:list){
				int a=0;
				InternetUseConditionVo vo=new InternetUseConditionVo();
				String bid=maps.get("BRENCH").toString().split("_")[0];
				vo.setBrench(maps.get("BRENCH").toString());
				vo.setUserId(maps.get("follow_user_id").toString());
				vo.setUserName(maps.get("NAME").toString());
				vo.setGrounp(maps.get("GROUNP").toString());
				vo.setBrenchId(bid);		
				
				
				for(Map map:callNumberList){
					//留电量
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					String fbl=map.get("BRENCH").toString().split("_")[0];
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){						
						if(fbl.equals(vo.getBrenchId())){
							vo.setCallNumber(Integer.valueOf(map.get("callNumber").toString()));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid)){
							vo.setCallNumber(Integer.valueOf(map.get("callNumber").toString()));
							break;
						}
					}
					
				}
				
				for(Map map:paidAmountList){
					//合同总金额
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
						if(map.get("BRENCH").toString().split("_")[0].equals(vo.getBrenchId()) ){
							vo.setPaidAmount(Float.valueOf(map.get("paidAmount").toString()));
							vo.setTotalAmount(Float.valueOf(map.get("totalAmount").toString()));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid)){
							vo.setPaidAmount(Float.valueOf(map.get("paidAmount").toString()));
							vo.setTotalAmount(Float.valueOf(map.get("totalAmount").toString()));
							break;
						}
					}
				}
				for(Map map:notFollowList){
					//未跟进=分配校区营运主任-分配咨询师
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
						if(map.get("BRENCH").toString().split("_")[0].equals(vo.getBrenchId()) ){
							vo.setNotFollow(Integer.valueOf(map.get("notFollowNumber")!=null?map.get("notFollowNumber").toString():"0"));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid) ){
							vo.setNotFollow(Integer.valueOf(map.get("notFollowNumber")!=null?map.get("notFollowNumber").toString():"0"));
							break;
						}
					}
				}
//				for(Map map:ditribute_ZXS){
//					//未跟进=分配校区营运主任-分配咨询师
//					if(map.get("bid")==null){
//						map.put("bid",map.get("oid"));
//					}
//					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
//						if(map.get("BRENCH").toString().split("_")[0].equals(vo.getBrenchId()) ){
//							vo.setDistributeZXS(Integer.valueOf(map.get("ditribute_ZXS")!=null?map.get("ditribute_ZXS").toString():"0"));
//							break;
//						}
//					}else{
//						if(map.containsValue(vo.getUserId()) && map.containsValue(bid) ){
//							vo.setDistributeZXS(Integer.valueOf(map.get("ditribute_ZXS")!=null?map.get("ditribute_ZXS").toString():"0"));
//							break;
//						}
//					}
//				}
//				if(vo.getDistributeCampus()==null)vo.setDistributeCampus(0);
//				if(vo.getDistributeZXS()==null)vo.setDistributeZXS(0);
//				vo.setNotFollow(vo.getDistributeCampus()-vo.getDistributeZXS());
				
				
				for(Map map:successNumberList){
					//签单量
					if(map.get("bid")==null){
						map.put("bid",map.get("oid"));
					}
					if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
						if(map.get("BRENCH").toString().split("_")[0].equals(vo.getBrenchId()) ){
							vo.setSuccessNumber(Integer.valueOf(map.get("successNumber").toString()));
							break;
						}
					}else{
						if(map.containsValue(vo.getUserId()) && map.containsValue(bid)){
							vo.setSuccessNumber(Integer.valueOf(map.get("successNumber").toString()));
							break;
						}
					}
				}
				
				if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
					vo.setUserName("");					
					if(interList!=null && interList.size()>0){
						for(InternetUseConditionVo intenertVo:interList){
							if(intenertVo.getBrenchId().equals(bid)){
								// 统计分析属于同一个分公司
								if(intenertVo.getCallNumber()==null && vo.getCallNumber()==null){
									intenertVo.setCallNumber(0);
								}
								else if(intenertVo.getCallNumber()==null){
									intenertVo.setCallNumber(0+vo.getCallNumber());
								}else if(vo.getCallNumber()==null){
									intenertVo.setCallNumber(intenertVo.getCallNumber()+0);
								}else {
									intenertVo.setCallNumber(intenertVo.getCallNumber()+vo.getCallNumber());
								}
								
								if(intenertVo.getNotFollow()==null && vo.getNotFollow()==null){
									intenertVo.setNotFollow(0);
								}
								else if(intenertVo.getNotFollow()==null){
									intenertVo.setNotFollow(0+vo.getNotFollow());
								}
								else if(vo.getNotFollow()==null) {
									intenertVo.setNotFollow(intenertVo.getNotFollow()+0);
								}else{
									intenertVo.setNotFollow(intenertVo.getNotFollow()+vo.getNotFollow());
								}

								if(intenertVo.getPaidAmount()==null && vo.getPaidAmount()==null ){
									intenertVo.setPaidAmount(Float.valueOf(0));
								}
								else if(intenertVo.getPaidAmount()==null){
									intenertVo.setPaidAmount(0+vo.getPaidAmount());
								}
								else if(vo.getPaidAmount()==null) {
									intenertVo.setPaidAmount(intenertVo.getPaidAmount()+0);
								}
								else {
									intenertVo.setPaidAmount(intenertVo.getPaidAmount()+vo.getPaidAmount());
								}
								
								if(intenertVo.getTotalAmount()==null && vo.getTotalAmount()==null ){
									intenertVo.setTotalAmount(Float.valueOf(0));
								}
								else if(intenertVo.getTotalAmount()==null){
									intenertVo.setTotalAmount(0+vo.getTotalAmount());
								}
								else if(vo.getTotalAmount()==null) {
									intenertVo.setTotalAmount(intenertVo.getTotalAmount()+0);
								}
								else {
									intenertVo.setTotalAmount(intenertVo.getTotalAmount()+vo.getTotalAmount());
								}
								
								if(intenertVo.getSuccessNumber()==null && vo.getSuccessNumber()==null ){
									intenertVo.setSuccessNumber(0);
								}
								else if(intenertVo.getSuccessNumber()==null){
									intenertVo.setSuccessNumber(0+vo.getSuccessNumber());
								}
								else if(vo.getSuccessNumber()==null) {
									intenertVo.setSuccessNumber(intenertVo.getSuccessNumber()+0);
								}else{
									intenertVo.setSuccessNumber(intenertVo.getSuccessNumber()+vo.getSuccessNumber());
								}								
								a=a+1;
								break;
							}
						}
					}		
				}
				
				if(a==0){
					interList.add(vo);
				}			
				}
				
		}
		dp.setDatas(interList);
    	dp.setRowCount(interList.size());
		return dp;
	}

	/**
	 * 获取新旧组织架构的角色SQL
	 * @return
	 */
	public String getAppendRoleSql (String alias){
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return " INNER JOIN user_organization_role ur on ur.user_ID = "+alias+".REFERUSER_ID  INNER JOIN role r on ur.role_ID = r.id  ";
		}else{
			return " INNER JOIN user_role ur on ur.userID = "+alias+".REFERUSER_ID  INNER JOIN role r on ur.roleID = r.id ";
		}
	}
	
	
	/**
	 * TMK资源利用情况
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public DataPackage tmkUseConditionList(DataPackage dp,String startDate,String endDate,String levelType,String brenchId){
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> params2 = new HashMap<String, Object>();
		Map<String, Object> params3 = new HashMap<String, Object>();
		Map<String, Object> params4 = new HashMap<String, Object>();
		if(endDate==null || StringUtils.isBlank(endDate)){
			endDate=DateTools.getCurrentDate();
		}
		StringBuffer sqlr=new StringBuffer();
		Organization org=userService.getCurrentLoginUserOrganization();
		
		// tmk角色对应到的部门的统计归属分公司
		sqlr.append(" select  CONCAT(g.id, '_', g.name) GROUNP, CONCAT(b.id, '_', b.name) BRENCH, ");

		sqlr.append(" u.user_id,u.name from `user` u ");
		sqlr.append("  INNER JOIN user_dept_job udj ON u.USER_ID=udj.USER_ID ");
		sqlr.append("  INNER JOIN organization o on udj.dept_id=o.id  ");
		sqlr.append(" LEFT JOIN organization b on b.id=o.belong ");
		sqlr.append(" LEFT JOIN organization g ON b.parentID=g.id ");
		sqlr.append(" where udj.JOB_ID in ('USE0000000031','USE0000000032','USE0000000033')  ");
		sqlr.append(" and b.orgType='BRENCH' ");
		if(org.getOrgType()==OrganizationType.DEPARTMENT){
			Organization parentOrg=organizationDao.findById(org.getParentId());
			sqlr.append(" and o.orgLevel like '"+parentOrg.getOrgLevel()+"%' ");
		}else{
			sqlr.append(" and o.orgLevel like '"+org.getOrgLevel()+"%' ");
		}
		 
		if (BasicOperationQueryLevelType.BRENCH.toString().equals(levelType)) {
			sqlr.append(" and b.id = :brenchId ");
			params.put("brenchId", brenchId);
			params2.put("brenchId", brenchId);
			params3.put("brenchId", brenchId);
			params4.put("brenchId", brenchId);
		}
		
		sqlr.append(" GROUP BY b.id,u.USER_ID ");

		List<Map<Object, Object>> list=organizationDao.findMapBySql(sqlr.toString(), params);
	
		//留电量

		StringBuffer sql=new StringBuffer();
		sql.append(" select COUNT(1) as callNumber,b.user_id from ( ");
		sql.append(" select cbd.*,uu.user_id from customer c  ");
		sql.append(" INNER JOIN customer_before_deliver cbd on c.id=cbd.CUSTOMER_ID  ");
		sql.append(" INNER JOIN ( ");
		sql.append(sqlr);
		sql.append(" ) uu on cbd.FOLLOW_USER_ID=uu.user_id ");
		sql.append("  where c.RES_ENTRANCE ='TMK' ");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sql.append(" and c.create_time >= :startDate ");
			params2.put("startDate", startDate + " 0000");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sql.append(" and c.create_time <= :endDate ");
			params2.put("endDate", endDate + " 2359");
		}
		sql.append(" group by cbd.FOLLOW_USER_ID,cbd.CUSTOMER_ID )b ");
		sql.append("  GROUP BY user_id ");
		List<Map<Object, Object>> callNumberList=organizationDao.findMapBySql(sql.toString(), params2);				 
		
		//签单量 ，合同总金额
		//Map<String, Object> params3 = new HashMap<String, Object>();
		StringBuffer sqlc=new StringBuffer();
		sqlc.append(" select COUNT(1) as successNumber,IFNULL(SUM(c.PAID_AMOUNT),0) as paidAmount,cbd.FOLLOW_USER_ID  from contract c ");
		sqlc.append(" INNER JOIN customer_before_deliver cbd  on cbd.CUSTOMER_ID=c.CUSTOMER_ID  ");
		sqlc.append(" INNER JOIN ( ");
		sqlc.append(sqlr);
		sqlc.append(" ) uu on uu.user_id=cbd.FOLLOW_USER_ID");
		sqlc.append(" where 1=1 ");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlc.append(" and c.create_time >= :startDate ");
			params3.put("startDate", startDate + " 0000");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and c.create_time <= :endDate ");
			params3.put("endDate", endDate + " 2359");
		}
		// 查询结束时间的前40天跟进量
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and cbd.FOLLOW_TIME >=date_sub(:endDate2, interval 40 day) ");
			sqlc.append(" and cbd.FOLLOW_TIME <= :endDate3 ");
			params3.put("endDate2", endDate);
			params3.put("endDate3", endDate + " 2359");
		}
		sqlc.append(" GROUP BY cbd.FOLLOW_USER_ID ");
		
		List<Map<Object, Object>> paidAmountList=organizationDao.findMapBySql(sqlc.toString(), params3);		
		
		//未跟进
		//Map<String, Object> params4 = new HashMap<String, Object>();
		StringBuffer sqlf=new StringBuffer();
		sqlf.append(" select COUNT(1) as notFollowNumber,follow_user_id from  ( ");
		sqlf.append(" select cbd.* from customer_before_deliver cbd ");
		sqlf.append(" INNER JOIN customer c on cbd.CUSTOMER_ID=c.id	");
		sqlf.append(" INNER JOIN (");
		sqlf.append(sqlr);
		sqlf.append(" ) uu on cbd.FOLLOW_USER_ID=uu.user_id");
		sqlf.append(" where c.id in (select customer_id from customer_before_deliver )");
		sqlf.append(" and c.id not in (select customer_id from customer_dynamic_status where dynamic_status_type = 'FOLLOWING ' and REFERUSER_ID !=cbd.FOLLOW_USER_ID and c.RES_ENTRANCE='TMK' ) ");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlf.append(" and c.create_time >= :startDate ");
			params4.put("startDate", startDate + " 0000");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlf.append(" and c.create_time <= :endDate ");
			params4.put("endDate", endDate + " 2359");
		}
		sqlf.append("  group by cbd.FOLLOW_USER_ID,cbd.CUSTOMER_ID ) a");
		sqlf.append("  group by a.follow_user_id ");
		List<Map<Object, Object>> notFollowList=organizationDao.findMapBySql(sqlf.toString(), params4);	
		
		List<InternetUseConditionVo> interList=new ArrayList<InternetUseConditionVo>();
		
		if(list!=null && list.size()>0){
			for(Map maps:list){
				int a=0;
				InternetUseConditionVo vo=new InternetUseConditionVo();
				String bid=StringUtil.toString(maps.get("BRENCH")).split("_")[0];
				vo.setBrench(StringUtil.toString(maps.get("BRENCH")));
				vo.setUserId(StringUtil.toString(maps.get("user_id")));
				vo.setUserName(StringUtil.toString(maps.get("name")));
				vo.setGrounp(StringUtil.toString(maps.get("GROUNP")));
				vo.setBrenchId(bid);				
				
				for(Map map:callNumberList){
					//留电量
					if(map.containsValue(vo.getUserId())){
						vo.setCallNumber(Integer.valueOf(StringUtil.toString(map.get("callNumber"))));
						break;
					}
				}
				for(Map map:paidAmountList){
					//合同总金额
					if(map.containsValue(vo.getUserId())){
						vo.setPaidAmount(Float.valueOf(StringUtil.toString(map.get("paidAmount"))));
						vo.setSuccessNumber(Integer.valueOf(StringUtil.toString(map.get("successNumber"))));
						break;
					}
				}
				for(Map map:notFollowList){
					//未跟进
					if(map.containsValue(vo.getUserId())){
						vo.setNotFollow(Integer.valueOf(StringUtil.toString(map.get("notFollowNumber"))));
						break;
					}
				}
				
				if(BasicOperationQueryLevelType.GROUNP.toString().equals(levelType)){
					vo.setUserName("");					
					if(interList!=null && interList.size()>0){
						for(InternetUseConditionVo tmkVo:interList){
							if(tmkVo.getBrenchId().equals(bid)){
								// 统计分析属于同一个分公司
								if(tmkVo.getCallNumber()==null && vo.getCallNumber()==null){
									tmkVo.setCallNumber(0);
								}
								else if(tmkVo.getCallNumber()==null){
									tmkVo.setCallNumber(0+vo.getCallNumber());
								}else if(vo.getCallNumber()==null){
									tmkVo.setCallNumber(tmkVo.getCallNumber()+0);
								}else {
									tmkVo.setCallNumber(tmkVo.getCallNumber()+vo.getCallNumber());
								}
								
								if(tmkVo.getNotFollow()==null && vo.getNotFollow()==null){
									tmkVo.setNotFollow(0);
								}
								else if(tmkVo.getNotFollow()==null){
									tmkVo.setNotFollow(0+vo.getNotFollow());
								}
								else if(vo.getNotFollow()==null) {
									tmkVo.setNotFollow(tmkVo.getNotFollow()+0);
								}else{
									tmkVo.setNotFollow(tmkVo.getNotFollow()+vo.getNotFollow());
								}

								if(tmkVo.getPaidAmount()==null && vo.getPaidAmount()==null ){
									tmkVo.setPaidAmount(Float.valueOf(0));
								}
								else if(tmkVo.getPaidAmount()==null){
									tmkVo.setPaidAmount(0+vo.getPaidAmount());
								}
								else if(vo.getPaidAmount()==null) {
									tmkVo.setPaidAmount(tmkVo.getPaidAmount()+0);
								}
								else {
									tmkVo.setPaidAmount(tmkVo.getPaidAmount()+vo.getPaidAmount());
								}
								
								if(tmkVo.getSuccessNumber()==null && vo.getSuccessNumber()==null ){
									tmkVo.setSuccessNumber(0);
								}
								else if(tmkVo.getSuccessNumber()==null){
									tmkVo.setSuccessNumber(0+vo.getSuccessNumber());
								}
								else if(vo.getSuccessNumber()==null) {
									tmkVo.setSuccessNumber(tmkVo.getSuccessNumber()+0);
								}else{
									tmkVo.setSuccessNumber(tmkVo.getSuccessNumber()+vo.getSuccessNumber());
								}								
								a=a+1;
								break;
							}
						}
					}		
				}
				
				if(a==0){
					interList.add(vo);
				}			
				}
				
		}	
		
		dp.setDatas(interList);
    	dp.setRowCount(interList.size());
		return dp;
	}
	
	//根据留电量，未跟进，签单量，查看对应的客户详情
	@Override
	public DataPackage resourceUseConditionInfo(DataPackage dataPackage,String followUserId,String year,String month,String numType,String brenchId){
		Map<String, Object> sqlcParams = new HashMap<String, Object>();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		Map<String, Object> sqloParams = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		StringBuffer sqlc=new StringBuffer();
		StringBuffer sqlo=new StringBuffer();
		
//		Organization org=null;
//		Boolean b=userService.getCurrentLoginUserOrganization().getOrgType()==OrganizationType.DEPARTMENT;
//		if(b){
//			String id=userService.getCurrentLoginUserOrganization().getParentId();
//			org=organizationDao.findById(id);
//		}else{
//			org=userService.getCurrentLoginUserOrganization();
//		}
		String startDate=year+"-"+month+"-01";
		String endDate=DateTools.getLastDayofMonth(Integer.valueOf(year),Integer.valueOf(month));	
						
		sqlc.append(" select sum(aaa.PLAN_AMOUNT) AS totalAmount,sum(aaa.PAID_AMOUNT) AS paidAmount,aaa.* from ( ");
		sqlc.append(" select c.PAID_AMOUNT as PAID_AMOUNT,cpp.PLAN_AMOUNT,cds.REFERUSER_ID AS follow_user_id,c.ID as contractID,");
		sqlc.append(" c.customer_id,cus.* from contract c ");
		sqlc.append(" INNER JOIN customer cus ON c.CUSTOMER_ID = cus.id ");
		sqlc.append(" INNER JOIN customer_dynamic_status cds ON cus.id = cds.CUSTOMER_ID ");
		sqlc.append(getAppendRoleSql("cds"));
		sqlc.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
		sqlc.append(" INNER JOIN organization o ON o.id = u.organizationID ");
		sqlc.append(" LEFT JOIN organization b on o.parentID=b.id ");
		sqlc.append(" LEFT JOIN organization g on b.parentID=g.id ");
		
		sqlc.append(" INNER JOIN ( select sum(cp.PLAN_AMOUNT) PLAN_AMOUNT, cp.CONTRACT_ID from contract_product cp ");
		sqlc.append(" where cp.CONTRACT_ID in (  ");
		sqlc.append(" select DISTINCT(c.id) from contract c  ");
		sqlc.append(" INNER JOIN customer_dynamic_status cds ON c.CUSTOMER_ID = cds.CUSTOMER_ID ");
		sqlc.append(getAppendRoleSql("cds"));
		sqlc.append(" where 1=1 ");		
		sqlc.append(" and r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sqlc.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.CONTRACT_SIGN+"'");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlc.append(" and cds.OCCOUR_TIME >= :startDate ");
			sqlcParams.put("startDate", startDate+" 00:00:00 ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and cds.OCCOUR_TIME <= :endDate ");
			sqlcParams.put("endDate", endDate+" 23:59:59 ");
		}
//		// 查询收款日期在跟进日期的后30天内签单量和合同金额
//		if(endDate != null &&StringUtils.isNotBlank(endDate)){
//			sqlc.append(" and f.TRANSACTION_TIME <=date_sub(cbd.FOLLOW_TIME, interval -30 day) ");
//		}
		if(followUserId.split("_")[0]!=null && StringUtils.isNotBlank(followUserId.split("_")[0])){
			Organization brench=organizationDao.findById(followUserId.split("_")[0]);
			if(brench!=null){			
				sqlc.append(" ) "); 
				sqlc.append(" GROUP BY cp.CONTRACT_ID ) cpp on c.id=cpp.CONTRACT_ID   ");
//				sqlc.append(" and  o.id in (select id from  organization o  where o.parentID = (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' ) )  ");
				sqlc.append(" and  o.id in (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )   ");
			}else{
				sqlc.append(" and  cds.REFERUSER_ID = :followUserId ");
				sqlcParams.put("followUserId", followUserId);
				sqlc.append(" ) "); 
				sqlc.append(" GROUP BY cp.CONTRACT_ID ) cpp on c.id=cpp.CONTRACT_ID   ");
			}
		}	
		sqlc.append(" where 1=1 ");		
		sqlc.append(" and r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
		sqlc.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.CONTRACT_SIGN+"'");
		if(startDate != null && StringUtils.isNotBlank(startDate)){
			sqlc.append(" and cds.OCCOUR_TIME >= :startDate ");
		}
		if(endDate != null &&StringUtils.isNotBlank(endDate)){
			sqlc.append(" and cds.OCCOUR_TIME <= :endDate ");
		}
		
		sqlc.append(" GROUP BY c.ID ) aaa  ");
		sqlc.append(" GROUP BY aaa.CUSTOMER_ID  ");
	
		//留电量
		if(numType.equals("callNum")){
			sql.append(" select CASE when CONCAT(b.id,'_', b.name) IS NULL THEN CONCAT(o.id,'_',o.name) ELSE ( CASE  WHEN CONCAT(g.id,'_',g.name) IS NULL THEN CONCAT(b.id,'_', b.name) ELSE CONCAT(g.id,'_',g.name)  END ) END as  GROUNP, ");
			sql.append("  CASE when CONCAT(b.id,'_', b.name) IS NULL THEN CONCAT(o.id,'_',o.name) ELSE CONCAT(b.id,'_', b.name) END as BRENCH, ");			
			sql.append("  CASE  WHEN b.id is NULL then CONCAT('', o.id)  ELSE CONCAT('', b.id)  end as bid , cds.REFERUSER_ID as follow_user_id, ");						
			sql.append("  c.* from customer c  ");
			sql.append(" INNER JOIN customer_dynamic_status cds ON cds.CUSTOMER_ID = c.id  ");
			sql.append(getAppendRoleSql("cds"));
			sql.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
			sql.append(" INNER JOIN organization o ON o.id = u.organizationID ");
			sql.append(" LEFT JOIN organization b on o.parentID=b.id ");
			sql.append(" LEFT JOIN organization g on b.parentID=g.id ");
			sql.append(" where r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
			sql.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.PHONECALL+"'");
			sql.append(" and c.DEAL_STATUS <> 'INVALID' ");
			if(startDate != null && StringUtils.isNotBlank(startDate)){
				sql.append(" and cds.OCCOUR_TIME >= :startDate  ");
				sqlParams.put("startDate", startDate+" 00:00:00 ");
				sqloParams.put("startDate", startDate+" 00:00:00 ");
			}
			if(endDate != null &&StringUtils.isNotBlank(endDate)){
				sql.append(" and cds.OCCOUR_TIME <= :endDate ");
				sqlParams.put("endDate", endDate+" 23:59:59 ");
				sqloParams.put("endDate", endDate+" 23:59:59 ");
			}
			if(followUserId.split("_")[0]!=null && StringUtils.isNotBlank(followUserId.split("_")[0])){
				Organization brench=organizationDao.findById(followUserId.split("_")[0]);
				if(brench!=null){
//					StringBuffer levelSql=new StringBuffer();
//					levelSql.append("(select id from organization where orgLevel   ");
//					if(org.getOrgType()==OrganizationType.GROUNP){
//						levelSql.append(" like '"+org.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )  ");
//					}else{
//						levelSql.append(" = '"+org.getOrgLevel()+"' ) ");
//					}
//					sql.append(" and  o.id in (select id from  organization o  where ");
//					sql.append(" and  o.id in (select id from  organization o  where o.parentID = (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' ) )  ");
					if(brench.getOrgType() == OrganizationType.GROUNP){
						sql.append(" and  o.id in (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )   ");
					}else{
						sql.append(" and  b.id in (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )   ");
					}
					
//					if(brench.getOrgType()==OrganizationType.GROUNP){
//						sql.append(" o.id = " + levelSql +")");	
//					}else{
//						sql.append(" o.parentID = " + levelSql);
//						sql.append(" or o.id in " + levelSql + ")" );
//					}
				}else{
					sqloParams.put("followUserId", followUserId);
					sqloParams.put("brenchId", brenchId);
					sql.append(" and cds.REFERUSER_ID= :followUserId ");
					sqlo.append("select * from ( ");
					sqlo.append( sql );
					sqlo.append(" ) a where bid= :brenchId and follow_user_id= :followUserId ") ;
				}
			}	
		
		//未跟进
	    //由于未跟进是通过 已分配-以获取的来计算得到的
		}else if(numType.equals("notFollowNum")){
			sql.append(" select CASE when CONCAT(b.id,'_', b.name) IS NULL THEN CONCAT(o.id,'_',o.name) ELSE ( CASE  WHEN CONCAT(g.id,'_',g.name) IS NULL THEN CONCAT(b.id,'_', b.name) ELSE CONCAT(g.id,'_',g.name)  END ) END as  GROUNP, ");
			sql.append("  CASE when CONCAT(b.id,'_', b.name) IS NULL THEN CONCAT(o.id,'_',o.name) ELSE CONCAT(b.id,'_', b.name) END as BRENCH, ");			
			sql.append("  CASE  WHEN b.id is NULL then o.id  ELSE b.id  end as bid , cds.REFERUSER_ID AS follow_user_id, ");						
			sql.append("  c.* from customer_dynamic_status cds ");
			sql.append(" INNER JOIN customer c on cds.CUSTOMER_ID = c.id ");
			sql.append(getAppendRoleSql("cds"));
			sql.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
            sql.append(" INNER JOIN organization o ON o.id = u.organizationID ");
            sql.append(" LEFT JOIN organization b ON o.parentID = b.id ");
            sql.append(" LEFT JOIN organization g ON b.parentID = g.id");
    		sql.append(" where r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
    		sql.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.APPOINTMENT_ONLINE+"'");
    		if(startDate != null && StringUtils.isNotBlank(startDate)){
    			sql.append(" and cds.OCCOUR_TIME >= :startDate ");
    			sqlParams.put("startDate", startDate+" 00:00:00 ");
    			sqloParams.put("startDate", startDate+" 00:00:00 ");
    		}
    		if(endDate != null &&StringUtils.isNotBlank(endDate)){
    			sql.append(" and cds.OCCOUR_TIME <= :endDate ");
    			sqlParams.put("endDate", endDate+" 23:59:59 ");
    			sqloParams.put("endDate", endDate+" 23:59:59 ");
    		}
			sql.append(" and c.id not in ( SELECT cdss.CUSTOMER_ID FROM customer_dynamic_status cdss ");
    		sql.append(getAppendRoleSql("cdss"));
			sql.append("  and rr.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
    		sql.append(" and cdss.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.COUNSELOR_NETWORK+"'");
			if(startDate != null && StringUtils.isNotBlank(startDate)){
				sql.append(" and cdss.OCCOUR_TIME >= :startDate ");
			}
			if(endDate != null &&StringUtils.isNotBlank(endDate)){
				sql.append(" and cdss.OCCOUR_TIME <= :endDate ");
			}
			sql.append(" ) ");
			if(followUserId.split("_")[0]!=null && StringUtils.isNotBlank(followUserId.split("_")[0])){
				Organization brench=organizationDao.findById(followUserId.split("_")[0]);
				if(brench!=null){
//					StringBuffer levelSql=new StringBuffer();
//					levelSql.append("(select id from organization where orgLevel   ");
//					if(org.getOrgType()==OrganizationType.GROUNP){
//						levelSql.append(" like '"+org.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )  ");
//					}else{
//						levelSql.append(" = '"+org.getOrgLevel()+"' ) ");
//					}
//					sql.append(" and  c.bl_school in (select id from  organization o  where ");
//					sql.append(" and  o.id in (select id from  organization o  where o.parentID = (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' ) )  ");
					if(brench.getOrgType() == OrganizationType.GROUNP){
						sql.append(" and  o.id in (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )   ");
					}else{
						sql.append(" and  b.id in (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )   ");
					}
//					if(brench.getOrgType()==OrganizationType.GROUNP){
//						sql.append(" o.id = " + levelSql +")");	
//					}else{
//						sql.append(" o.parentID = " + levelSql);
//						sql.append(" or o.id in " + levelSql + ")" );
//					}
					
				}else{
					sqloParams.put("followUserId", followUserId);
					sqloParams.put("brenchId", brenchId);
					sql.append(" and cds.REFERUSER_ID = :followUserId ");
					
					sqlo.append("select * from ( ");
					sqlo.append( sql );
					sqlo.append(" ) a where bid= :brenchId and follow_user_id= :followUserId ") ;
				}
			}					
//			sql.append(" and c.RES_ENTRANCE='ON_LINE'  ");
//			sql.append(" and c.DEAL_STATUS <> 'INVALID' ");
//			sql.append(" and c.DEAL_STATUS <> 'SIGNEUP'  ");
			
		//签单量
		}else if(numType.equals("contractNum")){
			sql.append(" select DISTINCT(cus.ID) as cusID,CASE when CONCAT(b.id,'_', b.name) IS NULL THEN CONCAT(o.id,'_',o.name) ELSE ( CASE  WHEN CONCAT(g.id,'_',g.name) IS NULL THEN CONCAT(b.id,'_', b.name) ELSE CONCAT(g.id,'_',g.name)  END ) END as  GROUNP, ");
			sql.append("  CASE when CONCAT(b.id,'_', b.name) IS NULL THEN CONCAT(o.id,'_',o.name) ELSE CONCAT(b.id,'_', b.name) END as BRENCH, ");			
			sql.append("  CASE  WHEN b.id is NULL then o.id  ELSE b.id  end as bid , cds.REFERUSER_ID AS follow_user_id, ");						
			sql.append(" cus.* ");
			sql.append(" from customer_dynamic_status cds ");
			sql.append(" INNER JOIN customer cus on cds.CUSTOMER_ID = cus.ID ");
			sql.append(getAppendRoleSql("cds"));
			sql.append(" INNER JOIN `user` u on u.USER_ID = cds.REFERUSER_ID ");
			sql.append(" INNER JOIN organization o ON o.id = u.organizationID ");
			sql.append(" LEFT JOIN organization b ON o.parentID = b.id ");
			sql.append(" LEFT JOIN organization g ON b.parentID = g.id ");
			sql.append(" where r.roleCode = '"+RoleCode.NETWORK_SPEC+"'");
			sql.append(" and cds.DYNAMIC_STATUS_TYPE ='"+CustomerEventType.CONTRACT_SIGN+"'");
			if(startDate != null && StringUtils.isNotBlank(startDate)){
				sql.append(" and cds.OCCOUR_TIME >= :startDate ");
				sqlParams.put("startDate", startDate+" 00:00:00 ");
				sqloParams.put("startDate", startDate+" 00:00:00 ");
			}
			if(endDate != null &&StringUtils.isNotBlank(endDate)){
				sql.append(" and cds.OCCOUR_TIME <= :endDate ");
				sqlParams.put("endDate", endDate+" 23:59:59 ");
				sqloParams.put("endDate", endDate+" 23:59:59 ");
			}			
//			sql.append(" and cbd.FOLLOW_USER_ID in ");		
//			sql.append(" ( SELECT USER_ID from user_dept_job  where JOB_ID in ( 'USE0000000114','USE0000000115','USE0000000116')  ) ");
//			//30天内签单量
//			sql.append("and c.create_time <=date_sub(cbd.FOLLOW_TIME, interval -30 day) ");
			if(followUserId.split("_")[0]!=null && StringUtils.isNotBlank(followUserId.split("_")[0])){
				Organization brench=organizationDao.findById(followUserId.split("_")[0]);
				if(brench!=null){
//					StringBuffer levelSql=new StringBuffer();
//					levelSql.append("(select id from organization where orgLevel   ");
//					if(org.getOrgType()==OrganizationType.GROUNP){
//						levelSql.append(" like '"+org.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )  ");
//					}else{
//						levelSql.append(" = '"+org.getOrgLevel()+"' ) ");
//					}
//					sql.append(" and o.id in (select id from  organization o  where     ");
//					sql.append(" and  o.id in (select id from  organization o  where o.parentID = (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' ) )  ");
					if(brench.getOrgType() == OrganizationType.GROUNP){
						sql.append(" and  o.id in (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )   ");
					}else{
						sql.append(" and  b.id in (select id from organization where orgLevel  like '"+brench.getOrgLevel()+"%' and orglevel = '"+brench.getOrgLevel()+"' )   ");
					}
//					if(brench.getOrgType()==OrganizationType.GROUNP){
//						sql.append(" o.id = " + levelSql +")");	
//					}else{
//						sql.append(" o.parentID = " + levelSql);
//						sql.append(" or o.id in " + levelSql + ")" );
//					}
				}else{
					sqloParams.put("followUserId", followUserId);
					sqloParams.put("brenchId", brenchId);
					sql.append(" and cds.REFERUSER_ID = :followUserId ");
					
					sqlo.append("select * from ( ");
					sqlo.append( sql );
					sqlo.append(" ) a where bid= :brenchId and follow_user_id= :followUserId ") ;
				}
			}	
			
		}
		
		List<Map<Object,Object>> list=customerDao.findMapBySql(sqlc.toString(),sqlcParams);
		List<CustomerVo> volist=new ArrayList<CustomerVo>();
		
		Boolean is=false;
		List<Customer> cusList=new ArrayList<Customer>();
		if(sqlo.length()>0){			
//			cusList=customerDao.findBySql(sqlo.toString()); 原来用的是这个 当然没有分页 星火 redmine #3620网络资源利用率点击查看客户详情，翻页功能失效
			dataPackage = customerDao.findPageBySql(sqlo.toString(), dataPackage,true,sqloParams);

			cusList= (List<Customer>) dataPackage.getDatas();
			is=true;
		}else if(sql.length()>0){
//			cusList=customerDao.findBySql(sql.toString());
			dataPackage = customerDao.findPageBySql(sql.toString(), dataPackage,true,sqlParams);
			cusList =(List<Customer>) dataPackage.getDatas();
			is=true;
		}
		if(cusList!=null && cusList.size()>0){
			volist=HibernateUtils.voListMapping(cusList, CustomerVo.class);
			}
			if(is){
				for(CustomerVo vo:volist){
					for(Map map:list){
						if(vo.getId().equals(map.get("ID").toString())){
							vo.setPaidAmount(Float.valueOf(map.get("paidAmount").toString()));
							vo.setTotalAmount(Float.valueOf(map.get("totalAmount").toString()));								
						}						
					}
					User user=new User();
					if(vo.getDeliverTarget()!=null && StringUtils.isNotBlank(vo.getDeliverTarget())){
						user=userService.findUserById(vo.getDeliverTarget());											
						if(user!=null){
							vo.setDeliverTargetName(user.getName());
							Organization campus=organizationDao.findById(vo.getBlSchool());
							vo.setDeliverTargetCampus(campus.getName());
						}else{
							Organization campus=organizationDao.findById(vo.getDeliverTarget());
							if(campus!=null){
								if(campus.getOrgType()==OrganizationType.DEPARTMENT){
									campus=organizationDao.findById(campus.getParentId());
								}
								vo.setDeliverTargetCampus(campus.getName());
							}
						}
					}
				}
			}else{
				for(Map map:list){								
					CustomerVo vo=new CustomerVo();
					vo.setId(map.get("ID").toString());
					vo.setName(map.get("NAME").toString());
					vo.setContact(map.get("CONTACT").toString());
					if(map.get("DELEVER_TARGET")!=null ){
						vo.setDeliverTarget(map.get("DELEVER_TARGET").toString());	
						User user=userService.findUserById(map.get("DELEVER_TARGET").toString());
						if(user!=null){
							vo.setDeliverTargetName(user.getName());
							Organization campus=organizationDao.findById(map.get("BL_SCHOOL").toString());
							vo.setDeliverTargetCampus(campus.getName());
						}else{
							Organization campus=organizationDao.findById(map.get("DELEVER_TARGET").toString());
							if(campus!=null){
								if(campus.getOrgType()==OrganizationType.DEPARTMENT){
									campus=organizationDao.findById(campus.getParentId());
								}
								vo.setDeliverTargetCampus(campus.getName());
							}
						}
					}
					if(map.get("VISIT_TYPE")!=null){
						vo.setVisitType(VisitType.valueOf(map.get("VISIT_TYPE").toString()));
					}
					if(map.get("DEAL_STATUS")!=null){
						vo.setDealStatus(CustomerDealStatus.valueOf(map.get("DEAL_STATUS").toString()));
					}
					vo.setTotalAmount(Float.valueOf(map.get("totalAmount").toString()));
					vo.setPaidAmount((Float.valueOf(map.get("paidAmount").toString())));
					volist.add(vo);
				}
			}



		dataPackage.setDatas(volist);
		return dataPackage;	
	}


	@Override
	public DataPackage courseList(Map<String, Object> params,DataPackage dp) {
		String orgLevel = null;
		if(params.get("organizationId") != null){
			orgLevel = organizationDao.findById(params.get("organizationId").toString()).getOrgLevel();
		}
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select branch.name branchName ,campus.name campusName,studyManager.name studentManagerName,s.name studentName,teacher.name teacherName,grade.NAME gradeName,subject.name subjectName,concat(c.COURSE_DATE,'') COURSE_DATE,c.COURSE_TIME ,c.PLAN_HOURS ,c.REAL_HOURS, c.COURSE_STATUS, ");
		sql.append(" c.TEACHING_ATTEND_TIME as teachingAttendTime, c.STADUY_MANAGER_AUDIT_TIME as studyManagerAuditTime, c.TEACHING_MANAGER_AUDIT_TIME as teachingManagerAuditTime ");  //考勤，确认，扣费时间
		sql.append(" from course c");
		sql.append(" LEFT JOIN organization as campus on campus.id = c.BL_CAMPUS_ID    ");
		sql.append(" LEFT JOIN organization as branch on branch.id = campus.parentID   ");
		sql.append(" LEFT JOIN user as studyManager on studyManager.user_id = c.STUDY_MANAGER_ID   ");
		sql.append(" LEFT JOIN student as s on s.ID = c.STUDENT_ID   ");
		sql.append(" LEFT JOIN user as teacher on teacher.user_id = c.TEACHER_ID   ");
		sql.append(" LEFT JOIN data_dict as grade on grade.id = c.GRADE   ");
		sql.append(" LEFT JOIN data_dict as subject on subject.id = c.SUBJECT   ");

		sql.append(" where 1=1   ");

		if(params.get("startDate") != null){
			sql.append(" and course_date >= :startDate ");
			sqlParams.put("startDate", params.get("startDate"));
		}
		if(params.get("endDate") != null){
			sql.append(" and course_date <= :endDate ");
			sqlParams.put("endDate", params.get("endDate"));
		}
		if(orgLevel != null){
			sql.append(" and campus.orgLevel like '").append(orgLevel).append("%'                                                                                                                                                                                                                                           ");
		}
		if (params.get("courseStatus")!=null){
			switch ((String)params.get("courseStatus")){
				case "weishangke"://未上课
					sql.append("  and c.COURSE_STATUS in ('NEW','STUDENT_ATTENDANCE') ");
					break;
				case "yikaoqin": //已考勤
					sql.append(" and c.COURSE_STATUS = 'TEACHER_ATTENDANCE' ");
					break;
				case "yiqueren":  //已确认
					sql.append(" and c.COURSE_STATUS = 'STUDY_MANAGER_AUDITED' ");
					break;
				case "unaudit": //未审
					sql.append(" and c.COURSE_STATUS in ('NEW','CHARGED','STUDY_MANAGER_AUDITED','TEACHER_ATTENDANCE') and (c.AUDIT_STATUS is null or c.AUDIT_STATUS = 'UNAUDIT') ");
					break;
				case "financeUnaudit": //未审
					sql.append(" and c.COURSE_STATUS = 'CHARGED' and (c.AUDIT_STATUS is null or c.AUDIT_STATUS = 'UNAUDIT') ");
					break;
			}

		}
		List<Organization> userOrganizations = organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append(" and (");
			sql.append(" campus.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or campus.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(" )");
		}

		List<Map<Object, Object>> list = organizationDao.findMapOfPageBySql(sql.toString(),dp, sqlParams);
		dp.setDatas(list);
		dp.setRowCount(organizationDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", sqlParams));
		return dp;
	}

	@Override
	public DataPackage miniCourse(Map<String, Object> params, DataPackage dataPackage) {
		String orgLevel=null;
		String organizationIdFinder = (String) params.get("organizationId");
		String startDate = (String)params.get("startDate");
		String endDate = (String)params.get("endDate");
		String miniClassTypeId = (String)params.get("miniclasstypes");

		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sql.append("select brench.name branchName, campus.name campusName, mc.NAME className,studyManage.name studyManageName,teacher.name teacherName, grade.name gradeName, subject.name subjectName,concat(mcc.COURSE_DATE,'') COURSE_DATE , CONCAT(mcc.COURSE_TIME,\" - \",mcc.COURSE_END_TIME) COURSE_TIME, mcc.COURSE_HOURS,mcc.COURSE_STATUS,  ");
		sql.append(" mcc.STUDY_MANAGER_CHARGE_TIME as studyManageChargeTime,mcc.TEACHING_ATTEND_TIME as teacherAttendTime ");
		

		sql.append(" from mini_class_course mcc ");
		sql.append(" INNER JOIN mini_class mc ON mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN organization campus on campus.id=mc.BL_CAMPUS_ID ");
		sql.append(" INNER JOIN organization brench on campus.parentID=brench.id ");
		sql.append(" INNER JOIN product p ON p.ID =mc.PRODUCE_ID ");
		sql.append(" LEFT JOIN user studyManage on studyManage.user_id= mcc.STUDY_MANEGER_ID ");
        sql.append(" LEFT JOIN user teacher on teacher.user_id = mcc.TEACHER_ID ");
		sql.append(" LEFT JOIN data_dict as grade on grade.id = mcc.GRADE   ");
		sql.append(" LEFT JOIN data_dict as subject on subject.id = mcc.SUBJECT   ");


		sql.append(" where 1=1 ");
		sql.append(" and mcc.course_status <> 'CANCEL' ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and mcc.COURSE_DATE >= :startDate ");
			sqlParams.put("startDate", startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and mcc.COURSE_DATE <= :endDate ");
			sqlParams.put("endDate", endDate);
		}
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and campus.orgLevel LIKE ").append("'"+orgLevel+"").append("%'");
		}

		if (params.get("courseStatus")!=null){
			switch ((String)params.get("courseStatus")){
				case "weishangke"://未上课
					sql.append("  and mcc.COURSE_STATUS = 'NEW' ");
					break;
				case "yikaoqin": //已考勤
					sql.append(" and mcc.COURSE_STATUS = 'TEACHER_ATTENDANCE' ");
					break;
				case "unaudit": //未审
					sql.append(" and mcc.COURSE_STATUS in ('NEW','CHARGED','TEACHER_ATTENDANCE') and (mcc.AUDIT_STATUS is null or mcc.AUDIT_STATUS = 'UNAUDIT') ");
					break;
				case "financeUnaudit": //未审
					sql.append(" and mcc.COURSE_STATUS = 'CHARGED' and (mcc.AUDIT_STATUS is null or mcc.AUDIT_STATUS = 'UNAUDIT') ");
					break;
			}

		}

		if(StringUtils.isNotBlank(miniClassTypeId)){
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID = :miniClassType0 ");
				sqlParams.put("miniClassType0", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sql.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					sqlParams.put("miniClassType" + i, miniClassTypes[i]);
				}
				sql.append(" )");
			}
		}

		List<Map<Object,Object>> list=organizationDao.findMapOfPageBySql(sql.toString(), dataPackage, sqlParams);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(organizationDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", sqlParams));
		return dataPackage;
	}

	@Override
	public DataPackage twoTeacherCourse(Map<String, Object> params, DataPackage dataPackage) {
		String orgLevel=null;
		String organizationIdFinder = (String) params.get("organizationId");
		String startDate = (String)params.get("startDate");
		String endDate = (String)params.get("endDate");
		String twoTeacherclasstypes = (String)params.get("twoTeacherclasstypes");

		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		

		
		//TODO 
		sql.append(" select course.COURSE_DATE,CONCAT(course.COURSE_TIME,\" - \",course.COURSE_END_TIME) COURSE_TIME,");
		sql.append(" oo.name as branchName, o.name as campusName,ttcsa.COURSE_STATUS,");
		sql.append(" course.COURSE_HOURS ,two.`NAME` as courseName,");
		sql.append(" d.`NAME` as gradeName,dd.`NAME` as subjectName,u.`NAME` as teacherName,uu.`NAME` as studyManegerName ");
		sql.append(" from two_teacher_class_student_attendent ttcsa ");
		sql.append(" left join two_teacher_class_two two on two.CLASS_TWO_ID= ttcsa.CLASS_TWO_ID");
		sql.append(" left join organization o on o.id = two.BL_CAMPUS_ID ");
		sql.append(" left join organization oo on oo.id = o.parentID ");
		sql.append(" left join two_teacher_class_course course on course.COURSE_ID= ttcsa.TWO_CLASS_COURSE_ID");
		sql.append(" left join two_teacher_class cla on cla.CLASS_ID= course.CLASS_ID");
		sql.append(" left join product p on p.ID= cla.PRODUCE_ID");
		sql.append(" left join data_dict d on d.ID = p.GRADE_ID ");
		sql.append(" left join data_dict dd on dd.ID = cla.`SUBJECT` ");
		sql.append(" left join `user` u on u.USER_ID = cla.TEACHER_ID ");
		sql.append(" left join `user` uu on uu.USER_ID = two.TEACHER_ID ");
				
	    sql.append(" where ttcsa.COURSE_STATUS<>'CANCEL' ");	

    	if(StringUtils.isNotBlank(startDate)){
    		sql.append(" and course.COURSE_DATE >= :startDate ");
    		sqlParams.put("startDate", startDate);
    	}   	
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and course.COURSE_DATE <= :endDate ");
			sqlParams.put("endDate", endDate);
		}		

		
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and o.orgLevel LIKE ").append("'"+orgLevel+"").append("%'");
		}

		if (params.get("courseStatus")!=null){
			switch ((String)params.get("courseStatus")){
				case "weishangke"://未上课
					sql.append("  and (ttcsa.COURSE_STATUS = 'NEW' or ttcsa.COURSE_STATUS = 'TEACHER_ATTENDANCE') ");
					break;
				case "yikaoqin": //已考勤
					sql.append(" and ttcsa.COURSE_STATUS = 'TEACHER_ATTENDANCE' ");
					break;
				case "unaudit": //未审
					sql.append(" and ttcsa.COURSE_STATUS in ('NEW','CHARGED','TEACHER_ATTENDANCE') and (ttcsa.AUDIT_STATUS is null or ttcsa.AUDIT_STATUS = 'UNAUDIT') ");
					break;
				case "financeUnaudit": //未审
					sql.append(" and ttcsa.COURSE_STATUS = 'CHARGED' and (ttcsa.AUDIT_STATUS is null or ttcsa.AUDIT_STATUS = 'UNAUDIT') ");
					break;
			}

		}
				
		if(StringUtils.isNotBlank(twoTeacherclasstypes)){
			String[] twoTeacherClassTypes=twoTeacherclasstypes.split(",");
			if(twoTeacherClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID in ( :twoTeacherClassTypes ))");
				sqlParams.put("twoTeacherClassTypes", twoTeacherClassTypes);
			}
		}
		sql.append(" and not EXISTS (select 1 from two_teacher_class_student_attendent vvv where vvv.TWO_CLASS_COURSE_ID =course.COURSE_ID and vvv.CHARGE_STATUS='CHARGED' and vvv.class_two_id = ttcsa.CLASS_TWO_ID) ");
		sql.append(" group by course.COURSE_ID,two.CLASS_TWO_ID ");
		List<Map<Object,Object>> list=twoTeacherAttendentDao.findMapOfPageBySql(sql.toString(), dataPackage, sqlParams);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(twoTeacherAttendentDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", sqlParams));
		return dataPackage;
	}

	
	@Override
	public DataPackage otmCourse(Map<String, Object> params, DataPackage dataPackage) {
		String orgLevel=null;
		String organizationIdFinder = (String) params.get("organizationId");
		String startDate = (String)params.get("startDate");
		String endDate = (String)params.get("endDate");
		String otmClassTypeId = (String)params.get("otmclasstypes");

		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sql.append("select brench.name branchName, campus.name campusName, concat(occ.OTM_CLASS_NAME, '') className, ocsa.studyManageName,teacher.name teacherName, grade.name gradeName, subject.name subjectName,concat(occ.COURSE_DATE,'') COURSE_DATE , CONCAT(occ.COURSE_TIME,\" - \",occ.COURSE_END_TIME) COURSE_TIME, occ.COURSE_HOURS,occ.COURSE_STATUS,  ");
		sql.append(" IFNULL(occ.STUDY_MANAGER_CHARGE_TIME, '') as studyManageChargeTime, IFNULL(occ.TEACHING_ATTEND_TIME, '') as teacherAttendTime ");
		
		sql.append(" from otm_class_course occ ");
		sql.append(" INNER JOIN otm_class oc ON occ.OTM_CLASS_ID = oc.OTM_CLASS_ID ");
		sql.append(" INNER JOIN organization campus on campus.id=oc.BL_CAMPUS_ID ");
		sql.append(" INNER JOIN organization brench on campus.parentID=brench.id ");
//		sql.append(" LEFT JOIN user studyManage on studyManage.user_id= occ.STUDY_MANEGER_ID ");

		sql.append(" LEFT JOIN ( select occ.OTM_CLASS_COURSE_ID, TRIM(group_concat(DISTINCT u.name)) studyManageName ");

		sql.append(" 	FROM otm_class_student_attendent ocsa ");
		sql.append(" 	INNER JOIN otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
		sql.append(" 	INNER JOIN otm_class oc ON occ.OTM_CLASS_ID = oc.OTM_CLASS_ID  ");
		sql.append(" 	INNER JOIN USER u ON u.user_id = ocsa.STUDY_MANAGER_ID ");
		sql.append(" 	INNER JOIN organization campus ON campus.id = oc.BL_CAMPUS_ID  ");
		sql.append(" 	INNER JOIN organization brench ON campus.parentID = brench.id  ");

		sql.append(" 	group by occ.OTM_CLASS_COURSE_ID) ocsa on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");

		sql.append(" LEFT JOIN user teacher on teacher.user_id = occ.TEACHER_ID ");
		sql.append(" LEFT JOIN data_dict as grade on grade.id = occ.GRADE   ");
		sql.append(" LEFT JOIN data_dict as subject on subject.id = occ.SUBJECT   ");
		sql.append(" 	where 1=1 ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and occ.COURSE_DATE >= :startDate ");
			sqlParams.put("startDate", startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and occ.COURSE_DATE <= :endDate ");
			sqlParams.put("endDate", endDate);
		}
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and campus.orgLevel LIKE ").append("'"+orgLevel+"").append("%'");
		}

		if(StringUtils.isNotBlank(otmClassTypeId)){
			String[] otmClassTypes=otmClassTypeId.split(",");
			if(otmClassTypes.length>0){
				sql.append(" and (oc.OTM_TYPE = :otmClassType0 ");
				sqlParams.put("otmClassType0", otmClassTypes[0]);
				for (int i = 1; i < otmClassTypes.length; i++) {
					sql.append(" or oc.OTM_TYPE = :otmClassType" + i);
					sqlParams.put("otmClassType" + i, otmClassTypes[i]);
				}
				sql.append(" )");
			}
		}

		if (params.get("courseStatus")!=null){
			switch ((String)params.get("courseStatus")){
				case "weishangke"://未上课
					sql.append("  and occ.COURSE_STATUS = 'NEW' ");
					break;
				case "yikaoqin": //已考勤
					sql.append(" and occ.COURSE_STATUS = 'TEACHER_ATTENDANCE' ");
					break;
				case "unaudit": //未审
					sql.append(" and occ.COURSE_STATUS in ('NEW','CHARGED','TEACHER_ATTENDANCE') and (occ.AUDIT_STATUS is null or occ.AUDIT_STATUS = 'UNAUDIT') ");
					break;
				case "financeUnaudit": //未审
					sql.append(" and occ.COURSE_STATUS = 'CHARGED' and (occ.AUDIT_STATUS is null or occ.AUDIT_STATUS = 'UNAUDIT') ");
					break;
			}

		}


		List<Map<Object,Object>> list=organizationDao.findMapOfPageBySql(sql.toString(), dataPackage, sqlParams);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(organizationDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", sqlParams));
		return dataPackage;
	}

	@Override
	public DataPackage otmStudent(Map<String, Object> params, DataPackage dataPackage) {
		String orgLevel=null;
		String organizationIdFinder = (String) params.get("organizationId");
		String startDate = (String)params.get("startDate");
		String endDate = (String)params.get("endDate");
		String otmClassTypeId = (String)params.get("otmclasstypes");

		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sql.append(" SELECT                                                                                    ");
		sql.append(" occ.OTM_CLASS_NAME className,                                                             ");
		sql.append(" occ.COURSE_DATE COURSE_DATE,                                                              ");
		sql.append(" concat(                                                                                   ");
		sql.append(" 		occ.COURSE_TIME,                                                                   ");
		sql.append(" 		\" - \",                                                                             ");
		sql.append(" 		occ.COURSE_END_TIME )                                                               ");
		sql.append("  COURSE_TIME,                                                                           ");
		sql.append(" 	s. NAME studentName,                                                               ");
		sql.append(" 		STUDY_MANAGER. NAME studyManageName,                                               ");
		sql.append(" 		teacher. NAME teacherName,                                                         ");
		sql.append(" 	grade. NAME gradeName,                                                             ");
		sql.append(" 		SUBJECT . NAME subjectName,                                                        ");
		sql.append(" 		occ.COURSE_HOURS COURSE_HOURS,                                                     ");
		sql.append(" 	ocsa.ATTENDENT_STATUS ATTENDENT_STATUS,                                            ");
		sql.append(" 		ocsa.CHARGE_STATUS CHARGE_STATUS                                                   ");
		sql.append(" 	FROM                                                                               ");
		sql.append(" otm_class_student_attendent ocsa                                                          ");
		sql.append(" LEFT JOIN student s ON ocsa.student_id = s.id                                            ");
		sql.append(" LEFT JOIN otm_class_course occ ON occ.OTM_CLASS_COURSE_ID = ocsa.OTM_CLASS_COURSE_ID     ");
		sql.append(" LEFT JOIN otm_class oc ON occ.OTM_CLASS_ID =oc.OTM_CLASS_ID     ");
		sql.append(" LEFT JOIN organization campus ON campus.id=oc.BL_CAMPUS_ID ");
		sql.append(" LEFT JOIN `user` STUDY_MANAGER ON ocsa.STUDY_MANAGER_ID = STUDY_MANAGER.user_id          ");
		sql.append(" LEFT JOIN `user` teacher ON occ.TEACHER_ID = teacher.user_id                             ");
		sql.append(" LEFT JOIN data_dict AS grade ON grade.id = occ.GRADE                                     ");
		sql.append(" LEFT JOIN data_dict AS SUBJECT ON SUBJECT .id = occ. SUBJECT                             ");
		sql.append(" WHERE                                                                                     ");
		sql.append(" 1 = 1                                                                                     ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and occ.COURSE_DATE >= :startDate ");
			sqlParams.put("startDate", startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and occ.COURSE_DATE <= :endDate ");
			sqlParams.put("endDate", endDate);
		}
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and campus.orgLevel LIKE ").append("'"+orgLevel+"").append("%'");
		}
		sql.append(" and occ.COURSE_STATUS <> 'CANCEL' ");
		
		if(StringUtils.isNotBlank(otmClassTypeId)){
			String[] otmClassTypes=otmClassTypeId.split(",");
			if(otmClassTypes.length>0){
				sql.append(" and (oc.OTM_TYPE = :otmClassType0 ");
				sqlParams.put("otmClassType0", otmClassTypes[0]);
				for (int i = 1; i < otmClassTypes.length; i++) {
					sql.append(" or oc.OTM_TYPE = :otmClassType" + i);
					sqlParams.put("otmClassType" + i, otmClassTypes[i]);
				}
				sql.append(" )");
			}
		}


		if (params.get("studentStatus")!=null){
			switch((String)params.get("studentStatus")){
				case "unchargeStudent":
					sql.append(" and ocsa.CHARGE_STATUS = 'UNCHARGE'");
					break;
			}
		}

		sql.append(" order by occ.OTM_CLASS_NAME,occ.COURSE_DATE  desc");

		List<Map<Object,Object>> list=organizationDao.findMapOfPageBySql(sql.toString(), dataPackage, sqlParams);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(organizationDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", sqlParams));
		return dataPackage;

	}

	@Override
	public DataPackage miniCourseStudentByStatus(Map<String, Object> params, DataPackage dataPackage) {
		String orgLevel=null;
		String organizationIdFinder = (String) params.get("organizationId");
		String startDate = (String)params.get("startDate");
		String endDate = (String)params.get("endDate");
		String miniClassTypeId = (String)params.get("miniclasstypes");

		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
	    sql.append("	SELECT                                                                                            ");
	    sql.append("	mc.NAME className,                                                                    ");
	    sql.append("	mcc.COURSE_DATE COURSE_DATE,                                                                      ");
	    sql.append("	CONCAT(                                                                                           ");
	    sql.append("			mcc.COURSE_TIME,                                                                          ");
	    sql.append("			\" - \",                                                                                    ");
	    sql.append("			mcc.COURSE_END_TIME                                                                       ");
	    sql.append("	) COURSE_TIME,                                                                                    ");
	    sql.append("			s.`NAME` studentName,                                                                     ");
	    sql.append("			studyManage.`NAME` studyManageName,                                                       ");
	    sql.append("			teacher.`NAME` teacherName,                                                               ");
	    sql.append("			grade.`NAME` gradeName,                                                                   ");
	    sql.append("			SUBJECT .`NAME` subjectName,                                                              ");
	    sql.append("			mcc.COURSE_HOURS COURSE_HOURS,                                                            ");
	    sql.append("			mcsa.ATTENDENT_STATUS ATTENDENT_STATUS,                                                   ");
	    sql.append("			mcsa.CHARGE_STATUS CHARGE_STATUS                                                          ");
	    sql.append("			FROM                                                                                      ");
	    sql.append("	mini_class_student_attendent mcsa                                                                 ");
	    sql.append("	LEFT JOIN student s ON s.ID = mcsa.STUDENT_ID                                                     ");
	    sql.append("	LEFT JOIN mini_class_course mcc ON mcsa.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID           ");
	    sql.append("	LEFT JOIN `user` studyManage ON studyManage.USER_ID = mcc.STUDY_MANEGER_ID                        ");
	    sql.append("	LEFT JOIN `user` teacher ON teacher.USER_ID = mcc.TEACHER_ID                                      ");
	    sql.append("	LEFT JOIN mini_class mc ON mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID                                   ");
		sql.append("    LEFT JOIN organization campus ON campus.id=mc.BL_CAMPUS_ID "  );
		sql.append("    LEFT JOIN product p ON p.ID =mc.PRODUCE_ID   ");
	    sql.append("	LEFT JOIN data_dict grade ON grade.id = mcc.GRADE                                                 ");
	    sql.append("	LEFT JOIN data_dict SUBJECT ON SUBJECT .ID = mcc.`SUBJECT`                                        ");
        sql.append("    where 1=1 ");

		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and mcc.COURSE_DATE >= :startDate ");
			sqlParams.put("startDate", startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and mcc.COURSE_DATE <= :endDate ");
			sqlParams.put("endDate", endDate);
		}
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and campus.orgLevel LIKE ").append("'"+orgLevel+"").append("%'");
		}
		sql.append(" and mcc.COURSE_STATUS <> 'CANCEL' ");

		if(StringUtils.isNotBlank(miniClassTypeId)){
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID = :miniClassType0 ");
				sqlParams.put("miniClassType0", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sql.append(" or p.CLASS_TYPE_ID = :miniClassType" + i);
					sqlParams.put("miniClassType" + i, miniClassTypes[i]);
				}
				sql.append(" )");
			}
		}

		if (params.get("studentStatus")!=null){
			switch((String)params.get("studentStatus")){
				case "unchargeStudent":
					sql.append(" and mcsa.CHARGE_STATUS = 'UNCHARGE'");
					break;
			}
		}
		
		sql.append(" order by mcc.MINI_CLASS_NAME,mcc.COURSE_DATE  desc");

		List<Map<Object,Object>> list=organizationDao.findMapOfPageBySql(sql.toString(), dataPackage, sqlParams);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(organizationDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", sqlParams));
		return dataPackage;
	}
	
	@Override
	public DataPackage twoCourseStudentByStatus(Map<String, Object> params, DataPackage dataPackage) {
		String orgLevel=null;
		String organizationIdFinder = (String) params.get("organizationId");
		String startDate = (String)params.get("startDate");
		String endDate = (String)params.get("endDate");
		String twoclasstypes = (String)params.get("twoclasstypes");

		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		
		
		//TODO 	
		sql.append(" select course.COURSE_DATE,CONCAT(course.COURSE_TIME,\" - \",course.COURSE_END_TIME) COURSE_TIME,");
		sql.append(" ttcsa.COURSE_STATUS,s.`NAME` studentName,ttcsa.ATTENDENT_STATUS,ttcsa.CHARGE_STATUS,");
		sql.append(" course.COURSE_HOURS ,two.`NAME` as courseName,");
		sql.append(" d.`NAME` as gradeName,dd.`NAME` as subjectName,u.`NAME` as teacherName,uu.`NAME` as studyManegerName ");
		sql.append(" from two_teacher_class_student_attendent ttcsa ");
		sql.append(" left join student s ON s.ID = ttcsa.STUDENT_ID");
		sql.append(" left join two_teacher_class_two two on two.CLASS_TWO_ID= ttcsa.CLASS_TWO_ID");
		sql.append(" left join organization o on o.id = two.BL_CAMPUS_ID ");
		sql.append(" left join two_teacher_class_course course on course.COURSE_ID= ttcsa.TWO_CLASS_COURSE_ID");
		sql.append(" left join two_teacher_class cla on cla.CLASS_ID= course.CLASS_ID");
		sql.append(" left join product p on p.ID= cla.PRODUCE_ID");
		sql.append(" left join data_dict d on d.ID = p.GRADE_ID ");
		sql.append(" left join data_dict dd on dd.ID = cla.`SUBJECT` ");
		sql.append(" left join `user` u on u.USER_ID = cla.TEACHER_ID ");
		sql.append(" left join `user` uu on uu.USER_ID = two.TEACHER_ID ");
				
	    sql.append(" where ttcsa.COURSE_STATUS<>'CANCEL' ");	
	    
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and o.orgLevel LIKE ").append("'"+orgLevel+"").append("%'");
		}
    	if(StringUtils.isNotBlank(startDate)){
    		sql.append(" and course.COURSE_DATE >= :startDate ");
    		sqlParams.put("startDate", startDate);
    	}   	
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and course.COURSE_DATE <= :endDate ");
			sqlParams.put("endDate", endDate);
		}				

		if(StringUtils.isNotBlank(twoclasstypes)){
			String[] twoTeacherClassTypes=twoclasstypes.split(",");
			if(twoTeacherClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID in ( :twoTeacherClassTypes ))");
				sqlParams.put("twoTeacherClassTypes", twoTeacherClassTypes);
			}
		}

		if (params.get("studentStatus")!=null){
			switch((String)params.get("studentStatus")){
				case "unchargeStudent":
					sql.append(" and ttcsa.CHARGE_STATUS = 'UNCHARGE'");
					break;
			}
		}
		
		sql.append(" order by course.COURSE_NAME,course.COURSE_DATE  desc");

		List<Map<Object,Object>> list=twoTeacherAttendentDao.findMapOfPageBySql(sql.toString(), dataPackage, sqlParams);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(twoTeacherAttendentDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", sqlParams));
		return dataPackage;
	}


}
