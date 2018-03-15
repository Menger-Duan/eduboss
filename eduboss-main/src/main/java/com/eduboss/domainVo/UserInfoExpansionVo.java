package com.eduboss.domainVo;

import java.util.List;

import com.eduboss.domain.ContractSigning;
import com.eduboss.domain.EducationExperience;
import com.eduboss.domain.FamilyMenber;
import com.eduboss.domain.TrainingExperience;
import com.eduboss.domain.WorkExperience;

public class UserInfoExpansionVo {

	private List<WorkExperience> workExperiences;
	private List<EducationExperience> educationExperiences;
	private List<FamilyMenber> familyMenbers;
	private List<TrainingExperience> trainingExperiences;
	private List<PersonnelTransferVo> personnelTransfers;
	private List<ContractSigning> contractSignings;
	private List<TeacherSubjectVo> teacherSubjectVos;
	
	public List<WorkExperience> getWorkExperiences() {
		return workExperiences;
	}
	public void setWorkExperiences(List<WorkExperience> workExperiences) {
		this.workExperiences = workExperiences;
	}
	public List<EducationExperience> getEducationExperiences() {
		return educationExperiences;
	}
	public void setEducationExperiences(
			List<EducationExperience> educationExperiences) {
		this.educationExperiences = educationExperiences;
	}
	public List<FamilyMenber> getFamilyMenbers() {
		return familyMenbers;
	}
	public void setFamilyMenbers(List<FamilyMenber> familyMenbers) {
		this.familyMenbers = familyMenbers;
	}
	public List<TrainingExperience> getTrainingExperiences() {
		return trainingExperiences;
	}
	public void setTrainingExperiences(List<TrainingExperience> trainingExperiences) {
		this.trainingExperiences = trainingExperiences;
	}
	public List<PersonnelTransferVo> getPersonnelTransfers() {
		return personnelTransfers;
	}
	public void setPersonnelTransfers(List<PersonnelTransferVo> personnelTransfers) {
		this.personnelTransfers = personnelTransfers;
	}
	public List<ContractSigning> getContractSignings() {
		return contractSignings;
	}
	public void setContractSignings(List<ContractSigning> contractSignings) {
		this.contractSignings = contractSignings;
	}
	public List<TeacherSubjectVo> getTeacherSubjectVos() {
		return teacherSubjectVos;
	}
	public void setTeacherSubjectVos(List<TeacherSubjectVo> teacherSubjectVos) {
		this.teacherSubjectVos = teacherSubjectVos;
	}
	
}
