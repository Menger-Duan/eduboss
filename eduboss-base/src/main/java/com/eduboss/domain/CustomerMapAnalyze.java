package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "student_parent_info_01")
public class CustomerMapAnalyze implements java.io.Serializable{

	
	/**
	 * ID
	 * */
	private Integer id;
	
	/**
	 * 学校名称
	 * */
	private String schoolName;
	
	/**
	 * 学生名字
	 * */
	private String studentName;
	
	/**
	 * 班级名称
	 * */
	private String className;
	
	/**
	 * 年级
	 * */
	private Integer grade;
	
	/**
	 * 联系电话
	 * */
	private String contact;
	
	/**
	 * 父亲名称
	 * */
	private String father;
	
	/**
	 * 父亲联系电话
	 * */
	private String fatherContact;
	
	/**
	 * 父亲地址
	 * */
	private String fatherAddress;
	
	/**
	 * 母亲名称
	 * */
	private String mother;
	
	/**
	 * 母亲联系电话
	 * */
	private String motherContact;
	
	/**
	 * 母亲地址
	 * */
	private String motherAddress;
	
	/**
	 * 经度
	 * */
	private String longitude;
	
	/**
	 * 纬度
	 * */
	private String latitude;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SCHOOL_NAME")
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	@Column(name = "STUDENT_NAME")
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Column(name = "CLASS_NAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "GRADE")
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Column(name = "CONTACT")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "FATHER")
	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	@Column(name = "FATHER_CONTACT")
	public String getFatherContact() {
		return fatherContact;
	}

	public void setFatherContact(String fatherContact) {
		this.fatherContact = fatherContact;
	}

	@Column(name = "FATHER_ADDRESS")
	public String getFatherAddress() {
		return fatherAddress;
	}

	public void setFatherAddress(String fatherAddress) {
		this.fatherAddress = fatherAddress;
	}

	@Column(name = "MOTHER")
	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	@Column(name = "MOTHER_CONTACT")
	public String getMotherContact() {
		return motherContact;
	}

	public void setMotherContact(String motherContact) {
		this.motherContact = motherContact;
	}

	@Column(name = "MOTHER_ADDRESS")
	public String getMotherAddress() {
		return motherAddress;
	}

	public void setMotherAddress(String motherAddress) {
		this.motherAddress = motherAddress;
	}

	@Column(name = "longitude")
	public String getlongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE")
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "CustomerMapAnalyze [longitude=" + longitude + ", className="
				+ className + ", contact=" + contact + ", father=" + father
				+ ", fatherAddress=" + fatherAddress + ", fatherContact="
				+ fatherContact + ", id=" + id + ", latitude=" + latitude
				+ ", mother=" + mother + ", motherAddress=" + motherAddress
				+ ", motherContact=" + motherContact + ", schoolName="
				+ schoolName + ", studentName=" + studentName + "]";
	}
	
	
	
}
