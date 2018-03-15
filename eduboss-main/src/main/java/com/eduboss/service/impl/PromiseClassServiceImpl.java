package com.eduboss.service.impl;

import com.eduboss.common.*;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.*;
import com.eduboss.dto.*;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.*;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.service.handler.impl.MiniClassConProdHandler;
import com.eduboss.service.handler.impl.OtherConProdHandler;
import com.eduboss.service.handler.impl.PromiseClassConProdHandler;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 目标班
 * */
@Service(value = "promiseClassService")
public class PromiseClassServiceImpl implements PromiseClassService{
	private final static Logger log = Logger.getLogger(PromiseClassServiceImpl.class);
	
	@Resource(name = "PromiseClassDao")
	private PromiseClassDao promiseClassDao;
	
	@Resource(name = "PromiseStudentDao") 
	private PromiseStudentDao promiseStudentDao;
	
	@Autowired
	private UserService userService;
	
	@Resource(name ="PromiseClassRecordDao")
	private PromiseClassRecordDao promiseClassRecordDao;
	
	@Resource(name ="PromiseClassDetailRecordDao")
	private PromiseClassDetailRecordDao promiseClassDetailRecordDao;
	
	@Autowired
	private ContractProductDao contractProductDao;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private StudentDao studentDao;
	

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private AccountChargeRecordsDao accountChargeRecordsDao;

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private StudnetAccMvDao studnetAccMvDao;
	
	@Autowired
	private PromiseClassSubjectDao promiseClassSubjectDao;

	@Autowired
	private SmallClassService smallClassService;

	@Autowired
	private PromiseClassEndAuditDao promiseClassEndAuditDao;

	@Autowired
	private ProductService productService;

	@Autowired
	private MiniClassStudentAttendentDao miniClassStudentAttendentDao;

	@Autowired
	private MiniClassCourseDao smallClassCourseDao;

	@Autowired
	private ContractDao contractDao;

	/**
	 * 查询目标班报名和管理列表
	 * */
	@Override
	public DataPackage getPromiseClassList(PromiseClass pc,DataPackage dataPackage) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from PromiseClass ";
		String hqlWhere="where 1=1 ";
		//根据开班时间查找
		if(StringUtil.isNotEmpty(pc.getStartDate())){
			hqlWhere+=" and startDate >= :startDate ";
			params.put("startDate", pc.getStartDate());
		}
		//根据目标班名称查找
		if(StringUtil.isNotEmpty(pc.getpName())){
			hqlWhere +=" and pName like :pName ";
			params.put("pName", "%" + pc.getpName() + "%");
		}
		//根据班时间查找
		if(StringUtil.isNotEmpty(pc.getEndDate())){
			hqlWhere += " and startDate <= :endDate ";
			params.put("endDate", pc.getEndDate());
		}
//		//根据校区查找
		
		
		
		if(pc.getpSchool() != null){
			if(StringUtil.isNotEmpty(pc.getpSchool().getId())){
				Organization org = organizationDao.findById(pc.getpSchool().getId());
				hqlWhere += " and pSchool.orgLevel like :orgLevel ";
				params.put("orgLevel", org.getOrgLevel() +"%");
			}
		}
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","pSchool.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("目标班管理与报名","nsql","hql");
		hqlWhere+=roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap);
		//只能看到本校区的
//		hqlWhere += " and pSchool.id like '%"+userService.getCurrentLoginUser().getOrganizationId()+"%'";
		
		//根据班主任查找
		if(pc.getHead_teacher() !=null){
			if(StringUtil.isNotEmpty(pc.getHead_teacher().getUserId())){
				hqlWhere += " and head_teacher.userId = :headTeacherId ";
				params.put("headTeacherId", pc.getHead_teacher().getUserId());
			}
		}
		
		//根据教室查找
		if(StringUtil.isNotEmpty(pc.getClassRoom())){
			hqlWhere += " and classRoom = :classRoom ";
			params.put("classRoom", pc.getClassRoom());
		}
		//根据状态查询
		if(StringUtil.isNotEmpty(pc.getpStatus())){
			hqlWhere += " and pStatus = :pStatus ";
			params.put("pStatus", pc.getpStatus());
		}
		//根据年份查询
		if(pc.getYear() != null && StringUtil.isNotBlank(pc.getYear().getId())) {
			hqlWhere += " and year.id = :yearId ";
			params.put("yearId", pc.getYear().getId());
		}
		
		hql += hqlWhere;
		hql +=" order by startDate desc ";
		dataPackage=promiseClassDao.findPageByHQL(hql, dataPackage, true, params);
		List<PromiseClass> list = (List<PromiseClass>) dataPackage.getDatas();
		List<PromiseClassVo> voList = new ArrayList<PromiseClassVo>();
		for(PromiseClass promise : list){
			PromiseClassVo vo=HibernateUtils.voObjectMapping(promise, PromiseClassVo.class);
			voList.add(vo);
		}
		
		dataPackage.setDatas(voList);
		return dataPackage;
	}
	
	/**
	 * 根据ID查询目标班
	 */
	public PromiseClass findPromiseClassById(String promiseClassId) {
		return promiseClassDao.findById(promiseClassId);
	}

	
	/**
	 * 查询目标班学生管理（班主任）
	 * */
	@Override
	public DataPackage getPromiseStudentList(PromiseStudentSearchDto dto, DataPackage dataPackage) {
		Map<String, Object> params = new HashMap<String, Object>();
		String[] userInfo = this.getCurUserAndDate();
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlWhere = new StringBuilder();
		hql.append(" from PromiseStudent where 1=1");


		hqlWhere.append(roleQLConfigService.getAppendSqlByAllOrg("目标班学生管理","hql","promiseClass.pSchool.id"));

		if(StringUtil.isNotEmpty(dto.getProductName())){
			hqlWhere.append(" and contractProduct.product.name like :productName ");
			params.put("productName", "%" + dto.getProductName() + "%");
		}

		//根据目标班名称查询
			if(StringUtil.isNotEmpty(dto.getpName())){
				hqlWhere.append(" and promiseClass.pName like :pName ");
				params.put("pName", "%" + dto.getpName() + "%");
			}

			if(StringUtil.isNotEmpty(dto.getPlSchoolId())){
				hqlWhere.append(" and promiseClass.pSchool.id = :pSchoolId ");
				params.put("pSchoolId",dto.getPlSchoolId());
			}

		//根据学生姓名查询
		if(StringUtil.isNotEmpty(dto.getStudentName())){
			hqlWhere.append(" and student.name like :studentName ");
			params.put("studentName", "%" + dto.getStudentName() + "%");
		}
		//根据状态查询
		if(dto.getCourseStatus()!=null && dto.getCourseStatus().length>0){
			hqlWhere.append(" and ( 1=2 ");
			int i =0;
			for(String status:dto.getCourseStatus()){
				hqlWhere.append(" or courseStatus = :courseStatus"+i);
				params.put("courseStatus"+i, status);
				i++;
			}
			hqlWhere.append(" ) ");
		}
		//根据完结状态查询
		if(StringUtil.isNotEmpty(dto.getResultStatus())){
			hqlWhere.append(" and resultStatus = :resultStatus ");
			params.put("resultStatus", dto.getResultStatus());
		}

		//是否退班
		if(StringUtil.isNotEmpty(dto.getAbortClass())){
			if("1".equals(dto.getAbortClass())) {
				hqlWhere.append(" and abortClass = :abortClass ");
				params.put("abortClass", dto.getAbortClass());
			}else{
				hqlWhere.append(" and abortClass is null ");
			}
		}

		//年份
		if(StringUtil.isNotEmpty(dto.getYear())){
			hqlWhere.append(" and contractProduct.product.productVersion.id = :productVersion ");
			params.put("productVersion", dto.getYear());
		}

		//年级
		if(StringUtil.isNotEmpty(dto.getGradeId())){
			hqlWhere.append(" and student.gradeDict.id = :gradeId ");
			params.put("gradeId", dto.getGradeId());
		}

		if(dto.getAuditStatus()!=null){
			hqlWhere.append(" and auditStatus = :auditStatus ");
			params.put("auditStatus", dto.getAuditStatus());
		}

		//分公司ID
		if(StringUtil.isNotEmpty(dto.getBrenchId())){
			hqlWhere.append(" and promiseClass.pSchool.parentId = :parentId ");
			params.put("parentId", dto.getBrenchId());
		}

		hql.append(hqlWhere);
		hql.append(" order by createTime desc ");
		dataPackage=promiseClassDao.findPageByHQL(hql.toString(), dataPackage, true, params);
		List<PromiseStudent> list = (List<PromiseStudent>) dataPackage.getDatas();
		List<PromiseStudentVo> voList = new ArrayList<PromiseStudentVo>();
		for(PromiseStudent  student : list){
			PromiseStudentVo vo = HibernateUtils.voObjectMapping(student, PromiseStudentVo.class);
			vo.setRemainingAmount(student.getContractProduct().getRemainingAmount());
			if(vo!=null){
				BigDecimal chargeHours = promiseClassRecordDao.countStudentPromiseChargeHoursByStudentId(vo.getStudentId());
				vo.setMonthHours(chargeHours);
			}

			//详情
			vo.setSubjectDetail(promiseClassSubjectDao.getPromiseSubjectDetailList(vo.getId()));

			if(student.getContractProduct()!=null && student.getContractProduct().getProduct()!=null && student.getContractProduct().getProduct().getCourseSeries()!=null
			&& student.getContractProduct().getProduct().getCourseSeries().getId().equals("fixedmoney")) {
				PromiseClassEndAudit p = promiseClassEndAuditDao.findOneByHQL("from PromiseClassEndAudit where promiseStudentId='" + student.getId() + "'", new HashMap<String, Object>());
				if(p!=null)
				vo.setAudit(p);
			}
			voList.add(vo);
						
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	@Override
	public List getPromiseSubjectDetailList(String promiseStudentId){
		return promiseClassSubjectDao.getPromiseSubjectDetailList(promiseStudentId);
	}


	/**
	 * 保存新增目标班信息
	 * */
	@Override
	public PromiseClass savePromiseClass(PromiseClass promiseClass) {
		//获取当前用户ID和当前系统时间 userInfo[0]：当前登录用户ID，userInfo[1]：为当前系统时间
		String[] userInfo = this.getCurUserAndDate();
		
		//表单提交过来如果是“”则是新增，如果不为“”则不修改
		if("".equals(promiseClass.getId())){
			promiseClass.setId(null);
			promiseClass.setModifyUserId(userInfo[0]);
			promiseClass.setModifyDate(userInfo[1]);
		}else{
			promiseClass.setCreateUserId(userInfo[0]);
			promiseClass.setCreateDate(userInfo[1]);
		}
		
		promiseClassDao.save(promiseClass);
		
		return promiseClass;
	}
	
	/**
	 * 删除目标班信息
	 * */
	public void deletePromiseClassByProClaId(String promiseClassId) {
		PromiseClass promiseClass = new PromiseClass();
		promiseClass.setId(promiseClassId);
		promiseClassDao.delete(promiseClass);
	}


	/**
	 * 学生报名目标班
	 * */
	@Override
	public void savaPromiveClassStudent(PromiseStudent promiseStudent) {
		//System.out.println("----------"+promiseStudent.getStudentId()+"; studentName = "+promiseStudent.getStudentName());
		String promiseClassId = promiseStudent.getPromiseClass().getId();
		//String promiseClassName = promiseStudent.getPromiseClassName();
		String startClassDate = promiseStudent.getCourseDate();
		String[] studentIds = promiseStudent.getStudent().getId().split(",");
		String[] contractProductIds = promiseStudent.getContractProduct().getId().split(",");
		int length = studentIds.length;
		if(length>1){
			List<PromiseStudent> list = new ArrayList<PromiseStudent>();
			//String[] StudentNames = promiseStudent.getStudentName().split(",");
			for (int j = 0; j < length; j++) {
				List<PromiseStudent> ps = promiseStudentDao.getPromiseStudentByContractPro(contractProductIds[j]);
				if(ps!=null && ps.size()>0){//如果有报目标班，那么就用目标班的校区，如果没有就用学生所属校区
					Student student = studentDao.findById(studentIds[j]);
					throw new ApplicationException("学生：" + student.getName() + "该产品已经报读具体的班级，请不要重复报班！");
				}
			}
			
			
		    for(int i=0; i< length; i++){
		        PromiseStudent pro = new PromiseStudent();
		       // pro.setPromiseClass().(promiseClassId);
		        PromiseClass promise = new PromiseClass();
		        pro.setPromiseClass(promise);
		        pro.getPromiseClass().setId(promiseClassId);
		        Student stu = new Student();
		        pro.setStudent(stu);
		        pro.getStudent().setId(studentIds[i]);
		        ContractProduct cp = new ContractProduct();
		        pro.setContractProduct(cp);
		        pro.getContractProduct().setId(contractProductIds[i]);
		        //pro.setCreateUserId(userId);
		       // pro.setCreateTime(curDate);
		        pro.setCourseStatus("1");
		        pro.setCourseDate(startClassDate);
		        list.add(pro);
		    }
		    for(PromiseStudent student : list){
		    	promiseStudentDao.updateOrSavePromiseStudent(student);
		    }
		}else{
			List<PromiseStudent> ps = promiseStudentDao.getPromiseStudentByContractPro(contractProductIds[0]);
			if(ps!=null && ps.size()>0){//如果有报目标班，那么就用目标班的校区，如果没有就用学生所属校区
				Student student = studentDao.findById(studentIds[0]);
				throw new ApplicationException("学生：" + student.getName() + "该产品已经报读具体的班级，请不要重复报班！");
			}
			
			ContractProduct cp = new ContractProduct();
			promiseStudent.setContractProduct(cp);
			promiseStudent.getContractProduct().setId(contractProductIds[0]);
			Student stu = new Student();
			promiseStudent.setStudent(stu);
			promiseStudent.getStudent().setId(studentIds[0]);
			//promiseStudent.setCreateUserId(userId);
			//promiseStudent.setCreateTime(curDate);
			promiseStudent.setCourseStatus("1");
			promiseStudentDao.updateOrSavePromiseStudent(promiseStudent);
		}
	}
	
	/**
	 * 退班
	 * */
	@Override
	public void cancelPromiseClassApply(String studentId,String promiseClassId,String contractProductId){
		//System.out.println("*******************************************************************************************"+studentId);
		//获取学生人数
		String[] studentIds = studentId.split(",");
		int length = studentIds.length;
		for(int i=0; i<length; i++){
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuilder hql = new StringBuilder();
			hql.append("update PromiseStudent set abortClass = '1' where student.id = :studentId ")
			.append(" and promiseClass.id = :promiseClassId ")
			.append(" and contractProduct.id = :contractProductId ");
			params.put("studentId", studentIds[i]);
			params.put("promiseClassId", promiseClassId);
			params.put("contractProductId",contractProductId);
			promiseStudentDao.excuteHql(hql.toString(), params);
		}
		
	}
	/**
	 * 查询符合报名目标班的学生
	 * */
	@Override
	public List<PromiseClassApplyVo> getStudentWantList(ProductType type,String gradeId,String promiseClassId,String status,String studentId,String stuendentName) {
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder();
		     sql.append(" select cp.* from contract_product cp ")
				.append(" left join product p on cp.PRODUCT_ID=p.id")
				.append(" left join contract c on c.id = cp.CONTRACT_ID")
				.append(" left join student s on s.id = c.STUDENT_ID")
				.append(" where cp.type =:type")
				.append(" and p.GRADE_ID=:gradeId")
				.append(" and (cp.PAID_STATUS='PAYING' or cp.PAID_STATUS='PAID')");
		params.put("type", type);
		params.put("gradeId", gradeId);

		//状态为本班有合同未完结的学生
		if(!"".equals(status)){
			 sql.append(" and s.id in (select student_id from promise_class_student pcs where pcs.PROMISE_CLASS_ID=:promiseClassId")
				.append(" and (pcs.COURSE_STATUS=:status or pcs.COURSE_STATUS='1') and pcs.abort_class is NULL)");
			params.put("promiseClassId", promiseClassId);
			params.put("status", status);
		}else{
			    //未报读该班，可以报读的学生
			     sql.append(" and cp.`STATUS`='").append(ContractProductStatus.NORMAL).append("' ")
					.append(" and c.STUDENT_ID not in (select student_id from promise_class_student pcs where ")
					.append("  pcs.COURSE_STATUS='1'  and pcs.abort_class is NULL and cp.id = pcs.contract_product_id)");
				if(!"".equals(stuendentName)){
					sql.append(" and s.name like :stuendentName ");
					params.put("stuendentName", "%" + stuendentName + "%");
				}
			    PromiseClass promiseClass = promiseClassDao.findById(promiseClassId);
			    Organization organization = promiseClass.getpSchool();
					sql.append(" and s.id in (select student_id from student_organization where ORGANIZATION_ID = '"+organization.getId()+"' )");
		}
		
		List<ContractProduct>  contractProducts = contractProductDao.findBySql(sql.toString(), params);
		List<PromiseClassApplyVo> voList = new ArrayList<PromiseClassApplyVo>();
		for(ContractProduct con : contractProducts){
			//只拿剩余资金大于0的
			if(con.getRemainingAmount().compareTo(BigDecimal.ZERO)>0){
				PromiseClassApplyVo vo = HibernateUtils.voObjectMapping(con, PromiseClassApplyVo.class);
				vo.setRemainningAmount(con.getRemainingAmount());
				voList.add(vo);
			}
		}

		return voList;
	}


	/**
	 * 根据目标班ID查询所有学生
	 * */
	@Override
	public DataPackage getStudentListByPromiseClassId(Map params,DataPackage dataPackage) {
		Map<String, Object> hqlParams = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseStudent where promiseClass.id = :promiseClassId ");
		hqlParams.put("promiseClassId", params.get("promiseClassId"));
		if(!"".equals(params.get("apply"))){
			hql.append(" and abortClass is null ");
		}
		//List<PromiseStudent> list  = promiseStudentDao.findBySql(hql.toString());
		dataPackage =  promiseStudentDao.findPageByHQL(hql.toString(), dataPackage, true, hqlParams);
		List<PromiseStudent> list = (List<PromiseStudent>) dataPackage.getDatas();
		List<PromiseStudentVo> voList = new ArrayList<PromiseStudentVo>();
		for(PromiseStudent promise : list){
			PromiseStudentVo vo = HibernateUtils.voObjectMapping(promise, PromiseStudentVo.class);
			//课程进度跟着学生走，跟学生详情里面的合同课程进度一致
			BigDecimal chargeHours = promiseClassRecordDao.countStudentPromiseChargeHoursByStudentId(vo.getStudentId());			
			vo.setMonthHours(chargeHours);			
			vo.setRemainingAmount(promise.getContractProduct().getRemainingAmount());
			voList.add(vo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	/**
	 * 根据合同产品获取所有学生
	 * */
	public List<PromiseStudent> getStudentListByContractProduct(ContractProduct contractProduct) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseStudent where contractProduct.id = :contractProductId ");
		params.put("contractProductId", contractProduct.getId());
		return promiseStudentDao.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 删除目标班学生
	 * */
	public void deletePromiseStudent(PromiseStudent promiseStudent) {
		promiseStudentDao.delete(promiseStudent);
	}

	/**
	 * 保存月结记录
	 * */
	@Override
	public PromiseClassRecord savaPromiseClassRecord(PromiseClassRecord promiseClassRecord,PromiseClassDetailRecord detail,String contractProductId) {
	
		//获取当前登录用户信息和当前日期
		String[] userInfo = this.getCurUserAndDate();
		promiseClassRecord.setCreateUserId(userInfo[0]);
		detail.setCreateUserId(userInfo[0]);
		promiseClassRecord.setCreateTime(userInfo[1]);
		detail.setCreateTime(userInfo[1]);
		
		if (CommonUtil.isNumeric(promiseClassRecord.getClassYear()) && CommonUtil.isNumeric(promiseClassRecord.getClassMonth())) {
			String classDate = DateTools.getLastDayofMonth(Integer.parseInt(promiseClassRecord.getClassYear()), Integer.parseInt(promiseClassRecord.getClassMonth()));
			promiseClassRecord.setClassDate(classDate);
		}
		
		//保存月结记录
		promiseClassRecordDao.save(promiseClassRecord);
		
		String recordId = promiseClassRecord.getId();
		
		PromiseStudent promiseStudent = promiseStudentDao.getPromiseClassContractProId(promiseClassRecord.getPromiseClass().getId(), promiseClassRecord.getStudentId());
	    //System.out.println(promiseStudent);
		ContractProduct contractProduct = new ContractProduct();
		if(promiseStudent != null){
			contractProduct = contractProductDao.findById(promiseStudent.getContractProduct().getId());
		}
//		System.out.println("######################################");
//		System.out.println(contractProduct==null ? "NULL contractProduct" : contractProduct.toString());
		
		//保存月结详细记录
		this.savePrmoseClassDetailRecord(detail, recordId);
		//调用扣费接口，产生一条扣费记录
		if("0".equals(promiseClassRecord.getStatus()))
		if(contractProduct != null){
			new PromiseClassConProdHandler().chargePromiseClassProduct(contractProduct.getContract(), contractProduct, 
					promiseClassRecord.getChargeAmount(), promiseClassRecord.getChargeHours(), promiseClassRecord);
		}
		
		//月结详细信息
		return promiseClassRecord;
		
	}


	/**
	 * 保存月结详细记录
	 * */
	@Override
	public void savePrmoseClassDetailRecord(PromiseClassDetailRecord detail,String recordId) {
		//获取当前登录用户信息和当前日期
		String[] userInfo = this.getCurUserAndDate();

		String ids = detail.getId();
		String[] dRecordIds={};
		if(ids!=null && !"".equals(ids)){
			dRecordIds =ids.split(",");
		}
		
		//表单提交过来的金条数据以“，”号分隔；需要分开变成集合
		String classType[] = detail.getClassType().split(",");
		String subject[] = detail.getSubject().split(",");
		String courseHours[] = detail.getCourseHours().split(",");
		String teacher[] = detail.getTeacher().split(",");
		int length=classType.length;
		int idsLeng = dRecordIds.length;
		//System.out.println("********************************************************************************** length = "+length);
		List<PromiseClassDetailRecord> list = new ArrayList<PromiseClassDetailRecord>();
		for(int i=0;i<length;i++){
			PromiseClassRecord record = new PromiseClassRecord();
			record.setId(recordId);
			PromiseClassDetailRecord detailRecord = new PromiseClassDetailRecord();
			
			if(idsLeng > i){
				detailRecord.setId(dRecordIds[i]);
			}else{
				detailRecord.setId(null);
			}
			
			detailRecord.setPromiseClassRecord(record);
			detailRecord.setCreateUserId(userInfo[0]);
			detailRecord.setCreateTime(userInfo[1]);
			detailRecord.setClassType(classType[i]);
			detailRecord.setSubject(subject[i]);
			detailRecord.setCourseHours(courseHours[i]);
			detailRecord.setTeacher(teacher[i]);
			list.add(detailRecord);
			System.out.println(detailRecord);
		}
		for(PromiseClassDetailRecord promise : list ){
			promiseClassDetailRecordDao.save(promise);
		}

	}
	
	/**
	 * 合同完结
	 * */
	@Override
	public void endPromiseClassContract(PromiseClassRecord record,String resultStatus){
		PromiseStudent promiseStudent = promiseStudentDao.getPromiseClassContractProId(record.getPromiseClass().getId(), record.getStudentId());
	    System.out.println(promiseStudent);
		ContractProduct contractProduct = new ContractProduct();
		if(promiseStudent != null){
			contractProduct = contractProductDao.findById(promiseStudent.getContractProduct().getId());
		}
		
		if(contractProduct.getPaidStatus()!=null && !contractProduct.getPaidStatus().equals(ContractProductPaidStatus.PAID)){
			throw new ApplicationException("没有支付完成的合同产品不能完结目标班！");
		}
		record.setClassYear("ended");
		record.setClassMonth("ended");
		String[] userInfo = this.getCurUserAndDate();
		record.setModifyUserId(userInfo[0]);
		record.setModifyTime(userInfo[1]);
		String currentDate = DateTools.getCurrentDate();
		String classDate = DateTools.getLastDayofMonth(Integer.parseInt(currentDate.substring(0, 4)), Integer.parseInt(currentDate.substring(5, 7)));
		record.setClassDate(classDate);
	
		//保存记录
		promiseClassRecordDao.save(record);
		ContractProductStatus endStatus=ContractProductStatus.ENDED;
	 if(contractProduct!=null && resultStatus!=null && "0".equals(resultStatus)){
		    List<AccountChargeRecords> chargeList = accountChargeRecordsDao.findAccountRecordsByContractProduct(contractProduct.getId(), "FALSE");
		    for (AccountChargeRecords acr : chargeList) {
				if(!acr.getProductType().equals(ProductType.ECS_CLASS)){//小班扣0记录不回滚
					continue;
				}
		    	 MoneyWashRecords mwr = new MoneyWashRecords();
				    mwr.setTransactionId(acr.getTransactionId());
				    mwr.setDetailReason("目标班完结处理标记为失败");
				    chargeService.washCharge(mwr);
				    
			}
		    ContractProductHandler prodHandler = contractService.selectHandlerByType(contractProduct.getType());
		    try {
		    	contractProductDao.flush();
		    	prodHandler.transferAmountToElectronicAdd(BigDecimal.ZERO, BigDecimal.ZERO, contractProduct,contractProduct.getRemainingAmountOfBasicAmount(), null);
		    	prodHandler.completeContractProduct(contractProduct.getRemainingAmountOfBasicAmount(), contractProduct.getPromotionAmount(), contractProduct.getPromotionAmount(), contractProduct, contractProduct.getRemainingAmountOfBasicAmount());
		    	contractProductDao.flush();
		    	StudnetAccMv studnetAccMv = contractService.getStudentAccoutInfo(record.getStudentId());
				studnetAccMvDao.save(studnetAccMv);
				studnetAccMvDao.flush();
				endStatus=ContractProductStatus.CLOSE_PRODUCT;
			} catch (Exception e) {
				throw new ApplicationException("目标班完结处理失败了");
			}
	    }else{
		//调用扣费接口，产生一条扣费记录
		if(contractProduct != null){
			new PromiseClassConProdHandler().chargePromiseClassProduct(contractProduct.getContract(), contractProduct, 
					record.getChargeAmount(), record.getChargeHours(), record);
		}
		
	 	
	    }    
	 	StringBuilder hql = new StringBuilder();
	    hql.append(" from PromiseStudent  where student.id = :studentId and promiseClass.id = :promiseClassId ");
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("studentId", record.getStudentId());
	    params.put("promiseClassId", record.getPromiseClass().getId());
	    List<PromiseStudent> list = promiseStudentDao.findAllByHQL(hql.toString(), params);
	    PromiseStudent student = list.get(0);
	    student.setCourseStatus("0");
	    student.getContractProduct().setStatus(endStatus);
	    student.setResultStatus(resultStatus);
		promiseStudentDao.save(student);
	}

	/**
	 * 对目标班进行结课处理
	 * */
	@Override
	public void endPromiseClass(String promiseClassId) {
		//获取成功率
		double rate = promiseClassDao.countStudentSuccessRate(promiseClassId);
		String successRate=null;
		if(rate == 100.0){
			successRate = String.format("%.0f",rate);
		}else{
			successRate = String.format("%.1f",rate);
		}
		
		//结课
		promiseClassDao.endPromiseClass(promiseClassId, successRate);
		
	}
	
	/**
	 * 合同完结时判断是否还有未扣款的月份的学生
	 * */
	public String getStudentRecordIsInProgress(String studentId,String contractProductId){
		ContractProduct cp = contractProductDao.findById(contractProductId);
		if(cp!=null && cp.getProduct()!=null && cp.getProduct().getProductVersion()!=null){
			String year =cp.getProduct().getProductVersion().getName();
			Integer y=Integer.valueOf(year);
			if(y>2017){
				return "3";
			}
		}

		return promiseClassRecordDao.getStudentRecordIsInProgress(studentId);
	}
	

	/**
	 * 根据目标班ID和学生ID查询月结记录信息
	 * */
	public List<PromiseClassRecordVo> findPrmoseClassRecordByProClaIdAndStuId(String promiseClassId,String studentId){
		return promiseClassRecordDao.getPromiseClassRecordByStudentIdAndPrmoseClassId(promiseClassId, studentId);
	}
	
	@Override
	public List<PromiseClassRecordVo> findPromiseClassRecordByConProIdAndStuId(
			String contractProductId, String studentId) {
		return promiseClassRecordDao.findPromiseClassRecordByConProIdAndStuId(contractProductId, studentId);
	}

	/**
	 * 根据目标班ID和学生ID删除所有月结记录信息
	 * */
	public void deletePrmoseClassRecordByProClaIdAndStuId(String promiseClassId,String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" delete from PromiseClassRecord where promiseClass.id = :promiseClassId ");
		hql.append(" and studentId = :studentId ");
		params.put("promiseClassId", promiseClassId);
		params.put("studentId", studentId);
		promiseClassRecordDao.excuteHql(hql.toString(), params);
	}
	
	/**
	 * 班主任学生管理界面点详情按钮，查出该学生的合同信息
	 * */
	@Override
	public List<PromiseClassApplyVo> findStudentContractInfo(String studentId,String contractProductId){
		return promiseStudentDao.findStudentContractInfo(studentId,  contractProductId);
	}
	
	/**
	 * 查询学生月结详细信息
	 * */
	@Override
	public List<PromiseClassDetailRecordVo>  findStudentMonthlyDetailInfo(PromiseClassRecord promiseClassRecord){
		return promiseClassDetailRecordDao.findStudentMonthlyDetailInfo(promiseClassRecord);
	}

	
	/**
	 * 获取当前登录用户ID和当前系统时间
	 * */
	public String[] getCurUserAndDate(){
		//获取当前登录用户ID
		String userId = userService.getCurrentLoginUser().getUserId();
		//获取当前系统时间设置创建时间
		Long cur = System.currentTimeMillis();
		Date date = new Date(cur);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String curDate = sdf.format(date);
		return new String[]{userId,curDate};
	}


	/**
	 * 查询目标班学生基本信息
	 * */
	@Override
	public List<PromiseClassApplyVo> getPromiseStudentInfo(PromiseStudent promiseStudent) {
		return promiseStudentDao.getPromiseStudentInfo(promiseStudent);
	}
	
	
	@Override
	public void promiseClassAutoCount(Integer startDate, Integer month,String countYearMonth,String lastDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" from PromiseClassRecord where classYear = :startDate and classMonth = :month and classDate = :lastDate ");
		hql.append(" and chargeAmount is not null and id not in (select promiseClassRecord.id from AccountChargeRecords where promiseClassRecord.id is not null)");
		params.put("startDate", startDate.toString());
		params.put("month", month.toString());
		params.put("lastDate", lastDate.toString());
		List<PromiseClassRecord> pcrList = promiseClassRecordDao.findAllByHQL(hql.toString(), params);
		log.info(pcrList.size()+"_开始时间："+DateTools.getCurrentDateTime());
		for (PromiseClassRecord pcr : pcrList) {
			if(pcr.getContractProductId()==null) continue;
			ContractProduct cp=contractProductDao.findById(pcr.getContractProductId());
			if(cp!=null && cp.getStatus().equals(ContractProductStatus.NORMAL)){// #更新合同产品课消金额以及课时
				
				if(cp.getIsFrozen()!=null && cp.getIsFrozen()==0){//冻结不扣费
					log.info("合同产品"+cp.getId()+"冻结，跳过！");
					continue;
				}
				
				//如果资金不够扣了，就拉到吧。
				if(cp.getPaidStatus().equals(ContractProductPaidStatus.PAYING) && cp.getRemainingAmount().compareTo(pcr.getChargeAmount())<0){
					log.info("合同产品"+cp.getId()+"金额不够，跳过！");
					continue;
				}
				
				Organization  blCampus= new Organization();
				
				List<PromiseStudent> ps = promiseStudentDao.getPromiseStudentByContractPro(cp.getId());
				if(ps!=null && ps.size()>0){//如果有报目标班，那么就用目标班的校区，如果没有就用学生所属校区
				   PromiseStudent pstu = ps.get(0);
				   if(pstu.getPromiseClass()!=null){//如果有目标班，就是目标班的学校
					   blCampus= pstu.getPromiseClass().getpSchool();
				   }else{//没有，扣费校区就是学生的学校
					   Student s=studentDao.findById(pcr.getStudentId());
						blCampus=s.getBlCampus();
				   }
				}else{//没有目标班就是学生的学校
					Student s=studentDao.findById(pcr.getStudentId());
					blCampus=s.getBlCampus();
				}
				log.info("合同产品"+cp.getId()+"执行扣费！"+DateTools.getCurrentDateTime());
				//合同产品扣费
				pcr.setChargeCampus(blCampus);//设置月结校区
				new PromiseClassConProdHandler().chargePromiseClassProductWithoutClass(cp.getContract(), cp, 
						pcr.getChargeAmount(), pcr.getChargeHours(), pcr,blCampus,lastDate);
			}
		}
		log.info("完成时间:"+DateTools.getCurrentDateTime());
		
	}
	
	/**
	 * 保存月结详情
	 * @param contractProductid
	 * @param re
	 */
	public void savePromiseDetailRecord(String contractProductid,PromiseClassRecord re,String countDate){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sbu=new StringBuilder();
		sbu.append(" select mcc.TEACHER_ID,mc.SUBJECT subjectId,sum(mcc.COURSE_HOURS) COURSE_HOURS  from ");
		sbu.append(" mini_class mc left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID");
		sbu.append(" left join mini_class_course mcc on mcc.MINI_CLASS_ID=mc.MINI_CLASS_ID");
		sbu.append(" left join mini_class_student_attendent  mcsa on mcsa.MINI_CLASS_COURSE_ID=mcc.MINI_CLASS_COURSE_ID  and mcsa.STUDENT_ID=mcs.STUDENT_ID");
		sbu.append(" where mcc.COURSE_DATE like :countDate AND  mcsa.CHARGE_STATUS='CHARGED' and mcs.CONTRACT_PRODUCT_ID = :contractProductid ");
		sbu.append(" group by mcc.teacher_id,mc.SUBJECT");
		params.put("countDate", countDate + "%");
		params.put("contractProductid", contractProductid);
		List list = promiseClassDao.findMapBySql(sbu.toString(), params);
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			PromiseClassDetailRecord dre=new PromiseClassDetailRecord();
			dre.setCreateTime(DateTools.getCurrentDateTime());
			dre.setPromiseClassRecord(re);
			dre.setClassType("1");
			dre.setCourseHours(StringUtil.toString(map.get("COURSE_HOURS")));
			dre.setSubject(StringUtil.toString(map.get("subjectId")));
			dre.setTeacher(StringUtil.toString(map.get("TEACHER_ID")));
			promiseClassDetailRecordDao.save(dre);
		}
		}
	
	/* (non-Javadoc)
	 * @see com.eduboss.service.PromiseClassService#getCanTurnToClassOrganization(java.lang.String)
	 */
	@Override
	public List<OrganizationVo> getCanTurnToClassOrganization(
			String promiseStudentId) {
		PromiseStudent promiseStudent = promiseStudentDao.findById(promiseStudentId);
		String hql =" from Organization where parentId='"+promiseStudent.getPromiseClass().getpSchool().getParentId()+"' and orgType='CAMPUS'";
		return HibernateUtils.voListMapping(organizationDao.findAllByHQL(hql, new HashMap<String, Object>()),OrganizationVo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.eduboss.service.PromiseClassService#getPromiseClassByCampus(java.lang.String)
	 */
	@Override
	public List<PromiseClassVo> getPromiseClassByCampus(String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from PromiseClass where pSchool.id = :campusId and pStatus<>'0'";
		params.put("campusId", campusId);
		return  HibernateUtils.voListMapping(promiseClassDao.findAllByHQL(hql, params),PromiseClassVo.class);
	}
	
	
	/* (non-Javadoc)
	 * @see com.eduboss.service.PromiseClassService#turnPromiseClass(java.lang.String, java.lang.String)
	 */
	@Override
	public Response turnPromiseClass(String promiseStudentId,
			String turnPromiseClassId,String newCourseDate) {
		Response res=new Response();
		PromiseStudent promiseStudent = promiseStudentDao.findById(promiseStudentId);
		cancelPromiseClassApply(promiseStudent.getStudent().getId(), promiseStudent.getPromiseClass().getId(),promiseStudent.getContractProduct().getId());
		
		//转班
		PromiseStudent newPromiseStudent=new PromiseStudent();
		newPromiseStudent.setContractProduct(promiseStudent.getContractProduct());
		newPromiseStudent.setStudent(promiseStudent.getStudent());
		newPromiseStudent.setCourseStatus("1");
		PromiseClass promiseClass=new PromiseClass();
		promiseClass.setId(turnPromiseClassId);
		newPromiseStudent.setPromiseClass(promiseClass);
		newPromiseStudent.setCourseDate(newCourseDate);
		promiseStudentDao.updateOrSavePromiseStudent(newPromiseStudent);
		promiseClassSubjectDao.updateNewPromiseStudentId(newPromiseStudent.getId(),promiseStudentId);
		return res;
	}

	@Override
	public Response savePromiseSubject(TreeSet<PromiseClassSubjectVo> subjectVos,String promiseStudentId) {
		User user=userService.getCurrentLoginUser();
		String userId=null;
		if(user!=null)userId=user.getUserId();
		Set<Integer> ids = new HashSet<>();
		for(PromiseClassSubjectVo sub:subjectVos){
			if(sub.getMiniClass()!=null && StringUtil.isNotBlank(sub.getMiniClass().getMiniClassId())){
                ids.add(sub.getId());
				savePromiseToMiniClass(sub);
			}else if(sub.getId()!=0){
                ids.add(sub.getId());
				PromiseClassSubject domain = promiseClassSubjectDao.findById(sub.getId());
				if(domain.getMiniClass()!=null && StringUtils.isNotBlank(domain.getMiniClass().getMiniClassId())){
					smallClassService.deleteStudentInMiniClasss(domain.getPromiseStudent().getStudent().getId(),domain.getMiniClass().getMiniClassId());
                    smallClassService.updateMiniClassRecruitStatus(domain.getMiniClass());
					domain.setMiniClass(null);
				}
				PromiseStudent ps = new PromiseStudent();
				ps.setId(sub.getPromiseStudent().getId());
				domain.setCourseHours(sub.getCourseHours());
				domain.setQuarterId(new DataDict(sub.getQuarterId()));
				domain.setSubject(new DataDict(sub.getSubjectId()));
				domain.setPromiseStudent(ps);
				domain.setModifyTime(DateTools.getCurrentDateTime());
				domain.setModifyUserId(userId);
				promiseClassSubjectDao.save(domain);
			}else {
				PromiseClassSubject domain = HibernateUtils.voObjectMapping(sub, PromiseClassSubject.class);
				domain.setModifyTime(DateTools.getCurrentDateTime());
				domain.setModifyUserId(userId);
				domain.setConsumeCourseHours(BigDecimal.ZERO);
				if (sub.getId() == 0) {
					domain.setCreateTime(DateTools.getCurrentDateTime());
					domain.setCreateUserId(userId);
				}
				promiseClassSubjectDao.save(domain);
				ids.add(domain.getId());
			}
		}

        deleteStudentSubject(promiseStudentId,ids);


		return new Response();
	}

	public void deleteStudentSubject(String promiseStudentId,Set<Integer> ids){
	    Map<String,Object> map = new HashMap<>();
        map.put("promiseStudentId",promiseStudentId);
	    StringBuilder hql = new StringBuilder();
	    hql.append(" from PromiseClassSubject where promiseStudent.id =:promiseStudentId");

	    for (Integer id:ids){
	        hql.append(" and id <>'"+id+"'");
        }

        List<PromiseClassSubject> list= promiseClassSubjectDao.findAllByHQL(hql.toString(),map);

	    for (PromiseClassSubject sub:list){
			if(sub.getMiniClass()!=null && StringUtils.isNotBlank(sub.getMiniClass().getMiniClassId())){
				smallClassService.deleteStudentInMiniClasss(sub.getPromiseStudent().getStudent().getId(),sub.getMiniClass().getMiniClassId());
				sub.setMiniClass(null);
			}
	        promiseClassSubjectDao.delete(sub);
        }


    }

	@Override
	public Response savePromiseToMiniClass(PromiseClassSubjectVo vo) {
		Response res=new Response();
		User user=userService.getCurrentLoginUser();
		String userId=null;
		if(user!=null)userId=user.getUserId();
		PromiseClassSubject promiseClassSubject= HibernateUtils.voObjectMapping(vo,PromiseClassSubject.class);
		if(vo.getId()!=0){
			PromiseClassSubject	domain=promiseClassSubjectDao.findById(vo.getId());
			if(domain.getMiniClass()!=null && !domain.getMiniClass().getMiniClassId().equals(promiseClassSubject.getMiniClass().getMiniClassId())){
				smallClassService.deleteStudentInMiniClasss(domain.getPromiseStudent().getStudent().getId(),domain.getMiniClass().getMiniClassId());
                smallClassService.updateMiniClassRecruitStatus(domain.getMiniClass());
            }

			if(domain.getMiniClass()==null || (domain.getMiniClass()!=null && !domain.getMiniClass().getMiniClassId().equals(promiseClassSubject.getMiniClass().getMiniClassId()))) {
				smallClassService.AddPromiseStudentForMiniClasss(domain.getPromiseStudent().getStudent(), promiseClassSubject.getMiniClass(), domain.getPromiseStudent().getContractProduct(), domain.getCourseHours().subtract(domain.getConsumeCourseHours()));
                smallClassService.updateMiniClassRecruitStatus(promiseClassSubject.getMiniClass());
			}
			domain.setMiniClass(promiseClassSubject.getMiniClass());
			domain.setModifyTime(DateTools.getCurrentDateTime());
			domain.setModifyUserId(userId);
		}else{
			res.setResultCode(-1);
			res.setResultMessage("分配科目ID不能为空");
		}
		return res;
	}

	public String getFirstSchoolTime(String miniClassId){
		List<MiniClassCourse> list = smallClassCourseDao.findAllUnChargeMiniClassCourse(miniClassId,null);
		return "";
	}

	@Override
	public DataPackage getPromiseSubjectList(DataPackage dp, String promiseStudentId) {
		Map<String,Object> paramMap=new HashMap<>();
		paramMap.put("promiseStudentId",promiseStudentId);
		dp= promiseClassSubjectDao.getPromiseSubjectList(dp,paramMap);
		List<PromiseClassSubjectVo> list=HibernateUtils.voListMapping((List)dp.getDatas(),PromiseClassSubjectVo.class);
		dp.setDatas(list);
		return dp;
	}

	@Override
	public DataPackage findMiniClassByPromiseSubjectId(DataPackage dp, Integer promiseSubjectId,String teacherId,String organizationId,String name) {
		Map<String,Object> paramMap=new HashMap<>();
		if(promiseSubjectId!=0){
			PromiseClassSubject pro=promiseClassSubjectDao.findById(promiseSubjectId);

			if(pro==null){
				throw new ApplicationException("科目数据已经在其他地方被改动，请刷新页面再重试。");
			}

			if(pro.getPromiseStudent()!=null && pro.getPromiseStudent().getPromiseClass()!=null && pro.getPromiseStudent().getPromiseClass().getpSchool()!=null)
			paramMap.put("organization",pro.getPromiseStudent().getPromiseClass().getpSchool().getId());
			if(pro.getSubject()!=null)
			paramMap.put("subject",pro.getSubject().getId());
			if(pro.getQuarterId()!=null)
			paramMap.put("quarter",pro.getQuarterId().getId());
			if(pro.getPromiseStudent()!=null && pro.getPromiseStudent().getPromiseClass()!=null && pro.getPromiseStudent().getPromiseClass().getGrade()!=null)
			paramMap.put("grade",pro.getPromiseStudent().getPromiseClass().getGrade());
			paramMap.put("hours",pro.getCourseHours().subtract(pro.getConsumeCourseHours()));


			if(pro.getMiniClass()!=null){
				paramMap.put("miniClassId",pro.getMiniClass().getMiniClassId());
			}

		}
		if(StringUtil.isNotBlank(organizationId)){
			paramMap.put("organization",organizationId);
		}
		if(StringUtil.isNotBlank(teacherId)){
			paramMap.put("teacherId",teacherId);
		}

		if(StringUtil.isNotBlank(name)){
			paramMap.put("name","%"+name+"%");
		}


		return smallClassService.findMiniClassByPromiseSubjectId(dp,paramMap);
	}

	@Override
	public Response deleteStudentFromClass(Integer id) {
		PromiseClassSubject	domain=promiseClassSubjectDao.findById(id);
		if(StringUtils.isNotBlank(domain.getMiniClass().getMiniClassId())){
			smallClassService.deleteStudentInMiniClasss(domain.getPromiseStudent().getStudent().getId(),domain.getMiniClass().getMiniClassId());
		}
		domain.setMiniClass(null);
		promiseClassSubjectDao.save(domain);
		return new Response();
	}

	@Override
	public PromiseStudentCustomerVo findStudentInfoByPromiseStudentId(String promiseStudentId) {
		PromiseStudent promiseStudent=promiseStudentDao.findById(promiseStudentId);
		return HibernateUtils.voObjectMapping(promiseStudent,PromiseStudentCustomerVo.class,"studentCustomer");
	}

	@Override
	public Response startAuditPromiseStudent(String promiseStudentId, String resultStatus) {
		PromiseStudent s =promiseStudentDao.findById(promiseStudentId);
		s.setResultStatus(resultStatus);
		PromiseClassEndAudit audit= new PromiseClassEndAudit();
		audit.setPromiseStudentId(promiseStudentId);
		audit.setAuditStatus(0);
		audit.setCreateTime(DateTools.getCurrentDateTime());
		audit.setCreateUserId(userService.getCurrentLoginUser().getUserId());
		promiseClassEndAuditDao.save(audit);
		return new Response();
	}

	@Override
	public Response confirmAuditPromiseStudent(int auditId, String resultStatus,String remark) {
		PromiseClassEndAudit audit=promiseClassEndAuditDao.findById(auditId);
		if(audit!=null){
			PromiseStudent student= promiseStudentDao.findById(audit.getPromiseStudentId());
			PromiseClassRecord record = new PromiseClassRecord();
			record.setPromiseClass(student.getPromiseClass());
			record.setStudentId(student.getStudent().getId());
			record.setContractProductId(student.getContractProduct().getId());
			record.setChargeAmount(student.getContractProduct().getRemainingAmount());
			record.setChargeHours(BigDecimal.ZERO);
			this.endPromiseClassContract(record,resultStatus);
			audit.setAuditStatus(1);
			audit.setModifyTime(DateTools.getCurrentDateTime());
			audit.setModifyUserId(userService.getCurrentLoginUser().getUserId());
			audit.setRemark(remark);
		}else{
			throw new ApplicationException("木有找到审批记录。");
		}
		return new Response();
	}

	@Override
	public Map<Object, Object> getEcsContractInfo(String contractProductId) {
		ContractProduct cp =contractProductDao.findById(contractProductId);
		ProductVo productVo=productService.findProductById(cp.getProduct().getId());
		//确定关联的VIP服务费ID
		String sonProductId ="";
		Map<String,String> sonProductMap = new HashMap<>();
		for(ProductVo p:productVo.getSonProduct()){
			sonProductMap.put(p.getId(),p.getId());
			sonProductId=p.getId();
		}

		if(sonProductMap.size()!=1){
			throw new ApplicationException("目标班产品关联的vip服务费产品超过1个或者没有关联vip服务费产品！");
		}
		//只获取关联目标班的产品数据。
		if(cp!=null && cp.getContract().getOtherProducts()!=null && StringUtils.isNotBlank(sonProductId)){
			//promiseClassSubjectDao.getEcsContractInfo(cp.getContract().getId())
				Map map = new HashMap();



				BigDecimal vipPadiAmount=BigDecimal.ZERO;
				for(ContractProduct conp :cp.getContract().getOtherProducts()){
					if(sonProductId.equals(conp.getProduct().getId()) && cp.getStatus().equals(conp.getStatus())){
						map.put("vipAmount",conp.getPlanAmount());
						map.put("vipProAmount",conp.getPromotionAmount());
						map.put("vipRealPayAmount",conp.getRealAmount());
						map.put("vipConsumeAmount",conp.getConsumeAmount());
						map.put("vipPaidAmount",conp.getPaidAmount());
						vipPadiAmount=conp.getPaidAmount();



						break;
					}
				}

				if(map.get("vipAmount")==null){
                    map.put("vipAmount",BigDecimal.ZERO);
                    map.put("vipProAmount",BigDecimal.ZERO);
                    map.put("vipRealPayAmount",BigDecimal.ZERO);
                    map.put("vipConsumeAmount",BigDecimal.ZERO);
                    map.put("vipPaidAmount",BigDecimal.ZERO);
                }


				map.put("realPaidAmount",cp.getPaidAmount().add(vipPadiAmount));//只算本产品跟对应的vip服务费金额

			if (isYearAfter2018(cp)){
				map.put("after2018", true);
			}else {
				map.put("after2018", false);
			}
			Contract contract = cp.getContract();
			if (contract.getPaidStatus()==ContractPaidStatus.PAID){
				map.put("paidStatus", "paid");//付款完成
			}else {
				map.put("paidStatus", "unPaid");//除了付款以外都是未付款
			}

			contractService.calculateContractDomain(contract);
			BigDecimal availableAmount = contract.getAvailableAmount();
			if (availableAmount.compareTo(BigDecimal.ZERO)>0){
				map.put("availableAmount", "unassigned");//合同还有未分配资金
			}else if (availableAmount.compareTo(BigDecimal.ZERO)==0){
				map.put("availableAmount", "assigned");//合同已经分配完资金
			}


			    map.put("coursePaidAmount",cp.getPaidAmount());
				map.put("courseAmount",cp.getPlanAmount());
				map.put("courseProAmount",cp.getPromotionAmount());
				map.put("courseName",cp.getProduct().getName());
				map.put("realPayAmount",cp.getRealAmount());
				map.put("consumeAmount",cp.getConsumeAmount());
				map.put("contractId",cp.getContract().getId());
				return map;
		}
		return null;
	}

	@Override
	public Map<Object,Object> getEcsContractChargeInfo(String contractProductId) {



		Map<Object,Object> returnMap= new HashMap<>();
		ContractProduct cp =contractProductDao.findById(contractProductId);

		if (isYearAfter2018(cp)){
			returnMap = getEcsContractChargeInfoAfter2018(cp, returnMap);
			return returnMap;
		}

		ProductVo productVo=productService.findProductById(cp.getProduct().getId());
		returnMap.put("realTotalAmount",cp.getRealAmount());
		//确定关联的VIP服务费ID
		String sonProductId ="";
		Map<String,String> sonProductMap = new HashMap<>();
		for(ProductVo p:productVo.getSonProduct()){
			sonProductMap.put(p.getId(),p.getId());
			sonProductId=p.getId();
		}

		//确定关联的VIP服务费
		BigDecimal realAmount=BigDecimal.ZERO;
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal promotionAmount = BigDecimal.ZERO;
		if(cp!=null && cp.getContract().getOtherProducts()!=null && sonProductMap.size()==1 && StringUtils.isNotBlank(sonProductId)){
			returnMap.put("result",true);
			for(ContractProduct conp :cp.getContract().getOtherProducts()){
				if(sonProductId.equals(conp.getProduct().getId())){
					realAmount=conp.getRealAmount();
					price= conp.getPrice();
					promotionAmount= conp.getPromotionAmount();
					returnMap.put("vipTotalAmount",conp.getRealAmount());
					break;
				}
			}
		}else{
			returnMap.put("result",false);
		}

		//扣费信息
		List<Map> list =promiseClassSubjectDao.getEcsContractChargeInfo(contractProductId);
		List<Map> listcp=new ArrayList<>();
		for (Map map :list){
			BigDecimal totalAmount = (BigDecimal) map.get("totalAmount");
			if(totalAmount.compareTo(BigDecimal.ZERO)>0){
				if(realAmount.compareTo(price)>=0){
					map.put("realVipAmount",price);
					map.put("proVipAmount",BigDecimal.ZERO);
					realAmount= realAmount.subtract(price);
				}else if(realAmount.compareTo(price)<0 && realAmount.compareTo(BigDecimal.ZERO)>0 && realAmount.add(promotionAmount).compareTo(price)>0){
					map.put("realVipAmount",realAmount);
					map.put("proVipAmount",price.subtract(realAmount));
					realAmount=BigDecimal.ZERO;
					promotionAmount=promotionAmount.subtract(price.subtract(realAmount));
				}else if(realAmount.compareTo(BigDecimal.ZERO)==0 && promotionAmount.compareTo(price)>0){
					map.put("realVipAmount",BigDecimal.ZERO);
					map.put("proVipAmount",price);
					promotionAmount= promotionAmount.subtract(price);
				}else{
					map.put("realVipAmount",BigDecimal.ZERO);
					map.put("proVipAmount",BigDecimal.ZERO);
				}
			}
			listcp.add(map);
		}

		returnMap.put("chargeInfo",listcp);
		return returnMap;
	}

	private Map<Object, Object> getEcsContractChargeInfoAfter2018(ContractProduct cp, Map<Object,Object> returnMap) {
		ProductVo productVo=productService.findProductById(cp.getProduct().getId());
		returnMap.put("realTotalAmount",cp.getRealAmount());
		//确定关联的VIP服务费ID
		String sonProductId ="";
		Map<String,String> sonProductMap = new HashMap<>();
		for(ProductVo p:productVo.getSonProduct()){
			sonProductMap.put(p.getId(),p.getId());
			sonProductId=p.getId();
		}

		//确定关联的VIP服务费
		BigDecimal realAmount=BigDecimal.ZERO;
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal promotionAmount = BigDecimal.ZERO;



		List<AccountChargeRecords> vipAccountChargeRecords = new ArrayList<>();

		if(cp!=null && cp.getContract().getOtherProducts()!=null && sonProductMap.size()==1 && StringUtils.isNotBlank(sonProductId)){
			returnMap.put("result",true);
			for(ContractProduct conp :cp.getContract().getOtherProducts()){
				if(sonProductId.equals(conp.getProduct().getId())){

					String contractProductId = conp.getId();
					vipAccountChargeRecords = getVipAccountChargeRecords(contractProductId);
					returnMap.put("vipTotalAmount",conp.getRealAmount());
					price = conp.getPrice();
					break;
				}
			}
		}else{
			returnMap.put("result",false);
		}
		BigDecimal priceCp = cp.getPrice();
		//扣费信息
		List<Map> list =promiseClassSubjectDao.getEcsContractChargeInfoAfter2018(cp.getId(), priceCp);
		List<Map> listcp=new ArrayList<>();
		for (Map map :list){
			BigDecimal totalAmount = (BigDecimal) map.get("totalAmount");
			if(totalAmount.compareTo(BigDecimal.ZERO)>0){


				if (vipAccountChargeRecords.size()>0){
					AccountChargeRecords realRecords = null;
					AccountChargeRecords promotionRecords = null;

					BigDecimal amount = BigDecimal.ZERO;

					for (AccountChargeRecords a : vipAccountChargeRecords){
						if (realRecords == null && a.getPayType() == PayType.REAL){
							realRecords = a;
							amount = amount.add(a.getAmount());
						}
						if (promotionRecords == null && a.getPayType() == PayType.PROMOTION && amount.add(a.getAmount()).compareTo(price) == 0){
							promotionRecords = a;
						}
					}


					map.put("realVipAmount", realRecords == null? BigDecimal.ZERO:realRecords.getAmount());
					map.put("proVipAmount",promotionRecords == null?BigDecimal.ZERO:promotionRecords.getAmount());

					if (realRecords!=null){
						vipAccountChargeRecords.remove(realRecords);
					}
					if (promotionRecords!=null){
						vipAccountChargeRecords.remove(promotionRecords);
					}

				}else {
					map.put("realVipAmount",BigDecimal.ZERO);
					map.put("proVipAmount",BigDecimal.ZERO);
				}

//				if(realAmount.compareTo(price)>=0){
//					map.put("realVipAmount",price);
//					map.put("proVipAmount",BigDecimal.ZERO);
//					realAmount= realAmount.subtract(price);
//				}else if(realAmount.compareTo(price)<0 && realAmount.compareTo(BigDecimal.ZERO)>0 && realAmount.add(promotionAmount).compareTo(price)>0){
//					map.put("realVipAmount",realAmount);
//					map.put("proVipAmount",price.subtract(realAmount));
//					realAmount=BigDecimal.ZERO;
//					promotionAmount=promotionAmount.subtract(price.subtract(realAmount));
//				}else if(realAmount.compareTo(BigDecimal.ZERO)==0 && promotionAmount.compareTo(price)>0){
//					map.put("realVipAmount",BigDecimal.ZERO);
//					map.put("proVipAmount",price);
//					promotionAmount= promotionAmount.subtract(price);
//				}else{
//					map.put("realVipAmount",BigDecimal.ZERO);
//					map.put("proVipAmount",BigDecimal.ZERO);
//				}
			}
			listcp.add(map);
		}

		returnMap.put("chargeInfo",listcp);
		return returnMap;
	}

	private List<AccountChargeRecords> getVipAccountChargeRecords(String contractProductId) {
		StringBuffer sql = new StringBuffer();
		sql.append("    SELECT                                                    ");
		sql.append("    		*                                                 ");
		sql.append("    		FROM                                              ");
		sql.append("    account_charge_records                                    ");
		sql.append("    		WHERE                                             ");
		sql.append("    CONTRACT_PRODUCT_ID = :contractProductId    AND CHARGE_PAY_TYPE='CHARGE' AND IS_WASHED='FALSE'              ");
		Map<String, Object> params = new HashMap<>();
		params.put("contractProductId", contractProductId);
		return accountChargeRecordsDao.findBySql(sql.toString(), params);
	}

	@Override
	public DataPackage getEcsContractChargeList(String contractProductId,PromiseChargeSearchDto dto,DataPackage dataPackage ) {
		return promiseClassSubjectDao.getEcsContractChargeList(contractProductId,dto,dataPackage);
	}


	/**
	 * 判断是否是2019年及以后的目标班合同产品
	 *
	 * @param targetConPrd
	 * @return
	 */
	@Override
	public boolean isYearAfter2018(ContractProduct targetConPrd) {
		Product product = targetConPrd.getProduct();
		if (product!=null&&product.getCategory() == ProductType.ECS_CLASS){
			DataDict productVersion = product.getProductVersion();
			if (productVersion!=null){
				String name = productVersion.getName();
				Integer integer = Integer.valueOf(name);
				if (integer>2018 &&targetConPrd.getType().equals(ProductType.ECS_CLASS) && targetConPrd.getProduct().getCourseSeries()!=null){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Response savaPromiseReturnAuditInfo(PromiseStudent promiseStudent) {
		Response res=new Response();
		User user=userService.getCurrentLoginUser();
		PromiseStudent stu=promiseStudentDao.findById(promiseStudent.getId());
		if(stu!=null){
			if(promiseStudent.getCourseStatus()==null || promiseStudent.getAuditStatus()==null){
                log.error(promiseStudent.getId()+" 传入参数有问题哟 AuditStatus："+ promiseStudent.getAuditStatus()+"，CourseStatus："+ promiseStudent.getCourseStatus());
			    throw new ApplicationException("传入参数有误");
            }

			if(user!=null)
			stu.setModifyUserId(user.getUserId());
			if( promiseStudent.getCourseStatus().equals("2") && promiseStudent.getAuditStatus().equals(PromiseAuditStatus.UNAUDIT)) {
				if (isYearAfter2018(stu.getContractProduct())){
					//有可分配资金，不可申请中途退费，提示去分配资金
					Contract contract = stu.getContractProduct().getContract();
					contractService.calculateContractDomain(contract);
					BigDecimal availableAmount = contract.getAvailableAmount();
					if (availableAmount.compareTo(BigDecimal.ZERO)>0){
						throw new ApplicationException("合同有可分配资金，不可申请中途退费，请去分配资金");
					}

				}else {
					if(!stu.getContractProduct().getPaidStatus().equals(ContractProductPaidStatus.PAID)){
						throw new ApplicationException("学生合同尚未完款，不能进行中途退费，请进行合同缩单或者完款！");
					}
				}

				stu.getContractProduct().setIsFrozen(0);
				if(StringUtils.isNotBlank(stu.getAuditRemark())){
				    stu.setOldAuditRemark(stu.getAuditRemark());
                }
			}else if ( promiseStudent.getCourseStatus().equals("3") && promiseStudent.getAuditStatus().equals(PromiseAuditStatus.RAUDIT)){
				returnPromiseClassFee(stu.getContractProduct(),promiseStudent.getReturnType());
				deleteReturnStudentAttendent(stu.getId(),stu.getStudent().getId());
			}else if ( promiseStudent.getCourseStatus().equals("1") && promiseStudent.getAuditStatus().equals(PromiseAuditStatus.AUDIT_FAILED)){
                stu.getContractProduct().setIsFrozen(1);
            }else{
				log.error(promiseStudent.getId()+" 传入异常类型 "+ promiseStudent.getAuditStatus());
				res.setResultCode(-1);
				res.setResultMessage("暂不支持");
			}
            stu.setAuditStatus(promiseStudent.getAuditStatus());
            stu.setAuditRemark(promiseStudent.getAuditRemark());
            stu.setReturnType(promiseStudent.getReturnType());
            stu.setModifyTime(DateTools.getCurrentDateTime());
            stu.setCourseStatus(promiseStudent.getCourseStatus());
			promiseStudentDao.save(stu);
		}else{
			res.setResultCode(-1);
			res.setResultMessage("找不到对应的精英班学生，请刷新列表");
		}
		return res;
	}

	/**
	 * 删除该合同产品报读所有小班的未考勤记录，但是不退班
	 * @param promiseStudentId
	 * @param studentId
	 */
	public void deleteReturnStudentAttendent(String promiseStudentId,String studentId){
		Map<String,Object> map = new HashMap<>();
		map.put("promiseStudentId",promiseStudentId);
		StringBuilder hql = new StringBuilder();
		hql.append(" from PromiseClassSubject where promiseStudent.id =:promiseStudentId");
		List<PromiseClassSubject> list= promiseClassSubjectDao.findAllByHQL(hql.toString(),map);
		for (PromiseClassSubject sub:list){
			if(sub.getMiniClass()!=null && StringUtils.isNotBlank(sub.getMiniClass().getMiniClassId())){
				String miniClassId = sub.getMiniClass().getMiniClassId();
				if(getCanDeleteStudent(studentId,miniClassId)) {
					//全部退掉
					smallClassService.deleteStudentInMiniClasss(studentId, miniClassId);
					//清除小班
					sub.setMiniClass(null);
				}else {
					miniClassStudentAttendentDao.deleteMiniClassStudentAttendentByStudentAndMiniClass(studentId, miniClassId);
				}
			}
		}
	}


	/**
	 * 判读是否有未考勤的记录，没有的话就可以退班。
	 * @param studentId
	 * @param miniClassId
	 * @return
	 */
	public Boolean getCanDeleteStudent(String studentId,String miniClassId){
		List<MiniClassStudentAttendent> list =miniClassStudentAttendentDao.getStudentMiniClassAttendentList(miniClassId,studentId);
		if(list.size()==0){
			return true;
		}
		for(MiniClassStudentAttendent att:list){
			if(att.getChargeStatus().equals(MiniClassStudentChargeStatus.UNCHARGE)){
				return true;
			}
		}
		return false;
	}



	public void returnPromiseClassFee(ContractProduct cp,PromiseReturnType type) {
		if (isYearAfter2018(cp)){
			returnPromiseClassFeeAfter2018(cp, type);
			return;
		}
		cp.setIsFrozen(1);
		chargeVipFee(cp,type);
		returnFeeFinally(cp,type);
	}

	private void returnPromiseClassFeeAfter2018(ContractProduct cp, PromiseReturnType type) {
		cp.setIsFrozen(1);
		/**
		 * 补扣目标班课程
		 */
		chargeZeroAmount(cp);
		returnVIPFee(cp, type);
		returnFeeFinally(cp,type);

		//完成合同状态
		finishedContractStatus(cp);

	}

	/**
	 * 完成合同状态
	 * @param cp
	 */
	private void finishedContractStatus(ContractProduct cp) {
		Contract contract = cp.getContract();
		int statusNum=0;
		for (ContractProduct conPrd : contract.getContractProducts()){
			if (conPrd.getStatus() == ContractProductStatus.ENDED || conPrd.getStatus() == ContractProductStatus.CLOSE_PRODUCT){
				statusNum+=1;
			}
		}
		if (statusNum == contract.getContractProducts().size()){
			contract.setContractStatus(ContractStatus.FINISHED);
		}
		contractDao.save(contract);
	}

	private void returnVIPFee(ContractProduct cp, PromiseReturnType type) {
		ProductVo productVo=productService.findProductById(cp.getProduct().getId());
		//确定关联的VIP服务费ID
		String sonProductId ="";
		Map<String,String> sonProductMap = new HashMap<>();
		for(ProductVo p:productVo.getSonProduct()){
			sonProductMap.put(p.getId(),p.getId());
			sonProductId=p.getId();
		}
		if (cp!=null&&cp.getContract().getOtherProducts()!=null&&sonProductMap.size()==1 && StringUtils.isNotBlank(sonProductId)){
			for (ContractProduct conp :cp.getContract().getOtherProducts()){
				if (sonProductId.equals(conp.getProduct().getId())&& (conp.getStatus() == ContractProductStatus.NORMAL ||conp.getStatus() == ContractProductStatus.STARTED )){
					if(conp.getPaidAmount().subtract(conp.getConsumeAmount()).compareTo(BigDecimal.ZERO)>=0){
						returnFeeFinally(conp,type);
					}
					break;
				}
			}
		}else {
			throw new ApplicationException("关联vip服务费不正确");
		}


	}

	private void chargeZeroAmount(ContractProduct cp) {
		List<AccountChargeRecords> accountChargeRecords = accountChargeRecordsDao.getAccountChargeRecords(cp);

		BigDecimal consumeAmount = BigDecimal.ZERO;
		BigDecimal consumeHours = BigDecimal.ZERO;
		BigDecimal realAmount = BigDecimal.ZERO;
		BigDecimal realHours = BigDecimal.ZERO;

		BigDecimal promotionAmount = BigDecimal.ZERO;
		BigDecimal promotionHours = BigDecimal.ZERO;

		MiniClass miniClass = null;

		BigDecimal price = cp.getPrice();
		BigDecimal dealDiscount = cp.getDealDiscount();
		price = price.multiply(dealDiscount);

		for (AccountChargeRecords record : accountChargeRecords){
			BigDecimal amount = price.multiply(record.getQuality());
			consumeAmount = consumeAmount.add(amount);
			consumeHours=consumeHours.add(record.getQuality());
			if (record.getPayType()==PayType.REAL){
				realAmount = realAmount.add(amount);
//				realHours = realHours.add(record.getQuality());
			}else if (record.getPayType()==PayType.PROMOTION){
				promotionAmount = promotionAmount.add(amount);
//				promotionHours = promotionHours.add(record.getQuality());
			}
			miniClass = record.getMiniClass();
		}
		if (miniClass==null){
			return;
		}

		Organization belongCampus= miniClass.getBlCampus();

		String transactionId = UUID.randomUUID().toString();

		MiniClassConProdHandler prodHandler = (MiniClassConProdHandler) contractService.selectHandlerByType(ProductType.SMALL_CLASS);
		if (realAmount.compareTo(BigDecimal.ZERO)>0){
			prodHandler.saveAccountChargeRecord(realAmount, cp, cp.getType(), null, realHours,
					ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, miniClass, null, null, null, null);
		}
		if (promotionAmount.compareTo(BigDecimal.ZERO)>0){
			prodHandler.saveAccountChargeRecord(promotionAmount, cp, cp.getType(), null, promotionHours,
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, miniClass, null, null, null, null);
		}

		prodHandler.doAfterForNewCharge(consumeAmount, realAmount, promotionAmount, consumeHours , cp );

	}

	public void returnFeeFinally(ContractProduct cp,PromiseReturnType type){
		PromiseClassConProdHandler handler = new PromiseClassConProdHandler();
		if (type.equals(PromiseReturnType.RETURN_TO_ACCOUNT)) {
			handler.midReturnFee(cp);//退给电子账户
		} else if (type.equals(PromiseReturnType.RETURN_TO_CUSTOMER)) {
			handler.midReturnFeeToCustomer(cp,new ArrayList<IncomeDistributionVo>());
		} else {
			throw new ApplicationException("未选择退费类型？");
		}
	}

	public void chargeVipFee( ContractProduct cp,PromiseReturnType type){
		//扣除的季节数
		List<Map> list=promiseClassSubjectDao.getEcsContractChargeInfo(cp.getId());
		BigDecimal i =BigDecimal.ZERO;
		for(Map map:list){
			if(map.get("totalAmount")!=null){
				BigDecimal totalAmount= (BigDecimal) map.get("totalAmount");
				if(totalAmount.compareTo(BigDecimal.ZERO)>0){
					i=i.add(BigDecimal.ONE);
				}
			}
		}

		ProductVo productVo=productService.findProductById(cp.getProduct().getId());
		//确定关联的VIP服务费ID
		String sonProductId ="";
		Map<String,String> sonProductMap = new HashMap<>();
		for(ProductVo p:productVo.getSonProduct()){
			sonProductMap.put(p.getId(),p.getId());
			sonProductId=p.getId();
		}

		//确定关联的VIP服务费   && cp.getContract().getOtherProducts().size()==1   兼容其他资料费不在这里扣。只扣关联的vip服务费。
		if(cp!=null && cp.getContract().getOtherProducts()!=null && sonProductMap.size()==1 && StringUtils.isNotBlank(sonProductId)){
			for(ContractProduct conp :cp.getContract().getOtherProducts()){
				if(sonProductId.equals(conp.getProduct().getId()) && (conp.getStatus() == ContractProductStatus.NORMAL ||conp.getStatus() == ContractProductStatus.STARTED ) ){
					if(conp.getQuantity().compareTo(i)>=0 && conp.getProduct()!=null && conp.getProduct().getId().equals(sonProductId)){
						OtherConProdHandler handler=new OtherConProdHandler();
						handler.chargeEcsOtherProduct(conp,i);
						if(conp.getQuantity().compareTo(BigDecimal.ZERO)>0){
							returnFeeFinally(conp,type);
						}
						break;
					}
				}
			}
		}else{
			throw new ApplicationException("关联vip服务费不正确");
		}
	}

}
