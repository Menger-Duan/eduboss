package com.eduboss.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "change_userrole_record")
public class ChangeUserRoleRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;	
	private String userId;	
	private String userName;
	private String deptId;
	private String deptName;
	private String campusId;
	private String campusName;
	private String branchId;
	private String branchName;
	private String createTime;
	
	public ChangeUserRoleRecord(){
		
	}
	

	/**
	 * @param id
	 * @param userId
	 * @param userName
	 * @param deptId
	 * @param deptName
	 * @param campusId
	 * @param campusName
	 * @param branchId
	 * @param branchName
	 * @param createTime
	 */
	public ChangeUserRoleRecord(String id, String userId, String userName, String deptId, String deptName,
			String campusId, String campusName, String branchId, String branchName, String createTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.deptId = deptId;
		this.deptName = deptName;
		this.campusId = campusId;
		this.campusName = campusName;
		this.branchId = branchId;
		this.branchName = branchName;
		this.createTime = createTime;
	}


	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "user_id", length = 32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "user_name", length = 32)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "dept_id", length = 32)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column(name = "dept_name", length = 32)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "campus_id", length = 32)
	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	@Column(name = "campus_name", length = 32)
	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	@Column(name = "branch_id", length = 32)
	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@Column(name = "branch_name", length = 32)
	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Column(name = "create_time", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	

}
