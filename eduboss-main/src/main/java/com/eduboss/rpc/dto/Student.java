package com.eduboss.rpc.dto;

public class Student {
	
	private String studentID;
	
	private String name;
	
	private String gender;
	
	private Label province;
	
	private Label city;
	
	private Label school;
	
	private Label grade;

	private String status;

	private String studentStatus;
	

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Label getProvince() {
		return province;
	}

	public void setProvince(Label province) {
		this.province = province;
	}

	public Label getCity() {
		return city;
	}

	public void setCity(Label city) {
		this.city = city;
	}

	public Label getSchool() {
		return school;
	}

	public void setSchool(Label school) {
		this.school = school;
	}

	public Label getGrade() {
		return grade;
	}

	public void setGrade(Label grade) {
		this.grade = grade;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStudentStatus() {
		if(studentStatus==null){
			return "1";
		}
		return studentStatus;
	}

	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}
}
