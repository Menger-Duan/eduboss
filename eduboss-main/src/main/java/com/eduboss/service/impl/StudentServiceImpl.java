package com.eduboss.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.*;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.*;
import com.eduboss.service.*;
import com.eduboss.service.handler.ClientHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.BonusType;
import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.CourseRequirementStatus;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.CourseSummaryType;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.CustomerStudentStatus;
import com.eduboss.common.ElectronicAccChangeType;
import com.eduboss.common.Memcached;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.PayType;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;
import com.eduboss.common.SchoolTempAuditStatus;
import com.eduboss.common.StudentOneOnManyStatus;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.common.StudentSmallClassStatus;
import com.eduboss.common.StudentStatus;
import com.eduboss.common.StudentType;
import com.eduboss.domainVo.StudentMap.ParamVo;
import com.eduboss.domainVo.StudentMap.StudentAddress;
import com.eduboss.domainVo.StudentMap.StudentMap;
import com.eduboss.eo.StudentSchoolImportedEo;
import com.eduboss.excel.imported.ExcelImporter;
import com.eduboss.excel.imported.ExecuteFunction;
import com.eduboss.excel.imported.PoiExcelImporter;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.jedis.FinanceMessage;
import com.eduboss.jedis.IncomeMessage;
import com.eduboss.jedis.StudentStatusMsg;
import com.eduboss.service.ContractProductSubjectService;
import com.eduboss.service.ContractService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.DataDictService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.OtmClassService;
import com.eduboss.service.RefundWorkflowService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.StudentDynamicStatusService;
import com.eduboss.service.StudentReturnService;
import com.eduboss.service.StudentService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.CheckPhoneNumber;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.ImageSizer;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.base.Function;
import com.google.common.collect.Maps;


@Service
public class StudentServiceImpl implements StudentService {


	private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);


	@Autowired
	private StudentDao studentDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private CourseRequirementDao courseRequirementDao;

	@Autowired
	private CourseSummaryDao courseSummaryDao;

	@Autowired
	private DataDictDao dataDictDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private UserService userService;

	@Autowired
	private StudnetAccMvDao studnetAccMvDao;

	@Autowired
	private CustomerStudentDao customerStudentDao;

	@Autowired
	private StudentSchoolDao studentSchoolDao;

	@Autowired
	private StudentSchoolTempDao studentSchoolTempDao;

	@Autowired
	private StudentScoreDao studentScoreDao;


	@Autowired
	private StudentFingerInfoDao studentFingerInfoDao;

    @Autowired
    private RoleQLConfigService roleQLConfigService;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ElectronicAccountChangeLogDao electronicAccountChangeLogDao;

    @Autowired
    private FundsChangeHistoryDao fundsChangeHistoryDao;

    @Autowired
    private ContractService contractService;

    @Autowired
    StudentMoneyChangesDao studentMoneyChangesDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StudentFollowUpDao studentFollowUpDao;

    @Autowired
    private TransactionCampusRecordDao transactionCampusRecordDao;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private StudentOrganizationDao studentOrganizationDao;

    @Autowired
    private MobileUserService mobileUserService;

    @Autowired
	private StudentCommentDao studentCommentDao;

    @Autowired
    private StudentDynamicStatusService studentDynamicStatusService;

    @Autowired
    private StudentReturnService studentReturnService;

    @Autowired
    private AccountChargeRecordsDao accountChargeRecordsDao;

    @Autowired
   	private StudentFileDao studentFileDao;

    @Autowired
    private PromiseClassRecordDao promiseClassRecordDao;

    @Autowired
    private OtmClassService otmClassService;

	@Autowired
	private MiniClassStudentAttendentDao miniClassStudentAttendentDao;

	@Autowired
	private RefundWorkflowService refundWorkflowService;


	@Autowired
	private MiniClassDao miniClassDao;

	@Autowired
	private PromiseClassDao promiseClassDao;

	@Autowired
	private MoneyWashRecordsDao moneyWashRecordsDao;

	@Autowired
	private OtmClassStudentDao otmClassStudentDao;

	@Autowired
	private MiniClassStudentDao miniClassStudentDao;

	@Autowired
	private ContractProductSubjectService contractProductSubjectService;

	@Autowired
	private TwoTeacherClassStudentDao twoTeacherClassStudentDao;
	
	@Autowired
	private ClientHandler clientHandler;
	
	@Autowired
	private RegionService regionService;


	private final static ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private StudentAccInfoDao studentAccInfoDao;

	public void saveStudent(Student student) {
		studentDao.save(student);
		studentOrganizationDao.saveStudentOrganization(student);
	}

	@Override
	public Student getStduentById(String id) {
		return studentDao.findById(id);
	}

	@Override
	public DataPackage getStudentList(Student student,boolean includingAccountInfo, DataPackage dp,ModelVo modelVo,String rcourseHour ,String rcourseHourEnd,String brenchId,String stuType,String stuNameGradeSchool) {
		return getStudentList(student, dp, modelVo, includingAccountInfo,rcourseHour,rcourseHourEnd,brenchId,stuType,stuNameGradeSchool);
	}


	@Override
	public DataPackage getStudentList(Student student, DataPackage dp,ModelVo modelVo, boolean includingAccountInfo,String rcourseHour ,String rcourseHourEnd,String brenchId,String stuType,String stuNameGradeSchool) {
		//如果有字段有值，用like进行条件查询
		Map<String, Object> params = new HashMap<String, Object>();
		long begin = System.currentTimeMillis();
		StringBuilder query = new StringBuilder(512);
		StringBuilder queryCount = new StringBuilder(512);
		queryCount.append(" select count(1) from student s LEFT JOIN student_school ss ON s.SCHOOL = ss.ID LEFT JOIN data_dict d on d.ID = s.GRADE_ID left join organization o on o.id = s.BL_CAMPUS_ID left join organization oo on oo.id = o.parentID ");
		query.append(" SELECT s.ID,s.`NAME`,s.SEX,s.CONTACT,s.CREATE_TIME,s.schoolOrTemp,ss.`NAME` SCHOOL_NAME,s.`STATUS`,s.STU_STATUS,s.ONEONONE_STATUS ONE_ON_ONE_STATUS,s.ATTANCE_NO, ");
		query.append(" d.`NAME` gradeName,o.`name` blCampusName,oo.`name` brenchName,u.`NAME` studyManagerName, ");
		query.append(" st.ID schoolTempId,st.`NAME` schoolTempName ");
		query.append(" ,IF(s.status = 'FINISH_CLASS' || s.status = 'STOP_CLASS',TIMESTAMPDIFF(DAY,DATE_FORMAT(s.FINISH_TIME,'%Y-%m-%d'),CURDATE()),null) as dayNumbers ");
		query.append(" from student s LEFT JOIN student_school ss on s.SCHOOL =ss.ID LEFT JOIN data_dict d on d.ID = s.GRADE_ID ");
		query.append(" left join organization o on o.id = s.BL_CAMPUS_ID left join organization oo on oo.id = o.parentID ");
		query.append(" left join `user` u on u.USER_ID = s.STUDY_MANEGER_ID ");
		query.append(" left join school_temp st on st.ID = s.SCHOOL_TEMP ");

		if(StringUtils.isNotEmpty(rcourseHour)||StringUtils.isNotEmpty(rcourseHourEnd)){
			query.append(" left join STUDNET_ACC_MV sam on s.ID=sam.STUDENT_ID ");
			queryCount.append(" left join STUDNET_ACC_MV sam on s.ID=sam.STUDENT_ID ");
		}
		query.append(" where 1=1 ");
		queryCount.append(" where 1=1 ");

		if(StringUtils.isNotBlank(stuNameGradeSchool) && stuNameGradeSchool!=null){
			query.append(" AND ( s.`NAME` like :studentName or d.`NAME` like :gradeName or ss.`NAME` like :schoolName )" );
			queryCount.append(" AND ( s.`NAME` like :studentName or d.`NAME` like :gradeName or ss.`NAME` like :schoolName )" );
			params.put("studentName", "%" + stuNameGradeSchool + "%");
			params.put("gradeName", "%" + stuNameGradeSchool + "%");
			params.put("schoolName", "%" + stuNameGradeSchool + "%");
	    }
		if(StringUtils.isNotEmpty(student.getId())){
			query.append(" AND s.ID LIKE :schoolId ");
			queryCount.append(" AND s.ID LIKE :schoolId ");
			params.put("schoolId", "%" + student.getId() + "%");
		}
		if(StringUtils.isNotEmpty(student.getName())){
			query.append(" AND s.`NAME` LIKE :studentName2 ");
			queryCount.append(" AND s.`NAME` LIKE :studentName2 ");
			params.put("studentName2", "%" + student.getName() + "%");
		}
		if(student.getSchool()!=null && StringUtils.isNotEmpty(student.getSchool().getName())) {
			query.append("  AND ss.`NAME` LIKE :schoolName2 ");
			queryCount.append("  AND ss.`NAME` LIKE :schoolName2 ");
			params.put("schoolName2", "%" + student.getSchool().getName() + "%");
		}
		if(StringUtils.isNotEmpty(student.getFatherPhone())){
			query.append(" AND s.FATHER_PHONE LIKE :fatherPhone");
			queryCount.append(" AND s.FATHER_PHONE LIKE :fatherPhone");
			params.put("fatherPhone", "%" + student.getFatherPhone() + "%");
		}
		if(StringUtils.isNotEmpty(modelVo.getStartDate())){
			query.append(" AND s.CREATE_TIME >= :startDate ");
			queryCount.append(" AND s.CREATE_TIME >= :startDate ");
			params.put("startDate", modelVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(modelVo.getEndDate())){
			query.append(" AND s.CREATE_TIME <= :endDate ");
			queryCount.append(" AND s.CREATE_TIME <= :endDate ");
			params.put("endDate", modelVo.getEndDate());
		}
		if(StringUtils.isNotEmpty(modelVo.getModelName1())){
			query.append(" AND s.BL_CAMPUS_ID in (SELECT ID FROM organization WHERE `NAME` LIKE :modelName1 )");
			queryCount.append(" AND s.BL_CAMPUS_ID in (SELECT ID FROM organization WHERE `NAME` LIKE :modelName1 )");
			params.put("modelName1",  "%" + modelVo.getModelName1() + "%");
		}

		if(StringUtils.isNotEmpty(student.getBlCampusId())){
			query.append(" AND s.BL_CAMPUS_ID = :blCampusId ");
			queryCount.append(" AND s.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", student.getBlCampusId());
		}
		if(StringUtils.isEmpty(student.getBlCampusId()) && StringUtils.isNotBlank(brenchId)){
			//查询分公司下所有学生
			query.append(" AND s.BL_CAMPUS_ID IN (SELECT ID FROM organization WHERE parentID = :brenchId )");
			queryCount.append(" AND s.BL_CAMPUS_ID IN (SELECT ID FROM organization WHERE parentID = :brenchId )");
			params.put("brenchId", brenchId);
		}

		if(StringUtils.isNotEmpty(student.getStudyManegerId())){
			if("null".equals(student.getStudyManegerId())){
				query.append(" AND s.STUDY_MANEGER_ID is null ");
				queryCount.append(" AND s.STUDY_MANEGER_ID is null ");
			}else{
				query.append(" AND s.STUDY_MANEGER_ID = :studyManegerId ");
				queryCount.append(" AND s.STUDY_MANEGER_ID = :studyManegerId ");
				params.put("studyManegerId", student.getStudyManegerId());
			}
		}

		if(StringUtils.isNotEmpty(rcourseHour)){
			query.append("  and sam.ONE_ON_ONE_REMAINING_HOUR >= :rcourseHour ");
			queryCount.append("  and sam.ONE_ON_ONE_REMAINING_HOUR >= :rcourseHour ");
			params.put("rcourseHour", rcourseHour);
		}
		if(StringUtils.isNotEmpty(rcourseHourEnd)){
			query.append(" and sam.ONE_ON_ONE_REMAINING_HOUR <= :rcourseHourEnd");
			queryCount.append(" and sam.ONE_ON_ONE_REMAINING_HOUR <= :rcourseHourEnd");
			params.put("rcourseHourEnd", rcourseHourEnd);
		}

		//将用户表与学生表的字段关联，得到学生的家长姓名与电话，11-17

		if(student.getStatus()!=null){//根据状态查询接口
			query.append(" AND s.STATUS = :status ");
			queryCount.append(" AND s.STATUS = :status ");
			params.put("status", student.getStatus());
		}

		if(student.getOneOnOneStatus()!=null){//一对一学生状态
			query.append(" AND s.ONEONONE_STATUS = :oneOnOneStatus ");
			queryCount.append(" AND s.ONEONONE_STATUS = :oneOnOneStatus ");
			params.put("oneOnOneStatus", student.getOneOnOneStatus());
		}

		if(student.getStudentStatus()!=null){
			if("1".equals(student.getStudentStatus())) {
				query.append(" AND (s.STU_STATUS = :studentStatus or s.STU_STATUS is null)");
				queryCount.append(" AND (s.STU_STATUS = :studentStatus or s.STU_STATUS is null)");
				params.put("studentStatus", student.getStudentStatus());
			} else if("0".equals(student.getStudentStatus())) {
				query.append(" AND s.STU_STATUS = :studentStatus ");
				queryCount.append(" AND s.STU_STATUS = :studentStatus ");
				params.put("studentStatus", student.getStudentStatus());
			}
		}else{
			query.append(" AND (s.STU_STATUS ='1' OR s.STU_STATUS is null)");
			queryCount.append(" AND (s.STU_STATUS ='1' OR s.STU_STATUS is null)");
		}

		if(StringUtils.isNotBlank(stuType) && stuType.equals("oneOnOneStudents")){
			query.append(" AND s.ONEONONE_STATUS is not null ");
			queryCount.append(" AND s.ONEONONE_STATUS is not null ");
		}

		if(student.getGradeDict()!=null && StringUtils.isNotEmpty(student.getGradeDict().getId())){
			query.append(" AND s.GRADE_ID = :gradeId ");
			queryCount.append(" AND s.GRADE_ID = :gradeId ");
			params.put("gradeId", student.getGradeDict().getId());
		}
		
		//#701729 增加结课天数
		if(student.getStatus() != null 
				&& StudentStatus.STOP_CLASS !=student.getStatus() 
				&& StudentStatus.FINISH_CLASS !=student.getStatus() 
				&& StringUtils.isNotBlank(modelVo.getStartTime())) {
			dp.setRowCount(0);
			dp.setDatas(new ArrayList<StudentVo>());
			return dp;
		}
		if(student.getStatus() == null || student.getStatus() == StudentStatus.FINISH_CLASS
				|| student.getStatus() == StudentStatus.STOP_CLASS) {

			if(StringUtils.isNotBlank(modelVo.getStartTime())) {
				if("15".equals(student.getFinishTime())) {
					query.append(" AND DATE_FORMAT(s.FINISH_TIME,'%Y-%m-%d')  < DATE_SUB(CURDATE(), INTERVAL :startTime  DAY) ");
					queryCount.append(" AND DATE_FORMAT(s.FINISH_TIME,'%Y-%m-%d')  < DATE_SUB(CURDATE(), INTERVAL :startTime  DAY) ");
					params.put("startTime", modelVo.getStartTime());
				}else {
					query.append(" AND DATE_FORMAT(s.FINISH_TIME,'%Y-%m-%d')  <= DATE_SUB(CURDATE(), INTERVAL :startTime  DAY) ");
					queryCount.append(" AND DATE_FORMAT(s.FINISH_TIME,'%Y-%m-%d')  <= DATE_SUB(CURDATE(), INTERVAL :startTime  DAY) ");
					params.put("startTime", modelVo.getStartTime());
				}
			}
			if(StringUtils.isNotBlank(modelVo.getEndTime())) {
				query.append(" AND DATE_FORMAT(s.FINISH_TIME,'%Y-%m-%d')  > DATE_SUB(CURDATE(), INTERVAL :endTime  DAY) ");
				queryCount.append(" AND DATE_FORMAT(s.FINISH_TIME,'%Y-%m-%d')  > DATE_SUB(CURDATE(), INTERVAL :endTime  DAY) ");
				params.put("endTime", modelVo.getEndTime());
			}
		}

		//学生列表只查询正式学生
		query.append(" and s.STUDENT_TYPE='ENROLLED' ");
		queryCount.append(" and s.STUDENT_TYPE='ENROLLED' ");


		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("stuId","s.id");
		sqlMap.put("manergerId","s.id");
		sqlMap.put("selStuId","(select STUDENT_ID from course where TEACHER_ID = '"+userService.getCurrentLoginUserId()+"' )");
		sqlMap.put("selManerger","(select STUDENT_ID from STUDENT_ORGANIZATION where STUDY_MANAGER_ID = '"+userService.getCurrentLoginUserId()+"' )");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","sql");
		query.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		queryCount.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));


		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			query.append(" order by s."+dp.getSidx()+" "+dp.getSord());
		}


		List<Map<Object, Object>> result=studentDao.findMapOfPageBySql(query.toString(), dp, params);

		if(result.size()==0){
			dp.setRowCount(0);
			dp.setDatas(new ArrayList<StudentVo>());
			return dp;
		}
		if (dp.getPageNo() == 0){
//			dp.setRowCount(studentDao.findCountSql("select count(*) from ( " + query.toString() + " ) countall ", params));
		}
		long time3 = System.currentTimeMillis();
		if (dp.getPageNo() == 0){
			dp.setRowCount(studentDao.findCountSql(queryCount.toString(), params, 300));
		}

		long time4 = System.currentTimeMillis();
		System.out.println("tim4-time3:"+(time4-time3)+"ms");



//		StringBuilder ids_stu = new StringBuilder(128);
//		List<StudentFingerInfo> listmap_finger =null;
//		Map<String, String> map = new HashMap<>();
//		for(int i=0;i<result.size();i++){
//			ids_stu.append("'"+result.get(i).get("ID")+"',");
//		}
//		listmap_finger = studentFingerInfoDao.findAllByHQL(" from StudentFingerInfo where studentId in ("+ids_stu.substring(0, ids_stu.length()-1)+")", new HashMap<String, Object>());
//		for(StudentFingerInfo studentFingerInfo:listmap_finger){
//			map.put(studentFingerInfo.getStudentId(), studentFingerInfo.getFingerInfo());
//		}


		List<StudentVo> studentVos = new ArrayList<>();
		StudentVo studentVo = null ;
		Map<Object, Object> object = null;
		for(int i=0; i<result.size();i++){
			object = result.get(i);
			studentVo = new StudentVo();
			studentVo.setId(StringUtil.toString(object.get("ID")));
			studentVo.setName(StringUtil.toString(object.get("NAME")));
			studentVo.setSex(StringUtil.toString(object.get("SEX")));
			studentVo.setContact(StringUtil.toString(object.get("CONTACT")));
			studentVo.setCreateTime(StringUtil.toString(object.get("CREATE_TIME")));
			studentVo.setSchoolName(StringUtil.toString(object.get("SCHOOL_NAME")));
			studentVo.setSchoolOrTemp(StringUtil.toString(object.get("schoolOrTemp")));
			studentVo.setGradeName(StringUtil.toString(object.get("gradeName")));
            studentVo.setBrenchName(StringUtil.toString(object.get("brenchName")));
            studentVo.setBlCampusName(StringUtil.toString(object.get("blCampusName")));
            studentVo.setStudyManegerName(StringUtil.toString(object.get("studyManagerName")));
			studentVo.setStatus(StringUtil.toString(object.get("STATUS")));
			studentVo.setStudentStatus(StringUtil.toString(object.get("STU_STATUS")));
			studentVo.setSchoolTempId(StringUtil.toString(object.get("schoolTempId")));
			studentVo.setSchoolTempName(StringUtil.toString(object.get("schoolTempName")));
			studentVo.setOneOnOneStatus(StringUtil.toString(object.get("ONE_ON_ONE_STATUS")));
			if(object.get("ONE_ON_ONE_STATUS")!=null){
				studentVo.setOneOnOneStatusName(StudentOneOnOneStatus.valueOf(StringUtil.toString(object.get("ONE_ON_ONE_STATUS"))).getName());
			}
			studentVo.setAttanceNo(StringUtil.toString(object.get("ATTANCE_NO")));
//            if(map.get(studentVo.getId())!=null){
//            	studentVo.setFingerInfo(map.get(studentVo.getId()));
//            }

			studentVo.setDayNumbers(StringUtil.toString(object.get("dayNumbers")));
		    //String cusSql = " SELECT * from customer WHERE ID IN (SELECT CUSTOMER_ID FROM customer_student_relation WHERE STUDENT_ID = '" + studentVo.getId() + "') ";
            String cusSql = " select * from customer c, customer_student_relation csr WHERE c.ID = csr.CUSTOMER_ID and csr.STUDENT_ID = '" + studentVo.getId() + "'";
            List<Customer> customers = customerDao.findBySql(cusSql, new HashMap<String, Object>());
			if (customers != null && customers.size() > 0) {
				Customer latestCustomer = null;
				for(Customer c: customers) {
					if (latestCustomer == null) {
						latestCustomer = c;
					} else {
						Date latesCreateTime = DateTools.getDate(latestCustomer.getCreateTime());
						Date createTime = DateTools.getDate(c.getCreateTime());
						if (createTime.after(latesCreateTime)) {
							latestCustomer = c;
						}
					}
				}
				studentVo.setLatestCustomerId(latestCustomer.getId());
				studentVo.setLatestCustomerName(latestCustomer.getName());
				studentVo.setLatestCustomerContact(latestCustomer.getContact());
			}
			long tr3 = System.currentTimeMillis();
			if (includingAccountInfo) {
				StudnetAccMvVo stuAccMv=getStudentAccoutInfo(studentVo.getId());
				if(stuAccMv.getRemainingAmount()==null)
					stuAccMv.setRemainingAmount(BigDecimal.ZERO);
				if(stuAccMv.getOneOnOneRemainingHour()==null)
					stuAccMv.setOneOnOneRemainingHour(BigDecimal.ZERO);
				if(stuAccMv.getElectronicAccount() == null)
					stuAccMv.setElectronicAccount(BigDecimal.ZERO);
				studentVo.setRemainingAmount(stuAccMv.getRemainingAmount());
				studentVo.setOneOnOneRemainingHour(stuAccMv.getOneOnOneRemainingHour());
				studentVo.setMiniRemainingHour(stuAccMv.getMiniRemainingHour());

				studentVo.setStudentAccount(stuAccMv);
			}
			long tr4 = System.currentTimeMillis();
			logger.debug("一次剩余课时查询耗时:"+(tr4-tr3));

			studentVos.add(studentVo);
		}
		dp.setDatas(studentVos);
		long end = System.currentTimeMillis();
		logger.debug("学生列表总耗时:"+(end-begin));
		return dp;
	}

	@Override
	public void deleteStudent(Student student) {
		studentDao.delete(student);
	}

	@Override
	public void saveOrUpdateStudent(Student student) {
		User user = userService.getCurrentLoginUser();
		student.setModifyTime(DateTools.getCurrentDateTime());
		student.setModifyUserId(user.getUserId());
		if(student.getStudentType() == null ){
			student.setStudentType(StudentType.ENROLLED);
		}
		if(student.getId()==null){
			if(StringUtils.isEmpty(student.getBlCampusId())){
				student.setBlCampusId(userService.getBelongCampus().getId());
			}
			if(student.getStatus()==null)
				student.setStatus(StudentStatus.NEW);
			student.setCreateTime(DateTools.getCurrentDateTime());
			student.setCreateUserId(user.getUserId());
		} else {
			Response res = new Response();
			Student s = studentDao.findById(student.getId());
			if (!s.getContact().equals(student.getContact())) {
//			    if (contractService.checkHasLiveContractByStudent(student.getId())) 存在直播合同的学生 现在是全部学生都需要
			    // 同步到直播
			    if (isContactUsed(student.getContact())) {
                    throw new ApplicationException("该号码已被客户或学生使用");
                }
			    if (!clientHandler.updateStudentContact(student.getId(), student.getContact())) {
			        throw new ApplicationException("同步到晓服务平台出错");
			    }
			}
			//为了处理修改学生的时候学校的问题--开始
			if (student.getSchool()!=null&&student.getSchoolTemp()==null){
				student.setSchoolOrTemp("school");
			}else if (student.getSchool()==null&&student.getSchoolTemp()!=null){
				student.setSchoolOrTemp(s.getSchoolOrTemp());
				student.setSchool(s.getSchool());
			}else if (student.getSchool()==null&&student.getSchoolTemp()==null){
				student.setSchoolOrTemp("school");
				student.setSchool(null);
			}
			student.setSchoolTemp(s.getSchoolTemp());
			//为了处理修改学生的时候学校的问题--结束

			if (null != s.getStudyManeger() && StringUtils.isNotBlank(student.getStudyManegerId()) && !s.getStudyManeger().getUserId().equals(student.getStudyManegerId())) {
				this.checkUpdateStudentManage(student.getId(), s.getStudyManeger().getUserId(), student.getStudyManegerId(),  "changeCourse", res);
				if (res.getResultCode() != 0) {
					throw new ApplicationException(res.getResultMessage());
				}
			}

		}
		if (StringUtils.isNotBlank(student.getAttanceNo())
				&& studentDao.checkIfAttendanceNoExist(student.getId(), student.getAttanceNo(),student.getBlCampusId())) {
			throw new ApplicationException(ErrorCode.STUDENT_ATTENDANCE_NUM_EXIST);
		}

		if (StringUtil.isNotBlank(student.getAddress())){
			String[] strings = null;
			try {
				strings = CustomerMapAnalyzeServiceImpl.doGet(student.getAddress());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (strings!=null){
				student.setLog(strings[0]);
				student.setLat(strings[1]);
			}
		}

		if(student.getId()==null){
			studentDao.save(student);
		} else {
			studentDao.merge(student);
		}
		studentOrganizationDao.saveStudentOrganization(student);
	}

	@Override
	public StudentVo findStudentById(String id) {
		Student student=studentDao.findById(id);
		StudentVo studentVo = HibernateUtils.voObjectMapping(student, StudentVo.class);
		Organization loginOrg = userService.getCurrentLoginUserOrganization();
		String orgId = StringUtil.isNotBlank(loginOrg.getBelong()) ? loginOrg.getBelong() : loginOrg.getId();
		User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(id, orgId);
		studentVo.setCurCampusStudyManagerId(studyManager != null ? studyManager.getUserId() : "");
		Set<Customer> customers = student.getCustomers();

		if (customers != null && customers.size() > 0) {
			Customer latestCustomer = null;
			for(Customer c: customers) {
				if (latestCustomer == null) {
					latestCustomer = c;
				} else {
					Date latesCreateTime = DateTools.getDate(latestCustomer.getCreateTime());
					Date createTime = DateTools.getDate(c.getCreateTime());
					if (createTime.after(latesCreateTime)) {
						latestCustomer = c;
					}
				}
			}
			studentVo.setLatestCustomerId(latestCustomer.getId());
			studentVo.setLatestCustomerName(latestCustomer.getName());
			studentVo.setLatestCustomerContact(latestCustomer.getContact());
		}

		List<StudentFingerInfo> studentFingerList=studentFingerInfoDao.findByCriteria(Expression.eq("studentId", id));
		if(studentFingerList!=null && studentFingerList.size()>0)
			studentVo.setFingerInfo(studentFingerList.get(0).getFingerInfo());
		studentVo.setOneOnOneAlreadyArrangeHour(courseDao.countOneOnOneNotChargeCourse(id));

		return studentVo;
	}

	@Override
	public void updateStudentManeger(String studentIds, String oldStudyManagerId, String newStudyManagerId) {
		if(StringUtils.isEmpty(studentIds)){
			throw new ApplicationException(ErrorCode.PARAMETER_EMPTY);
		}

		Response res = new Response();
		res = checkUpdateStudentManage(studentIds, oldStudyManagerId, newStudyManagerId, "changeCourse", res);
		if (res.getResultCode() != 0) {
			throw new ApplicationException(res.getResultMessage());
		}

//		List<Criterion> criterionList = new ArrayList<Criterion>();
//		SimpleExpression studyManagerLike = Expression.like("studyManager.userId", oldStudyManagerId);//旧学管
//		SimpleExpression statusLike = Expression.like("courseStatus", CourseStatus.TEACHER_ATTENDANCE);//老师已考勤
//		SimpleExpression courseDateLike = Expression.like("courseDate", DateTools.getCurrentDate());//日期是今天的
//		criterionList.add(Expression.or(statusLike, courseDateLike));
//		criterionList.add(studyManagerLike);
//		List courselist=courseDao.findAllByCriteria(criterionList);//找到老师已考勤且是旧血管的课程
//		if(courselist.size()>0){
//			throw new ApplicationException("要先考勤完今天和以前的课程才能换学管。");
//		}
		studentDao.updateStudentManeger(studentIds, oldStudyManagerId, newStudyManagerId);
	}

	public List<StudentVo> findStudentsByNameOrId(String term) {
		SimpleExpression contactLike = Expression.like("id", term, MatchMode.ANYWHERE);
		SimpleExpression nameLike = Expression.like("name", term, MatchMode.ANYWHERE);
		List<Student> studentList = (List<Student>)studentDao.findPageByCriteria(new DataPackage(0, 20), new ArrayList<Order>(), Expression.or(contactLike, nameLike)).getDatas();
		return HibernateUtils.voListMapping(studentList, StudentVo.class);
	}

	@Override
	public List<StudentVo> findStudentByCourseRequirement(String term,String orgId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(term)) {
			SimpleExpression contactLike = Expression.like("student.id", term, MatchMode.ANYWHERE);
			SimpleExpression nameLike = Expression.like("student.name", term, MatchMode.ANYWHERE);
			criterionList.add(Expression.or(contactLike, nameLike));
		}
		criterionList.add(Expression.eq("requirementStatus", CourseRequirementStatus.WAIT_FOR_ARRENGE));
		criterionList.add(Expression.eq("student.blCampusId", orgId));
		List<CourseRequirement> list = courseRequirementDao.findAllByCriteria(criterionList);
		List<StudentVo> studentList=new  ArrayList<StudentVo>();
		if (list != null){
			List<String> listId=new ArrayList<String>();
			for (CourseRequirement couReq : list) {
				if(couReq.getStudent()!=null && !listId.contains(couReq.getStudent().getId())){
					listId.add(couReq.getStudent().getId());
					studentList.add((StudentVo) HibernateUtils.voObjectMapping(couReq.getStudent(), StudentVo.class) );
				}
			}
		}
		return studentList;
	}

	/**
	 * 根据学生id查询未排课排课需求
	 * @param studentId
	 * @return
	 */
	public List<Map<String,String>> getCourseRequirementByStudentIdAndWaitArrenge(String studentId){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("student.id", studentId));
		criterionList.add(Expression.eq("requirementStatus", CourseRequirementStatus.WAIT_FOR_ARRENGE));
		List<CourseRequirement> couReqList=courseRequirementDao.findAllByCriteria(criterionList);
		if(couReqList!=null && couReqList.size()>0){
			for(CourseRequirement c : couReqList){
				Map<String,String> map=new HashMap<String, String>();
				map.put("id", c.getId());
				map.put("courseDateDesciption", c.getCourseDateDesciption());
				map.put("createUserName", c.getCreateUser().getName());
				if (c.getStudyManager() != null) {
					map.put("studyManagerName", c.getStudyManager().getName());
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 根据学生id 查询课程概要
	 *@param studentId
	 *@return
	 */
	@Override
    @Transactional(readOnly = true)
	public List<CourseSummarySearchResultVo> getCourseSummaryByStudentId(String studentId,String endDate) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
        criterionList.add(Restrictions.eq("delFlag", 0)); // 只查询未逻辑删除的
		criterionList.add(Expression.eq("student.id", studentId));
        criterionList.add(Expression.eq("type", CourseSummaryType.STUDENT));
        if(StringUtil.isNotEmpty(endDate)){
        	criterionList.add(Expression.ge("endDate", endDate));//课程结束日期大于这个日期的
        }
		List<Order> orderList=new ArrayList<Order>();
		orderList.add(Order.desc("startDate"));
		List<CourseSummary> couSumList=courseSummaryDao.findAllByCriteria(criterionList,orderList);
        for (CourseSummary summary : couSumList) {
            if(summary.getSpanHours() == null)
                summary.setSpanHours(courseDao.getCourseListByCourseSummaryId(summary.getCourseSummaryId()).getRowCount() * (summary.getPlanHours()==null?summary.getSpanHours():summary.getPlanHours()));
        }
        return HibernateUtils.voListMapping(couSumList, CourseSummarySearchResultVo.class);
	}

	@Override
	public String saveNewStudent(String stuName, String gradeId, String customerId) {
		return saveNewStudent(stuName, gradeId, customerId, "", "");
	}

	@Override
	public String saveNewStudent(String stuName, String gradeId, String customerId, String studyManagerId, String schoolId,String blCampusId) {
		Student student =  new Student();
		DataDict gradeDict = dataDictDao.findById(gradeId);
		student.setGradeDict(gradeDict);
		student.setStudentType(StudentType.ENROLLED);

		if(StringUtils.isEmpty(student.getName()))
			student.setName(stuName);
		if(student.getGradeDict()==null || StringUtils.isEmpty(student.getGradeDict().getId()))
			if(StringUtils.isNotEmpty(gradeId))
				student.setGradeDict(new DataDict(gradeId));

		if(StringUtils.isNotBlank(blCampusId)){
			student.setBlCampusId(blCampusId);
		}else{
			student.setBlCampusId(userService.getBelongCampus().getId());
		}

		student.setStatus(StudentStatus.NEW);

		User user = userService.getCurrentLoginUser();
		student.setModifyTime(DateTools.getCurrentDateTime());
		student.setModifyUserId(user.getUserId());
		if(student.getId()==null){
			student.setCreateTime(DateTools.getCurrentDateTime());
			student.setCreateUserId(user.getUserId());
		}

		Customer cus = customerDao.findById(customerId);

		cus.setDealStatus(CustomerDealStatus.SIGNEUP);
		cus.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
		cus.setDeliverTarget(user.getUserId());
		cus.setDeliverTargetName(user.getName());

		cus.getStudents().add(student);

		if (StringUtils.isNotEmpty(studyManagerId)) {
			student.setStudyManegerId(studyManagerId);
		}
		if (StringUtils.isNotEmpty(schoolId)) {
			student.setSchool(new StudentSchool(schoolId));
		}
        student.setSchoolOrTemp("school");
        student.setContact(cus.getContact());
		studentDao.save(student);
		studentOrganizationDao.saveStudentOrganization(student);
		customerDao.save(cus);
		return student.getId();
	}

	@Override
	public String saveNewStudent(String stuName, String gradeId, String customerId, String studyManagerId, String schoolId) {
		return saveNewStudent(stuName,gradeId,customerId,studyManagerId,schoolId,"");
	}


	/**
	 * 查找学生列表（自动搜索）
	 */
	public List<Map<Object,Object>> getStudentAutoComplate(String input) {
//		Organization organization=userService.getBelongCampus();
		List<Organization> orgs = userService.getCurrentLoginUser().getOrganization();
		return studentDao.getStudentAutoComplate(input, orgs);
	}

	/**
	 *实际收款
	 * @param studentId
	 * @return
	 */
	private BigDecimal getStudentRealPaidAmount(String studentId){
//		String sql =" select sum (case when FUNDS_PAY_TYPE='RECEIPT' then TRANSACTION_AMOUNT when FUNDS_PAY_TYPE='WASH' then (-TRANSACTION_AMOUNT) end ) from funds_change_history where CHANNEL in ('CASH', 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER')  and STUDENT_ID = '"+studentId+"' ";
		Map<String, Object> params = new HashMap<String, Object>();
		String hql =" select sum (case when fundsPayType = 'RECEIPT' then transactionAmount when fundsPayType ='WASH' then -transactionAmount end) from FundsChangeHistory where channel in ('CASH', 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER')  and STUDENT_ID = :studentId ";
		params.put("studentId", studentId);
		double realPaidAmount = fundsChangeHistoryDao.findSumHql(hql, params);
		BigDecimal result = BigDecimal.valueOf(realPaidAmount);
		return result;
	}

	/**
	 * 电子收款
	 * @param studentId
	 * @return
	 */
	private BigDecimal getStudentELECTRONICPaidAmount(String studentId){
		Map<String, Object> params = new HashMap<String, Object>();
		String hql =" select sum (case when fundsPayType = 'RECEIPT' then transactionAmount when fundsPayType ='WASH' then -transactionAmount end) from FundsChangeHistory where channel ='ELECTRONIC_ACCOUNT'  and STUDENT_ID = :studentId ";
		params.put("studentId", studentId);
		double electronic = fundsChangeHistoryDao.findSumHql(hql, params);
		BigDecimal result = BigDecimal.valueOf(electronic);
		return result;
	}


	/**
	 * 查询学生帐户情况, 除电子账户外，其他均实时计算(现在电子账户金额也要加入到总资金中tangyuping)
	 * @param studentId
	 * @return
	 */
	public StudnetAccMvVo getStudentAccoutInfo(String studentId) {
		StudnetAccMv account = studnetAccMvDao.getStudnetAccMvByStudentId(studentId);
		if (account == null) {
			account = new StudnetAccMv();
		}
		StudnetAccMvVo accVo = HibernateUtils.voObjectMapping(account, StudnetAccMvVo.class);

		//查询所有有效合同产品
		List<Criterion> produceCriterionList = new ArrayList<Criterion>();
		produceCriterionList.add(Expression.eq("contract.student.id", studentId));
//		produceCriterionList.add(Expression.in("status", new ContractProductStatus[]{ContractProductStatus.NORMAL, ContractProductStatus.STARTED}));
		List<ContractProduct> allContractProduces = contractProductDao.findAllByCriteria(produceCriterionList);

		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal paidAmount = BigDecimal.ZERO;

		BigDecimal realPaidAmount = BigDecimal.ZERO;
		BigDecimal electronicPaidAmount = BigDecimal.ZERO;

		BigDecimal promotinAmount = BigDecimal.ZERO;
		BigDecimal consumeAmount = BigDecimal.ZERO;
		BigDecimal remainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
		BigDecimal oneOnOnePromotinAmount = BigDecimal.ZERO;
		BigDecimal miniRemainingAmount = BigDecimal.ZERO;
		BigDecimal miniRemainingHour = BigDecimal.ZERO;
		BigDecimal otherConsumeAmount = BigDecimal.ZERO;
		BigDecimal ecsRemainingAmount = BigDecimal.ZERO;
		BigDecimal lectureRemainingAmount = BigDecimal.ZERO;  //讲座剩余资金(元)
		BigDecimal lectureRemainingHour = BigDecimal.ZERO;//讲座剩余课时



		BigDecimal oooRemainingPromotion = BigDecimal.ZERO;//一对一剩余金额
		BigDecimal oooRemainingPromotionHour = BigDecimal.ZERO;//一对一剩余课时
		BigDecimal miniRemainingPromotion = BigDecimal.ZERO;//小班剩余金额
		BigDecimal miniRemainingPromotionHour = BigDecimal.ZERO;//小班剩余课时

		BigDecimal ecsPromotinAmount = BigDecimal.ZERO;//目标班优惠金额

		BigDecimal otmRemainingAmount = BigDecimal.ZERO; // 一对多剩余资金
		BigDecimal otmRemainingHour = BigDecimal.ZERO; // 一对多剩余课时
		BigDecimal otmRemainingPromotion = BigDecimal.ZERO;// 一对多剩余优惠金额
		BigDecimal otmRemainingPromotionHour = BigDecimal.ZERO;//一对多剩余优惠课时

		BigDecimal twoTeacherRemainingAmount=BigDecimal.ZERO;//双师剩余金额
		BigDecimal twoTeacherRemainingHour=BigDecimal.ZERO;//双师剩余课时

		if (allContractProduces.size() > 0) {
			for (ContractProduct cp : allContractProduces) {
				if(cp.getPromotionAmount()==null)
					cp.setPromotionAmount(BigDecimal.ZERO);
//				paidAmount = paidAmount.add(cp.getPaidAmount());
				consumeAmount = consumeAmount.add(cp.getConsumeAmount());
				remainingAmount = remainingAmount.add(cp.getRemainingAmount());
				promotinAmount = promotinAmount.add(cp.getPromotionAmount());
//				totalAmount = totalAmount.add(cp.getPlanAmount());
				switch(cp.getType()) {
				case ONE_ON_ONE_COURSE:
					oneOnOneRemainingAmount = oneOnOneRemainingAmount.add(cp.getRemainingAmount());
					oooRemainingPromotion=oooRemainingPromotion.add(cp.getRemainingAmountOfPromotionAmount());
					if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
						oneOnOneRemainingHour = oneOnOneRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
						oooRemainingPromotionHour=oooRemainingPromotionHour.add(cp.getRemainingAmountOfPromotionAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
					}
					oneOnOnePromotinAmount = oneOnOnePromotinAmount.add(cp.getPromotionAmount());

					break;
				case SMALL_CLASS:
					miniRemainingAmount = miniRemainingAmount.add(cp.getRemainingAmount());
					miniRemainingPromotion=miniRemainingPromotion.add(cp.getRemainingAmountOfPromotionAmount());
					if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
						miniRemainingHour = miniRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
						miniRemainingPromotionHour=miniRemainingPromotionHour.add(cp.getRemainingAmountOfPromotionAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
					}
					break;
				case ONE_ON_MANY:
					otmRemainingAmount = otmRemainingAmount.add(cp.getRemainingAmount());
					otmRemainingPromotion=otmRemainingPromotion.add(cp.getRemainingAmountOfPromotionAmount());
					if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
						otmRemainingHour = otmRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
						otmRemainingPromotionHour=otmRemainingPromotionHour.add(cp.getRemainingAmountOfPromotionAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
					}
					break;
				case ECS_CLASS:
					ecsRemainingAmount = ecsRemainingAmount.add(cp.getRemainingAmount());
					ecsPromotinAmount = ecsPromotinAmount.add(cp.getRemainingAmountOfPromotionAmount());
					break;
				case OTHERS:
					otherConsumeAmount = otherConsumeAmount.add(cp.getRemainingAmount());
					break;
				case LECTURE:
					lectureRemainingAmount = lectureRemainingAmount.add(cp.getRemainingAmount());
					if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
						lectureRemainingHour = lectureRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
					}
				case TWO_TEACHER:
					twoTeacherRemainingAmount=twoTeacherRemainingAmount.add(cp.getRemainingAmount());
					if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
						twoTeacherRemainingHour = twoTeacherRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
					}
				}
			}
//			totalAmount = totalAmount.add(paidAmount).add(promotinAmount);

		}

//		accVo.setTotalAmount(totalAmount);
//		accVo.setPaidAmount(paidAmount);
		if(accVo.getElectronicAccount()!=null && accVo.getElectronicAccount().compareTo(BigDecimal.ZERO)>0){
			accVo.setRemainingAmount(remainingAmount.add(accVo.getElectronicAccount()));
		}else{
			accVo.setRemainingAmount(remainingAmount);
		}

		accVo.setOneOnOneRemainingAmount(oneOnOneRemainingAmount);
		accVo.setOneOnOneRemainingHour(oneOnOneRemainingHour);
		accVo.setMiniRemainingAmount(miniRemainingAmount);
		accVo.setMiniRemainingHour(miniRemainingHour);
		accVo.setOtherConsumeAmount(otherConsumeAmount);
		accVo.setEcsRemainingAmount(ecsRemainingAmount);
		accVo.setPromotinAmount(promotinAmount);
		//add by Yao   2015-11-13    管理端APP使用
		accVo.setOooRemainingPromotion(oooRemainingPromotion);
		accVo.setOooRemainingPromotionHour(oooRemainingPromotionHour);
		accVo.setMiniRemainingPromotion(miniRemainingPromotionHour);
		accVo.setMiniRemainingPromotionHour(miniRemainingPromotionHour);
		accVo.setEcsPromotinAmount(ecsPromotinAmount);

		// add by lixuejun 2016-01-13 一对多
		accVo.setOtmRemainingAmount(otmRemainingAmount);
		accVo.setOtmRemainingHour(otmRemainingHour);
		accVo.setOtmRemainingPromotion(otmRemainingPromotion);
		accVo.setOtmRemainingPromotionHour(otmRemainingPromotionHour);

		// 讲座
		accVo.setLectureRemainingAmount(lectureRemainingAmount);
		accVo.setLectureRemainingHour(lectureRemainingHour);

		//统计待付款
		List<Criterion> contractCriterionList = new ArrayList<Criterion>();
		contractCriterionList.add(Expression.eq("student.id", studentId));
//		contractCriterionList.add(Expression.in("paidStatus", new ContractPaidStatus[]{ContractPaidStatus.PAYING, ContractPaidStatus.UNPAY}));
		List<Contract> contarctList = contractDao.findAllByCriteria(contractCriterionList);
		BigDecimal totalPaddingAmount = BigDecimal.ZERO;
		BigDecimal availableAmount = BigDecimal.ZERO;
		for (Contract contract : contarctList) {
			contractService.calculateContractDomain(contract);
			if (contract.getPendingAmount().compareTo(BigDecimal.ZERO) > 0) {
				totalPaddingAmount = totalPaddingAmount.add(contract.getPendingAmount());
			}
			totalAmount = totalAmount.add(contract.getTotalAmount());
			paidAmount = paidAmount.add(contract.getPaidAmount());
		}
		accVo.setPaidAmount(paidAmount);
		accVo.setRealPaidAmount(getStudentRealPaidAmount(studentId));
		accVo.setElectronicPaidAmount(getStudentELECTRONICPaidAmount(studentId));
		accVo.setTotalAmount(totalAmount);
		accVo.setAvailableAmount(availableAmount);
		accVo.setArrangedAmount(totalPaddingAmount.subtract(availableAmount));
		accVo.setPlanAmount(totalPaddingAmount);
		accVo.setTwoTeacherRemainingAmount(twoTeacherRemainingAmount);
		accVo.setTwoTeacherRemainingHour(twoTeacherRemainingHour);

		return accVo;
	}

	public BigDecimal withDrawElectronicAmount(String studentId, BigDecimal withDrawAmount) {
		StudnetAccMv account = studnetAccMvDao.getStudnetAccMvByStudentId(studentId);
		if (account == null) {
			throw new ApplicationException("找不到学生账户信息。");
		}
		if (withDrawAmount != null && withDrawAmount.compareTo(account.getElectronicAccount()) > 0) {
			throw new ApplicationException("提取金额为空可大于学生电子账户余额。");
		}
		//扣费提取的钱
		account.setElectronicAccount(account.getElectronicAccount().subtract(withDrawAmount));
		electronicAccountChangeLogDao.saveElecAccChangeLog(account.getStudentId(), ElectronicAccChangeType.TRANSFER_OUT, "D", withDrawAmount, account.getElectronicAccount(), "提取电子账户余额");
		contractService.updateStudentAccountInfoAmount(account.getStudentId(),withDrawAmount,ElectronicAccChangeType.TRANSFER_OUT,null);//平行账户
		// 生成一条 退费记录在收入表里面
		Student student =  studentDao.findById(studentId);
		fundsChangeHistoryDao.saveOneFundRecord( BigDecimal.ZERO.subtract(withDrawAmount), PayWay.REFUND_MONEY, student);

		return account.getElectronicAccount();
	}
	@Override
	public BigDecimal putMoneyIncome(String studentId, BigDecimal withDrawAmount, String reason) {
		StudnetAccMv account = studnetAccMvDao.getStudnetAccMvByStudentId(studentId);
		if (account == null) {
			throw new ApplicationException("找不到学生账户信息。");
		}
		BigDecimal frozenAmount = account.getFrozenAmount() != null ? account.getFrozenAmount() : BigDecimal.ZERO;
		BigDecimal maxCanUsedAmount = account.getElectronicAccount().subtract(frozenAmount);
		if (withDrawAmount != null && withDrawAmount.compareTo(maxCanUsedAmount) > 0) {
			if (frozenAmount.compareTo(BigDecimal.ZERO) > 0) {
				throw new ApplicationException("提取金额为空或大于学生电子账户最大使用金额,其中冻结金额为" + frozenAmount.longValue());
			} else {
				throw new ApplicationException("提取金额为空或大于学生电子账户最大使用金额。");
			}
		}

		//扣费划归的钱
		account.setElectronicAccount(account.getElectronicAccount().subtract(withDrawAmount));
		String logRemark = "划归收入";
		if (StringUtil.isNotBlank(reason)) {
			logRemark += "(" + reason + ")";
		}
		contractService.updateStudentAccountInfoAmount(account.getStudentId(),withDrawAmount,ElectronicAccChangeType.NORMAL_INCOME_OUT,null);//平行账户
		electronicAccountChangeLogDao.saveElecAccChangeLog(account.getStudentId(), ElectronicAccChangeType.NORMAL_INCOME_OUT, "D", withDrawAmount, account.getElectronicAccount(), logRemark);

		// 生成一条 退费记录在收入表里面
		Student student =  studentDao.findById(studentId);
		fundsChangeHistoryDao.saveOneFundRecord( BigDecimal.ZERO.subtract(withDrawAmount), PayWay.IS_NORMAL_INCOME, student);
		//生产一条划归收入记录
		AccountChargeRecords record =  new AccountChargeRecords();
		String transactionId = UUID.randomUUID().toString();
		record.setTransactionId(transactionId);
		record.setTransactionTime(DateTools.getCurrentDateTime());
		record.setPayType(PayType.REAL);
		record.setAmount(withDrawAmount);
		record.setStudent(student);
		record.setOperateUser(userService.getCurrentLoginUser());
		record.setPayTime(DateTools.getCurrentDateTime());
		record.setQuality(BigDecimal.ZERO);
		record.setChargeType(ChargeType.IS_NORMAL_INCOME);
		record.setChargePayType(ChargePayType.CHARGE);
		Organization belongCampus= student.getBlCampus();


//		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())){
//			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
//		}
		record.setBlCampusId(belongCampus);
		accountChargeRecordsDao.save(record);

		IncomeMessage message=new IncomeMessage();
		message.setCampusId(belongCampus.getId());
		message.setAmount(withDrawAmount);
		message.setChargePayType(ChargePayType.CHARGE);
		message.setChargeType(ChargeType.IS_NORMAL_INCOME);
		message.setPayType(PayType.REAL);
		message.setCountDate(DateTools.getCurrentDateTime());

		try {// 加入队列
			JedisUtil.lpush(ObjectUtil.objectToBytes("incomeQueue"),ObjectUtil.objectToBytes(message));
		} catch (Exception e) {
			// 错了
		}

		return account.getElectronicAccount();
	}
//	@Override
//	public BigDecimal withDrawElectronicAmount(String studentId,
//		   BigDecimal withDrawAmount, String returnReason, String returnType,
//		   String cbsIdA, String userIdA, String bonusA, String bonusAA,
//		   String cbsIdB, String userIdB, String bonusB, String bonusBB,
//		   String cbsIdC, String userIdC, String bonusC, String bonusCC,
//		   String schoolA, String schoolB, String schoolC,String contractProductId) {
//
//		StudnetAccMv account = studnetAccMvDao.getStudnetAccMvByStudentId(studentId);
//		if (account == null) {
//			throw new ApplicationException("找不到学生账户信息。");
//		}
//		if (withDrawAmount != null && withDrawAmount.compareTo(account.getElectronicAccount()) > 0) {
//			throw new ApplicationException("提取金额为空可大于学生电子账户余额。");
//		}
//		//扣费提取的钱
//		account.setElectronicAccount(account.getElectronicAccount().subtract(withDrawAmount));
//		electronicAccountChangeLogDao.saveElecAccChangeLog(account.getStudentId(), ElectronicAccChangeType.TRANSFER_OUT, "D", withDrawAmount, account.getElectronicAccount(), "提取电子账户余额");
//
//		// 生成一条 退费记录在收入表里面
//		Student student =  studentDao.findById(studentId);
////		fundsChangeHistoryDao.saveOneFundRecord( BigDecimal.ZERO.subtract(withDrawAmount), PayWay.REFUND_MONEY, student);
//		ContractProduct contractProduct=null;
//		if(StringUtil.isNotBlank(contractProductId))
//			contractProduct=contractProductDao.findById(contractProductId);
//		//扣费记录
//		FundsChangeHistory fundsChangeHistory = new FundsChangeHistory();
//		fundsChangeHistory.setRemark("");
//		fundsChangeHistory.setTransactionAmount(BigDecimal.ZERO.subtract(withDrawAmount));
//		fundsChangeHistory.setTransactionTime(DateTools.getCurrentDateTime());
//		if(contractProduct!=null)
//			fundsChangeHistory.setContract(contractProduct.getContract());
//		fundsChangeHistory.setChannel(PayWay.REFUND_MONEY);
//		fundsChangeHistory.setStudent(student);
//		fundsChangeHistory.setChargeBy(userService.getCurrentLoginUser());
//		fundsChangeHistoryDao.save(fundsChangeHistory);
//		fundsChangeHistoryDao.flush();
//
//		//退费信息
//		StudentReturnFee srf=new StudentReturnFee();
//		srf.setCampus(userService.getBelongCampus());
//		srf.setCreateTime(DateTools.getCurrentDateTime());
//		srf.setCreateUser(userService.getCurrentLoginUser());
//		srf.setStudent(student);
//		srf.setReturnAmount(withDrawAmount);
//		srf.setReturnReason(returnReason);
//		srf.setFundsChangeHistory(fundsChangeHistory);
//		if(contractProduct!=null){
//			srf.setContractProduct(contractProduct);
//			srf.setContract(contractProduct.getContract());
//		}
//		if(StringUtil.isNotBlank(returnType))
//			srf.setReturnType(new DataDict(returnType));
//		studentReturnService.saveStudentReturn(srf);
//
//		RefundIncomeDistributeVo refundIncomeDistributeVo = new RefundIncomeDistributeVo();
//
//		contractService.saveContractBonus(fundsChangeHistory.getId(), BonusType.FEEDBACK_REFUND.toString(),srf.getId(),contractProduct, refundIncomeDistributeVo);
//
//		return account.getElectronicAccount();
//	}

	/**
	 * 结课退费（不经电子账户）
	 */
	@Override
	public void closeContractProduct(String studentId,
									 BigDecimal returnNormalAmount, BigDecimal returnSpecialAmount, BigDecimal returnPromotionAmount,
									 BigDecimal withDrawAmount, String returnReason, String returnType,
									 String contractProductId,
									 MultipartFile certificateImageFile, String accountName, String account_from, String returnCampusId, String returnUserId, List<IncomeDistributionVo> list, LiveContractProductRefundVo liveContractProductRefundVo) {

		// 生成一条 退费记录在收入表里面
		Student student =  studentDao.findById(studentId);
//		fundsChangeHistoryDao.saveOneFundRecord( BigDecimal.ZERO.subtract(withDrawAmount), PayWay.REFUND_MONEY, student);
		ContractProduct contractProduct=null;
		if(StringUtil.isNotBlank(contractProductId))
			contractProduct=contractProductDao.findById(contractProductId);
		//扣费记录
		FundsChangeHistory fundsChangeHistory = new FundsChangeHistory();
		fundsChangeHistory.setRemark("");
		fundsChangeHistory.setTransactionAmount(BigDecimal.ZERO.subtract(withDrawAmount));
		fundsChangeHistory.setTransactionTime(DateTools.getCurrentDateTime());
		if(contractProduct!=null)
			fundsChangeHistory.setContract(contractProduct.getContract());
		fundsChangeHistory.setChannel(PayWay.REFUND_MONEY);
		fundsChangeHistory.setStudent(student);
		fundsChangeHistory.setFundBlCampusId(student.getBlCampusId());//用学生的主校区
		fundsChangeHistory.setChargeBy(userService.getCurrentLoginUser());
		fundsChangeHistoryDao.save(fundsChangeHistory);


		if(certificateImageFile != null && certificateImageFile.getSize()>0){
			String fileName=certificateImageFile.getOriginalFilename().substring(0,certificateImageFile.getOriginalFilename().lastIndexOf("."));//上传的文件名
			String puff=certificateImageFile.getOriginalFilename().replace(fileName, "");
			String aliName = "CERTIFICATE_" + fundsChangeHistory.getId()+puff;//阿里云上面的文件名
			fundsChangeHistory.setCertificateImage(aliName);
			this.saveFileToRemoteServer(certificateImageFile, aliName);
			fundsChangeHistory.setCertificateImage(aliName);
		}

		fundsChangeHistoryDao.flush();

		//退费信息
		StudentReturnFee srf=new StudentReturnFee();
		Organization campus = student.getBlCampus();
		if (StringUtil.isNotBlank(returnCampusId)) {
			campus = organizationDao.findById(returnCampusId);
		}
		srf.setCampus(campus);
		if (liveContractProductRefundVo!=null&&StringUtil.isNotBlank(liveContractProductRefundVo.getCreateTime())){
			srf.setCreateTime(liveContractProductRefundVo.getCreateTime());
		}else {
			srf.setCreateTime(DateTools.getCurrentDateTime());
		}
		User createUser = userService.getCurrentLoginUser();
		if (StringUtil.isNotBlank(returnUserId)) {
			createUser = userService.findUserById(returnUserId);
		}
		srf.setCreateUser(createUser);
		srf.setStudent(student);
		srf.setReturnAmount(withDrawAmount.add(returnSpecialAmount));
		srf.setReturnSpecialAmount(returnSpecialAmount);
		srf.setReturnNormalAmount(returnNormalAmount);
		srf.setReturnPromotionAmount(returnPromotionAmount);
		srf.setReturnReason(returnReason);
		srf.setFundsChangeHistory(fundsChangeHistory);
		srf.setAccountName(accountName);
		srf.setAccount(account_from);
		if(contractProduct!=null){
			srf.setContractProduct(contractProduct);
			srf.setContract(contractProduct.getContract());
		}
		if(StringUtil.isNotBlank(returnType))
			srf.setReturnType(new DataDict(returnType));
		studentReturnService.saveStudentReturn(srf);

//		contractService.saveContractBonus(fundsChangeHistory.getId(), userIdA, bonusA, bonusAA, cbsIdA, cbsIdB, userIdB, bonusB, bonusBB, cbsIdC, userIdC, bonusC, bonusCC, schoolA, schoolB, schoolC,BonusType.FEEDBACK_REFUND.toString(),srf.getId(),contractProduct);

//		List<IncomeDistributionVo>  list = new ArrayList<>();
		
		if (list != null && !list.isEmpty()) {
		    contractService.saveIncomeDistributionForCloseContractProduct(fundsChangeHistory, list, srf.getId(), contractProduct);
		}

		if(!contractProduct.getType().equals(ProductType.LIVE)){
			FinanceMessage msg= new FinanceMessage();
			msg.setOnline(true);
			msg.setCampusId(student.getBlCampusId());
			msg.setCountDate(DateTools.getCurrentDate());
			msg.setRefundMoney(withDrawAmount.add(returnSpecialAmount));
			msg.setFlag("1");
			try {// 加入队列
				JedisUtil.lpush(ObjectUtil.objectToBytes(CommonUtil.FINANCE_QUEUE),ObjectUtil.objectToBytes(msg));
			} catch (Exception e) {
				// 错了
			}
		}
	}

	/**
	 *
	 * @param studentId
	 * @param returnNormalAmount 退实际金额
	 * @param returnPromotionAmount 退优惠
	 * @param withDrawAmount 退总计金额
	 * @param returnReason
	 * @param returnType
	 * @param contractProductId
	 * @param refundIncomeDistributeVo
	 * @return
	 */
	@Override
	public BigDecimal withDrawElectronicAmount(String studentId,
											   BigDecimal returnNormalAmount, BigDecimal returnSpecialAmount, BigDecimal returnPromotionAmount,
											   BigDecimal withDrawAmount, String returnReason, String returnType,
											   String contractProductId,
											   MultipartFile certificateImageFile, String accountName, String account_from, String returnCampusId, String returnUserId, RefundIncomeDistributeVo refundIncomeDistributeVo) {


		StudnetAccMv account = studnetAccMvDao.getStudnetAccMvByStudentId(studentId);
		if (account == null) {
			throw new ApplicationException("找不到学生账户信息。");
		}
		if (withDrawAmount != null && withDrawAmount.compareTo(account.getElectronicAccount()) > 0) {
			throw new ApplicationException("提取金额为不可大于学生电子账户余额。");
		}
		//扣费提取的钱
		account.setElectronicAccount(account.getElectronicAccount().subtract(withDrawAmount));
		electronicAccountChangeLogDao.saveElecAccChangeLog(account.getStudentId(), ElectronicAccChangeType.REFUND_OUT, "D", withDrawAmount, account.getElectronicAccount(), "提取电子账户余额");
		contractService.updateStudentAccountInfoAmount(account.getStudentId(),withDrawAmount,ElectronicAccChangeType.REFUND_OUT,null);//平行账户

		// 生成一条 退费记录在收入表里面
		Student student =  studentDao.findById(studentId);
//		fundsChangeHistoryDao.saveOneFundRecord( BigDecimal.ZERO.subtract(withDrawAmount), PayWay.REFUND_MONEY, student);
		ContractProduct contractProduct=null;
		if(StringUtil.isNotBlank(contractProductId))
			contractProduct=contractProductDao.findById(contractProductId);
		//扣费记录
		FundsChangeHistory fundsChangeHistory = new FundsChangeHistory();
		fundsChangeHistory.setRemark("");
		fundsChangeHistory.setTransactionAmount(BigDecimal.ZERO.subtract(withDrawAmount));
		fundsChangeHistory.setTransactionTime(DateTools.getCurrentDateTime());
		if(contractProduct!=null)
			fundsChangeHistory.setContract(contractProduct.getContract());
		fundsChangeHistory.setChannel(PayWay.REFUND_MONEY);
		fundsChangeHistory.setStudent(student);
		fundsChangeHistory.setFundBlCampusId(student.getBlCampusId());//用学生的主校区
		fundsChangeHistory.setChargeBy(userService.getCurrentLoginUser());
		fundsChangeHistoryDao.save(fundsChangeHistory);


		if(certificateImageFile != null && certificateImageFile.getSize()>0){
			String fileName=certificateImageFile.getOriginalFilename().substring(0,certificateImageFile.getOriginalFilename().lastIndexOf("."));//上传的文件名
			String puff=certificateImageFile.getOriginalFilename().replace(fileName, "");
			String aliName = "CERTIFICATE_" + fundsChangeHistory.getId()+puff;//阿里云上面的文件名
			fundsChangeHistory.setCertificateImage(aliName);
			this.saveFileToRemoteServer(certificateImageFile, aliName);
			fundsChangeHistory.setCertificateImage(aliName);
		}

		fundsChangeHistoryDao.flush();

		//退费信息
		StudentReturnFee srf=new StudentReturnFee();
		Organization campus = student.getBlCampus();
		if (StringUtil.isNotBlank(returnCampusId)) {
			campus = organizationDao.findById(returnCampusId);
		}
		srf.setCampus(campus);
		srf.setCreateTime(DateTools.getCurrentDateTime());
		User createUser = userService.getCurrentLoginUser();
		if (StringUtil.isNotBlank(returnUserId)) {
			createUser = userService.findUserById(returnUserId);
		}
		srf.setReturnSpecialAmount(returnSpecialAmount);
		srf.setCreateUser(createUser);
		srf.setStudent(student);
		srf.setReturnAmount(withDrawAmount.add(returnSpecialAmount));
		srf.setReturnNormalAmount(returnNormalAmount);
		srf.setReturnPromotionAmount(returnPromotionAmount);
		srf.setReturnReason(returnReason);
		srf.setFundsChangeHistory(fundsChangeHistory);
		srf.setAccountName(accountName);
		srf.setAccount(account_from);
		if(contractProduct!=null){
			srf.setContractProduct(contractProduct);
			srf.setContract(contractProduct.getContract());
		}
		if(StringUtil.isNotBlank(returnType))
			srf.setReturnType(new DataDict(returnType));
		studentReturnService.saveStudentReturn(srf);



		contractService.saveContractBonus(fundsChangeHistory.getId(), BonusType.FEEDBACK_REFUND.toString(),srf.getId(),contractProduct, refundIncomeDistributeVo);

		FinanceMessage msg= new FinanceMessage();
		if(contractProduct != null && contractProduct.getType().equals(ProductType.LIVE)){
			msg.setOnline(true);
		}
		msg.setCampusId(student.getBlCampusId());
		msg.setCountDate(DateTools.getCurrentDate());
		msg.setRefundMoney(withDrawAmount.add(returnSpecialAmount));
		msg.setFlag("1");
		try {// 加入队列
			JedisUtil.lpush(ObjectUtil.objectToBytes(CommonUtil.FINANCE_QUEUE),ObjectUtil.objectToBytes(msg));
		} catch (Exception e) {
			// 错了
		}

		return account.getElectronicAccount();
	}
    /**
     * 电子账户转账
     *
     * @param origStuId
     * @param destStuId
     * @param transferAmount
     */
    @Override
    public void transferElectronicAmount(String origStuId, String destStuId, BigDecimal transferAmount) {
        StudnetAccMv origAcc = studnetAccMvDao.getStudnetAccMvByStudentId(origStuId);
        StudnetAccMv destAcc = studnetAccMvDao.getStudnetAccMvByStudentId(destStuId);
        if(origAcc.getElectronicAccount() == null)
        	origAcc.setElectronicAccount(BigDecimal.ZERO);
        // 有足够的钱
        if(origAcc.getElectronicAccount().compareTo(transferAmount) >= 0){
            origAcc.setElectronicAccount(origAcc.getElectronicAccount().subtract(transferAmount));
            if(destAcc.getElectronicAccount() == null){
                destAcc.setElectronicAccount(transferAmount);
            }else{
                destAcc.setElectronicAccount(destAcc.getElectronicAccount().add(transferAmount));
            }
            studnetAccMvDao.save(origAcc);
            studnetAccMvDao.save(destAcc);

			StudentAccInfo info = studentAccInfoDao.findInfoByStudentId(origStuId);

			if(info.getAccountAmount().compareTo(origAcc.getElectronicAccount())>0){//只有从平行账户转的钱需要充值。
				contractService.updateStudentAccountInfoAmount(origAcc.getStudentId(),info.getAccountAmount().subtract(origAcc.getElectronicAccount()),ElectronicAccChangeType.TRANSFER_OUT,null);//平行账户
				contractService.updateStudentAccountInfoAmount(destAcc.getStudentId(),info.getAccountAmount().subtract(origAcc.getElectronicAccount()),ElectronicAccChangeType.RECEIPT_IN,null);//平行账户
			}

            electronicAccountChangeLogDao.saveElecAccChangeLog(origAcc.getStudentId(), ElectronicAccChangeType.TRANSFER_OUT, "D", transferAmount, origAcc.getElectronicAccount(), "电子账户转账给" + studentDao.findById(destStuId).getName() + "[" + destStuId + "]");
            electronicAccountChangeLogDao.saveElecAccChangeLog(destAcc.getStudentId(), ElectronicAccChangeType.RECEIPT_IN, "A", transferAmount, destAcc.getElectronicAccount(), "电子账户收到来自" + studentDao.findById(origStuId).getName() + "[" + origStuId + "]的转账");
        }else{
            throw new ApplicationException("账户余额不足，转账失败");
        }
    }

	@Override
	public void rechargeMoney(String studentId, BigDecimal money, String remark) {
		StudnetAccMv studnetAccMvByStudentId = studnetAccMvDao.getStudnetAccMvByStudentId(studentId);
		if (studnetAccMvByStudentId.getElectronicAccount() == null){
			studnetAccMvByStudentId.setElectronicAccount(BigDecimal.ZERO);
		}
		studnetAccMvByStudentId.setElectronicAccount(studnetAccMvByStudentId.getElectronicAccount().add(money));
		studnetAccMvDao.save(studnetAccMvByStudentId);
		if (studnetAccMvByStudentId.getElectronicAccount().compareTo(new BigDecimal("99999999"))>0){
			throw new ApplicationException("电子账户余额超过最大值,请联系系统管理员");
		}

//		contractService.updateStudentAccountInfoAmount(studentId,money,ElectronicAccChangeType.RECHARGE,null);//平行账户

		electronicAccountChangeLogDao.saveElecAccChangeLog(studentId, ElectronicAccChangeType.RECHARGE, "A", money, studnetAccMvByStudentId.getElectronicAccount(), remark);
	}

    @Override
	public DataPackage getCustomerStudentList(String studentId, DataPackage dp) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("student.id",studentId));
		criterionList.add(Expression.eq("customerStudentStatus",CustomerStudentStatus.NORMAL));
		dp = customerStudentDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "customer.id", "desc"), criterionList);
		List<CustomerStudent> list=(List<CustomerStudent>) dp.getDatas();
		dp.setDatas(HibernateUtils.voListMapping(list, CustomerStudentVo.class));
		return dp;
	}

    @Override
   	public DataPackage getDeletedCustomerStudentList(String studentId, DataPackage dp) {
   		List<Criterion> criterionList = new ArrayList<Criterion>();
   		criterionList.add(Expression.eq("student.id",studentId));
   		criterionList.add(Expression.eq("customerStudentStatus",CustomerStudentStatus.DELETE));
   		dp = customerStudentDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "modifyTime", "desc"), criterionList);
   		List<CustomerStudent> list=(List<CustomerStudent>) dp.getDatas();
   		dp.setDatas(HibernateUtils.voListMapping(list, CustomerStudentVo.class));
   		return dp;
   	}

	@Override
	public Response addCustomerStudent(CustomerStudent customerStudent) {
		Response res=new Response();
		if(StringUtils.isNotEmpty(customerStudent.getCustomer().getId()) &&  StringUtils.isNotEmpty(customerStudent.getStudent().getId())){
			CustomerStudent oldCustomerStudent=customerStudentDao.getOneCustomerStudent(customerStudent.getCustomer().getId(), customerStudent.getStudent().getId());
			if(oldCustomerStudent==null){
				customerStudent.setCreateTime(DateTools.getCurrentDateTime());
				customerStudent.setCustomerStudentStatus(CustomerStudentStatus.NORMAL);
				customerStudent.setCreateUser(userService.getCurrentLoginUser());
				customerStudent.setIsDeleted(false);
				customerStudentDao.save(customerStudent);
			}else{
				res.setResultCode(1);
				res.setResultMessage("当前客户已存在");
			}
		}else{
			res.setResultCode(1);
			res.setResultMessage("数据异常");
		}

		return res;
	}

	/**
	 * 修改学生关联客户关系
	 * @param customerStudent
	 * @return
	 */
	@Override
	public Response updateCustomerStudent(CustomerStudent customerStudent) {
		Response res=new Response();
		if(StringUtils.isNotEmpty(customerStudent.getCustomer().getId()) &&  StringUtils.isNotEmpty(customerStudent.getStudent().getId())){
			CustomerStudent cs = customerStudentDao.getOneCustomerStudent(customerStudent.getCustomer().getId(), customerStudent.getStudent().getId());
			cs.setRelation(customerStudent.getRelation());
			cs.setModifyTime(DateTools.getCurrentDateTime());
			cs.setModifyUser(userService.getCurrentLoginUser());
			customerStudentDao.save(cs);
		}else{
			res.setResultCode(1);
			res.setResultMessage("数据异常");
		}

		return res;
	}

	@Override
	public Response restoreCustomerStudent(CustomerStudent customerStudent) {
		Response res=new Response();
		if(StringUtils.isNotEmpty(customerStudent.getCustomer().getId()) &&  StringUtils.isNotEmpty(customerStudent.getStudent().getId())){
			CustomerStudent cs = customerStudentDao.getOneCustomerStudent(customerStudent.getCustomer().getId(), customerStudent.getStudent().getId());
			cs.setModifyTime(DateTools.getCurrentDateTime());
			cs.setModifyUser(userService.getCurrentLoginUser());
			cs.setCustomerStudentStatus(CustomerStudentStatus.NORMAL);
			customerStudentDao.save(cs);
		}else{
			res.setResultCode(1);
			res.setResultMessage("数据异常");
		}

		return res;
	}

	/**
	 * 删除学生关联客户关系
	 * @param customerStudent
	 * @return
	 */
	public Response deleteCustomerStudent(CustomerStudent customerStudent){
		Response res=new Response();
		if(StringUtils.isNotEmpty(customerStudent.getCustomer().getId()) &&  StringUtils.isNotEmpty(customerStudent.getStudent().getId())){
			CustomerStudent cs = customerStudentDao.getOneCustomerStudent(customerStudent.getCustomer().getId(), customerStudent.getStudent().getId());
			cs.setModifyTime(DateTools.getCurrentDateTime());
			cs.setModifyUser(userService.getCurrentLoginUser());
			cs.setCustomerStudentStatus(CustomerStudentStatus.DELETE);
			customerStudentDao.save(cs);
		}else{
			res.setResultCode(1);
			res.setResultMessage("数据异常");
		}
//		customerStudentDao.delete(customerStudent);
		return res;
	}

	/**
	 * 修改学生学管
	 * @param studentId
	 * @param studyManegerId
	 * @return
	 */
	public Response updateStudentStudyManager(String studentId, String studyManegerId) throws Exception {
		Response res =new Response();
		Student stu=studentDao.findById(studentId);
		if(stu==null){
			res.setResultCode(1);
			res.setResultMessage("该学生不存在!");
		}else{
			if (null != stu.getStudyManeger()) {
				res = checkUpdateStudentManage(studentId, stu.getStudyManeger().getUserId(), studyManegerId, "changeCourse", res);
				if (res.getResultCode() != 0) {
					return res;
				}
			}
			stu.setStudyManegerId(studyManegerId);
			studentDao.save(stu);
			otmClassService.updateOtmClassStudentAttendentStudyManager(studyManegerId, studentId, stu.getBlCampusId());
			studentOrganizationDao.saveStudentOrganization(stu);
			//通知新学管有新生分配了    关闭dwr   2016-12-17
//			messageService.sendMessage(MessageType.SYSTEM_MSG, "新分配学生:"+stu.getName(), "请及时帮新学生绑定考勤编号。", MessageDeliverType.SINGLE, studyManegerId);
		}

		return res;
	}

	/**
	 * 检查能否修改学管
	 * @param studentIds
	 * @param oldStudyManegerId
	 * @param newStudyManegerId
	 * @param changeCourse
	 * @param res
     * @return
     */
	private Response checkUpdateStudentManage(String studentIds, String oldStudyManegerId, String newStudyManegerId, String changeCourse, Response res) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		String whereHql = "where 1=1";
		if(!"all".equals(studentIds)){
			if (studentIds.contains(",")) {
				String[] ids = studentIds.split(",");
				criterionList.add(Restrictions.in("student.id", ids));
				String idsStr = "";
				for(String id : ids){
					idsStr+=",'"+id+"'";
				}
				whereHql += " and student.id in ("+idsStr.substring(1)+") ";
			} else {
				criterionList.add(Restrictions.eq("student.id", studentIds));
				whereHql += " and student.id='" + studentIds +"'";
			}
		}
		if (StringUtils.isNotBlank(oldStudyManegerId)) {
			criterionList.add(Restrictions.eq("studyManager.userId", oldStudyManegerId));
			whereHql += " and studyManager.userId='" + oldStudyManegerId +"'";
		}
		criterionList.add(Restrictions.le("courseDate", DateTools.getCurrentDate()));
		CourseStatus[] courseStatusArr = {CourseStatus.NEW, CourseStatus.STUDENT_ATTENDANCE, CourseStatus.TEACHER_ATTENDANCE, CourseStatus.STUDY_MANAGER_AUDITED};
		criterionList.add(Restrictions.in("courseStatus",
				courseStatusArr));
//		criterionList.add(Restrictions.ne("courseStatus",
//				CourseStatus.CHARGED));
        DataPackage dp = new DataPackage(0, 999);
		dp = courseDao.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "createTime", "desc"),
				criterionList);
		List<Course> courseLit = (List<Course>) dp.getDatas();
		if (courseLit.size() > 0) {
			res.setResultCode(1);
			res.setResultMessage("该学管仍有对应本学生的一对一课程未完成，请处理完该学管的一对一课程才进行操作。");
		} else {
			if ("changeCourse".equals(changeCourse)) {
				StringBuilder hql = new StringBuilder();
				Map<String, Object> params = new HashMap<String, Object>();
				hql.append("update Course set studyManager.userId = :newStudyManegerId ");
				params.put("newStudyManegerId", newStudyManegerId);
				whereHql += " and courseDate>'" + DateTools.getCurrentDate() + "' and courseStatus <> 'CHARGED ' ";
				hql.append(whereHql);
				courseDao.excuteHql(hql.toString(), params);
			}
		}
		return res;
	}

	@Override
	public Response checkCanUpdateStudentManage(Student student,String StudentManageId,Response response) {
		response = checkUpdateStudentManage(student.getId(), student.getStudyManegerId(), null,  null, response);
		if (response.getResultCode()==0){
			String studentIds = student.getId();
			if(!"all".equals(studentIds)){
				if (studentIds.contains(",")) {
					String[] ids = studentIds.split(",");
					for (String id : ids) {
						this.checkHasAuditingRefundWorkflow(id, StudentManageId, response);
					}
				} else {
					this.checkHasAuditingRefundWorkflow(studentIds, StudentManageId, response);
				}
			} else {
				List<Student> students = studentDao.getStudentListByStudyManager(student.getStudyManegerId());
				for (Student s : students) {
					checkHasAuditingRefundWorkflow(s.getId(), StudentManageId, response);
				}
			}
		}
		return response;
	}

	private Response checkHasAuditingRefundWorkflow(String id, String StudentManageId, Response response) {
		Student studentDb = studentDao.findById(id);
		String oldStudyManagerId = studentDb.getStudyManeger() != null ? studentDb.getStudyManeger().getUserId() : null;
		if (oldStudyManagerId != null && !oldStudyManagerId.equals(StudentManageId)) {
			if (refundWorkflowService.checkHasAuditingRefundWorkflow(oldStudyManagerId)) {
				response.setResultMessage("学管存在未完成的退费或转账审核，是否还继续替换指定学员的学管师？");
			}
		}
		return response;
	}

	/**
	 * 更新学生表合同产品第一次报读时间
	 * @param student
	 */
	@Override
	public void updateStudentProductFirstTime(Student student) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select cp.TYPE contractType, min(cp.create_time) time from contract c, contract_product cp where c.id=cp.contract_id ");
		sql.append(" and c.student_id = :studentId ");
		params.put("studentId", student.getId());
		sql.append(" and cp.type in ('ONE_ON_ONE_COURSE', 'SMALL_CLASS', 'ECS_CLASS', 'ONE_ON_MANY') GROUP BY cp.type ");
		List<Map<Object, Object>> result = contractProductDao.findMapBySql(sql.toString(), params);
		Map<String, String> map = new HashMap<>();
		for (Map<Object, Object> a :result){
			map.put(a.get("contractType").toString(), a.get("time").toString());
		}

		if (map.containsKey("ONE_ON_ONE_COURSE")){
		    student.setOneOnOneFirstTime(map.get("ONE_ON_ONE_COURSE"));
		}
		if (map.containsKey("SMALL_CLASS")){
			student.setSmallClassFirstTime(map.get("SMALL_CLASS"));
		}
		if (map.containsKey("ONE_ON_MANY")){
			student.setOneOnManyFirstTime(map.get("ONE_ON_MANY"));
		}
		if (map.containsKey("ECS_CLASS")){
			student.setEcsClassFirstTime(map.get("ECS_CLASS"));
		}
		studentDao.save(student);

	}

	/**
	 * 学生转校查找未结算课程
	 * @param studentId
	 * @return
     */
	@Override
	public Map<String, String> turnCampusWithUnpayMiniCourseByStudentId(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from MiniClassStudentAttendent msa where 1=1 ");
		hql.append(" and  msa.studentId = :studentId ");
		params.put("studentId", studentId);
		hql.append(" and  msa.chargeStatus= 'UNCHARGE'");
		hql.append(" and  msa.miniClassCourse.miniClass.miniClassId  in (SELECT mc.miniClassId from ContractProduct cp,MiniClass mc  where cp.product.id=mc.product.id and cp.type='SMALL_CLASS' and cp.status not in ('ENDED','CLOSE_PRODUCT') and cp.contract.student.id = '").append(studentId).append("' )");
		List<MiniClassStudentAttendent> miniClassStudentAttendents = miniClassStudentAttendentDao.findAllByHQL(hql.toString(), params);
		Map<String,String> map = new HashMap<>();
		for (MiniClassStudentAttendent m:miniClassStudentAttendents){
			MiniClass miniClass = m.getMiniClassCourse().getMiniClass();
			map.put(miniClass.getMiniClassId(),miniClass.getBlCampus().getId() + "_" + miniClass.getName());
		}
		return map.size()==0?null:map;
	}


	/**
	 * 导入学校数据
	 * @param file
     */
	@Override
	public Map<Boolean, String> importStudentSchoolFromExcel(File file) throws Exception {

		Map<Boolean, String> resultMap = new HashMap<>();

		String excel = "schoolImport";
		String table = "STUDENT_SCHOOL";
//		ShardedJedis client = redisDataSource.getRedisClient();
		Object o = Memcached.get(excel);
		if (o!=null){
			logger.error("存在导入");
			resultMap.put(false, "系统正在执行一次学生学校批量excel导入，请稍后再试。");
			return resultMap;
		}else {
			try {
//				client.set(excel, excel);
				Memcached.set(excel, excel);
				final List<StudentSchool> canLoadIntoDB = new ArrayList<>();
				final Map<String, String> distinctSchoolMap = new HashMap<>();
				List<Map<Object,Object>> schoolInDB = studentSchoolDao.findMapBySql("select NAME ,PARENT_ID, CATEGORY from "+table+" ; ", new HashMap<String, Object>());
				for (Map<Object,Object> map:schoolInDB){
					distinctSchoolMap.put(map.get("NAME")+"_"+map.get("PARENT_ID")+"_"+map.get("CATEGORY"),"1");
				}
				Function<StudentSchoolImportedEo, StudentSchoolVo> transferFunction = new Function<StudentSchoolImportedEo, StudentSchoolVo>(){
					@Override
					public StudentSchoolVo apply(StudentSchoolImportedEo eo) {
						if (!eo.getValidated()){
							return null;
						}

						StudentSchoolVo dto = new StudentSchoolVo();
						dto.setName(eo.getSchoolName());
						dto.setRegionId(eo.getCityId());
						dto.setSchoolTypeId(eo.getSchoolLevelId());
						if (distinctSchoolMap.get(dto.getName()+"_"+dto.getRegionId()+"_"+dto.getSchoolTypeId())!=null){
							eo.setValidated(false);
							eo.appendValidateMsg("学校名称在同一个城市和同一种学校级别下不是唯一");
							return null;
						}else {
							distinctSchoolMap.put(dto.getName()+"_"+dto.getRegionId()+"_"+dto.getSchoolTypeId(),"1");
						}

						dto.setValue(eo.getSchoolName());

						dto.setAddress(eo.getAddress());
						dto.setContact(eo.getContact());
						dto.setSchoolLeader(eo.getSchoolLeader());
						dto.setSchoolLeaderContact(eo.getSchoolLeaderContact());
						dto.setRemark(eo.getRemark());
						return  dto;
					}
				};
				ExecuteFunction<StudentSchoolVo> dbExecuteFunction = new ExecuteFunction<StudentSchoolVo>() {

					@Override
					public void execute(StudentSchoolVo t) throws Exception {
						StudentSchool studentSchool = HibernateUtils.voObjectMapping(t, StudentSchool.class);
						canLoadIntoDB.add(studentSchool);
					}
				};
				ExcelImporter<StudentSchoolImportedEo, StudentSchoolVo> importer = new PoiExcelImporter<>(true);
				importer.setMetaData(StudentSchoolImportedEo.class).loadExcel(new FileInputStream(file)).parse().addTransferFunction(transferFunction)
						.executeInFeedback(dbExecuteFunction).feebback2Excel().export2Stream(new FileOutputStream(file)).clearExcelWorkBook().closeExcelStream();

				loadDataInFile(canLoadIntoDB, table);

				resultMap.put(true, "导入成功");
				return resultMap;
			}catch (Exception e){
				logger.error("出错了");
				resultMap.put(false, "系统出错了");
				return resultMap;
			}finally {
//				client.del(excel);
				Memcached.delete(excel);
			}
		}



	}

	/**
	 * 获取一对一排课信息
	 * @param studentId
	 * @return
	 */
    @Override
    public Map<String, Object> findStudentOneOnOnePaiKeInfo(String studentId) {
    	Map<String, Object> map = new HashMap<>();
		StudentVo studentVo = findStudentById(studentId);
		StudnetAccMvVo studentAccoutInfo = getStudentAccoutInfo(studentId);
		map.put("oneOnOneAlreadyArrangeHour", studentVo.getOneOnOneAlreadyArrangeHour());
		map.put("studyManagerId", studentVo.getStudyManegerId());
		map.put("oneOnOneRemainingHour", studentAccoutInfo.getOneOnOneRemainingHour());
		return map;
    }



	/**
	 * 硬编码
	 * @param canLoadIntoDB
	 * @throws SQLException
     */
	private void loadDataInFile(List<StudentSchool> canLoadIntoDB, String table) throws SQLException {
		String fieldsterminated = "\t";
		String linesterminated = "\n";
		String loadDataSql = "LOAD DATA LOCAL INFILE 'sql.csv' INTO TABLE "+table+" FIELDS TERMINATED BY '"
				+ fieldsterminated + "'  LINES TERMINATED BY '" + linesterminated
				+ "' (ID, NAME, VALUE, PARENT_ID, CATEGORY, CREATE_USER_ID, CREATE_TIME, ADDRESS, CONTACT, REMARK, SCHOOL_LEADER, SCHOOL_LEADER_CONTACT) ";
		Connection con = null;
		try{
			con = DriverManager.getConnection(PropertiesUtils.getStringValue("jdbc.writeUrl"), PropertiesUtils.getStringValue("jdbc.username"), PropertiesUtils.getStringValue("jdbc.password"));
			con.setAutoCommit(false);
			PreparedStatement pt = con.prepareStatement(loadDataSql);
			com.mysql.jdbc.PreparedStatement mysqlStatement = null;
			if (pt.isWrapperFor(com.mysql.jdbc.Statement.class)) {
				mysqlStatement = pt.unwrap(com.mysql.jdbc.PreparedStatement.class);
			}

			String createUserId = userService.getCurrentLoginUser().getUserId();

			int i = 0;
			int batchSize = 1000;
			StringBuilder builder = new StringBuilder(10000);
			for (StudentSchool studentSchool : canLoadIntoDB){
				builder.append(UUID.randomUUID().toString().replaceAll("-", ""));
				builder.append("\t");
				builder.append(studentSchool.getName());
				builder.append("\t");
				builder.append(studentSchool.getName());
				builder.append("\t");
				builder.append(studentSchool.getRegion().getId());
				builder.append("\t");
				builder.append(studentSchool.getSchoolType().getId());
				builder.append("\t");
				builder.append(createUserId);
				builder.append("\t");
				builder.append(DateTools.getCurrentDateTime());//创建时间
				builder.append("\t");
				builder.append(studentSchool.getAddress()==null?"":studentSchool.getAddress());
				builder.append("\t");
				builder.append(studentSchool.getContact()==null?"":studentSchool.getContact());
				builder.append("\t");
				builder.append(studentSchool.getRemark()==null?"":studentSchool.getRemark());
				builder.append("\t");
				builder.append(studentSchool.getSchoolLeader()==null?"":studentSchool.getSchoolLeader());
				builder.append("\t");
				builder.append(studentSchool.getSchoolLeaderContact()==null?"":studentSchool.getSchoolLeaderContact());
				builder.append("\n");

				if (i % batchSize == 1){
					byte[] bytes = builder.toString().getBytes();
					InputStream in = new ByteArrayInputStream(bytes);
					mysqlStatement.setLocalInfileInputStream(in);
					mysqlStatement.executeUpdate();
					con.commit();
					builder = new StringBuilder(10000);
				}

				i++;

			}
			byte[] bytes = builder.toString().getBytes();
			InputStream in = new ByteArrayInputStream(bytes);
			mysqlStatement.setLocalInfileInputStream(in);
			mysqlStatement.executeUpdate();
			con.commit();
		}finally {
			con.close();
		}

	}

	@Override
	public DataPackage getStudentSchoolList(StudentSchool studentSchool, DataPackage dataPackage) {
		//如果有字段有值，用like进行条件查询
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(studentSchool);
		if(studentSchool.getSchoolType()!=null && StringUtils.isNotEmpty(studentSchool.getSchoolType().getId())){
			criterionList.add(Expression.eq("schoolType.id", studentSchool.getSchoolType().getId()));
		}
		if(studentSchool.getProvince()!=null && StringUtils.isNotEmpty(studentSchool.getProvince().getId())){
			criterionList.add(Expression.eq("province.id", studentSchool.getProvince().getId()));
		}
		if (studentSchool.getCity()!=null && StringUtils.isNotEmpty(studentSchool.getCity().getId())){
			criterionList.add(Expression.eq("city.id", studentSchool.getCity().getId()));
		}
		dataPackage= studentSchoolDao.findPageByCriteria(dataPackage, HibernateUtils.prepareOrder(dataPackage, "id", "desc"), criterionList);
		List<StudentSchool> list=(List<StudentSchool>) dataPackage.getDatas();
		dataPackage.setDatas(HibernateUtils.voListMapping(list, StudentSchoolVo.class));
		return dataPackage;
	}

	/**
	 * 待审核学生学校列表
	 * @param studentSchoolTemp
	 * @param dataPackage
	 * @return
	 */
	@Override
	public DataPackage getSchoolTempList(StudentSchoolTemp studentSchoolTemp, DataPackage dataPackage) {
//		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(studentSchoolTemp);
//		dataPackage =  studentSchoolTempDao.findPageByCriteria(dataPackage, HibernateUtils.prepareOrder(dataPackage, "id", "desc"), criterionList);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from StudentSchoolTemp sst where 1=1 ");

		if (studentSchoolTemp.getName()!=null && StringUtil.isNotBlank(studentSchoolTemp.getName())){
			hql.append(" and sst.name like :studentSchoolTempName ");
			params.put("studentSchoolTempName", "%" + studentSchoolTemp.getName() + "%");
		}

		if (studentSchoolTemp.getAuditUser()!=null && StringUtil.isNotBlank(studentSchoolTemp.getAuditUser().getName())){
			hql.append(" and sst.auditUser.name like :auditUserName ");
			params.put("auditUserName", "%" + studentSchoolTemp.getAuditUser().getName() + "%");
		}

		if (studentSchoolTemp.getProvince()!=null && StringUtil.isNotBlank(studentSchoolTemp.getProvince().getId()) ){
			hql.append(" and sst.province.id = '").append(studentSchoolTemp.getProvince().getId()).append("' ");
		}

		if (studentSchoolTemp.getCity()!=null && StringUtil.isNotBlank(studentSchoolTemp.getCity().getId())){
			hql.append(" and sst.city.id = :cityId ");
			params.put("cityId", studentSchoolTemp.getCity().getId());
		}

		if (studentSchoolTemp.getSchoolTempAuditStatus()!=null){
			int a=0;
			switch (studentSchoolTemp.getSchoolTempAuditStatus().getValue()){
				case "VALIDATE":
					a = 0;
					break;
				case "UNVALIDATE":
					a=1;
					break;
				case "UNAUDIT":
					a=2;
					break;
			}

			hql.append(" and sst.schoolTempAuditStatus = ").append(a);
		}

		hql.append(" order by schoolTempAuditStatus desc ,  createTime    desc");

		dataPackage = studentSchoolTempDao.findPageByHQL(hql.toString(),dataPackage, true, params);
		List<StudentSchoolTemp> list = (List<StudentSchoolTemp>) dataPackage.getDatas();
		List<SchoolTempVo> schoolTempVos = HibernateUtils.voListMapping(list, SchoolTempVo.class);
		for (SchoolTempVo schoolTempVo : schoolTempVos){
			Contract contract = contractDao.findById(schoolTempVo.getContractId());
			if (contract!=null){
				User signStaff = contract.getSignStaff();
				schoolTempVo.setSignUserName(signStaff.getName());
				schoolTempVo.setSignUserContact(signStaff.getContact());
			}
			Student student = studentDao.findById(schoolTempVo.getStudentId());
			if (student!=null){
				schoolTempVo.setStudentName(student.getName());
				schoolTempVo.setStudentGrade(student.getGradeDict().getName());
			}
		}
		dataPackage.setDatas(schoolTempVos);
		return dataPackage;
	}

	/**
	 * 根据id查找待审核学校
	 * @param id
	 * @return
	 */
	@Override
	public SchoolTempVo findSchoolTempById(String id) {

		StudentSchoolTemp stu = studentSchoolTempDao.findById(id);
		if(stu!=null){
			SchoolTempVo vo= HibernateUtils.voObjectMapping(stu, SchoolTempVo.class);
//			if(stu.getRegion()!=null && stu.getRegion().getParentDataDict() != null
//					&& StringUtils.isNotEmpty(stu.getRegion().getParentDataDict().getId())){
//				vo.setProvinceId(stu.getRegion().getParentDataDict().getId());
//			}
			if (SchoolTempAuditStatus.VALIDATE.getName().equals(vo.getSchoolTempAuditStatusName())){
				//已审核
				String studentId = vo.getStudentId();
				Student student = studentDao.findById(studentId);
				if (student!=null){
					StudentSchool school = student.getSchool();
					if (school!=null){
						vo.setSchoolId(school.getId());
						vo.setSchoolName(school.getName());
						vo.setMatchingProvinceId(school.getProvince().getId());
						vo.setMatchingRegionId(school.getCity().getId());
					}
				}
			}
			return vo;
		}
		return null;
	}

    @Override
    public void matchingStudentSchool(String id, String matchingSchool) {
		StudentSchoolTemp schoolTemp = studentSchoolTempDao.findById(id);
		String studentId = schoolTemp.getStudentId();
		Student student = studentDao.findById(studentId);
		StudentSchool studentSchool = studentSchoolDao.findById(matchingSchool);
		if (student!=null){

			student.setSchool(studentSchool);
			student.setSchoolOrTemp("school");
		}

		String contractId = schoolTemp.getContractId();
		Contract contract = contractDao.findById(contractId);
		if (contract!=null){
			contract.setSchool(studentSchool);
			contract.setSchoolOrTemp("school");
		}

		schoolTemp.setAuditUser(userService.getCurrentLoginUser());
		schoolTemp.setSchoolTempAuditStatus(SchoolTempAuditStatus.VALIDATE);
	}

	@Override
	public void saveNewSchool(StudentSchool studentSchool, String schoolTempId) {
		saveOrUpdateStudentSchool(studentSchool);

		StudentSchoolTemp schoolTemp = studentSchoolTempDao.findById(schoolTempId);
		if (schoolTemp!=null){
			schoolTemp.setAuditUser(userService.getCurrentLoginUser());
			schoolTemp.setSchoolTempAuditStatus(SchoolTempAuditStatus.VALIDATE);
			String studentId = schoolTemp.getStudentId();
			Student student = studentDao.findById(studentId);
			if (student!=null){
				student.setSchool(studentSchool);
				student.setSchoolOrTemp("school");
			}
			String contractId = schoolTemp.getContractId();
			Contract contract = contractDao.findById(contractId);
			if (contract!=null){
				contract.setSchool(studentSchool);
				contract.setSchoolOrTemp("school");
			}

		}

	}

	@Override
	public void unvalidSchool(String schoolTempId) {
		StudentSchoolTemp schoolTemp = studentSchoolTempDao.findById(schoolTempId);
		schoolTemp.setSchoolTempAuditStatus(SchoolTempAuditStatus.UNVALIDATE);
		String studentId = schoolTemp.getStudentId();
		Student student = studentDao.findById(studentId);
		if (student!=null){
			student.setSchoolOrTemp("schoolTemp");
		}
		String contractId = schoolTemp.getContractId();
		Contract contract = contractDao.findById(contractId);
		if (contract!=null){
			contract.setSchoolOrTemp("schoolTemp");
		}
		schoolTemp.setAuditUser(userService.getCurrentLoginUser());
	}

	@Override
	public void deleteStudentSchool(StudentSchool studentSchool) {
		studentSchoolDao.delete(studentSchool);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateStudentSchool(StudentSchool studentSchool) {
		User loginUser = userService.getCurrentLoginUser();
		studentSchool.setCreateUserId(loginUser.getUserId());
		studentSchool.setCreateTime(DateTools.getCurrentDateTime());
//		String regionId = studentSchool.getRegion().getId();
		String cityId =null;
		if (studentSchool.getCity()!=null){
			cityId = studentSchool.getCity().getId();
		}
		String schoolName = studentSchool.getName();
		String schoolTypeId  = studentSchool.getSchoolType().getId();
		if (StringUtil.isNotBlank(cityId) && StringUtil.isNotBlank(schoolName)){
			Map<String, Object> params = new HashMap<String, Object>();
			String sql =" select count(1) from student_school where  city_id = :cityId and  name = :schoolName and CATEGORY = :schoolTypeId ";
			params.put("cityId", cityId);
			params.put("schoolName", schoolName);
			params.put("schoolTypeId", schoolTypeId);
			if (StringUtil.isNotBlank(studentSchool.getId())) {
				sql += " and id != :id ";
				params.put("id", studentSchool.getId());
			}
			int num = studentSchoolDao.findCountSql(sql, params);
			if (num>0){
				throw  new ApplicationException("系统中已经存在城市相同的同名学校");
			}
		}
		if (StringUtil.isNotBlank(studentSchool.getAddress())){
			String[] strings = null;
			try {
				strings = CustomerMapAnalyzeServiceImpl.doGet(studentSchool.getAddress());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (strings!=null){
				studentSchool.setLog(strings[0]);
				studentSchool.setLat(strings[1]);
			}
		}
		studentSchoolDao.save(studentSchool);
	}





	@Override
	public StudentSchoolVo findStudentSchoolById(String id) {
		StudentSchool stu= studentSchoolDao.findById(id);
		if(stu!=null){
			StudentSchoolVo vo= HibernateUtils.voObjectMapping(stu, StudentSchoolVo.class);
			if(stu.getRegion()!=null && stu.getRegion().getParentDataDict() != null
					&& StringUtils.isNotEmpty(stu.getRegion().getParentDataDict().getId())){
				vo.setProvinceId(stu.getRegion().getParentDataDict().getId());
			}
			return vo;
		}
		return null;
	}

	@Override
	public List<StudentSchoolVo> getStudentSchoolForAutoCompelate(String term) {
		//SimpleExpression contactLike = Expression.like("id", term, MatchMode.ANYWHERE);
		//SimpleExpression nameLike = Expression.like("name", term, MatchMode.ANYWHERE);
		Organization campus = userService.getBelongCampus();
		if (StringUtils.isNotBlank(term)){
			List<StudentSchool> list= (List<StudentSchool>) studentSchoolDao.findPageByCriteria(new DataPackage(0, 20), new ArrayList<Order>()
					, Expression.like("name", term, MatchMode.ANYWHERE)).getDatas();
			return HibernateUtils.voListMapping(list, StudentSchoolVo.class);
		}
		List <StudentSchoolVo> voList= new ArrayList<>();
		return voList;
	}



	@Override
	public StuInfoForFundsVo getCampusInfoByCampusId(String blCampusId) {
		StuInfoForFundsVo forFundsVo=new StuInfoForFundsVo();
		Organization campus=organizationDao.findById(blCampusId);
		forFundsVo.setBlCampusName(campus.getName());
		forFundsVo.setBlCampusAddress(campus.getAddress());
		forFundsVo.setBlCampusPhone(campus.getContact());
		Organization brenchOrg=organizationDao.getBelongBrench(campus.getOrgLevel());
		if(brenchOrg!= null )
			forFundsVo.setBlBrenchName(brenchOrg.getName());
		else
			forFundsVo.setBlBrenchName("");
		return forFundsVo;
	}

	public AppStudentLoginResponse appLogin(String studentId, String appPassword) {
		if (StringUtils.isEmpty(studentId)) {
			throw new ApplicationException(ErrorCode.STUDENT_ID_EMPTY);
		}
		if (StringUtils.isEmpty(appPassword)) {
			throw new ApplicationException(ErrorCode.STUDENT_PASSWORD_EMPTY);
		}
		Student student= studentDao.findById(studentId);
		if (student == null || !appPassword.equals(student.getAppPassword())) {
			throw new ApplicationException(ErrorCode.STUDENT_LOGIN_FAIL);
		}
		AppStudentLoginResponse response = new AppStudentLoginResponse();
		//StudentVo vo = HibernateUtils.voObjectMapping(student, StudentVo.class, "appStudentMapping");
		StudentVo vo = HibernateUtils.voObjectMapping(student, StudentVo.class);

		StudnetAccMv account = studnetAccMvDao.getStudnetAccMvByStudentId(studentId);
		if (account != null) {
			vo.setOneOnOneRemainingHour(account.getOneOnOneRemainingHour());
			vo.setRemainingAmount(account.getRemainingAmount());
		}
		response.setStudent(vo);

		// 判断有无mobileUser
		MobileUser mobileUserForStu = mobileUserService.findMobileUserByStuId(studentId);
		response.setMobileUser(mobileUserForStu);



		return response;
	}

	@Override
	public List<UserVo> getStudentReferenceCampusContact(String studentId) {
		List<User> userList = userDao.getStudentReferenceUser(studentId);
		return HibernateUtils.voListMapping(userList, UserVo.class);
	}

	@Override
	public User getStudentStudyManager(String studentId) {
		if (StringUtils.isEmpty(studentId)) {
			throw new ApplicationException(ErrorCode.STUDENT_ID_EMPTY);
		}
		return studentDao.findById(studentId).getStudyManeger();
	}


	@Override
	public void deleteStudentScore(StudentScore studentscoremanage) {
		// 增加学生动态的记录项 开始
		studentDynamicStatusService.createScoreDynamicStatus(studentscoremanage, "DELETE_SCORE");
		// 增加学生动态的记录项 结束
		studentScoreDao.delete(studentscoremanage);
	}

	@Override
	public void saveOrUpdateStudentScore(StudentScore studentscoremanage,String[] subject,BigDecimal[] score,String[] classRange,String[] gradeRange) {
		studentscoremanage.setModifyTime(DateTools.getCurrentDateTime());
		studentscoremanage.setModifyUserId(userService.getCurrentLoginUser().getUserId());
		if(StringUtils.isNotEmpty(studentscoremanage.getId())){
			studentScoreDao.save(studentscoremanage);
			// 增加学生动态的记录项 开始
			studentDynamicStatusService.createScoreDynamicStatus(studentscoremanage, "EDIT_SCORE");
			// 增加学生动态的记录项 结束
		}else{
			studentscoremanage.setCreateTime(DateTools.getCurrentDateTime());
			studentscoremanage.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			for(int i=0;i<subject.length;i++){
				StudentScore coremanage=HibernateUtils.voObjectMapping(studentscoremanage, StudentScore.class);
				coremanage.setId(null);
				coremanage.setSubject(new DataDict(subject[i]));
				coremanage.setScore(score[i]);
				if(classRange.length >0 && StringUtils.isNotEmpty(classRange[i]))
					coremanage.setClassRange(classRange[i]);
				if(gradeRange.length >0 && StringUtils.isNotEmpty(gradeRange[i]))
					coremanage.setGradeRange(gradeRange[i]);
				studentScoreDao.save(coremanage);
				// 增加学生动态的记录项 开始
				studentDynamicStatusService.createScoreDynamicStatus(coremanage, "CREATE_NEW_SCORE");
				// 增加学生动态的记录项 结束
			}
		}

	}

	@Override
	public DataPackage getStudentScoreList(StudentScore studentScore,
			DataPackage dataPackage,BigDecimal min,BigDecimal max) {
		List<Criterion> criterionList=HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(studentScore);
		if(studentScore.getSubject()!=null && StringUtils.isNotEmpty(studentScore.getSubject().getId())){
			criterionList.add(Expression.eq("subject.id", studentScore.getSubject().getId()));
		}
		if(studentScore.getGrade()!=null && StringUtils.isNotEmpty(studentScore.getGrade().getId())){
			criterionList.add(Expression.eq("grade.id", studentScore.getGrade().getId()));
		}
		if(studentScore.getTypeExam()!=null && StringUtils.isNotEmpty(studentScore.getTypeExam().getId())){
			criterionList.add(Expression.eq("typeExam.id", studentScore.getTypeExam().getId()));
		}
		if(studentScore.getStudent()!=null && StringUtils.isNotEmpty(studentScore.getStudent().getId())){
			criterionList.add(Expression.eq("student.id", studentScore.getStudent().getId()));
		}
		if(min!=null){
			criterionList.add(Expression.ge("score", min));
		}
		if(max!=null){
			criterionList.add(Expression.le("score", max));
		}
		dataPackage=studentScoreDao.findPageByCriteria(dataPackage, HibernateUtils.prepareOrder(dataPackage, "id", "desc"), criterionList);
		List<StudentScore> list = (List<StudentScore>) dataPackage.getDatas();
		dataPackage.setDatas(HibernateUtils.voListMapping(list, StudentScoreVo.class));
		return dataPackage;
	}


	public StudentScoreVo findStudentScoreById(String id) {
		StudentScore stu= studentScoreDao.findById(id);
		if(stu!=null){
			StudentScoreVo vo= HibernateUtils.voObjectMapping(stu, StudentScoreVo.class);
			return vo;
		}
		return null;
	}
//修改考勤
	@Override
	public Response editAttanceNo(String studentId,String attanceNo, String icCardNo) {
		Response res =new Response();
		Student student = studentDao.findById(studentId);
		if(student==null){
			res.setResultCode(1);
			res.setResultMessage("该学生不存在!");
		}else{
			if (StringUtils.isNotBlank(attanceNo)
					&& studentDao.checkIfAttendanceNoExist(student.getId(), attanceNo,student.getBlCampusId())) {
				throw new ApplicationException(ErrorCode.STUDENT_ATTENDANCE_NUM_EXIST);
			}
			student.setAttanceNo(attanceNo);
			student.setIcCardNo(icCardNo);
			studentDao.save(student);
		}
		return res;
	}

    /**
     * 查找所有学生with课程数量
     *
     * @param courseVo
     * @return
     */
    @Override
    public List<StudentVo> findStudentWithCourseCount(CourseVo courseVo) {
    	Organization organization = userService.getBelongCampus();
        return studentDao.findStudentWithCourseCount(courseVo,organization);
    }

    /**
     * 查询学生
     * @param studentvo
     * @return
     */
    @Override
    public List<Map<String, String>> findStudentForMapView(StudentVo studentvo, ModelVo modelVo) {
    	//
//    	String hql=" from Student s where  1=1 and s.lat is not null and s.log is not null" + RoleCodeAuthoritySearchUtil.getStudentRoleCodeAuthority(userService.getCurrentLoginUser(),  userService.getBelongCampus());
    	Map<String, Object> params = new HashMap<String, Object>();
    	String hql=" from Student s where  1=1 and s.lat is not null and s.log is not null" ;
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","s.blCampus.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生地域分析","nsql","hql");
		hql+=roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap);
    	if(StringUtils.isNotEmpty(modelVo.getStartDate())) {
    		hql+=" and s.createTime >= :startDate ";
    		params.put("startDate", modelVo.getStartDate());
    	}
		if(StringUtils.isNotEmpty(modelVo.getEndDate())) {
			hql+=" and s.createTime <= :endDate ";
			params.put("endDate", modelVo.getEndDate());
		}
    	if (StringUtils.isNotBlank(studentvo.getBlCampusName())) {
    		hql += " and s.blCampusId in (select o.id from Organization o where o.name like :blCampusName)";
    		params.put("blCampusName", studentvo.getBlCampusName() + "%");
    	}

    	List<Student> studentList = studentDao.findAllByHQL(hql, params);
    	List<Map<String, String>>  studentMaps = new ArrayList<Map<String, String>>();
    	Map<String, String> map = null;
    	for (Student stu : studentList) {
    		if (StringUtils.isNotBlank(stu.getLat()) && StringUtils.isNotBlank(stu.getLog())) {
    			map = new HashMap<String, String>();
        		map.put("name", stu.getName());
        		map.put("lat", stu.getLat());
        		map.put("log", stu.getLog());
        		studentMaps.add(map);
    		}
    	}
    	return studentMaps;
    }

    @Override
	public Response editStudentMapInfo(String studentId, String address, String log, String lat) {
		Response res =new Response();
		Student student = studentDao.findById(studentId);
		if(student==null){
			res.setResultCode(1);
			res.setResultMessage("该学生不存在!");
		}else{
			if (StringUtils.isNotBlank(address) && StringUtils.isNotBlank(log) && StringUtils.isNotBlank(lat)) {
				student.setAddress(address);
				student.setLat(lat);
				student.setLog(log);
				studentDao.save(student);
			} else {
				res.setResultCode(1);
				res.setResultMessage("地理信息录入不完整!");
			}
		}
		return res;
	}

    /**
     * 修改学生指纹
     * @param studentId
     * @param fingerInfo
     */
	public String uploadStudentFingerInfo(String studentFingerNo,String studentId, String fingerInfo){
		StudentFingerInfo studentFinger = new StudentFingerInfo();
		studentFinger.setStudentFingerNo(studentFingerNo);
		studentFinger.setStudentId(studentId);
		studentFinger.setFingerInfo(fingerInfo);
		studentFingerInfoDao.save(studentFinger);
		return "1";
	}

	@Override
	public List<Object[]> getAllStudentFingerInfo() {
		return studentDao.getAllStudentFingerInfo();
	}

	@Override
	public List<StudentVo> getStudentListByName(String studentName,String brenchType,String conCampusId) {
		if(StringUtils.isEmpty(studentName))
			return null;

		int con=0;//标记添加的学生校区是否有同名学生在分公司或跨分公司校区
		int oth=0;
		Organization organization = userService.getBelongBranch();
		List<Student> list=new ArrayList<Student>();

		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqls=new StringBuffer();
		sqls.append(" select * from student where (STU_STATUS = 1 OR STU_STATUS IS NULL) and name = :studentName and student_type='ENROLLED' and BL_CAMPUS_ID NOT in (select id from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%')  ");
		params.put("studentName", studentName);

		StringBuffer mysql=new StringBuffer();
		mysql.append(" select * from student where (STU_STATUS = 1 OR STU_STATUS IS NULL)  and name= :studentName and student_type='ENROLLED' and BL_CAMPUS_ID in (select id from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%') ");


		if(StringUtils.isNotBlank(brenchType) && brenchType.equals("1")){
			//本分公司
			list = studentDao.findBySql(mysql.toString(), params);

		}else if(StringUtils.isNotBlank(brenchType) && brenchType.equals("2")){
			//跨分公司
			oth+=1;
			list=studentDao.findBySql(sqls.toString(), params);
		}
		else{
			//跨分公司
			List<Student> otherStu=studentDao.findBySql(sqls.toString(), params);

			//本公司
			List<Student> myList =  studentDao.findBySql(mysql.toString(), params);

			if((myList!=null && myList.size()>0) || (otherStu!=null && otherStu.size()>0) ){
				if(StringUtils.isNotBlank(conCampusId) && myList!=null && myList.size()>0){
					list=myList;
					for(Student stu:myList){
						if(stu.getBlCampusId().equals(conCampusId)) {
							con += 1;
							break;
						 }
					}
				}
				if(con==0){
					list=otherStu;
					oth+=1;
					for(Student stu:otherStu){
						if(stu.getBlCampusId().equals(conCampusId)) {
							con += 1;
							break;
						 }
					}
				}
			}
		}

		List<StudentVo> voList=new ArrayList<StudentVo>();
		List<StudentVo> firstStu=new ArrayList<StudentVo>();
		List<StudentVo> lastStu=new ArrayList<StudentVo>();
		if(list!=null && list.size()>0){
			for(Student stu:list){
				String sql="select cus.* from contract c,customer cus where c.CUSTOMER_ID=cus.id and  c.STUDENT_ID='"+stu.getId()+"' GROUP BY c.CUSTOMER_ID";
				List<Customer> cusList=customerDao.findBySql(sql, new HashMap<String, Object>());
				if(cusList!=null && cusList.size()>0){
					for(Customer cus:cusList){
						StudentVo vo=HibernateUtils.voObjectMapping(stu, StudentVo.class);
						vo.setCusContact(cus.getContact());
						vo.setCusName(cus.getName());
						vo.setCusId(cus.getId());
						vo.setCon(con);
						vo.setOth(oth);
						if(oth==0){
							vo.setBrenchName(organization.getName());
						}else{
							//查询跨分公司
							Organization campus=organizationDao.findById(stu.getBlCampusId());
							if(campus.getParentId()!=null){
								Organization brench=organizationDao.findById(campus.getParentId());
								vo.setBrenchName(brench.getName());
							}else{
								vo.setBrenchName("");
							}

						}
						if(stu.getBlCampusId().equals(conCampusId)){
							firstStu.add(vo);
						}else{
							lastStu.add(vo);
						}
					}
				}

			}

			//当前合同校区放在最前面显示
			if(firstStu!=null && firstStu.size()>0){
				for(StudentVo s : firstStu){
					voList.add(s);
				}
			}
			if(lastStu!=null && lastStu.size()>0){
				for(StudentVo s : lastStu){
					voList.add(s);
				}
			}
		}

		return voList;
	}

	@Override
	public void updateStudentStatus(Student student) throws Exception {
		if (student.getStudentStatus().equals("0")) {
			this.checkHasFrozenCpOrAcc(student);
		}
		if(StringUtils.isEmpty(student.getId()) || StringUtils.isEmpty(student.getStudentStatus())){
			throw new ApplicationException("操作异常，请联系管理员！");
		}
		StudnetAccMv account = studnetAccMvDao.getStudnetAccMvByStudentId(student.getId());

		if ("0".equals(student.getStudentStatus()) && (account.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0 || (account.getElectronicAccount() != null && account.getElectronicAccount().compareTo(BigDecimal.ZERO) > 0))) {
			throw new ApplicationException("该学生仍有剩余资金，请将剩余资金处理后再进行无效操作。");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentStatus", student.getStudentStatus());
		params.put("studentId", student.getId());
		studentDao.excuteHql("update Student set studentStatus = :studentStatus where id = :studentId ", params);
	}

	private void checkHasFrozenCpOrAcc(Student student) {
		List<Contract> contractList = contractDao.getPaidNormalDeposits(student.getId());
    	for (Contract contract : contractList) {
    		if (contractDao.checkHasFrozenCp(contract)) {
        		throw new ApplicationException("该学生有合同产品冻结，不能修改，如需操作，请先撤销退费或转账申请");
        	}
    	}
    	StudnetAccMv stua = studnetAccMvDao.getStudnetAccMvByStudentId(student.getId());
    	if (stua.getFrozenAmount() != null && stua.getFrozenAmount().compareTo(BigDecimal.ZERO) > 0 ) {
    		throw new ApplicationException("该学生有资金冻结，不能修改，如需操作，请先撤销退费或转账申请");
    	}
	}

	/**
	 * 获取学生资金变更记录
	 * */
	@Override
	public DataPackage getStudentMoneyChanges(StudentMoneyChanges studentMoneyChanges, Map map, DataPackage dataPackage) {
		//添加校区或分公司限制
		if (studentMoneyChanges.getStudent()!=null && ((studentMoneyChanges.getStudent().getBlCampus() == null || StringUtils.isEmpty(studentMoneyChanges.getStudent().getBlCampus().getId())) && userService.getBelongCampus() != null)) {
			studentMoneyChanges.getStudent().setBlCampus(userService.getBelongCampus());
		}

		dataPackage = studentMoneyChangesDao.getStudentMondyChanges(studentMoneyChanges,map, dataPackage);
//		List<StudentMoneyChangesVo> voList = new ArrayList<StudentMoneyChangesVo>();
//		for(StudentMoneyChanges stu : list){
//			StudentMoneyChangesVo vo = HibernateUtils.voObjectMapping(stu, StudentMoneyChangesVo.class);
//			voList.add(vo);
//		}
//		return voList;

		List<StudentMoneyChanges> list = (List<StudentMoneyChanges>) dataPackage.getDatas();
		List<StudentMoneyChangesVo> voList = HibernateUtils.voListMapping(list, StudentMoneyChangesVo.class);
		for (StudentMoneyChangesVo vo : voList) {
			if (vo.getChargePayType() == ChargePayType.WASH) {
				AccountChargeRecords acc = accountChargeRecordsDao.findById(vo.getId());
				MoneyWashRecords moneyWashRecords = moneyWashRecordsDao.findByTransactionId(acc.getTransactionId());
				vo.setWashReason(moneyWashRecords.getDetailReason());
			}
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	/**
	 * 学生回访跟进记录
	 * */
	@Override
	public DataPackage getStudentFollowUp(StudentFollowUpVo studentFollowUpVo,DataPackage dataPackage) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from StudentFollowUp where 1 = 1 ");
		if(StringUtil.isNotBlank(studentFollowUpVo.getStudentId())){
			hql.append(" and student.id = :studentId ");
			params.put("studentId", studentFollowUpVo.getStudentId());
		}
		hql.append(" order by createTime desc ");
		dataPackage = studentFollowUpDao.findPageByHQL(hql.toString(), dataPackage,  true, params);
		List<StudentFollowUp> list = (List<StudentFollowUp>) dataPackage.getDatas();
		List<StudentFollowUpVo> voList = new ArrayList<StudentFollowUpVo>();
		for(StudentFollowUp studentFollowUp : list){
			StudentFollowUpVo vo = HibernateUtils.voObjectMapping(studentFollowUp, StudentFollowUpVo.class);
			voList.add(vo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	/**
	 * 保存学生回访跟进记录
	 * */
	@Override
	public void saveStudentFollowUp(StudentFollowUp studentFollowUp) {
		User followUpUser = userService.getCurrentLoginUser();
		if(StringUtils.isNotBlank(studentFollowUp.getId())){

			StudentFollowUp domain=studentFollowUpDao.findById(studentFollowUp.getId());
			if(followUpUser!=null && !domain.getCreateUserId().equals(followUpUser.getUserId())){
				throw new ApplicationException("只能修改自己保存的记录");
			}
			domain.setFollowUpResult(studentFollowUp.getFollowUpResult());
			domain.setRemark(studentFollowUp.getRemark());
			domain.setNextTime(studentFollowUp.getNextTime());
			domain.setModifyTime(DateTools.getCurrentDateTime());
			domain.setModifyUserId(userService.getCurrentLoginUserId());
			studentFollowUpDao.save(domain);
		}else {
			studentFollowUp.setCreateTime(DateTools.getCurrentDateTime());
			studentFollowUp.setFollowUpTime(DateTools.getCurrentDateTime());
			studentFollowUp.setCreateUserId(userService.getCurrentLoginUserId());
			studentFollowUp.setFollowUpUser(followUpUser);
			studentFollowUpDao.save(studentFollowUp);
		}
	}


	/**
	 * 保存学生回访跟进记录
	 * */
	@Override
	public void saveStudentFollowUp(StudentFollowUp studentFollowUp,MultipartFile documentfile) {
//		Student student = new Student();
//		student.setId(studentFollowUp.getStudent().getId());
//		studentFollowUp.setStudent(student);
		studentFollowUp.setCreateTime(DateTools.getCurrentDateTime());
		studentFollowUp.setFollowUpTime(DateTools.getCurrentDateTime());
		User followUpUser= userService.getCurrentLoginUser();
		studentFollowUp.setCreateUserId(followUpUser.getUserId());
		studentFollowUp.setFollowUpUser(followUpUser);
		if(documentfile!=null && documentfile.getSize()>0){
			String fileName=documentfile.getOriginalFilename().substring
					(0,documentfile.getOriginalFilename().lastIndexOf("."));//上传的文件名
			String puff=documentfile.getOriginalFilename().replace(fileName, "");
			String aliName = UUID.randomUUID().toString()+puff;//阿里云上面的文件名
			studentFollowUp.setFileName(fileName);
			studentFollowUp.setFileAliName(aliName);
			this.saveFileToRemoteServer(documentfile, aliName);
		}
		studentFollowUpDao.save(studentFollowUp);
	}

	/**
	 * 把上传的档案存到阿里云
	 * @param myfile1
	 * @param fileName
	 */
	public void saveFileToRemoteServer(MultipartFile myfile1, String fileName)  {
		try {
			AliyunOSSUtils.put(fileName, myfile1.getInputStream(), myfile1.getSize());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("出错了");
		}
	}
	/**
	 * 根据学生ID查询电子账户记录
	 * */
	@Override
	public List<ElectronicAccountChangeLogVo> getElectronicAccountChangeLogByStudentId(
			ElectronicAccountChangeLogVo electronicAccountChangeLogVo) {
		return electronicAccountChangeLogDao.getElectronicAccountChangeLogByStudentId(electronicAccountChangeLogVo);
	}

	@Override
	public void saveStudentTransactionCampusRecord(
			TransactionCampusRecord transactionCampusRecord) {
		transactionCampusRecord.setId(null);
		transactionCampusRecord.setUser(userService.getCurrentLoginUser());
		transactionCampusRecord.setCreateTime(DateTools.getCurrentDateTime());
		transactionCampusRecordDao.save(transactionCampusRecord);

	}

	@Override
	public void updateStudentTrunCampus(TransactionCampusRecord transactionRecord) {
		Student student=studentDao.findById(transactionRecord.getStudent().getId());
		student.setBlCampusId(transactionRecord.getNewCampus().getId());
		student.setStudyManegerId(transactionRecord.getStudyManager().getUserId());
		student.setModifyTime(DateTools.getCurrentDateTime());
		student.setModifyUserId(userService.getCurrentLoginUser().getUserId());
		studentDao.save(student);
		studentOrganizationDao.saveStudentOrganization(student);
		transactionRecord.setId(null);
		transactionRecord.setUser(userService.getCurrentLoginUser());
		transactionRecord.setCreateTime(DateTools.getCurrentDateTime());
		transactionCampusRecordDao.save(transactionRecord);
		contractProductSubjectService.updateContractSubjectForTurnCampus(student.getId(), transactionRecord.getNewCampus().getId());
	}

    /**
     * 根据老师和科目查询可排课学生
     *
     * @param teacherId
     * @param subjectId
     * @return
     */
    @Override
    public List<StudentVo> getStudentByTeacherAndSubject(String teacherId, String subjectId) {
        Set<DataDict> grades = dataDictService.getCanTeachGradeByTeacherAndSubject(teacherId,subjectId);
        Iterator<DataDict> it = grades.iterator();
        String[] gradeIds = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            gradeIds[i] = it.next().getId();
        }
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.in("gradeDict.id",gradeIds));
        criterions.add(Restrictions.disjunction().add(Restrictions.ne("studentStatus","0")).add(Restrictions.isNull("studentStatus"))); // 排除无效学生
        return HibernateUtils.voListMapping(studentDao.findAllByCriteria(criterions),StudentVo.class);
    }

    /**
     * 根据老师、科目和产品获取可消费的学生
     *
     * @param teacherId
     * @param subjectId
     * @param productId
     * @return
     */
    @Override
    public List<StudentVo> getCanConsumeStudents(String teacherId, String subjectId, String productId) {
        Set<DataDict> grades = dataDictService.getCanTeachGradeByTeacherAndSubject(teacherId,subjectId);
        Iterator<DataDict> it = grades.iterator();
        String[] gradeIds = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            gradeIds[i] = it.next().getId();
        }
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.in("gradeDict.id",gradeIds));
        criterions.add(Restrictions.disjunction().add(Restrictions.ne("studentStatus","0")).add(Restrictions.isNull("studentStatus"))); // 排除无效学生
        List<Criterion> contractProductCriterions = new ArrayList<Criterion>();
        contractProductCriterions.add(Restrictions.not(Restrictions.in("status",new Object[]{ContractProductStatus.ENDED,ContractProductStatus.CLOSE_PRODUCT})));
        contractProductCriterions.add(Restrictions.eq("product.id",productId));
        List<ContractProduct> contractProducts = contractProductDao.findAllByCriteria(contractProductCriterions);
        Iterator<ContractProduct> itContractProducts = contractProducts.iterator();
        String[] studentIds = new String[contractProducts.size()];
        for (int i = 0; i < contractProducts.size(); i++) {
            studentIds[i] = itContractProducts.next().getContract().getStudent().getId();
        }
        criterions.add(Restrictions.in("id",studentIds));
        if(gradeIds.length == 0 || studentIds.length == 0 ){
            return new ArrayList<StudentVo>();
        }
        return HibernateUtils.voListMapping(studentDao.findAllByCriteria(criterions),StudentVo.class);
    }

    @Override
	public void saveStudentOrganization(StudentOrganizationForm studentOrganizationForm) {
		studentOrganizationDao.delStudentOrganization(studentOrganizationForm.getStudentId(),"0");
		for(StudentOrganizationVo studentOrganizationVo : studentOrganizationForm.getStudentOrganizations()){
			if(StringUtil.isBlank(studentOrganizationVo.getOrganization()))
				studentOrganizationVo.setOrganization(null);
			if(StringUtil.isBlank(studentOrganizationVo.getStudyManager()))
				studentOrganizationVo.setStudyManager(null);
			StudentOrganization studentOrganization=HibernateUtils.voObjectMapping(studentOrganizationVo, StudentOrganization.class);
			studentOrganizationDao.save(studentOrganization);
		}

	}

    @Override
    public void deleteStudentOrganization(StudentOrganizationVo studentOrganizationVo) {
    	StudentOrganization studentOrganization = studentOrganizationDao.findById(studentOrganizationVo.getId());
    	Response res = new Response();
    	String studyManagerId =  studentOrganization.getStudyManager() != null ? studentOrganization.getStudyManager().getUserId() : "";
		res = checkUpdateStudentManage(studentOrganization.getStudent().getId(), studyManagerId, "", "notChangeCourse", res);
		if (res.getResultCode() != 0) {
			throw new ApplicationException(res.getResultMessage());
		} else {
			studentOrganizationDao.delete(studentOrganization);
		}
    }

    public void saveOrUpdateStudentOrganization(StudentOrganizationVo studentOrganizationVo) throws Exception {
    	StudentOrganization studentOrganization;
    	if (StringUtils.isNotBlank(studentOrganizationVo.getId())) {
    		studentOrganization = studentOrganizationDao.findById(studentOrganizationVo.getId());
    		Response res = new Response();
    		String studyManagerId =  studentOrganization.getStudyManager() != null ? studentOrganization.getStudyManager().getUserId() : "";
    		res = checkUpdateStudentManage(studentOrganization.getStudent().getId(), studyManagerId, "", "notChangeCourse", res);
    		if (res.getResultCode() != 0) {
    			throw new ApplicationException(res.getResultMessage());
    		}
    	} else {
    		studentOrganization = new StudentOrganization();
    	}
    	studentOrganization.setIsMainOrganization(studentOrganizationVo.getIsMainOrganization());
    	Organization org = new Organization();
    	org.setId(studentOrganizationVo.getOrganization());
    	studentOrganization.setOrganization(org);
    	Student student = new Student();
    	student.setId(studentOrganizationVo.getStudent());
    	studentOrganization.setStudent(student);
    	User studyManager = new User();
    	studyManager.setUserId(studentOrganizationVo.getStudyManager());
    	studentOrganization.setStudyManager(studyManager);
    	otmClassService.updateOtmClassStudentAttendentStudyManager(studentOrganizationVo.getStudyManager(), studentOrganizationVo.getStudent(), studentOrganizationVo.getOrganization());
    	studentOrganizationDao.save(studentOrganization);
    }

	@Override
	public List<StudentOrganizationVo> getStudentOrganization(String studentId) {
		List<Criterion> criterionList =new ArrayList<Criterion>();
		criterionList.add(Expression.eq("student.id", studentId));
		List<StudentOrganization> studentOrganizationList=studentOrganizationDao.findAllByCriteria(criterionList);
		return HibernateUtils.voListMapping(studentOrganizationList, StudentOrganizationVo.class);
	}

	@Override
	public List<MobileUserVo> getMobileContactsForStaff(String staffId) {
		List<Student> stus = studentDao.findStudentByStaffId(staffId);
		List<MobileUserVo> voList =  new ArrayList<MobileUserVo>();
		for(Student stu : stus) {
			// 判断有无mobileUser
			MobileUser mobileUserForUser = mobileUserService.findMobileUserByStuId(stu.getId());
			MobileUserVo mobileUserVo = HibernateUtils.voObjectMapping(mobileUserForUser, MobileUserVo.class);
			mobileUserVo.setName(stu.getName());
			voList.add(mobileUserVo);
		}
		return voList;
	}
	@Override
	public List<StudentVo> findStudentByCampus() {
		Organization organization = userService.getBelongCampus();
		List<Student> students = studentDao.findStudentByCampus(organization);
//		return HibernateUtils.voListMapping(studentDao.findStudentByCampus(organization), StudentVo.class);
		List<StudentVo> studentVos=new ArrayList<StudentVo>();
		 for (int i = 0; i < students.size(); i++) {
	            Student student = students.get(i);
	            StudentVo vo = new StudentVo();
	            vo.setId(student.getId());
	            vo.setName(student.getName());
//	            map.put(student.getId(),vo);
	            studentVos.add(vo);
	        }
		 return studentVos;
	}

	/**
	 * 查找所有学生
	 */
	@Override
	public List<AutoCompleteOptionVo> getStudentAutoComplateAll(String term) {
		List<Student> students = studentDao.getStudentAutoComplate(term);
		List<AutoCompleteOptionVo> vos=new ArrayList<AutoCompleteOptionVo>();
		 for (int i = 0; i < students.size(); i++) {
	            Student student = students.get(i);
	            AutoCompleteOptionVo vo = new AutoCompleteOptionVo();
	            vo.setValue(student.getId());
	            String label = student.getName() + "-" + student.getId();
	            if (student.getBlCampus() != null && student.getBlCampus().getName() != null) {
	            	label += "," + student.getBlCampus().getName();
	            }
	            if (student.getStudyManeger() != null && student.getStudyManeger().getName() != null) {
	            	label += "," + student.getStudyManeger().getName();
	            }
	            vo.setLabel(label);
	            vos.add(vo);
	        }
		 return vos;
	}

	/**
	 * 查找某校区的学生
	 */
	@Override
	public List<StudentVo> findStudentsByCampusId(String campusId) {
		Organization organization = organizationDao.findById(campusId);
		List<Student> students = studentDao.findStudentByCampus(organization);
		List<StudentVo> studentVos=new ArrayList<StudentVo>();
		 for (int i = 0; i < students.size(); i++) {
	            Student student = students.get(i);
	            StudentVo vo = new StudentVo();
	            vo.setId(student.getId());
	            vo.setName(student.getName());
	            studentVos.add(vo);
	        }
		 return studentVos;
	}


	@Override
	public DataPackage getStudentComment(String stuId, DataPackage dataPackage) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from StudentComment where 1 = 1 ");
		if(StringUtil.isNotBlank(stuId)){
			hql.append(" and student.id = :stuId ");
			params.put("stuId", stuId);
		}
		dataPackage = studentCommentDao.findPageByHQL(hql.toString(), dataPackage, true, params);
		List<StudentComment> list = (List<StudentComment>) dataPackage.getDatas();
		List<StudentCommentVo> voList = new ArrayList<StudentCommentVo>();
		for(StudentComment studentComment : list){
			StudentCommentVo vo = HibernateUtils.voObjectMapping(studentComment, StudentCommentVo.class);
			voList.add(vo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	@Override
	public void saveStudentComment(StudentComment studentComment) {
		studentComment.setCreateTime(DateTools.getCurrentDateTime());
		User user= userService.getCurrentLoginUser();
		studentComment.setCreateUser(user);
		studentCommentDao.save(studentComment);

		//studentCommentDao.getCurrentSession().refresh(studentComment);
		// 增加一个动态的学生数据
		Student student=studentDao.findById(studentComment.getStudent().getId());
		studentComment.setStudent(student);
		studentDynamicStatusService.createCommentDynamicStatus(studentComment);

	}
	/**
	 * 获取所有学生校区*/
	@Override
	public List<Object> getAllStudentOrganization() {
		return studentDao.getAllStudentOrganization();
	}

	/**
	 * 查询该校区所有状态为"上课中"的学生ID*/
	@Override
	public List<Map<Object, Object>> getStudentIdByOrganizationId(String organizationId) {
		return studentDao.getStudentIdByOrganizationId(organizationId);
	}

	/**
	 * 查询所有状态为"上课中"的学生ID*/
	@Override
	public List<Map<Object, Object>> getAllStudentId() {
		return studentDao.getAllStudentId();
	}

	/**
	 通过学生ID查询学生指纹*/
	@Override
	public StudentFingerInfo getStudentFingerInfoByStudentId(String studentId) {
		List<StudentFingerInfo> studentFingerList=studentFingerInfoDao.findByCriteria(Expression.eq("studentId", studentId));
		if(studentFingerList!=null && studentFingerList.size()>0){
			return studentFingerList.get(0);
		}
		return null;
	}

	/**
	 * 获取所有上课中的学生指纹*/
	@Override
	public List<Object[]> getAllStudentFingerInfoByStatus() {
		return studentDao.getAllStudentFingerInfoByStatus();

	}

	/**
	 * 获取所有今天有课的学生指纹*/
	@Override
	public List<Object[]> getTodayStudentFingerInfo() {
		return studentDao.getTodayStudentFingerInfo();

	}


	/**
	 * 保存学生档案
	 * @param myfile
	 * @param studentFileVo
	 * @param servicePath
	 */

	@Override
	public void saveStudentFile(MultipartFile myfile, StudentFileVo studentFileVo,String servicePath) {
			StudentFile studentFile = HibernateUtils.voObjectMapping(studentFileVo, StudentFile.class);
			User user=userService.getCurrentLoginUser();
			String currentDateTime=DateTools.getCurrentDateTime();
			studentFile.setCreator(user);//创建者
			studentFile.setCreateTime(currentDateTime);//创建时间
			String aliName = UUID.randomUUID().toString()+myfile.getOriginalFilename().substring(myfile.getOriginalFilename().lastIndexOf("."));//阿里云上面的文件名
			studentFile.setFileName(myfile.getOriginalFilename());//上传的文件名
			studentFile.setSaveName(aliName);//阿里云上面的文件名

			try {
				AliyunOSSUtils.put(aliName, myfile.getInputStream(),myfile.getSize());//传原图到阿里云
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			studentFileDao.save(studentFile);

	}

	public void isNewFolder(String folder){
		File f1=new File(folder);
		if(!f1.exists())
		{
			f1.mkdirs();
		}
	}

	/**
	 * 获取学生档案
	 * @param studentFileVo
	 * @param dp
     * @return
     */
	@Override
	public DataPackage getStudentFile(StudentFileVo studentFileVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlWhere=" from StudentFile a where 1=1";
		if(StringUtils.isNotEmpty(studentFileVo.getStudentId())){
			hqlWhere+=" and a.student.id = :studentId ";
			params.put("studentId", studentFileVo.getStudentId());
		}
		if(studentFileVo.getStudentFileType()!=null){
			hqlWhere+=" and a.studentFileType  = :studentFileType ";
			params.put("studentFileType", studentFileVo.getStudentFileType());
		}
		hqlWhere+=" order by a.createTime desc";
		dp=studentFileDao.findPageByHQL(hqlWhere, dp, true, params);
		List<StudentFile> list= (List<StudentFile>) dp.getDatas();
		List<StudentFile> newList = new ArrayList<StudentFile>();
		for(StudentFile studentFile:list){
			//studentFile.setRealPath(PropertiesUtils.getStringValue("oss.access.url.prefix") + "" + studentFile.getSaveName());
			studentFile.setRealPath(studentFile.getSaveName());
			newList.add(studentFile);
		}

		List<StudentFileVo> voList =  HibernateUtils.voListMapping(list, StudentFileVo.class);
		dp.setDatas(voList);
		return dp;
	}

	/**
	 * PC端获取学生档案
	 * @param studentFileVo
	 * @param dp
	 * @param timeVo
     * @return
     */
	@Override
	public DataPackage getStudentFileByPC(StudentFileVo studentFileVo, DataPackage dp, TimeVo timeVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlWhere=" from StudentFile a where 1=1";
		if(StringUtils.isNotEmpty(studentFileVo.getStudentId())){
			hqlWhere+=" and a.student.id = :studentId ";
			params.put("studentId", studentFileVo.getStudentId());
		}
		if(studentFileVo.getStudentFileType()!=null){
			hqlWhere+=" and a.studentFileType  = :studentFileType ";
			params.put("studentFileType", studentFileVo.getStudentFileType());
		}

		if(StringUtils.isNotEmpty(timeVo.getStartDate())){
			hqlWhere+=" and a.createTime  >= :startDate ";
			params.put("startDate", timeVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotEmpty(timeVo.getEndDate())){
			hqlWhere+=" and a.createTime <= :endDate ";
			params.put("endDate", timeVo.getEndDate() + " 23:59:59");
		}


		hqlWhere+=" order by a.createTime desc";
		dp=studentFileDao.findPageByHQL(hqlWhere, dp, true, params);
		List<StudentFile> list= (List<StudentFile>) dp.getDatas();
		List<StudentFile> newList = new ArrayList<StudentFile>();
		for(StudentFile studentFile:list){
			studentFile.setRealPath(PropertiesUtils.getStringValue("oss.access.url.prefix") + "" + studentFile.getSaveName());
			newList.add(studentFile);
		}

		List<StudentFileVo> voList =  HibernateUtils.voListMapping(list, StudentFileVo.class);
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public void updateStudentForSimple(StudentVo studentVo) {
		if(studentVo.getId()!=null) {
			Student student = this.studentDao.findById(studentVo.getId());
			if(studentVo.getName()!=null) {			// 学生姓名
				student.setName(studentVo.getName());
			}
			if(studentVo.getContact()!=null) {			// 电话
				student.setContact(studentVo.getContact());
			}
			if(studentVo.getStatus()!=null) {  // 状态
				if(StudentStatus.NEW.getValue().equals(studentVo.getStatus())){//新签
					student.setStatus(StudentStatus.NEW);
				}else if(StudentStatus.CLASSING.getValue().equals(studentVo.getStatus())){//上课中
					student.setStatus(StudentStatus.CLASSING);
				}else if(StudentStatus.STOP_CLASS.getValue().equals(studentVo.getStatus())){//停课
					student.setStatus(StudentStatus.STOP_CLASS);
					if(StringUtils.isBlank(student.getFinishTime())) {
						student.setFinishTime(DateTools.getCurrentDateTime());
					}
				}else if(StudentStatus.FINISH_CLASS.getValue().equals(studentVo.getStatus())){//结课
					student.setStatus(StudentStatus.FINISH_CLASS);
					if(StringUtils.isBlank(student.getFinishTime())) {
						student.setFinishTime(DateTools.getCurrentDateTime());
					}
				}
			}
			if(studentVo.getEnrolDate()!=null) {  //入学日期
				student.setEnrolDate(studentVo.getEnrolDate());
			}
			if(studentVo.getIcCardNo()!=null) {  //IC卡编号
				student.setIcCardNo(studentVo.getIcCardNo());
			}
			if(studentVo.getSex()!=null) {  //性别
				student.setSex(studentVo.getSex());
			}
			if(studentVo.getBlCampusId()!=null) {  // 校区
				student.setBlCampusId(studentVo.getBlCampusId());
			}
			if(studentVo.getGradeId()!=null) {  // 年级
				student.setGradeDict(this.dataDictDao.findById(studentVo.getGradeId()));
			}
			if(studentVo.getAddress()!=null) {  // 联系地址
				student.setAddress(studentVo.getAddress());
			}
			if(studentVo.getBothday()!=null) {  // 出生日期
				student.setBothday(studentVo.getBothday());
			}
			if(studentVo.getFatherName()!=null) {  // 父亲名字
				student.setFatherName(studentVo.getFatherName());
			}
			if(studentVo.getFatherPhone()!=null) {  // 父亲电话
				student.setFatherPhone(studentVo.getFatherPhone());
			}
			if(studentVo.getMotherName()!=null) {  // 母亲名字
				student.setMotherName(studentVo.getMotherName());
			}
			if(studentVo.getNotherPhone()!=null) {  // 母亲电话
				student.setNotherPhone(studentVo.getNotherPhone());
			}
			if(studentVo.getSchoolId()!=null) {  // 学生学校
				student.setSchool(this.studentSchoolDao.findById(studentVo.getSchoolId()));
			}
			if(studentVo.getSchoolName()!=null) {  // 学生学校
				List<StudentSchool> list= studentSchoolDao.findByCriteria(Expression.eq("name", studentVo.getSchoolName()));
				if(list!=null && list.size()>0){//存在
					student.setSchool(list.get(0));
				}else{//不存在
					StudentSchool studentSchool=new StudentSchool();
					studentSchool.setName(studentVo.getSchoolName());
					studentSchoolDao.save(studentSchool);
					student.setSchool(studentSchool);
				}
			}

		}
	}

	 @Override
	public Map<Object, Object> getCusAndStuByStudentId(String studentId) {
		// TODO Auto-generated method stub
		return studentDao.getCusAndStuByStudentId(studentId);
	}

	 /**
	  * 批量升级学生
	  */

	@Override
	public void upGrade(String cla, String course, String orgnizationId, String gradeNames) {
		Organization org = organizationDao.findById(orgnizationId);
		OrganizationType type=org.getOrgType();
		this.studentDao.upgrade(cla, course, orgnizationId,type, gradeNames);

	}

	/**
	 * 小班学生列表
	 */
	@Override
	public DataPackage getMiniClassStudents(StudentVo studentvo,MiniClassVo miniclassvo, DataPackage dp,String brenchId, String miniClassGradeId,
			ModelVo modelvo,String stuNameGradeSchool){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("  from ContractProduct cp, MiniClassStudent mcs, Contract c, Student s, Organization brench");
		hql.append(" where 1=1 and cp.id = mcs.contractProduct.id and cp.contract.id = c.id and c.student.id = s.id ");
		hql.append(" and s.blCampus.parentId = brench.id ");
		hql.append(" and cp.type='small_class' ");
		//学生列表只查询正式学生
		hql.append(" and s.studentType='ENROLLED' ");
		//hql.append(roleQLConfigService.getValueResult("学生列表","hql"));
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","s.blCampus.orgLevel");
		sqlMap.put("stuId","mcs.miniClass.miniClassId");
		sqlMap.put("manergerId","mcs.miniClass.miniClassId");
		sqlMap.put("selStuId","( select miniClassId from MiniClass where teacher.userId ='"+userService.getCurrentLoginUserId()+"')");
		sqlMap.put("selManerger","( select miniClassId from MiniClass where studyManeger.userId ='"+userService.getCurrentLoginUserId()+"')");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","hql");
		hql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		if(StringUtils.isNotBlank(modelvo.getStartDate())){
			hql.append(" and cp.createTime >= :startDate ");
			params.put("startDate", modelvo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotBlank(modelvo.getEndDate())){
			hql.append(" and cp.createTime <= :endDate ");
			params.put("endDate", modelvo.getEndDate() + " 23:59:59");
		}
		if(StringUtils.isNotBlank(studentvo.getName())){
			hql.append(" and s.name like :studentName ");
			params.put("studentName", "%" + studentvo.getName() + "%");
		}
		if(StringUtils.isNotBlank(studentvo.getSchoolName())){
			hql.append(" and s.school.name like :schoolName ");
			params.put("schoolName", "%" + studentvo.getSchoolName() + "%");
		}
		if(StringUtils.isNotBlank(studentvo.getGradeId())){
			hql.append(" and s.gradeDict.id= :gradeId ");
			params.put("gradeId", studentvo.getGradeId());
		}
		if(StringUtils.isBlank(studentvo.getBlCampusId()) && StringUtils.isNotBlank(brenchId) ){
			hql.append(" and s.blCampus.id in (select id from Organization where parentId = :brenchId) ");
			params.put("brenchId", brenchId);
		}
		if(StringUtils.isNotBlank(studentvo.getBlCampusId())){
			hql.append(" and s.blCampus.id = :blCampusId ");
			params.put("blCampusId", studentvo.getBlCampusId());
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductName())){
			hql.append(" and cp.product.name like :productName ");
			params.put("productName", "%" + miniclassvo.getProductName() + "%");
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductVersionId())){
			hql.append(" and cp.product.productVersion.id = :productVersionId ");
			params.put("productVersionId", miniclassvo.getProductVersionId());
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductQuarterId())){
			hql.append(" and cp.product.productQuarter.id = :productQuarterId ");
			params.put("productQuarterId", miniclassvo.getProductQuarterId());
		}
		if(StringUtils.isNotBlank(modelvo.getModelName1())){
			hql.append(" and mcs.miniClass.name like :modelName1 ");
			params.put("modelName1", "%" + modelvo.getModelName1() + "%");
		}
		if( StringUtils.isNotBlank(miniclassvo.getSubjectId())){
			hql.append(" and mcs.miniClass.subject.id = :subjectId ");
			params.put("subjectId", miniclassvo.getSubjectId());
		}
		if(StringUtils.isNotBlank(miniClassGradeId)){
			hql.append(" and mcs.miniClass.grade.id = :miniClassGradeId ");
			params.put("miniClassGradeId", miniClassGradeId);
		}
		if(StringUtils.isNotBlank(miniclassvo.getTeacherId())){
			hql.append(" and mcs.miniClass.teacher.userId = :teacherId ");
			params.put("teacherId", miniclassvo.getTeacherId());
		}
		if(StringUtils.isNotBlank(miniclassvo.getStudyManegerId())){
			if("null".equals(miniclassvo.getStudyManegerId())){
				hql.append(" and mcs.miniClass.studyManeger.userId is null ");
			}else{
				hql.append(" and mcs.miniClass.studyManeger.userId = :studyManegerId ");
				params.put("studyManegerId", miniclassvo.getStudyManegerId());
			}
		}
		if(StringUtils.isNotBlank(modelvo.getModelName3())){
			hql.append(" and cp.contract.student.status = :modelName3 ");
			params.put("modelName3", StudentStatus.valueOf(modelvo.getModelName3()));
		}
		if(StringUtils.isNotBlank(studentvo.getSmallClassStatus())){
			hql.append(" and cp.contract.student.smallClassStatus = :smallClassStatus ");
			params.put("smallClassStatus", StudentSmallClassStatus.valueOf(studentvo.getSmallClassStatus()));
		}
		if(StringUtils.isNotBlank(modelvo.getModelName2())){
			if(modelvo.getModelName2().equals("1")){
				//已报读
				hql.append(" and cp.miniClassId is not null ");
			}else if(modelvo.getModelName2().equals("0")){
				hql.append(" and cp.miniClassId is null ");
			}

		}
		if(StringUtils.isNotBlank(miniclassvo.getMinCourseHours())){
			hql.append(" and (  ");
			hql.append("( ROUND ((cp.paidAmount + cp.promotionAmount - cp.consumeAmount)/cp.price,2) >= :minCourseHours and cp.paidStatus='PAID' )");
			params.put("minCourseHours", Integer.valueOf(miniclassvo.getMinCourseHours()));
			hql.append(" or ");
			hql.append(" ( ROUND ((cp.paidAmount - cp.consumeAmount )/cp.price,2) >= :minCourseHours2 and cp.paidStatus <> 'PAID' ) " );
			params.put("minCourseHours2", Integer.valueOf(miniclassvo.getMinCourseHours()));
			hql.append(" ) ");
			hql.append(" and cp.status != 'CLOSE_PRODUCT' and cp.status != 'REFUNDED'  ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getMaxCourseHours())){
			hql.append(" and (  ");
			hql.append("( ROUND ((cp.paidAmount + cp.promotionAmount - cp.consumeAmount)/cp.price,2) <= :maxCourseHours and cp.paidStatus='PAID' )");
			params.put("maxCourseHours", Integer.valueOf(miniclassvo.getMaxCourseHours()));
			hql.append(" or ");
			hql.append(" ( ROUND ((cp.paidAmount - cp.consumeAmount )/cp.price,2) <= :maxCourseHours2 and cp.paidStatus <> 'PAID'  ) " );
			params.put("maxCourseHours2", Integer.valueOf(miniclassvo.getMaxCourseHours()));
			hql.append(" ) ");
			if(StringUtils.isBlank(miniclassvo.getMinCourseHours())){
				hql.append(" and cp.status != 'CLOSE_PRODUCT' and cp.status != 'REFUNDED'  ");
			}
		}
		if(studentvo.getStudentStatus()!=null){
			if("1".equals(studentvo.getStudentStatus())) {
				hql.append(" and (s.studentStatus = :studentStatus or s.studentStatus is null) ");
				params.put("studentStatus", studentvo.getStudentStatus());
			}
			else if("0".equals(studentvo.getStudentStatus())) {
				hql.append(" and cp.contract.student.studentStatus = :studentStatus ");
				params.put("studentStatus", studentvo.getStudentStatus());
			}
		}else{
			hql.append(" and (s.studentStatus ='1' or s.studentStatus is null) ");
		}

		if(StringUtils.isNotBlank(stuNameGradeSchool) && stuNameGradeSchool!=null){
			hql.append(" and ( s.name like :studentName2 or s.gradeDict.name like :gradeName2 or s.school.name like :schoolName2 )");
			params.put("studentName2", "%" + stuNameGradeSchool + "%");
			params.put("gradeName2", "%" + stuNameGradeSchool + "%");
			params.put("schoolName2", "%" + stuNameGradeSchool + "%");
		}

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			if(dp.getSidx().equals("miniClassStatus")){
				hql.append(" order by mcs.miniClass.status " +dp.getSord());
			}else if(dp.getSidx().equals("miniClassStudentStatus")){
				hql.append(" order by s.smallClassStatus " +dp.getSord());
			}else if(dp.getSidx().equals("status")){
				hql.append(" order by s.status  "+dp.getSord());
			} else if (dp.getSidx().equals("contractProductCreateDate")) {
				hql.append(" order by cp.createTime  "+dp.getSord());
			} else if (dp.getSidx().equals("studentStatus")) {
				hql.append(" order by s.status  "+dp.getSord());
			}else{
				hql.append(" order by cp."+dp.getSidx()+" "+dp.getSord());
			}
		}

		dp = studentDao.findPageByHQL(hql.toString(), dp, true, params);
		List<Object[]> list = (List<Object[]>) dp.getDatas();
		List<SmallClassStudentVo> returnList = new ArrayList<SmallClassStudentVo>();
		for (Object[] objects : list) {
			ContractProduct cp = (ContractProduct)objects[0];
			MiniClassStudent mcs = (MiniClassStudent)objects[1];
			Contract contract = (Contract)objects[2];
			Student student = (Student) objects[3];
			Organization brench = (Organization)objects[4];
			SmallClassStudentVo vo = new SmallClassStudentVo();
			if (cp != null) {
				vo.setContractProductCreateDate(cp.getCreateTime().substring(0, 10));
				vo.setProductName(cp.getProduct().getName());
			}
			if (student != null) {
				vo.setStudentId(student.getId());
				vo.setStudentName(student.getName());
				vo.setSex(student.getSex());
				vo.setStudentcontact(student.getContact());
				vo.setBlCampusName(student.getBlCampus().getName());
				vo.setStudentAttanceNo(student.getAttanceNo());
				vo.setStudentStatus(student.getStatus()!=null ? student.getStatus().getName():"");
				vo.setStatus(student.getStudentStatus());
				if (student.getSchool() != null) {
					vo.setSchoolName(student.getSchool().getName());
				}
				if (student.getGradeDict() != null) {
					vo.setStudentGradeId(student.getGradeDict().getId());
					vo.setStudentGradeName(student.getGradeDict().getName());
				}
				if (student.getSmallClassStatus() != null) {
					vo.setMiniClassStudentStatus(student.getSmallClassStatus().getName());
				}
//				StudnetAccMvVo stuAccMv=this.getStudentAccoutInfo(student.getId());
				if(cp!=null){
					BigDecimal remainingHour=cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP);
					vo.setMiniRemainingHour(remainingHour);

				}
			}
			if (brench != null) {
				vo.setBrenchName(brench.getName());
			}
			if (mcs != null) {
				MiniClass mc = mcs.getMiniClass();
				if (mc != null) {
					vo.setMiniClassName(mc.getName());
					if (mc.getSubject() != null) {
						vo.setSubjectName(mc.getSubject().getName());
					}
					if (mc.getGrade() != null) {
						vo.setGradeName(mc.getGrade().getName());
					}
					if (null != mc.getStudyManeger()) {
						vo.setStudyManagerName(mc.getStudyManeger().getName());
					}
					if (mc.getTeacher() != null) {
						vo.setTeacherName(mc.getTeacher().getName());
					}
					vo.setStartDate(mc.getStartDate());
					vo.setStartTime(mc.getTimeSpace());
					vo.setMiniClassStatus(mc.getStatus().getName());
				}
			}
			returnList.add(vo);
		}
		dp.setDatas(returnList);

		return dp;
	}

	/**
	 * 小班学生列表
	 *//*
	@Override
	public DataPackage getMiniClassStudents(StudentVo studentvo,MiniClassVo miniclassvo, DataPackage dp,String brenchId, String miniClassGradeId,
			ModelVo modelvo,String stuNameGradeSchool){
		StringBuffer sql = new StringBuffer(" SELECT s.ID STUDENT_ID, s.`NAME` STUDENT_NAME, s.ATTANCE_NO, cp.CREATE_TIME CONTRACT_PRODUCT_CREATE_TIME, ");
		sql.append(" s.SEX, s.CONTACT, s.SCHOOL SCHOOL_ID, s.GRADE_ID STUDENT_GRADE_ID, s.STU_STATUS STUDENT_STATUS, s.`STATUS`, s.SMALL_CLASS_STATUS,  ");
		sql.append(" org_brench.`NAME` BRENCH_NAME, org_campus.`NAME` BL_CAMPUS_NAME, mcs.MINI_CLASS_ID, p.`NAME` PRODUCT_NAME, cp.ID CONTRACT_PRODUCT_ID ");



		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append(" FROM mini_class_student mcs ");
		sqlFrom.append(" left join  student s on s.id=mcs.student_id");
		sqlFrom.append(" left join  organization org_campus on s.BL_CAMPUS_ID = org_campus.id");
		sqlFrom.append(" left join  organization org_brench on org_brench.id = org_campus.parentID	 	");
		sqlFrom.append(" left join contract_product cp on cp.id=mcs.CONTRACT_PRODUCT_ID");
		sqlFrom.append(" left join product p on cp.PRODUCT_ID=p.ID	");
		sqlFrom.append(" left join mini_class mc on mc.MINI_CLASS_ID=mcs.MINI_CLASS_ID	");


		StringBuilder sqlWhere = new StringBuilder(" WHERE 1=1 ");

		sqlWhere.append(" AND cp.TYPE='SMALL_CLASS' ");
		//学生列表只查询正式学生
		sqlWhere.append(" AND s.STUDENT_TYPE='ENROLLED' ");
		sqlWhere.append(roleQLConfigService.getValueResult("学生列表","sql"));
		if(StringUtils.isNotBlank(modelvo.getStartDate())){
			sqlWhere.append(" AND cp.CREATE_TIME >= '"+modelvo.getStartDate()+" 00:00:00 ' ");
		}
		if(StringUtils.isNotBlank(modelvo.getEndDate())){
			sqlWhere.append(" AND cp.CREATE_TIME <= '"+modelvo.getEndDate()+"  23:59:59 ' ");
		}
		if(StringUtils.isNotBlank(studentvo.getName())){
			sqlWhere.append(" AND s.`NAME` LIKE '%"+studentvo.getName()+"%' ");
		}
		if(StringUtils.isNotBlank(studentvo.getSchoolName())){
			sql.append(" , ss.`NAME` SCHOOL_NAME");
			sqlFrom.append(" left join student_school ss on s.SCHOOL = ss.ID	");
			sqlWhere.append(" AND ss.`NAME` LIKE '%"+studentvo.getSchoolName()+"%' ");
		}
		if(StringUtils.isNotBlank(studentvo.getGradeId())){
			sqlWhere.append(" AND s.GRADE_ID= '"+studentvo.getGradeId()+"' ");
		}
		if(StringUtils.isBlank(studentvo.getBlCampusId()) && StringUtils.isNotBlank(brenchId) ){
			sqlWhere.append(" AND s.BL_CAMPUS_ID IN (SELECT ID FROM organization WHERE parentId='"+brenchId+"') ");
		}
		if(StringUtils.isNotBlank(studentvo.getBlCampusId())){
			sqlWhere.append(" AND s.BL_CAMPUS_ID = '"+studentvo.getBlCampusId()+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductName())){
			sqlWhere.append(" AND p.`NAME` LIKE '%"+miniclassvo.getProductName()+"%' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductVersionId())){
			sqlWhere.append(" AND p.PRODUCT_VERSION_ID = '"+miniclassvo.getProductVersionId()+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductQuarterId())){
			sqlWhere.append(" AND p.PRODUCT_QUARTER_ID = '"+miniclassvo.getProductQuarterId()+"' ");
		}
		if(StringUtils.isNotBlank(modelvo.getModelName1())){
			sqlWhere.append(" AND mc.`NAME` LIKE '%"+modelvo.getModelName1()+"%'");
		}
		if( StringUtils.isNotBlank(miniclassvo.getSubjectId())){
			sqlWhere.append(" AND mc.SUBJECT = '"+miniclassvo.getSubjectId()+"' ");
		}
		if(StringUtils.isNotBlank(miniClassGradeId)){
			sqlWhere.append(" AND mc.GRADE = '"+miniClassGradeId+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getTeacherId())){
			sqlWhere.append(" AND mc.TEACHER_ID = '"+miniclassvo.getTeacherId()+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getStudyManegerId())){
			if("null".equals(miniclassvo.getStudyManegerId())){
				sqlWhere.append(" AND mc.STUDY_MANEGER_ID IS NULL ");
			}else{
				sqlWhere.append(" AND mc.STUDY_MANEGER_ID = '"+miniclassvo.getStudyManegerId()+"' ");
			}
		}
		if(StringUtils.isNotBlank(modelvo.getModelName3())){
			sqlWhere.append(" AND s.STATUS = '"+modelvo.getModelName3()+"' ");
		}
		if(StringUtils.isNotBlank(studentvo.getSmallClassStatus())){
			sqlWhere.append(" AND s.SMALL_CLASS_STATUS = '"+studentvo.getSmallClassStatus()+"' ");
		}
		if(StringUtils.isNotBlank(modelvo.getModelName2())){
			if(modelvo.getModelName2().equals("1")){
				//已报读
				sqlWhere.append(" AND cp.MINI_CLASS_ID IS NOT NULL ");
			}else if(modelvo.getModelName2().equals("0")){
				sqlWhere.append(" AND cp.MINI_CLASS_ID IS NULL ");
			}

		}

		if(StringUtils.isNotBlank(miniclassvo.getMinCourseHours())){
			sqlWhere.append(" AND (  ");
			sqlWhere.append("( ROUND ((cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT)/cp.PRICE,2) >="+miniclassvo.getMinCourseHours()+" AND cp.PAID_STATUS='PAID' )");
			sqlWhere.append(" OR ");
			sqlWhere.append(" ( ROUND ((cp.PAID_AMOUNT - cp.CONSUME_AMOUNT )/cp.PRICE,2) >="+miniclassvo.getMinCourseHours()+" AND cp.PAID_STATUS <> 'PAID' ) " );
			sqlWhere.append(" ) ");
			sqlWhere.append(" AND cp.STATUS != 'CLOSE_PRODUCT' AND cp.STATUS != 'REFUNDED'  ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getMaxCourseHours())){
			sqlWhere.append(" AND (  ");
			sqlWhere.append("( ROUND ((cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT)/cp.PRICE,2) <="+miniclassvo.getMaxCourseHours()+" AND cp.PAID_STATUS='PAID' )");
			sqlWhere.append(" OR ");
			sqlWhere.append(" ( ROUND ((cp.PAID_AMOUNT - cp.CONSUME_AMOUNT )/cp.PRICE,2) <="+miniclassvo.getMaxCourseHours()+" and cp.PAID_STATUS <> 'PAID'  ) " );
			sqlWhere.append(" ) ");
			if(StringUtils.isBlank(miniclassvo.getMinCourseHours())){
				sqlWhere.append(" AND cp.STATUS != 'CLOSE_PRODUCT' AND cp.STATUS != 'REFUNDED'  ");
			}
		}

		if(studentvo.getStudentStatus()!=null){
			if("1".equals(studentvo.getStudentStatus()))
				sqlWhere.append(" AND (s.STU_STATUS ='"+studentvo.getStudentStatus()+"' OR s.STU_STATUS IS NULL) ");
			else if("0".equals(studentvo.getStudentStatus()))
				sqlWhere.append(" AND s.STU_STATUS ='"+studentvo.getStudentStatus()+"' ");
		}else{
			sqlWhere.append(" AND (s.STU_STATUS ='1' OR s.STU_STATUS IS NULL) ");
		}

		if(StringUtils.isNotBlank(stuNameGradeSchool) && stuNameGradeSchool!=null){
			sqlWhere.append(" AND ( s.`NAME` LIKE '%"+stuNameGradeSchool+"%' or mc.`NAME` like '%"+stuNameGradeSchool+"%')");
		}

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			if(dp.getSidx().equals("miniClassStatus")){
				sqlWhere.append(" ORDER BY mc.STATUS " +dp.getSord());
			}else if(dp.getSidx().equals("miniClassStudentStatus")){
				sqlWhere.append(" ORDER BY s.SMALL_CLASS_STATUS " +dp.getSord());
			}else if(dp.getSidx().equals("status")){
				sqlWhere.append(" ORDER BY s.STATUS  "+dp.getSord());
			} else if (dp.getSidx().equals("contractProductCreateDate")) {
				sqlWhere.append(" ORDER BY cp.CREATE_TIME  "+dp.getSord());
			} else if (dp.getSidx().equals("studentStatus")) {
				sqlWhere.append(" ORDER BY s.STATUS  "+dp.getSord());
			}else{
				sqlWhere.append(" ORDER BY cp."+dp.getSidx()+" "+dp.getSord());
			}
		}

		sql.append(sqlFrom).append(sqlWhere);

		dp = jdbcTemplateDao.queryPage(sql.toString(), SmallClassStudentJdbc.class, dp, true);
		List<SmallClassStudentJdbc> list = (List<SmallClassStudentJdbc>) dp.getDatas();

		Map<String, String> studentGradeMap = new HashMap<>();
		StringBuilder studentGrade_id = new StringBuilder(1024);
		StringBuilder schoolId = new StringBuilder(1024);
		StringBuilder miniclassId = new StringBuilder(1024);
		for (SmallClassStudentJdbc obj : list) {
			if(obj.getStudentGradeId()!=null){
				studentGrade_id.append("'"+obj.getStudentGradeId()+"',");
			}
			if(obj.getSchoolId()!=null){
				schoolId.append("'"+obj.getSchoolId()+"',");
			}
			if(obj.getMiniClassId()!=null){
				miniclassId.append("'"+obj.getMiniClassId()+"',");
			}
		}
		StringBuilder query_studentGrade = new StringBuilder(128);
		StringBuilder query_school = new StringBuilder(128);
		StringBuilder query_miniclass = new StringBuilder(128);
		Map<String, String> studentGrade = new HashMap<>();
		Map<String, String> school = new HashMap<>();
		Map<String, String> miniclass_subject = new HashMap<>();
		Map<String, String> miniclass_grade = new HashMap<>();
		Map<String, String> miniclass_study = new HashMap<>();
		Map<String, String> miniclass_teacher = new HashMap<>();
		Map<String, String> miniclass_status = new HashMap<>();
		Map<String, String> miniclass_name = new HashMap<>();
		if(studentGrade_id.length()>1){
			query_studentGrade.append(" select d.ID as stuGradeId,d.`NAME` as stuGradeName from data_dict d WHERE d.ID in ("+studentGrade_id.substring(0, studentGrade_id.length()-1)+")");
		    List<Map<String, String>> result  = dataDictDao.findMapBySql(query_studentGrade.toString());
            for(Map<String, String> map:result){
            	studentGrade.put(map.get("stuGradeId"), map.get("stuGradeName"));
            }
		}
		if(schoolId.length()>1){
			query_school.append(" select d.ID as schoolId,d.`NAME` as schoolName from data_dict d WHERE d.ID in ("+schoolId.substring(0, schoolId.length()-1)+")");
			List<Map<String, String>> result = dataDictDao.findMapBySql(query_school.toString());
			for(Map<String, String> map:result){
				school.put(map.get("schoolId"), map.get("schoolName"));
			}
		}
		if(miniclassId.length()>1){
			query_miniclass.append(" SELECT mc.`NAME` as miniclassName,mc.MINI_CLASS_ID as miniclassId,dd.`NAME` as subjectName,ddd.`NAME` as gradeName,u.`NAME` as studyManageName,uu.`NAME` as teacherName,mc.`STATUS` as `status` ");
			query_miniclass.append(" from mini_class mc  ");
			query_miniclass.append("left join data_dict dd on dd.ID = mc.`SUBJECT` ");
			query_miniclass.append("left join data_dict ddd on ddd.ID = mc.GRADE ");
			query_miniclass.append("left join `user` u on u.USER_ID = mc.STUDY_MANEGER_ID ");
			query_miniclass.append("left join `user` uu on uu.USER_ID = mc.TEACHER_ID   ");
			query_miniclass.append(" where mc.MINI_CLASS_ID in ("+miniclassId.substring(0, miniclassId.length()-1)+") ");
			List<Map<String, String>> result = miniClassDao.findMapBySql(query_miniclass.toString());
			for(Map<String, String> map:result){
				miniclass_subject.put(map.get("miniclassId"), map.get("subjectName"));
				miniclass_grade.put(map.get("miniclassId"), map.get("gradeName"));
				miniclass_study.put(map.get("miniclassId"), map.get("studyManageName"));
				miniclass_teacher.put(map.get("miniclassId"), map.get("teacherName"));
				miniclass_status.put(map.get("miniclassId"), map.get("status"));
				miniclass_name.put(map.get("miniclassId"), map.get("miniclassName"));
			}
		}






		List<SmallClassStudentVo> returnList = new ArrayList<SmallClassStudentVo>();
		for (SmallClassStudentJdbc smallClassStudentJdbc : list) {
			SmallClassStudentVo vo = HibernateUtils.voObjectMapping(smallClassStudentJdbc, SmallClassStudentVo.class, "smallClassStudentJdbc");

//			DataDict ddGrade = dataDictDao.findById(smallClassStudentJdbc.getStudentGradeId());
//			vo.setStudentGradeName(ddGrade.getName());
			vo.setStudentGradeName(smallClassStudentJdbc.getStudentGradeId()!=null?studentGrade.get(smallClassStudentJdbc.getStudentGradeId()):"");

			if (StringUtil.isNotBlank(smallClassStudentJdbc.getContractProductCreateTime())) {
				vo.setContractProductCreateDate(smallClassStudentJdbc.getContractProductCreateTime().substring(0, 10));
			}
//			if (StringUtil.isBlank(smallClassStudentJdbc.getSchoolName()) && StringUtil.isNotBlank(smallClassStudentJdbc.getSchoolId())) {
//				StudentSchool ss = studentSchoolDao.findById(smallClassStudentJdbc.getSchoolId());
//				vo.setSchoolName(ss.getName());
//			}
			if (StringUtil.isBlank(smallClassStudentJdbc.getSchoolName()) && StringUtil.isNotBlank(smallClassStudentJdbc.getSchoolId())) {
				vo.setSchoolName(school.get(smallClassStudentJdbc.getSchoolId()));
			}

			if(StringUtil.isNotBlank(smallClassStudentJdbc.getContractProductId())){
				ContractProduct cp = contractProductDao.findById(smallClassStudentJdbc.getContractProductId());
				BigDecimal remainingHour=cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP);
				vo.setMiniRemainingHour(remainingHour);
			}
//			if (StringUtil.isNotBlank(smallClassStudentJdbc.getMiniClassId())) {
//				MiniClassModalVo mc = miniClassDao.findById(smallClassStudentJdbc.getMiniClassId());
//				if (mc != null) {
//					vo.setMiniClassName(mc.getName());
//					if (mc.getSubject() != null) {
//						vo.setSubjectName(mc.getSubject().getName());
//					}
//					if (mc.getGrade() != null) {
//						vo.setGradeName(mc.getGrade().getName());
//					}
//					if (null != mc.getStudyManeger()) {
//						vo.setStudyManagerName(mc.getStudyManeger().getName());
//					}
//					if (mc.getTeacher() != null) {
//						vo.setTeacherName(mc.getTeacher().getName());
//					}
//					vo.setMiniClassStatus(mc.getStatus().getName());
//				}
//			}
			if (StringUtil.isNotBlank(smallClassStudentJdbc.getMiniClassId())) {
				vo.setMiniClassName(miniclass_name.get(smallClassStudentJdbc.getMiniClassId()));
				vo.setSubjectName(miniclass_subject.get(smallClassStudentJdbc.getMiniClassId()));
				vo.setGradeName(miniclass_grade.get(smallClassStudentJdbc.getMiniClassId()));
				vo.setStudyManagerName(miniclass_study.get(smallClassStudentJdbc.getMiniClassId()));
				vo.setTeacherName(miniclass_teacher.get(smallClassStudentJdbc.getMiniClassId()));
				vo.setMiniClassStatus(MiniClassStatus.valueOf(miniclass_status.get(smallClassStudentJdbc.getMiniClassId())).getName());
			}

			returnList.add(vo);
		}
		dp.setDatas(returnList);

		return dp;
	}*/


	/**
	 * 小班学生列表
	 *//*
	@Override
	public DataPackage getMiniClassStudentsSql(StudentVo studentvo,MiniClassVo miniclassvo, DataPackage dp,String brenchId, String miniClassGradeId,
			ModelVo modelvo,String stuNameGradeSchool){
		StringBuffer hql = new StringBuffer();

		hql.append("  select s.id studentId,s.name studentName,o1.name blCampusName,ss.name schoolName,dd.name gradeName,p.name productName,mc.name miniClassName,teacher.name teacherName,studyer.name studyManagerName,s.Small_class_status miniClassStudentStatus from  contract_product cp");
		hql.append(" left join mini_class_student mcs on cp.id=mcs.contract_product_id ");
		hql.append(" left join contract c on cp.contract_id=c.id ");
		hql.append(" left join student s on c.student_id=s.id");
		hql.append(" left join organization o1 on s.bl_campus_id=o1.id");
		hql.append(" left join organization o2 on o1.parentId=o2.id");
		hql.append(" left join product p on cp.product_id=p.id");
		hql.append(" left join mini_class mc on mcs.mini_class_id=mc.mini_class_id");
		hql.append(" left join data_dict dd on dd.id=s.Grade_id");
		hql.append(" left join student_school ss on s.SCHOOL=ss.ID ");
		hql.append(" left join user teacher on teacher.user_id=mc.TEACHER_ID ");
		hql.append(" left join user studyer on studyer.USER_ID=mc.STUDY_MANEGER_ID ");
		hql.append(" where 1=1 ");
		hql.append(" and cp.type='small_class' ");
		hql.append(roleQLConfigService.getValueResult("学生列表","sql"));


		if(StringUtils.isNotBlank(studentvo.getName())){
			hql.append(" and s.name like '%"+studentvo.getName()+"%' ");
		}
		if(StringUtils.isNotBlank(studentvo.getGradeId())){
			hql.append(" and dd.id= '"+studentvo.getGradeId()+"' ");
		}
		if(StringUtils.isBlank(studentvo.getBlCampusId()) && StringUtils.isNotBlank(brenchId) ){
			hql.append(" and o1.id in (select id from Organization where parentId='"+brenchId+"') ");
		}
		if(StringUtils.isNotBlank(studentvo.getBlCampusId())){
			hql.append(" and o1.id = '"+studentvo.getBlCampusId()+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductVersionId())){
			hql.append(" and p.PRODUCT_VERSION_ID = '"+miniclassvo.getProductVersionId()+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getProductQuarterId())){
			hql.append(" and p.PRODUCT_QUARTER_ID = '"+miniclassvo.getProductQuarterId()+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getTeacherId())){
			hql.append(" and mc.TEACHER_ID = '"+miniclassvo.getTeacherId()+"' ");
		}
		if(StringUtils.isNotBlank(miniclassvo.getStudyManegerId())){
			if("null".equals(miniclassvo.getStudyManegerId())){
				hql.append(" and mc.STUDY_MANEGER_ID is null ");
			}else{
				hql.append(" and mc.STUDY_MANEGER_ID = '"+miniclassvo.getStudyManegerId()+"' ");
			}
		}
		if(StringUtils.isNotBlank(studentvo.getSmallClassStatus())){
			hql.append(" and s.SMALL_CLASS_STATUS = '"+studentvo.getSmallClassStatus()+"' ");
		}
		if(studentvo.getStudentStatus()!=null){
			if("1".equals(studentvo.getStudentStatus()))
				hql.append(" and ( s.STU_STATUS ='"+studentvo.getStudentStatus()+"' or  s.STU_STATUS is null) ");
			else if("0".equals(studentvo.getStudentStatus()))
				hql.append(" and  s.STU_STATUS ='"+studentvo.getStudentStatus()+"' ");
		}else{
			hql.append(" and ( s.STU_STATUS ='1' or  s.STU_STATUS is null) ");
		}

		if(StringUtils.isNotBlank(stuNameGradeSchool) && stuNameGradeSchool!=null){
			hql.append(" and ( s.name like '%"+stuNameGradeSchool+"%' "
					+ "  or dd.NAME like '%"+stuNameGradeSchool+"%' "
					+ "  or teacher.NAME like '%"+stuNameGradeSchool+"%' "
					+ "  or studyer.NAME like '%"+stuNameGradeSchool+"%' "
					+ "  or ss.NAME like '%"+stuNameGradeSchool+"%' )");
		}

		hql.append(" order by cp.id desc ");

		dp = studentDao.findMapPageBySQL(hql.toString(), dp, false);


		List<Map> list = (List<Map>) dp.getDatas();
		for (Map map : list) {
			if (map.get("studentId") != null) {
				StudnetAccMvVo stuAccMv=this.getStudentAccoutInfo(map.get("studentId").toString());
				map.put("miniREmainingHour", stuAccMv.getMiniRemainingHour());
			}
		}
		dp.setDatas(list);

		return dp;
	}*/


	/**
	 * 目标班学生列表
	 */
	@Override
	public DataPackage getPromiseStudents(StudentVo studentvo,
			PromiseClassVo promiseClassvo, DataPackage dp, String brenchId,
			ModelVo modelvo,String productQuarterId,String stuNameGradeSchool) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("  from ContractProduct cp, PromiseStudent ps, Contract c, Student s, Organization brench ");
		hql.append(" where 1=1 and cp.id = ps.contractProduct.id and cp.contract.id = c.id and c.student.id = s.id ");
		hql.append(" and s.blCampus.parentId = brench.id ");
		hql.append(" and cp.type='ECS_CLASS' and ps.abortClass is null ");
		//学生列表只查询正式学生
		hql.append(" and s.studentType='ENROLLED' ");
		//hql.append(roleQLConfigService.getValueResult("学生列表","hql"));

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","s.blCampus.orgLevel");
		sqlMap.put("stuId","ps.promiseClass.id");
		sqlMap.put("manergerId","ps.promiseClass.id");
		sqlMap.put("selStuId","(select id from PromiseClass where head_teacher.userId = '"+userService.getCurrentLoginUserId()+"' )");
		sqlMap.put("selManerger","(select id from PromiseClass where head_teacher.userId = '"+userService.getCurrentLoginUserId()+"' ) ");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","hql");
		hql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		if(StringUtils.isNotBlank(modelvo.getStartDate())){
			hql.append(" and cp.createTime >= :startDate ");
			params.put("startDate", modelvo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotBlank(modelvo.getEndDate())){
			hql.append(" and cp.createTime <= :endDate ");
			params.put("endDate", modelvo.getEndDate() + " 23:59:59");
		}
		if(StringUtils.isNotBlank(studentvo.getName())){
			hql.append(" and s.name like :studentName ");
			params.put("studentName", "%" + studentvo.getName() + "%");
		}
		if(StringUtils.isNotBlank(studentvo.getSchoolName())){
			hql.append(" and s.school.name like :schoolName ");
			params.put("schoolName", "%" + studentvo.getSchoolName() + "%");
		}
		if(StringUtils.isNotBlank(studentvo.getGradeId())){
			hql.append(" and s.gradeDict.id= :gradeId ");
			params.put("gradeId", studentvo.getGradeId());
		}
		if(StringUtils.isBlank(studentvo.getBlCampusId()) && StringUtils.isNotBlank(brenchId) ){
			hql.append(" and s.blCampus.id in (select id from Organization where parentId = :brenchId) ");
			params.put("brenchId", brenchId);
		}
		if(StringUtils.isNotBlank(studentvo.getBlCampusId())){
			hql.append(" and s.blCampus.id = :blCampusId ");
			params.put("blCampusId", studentvo.getBlCampusId());
		}
		if(StringUtils.isNotBlank(promiseClassvo.getProductName())){
			hql.append(" and cp.product.name like :productName ");
			params.put("productName", promiseClassvo.getProductName());
		}
		if(StringUtils.isNotBlank(promiseClassvo.getYearId()) ){
			hql.append(" and cp.product.productVersion.id = :yearId ");
			params.put("yearId", promiseClassvo.getYearId());
		}
		if(StringUtils.isNotBlank(promiseClassvo.getpName()) ){
			hql.append(" and  ps.promiseClass.pname like :pName ");
			params.put("pName", "%" + promiseClassvo.getpName() + "%");
		}
		if(StringUtils.isNotBlank(promiseClassvo.getHead_teacherId()) ){
			hql.append(" and ps.promiseClass.head_teacher.userId = :head_teacherId ");
			params.put("head_teacherId",promiseClassvo.getHead_teacherId());
		}
		if(StringUtils.isNotBlank(promiseClassvo.getpStatus()) ){
			hql.append(" and ps.promiseClass.pStatus = :pStatus ");
			params.put("pStatus",promiseClassvo.getpStatus());
		}
		if(StringUtils.isNotBlank(modelvo.getModelName1()) ){
			hql.append(" and ps.resultStatus = :modelName1 ");
			params.put("modelName1",modelvo.getModelName1());
		}
		if(StringUtils.isNotBlank(studentvo.getStatus()) ){
			hql.append(" and s.status = :status ");
			params.put("status",StudentStatus.valueOf(studentvo.getStatus()));
		}
		if(studentvo.getStudentStatus()!=null){
			if("1".equals(studentvo.getStudentStatus())) {
				hql.append(" and (s.studentStatus = :studentStatus or s.studentStatus is null) ");
				params.put("studentStatus",studentvo.getStudentStatus());
			} else if("0".equals(studentvo.getStudentStatus())) {
				hql.append(" and s.studentStatus = :studentStatus ");
				params.put("studentStatus",studentvo.getStudentStatus());
			}
		}else{
			hql.append(" and (s.studentStatus ='1' or s.studentStatus is null) ");
		}

		if(StringUtils.isNotBlank(productQuarterId)){
			hql.append(" and cp.product.productQuarter.id = :productQuarterId ");
			params.put("productQuarterId",productQuarterId);
		}

		if(StringUtils.isNotBlank(stuNameGradeSchool) && stuNameGradeSchool!=null){
			hql.append(" and ( s.name like :studentName2 or s.gradeDict.name like :gradeName2 or s.school.name like :schoolName2 )");
			params.put("studentName2", "%" + stuNameGradeSchool + "%");
			params.put("gradeName2", "%" + stuNameGradeSchool + "%");
			params.put("schoolName2", "%" + stuNameGradeSchool + "%");
		}

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			if(dp.getSidx().equals("PSTATUS")){
				hql.append(" order by ps.promiseClass.pStatus " +dp.getSord());
			}else if(dp.getSidx().equals("resultStatus")){
				hql.append(" order by ps.resultStatus " +dp.getSord());
			}else if(dp.getSidx().equals("contractProductStatus")){
				hql.append(" order by cp.status " +dp.getSord());
			}else if(dp.getSidx().equals("status")){
				hql.append(" order by s.status  "+dp.getSord());
			} else if (dp.getSidx().equals("contractProductCreateDate")) {
				hql.append(" order by cp.createTime  "+dp.getSord());
			} else if (dp.getSidx().equals("studentStatus")) {
				hql.append(" order by s.status  "+dp.getSord());
			} else{
				hql.append(" order by cp."+dp.getSidx()+"  "+dp.getSord());
			}
		}


		dp = studentDao.findPageByHQL(hql.toString(), dp, true, params);

		List<Object[]> list = (List<Object[]>) dp.getDatas();
		List<PromiseClassStudentVo> returnList = new ArrayList<PromiseClassStudentVo>();
		for (Object[] objects : list) {
			ContractProduct cp = (ContractProduct)objects[0];
			PromiseStudent  promiseStudent = (PromiseStudent)objects[1];
//			Contract contract = (Contract) objects[2];
			Student student = (Student) objects[3];
			Organization brench = (Organization)objects[4];
			PromiseClassStudentVo vo = new PromiseClassStudentVo();
			if (cp != null) {
				vo.setContractProductCreateDate(cp.getCreateTime().substring(0, 10));
				vo.setProductName(cp.getProduct().getName());
				vo.setAmount(cp.getPlanAmount());
				vo.setPromotionAmount(cp.getPromotionAmount());
				vo.setPaidAmount(cp.getPaidAmount());
				vo.setConsumeAmount(cp.getConsumeAmount());
				vo.setPayingAmount(cp.getRealAmount().subtract(cp.getPaidAmount()));
				vo.setSurplusAmount(cp.getPaidAmount().add(cp.getPromotionAmount()).subtract(cp.getConsumeAmount()));
				vo.setProductHours(cp.getQuantityInProduct());
				vo.setContractProductStatus(cp.getStatus().getName());
				vo.setConsumeQuantity(cp.getConsumeQuanity());

			}
			if (student != null) {
				vo.setStudentId(student.getId());
				vo.setStudentName(student.getName());
				vo.setSex(student.getSex());
				vo.setStudentcontact(student.getContact());
				vo.setBlCampusName(student.getBlCampus().getName());
				vo.setStudentAttanceNo(student.getAttanceNo());
				vo.setStudentStatus(student.getStatus().getName());
				vo.setStatus(student.getStudentStatus());
				BigDecimal chargeHours = promiseClassRecordDao.countStudentPromiseChargeHoursByStudentId(student.getId());
				vo.setMonthHours(chargeHours);
				if (student.getSchool() != null) {
					vo.setSchoolName(student.getSchool().getName());
				}
				if (student.getGradeDict() != null) {
					vo.setStudentGradeId(student.getGradeDict().getId());
					vo.setStudentGradeName(student.getGradeDict().getName());
				}
			}
			if (brench != null) {
				vo.setBrenchName(brench.getName());
			}
			if (promiseStudent != null) {
				vo.setResultStatus(promiseStudent.getResultStatus());
				PromiseClass promiseClass = promiseStudent.getPromiseClass();
				if (promiseClass != null && StringUtil.isNotBlank(promiseClass.getId())) {
					vo.setPromiseClassName(promiseClass.getpName());
					vo.setPrimiseClassStatus(promiseClass.getpStatus());
					if (promiseClass.getpSchool() != null) {
						vo.setPromiseClassBlCampusName(promiseClass.getpSchool().getName());
					}
					if (promiseClass.getHead_teacher() != null) {
						vo.setHeadTeacherName(promiseClass.getHead_teacher().getName());
					}
				}
			}
			returnList.add(vo);
		}
		dp.setDatas(returnList);
		return dp;

	}

	/**
	 * 目标班学生列表
	 *//*
	@Override
	public DataPackage getPromiseStudents(StudentVo studentvo,
			PromiseClassVo promiseClassvo, DataPackage dp, String brenchId,
			ModelVo modelvo,String productQuarterId,String stuNameGradeSchool) {
		StringBuffer sql = new StringBuffer(" SELECT s.ID STUDENT_ID, s.`NAME` STUDENT_NAME, s.ATTANCE_NO, cp.CREATE_TIME CONTRACT_PRODUCT_CREATE_TIME,  ");
		sql.append(" s.SEX, s.CONTACT, s.SCHOOL SCHOOL_ID, s.GRADE_ID STUDENT_GRADE_ID, s.STU_STATUS STUDENT_STATUS, s.`STATUS`, s.ECS_CLASS_STATUS PRIMISE_CLASS_STATUS, ");
		sql.append(" org_brench.`NAME` BRENCH_NAME, org_campus.`NAME` BL_CAMPUS_NAME, pcs.PROMISE_CLASS_ID, p.`NAME` PRODUCT_NAME, cp.QUNNTITY_IN_PRODUCT, cp.PLAN_AMOUNT,  ");
		sql.append(" cp.PAID_AMOUNT, cp.CONSUME_AMOUNT, cp.CONSUME_QUANTITY CONSUME_QUANITY, cp.REAL_AMOUNT, cp.PROMOTION_AMOUNT, cp.STATUS CONTRACT_PRODUCT_STATUS ");

		StringBuffer sqlFrom = new StringBuffer("  FROM PROMISE_CLASS_STUDENT pcs ");
		sqlFrom.append(" LEFT JOIN contract_product cp ON pcs.CONTRACT_PRODUCT_ID = cp.ID ");
		sqlFrom.append(" LEFT JOIN product p ON cp.PRODUCT_ID = p.ID ");
		sqlFrom.append(" LEFT JOIN student s ON pcs.STUDENT_ID = s.ID ");
		sqlFrom.append(" LEFT JOIN organization org_campus ON s.BL_CAMPUS_ID = org_campus.ID ");
		sqlFrom.append(" LEFT JOIN organization org_brench ON org_campus.parentID = org_brench.ID ");
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		sqlWhere.append(" AND cp.TYPE='ECS_CLASS' and pcs.ABORT_CLASS is null ");
		//学生列表只查询正式学生
		sqlWhere.append(" AND s.STUDENT_TYPE='ENROLLED' ");
		sqlWhere.append(roleQLConfigService.getValueResult("学生列表","sql"));
		if(StringUtils.isNotBlank(modelvo.getStartDate())){
			sqlWhere.append(" and cp.CREATE_TIME >= '"+modelvo.getStartDate()+" 00:00:00 ' ");
		}
		if(StringUtils.isNotBlank(modelvo.getEndDate())){
			sqlWhere.append(" and cp.CREATE_TIME <= '"+modelvo.getEndDate()+"  23:59:59 ' ");
		}
		if(StringUtils.isNotBlank(studentvo.getName())){
			sqlWhere.append(" AND s.`NAME` LIKE '%"+studentvo.getName()+"%' ");
		}
		if(StringUtils.isNotBlank(studentvo.getSchoolName())){
			sql.append(" , ss.`NAME` SCHOOL_NAME ");
			sqlFrom.append(" , student_school ss ");
			sqlWhere.append(" AND  s.SCHOOL = ss.ID AND ss.`NAME` LIKE '%"+studentvo.getSchoolName()+"%' ");
		}
		if(StringUtils.isNotBlank(studentvo.getGradeId())){
			sqlWhere.append(" AND s.GRADE_ID= '"+studentvo.getGradeId()+"' ");
		}
		if(StringUtils.isBlank(studentvo.getBlCampusId()) && StringUtils.isNotBlank(brenchId) ){
			sqlWhere.append(" AND s.BL_CAMPUS_ID IN (SELECT ID FROM organization WHERE parentId='"+brenchId+"') ");
		}
		if(StringUtils.isNotBlank(studentvo.getBlCampusId())){
			sqlWhere.append(" AND s.BL_CAMPUS_ID = '"+studentvo.getBlCampusId()+"' ");
		}
		if(StringUtils.isNotBlank(promiseClassvo.getProductName())){
			sqlWhere.append(" AND p.`NAME` LIKE '%"+promiseClassvo.getProductName()+"%' ");
		}
		if(StringUtils.isNotBlank(promiseClassvo.getYearId()) ){
			sqlWhere.append(" AND p.PRODUCT_VERSION_ID = '"+promiseClassvo.getYearId()+"' ");
		}
		if(StringUtils.isNotBlank(promiseClassvo.getpName()) ){
			if (sqlFrom.indexOf("PROMISE_CLASS pc") < 0) {
				sqlFrom.append(" , PROMISE_CLASS pc ");
				sqlWhere.append(" AND pcs.PROMISE_CLASS_ID = pc.ID ");
			}
			sqlWhere.append(" AND pc.PNAME like '%"+promiseClassvo.getpName()+"%'");
		}
		if(StringUtils.isNotBlank(promiseClassvo.getHead_teacherId()) ){
			if (sqlFrom.indexOf("PROMISE_CLASS pc") < 0) {
				sqlFrom.append(" , PROMISE_CLASS pc ");
				sqlWhere.append(" AND pcs.PROMISE_CLASS_ID = pc.ID ");
			}
			sqlWhere.append(" AND pc.HEAD_TEACHER = '"+promiseClassvo.getHead_teacherId()+"' ");
		}
		if(StringUtils.isNotBlank(promiseClassvo.getpStatus()) ){
			if (sqlFrom.indexOf("PROMISE_CLASS pc") < 0) {
				sqlFrom.append(" , PROMISE_CLASS pc ");
				sqlWhere.append(" AND pcs.PROMISE_CLASS_ID = pc.ID ");
			}
			sqlWhere.append(" AND pc.PSTATUS ='"+promiseClassvo.getpStatus()+"' ");
		}
		if(StringUtils.isNotBlank(modelvo.getModelName1()) ){
			sqlWhere.append(" AND pcs.RESULT_STATUS = '"+modelvo.getModelName1()+"' ");
		}
		if(StringUtils.isNotBlank(studentvo.getStatus()) ){
			sqlWhere.append(" AND s.STATUS = '"+studentvo.getStatus()+"' ");
		}
		if(studentvo.getStudentStatus()!=null){
			if("1".equals(studentvo.getStudentStatus()))
				sqlWhere.append(" AND (s.STU_STATUS ='"+studentvo.getStudentStatus()+"' OR s.STU_STATUS IS NULL) ");
			else if("0".equals(studentvo.getStudentStatus()))
				sqlWhere.append(" AND s.STU_STATUS ='"+studentvo.getStudentStatus()+"' ");
		}else{
			sqlWhere.append(" AND (s.STU_STATUS ='1' OR s.STU_STATUS IS NULL) ");
		}

		if(StringUtils.isNotBlank(productQuarterId)){
			sqlWhere.append(" AND p.PRODUCT_QUARTER_ID = '"+productQuarterId+"' ");
		}

		if(StringUtils.isNotBlank(stuNameGradeSchool) && stuNameGradeSchool!=null){
			if (sqlFrom.indexOf("PROMISE_CLASS pc") < 0) {
				sqlFrom.append(" , PROMISE_CLASS pc ");
				sqlWhere.append(" AND pcs.PROMISE_CLASS_ID = pc.ID ");
			}
			sqlWhere.append(" AND ( s.`NAME` LIKE '%"+stuNameGradeSchool+"%' OR pc.`PNAME` LIKE '%"+stuNameGradeSchool+"%')");
		}

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			if(dp.getSidx().equals("PSTATUS")){
				if (sqlFrom.indexOf("PROMISE_CLASS pc") < 0) {
					sqlFrom.append(" , PROMISE_CLASS pc ");
					sqlWhere.append(" AND pcs.PROMISE_CLASS_ID = pc.ID ");
				}
				sqlWhere.append(" ORDER BY pc.PSTATUS " +dp.getSord());
			}else if(dp.getSidx().equals("contractProductStatus")){
				sqlWhere.append(" ORDER BY cp.STATUS " +dp.getSord());
			}else if(dp.getSidx().equals("status")){
				sqlWhere.append(" ORDER BY s.STATUS  "+dp.getSord());
			} else if (dp.getSidx().equals("contractProductCreateDate")) {
				sqlWhere.append(" ORDER BY cp.CREATE_TIME  "+dp.getSord());
			} else if (dp.getSidx().equals("studentStatus")) {
				sqlWhere.append(" ORDER BY s.STATUS  "+dp.getSord());
			} else if (dp.getSidx().equals("RESULT_STATUS")) {
				sqlWhere.append(" ORDER BY pcs.RESULT_STATUS "+dp.getSord());
			} else{
				sqlWhere.append(" ORDER BY cp."+dp.getSidx()+"  "+dp.getSord());
			}
		}

		sql.append(sqlFrom).append(sqlWhere);

		dp = jdbcTemplateDao.queryPage(sql.toString(), PromiseClassStudentJdbc.class, dp, true);

		List<PromiseClassStudentJdbc> list = (List<PromiseClassStudentJdbc>) dp.getDatas();

		List<PromiseClassStudentVo> returnList = new ArrayList<PromiseClassStudentVo>();
		for (PromiseClassStudentJdbc pcsJdbc : list) {
			PromiseClassStudentVo vo = HibernateUtils.voObjectMapping(pcsJdbc, PromiseClassStudentVo.class, "promiseClassStudentJdbc");
			if (StringUtil.isNotBlank(pcsJdbc.getContractProductCreateTime())) {
				vo.setContractProductCreateDate(pcsJdbc.getContractProductCreateTime().substring(0, 10));
				vo.setPayingAmount(pcsJdbc.getRealAmount().subtract(pcsJdbc.getPaidAmount()));
				vo.setSurplusAmount(pcsJdbc.getPaidAmount().add(pcsJdbc.getPromotionAmount()).subtract(pcsJdbc.getConsumeAmount()));

			}
			BigDecimal chargeHours = promiseClassRecordDao.countStuPromiseChargeHoursByStuIdJdbc(pcsJdbc.getStudentId());
			vo.setMonthHours(chargeHours);
			if (StringUtil.isBlank(pcsJdbc.getSchoolName()) && StringUtil.isNotBlank(pcsJdbc.getSchoolId())) {
				StudentSchool ss = studentSchoolDao.findById(pcsJdbc.getSchoolId());
				vo.setSchoolName(ss.getName());
			}
			if (StringUtil.isNotBlank(pcsJdbc.getStudentGradeId())) {
				DataDict ddGrade = dataDictDao.findById(pcsJdbc.getStudentGradeId());
				vo.setStudentGradeName(ddGrade.getName());
			}
			if (StringUtil.isNotBlank(pcsJdbc.getPromiseClassId())) {
				PromiseClass promiseClass = promiseClassDao.findById(pcsJdbc.getPromiseClassId());
				if (promiseClass != null && StringUtil.isNotBlank(promiseClass.getId())) {
					vo.setPromiseClassName(promiseClass.getpName());
					vo.setPrimiseClassStatus(promiseClass.getpStatus());
					if (promiseClass.getpSchool() != null) {
						vo.setPromiseClassBlCampusName(promiseClass.getpSchool().getName());
					}
					if (promiseClass.getHead_teacher() != null) {
						vo.setHeadTeacherName(promiseClass.getHead_teacher().getName());
					}
				}
			}
			returnList.add(vo);
		}
		dp.setDatas(returnList);
		return dp;

	}*/

	/**
	 * 更换用户组织架构
	 */
	@Override
	public void befroChangeStudyManegerOrg(String orgIds,String userId)
			throws Exception {
		int num=0;
		StringBuffer hql=new StringBuffer();
		String[] idArray = orgIds.split(",");
		if(userId == null)
			return;
		User user=userService.getUserById(userId);
		List<Organization> userOrgList=organizationDao.findOrganizationByUserId(user.getUserId());

		for(Organization org:userOrgList){
			num=0;
			for(String id:idArray){
				if(org.getId().equals(id)){
					num=num+1;
				}
			}
			if(num>0){

			}else{
				if(OrganizationType.DEPARTMENT==org.getOrgType()){
					if(StringUtils.isNotBlank(org.getParentId())){
						int a=0;
						for(String id:idArray){
							Organization dept=organizationDao.findById(id);
							if(StringUtils.isNotBlank(dept.getParentId()) && StringUtils.isNotBlank(org.getParentId()) &&  (org.getParentId().equals(id) || dept.getParentId().equals(org.getParentId()))){
								//选择的新组织架构中有部门上的校区,或是同一校区下的部门
								a++;
								break;
							}
						}
						if(a==0){
							hql.append(" from Student where studyManeger = '"+user.getUserId()+"' and (studentStatus is null or studentStatus != 0) and blCampusId = '"+org.getParentId()+"' ");
						}else{
							continue;
						}
					}

				}else if(OrganizationType.CAMPUS==org.getOrgType()){
					int b=0;
					if(StringUtils.isNotBlank(org.getId())){
						List<Organization> deptlist=organizationDao.findBySql("select * from organization where parentid='"+org.getId()+"' ", new HashMap<String, Object>());
						//判断当前新选择的有没有校区下的部门
						for(String id:idArray){
							for(Organization dept:deptlist){
								if(dept.getId().equals(id)){
									b++;
									break;
								}
							}
							if(b>0){
								break;
							}
						}
						if(b>0){
							continue;
						}else{
							hql.append(" from Student where studyManeger = '"+user.getUserId()+"' and (studentStatus is null or studentStatus != 0) and blCampusId = '"+org.getId()+"' ");
						}
					}

				}
				if(hql.length()>0 && hql!=null){
					List<Student> stuList=studentDao.findAllByHQL(hql.toString(), new HashMap<String, Object>());
					if(stuList!=null && stuList.size()>0){
						Organization blCampus=organizationDao.findById(org.getId());
						throw new ApplicationException("该用户在'"+blCampus.getName()+"'有带学生，需要批量更换学管师清除所带学生才可以修改相关组织架构");
					}
				}
				break;
			}
		}

	}

	/**
	 * 一对多学生列表
	 */
	@Override
	public DataPackage getOtmClassStudents(StudentVo studentvo,
			OtmClassVo otmClassvo, DataPackage dp, String brenchId, String otmClassGradeId,
			ModelVo modelVo, String stuNamegradeSchool, String rcourseHour, String rcourseHourEnd, String stuType) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql=new StringBuffer();
		hql.append(" select DISTINCT s  from Student s, Contract c,ContractProduct cp, Product p ");

		if(StringUtils.isNotBlank(stuNamegradeSchool) && stuNamegradeSchool!=null){
			hql.append(" left join s.gradeDict as gradeDict left join  s.school as school ");
		}

		StringBuffer hqlWhere=new StringBuffer();
		hqlWhere.append(" where s.id=c.student.id and c.id=cp.contract.id and p.id=cp.product.id ");
		hqlWhere.append(" and s.oneOnManyStatus is not null and p.oneOnManyType is not null");
		//学生列表只查询正式学生
		hqlWhere.append(" and s.studentType='ENROLLED' ");
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","s.blCampus.orgLevel");
		sqlMap.put("stuId","s.id");
		sqlMap.put("manergerId","s.id");
		sqlMap.put("selStuId","(select student.id from StudentOrganization where studyManager.userId = '"+userService.getCurrentLoginUserId()+"' )");
		sqlMap.put("selManerger","(select student.id from StudentOrganization where studyManager.userId = '"+userService.getCurrentLoginUserId()+"' ) ");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","hql");
		hqlWhere.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		if(StringUtils.isNotEmpty(modelVo.getStartDate())){
			hqlWhere.append(" and s.createTime >= :startDate ");
			params.put("startDate", modelVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(modelVo.getEndDate())){
			hqlWhere.append(" and s.createTime <= :endDate ");
			params.put("endDate", modelVo.getEndDate());
		}
		if(StringUtils.isNotBlank(studentvo.getName())){
			hqlWhere.append(" and s.name like :studentName ");
			params.put("studentName", "%" + studentvo.getName() + "%");
		}
		if(StringUtils.isNotEmpty(studentvo.getBlCampusId())){
			hqlWhere.append(" and s.blCampusId = :blCampusId ");
			params.put("blCampusId", studentvo.getBlCampusId());
		}
		if(StringUtils.isNotEmpty(studentvo.getOneOnManyStatus())){
			hqlWhere.append(" and s.oneOnManyStatus = :oneOnManyStatus ");
			params.put("oneOnManyStatus", StudentOneOnManyStatus.valueOf(studentvo.getOneOnManyStatus()));
		}
		if(StringUtils.isEmpty(studentvo.getBlCampusId()) && StringUtils.isNotBlank(brenchId)){
			//查询分公司下所有学生
			hqlWhere.append(" and s.blCampusId in (select id from Organization where parentId = :brenchId)");
			params.put("brenchId", brenchId);
		}
		if(StringUtils.isNotEmpty(studentvo.getSchoolName())) {
			hqlWhere.append(" and s.school.name like :schoolName ");
			params.put("schoolName", "%" + studentvo.getSchoolName() + "%");
		}
		if(StringUtils.isNotEmpty(studentvo.getStudyManegerId())){
			if("null".equals(studentvo.getStudyManegerId())){
				hqlWhere.append(" and s.studyManegerId is null ");
			}else{
				hqlWhere.append(" and s.studyManegerId = :studyManegerId ");
				params.put("studyManegerId", studentvo.getStudyManegerId());
			}
		}

		if(StringUtils.isNotBlank(otmClassGradeId)){
			hqlWhere.append(" and s.gradeDict.id = :otmClassGradeId ");
			params.put("otmClassGradeId", otmClassGradeId);
		}

		if(StringUtils.isNotEmpty(rcourseHour)){
			hql.append(",StudnetAccMv sam ");
			hqlWhere.append("and s.id=sam.studentId and sam.otmRemainingAmount >= :rcourseHour ");
			params.put("rcourseHour", rcourseHour);
		}
		if(StringUtils.isNotEmpty(rcourseHourEnd)){
			if((hql.indexOf("sam") < 0))
				hql.append(",StudnetAccMv sam ");
			hqlWhere.append("and s.id=sam.studentId and sam.otmRemainingAmount <= :rcourseHourEnd ");
			params.put("rcourseHourEnd", rcourseHourEnd);
		}

		if(StringUtils.isNotBlank(stuType) && stuType.equals("oneOnOneStudents")){
			hqlWhere.append(" and s.oneOnManyStatus is not null ");
		}

		if(StringUtils.isNotBlank(stuNamegradeSchool) && stuNamegradeSchool!=null){
			hqlWhere.append(" and ( s.name like :studentName2 or gradeDict.name like :gradeName2 or school.name like :schoolName2 )");
			params.put("studentName2", "%" + stuNamegradeSchool + "%");
			params.put("gradeName2", "%" + stuNamegradeSchool + "%");
			params.put("schoolName2", "%" + stuNamegradeSchool + "%");
		}

		if(StringUtils.isNotEmpty(studentvo.getGradeId())){
			hqlWhere.append(" and s.gradeDict.id = :gradeId ");
			params.put("gradeId", studentvo.getGradeId());
		}
		if(StringUtils.isNotBlank(modelVo.getModelName1())){
			hqlWhere.append(" and s.status = :status ");
			params.put("status", StudentStatus.valueOf(modelVo.getModelName1()));
		}

		if(studentvo.getStudentStatus()!=null){
			if("1".equals(studentvo.getStudentStatus())) {
				hqlWhere.append(" and (s.studentStatus = :studentStatus or s.studentStatus is null) ");
				params.put("studentStatus", studentvo.getStudentStatus());
			} else if("0".equals(studentvo.getStudentStatus())) {
				hqlWhere.append(" and cp.contract.student.studentStatus = :studentStatus ");
				params.put("studentStatus", studentvo.getStudentStatus());
			}
		}else{
			hqlWhere.append(" and (s.studentStatus ='1' or s.studentStatus is null) ");
		}

		if (StringUtils.isNotBlank(dp.getSord())&& StringUtils.isNotBlank(dp.getSidx())){
			hqlWhere.append(" order by s."+dp.getSidx()+" "+dp.getSord());
		}



		hql.append(hqlWhere);

		dp=studentDao.findPageByHQL(hql.toString(), dp, true, params);
		List<Student> list=(List<Student>)dp.getDatas();
		List<StudentVo> volist=new ArrayList<StudentVo>();
		if(list!=null){
			volist=HibernateUtils.voListMapping(list, StudentVo.class);
		}
		for(StudentVo vo:volist){
			String brenchName="";
			if(vo.getBlCampusId()!=null){
				Organization org=new Organization();
				org=organizationDao.findById(vo.getBlCampusId());
				String orgid=org.getParentId();
				org=organizationDao.findById(orgid);
				brenchName=org.getName();
			}else{
				brenchName="-";
			}
			vo.setBrenchName(brenchName);
			 Map<String, BigDecimal> map=new HashMap<String, BigDecimal>();
			 if(vo.getId()!=null && StringUtils.isNotBlank(vo.getId())){
				map = this.getOtmRemainingHour(vo.getId());
				if(map.containsKey("otTwoRemainingHour")){
					vo.setOtTwoRemainingHour(map.get("otTwoRemainingHour"));
				}else{
					vo.setOtTwoRemainingHour(new BigDecimal(0));
				}

				if(map.containsKey("otThreeRemainingHour")){
					vo.setOtThreeRemainingHour(map.get("otThreeRemainingHour"));
				}else{
					vo.setOtThreeRemainingHour(new BigDecimal(0));
				}

				if(map.containsKey("otFourRemainingHour")){
					vo.setOtFourRemainingHour(map.get("otFourRemainingHour"));
				}else{
					vo.setOtFourRemainingHour(new BigDecimal(0));
				}

				 if(map.containsKey("otFiveRemainingHour")){
					 vo.setOtFiveRemainingHour(map.get("otFiveRemainingHour"));
				 }else{
					 vo.setOtFiveRemainingHour(new BigDecimal(0));
				 }
				 vo.setOtmRemainingHour(vo.getOtTwoRemainingHour().add(vo.getOtThreeRemainingHour()).add(vo.getOtFourRemainingHour()).add(vo.getOtFiveRemainingHour()));
			 }


		}
		dp.setDatas(volist);
		return dp;
	}

	/**
	 * 一对多学生列表
	 *//*
	@Override
	public DataPackage getOtmClassStudents(StudentVo studentvo,
			OtmClassVo otmClassvo, DataPackage dp, String brenchId, String otmClassGradeId,
			ModelVo modelVo, String stuNamegradeSchool, String rcourseHour, String rcourseHourEnd, String stuType) {
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT DISTINCT s.*, s.ONEONMANY_SATUS ONE_ON_MANY_STATUS, s.SCHOOL SCHOOL_ID  ");
		StringBuffer sqlFrom = new StringBuffer(" from student s ");;
		StringBuffer sqlWhere=new StringBuffer(" WHERE 1=1 ");

		if(StringUtils.isNotBlank(stuNamegradeSchool) && stuNamegradeSchool!=null){
			sql.append(" , ss.`NAME` SCHOOL_NAME, dd_g.`NAME` GRADE_NAME ");
			sqlFrom.append(" LEFT JOIN data_dict dd_g ON s.GRADE_ID = dd_g.ID LEFT JOIN student_school ss ON s.SCHOOL = ss.ID ");
			sqlWhere.append(" AND ( s.`NAME` like '%"+stuNamegradeSchool+"%' or dd_g.`NAME` like '%"+stuNamegradeSchool+"%' or ss.`NAME` like '%"+stuNamegradeSchool+"%' )");
		}
		sqlFrom.append(" , contract c,contract_product cp, product p ");

		sqlWhere.append(" AND s.ID=c.STUDENT_ID AND c.ID=cp.CONTRACT_ID AND p.ID=cp.PRODUCT_ID ");
		sqlWhere.append(" AND s.ONEONMANY_SATUS IS NOT NULL AND p.ONE_ON_MANY_TYPE IS NOT NULL");
		//学生列表只查询正式学生
		sqlWhere.append(" AND s.STUDENT_TYPE='ENROLLED' ");
		sqlWhere.append(roleQLConfigService.getValueResult("学生列表","sql"));
		if(StringUtils.isNotEmpty(modelVo.getStartDate())){
			sqlWhere.append(" AND s.CREATE_TIME >= '"+modelVo.getStartDate()+"'");
		}
		if(StringUtils.isNotEmpty(modelVo.getEndDate())){
			sqlWhere.append(" AND s.CREATE_TIME <= '"+modelVo.getEndDate()+"'");
		}
		if(StringUtils.isNotBlank(studentvo.getName())){
			sqlWhere.append(" AND s.`NAME` like '"+studentvo.getName()+"%' ");
		}
		if(StringUtils.isNotEmpty(studentvo.getBlCampusId())){
			sqlWhere.append(" AND s.BL_CAMPUS_ID = '"+studentvo.getBlCampusId()+"' ");
		}
		if(StringUtils.isNotEmpty(studentvo.getOneOnManyStatus())){
			sqlWhere.append(" AND s.ONEONMANY_SATUS = '"+studentvo.getOneOnManyStatus()+"' ");
		}
		if(StringUtils.isEmpty(studentvo.getBlCampusId()) && StringUtils.isNotBlank(brenchId)){
			//查询分公司下所有学生
			sqlWhere.append(" AND s.BL_CAMPUS_ID in (SELECT ID from organization WHERE parentId='"+brenchId+"')");
		}
		if(StringUtils.isNotEmpty(studentvo.getSchoolName())) {
			if (sqlFrom.indexOf("student_school") < 0) {
				sql.append(" , ss.`NAME` SCHOOL_NAME ");
				sqlFrom.append(" , student_school ss");
			}
			sqlWhere.append(" AND s.SCHOOL = ss.ID AND ss.`NAME` like '%"+studentvo.getSchoolName()+"%'");
		}
		if(StringUtils.isNotEmpty(studentvo.getStudyManegerId())){
			if("null".equals(studentvo.getStudyManegerId())){
				sqlWhere.append(" AND s.STUDY_MANEGER_ID is null ");
			}else{
				sqlWhere.append(" AND s.STUDY_MANEGER_ID = '"+studentvo.getStudyManegerId()+"'");
			}
		}

		if(StringUtils.isNotBlank(otmClassGradeId)){
			sqlWhere.append(" and s.GRADE_ID = '"+otmClassGradeId+"' ");
		}

		if(StringUtils.isNotEmpty(rcourseHour)){
			sql.append(",STUDNET_ACC_MV sam ");
			sqlWhere.append("AND s.ID=sam.STUDENT_ID AND sam.OTM_REMAINING_AMOUNT >= "+rcourseHour+"");
		}
		if(StringUtils.isNotEmpty(rcourseHourEnd)){
			if((sql.indexOf("sam") < 0))
				sql.append(",STUDNET_ACC_MV sam ");
			sqlWhere.append("AND s.id=sam.STUDENT_ID AND sam.OTM_REMAINING_AMOUNT <= "+rcourseHourEnd+"");
		}

		if(StringUtils.isNotBlank(stuType) && stuType.equals("oneOnOneStudents")){
			sqlWhere.append(" AND s.ONEONMANY_SATUS is not null ");
		}

		if(StringUtils.isNotEmpty(studentvo.getGradeId())){
			sqlWhere.append(" and s.GRADE_ID='"+studentvo.getGradeId()+"'");
		}
		if(StringUtils.isNotBlank(modelVo.getModelName1())){
			sqlWhere.append(" and s.STATUS='"+modelVo.getModelName1()+"'");
		}

		if(studentvo.getStudentStatus()!=null){
			if("1".equals(studentvo.getStudentStatus()))
				sqlWhere.append(" AND (s.STU_STATUS ='"+studentvo.getStudentStatus()+"' OR s.STU_STATUS IS NULL) ");
			else if("0".equals(studentvo.getStudentStatus()))
				sqlWhere.append(" AND s.STU_STATUS ='"+studentvo.getStudentStatus()+"' ");
		}else{
			sqlWhere.append(" AND (s.STU_STATUS ='1' OR s.STU_STATUS IS NULL) ");
		}

		if (StringUtils.isNotBlank(dp.getSord())&& StringUtils.isNotBlank(dp.getSidx())){
			sqlWhere.append(" order by s."+dp.getSidx()+" "+dp.getSord());
		}



		sql.append(sqlFrom).append(sqlWhere);
		dp=jdbcTemplateDao.queryPage(sql.toString(), StudentJdbc.class, dp, false);

		int sum = jdbcTemplateDao.findCountSql("select count(distinct s.id ) " + sql.substring(sql.indexOf("from")));

		List<StudentJdbc> list=(List<StudentJdbc>)dp.getDatas();
		List<StudentVo> volist=new ArrayList<StudentVo>();
		if(list!=null){
			volist=HibernateUtils.voListMapping(list, StudentVo.class, "studentJdbc");
		}

		//年级加载
		List<DataDict> gradeList = dataDictDao.getDataDictListByCategory(DataDictCategory.STUDENT_GRADE);
		Map<String,String > gradeMap= new HashMap<String,String>();
		for (DataDict dataDict : gradeList) {
			gradeMap.put(dataDict.getId(), dataDict.getName());
		}

		//校区 分公司 加载
		List<Organization> brench = organizationDao.findAllByHQL(" from Organization where orgType='"+OrganizationType.BRENCH+"'");
		List<Organization> campus = organizationDao.findAllByHQL(" from Organization where orgType='"+OrganizationType.CAMPUS+"'");
		Map<String,String > brenchMap= new HashMap<String,String>();
		Map<String,Organization > campusMap= new HashMap<String,Organization>();

		for (Organization o : campus) {
			campusMap.put(o.getId(), o);
		}

		for (Organization o : brench) {
			brenchMap.put(o.getId(), o.getName());
		}

		//用户信息加载
		List<User> user = userDao.findAll();
		Map<String,String > userMap= new HashMap<String,String>();
		for (User user2 : user) {
			userMap.put(user2.getUserId(), user2.getName());
		}

		for(StudentVo vo:volist){

			//学校数据太大，单个查询速度快
			if (StringUtil.isBlank(vo.getSchoolName()) && StringUtil.isNotBlank(vo.getSchoolId())) {
				StudentSchool ss = studentSchoolDao.findById(vo.getSchoolId());
				vo.setSchoolName(ss.getName());
			}

			if (StringUtil.isBlank(vo.getGradeName()) && StringUtil.isNotBlank(vo.getGradeId())) {
				vo.setGradeName(gradeMap.get(vo.getGradeId()));
			}
			String brenchName="";
			if(vo.getBlCampusId()!=null){
				Organization org=campusMap.get(vo.getBlCampusId());
				vo.setBlCampusName(org.getName());
				String orgid=org.getParentId();
				brenchName=brenchMap.get(orgid);
			}else{
				brenchName="-";
			}
			vo.setBrenchName(brenchName);
			 Map<String, BigDecimal> map=new HashMap<String, BigDecimal>();
			 if(vo.getId()!=null && StringUtils.isNotBlank(vo.getId())){
				map = this.getOtmRemainingHour(vo.getId());
				if(map.containsKey("otTwoRemainingHour")){
					vo.setOtTwoRemainingHour(map.get("otTwoRemainingHour"));
				}else{
					vo.setOtTwoRemainingHour(new BigDecimal(0));
				}

				if(map.containsKey("otThreeRemainingHour")){
					vo.setOtThreeRemainingHour(map.get("otThreeRemainingHour"));
				}else{
					vo.setOtThreeRemainingHour(new BigDecimal(0));
				}

				if(map.containsKey("otFourRemainingHour")){
					vo.setOtFourRemainingHour(map.get("otFourRemainingHour"));
				}else{
					vo.setOtFourRemainingHour(new BigDecimal(0));
				}

				 if(map.containsKey("otFiveRemainingHour")){
					 vo.setOtFiveRemainingHour(map.get("otFiveRemainingHour"));
				 }else{
					 vo.setOtFiveRemainingHour(new BigDecimal(0));
				 }
				 vo.setOtmRemainingHour(vo.getOtTwoRemainingHour().add(vo.getOtThreeRemainingHour()).add(vo.getOtFourRemainingHour()).add(vo.getOtFiveRemainingHour()));
			 }
			 if(StringUtils.isNotEmpty(vo.getStudyManegerId()))
				 vo.setStudyManegerName(userMap.get(vo.getStudyManegerId()));

		}

		dp.setRowCount(sum);
		dp.setDatas(volist);
		return dp;
	}*/

	/**
	 * 学生一对二，一对三，一对四,一对五 剩余课时
	 */
	public Map<String, BigDecimal> getOtmRemainingHour(String id){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append("select cp.* from contract_product cp INNER JOIN contract c on c.id=cp.contract_id ");
		sql.append(" where c.STUDENT_ID = :studentId and cp.TYPE='ONE_ON_MANY'");
		params.put("studentId", id);
		List<ContractProduct> list=contractProductDao.findBySql(sql.toString(), params);
		//一对二，剩余资金，剩余课时，优惠金额，优惠课时
		BigDecimal otTwoRemainingAmount = BigDecimal.ZERO;
		BigDecimal otTwoRemainingHour=BigDecimal.ZERO;
		BigDecimal otTwoRemainingPromotion = BigDecimal.ZERO;
	    BigDecimal otTwoRemainingPromotionHour = BigDecimal.ZERO;

		//一对三，剩余资金，剩余课时，优惠金额，优惠课时
		BigDecimal otThreeRemainingAmount = BigDecimal.ZERO;
		BigDecimal otThreeRemainingHour=BigDecimal.ZERO;
		BigDecimal otThreeRemainingPromotion = BigDecimal.ZERO;
	    BigDecimal otThreeRemainingPromotionHour = BigDecimal.ZERO;

		//一对四，剩余资金，剩余课时，优惠金额，优惠课时
		BigDecimal otFourRemainingAmount = BigDecimal.ZERO;
	    BigDecimal otFourRemainingHour=BigDecimal.ZERO;
	    BigDecimal otFourRemainingPromotion = BigDecimal.ZERO;
	    BigDecimal otFourRemainingPromotionHour = BigDecimal.ZERO;


		//一对五，剩余资金，剩余课时，优惠金额，优惠课时
		BigDecimal otFiveRemainingAmount = BigDecimal.ZERO;
		BigDecimal otFiveRemainingHour=BigDecimal.ZERO;
		BigDecimal otFiveRemainingPromotion = BigDecimal.ZERO;
		BigDecimal otFiveRemainingPromotionHour = BigDecimal.ZERO;

	    Map<String, BigDecimal> map=new HashMap<String, BigDecimal>();

		if(list!=null && list.size()>0){
			for(ContractProduct cp:list){
				Product p=cp.getProduct();
				if(p.getOneOnManyType()!=null){
					switch(p.getOneOnManyType()) {
					case 2:
						otTwoRemainingAmount = otTwoRemainingAmount.add(cp.getRemainingAmount());
						otTwoRemainingPromotion=otTwoRemainingPromotion.add(cp.getRemainingAmountOfPromotionAmount());
						if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
							otTwoRemainingHour = otTwoRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
							otTwoRemainingPromotionHour=otTwoRemainingPromotionHour.add(cp.getRemainingAmountOfPromotionAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
						}
						map.put("otTwoRemainingHour", otTwoRemainingHour);
						break;
					case 3:
						otThreeRemainingAmount = otThreeRemainingAmount.add(cp.getRemainingAmount());
						otThreeRemainingPromotion=otThreeRemainingPromotion.add(cp.getRemainingAmountOfPromotionAmount());
						if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
							otThreeRemainingHour = otThreeRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
							otThreeRemainingPromotionHour=otThreeRemainingPromotionHour.add(cp.getRemainingAmountOfPromotionAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
						}
						map.put("otThreeRemainingHour", otThreeRemainingHour);
						break;
					case 4:
						otFourRemainingAmount = otFourRemainingAmount.add(cp.getRemainingAmount());
						otFourRemainingPromotion=otFourRemainingPromotion.add(cp.getRemainingAmountOfPromotionAmount());
						if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
							otFourRemainingHour = otFourRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
							otFourRemainingPromotionHour=otFourRemainingPromotionHour.add(cp.getRemainingAmountOfPromotionAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP));
						}
						map.put("otFourRemainingHour", otFourRemainingHour);
						break;
					case 5:
						otFiveRemainingAmount = otFiveRemainingAmount.add(cp.getRemainingAmount());
						otFiveRemainingPromotion = otFiveRemainingPromotion.add(cp.getRemainingAmountOfPromotionAmount());
						if(BigDecimal.ZERO.compareTo(cp.getPrice())!=0){
							otFiveRemainingHour = otFiveRemainingHour.add(cp.getRemainingAmount().divide(cp.getPrice(),2,RoundingMode.HALF_UP));
							otFiveRemainingPromotionHour = otFiveRemainingPromotionHour.add(cp.getRemainingAmountOfPromotionAmount().divide(cp.getPrice(),2,RoundingMode.HALF_UP));
						}
						map.put("otFiveRemainingHour",otFiveRemainingHour);
						break;
					}
				}
			}

		}
		return map;
	}

	/**
	 * 保存潜在学生到student
	 */
	@Override
	public String savePotentialStudent(StudentImportVo vo, String customerId){
		String stuId = "";
		Customer customer = null; //new Customer(customerId)
		if (StringUtils.isNotBlank(customerId)){
			customer = customerDao.findById(customerId);
		}
		Student stu=new Student();
		User user=userService.getCurrentLoginUser();
			if(StringUtils.isNotBlank(vo.getId())){
				stu=studentDao.findById(vo.getId());
				stu.setModifyTime(DateTools.getCurrentDateTime());
				stu.setModifyUserId(user.getUserId());
			}else{
				stu.setStudentType(StudentType.POTENTIAL);
				stu.setCreateTime(DateTools.getCurrentDateTime());
				stu.setCreateUserId(user.getUserId());
			}
			if(StringUtils.isNotBlank(vo.getName())){
				stu.setName(vo.getName());
			}
			if(StringUtils.isNotBlank(vo.getSex())){
				stu.setSex(vo.getSex());
			}
			if(StringUtils.isNotBlank(vo.getContact())){
				stu.setContact(vo.getContact());
			} else {
			    stu.setContact(customer.getContact()); // 同步客户的电话号码到学生
			}
			if(StringUtils.isNotBlank(vo.getSchoolId())){
				StudentSchool school=studentSchoolDao.findById(vo.getSchoolId());
				stu.setSchool(school);
			}
			if(StringUtils.isNotBlank(vo.getBothday())){
				stu.setBothday(vo.getBothday());
			}
			if(StringUtils.isNotBlank(vo.getFatherName())){
				stu.setFatherName(vo.getFatherName());
			}
			if(StringUtils.isNotBlank(vo.getFatherPhone())){
				stu.setFatherPhone(vo.getFatherPhone());
			}
			if(StringUtils.isNotBlank(vo.getMotherName())){
				stu.setMotherName(vo.getMotherName());
			}
			if(StringUtils.isNotBlank(vo.getNotherPhone())){
				stu.setNotherPhone(vo.getNotherPhone());
			}
			if(StringUtils.isNotBlank(vo.getGradeId())){
				stu.setGradeDict(dataDictDao.findById(vo.getGradeId()));
			}
			if(StringUtils.isNotBlank(vo.getClasses())){
				stu.setClasses(vo.getClasses());
			}
			if (StringUtils.isNotBlank(vo.getAddress())){
				String[] strings = null;
				try {
					strings = CustomerMapAnalyzeServiceImpl.doGet(vo.getAddress());
				} catch (Exception e) {
					e.printStackTrace();
				}
				stu.setAddress(vo.getAddress());
				if (strings!=null){
					stu.setLog(strings[0]);
					stu.setLat(strings[1]);
				}
			}

			if (customer!=null){
				Organization blCampus = customer.getBlCampusId();
				stu.setBlCampusId(blCampus.getId());
			}

		stu.setSchoolOrTemp("school");

			if(stu.getName() != null){
				studentDao.save(stu);
				studentDao.commit();
				
				System.out.println("学生id："+stu.getId());
				stuId = stu.getId();
				
			}

			//保存客户学生关系
			if(StringUtils.isBlank(vo.getId())){
				CustomerStudent cusStu=new CustomerStudent();
				CustomerStudentId id=new CustomerStudentId();
				if(StringUtils.isNotBlank(customerId)){
					id.setCustomerId(customerId);
				}
				if(StringUtils.isNotBlank(stu.getId())){
					id.setStudentId(stu.getId());
				}

				cusStu.setCustomer(customer);
				cusStu.setStudent(stu);
				cusStu.setIsDeleted(false);
				this.addCustomerStudent(cusStu);
			}
			
			
		return stuId;
		
		
		
		
	}
	
	@Override
	public Map<String, Object> getStudentInfo(DataPackage dataPackage, String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String,Object>();
		Integer start = (dataPackage.getPageNo()-1)*dataPackage.getPageSize();	
		List<StudentInfoVo> studentInfoVos = new ArrayList<StudentInfoVo>();
		StringBuffer query = new StringBuffer();
		query.append(" select * from student s ");
		if(StringUtils.isNotBlank(studentId)){
			query.append(" where s.ID ='"+studentId+"' ");
			params.put("studentId", studentId);
		}
		String count ="select count(*) from ( " + query.toString() + " ) countall ";
		query.append(" limit "+start+","+dataPackage.getPageSize());
		List<Student> students = studentDao.findBySql(query.toString(), params);
		for(Student student:students){
			System.out.println(student.getId());
			//StudentVo studentVo = HibernateUtils.voObjectMapping(student, StudentVo.class);
			StudentInfoVo studentInfoVo = new StudentInfoVo();
			studentInfoVo.setId(student.getId());
			studentInfoVo.setName(student.getName());
			studentInfoVo.setMobilephone(student.getContact());
			studentInfoVo.setGradeId(student.getGradeDict().getValue());
			studentInfoVo.setGradeName(student.getGradeDict().getName());
			studentInfoVo.setOrganizationId(student.getBlCampusId());
			studentInfoVo.setOrganizationName(student.getBlCampus().getName());
			studentInfoVo.setCreateDate(student.getCreateTime());
			studentInfoVo.setUpdateDate(student.getModifyTime());
			studentInfoVo.setSex(student.getSex()!=null?student.getSex():"");
			studentInfoVos.add(studentInfoVo);
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		Integer totalCount = studentDao.findCountSql(count, params);
		Integer pageSize = dataPackage.getPageSize();
	    Integer totalPage = totalCount / pageSize;  
        if (totalCount % pageSize > 0) {  
        	totalPage++;  
        } 
		resultMap.put("totalPage",totalPage );
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", studentInfoVos);
		map.put("resultStatus", 200);
		map.put("resultMessage", " 学生列表");
		map.put("result", resultMap);				
		
		return map;
	}
	
	@Override
	public Map<String, Object> getClassStudentInfo(String classId,String type) {
		//查询一对一和一对多 小班的学生信息
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			ProductType productType =ProductType.valueOf(type);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("classId", classId);
			List<ClassStudentInfoVo> classStudentInfoVos = new ArrayList<ClassStudentInfoVo>();
			StringBuffer query = new StringBuffer();			
			
			if(productType == ProductType.ONE_ON_ONE_COURSE){
				//查询小班的学生信息
				Course course =courseDao.findById(classId);
				if(course!=null){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					classStudentInfoVo.setId(course.getStudent().getId());
					classStudentInfoVo.setName(course.getStudent().getName());
					classStudentInfoVos.add(classStudentInfoVo);
				}
			}else if(productType ==ProductType.ONE_ON_MANY){
				//查询一对多
				query.append("select * from otm_class_student where OTM_CLASS_ID = :classId ");
				List<OtmClassStudent> otmClassStudents =otmClassStudentDao.findBySql(query.toString(), params);
				for(OtmClassStudent otmClassStudent:otmClassStudents){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					classStudentInfoVo.setId(otmClassStudent.getStudent().getId());
					classStudentInfoVo.setName(otmClassStudent.getStudent().getName());
					classStudentInfoVos.add(classStudentInfoVo);
				}
			}else if(productType == ProductType.SMALL_CLASS){
				//查询小班
				query.append("select * from mini_class_student where MINI_CLASS_ID = :classId ");
			    List<MiniClassStudent> miniClassStudents = miniClassStudentDao.findBySql(query.toString(), params);
			    for(MiniClassStudent miniClassStudent:miniClassStudents){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					classStudentInfoVo.setId(miniClassStudent.getStudent().getId());
					classStudentInfoVo.setName(miniClassStudent.getStudent().getName());
					classStudentInfoVos.add(classStudentInfoVo);	    	
			    }
			}else if(productType == ProductType.TWO_TEACHER){
			    //查询双师  副班的id
			    query.append("select * from two_teacher_class_student ttcs where ttcs.CLASS_TWO_ID = :classId ");
			    List<TwoTeacherClassStudent> twoTeacherClassStudents = twoTeacherClassStudentDao.findBySql(query.toString(),params);
			    for(TwoTeacherClassStudent twoTeacherClassStudent:twoTeacherClassStudents){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					classStudentInfoVo.setId(twoTeacherClassStudent.getStudent().getId());
					classStudentInfoVo.setName(twoTeacherClassStudent.getStudent().getName());
					classStudentInfoVos.add(classStudentInfoVo);	    	
			    }
			}		    
			map.put("resultStatus", 200);
			map.put("resultMessage", "班级内学生信息");
			map.put("result", classStudentInfoVos);		
			return map;
			
		} catch (IllegalArgumentException e) {
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
			return map;
		}
		
		
		
	}

	/**
	 * 更新学生状态
	 * @param msg
	 */
	@Override
	public void setStudentRealStatus(StudentStatusMsg msg) {
		if(StringUtil.isNotBlank(msg.getStudentId())){
			Student student=studentDao.findById(msg.getStudentId());
			if(student!=null){
				StudnetAccMvVo acc=  getStudentAccoutInfo(student.getId());
				updateStudentStatusRightNow(student,msg.getType(),acc);
			}
		}
	}


	/**
	 * 更新学生状态
	 * @param student
	 * @param type
	 */
	public void updateStudentStatusRightNow(Student student,ProductType type,StudnetAccMvVo acc){
		if(acc.getTotalAmount().compareTo(BigDecimal.ZERO)==0 && !student.getStatus().equals(StudentStatus.GRADUATION) && !student.getStatus().equals(StudentStatus.NEW)){//总剩余等于0且不是毕业和新签状态，就是结课
			student.setStatus(StudentStatus.FINISH_CLASS);
			student.setFinishTime(DateTools.getCurrentDateTime());
		}else if(acc.getTotalAmount().compareTo(BigDecimal.ZERO)>0){
			if(acc.getRemainingAmount().compareTo(BigDecimal.ZERO)>0 &&acc.getConsumeAmount().compareTo(BigDecimal.ZERO)>0) {
				student.setStatus(StudentStatus.CLASSING);
			}else if(student.getStatus()!=null && !student.getStatus().equals(StudentStatus.GRADUATION) && !student.getStatus().equals(StudentStatus.NEW) && acc.getRemainingAmount().compareTo(BigDecimal.ZERO)==0){
				student.setStatus(StudentStatus.FINISH_CLASS);
				student.setFinishTime(DateTools.getCurrentDateTime());
			}
			if(acc.getOneOnOneRemainingAmount().compareTo(BigDecimal.ZERO)>0 && acc.getOneOnOneConsumeAmount().compareTo(BigDecimal.ZERO)>0){
				student.setOneOnOneStatus(StudentOneOnOneStatus.CLASSING);
			}else if(student.getOneOnOneStatus()!=null && !student.getOneOnOneStatus().equals(StudentOneOnOneStatus.GRADUATION) && !student.getOneOnOneStatus().equals(StudentOneOnOneStatus.NEW) && acc.getOneOnOneRemainingAmount().compareTo(BigDecimal.ZERO)==0){
				student.setOneOnOneStatus(StudentOneOnOneStatus.FINISH_CLASS);
			}

			if(acc.getOtmRemainingAmount().compareTo(BigDecimal.ZERO)>0 && acc.getOtmConsumeAmount().compareTo(BigDecimal.ZERO)>0){
				student.setOneOnManyStatus(StudentOneOnManyStatus.CLASSING);
			}else if (student.getOneOnManyStatus()!=null && !student.getOneOnManyStatus().equals(StudentOneOnManyStatus.GRADUATION) && !student.getOneOnManyStatus().equals(StudentOneOnManyStatus.NEW) && acc.getOtmRemainingAmount().compareTo(BigDecimal.ZERO)==0){
				student.setOneOnManyStatus(StudentOneOnManyStatus.FINISH_CLASS);
			}

			if(acc.getMiniRemainingAmount().compareTo(BigDecimal.ZERO)>0 && acc.getMiniConsumeAmount().compareTo(BigDecimal.ZERO)>0){
				student.setSmallClassStatus(StudentSmallClassStatus.CLASSING);
			}else if (student.getSmallClassStatus()!=null && !student.getSmallClassStatus().equals(StudentSmallClassStatus.GRADUATION) && !student.getSmallClassStatus().equals(StudentSmallClassStatus.NEW) && acc.getMiniRemainingAmount().compareTo(BigDecimal.ZERO)==0){
				student.setSmallClassStatus(StudentSmallClassStatus.FINISH_CLASS);
			}
		}
	}


	/*******************************************************************20170613**********************************************/
	@Override
	public List<MobileStudentVo> getMobileStudentList(MobileStudentListTo to) {
		//如果有字段有值，用like进行条件查询
		Map<String, Object> params = new HashMap<String, Object>();
		long begin = System.currentTimeMillis();
		StringBuilder query = new StringBuilder(512);
		query=getFirstSqlForStudentList(query);

		if(StringUtils.isNotBlank(to.getStuNameGrade()) && to.getStuNameGrade()!=null){
			query.append(" AND ( s.`NAME` like :stuNameGrade or d.`NAME` like :stuNameGrade or ss.`NAME` like :stuNameGrade )" );
			params.put("stuNameGrade", "%" + to.getStuNameGrade() + "%");
		}

		if(StringUtils.isNotEmpty(to.getStudent().getBlCampusId())){
			query.append(" AND s.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", to.getStudent().getBlCampusId());
		}
		if(StringUtils.isEmpty(to.getStudent().getBlCampusId()) && StringUtils.isNotBlank(to.getBrenchId())){
			//查询分公司下所有学生
			query.append(" AND s.BL_CAMPUS_ID IN (SELECT ID FROM organization WHERE parentID = :brenchId )");
			params.put("brenchId", to.getBrenchId());
		}

		if(StringUtils.isNotEmpty(to.getStudent().getStudyManegerId())){
			if("null".equals(to.getStudent().getStudyManegerId())){
				query.append(" AND s.STUDY_MANEGER_ID is null ");
			}else{
				query.append(" AND s.STUDY_MANEGER_ID = :studyManegerId ");
				params.put("studyManegerId", to.getStudent().getStudyManegerId());
			}
		}

		if(StringUtils.isNotEmpty(to.getRcourseHour())){
			query.append("  and acc.ONE_ON_ONE_REMAINING_HOUR >= :rcourseHour ");
			params.put("rcourseHour", to.getRcourseHour());
		}
		if(StringUtils.isNotEmpty(to.getRcourseHourEnd())){
			query.append(" and acc.ONE_ON_ONE_REMAINING_HOUR <= :rcourseHourEnd");
			params.put("rcourseHourEnd", to.getRcourseHourEnd());
		}


		if(to.getStudent().getOneOnOneStatus()!=null){//一对一学生状态
			query.append(" AND s.ONEONONE_STATUS = :oneOnOneStatus ");
			params.put("oneOnOneStatus", to.getStudent().getOneOnOneStatus());
		}

		query.append(" AND (s.STU_STATUS ='1' OR s.STU_STATUS is null)");

            if(StringUtils.isNotBlank(to.getStuType()) && to.getStuType().equals("oneOnOneStudents")){
			query.append(" AND s.ONEONONE_STATUS is not null ");
		}else if(StringUtils.isNotBlank(to.getStuType()) && to.getStuType().equals("oneOnManyStudents")){
            query.append(" and s.oneOnManyStatus is not null and p.oneOnManyType is not null");
        }

		if(to.getStudent().getGradeDict()!=null && StringUtils.isNotEmpty(to.getStudent().getGradeDict().getId())){
			query.append(" AND s.GRADE_ID = :gradeId ");
			params.put("gradeId", to.getStudent().getGradeDict().getId());
		}

		//学生列表只查询正式学生
		query.append(" and s.STUDENT_TYPE='ENROLLED' ");

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("stuId","s.STUDY_MANEGER_ID");
		sqlMap.put("manergerId","s.STUDY_MANEGER_ID");
		sqlMap.put("selStuId","('"+userService.getCurrentLoginUserId()+"' )");
		sqlMap.put("selManerger","('"+userService.getCurrentLoginUserId()+"' )");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","sql");
		query.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		query.append(" order by s.create_time desc ");


		List<Map<Object, Object>> result=studentDao.findMapOfPageBySql(query.toString(), new DataPackage(to.getPageNo(),to.getPageSize()), params);
		List<MobileStudentVo> list=HibernateUtils.voListMapping(result, MobileStudentVo.class);
		for(MobileStudentVo vo:list){
			if(StringUtils.isNotBlank(vo.getOneOnOneStatus())){
				vo.setOneOnOneStatusName(StudentOneOnOneStatus.valueOf(vo.getOneOnOneStatus()).getName());
			}
		}

		return list;
	}


	public StringBuilder getFirstSqlForStudentList(StringBuilder sql){
		sql.append(" SELECT s.id,s.`name`,s.sex,s.contact,s.CREATE_TIME createTime,s.schoolOrTemp,ss.`NAME` schoolName,s.`status`,s.STU_STATUS stuStatus," +
				"s.ONEONONE_STATUS oneOnOneStatus,s.ATTANCE_NO attanceNo, ");
		sql.append(" d.`NAME` gradeName,o.`name` blCampusName,oo.`name` brenchName,u.`NAME` studyManegerName, ");
		sql.append(" acc.remaining_amount remainingAmount,acc.ONE_ON_ONE_REMAINING_HOUR oneOnOneRemainingHour,acc.OTM_REMAINING_HOUR otmRemainingHour ");

		sql.append(" from student s LEFT JOIN student_school ss on s.SCHOOL =ss.ID LEFT JOIN data_dict d on d.ID = s.GRADE_ID ");
		sql.append(" left join organization o on o.id = s.BL_CAMPUS_ID left join organization oo on oo.id = o.parentID ");
		sql.append(" left join `user` u on u.USER_ID = s.STUDY_MANEGER_ID ");
		sql.append(" left join studnet_acc_mv acc on acc.student_id = s.id ");

		sql.append(" where 1=1 ");

		return sql;
	}

	/**
	 * 一对多学生列表
	 */
	@Override
	public List<MobileStudentVo> getMobileOtmClassStudents(MobileStudentListTo to) {
		//如果有字段有值，用like进行条件查询
		Map<String, Object> params = new HashMap<String, Object>();
		long begin = System.currentTimeMillis();
		StringBuilder query = new StringBuilder(512);
		query=getFirstSqlForStudentList(query);

		if(StringUtils.isNotBlank(to.getStuNameGrade()) && to.getStuNameGrade()!=null){
			query.append(" AND ( s.`NAME` like :stuNameGrade or d.`NAME` like :stuNameGrade or ss.`NAME` like :stuNameGrade )" );
			params.put("stuNameGrade", "%" + to.getStuNameGrade() + "%");
		}

		if(StringUtils.isNotEmpty(to.getStudentVo().getBlCampusId())){
			query.append(" AND s.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", to.getStudentVo().getBlCampusId());
		}
		if(StringUtils.isEmpty(to.getStudentVo().getBlCampusId()) && StringUtils.isNotBlank(to.getBrenchId())){
			//查询分公司下所有学生
			query.append(" AND s.BL_CAMPUS_ID IN (SELECT ID FROM organization WHERE parentID = :brenchId )");
			params.put("brenchId", to.getBrenchId());
		}

		if(StringUtils.isNotEmpty(to.getStudentVo().getStudyManegerId())){
			if("null".equals(to.getStudentVo().getStudyManegerId())){
				query.append(" AND s.STUDY_MANEGER_ID is null ");
			}else{
				query.append(" AND s.STUDY_MANEGER_ID = :studyManegerId ");
				params.put("studyManegerId", to.getStudentVo().getStudyManegerId());
			}
		}

		if(StringUtils.isNotEmpty(to.getRcourseHour())){
			query.append("  and acc.OTM_REMAINING_HOUR >= :rcourseHour ");
			params.put("rcourseHour", to.getRcourseHour());
		}
		if(StringUtils.isNotEmpty(to.getRcourseHourEnd())){
			query.append(" and acc.OTM_REMAINING_HOUR <= :rcourseHourEnd");
			params.put("rcourseHourEnd", to.getRcourseHourEnd());
		}


		if(StringUtils.isNotBlank(to.getStudentVo().getOneOnManyStatus())){//一对一学生状态
			query.append(" AND s.ONEONMANY_SATUS = :oneOnOneStatus ");
			params.put("oneOnOneStatus", to.getStudentVo().getOneOnManyStatus());
		}

		if(StringUtils.isNotBlank(to.getStuType()) && to.getStuType().equals("oneOnManyStudents")){
			query.append(" and s.ONEONMANY_SATUS is not null ");
		}

		query.append(" AND (s.STU_STATUS ='1' OR s.STU_STATUS is null)");


		if(StringUtils.isNotEmpty(to.getStudentVo().getGradeId())){
			query.append(" AND s.GRADE_ID = :gradeId ");
			params.put("gradeId", to.getStudentVo().getGradeId());
		}

		//学生列表只查询正式学生
		query.append(" and s.STUDENT_TYPE='ENROLLED' ");
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("stuId","s.STUDY_MANEGER_ID");
		sqlMap.put("manergerId","s.STUDY_MANEGER_ID");
		sqlMap.put("selStuId","('"+userService.getCurrentLoginUserId()+"' )");
		sqlMap.put("selManerger","('"+userService.getCurrentLoginUserId()+"' )");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","sql");
		query.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		query.append(" order by s.create_time desc ");


		List<Map<Object, Object>> result=studentDao.findMapOfPageBySql(query.toString(), new DataPackage(to.getPageNo(),to.getPageSize()), params);
		List<MobileStudentVo> list=HibernateUtils.voListMapping(result, MobileStudentVo.class);
		for(MobileStudentVo vo:list){
			if(StringUtils.isNotBlank(vo.getOneOnOneStatus())){
				vo.setOneOnOneStatusName(StudentOneOnOneStatus.valueOf(vo.getOneOnOneStatus()).getName());
			}
		}

		return list;
	}



	/**
	 * 小班学生列表
	 */
	@Override
	public List getMobileMiniClassStudents(MobileStudentListTo to) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();

		hql.append(" select distinct s.id studentId,s.name studentName,ss.name schoolName,dd.name studentGradeName,p.name productName,gra.name gradeName");
		hql.append(" ,mc.name miniClassName,u.name teacherName,uu.name studyManagerName,s.SMALL_CLASS_STATUS miniClassStudentStatus from student s ");
		hql.append(" left join data_dict dd on s.Grade_id=dd.id");
		hql.append(" left join organization o on o.id = s.bl_campus_id");
		hql.append(" left join contract c on c.student_id = s.id");
		hql.append(" left join contract_product cp on cp.contract_id = c.id");
		hql.append(" left join product p on p.id = cp.product_id");
		hql.append(" left join mini_class_student mcs on mcs.contract_product_id = cp.id");
		hql.append(" left join mini_class mc on mc.MINI_CLASS_ID=mcs.MINI_CLASS_ID");
		hql.append(" left join student_school ss on ss.id = s.SCHOOL");
		hql.append(" left join data_dict gra on gra.id =mc.Grade");
		hql.append(" left join user u on u.user_id = mc.TEACHER_ID");
		hql.append(" left join user uu on uu.user_id =mc.STUDY_MANEGER_ID");
		hql.append(" where cp.type='SMALL_CLASS' and s.student_Type='ENROLLED' ");
		hql.append(" and (s.STU_STATUS ='1' or s.STU_STATUS is null)");

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("stuId","mc.TEACHER_ID");
		sqlMap.put("manergerId","mc.STUDY_MANEGER_ID");
		sqlMap.put("selStuId","('"+userService.getCurrentLoginUserId()+"' )");
		sqlMap.put("selManerger","('"+userService.getCurrentLoginUserId()+"' )");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","sql");
		hql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		if(StringUtils.isNotBlank(to.getGradeId())){
			hql.append(" and s.GRADE_ID= :gradeId ");
			params.put("gradeId", to.getGradeId());
		}
		if(StringUtils.isBlank(to.getBlCampusId()) && StringUtils.isNotBlank(to.getBrenchId()) ){
			hql.append(" and s.BL_CAMPUS_ID in (select id from Organization where parentId = :brenchId) ");
			params.put("brenchId", to.getBrenchId());
		}
		if(StringUtils.isNotBlank(to.getBlCampusId())){
			hql.append(" and s.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", to.getBlCampusId());
		}
		if(StringUtils.isNotBlank(to.getProductVersionId())){
			hql.append(" and p.PRODUCT_VERSION_ID = :productVersionId ");
			params.put("productVersionId", to.getProductVersionId());
		}
		if(StringUtils.isNotBlank(to.getProductQuarterId())){
			hql.append(" and p.PRODUCT_QUARTER_ID = :productQuarterId ");
			params.put("productQuarterId", to.getProductQuarterId());
		}
		if(StringUtils.isNotBlank(to.getMiniClassGradeId())){
			hql.append(" and mc.GRADE = :miniClassGradeId ");
			params.put("miniClassGradeId", to.getMiniClassGradeId());
		}
		if(StringUtils.isNotBlank(to.getTeacherId())){
			hql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", to.getTeacherId());
		}
		if(StringUtils.isNotBlank(to.getStudyManegerId())){
			if("null".equals(to.getStudyManegerId())){
				hql.append(" and mc.STUDY_MANEGER_ID is null ");
			}else{
				hql.append(" and mc.STUDY_MANEGER_ID = :studyManegerId ");
				params.put("studyManegerId", to.getStudyManegerId());
			}
		}
		if(StringUtils.isNotBlank(to.getSmallClassStatus())){
			hql.append(" and s.SMALL_CLASS_STATUS = :smallClassStatus ");
			params.put("smallClassStatus", StudentSmallClassStatus.valueOf(to.getSmallClassStatus()));
		}

		if(StringUtils.isNotBlank(to.getStuNameGrade()) && to.getStuNameGrade()!=null){
			hql.append(" and ( s.name like :studentName2 or dd.name like :studentName2 or ss.name like :studentName2 )");
			params.put("studentName2", "%" + to.getStuNameGrade() + "%");
		}

		hql.append(" order by cp.create_Time desc ");

		hql.append(" limit "+to.getPageNo()*to.getPageSize()+","+(to.getPageNo()+1)*to.getPageSize());

		List<Map<Object, Object>> list = studentDao.findMapBySql(hql.toString(),params);
		for(Map map :list){
			if(map.get("miniClassStudentStatus")!=null){
				map.put("miniClassStudentStatus",StudentSmallClassStatus.valueOf(map.get("miniClassStudentStatus").toString()).getName());
			}
		}

//		List<SmallClassStudentVo> returnList = new ArrayList<SmallClassStudentVo>();
//		for (Object[] objects : list) {
//			ContractProduct cp = (ContractProduct)objects[0];
//			MiniClassStudent mcs = (MiniClassStudent)objects[1];
//			Contract contract = (Contract)objects[2];
//			Student student = (Student) objects[3];
//			Organization brench = (Organization)objects[4];
//			SmallClassStudentVo vo = new SmallClassStudentVo();
//			if (cp != null) {
//				vo.setContractProductCreateDate(cp.getCreateTime().substring(0, 10));
//				vo.setProductName(cp.getProduct().getName());
//			}
//			if (student != null) {
//				vo.setStudentId(student.getId());
//				vo.setStudentName(student.getName());
//				vo.setSex(student.getSex());
//				vo.setStudentcontact(student.getContact());
//				vo.setBlCampusName(student.getBlCampus().getName());
//				vo.setStudentAttanceNo(student.getAttanceNo());
//				vo.setStudentStatus(student.getStatus().getName());
//				vo.setStatus(student.getStudentStatus());
//				if (student.getSchool() != null) {
//					vo.setSchoolName(student.getSchool().getName());
//				}
//				if (student.getGradeDict() != null) {
//					vo.setStudentGradeId(student.getGradeDict().getId());
//					vo.setStudentGradeName(student.getGradeDict().getName());
//				}
//				if (student.getSmallClassStatus() != null) {
//					vo.setMiniClassStudentStatus(student.getSmallClassStatus().getName());
//				}
////				StudnetAccMvVo stuAccMv=this.getStudentAccoutInfo(student.getId());
//				if(cp!=null){
//					BigDecimal remainingHour=cp.getRemainingAmount().divide(cp.getPrice(), 2, RoundingMode.HALF_UP);
//					vo.setMiniRemainingHour(remainingHour);
//
//				}
//			}
//			if (brench != null) {
//				vo.setBrenchName(brench.getName());
//			}
//			if (mcs != null) {
//				MiniClassModalVo mc = mcs.getMiniClass();
//				if (mc != null) {
//					vo.setMiniClassName(mc.getName());
//					if (mc.getSubject() != null) {
//						vo.setSubjectName(mc.getSubject().getName());
//					}
//					if (mc.getGrade() != null) {
//						vo.setGradeName(mc.getGrade().getName());
//					}
//					if (null != mc.getStudyManeger()) {
//						vo.setStudyManagerName(mc.getStudyManeger().getName());
//					}
//					if (mc.getTeacher() != null) {
//						vo.setTeacherName(mc.getTeacher().getName());
//					}
//					vo.setMiniClassStatus(mc.getStatus().getName());
//				}
//			}
//			returnList.add(vo);
//		}
//		dp.setDatas(returnList);

		return list;
	}

	/**
	 * 目标班学生列表
	 */
	@Override
	public List getMobilePromiseStudents(MobileStudentListTo to) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select s.id studentId,s.name studentName,ss.name schoolName,dd.name studentGradeName,p.name productName,gra.name gradeName");
		hql.append(" ,mc.pname promiseClassName,u.name headTeacherName,s.STU_STATUS studentStatus,cp.CONSUME_QUANTITY consumeQuantity,s.status from student s ");
		hql.append(" left join data_dict dd on s.Grade_id=dd.id");
		hql.append(" left join organization o on o.id = s.bl_campus_id");
		hql.append(" left join contract c on c.student_id = s.id");
		hql.append(" left join contract_product cp on cp.contract_id = c.id");
		hql.append(" left join product p on p.id = cp.product_id");
		hql.append(" left join promise_class_student mcs on mcs.contract_product_id = cp.id");
		hql.append(" left join promise_class mc on mc.id=mcs.PROMISE_CLASS_ID");
		hql.append(" left join student_school ss on ss.id = s.SCHOOL");
		hql.append(" left join data_dict gra on gra.id =mc.Grade");
		hql.append(" left join user u on u.user_id = mc.HEAD_TEACHER");
		hql.append(" where cp.type='ECS_CLASS' and mcs.abort_Class is null and s.student_Type='ENROLLED' ");
		hql.append(" and (s.STU_STATUS ='1' or s.STU_STATUS is null)");

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("stuId","s.STUDY_MANEGER_ID");
		sqlMap.put("manergerId","s.STUDY_MANEGER_ID");
		sqlMap.put("selStuId","('"+userService.getCurrentLoginUserId()+"' )");
		sqlMap.put("selManerger","('"+userService.getCurrentLoginUserId()+"' )");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("学生列表","nsql","sql");
		hql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));


		if(StringUtils.isNotBlank(to.getGradeId())){
			hql.append(" and s.GRADE_ID= :gradeId ");
			params.put("gradeId", to.getGradeId());
		}
		if(StringUtils.isBlank(to.getBlCampusId()) && StringUtils.isNotBlank(to.getBrenchId()) ){
			hql.append(" and s.BL_CAMPUS_ID in (select id from Organization where parentId = :brenchId) ");
			params.put("brenchId", to.getBrenchId());
		}
		if(StringUtils.isNotBlank(to.getBlCampusId())){
			hql.append(" and s.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", to.getBlCampusId());
		}
		if(StringUtils.isNotBlank(to.getYearId()) ){
			hql.append(" and p.PRODUCT_VERSION_ID = :yearId ");
			params.put("yearId", to.getYearId());
		}
		if(StringUtils.isNotBlank(to.getHead_teacherId()) ){
			hql.append(" and mc.HEAD_TEACHER = :head_teacherId ");
			params.put("head_teacherId",to.getHead_teacherId());
		}
		if(StringUtils.isNotBlank(to.getpStatus()) ){
			hql.append(" and mc.pStatus = :pStatus ");
			params.put("pStatus",to.getpStatus());
		}

		if(StringUtils.isNotBlank(to.getProductQuarterId())){
			hql.append(" and p.PRODUCT_QUARTER_ID = :productQuarterId ");
			params.put("productQuarterId",to.getProductQuarterId());
		}

		if(StringUtils.isNotBlank(to.getStuNameGradeSchool()) && to.getStuNameGradeSchool()!=null){
			hql.append(" and ( s.name like :studentName2 or dd.name like :studentName2 or ss.name like :studentName2 )");
			params.put("studentName2", "%" + to.getStuNameGradeSchool() + "%");
		}

		if(StringUtils.isNotBlank(to.getModelName1()) ){
			hql.append(" and mcs.result_Status = :modelName1 ");
			params.put("modelName1",to.getModelName1());
		}
		if(StringUtils.isNotBlank(to.getStatus()) ){
			hql.append(" and s.status = :status ");
			params.put("status",to.getStatus());
		}

		hql.append(" order by cp.create_Time desc " );

		hql.append(" limit "+to.getPageNo()*to.getPageSize()+","+(to.getPageNo()+1)*to.getPageSize());

		List<Map<Object, Object>> list=studentDao.findMapBySql(hql.toString(),params);

		for(Map map :list){
			if(map.get("status")!=null){
				map.put("studentStatus",StudentStatus.valueOf(map.get("status").toString()).getName());
			}
			BigDecimal chargeHours = promiseClassRecordDao.countStudentPromiseChargeHoursByStudentId(map.get("studentId").toString());
			map.put("monthHours",chargeHours);
		}

		return list;

	}

	/**
	 * 获取学生地图 地域分析
	 *
	 * @param vo
	 * @return
	 */
	@Override
	public Map<String, StudentMap> getStudentAddressAndLatLog(ParamVo vo) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> map = new HashMap<>();
		sql.append(" SELECT s.`NAME` studentName, s.BL_CAMPUS_ID stuBlCampus, s.ADDRESS address, s.LAT , s.LOG,s.GRADE_ID gradeId, grade.`NAME` gradeName, s.STUDENT_TYPE studentType FROM student s, organization o, region province, region city, data_dict grade");
		sql.append(" WHERE s.BL_CAMPUS_ID=o.id");
		sql.append(" AND o.province_id=province.id");
		sql.append(" AND o.city_id= city.id");
		sql.append(" AND s.GRADE_ID=grade.ID");
		if (StringUtils.isNotBlank(vo.getProvinceId())){
			sql.append(" AND province.id=:provinceId");
			map.put("provinceId", vo.getProvinceId());
		}
		if (StringUtils.isNotBlank(vo.getCityId())){
			sql.append(" AND city.id=:cityId");
			map.put("cityId", vo.getCityId());
		}

		if (StringUtils.isNotBlank(vo.getStudentType())){
			sql.append(" AND s.STUDENT_TYPE=:studentType ");
			map.put("studentType", vo.getStudentType());
		}

		sql.append(" AND (s.`STATUS`<>'GRADUATION' OR s.`STATUS` IS NULL) ");
		sql.append(" AND s.LAT is NOT NULL");
		sql.append(" AND s.LOG is NOT NULL ");
		List<Map<Object, Object>> list = studentDao.findMapBySql(sql.toString(), map);
		Map<String, StudentMap> result = new HashMap<>();
		for (Map m:list){
			StudentAddress studentAddress = new StudentAddress();
			studentAddress.setStudentName((String) m.get("studentName"));
			studentAddress.setStuBlCampus((String) m.get("stuBlCampus"));
			studentAddress.setAddress((String) m.get("address"));
			studentAddress.setLat((String) m.get("LAT"));
			studentAddress.setLog((String) m.get("LOG"));
			studentAddress.setGradeId((String) m.get("gradeId"));
			studentAddress.setGradeName((String)m.get("gradeName"));
			studentAddress.setStudentType((String) m.get("studentType"));
			if (result.get(studentAddress.getGradeId()) == null){
				StudentMap studentMap = new StudentMap();
				studentMap.getStudentAddressList().add(studentAddress);
				if ("ENROLLED".equals(studentAddress.getStudentType())){
					int enrolledSize = studentMap.getEnrolledSize();
					studentMap.setEnrolledSize(enrolledSize+1);
				}else if ("POTENTIAL".equals(studentAddress.getStudentType())){
					int potentialSize = studentMap.getPotentialSize();
					studentMap.setPotentialSize(potentialSize+1);
				}
				studentMap.setGradeName(studentAddress.getGradeName());
				result.put(studentAddress.getGradeId(), studentMap);
			}else {
				StudentMap studentMap = result.get(studentAddress.getGradeId());
				studentMap.getStudentAddressList().add(studentAddress);
				if ("ENROLLED".equals(studentAddress.getStudentType())){
					int enrolledSize = studentMap.getEnrolledSize();
					studentMap.setEnrolledSize(enrolledSize+1);
				}else if ("POTENTIAL".equals(studentAddress.getStudentType())){
					int potentialSize = studentMap.getPotentialSize();
					studentMap.setPotentialSize(potentialSize+1);
				}
				studentMap.setGradeName(studentAddress.getGradeName());
			}
		}
		return result;
	}

	@Override
	public StudentFollowUpVo getStudentFollowUpById(String id) {
		return HibernateUtils.voObjectMapping(studentFollowUpDao.findById(id),StudentFollowUpVo.class);
	}

	@Override
	public Response delStudentFollowUpById(String id) {
		Response res = new Response();
		User followUpUser = userService.getCurrentLoginUser();
		StudentFollowUp followUp=studentFollowUpDao.findById(id);
		if(followUp!=null) {
			if(followUpUser!=null && !followUp.getCreateUserId().equals(followUpUser.getUserId())){
				res.setResultCode(-1);
				res.setResultMessage("只能删除自己保存的记录！");
			}else {
				studentFollowUpDao.delete(followUp);
			}
		}else{
			res.setResultCode(-1);
			res.setResultMessage("找不到对应的记录");
		}
		return res;
	}

    @Override
    public boolean updateStudentContact(StudentChangeVo studentChangeVo, HttpServletResponse response) throws Exception{
        Student student = studentDao.findById(studentChangeVo.getStudentId());
        if (student == null) {
            response.sendError(HttpStatus.SC_NOT_FOUND, "Student Not Found");
            return false;
        }
        if (StringUtil.isNotBlank(student.getContact()) && student.getContact().equals(studentChangeVo.getNewContact())) {
            response.setStatus(HttpStatus.SC_OK);
            return true;
        }
        if (!CommonUtil.checkNumber(studentChangeVo.getNewContact())) {
            response.sendError(HttpStatus.SC_BAD_REQUEST, "Invalid Phone Number");
            return false;
        } 
        if (isContactUsed(studentChangeVo.getNewContact())) {
            response.sendError(HttpStatus.SC_BAD_REQUEST, "Used Phone Number");
        }
        student.setContact(studentChangeVo.getNewContact());
        studentDao.merge(student);
        response.setStatus(HttpStatus.SC_OK);
        return true;
    }

    /**
     * 检查手机号码是否被使用
     */
    @Override
    public boolean isContactUsed(String contact) {
        Map<String, Object> params = Maps.newHashMap();
        String stuSql = " select count(1) from student where CONTACT = :contact ";
        params.put("contact", contact);
        int stuCount = studentDao.findCountSql(stuSql, params);
        if (stuCount > 0) {
            return true;
        }
        String cusSql = " select count(1) from customer where CONTACT = :contact ";
        int cusCount = studentDao.findCountSql(cusSql, params);
        if (cusCount > 0) {
            return true;
        }
        return false;
    }

	@Override
	public List<StudentTransaferVo> getStudentDetailByContact(User user, String contact) {
		// TODO Auto-generated method stub
		long time1 = System.currentTimeMillis();
		//待跟进
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.id as studentId,s.name as studentName,s.contact,s.grade_id as gradeId,d.name as gradeName,c.contact as customerContact,o.city_id cityNum ");
		sql.append(" from student s");
		sql.append(" left join organization o on s.BL_CAMPUS_ID = o.id ");
		sql.append(" left join customer_student_relation csr on s.id=csr.student_id ");
		sql.append(" left join customer c on csr.customer_id=c.id ");
		sql.append(" left join contract con on con.student_id = s.id ");
		sql.append(" left join data_dict d on d.id = s.grade_id ");
		sql.append(" where  c.DELEVER_TARGET = :deleverTarget  and ( c.DEAL_STATUS = '"+CustomerDealStatus.NEW+"' or  c.DEAL_STATUS = '"+CustomerDealStatus.FOLLOWING+"')  ");
		//sql.append(" or  (con.create_user_id= :deleverTarget2 and date_format(con.CREATE_TIME,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d') ) ) ");
		sql.append(" and (s.stu_status = '1' or s.stu_status is null)");
		if(StringUtils.isNotBlank(contact)) {
			params.put("contact1", "%"+contact+"%");
			params.put("contact2", "%"+contact+"%");
			sql.append(" and ( (s.contact like :contact1) or ( ( s.contact is null or s.contact ='') and c.contact like :contact2 ) )");
		}
		//sql.append(" order by s.contact ");
		params.put("deleverTarget", user.getUserId());
		//params.put("deleverTarget2", user.getUserId());
		List<Map<Object, Object>> list = studentDao.findMapBySql(sql.toString(), params);
		List<StudentTransaferVo> vos = new ArrayList<>();
		//Pattern p = Pattern.compile("(^13\\d{9}$)|(^14[57]\\d{8}$)|(^15\\d{9}$)|(^17[0135678]\\d{8}$)|(^18\\d{9}$)");  
		if(list!=null && list.size()>0) {
			for(Map maps : list) {
				StudentTransaferVo vo = new StudentTransaferVo();	
				vo.setStudentId(maps.get("studentId")!=null?maps.get("studentId").toString():null);
				vo.setStudentName(maps.get("studentName")!=null?maps.get("studentName").toString():null);
				vo.setContact(maps.get("contact")!=null?maps.get("contact").toString():null);
				vo.setGradeId(maps.get("gradeId")!=null?maps.get("gradeId").toString():null);
				vo.setGradeName(maps.get("gradeName")!=null?maps.get("gradeName").toString():null);
				vo.setCityNum(maps.get("cityNum")!=null?maps.get("cityNum").toString():null);

				if(StringUtils.isBlank(vo.getContact())) {
					String studentContact = maps.get("customerContact")!=null?maps.get("customerContact").toString():"";
					//Matcher m = p.matcher(studentContact);
					if(CheckPhoneNumber.isMobileNO(studentContact)) {
						vo.setContact(studentContact);
						vos.add(vo);
					}
				}else {
					vos.add(vo);
				}
				
			}
		}
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" select s.id as studentId,s.name as studentName,s.contact,s.grade_id as gradeId,d.name as gradeName,c.contact as customerContact,o.city_id cityNum ");
		sql2.append(" from student s");
		sql2.append(" left join organization o on s.BL_CAMPUS_ID = o.id ");
		sql2.append(" left join customer_student_relation csr on s.id=csr.student_id ");
		sql2.append(" left join customer c on csr.customer_id=c.id ");
		sql2.append(" left join contract con on con.student_id = s.id ");
		sql2.append(" left join data_dict d on d.id = s.grade_id ");
		//sql2.append(" where ( ( c.DELEVER_TARGET = :deleverTarget1  and c.DEAL_STATUS = '"+CustomerDealStatus.STAY_FOLLOW+"') ");
		sql2.append(" where  (con.create_user_id= :deleverTarget and date_format(con.CREATE_TIME,'%Y-%m-%d') = date_format(now(),'%Y-%m-%d') )  ");
		sql.append(" and (s.stu_status = '1' or s.stu_status is null)");
		if(StringUtils.isNotBlank(contact)) {
			params.put("contact1", "%"+contact+"%");
			params.put("contact2", "%"+contact+"%");
			sql2.append(" and ( (s.contact like :contact1) or ( ( s.contact is null or s.contact ='') and c.contact like :contact2 ) )");
		}
		//sql.append(" order by s.contact ");
		//params.put("deleverTarget1", user.getUserId());
		//params.put("deleverTarget2", user.getUserId());
		List<Map<Object, Object>> list2 = studentDao.findMapBySql(sql2.toString(), params);
		if(list2!=null && list2.size()>0) {
			for(Map maps : list2) {
				StudentTransaferVo vo = new StudentTransaferVo();	
				vo.setStudentId(maps.get("studentId")!=null?maps.get("studentId").toString():null);
				vo.setStudentName(maps.get("studentName")!=null?maps.get("studentName").toString():null);
				vo.setContact(maps.get("contact")!=null?maps.get("contact").toString():null);
				vo.setGradeId(maps.get("gradeId")!=null?maps.get("gradeId").toString():null);
				vo.setGradeName(maps.get("gradeName")!=null?maps.get("gradeName").toString():null);
				vo.setCityNum(maps.get("cityNum")!=null?maps.get("cityNum").toString():null);
				
				if(StringUtils.isBlank(vo.getContact())) {
					String studentContact = maps.get("customerContact")!=null?maps.get("customerContact").toString():"";
					//Matcher m = p.matcher(studentContact);
					if(CheckPhoneNumber.isMobileNO(studentContact)) {
						vo.setContact(studentContact);
						vos.add(vo);
					}
				}else {
					vos.add(vo);
				}
			}
		}
		
		StringBuffer sql3 = new StringBuffer();
		sql3.append(" select DISTINCT s.id as studentId,s.name as studentName,s.contact,s.grade_id as gradeId,d.name as gradeName,c.contact as customerContact,o.city_id cityNum ");
		sql3.append(" from student s");
		sql3.append(" left join organization o on s.BL_CAMPUS_ID = o.id ");
		sql3.append(" LEFT JOIN student_organization so ON s.ID = so.STUDENT_ID ");
		sql3.append(" left join customer_student_relation csr on s.id=csr.student_id ");
		sql3.append(" left join customer c on csr.customer_id=c.id ");
		sql3.append(" left join contract con on con.student_id = s.id ");
		sql3.append(" left join data_dict d on d.id = s.grade_id ");
		sql3.append(" where  ( s.STUDY_MANEGER_ID = :deleverTarget or  so.STUDY_MANAGER_ID =:studyManagerId )  ");
		sql3.append(" and (s.stu_status = '1' or s.stu_status is null)");
		if(StringUtils.isNotBlank(contact)) {
			params.put("contact1", "%"+contact+"%");
			params.put("contact2", "%"+contact+"%");
			sql3.append(" and ( (s.contact like :contact1) or ( ( s.contact is null or s.contact ='') and c.contact like :contact2 ) )");
		}
		params.put("deleverTarget", user.getUserId());
		params.put("studyManagerId", user.getUserId());
		List<Map<Object, Object>> list3 = studentDao.findMapBySql(sql3.toString(), params);
		
		if(list3!=null && list3.size()>0) {
			for(Map maps : list3) {
				StudentTransaferVo vo = new StudentTransaferVo();	
				vo.setStudentId(maps.get("studentId")!=null?maps.get("studentId").toString():null);
				vo.setStudentName(maps.get("studentName")!=null?maps.get("studentName").toString():null);
				vo.setContact(maps.get("contact")!=null?maps.get("contact").toString():null);
				vo.setGradeId(maps.get("gradeId")!=null?maps.get("gradeId").toString():null);
				vo.setGradeName(maps.get("gradeName")!=null?maps.get("gradeName").toString():null);
				vo.setCityNum(maps.get("cityNum")!=null?maps.get("cityNum").toString():null);
				
				if(StringUtils.isBlank(vo.getContact())) {
					String studentContact = maps.get("customerContact")!=null?maps.get("customerContact").toString():"";
					//Matcher m = p.matcher(studentContact);
					if(CheckPhoneNumber.isMobileNO(studentContact)) {
						vo.setContact(studentContact);
						vos.add(vo);
					}
				}else {
					vos.add(vo);
				}
			}
		}
		
		vos = removeDuplicate(vos);
		long time2 = System.currentTimeMillis();
		System.out.println("time2-time1:"+(time2-time1)+"ms");
		return vos;
	}
    /**
     * 根据学生编号查询学生分公司城市和年级
     */
    @Override
    public StudentCityGradeVo findStudentCityGradeVoByStudentId(String studentId) {
        StudentCityGradeVo result = new StudentCityGradeVo();
        Student student = studentDao.findById(studentId);
        result.setStudentId(studentId);
        if (student != null) {
            if (student.getBlCampus() != null && StringUtil.isNotBlank(student.getBlCampus().getBelong())) {
                Organization branch = organizationDao.findById(student.getBlCampus().getBelong());
                if (branch.getOrgType() == OrganizationType.CAMPUS) {
                    branch = organizationDao.findById(branch.getParentId());
                }
                if (branch != null && StringUtil.isNotBlank(branch.getCityId())) {
                    Region city = regionService.getRegionById(branch.getCityId());
                    if (city != null) {
                        result.setCityId(city.getId());
                        result.setCityName(city.getName());
                    }
                }
            }
            DataDict grade = student.getGradeDict();
            if (grade != null) {
                result.setGradeId(grade.getId());
                result.setGradeName(grade.getName());
            }
        }
        return result;
    }

	/*******************************************************************20170613**********************************************/

	private List<StudentTransaferVo> removeDuplicate(List<StudentTransaferVo> list) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getStudentId().equals(list.get(i).getStudentId())) {
					list.remove(j);
				}
			}
		}		
		return list;
	}


	@Override
	public Student findStudentByRelateNo(String studentRelateNo) {
	    Student student = null;
	    String hql = " from Student where relatedStudentNo = :relatedStudentNo ";
	    Map<String, Object> params = Maps.newHashMap();
	    params.put("relatedStudentNo", studentRelateNo);
	    List<Student> list = studentDao.findAllByHQL(hql, params);
	    if (list != null && list.size() > 0) {
	        student = list.get(0);
	    }
	    return student;
	}

	@Override
	public List<StudentTransaferVo> getStudentDetailByContactAdvance(User user, String contact) {
		// TODO Auto-generated method stub
		Organization brench = userService.getBelongBranchByUserId(user.getUserId());
		if(brench == null) {
			logger.info("getStudentDetailByContactAdvance:没有所属分公司"+user.getName()+","+user.getContact());
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct s.id as studentId,s.name as studentName,s.contact,s.grade_id as gradeId,d.name as gradeName,c.contact as customerContact,o.city_id cityNum ");
		sql.append(" from student s");
		sql.append(" left join organization o on s.BL_CAMPUS_ID = o.id ");
		sql.append(" left join customer_student_relation csr on s.id=csr.student_id ");
		sql.append(" left join customer c on csr.customer_id=c.id ");
		sql.append(" left join data_dict d on d.id = s.grade_id ");
		sql.append(" where   ( c.DEAL_STATUS !='"+CustomerDealStatus.INVALID+"')  ");
		sql.append(" and (s.stu_status = '1' or s.stu_status is null)");
		sql.append(" and o.orgLevel like :orgLevel ");
		params.put("orgLevel", brench.getOrgLevel()+"%");
		if(StringUtils.isNotBlank(contact)) {
			params.put("contact1", "%"+contact+"%");
			params.put("contact2", "%"+contact+"%");
			sql.append(" and ( (s.contact like :contact1) or ( ( s.contact is null or s.contact ='') and c.contact like :contact2 ) )");
		}
		List<Map<Object, Object>> list = studentDao.findMapBySql(sql.toString(), params);
		List<StudentTransaferVo> vos = new ArrayList<>();
		//Pattern p = Pattern.compile("(^13\\d{9}$)|(^14[57]\\d{8}$)|(^15\\d{9}$)|(^17[0135678]\\d{8}$)|(^18\\d{9}$)");  
		if(list!=null && list.size()>0) {
			for(Map maps : list) {
				StudentTransaferVo vo = new StudentTransaferVo();	
				vo.setStudentId(maps.get("studentId")!=null?maps.get("studentId").toString():null);
				vo.setStudentName(maps.get("studentName")!=null?maps.get("studentName").toString():null);
				vo.setContact(maps.get("contact")!=null?maps.get("contact").toString():null);
				vo.setGradeId(maps.get("gradeId")!=null?maps.get("gradeId").toString():null);
				vo.setGradeName(maps.get("gradeName")!=null?maps.get("gradeName").toString():null);
				vo.setCityNum(maps.get("cityNum")!=null?maps.get("cityNum").toString():null);

				if(StringUtils.isBlank(vo.getContact())) {
					String studentContact = maps.get("customerContact")!=null?maps.get("customerContact").toString():"";
					//Matcher m = p.matcher(studentContact);
					if(CheckPhoneNumber.isMobileNO(studentContact)) {
						vo.setContact(studentContact);
						vos.add(vo);
					}
				}else {
					vos.add(vo);
				}
				
			}
		}
		
		return vos;
	}

	@Override
	public List<Student> findStuByManegerIdAndCampusId(String userId, String changeCampusId) {
		return studentDao.findStuByManegerIdAndCampusId(userId,changeCampusId);
	}

}
