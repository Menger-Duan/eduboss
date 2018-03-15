package com.eduboss.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * 系统公告管理
 * */

@Entity
@Table(name = "SYSTEM_NOTICE")
public class SystemNotice implements java.io.Serializable{

	private String id;
	
	private String title;
	
	private String content;
	
	private String isReading;
	
	private String filePath;
	
	private String createTime;
	
	private User createUser;
	
	private String modyfyTime;
	
	private User modifyUser;
	
	private List<Role> role = new ArrayList<Role>();
	
	private List<Organization> organization = new ArrayList<Organization>();
	
	private DataDict noticeType;
	
	private String realFileName;
	
	public SystemNotice(){
		
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "IS_READING")
	public String getIsReading() {
		return isReading;
	}

	public void setIsReading(String isReading) {
		this.isReading = isReading;
	}

	@Column(name = "FILE_PATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "MODIFY_TIME")
	public String getModyfyTime() {
		return modyfyTime;
	}

	public void setModyfyTime(String modyfyTime) {
		this.modyfyTime = modyfyTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER")
	public User getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "SYSTEM_NOTICE_ROLE", joinColumns = { @JoinColumn(name = "notice_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.LAZY)
	@JoinTable(name = "SYSTEM_NOTICE_ORG", joinColumns = { @JoinColumn(name = "notice_id") }, inverseJoinColumns = { @JoinColumn(name = "org_id") })
	public List<Organization> getOrganization() {
		return organization;
	}

	public void setOrganization(List<Organization> organization) {
		this.organization = organization;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NOTICE_TYPE")
	@NotFound(action=NotFoundAction.IGNORE)
	public DataDict getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(DataDict noticeType) {
		this.noticeType = noticeType;
	}

	@Column(name = "REAL_FILE_NAME")
	public String getRealFileName() {
		return realFileName;
	}

	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}
	
	
	
}
