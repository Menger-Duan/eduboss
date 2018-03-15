package com.eduboss.dao;

import com.eduboss.domain.Student;
import com.eduboss.domain.StudentOrganization;
import com.eduboss.domain.User;

public interface StudentOrganizationDao extends GenericDAO<StudentOrganization, String>{

	public void delStudentOrganization(String studentId, String isMainOrganization);

	public void saveStudentOrganization(Student student);
	
	public User findStudentOrganizationByStudentAndOrganization(String studentId,String organizationId);
	
	public StudentOrganization findByStuIdandOrgId(String studentId,String organizationId);
	
	public void delStudentOrganizationByStuIdandOrgId(String studentId, String organizationId);
}
