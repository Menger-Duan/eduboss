package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.LectureClassAttendanceStatus;
import com.eduboss.common.LectureClassStatus;
import com.eduboss.common.LectureClassStudentChargeStatus;
import com.eduboss.common.ProductType;
import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.LectureClassDao;
import com.eduboss.dao.LectureClassStudentDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.LectureClass;
import com.eduboss.domain.LectureClassStudent;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domainVo.LectureClassStudentVo;
import com.eduboss.domainVo.LectureClassVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ContractService;
import com.eduboss.service.LectureClassService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service("LectureClassService")
public class LectureClassServiceImpl implements LectureClassService {
	
	
	@Autowired
	private LectureClassDao lectureClassDao;
	

	@Autowired
	private LectureClassStudentDao lectureClassStudentDao;
	

	@Autowired
	private UserService userService;
	

	@Autowired
	private StudentDao studentDao;
	

	@Autowired
	private ContractProductDao contractProductDao;
	

	@Autowired
	private ContractService contractService;
	

	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	@Autowired
	private AccountChargeRecordsDao accountchargeRecordsDao;

	@Override
	public DataPackage getLectureClassList(DataPackage dp,LectureClassVo vo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder();
		String sql=" select o.orgLevel from organization o,REF_USER_ORG ref where o.id =ref.BRANCH_ID and ref.USER_ID='"+userService.getCurrentLoginUser().getUserId()+"'";
		List<Map<Object,Object>> map = lectureClassDao.findMapBySql(sql, new HashMap<String, Object>());
		hql.append(" select lc.* from lecture_class lc ,product p ");
		hql.append(" where 1=1 and lc.product=p.id ");
		if(map.size()>0){
			Map<Object,Object> yoxi=map.get(0);
			hql.append(" and lc.BL_BRANCH_ID in(select id from organization where orgLevel like '"+yoxi.get("orgLevel")+"%')");
		}
		
		if(StringUtils.isNotBlank(vo.getLectureName())){
			hql.append(" and lc.lecture_name like :lectureName ");
			params.put("lectureName", "%" + vo.getLectureName() + "%");
		}
		
		if(StringUtils.isNotBlank(vo.getBlBranchId())){
			hql.append(" and lc.bl_branch_id = :blBranchId ");
			params.put("blBranchId", vo.getBlBranchId());
		}
		
		if(StringUtils.isNotBlank(vo.getGradeId())){
			hql.append(" and lc.grade = :gradeId ");
			params.put("gradeId", vo.getGradeId());
		}
		
		if(StringUtils.isNotBlank(vo.getSubjectId())){
			hql.append(" and lc.subject = :subjectId ");
			params.put("subjectId", vo.getSubjectId());
		}
		
		if(StringUtils.isNotBlank(vo.getStartDate())){
			hql.append(" and lc.start_date = :startDate ");
			params.put("startDate", vo.getStartDate());
		}
		if(StringUtils.isNotBlank(vo.getTeacher())){
			hql.append(" and lc.teachers like :teacher ");
			params.put("teacher", "%" + vo.getTeacher() + "%");
		}
		if(StringUtils.isNotBlank(vo.getRecruitStudentStatusId())){
			hql.append(" and lc.RECRUIT_STUDENT_STATUS = :recruitStudentStatusId ");
			params.put("recruitStudentStatusId", vo.getRecruitStudentStatusId());
		}
		
		if(vo.getLectureStatus()!=null){
			hql.append(" and lc.lecture_status = :lectureStatus ");
			params.put("lectureStatus", vo.getLectureStatus());
		}
		if(StringUtils.isNotBlank(vo.getClassTypeId())){
			hql.append(" and p.CLASS_TYPE_ID = :classTypeId ");
			params.put("classTypeId", vo.getClassTypeId());
		}
		
		dp = lectureClassDao.findPageBySql(hql.toString(), dp, true, params);
		dp.setDatas(HibernateUtils.voListMapping((List<LectureClass>)dp.getDatas(), LectureClassVo.class));
		return dp;
	}
	
	
	@Override
	public Response saveOrUpdateLectureClass(String oper,LectureClassVo lectureClassVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		Response res= new Response();
		LectureClass lectureClass=HibernateUtils.voObjectMapping(lectureClassVo, LectureClass.class);
		String sql ="select count(1) from lecture_class where lecture_Name = :lectureName and bl_branch_id= :blBranchId ";
		params.put("lectureName", lectureClassVo.getLectureName());
		params.put("blBranchId", lectureClassVo.getBlBranchId());
		if(StringUtils.isNotBlank(lectureClass.getLectureId())){
			sql+=" and lecture_id <>'"+lectureClass.getLectureId()+"'";
			if(lectureClassDao.findCountSql(sql, params)>0){
				res.setResultCode(-1);
				res.setResultMessage("讲座名称不能跟同分公司的其他讲座重名");
				return res;
			}
			lectureClass.setModifyTime(DateTools.getCurrentTime());
			lectureClass.setModifyUser(userService.getCurrentLoginUser().getUserId());
			lectureClassDao.save(lectureClass);
		}else{
			if(lectureClassDao.findCountSql(sql, params)>0){
				res.setResultCode(-1);
				res.setResultMessage("讲座名称不能跟同分公司的其他讲座重名");
				return res;
			}
			lectureClass.setCreateTime(DateTools.getCurrentTime());
			lectureClass.setCreateUser(userService.getCurrentLoginUser().getUserId());
			lectureClass.setLectureStatus(LectureClassStatus.PENDDING_START);
			lectureClassDao.save(lectureClass);
		}
		return res;
	}
	
	
	@Override
	public LectureClassVo findLectureClassById(String lectureClassId) {
		LectureClass lecture = lectureClassDao.findById(lectureClassId);
		return HibernateUtils.voObjectMapping(lecture, LectureClassVo.class);
	}
	
	@Override
	public Response delteLectureClass(String lectureClassId) {
		Response res=new Response();
		try{
			//如果已有扣费学生
			if(lectureClassStudentDao.findNumByLecutureClassId(lectureClassId)>0){
				res.setResultCode(-1);
				res.setResultMessage("已有学生产生扣费，不能删除！");
				return res;
			}
			List<LectureClassStudent> lectureClassList = this.getLectureClassStudentByLectureClassId(lectureClassId);
			for (LectureClassStudent lectureClassStudent : lectureClassList) {
				int count = accountchargeRecordsDao.countAccountChargeRecordsByLecStuId(lectureClassStudent.getId());
				if (count > 0) {
					throw new ApplicationException("此讲座发生过扣费或者扣费冲销，不允许删除。");
				}
			}
			lectureClassStudentDao.deleteInfoByLectureId(lectureClassId);//删除学生
			LectureClass lecture = lectureClassDao.findById(lectureClassId);
			lectureClassDao.delete(lecture);//删除讲座
			
		}catch(Exception e){
			res.setResultCode(-1);
			res.setResultMessage("删除出错咯，如果再次尝试不成功请联系管理员！");
		}
		return res;
	}
	
	@Override
	public List<LectureClassStudent> getLectureClassStudentByLectureClassId(String lectureClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" from LectureClassStudent where lectureClass.lectureId = :lectureClassId order by createTime desc");
		params.put("lectureClassId", lectureClassId);
		return lectureClassStudentDao.findAllByHQL(hql.toString(), params);
	}
	
	
	@Override
	public Response addStudentToLectureClass(LectureClassStudentVo vo) {
		Response res=new Response();
		String [] stuArr={};
		String [] cpArr={};
		if(StringUtils.isNotBlank(vo.getStudentId()) && StringUtils.isNotBlank(vo.getContractProductId())){
			stuArr=vo.getStudentId().split(",");
			cpArr=vo.getContractProductId().split(",");
		}
		for (int i = 0; i < cpArr.length; i++) {
			if(getCountByStudentAndLecture(stuArr[i],vo.getLectureId())>0){
				res.setResultCode(-1);
				res.setResultMessage("该学生已在本讲座报读，请先退班处理再报进班或者先报合同产品，再进去讲座报名调整关联产品！");
			}
			LectureClassStudent student=new LectureClassStudent();
			if(vo.getAuditHours()!=null && vo.getAuditHours().compareTo(BigDecimal.ZERO)>0){
				student.setAuditHours(vo.getAuditHours());
			}else{
				student.setAuditHours(BigDecimal.ONE);
			}
			student.setLectureClass(new LectureClass(vo.getLectureId()));
			student.setStudent(new Student(stuArr[i]));
			student.setContractProduct(new ContractProduct(cpArr[i]));
			student.setCreateTime(DateTools.getCurrentTime());
			student.setCreateUser(userService.getCurrentLoginUser().getUserId());
			student.setAuditStatus(LectureClassAttendanceStatus.NEW);
			student.setChargeStatus(LectureClassStudentChargeStatus.UNCHARGE);
			student.setHasTeacherAttendance(BaseStatus.FALSE);
			lectureClassStudentDao.save(student);
		}
		
		return res;
	}
	
	
	@Override
	public int getCountByStudentAndLecture(String studentId, String lectureId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from LectureClassStudent where student.id = :studentId and lectureClass.lectureId = :lectureId ";
		params.put("studentId", studentId);
		params.put("lectureId", lectureId);
		return lectureClassStudentDao.findAllByHQL(hql, params).size();
	}
	
	@Override
	public int getCountByLecture(String lectureId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="select Count(1) from lecture_class_Student where lecture_id = :lectureId ";
		params.put("lectureId", lectureId);
		return lectureClassStudentDao.findCountSql(sql, params);
	}
	
	
	@Override
	public DataPackage getLectureClassStudentList(DataPackage dp,
			LectureClassStudentVo vo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder();
		hql.append("from LectureClassStudent where 1=1 ");
		if(StringUtils.isNotBlank(vo.getLectureId())){
			hql.append(" and lectureClass.lectureId = :lectureId ");
			params.put("lectureId", vo.getLectureId());
		}
		
		if(StringUtils.isNotBlank(vo.getStudentName())){
			hql.append(" and student.name like :studentName ");
			params.put("studentName", "%" + vo.getStudentName() + "%");
		}
		
		if(StringUtils.isNotBlank(vo.getSblBranchId())){
			hql.append(" and lectureClass.blBranch.id = :sblBranchId ");
			params.put("sblBranchId", vo.getSblBranchId());
		}
		
		if(vo.getChargeStatus()!=null){
			hql.append(" and chargeStatus = :chargeStatus ");
			params.put("chargeStatus", vo.getChargeStatus());
		}
		
		if(vo.getAuditStatus()!=null){
			hql.append(" and auditStatus = :auditStatus ");
			params.put("auditStatus", vo.getAuditStatus());
		}
		
		if(vo.getHasTeacherAttendance()!=null){
			hql.append(" and hasTeacherAttendance = :hasTeacherAttendance ");
			params.put("hasTeacherAttendance", vo.getHasTeacherAttendance());
		}
		
		if(StringUtils.isNotBlank(vo.getBlSchoolId())){
			hql.append(" and student.blCampusId = :blSchoolId ");
			params.put("blSchoolId", vo.getBlSchoolId());
		}
		

		hql.append(roleQLConfigService.getAppendSqlByAllOrg("讲座学生列表","hql","student.blCampusId"));
		
		dp = lectureClassStudentDao.findPageByHQL(hql.toString(), dp, true, params);
		
		List<LectureClassStudentVo> volist=HibernateUtils.voListMapping((List<LectureClassStudent>)dp.getDatas(), LectureClassStudentVo.class);
		for (LectureClassStudentVo voo : volist) {
			Organization o=organizationDao.findById(voo.getBlSchoolId());
			voo.setBlSchoolName(o.getName());
		}
		dp.setDatas(volist);
		return dp;
	}
	
	@Override
	public Response deleteStudentFromLectureClass(String lectureClassStudentId) {
			Response res=new Response();
			try{
//				int count = accountchargeRecordsDao.countAccountChargeRecordsByLecStuId(lectureClassStudentId);
//				if (count > 0) {
//					throw new ApplicationException("此讲座发生过扣费或者扣费冲销，不允许退班。");
//				}
				LectureClassStudent student = lectureClassStudentDao.findById(lectureClassStudentId);
				if(student.getAuditStatus().equals(LectureClassAttendanceStatus.CONPELETE)
					|| student.getChargeStatus().equals(LectureClassStudentChargeStatus.CHARGED)){
					res.setResultCode(-1);
					res.setResultMessage("学生已考勤完毕不能退班！");
				}else{
					lectureClassStudentDao.delete(student);
				}
			}catch(Exception e){
				res.setResultCode(-1);
				res.setResultMessage("学生退班出错咯，如果再次尝试不成功请联系管理员！");
			}
			return res;
	}
	
	@Override
	public List<StudentVo> getStudentWantListByLectureClassId(String lectureClassId) {
		LectureClass le = lectureClassDao.findById(lectureClassId);
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.*,c.id contractId,ifnull(cp.ID,'') as ID1,cp.PAID_AMOUNT-cp.CONSUME_AMOUNT paidAmount,"
						+ "((cp.PAID_AMOUNT-cp.CONSUME_AMOUNT)/(PRICE*DEAL_DISCOUNT)) quantity "
						+ "from student s "
						+ "INNER JOIN contract c on s.id = c.STUDENT_ID "
						+ "INNER JOIN contract_product cp on c.id = cp.CONTRACT_ID  ");
		// 已经报读了这个产品
		sql.append(" where cp.PRODUCT_ID ='"+ le.getProduct().getId()+"' and cp.type ='LECTURE'");
		// 且没有报读到其他 实际小班的
//		sql.append("and not exists ( select 1 from lecture_class_student where  lecture_class_student.CONTRACT_PRODUCT_ID = cp.ID  and  LECTURE_ID <> '").append(lectureClassId).append("')");
		// 还没有报读到该小班的
		sql.append(" and s.id not in(");
		sql.append(" select STUDENT_ID from lecture_class_student where LECTURE_ID   = '").append(lectureClassId).append("' )");
//		sql.append(" and s.id in (select STUDENT_ID from STUDENT_ORGANIZATION where ORGANIZATION_ID= '").append(userService.getBelongCampus().getId()).append("' ) ");
		//排除合同产品结课的
		sql.append(" and cp.STATUS <> '").append(ContractProductStatus.CLOSE_PRODUCT).append("' ");
		
//		List<Organization> orgList = userService.getCurrentLoginUser().getOrganization();
//		
//		if(orgList.size()>0){
//			sql.append(" and (s.bl_campus_id in(select id from Organization where orgLevel like '"+orgList.get(0).getOrgLevel()+"%'");
//			for (int i=1;i<orgList.size();i++) {
//				sql.append(" or orgLevel like '"+orgList.get(i).getOrgLevel()+"%'");
//			}
//			sql.append(") or s.STUDY_MANEGER_ID='"+userService.getCurrentLoginUser().getUserId()+"')");
//		}
		
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("讲座学生列表","sql","s.bl_campus_id"));
		
		// ID1 是合同产品ID。
		List<Object[]> list= studentDao.getCurrentSession().createSQLQuery(sql.toString()).addEntity("s", Student.class).addScalar("contractId").addScalar("ID1").addScalar("paidAmount").addScalar("quantity").list();
		List<Student> students = new ArrayList<Student>();
		for (int i = 0; i < list.size(); i++) {
			students.add((Student) list.get(i)[0]);
		}
		List<StudentVo> vos = HibernateUtils.voListMapping(students, StudentVo.class);
		for (int i = 0; i < vos.size(); i++) {
			vos.get(i).setContractId(list.get(i)[1].toString());
			vos.get(i).setContractProductId(list.get(i)[2].toString());
			// 剩余资金 和 剩余课时 的显示
			if(!"".equals(list.get(i)[2].toString())){
				ContractProduct conProd = contractProductDao.findById(list.get(i)[2].toString());
				vos.get(i).setProductId(conProd.getProduct().getId());
				vos.get(i).setProductName(conProd.getProduct().getName());
				vos.get(i).setRemainingAmount(conProd.getRemainingAmount()==null? BigDecimal.ZERO :conProd.getRemainingAmount());
				vos.get(i).setQuantity((conProd.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 || conProd.getPrice().compareTo(BigDecimal.ZERO) == 0 ||conProd.getDealDiscount().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : conProd.getRemainingAmount().divide(conProd.getPrice().multiply(conProd.getDealDiscount()),2));
			}
		}
		return vos;
	}
	
	@Override
	public List<StudentVo> getLectureClasssStudentByClassId(
			String classId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" from LectureClassStudent where lectureClass.lectureId= :classId order by createTime desc");
		params.put("classId", classId);
		List<LectureClassStudent> LectureClassList =lectureClassStudentDao.findAllByHQL(hql.toString(), params);
		List<StudentVo> stuList=new ArrayList<StudentVo>();
		for(LectureClassStudent lecStu : LectureClassList){
			if(lecStu.getStudent()!=null){
				StudentVo vo=HibernateUtils.voObjectMapping(lecStu.getStudent(), StudentVo.class);
				if (null != lecStu.getContractProduct()) {
					vo.setProductName(lecStu.getContractProduct().getProduct().getName());
				}
				if(lecStu.getAuditHours()!=null){
					vo.setQuantity(lecStu.getAuditHours());
				}else{
					vo.setQuantity(BigDecimal.ONE);
				}
				stuList.add(vo);
			}
		}
		return stuList;
	}
	
	
	@Override
	public Response removeStudentFromLecture(String lectureId, String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String[] stuArr=studentId.split(",");
		Response res=new Response();
		StringBuilder hql=new StringBuilder();
		hql.append(" from LectureClassStudent where lectureClass.lectureId= :lectureId ");
		params.put("lectureId", lectureId);
		if(stuArr.length>1){
			hql.append(" and student.id in(:studentIds)");
			params.put("studentIds", stuArr);
		}else{
			hql.append(" and student.id = :studentId ");
			params.put("studentId", studentId);
		}
		
		hql.append("  order by createTime desc");
		
		
		List<LectureClassStudent> list =lectureClassStudentDao.findAllByHQL(hql.toString(), params);
		if(list.size()>0){
			for (LectureClassStudent deldomain : list) {
				int count = accountchargeRecordsDao.countAccountChargeRecordsByLecStuId(deldomain.getId());
				if (count > 0) {
					throw new ApplicationException("此讲座发生过扣费或者扣费冲销，不允许退班。");
				}
				if(deldomain.getChargeStatus().equals(LectureClassStudentChargeStatus.CHARGED)){
					res.setResultCode(-1);
					res.setResultMessage(deldomain.getStudent().getName()+"已扣费完毕不能退班！");
				}else{
					lectureClassStudentDao.delete(deldomain);
				}
			}
		}else{
			res.setResultCode(-1);
			res.setResultMessage("未找到该学生，请刷新后重试。");
		}
		return res;
	}
	
	
	@Override
	public List<Map<Object, Object>> getStudentWantListByBranchAndType(
			String lectureClassId, String productType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String [] pro={};
		if(StringUtils.isNotBlank(productType)){
			pro=productType.split(",");
		}
		LectureClass le = lectureClassDao.findById(lectureClassId);
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.ID as studentId,s.name as studentName,o.name as campusName,concat(o.id,'') as campusId,concat(cp.id,'') as contractProductId,cp.type,cp.PRODUCT_ID as productId ,p.name as productName"
						+ ",(case when cp.paid_status='PAID' THEN(cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT) ELSE (cp.PAID_AMOUNT-cp.CONSUME_AMOUNT) END) paidAmount, "
						+ "round(case when cp.paid_status='PAID' THEN(cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT) ELSE (cp.PAID_AMOUNT-cp.CONSUME_AMOUNT) END/(cp.PRICE),2) quantity  ");
		
		sql.append(" FROM student s");
		sql.append(" LEFT JOIN contract c ON c.student_id = s.id");
		sql.append(" LEFT JOIN contract_product cp ON cp.contract_id = c.id");
		sql.append(" left join product p on cp.product_id=p.ID");
		sql.append(" LEFT JOIN organization o ON s.BL_CAMPUS_ID = o.id");
		// 已经报读了这个产品
		sql.append(" where o.parentId='"+ le.getBlBranch().getId()+"'  AND cp.type <> 'OTHERS' and cp.type <>'LECTURE' AND cp.status = 'NORMAL' ");
		// 还没有报读到该小班的
		sql.append(" and s.id not in(");
		sql.append(" select STUDENT_ID from lecture_class_student where LECTURE_ID   = :lectureClassId )");
		params.put("lectureClassId", lectureClassId);
		sql.append(" AND (CASE WHEN cp.paid_status = 'PAID' THEN (cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT) ELSE (cp.PAID_AMOUNT - cp.CONSUME_AMOUNT) END) > 0 ");
		
		if(pro.length>0){
			sql.append(" and cp.type in(:proTypes)");
			params.put("proTypes", pro);
		}
		
		//当前登录人的组织架构中
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("讲座学生列表","sql","s.bl_campus_id"));
		sql.append("order by o.id,s.name desc");
		return lectureClassDao.findMapBySql(sql.toString(), params);
	}
	
	@Override
	public Response auditLectureClassStudent(String ids,
			LectureClassAttendanceStatus auditStatus) {
		Response res=new Response();
		String[] lecIds=ids.split(",");
		User user=userService.getCurrentLoginUser();
		String nowDate=DateTools.getCurrentDateTime();
		for (int i = 0; i < lecIds.length; i++) {
			LectureClassStudent stu = lectureClassStudentDao.findById(lecIds[i]);
			if(stu!=null){
				if(stu.getChargeStatus().equals(LectureClassStudentChargeStatus.CHARGED)){
					res.setResultCode(-1);
					res.setResultMessage(stu.getStudent().getName()+"已扣费不能再考勤！");
					return res;
				}
				stu.setAuditStatus(auditStatus);
				stu.setAuditTime(nowDate);
				stu.setHasTeacherAttendance(BaseStatus.TRUE);
				stu.setTeacher(user);
				stu.setModifyTime(nowDate);
				stu.setModifyUser(user.getUserId());
				lectureClassStudentDao.save(stu);
			}
		}
		return res;
	}
	
	
	@Override
	public Response chargeLectureClassStudent(String ids) {
		Response res=new Response();
		String[] lecIds=ids.split(",");
		User user=userService.getCurrentLoginUser();
		String nowDate=DateTools.getCurrentDateTime();
		for (int i = 0; i < lecIds.length; i++) {
			LectureClassStudent stu = lectureClassStudentDao.findById(lecIds[i]);
			if(stu!=null){
				if(stu.getHasTeacherAttendance().equals(BaseStatus.FALSE)){
					res.setResultCode(-1);
					res.setResultMessage(stu.getStudent().getName()+"未考勤不能扣费！");
					return res;
				}
				
				if(stu.getChargeStatus().equals(LectureClassStudentChargeStatus.CHARGED)){
					res.setResultCode(-1);
					res.setResultMessage(stu.getStudent().getName()+"已扣费不能再扣费！");
					return res;
				}
				
				ContractProductHandler handler = contractService.selectHandlerByType(ProductType.LECTURE);
				handler.chargeLectureClass(stu);
				
				stu.setChargeStatus(LectureClassStudentChargeStatus.CHARGED);
				stu.setChargeUser(user);
				stu.setChargeTime(nowDate);
				stu.setModifyTime(nowDate);
				stu.setModifyUser(user.getUserId());
				lectureClassStudentDao.save(stu);
			}
		}
		return res;
	}
	
	
	/* 根据合同产品删除报名记录
	 * (non-Javadoc)
	 * @see com.eduboss.service.LectureClassService#deleteStudentByContractProduct(java.lang.String)
	 */
	@Override
	public void deleteStudentByContractProduct(
			String contractProductId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder();
		hql.append("delete from LectureClassStudent where contractProduct.id = :contractProductId ");
		params.put("contractProductId", contractProductId);
		lectureClassStudentDao.excuteHql(hql.toString(), params);
	}
	
}
