package com.eduboss.dto;

/**
 * 这个是VOJO 从contract界面传过来的学员信息
 * @author robinzhang
 *
 */
public class StudentContractRequest {
	
	private String cus_name;
	private String contact;
	private String consultant;
	private String stu_name;
	private String grade;
	
	public StudentContractRequest(String cus_name, String contact,
			String consultant, String stu_name, String grade) {
		super();
		this.cus_name = cus_name;
		this.contact = contact;
		this.consultant = consultant;
		this.stu_name = stu_name;
		this.grade = grade;
	}
	
	public StudentContractRequest() {}
	
	public String getCus_name() {
		return cus_name;
	}
	public void setCus_name(String cus_name) {
		this.cus_name = cus_name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getConsultant() {
		return consultant;
	}
	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
	
}
