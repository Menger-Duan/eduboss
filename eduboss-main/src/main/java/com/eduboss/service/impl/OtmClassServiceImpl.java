package com.eduboss.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.BaseStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.CourseSummaryStatus;
import com.eduboss.common.OtmClassAttendanceStatus;
import com.eduboss.common.OtmClassStatus;
import com.eduboss.common.OtmClassStudentChargeStatus;
import com.eduboss.common.ProductType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.StudentStatus;
import com.eduboss.common.TeacherLevel;
import com.eduboss.common.TeacherType;
import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.CourseConflictDao;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.OtmClassCourseDao;
import com.eduboss.dao.OtmClassDao;
import com.eduboss.dao.OtmClassStudentAttendentDao;
import com.eduboss.dao.OtmClassStudentDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.StudentOrganizationDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MoneyWashRecords;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.OtmClassStudent;
import com.eduboss.domain.OtmClassStudentAttendent;
import com.eduboss.domain.OtmClassStudentId;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AccountChargeRecordsVo;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.HasClassCourseVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.domainVo.OtmClassStudentAttendentVo;
import com.eduboss.domainVo.OtmClassStudentVo;
import com.eduboss.domainVo.OtmClassVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.StudyManagerMobileVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.ChargeService;
import com.eduboss.service.MessageService;
import com.eduboss.service.MoneyWashRecordsService;
import com.eduboss.service.OdsMonthIncomeCampusService;
import com.eduboss.service.OtmClassService;
import com.eduboss.service.UserService;
import com.eduboss.task.SendJoinOtmCourseMsgThread;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FileUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.ImageSizer;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.PushRedisMessageUtil;
import com.eduboss.utils.StringUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("com.eduboss.service.OtmClassService")
public class OtmClassServiceImpl implements OtmClassService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OtmClassDao otmClassDao;
	
	@Autowired
	private OtmClassStudentDao otmClassStudentDao;
	
	@Autowired
	private OtmClassCourseDao otmClassCourseDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
	private OtmClassStudentAttendentDao otmClassStudentAttendentDao;
	
	@Autowired
	private AccountChargeRecordsDao accountChargeRecordsDao;
	
	@Autowired
	private ContractProductDao contractproductDao;
	
	@Autowired
	private CourseConflictDao courseConflictDao;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ChargeService chargeService;
	
	@Autowired
	private StudentOrganizationDao studentOrganizationDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MoneyWashRecordsService moneyWashRecordsService;

	@Autowired
	private OdsMonthIncomeCampusService odsMonthIncomeCampusService;

	/**
	 * 获取一对多班级列表
	 * @param otmClassVo
	 * @param dp
	 * @return
	 *//*
	@Override
	public DataPackage getOtmClassList(OtmClassVo otmClassVo, DataPackage dp) {
		String hql=" from OtmClass otm ";
		String hqlWhere="";
			
		if(StringUtils.isNotEmpty(otmClassVo.getStartDate())){
			hqlWhere+=" and startDate>= '"+otmClassVo.getStartDate()+"'";
		}
		if(StringUtils.isNotEmpty(otmClassVo.getEndDate())){
			hqlWhere+=" and startDate<= '"+otmClassVo.getEndDate()+"'";
		}
		
		if(StringUtils.isNotEmpty(otmClassVo.getName())){
			hqlWhere+=" and name like '%"+otmClassVo.getName()+"%'";
		}
		
		if(otmClassVo.getOtmType() != null && otmClassVo.getOtmType() > 0){
			hqlWhere+=" and otmType = '"+otmClassVo.getOtmType()+"'";
		}
		
		if(StringUtils.isNotEmpty(otmClassVo.getGradeId())){
			hqlWhere+=" and grade.id = '"+otmClassVo.getGradeId()+"'";
		}
		
		if(StringUtils.isNotEmpty(otmClassVo.getSubjectId())){
			hqlWhere+=" and subject.id = '"+otmClassVo.getSubjectId()+"'";
		}
		
		if(StringUtils.isNotEmpty(otmClassVo.getBlCampusId())){
			hqlWhere+=" and blCampus.id = '"+otmClassVo.getBlCampusId()+"'";
		}
		
		if(StringUtils.isNotEmpty(otmClassVo.getRecruitStudentStatusId())){
			hqlWhere+=" and recruitStudentStatus.id = '"+otmClassVo.getRecruitStudentStatusId()+"'";
		}
		
		if(otmClassVo.getStatus()!=null){
			hqlWhere+=" and status = '"+otmClassVo.getStatus()+"'";
		}
		
		//归属组织架构
		Organization campus = userService.getBelongCampus();
		if (campus != null && StringUtils.isNotEmpty(campus.getOrgLevel())) {
			String orgLevel = campus.getOrgLevel();
			hqlWhere+=" and blCampus.id in (select id from Organization where orgLevel like '"+orgLevel+"%')";
		}
		
		
		if(!"".equals(hqlWhere)){
			hqlWhere="where "+hqlWhere.substring(4);
		}
		hql+=hqlWhere;
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by otm."+dp.getSidx()+" "+dp.getSord();
		} 
		
		dp=otmClassDao.findPageByHQL(hql, dp);
		
		List<OtmClass> list=(List<OtmClass>) dp.getDatas();
		List<OtmClassVo> voList= new ArrayList<OtmClassVo>();
		for(OtmClass otm : list){
			OtmClassVo vo=HibernateUtils.voObjectMapping(otm, OtmClassVo.class);
			List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
			
			StudentCriterionList.add(Expression.eq("id.otmClassId", otm.getOtmClassId()));
			List<OtmClassStudent> otmStudentList = otmClassStudentDao.findAllByCriteria(StudentCriterionList);
			String studentNames = "";
			if (otmStudentList != null && otmStudentList.size() > 0) {
				for (OtmClassStudent otmClassStudent : otmStudentList) {
					studentNames += otmClassStudent.getStudent().getName() + ",";
				}
				studentNames = studentNames.substring(0, studentNames.length() - 1);
			}
			vo.setStudentNames(studentNames);
			vo.setOtmClassPeopleNum(otmStudentList.size());
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}*/
	
	/**
	 * 获取一对多班级列表
	 * @param otmClassVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOtmClassList(OtmClassVo otmClassVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " SELECT * FROM OTM_CLASS ";
		String sqlWhere = " WHERE 1=1 ";
			
		if(StringUtils.isNotEmpty(otmClassVo.getStartDate())){
			sqlWhere+=" AND START_DATE >= :startDate ";
			params.put("startDate", otmClassVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(otmClassVo.getEndDate())){
			sqlWhere+=" AND START_DATE <= :endDate ";
			params.put("endDate", otmClassVo.getEndDate());
		}
		if(StringUtils.isNotEmpty(otmClassVo.getName())){
			sqlWhere += " AND NAME LIKE :name ";
			params.put("name", "%" + otmClassVo.getName() + "%");
		}
		if(otmClassVo.getOtmType() != null && otmClassVo.getOtmType() > 0){
			sqlWhere+=" AND OTM_TYPE = :otmType ";
			params.put("otmType", otmClassVo.getOtmType());
		}
		if(StringUtils.isNotEmpty(otmClassVo.getGradeId())){
			sqlWhere+=" AND GRADE = :gradeId ";
			params.put("gradeId", otmClassVo.getGradeId());
		}
		if(StringUtils.isNotEmpty(otmClassVo.getSubjectId())){
			sqlWhere+=" AND SUBJECT = :subjectId ";
			params.put("subjectId", otmClassVo.getSubjectId());
		}
		if(StringUtils.isNotEmpty(otmClassVo.getBlCampusId())){
			sqlWhere+=" AND BL_CAMPUS_ID = :blCampusId ";
			params.put("blCampusId", otmClassVo.getBlCampusId());
		}
		if(StringUtils.isNotEmpty(otmClassVo.getRecruitStudentStatusId())){
			sqlWhere+=" AND RECRUIT_STUDENT_STATUS = :recruitStudentStatusId ";
			params.put("recruitStudentStatusId", otmClassVo.getRecruitStudentStatusId());
		}
		if(otmClassVo.getStatus()!=null){
			sqlWhere+=" AND STATUS = :status ";
			params.put("status", otmClassVo.getStatus());
		}
		
		//归属组织架构
		Organization campus = userService.getBelongCampus();
		if (campus != null && StringUtils.isNotEmpty(campus.getOrgLevel())) {
			String orgLevel = campus.getOrgLevel();
			sqlWhere+=" AND BL_CAMPUS_ID IN (SELECT ID FROM organization where orgLevel like '" + orgLevel + "%')";
		}
		sql += sqlWhere;
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sql+=" order by "+dp.getSidx()+" "+dp.getSord();
		} 
		dp = otmClassDao.findPageBySql(sql, dp, true, params);
		List<OtmClassVo> voList = HibernateUtils.voListMapping((List<OtmClass>) dp.getDatas(), OtmClassVo.class);
		
		for(OtmClassVo otmVo : voList){
			if (StringUtil.isNotBlank(otmVo.getBlCampusId())) {
				Organization campusOrg = organizationDao.findById(otmVo.getBlCampusId());
				otmVo.setBlCampusName(campusOrg.getName());
			}
			if (StringUtil.isNotBlank(otmVo.getGradeId())) {
				DataDict gradeDict = dataDictDao.findById(otmVo.getGradeId());
				otmVo.setGradeName(gradeDict.getName());
			}
			if (StringUtil.isNotBlank(otmVo.getSubjectId())) {
				DataDict subjectDict = dataDictDao.findById(otmVo.getSubjectId());
				otmVo.setSubjectName(subjectDict.getName());
			}
			if (StringUtil.isNotBlank(otmVo.getRecruitStudentStatusId())) {
				DataDict recruitStudentStatusDict = dataDictDao.findById(otmVo.getRecruitStudentStatusId());
				otmVo.setRecruitStudentStatusName(recruitStudentStatusDict.getName());
			}
			if (StringUtil.isNotBlank(otmVo.getTeacherId())) {
				User teacher = userDao.findById(otmVo.getTeacherId());
				otmVo.setTeacherName(teacher.getName());
			}
			List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
			StudentCriterionList.add(Expression.eq("otmClass.otmClassId", otmVo.getOtmClassId()));
			List<OtmClassStudent> otmStudentList = otmClassStudentDao.findAllByCriteria(StudentCriterionList);
			String studentNames = "";
			if (otmStudentList != null && otmStudentList.size() > 0) {
				for (OtmClassStudent otmClassStudent : otmStudentList) {
					studentNames += otmClassStudent.getStudent().getName() + ",";
				}
				studentNames = studentNames.substring(0, studentNames.length() - 1);
			}
			otmVo.setStudentNames(studentNames);
			otmVo.setOtmClassPeopleNum(otmStudentList.size());
		}
		dp.setDatas(voList);
		return dp;
	}

	/**
	 * 删除和编辑一对多班级信息
	 * @param oper
	 * @param otmClassVo
	 */
	@Override
	public void operationOtmClassRecord(String oper, OtmClassVo otmClassVo) throws Exception  {
		OtmClass otmClass = HibernateUtils.voObjectMapping(otmClassVo, OtmClass.class);
		if ("del".equalsIgnoreCase(oper)) {
			deleteOtmClass(otmClass);
		} else {
			saveOrUpdateOtmClass(otmClass);
		}
	}
	
	@Override
	public void deleteOtmClass(OtmClass otmClass) throws Exception {
		
		if (StringUtils.isBlank(otmClass.getOtmClassId())) {
			throw new ApplicationException("要删除的一对多ID为空！");
		}
		// 删除一对多的课程
		otmClassDao.deleteOtmClassCourse(otmClass.getOtmClassId());
		// 删除一对多记录
		otmClassDao.delete(otmClass);
	}
	
	@Override
	public void saveOrUpdateOtmClass(OtmClass otmClass) throws Exception {
		
		otmClass.setModifyTime(DateTools.getCurrentDateTime());
		User user = userService.getCurrentLoginUser();
		if (otmClass.getOtmType() > 0) {
			otmClass.setPeopleQuantity(otmClass.getOtmType());
		}
		otmClass.setModifyUserId(user.getUserId());
		if(otmClass.getOtmClassId()==null){
			otmClass.setCreateTime(DateTools.getCurrentDateTime());
			otmClass.setCreateUserId(user.getUserId());
			otmClass.setStatus(OtmClassStatus.PENDDING_START);
		}
		
		otmClassDao.save(otmClass);
		//修改今天之后的一对多课程
		otmClassCourseDao.updateOtmClassCourse(otmClass);
	}
	
	/**
	 * 根据id查找一对多
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public OtmClassVo findOtmClassById(String id) throws Exception {
		OtmClassVo vo = null;
		if (StringUtil.isNotBlank(id)) {
			OtmClass otmClass = otmClassDao.findById(id);
			vo = HibernateUtils.voObjectMapping(otmClass, OtmClassVo.class);
		}
		return vo;
	}
	
	/**
	 * 根据一对多id查看合同拟报读学生id列表
	 * @param otmClasId
	 * @return
	 */
	@Override
	public List<StudentVo> getStudentWantListByOtmClassId(String otmClassId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.*, sum(case when cp.PAID_STATUS = 'PAID' then (cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT) else (cp.PAID_AMOUNT - cp.CONSUME_AMOUNT) end) as remainingAmount,  ");
		sql.append(" sum(case when cp.DEAL_DISCOUNT > 0 then (case when cp.PAID_STATUS = 'PAID' then (cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT) else (cp.PAID_AMOUNT - cp.CONSUME_AMOUNT) end)/(cp.PRICE*cp.DEAL_DISCOUNT)  ");
		sql.append(" else (case when cp.PAID_STATUS = 'PAID' then (cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT) else (cp.PAID_AMOUNT - cp.CONSUME_AMOUNT) end)/cp.PRICE end) as quantity ");
		sql.append(" from student s inner join contract c on s.id = c.STUDENT_ID ");
		sql.append(" left join contract_product cp on c.id = cp.contract_id and cp.type ='ONE_ON_MANY' ");
		sql.append(" where 1 = 1 ");
		// 一对多产品类型符合的学生
		if (StringUtil.isNotBlank(otmClassId)) {
			OtmClass otmClass = otmClassDao.findById(otmClassId);
			String blCampusId = otmClass.getBlCampus().getId();
			sql.append(" and '" + blCampusId + "' in (select ORGANIZATION_ID from student_organization so where s.ID = so.STUDENT_ID and STUDY_MANEGER_ID is not null) ");
			sql.append(" and cp.PRODUCT_ID in (select id from product where ONE_ON_MANY_TYPE in (select OTM_TYPE from otm_class where OTM_CLASS_ID = '").append(otmClassId).append("' ))");
		}
		// 排除已报读该一对多的学生
		sql.append(" and s.id not in (select STUDENT_ID from otm_class_student where OTM_CLASS_ID = '").append(otmClassId).append("')");
		//排除已经结课的合同产品 CLOSE_PRODUCT
		sql.append(" and cp.STATUS <> '").append(ContractProductStatus.CLOSE_PRODUCT).append("' ");
		sql.append(" group by s.ID ");
		
		// ID1 是合同产品ID。
		List<Object[]> list= studentDao.getCurrentSession().createSQLQuery(sql.toString()).addEntity("s", Student.class).addScalar("remainingAmount").addScalar("quantity").list();
		List<Student> students = new ArrayList<Student>();
		for (int i = 0; i < list.size(); i++) {
			students.add((Student) list.get(i)[0]);
		}
		List<StudentVo> vos = HibernateUtils.voListMapping(students, StudentVo.class);
		for (int i = 0; i < vos.size(); i++) {
			// 剩余资金 和 剩余课时 的显示
			vos.get(i).setRemainingAmount((BigDecimal) list.get(i)[1]);
			vos.get(i).setQuantity((BigDecimal) list.get(i)[2]);
			// 剩余资金 和 剩余课时 的显示
//			if(!"".equals(list.get(i)[2].toString())){
//				ContractProduct conProd = contractProductDao.findById(list.get(i)[2].toString());
//				vos.get(i).setRemainingAmount(conProd.getRemainingAmount()==null? BigDecimal.ZERO :conProd.getRemainingAmount());
//				vos.get(i).setQuantity((conProd.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 || conProd.getPrice().compareTo(BigDecimal.ZERO) == 0 ||conProd.getDealDiscount().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : conProd.getRemainingAmount().divide(conProd.getPrice().multiply(conProd.getDealDiscount()),2));
//			}
		}
		return vos;
	}
	
	/**
	 * 根据一对多id查看一对多已报读学生
	 * @param otmClasId
	 * @return
	 */
	public List<StudentVo> getStudentAlreadyEnrollOtmClasss(String otmClassId) {
		List<Criterion> list=new ArrayList<Criterion>(); 
		list.add(Expression.eq("otmClass.otmClassId", otmClassId));
		List<Order> orderList=new ArrayList<Order>();
		orderList.add(Order.desc("createTime"));
		
		List<OtmClassStudent> otmClassList =otmClassStudentDao.findAllByCriteria(list,orderList);
		List<StudentVo> stuList=new ArrayList<StudentVo>();
		for(OtmClassStudent otmStudent : otmClassList){
			if(otmStudent.getStudent()!=null){
				StudentVo vo=HibernateUtils.voObjectMapping(otmStudent.getStudent(), StudentVo.class);
				vo.setOtmClassStudentChargeStatus(otmStudent.getOtmClassStudentChargeStatus().getValue());
				if(StringUtils.isEmpty(otmStudent.getFirstSchoolTime())){
					vo.setFirstSchoolTime("");
				}else{
					vo.setFirstSchoolTime(otmStudent.getFirstSchoolTime());
				}
				
				stuList.add(vo);
			}
		}
		return stuList;
	}
	
	/**
	 * 批量添加一对多学生
	 * @param studentIds  学生id （多条用，隔开）
	 * @param otmClassId 一对多id
	 * @param firstSchoolTime 第一次上课时间
	 * @param isAllowAddStudent 是否
	 * @throws Exception
	 */
	public void AddStudentForOtmClasss(String studentIds, String otmClassId, String firstSchoolTime, Boolean isAllowAddStudent) throws Exception {
		String[] stuIds=studentIds.split(",");
		boolean allowRegistration =true;
		if(isAllowAddStudent!=null && isAllowAddStudent) {
			allowRegistration = allowAddStudent4OtmClass(otmClassId, stuIds.length);// 是否允许添加一对多学生
		}
		
		for (int i = 0;i < stuIds.length; i++) {
			if (null !=otmClassStudentDao.getOneOtmClassStudent(otmClassId, stuIds[i])) {
				Student student = studentDao.findById(stuIds[i]);
				throw new ApplicationException("学生：" + student.getName() + "已经在有报名记录了！");
			}
		}
		
		if (allowRegistration) {
			OtmClass otmClass = otmClassDao.findById(otmClassId);
			int otmClassMaxPeopleNumber = otmClass.getPeopleQuantity();// 最大人数
			int peopleExistingNumber = getOtmClassExistingPeopleNumber(otmClassId);// 现有人数
			User user = userService.getCurrentLoginUser();
			for(int i = 0;i < stuIds.length; i++){
				OtmClassStudent otmStu =new OtmClassStudent();
				otmStu.setStudent(new Student(stuIds[i]));
				otmStu.setOtmClass(otmClass);
				otmStu.setOtmClassStudyStatus(OtmClassStatus.PENDDING_START);
				otmStu.setCreateUserId(user.getUserId());
				otmStu.setCreateTime(DateTools.getCurrentDateTime());
				otmStu.setModifyUserId(user.getUserId());
				otmStu.setModifyTime(DateTools.getCurrentDateTime());
				otmStu.setOtmClassStudentChargeStatus(OtmClassStudentChargeStatus.CHARGED);
				otmStu.setFirstSchoolTime(firstSchoolTime);
				otmClassStudentDao.save(otmStu);
				this.addOtmClassStudentAttendents(stuIds[i], otmClassId, firstSchoolTime);
			}
			
			if(otmClass.getRecruitStudentStatus()==null){
				DataDict dd = dataDictDao.findById("DAT0000000268");//招生中，暂定用id来找，如果找不到就不处理吧。
				if(dd!=null){
					otmClass.setRecruitStudentStatus(dd);
					otmClassDao.save(otmClass);
				}
			}else if(otmClass.getStatus().getValue().equals("PENDDING_START") && otmClass.getRecruitStudentStatus()!=null && !otmClass.getRecruitStudentStatus().getId().equals("DAT0000000270")){
				
				int afterPeopleNumber = peopleExistingNumber + stuIds.length;
				if (afterPeopleNumber >= otmClassMaxPeopleNumber) {//如果超过了招生人数
					DataDict dd = dataDictDao.findById("DAT0000000270");//已满员
					if(dd!=null){
						otmClass.setRecruitStudentStatus(dd);
						otmClassDao.save(otmClass);
					}
				} 
			}
		}
	}
	
	/**
	 * 增加对应一对多的对应学生在课程上课时间再firstSchoolTime之后的考勤记录
	 * @param studentId
	 * @param otmClassId
	 * @param firstSchoolTime
	 */
	private void addOtmClassStudentAttendents(String studentId,String otmClassId,String firstSchoolTime) {
		User user = userService.getCurrentLoginUser();
		DataPackage dp = new DataPackage(0, 999);
		dp = otmClassCourseDao.getOtmClassCourseListByOtmClassId(otmClassId, firstSchoolTime, dp);
		List<OtmClassCourse> otmClassCourseList = (List<OtmClassCourse>) dp.getDatas();
		for (OtmClassCourse otmClassCourse: otmClassCourseList) {
			OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao.getOneOtmClassStudentAttendent(otmClassCourse.getOtmClassCourseId(),studentId);
			if (otmClassStudentAttendent == null) {
				if (otmClassCourse.getCourseStatus() == CourseStatus.CHARGED) {
					// 处理信息发送
					this.sendJoinOtmCourseMsg(otmClassCourse.getOtmClassCourseId(), studentId);
				}
				User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(studentId, otmClassCourse.getOtmClass().getBlCampus().getId());
				otmClassStudentAttendent = new OtmClassStudentAttendent();
				otmClassStudentAttendent.setOtmClassCourse(otmClassCourse);
				otmClassStudentAttendent.setStudentId(studentId);
				otmClassStudentAttendent.setOtmClassAttendanceStatus(OtmClassAttendanceStatus.NEW); // 未上课
				otmClassStudentAttendent.setChargeStatus(OtmClassStudentChargeStatus.UNCHARGE);// 未扣费
				otmClassStudentAttendent.setCreateUser(user);
				otmClassStudentAttendent.setCreateTime(DateTools.getCurrentDateTime());
				otmClassStudentAttendent.setModifyUser(user);
				otmClassStudentAttendent.setModifyTime(DateTools.getCurrentDateTime());
				otmClassStudentAttendent.setStudyManager(studyManager);
				otmClassStudentAttendentDao.save(otmClassStudentAttendent);
			}
		}
	}
	
	/**
	 * 一对多课程学生插班
	 * @param otmCourseId
	 * @param studentId
     */
	private void sendJoinOtmCourseMsg(String otmCourseId, String studentId){
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendJoinOtmCourseMsgThread thread = new SendJoinOtmCourseMsgThread(otmCourseId, studentId, currentUserId);
		taskExecutor.execute(thread);
	}
	
	/**
	 * 是否允许新增一对多学生
	 */
	@Override
	public boolean allowAddStudent4OtmClass(String otmClassId, int newStudentNumber) {
		boolean returnAllow = false;// 是否允许新增一对多学生
		
		OtmClass otmClass = otmClassDao.findById(otmClassId);
		
		int otmClassMaxPeopleNumber = otmClass.getOtmType(); // 最大人数
		
		int peopleExistingNumber = getOtmClassExistingPeopleNumber(otmClassId);// 现有人数
		int afterPeopleNumber = peopleExistingNumber + newStudentNumber;
		if (afterPeopleNumber <= otmClassMaxPeopleNumber) {
			// 一对多现有人数少于最大人数，还可以继续报名
			returnAllow = true;
		} else {
			returnAllow = false;
			throw new ApplicationException("报名人数超出一对多最大人数约束（"+ otmClassMaxPeopleNumber + "人）");
		}
		
		
		return returnAllow;
	}
	
	/**
	 * 一对多现有人数
	 * @param otmClassId
	 * @return
	 */
	@Override
	public int getOtmClassExistingPeopleNumber(String otmClassId) {
		List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
		StudentCriterionList.add(Expression.eq("otmClass.otmClassId", otmClassId));
		int peopleExistingNumber = otmClassStudentDao.findCountByCriteria(StudentCriterionList);
		return peopleExistingNumber;
	}
	
	/**
	 * 修改一对多学生首次上课时间
	 * @param studentId
	 * @param otmClassId
	 * @param firstSchoolTime
	 * @return
	 */
	@Override
	public void updateOtmClassStudentfirstSchoolTime(String studentId,String otmClassId,String firstSchoolTime) {
		OtmClassStudent otmClassStudent = otmClassStudentDao.getOneOtmClassStudent(otmClassId, studentId);
		if(otmClassStudent!=null){
			otmClassStudent.setFirstSchoolTime(firstSchoolTime);
			otmClassStudentDao.save(otmClassStudent);
			otmClassStudentAttendentDao.deleteOtmClassStudentAttendentByStudentAndOtmClass(studentId, otmClassId);
			this.addOtmClassStudentAttendents(studentId, otmClassId, firstSchoolTime);
		}
	}
	
	/**
	 * 获取有考勤记录的一对多学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param otmClassId 一对多班级id
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Student> getOtmClassAttendedStudent(String studentIds, String otmClassId) throws Exception {
		List<Student> studentList = new ArrayList<Student>();
		String[] stuIds=studentIds.split(",");
		for (String id : stuIds) {
			int count = otmClassStudentAttendentDao.getCountOfUnchargedAttendenceRecordByOtmClassAndStudent(otmClassId, id, "UNCHARGE");
			if (count > 0) {
				studentList.add(studentDao.findById(id));
			}
		}
		return studentList;
	}
	
	/**
	 * 批量删除一对多学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param otmClassId 一对多id
	 * @return
	 * @throws Exception 
	 */
	@Override
	public void deleteStudentInOtmClasss(String studentIds, String otmClassId) throws Exception {
		String[] stuIds=studentIds.split(",");
		for (String id : stuIds) {
			OtmClassStudent oneOtmClassStudent = otmClassStudentDao.getOneOtmClassStudent(otmClassId, id);
			if(oneOtmClassStudent != null)
			{
				otmClassStudentDao.delete(oneOtmClassStudent);
			}
			otmClassStudentAttendentDao.deleteOtmClassStudentAttendentByStudentAndOtmClass(id, otmClassId);
		}
	}
	
	/**
	 * 一对多课程详情
	 * @param otmClassId
	 * @param gridRequest
	 * @param modelVo
	 * @return
	 */
	@Override
	public DataPackage getOtmClassCourseDetail(String otmClassId, DataPackage dp,ModelVo modelVo) {
		dp = otmClassCourseDao.getOtmClassCourseDetail(otmClassId, dp, modelVo);
		List<OtmClassCourseVo> voList = HibernateUtils.voListMapping((List<OtmClassCourse>) dp.getDatas(), OtmClassCourseVo.class);
		List<OtmClassCourseVo> newList = new ArrayList<OtmClassCourseVo>();
		
		for(OtmClassCourseVo otmClassCourseVo: voList) {
			// 上课人数
			List<Criterion> criterionList = new ArrayList<Criterion>();
			criterionList.add(Expression.eq("otmClassCourse.otmClassCourseId", otmClassCourseVo.getOtmClassCourseId()));
			//		criterionList.add(Expression.eq("otmClassAttendanceStatus", OtmClassAttendanceStatus.CONPELETE));
			List<OtmClassAttendanceStatus> list = new ArrayList<>();
			list.add(OtmClassAttendanceStatus.CONPELETE);
			list.add(OtmClassAttendanceStatus.LATE);
			criterionList.add(Expression.in("otmClassAttendanceStatus", list));

			otmClassCourseVo.setCompleteClassPeopleNum(otmClassStudentAttendentDao.findCountByCriteria(criterionList));
			OtmClassStudentAttendentVo otmClassStudentAttendentVo = new OtmClassStudentAttendentVo();
			otmClassStudentAttendentVo.setOtmClassCourseId(otmClassCourseVo.getOtmClassCourseId());
			otmClassCourseVo.setStudyManagerName(this.getStudyManagerName(otmClassCourseVo.getOtmClassCourseId()));
			
			// todo 做好考勤扣费后再修改。
			// 扣费人数
			otmClassCourseVo.setChargedPeopleQuantity(accountChargeRecordsDao.getOtmClassCourseChargeStudentNum(otmClassCourseVo.getOtmClassCourseId()));
			
			// 扣费金额
			otmClassCourseVo.setChargedMoneyQuantity(accountChargeRecordsDao.getOtmClassConsumeFinance(otmClassCourseVo.getOtmClassCourseId()).doubleValue());
			
			newList.add(otmClassCourseVo);
		}
		dp.setDatas(newList);
		return dp;
	}
	
	/**
	 * 一对多详情-在读学生信息列表
	 * @param otmClassId
	 * @param gridRequest
	 * @return
	 */
	@Override
	public DataPackage getOtmClassDetailStudentList(String otmClassId, DataPackage dp) {
		dp = otmClassStudentDao.getOtmClassDetailStudentList(otmClassId, dp);
		List<OtmClassStudent> otmClassStudentList = (List<OtmClassStudent>) dp.getDatas();
		List<OtmClassStudentVo> newList = new ArrayList<OtmClassStudentVo>();
		for(OtmClassStudent otmClassStudent: otmClassStudentList) {
			OtmClassStudentVo otmClassStudentVo = this.findOtmClassStuRemainFinAndHour(otmClassStudent.getOtmClass().getOtmClassId(), otmClassStudent.getStudent().getId());
			newList.add(otmClassStudentVo);
		}
		dp.setDatas(newList);
		return dp;
	}
	
	/**
	 * 一对多详情-一对多学生剩余资金&剩余课时&累计扣费
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	@Override
	public OtmClassStudentVo findOtmClassStuRemainFinAndHour(String otmClassId, String studentId) {
		OtmClassStudent otmClassStudent = otmClassStudentDao.getOneOtmClassStudent(otmClassId, studentId);
		int otmType = otmClassStudent.getOtmClass().getOtmType();
		List<ContractProduct> cpList = contractproductDao.getOtmContractProductByOtmTypeAndStu(otmType, studentId);
		OtmClassStudentVo otmClassStudentVo = HibernateUtils.voObjectMapping(otmClassStudent, OtmClassStudentVo.class);
		BigDecimal surplusFinance = BigDecimal.ZERO; // 剩余资金
		BigDecimal surplusCourseHour = BigDecimal.ZERO;// 剩余课时
		BigDecimal realPrice = BigDecimal.ZERO; // 真实价格
		if (cpList != null && cpList.size() > 0) {
			for (ContractProduct cp : cpList) {
				surplusFinance = surplusFinance.add(cp.getRemainingAmount());
				if (cp.getDealDiscount().compareTo(BigDecimal.ZERO) > 0) {
					realPrice = cp.getPrice().multiply(cp.getDealDiscount());
				} else {
					realPrice  = cp.getPrice();
				}
				if (cp.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
					surplusCourseHour = surplusCourseHour.add(cp.getRemainingAmount().divide(realPrice, 2, RoundingMode.HALF_UP));
				}
			}
		} else {
//			throw new ApplicationException("一对多学生表ID为："+studentId+"的数据没有关联一对多的合同产品");
		}
		// todo 做完考勤扣费后
		BigDecimal consumeAmount = BigDecimal.ZERO;
		List<AccountChargeRecords> recordList = accountChargeRecordsDao.getAccountChargeRecordsByOtmClassAndStudent(otmClassId, otmClassStudentVo.getStudentId());
		for (AccountChargeRecords record : recordList) {
			consumeAmount = consumeAmount.add(record.getAmount());
		}
		otmClassStudentVo.setOtmClassSurplusFinance(surplusFinance);
		otmClassStudentVo.setOtmClassSurplusCourseHour(surplusCourseHour);
		otmClassStudentVo.setOtmClassTotalCharged(consumeAmount);
		return otmClassStudentVo;
	}
	
	/**
	 * 所有otmType一对多班级列表，用作Selection 
	 * @param otmType
	 * @return
	 */
	@Override
	public List<OtmClass> getOtmClassList4Selection(Integer otmType) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClass otm where 1=1 ");
		if(otmType != null && otmType > 0) {
			hql.append(" and otmType = :otmType ");
			params.put("otmType", otmType);
		}
		Organization organization=userService.getBelongCampus();
		hql.append("and blCampus.id in (select id from Organization where orgLevel like '"+organization.getOrgLevel()+"%')");
		List<OtmClass> list=otmClassDao.findAllByHQL(hql.toString(), params);
		return list;
	}
	
	/**
	 * 一对多学生转班 + 设置第一次上课日期
	 * @param oldOtmClassId
	 * @param newOtmClassId
	 * @param studentIds
	 * @param firstClassDate
	 * @return
	 */
	@Override
	public void stuChgOtmClassAndSetFistClassDate(String oldOtmClassId, String newOtmClassId, String studentIds, String firstClassDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		OtmClassStudent otmClassStudent = null;
		String[] stuIds = studentIds.split(",");
		this.allowAddStudent4OtmClass(newOtmClassId, stuIds.length);
		for(String id: stuIds){
			otmClassStudent = otmClassStudentDao.getOneOtmClassStudent(newOtmClassId, id);
			if (null != otmClassStudent) {
				Student student = studentDao.findById(id);
				throw new ApplicationException("学生" + student.getName() + "在目标一对多中已有记录！");
			}
			otmClassStudentAttendentDao.deleteOtmClassStudentAttendentByStudentAndOtmClass(id, oldOtmClassId);
			this.addOtmClassStudentAttendents(id, newOtmClassId, firstClassDate);
		}
		
		String hql=" update OtmClassStudent set otmClass.otmClassId = :newOtmClassId , firstSchoolTime = :firstClassDate where  " +
				"otmClass.otmClassId = :oldOtmClassId and student.id in(:stuIds) ";
		params.put("newOtmClassId", newOtmClassId);
		params.put("firstClassDate", firstClassDate);
		params.put("oldOtmClassId", oldOtmClassId);
		params.put("stuIds", stuIds);
		otmClassStudentDao.excuteHql(hql, params);
	}
	
	/**
	 * 一对多详情-学生详情
	 * @param otmClassId
	 * @param studentId
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOtmClassDetailStudentDetail(String otmClassId, String studentId, DataPackage dp) {
		OtmClassStudent otmClassStudent = otmClassStudentDao.getOneOtmClassStudent(otmClassId, studentId);
		if (otmClassStudent == null) {
			throw new ApplicationException("对应的一对多学生不存在！");
		}
		// 所有课程
		dp = otmClassCourseDao.getOtmClassCourseListByOtmClassId(otmClassId, otmClassStudent.getFirstSchoolTime(), dp);
		List<OtmClassCourseVo> voList = HibernateUtils.voListMapping((List<OtmClassCourseVo>) dp.getDatas(), OtmClassCourseVo.class);
		
		// 加入学生每节课程的考勤和扣费信息
		for (OtmClassCourseVo otmClassCourseVo:voList) {
			// 取考勤表数据
			OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao.getOneOtmClassStudentAttendent(otmClassCourseVo.getOtmClassCourseId(), studentId);
			if (otmClassStudentAttendent != null) {
				if (otmClassStudentAttendent.getOtmClassAttendanceStatus() != null) {
					otmClassCourseVo.setOtmClassAttendanceStatus(otmClassStudentAttendent.getOtmClassAttendanceStatus().getName());// 设置考勤状态
				}
				if (otmClassStudentAttendent.getChargeStatus() != null) {
					otmClassCourseVo.setChargeStatus(otmClassStudentAttendent.getChargeStatus().getName());// 设置扣费状态
				}
			}
			otmClassCourseVo.setStudyManagerName(this.getStudyManagerName(otmClassCourseVo.getOtmClassCourseId()));
			
			// todo 做完考勤扣费再做
			// 取扣费记录表数据
			List<AccountChargeRecords> accountChargeRecordList = accountChargeRecordsDao.getOtmClassCourseRecordByStudentId(otmClassCourseVo.getOtmClassCourseId(), studentId);

			if (accountChargeRecordList!=null){
				Double allAmount =0.0;
				for (AccountChargeRecords accountChargeRecords : accountChargeRecordList){
					if (accountChargeRecords != null) {
						if (accountChargeRecords.getAmount() != null) {
							allAmount += accountChargeRecords.getAmount().doubleValue();
						}
					}
				}
				otmClassCourseVo.setChargedMoneyQuantity(allAmount);// 设置扣费金额
			}


		}
		
		dp.setDatas(voList);
		return dp;
	}
	
	/**
	 * 一对多课时信息 
	 * @param otmClassId
	 * @return
	 */
	@Override
	public Map getOtmClassCourseTimeAnalyze(String otmClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ifnull(oc.TOTAL_CLASS_HOURS,0) as TOTAL_CLASS_HOURS, ifnull(sum(oc.COURSE_HOURS),0) as ARRANGED_COURSE_HOURS, (oc.TOTAL_CLASS_HOURS - ifnull(sum(occ.COURSE_HOURS),0)) AS NOT_ARRANG_COURSE_HOURS, ");
		sql.append(" SUM(CASE WHEN occ.COURSE_STATUS = 'CHARGED' THEN occ.COURSE_HOURS ELSE 0 END) AS CHARGED_COURSE_HOURS, ");
		sql.append(" SUM(CASE WHEN occ.COURSE_STATUS = 'NEW' THEN occ.COURSE_HOURS ELSE 0 END) AS NEW_COURSE_HOURS, ");
		sql.append(" SUM(CASE WHEN occ.COURSE_STATUS = 'TEACHER_ATTENDANCE' or occ.COURSE_STATUS = 'CHARGED' THEN occ.COURSE_HOURS ELSE 0 END) AS ATTENDANCE_COURSE_HOURS ");
		sql.append(" FROM otm_class oc LEFT JOIN otm_class_course occ on oc.OTM_CLASS_ID = occ.OTM_CLASS_ID ");
		sql.append(" WHERE oc.OTM_CLASS_ID = :otmClassId ");
		params.put("otmClassId", otmClassId);
		List<Map<Object, Object>> list = otmClassDao.findMapBySql(sql.toString(), params);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			throw new ApplicationException("没找到一对多ID为：" + otmClassId + "的课时信息");
		}
	}
	
	/**
	 * 保存一对多课程列表
	 * @param courseList
	 */
	public void saveOtmCourseList(OtmClassCourse[] otmClassCourseList) {
		User user = userService.getCurrentLoginUser();
		for (OtmClassCourse otmClassCourse : otmClassCourseList) {
			try{
				if(DateTools.getDateSpace(DateTools.getFistDayofMonth(),otmClassCourse.getCourseDate())<0){
					OtmClass oc=otmClassDao.findById(otmClassCourse.getOtmClass().getOtmClassId());
					odsMonthIncomeCampusService.isFinishAudit(oc.getBlCampus().getId(),DateTools.getDateToString(DateTools.getLastDayOfMonth(otmClassCourse.getCourseDate())));
				}
			}catch(ParseException e){
				e.printStackTrace();
			}

			otmClassCourse.setCreateTime(DateTools.getCurrentDateTime());
			otmClassCourse.setCreateUserId(user.getUserId());
			otmClassCourse.setModifyTime(DateTools.getCurrentDateTime());
			otmClassCourse.setModifyUserId(user.getUserId());
			otmClassCourse.setCourseStatus(CourseStatus.NEW);
			otmClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
			if (null == otmClassCourse.getGrade()
					|| null == otmClassCourse.getGrade().getId()
					|| "".equals(otmClassCourse.getGrade().getId())) {
				otmClassCourse.setGrade(null);
			}
			otmClassCourseDao.save(otmClassCourse);
			this.initOtmClassStudentAttendent(otmClassCourse);
		}
	}
	
	/**
	 * 初始化一对多考勤记录
	 * @param otmClassCourse
	 */
	public void initOtmClassStudentAttendent(OtmClassCourse otmClassCourse) {
		User user = userService.getCurrentLoginUser();
		OtmClassStudentVo otmClassStudentVo = new OtmClassStudentVo();
		otmClassStudentVo.setOtmClassId(otmClassCourse.getOtmClass().getOtmClassId());
		otmClassStudentVo.setFirstSchoolTime(otmClassCourse.getCourseDate());
		DataPackage dp = new DataPackage(0, 999);
		dp = otmClassStudentDao.getOtmClassStudentList(otmClassStudentVo, dp);
		List<OtmClassStudent> otmClassStudentList = (List<OtmClassStudent>) dp.getDatas();
		for (OtmClassStudent ocs: otmClassStudentList) {
			Student student = ocs.getStudent();
			User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(student.getId(), ocs.getOtmClass().getBlCampus().getId());
			OtmClassStudentAttendent otmClassStudentAttendent = new OtmClassStudentAttendent();
			otmClassStudentAttendent.setOtmClassCourse(otmClassCourse);
			otmClassStudentAttendent.setStudentId(student.getId());
			otmClassStudentAttendent.setOtmClassAttendanceStatus(OtmClassAttendanceStatus.NEW); // 未上课
			otmClassStudentAttendent.setChargeStatus(OtmClassStudentChargeStatus.UNCHARGE);// 未扣费
			otmClassStudentAttendent.setCreateUser(user);
			otmClassStudentAttendent.setCreateTime(DateTools.getCurrentDateTime());
			otmClassStudentAttendent.setStudyManager(studyManager); // 保存学管师
			otmClassStudentAttendentDao.save(otmClassStudentAttendent);
		}
	}
	
	/**
	 * 一对多课程列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 *//*
	public DataPackage getOtmClassCourseList(OtmClassCourseVo otmClassCourseVo, DataPackage dp) {
		dp = otmClassCourseDao.getOtmClassCourseList(otmClassCourseVo, dp);
		List<OtmClassCourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<OtmClassCourse>) dp.getDatas(), OtmClassCourseVo.class);
		List<OtmClassStudentAttendent> otmClassStudentAttendentList = null;
		int studentCount = 0;//报名的学生人数
		int attendanceCount =0;//考勤人数
		int crashInd=0; //冲突
		for(OtmClassCourseVo vo : searchResultVoList){			
			//查询参与考勤的人数
			otmClassStudentAttendentList=otmClassStudentAttendentDao.findByCriteria(Expression.eq("otmClassCourse.otmClassCourseId", vo.getOtmClassCourseId()));
			
			//报名的所有学生
			studentCount=otmClassStudentDao.findCountHql("select count(*) from OtmClassStudent where otmClass.otmClassId='"+vo.getOtmClassId()+"' and firstSchoolTime <= '" + vo.getCourseDate() + "' ");
			
			vo.setAuditStatusName(vo.getAuditStatus() == null ? null : vo.getAuditStatus().getName());
			vo.setStudentCount(studentCount);//报名的学生人数
			if(otmClassStudentAttendentList!=null){
				// 考勤人数
				attendanceCount=otmClassStudentAttendentList.size();
				vo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, leaveNumber=0, absentNumber=0, chargeNumber = 0,makeUp=0 ; // 未上课，已上课， 请假， 缺勤， 扣费 人数
				String studyManagerName = "";
				for(OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendentList){
					if (null != otmClassStudentAttendent.getStudyManager() && !studyManagerName.contains(otmClassStudentAttendent.getStudyManager().getName())) {
						studyManagerName += otmClassStudentAttendent.getStudyManager().getName() + ",";
					}
					
					if(OtmClassStudentChargeStatus.CHARGED.equals(otmClassStudentAttendent.getChargeStatus()))
						chargeNumber++;
					if(OtmClassAttendanceStatus.NEW.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						newNumber++;
					else if(OtmClassAttendanceStatus.CONPELETE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						completeNumber++;
					else if(OtmClassAttendanceStatus.LEAVE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						leaveNumber++;
					else if(OtmClassAttendanceStatus.ABSENT.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						absentNumber++;
//					else if(OtmClassAttendanceStatus.ABSENT.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus())
//							&& OtmClassAttendanceStatus.LEAVE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()) 
//							&& otmClassStudentAttendent.getSupplementDate() != null)
//						makeUp++; //补课人数   请假缺勤且补课日期不为空
				}
				if (studyManagerName.length() > 0) {
					studyManagerName = studyManagerName.substring(0, studyManagerName.length() - 1);
				}
				vo.setStudyManagerName(studyManagerName);
				vo.setDeductionCount(chargeNumber);// 扣费人数
				vo.setNewClassPeopleNum(newNumber); // 设置 未上课
				vo.setCompleteClassPeopleNum(completeNumber); // 已上课
				vo.setLeaveClassPeopleNum(leaveNumber); // 请假
				vo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				vo.setNoAttendanceCount(attendanceCount-completeNumber-leaveNumber-absentNumber); // 未考勤
//				vo.setNoAttendanceCount(studentCount-attendanceCount); // 未考勤
//				vo.setMakeUp(makeUp);//补课
			}else{
				vo.setAttendanceCount(0);//考勤人数
				vo.setDeductionCount(0);// 扣费人数
				vo.setNewClassPeopleNum(0); // 设置 未上课
				vo.setCompleteClassPeopleNum(0); // 已上课
				vo.setLeaveClassPeopleNum(0); // 请假
				vo.setAbsentClassPeopleNum(0); // 缺勤
				vo.setNoAttendanceCount(studentCount); // 未考勤
//				vo.setMakeUp(0);
			}
						
			//查询学生课表冲突
			crashInd = courseConflictDao.countDistinctConflicts(
					vo.getOtmClassCourseId(), otmClassCourseVo.getStudentId(),
					otmClassCourseVo.getTeacherId(), vo.getCourseDate(),
					vo.getCourseTime().toString() + " - " + vo.getCourseEndTime().toString());
			vo.setCrashInd(crashInd);
			
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}*/
	
	/**
	 * 一对多课程列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseList(OtmClassCourseVo otmClassCourseVo, DataPackage dp) {
		dp = otmClassCourseDao.getOtmClassCourseList(otmClassCourseVo, dp);
		List<OtmClassCourseVo> searchResultVoList = HibernateUtils.voListMapping((List<OtmClassCourse>) dp.getDatas(), OtmClassCourseVo.class);
		List<OtmClassStudentAttendent> otmClassStudentAttendentList = null;
		int studentCount = 0;//报名的学生人数
		int attendanceCount =0;//考勤人数
		int crashInd=0; //冲突
		for(OtmClassCourseVo vo : searchResultVoList){
			if (StringUtil.isBlank(vo.getBlCampusName()) && StringUtil.isNotBlank(vo.getOtmClassId())) {
				OtmClass oc = otmClassDao.findById(vo.getOtmClassId());
				vo.setBlCampusName(oc.getBlCampus().getName());
			}
			
			if (StringUtil.isBlank(vo.getGradeName()) && StringUtil.isNotBlank(vo.getGradeId())) {
				DataDict gradeDict = dataDictDao.findById(vo.getGradeId());
				vo.setGradeName(gradeDict.getName());
			}
			
			if (StringUtil.isBlank(vo.getSubjectName()) && StringUtil.isNotBlank(vo.getSubjectId())) {
				DataDict subjectDict = dataDictDao.findById(vo.getSubjectId());
				vo.setSubjectName(subjectDict.getName());
			}
			
			if ((StringUtil.isBlank(vo.getTeacherName()) || StringUtil.isBlank(vo.getTeacherMobile())) && StringUtil.isNotBlank(vo.getTeacherId())) {
				User teacher = userDao.findById(vo.getTeacherId());
				vo.setTeacherName(teacher.getName());
				vo.setTeacherMobile(teacher.getContact());
			}
			
			vo.setCourseStatusName(vo.getCourseStatus() == null ? null : CourseStatus.valueOf(vo.getCourseStatus()).getName());
			
			vo.setAuditStatusName(vo.getAuditStatus() == null ? null : vo.getAuditStatus().getName());
			//查询参与考勤的人数
			otmClassStudentAttendentList=otmClassStudentAttendentDao.findByCriteria(Expression.eq("otmClassCourse.otmClassCourseId", vo.getOtmClassCourseId()));
//			otmClassStudentAttendentList=otmClassStudentAttendentDao.getOtmClassStudentAttendentListJdbc(vo.getOtmClassCourseId());
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("otmClassId", vo.getOtmClassId());
			params.put("courseDate", vo.getCourseDate());
			//报名的所有学生
			studentCount=otmClassDao.findCountSql("select count(*) from otm_class_student where OTM_CLASS_ID = :otmClassId and FIRST_SCHOOL_TIME <= :courseDate ", params);
			
			vo.setStudentCount(studentCount);//报名的学生人数
			if(otmClassStudentAttendentList!=null){
				// 考勤人数
				attendanceCount=otmClassStudentAttendentList.size();
				vo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, lateClassPeopleNum = 0, absentNumber=0, chargeNumber = 0,makeUp=0 ; // 未上课，已上课， 请假， 缺勤， 扣费 人数, leaveNumber=0
				String studyManagerName = "";
				for(OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendentList){
					if (null != otmClassStudentAttendent.getStudyManager() && !studyManagerName.contains(otmClassStudentAttendent.getStudyManager().getName())) {
						studyManagerName += otmClassStudentAttendent.getStudyManager().getName() + ",";
					}
					
					if(OtmClassStudentChargeStatus.CHARGED.equals(otmClassStudentAttendent.getChargeStatus()))
						chargeNumber++;
					if(OtmClassAttendanceStatus.NEW.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						newNumber++;
					else if(OtmClassAttendanceStatus.CONPELETE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						completeNumber++;
//					else if(OtmClassAttendanceStatus.LEAVE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
//						leaveNumber++;
					else if (OtmClassAttendanceStatus.LATE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						lateClassPeopleNum++;
					else if(OtmClassAttendanceStatus.ABSENT.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						absentNumber++;
				}
				if (studyManagerName.length() > 0) {
					studyManagerName = studyManagerName.substring(0, studyManagerName.length() - 1);
				}
				vo.setStudyManagerName(studyManagerName);
				vo.setDeductionCount(chargeNumber);// 扣费人数
				vo.setNewClassPeopleNum(newNumber); // 设置 未上课
				vo.setCompleteClassPeopleNum(completeNumber); // 已准时上课
				vo.setLateClassPeopleNum(lateClassPeopleNum); //迟到
//				vo.setLeaveClassPeopleNum(leaveNumber); // 请假

				vo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				vo.setNoAttendanceCount(attendanceCount-completeNumber-lateClassPeopleNum-absentNumber); // 未考勤
			}else{
				vo.setAttendanceCount(0);//考勤人数
				vo.setDeductionCount(0);// 扣费人数
				vo.setNewClassPeopleNum(0); // 设置 未上课
				vo.setCompleteClassPeopleNum(0); // 已上课
//				vo.setLeaveClassPeopleNum(0); // 请假
				vo.setAbsentClassPeopleNum(0); // 缺勤
				vo.setLateClassPeopleNum(0); //迟到
				vo.setNoAttendanceCount(studentCount); // 未考勤
				vo.setLateClassPeopleNum(0);
			}
			//查询学生课表冲突
			crashInd = courseConflictDao.findCourseConflictCount(
					vo.getOtmClassCourseId(), otmClassCourseVo.getStudentId(),
					vo.getTeacherId());
			/*crashInd = courseConflictDao.countDistinctConflicts(
					vo.getOtmClassCourseId(), otmClassCourseVo.getStudentId(),
					otmClassCourseVo.getTeacherId(), vo.getCourseDate(),
					vo.getCourseTime().toString() + " - " + vo.getCourseEndTime().toString());*/
			vo.setCrashInd(crashInd);
			int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(vo.getOtmClassCourseId(), ProductType.ONE_ON_MANY);
			if (count > 0) {
				vo.setIsWashed("TRUE");
			} else {
				vo.setIsWashed("FALSE");
			}
			
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}
	
	/**
	 * 一对多考勤前判断
	 * @param otmClassCourseId
	 */
	public void otmClassCourseBeforAttendance(String otmClassCourseId) {
		this.checkOtmClassCourseConflict(otmClassCourseId);
	}
	
	
	/**
	 * 一对多考勤前判断
	 * @param otmClassCourseId
	 */
	public void otmClassCourseBeforAttendance(List<OtmClassStudentAttendentVo> otmStudent,String teacherId,String courseId) {
		this.checkOtmClassCourseConflict(courseId);
		/*for (OtmClassStudentAttendentVo ocsa : otmStudent) {
			List<CourseConflict> conflictList= courseConflictDao.findCourseConflictList(courseId,ocsa.getStudentId(), teacherId);
			if(conflictList.size()>0){
				String teacherName = "";
				String studentName = "";
				if(conflictList!=null && conflictList.size()>1){
					for(CourseConflict c:conflictList){
						if(courseId.equals(c.getCourseId())){
							teacherName=c.getTeacher().getName();
							studentName = c.getStudent().getName();
						}
					}
					throw new ApplicationException("课程存在以下学生老师冲突，请移步学生老师课程表确认并修改后再考勤。老师：" + teacherName + ", 学生：" + studentName);
				}
			}
		}*/
		
	}
	
	private void checkOtmClassCourseConflict(String otmClassCourseId) {
		if(StringUtil.isNotBlank(otmClassCourseId)){
			OtmClassCourse otmCourse = otmClassCourseDao.findById(otmClassCourseId);					
			OtmClassStudentAttendentVo vo = new OtmClassStudentAttendentVo();
			vo.setOtmClassCourseId(otmClassCourseId);
			DataPackage dp = new DataPackage(0,999);
			dp = otmClassStudentAttendentDao.getOtmClassStudentAttendentList(vo, dp);
			List<OtmClassStudentAttendent> occsList= (List<OtmClassStudentAttendent>) dp.getDatas();
			String teacherName = "";
			String studentName = "";
			boolean isConflict = false;
			for (OtmClassStudentAttendent ocsa : occsList) {
				int count = courseConflictDao.findCourseConflictCount(otmClassCourseId,ocsa.getStudentId(), otmCourse.getTeacher().getUserId());
				if (count > 0) {
					isConflict = true;
					teacherName = otmCourse.getTeacher().getName();
					studentName += studentDao.findById(ocsa.getStudentId()).getName();
				}
			}
			if (isConflict) {
				throw new ApplicationException("课程存在以下学生老师冲突，请移步学生老师课程表确认并修改后再考勤。老师：" + teacherName + ", 学生：" + studentName);
			}
		}
	}
	
	/**
	 * 获取一对多学生考勤列表
	 * @param otmClassStudentVo
	 * @param oprationCode
	 * @param dp
	 * @return
	 * @throws Exception 
	 */
	public DataPackage getOtmClassStudentAttendentList(OtmClassStudentAttendentVo otmClassStudentAttendentVo, String oprationCode, DataPackage dp) throws Exception {
		OtmClassCourse currOtmClassCourse =  otmClassCourseDao.findById(otmClassStudentAttendentVo.getOtmClassCourseId());
//		OtmClassStudentVo otmClassStudentVo = new OtmClassStudentVo();
//		otmClassStudentVo.setOtmClassId(currOtmClassCourse.getOtmClass().getOtmClassId());
//		otmClassStudentVo.setFirstSchoolTime(currOtmClassCourse.getCourseDate());
		dp = otmClassStudentDao.getOtmClassStudentAttendentList(otmClassStudentAttendentVo, dp);
		
		List<OtmClassStudentVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<OtmClassStudent>) dp.getDatas(), OtmClassStudentVo.class);
		
		// 过滤未报名的学生
//		searchResultVoList = filterNoSignStudent(currMiniClassCourse, searchResultVoList);
		
		for (OtmClassStudentVo eachVo:searchResultVoList){
			
			eachVo.setStudentName(studentDao.findById(eachVo.getStudentId()).getName());
			
			OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao.getOneOtmClassStudentAttendent(otmClassStudentAttendentVo.getOtmClassCourseId(), eachVo.getStudentId());
			eachVo.setCourseDate(currOtmClassCourse.getCourseDate());// 设置考勤的课程日期
			
			// 设置考勤状态
			if (otmClassStudentAttendent != null) {
				eachVo.setOtmClassAttendanceStatus(otmClassStudentAttendent.getOtmClassAttendanceStatus().getValue());
				eachVo.setOtmClassStudentChargeStatus(otmClassStudentAttendent.getChargeStatus().getValue());
				if (null != otmClassStudentAttendent.getStudyManager()) {
					eachVo.setStudyManagerName(otmClassStudentAttendent.getStudyManager().getName());
				}
			} else {// 没有考勤记录的情况，初始化考勤状态为“未考勤”
				eachVo.setOtmClassAttendanceStatus(OtmClassAttendanceStatus.NEW.getValue());
				eachVo.setOtmClassStudentChargeStatus(OtmClassStudentChargeStatus.UNCHARGE.getValue());
			}
			
			// 设置老师是否考勤状态
			if (otmClassStudentAttendent != null && otmClassStudentAttendent.getHasTeacherAttendance ()!= null) {// 设置老师是否已考勤字段
				eachVo.setHasTeacherAttendance(otmClassStudentAttendent.getHasTeacherAttendance());
			} else {
				eachVo.setHasTeacherAttendance(BaseStatus.FALSE);
			}
			
			// 设置剩余金额
			BigDecimal remainAmount = contractproductDao.countOtmStudentRemainAmount(otmClassStudentAttendent);
			eachVo.setOtmClassSurplusFinance(remainAmount);
			
			if ("charge".equals(oprationCode)) {
				String orgId = currOtmClassCourse.getOtmClass().getBlCampus().getId();
				User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(eachVo.getStudentId(), orgId);
				if (studyManager != null && userService.getCurrentLoginUser().getUserId().equals(studyManager.getUserId())) {
					eachVo.setCanCharge(BaseStatus.TRUE);
				} else {
					eachVo.setCanCharge(BaseStatus.FALSE);
				}
			}
			
			Student stu=studentDao.findById(eachVo.getStudentId());
			eachVo.setStuStatus(stu.getStatus()== null ? null : stu.getStatus());
		}
		
//		List<OtmClassStudentVo> zeroList = new ArrayList<OtmClassStudentVo>();
//		for (OtmClassStudentVo eachVo:searchResultVoList) {
//			
//			if (eachVo.getContractProductRemainingAmount().compareTo(BigDecimal.ZERO) == 0 
//					&& eachVo.getMiniClassAttendanceStatus() == MiniClassAttendanceStatus.NEW.getValue()) {
//				zeroList.add(eachVo);
//			} else {
//				returnList.add( eachVo);
//			}
//		}
//		returnList.addAll(zeroList);
		
		dp.setDatas(searchResultVoList);
//		dp.setRowCount(searchResultVoList.size());
		return dp;
	}
	
	/**
	 * 根据id获取一对多课程
	 * @param vo
	 * @return
	 */
	public OtmClassCourseVo findOtmClassCourseById(OtmClassCourseVo vo) {
		OtmClassCourse otmClassCourse= otmClassCourseDao.findById(vo.getOtmClassCourseId());
		OtmClassCourseVo otmClassCourseVo = HibernateUtils.voObjectMapping(otmClassCourse, OtmClassCourseVo.class);
		if(otmClassCourseVo!=null){		
			//查询参与考勤的人数
			List<OtmClassStudentAttendent> otmClassStudentAttendentList=otmClassStudentAttendentDao.findByCriteria(Expression.eq("otmClassCourse.otmClassCourseId", otmClassCourseVo.getOtmClassCourseId()));
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("otmClassId", otmClassCourseVo.getOtmClassId());
			params.put("firstSchoolTime", otmClassCourseVo.getCourseDate());
			//报名的所有学生
			int studentCount=otmClassStudentDao.findCountHql("select count(*) from OtmClassStudent where otmClass.otmClassId = :otmClassId and firstSchoolTime <= :firstSchoolTime ", params);
				
			otmClassCourseVo.setStudentCount(studentCount);//报名的学生人数
			if(otmClassStudentAttendentList!=null && otmClassStudentAttendentList.size() > 0){
				User currentUser = userService.getCurrentLoginUser();
				boolean isStudyCharged = false;
				List<StudyManagerMobileVo> studyManagerMobileVos = new ArrayList<StudyManagerMobileVo>();
				StudyManagerMobileVo studyManagerMobileVo = null;
				// 考勤人数
				int attendanceCount=otmClassStudentAttendentList.size();//考勤人数
				otmClassCourseVo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, lateClassPeopleNum = 0, absentNumber=0, chargeNumber = 0; // 未上课，已准时上课， 迟到， 缺勤， 扣费 人数 , leaveNumber=0
				for(OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendentList){
					User studyManager = otmClassStudentAttendent.getStudyManager();
					if (null != studyManager) {
						if (currentUser.getUserId().equals(studyManager.getUserId()) 
								&& otmClassStudentAttendent.getChargeStatus() == OtmClassStudentChargeStatus.CHARGED) {
							isStudyCharged = true;
						}
						studyManagerMobileVo = new StudyManagerMobileVo();
						studyManagerMobileVo.setId(studyManager.getUserId());
						studyManagerMobileVo.setName(studyManager.getName());
						studyManagerMobileVo.setContact(studyManager.getContact());
						studyManagerMobileVos.add(studyManagerMobileVo);
					}
					if(OtmClassStudentChargeStatus.CHARGED.equals(otmClassStudentAttendent.getChargeStatus()))
						chargeNumber++;
					if(OtmClassAttendanceStatus.NEW.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						newNumber++;
					else if(OtmClassAttendanceStatus.CONPELETE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						completeNumber++;
					else if (OtmClassAttendanceStatus.LATE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						lateClassPeopleNum++;
//					else if(OtmClassAttendanceStatus.LEAVE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
//						leaveNumber++;
					else if(OtmClassAttendanceStatus.ABSENT.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						absentNumber++;
					
				}
				otmClassCourseVo.setStudyManagerMobileVos(studyManagerMobileVos);
				otmClassCourseVo.setDeductionCount(chargeNumber);// 扣费人数
				otmClassCourseVo.setNewClassPeopleNum(newNumber); // 设置 未上课
				otmClassCourseVo.setCompleteClassPeopleNum(completeNumber+lateClassPeopleNum); // 已上课
//				otmClassCourseVo.setLeaveClassPeopleNum(leaveNumber); // 请假
				otmClassCourseVo.setLateClassPeopleNum(lateClassPeopleNum);
				otmClassCourseVo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				vo.setNoAttendanceCount(attendanceCount-completeNumber-lateClassPeopleNum-absentNumber); // 未考勤
				if (!isStudyCharged && vo.getCurrentRoleId() != null && vo.getCurrentRoleId().equals("classTeacherDeduction") 
						&& otmClassCourseVo.getCourseStatus().equals(CourseStatus.CHARGED.getValue())) {
					otmClassCourseVo.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE.getValue());
					otmClassCourseVo.setCourseStatusName(CourseStatus.TEACHER_ATTENDANCE.getName());
				}
			}else{
				otmClassCourseVo.setAttendanceCount(0);//考勤人数
				otmClassCourseVo.setDeductionCount(0);// 扣费人数
				otmClassCourseVo.setNewClassPeopleNum(0); // 设置 未上课
				otmClassCourseVo.setCompleteClassPeopleNum(0); // 已上课
//				otmClassCourseVo.setLeaveClassPeopleNum(0); // 请假
				otmClassCourseVo.setLateClassPeopleNum(0); //迟到
				otmClassCourseVo.setAbsentClassPeopleNum(0); // 缺勤
				otmClassCourseVo.setNoAttendanceCount(studentCount); // 未考勤
			}
		}else{
			otmClassCourseVo=new OtmClassCourseVo();
		}
		if (!otmClassCourseVo.getCourseStatus().equals(CourseStatus.NEW.getValue())) {
			MoneyWashRecords moneyWashRecords = moneyWashRecordsService.findMoneyWashRecordsByOtmCourseId(otmClassCourseVo.getOtmClassCourseId());
			if (moneyWashRecords != null) {
				otmClassCourseVo.setIsWashed("TRUE");
				otmClassCourseVo.setWashRemark(moneyWashRecords.getDetailReason());
			} else {
				otmClassCourseVo.setIsWashed("FALSE");
			}
		} else {
			otmClassCourseVo.setIsWashed("FALSE");
		}
		return otmClassCourseVo;
		
	}
	
	/**
	 * 更新一对多学生考勤信息
	 * @param otmClassCourseId
	 * @param attendanceData
	 * @param oprationCode
	 * @return
	 * @throws Exception 
	 */
	public Response modifyOtmClassCourseStudentAttendance(String otmClassCourseId, String attendanceData, String oprationCode) throws Exception {
		Response res = new Response();
		// 解析JSON
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, OtmClassStudentAttendentVo.class);
		List<OtmClassStudentAttendentVo> inputList =  (List<OtmClassStudentAttendentVo>)objectMapper.readValue(attendanceData, javaType);
		if(inputList.size()<=0){
			res.setResultCode(-2);
			res.setResultMessage("请选择要考勤学生的记录！");
			return res;
		}
		// 是否有操作权限
		OtmClassCourse otmClassCourse = otmClassCourseDao.findById(otmClassCourseId);
		
		if(otmClassCourse.getAuditStatus()!=null && otmClassCourse.getAuditStatus() == AuditStatus.UNVALIDATE){
			otmClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
		}
		if ("attendance".equals(oprationCode)) {
			permission4OtmClassCourseAttendanceOperation(otmClassCourse.getTeacher().getUserId(), "", oprationCode);
//			this.otmClassCourseBeforAttendance(otmClassCourseId);
			this.otmClassCourseBeforAttendance(inputList, otmClassCourse.getTeacher().getUserId(), otmClassCourseId);
		}
		
		String stuName="";
 		for (int i = 0; i < inputList.size(); i++) {
			OtmClassStudentAttendentVo eachInputStuAttendentVo = inputList.get(i);			
 			// 一对多扣费
			if ("charge".equals(oprationCode)) {
				Student student = studentDao.findById(eachInputStuAttendentVo.getStudentId());
				User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(eachInputStuAttendentVo.getStudentId(), otmClassCourse.getOtmClass().getBlCampus().getId());
				String studyManagerId = studyManager != null ? studyManager.getUserId() : "";
				permission4OtmClassCourseAttendanceOperation(otmClassCourse.getTeacher().getUserId(), studyManagerId, oprationCode);
				
				// 获取 targe Contract product list
				List<ContractProduct> cpList = contractproductDao.getOtmContractProductByOtmTypeAndStu(otmClassCourse.getOtmClass().getOtmType(), eachInputStuAttendentVo.getStudentId());
				Double courseHour  = otmClassCourse.getCourseHours();
				BigDecimal lastDetel = null;
				for (ContractProduct cp : cpList) {
					if(cp!=null && (cp.getStatus() == ContractProductStatus.STARTED
							||	cp.getStatus() == ContractProductStatus.NORMAL)) {
						BigDecimal remainingAmount = cp.getRemainingAmount();
						// 旧数据 可能会是 price 价格为 0
						BigDecimal price = cp.getPrice() == null? BigDecimal.ZERO:cp.getPrice();
						BigDecimal detel = BigDecimal.ZERO;
						if (cp.getDealDiscount().compareTo(BigDecimal.ZERO) > 0) {
							detel = price.multiply(cp.getDealDiscount()).multiply(BigDecimal.valueOf(courseHour));
						} else {
							detel = price.multiply(BigDecimal.valueOf(courseHour));
						}
						if (lastDetel == null) {
							lastDetel = detel;
						}
						if(remainingAmount.compareTo(lastDetel)>=0 && (cp.getStatus() == ContractProductStatus.NORMAL ||cp.getStatus() == ContractProductStatus.STARTED)) {
	//							updateContractAndStuViewForNewCharge(detel, new BigDecimal(courseHour) , targetConPrd );
	//							doAfterForNewCharge(detel, new BigDecimal(courseHour) , targetConPrd );
							lastDetel = BigDecimal.ZERO;
							break;
						} else {
							if (cp.getStatus() == ContractProductStatus.NORMAL ||cp.getStatus() == ContractProductStatus.STARTED) {
								lastDetel = lastDetel.subtract(remainingAmount);
							}
							
						}
					}
				}
				if (lastDetel != null && lastDetel.compareTo(BigDecimal.ZERO) > 0) {
					stuName+=student.getName()+",";
				}
				

				if(student.getStatus() != null && student.getStatus()==StudentStatus.GRADUATION){
					String errMsg = "学生（"+student.getName()+"）状态为“毕业”，相关课程操作已被禁止，请核实后再继续操作。";
					throw new ApplicationException(errMsg);
				}

			}
		}
 	 		
 		if(StringUtils.isNotBlank(stuName)){
				throw new ApplicationException(""+stuName+" 剩余课时不足以执行本次扣费，请续费后再重新扣费");
 		}else{
 			// 循环设每个学生考勤
 	 		for (int i = 0; i < inputList.size(); i++) {
 				OtmClassStudentAttendentVo eachInputStuAttendentVo = inputList.get(i);
 				
// 				Student student = studentDao.findById(eachInputStuAttendentVo.getStudentId());
 				User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(eachInputStuAttendentVo.getStudentId(), otmClassCourse.getOtmClass().getBlCampus().getId());
				String studyManagerId = studyManager != null ? studyManager.getUserId() : "";
 				// 更新考勤记录
 				updateOtmClassAttendanceRecord(otmClassCourseId, eachInputStuAttendentVo, oprationCode, studyManagerId);
 				
 				// 一对多扣费
 				if ("charge".equals(oprationCode)) {
 					boolean chargeResult = otmClassCourseCharge(otmClassCourseId, eachInputStuAttendentVo, studyManagerId);
 					// 更新一对多状态
 					if(chargeResult && (i == inputList.size() - 1)) {
 						updateOtmClassStatus(otmClassCourseId);
 					}
 				}
 			}

			if ("charge".equals(oprationCode)) {
				for (OtmClassStudentAttendentVo input : inputList) {
					//学生状态处理
					PushRedisMessageUtil.pushStudentToMsg(input.getStudentId());
				}
			}

		}
		return res;
	}
	
	/**
	 * 一对多详情-主界面信息
	 * @param otmClassId
	 * @return
	 */
	public OtmClassCourseVo getOtmClassDetailAttChaPageInfo(String otmClassCourseId) {
		OtmClassCourse otmClassCourse =otmClassCourseDao.findById(otmClassCourseId);
		if(otmClassCourse == null)
		{
			throw new ApplicationException("获取不到对应的一对多课程数据！");
		}
		OtmClassCourseVo otmClassCourseVo = HibernateUtils.voObjectMapping(otmClassCourse, OtmClassCourseVo.class);
		otmClassCourseVo.setStudyManagerName(this.getStudyManagerName(otmClassCourseVo.getOtmClassCourseId()));
		// 一对多课程状态
		if (StringUtils.isNotBlank(otmClassCourseVo.getCourseStatus())) {
			CourseStatus courseStatus = CourseStatus.valueOf(otmClassCourseVo.getCourseStatus());
			otmClassCourseVo.setCourseStatus(courseStatus.getName());
		}
		
		// 一对多课程上课人数
		int otmClassCourseStudentNum = this.getOtmClassCourseStudentNum(otmClassCourseId);
		otmClassCourseVo.setOtmClassCourseStudentNum(otmClassCourseStudentNum);
		return otmClassCourseVo;
	}
	
	/**
	 * 一对多扣费
	 * @param otmClassCourseId
	 * @param eachInputStuAttendentVo
	 * @throws Exception
	 */
	@Override
	public boolean otmClassCourseCharge(String otmClassCourseId, OtmClassStudentAttendentVo eachInputStuAttendentVo, String studyManagerId) throws Exception {
		OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao
				.getOneOtmClassStudentAttendent(otmClassCourseId,
						eachInputStuAttendentVo.getStudentId());// 已有学生考勤记录
		
		if (null == otmClassStudentAttendent) {
			throw new RuntimeException("该学生考勤记录为空，请检查！");
		}
		if (otmClassStudentAttendent.getHasTeacherAttendance() != BaseStatus.TRUE ) {
			throw new ApplicationException("老师未考勤，不允许扣费！");
//			throw new RuntimeException("老师未考勤，不允许扣费(请检查老师是否已考勤字段)！");
		}
		if (!otmClassCourseHasStuAttendentRecord(otmClassStudentAttendent.getId())) {
			throw new RuntimeException("未有该学生的考勤记录，不允许扣费！");// 数据库里是否已经有考勤记录, 已有考勤记录才允许扣费
		}
		
		// 如果是班主任（学管）角色，才执行扣费操作
		if ((userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER)
				||  userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER_HEAD)) 
				&& userService.getCurrentLoginUser().getUserId().equals(studyManagerId) ) {
				// 一对多扣费
			if (chargeService.chargeOtmClass(otmClassStudentAttendent.getId(), userService.getCurrentLoginUser())
					&& OtmClassStudentChargeStatus.UNCHARGE.equals(otmClassStudentAttendent.getChargeStatus())) {
				otmClassStudentAttendent.setChargeStatus(OtmClassStudentChargeStatus.CHARGED);
				return true;
			} else {
				throw new ApplicationException(ErrorCode.OTM_CLASS_MONEY_NOT_ENOUGH);
			}
		}
		
		return false;
	}
	
	/**
	 * 是否已经有该学生的考勤记录
	 * @param otmClassStudentAttendentId
	 * @return
	 */
	@Override
	public boolean otmClassCourseHasStuAttendentRecord(
			String otmClassStudentAttendentId) {
		boolean hasAttendentRecord = false;// 是否已有考勤记录
		OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao.findById(otmClassStudentAttendentId);
		if (otmClassStudentAttendent.getOtmClassAttendanceStatus()  != null) {
			hasAttendentRecord = true;// 已有考勤记录
		} else {
			hasAttendentRecord = false;// 未有考勤记录
		}
		return hasAttendentRecord;
	}
	
	/**
	 * 更新一对多班级状态
	 * @param otmClassCourseId
	 */
	@Override
	public void updateOtmClassStatus(String otmClassCourseId) {
		OtmClassCourse otmClassCourse = otmClassCourseDao.findById(otmClassCourseId);
		OtmClass otmClass = otmClassDao.findById((otmClassCourse.getOtmClass().getOtmClassId()));
		
		if (null == otmClass.getConsume()) {
			otmClass.setConsume(0.0);
		}
		
		//设置扣费时间
		otmClassCourse.setStudyManageChargeTime(DateTools.getCurrentDateTime());
		
		// 第一次上课则更新一对多状态
		if(otmClass.getConsume() == 0){
			otmClass.setStatus(OtmClassStatus.STARTED);
		}
		if(otmClassCourse.getCourseStatus()!=null && otmClassCourse.getCourseStatus().equals(CourseStatus.TEACHER_ATTENDANCE)) {
			otmClassCourse.setCourseStatus(CourseStatus.CHARGED);
			otmClassCourseDao.save(otmClassCourse);
			otmClassCourseDao.flush();
		}
		
		// 更新已上课时
		otmClass.setConsume(otmClassStudentAttendentDao.findConsumeCount(otmClassCourse));
		
//		// 最后一次上课  一对多是固定课时且消耗课时等于总课时数 结班
//		if(OtmClassCourseType.CONSTANT.equals(miniClass.getMiniClassCourseType()) && miniClass.getConsume().equals(miniClass.getTotalClassHours())){
//			miniClass.setStatus(MiniClassStatus.CONPELETE);
//		}
		otmClassDao.save(otmClass);
	}
	
	/**
	 * 一对多课程上课人数
	 * @param otmClassCourseId
	 * @return
	 */
	public int getOtmClassCourseStudentNum(String otmClassCourseId) {
		// 上课人数
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("otmClassCourse.otmClassCourseId", otmClassCourseId));
//		criterionList.add(Expression.eq("otmClassAttendanceStatus", OtmClassAttendanceStatus.CONPELETE));
		List<OtmClassAttendanceStatus> list = new ArrayList<>();
		list.add(OtmClassAttendanceStatus.CONPELETE);
		list.add(OtmClassAttendanceStatus.LATE);
		criterionList.add(Expression.in("otmClassAttendanceStatus", list));
		int otmClassCourseStudentNum = otmClassStudentAttendentDao.findCountByCriteria(criterionList);
		return otmClassCourseStudentNum;
	}
	
	/**
	 * 一对多详情-一对多考勤扣费详情
	 * @param otmClassStudentVo
	 * @return
	 * @throws Exception 
	 */
	@Override
	public DataPackage getOtmClassAttChargedDetail(OtmClassStudentAttendentVo otmClassStudentAttendentVo, DataPackage dp) {
		OtmClassCourse currOtmClassCourse =  otmClassCourseDao.findById(otmClassStudentAttendentVo.getOtmClassCourseId());
		OtmClassStudentVo otmClassStudentVo = new OtmClassStudentVo();
		otmClassStudentVo.setOtmClassId(currOtmClassCourse.getOtmClass().getOtmClassId());
		otmClassStudentVo.setFirstSchoolTime(currOtmClassCourse.getCourseDate());
		dp = otmClassStudentDao.getOtmClassStudentListUncharge(otmClassStudentVo, dp);//查找未扣费学生
		if(dp == null)
		{
			throw new ApplicationException("获取不到未扣费学生信息");
		}
		List<OtmClassStudentVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<OtmClassStudent>) dp.getDatas(), OtmClassStudentVo.class);
		List<AccountChargeRecordsVo> acrVoList = HibernateUtils.voListMapping(
				accountChargeRecordsDao
						.getOtmClassCourseChargeStudentList(otmClassStudentAttendentVo
								.getOtmClassCourseId()),
				AccountChargeRecordsVo.class);
		
		String studentIDsString="";
		for (OtmClassStudentVo ocsv : searchResultVoList) {
			studentIDsString+=ocsv.getStudentId()+",";
		}
		
		int count = dp.getRowCount();
		
		for (AccountChargeRecordsVo accountChargeRecordsVo : acrVoList) {
			if(studentIDsString.contains(accountChargeRecordsVo.getStudentId())){
				continue;
			}
			OtmClassStudentVo ocsVo = new OtmClassStudentVo();
			ocsVo.setStudentId(accountChargeRecordsVo.getStudentId());
			ocsVo.setStudentName(accountChargeRecordsVo.getStudentName());
			searchResultVoList.add(ocsVo);
			count++;
		}
		
		
		// 过滤未报名的学生
		//searchResultVoList = filterNoSignStudent(currMiniClassCourse, searchResultVoList);
		
		for (OtmClassStudentVo eachVo:searchResultVoList){
			OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao.getOneOtmClassStudentAttendent(otmClassStudentAttendentVo.getOtmClassCourseId(), eachVo.getStudentId());
			eachVo.setCourseDate(currOtmClassCourse.getCourseDate());// 设置考勤的课程日期
			// 设置考勤状态
			if (otmClassStudentAttendent != null) {
				eachVo.setOtmClassAttendanceStatus(otmClassStudentAttendent.getOtmClassAttendanceStatus().getValue());
				eachVo.setOtmClassStudentChargeStatus(otmClassStudentAttendent.getChargeStatus().getValue());
			} else {// 没有考勤记录的情况，初始化考勤状态为“未考勤”
				eachVo.setOtmClassAttendanceStatus(OtmClassAttendanceStatus.NEW.getValue());
				eachVo.setOtmClassStudentChargeStatus(OtmClassStudentChargeStatus.UNCHARGE.getValue());
			}
			
			// 设置老师是否考勤状态
			if (otmClassStudentAttendent != null && otmClassStudentAttendent.getHasTeacherAttendance ()!= null) {// 设置老师是否已考勤字段
				eachVo.setHasTeacherAttendance(otmClassStudentAttendent.getHasTeacherAttendance());
			} else {
				eachVo.setHasTeacherAttendance(BaseStatus.FALSE);
			}
			
		}
		
		dp.setDatas(searchResultVoList);
		dp.setRowCount(count);
		return dp;
	}
	
	/**
	 * 修改一对多课程信息
	 * @param otmClassCourseVo
	 * @throws Exception
	 */
	public void saveOrUpdateOtmClassCourse(OtmClassCourseVo otmClassCourseVo) throws Exception {
		OtmClassCourse otmClassCourse = otmClassCourseDao.findById(otmClassCourseVo.getOtmClassCourseId());

		//营收凭证如果已经审批完成就不能再排以前的课程了。
		try{
			if( ((otmClassCourseVo.getCancelStatus()!=null && !"cancel".equals(otmClassCourseVo.getCancelStatus())) || otmClassCourseVo.getCancelStatus() == null)
					&& DateTools.getDateSpace(DateTools.getFistDayofMonth(),otmClassCourseVo.getCourseDate())<0){
				odsMonthIncomeCampusService.isFinishAudit(otmClassCourse.getOtmClass().getBlCampus().getId(),DateTools.getDateToString(DateTools.getLastDayOfMonth(otmClassCourseVo.getCourseDate())));
			}
		}catch(ParseException e){
			e.printStackTrace();
		}


		//取消一对多课程
		if(otmClassCourseVo.getCancelStatus() != null && otmClassCourseVo.getCancelStatus().equals("cancel")){
			if(!otmClassCourse.getCourseStatus().equals(CourseStatus.NEW) && !otmClassCourse.getCourseStatus().equals(CourseStatus.TEACHER_ATTENDANCE)){
				throw new ApplicationException("只有“未上课”和“老师已考勤”状态的课程才允许取消!");
			}
			this.updateOtmclassCourseStatus(otmClassCourseVo.getOtmClassCourseId(), CourseStatus.CANCEL);
			return;
		}
		
		if(otmClassCourse.getCourseStatus()!=null && !otmClassCourse.getCourseStatus().equals(CourseStatus.NEW)){
			throw new ApplicationException("只有“未上课”的课程才允许修改!");
		}
		
		if (StringUtils.isNotBlank(otmClassCourseVo.getCourseDate())) {
			otmClassCourse.setCourseDate(otmClassCourseVo.getCourseDate());
			this.deleteOtmClassStudentAttendentByOtmClassCourse(otmClassCourse);
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getCourseTime())) {
			otmClassCourse.setCourseTime(otmClassCourseVo.getCourseTime());
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getCourseEndTime())) {
			otmClassCourse.setCourseEndTime(otmClassCourseVo.getCourseEndTime());
		}
		if (otmClassCourseVo.getCourseHours() != null) {
			otmClassCourse.setCourseHours(otmClassCourseVo.getCourseHours());
		}
		
		if (StringUtils.isNotBlank(otmClassCourseVo.getSubjectId())) {
			otmClassCourse.setSubject(new DataDict(otmClassCourseVo.getSubjectId()));
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getTeacherId())) {
			User teacher = new User();
			teacher.setUserId(otmClassCourseVo.getTeacherId());
			otmClassCourse.setTeacher(teacher);
		}
		
		otmClassCourse.setModifyTime(DateTools.getCurrentDateTime());
		User user = userService.getCurrentLoginUser();
		otmClassCourse.setModifyUserId(user.getUserId());
		if(otmClassCourse.getOtmClassCourseId()==null){
			otmClassCourse.setCreateTime(DateTools.getCurrentDateTime());
			otmClassCourse.setCreateUserId(user.getUserId());
		}
		if(otmClassCourse.getAuditStatus() == AuditStatus.UNVALIDATE){
			otmClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
		}
		otmClassCourseDao.save(otmClassCourse);
	}
	
	/**
	 * 更新一对多课程状态
	 * @author tangyuping
	 * @param otmClasscourseId
	 * @param courseStatus
	 */
	public void updateOtmclassCourseStatus(String otmClasscourseId, CourseStatus courseStatus){
		OtmClassCourse course = otmClassCourseDao.findById(otmClasscourseId);
		courseConflictDao.deleteCourseConflictByCourseId(otmClasscourseId);
		course.setCourseStatus(courseStatus);
		otmClassCourseDao.save(course);
	}
	
	/**
     * 删除报名时间在courseDate之前的未扣费的未上课的考勤记录
     * @param otmClassCourse
     */
	public void deleteOtmClassStudentAttendentByOtmClassCourse(OtmClassCourse otmClassCourse) {
		String hql=" delete OtmClassStudentAttendent where otmClassCourse.otmClassCourseId = :otmClassCourseId and otmClassAttendanceStatus='NEW'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("otmClassCourseId", otmClassCourse.getOtmClassCourseId());
		otmClassStudentDao.excuteHql(hql, params);
		User user = userService.getCurrentLoginUser();
		OtmClassStudentVo otmClassStudentVo = new OtmClassStudentVo();
		otmClassStudentVo.setOtmClassId(otmClassCourse.getOtmClass().getOtmClassId());
		otmClassStudentVo.setFirstSchoolTime(otmClassCourse.getCourseDate());
		DataPackage dp = new DataPackage(0, 999);
		dp = otmClassStudentDao.getOtmClassStudentList(otmClassStudentVo, dp);
		List<OtmClassStudent> otmClassStudentList = (List<OtmClassStudent>) dp.getDatas();
		for (OtmClassStudent ocs: otmClassStudentList) {
			User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(ocs.getStudent().getId(), otmClassCourse.getOtmClass().getBlCampus().getId());
			Student student = ocs.getStudent();
			OtmClassStudentAttendent otmClassStudentAttendent = new OtmClassStudentAttendent();
			otmClassStudentAttendent.setOtmClassCourse(otmClassCourse);
			otmClassStudentAttendent.setStudentId(student.getId());
			otmClassStudentAttendent.setOtmClassAttendanceStatus(OtmClassAttendanceStatus.NEW); // 未上课
			otmClassStudentAttendent.setChargeStatus(OtmClassStudentChargeStatus.UNCHARGE);// 未扣费
			otmClassStudentAttendent.setCreateUser(user);
			otmClassStudentAttendent.setCreateTime(DateTools.getCurrentDateTime());
			otmClassStudentAttendent.setStudyManager(studyManager);
			otmClassStudentAttendentDao.save(otmClassStudentAttendent);
		}
	}
	
	/**
	 * 删除一对多课程
	 * @param otmClassCourseId
	 */
	public void deleteOtmClassCourse(String otmClassCourseId) {
		for(String id : otmClassCourseId.split(",")){
			int count = accountChargeRecordsDao.countAccountChargeRecordsByOtmClassCourseId(id);
			if (count > 0) {
				throw new ApplicationException("此课程发生过扣费或者扣费冲销，不允许删除，请取消课程或者修改课程信息重新考勤扣费。");
			}
			otmClassStudentAttendentDao.deleteOtmClassStudentAttendent(id);
			otmClassCourseDao.delete(new OtmClassCourse(id));
		}
	}
	
	/**
	 * 更新一对多课程考勤中的学管师，只更新未扣费的
	 * @param studyManagerId
	 */
	public void updateOtmClassStudentAttendentStudyManager(String studyManagerId, String studentId,  String blCampusId) throws Exception {
		if (StringUtil.isBlank(studyManagerId) || StringUtil.isBlank(studentId) || StringUtil.isBlank(blCampusId)) {
			throw new ApplicationException("更新一对多课程的学管中出现错误！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " update otm_class_student_attendent ocsa left join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID"
				+ " left join otm_class oc on occ.OTM_CLASS_ID = oc.OTM_CLASS_ID set ocsa.STUDY_MANAGER_ID = :studyManagerId ";
		sql += " where 1=1 ";
		sql += " and ocsa.CHARGE_STATUS = 'UNCHARGE' and ocsa.STUDENT_ID = :studentId and oc.BL_CAMPUS_ID = :blCampusId ";
		params.put("studyManagerId", studyManagerId);
		params.put("studentId", studentId);
		params.put("blCampusId", blCampusId);
		otmClassDao.excuteSql(sql, params);
	}
	
	/**
	 * 更新考勤记录
	 * @param miniClassCourseId
	 * @param eachInputStuAttendentVo
	 * @param oprationCode
	 */
	@Override
	public void updateOtmClassAttendanceRecord(String otmClassCourseId, OtmClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode, String studyManagerId) {
		User user = userService.getCurrentLoginUser();
		
		// 根据用户角色更新一对多考勤记录
		OtmClassStudentAttendent otmClassStudentAttendent = updateOtmClassAttendanceRecordByUserRole(otmClassCourseId, eachInputStuAttendentVo, oprationCode, studyManagerId);
		
		// 修改人记录
		String currDateTime = DateTools.getCurrentDateTime();
		otmClassStudentAttendent.setModifyUser(user);
		otmClassStudentAttendent.setModifyTime(currDateTime);
		if (otmClassStudentAttendent.getId() == null) {
			otmClassStudentAttendent.setCreateUser(user);
			otmClassStudentAttendent.setCreateTime(currDateTime);			
		}		
		otmClassStudentAttendent.setAttendentUser(user);// 考勤操作人
		otmClassStudentAttendentDao.save(otmClassStudentAttendent);
		
		//课程表添加考勤时间,只取第一次考勤时间
		OtmClassCourse course = otmClassStudentAttendent.getOtmClassCourse();
		if(course.getFirstAttendTime() == null &&  !"charge".equals(oprationCode)){
			course.setFirstAttendTime(DateTools.getCurrentDateTime());
		}
		
//		if(otmClassStudentAttendent.getOtmClassAttendanceStatus().equals(MiniClassAttendanceStatus.LEAVE)){
//			//删除冲突表当前冲突记录
//			courseConflictDao.deleteCourseConflictByCourseIdAndStudentId(miniClassStudentAttendent.getMiniClassCourse().getMiniClassCourseId(), miniClassStudentAttendent.getStudentId());
//		
//		}
//		miniClassStudentAttendentDao.flush();
//		miniClassStudentAttendentDao.getHibernateTemplate().refresh(miniClassStudentAttendent);
	}
	
	/**
	 * 是否具有一对多考勤或一对多扣费的操作权限
	 * @param miniClass
	 * @param miniClassStudentAttendent
	 * @throws Exception 
	 */
	public void permission4OtmClassCourseAttendanceOperation(String otmClassCourseTeacherId, String otmClassCourseStudyManegerId, String oprationCode)  {
		
		if ("attendance".equals(oprationCode)) {
			// 如果是老师考勤，是不是课程指定的老师
			if (!userService.getCurrentLoginUser().getUserId().equals(otmClassCourseTeacherId)){
				throw new ApplicationException("您不是当前一对多课程指定的老师，不允许考勤！");
			}
		} else if ("charge".equals(oprationCode)) {
			if (!userService.getCurrentLoginUser().getUserId().equals(otmClassCourseStudyManegerId)) {
				throw new ApplicationException("您不是当前一对多班级指定的(班主任)学管，不允许扣费！");
			}
		} else {
			throw new ApplicationException("没有相关操作，请检查！");
		}
	}
	
	/**
	 * 根据用户角色更新一对多考勤记录
	 * @param otmClassCourseId
	 * @param eachInputStuAttendentVo
	 * @param oprationCode
	 */
	public OtmClassStudentAttendent updateOtmClassAttendanceRecordByUserRole(String otmClassCourseId, OtmClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode, String studyManagerId) {
		OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao
				.getOneOtmClassStudentAttendent(otmClassCourseId,
						eachInputStuAttendentVo.getStudentId());// 已有学生考勤记录
		OtmClassAttendanceStatus newStatus = OtmClassAttendanceStatus.valueOf(eachInputStuAttendentVo.getAttendanceType());// 新的考勤状态
		OtmClassCourse otmClassCourse = otmClassCourseDao.findById(otmClassCourseId);
		
		if (newStatus == OtmClassAttendanceStatus.LEAVE){ // FIXME: 2017/3/15 等app改好统一改回来
			newStatus = OtmClassAttendanceStatus.LATE;
		}
		
		if ("attendance".equals(oprationCode)) {// 如果是考勤
			if (otmClassStudentAttendent != null) {// 更新考勤状态
				otmClassStudentAttendent.setOtmClassAttendanceStatus(newStatus);
			} else {// 新建学生考勤
				otmClassStudentAttendent = new OtmClassStudentAttendent();
				otmClassStudentAttendent.setOtmClassCourse(otmClassCourse);
				otmClassStudentAttendent.setStudentId(eachInputStuAttendentVo.getStudentId());
				otmClassStudentAttendent.setOtmClassAttendanceStatus(newStatus);
				otmClassStudentAttendent.setChargeStatus(OtmClassStudentChargeStatus.UNCHARGE);// 未扣费
			}
			otmClassStudentAttendent.setHasTeacherAttendance(BaseStatus.TRUE);// 老师考勤
			//当前一对多第一次考勤更新一对多课程状态
			if(otmClassCourse.getCourseStatus() != null && otmClassCourse.getCourseStatus().equals(CourseStatus.NEW)){
				otmClassCourse.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
			}
				
			otmClassCourse.setTeacherAttendTime(DateTools.getCurrentDateTime());
			
			//通知老师学管学生已来扣费    关闭dwr   2016-12-17
//			messageService.sendMessage(MessageType.SYSTEM_MSG, "老师一对多课程考勤提醒", otmClassStudentAttendent.getOtmClassCourse().getTeacher().getName() + " 已对一对多 "+otmClassStudentAttendent.getOtmClassCourse().getOtmClassName()+" 考勤，请及时核对扣费", MessageDeliverType.SINGLE, studyManagerId);
			
		} else if ("charge".equals(oprationCode)) {// 如果是扣费
			if (otmClassStudentAttendent != null) {// 更新考勤状态
				otmClassStudentAttendent.setOtmClassAttendanceStatus(newStatus);
			} else {// 学管不允许新建学生考勤，必须是老师新建学生考勤
				throw new ApplicationException("学管(班主任)不允许新建学生考勤，必须是老师先考勤！");
			}
		} else {
			throw new ApplicationException("没有相关操作，请检查！");
		}
		return otmClassStudentAttendent;
	}
	
	/**
	 * 获取一对多课程学管师们的名称
	 * @param otmClassCourseId
	 * @return
	 */
	@Override
	public String getStudyManagerName(String otmClassCourseId) {
		OtmClassStudentAttendentVo otmClassStudentAttendentVo = new OtmClassStudentAttendentVo();
		otmClassStudentAttendentVo.setOtmClassCourseId(otmClassCourseId);
		DataPackage dp_attendent = new DataPackage(0, 999);
		otmClassStudentAttendentDao.getOtmClassStudentAttendentList(otmClassStudentAttendentVo, dp_attendent);
		List<OtmClassStudentAttendent> otmClassStudentAttendents =  (List<OtmClassStudentAttendent>) dp_attendent.getDatas();
		String studyManagerName = "";
		for (OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendents) {
			if (null != otmClassStudentAttendent.getStudyManager() && !studyManagerName.contains(otmClassStudentAttendent.getStudyManager().getName() + ",")) {
				studyManagerName += otmClassStudentAttendent.getStudyManager().getName() + ",";
			}
		}
		if (studyManagerName.length() > 0) {
			studyManagerName = studyManagerName.substring(0, studyManagerName.length() - 1);
		}
		return studyManagerName;
	}
	
	/**
	 * 判断 一段时间内 有无老师的一对多课程
	 * @param teacherId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<HasClassCourseVo> checkHasOtmClassForPeriodDate(String teacherId,
			String startDate, String endDate) {
		List<String> dates =   DateTools.getDates(startDate, endDate);		
		boolean hasCourse = false; 
		boolean hasUnCheckAttendanceCourse = false;
		
		boolean hasDeducationCourse = false; 
		boolean hasUnDeducationCourse = false;
		List<HasClassCourseVo> listHasOtmClass = new ArrayList<HasClassCourseVo>();
		for(String strDate : dates) {
			HasClassCourseVo hasClassCourseVo=new HasClassCourseVo();
			hasClassCourseVo.setDateValue(strDate);//日期
			hasCourse = this.checkHasOtmClassForDate(teacherId, strDate, new ArrayList<CourseStatus>());
			if(hasCourse) { // NEW STUDENT_ATTENDANCE 检查 新， 学生已考勤
				List<CourseStatus> listOfStatus = new ArrayList<CourseStatus>();
				listOfStatus.add(CourseStatus.NEW);
				listOfStatus.add(CourseStatus.STUDENT_ATTENDANCE);
				
				hasUnCheckAttendanceCourse = this.checkHasOtmClassForDate(teacherId, strDate, listOfStatus );
				if(hasUnCheckAttendanceCourse ) { // NEW STUDENT_ATTENDANCE 检查 新， 学生已考勤					
					hasClassCourseVo.setHasValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());
				} else {	// 有课 且 都是 没有未考勤的， 就是全部是 不用考勤的  ，  // TEACHER_ATTENDANCE STUDY_MANAGER_AUDITED CHARGED  老师已考勤，  学管已确认， 教务已经扣费 
					hasClassCourseVo.setHasValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());					
				}
			} else {
				hasClassCourseVo.setHasValue(CourseSummaryStatus.NO_COURSE.getValue());
			}
			
			hasDeducationCourse = this.checkHasDeductionOtmClassForDate(teacherId, strDate, new ArrayList<CourseStatus>());
			if(hasDeducationCourse) { // NEW STUDENT_ATTENDANCE 检查 新， 学生已考勤
				List<CourseStatus> listOfStatus = new ArrayList<CourseStatus>();
				listOfStatus.add(CourseStatus.NEW);
				listOfStatus.add(CourseStatus.STUDENT_ATTENDANCE);
				listOfStatus.add(CourseStatus.TEACHER_ATTENDANCE);
				hasUnDeducationCourse = this.checkHasDectionOtmClassForDateNew(teacherId, strDate, listOfStatus );
				if(hasUnDeducationCourse ) { // TEACHER_ATTENDANCE 老师已考勤					
					hasClassCourseVo.setHasDeductionValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());
				} else {	
					hasClassCourseVo.setHasDeductionValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());					
				}
			} else {
				hasClassCourseVo.setHasDeductionValue(CourseSummaryStatus.NO_COURSE.getValue());
			}
			
			listHasOtmClass.add(hasClassCourseVo);
		}
		return listHasOtmClass;
	}
	
	/**
	 * 判断 某一天 有无老师的一对多 课程, 根据不同的状态
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	@Override
	public boolean checkHasOtmClassForDate(String teacherId, String date,  List<CourseStatus> listOfStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer countHql = new StringBuffer();
		countHql.append("select count(*) from OtmClassCourse where 1 =1 ") 
				.append(" and teacher.userId = :teacherId ")
				.append(" and courseDate = :date ");
		params.put("teacherId", teacherId);
		params.put("date", date);
		if(listOfStatus.size()>0) {
			countHql.append(" and (courseStatus in (:listOfStatus) or auditStatus='UNVALIDATE')");
			params.put("listOfStatus", listOfStatus);;
		}
		int count = this.otmClassCourseDao.findCountHql(countHql.toString(), params);
		return count >=1 ;
	}
	
	/**
	 * 判断 某一天 有无学管师的一对多  课程, 根据不同状态
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	@Override
	public boolean checkHasDeductionOtmClassForDate(String teacherId, String date, List<CourseStatus> listOfStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer countHql = new StringBuffer();
		countHql.append("select count(DISTINCT otmClassCourse) from OtmClassStudentAttendent where 1 =1 ") 
				.append(" and studyManager.userId = :teacherId ")
				.append(" and otmClassCourse.courseDate = :date ");
		params.put("teacherId", teacherId);
		params.put("date", date);
		if(listOfStatus.size()>0) {
			countHql.append(" and (otmClassCourse.courseStatus in (:listOfStatus)");
			params.put("listOfStatus", listOfStatus);
		}
		int count = this.otmClassStudentAttendentDao.findCountHql(countHql.toString(), params);
		return count >=1 ;
	}
	
	/**
	 * 判断 某一天 有无学管师的一对多  课程, 根据不同状态  新逻辑，兼容多学管。
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	public boolean checkHasDectionOtmClassForDateNew(String teacherId, String date, List<CourseStatus> listOfStatus) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append("select DISTINCT ocsa.otmClassCourse from OtmClassStudentAttendent ocsa where 1 =1 ") 
				.append(" and ocsa.studyManager.userId = :teacherId ")
				.append(" and ocsa.otmClassCourse.courseDate = :date ");
		params.put("teacherId", teacherId);
		params.put("date", date);
		if(listOfStatus.size()>0) {
			hql.append(" and (ocsa.otmClassCourse.courseStatus in (:listOfStatus)");
			params.put("listOfStatus", listOfStatus);
			hql.append(" or (ocsa.otmClassCourse.courseStatus = 'CHARGED' and ocsa.chargeStatus = 'UNCHARGE')) ");
		}
		List<OtmClassCourse> list = otmClassCourseDao.findAllByHQL(hql.toString(), params);
		if (list.size() > 0) {
			for (OtmClassCourse otmClassCourse : list) {
				if (otmClassCourse.getCourseStatus() != CourseStatus.CHARGED) {
					return true;
				} else {
					OtmClassStudentAttendentVo otmClassStudentAttendentVo = new OtmClassStudentAttendentVo();
					otmClassStudentAttendentVo.setOtmClassCourseId(otmClassCourse.getOtmClassCourseId());
					DataPackage dp_attendent = new DataPackage(0, 999);
					otmClassStudentAttendentDao.getOtmClassStudentAttendentList(otmClassStudentAttendentVo, dp_attendent);
					List<OtmClassStudentAttendent> otmClassStudentAttendents =  (List<OtmClassStudentAttendent>) dp_attendent.getDatas();
					boolean hasCharged = false;
					for (OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendents) {
						if (otmClassStudentAttendent.getStudyManager().getUserId().equals(teacherId) && otmClassStudentAttendent.getChargeStatus() == OtmClassStudentChargeStatus.CHARGED) {
							hasCharged = true;
						}
					}
					if (!hasCharged) {
						return true;
					}
				} 
			}
		}
		return false;
	}
	
	/**
	 * 一对多课程列表 手机端
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOtmClassCourseListForMobile(
			OtmClassCourseVo otmClassCourseVo, DataPackage dp) {
		dp = otmClassCourseDao.getOtmClassCourseListForMobile(otmClassCourseVo, dp);
		List<OtmClassCourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<MiniClassCourse>) dp.getDatas(), OtmClassCourseVo.class);
		List<OtmClassStudentAttendent> otmClassStudentAttendentList = null;
		int studentCount = 0;//报名的学生人数
		int attendanceCount =0;//考勤人数
		for(OtmClassCourseVo vo : searchResultVoList){			
			//查询参与考勤的人数
			otmClassStudentAttendentList=otmClassStudentAttendentDao.findByCriteria(Expression.eq("otmClassCourse.otmClassCourseId", vo.getOtmClassCourseId()));
			
			vo.setAuditStatusName(vo.getAuditStatus() == null ? null : vo.getAuditStatus().getName());
			//报名的所有学生
//			studentCount=miniClassStudentDao.findCountHql("select count(*) from MiniClassStudent where miniClass.miniClassId='"+vo.getMiniClassId()+"' and firstSchoolTime <= '" + vo.getCourseDate() + "' ");
			
//			String studyManagerName = "";
			Student stu = null;
			String studentName = "";
			
			if (otmClassStudentAttendentList.size() > 0) {
				User currentUser = userService.getCurrentLoginUser();
				boolean isStudyCharged = false;
				List<StudyManagerMobileVo> studyManagerMobileVos = new ArrayList<StudyManagerMobileVo>();
				StudyManagerMobileVo studyManagerMobileVo = null;
				for (OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendentList) {
//					if (null != otmClassStudentAttendent.getStudyManager() && !studyManagerName.contains(otmClassStudentAttendent.getStudyManager().getName() + ",")) {
//						studyManagerName += otmClassStudentAttendent.getStudyManager().getName() + ",";
//					}
					User studyManager = otmClassStudentAttendent.getStudyManager();
					if (null != studyManager) {
						if (currentUser.getUserId().equals(studyManager.getUserId()) 
								&& otmClassStudentAttendent.getChargeStatus() == OtmClassStudentChargeStatus.CHARGED) {
							isStudyCharged = true;
						}
						studyManagerMobileVo = new StudyManagerMobileVo();
						studyManagerMobileVo.setId(studyManager.getUserId());
						studyManagerMobileVo.setName(studyManager.getName());
						studyManagerMobileVo.setContact(studyManager.getContact());
						studyManagerMobileVos.add(studyManagerMobileVo);
					}
					if (StringUtil.isNotBlank(otmClassStudentAttendent.getStudentId())) {
						stu = studentDao.findById(otmClassStudentAttendent.getStudentId());
						if (!studentName.contains(stu.getName() + ",")) {
							studentName += stu.getName() + ",";
						}
					}
				}
				
				vo.setStudyManagerMobileVos(studyManagerMobileVos);
				
				if (!isStudyCharged && otmClassCourseVo.getCurrentRoleId() != null && otmClassCourseVo.getCurrentRoleId().equals("classTeacherDeduction") 
						&& vo.getCourseStatus().equals(CourseStatus.CHARGED.getValue())) {
					vo.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE.getValue());
					vo.setCourseStatusName(CourseStatus.TEACHER_ATTENDANCE.getName());
				}
			}
//			if (studyManagerName.length() > 0) {
//				studyManagerName = studyManagerName.substring(0, studyManagerName.length() - 1);
//			}
			if (studentName.length() > 0) {
				studentName = studentName.substring(0, studentName.length() - 1);
			}
			
			
			vo.setStudentName(studentName);
//			vo.setStudyManagerName(studyManagerName);
			studentCount=otmClassStudentAttendentList.size();
			vo.setStudentCount(studentCount);//报名的学生人数
			
			if(otmClassStudentAttendentList!=null && otmClassStudentAttendentList.size() > 0){
				// 考勤人数
				attendanceCount=otmClassStudentAttendentList.size();
				vo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, lateClassPeopleNum=0, absentNumber=0, chargeNumber = 0; // 未上课，已准时上课， 迟到， 扣费 人数
				for(OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendentList){
					if(OtmClassStudentChargeStatus.CHARGED.equals(otmClassStudentAttendent.getChargeStatus()))
						chargeNumber++;
					if(OtmClassAttendanceStatus.NEW.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						newNumber++;
					else if(OtmClassAttendanceStatus.CONPELETE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						completeNumber++;
					else if(OtmClassAttendanceStatus.LATE.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						lateClassPeopleNum++;
					else if(OtmClassAttendanceStatus.ABSENT.equals(otmClassStudentAttendent.getOtmClassAttendanceStatus()))
						absentNumber++;
				}
				
				vo.setDeductionCount(chargeNumber);// 扣费人数
				vo.setNewClassPeopleNum(newNumber); // 设置 未上课
				vo.setCompleteClassPeopleNum(completeNumber+lateClassPeopleNum); // 已上课
//				vo.setLeaveClassPeopleNum(leaveNumber); // 请假
				vo.setLateClassPeopleNum(lateClassPeopleNum);
				vo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				vo.setNoAttendanceCount(attendanceCount-completeNumber-lateClassPeopleNum-absentNumber); // 未考勤
//				vo.setNoAttendanceCount(studentCount-attendanceCount); // 未考勤
			}  else {
				vo.setAttendanceCount(0);//考勤人数
				vo.setDeductionCount(0);// 扣费人数
				vo.setNewClassPeopleNum(0); // 设置 未上课
				vo.setCompleteClassPeopleNum(0); // 已上课
				vo.setLateClassPeopleNum(0);//迟到
//				vo.setLeaveClassPeopleNum(0); // 请假
				vo.setAbsentClassPeopleNum(0); // 缺勤
				vo.setNoAttendanceCount(studentCount); // 未考勤
			}
			if (!vo.getCourseStatus().equals(CourseStatus.NEW.getValue())) {
				MoneyWashRecords moneyWashRecords = moneyWashRecordsService.findMoneyWashRecordsByOtmCourseId(vo.getOtmClassCourseId());
				if (moneyWashRecords != null) {
					vo.setIsWashed("TRUE");
					vo.setWashRemark(moneyWashRecords.getDetailReason());
				} else {
					vo.setIsWashed("FALSE");
				}
			} else {
				vo.setIsWashed("FALSE");
			}
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}
	
	/**
	 * 保存一对多考勤 签名图片
	 * @param miniClassCourseId
	 * @param attendancePicFile
	 */
	public void saveOtmClassAttendancePic(String otmClassCourseId,MultipartFile attendancePicFile,String servicePath) {
		OtmClassCourse otmClassCourse = otmClassCourseDao.findById(otmClassCourseId);
		if(otmClassCourse !=null) {
			// 统一使用 文件名 来 标明签名文件
			//String fileName = UUID.randomUUID().toString();
			String fileName =  String.format("OTM_CLASS_ATTEND_PIC_%s.jpg", otmClassCourseId);
			otmClassCourse.setAttendacePicName(fileName);
			
			String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
			FileUtil.isNewFolder(folder);			 
			
			String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG 
			String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
			String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小					 
			
			String relFileName=folder+"/realFile_"+otmClassCourseId+UUID.randomUUID().toString()+".jpg";
			File realFile=new File(relFileName);
			File bigFile=new File(folder+"/"+bigFileName);
			File midFile=new File(folder+"/"+midFileName);
			File smallFile=new File(folder+"/"+smallFileName);			
			
			try {				
				attendancePicFile.transferTo(realFile);
				ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
				ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
				ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
				AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
				AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
				AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云				
				AliyunOSSUtils.put(fileName, realFile);//传原图到阿里云				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{				
				bigFile.delete();
				midFile.delete();
				smallFile.delete();
				realFile.delete();
			}			
		}
	}
	
	/**
	 * 一对多审批汇总
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param otmTypes
	 * @return
	 */
	@Override
	public DataPackage getOtmClassCourseAuditAnalyze(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes,String anshazhesuan) {
	    if (StringUtil.isBlank(vo.getBlCampusId())) {
	        return dp;
        }
		if ("keshi".equals(anshazhesuan)){
			dp= otmClassCourseDao.getOtmClassCourseAuditAnalyze(dp, vo, AuditStatus,otmClassTypes);
		}else {
			dp= otmClassCourseDao.getOtmClassCourseAuditAnalyzeXiaoShi(dp, vo, AuditStatus,otmClassTypes);
		}


		List<Map<String,String>> list= (List<Map<String, String>>) dp.getDatas();
		List<Map<String,String>> newlist=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : list) {
			if(StringUtils.isNotBlank(map.get("teacherLevel"))){
				map.put("teacherLevelName", TeacherLevel.valueOf(map.get("teacherLevel")).getName());
			}
			
			if(StringUtils.isNotBlank(map.get("teacherType"))){
				map.put("teacherTypeName", TeacherType.valueOf(map.get("teacherType")).getName());
			}
			
			
			newlist.add(map);
		}
		dp.setDatas(newlist);
		return dp;
	}
	
	/**
	 * 一对多课程审批汇总工资
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param otmTypes
	 * @return
	 */
	@Override
	public DataPackage otmClaCourseAuditAnalyzeSalary(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes,String anshazhesuan) {
		if (StringUtil.isBlank(vo.getBrenchId())) {
		    return dp;
		}
		dp= otmClassCourseDao.otmClaCourseAuditAnalyzeSalary(dp, vo,AuditStatus,otmClassTypes,anshazhesuan);
		
		List<Map<String,String>> list= (List<Map<String, String>>) dp.getDatas();
		List<Map<String,String>> newlist=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : list) {
			if(StringUtils.isNotBlank(map.get("teacherLevel"))){
				map.put("teacherLevelName", TeacherLevel.valueOf(map.get("teacherLevel")).getName());
			}
			
			if(StringUtils.isNotBlank(map.get("teacherType"))){
				map.put("teacherTypeName", TeacherType.valueOf(map.get("teacherType")).getName());
			}
			
			
			newlist.add(map);
		}
		dp.setDatas(newlist);
		return dp;
	}
	
	/**
	 * 一对多审批列表
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @return
	 */
	public DataPackage otmClassCourseAuditList(DataPackage dataPackage,String startDate,String endDate,
			String campusId,String teacherId,String auditStatus,String subject) {
		dataPackage = otmClassCourseDao.otmClassCourseAuditList(dataPackage, startDate, endDate, campusId, teacherId, auditStatus,subject);
		List<Map<String, Object>> list = (List<Map<String, Object>>) dataPackage.getDatas();
		for(Map<String, Object> map : list) {
			String courseId = (String) map.get("courseId");
			String studyManager = this.getStudyManagerName(courseId);
			map.put("studyManeger", studyManager);
		}
		dataPackage.setDatas(list);
		return dataPackage;
	}

	@Override
	public List getOtmClassCourseAuditSalaryNums( BasicOperationQueryVo vo, String auditStatus, String otmClassTypes) {
		List otmClassCourseAuditSalaryNums = otmClassCourseDao.getOtmClassCourseAuditSalaryNums( vo, auditStatus, otmClassTypes);
		return otmClassCourseAuditSalaryNums;
	}

	/**
	 * 一对多课程审批
	 */
	public void otmClassCourseAudit(String courseId, String auditStatus) {
		OtmClassCourse otmClassCourse = otmClassCourseDao.findById(courseId);
		otmClassCourse.setAuditStatus(AuditStatus.valueOf(auditStatus));
		otmClassCourseDao.save(otmClassCourse);
	}

	@Override
	public OtmClassCourse getOtmClassCourseById(String otmClassCourseId) {
		OtmClassCourse otmClassCourse = otmClassCourseDao.findById(otmClassCourseId);
		return otmClassCourse;
	}

	@Override
	public void updateOtmClassCourse(OtmClassCourse otmClassCourse) {
		otmClassCourseDao.save(otmClassCourse);		
	}
	
}
