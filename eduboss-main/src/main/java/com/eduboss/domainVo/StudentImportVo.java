package com.eduboss.domainVo;

//目前仅用于上传客户资源时，将学生信息导入到pointial_student表的json字段
public class StudentImportVo implements java.io.Serializable{

    private String name;
    private String contact;
    private String bothday;
    private String sex;
    private String fatherName;
    private String fatherPhone;
    private String motherName;
    private String notherPhone;
    private String classes;//班级
    private String stuId;
    private String schoolId;  //跟学生列表字段一致，其他地方有调用，先多一个重复的字段
    private String schoolName;
    private String gradeId;
    private String id;
    private String regionName;
    private String address; //学生地址
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


	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}



	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "StudentImportVo [name=" + name + ", contact=" + contact
				+ ", bothday=" + bothday + ", school=" + schoolId
				+ ", stuSchoolName=" + schoolName + ", gradeDict="
				+ gradeId + ", sex=" + sex + ", fatherName=" + fatherName
				+ ", fatherPhone=" + fatherPhone + ", motherName=" + motherName
				+ ", notherPhone=" + notherPhone + ", classes=" + classes +"]";
	}
	public StudentImportVo(String name, String contact, String bothday,
			String school, String stuSchoolName, String gradeDict, String sex,
			String fatherName, String fatherPhone, String motherName,
			String notherPhone) {
		this.name = name;
		this.contact = contact;
		this.bothday = bothday;
		this.schoolId = school;
		this.schoolName = stuSchoolName;
		this.gradeId = gradeDict;
		this.sex = sex;
		this.fatherName = fatherName;
		this.fatherPhone = fatherPhone;
		this.motherName = motherName;
		this.notherPhone = notherPhone;
		
	}
	
	public StudentImportVo(String name, String contact,String stuSchoolName, 
			String gradeDict, String school,String fatherName,
			String fatherPhone, String motherName, String notherPhone,String classes) {
		this.name = name;
		this.contact = contact;
		this.schoolId = school;
		this.schoolName = stuSchoolName;
		this.gradeId = gradeDict;
		this.fatherName = fatherName;
		this.fatherPhone = fatherPhone;
		this.motherName = motherName;
		this.notherPhone = notherPhone;
		this.classes=classes;
	}
	public StudentImportVo() {

	}
    


}
