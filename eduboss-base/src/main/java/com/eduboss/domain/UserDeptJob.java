package com.eduboss.domain;

import javax.persistence.*;

@Entity
@Table(name = "USER_DEPT_JOB")
public class UserDeptJob implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 2209798621637441937L;

	private int id;
	
	private int isMajorRole;

	private String objUid; //外部联系人

	private String userId;
	private String deptId;
	private String jobId;

	// Constructors

	/** default constructor */
	public UserDeptJob() {
	}

	public UserDeptJob(String userId, String deptId, String jobId,int isMajorRole) {
		this.userId = userId;
		this.deptId = deptId;
		this.jobId = jobId;
		this.isMajorRole=isMajorRole;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "isMajorRole")
	public int getIsMajorRole() {
		return this.isMajorRole;
	}

	public void setIsMajorRole(int isMajorRole) {
		this.isMajorRole = isMajorRole;
	}

	@Column(name = "OBJ_UID", length = 32)
	public String getObjUid() {
		return objUid;
	}

	public void setObjUid(String objUid) {
		this.objUid = objUid;
	}

	@Column(name = "USER_ID", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "DEPT_ID", nullable = false, length = 32)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column(name = "JOB_ID", nullable = false, length = 32)
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}