package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentOrganizationDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentOrganization;
import com.eduboss.domain.User;
import com.eduboss.utils.StringUtil;

@Repository
public class StudentOrganizationDaoImpl extends GenericDaoImpl<StudentOrganization, String> implements StudentOrganizationDao {

	@Override
	public void delStudentOrganization(String studentId, String isMainOrganization) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" delete StudentOrganization where student.id = :studentId and isMainOrganization = :isMainOrganization ";
		params.put("studentId", studentId);
		params.put("isMainOrganization", isMainOrganization);
		super.excuteHql(hql, params);
	}
	
	
	public void saveStudentOrganization(Student student){
		StudentOrganization studentOrganization = null;
		List<StudentOrganization> list = findByCriteria(Expression.eq("student.id", student.getId()),Expression.eq("isMainOrganization", "1"));
		if(list!=null && list.size()>0)
			studentOrganization=list.get(0);
		if(studentOrganization == null)
			studentOrganization =new StudentOrganization();
		studentOrganization.setStudent(student);
		if(StringUtil.isNotBlank(student.getBlCampusId()))
			studentOrganization.setOrganization(new Organization(student.getBlCampusId()));
		if(StringUtil.isNotBlank(student.getStudyManegerId()))
			studentOrganization.setStudyManager(new User(student.getStudyManegerId()));
		studentOrganization.setIsMainOrganization("1");
		save(studentOrganization);
	}
	
	public User findStudentOrganizationByStudentAndOrganization(String studentId,String organizationId){
		List<StudentOrganization> list = findByCriteria(Expression.eq("student.id", studentId),Expression.eq("organization.id", organizationId));
		if(list!=null && list.size()>0){
			for (StudentOrganization studentOrganization: list) {
				if ("1".equals(studentOrganization.getIsMainOrganization())) {
					return studentOrganization.getStudyManager();
				}
			}
			return list.get(0).getStudyManager();
		}
		return null;
	}


	@Override
	public StudentOrganization findByStuIdandOrgId(String studentId, String organizationId) {
		List<StudentOrganization> list = findByCriteria(Expression.eq("student.id", studentId),Expression.eq("organization.id", organizationId));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}


	@Override
	public void delStudentOrganizationByStuIdandOrgId(String studentId, String organizationId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" delete StudentOrganization where student.id = :studentId and organization.id = :organizationId ";
		params.put("studentId", studentId);
		params.put("organizationId", organizationId);
		super.excuteHql(hql, params);
	}
}
