package com.eduboss.domainVo;


public class CusFollowStuVo {
	
	
	private String id;
	private String name;
	private String contact;
	private String school;
	private String stuSchoolName;
	private String gradeDict;
	private String fatherName;
	private String fatherPhone;
	private String motherName;
	private String notherPhone;
	private String customerId;
	private String createTime;
	private String createUser;
	private String modifyTime;
	private String modifyUser;
	
	private String classes;
	private String bothday;
	private String sex;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getStuSchoolName() {
		return stuSchoolName;
	}
	public void setStuSchoolName(String stuSchoolName) {
		this.stuSchoolName = stuSchoolName;
	}
	public String getGradeDict() {
		return gradeDict;
	}
	public void setGradeDict(String gradeDict) {
		this.gradeDict = gradeDict;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getFatherPhone() {
		return fatherPhone;
	}
	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getNotherPhone() {
		return notherPhone;
	}
	public void setNotherPhone(String notherPhone) {
		this.notherPhone = notherPhone;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getBothday() {
		return bothday;
	}
	public void setBothday(String bothday) {
		this.bothday = bothday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getStudentJson(){
		StringBuffer json=new StringBuffer();
         json.append("{\"name\":\""+this.name+"\",\"contact\":\""+this.contact+"\","
         		+ "\"stuSchoolName\":\""+this.stuSchoolName+"\",\"gradeDict\":\""+this.gradeDict+"\","
         		+ "\"fatherName\":\""+this.fatherName+"\",\"fatherPhone\":\""+this.fatherPhone+"\",\"motherName\":\""+this.motherName+"\""
         		+ ",\"classes\":\""+this.classes+"\""
         		+ ",\"bothday\":\""+this.bothday+"\""
         		+ ",\"sex\":\""+this.sex+"\""
         		+ ",\"notherPhone\":\""+this.notherPhone+"\"}");
		return json.toString();
	}
	

	
}
