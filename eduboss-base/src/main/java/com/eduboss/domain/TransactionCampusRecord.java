package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * 学生转校记录
 * */


@Entity
@Table(name = "TRANSACTION_CAMPUS_RECORD")
public class TransactionCampusRecord implements java.io.Serializable{
	
	/**
	 * 转校记录ID
	 * */
	private String id;
	
	/**
	 * 学生ID
	 * */
	private Student student;
	
	/**
	 * 原校区 ID
	 * */
	private Organization resourceCampus;
	
	/**
	 * 新校区ID
	 * */
	private Organization newCampus;
	
	/**
	 * 创建者ID
	 * */
	private User user;
	
	/**
	 * 创建时间
	 * */
	private String createTime;

	//学管师
	private User studyManager;

	@Id
    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCE_CAMPUS_ID")
	public Organization getResourceCampus() {
		return resourceCampus;
	}

	public void setResourceCampus(Organization resourceCampus) {
		this.resourceCampus = resourceCampus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NEW_CAMPUS_ID")
	public Organization getNewCampus() {
		return newCampus;
	}

	public void setNewCampus(Organization newCampus) {
		this.newCampus = newCampus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_MANAGER_ID")
	public User getStudyManager() {
		return studyManager;
	}

	public void setStudyManager(User studyManager) {
		this.studyManager = studyManager;
	}
}
