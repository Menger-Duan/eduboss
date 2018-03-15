package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.StudentEventType;
import com.eduboss.dao.StudentDynamicStatusDao;
import com.eduboss.domain.Contract;
import com.eduboss.domain.Course;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.StudentComment;
import com.eduboss.domain.StudentDynamicStatus;
import com.eduboss.domain.StudentScore;
import com.eduboss.domainVo.CustomerDynamicStatusVo;
import com.eduboss.domainVo.StudentDynamicStatusVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.StudentDynamicStatusService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service("com.eduboss.service.StudentDynamicStatusService")
public class StudentDynamicStatusServiceImpl implements StudentDynamicStatusService {
	
	@Autowired
	private StudentDynamicStatusDao studentDynamicStatusDao;

	@Autowired
	private UserService userService; 
	
	@Override
	public DataPackage findStudentDynamicStatus(StudentDynamicStatus cds, DataPackage dp) {
		//如果有字段有值，用like进行条件查询
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(cds);
		criterionList.add(Expression.like("student.name", cds.getStudent().getId(), MatchMode.ANYWHERE));
		dp = studentDynamicStatusDao.findPageByCriteria(dp, 
				HibernateUtils.prepareOrder(dp, "occourTime", "desc"), 
				criterionList);
		
		List<CustomerDynamicStatusVo> voDatas = HibernateUtils.voListMapping((List<CustomerDynamicStatus>)dp.getDatas(), CustomerDynamicStatusVo.class);
		dp.setDatas(voDatas);
		
		return dp;
	}

	@Override
	public void createContractDynamicStatus(Contract contract,
			String contractStatus) {
		StudentDynamicStatus newStatus =  new StudentDynamicStatus();
		newStatus.setStudent(contract.getStudent());
		newStatus.setDynamicStatusType(StudentEventType.CONTRACT);
		newStatus.setOccourTime(DateTools.getCurrentDateTime());
		newStatus.setReferuser(userService.getCurrentLoginUser());
		if("CREATE_NEW_CONTRACT".equals(contractStatus)) {
			newStatus.setDescription(String.format("新签了 一份 总额是 %s 的合同", contract.getTotalAmount()));
		} else if("EDIT_CONTRACT".equals(contractStatus))  {
			newStatus.setDescription(String.format("修改了 一份 总额是 %s 的合同", contract.getTotalAmount()));
		}
		studentDynamicStatusDao.save(newStatus);
	}

	@Override
	public void createScoreDynamicStatus(StudentScore studentScoreManage,
			String scoreStatus) {
		// CREATE_NEW_SCORE
		this.studentDynamicStatusDao.flush();
		this.studentDynamicStatusDao.getHibernateTemplate().getSessionFactory().getCurrentSession().refresh(studentScoreManage);
		StudentDynamicStatus newStatus =  new StudentDynamicStatus();
		newStatus.setStudent(studentScoreManage.getStudent());
		newStatus.setDynamicStatusType(StudentEventType.SCORE);
		newStatus.setOccourTime(DateTools.getCurrentDateTime());
		newStatus.setReferuser(userService.getCurrentLoginUser());
		String desc =  String.format(" 一个成绩 : 考试时间  %s, 年级 %s, 考试类型 %s, 科目 %s, 成绩 %d, 班级名次 %s, 年纪名次 %s", 
				studentScoreManage.getTime(),  studentScoreManage.getGrade().getName(), studentScoreManage.getTypeExam().getName(),
				studentScoreManage.getSubject().getName() ,studentScoreManage.getScore().intValue()
				, studentScoreManage.getGradeRange(), studentScoreManage.getClassRange()
				 );
		if("CREATE_NEW_SCORE".equals(scoreStatus)) {
			newStatus.setDescription( String.format("%s %s", "新增加了", desc ));
		} else if("EDIT_SCORE".equals(scoreStatus))  {
			newStatus.setDescription( String.format("%s %s", "修改了", desc ));
		}else if("DELETE_SCORE".equals(scoreStatus))  {
			newStatus.setDescription( String.format("%s %s", "删除了", desc ));
		}
		studentDynamicStatusDao.save(newStatus);
		
	}

	@Override
	public void createCourseDynamicStatus(Course course, String courseStatus) {
		// TODO Auto-generated method stub
		StudentDynamicStatus newStatus =  new StudentDynamicStatus();
		newStatus.setStudent(course.getStudent());
		newStatus.setDynamicStatusType(StudentEventType.COURSE);
		newStatus.setOccourTime(DateTools.getCurrentDateTime());
		newStatus.setReferuser(userService.getCurrentLoginUser());
		if("TEACHER_ATTENDANCE".equals(courseStatus)) {
			newStatus.setDescription(String.format("%s 老师 已经考勤了  %s %s 的课程", course.getTeacher().getName(), course.getGrade().getName(), course.getSubject().getName()));
		} 
		studentDynamicStatusDao.save(newStatus);
	}

	
	@Override
	public void createCommentDynamicStatus(StudentComment comment) {
		// TODO Auto-generated method stub
		StudentDynamicStatus newStatus =  new StudentDynamicStatus();
		newStatus.setStudent(comment.getStudent());
		newStatus.setDynamicStatusType(StudentEventType.COMMENT);
		newStatus.setOccourTime(DateTools.getCurrentDateTime());
		newStatus.setReferuser(userService.getCurrentLoginUser());
		newStatus.setDescription(String.format("%s 老师 对  %s 学生 进行了一条评论: %s", comment.getCreateUser().getName(), comment.getStudent().getName(),
				comment.getComment()));
		studentDynamicStatusDao.save(newStatus);
	}
	
	@Override
	public List<StudentDynamicStatusVo> getStudentDynamicStatusByPage(
			String studentId, int pageNo, int pageSize,
			StudentEventType studentEventType) {
		StringBuffer hql = new StringBuffer();
		hql.append("from StudentDynamicStatus as status where 1 = 1" )
			.append(" and student.id = :studentId ")
			.append(" and dynamicStatusType = :studentEventType ");
		Map<String, Object> params = Maps.newHashMap();
		params.put("studentId",studentId);
		params.put("studentEventType", studentEventType);
		DataPackage dataPackage = new DataPackage();
		dataPackage.setPageNo(pageNo);
		dataPackage.setPageSize(pageSize);
		List<StudentDynamicStatus> statusList = (List<StudentDynamicStatus>)studentDynamicStatusDao.findPageByHQL(hql.toString(), dataPackage,true, params).getDatas();
		List<StudentDynamicStatusVo> statusVoList = HibernateUtils.voListMapping(statusList, StudentDynamicStatusVo.class); 
		return statusVoList;
	}
//	@Override
//	public DataPackage findCustomerDynamicStatusByCustomerId(String customerId, DataPackage dp) {
//		dp = customerDynamicStatusDao.findByCustomerId(customerId, dp);
//		List<CustomerDynamicStatusVo> voDatas = HibernateUtils.voListMapping((List<CustomerDynamicStatus>)dp.getDatas(), CustomerDynamicStatusVo.class);
//		dp.setDatas(voDatas);
//		return dp;
//	}
//
//	@Override
//	public void saveCustomerDynamicStatus(CustomerDynamicStatus cds) {
//		customerDynamicStatusDao.save(cds);
//	}
//	
//	@Override
//	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description, String referUrl) {
//		CustomerDynamicStatus cds = new CustomerDynamicStatus();
//		cds.setCustomer(new Customer(customerId));
//		cds.setDescription(description);
//		cds.setReferUrl(referUrl);
//		cds.setDynamicStatusType(dynamicStatusType);
//		cds.setOccourTime(DateTools.getCurrentDateTime());
//		cds.setReferuser(userService.getCurrentLoginUser());
//		customerDynamicStatusDao.save(cds);
//		
//		//发送浏览消息给本客户相关的用户
//		List<CustomerDynamicStatus> statuses = customerDynamicStatusDao.findByCriteria(Expression.eq("customer.id", customerId));
//		List<String> sendUseIds = new ArrayList<String>();
//		for (CustomerDynamicStatus status : statuses) {
//			if (status!=null && status.getReferuser()!=null && status.getReferuser().getUserId()!=null
//					&& !sendUseIds.contains(status.getReferuser().getUserId()) && !status.getReferuser().getUserId().equalsIgnoreCase(userService.getCurrentLoginUser().getUserId())) {
//				sendUseIds.add(status.getReferuser().getUserId());
//				
//				//发送
//				messageService.sendMessage(MessageType.SYSTEM_MSG, "客户"+status.getCustomer().getName()+"新动态:"+dynamicStatusType.getName(), description, MessageDeliverType.SINGLE, status.getReferuser().getUserId());
//			}
//		}
//	}
//
//	@Override
//	public void saveCustomerDynamicStatus(String customerId, CustomerEventType dynamicStatusType, String description) {
//		saveCustomerDynamicStatus( customerId,  dynamicStatusType,  description, ""); 
//	}

}
