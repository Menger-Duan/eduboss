package com.eduboss.domain;


import javax.persistence.*;

@Entity
@Table(name = "organization_modify_log")
public class OrganizationModifyLog implements java.io.Serializable {

	private Integer id ;
	private String organizationId;
	private String logInfo;
	private String createUser;
	private String createTime;

	private String createUserName;

	public OrganizationModifyLog() {
	}

	public OrganizationModifyLog(String organizationId, String logInfo, String createUser) {
		this.organizationId = organizationId;
		this.logInfo = logInfo;
		this.createUser = createUser;
	}

	@Id
	@Column(name = "id", unique = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "organization_id")
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name = "create_time",updatable = false,insertable = false)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	@Column(name = "log_info")
	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	@Column(name = "create_user")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	@Transient
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
}